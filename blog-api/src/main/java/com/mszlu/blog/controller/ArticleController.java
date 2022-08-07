package com.mszlu.blog.controller;


import com.mszlu.blog.cache.Cache;
import com.mszlu.blog.common.aop.LogAnnotation;
import com.mszlu.blog.service.ArticleService;
import com.mszlu.blog.vo.Result;
import com.mszlu.blog.vo.params.ArticleParam;
import com.mszlu.blog.vo.params.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


//json数据进行交互
@RestController
@RequestMapping("articles")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    /**
     * 首页文章列表
     *
     * @param pageParams
     * @return
     */
    @LogAnnotation(module = "文章",operation = "展示文章列表")
    @PostMapping
    public Result listArticle(@RequestBody PageParams pageParams) {
        return articleService.listArticle(pageParams);
    }



    @PostMapping("hot")
    @Cache(expire = 5 * 60 * 1000,name = "hot_article")
    public Result hotArticle() {
        int limit = 5;
        return articleService.hotArticle(limit);
    }

    @PostMapping("new")
    public Result newAtricle() {
        int limit = 5;
        return articleService.newArticle(limit);
    }

    /**
     * 文章归档
     *
     * @return
     */
    @PostMapping("listArchives")
    public Result listArchives() {
        return articleService.listArchives();
    }


    @PostMapping("/view/{id}")
    public Result findArticleById(@PathVariable("id") Long articleId){
        return articleService.findArticleById(articleId);
    }


    @PostMapping("publish")
    public Result publish(@RequestBody ArticleParam articleParam){
        return articleService.publish(articleParam);
    }




}
