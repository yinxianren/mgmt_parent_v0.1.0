package com.rxh.pojo.sys;

import java.io.Serializable;

public class SysGroup implements Serializable {
     // 常量组编码
    private String code;

     // 常量组名称
    private String name;

     // 1:系统 2:商户 3:代理商
    private Short model;

     // 0: 否 1: 是
    private Short system;

    /**
     * 常量组编码
     * @return code 常量组编码
     */
    public String getCode() {
        return code;
    }

    /**
     * 常量组编码
     * @param code 常量组编码
     */
    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    /**
     * 常量组名称
     * @return name 常量组名称
     */
    public String getName() {
        return name;
    }

    /**
     * 常量组名称
     * @param name 常量组名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 1:系统 2:商户 3:代理商
     * @return model 1:系统 2:商户 3:代理商
     */
    public Short getModel() {
        return model;
    }

    /**
     * 1:系统 2:商户 3:代理商
     * @param model 1:系统 2:商户 3:代理商
     */
    public void setModel(Short model) {
        this.model = model;
    }

    /**
     * 0: 否 1: 是
     * @return system 0: 否 1: 是
     */
    public Short getSystem() {
        return system;
    }

    /**
     * 0: 否 1: 是
     * @param system 0: 否 1: 是
     */
    public void setSystem(Short system) {
        this.system = system;
    }
}