package com.rxh.utils;

/**
 * Created with IntelliJ IDEA.
 * User: 陈俊雄
 * Date: 2018/9/25
 * Time: 10:55
 * Project: Management
 * Package: com.rxh.utils
 */
public class CardHandle {
    public static String getSimpleCardNum(String cardNum) {
        int length = cardNum.length();
        return length > 10 ? cardNum.substring(0, 6) + "***" + cardNum.substring(length - 4, length) : cardNum;
    }
}
