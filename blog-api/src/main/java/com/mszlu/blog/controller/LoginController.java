package com.mszlu.blog.controller;


import com.mszlu.blog.service.LoginService;
import com.mszlu.blog.vo.Result;
import com.mszlu.blog.vo.params.LoginParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("login")
public class LoginController {
    @Autowired
    private LoginService loginService;


    @PostMapping
    public Result login(@RequestBody LoginParams loginParams){
        System.out.println(loginParams.getPassword());
        // 登录 验证用户
        return  loginService.login(loginParams);

    }

}
