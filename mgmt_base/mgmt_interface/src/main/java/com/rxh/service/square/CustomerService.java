package com.rxh.service.square;


import com.rxh.pojo.base.Page;
import com.rxh.pojo.base.PageResult;
import com.rxh.square.pojo.UserLoginIp;

import java.util.List;
import java.util.Map;

public interface CustomerService {
     Map<String, Object> getInitCustomers();

    PageResult findByPage(Page page);

    int insertIp(UserLoginIp record);

    Boolean deleteIp(List<String> ids);

}
