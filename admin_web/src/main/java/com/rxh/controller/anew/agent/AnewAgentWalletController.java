package com.rxh.controller.anew.agent;

import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.table.agent.AgentMerchantWalletTable;
import com.internal.playment.common.page.Page;
import com.rxh.service.agent.AnewAgentMerchantService;
import com.rxh.service.agent.AnewAgentWalletService;
import com.rxh.service.system.NewSystemConstantService;
import com.rxh.spring.annotation.SystemLogInfo;
import com.internal.playment.common.enums.SystemConstant;
import com.internal.playment.common.page.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/agentWallet")
@Slf4j
public class AnewAgentWalletController {

    @Resource
    private NewSystemConstantService constantService;
    @Autowired
    private AnewAgentWalletService anewAgentWalletService;
    @Autowired
    private AnewAgentMerchantService anewAgentMerchantService;


    @SystemLogInfo(description = "代理商钱包查询")
    @RequestMapping("/search")
    public ResponseVO search(@RequestBody AgentMerchantWalletTable agentWallet){
        try {
            return anewAgentWalletService.search(agentWallet);
        }catch (Exception e){
            log.error(e.getMessage());
            ResponseVO responseVO = new ResponseVO();
            responseVO.setCode(StatusEnum._1.getStatus());
            responseVO.setMessage(StatusEnum._1.getRemark());
            return responseVO;
        }
    }

    @RequestMapping("/init")
    public Map<String, Object> init() {
        Map<String, Object> init = new HashMap<>();
        init.put("productTypes", constantService.getConstantByGroupName(SystemConstant.PRODUCTTYPE).getData());
        init.put("agents", anewAgentMerchantService.list(null).getData());
        return init;
    }
    @RequestMapping(value="/findAgentWallteDetailsPage")
    public ResponseVO search(@RequestBody Page page ){
        try {
            return anewAgentWalletService.pageByDetails(page);
        }catch (Exception e){
            log.error(e.getMessage());
            ResponseVO responseVO = new ResponseVO();
            responseVO.setCode(StatusEnum._1.getStatus());
            responseVO.setMessage(StatusEnum._1.getRemark());
            return responseVO;
        }
    }

}
