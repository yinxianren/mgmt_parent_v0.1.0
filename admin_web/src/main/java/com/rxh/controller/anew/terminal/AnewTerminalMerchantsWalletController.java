package com.rxh.controller.anew.terminal;

import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.table.terminal.TerminalMerchantsWalletTable;
import com.internal.playment.common.page.Page;
import com.rxh.service.merchant.AnewMerchantInfoService;
import com.rxh.service.AnewTerminalMerchantsWalletService;
import com.rxh.service.system.NewSystemConstantService;
import com.rxh.spring.annotation.SystemLogInfo;
import com.internal.playment.common.enums.SystemConstant;
import com.internal.playment.common.page.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/terminalMerchantsWallet")
public class AnewTerminalMerchantsWalletController {

    @Resource
    private NewSystemConstantService constantService;
    @Autowired
    private AnewTerminalMerchantsWalletService anewTerminalMerchantsWalletService;
    @Autowired
    private AnewMerchantInfoService anewMerchantInfoService;

    @SystemLogInfo(description = "终端商户钱包查询")
    @RequestMapping("/search")
    public ResponseVO search(@RequestBody TerminalMerchantsWalletTable terminalMerchantsWallet){
        try {
            return  anewTerminalMerchantsWalletService.search(terminalMerchantsWallet);
        }catch (Exception e){
            e.printStackTrace();
            ResponseVO responseVO = new ResponseVO();
            responseVO.setCode(StatusEnum._1.getStatus());
            responseVO.setMessage(StatusEnum._1.getRemark());
            return responseVO;
        }
    };


    @RequestMapping("/idsInit")
    public Map<String, Object> idsInit() {
        Map<String, Object> init = new HashMap<>();
        init.put("paytype", constantService.getConstantByGroupName(SystemConstant.PAYTYPE).getData());
        // init.put("channels", terminalMerchantsWalletService.getIdsAndName());
//        init.put("organizations", organizationService.getIdsAndName());
        init.put("merchants", anewMerchantInfoService.getMerchants(null).getData());
        return init;
    }

    @RequestMapping(value="/findTerminalMerchantsDetails")
    public ResponseVO findTerminalMerchantsDetails(@RequestBody Page page ) {
        try {
            return  anewTerminalMerchantsWalletService.pageByDetail(page);
        }catch (Exception e){
            e.printStackTrace();
            ResponseVO responseVO = new ResponseVO();
            responseVO.setCode(StatusEnum._1.getStatus());
            responseVO.setMessage(StatusEnum._1.getRemark());
            return responseVO;
        }
    }

    @RequestMapping("/init")
    public Map<String, Object> init() {
        Map<String, Object> init = new HashMap<>();
        init.put("merchants", anewMerchantInfoService.getMerchants(null).getData());
        init.put("terminalMerIds", anewTerminalMerchantsWalletService.listByTerminalId());
        init.put("productTypes",constantService.getConstantByGroupName(SystemConstant.PRODUCTTYPE).getData());
        return init;
    }
}
