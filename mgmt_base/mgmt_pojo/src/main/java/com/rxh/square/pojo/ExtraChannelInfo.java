package com.rxh.square.pojo;

import java.io.Serializable;
import java.util.Date;

public class ExtraChannelInfo implements Serializable {
    private String id;

    private String extraChannelId;

    private String extraChannelName;

    private String type;

    private String url;

    private String data;
//
//    private String inChannelId;
//
//    private String outChannelId;

    private Date crateTime;

    private String operator;
    private String organizationId;

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getExtraChannelId() {
        return extraChannelId;
    }

    public void setExtraChannelId(String extraChannelId) {
        this.extraChannelId = extraChannelId == null ? null : extraChannelId.trim();
    }

    public String getExtraChannelName() {
        return extraChannelName;
    }

    public void setExtraChannelName(String extraChannelName) {
        this.extraChannelName = extraChannelName == null ? null : extraChannelName.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data == null ? null : data.trim();
    }
//
//    public String getInChannelId() {
//        return inChannelId;
//    }
//
//    public void setInChannelId(String inChannelId) {
//        this.inChannelId = inChannelId == null ? null : inChannelId.trim();
//    }
//
//    public String getOutChannelId() {
//        return outChannelId;
//    }
//
//    public void setOutChannelId(String outChannelId) {
//        this.outChannelId = outChannelId == null ? null : outChannelId.trim();
//    }

    public Date getCrateTime() {
        return crateTime;
    }

    public void setCrateTime(Date crateTime) {
        this.crateTime = crateTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }
}