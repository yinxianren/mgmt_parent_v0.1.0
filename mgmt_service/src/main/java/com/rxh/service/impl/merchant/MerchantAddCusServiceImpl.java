package com.rxh.service.impl.merchant;

import com.rxh.mapper.square.MerchantRegisterInfoMapper;
import com.rxh.pojo.merchant.MerchantRegisterInfo;
import com.rxh.service.merchant.MerchantAddCusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MerchantAddCusServiceImpl  implements MerchantAddCusService {

    @Autowired
    private MerchantRegisterInfoMapper merchantRegisterInfoMapper;


    @Override
    public List<MerchantRegisterInfo> selsectUsableByMerchId(String merchId, Integer status) {
//        return merchantRegisterInfoMapper.selsectUsableByMerchId(merchId,status);
        return null;
    }

    @Override
    public List<MerchantRegisterInfo> getMerchantRegisterInfos( MerchantRegisterInfo merchantRegisterInfo) {
//        String merId=merchantRegisterInfo.getMerId();
//        String terminalMerId=merchantRegisterInfo.getTerminalMerId();
//        Integer status=merchantRegisterInfo.getStatus();
        return null;
//        return merchantRegisterInfoMapper.getMerchantRegisterInfos(merId,terminalMerId,status);
    }

    @Override
    public Integer insertSelective(MerchantRegisterInfo merchantRegisterInfo) {
        return merchantRegisterInfoMapper.insertSelective(merchantRegisterInfo);
    }

    @Override
    public Integer updateById(String id, Integer status, String param) {
        return null;
//        return merchantRegisterInfoMapper.updateById(id,status,param);

    }


}
