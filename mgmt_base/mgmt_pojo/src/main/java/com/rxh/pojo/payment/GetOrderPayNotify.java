package com.rxh.pojo.payment;

import java.io.Serializable;

public class GetOrderPayNotify implements Serializable {


    private String returnJson;
    private String notifyUrl;
    // 通知周期
    private Long notifyCycle;
    // 通知次数
    private Integer notifyTimes;


    public String getReturnJson() {
        return returnJson;
    }

    public void setReturnJson(String returnJson) {
        this.returnJson = returnJson;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public Long getNotifyCycle() {
        return notifyCycle;
    }

    public void setNotifyCycle(Long notifyCycle) {
        this.notifyCycle = notifyCycle;
    }

    public Integer getNotifyTimes() {
        return notifyTimes;
    }

    public void setNotifyTimes(Integer notifyTimes) {
        this.notifyTimes = notifyTimes;
    }
}
