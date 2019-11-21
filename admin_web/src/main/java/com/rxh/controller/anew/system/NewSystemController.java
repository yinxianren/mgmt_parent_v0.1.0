package com.rxh.controller.anew.system;

import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.page.Page;
import com.internal.playment.common.table.system.SysConstantTable;
import com.rxh.service.system.NewAgentSysLogService;
import com.rxh.service.system.NewMerchantSysLogService;
import com.rxh.service.system.NewSystemConstantService;
import com.rxh.service.system.NewSystemLogService;
import com.rxh.spring.annotation.SystemLogInfo;
import com.internal.playment.common.page.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
public class NewSystemController {

    @Autowired
    private NewMerchantSysLogService newMerchantSysLogService;
    @Autowired
    private NewAgentSysLogService newAgentSysLogService;
    @Autowired
    private NewSystemConstantService newSystemConstantService;
    @Autowired
    private NewSystemLogService newSystemLogService;

    @RequestMapping(value = "/getConstantByGroupName")
    @ResponseBody
    public ResponseVO getConstantByGroupName(@RequestBody String groupName) {
        try {
            return newSystemConstantService.getConstantByGroupName(groupName);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseVO(StatusEnum._1.getStatus(),StatusEnum._1.getRemark());
        }
    }

    @RequestMapping(value = "/getConstantByGroupNameAndSortValueIsNotNULL")
    @ResponseBody
    public List<SysConstantTable> getConstantByGroupNameAndSortValueIsNotNULL(@RequestBody String groupName) {
        return (List)newSystemConstantService.getConstantByGroupName(groupName).getData();
    }

    @SystemLogInfo(description = "系统日志查询")
    @RequestMapping(value = "/getSystemLog")
    @ResponseBody
    public ResponseVO getSystemLog(@RequestBody Page page) {
        try {
            return newSystemLogService.page(page);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseVO(StatusEnum._1.getStatus(),StatusEnum._1.getRemark());
        }
    }

    @SystemLogInfo(description = "商户系统日志查询")
    @RequestMapping(value = "/getMerchantSystemLog")
    @ResponseBody
    public ResponseVO getMerchantSystemLog(@RequestBody Page page) {
        try {
            return newMerchantSysLogService.page(page);
        }catch (Exception e){
            e.printStackTrace();
            return  new ResponseVO(StatusEnum._1.getStatus(),StatusEnum._1.getRemark());
        }
    }

    @SystemLogInfo(description = "代理商系统日志查询")
    @RequestMapping(value = "/getAgentSystemLog")
    @ResponseBody
    public ResponseVO getAgentSystemLog(@RequestBody Page page) {
        try {
            return newAgentSysLogService.page(page);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseVO(StatusEnum._1.getStatus(),StatusEnum._1.getRemark());
        }
    }


   /* @RequestMapping(value = "/getAreaInfoByCountryCode")
    @ResponseBody
    public List<SysArea> getAreaInfoByCountryCode(@RequestBody String countryCode) {
        return systemService.getAreaInfoByCountryCode(countryCode);
    }*/



}
