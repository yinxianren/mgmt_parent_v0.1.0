package com.rxh.controller.anew.merchant;


import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.table.merchant.MerchantWalletTable;
import com.rxh.pojo.base.Page;
import com.rxh.service.*;
import com.rxh.service.agent.AnewAgentMerchantService;
import com.rxh.service.merchant.AnewMerchantInfoService;
import com.rxh.service.merchant.AnewMerchantWalletService;
import com.rxh.service.square.MerchantInfoService;
import com.rxh.service.square.MerchantWalletService;
import com.rxh.spring.annotation.SystemLogInfo;
import com.rxh.square.pojo.MerchantInfo;
import com.rxh.util.UserInfoUtils;
import com.rxh.utils.SystemConstant;
import com.rxh.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

@RestController
@RequestMapping("/merchantWallet")
public class AnewMerchantWalletController {

    @Resource
    private MerchantWalletService merchantWalletService;
    @Resource
    private MerchantInfoService merchantInfoService;
    @Resource
    private BCryptPasswordEncoder passwordEncoder;
    @Resource
    private UserService userService;
    @Autowired
    private ConstantService constantService;
    @Autowired
    private AnewMerchantWalletService anewMerchantWalletService;
    @Autowired
    private AnewMerchantInfoService anewMerchantInfoService;
    @Autowired
    private AnewAgentMerchantService anewAgentMerchantService;


    @SystemLogInfo(description = "商户钱包查询")
    @RequestMapping("/search")
    public ResponseVO search(@RequestBody MerchantWalletTable param){
        try {
            return  anewMerchantWalletService.search(param);
        }catch (Exception e){
            e.printStackTrace();
            ResponseVO responseVO = new ResponseVO();
            responseVO.setCode(StatusEnum._1.getStatus());
            responseVO.setMessage(StatusEnum._1.getRemark());
            return responseVO;
        }


    }
    @RequestMapping("/idsInit")
    public Map<String, Object> init() {
        Map<String, Object> init = new HashMap<>();
        init.put("agents", anewAgentMerchantService.list(null).getData());
        init.put("merchants", anewMerchantInfoService.getMerchants(null).getData());
        return init;
    }
//    @RequestMapping("/batchDel")
//    public Result delete(@RequestBody List<String> ids){
//        return merchantWalletService.deleteByPrimaryKey(ids);
//    }

    @RequestMapping("/invest")
    public int invest(@RequestBody Object investInfo) {
        LinkedHashMap map = (LinkedHashMap) investInfo;
        String password=map.get("password")+"";
        String userName =   UserInfoUtils.getName();
        String pword = userService.getPassWord(userName);
        Boolean a = passwordEncoder.matches(password,pword);
        List list =new ArrayList();
        for(MerchantInfo merchantInfo:merchantInfoService.getAllmerId()){
            list.add(merchantInfo.getMerId());
        }
        Boolean b=list.contains(map.get("merId")+"");
        if(a){
            if(b) {
                return merchantWalletService.invest(investInfo);
            }else {
                return 3;
            }
        }else {
            return 2;
        }

    }

    @RequestMapping(value="/findMerchantsDetails")
    public ResponseVO findMerchantsDetails(@RequestBody Page page ) {
        try {
            return anewMerchantWalletService.pageByDetails(page);
        }catch (Exception e){
            e.printStackTrace();
            ResponseVO responseVO = new ResponseVO();
            responseVO.setCode(StatusEnum._1.getStatus());
            responseVO.setMessage(StatusEnum._1.getRemark());
            return responseVO;
        }
    }

    @RequestMapping("/init")
    public Map<String, Object> inits() {
        Map<String, Object> init = new HashMap<>();
        init.put("merchants", anewMerchantInfoService.getMerchants(null).getData());
        init.put("productTypes", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.PRODUCTTYPE));
        init.put("detailsTypes",constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.DETAILSTYPE));
        return init;
    }


}
