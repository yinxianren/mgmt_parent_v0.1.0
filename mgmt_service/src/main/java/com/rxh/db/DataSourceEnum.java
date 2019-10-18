package com.rxh.db;

/**
 * Created with IntelliJ IDEA.
 * User: 陈俊雄
 * Date: 2018/3/14
 * Time: 16:00
 * Project: Management
 * Package: com.rxh.db
 */
public enum DataSourceEnum {
    DS1("ds1"), DS2("ds2");

    private String key;

    DataSourceEnum(String key) { this.key = key; }

    public String getKey() { return key; }

    public void setKey(String key) {  this.key = key; }
}
