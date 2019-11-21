package com.rxh.service.system;

import com.rxh.vo.ResponseVO;

import java.util.Map;

public interface NewSystemConstantService {

    ResponseVO getConstantByGroupName(String GroupName);

    Map getConstantsMapByGroupName(String GroupName);
}
