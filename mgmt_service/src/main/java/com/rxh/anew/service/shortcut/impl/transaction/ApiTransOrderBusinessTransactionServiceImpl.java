package com.rxh.anew.service.shortcut.impl.transaction;

import com.rxh.anew.service.db.business.PayOrderInfoDBService;
import com.rxh.anew.service.db.business.TransOrderInfoDBService;
import com.rxh.anew.table.business.PayOrderInfoTable;
import com.rxh.anew.table.business.TransOrderInfoTable;
import com.rxh.service.anew.transaction.ApiTransOrderBusinessTransactionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/11/6
 * Time: 下午9:24
 * Description:
 */
@Transactional(rollbackFor= Exception.class)
@AllArgsConstructor
@Service
public class ApiTransOrderBusinessTransactionServiceImpl implements ApiTransOrderBusinessTransactionService {

    private final PayOrderInfoDBService payOrderInfoDBService;
    private final TransOrderInfoDBService transOrderInfoDBService;

    @Override
    public void updateByPayOrderCorrelationInfo(TransOrderInfoTable transOrderInfoTable, List<PayOrderInfoTable> payOrderInfoTableList) {
        transOrderInfoDBService.updateById(transOrderInfoTable);
        payOrderInfoDBService.updateBatchById(payOrderInfoTableList);
    }

}
