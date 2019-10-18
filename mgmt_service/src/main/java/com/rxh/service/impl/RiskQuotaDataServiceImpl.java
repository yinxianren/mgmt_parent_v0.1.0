package com.rxh.service.impl;

import com.rxh.mapper.square.RiskSquareQuotaDataMapper;
import com.rxh.service.square.RiskQuotaDataService;
import com.rxh.square.pojo.RiskQuotaData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class RiskQuotaDataServiceImpl implements RiskQuotaDataService {

    @Resource
    private RiskSquareQuotaDataMapper riskSquareQuotaDataMapper;


    @Override
    public List<RiskQuotaData> getAll() {
        return riskSquareQuotaDataMapper.selectByExample(null);
    }

    @Override
    public List<RiskQuotaData> getRiskQuotaDataByWhereCondition(RiskQuotaData riskQuotaData) {
        return riskSquareQuotaDataMapper.selectRiskQuotaDataByWhereCondition(riskQuotaData);
    }

    @Override
    public Integer getMaxId() {
        return riskSquareQuotaDataMapper.selectMaxId();
    }
}
