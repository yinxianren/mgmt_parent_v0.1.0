package com.rxh.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rxh.mapper.square.CustomerMapper;
import com.rxh.mapper.square.UserLoginIpMapper;
import com.rxh.pojo.base.Page;
import com.rxh.pojo.base.PageResult;
import com.rxh.pojo.base.SearchInfo;
import com.rxh.service.square.CustomerService;
import com.rxh.square.pojo.Customer;
import com.rxh.square.pojo.UserLoginIp;
import com.rxh.utils.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private UserLoginIpMapper userLoginIpMapper;

    @Override
    public Map<String, Object> getInitCustomers() {
        Map<String, Object> map = new HashMap<>();
        ArrayList<Customer> list = new ArrayList<>();
        List<Customer> agents = customerMapper.getAgents();
        List<Customer> merChants = customerMapper.getMerChants();
        list.addAll(agents);
        list.addAll(merChants);
        map.put("customers",list);
        map.put("agents",agents);
        map.put("merChants",merChants);
        return map;
    }

    @Override
    public PageResult findByPage(Page page) {
        // 设置分页信息
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        if (page.getOrderBy() == null || page.getOrderBy().size() == 0) {
            PageHelper.orderBy("add_time DESC");
        } else {
            page.getOrderBy().forEach((key, value) -> PageHelper.orderBy(key + " " + value));
        }
        // 执行查询
        if (page.getSearchInfo() != null && page.getSearchInfo().getEndDate() != null) {
            page.getSearchInfo().setEndDate(com.rxh.utils.DateUtils.addDays(page.getSearchInfo().getEndDate(), 1));
        }
        // 执行查询
        if (page.getSearchInfo() != null && page.getSearchInfo().getEndDate2() != null) {
            page.getSearchInfo().setEndDate2(com.rxh.utils.DateUtils.addDays(page.getSearchInfo().getEndDate2(), 1));
        }
        List<UserLoginIp> list = userLoginIpMapper.selectBySearchInfo(page.getSearchInfo());
        PageInfo<UserLoginIp> pageInfo = new PageInfo<>(list);
        // 获取查询结果
        PageResult result = new PageResult();
        result.setRows(list);
        result.setTotal(pageInfo.getTotal());
        result.setAllPage(pageInfo.getPages());
        // 返回结果
        return result;
    }


    @Override
    public int insertIp(UserLoginIp record) {
        SearchInfo searchInfo = new SearchInfo();
        searchInfo.setLoginIp(record.getLoginIp());
        searchInfo.setCustomerId(record.getCustomerid());
        List<UserLoginIp> loginIps = userLoginIpMapper.selectBySearchInfo(searchInfo);
        if (loginIps.size()>0){
            return 0;
        }

        record.setId(UUID.createKey("user_login_ip", ""));
        record.setAddTime(new Date());
        return userLoginIpMapper.insertSelective(record);
    }

    @Override
    public Boolean deleteIp(List<String> ids) {
        int i=0;
        for (String id : ids) {
            userLoginIpMapper.deleteByPrimaryKey(id);
            i++;
        }
        return i==ids.size();
    }
}
