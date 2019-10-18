package com.rxh.service.impl.wallet;

import com.rxh.mapper.square.*;
import com.rxh.service.wallet.HaiYiPayWalletService;
import com.rxh.square.pojo.*;
import com.rxh.utils.PayMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 */
@Transactional(rollbackFor=Exception.class)
@Service
public class HaiYiPayWalletServiceImpl implements HaiYiPayWalletService {

    @Autowired
    private TransOrderMapper transOrderMapper; //订单
    @Autowired
    private MerchantsDetailsMapper merchantsDetailsMapper;//商户钱包明细
    @Autowired
    private AgentMerchantsDetailsMapper agentMerchantsDetailsMapper;//代理商钱包明细
    @Autowired
    private MerchantWalletMapper merchantWalletMapper;//商户钱包
    @Autowired
    private AgentWalletMapper agentWalletMapper;//代理商钱包
    @Autowired
    private TransAuditMapper transAuditMapper; //审计表
    @Autowired
    private ChannelWalletMapper channelWalletMapper;
    @Autowired
    private ChannelDetailsMapper channelDetailsMapper;
    @Autowired
    private TransBankInfoMapper transBankInfoMapper;
    @Autowired
    private RiskSquareQuotaDataMapper riskSquareQuotaDataMapper;


    @Override
    public Integer updateHaiYiPaySuccessTrandsOrderDetail(PayMap<String, Object> payMap) {

        int count=0;
        //1.更新订单状态为成功
        TransOrder transOrder= (TransOrder) payMap.get("transOrder");
        if(null != transOrder )count += transOrderMapper.updateOrderStatus(transOrder.getTransId(),transOrder.getOrderStatus());

        //2.商户钱包
        MerchantsDetails merchantsDetails= (MerchantsDetails) payMap.get("merchantsDetails");
        MerchantWallet merchantWallet= (MerchantWallet) payMap.get("merchantWallet");
        if (null != merchantsDetails ) count += merchantsDetailsMapper.insert(merchantsDetails);
        if(null != merchantWallet)  count += merchantWalletMapper.updateByPrimaryKey(merchantWallet);

        //3.代理钱包
        AgentWallet insertAgentWallet= (AgentWallet) payMap.get("insertAgentWallet");
        AgentWallet updateAgentWallet= (AgentWallet) payMap.get("updateAgentWallet");
        AgentMerchantsDetails agentMerchantsDetails= (AgentMerchantsDetails) payMap.get("agentMerchantsDetails");
        if(null != insertAgentWallet) count += agentWalletMapper.insert(insertAgentWallet);
        if(null != updateAgentWallet) count += agentWalletMapper.updateByPrimaryKey(updateAgentWallet);
        if(null != agentMerchantsDetails ) count += agentMerchantsDetailsMapper.insert(agentMerchantsDetails);
        //4.通道钱包
        ChannelDetails channelDetails= (ChannelDetails)payMap.get("channelDetails");
        ChannelWallet insertChannelWallet=(ChannelWallet)payMap.get("insertChannelWallet");
        ChannelWallet updateChannelWallet=(ChannelWallet)payMap.get("updateChannelWallet");
        if(null != insertChannelWallet)  count +=  channelWalletMapper.insertSelective(insertChannelWallet);
        if(null != updateChannelWallet)  count += channelWalletMapper.updateByPrimaryKey(updateChannelWallet);
        if(null !=channelDetails )count +=  channelDetailsMapper.insertSelective(channelDetails);

        return count;
    }

    @Override
    public  void updateHaiYiPayFailTrandsOrderDetail(TransOrder transOrder, MerchantWallet merchantWallet) {
        //1. 更新订单状态为失败
        transOrderMapper.updateOrderStatus(transOrder.getTransId(),transOrder.getOrderStatus());
        //2. 更新商户钱包
        merchantWalletMapper.updateByPrimaryKey(merchantWallet);
    }


    @Override
    public Integer updateHaiYiPayTrandsOrderAndTransAudit(PayMap<String, Object> map) {
        int count=0;
        TransAudit   transAudit= (TransAudit) map.get("transAudit");
        TransOrder transOrder= (TransOrder) map.get("transOrder");
        MerchantWallet merchantWallet= (MerchantWallet) map.get("merchantWallet");
        ChannelWallet channelWallet= (ChannelWallet) map.get("channelWallet");
        List<RiskQuotaData> quotaDataList= (List<RiskQuotaData>) map.get("riskQuotaDataList");

        if(null != transAudit)  count += transAuditMapper.updateByPrimaryKeySelective(transAudit);
        if(null != transOrder)  count += transOrderMapper.updateByPrimaryKey(transOrder);
        if(null != merchantWallet)  count += merchantWalletMapper.updateByPrimaryKey(merchantWallet);
        if(null != channelWallet) count += channelWalletMapper.updateByPrimaryKey(channelWallet);
        if(null !=quotaDataList && quotaDataList.size()>0)  count += riskSquareQuotaDataMapper.insertOrUpdateRiskQuotaData(quotaDataList);
        return count;
    }

    @Override
    public Integer saveTransOrderInfo(PayMap<String, Object> map) {
        int count=0;
        TransAudit   transAudit= (TransAudit) map.get("transAudit");
        TransOrder transOrder= (TransOrder) map.get("transOrder");
        TransBankInfo transBankInfo= (TransBankInfo) map.get("transBankInfo");

        if(null != transAudit)   count += transAuditMapper.insertSelective(transAudit);
        if(null != transOrder)  count += transOrderMapper.insertSelective(transOrder);
        if(null != transBankInfo)  count += transBankInfoMapper.insertSelective(transBankInfo);

        return count;
    }


}
