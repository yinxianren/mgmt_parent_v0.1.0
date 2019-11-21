package com.rxh.controller;

import com.rxh.pojo.Result;
import com.rxh.service.ConstantService;
import com.rxh.service.square.*;
import com.rxh.spring.annotation.SystemLogInfo;
import com.rxh.square.pojo.MerchantInfo;
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
    MerchantInfoService merchantInfoService;
    @Autowired
    ConstantService constantService;
    @Autowired
    AgentMerchantInfoService agentMerchantInfoService;
    @Resource
    private ChannelWalletService channelWalletService;
    @Resource
    private OrganizationService organizationService;
    @Resource
    private MerchantWalletService merchantWalletService;
    @Resource
    private   BCryptPasswordEncoder passwordEncoder;

    @ResponseBody
    @RequestMapping("/getOne")
    public MerchantInfo getOne(MerchantInfo merchantInfo){
        return merchantInfoService.getMerchantById(merchantInfo.getMerId());
    }

    @SystemLogInfo(description = "商户基本信息获取")
    @ResponseBody
    @RequestMapping("/getMerchantInfo")
    public List<MerchantInfo> getMerchantInfo(){
        String merchantId = UserInfoUtils.getMerchantId();
        List<MerchantInfo> merchantInfos = new ArrayList<>();
        merchantInfos.add(merchantInfoService.getMerchantById(merchantId));
        return merchantInfos;
    }




    @RequestMapping("/addMerchantInfo")
    public Result insert(@RequestBody  MerchantInfo record){
        String passWord = record.getPassword();
        String encode = passwordEncoder.encode(passWord);
        record.setPassword(encode);
        return merchantInfoService.insert(record, UserInfoUtils.getUserName());
    }
    @RequestMapping("/batchDel")
    public Result delete(@RequestBody List<String> ids){
        String[] strArr=new String[ids.size()];
        for(int i=0;i<ids.size();i++){
            strArr[i]=ids.get(i);
        }
        return merchantInfoService.deleteByPrimaryKey(strArr);
    }

    @RequestMapping("/updateMerchantInfo")
    public Result update(@RequestBody MerchantInfo record){
        return merchantInfoService.update(record,UserInfoUtils.getUserName());
    }

    @RequestMapping("/getAllMerchantInfo")
    Result getAll(){

        return merchantInfoService.getAll();
    }

    @RequestMapping("/getMerchantInfoListByMerchantInfo")
    Result search(@RequestBody MerchantInfo merchantInfo){
        return merchantInfoService.search(merchantInfo);
    }

    @RequestMapping("/getRandomSecretkey")
    @ResponseBody
    public Result getRandomSecretkey(){
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
        return new Result(Result.SUCCESS,val);
    }

    @RequestMapping("/init")
    public Map<String, Object> init() {
        Map<String, Object> init = new HashMap<>();
        init.put("merchantType", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.MERCHANTTYPE));
        init.put("identityType", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.IDENTITYTYPE));
        init.put("payType", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.PAYTYPE));
        init.put("status", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.availableStatus));
        init.put("merchants", merchantInfoService.getIdsAndName());
        init.put("agentMerchants", agentMerchantInfoService.getAllIdAndName());
        init.put("organizations",organizationService .getIdsAndName());
        init.put("channels", channelWalletService.getIdsAndName());
        return init;
    }
    /**
     * 获取id
     */
    @ResponseBody
    @RequestMapping(value = "/getMerchantIdIncre", method = RequestMethod.GET)
    public String getAgentMerchantIdIncre(){
        String merchantId = merchantWalletService.getMaxMerId();
        return merchantId;
    }


}
