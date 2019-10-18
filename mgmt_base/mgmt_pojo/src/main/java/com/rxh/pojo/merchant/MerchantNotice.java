package com.rxh.pojo.merchant;

import java.io.Serializable;
import java.util.Date;

public class MerchantNotice implements Serializable {
     // 
    private Integer id;

     // 公告标题
    private String noticeTitle;

     // 0：系统管理员  1：商户管理员
    private String noticeSelect;

     // 创建人
    private String creator;

     // 创建时间
    private Date createTime;

     // 最后操作人
    private String modifier;

     // 最后修改时间
    private Date modifyTime;

     // 新闻内容
    private String noticeContent;

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
     * 公告标题
     * @return notice_title 公告标题
     */
    public String getNoticeTitle() {
        return noticeTitle;
    }

    /**
     * 公告标题
     * @param noticeTitle 公告标题
     */
    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle == null ? null : noticeTitle.trim();
    }

    /**
     * 0：系统管理员  1：商户管理员
     * @return notice_select 0：系统管理员  1：商户管理员
     */
    public String getNoticeSelect() {
        return noticeSelect;
    }

    /**
     * 0：系统管理员  1：商户管理员
     * @param noticeSelect 0：系统管理员  1：商户管理员
     */
    public void setNoticeSelect(String noticeSelect) {
        this.noticeSelect = noticeSelect == null ? null : noticeSelect.trim();
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

    /**
     * 新闻内容
     * @return notice_content 新闻内容
     */
    public String getNoticeContent() {
        return noticeContent;
    }

    /**
     * 新闻内容
     * @param noticeContent 新闻内容
     */
    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent == null ? null : noticeContent.trim();
    }
}