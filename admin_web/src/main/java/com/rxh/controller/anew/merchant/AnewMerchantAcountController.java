package com.rxh.controller.anew.merchant;

import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.table.merchant.MerchantAcountTable;
import com.rxh.service.merchant.NewMerchantAcountService;
import com.rxh.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/merchantAcount")
public class AnewMerchantAcountController {

    @Autowired
    private NewMerchantAcountService newMerchantAcountService;

    @RequestMapping("/update")
    public ResponseVO update(@RequestBody MerchantAcountTable merchantAcount){
        try {
            return newMerchantAcountService.savaOrUpdate(merchantAcount);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseVO(StatusEnum._1.getStatus(),StatusEnum._1.getRemark());
        }
    }
    @RequestMapping("/search")
    public ResponseVO search(@RequestBody MerchantAcountTable merchantAcount){
        try {
            return newMerchantAcountService.getOne(merchantAcount);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseVO(StatusEnum._1.getStatus(),StatusEnum._1.getRemark());
        }
    }
}
