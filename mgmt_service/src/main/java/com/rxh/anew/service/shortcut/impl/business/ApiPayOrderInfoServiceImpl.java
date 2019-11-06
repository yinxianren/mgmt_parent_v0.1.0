package com.rxh.anew.service.shortcut.impl.business;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rxh.anew.service.db.business.PayOrderInfoDBService;
import com.rxh.anew.table.business.PayOrderInfoTable;
import com.rxh.payInterface.NewPayAssert;
import com.rxh.service.anew.business.ApiPayOrderInfoService;
import com.rxh.vo.PageInfo;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/24
 * Time: 下午7:44
 * Description:
 */

@AllArgsConstructor
@Service
public class ApiPayOrderInfoServiceImpl implements ApiPayOrderInfoService, NewPayAssert {
    private final  PayOrderInfoDBService payOrderInfoDBService;

    @Override
    public PayOrderInfoTable getOne(PayOrderInfoTable pit) {
        if(isNull(pit)) return null;
        LambdaQueryWrapper<PayOrderInfoTable> lambdaQueryWrapper = new QueryWrapper<PayOrderInfoTable>() .lambda();
        if( !isNull(pit.getStatus())) lambdaQueryWrapper.eq(PayOrderInfoTable::getStatus,pit.getStatus());
        if( !isBlank(pit.getPlatformOrderId())) lambdaQueryWrapper.eq(PayOrderInfoTable::getPlatformOrderId,pit.getPlatformOrderId());
        if( !isBlank(pit.getMerOrderId())) lambdaQueryWrapper.eq(PayOrderInfoTable::getMerOrderId,pit.getMerOrderId());
        if( !isBlank(pit.getMerchantId())) lambdaQueryWrapper.eq(PayOrderInfoTable::getMerchantId,pit.getMerchantId());
        if( !isBlank(pit.getTerminalMerId())) lambdaQueryWrapper.eq(PayOrderInfoTable::getTerminalMerId,pit.getTerminalMerId());
        if( !isBlank(pit.getBussType())) lambdaQueryWrapper.eq(PayOrderInfoTable::getBussType,pit.getBussType());
        if( isHasElement(pit.getStatusCollect())) lambdaQueryWrapper.in(PayOrderInfoTable::getStatus,pit.getStatusCollect());
        return payOrderInfoDBService.getOne(lambdaQueryWrapper);
    }

    @Override
    public List<PayOrderInfoTable> getList(PayOrderInfoTable pit) {
        if(isNull(pit)) return null;
        LambdaQueryWrapper<PayOrderInfoTable> lambdaQueryWrapper = new QueryWrapper<PayOrderInfoTable>() .lambda();
        if(isHasElement(pit.getMerOrderIdCollect()) ) lambdaQueryWrapper.in(PayOrderInfoTable::getMerOrderId,pit.getMerOrderIdCollect());
        if(isHasElement(pit.getStatusCollect())) lambdaQueryWrapper.in(PayOrderInfoTable::getStatus,pit.getStatusCollect());
        if( !isNull(pit.getStatus())) lambdaQueryWrapper.eq(PayOrderInfoTable::getStatus,pit.getStatus());
        if( !isBlank(pit.getPlatformOrderId())) lambdaQueryWrapper.eq(PayOrderInfoTable::getPlatformOrderId,pit.getPlatformOrderId());
        if( !isBlank(pit.getMerOrderId())) lambdaQueryWrapper.eq(PayOrderInfoTable::getMerOrderId,pit.getMerOrderId());
        if( !isBlank(pit.getMerchantId())) lambdaQueryWrapper.eq(PayOrderInfoTable::getMerchantId,pit.getMerchantId());
        if( !isBlank(pit.getTerminalMerId())) lambdaQueryWrapper.eq(PayOrderInfoTable::getTerminalMerId,pit.getTerminalMerId());
        if( !isBlank(pit.getBussType())) lambdaQueryWrapper.eq(PayOrderInfoTable::getBussType,pit.getBussType());
        return payOrderInfoDBService.list(lambdaQueryWrapper);
    }

    @Override
    public boolean save(PayOrderInfoTable pit) {
        if(isNull(pit)) return false;
        return payOrderInfoDBService.save(pit);
    }

    @Override
    public boolean updateByPrimaryKey(PayOrderInfoTable pit) {
        if(isNull(pit)) return false;
        return payOrderInfoDBService.updateById(pit);
    }

    @Override
    public IPage page(PayOrderInfoTable pit) {
        if (isNull(pit)) return new Page();
        LambdaQueryWrapper<PayOrderInfoTable> queryWrapper = new QueryWrapper<PayOrderInfoTable>().lambda();
        if (StringUtils.isNotEmpty(pit.getMerchantId())) queryWrapper.eq(PayOrderInfoTable::getMerchantId,pit.getMerchantId());
        if (StringUtils.isNotEmpty(pit.getPlatformOrderId())) queryWrapper.eq(PayOrderInfoTable::getPlatformOrderId,pit.getPlatformOrderId());
//            if (StringUtils.isNotEmpty(pit.getOrganizationId())) queryWrapper.setChannelId(searchInfo.getExpressName());
        if (null != (pit.getStatus())) queryWrapper.eq(PayOrderInfoTable::getStatus,pit.getStatus());
        if (null != (pit.getSettleStatus())) queryWrapper.eq(PayOrderInfoTable::getSettleStatus,pit.getSettleStatus());
        if (StringUtils.isNotEmpty(pit.getProductId())) queryWrapper.eq(PayOrderInfoTable::getProductId,pit.getProductId());
        if (null != (pit.getBeginTime())) queryWrapper.ge(PayOrderInfoTable::getCreateTime,pit.getBeginTime());
        if (null != pit.getEndTime()) queryWrapper.le(PayOrderInfoTable::getUpdateTime,pit.getEndTime());
        IPage<PayOrderInfoTable> iPage = new Page(pit.getPageNum(),pit.getPageSize());
        return payOrderInfoDBService.page(iPage,queryWrapper);
    }
}
