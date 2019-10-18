package com.rxh.yacolpay;

import java.io.Serializable;

/**
 * 雅酷快捷支付异步通知返回对象
 */
public class YaColTradeObject implements Serializable {

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
    private String outer_trade_no;
    private String inner_trade_no;	//内部交易凭证号
    private String trade_status;	//交易状态
    private String trade_amount;//	交易金额;
    private String gmt_create;//	交易创建时间;
    private String gmt_payment;//	交易支付时间;
    private String gmt_close;//	交易关闭时间;
    private String pay_method;//	支付方式;

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

    public String getTrade_status() {
        return trade_status;
    }

    public void setTrade_status(String trade_status) {
        this.trade_status = trade_status;
    }

    public String getTrade_amount() {
        return trade_amount;
    }

    public void setTrade_amount(String trade_amount) {
        this.trade_amount = trade_amount;
    }

    public String getGmt_create() {
        return gmt_create;
    }

    public void setGmt_create(String gmt_create) {
        this.gmt_create = gmt_create;
    }

    public String getGmt_payment() {
        return gmt_payment;
    }

    public void setGmt_payment(String gmt_payment) {
        this.gmt_payment = gmt_payment;
    }

    public String getGmt_close() {
        return gmt_close;
    }

    public void setGmt_close(String gmt_close) {
        this.gmt_close = gmt_close;
    }

    public String getPay_method() {
        return pay_method;
    }

    public void setPay_method(String pay_method) {
        this.pay_method = pay_method;
    }

    @Override
    public String toString() {
        return "YaColTradeObject{" +
                "notify_type='" + notify_type + '\'' +
                ", notify_id='" + notify_id + '\'' +
                ", _input_charset='" + _input_charset + '\'' +
                ", notify_time='" + notify_time + '\'' +
                ", sign='" + sign + '\'' +
                ", sign_type='" + sign_type + '\'' +
                ", version='" + version + '\'' +
                ", memo='" + memo + '\'' +
                ", error_code='" + error_code + '\'' +
                ", error_message='" + error_message + '\'' +
                ", outer_trade_no='" + outer_trade_no + '\'' +
                ", inner_trade_no='" + inner_trade_no + '\'' +
                ", trade_status='" + trade_status + '\'' +
                ", trade_amount='" + trade_amount + '\'' +
                ", gmt_create='" + gmt_create + '\'' +
                ", gmt_payment='" + gmt_payment + '\'' +
                ", gmt_close='" + gmt_close + '\'' +
                ", pay_method='" + pay_method + '\'' +
                '}';
    }
}
