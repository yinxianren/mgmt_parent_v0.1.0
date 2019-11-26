package com.rxh.controller;

import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.page.ResponseVO;
import com.internal.playment.common.table.merchant.MerchantInfoTable;
import com.rxh.service.AnewChannelWalletService;
import com.rxh.service.OrganizationInfoService;
import com.rxh.service.agent.AnewAgentMerchantService;
import com.rxh.service.merchant.AnewMerchantInfoService;
import com.rxh.service.merchant.AnewMerchantWalletService;
import com.rxh.service.system.NewSystemConstantService;
import com.rxh.spring.annotation.SystemLogInfo;
import com.rxh.util.UserInfoUtils;
import com.internal.playment.common.enums.SystemConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

@RestController
@RequestMapping("/merchantInfo")
public class MerchantInfoController {


    @Autowired
    AnewMerchantInfoService merchantInfoService;
    @Autowired
    NewSystemConstantService constantService;
    @Autowired
    AnewAgentMerchantService agentMerchantInfoService;
    @Resource
    private AnewChannelWalletService channelWalletService;
    @Resource
    private OrganizationInfoService organizationService;
    @Resource
    private AnewMerchantWalletService merchantWalletService;
    @Resource
    private   BCryptPasswordEncoder passwordEncoder;

    @ResponseBody
    @RequestMapping("/getOne")
    public MerchantInfoTable getOne(MerchantInfoTable merchantInfo){
        return ((List<MerchantInfoTable>)merchantInfoService.getMerchants(merchantInfo).getData()).get(0);
    }



    @SystemLogInfo(description = "新增商户")
    @RequestMapping("/addMerchantInfo")
    public ResponseVO insert(@RequestBody  MerchantInfoTable record){
        record.setCreateTime(new Date());
        record.setUpdateTime(new Date());
        return merchantInfoService.saveOrUpdate(record);
    }
    @SystemLogInfo(description = "删除商户")
    @RequestMapping("/batchDel")
    public ResponseVO delete(@RequestBody List<String> ids){
        return merchantInfoService.delByIds(ids);
    }
    @SystemLogInfo(description = "更新商户信息")
    @RequestMapping("/updateMerchantInfo")
    public ResponseVO update(@RequestBody MerchantInfoTable record){
        record.setUpdateTime(new Date());
        return merchantInfoService.saveOrUpdate(record);
    }
    @SystemLogInfo(description = "获取商户列表")
    @RequestMapping("/getAllMerchantInfoByAgentMerchantId")
             ResponseVO getAllByAgentMerchantId(){
        String agentMerchantId = UserInfoUtils.getMerchantId();
        MerchantInfoTable merchantInfoTable = new MerchantInfoTable();
        merchantInfoTable.setAgentMerchantId(agentMerchantId);
        return merchantInfoService.getMerchants(merchantInfoTable);
    }

    @RequestMapping("/getMerchants")
    ResponseVO getMerchants(){
        String agentMerchantId = UserInfoUtils.getMerchantId();
        MerchantInfoTable merchantInfoTable = new MerchantInfoTable();
        merchantInfoTable.setAgentMerchantId(agentMerchantId);
        return merchantInfoService.getMerchants(merchantInfoTable);
    }



    @RequestMapping("/getMerchantInfoListByMerchantInfo")
    ResponseVO search(@RequestBody MerchantInfoTable merchantInfo){
        String agentMerchantId = UserInfoUtils.getMerchantId();
        MerchantInfoTable merchantInfoTable = new MerchantInfoTable();
        merchantInfoTable.setAgentMerchantId(agentMerchantId);
        return merchantInfoService.getMerchants(merchantInfoTable);
    }

    @RequestMapping("/getRandomSecretkey")
    @ResponseBody
    public ResponseVO getRandomSecretkey(){
        String val = "";
        Random random = new Random();
        //length为几位密码
        for(int i = 0; i < 8; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if( "char".equalsIgnoreCase(charOrNum) ) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char)(random.nextInt(26) + temp);
            } else if( "num".equalsIgnoreCase(charOrNum) ) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return new ResponseVO(StatusEnum._0.getStatus(),val);
    }

    @RequestMapping("/init")
    public Map<String, Object> init() {
        Map<String, Object> init = new HashMap<>();
        init.put("merchantType", constantService.getConstantByGroupName(SystemConstant.MERCHANTTYPE).getData());
        init.put("identityType", constantService.getConstantByGroupName(SystemConstant.IDENTITYTYPE).getData());
        init.put("payType", constantService.getConstantByGroupName(SystemConstant.PAYTYPE).getData());
        init.put("status", constantService.getConstantByGroupName(SystemConstant.availableStatus).getData());
        init.put("merchants", merchantInfoService.getMerchants(null).getData());
        init.put("agentMerchants", agentMerchantInfoService.list(null).getData());
        init.put("organizations",organizationService .getAll(null).getData());
        init.put("channels", channelWalletService.search(null).getData());
        return init;
    }

    /**
     * 获取parentId
     */
    @ResponseBody
    @RequestMapping(value = "/parentId", method = RequestMethod.GET)
    public String parentId(){
        String parentId = UserInfoUtils.getMerchantId().toString();
        return parentId;
    }






}
