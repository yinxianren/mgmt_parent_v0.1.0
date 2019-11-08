package com.rxh.anew.wallet.impl;

import com.alibaba.fastjson.JSON;
import com.rxh.anew.CommonRPCComponent;
import com.rxh.anew.inner.InnerPrintLogObject;
import com.rxh.anew.service.PayWalletService;
import com.rxh.anew.table.agent.AgentMerchantSettingTable;
import com.rxh.anew.table.agent.AgentMerchantWalletTable;
import com.rxh.anew.table.agent.AgentMerchantsDetailsTable;
import com.rxh.anew.table.business.PayOrderInfoTable;
import com.rxh.anew.table.business.TransOrderInfoTable;
import com.rxh.anew.table.channel.ChannelDetailsTable;
import com.rxh.anew.table.channel.ChannelInfoTable;
import com.rxh.anew.table.channel.ChannelWalletTable;
import com.rxh.anew.table.merchant.MerchantInfoTable;
import com.rxh.anew.table.merchant.MerchantRateTable;
import com.rxh.anew.table.merchant.MerchantWalletTable;
import com.rxh.anew.table.merchant.MerchantsDetailsTable;
import com.rxh.anew.table.system.SystemOrderTrackTable;
import com.rxh.anew.table.terminal.TerminalMerchantsDetailsTable;
import com.rxh.anew.table.terminal.TerminalMerchantsWalletTable;
import com.rxh.anew.wallet.PayWalletComponent;
import com.rxh.enums.StatusEnum;
import com.rxh.exception.NewPayException;
import com.rxh.payInterface.NewPayAssert;
import com.rxh.tuple.Tuple2;
import lombok.AllArgsConstructor;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/28
 * Time: 下午5:12
 * Description:
 */
@AllArgsConstructor
@Component
public class PayWalletComponentComponentImpl implements PayWalletComponent, NewPayAssert {

    private final PayWalletService payWalletService;
    private final CommonRPCComponent commonRPCComponent;

    @Override
    public void payOrderWallet( ActiveMQObjectMessage objectMessage) {
        final String bussType = "快捷MQ队列--->收单业务钱包更新";
        InnerPrintLogObject ipo;
        PayOrderInfoTable poi = null;
        try {
            poi = (PayOrderInfoTable) objectMessage.getObject();
            //创建日志打印对象
            ipo = new InnerPrintLogObject(poi.getMerchantId(), poi.getTerminalMerId(),bussType);
            //获取商户信息
            MerchantInfoTable mit = payWalletService.getMerInfo(ipo);
            //获取商户产品费率
            MerchantRateTable mrt = payWalletService.getMerRate(ipo,poi.getProductId());
            //获取商户钱包
            MerchantWalletTable mwt = payWalletService.getMerWallet(ipo);
            //更新商户钱包 ,保存商户钱包明细
            Tuple2<MerchantWalletTable, MerchantsDetailsTable>  merWalletTuple = payWalletService.updateMerWalletByPayOrder(mwt,poi,mrt);
            //获取终端商户钱包
            TerminalMerchantsWalletTable tmw = payWalletService.getTerMerWallet(ipo);
            //更新终端商户钱包 保存终端商户钱包明细
            Tuple2<TerminalMerchantsWalletTable, TerminalMerchantsDetailsTable> terMerWalletTuple = payWalletService.updateTerMerWalletByPayOrder(tmw,poi,mrt);
            //获取通道信息
            ChannelInfoTable  cit = payWalletService.getChannelInfo(poi.getChannelId(),ipo);
            //获取通道钱包
            ChannelWalletTable cwt = payWalletService.getChanWallet(poi.getChannelId(),ipo);
            //更新通道钱包 保存通道钱包明细
            Tuple2<ChannelWalletTable, ChannelDetailsTable> chanWalletTuple = payWalletService.updateChannelWalletByPayOrder(cwt,cit,poi,mrt);
            //获取代理商设置
            AgentMerchantSettingTable ams = payWalletService.getAgentMerSet(mit.getAgentMerchantId(),poi.getProductId(),ipo);
            //获取代理商钱包
            AgentMerchantWalletTable amw = payWalletService.getAgentMerWallet(mit.getAgentMerchantId(),ipo);
            //更新代理商钱包 保存代理商钱包明细
            Tuple2<AgentMerchantWalletTable, AgentMerchantsDetailsTable> agentMerWalletTuple = payWalletService.updateAgentMerWalletByPayOrder(amw,ams,poi);
            //更新订单状态，从队列处理中该为成功
            poi = poi.setStatus(StatusEnum._0.getStatus());
            //执行事务处理
            commonRPCComponent.apiPayOrderBusinessTransactionService.updateOrSavePayOrderBussInfo(merWalletTuple,terMerWalletTuple,chanWalletTuple,agentMerWalletTuple,poi);
        }catch (Exception e){
            e.printStackTrace();
            if( !isNull(poi) ){
                poi = poi.setStatus(StatusEnum._8.getStatus());
                commonRPCComponent.apiPayOrderInfoService.updateByPrimaryKey(poi);
                commonRPCComponent.apiSystemOrderTrackService.save( new SystemOrderTrackTable()
                        .setRequestPath(bussType)
                        .setPlatformPrintLog( e instanceof NewPayException ? ((NewPayException)e).getInnerPrintMsg() : e.getMessage())
                        .setId(null)
                        .setMerId(poi.getMerchantId())
                        .setMerOrderId(poi.getMerOrderId())
                        .setPlatformOrderId(poi.getPlatformOrderId())
                        .setAmount(poi.getAmount())
                        .setReturnUrl(null)
                        .setNoticeUrl(null)
                        .setTradeCode(StatusEnum._8.getStatus())
                        .setRequestMsg(StatusEnum._8.getRemark())
                        .setReferPath(" public void payOrderWallet( ActiveMQObjectMessage objectMessage) ")
                        .setResponseResult(JSON.toJSONString(poi))
                        .setTradeTime(new Date())
                        .setCreateTime(new Date()));
            }
        }
    }


    @Override
    public void transOrderWallet( ActiveMQObjectMessage objectMessage) {
        final String bussType = "快捷MQ队列--->代付业务钱包更新";
        InnerPrintLogObject ipo;
        TransOrderInfoTable toit = null;
        try{
            toit = (TransOrderInfoTable) objectMessage.getObject();
            //创建日志打印对象
            ipo = new InnerPrintLogObject(toit.getMerchantId(), toit.getTerminalMerId(),bussType);
            //获取商户信息
            MerchantInfoTable mit = payWalletService.getMerInfo(ipo);
            //获取商户产品费率
            MerchantRateTable mrt = payWalletService.getMerRate(ipo,toit.getProductId());
            //获取商户钱包
            MerchantWalletTable mwt = payWalletService.getMerWallet(ipo);
            //更新商户钱包 ,保存商户钱包明细
            Tuple2<MerchantWalletTable, MerchantsDetailsTable>  merWalletTuple = payWalletService.updateMerWalletByTransOrder(mwt,toit,mrt);
            //获取终端商户钱包
            TerminalMerchantsWalletTable tmw = payWalletService.getTerMerWallet(ipo);
            //更新终端商户钱包 保存终端商户钱包明细
            Tuple2<TerminalMerchantsWalletTable, TerminalMerchantsDetailsTable> terMerWalletTuple = payWalletService.updateTerMerWalletByTransOrder(tmw,toit,mrt);
            //获取通道信息
            ChannelInfoTable  cit = payWalletService.getChannelInfo(toit.getChannelId(),ipo);
            //获取通道钱包
            ChannelWalletTable cwt = payWalletService.getChanWallet(toit.getChannelId(),ipo);
            //更新通道钱包 保存通道钱包明细
            Tuple2<ChannelWalletTable, ChannelDetailsTable> chanWalletTuple = payWalletService.updateChannelWalletByTransOrder(cwt,cit,toit,mrt);
            //获取代理商设置
            AgentMerchantSettingTable ams = payWalletService.getAgentMerSet(mit.getAgentMerchantId(),toit.getProductId(),ipo);
            //获取代理商钱包
            AgentMerchantWalletTable amw = payWalletService.getAgentMerWallet(mit.getAgentMerchantId(),ipo);
            //更新代理商钱包 保存代理商钱包明细
            Tuple2<AgentMerchantWalletTable, AgentMerchantsDetailsTable> agentMerWalletTuple = payWalletService.updateAgentMerWalletByTransOrder(amw,ams,toit);
            //更新订单状态，从队列处理中该为成功
            toit= toit.setStatus(StatusEnum._0.getStatus());
            //执行事务处理
            commonRPCComponent.apiPayOrderBusinessTransactionService.updateOrSaveTransOrderBussInfo(merWalletTuple,terMerWalletTuple,chanWalletTuple,agentMerWalletTuple,toit);
        }catch (Exception e){
            e.printStackTrace();
            if( !isNull(toit) ){
                toit = toit.setStatus(StatusEnum._8.getStatus());
                commonRPCComponent.apiTransOrderInfoService.updateById(toit);
                commonRPCComponent.apiSystemOrderTrackService.save( new SystemOrderTrackTable()
                        .setRequestPath(bussType)
                        .setPlatformPrintLog( e instanceof NewPayException ? ((NewPayException)e).getInnerPrintMsg() : e.getMessage())
                        .setId(null)
                        .setMerId(toit.getMerchantId())
                        .setMerOrderId(toit.getMerOrderId())
                        .setPlatformOrderId(toit.getPlatformOrderId())
                        .setAmount(toit.getAmount())
                        .setReturnUrl(null)
                        .setNoticeUrl(null)
                        .setTradeCode(StatusEnum._8.getStatus())
                        .setRequestMsg(StatusEnum._8.getRemark())
                        .setReferPath(" public void transOrderWallet( ActiveMQObjectMessage objectMessage) ")
                        .setResponseResult(JSON.toJSONString(toit))
                        .setTradeTime(new Date())
                        .setCreateTime(new Date()));
            }
        }
    }



}
