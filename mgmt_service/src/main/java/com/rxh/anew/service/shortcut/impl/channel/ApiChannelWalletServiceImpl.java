package com.rxh.anew.service.shortcut.impl.channel;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rxh.anew.service.db.channel.ChannelWalletDbService;
import com.rxh.anew.table.channel.ChannelWalletTable;
import com.rxh.payInterface.NewPayAssert;
import com.rxh.service.anew.channel.ApiChannelWalletService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/29
 * Time: 下午1:48
 * Description:
 */
@AllArgsConstructor
@Service
public class ApiChannelWalletServiceImpl implements ApiChannelWalletService , NewPayAssert {

   private final ChannelWalletDbService channelWalletDbService;

    @Override
    public ChannelWalletTable getOne(ChannelWalletTable cwt) {
        if(isNull(cwt)) return null;
        LambdaQueryWrapper<ChannelWalletTable> lambdaQueryWrapper = new QueryWrapper<ChannelWalletTable>().lambda();
        if( !isBlank(cwt.getChannelId())) lambdaQueryWrapper.eq(ChannelWalletTable::getChannelId,cwt.getChannelId());
        if( !isBlank(cwt.getOrganizationId())) lambdaQueryWrapper.eq(ChannelWalletTable::getOrganizationId,cwt.getOrganizationId());
        if( !isNull(cwt.getStatus())) lambdaQueryWrapper.eq(ChannelWalletTable::getStatus,cwt.getStatus());
        return channelWalletDbService.getOne(lambdaQueryWrapper);
    }

    @Override
    public boolean updateOrSave(ChannelWalletTable cwt) {
        if(isNull(cwt)) return  false;
        return channelWalletDbService.saveOrUpdate(cwt);
    }
}
