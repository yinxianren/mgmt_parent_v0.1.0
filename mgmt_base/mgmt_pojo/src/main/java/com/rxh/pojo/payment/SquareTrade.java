package com.rxh.pojo.payment;

import com.rxh.pojo.merchant.MerchantBankCardBinding;
import com.rxh.pojo.merchant.MerchantBasicInformationRegistration;
import com.rxh.pojo.merchant.MerchantRegisterInfo;
import com.rxh.square.pojo.*;

import java.io.Serializable;

/**
 *
 * @author panda
 * @version 0.0.2 :增加部分链接操作方法
 */
public class SquareTrade implements Serializable {


    private ChannelInfo channelInfo ;
    private ChannelInfo payOrderChannel ;
    private TransOrder transOrder;
    private PayOrder payOrder;
    private MerchantInfo merchantInfo;
    private TransAudit transAudit;
    private String merchName;
    private String MerOrderId;
    private String AuthCode;
    private String bankCode;
    private String bankYxq;
    private String bankCvv;
    private String ip;
    private FinanceDrawing financeDrawing;
    private TradeObjectSquare tradeObjectSquare;
    private ExtraChannelInfo extraChannelInfo;
    private  MerchantCard merchantCard;
    //    private MerchantAddCusInfo merchantAddCusInfo; //进件信息废除对象
    private MerchantBasicInformationRegistration merchantBasicInformationRegistration;//进件信息新对象
    private MerchantBankCardBinding merchantBankCardBinding;
    private MerchantRegisterInfo merchantRegisterInfo;
    private MerchantChannelHistory merchantChannelHistory;
    private  TerminalMerchantsDetails terminalMerchantsDetails ;
    private MerchantRegisterCollect merchantRegisterCollect;
    private BankCode bankInfo;//银行信息

    public BankCode getBankInfo() {
        return bankInfo;
    }

    public void setBankInfo(BankCode bankInfo) {
        this.bankInfo = bankInfo;
    }

    public MerchantRegisterCollect getMerchantRegisterCollect() {
        return merchantRegisterCollect;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setMerchantRegisterCollect(MerchantRegisterCollect merchantRegisterCollect) {
        this.merchantRegisterCollect = merchantRegisterCollect;
    }
    public SquareTrade lsetMerchantRegisterCollect(MerchantRegisterCollect merchantRegisterCollect) {
        this.setMerchantRegisterCollect( merchantRegisterCollect) ;
        return this;
    }
    public MerchantBankCardBinding getMerchantBankCardBinding() {
        return merchantBankCardBinding;
    }

    public void setMerchantBankCardBinding(MerchantBankCardBinding merchantBankCardBinding) {
        this.merchantBankCardBinding = merchantBankCardBinding;
    }

    public TerminalMerchantsDetails getTerminalMerchantsDetails() {
        return terminalMerchantsDetails;
    }

    public void setTerminalMerchantsDetails(TerminalMerchantsDetails terminalMerchantsDetails) {
        this.terminalMerchantsDetails = terminalMerchantsDetails;
    }

    public PayCardholderInfo getPayCardholderInfo() {
        return payCardholderInfo;
    }

    public void setPayCardholderInfo(PayCardholderInfo payCardholderInfo) {
        this.payCardholderInfo = payCardholderInfo;
    }

    public PayProductDetail getPayProductDetail() {
        return payProductDetail;
    }

    public void setPayProductDetail(PayProductDetail payProductDetail) {
        this.payProductDetail = payProductDetail;
    }

    private  PayCardholderInfo payCardholderInfo;
    private  PayProductDetail payProductDetail;




    public MerchantChannelHistory getMerchantChannelHistory() {
        return merchantChannelHistory;
    }

    public ChannelInfo getPayOrderChannel() {
        return payOrderChannel;
    }

    public void setPayOrderChannel(ChannelInfo payOrderChannel) {
        this.payOrderChannel = payOrderChannel;
    }
    public SquareTrade lsetPayOrderChannel(ChannelInfo payOrderChannel) {
        this.setPayOrderChannel(payOrderChannel);
        return this;
    }


    public void setMerchantChannelHistory(MerchantChannelHistory merchantChannelHistory) {
        this.merchantChannelHistory = merchantChannelHistory;
    }

    public MerchantRegisterInfo getMerchantRegisterInfo() {
        return merchantRegisterInfo;
    }

    public void setMerchantRegisterInfo(MerchantRegisterInfo merchantRegisterInfo) {
        this.merchantRegisterInfo = merchantRegisterInfo;
    }
    public SquareTrade lsetMerchantRegisterInfo(MerchantRegisterInfo merchantRegisterInfo) {
        this.setMerchantRegisterInfo(merchantRegisterInfo);
        return this;
    }

    public TransAudit getTransAudit() {
        return transAudit;
    }

    public void setTransAudit(TransAudit transAudit) {
        this.transAudit = transAudit;
    }
    public SquareTrade lsetTransAudit(TransAudit transAudit) {
        this.setTransAudit( transAudit);
        return this;
    }
    public MerchantCard getMerchantCard() {
        return merchantCard;
    }

    public void setMerchantCard(MerchantCard merchantCard) {
        this.merchantCard = merchantCard;
    }
    public SquareTrade lsetMerchantCard(MerchantCard merchantCard) {
        this.setMerchantCard(merchantCard);
        return this;
    }
    public ExtraChannelInfo getExtraChannelInfo() {
        return extraChannelInfo;
    }

    public MerchantBasicInformationRegistration getMerchantBasicInformationRegistration() {
        return merchantBasicInformationRegistration;
    }

    public void setMerchantBasicInformationRegistration(MerchantBasicInformationRegistration merchantBasicInformationRegistration) {
        this.merchantBasicInformationRegistration = merchantBasicInformationRegistration;
    }

    public void setExtraChannelInfo(ExtraChannelInfo extraChannelInfo) {
        this.extraChannelInfo = extraChannelInfo;
    }
    public SquareTrade lsetExtraChannelInfo(ExtraChannelInfo extraChannelInfo) {
        this.setExtraChannelInfo( extraChannelInfo);
        return this;
    }
    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankYxq() {
        return bankYxq;
    }

    public void setBankYxq(String bankYxq) {
        this.bankYxq = bankYxq;
    }

    public String getBankCvv() {
        return bankCvv;
    }

    public void setBankCvv(String bankCvv) {
        this.bankCvv = bankCvv;
    }

    public String getAuthCode() {
        return AuthCode;
    }

    public void setAuthCode(String authCode) {
        AuthCode = authCode;
    }

    public PayOrder getPayOrder() {
        return payOrder;
    }

    public void setPayOrder(PayOrder payOrder) {
        this.payOrder = payOrder;
    }
    public SquareTrade lsetPayOrder(PayOrder payOrder) {
        this.setPayOrder( payOrder) ;
        return this;
    }
    public String getMerOrderId() {
        return MerOrderId;
    }

    public void setMerOrderId(String merOrderId) {
        MerOrderId = merOrderId;
    }
    public SquareTrade lsetMerOrderId(String merOrderId) {
        this.setMerOrderId(merOrderId);
        return this;
    }
    public MerchantInfo getMerchantInfo() {
        return merchantInfo;
    }

    public void setMerchantInfo(MerchantInfo merchantInfo) {
        this.merchantInfo = merchantInfo;
    }
    public SquareTrade lsetMerchantInfo(MerchantInfo merchantInfo) {
        this.setMerchantInfo( merchantInfo);
        return this;
    }
    public String getMerchName() {
        return merchName;
    }

    public void setMerchName(String merchName) {
        this.merchName = merchName;
    }

    public ChannelInfo getChannelInfo() {
        return channelInfo;
    }

    public void setChannelInfo(ChannelInfo channelInfo) {
        this.channelInfo = channelInfo;
    }
    public SquareTrade lsetChannelInfo(ChannelInfo channelInfo) {
        this.setChannelInfo( channelInfo);
        return this;
    }
    public TransOrder getTransOrder() {
        return transOrder;
    }

    public void setTransOrder(TransOrder transOrder) {
        this.transOrder = transOrder;
    }
    public SquareTrade lsetTransOrder(TransOrder transOrder) {
        this.setTransOrder( transOrder);
        return this;
    }
    public TradeObjectSquare getTradeObjectSquare() {
        return tradeObjectSquare;
    }

    public void setTradeObjectSquare(TradeObjectSquare tradeObjectSquare) {
        this.tradeObjectSquare = tradeObjectSquare;
    }
    public SquareTrade lsetTradeObjectSquare(TradeObjectSquare tradeObjectSquare) {
        this.setTradeObjectSquare( tradeObjectSquare);
        return this;
    }
    public FinanceDrawing getFinanceDrawing() {
        return financeDrawing;
    }

    public void setFinanceDrawing(FinanceDrawing financeDrawing) {
        this.financeDrawing = financeDrawing;
    }
}
