package com.rxh.controller.anew.system;

import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.table.system.SysGroupTable;
import com.internal.playment.common.page.Page;
import com.rxh.service.system.NewSysGroupService;
import com.rxh.spring.annotation.SystemLogInfo;
import com.rxh.vo.ResponseVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/sysgroup")
public class NewSysGroupController {

    @Autowired
    private NewSysGroupService newSysGroupService;

    @SystemLogInfo(description = "常量组别查询")
    @RequestMapping("/findSysGroup")
    @ResponseBody
    public ResponseVO findSysGroup(@RequestBody Page page){
        try {
            return newSysGroupService.page(page);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseVO(StatusEnum._1.getStatus(),StatusEnum._1.getRemark());
        }
    }

    @SystemLogInfo(description = "常量组别新增")
    @RequestMapping("/addSysGroup")
    @ResponseBody
    public ResponseVO addSysGroup(@RequestBody SysGroupTable sysGroup){
        try {
            sysGroup.setCreateTime(new Date());
            return newSysGroupService.saveOrUpdate(sysGroup);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseVO(StatusEnum._1.getStatus(),StatusEnum._1.getRemark());
        }
    }

    @SystemLogInfo(description = "常量组别更新")
    @RequestMapping("/update")
    @ResponseBody
    public ResponseVO update(@RequestBody SysGroupTable sysGroup){
        try {
            sysGroup.setUpdateTime(new Date());
            return newSysGroupService.saveOrUpdate(sysGroup);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseVO(StatusEnum._1.getStatus(),StatusEnum._1.getRemark());
        }
    }

    @SystemLogInfo(description = "常量组别删除")
    @RequestMapping("/batchDel")
    @ResponseBody
    public ResponseVO batchDel(@RequestBody Map<String, Object> paramMap) {
        try {
            String codes = paramMap.get("codes").toString();
            return newSysGroupService.batchByIds(codes);
        }catch (Exception e){
            e.printStackTrace();
            return  new ResponseVO(StatusEnum._1.getStatus(),StatusEnum._1.getRemark());
        }
    }

    @RequestMapping(value = "/groupName/isExist")
    @ResponseBody
    public ResponseVO isGroupNameExist(@RequestBody String groupName) {
        try {
            SysGroupTable sysGroupTable = new SysGroupTable();
            sysGroupTable.setName(groupName);
            ResponseVO responseVO = newSysGroupService.getList(sysGroupTable);
            if (CollectionUtils.isEmpty((List)responseVO.getData())) responseVO.setCode(StatusEnum._0.getStatus());
            else responseVO.setCode(StatusEnum._1.getStatus());
            return responseVO;
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseVO(StatusEnum._1.getStatus(),StatusEnum._1.getRemark());
        }
    }

    @RequestMapping(value = "/groupCode/isExist")
    @ResponseBody
    public ResponseVO isGroupcodeExist(@RequestBody String groupCode) {
        try {
            SysGroupTable sysGroupTable = new SysGroupTable();
            sysGroupTable.setCode(groupCode);
            ResponseVO responseVO = newSysGroupService.getList(sysGroupTable);
            if (CollectionUtils.isEmpty((List)responseVO.getData())) responseVO.setCode(StatusEnum._0.getStatus());
            else responseVO.setCode(StatusEnum._1.getStatus());
            return responseVO;
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseVO(StatusEnum._1.getStatus(),StatusEnum._1.getRemark());
        }
    }
}
