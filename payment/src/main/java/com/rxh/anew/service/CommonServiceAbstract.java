package com.rxh.anew.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rxh.anew.CommonRPCComponent;
import com.rxh.anew.inner.InnerPrintLogObject;
import com.rxh.anew.table.merchant.MerchantInfoTable;
import com.rxh.enums.ResponseCodeEnum;
import com.rxh.exception.NewPayException;
import com.rxh.payInterface.NewPayAssert;
import com.rxh.payInterface.PayUtil;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/21
 * Time: 上午9:15
 * Description:
 */

public abstract class CommonServiceAbstract implements NewPayAssert, PayUtil {

    @Autowired
    private CommonRPCComponent commonRPCComponent;



    public MerchantInfoTable getOneMerInfo(InnerPrintLogObject ipo) throws NewPayException {
        MerchantInfoTable merchantInfoTable = new MerchantInfoTable();
        merchantInfoTable.setMerchantId(ipo.getMerId());
        merchantInfoTable = commonRPCComponent.anewMerchantInfoService.getOne(merchantInfoTable);
        isNull(merchantInfoTable,
                ResponseCodeEnum.RXH00017.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00017.getMsg()),
                format(" %s",ResponseCodeEnum.RXH00017.getMsg()));
        return merchantInfoTable;
    }
}
