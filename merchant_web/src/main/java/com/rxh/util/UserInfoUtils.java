package com.rxh.util;

import com.rxh.square.pojo.MerchantInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 * @author : 陈俊雄
 * Date: 2018/5/3
 * Time: 16:40
 * Project: Management
 * Package: com.rxh.util
 */
@Component
public class UserInfoUtils {

    @Autowired
    private HttpServletRequest request;

    private static UserInfoUtils userInfoUtils;

    @PostConstruct
    public void init() {
        userInfoUtils = this;
    }

    /**
     * 因商户登录名存在重复的情况下，spring security中name值为当前登录用id
     *
     * @return 获取当前用户登录ID
     */
    public static Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication == null ? null : Long.valueOf(authentication.getName());
    }

    /**
     * 获取当前用户所属商户ID
     *
     * @return 商户ID
     */
    public static String getMerchantId() {
       MerchantInfo merchantInfo = (MerchantInfo) userInfoUtils.request.getSession().getAttribute("merchantInfo");
        return merchantInfo.getMerId();
    }

    public static String getUserName() {
        return userInfoUtils.request.getSession().getAttribute("userName").toString();
    }
}
