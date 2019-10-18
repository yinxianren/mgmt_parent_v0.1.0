package com.rxh.pojo.sys;

import java.io.Serializable;
import java.util.Date;

public class SysEmail implements Serializable {
     // 
    private String id;

     // 邮箱配置表id
    private String writer;

     // 收件人
    private String toAddress;

     // 标题
    private String subject;

     // 格式: 0-文本 1-HTML
    private Short scheme;

     // 类型: 0-实际 1-模板Json
    private Short type;

     // 内容
    private String content;

     // 附件
    private String attachment;

     // 发送类型: 0-即时 1-定时
    private Short sendType;

     // 发送时间
    private Date sendTime;

     // 状态: 0-已发 1-未发
    private Short status;

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
     * 邮箱配置表id
     * @return writer 邮箱配置表id
     */
    public String getWriter() {
        return writer;
    }

    /**
     * 邮箱配置表id
     * @param writer 邮箱配置表id
     */
    public void setWriter(String writer) {
        this.writer = writer == null ? null : writer.trim();
    }

    /**
     * 收件人
     * @return to_address 收件人
     */
    public String getToAddress() {
        return toAddress;
    }

    /**
     * 收件人
     * @param toAddress 收件人
     */
    public void setToAddress(String toAddress) {
        this.toAddress = toAddress == null ? null : toAddress.trim();
    }

    /**
     * 标题
     * @return subject 标题
     */
    public String getSubject() {
        return subject;
    }

    /**
     * 标题
     * @param subject 标题
     */
    public void setSubject(String subject) {
        this.subject = subject == null ? null : subject.trim();
    }

    /**
     * 格式: 0-文本 1-HTML
     * @return scheme 格式: 0-文本 1-HTML
     */
    public Short getScheme() {
        return scheme;
    }

    /**
     * 格式: 0-文本 1-HTML
     * @param scheme 格式: 0-文本 1-HTML
     */
    public void setScheme(Short scheme) {
        this.scheme = scheme;
    }

    /**
     * 类型: 0-实际 1-模板Json
     * @return type 类型: 0-实际 1-模板Json
     */
    public Short getType() {
        return type;
    }

    /**
     * 类型: 0-实际 1-模板Json
     * @param type 类型: 0-实际 1-模板Json
     */
    public void setType(Short type) {
        this.type = type;
    }

    /**
     * 内容
     * @return content 内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 内容
     * @param content 内容
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    /**
     * 附件
     * @return attachment 附件
     */
    public String getAttachment() {
        return attachment;
    }

    /**
     * 附件
     * @param attachment 附件
     */
    public void setAttachment(String attachment) {
        this.attachment = attachment == null ? null : attachment.trim();
    }

    /**
     * 发送类型: 0-即时 1-定时
     * @return send_type 发送类型: 0-即时 1-定时
     */
    public Short getSendType() {
        return sendType;
    }

    /**
     * 发送类型: 0-即时 1-定时
     * @param sendType 发送类型: 0-即时 1-定时
     */
    public void setSendType(Short sendType) {
        this.sendType = sendType;
    }

    /**
     * 发送时间
     * @return send_time 发送时间
     */
    public Date getSendTime() {
        return sendTime;
    }

    /**
     * 发送时间
     * @param sendTime 发送时间
     */
    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    /**
     * 状态: 0-已发 1-未发
     * @return status 状态: 0-已发 1-未发
     */
    public Short getStatus() {
        return status;
    }

    /**
     * 状态: 0-已发 1-未发
     * @param status 状态: 0-已发 1-未发
     */
    public void setStatus(Short status) {
        this.status = status;
    }
}