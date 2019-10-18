package com.rxh.vo;

import com.rxh.pojo.sys.SysLog;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: 陈俊雄
 * Date: 2018/5/14
 * Time: 14:32
 * Project: Management
 * Package: com.rxh.vo
 */
public class VoSysLog extends SysLog {
    private Date endTime;

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
