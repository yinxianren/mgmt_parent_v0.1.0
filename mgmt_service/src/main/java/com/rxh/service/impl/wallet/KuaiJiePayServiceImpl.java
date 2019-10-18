package com.rxh.service.impl.wallet;

import com.rxh.mapper.square.*;
import com.rxh.service.wallet.KuaiJiePayService;
import com.rxh.square.pojo.*;
import com.rxh.utils.PayMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(rollbackFor=Exception.class)
@Service
public class KuaiJiePayServiceImpl implements KuaiJiePayService {

    @Autowired
    private PayOrderMapper payOrderMapper;
    @Autowired
    private PayCardholderInfoMapper payCardholderInfoMapper;
    @Autowired
    private PayProductDetailMapper  payProductDetailMapper;
    @Autowired
    private RiskSquareQuotaDataMapper riskSquareQuotaDataMapper;
    @Autowired
    private MerchantChannelHistoryMapper merchantChannelHistoryMapper;
    @Autowired
    private TerminalMerchantsDetailsMapper terminalMerchantsDetailsMapper;
    @Autowired
    private TerminalMerchantsWalletMapper terminalMerchantsWalletMapper;
    @Autowired
    private AgentMerchantsDetailsMapper agentMerchantsDetailsMapper;
    @Autowired
    private AgentWalletMapper agentWalletMapper;
    @Autowired
    private MerchantsDetailsMapper merchantsDetailsMapper;
    @Autowired
    private MerchantWalletMapper merchantWalletMapper;
    @Autowired
    private ChannelDetailsMapper channelDetailsMapper;
    @Autowired
    private ChannelWalletMapper channelWalletMapper;
    @Autowired
    private TransBankInfoMapper transBankInfoMapper;
    @Autowired
    private TransOrderMapper transOrderMapper;
    @Autowired
    private TransAuditMapper transAuditMapper;

    @Override
    public Integer saveAndUpdateTransPayRecord(PayOrder payOrder, PayCardholderInfo payCardholderInfo, PayProductDetail payProductDetail, List<RiskQuotaData> riskQuotaDataList) {
        int count=0;
        //1.保存订单
        if(null !=payOrder )  count=payOrderMapper.insertSelective(payOrder);
        //2.保存代收付款人信息表
        if( null !=payCardholderInfo )  count+=payCardholderInfoMapper.insertSelective(payCardholderInfo);
        //3.保存支付产品细节
        if( null !=payProductDetail )  count+=payProductDetailMapper.insertSelective(payProductDetail);
        //4.更新风控
        if( null !=riskQuotaDataList ) count+=riskSquareQuotaDataMapper.insertOrUpdateRiskQuotaData(riskQuotaDataList);
        return count;
    }

    @Override
    public Integer saveAndUpdateTransOrderAndTransAudit(TransOrder transOrder, TransAudit transAudit) {
        int count=0;
        if(null != transOrder) count += transOrderMapper.updateByPrimaryKey(transOrder);
        if(null != transAudit) count += transAuditMapper.updateByPrimaryKey(transAudit);
        return count;
    }

    @Override
    public Integer saveAndUpdatePayOrderRecord(PayMap<String, Object> payMap) {
        int count = 0;
        //商户钱包
        MerchantWallet insertMerchantWallet=(MerchantWallet) payMap.get("insertMerchantWallet");
        MerchantWallet updateMerchantWallet=(MerchantWallet) payMap.get("updateMerchantWallet");
        MerchantsDetails merchantsDetails=(MerchantsDetails)payMap.get("merchantsDetails");
        if(null != insertMerchantWallet)  count +=  merchantWalletMapper.insertSelective(insertMerchantWallet);
        if(null != updateMerchantWallet)  count +=  merchantWalletMapper.updateByPrimaryKey(updateMerchantWallet);
        if(null != merchantsDetails) count +=  merchantsDetailsMapper.insertSelective(merchantsDetails);
        //通道钱包
        ChannelWallet insertChannelWallet=(ChannelWallet)payMap.get("insertChannelWallet");
        ChannelWallet updateChannelWallet=(ChannelWallet)payMap.get("updateChannelWallet");
        ChannelDetails channelDetails= (ChannelDetails)payMap.get("channelDetails");
        if(null != insertChannelWallet)  count +=  channelWalletMapper.insertSelective(insertChannelWallet);
        if(null != updateChannelWallet)  count += channelWalletMapper.updateByPrimaryKey(updateChannelWallet);
        if(null != channelDetails)  count +=  channelDetailsMapper.insertSelective(channelDetails);
        ///获取终端商户钱包
        TerminalMerchantsWallet insertTerminalMerchantsWallet =(TerminalMerchantsWallet)payMap.get("insertTerminalMerchantsWallet");
        TerminalMerchantsWallet updateTerminalMerchantsWallet=(TerminalMerchantsWallet)payMap.get("updateTerminalMerchantsWallet");
        TerminalMerchantsDetails terminalMerchantsDetails =(TerminalMerchantsDetails)payMap.get("terminalMerchantsDetails");
        if(null != insertTerminalMerchantsWallet) count += terminalMerchantsWalletMapper.insertSelective(insertTerminalMerchantsWallet);
        if(null != updateTerminalMerchantsWallet) count += terminalMerchantsWalletMapper.updateByPrimaryKey(updateTerminalMerchantsWallet);
        if(null != terminalMerchantsDetails) count = terminalMerchantsDetailsMapper.insertSelective(terminalMerchantsDetails);
        //代理商钱包
        AgentWallet  insertAgentWallet= (AgentWallet)payMap.get("insertAgentWallet");
        AgentWallet  updateAgentWallet=(AgentWallet)payMap.get("updateAgentWallet");
        AgentMerchantsDetails agentMerchantsDetails =(AgentMerchantsDetails) payMap.get("agentMerchantsDetails");
        if( null != insertAgentWallet) count += agentWalletMapper.insertSelective(insertAgentWallet);
        if( null != updateAgentWallet)  count +=  agentWalletMapper.updateByPrimaryKey(updateAgentWallet);
        if( null != agentMerchantsDetails)  count += agentMerchantsDetailsMapper.insert(agentMerchantsDetails);
        PayOrder payOrder= (PayOrder) payMap.get("payOrder");
        if(null != payOrder)  count += payOrderMapper.updateByPrimaryKey(payOrder);
        return count;
    }

    @Override
    public Integer saveAndUpdateTransOrderRecord(PayMap<String, Object> payMap) {
        int count = 0;
        MerchantsDetails merchantsDetails= (MerchantsDetails) payMap.get("merchantsDetails");
        MerchantWallet merchantWallet= (MerchantWallet) payMap.get("merchantWallet");
        if( null != merchantsDetails)   count +=  merchantsDetailsMapper.insertSelective(merchantsDetails);
        if( null != merchantWallet)   count +=  merchantWalletMapper.updateByPrimaryKey(merchantWallet);

        ChannelDetails channelDetails= (ChannelDetails) payMap.get("channelDetails");
        ChannelWallet channelWallet= (ChannelWallet) payMap.get("channelWallet");
        if( null != channelDetails )  count +=  channelDetailsMapper.insertSelective(channelDetails);
        if( null != channelWallet ) count += channelWalletMapper.updateByPrimaryKey(channelWallet);

        TerminalMerchantsDetails terminalMerchantsDetails= (TerminalMerchantsDetails) payMap.get("terminalMerchantsDetails");
        TerminalMerchantsWallet terminalMerchantsWallet= (TerminalMerchantsWallet) payMap.get("terminalMerchantsWallet");
        if( null != terminalMerchantsDetails )    count = terminalMerchantsDetailsMapper.insertSelective(terminalMerchantsDetails);
        if( null != terminalMerchantsWallet ) count += terminalMerchantsWalletMapper.updateByPrimaryKey(terminalMerchantsWallet);

        AgentWallet agentWallet= (AgentWallet) payMap.get("agentWallet");
        AgentMerchantsDetails agentMerchantsDetails= (AgentMerchantsDetails) payMap.get("agentMerchantsDetails");
        if( null !=  agentWallet ) count +=  agentWalletMapper.updateByPrimaryKey(agentWallet);
        if( null !=  agentMerchantsDetails )   count += agentMerchantsDetailsMapper.insertSelective(agentMerchantsDetails);

        TransOrder transOrder= (TransOrder) payMap.get("transOrder");
        PayOrder payOrder= (PayOrder) payMap.get("payOrder");

        if(null != transOrder ) count += transOrderMapper.updateByPrimaryKey(transOrder);
        if(null != payOrder )  count += payOrderMapper.updateByPrimaryKey(payOrder);
        return count;
    }

    @Override
    public Integer saveTransOrderPayRecord(PayMap<String, Object> payMap) {
        int count = 0;
        TransBankInfo transBankInfo= (TransBankInfo) payMap.get("transBankInfo");
        TransOrder transOrder= (TransOrder) payMap.get("transOrder");
        TransAudit transAudit= (TransAudit) payMap.get("transAudit");
        if(null != transBankInfo) count += transBankInfoMapper.insertSelective(transBankInfo);
        if(null != transOrder) count += transOrderMapper.insertSelective(transOrder);
        if(null != transAudit) count += transAuditMapper.insertSelective(transAudit);
        return count;
    }


    @Override
    public Integer saveAndUpdateWalletsAndRecord(PayMap<String, Object> payMap) {
        int count=0;
        PayOrder payOrder= (PayOrder) payMap.get("payOrder");
        List<RiskQuotaData>  riskQuotaDataList= (List<RiskQuotaData>) payMap.get("riskQuotaDataList");
        MerchantChannelHistory merchantChannelHistory= (MerchantChannelHistory) payMap.get("merchantChannelHistory");
        if( null != payOrder )   count += payOrderMapper.updateByPrimaryKey(payOrder);
        if( null != riskQuotaDataList )    count +=  riskSquareQuotaDataMapper.insertOrUpdateRiskQuotaData(riskQuotaDataList);
        if( null != merchantChannelHistory )  count +=merchantChannelHistoryMapper.insert(merchantChannelHistory);
        return count;
    }


}
