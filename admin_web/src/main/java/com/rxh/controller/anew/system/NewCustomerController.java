package com.rxh.controller.anew.system;

import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.table.system.UserLoginIpTable;
import com.rxh.pojo.base.Page;
import com.rxh.pojo.base.PageResult;
import com.rxh.service.AnewUserLoginIpService;
import com.rxh.service.square.CustomerService;
import com.rxh.spring.annotation.SystemLogInfo;
import com.rxh.square.pojo.UserLoginIp;
import com.rxh.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;
import java.util.Map;


/*
jones
 */
@Controller
@RequestMapping("/customer")
public class NewCustomerController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private AnewUserLoginIpService anewUserLoginIpService;

    @RequestMapping(value = "/getInit")
    @ResponseBody
    public Map<String, Object> getInit() {
        return customerService.getInitCustomers();
    }


    @SystemLogInfo(description = "IP查询")
    @RequestMapping("/searchIp")
    @ResponseBody
    public ResponseVO searchIP(@RequestBody Page page ) {
        try {
            return anewUserLoginIpService.page(page);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseVO(StatusEnum._1.getStatus(),StatusEnum._1.getRemark());
        }
    }

    @SystemLogInfo(description = "IP删除")
    @RequestMapping("/delIp")
    @ResponseBody
    public ResponseVO bathDel(@RequestBody List<Long> ids){
        try {
            return anewUserLoginIpService.delByIds(ids);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseVO(StatusEnum._1.getStatus(),StatusEnum._1.getRemark());
        }
    }

    @SystemLogInfo(description = "IP新增")
    @RequestMapping("/insertIp")
    @ResponseBody
    public ResponseVO insertIp(@RequestBody UserLoginIpTable record ){
        try {
            record.setAddTime(new Date());
            return anewUserLoginIpService.saveOrUpdate(record);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseVO(StatusEnum._1.getStatus(),StatusEnum._1.getRemark());
        }
    }

}
