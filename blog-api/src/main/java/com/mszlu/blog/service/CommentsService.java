package com.mszlu.blog.service;

import com.mszlu.blog.vo.Result;
import com.mszlu.blog.vo.params.CommentParams;

public interface CommentsService {

    /**
     * 通过文章的id查找评论
     * @param id
     * @return
     */
    Result commentByArticleId(Long id);


    /**
     * 写评论
     * @param commentParams
     * @return
     */
    Result comment(CommentParams commentParams);
}
