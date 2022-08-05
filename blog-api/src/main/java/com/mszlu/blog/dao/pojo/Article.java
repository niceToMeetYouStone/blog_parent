package com.mszlu.blog.dao.pojo;


import lombok.Data;

@Data
public class Article {
    public static final Integer Article_TOP = 1;

    public static final Integer Article_Common = 0;

    private String id;

    private String title;

    private String summary;

    private int commentCounts;

    private int viewCounts;

    /**
     * 作者id
     */
    private Long authorId;
    /**
     * 内容id
     */
    private Long bodyId;
    /**
     * 类别id
     */
    private Long categoryId;

    /**
     * 置顶
     */
    private int weight;


    /**
     * 创建时间
     */
    private Long createDate;
}
