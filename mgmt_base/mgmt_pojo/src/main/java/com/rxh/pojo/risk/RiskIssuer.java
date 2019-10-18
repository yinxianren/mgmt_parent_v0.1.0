package com.rxh.pojo.risk;

import java.io.Serializable;
import java.util.Date;

public class RiskIssuer implements Serializable {
    private Long id;

     // 发卡行名字
    private String name;

     // 常量组country提供
    private String country;

     // 电话可含"-"等字符
    private String phone;

     //
    private String remark;

     // 创建人
    private String creator;

     // 创建时间
    private Date createTime;

     // 最后操作人
    private String modifier;

     // 最后修改时间
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
     * 发卡行名字
     * @return name 发卡行名字
     */
    public String getName() {
        return name;
    }

    /**
     * 发卡行名字
     * @param name 发卡行名字
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
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
     * 电话可含"-"等字符
     * @return phone 电话可含"-"等字符
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 电话可含"-"等字符
     * @param phone 电话可含"-"等字符
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
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
     * 最后操作人
     * @return modifier 最后操作人
     */
    public String getModifier() {
        return modifier;
    }

    /**
     * 最后操作人
     * @param modifier 最后操作人
     */
    public void setModifier(String modifier) {
        this.modifier = modifier == null ? null : modifier.trim();
    }

    /**
     * 最后修改时间
     * @return modify_time 最后修改时间
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * 最后修改时间
     * @param modifyTime 最后修改时间
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}