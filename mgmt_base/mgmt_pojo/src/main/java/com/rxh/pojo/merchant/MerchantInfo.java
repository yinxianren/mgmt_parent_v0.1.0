package com.rxh.pojo.merchant;

import java.io.Serializable;
import java.util.Date;

public class MerchantInfo implements Serializable {
    // 
    private Integer id;

    // 商户名称
    private String name;

    // 商户类型 0：公司 1：个人
    private Short category;

    // 省份
    private Integer province;

    // 城市
    private Integer city;

    // 区域
    private Integer area;

    // 地址
    private String address;

    // 联系人
    private String contacts;

    // 联系电话
    private String phone;

    // 联系邮箱
    private String email;

    // 联系传真
    private String fax;

    // 联系qq
    private Long qq;

    // 0:网站 1:宣传 2:业务员
    private Short regType;

    // 说明
    private String remark;

    // 创建人
    private String creator;

    // 创建时间
    private Date createTime;

    // 最后修改时间
    private String modifier;

    // 最后修改用户
    private Date modifyTime;

    // 公司证件地址
    private String certificate;

    // 
    private String salesman;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 商户名称
     *
     * @return name 商户名称
     */
    public String getName() {
        return name;
    }

    /**
     * 商户名称
     *
     * @param name 商户名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 商户类型 0：公司 1：个人
     *
     * @return category 商户类型 0：公司 1：个人
     */
    public Short getCategory() {
        return category;
    }

    /**
     * 商户类型 0：公司 1：个人
     *
     * @param category 商户类型 0：公司 1：个人
     */
    public void setCategory(Short category) {
        this.category = category;
    }

    /**
     * 省份
     *
     * @return province 省份
     */
    public Integer getProvince() {
        return province;
    }

    /**
     * 省份
     *
     * @param province 省份
     */
    public void setProvince(Integer province) {
        this.province = province;
    }

    /**
     * 城市
     *
     * @return city 城市
     */
    public Integer getCity() {
        return city;
    }

    /**
     * 城市
     *
     * @param city 城市
     */
    public void setCity(Integer city) {
        this.city = city;
    }

    /**
     * 区域
     *
     * @return area 区域
     */
    public Integer getArea() {
        return area;
    }

    /**
     * 区域
     *
     * @param area 区域
     */
    public void setArea(Integer area) {
        this.area = area;
    }

    /**
     * 地址
     *
     * @return address 地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 地址
     *
     * @param address 地址
     */
    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    /**
     * 联系人
     *
     * @return contacts 联系人
     */
    public String getContacts() {
        return contacts;
    }

    /**
     * 联系人
     *
     * @param contacts 联系人
     */
    public void setContacts(String contacts) {
        this.contacts = contacts == null ? null : contacts.trim();
    }

    /**
     * 联系电话
     *
     * @return phone 联系电话
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 联系电话
     *
     * @param phone 联系电话
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /**
     * 联系邮箱
     *
     * @return email 联系邮箱
     */
    public String getEmail() {
        return email;
    }

    /**
     * 联系邮箱
     *
     * @param email 联系邮箱
     */
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    /**
     * 联系传真
     *
     * @return fax 联系传真
     */
    public String getFax() {
        return fax;
    }

    /**
     * 联系传真
     *
     * @param fax 联系传真
     */
    public void setFax(String fax) {
        this.fax = fax == null ? null : fax.trim();
    }

    /**
     * 联系qq
     *
     * @return qq 联系qq
     */
    public Long getQq() {
        return qq;
    }

    /**
     * 联系qq
     *
     * @param qq 联系qq
     */
    public void setQq(Long qq) {
        this.qq = qq;
    }

    /**
     * 0:网站 1:宣传 2:业务员
     *
     * @return reg_type 0:网站 1:宣传 2:业务员
     */
    public Short getRegType() {
        return regType;
    }

    /**
     * 0:网站 1:宣传 2:业务员
     *
     * @param regType 0:网站 1:宣传 2:业务员
     */
    public void setRegType(Short regType) {
        this.regType = regType;
    }

    /**
     * 说明
     *
     * @return remark 说明
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 说明
     *
     * @param remark 说明
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * 创建人
     *
     * @return creator 创建人
     */
    public String getCreator() {
        return creator;
    }

    /**
     * 创建人
     *
     * @param creator 创建人
     */
    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    /**
     * 创建时间
     *
     * @return create_time 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 最后修改时间
     *
     * @return modifier 最后修改时间
     */
    public String getModifier() {
        return modifier;
    }

    /**
     * 最后修改时间
     *
     * @param modifier 最后修改时间
     */
    public void setModifier(String modifier) {
        this.modifier = modifier == null ? null : modifier.trim();
    }

    /**
     * 最后修改用户
     *
     * @return modify_time 最后修改用户
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * 最后修改用户
     *
     * @param modifyTime 最后修改用户
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * 公司证件地址
     *
     * @return certificate 公司证件地址
     */
    public String getCertificate() {
        return certificate;
    }

    /**
     * 公司证件地址
     *
     * @param certificate 公司证件地址
     */
    public void setCertificate(String certificate) {
        this.certificate = certificate == null ? null : certificate.trim();
    }

    /**
     * @return salesman
     */
    public String getSalesman() {
        return salesman;
    }

    /**
     * @param salesman
     */
    public void setSalesman(String salesman) {
        this.salesman = salesman == null ? null : salesman.trim();
    }
}