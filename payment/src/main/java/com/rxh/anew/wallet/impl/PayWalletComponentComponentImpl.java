package com.rxh.anew.wallet.impl;

import com.rxh.anew.inner.InnerPrintLogObject;
import com.rxh.anew.service.PayWalletService;
import com.rxh.anew.table.business.PayOrderInfoTable;
import com.rxh.anew.table.merchant.MerchantInfoTable;
import com.rxh.anew.table.merchant.MerchantRateTable;
import com.rxh.anew.table.merchant.MerchantWalletTable;
import com.rxh.anew.table.merchant.MerchantsDetailsTable;
import com.rxh.anew.table.system.MerchantSettingTable;
import com.rxh.anew.table.terminal.TerminalMerchantsDetailsTable;
import com.rxh.anew.table.terminal.TerminalMerchantsWalletTable;
import com.rxh.anew.wallet.PayWalletComponent;
import com.rxh.cache.redis.MerchantSettingCache;
import com.rxh.payInterface.NewPayAssert;
import com.rxh.tuple.Tuple2;
import lombok.AllArgsConstructor;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.springframework.stereotype.Component;

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

    @Override
    public void payOrderWallet( ActiveMQObjectMessage objectMessage) {
        final String bussType = "收单业务钱包更新";
        InnerPrintLogObject ipo;
        try {
            PayOrderInfoTable poi = (PayOrderInfoTable) objectMessage.getObject();
            //创建日志打印对象
            ipo = new InnerPrintLogObject(poi.getMerchantId(), poi.getTerminalMerId(),bussType);
            //获取商户信息
            MerchantInfoTable mit = payWalletService.getMerInfo(ipo);
            //获取商户产品费率
            MerchantRateTable mrt = payWalletService.getMerRate(poi,ipo);
            //获取商户钱包
            MerchantWalletTable mwt = payWalletService.getMerWallet(ipo);
            //更新商户钱包 ,保存商户钱包明细
            Tuple2<MerchantWalletTable, MerchantsDetailsTable>  merWalletTuple = payWalletService.updateMerWallet(mwt,poi,mrt);
            //获取终端商户钱包
            TerminalMerchantsWalletTable tmw = payWalletService.getTerMerWallet(ipo);
            //更新终端商户钱包 保存终端商户钱包明细
            Tuple2<TerminalMerchantsWalletTable, TerminalMerchantsDetailsTable> terMerWalletTuple = payWalletService.updateTerMerWallet(tmw,poi,mrt);
            //获取通道钱包

            //更新通道钱包 保存通道钱包明细


            //更新代理商钱包 保存代理商钱包明细


        }catch (Exception e){

        }
    }


    @Override
    public void transOrderWallet( ActiveMQObjectMessage objectMessage) {

    }



}
