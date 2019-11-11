package com.rxh.controller.anew.channel;

import com.internal.playment.common.enums.StatusEnum;
import com.rxh.anew.table.system.BankRateTable;
import com.rxh.service.AnewBankRateService;
import com.rxh.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/bankRate")
public class AnewOrganizationBankRateController {

    @Autowired
    private AnewBankRateService anewBankRateService;

    @RequestMapping("/search")
    public ResponseVO  search(BankRateTable rateTable){
        try {
            return anewBankRateService.search(rateTable);
        }catch (Exception e){
            e.printStackTrace();
           return new ResponseVO(StatusEnum._1.getStatus(), StatusEnum._1.getRemark());
        }

    }

    @PostMapping("/save")
    public ResponseVO save(BankRateTable rateTable){
        try {
            return anewBankRateService.saveOrUpdate(rateTable);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseVO(StatusEnum._1.getStatus(),StatusEnum._1.getRemark());
        }
    }

    @PostMapping("/update")
    public ResponseVO update(BankRateTable rateTable){
        try {
            return anewBankRateService.saveOrUpdate(rateTable);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseVO(StatusEnum._1.getStatus(),StatusEnum._1.getRemark());
        }
    }

    @PostMapping("/delete")
    public ResponseVO delete (@RequestBody List<String> idList){
        try {
            return anewBankRateService.removeByIds(idList);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseVO(StatusEnum._1.getStatus(),StatusEnum._1.getRemark());
        }

    }


}
