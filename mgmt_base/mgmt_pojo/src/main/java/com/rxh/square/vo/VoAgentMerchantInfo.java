package com.rxh.square.vo;

import com.rxh.square.pojo.AgentMerchantInfo;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: hul
 * Date: 2018/5/19
 * Time: 14:31
 * Project: Management
 * Package: com.rxh.squate.vo
 */
public class VoAgentMerchantInfo extends AgentMerchantInfo {

    private String id;
    //代理商户号
    private String agentMerchantId;

    // 单笔手续费
    private Integer singleFee;

    // 手续费率
    private BigDecimal rateFee;

    // 结算周期
    private String settlecycle;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getAgentMerchantId() {
        return agentMerchantId;
    }

    @Override
    public void setAgentMerchantId(String agentMerchantId) {
        this.agentMerchantId = agentMerchantId;
    }

    public Integer getSingleFee() {
        return singleFee;
    }

    public void setSingleFee(Integer singleFee) {
        this.singleFee = singleFee;
    }

    public BigDecimal getRateFee() {
        return rateFee;
    }

    public void setRateFee(BigDecimal rateFee) {
        this.rateFee = rateFee;
    }

    public String getSettlecycle() {
        return settlecycle;
    }

    public void setSettlecycle(String settlecycle) {
        this.settlecycle = settlecycle;
    }
}
