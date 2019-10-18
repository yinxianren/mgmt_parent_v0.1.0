package com.rxh.pojo.sys;

import java.io.Serializable;
import java.util.Date;

public class SysMerchantTradeTotalNum implements Serializable {
     // 
    private Long id;

     // 
    private Integer successTotalNum;

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
     * @return success_total_num 
     */
    public Integer getSuccessTotalNum() {
        return successTotalNum;
    }

    /**
     * 
     * @param successTotalNum 
     */
    public void setSuccessTotalNum(Integer successTotalNum) {
        this.successTotalNum = successTotalNum;
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