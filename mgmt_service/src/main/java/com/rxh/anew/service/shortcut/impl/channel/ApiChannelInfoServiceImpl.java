package com.rxh.anew.service.shortcut.impl.channel;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rxh.anew.service.db.channel.ChannelInfoDbService;
import com.rxh.anew.table.channel.ChannelInfoTable;
import com.rxh.enums.StatusEnum;
import com.rxh.payInterface.NewPayAssert;
import com.rxh.service.anew.channel.ApiChannelInfoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/22
 * Time: 下午3:42
 * Description:
 */
@AllArgsConstructor
@Service
public class ApiChannelInfoServiceImpl implements ApiChannelInfoService , NewPayAssert {

    private final ChannelInfoDbService channelInfoDbService;


    @Override
    public List<ChannelInfoTable> batchGetByChannelId(Set<String> channelIdSet) {
        if(isHasNotElement(channelIdSet)) return null;
        LambdaQueryWrapper<ChannelInfoTable>  lambdaQueryWrapper = new QueryWrapper<ChannelInfoTable>()
                .lambda().eq(ChannelInfoTable::getStatus, StatusEnum._0.getStatus());//默认只取可用通道
        lambdaQueryWrapper.in(ChannelInfoTable::getChannelId,channelIdSet);
        return channelInfoDbService.list();
    }

    @Override
    public ChannelInfoTable getOne(ChannelInfoTable cit) {
        if(isNull(cit)) return null;
        LambdaQueryWrapper<ChannelInfoTable>  lambdaQueryWrapper = new QueryWrapper<ChannelInfoTable>()
                .lambda().eq(ChannelInfoTable::getStatus, StatusEnum._0.getStatus());//默认只取可用通道
        if( !isBlank(cit.getChannelId()) ) lambdaQueryWrapper.eq(ChannelInfoTable::getChannelId,cit.getChannelId());
        return channelInfoDbService.getOne(lambdaQueryWrapper);
    }
}
