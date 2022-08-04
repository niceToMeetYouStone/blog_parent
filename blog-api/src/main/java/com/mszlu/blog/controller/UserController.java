package com.mszlu.blog.controller;


import com.mszlu.blog.service.UserService;
import com.mszlu.blog.vo.Result;
import jdk.internal.net.http.ResponseSubscribers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("currentUser")
    public Result currentUser(@RequestHeader("Authorization") String token){
         return  userService.findUserByToken(token);

    }

}
