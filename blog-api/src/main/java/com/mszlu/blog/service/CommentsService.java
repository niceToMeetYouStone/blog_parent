package com.mszlu.blog.service;

import com.mszlu.blog.vo.Result;

public interface CommentsService {

    /**
     * 通过文章的id查找评论
     * @param id
     * @return
     */
    Result commentByArtileId(Long id);
}
