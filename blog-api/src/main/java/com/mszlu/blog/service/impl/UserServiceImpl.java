package com.mszlu.blog.service.impl;

import com.mszlu.blog.dao.mapper.SysUserMapper;
import com.mszlu.blog.dao.pojo.SysUser;
import com.mszlu.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private SysUserMapper sysUserMapper;


    @Override
    public SysUser findUserById(Long userId) {
        SysUser sysUser = sysUserMapper.selectBatchIds(userId);
        if(sysUser == null){
            sysUser = new SysUser();
            sysUser.setNickname("hellowWorld");
        }
        return sysUser;
    }
}
