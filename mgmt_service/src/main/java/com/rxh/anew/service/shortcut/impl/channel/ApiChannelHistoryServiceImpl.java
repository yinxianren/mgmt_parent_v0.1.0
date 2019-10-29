package com.rxh.anew.service.shortcut.impl.channel;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rxh.anew.service.db.channel.ChannelHistoryDbService;
import com.rxh.anew.table.channel.ChannelHistoryTable;
import com.rxh.payInterface.NewPayAssert;
import com.rxh.service.anew.channel.ApiChannelHistoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/25
 * Time: 上午9:54
 * Description:
 */
@AllArgsConstructor
@Service
public class ApiChannelHistoryServiceImpl implements ApiChannelHistoryService, NewPayAssert {

    private final ChannelHistoryDbService channelHistoryDbService;


    @Override
    public ChannelHistoryTable getOne(ChannelHistoryTable ch) {
        if(isNull(ch)) return null;
        LambdaQueryWrapper<ChannelHistoryTable> lambdaQueryWrapper = new QueryWrapper<ChannelHistoryTable>().lambda();
        if( !isBlank(ch.getMerchantId()) ) lambdaQueryWrapper.eq(ChannelHistoryTable::getMerchantId,ch.getMerchantId());
        if( !isBlank(ch.getTerminalMerId()) ) lambdaQueryWrapper.eq(ChannelHistoryTable::getTerminalMerId,ch.getTerminalMerId());
        if( !isNull(ch.getCreateTime()) ) lambdaQueryWrapper.eq(ChannelHistoryTable::getCreateTime,ch.getCreateTime());
        return channelHistoryDbService.getOne(lambdaQueryWrapper);
    }

    @Override
    public boolean save(ChannelHistoryTable ch) {
        if(isNull(ch)) return false;
        return channelHistoryDbService.save(ch);
    }

    @Override
    public boolean saveOrUpdateBatch(Collection<ChannelHistoryTable> entityList) {
        if(isHasNotElement(entityList)) return false;
        return channelHistoryDbService.saveOrUpdateBatch(entityList);
    }


}
