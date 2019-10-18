package com.rxh.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: 陈俊雄
 * Date: 2018/7/16
 * Time: 11:00
 * Project: Management
 * Package: com.rxh.utils
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    private final static Logger logger = LoggerFactory.getLogger(DateUtils.class);

    public static Date dateFormat(DateFormat dateFormat, String dateStr) {
        try {
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            logger.error("日期转换异常！", e);
        }
        return null;
    }
}
