package com.rxh.pojo.sys;

import java.io.Serializable;

public class SysModelTime implements Serializable {
     // 
    private String modelName;

     // 
    private Long lastestTime;

    /**
     * 
     * @return model_name 
     */
    public String getModelName() {
        return modelName;
    }

    /**
     * 
     * @param modelName 
     */
    public void setModelName(String modelName) {
        this.modelName = modelName == null ? null : modelName.trim();
    }

    /**
     * 
     * @return lastest_time 
     */
    public Long getLastestTime() {
        return lastestTime;
    }

    /**
     * 
     * @param lastestTime 
     */
    public void setLastestTime(Long lastestTime) {
        this.lastestTime = lastestTime;
    }
}