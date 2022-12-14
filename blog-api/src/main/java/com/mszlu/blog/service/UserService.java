package com.mszlu.blog.service;

import com.mszlu.blog.dao.pojo.SysUser;
import com.mszlu.blog.vo.Result;
import com.mszlu.blog.vo.UserVo;

public interface UserService {
    SysUser findUserById(Long userId);

    SysUser findUser(String account, String password);

    /**
     * 通过token查看用户
     * @param token
     * @return
     */
    Result findUserByToken(String token);

    /**
     * 通过用户名查找用户
     * @param account
     * @return
     */
    SysUser findUserByAccount(String account);

    /**
     * 保存用户信息
     * @param sysUser
     */
    void save(SysUser sysUser);

    /**
     * t通过用户的id获取UserVo对象
     * @param authorId
     * @return
     */
    UserVo findUserVoById(Long authorId);
}


