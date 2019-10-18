package com.rxh.pojo.risk;

import java.io.Serializable;
import java.util.Date;

public class RiskDangerIp implements Serializable {
     // 
    private Long id;

     // asn,ipv4,ipv6
    private String type;

     // 常量组country提供
    private String country;

     // 
    private Integer ip;

     // 
    private String remark;

     // 创建人
    private String creator;

     // 创建时间
    private Date createTime;

     // 最后修改时间
    private String modifier;

     // 最后修改用户
    private Date modifyTime;

    /**
     * 
     * @return id 
     */
    public Long getId() {
        return id;
    }

    /**
     * 
     * @param id 
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * asn,ipv4,ipv6
     * @return type asn,ipv4,ipv6
     */
    public String getType() {
        return type;
    }

    /**
     * asn,ipv4,ipv6
     * @param type asn,ipv4,ipv6
     */
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    /**
     * 常量组country提供
     * @return country 常量组country提供
     */
    public String getCountry() {
        return country;
    }

    /**
     * 常量组country提供
     * @param country 常量组country提供
     */
    public void setCountry(String country) {
        this.country = country == null ? null : country.trim();
    }

    /**
     * 
     * @return ip 
     */
    public Integer getIp() {
        return ip;
    }

    /**
     * 
     * @param ip 
     */
    public void setIp(Integer ip) {
        this.ip = ip;
    }

    /**
     * 
     * @return remark 
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 
     * @param remark 
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * 创建人
     * @return creator 创建人
     */
    public String getCreator() {
        return creator;
    }

    /**
     * 创建人
     * @param creator 创建人
     */
    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    /**
     * 创建时间
     * @return create_time 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 创建时间
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 最后修改时间
     * @return modifier 最后修改时间
     */
    public String getModifier() {
        return modifier;
    }

    /**
     * 最后修改时间
     * @param modifier 最后修改时间
     */
    public void setModifier(String modifier) {
        this.modifier = modifier == null ? null : modifier.trim();
    }

    /**
     * 最后修改用户
     * @return modify_time 最后修改用户
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * 最后修改用户
     * @param modifyTime 最后修改用户
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}