package com.rxh.service.square;

import com.rxh.pojo.merchant.MerchantRegisterInfo;
import com.rxh.pojo.payment.TradeObjectSquare;
import com.rxh.square.pojo.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 代付接口
 */

public interface PaymentRecordSquareService {
    // 获取商户
    MerchantInfo getMerchantInfoByMerId(String merId);

    //
    List<ChannelInfo> getChannelByPayType(Map<String,Object> paramMap);

    AgentMerchantSetting getAgentMerchantSettingByParentId(String parentId);

    int saveOrUpadateTransOrder(TransOrder transOrder);

    int saveOrUpadateTransBankInfo(TransBankInfo transBankInfo);


    int saveOrUpadateTransAudit(TransAudit transAudit);

    int saveOrUpdateSystemOrderTrack(SystemOrderTrack systemOrderTrack);

    MerchantQuotaRisk getMerchantQuotaRiskByMerId(String merId);

    Integer insertOrUpdateRiskQuotaData(List<RiskQuotaData> quotaDataList);

    void UpdateTransOrder(TransOrder transOrder);

    int batchUpdateSuccessOrderStatus(List<String> merOrderIds);

    SystemOrderTrack getSystemOrderTrack(String orderId);

    int saveOrUpadatePayOrder(PayOrder payOrder);

    int saveOrUpadatePayCardholderInfo(PayCardholderInfo payCardholderInfo);

    ChannelWallet getChannelWallet(String channelId);

    AgentWallet getAgentMerchantWallet(String parentId);

    MerchantWallet getMerchantWallet(String merId);

    MerchantRate getMerchantRate(String merId);

    AgentMerchantSetting getAgentMerchantSetting(String parentId);

    List<AgentMerchantSetting> getAgentMerchantSetting(AgentMerchantSetting agentMerchantSetting);

    ChannelInfo getChannelInfo(String channelId);

    void UpdatePayOrder(PayOrder payOrder);

    PayOrder getPayOrderById(String id);

    PayCardholderInfo getPayCardholderInfo(String merId);

    Integer insertRiskQuotaData(List<RiskQuotaData> quotaDataList);

    TransAudit getTransAuditByTransId(String transId);

    int updateTransAudit(TransAudit transAudit);

    void updateMerchantWallet(MerchantWallet merchantWallet);

    void updateAgentWallet(AgentWallet agentWallet);

    void updateChannelWallet(ChannelWallet channelWallet);

    List<PayOrder> getPayOrderByMerOrderId(String merId, String merOrderId);

    List<PayOrder> getPayOrderByWhereCondition(PayOrder record);

    PayOrder getProcessingPayOrderByMerOrderId(String merId, String merOrderId);

    TransOrder getTransOrderByMerOrderId(String merId, String merOrderId);

    MerchantRate getMerchantRateByIdAndPayType(String merId, String payType);

    MerchantCard checkBizType(String merId, String bizType);

    ExtraChannelInfo getChannelInfoByBizType(String bizType);

    void saveMerchantCard(MerchantCard param);

    void updateMerchantCard(MerchantCard merchantCard);

    MerchantCard getMerchanCardByMerchantIdAndBizType(String merId, String bizType);

    List<ExtraChannelInfo> getChannelInfosByPayType(TradeObjectSquare tradeObjectSquare);

    List<MerchantCard> getAllMerchantCard(String merId,String terminalMerId,String status);

    ExtraChannelInfo searchExtraChannelInfo(String channelId,String type);

    MerchantCard searchMerchantCard(String extraChannelId, String merId, String terminalMerId);

    TerminalMerchantsWallet getTerminalMerchantsWallet(String merId, String terminalMerId);

    void insertTerminalMerchantsWallet(TerminalMerchantsWallet terminalMerchantsWallet);

    void updateTerminalMerchantsWallet(TerminalMerchantsWallet terminalMerchantsWallet);

    void insertTerminalMerchantsDetails(TerminalMerchantsDetails terminalMerchantsDetails);

    void insertMerchantsDetails(MerchantsDetails merchantsDetails);

    int insetByBathMerchantsDetails(List<MerchantsDetails> list);

    void insertChannelDetails(ChannelDetails channelDetails);

    void insertAgentMerchantsDetails(AgentMerchantsDetails agentMerchantsDetails);

    int insetByBathAgentMerchantsDetails(List<AgentMerchantsDetails> agentMerchantsDetailsList);

    MerchantCard getMerchantCardByMerIdAndMerOrderId(String merId, String merOrderId);

    ExtraChannelInfo getExtraChannelInfoByExtraChannelId(String extraChannelId);

    List<MerchantRegisterInfo> getMerchantRegisterInfos(String merId, String terminalMerId, String status);

    MerchantCard getMerchantCardByMerIdAndMerOrderIdAndTermId(String merId, String merOrderId, String terminalMerId, short status);

    ExtraChannelInfo getExtraChannelInfoByInChannelId(String channelId);

    MerchantCard getMerchantCardByMerIdAndTermIdAndExtraId(String merId,  String terminalMerId, String extraChannelId);

    TransOrder getTransOrderByMerOrderIdAndMerIdAndTerMerId(String merOrderId, String merId, String terminalMerId);

    PayOrder getPayOrderByMerOrderIdAndMerIdAndTerMerId(String merOrderId, String merId, String terminalMerId);


    BigDecimal getTransOrderAmount(String merId, String terminalMerId, String originalMerOrderId);

    MerchantChannelHistory getLastUseChannel(TradeObjectSquare tradeObjectSquare);

    void updateMerchantChannelHistory(MerchantChannelHistory merchantChannelHistory);

    void insertMerchantChannelHistory(MerchantChannelHistory newData);

    ExtraChannelInfo getExtraChannelInfoByOrgId(String orgId, String type);

    MerchantRegisterInfo getMerchantRegisterInfoByMerIdAndTerminalMerId(String merId, String terminalMerId);

    List<MerchantRegisterCollect> getmMrchantRegisterCollectByMerIdAndTerminalMerId(String merId, String terminalMerId,String cardNum);

    MerchantRegisterCollect searchMerchantRegisterCollect(String merId, String terminalMerId, String bankCardNum, String extraChannelId);

    MerchantRegisterCollect getMerchantRegisterCollectbyParam(String extraChannelId, String merId, String merOrderId);

    PayCardholderInfo getPayCardholderInfoByPayId(String payId);

    AgentMerchantSetting getAgentMerchantSettingByParentIdAndPayType(String parentId, String payType);

    void insertMerchantWallet(MerchantWallet merchantWallet);

    void insertChannelWallet(ChannelWallet channelWallet);

    TransOrder getTransOrderOrderId(String toString);

    void insertAgentWallet(AgentWallet agentWallet);

    AgentMerchantsDetails getAgentMerchantsDetails(String orderId);

    ChannelDetails getChannelDetails(String orderId);

    TerminalMerchantsDetails getTerminalMerchantsDetails(String orderId);

    MerchantsDetails getMerchantDetails(String orderId);

    TransOrder getTransOrderByMerOrderIdAndMerId(String merOrderId, String merId);

    int batchUpdateFailOrderStatus(List<String> merOrderIds);

    TransOrder checkTransOrderMul(String merOrderId, String merId, String terminalMerId);

    List<TransOrder> getTransOrderByWhereCondition(TransOrder  transOrder);

    List<ChannelWallet> getChannelWalletByIds(List<String> channelInfoList_secondaryChannel_channelId);

    MerchantCard getMerchantCardByMerIdAndMerOrderIdAndTermIdAndCardNum(String merId, String merOrderId, String terminalMerId, String bankCardNum, short status);
}
