package com.rxh.spring.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisCacheDeleteByHashKey {

    /**
     *  缓存中的haskKey值
     * @return
     */
    String hashKey();

}
