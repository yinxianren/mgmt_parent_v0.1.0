package com.rxh.anew.service.shortcut.impl.channel;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rxh.anew.service.db.channel.ChannelInfoDbService;
import com.rxh.anew.table.channel.ChannelInfoTable;
import com.rxh.enums.StatusEnum;
import com.rxh.payInterface.NewPayAssert;
import com.rxh.service.anew.channel.ApiChannelInfoService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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

    @Override
    public List<ChannelInfoTable> getChannel(ChannelInfoTable cit) {
        if(isNull(cit)) return channelInfoDbService.list();
        LambdaQueryWrapper<ChannelInfoTable>  lambdaQueryWrapper = new QueryWrapper<ChannelInfoTable>().lambda();
        if( !isBlank(cit.getChannelId()) ) lambdaQueryWrapper.eq(ChannelInfoTable::getChannelId,cit.getChannelId());
        if( !isBlank(cit.getOrganizationId()) ) lambdaQueryWrapper.eq(ChannelInfoTable::getOrganizationId,cit.getOrganizationId());
        if( !isNull(cit.getChannelLevel()) ) lambdaQueryWrapper.eq(ChannelInfoTable::getChannelLevel,cit.getChannelLevel());
        if( !isBlank(cit.getProductId()) ) lambdaQueryWrapper.eq(ChannelInfoTable::getProductId,cit.getProductId());
        return channelInfoDbService.list(lambdaQueryWrapper);
    }

    @Override
    public Boolean savaOrUpdate(ChannelInfoTable cit) {
        if (isNull(cit)) return false;
        return channelInfoDbService.saveOrUpdate(cit);
    }

    @Override
    public Boolean delByIds(List<String> ids) {
        if (isHasNotElement(ids)) return false;
        return channelInfoDbService.removeByIds(ids);
    }

    @Override
    public List<ChannelInfoTable> getChannels(List<String> orgIds) {
        if (isHasNotElement(orgIds)) return null;
        LambdaQueryWrapper<ChannelInfoTable> queryWrapper = new QueryWrapper<ChannelInfoTable>().lambda();
        queryWrapper.in(ChannelInfoTable::getOrganizationId,orgIds);
        queryWrapper.eq(ChannelInfoTable::getStatus,1);
        return channelInfoDbService.list(queryWrapper);
    }
}
