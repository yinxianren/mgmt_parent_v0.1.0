package com.rxh.anew.service.shortcut.impl.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rxh.anew.service.db.system.RiskQuotaDBService;
import com.rxh.anew.table.system.RiskQuotaTable;
import com.rxh.payInterface.NewPayAssert;
import com.rxh.service.anew.system.ApiRiskQuotaService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/25
 * Time: 上午10:57
 * Description:
 */
@AllArgsConstructor
@Service
public class ApiRiskQuotaServiceImpl implements ApiRiskQuotaService , NewPayAssert {

  private final RiskQuotaDBService  riskQuotaDBService;

    @Override
    public List<RiskQuotaTable> getListByTimeType(Set<String> timeTypeSet, RiskQuotaTable rq) {
        if(isHasNotElement(timeTypeSet)) return null;
        LambdaQueryWrapper<RiskQuotaTable> lambdaQueryWrapper = new QueryWrapper<RiskQuotaTable>()
                .lambda().in(RiskQuotaTable::getTimeType,timeTypeSet);
        if( isBlank(rq.getMeridChannelid()) )  lambdaQueryWrapper.eq(RiskQuotaTable::getMeridChannelid,rq.getMeridChannelid());
        if( isBlank(rq.getBussType()) )  lambdaQueryWrapper.eq(RiskQuotaTable::getBussType,rq.getBussType());
        return riskQuotaDBService.list(lambdaQueryWrapper);
    }

    @Override
    public List<RiskQuotaTable> getListByChMerId(Set<String> meridChannelidSet, RiskQuotaTable rq) {
        if(isHasNotElement(meridChannelidSet)) return null;
        LambdaQueryWrapper<RiskQuotaTable> lambdaQueryWrapper = new QueryWrapper<RiskQuotaTable>()
                .lambda().in(RiskQuotaTable::getMeridChannelid,meridChannelidSet);
        if( isBlank(rq.getBussType()) )  lambdaQueryWrapper.eq(RiskQuotaTable::getBussType,rq.getBussType());
        return riskQuotaDBService.list(lambdaQueryWrapper);
    }

    @Override
    public boolean save(RiskQuotaTable rq) {
        if(isNull(rq)) return  false;
        return riskQuotaDBService.save(rq);
    }

    @Override
    public boolean updateByPrimaryKey(RiskQuotaTable rq) {
        if(isNull(rq)) return  false;
        return riskQuotaDBService.updateById(rq);
    }
}
