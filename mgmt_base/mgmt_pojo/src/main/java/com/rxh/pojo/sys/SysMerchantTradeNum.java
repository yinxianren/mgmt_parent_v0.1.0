package com.rxh.pojo.sys;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class SysMerchantTradeNum implements Serializable {
     // 
    private Long id;

     // 
    private Integer merId;

     // 
    private BigDecimal successNum;

     // 
    private Date tradeTime;

    /**
     * 
     * @return id 
     */
    public Long getId() {
        return id;
    }

    /**
     * 
     * @param id 
     */
    public void setId(Long id) {
        this.id = id;
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
     * @return success_num 
     */
    public BigDecimal getSuccessNum() {
        return successNum;
    }

    /**
     * 
     * @param successNum 
     */
    public void setSuccessNum(BigDecimal successNum) {
        this.successNum = successNum;
    }

    /**
     * 
     * @return trade_time 
     */
    public Date getTradeTime() {
        return tradeTime;
    }

    /**
     * 
     * @param tradeTime 
     */
    public void setTradeTime(Date tradeTime) {
        this.tradeTime = tradeTime;
    }
}