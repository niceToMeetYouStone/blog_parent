package com.mszlu.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mszlu.blog.dao.mapper.CommmentsMapper;
import com.mszlu.blog.dao.pojo.Comment;
import com.mszlu.blog.dao.pojo.SysUser;
import com.mszlu.blog.service.CommentsService;
import com.mszlu.blog.service.UserService;
import com.mszlu.blog.utils.UserThreadLocal;
import com.mszlu.blog.vo.CommentVo;
import com.mszlu.blog.vo.Result;
import com.mszlu.blog.vo.UserVo;
import com.mszlu.blog.vo.params.CommentParams;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
@Slf4j
public class CommentsServiceImpl implements CommentsService {


    @Autowired
    private CommmentsMapper commmentsMapper;


    @Autowired
    private UserService userService;


    /**
     * 根据文章的id查找评论信息
     * 根据作者的id查询作者的信息
     * 判断如果level=1 查询有没有子评论
     * 如果有子评论，根据评论id进行查询
     *
     * @param id
     * @return
     */
    @Override
    public Result commentByArticleId(Long id) {
        log.info("评论模块  id： " + id);
        LambdaQueryWrapper<Comment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Comment::getArticleId, id);
        lambdaQueryWrapper.eq(Comment::getLevel, 1);
        List<Comment> comments = commmentsMapper.selectList(lambdaQueryWrapper);
        return Result.success(copyList(comments));

    }


    @Override
    public Result comment(CommentParams commentParams) {
        //
        log.info("实现评论 commentParams：" +commentParams.toString());
        //
        // SysUser user = UserThreadLocal.get();
        // Comment comment = new Comment();
        // comment.setContent(commentParams.getComment());
        // comment.setArticleId(commentParams.getArticleId());
        // comment.setAuthorId(user.getId());
        // comment.setCreateDate(System.currentTimeMillis());
        // Long parent = Long.valueOf(commentParams.getParent());
        // if(parent == null || parent == 0){
        //     comment.setLevel(1);
        // }else {
        //     comment.setLevel(2);
        // }
        // comment.setParentId(parent == null?String.valueOf(0):String.valueOf(parent));
        // Long toUserId = Long.valueOf(commentParams.getToUserId());
        // comment.setToUid(toUserId == null?String.valueOf(0):String.valueOf(toUserId));
        // this.commmentsMapper.insert(comment);
        return Result.success(null);
    }

    private List<CommentVo> copyList(List<Comment> comments) {
        List<CommentVo> commentVoList = new ArrayList<>();
        for (Comment comment : comments) {
            commentVoList.add(copy(comment));
        }
        return commentVoList;
    }


    private CommentVo copy(Comment comment) {
        CommentVo commentVo = new CommentVo();
        // 获取作者信息
        Long authorId = Long.valueOf(comment.getAuthorId());
        UserVo userVo = userService.findUserVoById(authorId);

        commentVo.setAuthor(userVo);
        //子评论
        Integer level = comment.getLevel();
        if (level == 1) {
            String id = comment.getId();
            List<CommentVo> commentVoList = findCommentsParentId(id);
            commentVo.setChildren(commentVoList);
        }
        commentVo.setCreateDate(new DateTime(comment.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        // to User 给谁评论
        if (level > 1) {
            Long toUid = Long.valueOf(comment.getToUid());
            UserVo userVoUid = userService.findUserVoById(toUid);
            commentVo.setToUser(userVoUid);
        }
        return commentVo;

    }

    /**
     * 子评论的查询
     *
     * @param id
     * @return
     */
    private List<CommentVo> findCommentsParentId(String id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(Comment::getParentId, id);
        queryWrapper.eq(Comment::getLevel, 2);
        return copyList(commmentsMapper.selectList(queryWrapper));
    }
}
