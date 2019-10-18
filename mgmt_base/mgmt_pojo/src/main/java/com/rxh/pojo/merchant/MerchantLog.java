package com.rxh.pojo.merchant;

import java.io.Serializable;
import java.util.Date;

public class MerchantLog implements Serializable {
    /**
     *
     */
    private Long id;

    /**
     * 商户ID
     */
    private Integer merId;

    /**
     * 常量组log提供
     */
    private Short type;

    /**
     * 操作者
     */
    private String operator;

    /**
     * 操作时间
     */
    private Date startTime;

    /**
     * 方法用时（毫秒）
     */
    private Long spendTime;

    /**
     * 操作IP
     */
    private String requestIp;

    /**
     * 请求uri
     */
    private String requestUri;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 方法描述，如是异常则是异常描述
     */
    private String methodDescription;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 商户ID
     *
     * @return mer_id 商户ID
     */
    public Integer getMerId() {
        return merId;
    }

    /**
     * 商户ID
     *
     * @param merId 商户ID
     */
    public void setMerId(Integer merId) {
        this.merId = merId;
    }

    /**
     * 常量组log提供
     *
     * @return type 常量组log提供
     */
    public Short getType() {
        return type;
    }

    /**
     * 常量组log提供
     *
     * @param type 常量组log提供
     */
    public void setType(Short type) {
        this.type = type;
    }

    /**
     * 操作者
     *
     * @return operator 操作者
     */
    public String getOperator() {
        return operator;
    }

    /**
     * 操作者
     *
     * @param operator 操作者
     */
    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    /**
     * 操作时间
     *
     * @return start_time 操作时间
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * 操作时间
     *
     * @param startTime 操作时间
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * 方法用时（毫秒）
     *
     * @return spend_time 方法用时（毫秒）
     */
    public Long getSpendTime() {
        return spendTime;
    }

    /**
     * 方法用时（毫秒）
     *
     * @param spendTime 方法用时（毫秒）
     */
    public void setSpendTime(Long spendTime) {
        this.spendTime = spendTime;
    }

    /**
     * 操作IP
     *
     * @return request_ip 操作IP
     */
    public String getRequestIp() {
        return requestIp;
    }

    /**
     * 操作IP
     *
     * @param requestIp 操作IP
     */
    public void setRequestIp(String requestIp) {
        this.requestIp = requestIp == null ? null : requestIp.trim();
    }

    /**
     * 请求uri
     *
     * @return request_uri 请求uri
     */
    public String getRequestUri() {
        return requestUri;
    }

    /**
     * 请求uri
     *
     * @param requestUri 请求uri
     */
    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri == null ? null : requestUri.trim();
    }

    /**
     * 方法名称
     *
     * @return method_name 方法名称
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * 方法名称
     *
     * @param methodName 方法名称
     */
    public void setMethodName(String methodName) {
        this.methodName = methodName == null ? null : methodName.trim();
    }

    /**
     * 方法描述，如是异常则是异常描述
     *
     * @return method_description 方法描述，如是异常则是异常描述
     */
    public String getMethodDescription() {
        return methodDescription;
    }

    /**
     * 方法描述，如是异常则是异常描述
     *
     * @param methodDescription 方法描述，如是异常则是异常描述
     */
    public void setMethodDescription(String methodDescription) {
        this.methodDescription = methodDescription == null ? null : methodDescription.trim();
    }
}