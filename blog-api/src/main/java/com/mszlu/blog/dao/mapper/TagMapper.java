package com.mszlu.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mszlu.blog.dao.pojo.Tag;

import java.util.List;

/**
 * @author zhangrui
 */
public interface TagMapper extends BaseMapper<Tag> {
    /**
     * 根据文章id查询文件名
     * @param id
     * @return
     */
    List<Tag> findTagsByArticleId(Long id);

    /**
     * 查询最热的标签前n条
     * @param limit
     * @return
     */
    List<Long> findTagsByArticleIds(int limit);


    /**
     * 通过tag的id查询tag
     * @param tagIds
     * @return
     */
    List<Tag> findTagsByTagIds(List<Long> tagIds);
}
