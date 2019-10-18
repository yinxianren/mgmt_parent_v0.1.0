package com.rxh.pojo.base;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class SearchInfo implements Serializable {
    public static final int ID_SEARCH_TYPE_ORDER = 0;
    public static final int ID_SEARCH_TYPE_MERCHANT_ORDER = 1;
    public static final int ID_SEARCH_TYPE_BANK_ORDER = 2;

    /**
     * ID查找类型（用于 com.rxh.mapper.core.CoreOrderMapper.Order_Search_Info）：
     * 0：平台订单号
     * 1：商户订单号
     * 2：银行流水号
     */
    private Integer idSearchType;
    private String id;
    private String merId;
    private Integer siteId;
    private String siteUrl;
    private String email;
    private Short exceptionStatus;
    private Integer acquirerAccount;
    private String payMode;
    private String payType;
    private Short bankStatus;
    private Short shipStatus;
    private Short financeStatus;
    private String ip;
    /*
    时间查询类型（暂用于保证金查询）：
    0：交易日期
    1：释放日期
     */
    private Integer timeSearchType;
    /*
    Date可为创建时间
    Date2可为修改时间
     */
    private Date startDate;
    private Date endDate;
    private Date startDate2;
    private Date endDate2;
    // 保证金释放状态
    private Short bondStatus;
    private Short changeType;
    /*
    用于查询多个异常状态的信息
     */
    private List<Short> changeTypeList;
    private Short changeStatus;
    // 创建人
    private String creator;
    // 修改人
    private String modifier;
    // 申请人
    private String applicant;
    // 审核人
    private String auditor;
    private Short type;
    private Short status;
    private String content;
    //merchant_rate_percent
    private String elementValue;
    private String typeValue;
    private BigDecimal upPercent;
    //商户订单号
    private String merOrderId;
    //快递公司
    private String expressName;
    //快递号
    private String expressNo;
    private String country;
    // 原始币种
    private String sourceCurrency;
    // 目标币种
    private String targetCurrency;
    //风控专区

    private Short refType;
    private Integer refId;
    private String element;
    private String transId;
    private String organizationId;
    private String channelId;
    private String channelTransCode;
    private String currency;
    private BigDecimal amount;
    private BigDecimal realAmount;
    private BigDecimal fee;
    private String channelName;
    private BigDecimal channelFee;
    private BigDecimal income;
    private Integer orderStatus;
    private Integer settleStatus;
    private BigDecimal agentFee;
    private Date tradeTime;

    private String payId;
    private  String orderId;

    private String parentId;

    private String settleCycle;

    private String terminalMerId;

    private String customerId;

    private String loginIp;

    private String agentMerId;


    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getTerminalMerId() {
        return terminalMerId;
    }

    public void setTerminalMerId(String terminalMerId) {
        this.terminalMerId = terminalMerId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    public String getSettleCycle() {
        return settleCycle;
    }

    public void setSettleCycle(String settleCycle) {
        this.settleCycle = settleCycle;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelTransCode() {
        return channelTransCode;
    }

    public void setChannelTransCode(String channelTransCode) {
        this.channelTransCode = channelTransCode;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(BigDecimal realAmount) {
        this.realAmount = realAmount;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public BigDecimal getChannelFee() {
        return channelFee;
    }

    public void setChannelFee(BigDecimal channelFee) {
        this.channelFee = channelFee;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getSettleStatus() {
        return settleStatus;
    }

    public void setSettleStatus(Integer settleStatus) {
        this.settleStatus = settleStatus;
    }

    public BigDecimal getAgentFee() {
        return agentFee;
    }

    public void setAgentFee(BigDecimal agentFee) {
        this.agentFee = agentFee;
    }

    public Date getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(Date tradeTime) {
        this.tradeTime = tradeTime;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public Integer getRefId() {
        return refId;
    }

    public void setRefId(Integer refId) {
        this.refId = refId;
    }

    public Short getRefType() {
        return refType;
    }

    public void setRefType(Short refType) {
        this.refType = refType;
    }

    public BigDecimal getUpPercent() {
        return upPercent;
    }

    public void setUpPercent(BigDecimal upPercent) {
        this.upPercent = upPercent;
    }

    public String getTypeValue() {
        return typeValue;
    }

    public void setTypeValue(String typeValue) {
        this.typeValue = typeValue;
    }

    public String getElementValue() {
        return elementValue;
    }
    public void setElementValue(String elementValue) {
        this.elementValue = elementValue;
    }
    public String getExpressName() {
        return expressName;
    }

    public void setExpressName(String expressName) {
        this.expressName = expressName;
    }

    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo;
    }

    public String getMerOrderId() {
        return merOrderId;
    }

    public void setMerOrderId(String merOrderId) {
        this.merOrderId = merOrderId;
    }

    public Integer getIdSearchType() {
        return idSearchType;
    }

    public void setIdSearchType(Integer idSearchType) {
        this.idSearchType = idSearchType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMerId() {
        return merId;
    }

    public void setMerId(String merId) {
        this.merId = merId;
    }

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Short getExceptionStatus() {
        return exceptionStatus;
    }

    public void setExceptionStatus(Short exceptionStatus) {
        this.exceptionStatus = exceptionStatus;
    }

    public Integer getAcquirerAccount() {
        return acquirerAccount;
    }

    public void setAcquirerAccount(Integer acquirerAccount) {
        this.acquirerAccount = acquirerAccount;
    }

    public String getPayMode() {
        return payMode;
    }

    public void setPayMode(String payMode) {
        this.payMode = payMode;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public Short getBankStatus() {
        return bankStatus;
    }

    public void setBankStatus(Short bankStatus) {
        this.bankStatus = bankStatus;
    }

    public Short getShipStatus() {
        return shipStatus;
    }

    public void setShipStatus(Short shipStatus) {
        this.shipStatus = shipStatus;
    }

    public Short getFinanceStatus() {
        return financeStatus;
    }

    public void setFinanceStatus(Short financeStatus) {
        this.financeStatus = financeStatus;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getTimeSearchType() {
        return timeSearchType;
    }

    public void setTimeSearchType(Integer timeSearchType) {
        this.timeSearchType = timeSearchType;
    }


    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getStartDate2() {
        return startDate2;
    }

    public void setStartDate2(Date startDate2) {
        this.startDate2 = startDate2;
    }

    public Date getEndDate2() {
        return endDate2;
    }

    public void setEndDate2(Date endDate2) {
        this.endDate2 = endDate2;
    }

    public Short getBondStatus() {
        return bondStatus;
    }

    public void setBondStatus(Short bondStatus) {
        this.bondStatus = bondStatus;
    }

    public Short getChangeType() {
        return changeType;
    }

    public void setChangeType(Short changeType) {
        this.changeType = changeType;
    }

    public List<Short> getChangeTypeList() {
        return changeTypeList;
    }

    public void setChangeTypeList(List<Short> changeTypeList) {
        this.changeTypeList = changeTypeList;
    }

    public Short getChangeStatus() {
        return changeStatus;
    }

    public void setChangeStatus(Short changeStatus) {
        this.changeStatus = changeStatus;
    }

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSourceCurrency() {
        return sourceCurrency;
    }

    public void setSourceCurrency(String sourceCurrency) {
        this.sourceCurrency = sourceCurrency;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(String targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAgentMerId() {
        return agentMerId;
    }

    public void setAgentMerId(String agentMerId) {
        this.agentMerId = agentMerId;
    }
}
