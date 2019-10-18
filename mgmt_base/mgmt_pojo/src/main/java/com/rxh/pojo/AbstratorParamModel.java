package com.rxh.pojo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *  公共参数字段
 * @author 詹光活
 * @date 20190628
 */
public class AbstratorParamModel implements Serializable {

    // 返回地址
    protected String returnUrl;
    // 通知地址
    protected String noticeUrl;
    // 签名字符串
    protected String md5Info;
    //  签名字符串
    private String  signMsg;
    // 商户号
    protected String merId;
   //用用终端号
    protected String terminalMerId;
    // 商户订单号
    protected String merOrderId;
    // 代付金额
    private BigDecimal amount;

    public String getSignMsg() {
        return signMsg;
    }

    public void setSignMsg(String signMsg) {
        this.signMsg = signMsg;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount != null ? amount : null;
    }

    public String getMerOrderId() {
        return merOrderId;
    }

    public void setMerOrderId(String merOrderId) {
        this.merOrderId = merOrderId;
    }

    public String getMerId() {
        return merId;
    }

    public void setMerId(String merId) {
        this.merId = merId;
    }

    public String getTerminalMerId() {
        return terminalMerId;
    }

    public void setTerminalMerId(String terminalMerId) {
        this.terminalMerId = terminalMerId;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getNoticeUrl() {
        return noticeUrl;
    }

    public void setNoticeUrl(String noticeUrl) {
        this.noticeUrl = noticeUrl;
    }

    public String getMd5Info() {
        return md5Info;
    }

    public void setMd5Info(String md5Info) {
        this.md5Info = md5Info;
    }



}
