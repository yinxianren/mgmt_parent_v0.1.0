package com.rxh.service.impl.wallet;

import com.rxh.mapper.square.*;
import com.rxh.service.square.PaymentRecordSquareService;
import com.rxh.service.wallet.AllinPayWalletService;
import com.rxh.square.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(rollbackFor=Exception.class)
@Service
public class AllinPayWalletServiceImpl implements AllinPayWalletService {
    @Autowired
    private TerminalMerchantsDetailsMapper terminalMerchantsDetailsMapper;
    @Autowired
    private TerminalMerchantsWalletMapper terminalMerchantsWalletMapper;
    @Autowired
    private ChannelWalletMapper channelWalletMapper;
    @Autowired
    private ChannelDetailsMapper channelDetailsMapper;
    @Autowired
    private AgentMerchantsDetailsMapper agentMerchantsDetailsMapper;
    @Autowired
    private AgentWalletMapper agentWalletMapper;
    @Autowired
    private MerchantWalletMapper merchantWalletMapper;
    @Autowired
    private MerchantsDetailsMapper merchantsDetailsMapper;
    @Autowired
    private PaymentRecordSquareService paymentRecordSquareService;
    @Autowired
    private TransOrderMapper transOrderMapper;

    @Override
    public void updateAllinPayWallet(AgentWallet agentWallet, MerchantWallet merchantWallet, ChannelWallet channelWallet, TerminalMerchantsWallet terminalMerchantsWallet,TransOrder transOrder,
                                     MerchantsDetails merchantsDetails, TerminalMerchantsDetails terminalMerchantsDetails, AgentMerchantsDetails agentMerchantsDetails,ChannelDetails channelDetails) {
           //代理商钱包
           if (agentWallet != null){

               agentWalletMapper.updateByPrimaryKey(agentWallet);
               //代理商钱包明细
               agentMerchantsDetailsMapper.insert(agentMerchantsDetails);
           };
           //商户钱包
           if (merchantWallet != null){

               merchantWalletMapper.updateByPrimaryKey(merchantWallet);
               //商户钱包明细
               merchantsDetailsMapper.insert(merchantsDetails);
           };
           //平台钱包
           if (channelWallet != null){

               channelWalletMapper.updateByPrimaryKey(channelWallet);
               //平台钱包明细
               channelDetailsMapper.insert(channelDetails);
           };
           //子商户钱包
           if (terminalMerchantsWallet != null){

               if (transOrder.getTerminalMerId()!=null&&!transOrder.getTerminalMerId().equals("")){
                   TerminalMerchantsWallet terminalMerchantsWallet1=paymentRecordSquareService.getTerminalMerchantsWallet(transOrder.getMerId(),transOrder.getTerminalMerId());
                   if (terminalMerchantsWallet1==null){
                       terminalMerchantsWalletMapper.insert(terminalMerchantsWallet);
                   }else {
                       terminalMerchantsWalletMapper.updateByPrimaryKey(terminalMerchantsWallet);
                   }
                   //子商户钱包明细
                   terminalMerchantsDetailsMapper.insert(terminalMerchantsDetails);
               }

           };
           //更新订单状态
           transOrderMapper.updateByPrimaryKey(transOrder);
    }
}
