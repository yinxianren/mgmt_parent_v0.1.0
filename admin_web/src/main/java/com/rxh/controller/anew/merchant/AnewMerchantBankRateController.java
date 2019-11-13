package com.rxh.controller.anew.merchant;

import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.table.merchant.MerchantBankRateTable;
import com.rxh.service.AnewMerchantBankRateService;
import com.rxh.service.ConstantService;
import com.rxh.utils.SystemConstant;
import com.rxh.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/merchantBankRate")
public class AnewMerchantBankRateController {

    @Autowired
    private AnewMerchantBankRateService anewMerchantBankRateService;
    @Autowired
    private ConstantService constantService;

    @RequestMapping("/search")
    public ResponseVO search(MerchantBankRateTable merchantBankRateTable){
        try {
            return anewMerchantBankRateService.search(merchantBankRateTable);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseVO(StatusEnum._1.getStatus(),StatusEnum._1.getRemark());
        }
    }

    @PostMapping("update")
    public ResponseVO update(@RequestBody List<MerchantBankRateTable> list){
        try {
            return anewMerchantBankRateService.saveOrUpdate(list);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseVO(StatusEnum._1.getStatus(),StatusEnum._1.getRemark());
        }
    }

    @RequestMapping("/init")
    public ResponseVO init(){
        try {
            Map map = new HashMap();
//            map.put("bankNames",constantService.getConstantByGroupName("bankName"));
            map.put("status", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.availableStatus));
            ResponseVO responseVO = new ResponseVO();
            responseVO.setCode(StatusEnum._0.getStatus());
            responseVO.setMessage(StatusEnum._0.getRemark());
            responseVO.setData(map);
            return responseVO;
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseVO(StatusEnum._1.getStatus(),StatusEnum._1.getRemark());
        }
    }
}
