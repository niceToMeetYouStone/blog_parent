package com.mszlu.blog.service;

import com.mszlu.blog.dao.pojo.SysUser;

public interface TokenService {

    /**
     * 通过token查找用户
     * @param token
     * @return
     */
    SysUser checkToken(String token);
}
