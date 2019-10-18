package com.rxh.pojo.merchant;

import java.io.Serializable;
import java.util.Date;

public class MerchantPay implements Serializable {
     // 
    private Integer id;

     // 商户域名表主键/商户配置表主键
    private Integer refId;

     // 1:商户 2:域名
    private Short refType;

     // 支付邮箱
    private String email;

     // 常量组algorithm提供
    private Short algorithm;

     // 密钥,加密盐值
    private String secretKey;

     // 多选,常量组currency提供
    private String currency;

     // 多选,常量组PayMode提供
    private String payMode;

     // 0 是 1 否
    private Short realRate;

     // 0内嵌 1跳转 2都可
    private Short interType;

     // 常量组Status提供
    private Short status;

     // 最后修改时间
    private String modifier;

     // 最后修改用户
    private Date modifyTime;

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
     * 商户域名表主键/商户配置表主键
     * @return ref_id 商户域名表主键/商户配置表主键
     */
    public Integer getRefId() {
        return refId;
    }

    /**
     * 商户域名表主键/商户配置表主键
     * @param refId 商户域名表主键/商户配置表主键
     */
    public void setRefId(Integer refId) {
        this.refId = refId;
    }

    /**
     * 1:商户 2:域名
     * @return ref_type 1:商户 2:域名
     */
    public Short getRefType() {
        return refType;
    }

    /**
     * 1:商户 2:域名
     * @param refType 1:商户 2:域名
     */
    public void setRefType(Short refType) {
        this.refType = refType;
    }

    /**
     * 支付邮箱
     * @return email 支付邮箱
     */
    public String getEmail() {
        return email;
    }

    /**
     * 支付邮箱
     * @param email 支付邮箱
     */
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    /**
     * 常量组algorithm提供
     * @return algorithm 常量组algorithm提供
     */
    public Short getAlgorithm() {
        return algorithm;
    }

    /**
     * 常量组algorithm提供
     * @param algorithm 常量组algorithm提供
     */
    public void setAlgorithm(Short algorithm) {
        this.algorithm = algorithm;
    }

    /**
     * 密钥,加密盐值
     * @return secret_key 密钥,加密盐值
     */
    public String getSecretKey() {
        return secretKey;
    }

    /**
     * 密钥,加密盐值
     * @param secretKey 密钥,加密盐值
     */
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey == null ? null : secretKey.trim();
    }

    /**
     * 多选,常量组currency提供
     * @return currency 多选,常量组currency提供
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * 多选,常量组currency提供
     * @param currency 多选,常量组currency提供
     */
    public void setCurrency(String currency) {
        this.currency = currency == null ? null : currency.trim();
    }

    /**
     * 多选,常量组PayMode提供
     * @return pay_mode 多选,常量组PayMode提供
     */
    public String getPayMode() {
        return payMode;
    }

    /**
     * 多选,常量组PayMode提供
     * @param payMode 多选,常量组PayMode提供
     */
    public void setPayMode(String payMode) {
        this.payMode = payMode == null ? null : payMode.trim();
    }

    /**
     * 0 是 1 否
     * @return real_rate 0 是 1 否
     */
    public Short getRealRate() {
        return realRate;
    }

    /**
     * 0 是 1 否
     * @param realRate 0 是 1 否
     */
    public void setRealRate(Short realRate) {
        this.realRate = realRate;
    }

    /**
     * 0内嵌 1跳转 2都可
     * @return inter_type 0内嵌 1跳转 2都可
     */
    public Short getInterType() {
        return interType;
    }

    /**
     * 0内嵌 1跳转 2都可
     * @param interType 0内嵌 1跳转 2都可
     */
    public void setInterType(Short interType) {
        this.interType = interType;
    }

    /**
     * 常量组Status提供
     * @return status 常量组Status提供
     */
    public Short getStatus() {
        return status;
    }

    /**
     * 常量组Status提供
     * @param status 常量组Status提供
     */
    public void setStatus(Short status) {
        this.status = status;
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