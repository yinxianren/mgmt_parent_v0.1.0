package com.rxh.controller;

import com.rxh.pojo.sys.SysGroup;
import com.rxh.service.GroupService;
import com.rxh.spring.annotation.SystemLogInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.Map;

@Controller
//@RequestMapping("/sysgroup")
public class SysGroupController {

    @Autowired
    private GroupService groupService;
    @SystemLogInfo(description = "常量组别查询")
    @RequestMapping("/findSysGroup")
    @ResponseBody
    public Map<String, Object> findSysGroup(@RequestBody Map<String, Object> paramMap){
        return groupService.findSysGroup(paramMap);
    }
    @SystemLogInfo(description = "常量组别新增")
    @RequestMapping("/addSysGroup")
    @ResponseBody
    public Map<String, Object> addSysGroup(@RequestBody SysGroup sysGroup){//
        return  groupService.addSysGroup(sysGroup);
    }
    @SystemLogInfo(description = "常量组别更新")
    @RequestMapping("/update")
    @ResponseBody
    public Map<String, Object> update(@RequestBody SysGroup sysGroup){
        return groupService.updateSysGroup(sysGroup);
    }
    @SystemLogInfo(description = "常量组别删除")
    @RequestMapping("/batchDel")
    @ResponseBody
    public Map<String, Object> batchDel(@RequestBody Map<String, Object> paramMap) {
        Map<String, Object> result = groupService.deleteByIds(paramMap);
        return result;
    }

    @RequestMapping(value = "/groupName/isExist")
    @ResponseBody
    public Boolean isGroupNameExist(@RequestBody String groupName) {
        return groupService.selectUserByGroupName(groupName) != null;
    }

    @RequestMapping(value = "/groupCode/isExist")
    @ResponseBody
    public Boolean isGroupcodeExist(@RequestBody String groupCode) {
        return groupService.selectUserByGroupCode(groupCode) != null;
    }


}
