package com.rxh.service.anew.transaction;

import com.rxh.anew.table.business.PayOrderInfoTable;
import com.rxh.anew.table.channel.ChannelHistoryTable;
import com.rxh.anew.table.system.RiskQuotaTable;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/28
 * Time: 下午1:45
 * Description:
 */
public interface ApiPayOrderBusinessTransactionService {


    void updateByPayOrderCorrelationInfo(PayOrderInfoTable pit, ChannelHistoryTable cht, Set<RiskQuotaTable> rqtSet);
}
