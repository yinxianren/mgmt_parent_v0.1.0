package com.rxh.controller.anew.merchant;

import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.table.merchant.MerchantQuotaRiskTable;
import com.rxh.service.AnewMerchantQuotaRiskService;
import com.rxh.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/merchantQuotaRisk")
public class AnewMerchantQuotaRiskController {

    @Autowired
    private AnewMerchantQuotaRiskService anewMerchantQuotaRiskService;

    @RequestMapping("/search")
    public ResponseVO search(@RequestBody MerchantQuotaRiskTable merchantQuotaRiskTable){
        try {
            return anewMerchantQuotaRiskService.search(merchantQuotaRiskTable);
        }catch (Exception e){
            e.printStackTrace();
            ResponseVO responseVO = new ResponseVO();
            responseVO.setCode(StatusEnum._1.getStatus());
            responseVO.setMessage("失败");
            return responseVO;
        }

    }
    @RequestMapping("/update")
    public ResponseVO update(@RequestBody  MerchantQuotaRiskTable merchantQuotaRiskTable){
        try {
            return anewMerchantQuotaRiskService.saveOrUpdate(merchantQuotaRiskTable);
        }catch (Exception e){
            e.printStackTrace();
            ResponseVO responseVO = new ResponseVO();
            responseVO.setCode(StatusEnum._1.getStatus());
            responseVO.setMessage("失败");
            return responseVO;
        }

    }

}
