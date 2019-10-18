package com.rxh.spring.object.mapper;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * Created with IntelliJ IDEA.
 * User: 陈俊雄
 * Date: 2018/4/23
 * Time: 12:01
 * Project: Management
 * Package: com.rxh.spring.object.mapper
 *
 * Long2String
 */
public class Long2StringObjectMapper extends ObjectMapper {

    public Long2StringObjectMapper() {
        super();
        // 解决Object中Long类型转Json对象页面Js获取丢失精度问题
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        registerModule(simpleModule);
        // 解决Json转对象时 Json含有对象没有的字段导致Http Json解析报400请求错误
        this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true);
    }
}
