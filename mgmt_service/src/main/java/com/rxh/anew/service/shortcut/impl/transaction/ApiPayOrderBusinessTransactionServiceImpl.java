package com.rxh.anew.service.shortcut.impl.transaction;

import com.rxh.anew.service.db.agent.AgentMerchantWalletDBService;
import com.rxh.anew.service.db.agent.AgentMerchantsDetailsDBService;
import com.rxh.anew.service.db.business.PayOrderInfoDBService;
import com.rxh.anew.service.db.channel.ChannelDetailsDbService;
import com.rxh.anew.service.db.channel.ChannelHistoryDbService;
import com.rxh.anew.service.db.channel.ChannelWalletDbService;
import com.rxh.anew.service.db.merchant.MerchantWalletDBService;
import com.rxh.anew.service.db.merchant.MerchantsDetailsDBService;
import com.rxh.anew.service.db.system.RiskQuotaDBService;
import com.rxh.anew.service.db.terminal.TerminalMerchantsDetailsDBService;
import com.rxh.anew.service.db.terminal.TerminalMerchantsWalletDBservice;
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
import com.rxh.service.anew.transaction.ApiPayOrderBusinessTransactionService;
import com.rxh.tuple.Tuple2;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;


/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/28
 * Time: 下午1:50
 * Description:
 */

@Transactional(rollbackFor= Exception.class)
@AllArgsConstructor
@Service
public class ApiPayOrderBusinessTransactionServiceImpl implements ApiPayOrderBusinessTransactionService {

    private final PayOrderInfoDBService payOrderInfoDBService;
    private final ChannelHistoryDbService channelHistoryDbService;
    private final RiskQuotaDBService riskQuotaDBService;

    private final MerchantWalletDBService merchantWalletDBService;
    private final MerchantsDetailsDBService merchantsDetailsDBService;

    private final TerminalMerchantsWalletDBservice terminalMerchantsWalletDBservice;
    private final TerminalMerchantsDetailsDBService terminalMerchantsDetailsDBService;

    private final ChannelWalletDbService channelWalletDbService;
    private final ChannelDetailsDbService channelDetailsDbService;

    private final AgentMerchantWalletDBService agentMerchantWalletDBService;
    private final AgentMerchantsDetailsDBService agentMerchantsDetailsDBService;

    @Override
    public void updateByPayOrderCorrelationInfo(PayOrderInfoTable pit, ChannelHistoryTable cht, Set<RiskQuotaTable> rqtSet) {
        payOrderInfoDBService.updateById(pit);
        channelHistoryDbService.saveOrUpdate(cht);
        riskQuotaDBService.saveOrUpdateBatch(rqtSet);
    }

    @Override
    public void updateOrSavePayOrderBussInfo(
            Tuple2<MerchantWalletTable, MerchantsDetailsTable> merWalletTuple,
            Tuple2<TerminalMerchantsWalletTable, TerminalMerchantsDetailsTable> terMerWalletTuple,
            Tuple2<ChannelWalletTable, ChannelDetailsTable> chanWalletTuple,
            Tuple2<AgentMerchantWalletTable, AgentMerchantsDetailsTable> agentMerWalletTuple,
            PayOrderInfoTable poi) {
        //商户
        merchantWalletDBService.saveOrUpdate(merWalletTuple._1);
        merchantsDetailsDBService.save(merWalletTuple._2);
        //终端商户
        terminalMerchantsWalletDBservice.saveOrUpdate(terMerWalletTuple._1);
        terminalMerchantsDetailsDBService.save(terMerWalletTuple._2);
        //通道
        channelWalletDbService.saveOrUpdate(chanWalletTuple._1);
        channelDetailsDbService.save(chanWalletTuple._2);
        //终端商户
        agentMerchantWalletDBService.saveOrUpdate(agentMerWalletTuple._1);
        agentMerchantsDetailsDBService.save(agentMerWalletTuple._2);
        //更新订单
        payOrderInfoDBService.updateById(poi);
    }

}
