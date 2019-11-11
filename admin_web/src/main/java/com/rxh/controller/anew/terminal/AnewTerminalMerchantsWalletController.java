package com.rxh.controller.anew.terminal;

import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.table.terminal.TerminalMerchantsWalletTable;
import com.rxh.pojo.base.Page;
import com.rxh.pojo.base.PageResult;
import com.rxh.service.AnewMerchantInfoService;
import com.rxh.service.AnewTerminalMerchantsWalletService;
import com.rxh.service.ConstantService;
import com.rxh.service.square.MerchantInfoService;
import com.rxh.service.square.OrganizationService;
import com.rxh.service.square.TerminalMerchantsWalletService;
import com.rxh.spring.annotation.SystemLogInfo;
import com.rxh.square.pojo.TerminalMerchantsWallet;
import com.rxh.utils.SystemConstant;
import com.rxh.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/terminalMerchantsWallet")
public class AnewTerminalMerchantsWalletController {

    @Resource
    private ConstantService constantService;
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
        init.put("paytype", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.PAYTYPE));
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
        init.put("productTypes",constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.PRODUCTTYPE));
        return init;
    }
}
