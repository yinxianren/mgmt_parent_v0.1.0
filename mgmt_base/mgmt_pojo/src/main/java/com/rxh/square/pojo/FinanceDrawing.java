package com.rxh.square.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 主要针对商户和代理商线下提现的记录,只走系统内部流程,不走对外接口
 * 
 * @author wcyong
 * 
 * @date 2019-05-27
 */
public class FinanceDrawing implements Serializable {
    private String id;

    private String customerId;

    private BigDecimal drawingAmount;

    private BigDecimal transferAmount;

    private Integer status;

    private Date applicationTime;

    private String applicant;

    private Date transferTime;

    private String transferer;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId == null ? null : customerId.trim();
    }

    public BigDecimal getDrawingAmount() {
        return drawingAmount;
    }

    public void setDrawingAmount(BigDecimal drawingAmount) {
        this.drawingAmount = drawingAmount;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getApplicationTime() {
        return applicationTime;
    }

    public void setApplicationTime(Date applicationTime) {
        this.applicationTime = applicationTime;
    }

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant == null ? null : applicant.trim();
    }

    public Date getTransferTime() {
        return transferTime;
    }

    public void setTransferTime(Date transferTime) {
        this.transferTime = transferTime;
    }

    public String getTransferer() {
        return transferer;
    }

    public void setTransferer(String transferer) {
        this.transferer = transferer == null ? null : transferer.trim();
    }
}