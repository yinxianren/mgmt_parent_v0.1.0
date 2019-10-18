package com.rxh.pojo.merchant;

import java.io.Serializable;
import java.util.Date;

public class MerchantQuestion implements Serializable {
    // 编号:
    private String id;

    // 商户表主键
    private Integer merId;

    // 联系电话
    private String phone;

    // 联系邮箱
    private String email;

    // 1业务咨询2系统建议3订单处理
    private Short type;

    // 商户咨询时间
    private Date time;

    // 1待处理 2已处理
    private Short status;

    // 回复人员
    private String answerPeople;

    // 回复时间
    private Date answerTime;

    /**
     * 编号:
     * @return id 编号:
    */
    public String getId() {
        return id;
    }

    /**
     * 编号:
     * @param id 编号:
    */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * 商户表主键
     * @return mer_id 商户表主键
    */
    public Integer getMerId() {
        return merId;
    }

    /**
     * 商户表主键
     * @param merId 商户表主键
    */
    public void setMerId(Integer merId) {
        this.merId = merId;
    }

    /**
     * 联系电话
     * @return phone 联系电话
    */
    public String getPhone() {
        return phone;
    }

    /**
     * 联系电话
     * @param phone 联系电话
    */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /**
     * 联系邮箱
     * @return email 联系邮箱
    */
    public String getEmail() {
        return email;
    }

    /**
     * 联系邮箱
     * @param email 联系邮箱
    */
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    /**
     * 1业务咨询2系统建议3订单处理
     * @return type 1业务咨询2系统建议3订单处理
    */
    public Short getType() {
        return type;
    }

    /**
     * 1业务咨询2系统建议3订单处理
     * @param type 1业务咨询2系统建议3订单处理
    */
    public void setType(Short type) {
        this.type = type;
    }

    /**
     * 商户咨询时间
     * @return time 商户咨询时间
    */
    public Date getTime() {
        return time;
    }

    /**
     * 商户咨询时间
     * @param time 商户咨询时间
    */
    public void setTime(Date time) {
        this.time = time;
    }

    /**
     * 1待处理 2已处理
     * @return status 1待处理 2已处理
    */
    public Short getStatus() {
        return status;
    }

    /**
     * 1待处理 2已处理
     * @param status 1待处理 2已处理
    */
    public void setStatus(Short status) {
        this.status = status;
    }

    /**
     * 回复人员
     * @return answer_people 回复人员
    */
    public String getAnswerPeople() {
        return answerPeople;
    }

    /**
     * 回复人员
     * @param answerPeople 回复人员
    */
    public void setAnswerPeople(String answerPeople) {
        this.answerPeople = answerPeople == null ? null : answerPeople.trim();
    }

    /**
     * 回复时间
     * @return answer_time 回复时间
    */
    public Date getAnswerTime() {
        return answerTime;
    }

    /**
     * 回复时间
     * @param answerTime 回复时间
    */
    public void setAnswerTime(Date answerTime) {
        this.answerTime = answerTime;
    }
}