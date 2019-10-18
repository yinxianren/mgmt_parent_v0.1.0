package com.rxh.utils;

import com.rxh.pojo.sys.SysConstant;

/**
 * Created with IntelliJ IDEA.
 * User: 陈俊雄
 * Date: 2018/11/8
 * Time: 15:29
 * Project: Management
 * Package: com.rxh.utils
 */
public class CrossUtils {
    public final static String PAY_1043 = "pay.1043";
    public final static String PAY_1047 = "pay.1047";
    public final static String PAY_1048 = "pay.1048";
    public final static String ERROR_4001 = "error.4001";
    public final static String ERROR_5001 = "error.5001";

    public static String getProvince(String country, String province, SysConstant provinceConstant) {
        if (StringUtils.equalsAnyIgnoreCase(country, "US", "CA")) {
            return provinceConstant.getSecondValue();
        } else {
            return province;
        }
    }
}
