package com.rxh.pojo.sys;

import java.io.Serializable;
import java.util.Date;

public class SysNews implements Serializable {
     // 
    private String id;

     // 常量组news提供
    private String type;

     // 
    private String title;

     // 标题和地址二选一
    private String url;

     // 
    private String author;

     // 
    private Date publishTime;

     // 
    private String remark;

     // 最后修改时间
    private String modifier;

     // 最后修改用户
    private Date modifyTime;

     // 
    private String content;

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
     * 常量组news提供
     * @return type 常量组news提供
     */
    public String getType() {
        return type;
    }

    /**
     * 常量组news提供
     * @param type 常量组news提供
     */
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    /**
     * 
     * @return title 
     */
    public String getTitle() {
        return title;
    }

    /**
     * 
     * @param title 
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * 标题和地址二选一
     * @return url 标题和地址二选一
     */
    public String getUrl() {
        return url;
    }

    /**
     * 标题和地址二选一
     * @param url 标题和地址二选一
     */
    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    /**
     * 
     * @return author 
     */
    public String getAuthor() {
        return author;
    }

    /**
     * 
     * @param author 
     */
    public void setAuthor(String author) {
        this.author = author == null ? null : author.trim();
    }

    /**
     * 
     * @return publish_time 
     */
    public Date getPublishTime() {
        return publishTime;
    }

    /**
     * 
     * @param publishTime 
     */
    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
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

    /**
     * 
     * @return content 
     */
    public String getContent() {
        return content;
    }

    /**
     * 
     * @param content 
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}