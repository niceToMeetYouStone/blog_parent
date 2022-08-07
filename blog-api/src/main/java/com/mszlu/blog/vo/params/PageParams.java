package com.mszlu.blog.vo.params;


import lombok.Data;

@Data
public class PageParams {
    private Integer page = 1;
    private Integer pageSize = 10;
    private Long categoryId;
    private Long tagId;
    private Integer year;
    private Integer month;


}
