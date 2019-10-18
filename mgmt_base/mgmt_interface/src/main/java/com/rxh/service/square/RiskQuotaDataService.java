package com.rxh.service.square;

import com.rxh.square.pojo.RiskQuotaData;

import java.util.List;

public interface RiskQuotaDataService {

    List<RiskQuotaData> getAll();

    List<RiskQuotaData> getRiskQuotaDataByWhereCondition(RiskQuotaData riskQuotaData);

    Integer getMaxId();
}
