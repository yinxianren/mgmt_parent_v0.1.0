package com.rxh.controller.anew.channel;


import com.rxh.anew.table.system.OrganizationInfoTable;
import com.rxh.cache.RedisCacheCommonCompoment;
import com.rxh.service.ConstantService;
import com.rxh.service.OrganizationInfoService;
import com.rxh.service.anew.channel.ApiProductTypeSettingService;
import com.rxh.service.square.OrganizationService;
import com.rxh.spring.annotation.SystemLogInfo;
import com.rxh.utils.SystemConstant;
import com.rxh.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/organization")
public class AnewOrganizationInfoController {

    @Autowired
    private ConstantService constantService;
    @Autowired
    private OrganizationInfoService organizationInfoService;

    @SystemLogInfo(description = "支付机构查询")
    @RequestMapping("/getAll")
    public ResponseVO getAllOrganization(@RequestBody OrganizationInfoTable organizationInfo){
        return organizationInfoService.getAll(organizationInfo);
    }
    @SystemLogInfo(description = "支付机构增加")
    @RequestMapping("/insert")
    public ResponseVO insert(@RequestBody OrganizationInfoTable record){
        record.setCreator(SecurityContextHolder.getContext().getAuthentication().getName());
        record.setCreateTime(new Date());
        record.setUpdateTime(new Date());
        record.setOrganizationId("ORG"+System.currentTimeMillis());
        return organizationInfoService.savaOrUpdate(record);
    }
    @SystemLogInfo(description = "支付机构删除")
    @RequestMapping("/delete")
    public ResponseVO delete (@RequestBody List<String> idList){
        return organizationInfoService.removeByIds(idList);
    }
    @SystemLogInfo(description = "支付机构修改")
    @RequestMapping("/update")
    public ResponseVO update(@RequestBody OrganizationInfoTable record){
        record.setUpdateTime(new Date());
        return organizationInfoService.savaOrUpdate(record);
    }
    @RequestMapping("/init")
    public Map<String, Object> init(){
        Map<String, Object> init = new HashMap<>();
        init.put("status", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.availableStatus));
        return init;
    }





}
