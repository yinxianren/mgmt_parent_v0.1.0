package com.rxh.anew.service.shortcut.impl.agent;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rxh.anew.service.db.agent.AgentMerchantsDetailsDBService;
import com.rxh.anew.table.agent.AgentMerchantsDetailsTable;
import com.rxh.anew.table.merchant.MerchantsDetailsTable;
import com.rxh.payInterface.NewPayAssert;
import com.rxh.service.anew.agent.ApiAgentMerchantsDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/29
 * Time: 下午3:30
 * Description:    com.rxh.service.anew.agent.ApiAgentMerchantsDetailsService com.rxh.service.anew.agent.ApiAgentMerchantWalletService
 */
@AllArgsConstructor
@Service
public class ApiAgentMerchantsDetailsServiceImpl implements ApiAgentMerchantsDetailsService, NewPayAssert {

    private final AgentMerchantsDetailsDBService agentMerchantsDetailsDBService;

    @Override
    public boolean save(AgentMerchantsDetailsTable amd) {
        if(isNull(amd)) return  false;
        return agentMerchantsDetailsDBService.save(amd);
    }

    @Override
    public IPage page(AgentMerchantsDetailsTable amd) {
        if (isNull(amd)) return new Page();
        LambdaQueryWrapper<AgentMerchantsDetailsTable> queryWrapper = new LambdaQueryWrapper<>();
        if (!isBlank(amd.getAgentMerchantId())) queryWrapper.eq(AgentMerchantsDetailsTable::getAgentMerchantId,amd.getAgentMerchantId());
        if (!isBlank(amd.getMerOrderId())) queryWrapper.eq(AgentMerchantsDetailsTable::getMerOrderId,amd.getMerOrderId());
        if (!isBlank(amd.getPlatformOrderId())) queryWrapper.eq(AgentMerchantsDetailsTable::getPlatformOrderId,amd.getPlatformOrderId());
        if (!isBlank(amd.getProductId())) queryWrapper.eq(AgentMerchantsDetailsTable::getProductId,amd.getProductId());
        if (!isNull(amd.getBeginTime())) queryWrapper.gt(AgentMerchantsDetailsTable::getUpdateTime,amd.getBeginTime());
        if (!isNull(amd.getEndTime())) queryWrapper.lt(AgentMerchantsDetailsTable::getUpdateTime,amd.getEndTime());
        IPage iPage = new Page(amd.getPageNum(),amd.getPageSize());

        return agentMerchantsDetailsDBService.page(iPage,queryWrapper);
    }
}
