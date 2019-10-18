package com.rxh.pojo.risk;

import java.io.Serializable;
import java.math.BigDecimal;

public class RiskMaxmindOutput implements Serializable {
     // 持卡人表主键
    private String cardholderId;

     // 针对客户(风控分数)
    private BigDecimal score;

     // 针对客户
    private String err;

     // 
    private String remark;

    /**
     * 持卡人表主键
     * @return cardholder_id 持卡人表主键
     */
    public String getCardholderId() {
        return cardholderId;
    }

    /**
     * 持卡人表主键
     * @param cardholderId 持卡人表主键
     */
    public void setCardholderId(String cardholderId) {
        this.cardholderId = cardholderId == null ? null : cardholderId.trim();
    }

    /**
     * 针对客户(风控分数)
     * @return score 针对客户(风控分数)
     */
    public BigDecimal getScore() {
        return score;
    }

    /**
     * 针对客户(风控分数)
     * @param score 针对客户(风控分数)
     */
    public void setScore(BigDecimal score) {
        this.score = score;
    }

    /**
     * 针对客户
     * @return err 针对客户
     */
    public String getErr() {
        return err;
    }

    /**
     * 针对客户
     * @param err 针对客户
     */
    public void setErr(String err) {
        this.err = err == null ? null : err.trim();
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
}