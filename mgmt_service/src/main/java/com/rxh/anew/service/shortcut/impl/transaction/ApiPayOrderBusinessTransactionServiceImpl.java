package com.rxh.anew.service.shortcut.impl.transaction;

import com.rxh.anew.service.db.business.PayOrderInfoDBService;
import com.rxh.anew.service.db.channel.ChannelHistoryDbService;
import com.rxh.anew.service.db.system.RiskQuotaDBService;
import com.rxh.anew.table.business.PayOrderInfoTable;
import com.rxh.anew.table.channel.ChannelHistoryTable;
import com.rxh.anew.table.system.RiskQuotaTable;
import com.rxh.service.anew.transaction.ApiPayOrderBusinessTransactionService;
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

    @Override
    public void updateByPayOrderCorrelationInfo(PayOrderInfoTable pit, ChannelHistoryTable cht, Set<RiskQuotaTable> rqtSet) {
        payOrderInfoDBService.updateById(pit);
        channelHistoryDbService.saveOrUpdate(cht);
        riskQuotaDBService.saveOrUpdateBatch(rqtSet);
    }

}
