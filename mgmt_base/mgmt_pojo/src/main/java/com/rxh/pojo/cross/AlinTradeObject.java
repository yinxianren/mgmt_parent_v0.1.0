package com.rxh.pojo.cross;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 通联收银宝 异步返回对象
 */
public class AlinTradeObject implements Serializable {
    private String appid; //第三方appid
    private String outtrxid; //第三方app交易号
    private Short trxcode; //交易类型
    private String trxid; //收银宝平台流水号
    private BigDecimal trxamt; //交易金额(付款类交易指到账金额)
    private BigDecimal initamt; //初始金额(付款类交易有效)
    private BigDecimal fee; //手续费
    private String trxdate; //交易请求日期
    private String paytime; //交易完成时间
    private String trxstatus; //交易状态
    private String cusid; //商户号
    private String trxreserved; //交易备注
    private String cusorderid; //商户订单号
    private String acct; //支付人帐号
    private String sign; //签名信息

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getOuttrxid() {
        return outtrxid;
    }

    public void setOuttrxid(String outtrxid) {
        this.outtrxid = outtrxid;
    }

    public Short getTrxcode() {
        return trxcode;
    }

    public void setTrxcode(Short trxcode) {
        this.trxcode = trxcode;
    }

    public String getTrxid() {
        return trxid;
    }

    public void setTrxid(String trxid) {
        this.trxid = trxid;
    }

    public BigDecimal getTrxamt() {
        return trxamt;
    }

    public void setTrxamt(BigDecimal trxamt) {
        this.trxamt = trxamt;
    }

    public BigDecimal getInitamt() {
        return initamt;
    }

    public void setInitamt(BigDecimal initamt) {
        this.initamt = initamt;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getTrxdate() {
        return trxdate;
    }

    public void setTrxdate(String trxdate) {
        this.trxdate = trxdate;
    }

    public String getPaytime() {
        return paytime;
    }

    public void setPaytime(String paytime) {
        this.paytime = paytime;
    }

    public String getTrxstatus() {
        return trxstatus;
    }

    public void setTrxstatus(String trxstatus) {
        this.trxstatus = trxstatus;
    }

    public String getCusid() {
        return cusid;
    }

    public void setCusid(String cusid) {
        this.cusid = cusid;
    }

    public String getTrxreserved() {
        return trxreserved;
    }

    public void setTrxreserved(String trxreserved) {
        this.trxreserved = trxreserved;
    }

    public String getCusorderid() {
        return cusorderid;
    }

    public void setCusorderid(String cusorderid) {
        this.cusorderid = cusorderid;
    }

    public String getAcct() {
        return acct;
    }

    public void setAcct(String acct) {
        this.acct = acct;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
