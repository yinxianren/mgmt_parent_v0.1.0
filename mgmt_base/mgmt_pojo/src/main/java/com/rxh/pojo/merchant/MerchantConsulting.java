package com.rxh.pojo.merchant;

import java.io.Serializable;
import java.util.Date;

public class MerchantConsulting implements Serializable {
     // 
    private String id;

     // 邮箱
    private String email;

     // 手机
    private String phone;

     // 咨询内容
    private String content;

     // 咨询时间
    private Date time;

     // 回复
    private String answer;

     // 回复人
    private String answerPeople;

     // 修改人
    private Date answerTime;

     // 最后修改时间
    private String modifier;

     // 最后修改用户
    private Date modifyTime;

    /**
     * 
     * @return id 
     */
    public String getId() {
        return id;
    }

    /**
     * 
     * @param id 
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * 邮箱
     * @return email 邮箱
     */
    public String getEmail() {
        return email;
    }

    /**
     * 邮箱
     * @param email 邮箱
     */
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    /**
     * 手机
     * @return phone 手机
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 手机
     * @param phone 手机
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /**
     * 咨询内容
     * @return content 咨询内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 咨询内容
     * @param content 咨询内容
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    /**
     * 咨询时间
     * @return time 咨询时间
     */
    public Date getTime() {
        return time;
    }

    /**
     * 咨询时间
     * @param time 咨询时间
     */
    public void setTime(Date time) {
        this.time = time;
    }

    /**
     * 回复
     * @return answer 回复
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * 回复
     * @param answer 回复
     */
    public void setAnswer(String answer) {
        this.answer = answer == null ? null : answer.trim();
    }

    /**
     * 回复人
     * @return answer_people 回复人
     */
    public String getAnswerPeople() {
        return answerPeople;
    }

    /**
     * 回复人
     * @param answerPeople 回复人
     */
    public void setAnswerPeople(String answerPeople) {
        this.answerPeople = answerPeople == null ? null : answerPeople.trim();
    }

    /**
     * 修改人
     * @return answer_time 修改人
     */
    public Date getAnswerTime() {
        return answerTime;
    }

    /**
     * 修改人
     * @param answerTime 修改人
     */
    public void setAnswerTime(Date answerTime) {
        this.answerTime = answerTime;
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