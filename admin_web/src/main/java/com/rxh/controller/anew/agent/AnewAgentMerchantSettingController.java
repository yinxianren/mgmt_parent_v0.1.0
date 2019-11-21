package com.rxh.controller.anew.agent;

import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.table.agent.AgentMerchantSettingTable;
import com.rxh.service.agent.AnewAgentMerchantSettingService;
import com.rxh.service.system.NewSystemConstantService;
import com.internal.playment.common.enums.SystemConstant;
import com.internal.playment.common.page.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/agentMerchantSetting")
public class AnewAgentMerchantSettingController {

    @Resource
    private AnewAgentMerchantSettingService anewAgentMerchantSettingService;
    @Autowired
    private NewSystemConstantService constantService;

    @RequestMapping("/search")
    public ResponseVO search(@RequestBody AgentMerchantSettingTable agentMerchantSetting){
        try {
            return anewAgentMerchantSettingService.getList(agentMerchantSetting);
        }catch (Exception e){
            e.printStackTrace();
            ResponseVO responseVO = new ResponseVO();
            responseVO.setCode(StatusEnum._1.getStatus());
            responseVO.setMessage("失败");
            return responseVO;
        }
    }

    @RequestMapping("/update")
    public ResponseVO update(@RequestBody  List<AgentMerchantSettingTable> param ){

        try {
            return anewAgentMerchantSettingService.betchUpdate(param);
        }catch (Exception e){
            e.printStackTrace();
            ResponseVO responseVO = new ResponseVO();
            responseVO.setCode(StatusEnum._1.getStatus());
            responseVO.setMessage("失败");
            return responseVO;
        }
    }

    @RequestMapping("/init")
    public Map<String, Object> init(){
        Map<String, Object> init = new HashMap<>();
        init.put("status", constantService.getConstantByGroupName(SystemConstant.availableStatus).getData());
        init.put("agentRate",constantService.getConstantByGroupName(SystemConstant.PRODUCTTYPE).getData());
        return init;
    }
}
