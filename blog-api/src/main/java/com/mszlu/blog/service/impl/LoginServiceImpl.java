package com.mszlu.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.mszlu.blog.dao.pojo.SysUser;
import com.mszlu.blog.service.LoginService;
import com.mszlu.blog.service.UserService;
import com.mszlu.blog.utils.JwtUtils;
import com.mszlu.blog.vo.ErrorCode;
import com.mszlu.blog.vo.Result;
import com.mszlu.blog.vo.params.LoginParams;
import com.qiniu.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.concurrent.TimeUnit;


@Service
public class LoginServiceImpl implements LoginService {
    private final String slat = "mszlu!@#";
    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


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
        SysUser sysUser = this.userService.findUser(account, password);
        System.out.println("存入用户 sysUser" + sysUser);
        if (sysUser == null) {
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(), ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
        String token = JwtUtils.createToken(Long.valueOf(sysUser.getId()));
        this.redisTemplate.opsForValue().set("TOKEN_" + token, JSON.toJSONString(sysUser), 1, TimeUnit.DAYS);

        return Result.success(token);
    }


    @Override
    public Result logout(String token) {
        redisTemplate.delete(token);
        return Result.success(null);
    }

    @Override
    public Result register(LoginParams loginParams) {
        /**
         * 1.判断参数是否合法
         * 2.判断账户是否存在，存在返回账户已经注册
         * 3.如果账户不存在，注册用户
         * 4.生成token，传入redis并返回
         * 5.注意加上事务，一旦中间过程出现任何问题，要实现回滚
         */
        String account = loginParams.getAccount();
        String password = loginParams.getPassword();
        String nickname = loginParams.getNickname();

        if (StringUtils.isNullOrEmpty(account) || StringUtils.isNullOrEmpty(password) || StringUtils.isNullOrEmpty(nickname)) {
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }
        // 通过用户名查找用户
        SysUser user = userService.findUserByAccount(account);
        if (user != null) {
            return Result.fail(ErrorCode.ACCOUNT_EXIST.getCode(), ErrorCode.ACCOUNT_EXIST.getMsg());
        }
        SysUser sysUser = new SysUser();
        sysUser.setNickname(nickname);
        sysUser.setAccount(account);
        sysUser.setPassword(DigestUtils.md5Hex(password + slat));
        sysUser.setCreateDate(System.currentTimeMillis());
        sysUser.setLastLogin(System.currentTimeMillis());
        sysUser.setAvatar("/static/img/logo.b3a48c0.png");
        sysUser.setAdmin(1); //1 为true
        sysUser.setDeleted(0); // 0 为false
        sysUser.setSalt("");
        sysUser.setStatus("");
        sysUser.setEmail("");
        // 保存用户
        this.userService.save(sysUser);
        String token = JwtUtils.createToken(Long.valueOf(sysUser.getId()));
        redisTemplate.opsForValue().set("TOKEN_" + token, JSON.toJSONString(sysUser), 1, TimeUnit.DAYS);
        return Result.success(token);
    }
}
