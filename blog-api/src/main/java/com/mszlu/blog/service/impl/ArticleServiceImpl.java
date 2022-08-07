package com.mszlu.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mszlu.blog.dao.dos.Archives;
import com.mszlu.blog.dao.mapper.ArticleBodyMapper;
import com.mszlu.blog.dao.mapper.ArticleMapper;
import com.mszlu.blog.dao.mapper.ArticleTagMapper;
import com.mszlu.blog.dao.pojo.Article;
import com.mszlu.blog.dao.pojo.ArticleBody;
import com.mszlu.blog.dao.pojo.ArticleTag;
import com.mszlu.blog.dao.pojo.SysUser;
import com.mszlu.blog.service.*;
import com.mszlu.blog.utils.UserThreadLocal;
import com.mszlu.blog.vo.ArticleBodyVo;
import com.mszlu.blog.vo.ArticleVo;
import com.mszlu.blog.vo.Result;
import com.mszlu.blog.vo.TagVo;
import com.mszlu.blog.vo.params.ArticleParam;
import com.mszlu.blog.vo.params.PageParams;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private TagsService tagsService;
    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ThreadServices threadServices;
    @Autowired
    private ArticleTagMapper articleTagMapper;


    /**
     * 1.分页查询article分页列表
     *
     * @param pageParams
     * @return
     */
    @Override
    public Result listArticle(PageParams pageParams) {
        /**
         * 分页查询article数据表
         */
        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        if (pageParams.getCategoryId() != null) {
            queryWrapper.eq(Article::getCategoryId, pageParams.getCategoryId());
        }
        List<Long> articleIdList = new ArrayList();
        if (pageParams.getTagId() != null) {
            LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
            articleTagLambdaQueryWrapper.eq(ArticleTag::getTagId, pageParams.getTagId());
            List<ArticleTag> articleTagList = articleTagMapper.selectList(articleTagLambdaQueryWrapper);
            for (ArticleTag articleTag : articleTagList) {
                articleIdList.add(articleTag.getArticleId());
            }
        }
        if (articleIdList.size() > 0) {
            queryWrapper.in(Article::getId, articleIdList);
        }
        //对显示页面的年月分类进行过滤
        if(pageParams.getYear()!=null && pageParams.getMonth() !=null){
            Integer year = pageParams.getYear();
            Integer month = pageParams.getMonth();
            articleIdList = articleMapper.findArticleByYearAndMonth(year, month);
        }
        if(articleIdList.size()>0){
            queryWrapper.in(Article::getId,articleIdList);
        }

        // 是否置顶排序
        queryWrapper.orderByDesc(Article::getWeight, Article::getCreateDate);
        Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);
        List<Article> records = articlePage.getRecords();
        // 当前数据为数据库返回数据，需要进行封装
        List<ArticleVo> articleVoList = copyList(records, true, true, false, false);
        return Result.success(articleVoList);
    }

    @Override
    public Result
    hotArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getViewCounts);
        queryWrapper.select(Article::getId, Article::getTitle);
        queryWrapper.last("limit " + limit);
        // select * from article order by view_counts desc limit 5;
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(copyList(articles, false, false, false, false));
    }

    @Override
    public Result newArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getCreateDate);
        queryWrapper.last("limit " + limit);
        List<Article> articleList = articleMapper.selectList(queryWrapper);
        return Result.success(articleList);
    }

    @Override
    public Result listArchives() {
        List<Archives> archivesList = articleMapper.listArchives();
        return Result.success(archivesList);
    }

    @Override
    public Result findArticleById(Long articleId) {
        /**
         * 根据id查询文章的信息
         * 根据bodayID 和tagID做关联查询
         */
        log.info("articleId: " + articleId);
        Article article = articleMapper.selectById(articleId);
        log.info("article:" + article.toString());
        ArticleVo articleVo = copy(article, true, true, true, true);
        /**
         * 查看文章后，新增阅读操作
         * 增加了更新操作，是有写锁的
         * 增加了此次操作的耗时 如果一段更新出问题不能影响文章的查看操作
         * 线程池结束解决
         * 可以把更新操作扔到线程池中执行，和主线程不相关了
         */

        threadServices.updateArtileViewCont(articleMapper, article);
        return Result.success(articleVo);
    }

    @Override
    @Transactional
    public Result publish(ArticleParam articleParam) {
        Article article = new Article();
        SysUser sysUser = UserThreadLocal.get();
        log.info("写文章 sysUser：" + sysUser);
        article.setAuthorId(sysUser.getId());
        article.setCategoryId(articleParam.getCategory().getId());
        article.setCreateDate(System.currentTimeMillis());
        article.setSummary(articleParam.getSummary());
        article.setTitle(articleParam.getTitle());
        article.setViewCounts(0);
        article.setWeight(Article.Article_Common);
        article.setBodyId(-1L);
        this.articleMapper.insert(article);


        // tags
        List<TagVo> tags = articleParam.getTags();
        if (tags != null) {
            for (TagVo tag : tags) {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(article.getId());
                articleTag.setTagId(tag.getId());

                this.articleTagMapper.insert(articleTag);
            }
        }

        ArticleBody articleBody = new ArticleBody();
        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
        articleBody.setArticleId(article.getId());
        articleBodyMapper.insert(articleBody);


        article.setBodyId(articleBody.getId());
        articleMapper.updateById(article);
        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(article.getId());
        return Result.success(articleVo);

    }

    @Autowired
    private ArticleBodyMapper articleBodyMapper;

    private ArticleBodyVo findBodyById(Long bodyId) {
        log.info("bodyId: " + bodyId);
        LambdaQueryWrapper<ArticleBody> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleBody::getId, bodyId);
        ArticleBody articleBody = articleBodyMapper.selectOne(queryWrapper);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        log.info("articleBody: " + articleBody.toString());
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;

    }


    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record, isTag, isAuthor, isBody, isCategory));
        }
        return articleVoList;
    }

    private ArticleVo copy(Article article, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory) {
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article, articleVo);
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        //并不是所有的接口都有作者和标签
        if (isTag) {
            Long articleId = Long.valueOf(article.getId());
            articleVo.setTags(tagsService.findTagsByArticleId(articleId));
        }
        if (isAuthor) {
            Long authorId = Long.valueOf(article.getAuthorId());
            articleVo.setAuthor(userService.findUserById(authorId).getNickname());
        }
        if (isBody) {
            Long boyId = Long.valueOf(article.getBodyId());
            articleVo.setBody(findBodyById(boyId));
        }
        if (isCategory) {
            Long categoryId = Long.valueOf(article.getCategoryId());
            articleVo.setCategory(categoryService.findCategoryById(categoryId));
        }
        return articleVo;
    }

}
