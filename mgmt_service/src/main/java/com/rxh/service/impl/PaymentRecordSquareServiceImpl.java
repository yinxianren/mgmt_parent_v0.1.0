package com.rxh.service.impl;

import com.rxh.mapper.square.*;
import com.rxh.pojo.merchant.MerchantRegisterInfo;
import com.rxh.pojo.payment.TradeObjectSquare;
import com.rxh.service.square.PaymentRecordSquareService;
import com.rxh.service.trading.PayCardholderInfoService;
import com.rxh.spring.annotation.RedisCacheDelete;
import com.rxh.square.pojo.*;
import com.rxh.utils.SystemConstant;
import com.rxh.utils.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class PaymentRecordSquareServiceImpl implements PaymentRecordSquareService {

    @Autowired
    private MerchantSquareInfoMapper merchantSquareInfoMapper;

    @Autowired
    private ChannelInfoMapper channelInfoMapper;

    @Autowired
    private AgentMerchantInfoMapper agentMerchantInfoMapper;
    @Autowired
    private AgentMerchantSettingMapper agentMerchantSettingMapper;

    @Autowired
    private TransOrderMapper transOrderMapper;
    @Autowired
    private PayOrderMapper payOrderMapper;
    @Autowired
    private MerchantCardMapper merchantCardMapper;
    @Autowired
    private ExtraChannelInfoMapper extraChannelInfoMapper;

    @Autowired
    private TransBankInfoMapper transBankInfoMapper;
    @Autowired
    private PayCardholderInfoMapper payCardholderInfoMapper;
    @Autowired
    private TransAuditMapper transAuditMapper;
    @Autowired
    private SystemOrderTrackMapper systemOrderTrackMapper;
    @Autowired
    private MerchantQuotaRiskMapper merchantQuotaRiskMapper;
    @Autowired
    private RiskSquareQuotaDataMapper riskSquareQuotaDataMapper;
    @Autowired
    private PayCardholderInfoService payCardholderInfoService;

    @Autowired
    private MerchantWalletMapper merchantWalletMapper;
    @Autowired
    private MerchantSquareRateMapper merchantSquareRateMapper;
    @Autowired
    private AgentWalletMapper agentWalletMapper;

    @Autowired
    private ChannelWalletMapper channelWalletMapper;
    @Autowired
    private TerminalMerchantsWalletMapper terminalMerchantsWalletMapper;
    @Autowired
    private TerminalMerchantsDetailsMapper terminalMerchantsDetailsMapper;

    @Autowired
    private MerchantRegisterInfoMapper merchantRegisterInfoMapper;
    @Autowired
    private AgentMerchantsDetailsMapper agentMerchantsDetailsMapper;
    @Autowired
    private ChannelDetailsMapper channelDetailsMapper;
    @Autowired
    private MerchantsDetailsMapper merchantsDetailsMapper;
    @Autowired
    private MerchantChannelHistoryMapper merchantChannelHistoryMapper;
    @Autowired
    private MerchantRegisterCollectMapper merchantRegisterCollectMapper;



    @Override
    public MerchantInfo getMerchantInfoByMerId(String merId) {
        return merchantSquareInfoMapper.selectByPrimaryKey(merId);
    }

    @Override
    public List<ChannelInfo> getChannelByPayType(Map<String, Object> paramMap) {
        return channelInfoMapper.getChannelByPayType(paramMap);
    }

    @Override
    public AgentMerchantSetting getAgentMerchantSettingByParentId(String parentId) {
        return agentMerchantSettingMapper.getAgentMerchantSettingByParentId(parentId);
    }

    @Transactional
    @Override
    public int saveOrUpadateTransOrder(TransOrder transOrder) {
        return transOrderMapper.insertSelective(transOrder);
    }

    @Transactional
    @Override
    public int saveOrUpadatePayOrder(PayOrder payOrder) {
        return payOrderMapper.insertSelective(payOrder);
    }

    @Transactional
    @Override
    public int saveOrUpadateTransBankInfo(TransBankInfo transBankInfo) {
        return transBankInfoMapper.insertSelective(transBankInfo);
    }

    @Transactional
    @Override
    public int saveOrUpadatePayCardholderInfo(PayCardholderInfo payCardholderInfo) {
        return payCardholderInfoMapper.insertSelective(payCardholderInfo);
    }

    @Override
    public int saveOrUpadateTransAudit(TransAudit transAudit) {
        int a = transAuditMapper.insertSelective(transAudit);
        return a;
    }

    @Override
    public int saveOrUpdateSystemOrderTrack(SystemOrderTrack systemOrderTrack) {
        String id = UUID.createKey("system_order_track", "");
        systemOrderTrack.setId(id);

        return systemOrderTrackMapper.insertSelective(systemOrderTrack);
    }

    @Override
    public MerchantQuotaRisk getMerchantQuotaRiskByMerId(String merId) {
        return merchantQuotaRiskMapper.search(merId);
    }

    @Override
    public void UpdateTransOrder(TransOrder transOrder) {
        transOrderMapper.updateByPrimaryKey(transOrder);
    }

    @Transactional
    @Override
    public int batchUpdateSuccessOrderStatus(List<String> merOrderIds){
        return transOrderMapper.batchUpdateSuccessOrderStatus(merOrderIds);
    }

    @Transactional
    @Override
    public int batchUpdateFailOrderStatus(List<String> merOrderIds){
        return transOrderMapper.batchUpdateFailOrderStatus(merOrderIds);
    }

    @Override
    public TransOrder checkTransOrderMul(String merOrderId, String merId, String terminalMerId) {
        return transOrderMapper.checkTransOrderMul(merOrderId,merId,terminalMerId);
    }

    @Override
    public List<TransOrder> getTransOrderByWhereCondition(TransOrder transOrder) {
        return transOrderMapper.getTransOrderByWhereCondition(transOrder);
    }

    @Override
    public List<ChannelWallet> getChannelWalletByIds(List<String> channelInfoList_secondaryChannel_channelId) {
        return channelWalletMapper.getChannelWalletByIds(channelInfoList_secondaryChannel_channelId);
    }

    @Override
    public SystemOrderTrack getSystemOrderTrack(String orderId) {
        return systemOrderTrackMapper.getSystemOrderTrack(orderId);
    }

    @Override
    public PayCardholderInfo getPayCardholderInfo(String merId) {
        return null;
    }

    @Override
    public Integer insertRiskQuotaData(List<RiskQuotaData> quotaDataList) {
        return riskSquareQuotaDataMapper.insertRiskQuotaData(quotaDataList);
    }

    @Override
    public TransAudit getTransAuditByTransId(String transId) {
        return transAuditMapper.findTransAuditByTransId(transId);
    }

    @Override
    public int updateTransAudit(TransAudit transAudit) {
        return transAuditMapper.updateByPrimaryKeySelective(transAudit);
    }

    @Caching(evict = {@CacheEvict(value = "risk_quota_data", allEntries = true)})
    @Override
    public Integer insertOrUpdateRiskQuotaData(List<RiskQuotaData> quotaDataList) {
        return riskSquareQuotaDataMapper.insertOrUpdateRiskQuotaData(quotaDataList);
    }

    @Override
    public ChannelWallet getChannelWallet(String channelId) {
        return channelWalletMapper.getChannelWallet(channelId);
    }

    @Override
    public AgentWallet getAgentMerchantWallet(String parentId) {
        return agentWalletMapper.selectByPrimaryKey(parentId);
    }

    @Override
    public MerchantWallet getMerchantWallet(String merId) {
        return merchantWalletMapper.selectByPrimaryKey(merId);
    }


    @Override
    public MerchantRate getMerchantRate(String merId) {
        return merchantSquareRateMapper.getMerchantRate(merId);
    }

    @Override
    public AgentMerchantSetting getAgentMerchantSetting(String parentId) {
        return agentMerchantSettingMapper.getAgentMerchantSettingByParentId(parentId);
    }
    @Override
    public List<AgentMerchantSetting> getAgentMerchantSetting(AgentMerchantSetting agentMerchantSetting){
        return agentMerchantSettingMapper.search(agentMerchantSetting);
    }
    @Override
    public ChannelInfo getChannelInfo(String channelId) {
        return channelInfoMapper.selectByPrimaryKey(channelId);
    }

    @Override
    public void UpdatePayOrder(PayOrder payOrder) {
        payOrderMapper.updateByPrimaryKey(payOrder);
    }


    @Override
    public PayOrder getPayOrderById(String id) {
        return payOrderMapper.selectByPrimaryKey(id);
    }



    @Transactional
    @Override
    public void updateMerchantWallet(MerchantWallet merchantWallet) {
            merchantWalletMapper.updateByPrimaryKey(merchantWallet);
    }

    @Override
    public void updateAgentWallet(AgentWallet agentWallet) {
            agentWalletMapper.updateByPrimaryKey(agentWallet);
    }

    @Override
    public void updateChannelWallet(ChannelWallet channelWallet) {
        channelWalletMapper.updateByPrimaryKey(channelWallet);
    }

    @Override
    public  List<PayOrder> getPayOrderByMerOrderId(String merId, String merOrderId) {
        return payOrderMapper.getPayOrderByMerOrderId(merId, merOrderId);
    }

    @Override
    public List<PayOrder> getPayOrderByWhereCondition(PayOrder record) {
        return payOrderMapper.getPayOrderByWhereCondition(record);
    }

    @Override
    public PayOrder getProcessingPayOrderByMerOrderId(String merId, String merOrderId) {
        return payOrderMapper.getProcessingPayOrderByMerOrderId(merId, merOrderId);
    }


    @Override
    public TransOrder getTransOrderByMerOrderId(String merId, String merOrderId) {
        return transOrderMapper.getTransOrderByMerOrderId(merId, merOrderId);
    }

    @Override
    public MerchantRate getMerchantRateByIdAndPayType(String merId, String payType) {
        return merchantSquareRateMapper.getMerchantRateByIdAndPayType(merId, payType);
    }


    @Override
    public MerchantCard checkBizType(String merId, String bizType) {
        MerchantCard param = new MerchantCard();
        param.setMerId(merId);
        param.setExtraChannelId(bizType);
        return merchantCardMapper.search(param);
    }


    @Override
    public ExtraChannelInfo getChannelInfoByBizType(String bizType) {
        ExtraChannelInfo param = new ExtraChannelInfo();
        param.setExtraChannelId(bizType);
        return extraChannelInfoMapper.search(param);
    }

    @RedisCacheDelete(hashKey="merchant_card",keyNameArry={"#merId","#terminalMerId"},keyIndex = 0)
    @Override
    public void saveMerchantCard(MerchantCard param) {
        merchantCardMapper.insertSelective(param);
    }

    @RedisCacheDelete(hashKey="merchant_card",keyNameArry={"#merId","#terminalMerId"},keyIndex = 0)
    @Override
    public void updateMerchantCard(MerchantCard merchantCard) {
        merchantCardMapper.update(merchantCard);
    }

    @Override
    public MerchantCard getMerchanCardByMerchantIdAndBizType(String merId, String bizType) {
        MerchantCard param = new MerchantCard();
        param.setMerId(merId);
        param.setExtraChannelId(bizType);
        return merchantCardMapper.search(param);
    }

    @Override
    public List<ExtraChannelInfo> getChannelInfosByPayType(TradeObjectSquare tradeObjectSquare) {
        Integer value=tradeObjectSquare.getBizType()==null ?  null: Integer.valueOf(tradeObjectSquare.getBizType());
        return extraChannelInfoMapper.getChannelInfosByPayType(value);
    }

    @Override
    public List<MerchantCard> getAllMerchantCard(String merId, String terminalMerId,String status) {
        return merchantCardMapper.getAllMerchantCard(merId, terminalMerId,Integer.valueOf(status));
    }

    @Override
    public ExtraChannelInfo searchExtraChannelInfo(String channelId,String type) {
        return extraChannelInfoMapper.searchExtraChannelInfo(channelId,type);
    }

    @Override
    public MerchantCard searchMerchantCard(String extraChannelId, String merId, String terminalMerId) {
        return merchantCardMapper.searchMerchantCard(extraChannelId, merId, terminalMerId);
    }

    @Override
    public TerminalMerchantsWallet getTerminalMerchantsWallet(String merId, String terminalMerId) {
        return terminalMerchantsWalletMapper.search(merId, terminalMerId);
    }

    @Override
    public void insertTerminalMerchantsWallet(TerminalMerchantsWallet terminalMerchantsWallet) {
        terminalMerchantsWalletMapper.insertSelective(terminalMerchantsWallet);
    }

    @Override
    public void updateTerminalMerchantsWallet(TerminalMerchantsWallet terminalMerchantsWallet) {
        terminalMerchantsWalletMapper.updateByPrimaryKey(terminalMerchantsWallet);
    }

    @Override
    public void insertTerminalMerchantsDetails(TerminalMerchantsDetails terminalMerchantsDetails) {

        terminalMerchantsDetailsMapper.insertSelective(terminalMerchantsDetails);
    }

    @Override
    public void insertMerchantsDetails(MerchantsDetails merchantsDetails) {
        merchantsDetailsMapper.insertSelective(merchantsDetails);
    }

    @Transactional
    @Override
    public int insetByBathMerchantsDetails(List<MerchantsDetails> list){
            return merchantsDetailsMapper.insetByBath(list);

    }

    @Override
    public void insertChannelDetails(ChannelDetails channelDetails) {
        channelDetailsMapper.insertSelective(channelDetails);
    }
    @Transactional
    @Override
    public void insertAgentMerchantsDetails(AgentMerchantsDetails agentMerchantsDetails) {
            agentMerchantsDetailsMapper.insertSelective(agentMerchantsDetails);
    }

    @Transactional
    @Override
    public int insetByBathAgentMerchantsDetails(List<AgentMerchantsDetails> agentMerchantsDetailsList){
            return  agentMerchantsDetailsMapper.insetByBath(agentMerchantsDetailsList);
    }


    @Override
    public MerchantCard getMerchantCardByMerIdAndMerOrderId(String merId, String merOrderId) {
        MerchantCard merchantCard = new MerchantCard();
        merchantCard.setMerId(merId);
        merchantCard.setMerOrderId(merOrderId);
        merchantCard.setStatus(Integer.valueOf(SystemConstant.BANK_STATUS_SUCCESS));
        return merchantCardMapper.search(merchantCard);
    }

    @Override
    public ExtraChannelInfo getExtraChannelInfoByExtraChannelId(String extraChannelId) {
        ExtraChannelInfo extraChannelInfo = new ExtraChannelInfo();
        extraChannelInfo.setExtraChannelId(extraChannelId);
        return extraChannelInfoMapper.search(extraChannelInfo);
    }

    @Override
    public List<MerchantRegisterInfo> getMerchantRegisterInfos(String merId, String terminalMerId, String status) {

        // return merchantRegisterInfoMapper.getMerchantRegisterInfos(merId,terminalMerId,Integer.valueOf(status));
        return null;
    }

    @Override
    public MerchantCard getMerchantCardByMerIdAndMerOrderIdAndTermId(String merId, String merOrderId, String terminalMerId, short status) {
        MerchantCard merchantCard = new MerchantCard();
        merchantCard.setMerId(merId);
        merchantCard.setTerminalMerId(terminalMerId);
        merchantCard.setMerOrderId(merOrderId);
        merchantCard.setStatus(Integer.valueOf(status));
        return merchantCardMapper.search(merchantCard);
    }

    @Override
    public MerchantCard getMerchantCardByMerIdAndMerOrderIdAndTermIdAndCardNum(String merId, String merOrderId, String terminalMerId, String bankCardNum, short status) {
        MerchantCard merchantCard = new MerchantCard();
        merchantCard.setMerId(merId);
        merchantCard.setTerminalMerId(terminalMerId);
        merchantCard.setMerOrderId(merOrderId);
        merchantCard.setCardNum(bankCardNum);
        merchantCard.setStatus(Integer.valueOf(status));
        return merchantCardMapper.search(merchantCard);
    }

    @Override
    public ExtraChannelInfo getExtraChannelInfoByInChannelId(String channelId) {
        ExtraChannelInfo extraChannelInfo = new ExtraChannelInfo();
        extraChannelInfo.setOrganizationId(channelId);
        extraChannelInfo.setType(SystemConstant.BONDCARD);
        return extraChannelInfoMapper.search(extraChannelInfo);
    }

    @Override
    public MerchantCard getMerchantCardByMerIdAndTermIdAndExtraId(String merId,String terminalMerId, String extraChannelId) {
        MerchantCard merchantCard = new MerchantCard();
        merchantCard.setMerId(merId);
        merchantCard.setTerminalMerId(terminalMerId);
        merchantCard.setExtraChannelId(extraChannelId);
        merchantCard.setStatus(Integer.valueOf(SystemConstant.SUCCESS));
        return merchantCardMapper.search(merchantCard);
    }


    @Override
    public TransOrder getTransOrderByMerOrderIdAndMerIdAndTerMerId(String merOrderId, String merId, String terminalMerId) {

        return transOrderMapper.getTransOrderByMerOrderIdAndMerIdAndTerMerId(merOrderId,merId,terminalMerId);
    }

    @Override
    public PayOrder getPayOrderByMerOrderIdAndMerIdAndTerMerId(String merOrderId, String merId, String terminalMerId) {

        return payOrderMapper.getPayOrderByMerOrderIdAndMerIdAndTerMerId(merOrderId,merId,terminalMerId);
    }

    @Override
    public BigDecimal getTransOrderAmount(String merId, String terminalMerId, String originalMerOrderId) {
        return transOrderMapper.getTransOrderAmount(merId,terminalMerId,originalMerOrderId);
    }

    @Override
    public MerchantChannelHistory getLastUseChannel(TradeObjectSquare tradeObjectSquare) {

        return merchantChannelHistoryMapper.getLastUseChannel(tradeObjectSquare.getMerId(),SystemConstant.SUCCESS);
    }

    @Override
    public void updateMerchantChannelHistory(MerchantChannelHistory merchantChannelHistory) {
        merchantChannelHistoryMapper.updateByPrimaryKey(merchantChannelHistory);
    }

    @Override
    public void insertMerchantChannelHistory(MerchantChannelHistory newData) {
        merchantChannelHistoryMapper.insertSelective(newData);
    }

    @Override
    public ExtraChannelInfo getExtraChannelInfoByOrgId(String orgId, String type) {
        ExtraChannelInfo extraChannelInfo = new ExtraChannelInfo();
        extraChannelInfo.setType(type);
        extraChannelInfo.setOrganizationId(orgId);
        return extraChannelInfoMapper.search(extraChannelInfo);
    }

    @Override
    public MerchantRegisterInfo getMerchantRegisterInfoByMerIdAndTerminalMerId(String merId, String terminalMerId) {
        return merchantRegisterInfoMapper.getMerchantRegisterInfoByMerIdAndTerminalMerId(merId,terminalMerId);
    }

    @Override
    public List<MerchantRegisterCollect> getmMrchantRegisterCollectByMerIdAndTerminalMerId(String merId, String terminalMerId ,String cardNum) {
        return merchantRegisterCollectMapper.getmMrchantRegisterCollectByMerIdAndTerminalMerId(merId,terminalMerId,cardNum);
    }

    @Override
    public MerchantRegisterCollect searchMerchantRegisterCollect(String merId, String terminalMerId, String bankCardNum, String extraChannelId) {
        return merchantRegisterCollectMapper.searchMerchantRegisterCollect(merId,terminalMerId,bankCardNum,extraChannelId);
    }

    @Override
    public MerchantRegisterCollect getMerchantRegisterCollectbyParam(String extraChannelId, String merId, String merOrderId) {
        return merchantRegisterCollectMapper.getMerchantRegisterCollectbyParam(extraChannelId,merId,merOrderId);
    }

    @Override
    public PayCardholderInfo getPayCardholderInfoByPayId(String payId) {
        return payCardholderInfoMapper.selectByPrimaryKey(payId);
    }

    @Override
    public AgentMerchantSetting getAgentMerchantSettingByParentIdAndPayType(String parentId, String payType) {
        return agentMerchantSettingMapper.getAgentMerchantSettingByParentIdAndPayType(parentId,payType);
    }

    @Transactional
    @Override
    public void insertMerchantWallet(MerchantWallet merchantWallet) {
            merchantWalletMapper.insertSelective(merchantWallet);
    }

    @Override
    public void insertChannelWallet(ChannelWallet channelWallet) {
        channelWalletMapper.insertSelective(channelWallet);
    }

    @Override
    public TransOrder getTransOrderOrderId(String id) {
        return transOrderMapper.selectByPrimaryKey(id);
    }

    @Transactional
    @Override
    public void insertAgentWallet(AgentWallet agentWallet) {
            agentWalletMapper.insertSelective(agentWallet);
    }

    @Override
    public AgentMerchantsDetails getAgentMerchantsDetails(String orderId) {
        return agentMerchantsDetailsMapper.searchByOrderId(orderId);
    }

    @Override
    public ChannelDetails getChannelDetails(String orderId) {
        return channelDetailsMapper.searchByOrderId(orderId);
    }

    @Override
    public TerminalMerchantsDetails getTerminalMerchantsDetails(String orderId) {
        return terminalMerchantsDetailsMapper.searchByOrderId(orderId);
    }

    @Override
    public MerchantsDetails getMerchantDetails(String orderId) {
        return merchantsDetailsMapper.getMerchantDetails(orderId);
    }

    @Override
    public TransOrder getTransOrderByMerOrderIdAndMerId(String merOrderId, String merId) {
        return transOrderMapper.getTransOrderByMerOrderIdAndMerId(merId,merOrderId);
    }
}
