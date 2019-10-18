package com.rxh.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created with IntelliJ IDEA.
 * User: 陈俊雄
 * Date: 2018/5/3
 * Time: 16:40
 * Project: Management
 * Package: com.rxh.util
 */
public class UserInfoUtils {
    /**
     * 获取当前用户登录用户名
     *
     * @return 当前用户登录用户名
     */
    public static String getName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication == null ? null : authentication.getName();
    }
}
