package com.rxh.service.square;


import com.rxh.pojo.base.Page;
import com.rxh.pojo.base.PageResult;
import com.rxh.square.pojo.TerminalMerchantsDetails;

import java.util.List;

public interface TerminalMerchantsDetailsService {

    PageResult terminalMerchantsDetails(Page page);

    List<TerminalMerchantsDetails> getIds();
}
