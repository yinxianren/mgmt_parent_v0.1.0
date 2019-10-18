package com.rxh.payInterface;

import java.util.Arrays;

/**
 *   工具类
 * @author panda
 * @date 20190718
 */
public interface PayUtil {


    /**
     *  格式文本
     * @param format
     * @param args
     * @return
     */
    default  String format(String format, Object... args){
        return String.format(format,args);
    }

    /**
     *  去除前后空格
     * @param str
     * @return
     */
    default String trimEmpty(String str){
        return str.trim();
    }

    /**
     * 去除所有空字符
     * @param string
     * @return
     */
    default String  trimAllEmpty(String string){
        String str= string.trim();
        if(str.contains(" ")){
            str= Arrays
                    .stream(str.split(" "))
                    .reduce((str1,str2)->str1+str2)
                    .orElse(String.format("无效值:",string));
        }
        return str;
    }


}
