package com.rxh.spring.annotation;


import java.lang.annotation.*;

/**
 *  删除redis中的缓存
 * @author  panda
 * @date 20190806
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisCacheDelete {
    /**
     *  缓存中的haskKey值
     * @return
     */
    String hashKey();

    /**
     * 指明方法参数中，第几个参数为缓存的key值,默认为0
     * @return
     */
    int keyIndex() default 0;

    /**
     * 指明方法参数中，指定对象那个属性为缓存的key值
     * 取对象值时注意用 #号标识，否则认为是错误使用
     * @return
     */
    String keyName() default "";

    /**
     *   复合多个拼接成的key值，按顺序排列拼接，自认为用“-”进行拼接，使用该参数是，keyName 配置无效
     * @return
     */
    String[] keyNameArry() default "";
}
