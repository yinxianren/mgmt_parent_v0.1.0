package com.rxh.controller.anew.merchant;

import com.rxh.cache.RedisCacheCommonCompoment;
import com.rxh.pojo.Result;
import com.rxh.service.ConstantService;
import com.rxh.service.square.*;
import com.rxh.spring.annotation.SystemLogInfo;
import com.rxh.square.pojo.MerchantInfo;
import com.rxh.util.UserInfoUtils;
import com.rxh.utils.SystemConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/merchantInfo")
public class AnewMerchantInfoController {


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
    @Autowired
    private RedisCacheCommonCompoment redisCacheCommonCompoment;

    @SystemLogInfo(description = "商户查询")
    @ResponseBody
    @RequestMapping("/getOne")
    public MerchantInfo getOne(MerchantInfo merchantInfo){
        //modify by gjm 添加redis  at 20190808 start
        MerchantInfo merchantInf = redisCacheCommonCompoment.merchantInfoCache.getOne(merchantInfo.getMerId());
        if(null == merchantInf){
            merchantInf = merchantInfoService.getMerchantById(merchantInfo.getMerId());
        }
        //modify by gjm 添加redis  at 20190808 end
        return merchantInf;
    }



    @SystemLogInfo(description = "商户新增")
    @RequestMapping("/addMerchantInfo")
    public Result insert(@RequestBody  MerchantInfo record){
        String passWord = record.getPassword();
        String encode = passwordEncoder.encode(passWord);
        record.setPassword(encode);
        return merchantInfoService.insert(record, UserInfoUtils.getName());
    }
    @SystemLogInfo(description = "商户删除")
    @RequestMapping("/batchDel")
    public Result delete(@RequestBody List<String> ids){
        String[] strArr=new String[ids.size()];
        for(int i=0;i<ids.size();i++){
            strArr[i]=ids.get(i);
        }
        return merchantInfoService.deleteByPrimaryKey(strArr);
    }
    @SystemLogInfo(description = "商户更新")
    @RequestMapping("/updateMerchantInfo")
    public Result update(@RequestBody MerchantInfo record){
        return merchantInfoService.update(record,UserInfoUtils.getName());
    }
    @SystemLogInfo(description = "商户查询")
    @RequestMapping("/getAllMerchantInfo")
    Result getAll(){
        //modify by gjm 添加redis  at 20190808 start
        Result<List> result = new Result<>();
        List<MerchantInfo> merchantInfoList = redisCacheCommonCompoment.merchantInfoCache.getAll();
        if(null == merchantInfoList || merchantInfoList.isEmpty()){
            result = merchantInfoService.getAll();
        }else{
            result.setCode(Result.SUCCESS);
            result.setMsg("查询成功");
            result.setData(merchantInfoList);
        }
        //modify by gjm 添加redis  at 20190808 end
        return result;
    }
    @SystemLogInfo(description = "商户查询")
    @RequestMapping("/getMerchantInfoListByMerchantInfo")
    Result search(@RequestBody MerchantInfo merchantInfo){
        //modify by gjm 添加redis  at 20190808 start
        Result<List> result = new Result<>();
        List<MerchantInfo> merchantInfoList = null;
        if(null == merchantInfoList || merchantInfoList.isEmpty()){
            result = merchantInfoService.search(merchantInfo);
        }else{
            result.setCode(Result.SUCCESS);
            result.setMsg("查询成功");
            result.setData(merchantInfoList);
        }
        //modify by gjm 添加redis  at 20190808 end
        return result;
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
