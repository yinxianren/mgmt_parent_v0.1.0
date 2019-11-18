package com.rxh.service.impl;

import com.internal.playment.api.db.system.ApiSysConstantService;
import com.internal.playment.common.enums.StatusEnum;
import com.rxh.service.NewSystemService;
import com.rxh.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/11/18
 * Time: 上午9:30
 * Description:
 */
public class NewSystemServiceImpl implements NewSystemService {

    @Autowired
    private ApiSysConstantService apiSysConstantService;

    @Override
    public ResponseVO getConstantByGroupName(String GroupName) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setCode(StatusEnum._0.hashCode());
        responseVO.setMessage(StatusEnum._0.getRemark());
        responseVO.setData(apiSysConstantService.);
        return null;
    }
}
