package com.rxh.service.trading;

import com.rxh.pojo.base.Page;
import com.rxh.pojo.base.PageResult;
import com.rxh.square.pojo.TransAudit;


/**
 * @author ：zoe
 * @Date ：Created in 2019/5/19 16:00
 */
public interface TransAuditService {

    PageResult findTransAudit(Page page);

    TransAudit findTransAuditByTransId(String transId);
}
