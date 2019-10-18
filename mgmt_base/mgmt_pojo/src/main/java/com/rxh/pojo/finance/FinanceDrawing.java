package com.rxh.pojo.finance;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class FinanceDrawing implements Serializable {

    private Integer id;

     // 外键:商户号
    private Integer refId;

     // 1：商户提现 2:公司人员代为提现
    private Short type;

     // 提现币种
    private String currency;

     // 提现金额
    private BigDecimal amount;

    // 划款金额
    private BigDecimal transferAmount;

     // 划款币种
    private String transferCurrency;

     // 划款汇率
    private BigDecimal rate;

     // 申请人
    private String applicant;

     // 申请时间
    private Date applicationTime;

     // 审核人
    private String auditor;

     // 审核时间
    private Date auditTime;

     // 划款人
    private String transfer;

     // 划款时间
    private Date transferTime;

     // 0：无效，1:待审核,2:待划款 3:已划款 4为自动扣款  5:待复核
    private Short status;

     // 备注说明
    private String remark;

    // 划款说明
    private String transferRemark;

    // 复核说明
    private String reviewRemark;

     // 实际划款金额
    private BigDecimal realAmount;
    
    private String merName;
    
    private String reconDate;
    
    
    //账单类型: 1:日账单 2:月账单
  	private Integer reconType;
  	
    //申请开始时间
    private String minApplicationTime ;
   
    //申请结束时间
    private String maxApplicationTime;
    
	//划款开始时间
	private String minTransferTime;
	
	// 结束时间
	private String maxTransferTime;
	
	//多项状态查询
    private String statusString;

    /**
     * 
     * @return id 
     */
    public Integer getId() {
        return id;
    }

    /**
     * 
     * @param id 
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 外键:商户号
     * @return ref_id 外键:商户号
     */
    public Integer getRefId() {
        return refId;
    }

    /**
     * 外键:商户号
     * @param refId 外键:商户号
     */
    public void setRefId(Integer refId) {
        this.refId = refId;
    }

    /**
     * 1：商户提现 2:公司人员代为提现
     * @return type 1：商户提现 2:公司人员代为提现
     */
    public Short getType() {
        return type;
    }

    /**
     * 1：商户提现 2:公司人员代为提现
     * @param type 1：商户提现 2:公司人员代为提现
     */
    public void setType(Short type) {
        this.type = type;
    }

    /**
     * 提现币种
     * @return currency 提现币种
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * 提现币种
     * @param currency 提现币种
     */
    public void setCurrency(String currency) {
        this.currency = currency == null ? null : currency.trim();
    }

    /**
     * 提现金额
     * @return amount 提现金额
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * 提现金额
     * @param amount 提现金额
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * 划款金额
     * @return transfer_amount 划款金额
     */
    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    /**
     * 划款金额
     * @param transferAmount 划款金额
     */
    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    /**
     * 划款币种
     * @return transfer_currency 划款币种
     */
    public String getTransferCurrency() {
        return transferCurrency;
    }

    /**
     * 划款币种
     * @param transferCurrency 划款币种
     */
    public void setTransferCurrency(String transferCurrency) {
        this.transferCurrency = transferCurrency == null ? null : transferCurrency.trim();
    }

    /**
     * 划款汇率
     * @return rate 划款汇率
     */
    public BigDecimal getRate() {
        return rate;
    }

    /**
     * 划款汇率
     * @param rate 划款汇率
     */
    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    /**
     * 申请人
     * @return applicant 申请人
     */
    public String getApplicant() {
        return applicant;
    }

    /**
     * 申请人
     * @param applicant 申请人
     */
    public void setApplicant(String applicant) {
        this.applicant = applicant == null ? null : applicant.trim();
    }

    /**
     * 申请时间
     * @return application_time 申请时间
     */
    public Date getApplicationTime() {
        return applicationTime;
    }

    /**
     * 申请时间
     * @param applicationTime 申请时间
     */
    public void setApplicationTime(Date applicationTime) {
        this.applicationTime = applicationTime;
    }

    /**
     * 审核人
     * @return auditor 审核人
     */
    public String getAuditor() {
        return auditor;
    }

    /**
     * 审核人
     * @param auditor 审核人
     */
    public void setAuditor(String auditor) {
        this.auditor = auditor == null ? null : auditor.trim();
    }

    /**
     * 审核时间
     * @return audit_time 审核时间
     */
    public Date getAuditTime() {
        return auditTime;
    }

    /**
     * 审核时间
     * @param auditTime 审核时间
     */
    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    /**
     * 划款人
     * @return transfer 划款人
     */
    public String getTransfer() {
        return transfer;
    }

    /**
     * 划款人
     * @param transfer 划款人
     */
    public void setTransfer(String transfer) {
        this.transfer = transfer == null ? null : transfer.trim();
    }

    /**
     * 划款时间
     * @return transfer_time 划款时间
     */
    public Date getTransferTime() {
        return transferTime;
    }

    /**
     * 划款时间
     * @param transferTime 划款时间
     */
    public void setTransferTime(Date transferTime) {
        this.transferTime = transferTime;
    }

    /**
     * 0：无效，1:待审核,2:待划款 3:待复核 4为自动扣款
     * @return status 0：无效，1:待审核,2:待划款 3:待复核 4为自动扣款
     */
    public Short getStatus() {
        return status;
    }

    /**
     * 0：无效，1:待审核,2:待划款 3:待复核 4为自动扣款
     * @param status 0：无效，1:待审核,2:待划款 3:待复核 4为自动扣款
     */
    public void setStatus(Short status) {
        this.status = status;
    }

    /**
     * 备注说明
     * @return remark 备注说明
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 备注说明
     * @param remark 备注说明
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * 实际划款金额
     * @return real_amount 实际划款金额
     */
    public BigDecimal getRealAmount() {
        return realAmount;
    }

    /**
     * 实际划款金额
     * @param realAmount 实际划款金额
     */
    public void setRealAmount(BigDecimal realAmount) {
        this.realAmount = realAmount;
    }

	public String getMerName() {
		return merName;
	}

	public void setMerName(String merName) {
		this.merName = merName;
	}

	public String getReconDate() {
		return reconDate;
	}

	public void setReconDate(String reconDate) {
		this.reconDate = reconDate;
	}

	public Integer getReconType() {
		return reconType;
	}

	public void setReconType(Integer reconType) {
		this.reconType = reconType;
	}

	public String getMinApplicationTime() {
		return minApplicationTime;
	}

	public void setMinApplicationTime(String minApplicationTime) {
		this.minApplicationTime = minApplicationTime;
	}

	public String getMaxApplicationTime() {
		return maxApplicationTime;
	}

	public void setMaxApplicationTime(String maxApplicationTime) {
		this.maxApplicationTime = maxApplicationTime;
	}

	public String getMinTransferTime() {
		return minTransferTime;
	}

	public void setMinTransferTime(String minTransferTime) {
		this.minTransferTime = minTransferTime;
	}

	public String getMaxTransferTime() {
		return maxTransferTime;
	}

	public void setMaxTransferTime(String maxTransferTime) {
		this.maxTransferTime = maxTransferTime;
	}

	public String getStatusString() {
		return statusString;
	}

	public void setStatusString(String statusString) {
		this.statusString = statusString;
	}

    public String getTransferRemark() {
        return transferRemark;
    }

    public void setTransferRemark(String transferRemark) {
        this.transferRemark = transferRemark;
    }

    public String getReviewRemark() {
        return reviewRemark;
    }

    public void setReviewRemark(String reviewRemark) {
        this.reviewRemark = reviewRemark;
    }
}