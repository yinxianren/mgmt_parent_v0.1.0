package com.rxh.controller;

import com.rxh.pojo.base.Page;
import com.rxh.pojo.base.PageResult;
import com.rxh.pojo.merchant.MerchantQuestionWithBLOBs;
import com.rxh.pojo.sys.SysArea;
import com.rxh.pojo.sys.SysConstant;
import com.rxh.service.ConstantService;
import com.rxh.service.SystemService;
import com.rxh.util.UserInfoUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

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
@ResponseBody
public class SystemController {

    @Resource
    private ConstantService constantService;
    @Resource
    private SystemService systemService;

    @RequestMapping(value = "/getConstantByGroupName")
    public List<SysConstant> getConstantByGroupName(@RequestBody String groupName) {
        return constantService.getConstantByGroupName(groupName);
    }

    @RequestMapping(value = "/getConstantByGroupNameAndSortValueIsNotNULL")
    public List<SysConstant> getConstantByGroupNameAndSortValueIsNotNULL(@RequestBody String groupName) {
        return constantService.getConstantByGroupNameAndSortValueIsNotNULL(groupName);
    }

    @RequestMapping(value = "/getSystemLog")
    public PageResult getSystemLog(@RequestBody Page page) {
        return systemService.getSystemLog(page);
    }

    @RequestMapping(value = "/getAreaInfoByCountryCode")
    public List<SysArea> getAreaInfoByCountryCode(@RequestBody String countryCode) {
        return systemService.getAreaInfoByCountryCode(countryCode);
    }

    @RequestMapping(value = "/getOpinionInit")
    public Map<String, Object> getOpinionInit() {
        return systemService.getMerchantOpinionInit();
    }

}