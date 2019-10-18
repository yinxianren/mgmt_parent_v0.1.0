package com.rxh.spring.annotation;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 * User: 陈俊雄
 * Date: 2018/5/10
 * Time: 11:57
 * Project: Management
 * Package: com.rxh.spring.annotation
 * <p>
 * 自定义注解，用于系统日志记录方法信息
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SystemLogInfo {
    /**
     * 方法描述
     *
     * @return 描述内容
     */
    String description() default "";
}
