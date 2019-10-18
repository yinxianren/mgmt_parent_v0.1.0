package com.rxh.spring.annotation;


import java.lang.annotation.*;

/**
 *  综合操作redis 缓存,配合RedisPosting使用
 * @author  panda
 * @date 20190806
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisCacheAble {

}
