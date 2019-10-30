package com.rxh.service.anew.transaction;

import com.rxh.anew.table.agent.AgentMerchantWalletTable;
import com.rxh.anew.table.agent.AgentMerchantsDetailsTable;
import com.rxh.anew.table.business.PayOrderInfoTable;
import com.rxh.anew.table.channel.ChannelDetailsTable;
import com.rxh.anew.table.channel.ChannelHistoryTable;
import com.rxh.anew.table.channel.ChannelWalletTable;
import com.rxh.anew.table.merchant.MerchantWalletTable;
import com.rxh.anew.table.merchant.MerchantsDetailsTable;
import com.rxh.anew.table.system.RiskQuotaTable;
import com.rxh.anew.table.terminal.TerminalMerchantsDetailsTable;
import com.rxh.anew.table.terminal.TerminalMerchantsWalletTable;
import com.rxh.tuple.Tuple2;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/28
 * Time: 下午1:45
 * Description:
 */
public interface ApiPayOrderBusinessTransactionService {

    /**
     *
     * @param pit
     * @param cht
     * @param rqtSet
     */
    void updateByPayOrderCorrelationInfo(PayOrderInfoTable pit, ChannelHistoryTable cht, Set<RiskQuotaTable> rqtSet);

    /**
     *
     * @param merWalletTuple
     * @param terMerWalletTuple
     * @param chanWalletTuple
     * @param agentMerWalletTuple
     * @param poi
     */
    void updateOrSavePayOrderBussInfo(Tuple2<MerchantWalletTable, MerchantsDetailsTable> merWalletTuple, Tuple2<TerminalMerchantsWalletTable, TerminalMerchantsDetailsTable> terMerWalletTuple, Tuple2<ChannelWalletTable, ChannelDetailsTable> chanWalletTuple, Tuple2<AgentMerchantWalletTable, AgentMerchantsDetailsTable> agentMerWalletTuple, PayOrderInfoTable poi);
}
