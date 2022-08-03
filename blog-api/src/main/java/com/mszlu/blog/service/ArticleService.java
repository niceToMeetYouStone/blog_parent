package com.mszlu.blog.service;

import com.mszlu.blog.vo.ArticleVo;
import com.mszlu.blog.vo.Result;
import com.mszlu.blog.vo.params.PageParams;

import java.util.List;

public interface ArticleService {

    /**
     * 分页查询首页文章列表
     * @param pageParams
     * @return
     */
     Result listArticle(PageParams pageParams);



}
