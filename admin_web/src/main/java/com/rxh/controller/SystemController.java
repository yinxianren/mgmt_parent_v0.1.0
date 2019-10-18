package com.rxh.controller;

import com.rxh.pojo.base.Page;
import com.rxh.pojo.base.PageResult;
import com.rxh.pojo.sys.SysArea;
import com.rxh.pojo.sys.SysConstant;
import com.rxh.service.AgentSystemService;
import com.rxh.service.ConstantService;
import com.rxh.service.MerchantSystemService;
import com.rxh.service.SystemService;
import com.rxh.spring.annotation.SystemLogInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: 陈俊雄
 * Date: 2018/3/15
 * Time: 10:10
 * Project: Management
 * Package: com.rxh.controller
 */
@Controller
@RequestMapping(value = "/system")
public class SystemController {

    @Resource
    private ConstantService constantService;
    @Resource
    private SystemService systemService;
    @Resource
    private MerchantSystemService merchantSystemService;
    @Resource
    private AgentSystemService agentSystemService;

    @RequestMapping(value = "/getConstantByGroupName")
    @ResponseBody
    public List<SysConstant> getConstantByGroupName(@RequestBody String groupName) {
        return constantService.getConstantByGroupName(groupName);
    }

    @RequestMapping(value = "/getConstantByGroupNameAndSortValueIsNotNULL")
    @ResponseBody
    public List<SysConstant> getConstantByGroupNameAndSortValueIsNotNULL(@RequestBody String groupName) {
        return constantService.getConstantByGroupNameAndSortValueIsNotNULL(groupName);
    }

    @SystemLogInfo(description = "系统日志查询")
    @RequestMapping(value = "/getSystemLog")
    @ResponseBody
    public PageResult getSystemLog(@RequestBody Page page) {
        return systemService.getSystemLog(page);
    }
    @SystemLogInfo(description = "商户系统日志查询")
    @RequestMapping(value = "/getMerchantSystemLog")
    @ResponseBody
    public PageResult getMerchantSystemLog(@RequestBody Page page) {
        return merchantSystemService.getSystemLog(page);
    }
    @SystemLogInfo(description = "代理商系统日志查询")
    @RequestMapping(value = "/getAgentSystemLog")
    @ResponseBody
    public PageResult getAgentSystemLog(@RequestBody Page page) {
        return agentSystemService.getSystemLog(page);
    }


    @RequestMapping(value = "/getAreaInfoByCountryCode")
    @ResponseBody
    public List<SysArea> getAreaInfoByCountryCode(@RequestBody String countryCode) {
        return systemService.getAreaInfoByCountryCode(countryCode);
    }



}
