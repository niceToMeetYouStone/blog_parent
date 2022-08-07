package com.mszlu.blog.cache;


import com.alibaba.fastjson.JSON;
import com.mszlu.blog.vo.Result;
import com.qiniu.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Duration;


@Aspect
@Component
@Slf4j
public class CacheAspect {
    @Autowired
    private RedisTemplate<String,String> redisTemplate;


    @Pointcut("@annotation(com.mszlu.blog.cache.Cache)")
    public void pt(){}

    @Around("pt()")
    public Object around(ProceedingJoinPoint joinPoint){


        try {
            Signature signature = joinPoint.getSignature();
            //类名
            String   className= joinPoint.getTarget().getClass().getSimpleName();
            // 调用类的方法
            String methodName = signature.getName();
            Class[] parameterTypes = new Class[joinPoint.getArgs().length];
            Object[] args = joinPoint.getArgs();

            // 参数
            String params = "";
            for (int i = 0; i < args.length; i++) {
                if(args[i] !=null){
                    params += JSON.toJSONString(args[i]);
                    parameterTypes[i] = args[i].getClass();
                }else{
                    parameterTypes[i] = null;
                }
            }


            if (StringUtils.isNullOrEmpty(params)){
                // 加密 以防止key过长的字符及转义获取不到的情况
                params = DigestUtils.md5Hex(params);
            }
            Method method = joinPoint.getSignature().getDeclaringType().getMethod(methodName,parameterTypes);
            // /获取Cache注解
            Cache annnotation = method.getAnnotation(Cache.class);
            // 缓存时间过期
            long expire = annnotation.expire();
            // 缓存名称
            String name = annnotation.name();
            // 先从redis中获取
            String key = name +"::" +className + "::" +methodName+"::"+params;
            String redisValue = redisTemplate.opsForValue().get(key);
            if(!StringUtils.isNullOrEmpty(redisValue)){
                log.info("命中缓存-------，{}，{}",className,methodName);
                return JSON.parseObject(redisValue, Result.class);
            }

            Object proceed = joinPoint.proceed();
            redisTemplate.opsForValue().set(key,JSON.toJSONString(proceed), Duration.ofMillis(expire));
            log.info("存入缓存-----------,{},{}",className,methodName);
            return proceed;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return Result.fail(-999,"系统错误");

    }



}
