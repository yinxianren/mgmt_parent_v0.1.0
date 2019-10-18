package com.rxh.pojo.merchant;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class MerchantRatePercent implements Serializable {
    public final static short STATUS_HISTORY = 1;
    public final static short STATUS_NOW = 0;
     /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 主键
    private Long id;

     // 0通用，1原始币种，2域名
    private Short type;

     // 元素值
    private String typeValue;
   
    //商户号
    private String elementValue;

     // 上抛点数
    private BigDecimal upPercent;
    
    //0:当前汇率，1:历史汇率
    private Short status = 0;

     // 备注
    private String remark;

     // 创建人
    private String creator;

     // 创建时间
    private Date createTime;

     // 修改人
    private String modifier;

     // 修改时间
    private Date modifyTime;
    
    //创建开始时间
    private String minCreateTime ;
   
    //创建结束时间
    private String maxCreateTime;
    
	//修改开始时间
	private String minModifyTime;
	
	//修改结束时间
	private String maxModifyTime;
	
	
    //元素名称
    private  String typeValueName;
    
    

    /**
     * 主键
     * @return Id 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 主键
     * @param id 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 0通用，1商户号，2原始币种，3域名
     * @return type 0通用，1商户号，2原始币种，3域名
     */
    public Short getType() {
        return type;
    }

    /**
     * 0通用，1商户号，2原始币种，3域名
     * @param type 0通用，1商户号，2原始币种，3域名
     */
    public void setType(Short type) {
        this.type = type;
    }

    /**
     * 元素值
     * @return type_value 元素值
     */
    public String getTypeValue() {
        return typeValue;
    }

    /**
     * 元素值
     * @param typeValue 元素值
     */
    public void setTypeValue(String typeValue) {
        this.typeValue = typeValue == null ? null : typeValue.trim();
    }

    
    public BigDecimal getUpPercent() {
        return upPercent;
    }

    /**
     * 上抛点数
     * @param upPercent 上抛点数
     */
    public void setUpPercent(BigDecimal upPercent) {
        this.upPercent = upPercent;
    }
    
    public String getElementValue() {
		return elementValue;
	}

	public void setElementValue(String elementValue) {
		this.elementValue = elementValue;
	}


    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    /**
     * 备注
     * @return remark 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 备注
     * @param remark 备注
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

	public String getMinCreateTime() {
		return minCreateTime;
	}

	public void setMinCreateTime(String minCreateTime) {
		this.minCreateTime = minCreateTime;
	}

	public String getMaxCreateTime() {
		return maxCreateTime;
	}

	public void setMaxCreateTime(String maxCreateTime) {
		this.maxCreateTime = maxCreateTime;
	}

	public String getMinModifyTime() {
		return minModifyTime;
	}

	public void setMinModifyTime(String minModifyTime) {
		this.minModifyTime = minModifyTime;
	}

	public String getMaxModifyTime() {
		return maxModifyTime;
	}

	public void setMaxModifyTime(String maxModifyTime) {
		this.maxModifyTime = maxModifyTime;
	}

	public String getTypeValueName() {
		return typeValueName;
	}

	public void setTypeValueName(String typeValueName) {
		this.typeValueName = typeValueName;
	}

}