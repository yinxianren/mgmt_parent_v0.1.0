package com.rxh.service.impl;

import com.rxh.mapper.square.AgentMerchantSettingMapper;
import com.rxh.mapper.sys.SysConstantMapper;
import com.rxh.pojo.Result;
import com.rxh.pojo.sys.SysConstant;
import com.rxh.service.square.AgentMerchantSettingService;
import com.rxh.spring.annotation.RedisCacheDeleteByHashKey;
import com.rxh.square.pojo.AgentMerchantSetting;
import com.rxh.utils.SystemConstant;
import com.rxh.utils.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AgentMerchantSettingServiceImpl implements AgentMerchantSettingService {

    @Autowired
    private AgentMerchantSettingMapper agentMerchantSettingMapper;
    @Autowired
    private SysConstantMapper sysConstantMapper;


    @RedisCacheDeleteByHashKey(hashKey = "agent_merchant_setting")
    @Override
    public Result insert(AgentMerchantSetting record) {
        Result<String> result = new Result<>();
        int i = agentMerchantSettingMapper.insert(record);
        if (i>0){
            result.setCode(Result.SUCCESS);
            result.setMsg("新增代理商费率成功");
        }else {
            result.setCode(Result.FAIL);
            result.setMsg("新增代理商费率失败");
        }
        return result;
    }

    @RedisCacheDeleteByHashKey(hashKey = "agent_merchant_setting")
    @Override
    public Result update(List<AgentMerchantSetting> param) {
        //3个对象
        Result<String> result = new Result<>();
        try {
            for (AgentMerchantSetting agentMerchantSetting : param) {
                if(agentMerchantSetting.getId().length()>0 && agentMerchantSetting.getId()!=null){
                    //id不为空代表update
                    agentMerchantSettingMapper.updateByPrimaryKey(agentMerchantSetting);
                }else if (agentMerchantSetting.getId().length()==0&&agentMerchantSetting.getStatus()==0){
                    //id为'temp'代表不存在记录 插入状态为通过的数据
                    Long id = UUID.createKey("merchant_rate");
                    agentMerchantSetting.setId(id.toString());
                    agentMerchantSettingMapper.insert(agentMerchantSetting);
                }
            }
            result.setCode(Result.SUCCESS);
            result.setMsg("更新代理商费率成功");
        } catch (Exception e) {
            result.setCode(Result.FAIL);
            result.setMsg("更新代理商费率失败");
            e.printStackTrace();
        } finally {
            return result;
        }
    }

    @RedisCacheDeleteByHashKey(hashKey = "agent_merchant_setting")
    @Override
    public Result delete(String id) {
        return null;
    }

    @Override
    public List<AgentMerchantSetting> search(String merId) {
        List<AgentMerchantSetting> list=new ArrayList<>();

        List<AgentMerchantSetting> result = agentMerchantSettingMapper.search(new AgentMerchantSetting().lsetAgentMerchantId(merId));
        List<SysConstant> payTypes = sysConstantMapper.selectByGroupName(SystemConstant.PAYTYPE);
        List<String> resultStr=new ArrayList<>();
        if(result.size()==0){
            for (int i = 0; i < payTypes.size(); i++) {
                AgentMerchantSetting agentMerchantSetting = new AgentMerchantSetting();
                agentMerchantSetting.setId("");
                agentMerchantSetting.setPayType(payTypes.get(i).getFirstValue());
                agentMerchantSetting.setAgentMerchantId(merId);
                agentMerchantSetting.setStatus(1);
                list.add(agentMerchantSetting);
            }
            return list;
        }
        for (AgentMerchantSetting rate : result) {
            resultStr.add(rate.getPayType());
        }
        for (SysConstant rate : payTypes) {
            if (!resultStr.contains(rate.getFirstValue())){
                AgentMerchantSetting agentMerchantSetting = new AgentMerchantSetting();
                agentMerchantSetting.setId("");
                agentMerchantSetting.setStatus(1);
                agentMerchantSetting.setAgentMerchantId(merId);
                agentMerchantSetting.setPayType(rate.getFirstValue());
                result.add(agentMerchantSetting);
            }

        }
        return result;
    }

    @Override
    public List<AgentMerchantSetting> merSearch(String merId) {
        List<AgentMerchantSetting> result = agentMerchantSettingMapper.merSearch(merId);
        System.out.println(result);
        return result;
    }

    @Override
    public List<AgentMerchantSetting> getAll() {
        return agentMerchantSettingMapper.selectByExample(null);
    }

    @Override
    public AgentMerchantSetting getAgentMerchantSettingByMerIdAndPayType(String merId,String payType){
        return agentMerchantSettingMapper.getAgentMerchantSettingByParentIdAndPayType(merId,payType);
    }
}
