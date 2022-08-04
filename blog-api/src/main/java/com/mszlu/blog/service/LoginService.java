package com.mszlu.blog.service;

import com.mszlu.blog.dao.pojo.SysUser;
import com.mszlu.blog.vo.Result;
import com.mszlu.blog.vo.params.LoginParams;

public interface LoginService {
    /**
     * d登陆功能
     * @param loginParama
     * @return
     */
    Result login(LoginParams loginParama);


    /**
     * 退出登录,删除token
     * @param token
     * @return
     */
    Result logout(String token);
}
