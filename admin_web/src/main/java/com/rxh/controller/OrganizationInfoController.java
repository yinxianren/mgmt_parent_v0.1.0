package com.rxh.controller;


import com.rxh.cache.RedisCacheCommonCompoment;
import com.rxh.service.ConstantService;
import com.rxh.service.square.OrganizationService;
import com.rxh.spring.annotation.SystemLogInfo;
import com.rxh.square.pojo.OrganizationInfo;
import com.rxh.utils.SystemConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/organization")
public class OrganizationInfoController {

    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private ConstantService constantService;
    @Autowired
    private RedisCacheCommonCompoment redisCacheCommonCompoment;

    @SystemLogInfo(description = "支付机构查询")
    @RequestMapping("/getAll")
    public List<OrganizationInfo> getAllOrganization(@RequestBody OrganizationInfo organizationInfo){
        //modify by gjm 添加redis  at 20190809 start
        List<OrganizationInfo> organizationInfos = null;
        if (organizationInfo == null){
            organizationInfos = redisCacheCommonCompoment.organizationInfoCache.getAll();
        }

        if(null == organizationInfos || organizationInfos.isEmpty()){
            organizationInfos = organizationService.getAll(organizationInfo);
        }
        //modify by gjm 添加redis  at 20190809 end
        return organizationInfos;
    }
    @SystemLogInfo(description = "支付机构增加")
    @RequestMapping("/insert")
    public int insert(@RequestBody OrganizationInfo record){
        record.setCreator(SecurityContextHolder.getContext().getAuthentication().getName());

        int insert = organizationService.insert(record);
        return insert;
    }
    @SystemLogInfo(description = "支付机构删除")
    @RequestMapping("/delete")
    int delete(@RequestBody List<String> idList){
        int i = organizationService.deleteByPrimaryKey(idList);
        return i;
    }
    @SystemLogInfo(description = "支付机构修改")
    @RequestMapping("/update")
    public int   update(@RequestBody OrganizationInfo record){
        int i = organizationService.updateByPrimaryKeySelective(record);
        return i;
    }
    @RequestMapping("/init")
    public Map<String, Object> init(){
        Map<String, Object> init = new HashMap<>();
        init.put( "organizations",organizationService.getIdsAndName());
        init.put("status", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.availableStatus));
//        init.put("merchantType", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.MERCHANTTYPE));
    //        init.put("identityType", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.IDENTITYTYPE));
    //        init.put("payType", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.PAYTYPE));
    //        init.put("merchants", merchantInfoService.getIdsAndName());
    //        init.put("agentMerchants", agentMerchantInfoService.getAllIdAndName());
    //        init.put("organizations",organizationService .getIdsAndName());
    //        init.put("channels", channelWalletService.getIdsAndName());
        return init;
    }





}
