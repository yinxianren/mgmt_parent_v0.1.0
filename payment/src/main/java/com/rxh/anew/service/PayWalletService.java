package com.rxh.anew.service;

import com.rxh.anew.inner.InnerPrintLogObject;
import com.rxh.anew.table.agent.AgentMerchantSettingTable;
import com.rxh.anew.table.agent.AgentMerchantWalletTable;
import com.rxh.anew.table.agent.AgentMerchantsDetailsTable;
import com.rxh.anew.table.business.PayOrderInfoTable;
import com.rxh.anew.table.channel.ChannelDetailsTable;
import com.rxh.anew.table.channel.ChannelInfoTable;
import com.rxh.anew.table.channel.ChannelWalletTable;
import com.rxh.anew.table.merchant.MerchantInfoTable;
import com.rxh.anew.table.merchant.MerchantRateTable;
import com.rxh.anew.table.merchant.MerchantWalletTable;
import com.rxh.anew.table.merchant.MerchantsDetailsTable;
import com.rxh.anew.table.terminal.TerminalMerchantsDetailsTable;
import com.rxh.anew.table.terminal.TerminalMerchantsWalletTable;
import com.rxh.exception.NewPayException;
import com.rxh.tuple.Tuple2;

public interface PayWalletService {
    /**
     *  获取商户信息
     * @param ipo
     * @return
     */
    MerchantInfoTable getMerInfo(InnerPrintLogObject ipo) throws NewPayException;

    /**
     *
     * @param poi
     * @param ipo
     * @return
     */
    MerchantRateTable getMerRate(PayOrderInfoTable poi, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *
     * @param ipo
     * @return
     */
    MerchantWalletTable getMerWallet(InnerPrintLogObject ipo) throws NewPayException;

    /**
     *
     * @param mwt
     * @param poi
     * @param mrt
     * @return
     */
    Tuple2<MerchantWalletTable, MerchantsDetailsTable> updateMerWallet(MerchantWalletTable mwt, PayOrderInfoTable poi, MerchantRateTable mrt);

    /**
     *
     * @param ipo
     * @return
     */
    TerminalMerchantsWalletTable getTerMerWallet(InnerPrintLogObject ipo) throws NewPayException;

    /**
     *
     * @param tmw
     * @param poi
     * @return
     */
    Tuple2<TerminalMerchantsWalletTable, TerminalMerchantsDetailsTable> updateTerMerWallet(TerminalMerchantsWalletTable tmw, PayOrderInfoTable poi, MerchantRateTable mrt);

    /**
     *
     * @param channelId
     * @param ipo
     * @return
     */
    ChannelWalletTable getChanWallet(String channelId, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *
     * @param channelId
     * @param ipo
     * @return
     */
    ChannelInfoTable getChannelInfo(String channelId, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *
     * @param cwt
     * @param cit
     * @param poi
     * @return
     */
    Tuple2<ChannelWalletTable, ChannelDetailsTable> updateChannelWallet(ChannelWalletTable cwt, ChannelInfoTable cit, PayOrderInfoTable poi,MerchantRateTable mrt);

    /**
     *
     * @param agentMerchantId
     * @param ipo
     * @return
     */
    AgentMerchantSettingTable getAgentMerSet(String agentMerchantId, String  productId,InnerPrintLogObject ipo) throws NewPayException;

    /**
     *
     * @param agentMerchantId
     * @param ipo
     * @return
     */
    AgentMerchantWalletTable getAgentMerWallet(String agentMerchantId, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *
     * @param amw
     * @param ams
     * @param poi
     * @return
     */
    Tuple2<AgentMerchantWalletTable, AgentMerchantsDetailsTable> updateAgentMerWallet(AgentMerchantWalletTable amw, AgentMerchantSettingTable ams, PayOrderInfoTable poi);
}
