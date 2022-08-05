package com.mszlu.blog.vo;


import lombok.Data;

import java.util.List;

@Data
public class CommentVo {
    private  String id;
    private UserVo author;
    private  String  content;
    private List<CommentVo> children;
    private String createDate;
    private Integer level;
    private UserVo toUser;

}
