package com.rxh.controller;

import com.internal.playment.common.enums.SystemConstant;
import com.internal.playment.common.page.Page;
import com.internal.playment.common.page.ResponseVO;
import com.internal.playment.common.table.system.SysConstantTable;
import com.rxh.pojo.base.PageResult;
import com.rxh.pojo.merchant.MerchantQuestionWithBLOBs;
import com.rxh.pojo.sys.SysArea;
import com.rxh.pojo.sys.SysConstant;
import com.rxh.service.ConstantService;
import com.rxh.service.SystemService;
import com.rxh.service.system.NewSystemConstantService;
import com.rxh.service.system.NewSystemLogService;
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
    private NewSystemConstantService constantService;
    @Resource
    private NewSystemLogService systemService;

    @RequestMapping(value = "/getConstantByGroupName")
    public List<SysConstantTable> getConstantByGroupName(@RequestBody String groupName) {
        return (List)constantService.getConstantByGroupName(groupName).getData();
    }

    @RequestMapping(value = "/getConstantByGroupNameAndSortValueIsNotNULL")
    public List<SysConstantTable> getConstantByGroupNameAndSortValueIsNotNULL(@RequestBody String groupName) {
        return (List)constantService.getConstantByGroupName(groupName).getData();
    }

    @RequestMapping(value = "/getSystemLog")
    public ResponseVO getSystemLog(@RequestBody Page page) {
        return systemService.page(page);
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