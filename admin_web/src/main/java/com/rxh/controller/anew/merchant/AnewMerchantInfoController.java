package com.rxh.controller.anew.merchant;

import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.table.merchant.MerchantInfoTable;
import com.rxh.service.*;
import com.rxh.service.agent.AnewAgentMerchantService;
import com.rxh.service.merchant.AnewMerchantInfoService;
import com.rxh.service.system.NewSystemConstantService;
import com.rxh.spring.annotation.SystemLogInfo;
import com.internal.playment.common.enums.SystemConstant;
import com.internal.playment.common.page.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/merchantInfo")
public class AnewMerchantInfoController {


    @Autowired
    private NewSystemConstantService constantService;
    @Autowired
    private AnewMerchantInfoService anewMerchantInfoService;
    @Autowired
    private AnewAgentMerchantService anewAgentMerchantService;
    @Autowired
    private AnewChannelService anewChannelService;
    @Autowired
    private OrganizationInfoService organizationInfoService;

    @SystemLogInfo(description = "商户查询")
    @ResponseBody
    @RequestMapping("/getOne")
    public ResponseVO getOne(MerchantInfoTable merchantInfo){
        try {
            return anewMerchantInfoService.getMerchants(merchantInfo);
        }catch (Exception e){
            e.printStackTrace();
            ResponseVO responseVO = new ResponseVO();
            responseVO.setCode(StatusEnum._1.getStatus());
            responseVO.setMessage("失败");
            return responseVO;
        }
    }

    @SystemLogInfo(description = "商户新增")
    @RequestMapping("/addMerchantInfo")
    public ResponseVO insert(@RequestBody  MerchantInfoTable record){
        try {
            record.setCreateTime(new Date());
            record.setUpdateTime(new Date());
            record.setMerchantId("M"+System.currentTimeMillis());
            return anewMerchantInfoService.saveOrUpdate(record);
        }catch (Exception e){
            e.printStackTrace();
            ResponseVO responseVO = new ResponseVO();
            responseVO.setCode(StatusEnum._1.getStatus());
            responseVO.setMessage("失败");
            return responseVO;
        }

    }
    @SystemLogInfo(description = "商户删除")
    @RequestMapping("/batchDel")
    public ResponseVO delete(@RequestBody List<String> ids){
        try {
            return anewMerchantInfoService.delByIds(ids);
        }catch (Exception e){
            e.printStackTrace();
            ResponseVO responseVO = new ResponseVO();
            responseVO.setCode(StatusEnum._1.getStatus());
            responseVO.setMessage("失败");
            return responseVO;
        }

    }

    @SystemLogInfo(description = "商户更新")
    @RequestMapping("/updateMerchantInfo")
    public ResponseVO update(@RequestBody MerchantInfoTable record){
        try {
            record.setUpdateTime(new Date());
            return anewMerchantInfoService.saveOrUpdate(record);
        }catch (Exception e){
            e.printStackTrace();
            ResponseVO responseVO = new ResponseVO();
            responseVO.setCode(StatusEnum._1.getStatus());
            responseVO.setMessage("失败");
            return responseVO;
        }

    }
    @SystemLogInfo(description = "商户查询")
    @RequestMapping("/getAllMerchantInfo")
    ResponseVO getAll(){
        try {
            return anewMerchantInfoService.getMerchants(null);
        }catch (Exception e){
            e.printStackTrace();
            ResponseVO responseVO = new ResponseVO();
            responseVO.setCode(StatusEnum._1.getStatus());
            responseVO.setMessage("失败");
            return responseVO;
        }

    }

    @SystemLogInfo(description = "商户查询")
    @RequestMapping("/getMerchantInfoListByMerchantInfo")
    ResponseVO search(@RequestBody MerchantInfoTable merchantInfo){
        try {
            return anewMerchantInfoService.getMerchants(merchantInfo);
        }catch (Exception e){
            e.printStackTrace();
            ResponseVO responseVO = new ResponseVO();
            responseVO.setCode(StatusEnum._1.getStatus());
            responseVO.setMessage("失败");
            return responseVO;
        }

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
        ResponseVO responseVO = new ResponseVO();
        responseVO.setCode(0);
        responseVO.setData(val);
        return responseVO;
    }

    @RequestMapping("/init")
    public Map<String, Object> init() {
        Map<String, Object> init = new HashMap<>();
        init.put("merchantType", constantService.getConstantByGroupName(SystemConstant.MERCHANTTYPE).getData());
        init.put("identityType", constantService.getConstantByGroupName(SystemConstant.IDENTITYTYPE).getData());
        init.put("payType", constantService.getConstantByGroupName(SystemConstant.PAYTYPE).getData());
        init.put("status", constantService.getConstantByGroupName(SystemConstant.availableStatus).getData());
        init.put("merchants", anewMerchantInfoService.getMerchants(null).getData());
        init.put("agentMerchants", anewAgentMerchantService.list(null).getData());
        init.put("organizations",organizationInfoService.getAll(null).getData());
        init.put("channels", anewChannelService.getAll(null).getData());
        return init;
    }

}
