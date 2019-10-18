package com.rxh.pojo.risk;

import java.io.Serializable;
import java.util.Date;

public class RiskBusiness implements Serializable {
     // 
    private Long id;

     // 
    private Long startNo;

     // 
    private Long endNo;

     // 常量组PayMode子组提供
    private String payType;

     // 
    private String remark;

     // 创建人
    private String creator;

     // 创建时间
    private Date createTime;

     // 最后修改时间
    private String modifier;

     // 最后修改用户
    private Date modifyTime;

    /**
     * 
     * @return id 
     */
    public Long getId() {
        return id;
    }

    /**
     * 
     * @param id 
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 
     * @return start_no 
     */
    public Long getStartNo() {
        return startNo;
    }

    /**
     * 
     * @param startNo 
     */
    public void setStartNo(Long startNo) {
        this.startNo = startNo;
    }

    /**
     * 
     * @return end_no 
     */
    public Long getEndNo() {
        return endNo;
    }

    /**
     * 
     * @param endNo 
     */
    public void setEndNo(Long endNo) {
        this.endNo = endNo;
    }

    /**
     * 常量组PayMode子组提供
     * @return pay_type 常量组PayMode子组提供
     */
    public String getPayType() {
        return payType;
    }

    /**
     * 常量组PayMode子组提供
     * @param payType 常量组PayMode子组提供
     */
    public void setPayType(String payType) {
        this.payType = payType == null ? null : payType.trim();
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

    /**
     * 创建人
     * @return creator 创建人
     */
    public String getCreator() {
        return creator;
    }

    /**
     * 创建人
     * @param creator 创建人
     */
    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    /**
     * 创建时间
     * @return create_time 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 创建时间
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 最后修改时间
     * @return modifier 最后修改时间
     */
    public String getModifier() {
        return modifier;
    }

    /**
     * 最后修改时间
     * @param modifier 最后修改时间
     */
    public void setModifier(String modifier) {
        this.modifier = modifier == null ? null : modifier.trim();
    }

    /**
     * 最后修改用户
     * @return modify_time 最后修改用户
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * 最后修改用户
     * @param modifyTime 最后修改用户
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}