package com.rxh.pojo.sys;

import java.io.Serializable;
import java.util.Date;

public class SysRecentTradeSituation implements Serializable {
     // 主键
    private Long id;

     // 成功笔数
    private Integer successNum;

     // 失败笔数
    private Integer failNum;

     // 交易时间
    private Date tradeTime;

    /**
     * 主键
     * @return id 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 主键
     * @param id 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 成功笔数
     * @return success_num 成功笔数
     */
    public Integer getSuccessNum() {
        return successNum;
    }

    /**
     * 成功笔数
     * @param successNum 成功笔数
     */
    public void setSuccessNum(Integer successNum) {
        this.successNum = successNum;
    }

    /**
     * 失败笔数
     * @return fail_num 失败笔数
     */
    public Integer getFailNum() {
        return failNum;
    }

    /**
     * 失败笔数
     * @param failNum 失败笔数
     */
    public void setFailNum(Integer failNum) {
        this.failNum = failNum;
    }

    /**
     * 交易时间
     * @return trade_time 交易时间
     */
    public Date getTradeTime() {
        return tradeTime;
    }

    /**
     * 交易时间
     * @param tradeTime 交易时间
     */
    public void setTradeTime(Date tradeTime) {
        this.tradeTime = tradeTime;
    }
}