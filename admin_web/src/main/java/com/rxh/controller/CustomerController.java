package com.rxh.controller;

import com.rxh.pojo.base.Page;
import com.rxh.pojo.base.PageResult;
import com.rxh.service.square.CustomerService;
import com.rxh.spring.annotation.SystemLogInfo;
import com.rxh.square.pojo.OrganizationInfo;
import com.rxh.square.pojo.UserLoginIp;
import com.rxh.utils.SystemConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/*
jones
 */
@Controller
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @RequestMapping(value = "/getInit")
    @ResponseBody
    public Map<String, Object> getInit() {
        return customerService.getInitCustomers();
    }


    @SystemLogInfo(description = "IP查询")

    @RequestMapping("/searchIp")
    @ResponseBody
    public PageResult searchIP(@RequestBody Page page ) {
        PageResult pageResult = customerService.findByPage(page);
        return pageResult;
    }
    @SystemLogInfo(description = "IP删除")
    @RequestMapping("/delIp")
    @ResponseBody
    public Boolean bathDel(@RequestBody List<String> ids){
        return customerService.deleteIp(ids);
    }

    @SystemLogInfo(description = "IP新增")
    @RequestMapping("/insertIp")
    @ResponseBody
    public Boolean insertIp(@RequestBody UserLoginIp record ){
        return customerService.insertIp(record)>0;
    }

}
