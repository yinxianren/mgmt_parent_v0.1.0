package com.rxh.service;

import com.rxh.pojo.base.Page;
import com.rxh.pojo.base.PageResult;
import com.rxh.pojo.sys.SysArea;
import com.rxh.pojo.sys.SysLog;

import java.util.List;
import java.util.Map;


public interface MerchantSystemService {
    void saveSystemLog(SysLog log);

    PageResult getSystemLog(Page page);

    List<SysArea> getAreaInfoByCountryCode(String countryCode);

    /**
     *  获取建议与意见的初始值
     * @return
     */
    Map<String, Object> getMerchantOpinionInit();


    String getLastId(String tableName);
}
