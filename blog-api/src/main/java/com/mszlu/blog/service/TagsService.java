package com.mszlu.blog.service;


import com.mszlu.blog.vo.Result;
import com.mszlu.blog.vo.TagVo;

import java.util.List;

public interface TagsService {
    List<TagVo> findTagsByArticleId(Long id);

    /**
     * 查询最热的标签的前n条
     * @param limit
     * @return
     */
    Result hots(int limit);

    /**
     * 所有的标签
     * @return
     */
    Result findAll();
}
