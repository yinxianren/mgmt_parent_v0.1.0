package com.rxh.spring.annotation;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 * User: 王德明
 * Date: 2018/7/2
 * Time: 15:29
 * Project: Management_new
 * Package: com.rxh.spring.annotation
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface redisCache {
    Class type();
    String key();
    String field();
    int expireTime() default 3600;
}
