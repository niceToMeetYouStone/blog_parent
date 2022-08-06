package com.mszlu.blog.common.aop;

import java.lang.annotation.*;

/**
 * 添加AOP切面日志的注解
 * @author zhangrui
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {
    String module() default "";
    String operation() default "";
}
