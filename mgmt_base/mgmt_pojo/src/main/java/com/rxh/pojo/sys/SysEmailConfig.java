package com.rxh.pojo.sys;

import java.io.Serializable;
import java.util.Date;

public class SysEmailConfig implements Serializable {
     // 
    private String id;

     // 拥有者:0-总部 或者 代理商id
    private Integer refId;

     // 类型: 0-支付 1-客服
    private Short type;

     // 邮箱用户名
    private String username;

     // 邮箱密码
    private String password;

     // 发送者名称
    private String showName;

     // 发送者的主机号
    private String host;

     // 发送者的主机端口号
    private Short port;

     // 备注
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
     * 拥有者:0-总部 或者 代理商id
     * @return ref_id 拥有者:0-总部 或者 代理商id
     */
    public Integer getRefId() {
        return refId;
    }

    /**
     * 拥有者:0-总部 或者 代理商id
     * @param refId 拥有者:0-总部 或者 代理商id
     */
    public void setRefId(Integer refId) {
        this.refId = refId;
    }

    /**
     * 类型: 0-支付 1-客服
     * @return type 类型: 0-支付 1-客服
     */
    public Short getType() {
        return type;
    }

    /**
     * 类型: 0-支付 1-客服
     * @param type 类型: 0-支付 1-客服
     */
    public void setType(Short type) {
        this.type = type;
    }

    /**
     * 邮箱用户名
     * @return username 邮箱用户名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 邮箱用户名
     * @param username 邮箱用户名
     */
    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    /**
     * 邮箱密码
     * @return password 邮箱密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 邮箱密码
     * @param password 邮箱密码
     */
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    /**
     * 发送者名称
     * @return show_name 发送者名称
     */
    public String getShowName() {
        return showName;
    }

    /**
     * 发送者名称
     * @param showName 发送者名称
     */
    public void setShowName(String showName) {
        this.showName = showName == null ? null : showName.trim();
    }

    /**
     * 发送者的主机号
     * @return host 发送者的主机号
     */
    public String getHost() {
        return host;
    }

    /**
     * 发送者的主机号
     * @param host 发送者的主机号
     */
    public void setHost(String host) {
        this.host = host == null ? null : host.trim();
    }

    /**
     * 发送者的主机端口号
     * @return port 发送者的主机端口号
     */
    public Short getPort() {
        return port;
    }

    /**
     * 发送者的主机端口号
     * @param port 发送者的主机端口号
     */
    public void setPort(Short port) {
        this.port = port;
    }

    /**
     * 备注
     * @return remark 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 备注
     * @param remark 备注
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