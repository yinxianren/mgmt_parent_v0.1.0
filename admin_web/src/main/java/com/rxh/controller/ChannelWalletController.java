package com.rxh.controller;

import com.rxh.pojo.Result;
import com.rxh.pojo.base.Page;
import com.rxh.pojo.base.PageResult;
import com.rxh.service.ConstantService;
import com.rxh.service.square.ChannelWalletService;
import com.rxh.service.square.OrganizationService;
import com.rxh.spring.annotation.SystemLogInfo;
import com.rxh.square.pojo.ChannelInfo;
import com.rxh.square.pojo.ChannelWallet;
import com.rxh.utils.SystemConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/channelWallet")
public class ChannelWalletController {

    @Resource
    private ChannelWalletService channelWalletService;

    @Resource
    private ConstantService constantService;
    @Resource
    private OrganizationService organizationService;
    @SystemLogInfo(description = "平台钱包查询    ")
    @RequestMapping("/search")
    public List<ChannelWallet> search(@RequestBody ChannelWallet channelInfo){

        List<ChannelWallet> result = channelWalletService.search(channelInfo);
        return result;
    };


    @RequestMapping("/idsInit")
    public Map<String, Object> init() {
        Map<String, Object> init = new HashMap<>();
        init.put("paytype", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.PAYTYPE));
        init.put("channels", channelWalletService.getIdsAndName());
        init.put("organizations", organizationService.getIdsAndName());
        return init;
    }

    @RequestMapping("/batchDel")
    public Result delete(@RequestBody List<String> ids){
        return channelWalletService.deleteByPrimaryKey(ids);
    }

    @RequestMapping("/init")
    public Map<String, Object> init1() {
        Map<String, Object> init = new HashMap<>();
        init.put("payTypes", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.PAYTYPE));
        init.put("detailsTypes",constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.DETAILSTYPE));
        return init;
    }

    @RequestMapping(value="/findChannelWallteDetailsPage")
    public PageResult search(@RequestBody Page page ){

        PageResult pageResult = channelWalletService.findChannelWallteDetails(page);
        return pageResult;
    };

}
