package com.rxh.anew.service.shortcut.impl.channel;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rxh.anew.service.db.channel.ChannelExtraInfoDbService;
import com.rxh.anew.table.channel.ChannelExtraInfoTable;
import com.rxh.payInterface.NewPayAssert;
import com.rxh.service.anew.channel.ApiChannelExtraInfoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/22
 * Time: 下午10:15
 * Description:
 */
@AllArgsConstructor
@Service
public class ApiChannelExtraInfoServiceImpl implements ApiChannelExtraInfoService, NewPayAssert {

    private final ChannelExtraInfoDbService channelExtraInfoDbService;


    @Override
    public ChannelExtraInfoTable getOne(ChannelExtraInfoTable cei) {
        if( isNull(cei) )  return null;
        LambdaQueryWrapper<ChannelExtraInfoTable> lambdaQueryWrapper = new QueryWrapper<ChannelExtraInfoTable>()
                .lambda().eq(ChannelExtraInfoTable::getStatus,0);//默认只取启用
        if( !isBlank(cei.getOrganizationId()) ) lambdaQueryWrapper.eq(ChannelExtraInfoTable::getOrganizationId,cei.getOrganizationId());
        if( !isBlank(cei.getBussType()) ) lambdaQueryWrapper.eq(ChannelExtraInfoTable::getBussType,cei.getBussType());
        return channelExtraInfoDbService.getOne(lambdaQueryWrapper);
    }

    @Override
    public List<ChannelExtraInfoTable> lsit(ChannelExtraInfoTable cei) {
        if( isNull(cei) )  return null;
        LambdaQueryWrapper<ChannelExtraInfoTable> lambdaQueryWrapper = new QueryWrapper<ChannelExtraInfoTable>().lambda();
        if( !isBlank(cei.getOrganizationId()) ) lambdaQueryWrapper.eq(ChannelExtraInfoTable::getOrganizationId,cei.getOrganizationId());
        if( !isBlank(cei.getBussType()) ) lambdaQueryWrapper.eq(ChannelExtraInfoTable::getBussType,cei.getBussType());
        if( isNull(cei.getStatus()) ) lambdaQueryWrapper.eq(ChannelExtraInfoTable::getStatus,cei.getStatus());
        return channelExtraInfoDbService.list(lambdaQueryWrapper);
    }
}
