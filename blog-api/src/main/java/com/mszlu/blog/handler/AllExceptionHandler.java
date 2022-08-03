package com.mszlu.blog.handler;


import com.mszlu.blog.vo.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


// 对加了aontroller的注解的进行拦截处理
@ControllerAdvice
public class AllExceptionHandler {
    // 进行异常处理，处理excaption的异常
    @ExceptionHandler(Exception.class)
    public Result doExcaption(Exception e) {
        e.printStackTrace();
        return Result.fail(-999, "系统异常");
    }
}
