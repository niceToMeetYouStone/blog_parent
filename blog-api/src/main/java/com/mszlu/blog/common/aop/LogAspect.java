package com.mszlu.blog.common.aop;


import com.alibaba.fastjson.JSON;
import com.mszlu.blog.utils.HttpContextUtils;
import com.mszlu.blog.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;



import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 日志切面
 */
@Aspect
@Component
@Slf4j
public class LogAspect {


    @Pointcut("@annotation(com.mszlu.blog.common.aop.LogAnnotation)")
    public void logPointCut(){

    }



    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long begin = System.currentTimeMillis();
        // 执行方法
        Object result = point.proceed();
        // 执行时长
        Long time = System.currentTimeMillis()-begin;
        // 保存日志
        recordLog(point,time);
        return  result;

    }

    private void recordLog(ProceedingJoinPoint joinPoint, Long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LogAnnotation logAnnotation = method.getAnnotation(LogAnnotation.class);
        log.info("=============================log start==================================================");
        log.info("module:{}",logAnnotation.module());
        log.info("operation:{}",logAnnotation.operation());


        // 请求的方法名
        String ClassName = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        log.info("request method：{}",ClassName + "."+methodName+"()");

        //请求参数
        Object[] args = joinPoint.getArgs();
        String params = JSON.toJSONString(args[0]);
        log.info("params:{}",params);


        //请求的iP地址
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        log.info("ip:{}", IpUtils.getIpAddr(request));

        log.info("excute time: {}ms",time);
        log.info("===================================log end================================================");

    }
}
