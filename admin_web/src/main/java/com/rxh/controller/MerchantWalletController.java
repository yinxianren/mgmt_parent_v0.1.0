package com.rxh.controller;


import com.rxh.pojo.Result;
import com.rxh.pojo.sys.SysUser;
import com.rxh.service.UserService;
import com.rxh.service.square.AgentMerchantInfoService;
import com.rxh.service.square.MerchantInfoService;
import com.rxh.service.square.MerchantWalletService;
import com.rxh.spring.annotation.SystemLogInfo;
import com.rxh.square.pojo.MerchantInfo;
import com.rxh.square.pojo.MerchantWallet;
import com.rxh.util.UserInfoUtils;
import com.rxh.square.pojo.MerchantsDetails;
import com.rxh.utils.SystemConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

@RestController
@RequestMapping("/merchantWallet")
public class MerchantWalletController {

    @Resource
    private MerchantWalletService merchantWalletService;
    @Resource
    private AgentMerchantInfoService agentMerchantInfoService;
    @Resource
    private MerchantInfoService merchantInfoService;
    @Resource
    private BCryptPasswordEncoder passwordEncoder;
    @Resource
    private UserService userService;



    @SystemLogInfo(description = "商户钱包查询")
    @RequestMapping("/search")
    public List<MerchantWallet> search(@RequestBody MerchantInfo param){
        return merchantWalletService.searchByParam(param);

    }
    @RequestMapping("/idsInit")
    public Map<String, Object> init() {
        Map<String, Object> init = new HashMap<>();
        init.put("agents", agentMerchantInfoService.getAllIdAndName());
        init.put("merchants", merchantInfoService.getIdsAndName());
        return init;
    }
    @RequestMapping("/batchDel")
    public Result delete(@RequestBody List<String> ids){
        return merchantWalletService.deleteByPrimaryKey(ids);
    }

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


}
