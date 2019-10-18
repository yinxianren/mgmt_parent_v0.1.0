package com.rxh.service.impl;

import com.rxh.mapper.merchant.MerchantInfoMapper;
import com.rxh.mapper.square.*;
import com.rxh.pojo.Result;
import com.rxh.pojo.merchant.MerchantUser;
import com.rxh.service.AgmentUserService;
import com.rxh.service.square.AgentMerchantInfoService;
import com.rxh.square.pojo.*;

import com.rxh.square.vo.VoAgentMerchantInfo;
import com.rxh.utils.UUID;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AgentMerchantInfoServiceImpl implements AgentMerchantInfoService {

    @Autowired
    private AgentMerchantInfoMapper agentMerchantInfoMapper;

    @Autowired
    private AgentMerchantSettingMapper agentMerchantSettingMapper;

    @Autowired
    private AgentWalletMapper agentWalletMapper;
    @Autowired
    private AgmentUserService agmentUserService;
    @Autowired
    private MerchantSquareInfoMapper merchantSquareInfoMapper;
    @Autowired
    private PayOrderMapper payOrderMapper;
    @Autowired
    private TransOrderMapper transOrderMapper;

    @Override
    @Transactional
    public synchronized int insertAgentMerchantInfo(AgentMerchantInfo agentMerchantInfo,String username) {
        String maxAgentMerchantId = agentMerchantInfoMapper.getMaxAgentMerchantId();
        String id = UUID.createNumber("A",maxAgentMerchantId);
        // 保存商户信息
        agentMerchantInfo.setAgentMerchantId(id);
        int num = agentMerchantInfoMapper.insert(agentMerchantInfo);
        // 保存商户配置信息
        saveAgentMerchantSetting(agentMerchantInfo,username);
        return num;
    }

    private void saveAgentMerchantSetting(AgentMerchantInfo agentMerchantInfo,String username) {
        // AgentMerchantSetting agentMerchantSetting = agentMerchantInfo.getAgentMerchantSetting();
        // agentMerchantSetting.setId(UUID.createKey("agent_merchant_setting",""));
        // agentMerchantSetting.setAgentMerchantId(agentMerchantInfo.getAgentMerchantId());

        AgentWallet agentWallet = new AgentWallet();
        agentWallet.setUpdateTime(new Date());
        agentWallet.setAgentMerchantId(agentMerchantInfo.getAgentMerchantId());

        MerchantUser merchantUser=new MerchantUser();
        merchantUser.setUserName(agentMerchantInfo.getLoginName());
        merchantUser.setPassword(agentMerchantInfo.getPassword());
        merchantUser.setBelongto(agentMerchantInfo.getAgentMerchantId());

        agentWalletMapper.insertSelective(agentWallet);
        // agentMerchantSettingMapper.insert(agentMerchantSetting);
        agmentUserService.addAdminRole(agentMerchantInfo.getAgentMerchantId(),username,merchantUser);

    }

    @Override
    public int deleteAgentMerchantInfo(String id) {
       List<MerchantInfo> merchantInfos= merchantSquareInfoMapper.getMerchantsByParentId(id);
       if (merchantInfos.size()>0){
           for (MerchantInfo merchantInfo : merchantInfos) {
               Integer payNum = payOrderMapper.countByMerId(merchantInfo.getMerId());
               Integer transNum = transOrderMapper.countByMerId(merchantInfo.getMerId());
               if (payNum>0||transNum>0) {
                   return 0;
               }
           }
       }
        int delNum = agentMerchantInfoMapper.deleteByPrimaryKey(id);
        return delNum;
    }

    @Override
    @Transactional
    public int update(AgentMerchantInfo agentMerchantInfo) {
        // 修改商户配置信息
       // agentMerchantSettingMapper.updateByMerId(agentMerchantInfo.getAgentMerchantSetting());
        int updateNum = agentMerchantInfoMapper.updateByPrimaryKey(agentMerchantInfo);
        return updateNum;
    }

    @Override
    public List<AgentMerchantInfo> getAll() {
        List<AgentMerchantInfo> agentMerchantInfos = agentMerchantInfoMapper.selectByExample(null);
        return agentMerchantInfos;
    }

    @Override
    public Boolean isAgentMerchantIdExist(String agentMerchantId) {
        return agentMerchantInfoMapper.selectByPrimaryKey(agentMerchantId) != null;
    }

    @Override
    public List<AgentMerchantInfo> getAllByVoAgentMerchantInfo(VoAgentMerchantInfo voAgentMerchantInfo) {
        List<VoAgentMerchantInfo> voAgentMerchantInfos = agentMerchantInfoMapper.getAllByVoAgentMerchantInfo(voAgentMerchantInfo);
        // 将VoAgentMerchantInfo 转换成AgentMerchantInfo返回给前端
        List<AgentMerchantInfo> agentMerchantInfos = new ArrayList<>();
        for (VoAgentMerchantInfo vo : voAgentMerchantInfos){
            AgentMerchantInfo agentMerchantInfo = new AgentMerchantInfo();
            BeanUtils.copyProperties(vo,agentMerchantInfo);
            AgentMerchantSetting agentMerchantSetting = new AgentMerchantSetting();
            BeanUtils.copyProperties(vo,agentMerchantSetting);
            agentMerchantInfo.setAgentMerchantSetting(agentMerchantSetting);
            agentMerchantInfos.add(agentMerchantInfo);
        }
        return agentMerchantInfos;
    }

    @Override
    public Result deleteByIdArray(List<String> idArray) {
        // 判断代理商是否有被代理商钱包表引用
        List<AgentWallet> agentWallets = agentWalletMapper.selectByExample(null);
        for (String id : idArray) {
            List<MerchantInfo> merchantInfos= merchantSquareInfoMapper.getMerchantsByParentId(id);
            if (merchantInfos.size()>0){
                for (MerchantInfo merchantInfo : merchantInfos) {
                    Integer payNum = payOrderMapper.countByMerId(merchantInfo.getMerId());
                    Integer transNum = transOrderMapper.countByMerId(merchantInfo.getMerId());
                    if (payNum>0||transNum>0) {
                        return  new Result(Result.FAIL,"代理商编号："+id+"存在订单");
                    }
                }
            }
        }

        // for (String id : idArray){
        //     for (AgentWallet agentWallet : agentWallets){
        //         if(id.equals(agentWallet.getAgentMerchantId())){
        //             return  new Result(Result.FAIL,"代理商编号："+id+"在钱包中被引用");
        //         }
        //     }
        // }
        int num = 0;
        for(String id : idArray){
            //删除商户配置表信息
            agentMerchantSettingMapper.deleteByAgentMerchantId(id);
            agentWalletMapper.deleteByPrimaryKey(id);
            //删除商户表信息
            deleteAgentMerchantInfo(id);
            num++ ;
        }
        return new Result(Result.SUCCESS,"删除数据成功");
    }

    @Override
    public String getAgentMerchantIdIncre() {
        return agentMerchantInfoMapper.getAgentMerchantIdIncre();
    }

    @Override
    public String getMaxAgentMerchantId() {
        String maxAgentMerchantId = agentMerchantInfoMapper.getMaxAgentMerchantId();
       /* int id = UUID.createIntegerKey(maxAgentMerchantId);
          return String.valueOf(id);*/
        String id = UUID.createNumber("A",maxAgentMerchantId);
        return  id ;

    }

    @Override
    public List<AgentMerchantInfo> getAllIdAndName() {
        return agentMerchantInfoMapper.getAllIdAndName();
    }

    @Override
    public List<MerchantInfo> getAllMerchantIdAndName(String merchantId) {
        return agentMerchantInfoMapper.getAllMerchantIdAndName(merchantId);
    }

    @Override
    public AgentMerchantInfo getMerchantInfo(String merchantId) {
        AgentMerchantInfo merchantInfo = new AgentMerchantInfo();
        merchantInfo.setAgentMerchantId(agentMerchantInfoMapper.selectByPrimaryKey(merchantId).getAgentMerchantId());
        merchantInfo.setAgentMerchantName(agentMerchantInfoMapper.selectByPrimaryKey(merchantId).getAgentMerchantName());
        merchantInfo.setAgentMerchantShortName(agentMerchantInfoMapper.selectByPrimaryKey(merchantId).getAgentMerchantShortName());
        merchantInfo.setAgentIdentityType(agentMerchantInfoMapper.selectByPrimaryKey(merchantId).getAgentIdentityType());
        merchantInfo.setAgentIdentityNum(agentMerchantInfoMapper.selectByPrimaryKey(merchantId).getAgentIdentityNum());
        merchantInfo.setAgentIdentityUrl(agentMerchantInfoMapper.selectByPrimaryKey(merchantId).getAgentIdentityUrl());
        merchantInfo.setAgentPhone(agentMerchantInfoMapper.selectByPrimaryKey(merchantId).getAgentPhone());
        merchantInfo.setAgentPhoneStatus(agentMerchantInfoMapper.selectByPrimaryKey(merchantId).getAgentPhoneStatus());
        merchantInfo.setAgentEmail(agentMerchantInfoMapper.selectByPrimaryKey(merchantId).getAgentEmail());
        merchantInfo.setAgentEmailStatus(agentMerchantInfoMapper.selectByPrimaryKey(merchantId).getAgentEmailStatus());
        merchantInfo.setAgentQq(agentMerchantInfoMapper.selectByPrimaryKey(merchantId).getAgentQq());
        merchantInfo.setAgentStatus(agentMerchantInfoMapper.selectByPrimaryKey(merchantId).getAgentStatus());
        merchantInfo.setPassword(agentMerchantInfoMapper.selectByPrimaryKey(merchantId).getPassword());
        merchantInfo.setLoginName(agentMerchantInfoMapper.selectByPrimaryKey(merchantId).getLoginName());
        return merchantInfo;
    }
}
