package com.mszlu.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.mszlu.blog.dao.pojo.SysUser;
import com.mszlu.blog.service.TokenService;
import com.mszlu.blog.utils.JwtUtils;
import com.qiniu.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public SysUser checkToken(String token) {
        if(StringUtils.isNullOrEmpty(token)){
            return null;
        }

        Map<String, Object> map = JwtUtils.checkToken(token);
        if(map == null){
            return null;
        }
        String userJson = redisTemplate.opsForValue().get("TOKEN_" + token);
        if(StringUtils.isNullOrEmpty(userJson)){
            return null;
        }
        SysUser sysUser = JSON.parseObject(userJson, SysUser.class);
        return sysUser;
    }
}
