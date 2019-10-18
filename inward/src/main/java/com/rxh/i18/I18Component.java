package com.rxh.i18;

import com.rxh.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: 陈俊雄
 * Date: 2018/9/26
 * Time: 14:19
 * Project: Management
 * Package: com.rxh.i18
 */
@Component
public class I18Component {
    private final MessageSource messageSource;

    @Autowired
    public I18Component(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * 根据语言获取Locale对象
     *
     * @param lang 语言
     * @return Locale
     */
    public Locale getLocale(String lang) {
        return new Locale(lang);
    }

    /**
     * 根据语言和国家获取Locale对象
     *
     * @param lang    语言
     * @param country 国家
     * @return Locale
     */
    public Locale getLocale(String lang, String country) {
        return new Locale(lang, country);
    }

    /**
     * 根据properties的key值以及语言获取国际化信息
     *
     * @param code properties key
     * @param lang 语言
     * @return 国际化信息
     */
    public String getI18Msg(String code, String lang) {
        String i18Msg = getI18Msg(code, new Locale(lang, ""));
        return StringUtils.isBlank(i18Msg) ? getI18Msg(code, Locale.ENGLISH) : i18Msg;
    }

    /**
     * 根据properties的key值以及语言获取国际化信息（含占位符）
     *
     * @param code code properties key
     * @param args properties占位符替换信息
     * @param lang 语言
     * @return 国际化信息
     */
    public String getI18Msg(String code, Object[] args, String lang) {
        String i18Msg = getI18Msg(code, args, new Locale(lang, ""));
        return StringUtils.isBlank(i18Msg) ? getI18Msg(code, args, Locale.ENGLISH) : i18Msg;
    }

    public String getI18Msg(String code, Object[] args, String defaultMessage, String lang) {
        String i18Msg = getI18Msg(code, args, new Locale(lang, ""));
        return StringUtils.isBlank(i18Msg) ? getI18Msg(code, args, defaultMessage, Locale.ENGLISH) : i18Msg;
    }

    /*public String getI18Msg(String code, String lang, String country) {
        String i18Msg = getI18Msg(code, new Locale(lang, country));
        return StringUtils.isBlank(i18Msg) ? getI18Msg(code, Locale.ENGLISH) : i18Msg;
    }


    public String getI18Msg(String code, Object[] args, String lang, String country) {
        String i18Msg = getI18Msg(code, args, new Locale(lang, country));
        return StringUtils.isBlank(i18Msg) ? getI18Msg(code, args, Locale.ENGLISH) : i18Msg;
    }

    public String getI18Msg(String code, Object[] args, String defaultMessage, String lang, String country) {
        String i18Msg = getI18Msg(code, args, new Locale(lang, country));
        return StringUtils.isBlank(i18Msg) ? getI18Msg(code, args, defaultMessage, Locale.ENGLISH) : i18Msg;
    }*/

    public String getI18Msg(String code, Locale locale) {
        return getI18Msg(code, null, locale);
    }

    public String getI18Msg(String code, Object[] args, Locale locale) {
        return messageSource.getMessage(code, args, locale);
    }

    public String getI18Msg(String code, Object[] args, String defaultMessage, Locale locale) {
        return messageSource.getMessage(code, args, defaultMessage, locale);
    }
}
