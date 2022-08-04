package com.mszlu.blog.handler;


import com.alibaba.fastjson.JSON;
import com.mszlu.blog.dao.pojo.SysUser;
import com.mszlu.blog.service.TokenService;
import com.mszlu.blog.vo.ErrorCode;
import com.mszlu.blog.vo.Result;
import com.qiniu.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private TokenService tokenService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 在执行方法之前执行（Handler）
        /**
         * 需要判断请求的接口路径是否为HandlerMethod()
         * 判断token是否为空，如果为空未登录
         * 如果token不为空，进行登录验证LoginService checkToken
         * 如果 认证成功放行
         */
        if(!(handler instanceof HandlerMethod)){
            // handler 可能RequestResourceHandler springboot 下的静态资源
            return true;
        }
        String token = request.getHeader("Authorization");
        log.info("=================request start===========================");
        String requestURI = request.getRequestURI();
        log.info("request uri:{}",requestURI);
        log.info("request method:{}",request.getMethod());
        log.info("token:{}", token);
        log.info("=================request end===========================");

        if(StringUtils.isNullOrEmpty(token)){
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(),ErrorCode.NO_LOGIN.getMsg());
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }
        SysUser sysUser = tokenService.checkToken(token);
        if(sysUser == null){
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(),ErrorCode.NO_LOGIN.getMsg());
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }
        // 验证成功放行
        return true;
    }
}
