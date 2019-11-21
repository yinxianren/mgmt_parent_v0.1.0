package com.rxh.service.impl.merchnat;

import com.internal.playment.api.db.merchant.ApiMerchantRateService;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.table.merchant.MerchantRateTable;
import com.rxh.pojo.sys.SysConstant;
import com.rxh.service.merchant.AnewMerchantRateService;
import com.rxh.service.ConstantService;
import com.rxh.service.system.NewSystemConstantService;
import com.rxh.utils.SystemConstant;
import com.rxh.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
public class AnewMerchantRateServiceImpl implements AnewMerchantRateService {

    @Autowired
    private ApiMerchantRateService apiMerchantRateService;
    @Autowired
    private NewSystemConstantService constantService;
    @Override
    public ResponseVO batchUpdate(List<MerchantRateTable> param) {
        ResponseVO responseVO = new ResponseVO();
        Date date = new Date();
        for (MerchantRateTable merchantRateTable : param){
            if (merchantRateTable.getId() == null){
                merchantRateTable.setCreateTime(date);
            }
            merchantRateTable.setUpdateTime(date);
        }
        Boolean b  = apiMerchantRateService.batchSaveOrUpdate(param);
        responseVO.setCode(StatusEnum._0.getStatus());
        return responseVO;
    }

    @Override
    public ResponseVO getList(MerchantRateTable m) {
        ResponseVO responseVO = new ResponseVO();
        List<MerchantRateTable> list = apiMerchantRateService.getList(m);
        List<String> productIds = new ArrayList<>();
        for (MerchantRateTable merchantRateTable : list){
            productIds.add(merchantRateTable.getProductId());
        }
        List<SysConstant> constantList = (List)constantService.getConstantByGroupName(SystemConstant.PRODUCTTYPE).getData();
        for (SysConstant sysConstant :constantList){
            MerchantRateTable merchantRateTable = new MerchantRateTable();
            if (CollectionUtils.isEmpty(list)){
                merchantRateTable.setProductId(sysConstant.getFirstValue());
                merchantRateTable.setStatus(StatusEnum._1.getStatus());
                merchantRateTable.setMerchantId(m.getMerchantId());
                list.add(merchantRateTable);
                continue;
            }
            if (!productIds.contains(sysConstant.getFirstValue())){
                merchantRateTable.setProductId(sysConstant.getFirstValue());
                merchantRateTable.setStatus(StatusEnum._1.getStatus());
                merchantRateTable.setMerchantId(m.getMerchantId());
                list.add(merchantRateTable);
            }
        }
        responseVO.setData(list);
        responseVO.setCode(StatusEnum._0.getStatus());
        return responseVO;
    }

    @Override
    public ResponseVO init() {
        Map<String,Object> init = new HashMap<>();
        init.put("productTypes", constantService.getConstantByGroupName(SystemConstant.PRODUCTTYPE).getData());
        init.put("status", constantService.getConstantByGroupName(SystemConstant.availableStatus).getData());
        ResponseVO responseVO = new ResponseVO();
        responseVO.setCode(StatusEnum._0.getStatus());
        responseVO.setData(init);
        return responseVO;
    }
}
