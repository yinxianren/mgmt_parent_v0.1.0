package com.rxh.pojo.merchant;

import java.io.Serializable;
import java.util.Date;

public class MerchantAgentManagement implements Serializable {
     // 
    private Long id;

     // 商户管理号
    private Integer mainMerId;

     // 创建人
    private String creator;

     // 创建时间
    private Date createTime;

     // 修改时间
    private Date modifyTime;

     // 修改人
    private String modifier;

     // 说明
    private String remark;

     // 管理的商户列表
    private String agentMerIdSets;

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
     * 商户管理号
     * @return main_mer_id 商户管理号
     */
    public Integer getMainMerId() {
        return mainMerId;
    }

    /**
     * 商户管理号
     * @param mainMerId 商户管理号
     */
    public void setMainMerId(Integer mainMerId) {
        this.mainMerId = mainMerId;
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
     * 修改时间
     * @return modify_time 修改时间
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * 修改时间
     * @param modifyTime 修改时间
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * 修改人
     * @return modifier 修改人
     */
    public String getModifier() {
        return modifier;
    }

    /**
     * 修改人
     * @param modifier 修改人
     */
    public void setModifier(String modifier) {
        this.modifier = modifier == null ? null : modifier.trim();
    }

    /**
     * 说明
     * @return remark 说明
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 说明
     * @param remark 说明
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * 管理的商户列表
     * @return agent_mer_id_sets 管理的商户列表
     */
    public String getAgentMerIdSets() {
        return agentMerIdSets;
    }

    /**
     * 管理的商户列表
     * @param agentMerIdSets 管理的商户列表
     */
    public void setAgentMerIdSets(String agentMerIdSets) {
        this.agentMerIdSets = agentMerIdSets == null ? null : agentMerIdSets.trim();
    }
}