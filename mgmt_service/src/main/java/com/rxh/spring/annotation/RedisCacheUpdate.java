package com.rxh.spring.annotation;

import java.lang.annotation.*;

/**
 *  更新 redis 中的缓存
 * @author  panda
 * @date 20190806
 *
 *   使用案例：
 *      @RedisCacheUpdate(hashKey = "merchant_info",keyName = "#merId",updateObjectIndex = 0)
 *      public Result update(MerchantInfo record, String name) {
 *          .........
 *      }
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisCacheUpdate {

    /**
     *  缓存中的haskKey值
     * @return
     */
    String hashKey();

    /**
     * 指明方法参数中，以对象中那个属性为缓存的key值,取对象值时注意用 #号标识，否则认为是错误使用
     * @return
     */
    String keyName() default "";

    /**
     *   复合多个拼接成的key值，按顺序排列拼接，自认为用“-”进行拼接，使用该参数是，keyName 配置无效
     * @return
     */
    String[] keyNameArry() default "";
    /**
     *  使用方法参数中，那个对象为更新主体
     * @return
     */
    int updateObjectIndex() default 0;


}
