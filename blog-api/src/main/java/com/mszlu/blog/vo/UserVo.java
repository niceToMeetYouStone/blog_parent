package com.mszlu.blog.vo;


import com.mszlu.blog.common.aop.LogAnnotation;
import lombok.Data;

@Data
public class UserVo {
    private String nickname;
    private String avatar;
    private Long id;
}
