package com.mszlu.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.mszlu.blog.dao.pojo.SysUser;
import com.mszlu.blog.service.LoginService;
import com.mszlu.blog.service.UserService;
import com.mszlu.blog.utils.JWTUtils;
import com.mszlu.blog.vo.ErrorCode;
import com.mszlu.blog.vo.Result;
import com.mszlu.blog.vo.params.LoginParams;
import com.qiniu.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Map;
import java.util.concurrent.TimeUnit;


@Service
public class LoginServiceImpl implements LoginService {
    private final String slat = "mszlu!@#";
    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;


    @Override
    public Result login(LoginParams loginParama) {
        /**
         * 检查参数是否合法
         * 检查用户名和密码在user中查询，如果不存在登录失败
         * 如果存在，使用jwt生成token，返回给前端
         * 将token放入token中  设置过期时 登录人证的时候，先看token是否合法，去redis看看是否存在
         */
        String account = loginParama.getAccount();
        String password = loginParama.getPassword();
        if (StringUtils.isNullOrEmpty(account) || StringUtils.isNullOrEmpty(password)) {
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }
        password = DigestUtils.md5Hex(password + slat);
        SysUser sysUser = userService.findUser(account, password);
        if (sysUser == null) {
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(), ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }

        System.out.println(sysUser.toString());
        String token = JWTUtils.createToken(sysUser.getId());
        redisTemplate.opsForValue().set("TOKEN_" + token, JSON.toJSONString(sysUser), 1, TimeUnit.DAYS);

        return Result.success(token);


    }


    @Override
    public Result logout(String token) {
        redisTemplate.delete(token);
        return Result.success(null);
    }
}
