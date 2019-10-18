package com.rxh.service.square;

import com.rxh.pojo.base.Page;
import com.rxh.pojo.base.PageResult;
import com.rxh.square.pojo.FinanceDrawing;

import java.util.List;
import java.util.Map;


public interface FinanceDrawingService {

    List<FinanceDrawing> search(FinanceDrawing financeDrawing);

    PageResult findByPage(Page page);

    Map<String, Object> getBalanceChangeInit();

    Boolean update(FinanceDrawing param);

    Boolean drawMoney(FinanceDrawing param);
}
