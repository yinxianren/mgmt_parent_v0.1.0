package com.rxh.pojo.sys;

import java.io.Serializable;


public class SysConstant implements Serializable {
    //
    private String id;

    // 常量名称
    private String name;

    // 常量值
    private String firstValue;

    // 常量值
    private String secondValue;

    // 常量组别
    private String groupCode;

    // 排序
    private Integer sortValue;

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 常量名称
     *
     * @return name 常量名称
     */
    public String getName() {
        return name;
    }

    /**
     * 常量名称
     *
     * @param name 常量名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 常量值
     *
     * @return first_value 常量值
     */
    public String getFirstValue() {
        return firstValue;
    }

    /**
     * 常量值
     *
     * @param firstValue 常量值
     */
    public void setFirstValue(String firstValue) {
        this.firstValue = firstValue == null ? null : firstValue.trim();
    }

    /**
     * 常量值
     *
     * @return second_value 常量值
     */
    public String getSecondValue() {
        return secondValue;
    }

    /**
     * 常量值
     *
     * @param secondValue 常量值
     */
    public void setSecondValue(String secondValue) {
        this.secondValue = secondValue == null ? null : secondValue.trim();
    }

    /**
     * 常量组别
     *
     * @return group_code 常量组别
     */
    public String getGroupCode() {
        return groupCode;
    }

    /**
     * 常量组别
     *
     * @param groupCode 常量组别
     */
    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode == null ? null : groupCode.trim();
    }

    /**
     * 排序
     *
     * @return sort_value 排序
     */
    public Integer getSortValue() {
        return sortValue;
    }

    /**
     * 排序
     *
     * @param sortValue 排序
     */
    public void setSortValue(Integer sortValue) {
        this.sortValue = sortValue;
    }
}