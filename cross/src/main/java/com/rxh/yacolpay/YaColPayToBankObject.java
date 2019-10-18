package com.rxh.yacolpay;

import java.io.Serializable;
import java.math.BigDecimal;

public class YaColPayToBankObject  implements Serializable {


    private String notify_type;
    private String notify_id;
    private String _input_charset;
    private String notify_time;
    private String sign;
    private String sign_type;
    private String version;
    private String memo;
    private String error_code;
    private String error_message;
    private String outer_trade_no; //商户订单号
    private String inner_trade_no;	//出款交易凭证号
    private BigDecimal withdraw_amount ;//金额
    private  BigDecimal platform_fee;//平台方手续费
    private  String withdraw_status; //交易状态

    public String getNotify_type() {
        return notify_type;
    }

    public void setNotify_type(String notify_type) {
        this.notify_type = notify_type;
    }

    public String getNotify_id() {
        return notify_id;
    }

    public void setNotify_id(String notify_id) {
        this.notify_id = notify_id;
    }

    public String get_input_charset() {
        return _input_charset;
    }

    public void set_input_charset(String _input_charset) {
        this._input_charset = _input_charset;
    }

    public String getNotify_time() {
        return notify_time;
    }

    public void setNotify_time(String notify_time) {
        this.notify_time = notify_time;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }

    public String getOuter_trade_no() {
        return outer_trade_no;
    }

    public void setOuter_trade_no(String outer_trade_no) {
        this.outer_trade_no = outer_trade_no;
    }

    public String getInner_trade_no() {
        return inner_trade_no;
    }

    public void setInner_trade_no(String inner_trade_no) {
        this.inner_trade_no = inner_trade_no;
    }

    public BigDecimal getWithdraw_amount() {
        return withdraw_amount;
    }

    public void setWithdraw_amount(BigDecimal withdraw_amount) {
        this.withdraw_amount = withdraw_amount;
    }

    public BigDecimal getPlatform_fee() {
        return platform_fee;
    }

    public void setPlatform_fee(BigDecimal platform_fee) {
        this.platform_fee = platform_fee;
    }

    public String getWithdraw_status() {
        return withdraw_status;
    }

    public void setWithdraw_status(String withdraw_status) {
        this.withdraw_status = withdraw_status;
    }
}
