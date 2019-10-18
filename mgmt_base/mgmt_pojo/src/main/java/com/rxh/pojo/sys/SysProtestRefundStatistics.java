package com.rxh.pojo.sys;

import java.io.Serializable;
import java.util.Date;

public class SysProtestRefundStatistics implements Serializable {
     // 
    private String id;

     // 
    private Integer merId;

     // 
    private Integer refundNum;

     // 
    private Integer protestNum;

     // 
    private Date applicationTime;

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
     * 
     * @return mer_id 
     */
    public Integer getMerId() {
        return merId;
    }

    /**
     * 
     * @param merId 
     */
    public void setMerId(Integer merId) {
        this.merId = merId;
    }

    /**
     * 
     * @return refund_num 
     */
    public Integer getRefundNum() {
        return refundNum;
    }

    /**
     * 
     * @param refundNum 
     */
    public void setRefundNum(Integer refundNum) {
        this.refundNum = refundNum;
    }

    /**
     * 
     * @return protest_num 
     */
    public Integer getProtestNum() {
        return protestNum;
    }

    /**
     * 
     * @param protestNum 
     */
    public void setProtestNum(Integer protestNum) {
        this.protestNum = protestNum;
    }

    /**
     * 
     * @return application_time 
     */
    public Date getApplicationTime() {
        return applicationTime;
    }

    /**
     * 
     * @param applicationTime 
     */
    public void setApplicationTime(Date applicationTime) {
        this.applicationTime = applicationTime;
    }
}