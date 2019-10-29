package com.rxh.anew.service.shortcut.impl.channel;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rxh.anew.service.db.channel.ChannelDetailsDbService;
import com.rxh.anew.table.channel.ChannelDetailsTable;
import com.rxh.payInterface.NewPayAssert;
import com.rxh.service.anew.channel.ApiChannelDetailsService;
import lombok.AllArgsConstructor;
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
}
