package com.rxh.spring.annotation;


import java.lang.annotation.*;

/**
 * 批量删除不同缓存区的数据
 * @author panda
 * @date 20190806
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisCacheDeleteByBatch {

    String[] hashKey();

    /**
     *  0:String  1:Array   2:List   3:set  （:4Map，暂不支持）
     * @return
     */
    int  keyType() default 0;


    /**
     *  要删除的对象所在的参数位置
     * @return
     */
    int del0bjectIndex() default 0;

    /**
     *  如果是对象类型需指明key的字段名称
     *  取对象值时注意用 #号标识，否则认为是错误使用
     *  类型为 list,set 要指定该值
     * @return
     */
    String  keyName() default "";
}
