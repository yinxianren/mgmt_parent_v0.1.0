package com.rxh.service.impl.merchnat;

import com.internal.playment.api.db.merchant.ApiMerchantBankRateSerrvice;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.table.merchant.MerchantBankRateTable;
import com.rxh.pojo.sys.SysConstant;
import com.rxh.service.merchant.AnewMerchantBankRateService;
import com.rxh.service.ConstantService;
import com.rxh.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AnewMerchantBankRateServiceImpl implements AnewMerchantBankRateService {

    @Autowired
    private ApiMerchantBankRateSerrvice apiMerchantBankRateSerrvice;
    @Autowired
    private ConstantService constantService;

    @Override
    public ResponseVO search(MerchantBankRateTable merchantBankRateTable) {
        ResponseVO responseVO = new ResponseVO();
        List<MerchantBankRateTable> list = apiMerchantBankRateSerrvice.getList(merchantBankRateTable);
        List<String> bankNames = new ArrayList<>();
        for (MerchantBankRateTable merchantBankRateTable1 : list){
            bankNames.add(merchantBankRateTable1.getBankName());
        }
        List<SysConstant> constantList = constantService.getConstantByGroupName("bankName");
        for(SysConstant sysConstant : constantList){
            if (!bankNames.contains(sysConstant.getName())){
                MerchantBankRateTable merchantBankRateTable1 = new MerchantBankRateTable();
                merchantBankRateTable1.setBankName(sysConstant.getName());
                merchantBankRateTable1.setBankCode(sysConstant.getFirstValue());
                merchantBankRateTable1.setStatus(StatusEnum._1.getStatus());
                merchantBankRateTable1.setRateFee(new BigDecimal(0));
                merchantBankRateTable1.setMerchantId(merchantBankRateTable.getMerchantId());
                list.add(merchantBankRateTable1);
            }
        }
        responseVO.setCode(StatusEnum._0.getStatus());
        responseVO.setMessage(StatusEnum._0.getRemark());
        responseVO.setData(list);
        return responseVO;
    }

    @Override
    public ResponseVO saveOrUpdate(List<MerchantBankRateTable> list) {
        ResponseVO responseVO = new ResponseVO();
        for (MerchantBankRateTable merchantBankRateTable : list){
            if (null == (merchantBankRateTable.getId())){
                merchantBankRateTable.setCreateTime(new Date());
            }
            merchantBankRateTable.setUpdateTime(new Date());
        }
        Boolean b = apiMerchantBankRateSerrvice.batchSaveOrUpdate(list);
        if (b){
            responseVO.setMessage(StatusEnum._0.getRemark());
            responseVO.setCode(StatusEnum._0.getStatus());
        }else{
            responseVO.setMessage(StatusEnum._1.getRemark());
            responseVO.setCode(StatusEnum._1.getStatus());
        }
        return responseVO;
    }
}
