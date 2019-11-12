package com.rxh.controller.anew.channel;

import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.table.system.BankRateTable;
import com.internal.playment.common.table.system.OrganizationInfoTable;
import com.rxh.service.AnewBankRateService;
import com.rxh.service.ConstantService;
import com.rxh.service.OrganizationInfoService;
import com.rxh.utils.SystemConstant;
import com.rxh.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bankRate")
public class AnewOrganizationBankRateController {

    @Autowired
    private AnewBankRateService anewBankRateService;
    @Autowired
    private OrganizationInfoService organizationService;
    @Autowired
    private ConstantService constantService;

    @RequestMapping("/search")
    public ResponseVO search(BankRateTable rateTable){
        try {
            return anewBankRateService.search(rateTable);
        }catch (Exception e){
            e.printStackTrace();
           return new ResponseVO(StatusEnum._1.getStatus(), StatusEnum._1.getRemark());
        }

    }

    @PostMapping("/save")
    public ResponseVO save(@RequestBody BankRateTable rateTable){
        try {
            rateTable.setCreateTime(new Date());
            rateTable.setTradeTime(new Date());
            return anewBankRateService.saveOrUpdate(rateTable);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseVO(StatusEnum._1.getStatus(),StatusEnum._1.getRemark());
        }
    }

    @PostMapping("/update")
    public ResponseVO update(@RequestBody BankRateTable rateTable){
        try {
            rateTable.setTradeTime(new Date());
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

    @RequestMapping(value = "/init")
    @ResponseBody
    public Map<String, Object> getChannelInfoInit() {
        Map<String, Object> init = new HashMap<>();
        init.put("organizations",organizationService.getAll(new OrganizationInfoTable()).getData());
        init.put("status", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.availableStatus));
        return init;
    }


}
