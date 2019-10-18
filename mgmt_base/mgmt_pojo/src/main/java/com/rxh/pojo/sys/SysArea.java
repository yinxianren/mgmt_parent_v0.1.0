package com.rxh.pojo.sys;

import java.io.Serializable;
import java.util.List;

public class SysArea implements Serializable {
    // 
    private Integer id;

    // 
    private String name;

    // 
    private String shortName;

    // 当level为0时，pid为常量组Country提供
    private Integer pid;

    // 所属国家
    private String countryCode;

    // 身份证前6位为行政代码
    private String areaCode;

    // 
    private String zipCode;

    // 电话区号
    private String cityCode;

    // 0-省 1-市 2-县 3-镇
    private Short level;

    // 
    private String longitude;

    // 
    private String latitude;

    // 中国区域为 拼音
    private String extend;

    // 排序
    private Integer sortValue;

    // 子区域
    private List<SysArea> childArea;

    /**
     * 
     * @return id 
    */
    public Integer getId() {
        return id;
    }

    /**
     * 
     * @param id 
    */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 
     * @return name 
    */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name 
    */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 
     * @return short_name 
    */
    public String getShortName() {
        return shortName;
    }

    /**
     * 
     * @param shortName 
    */
    public void setShortName(String shortName) {
        this.shortName = shortName == null ? null : shortName.trim();
    }

    /**
     * 当level为0时，pid为常量组Country提供
     * @return pid 当level为0时，pid为常量组Country提供
    */
    public Integer getPid() {
        return pid;
    }

    /**
     * 当level为0时，pid为常量组Country提供
     * @param pid 当level为0时，pid为常量组Country提供
    */
    public void setPid(Integer pid) {
        this.pid = pid;
    }

    /**
     * 所属国家
     * @return country_code 所属国家
    */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * 所属国家
     * @param countryCode 所属国家
    */
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode == null ? null : countryCode.trim();
    }

    /**
     * 身份证前6位为行政代码
     * @return area_code 身份证前6位为行政代码
    */
    public String getAreaCode() {
        return areaCode;
    }

    /**
     * 身份证前6位为行政代码
     * @param areaCode 身份证前6位为行政代码
    */
    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode == null ? null : areaCode.trim();
    }

    /**
     * 
     * @return zip_code 
    */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * 
     * @param zipCode 
    */
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode == null ? null : zipCode.trim();
    }

    /**
     * 电话区号
     * @return city_code 电话区号
    */
    public String getCityCode() {
        return cityCode;
    }

    /**
     * 电话区号
     * @param cityCode 电话区号
    */
    public void setCityCode(String cityCode) {
        this.cityCode = cityCode == null ? null : cityCode.trim();
    }

    /**
     * 0-省 1-市 2-县 3-镇
     * @return level 0-省 1-市 2-县 3-镇
    */
    public Short getLevel() {
        return level;
    }

    /**
     * 0-省 1-市 2-县 3-镇
     * @param level 0-省 1-市 2-县 3-镇
    */
    public void setLevel(Short level) {
        this.level = level;
    }

    /**
     * 
     * @return longitude 
    */
    public String getLongitude() {
        return longitude;
    }

    /**
     * 
     * @param longitude 
    */
    public void setLongitude(String longitude) {
        this.longitude = longitude == null ? null : longitude.trim();
    }

    /**
     * 
     * @return latitude 
    */
    public String getLatitude() {
        return latitude;
    }

    /**
     * 
     * @param latitude 
    */
    public void setLatitude(String latitude) {
        this.latitude = latitude == null ? null : latitude.trim();
    }

    /**
     * 中国区域为 拼音
     * @return extend 中国区域为 拼音
    */
    public String getExtend() {
        return extend;
    }

    /**
     * 中国区域为 拼音
     * @param extend 中国区域为 拼音
    */
    public void setExtend(String extend) {
        this.extend = extend == null ? null : extend.trim();
    }

    /**
     * 排序
     * @return sort_value 排序
    */
    public Integer getSortValue() {
        return sortValue;
    }

    /**
     * 排序
     * @param sortValue 排序
    */
    public void setSortValue(Integer sortValue) {
        this.sortValue = sortValue;
    }

    public List<SysArea> getChildArea() {
        return childArea;
    }

    public void setChildArea(List<SysArea> childArea) {
        this.childArea = childArea;
    }
}