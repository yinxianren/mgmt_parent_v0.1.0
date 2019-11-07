package com.rxh.anew.service.shortcut.impl.channel;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rxh.anew.service.db.channel.ChannelDetailsDbService;
import com.rxh.anew.table.agent.AgentMerchantsDetailsTable;
import com.rxh.anew.table.channel.ChannelDetailsTable;
import com.rxh.payInterface.NewPayAssert;
import com.rxh.service.anew.channel.ApiChannelDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/29
 * Time: 下午1:51
 * Description:
 */

@AllArgsConstructor
@Service
public class ApiChannelDetailsServiceImpl implements ApiChannelDetailsService, NewPayAssert {
    private final ChannelDetailsDbService channelDetailsDbService;

    @Override
    public ChannelDetailsTable getOne(ChannelDetailsTable cdt) {
        if(isNull(cdt)) return null;
        LambdaQueryWrapper<ChannelDetailsTable> lambdaQueryWrapper = new QueryWrapper<ChannelDetailsTable>().lambda();
        if( !isBlank(cdt.getChannelId())) lambdaQueryWrapper.eq(ChannelDetailsTable::getChannelId,cdt.getChannelId());
        if( !isBlank(cdt.getPlatformOrderId())) lambdaQueryWrapper.eq(ChannelDetailsTable::getPlatformOrderId,cdt.getPlatformOrderId());
        if( !isNull(cdt.getStatus())) lambdaQueryWrapper.eq(ChannelDetailsTable::getStatus,cdt.getStatus());
        return channelDetailsDbService.getOne(lambdaQueryWrapper);
    }

    @Override
    public boolean updateOrSave(ChannelDetailsTable cdt) {
        if(isNull(cdt)) return false;
        return channelDetailsDbService.saveOrUpdate(cdt);
    }

    @Override
    public IPage page(ChannelDetailsTable cdt) {
        if (isNull(cdt)) return new Page();
        LambdaQueryWrapper<ChannelDetailsTable> queryWrapper = new LambdaQueryWrapper();
        if (!isBlank(cdt.getChannelId())) queryWrapper.eq(ChannelDetailsTable::getChannelId,cdt.getChannelId());
        if (!isBlank(cdt.getMerOrderId())) queryWrapper.eq(ChannelDetailsTable::getMerOrderId,cdt.getMerOrderId());
        if (!isBlank(cdt.getPlatformOrderId())) queryWrapper.eq(ChannelDetailsTable::getPlatformOrderId,cdt.getPlatformOrderId());
        if (!isBlank(cdt.getProductId())) queryWrapper.eq(ChannelDetailsTable::getProductId,cdt.getProductId());
        if (!isNull(cdt.getBeginTime())) queryWrapper.gt(ChannelDetailsTable::getUpdateTime,cdt.getBeginTime());
        if (!isNull(cdt.getEndTime())) queryWrapper.lt(ChannelDetailsTable::getUpdateTime,cdt.getEndTime());
        IPage iPage = new Page(cdt.getPageNum(),cdt.getPageSize());
        return channelDetailsDbService.page(iPage,queryWrapper);
    }
}
