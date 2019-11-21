package com.rxh.service.system;

import com.internal.playment.common.page.ResponseVO;

import java.util.Map;

public interface NewSystemConstantService {

    ResponseVO getConstantByGroupName(String GroupName);

    Map getConstantsMapByGroupName(String GroupName);
}
