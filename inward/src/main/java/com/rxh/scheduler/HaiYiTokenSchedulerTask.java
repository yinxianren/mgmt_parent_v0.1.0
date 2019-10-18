package com.rxh.scheduler;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rxh.pojo.cross.BankResult;
import com.rxh.service.square.ChannelInfoService;
import com.rxh.service.square.OrganizationService;
import com.rxh.square.pojo.ChannelInfo;
import com.rxh.square.pojo.OrganizationInfo;
import com.rxh.utils.HttpClientUtils;
import com.rxh.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 *  描述： 明天定时获取token
 * @author monkey
 * @date 2019/7/9
 */
//@Component
public class HaiYiTokenSchedulerTask {


    private Logger logger = LoggerFactory.getLogger(HaiYiTokenSchedulerTask.class);

//    @Autowired
    private OrganizationService organizationService;

//    @Autowired
    private ChannelInfoService channelInfoService;

    private  String token="";

    //每天一点半执行
//    @Scheduled(cron = "0 30 1 * * ?")
    public void execute(){
        int iniTime=5;
        for(int i=1;i<6;i++) {
            if (!run()){
                try {
                    TimeUnit.MINUTES.sleep(iniTime * i);
                } catch (InterruptedException e) {
                    logger.info("【海懿支付定时任务---->发起获取Token任务】execute() 执行execute() 异常捕获，本次休眠时间：{}分钟，被捕获次数：{} 次",iniTime, i);
                    continue;
                }
            }else{
                return;
            }
        }
    }

    /**
     *  手动获取token
     * @return
     */
    public String  handOperationGetToken(){
        if(run()){
            return getToken();
        }
        return "获取失败请重新获取！";
    }


    private boolean run(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        logger.info("【海懿支付定时任务---->发起获取Token任务】时间：{}",formatter.format(new Date()));
        try {
            // 1.获取支付机构表
            List<OrganizationInfo> organizationInfoList = organizationService.selectAll();
            if (null == organizationInfoList || organizationInfoList.size() == 0) {
                logger.info("【海懿支付定时任务---->发起获取Token任务】查询OrganizationInfo结果：没有获取到有相关机构信息！");
                return false;
            }
            Optional<OrganizationInfo> organizationInfoOptional =organizationInfoList
                    .stream()
                    .distinct()
                    .filter(t -> t.getOrganizationName().contains("海懿"))
                    .findFirst();
            OrganizationInfo organizationInfo=null;
            try{
                organizationInfo = organizationInfoOptional.get();
            }catch ( Exception e){
                logger.info("【海懿支付定时任务---->发起获取Token任务】过滤海懿支付相关信息结果：没有获取到有关海懿支付相关机构信息！");
                return false;
            }
            // 2.获取通道信息
            ChannelInfo channelInfo = new ChannelInfo();
            channelInfo.setOrganizationId(organizationInfo.getOrganizationId());
//        channelInfo.setType(4);//4 代表代付支付
            List<ChannelInfo> channelInfoList = channelInfoService.selectByExample(channelInfo);
            if (null == channelInfoList || channelInfoList.size() == 0) {
                logger.info("【海懿支付定时任务---->发起获取Token任务】 获取海懿支付通道相关信息结果：没有获取到有关海懿支付相通道构信息！");
                return false;
            }
            Optional<ChannelInfo>  channelInfoOptional= channelInfoList
                    .stream()
                    .distinct()
                    .filter(t->t.getType() == 4 )  //类型为4表示代付
                    .findFirst();
            try{
                channelInfo=channelInfoOptional.get();
            }catch (Exception e){
                logger.info("【海懿支付定时任务---->发起获取Token任务】 获取海懿支付通道相关信息结果：没有获取到有关海懿代付配置信息！");
                return false;
            }
            //3.获取海懿通道配置信息
            String others=channelInfo.getOthers();
            JSONObject othersJsonObject = JSON.parseObject(others);
            String appId=othersJsonObject.getString("appId");// 商户appId	appId
            String appKey=othersJsonObject.getString("appKey");// 商户appKey	appKey
            String tokenPath=othersJsonObject.getString("tokenPath");  // 资源路径
            String paymentUrl=othersJsonObject.getString("paymentUrl");  // 资源路径

            //5.封装数据
            Map<String,String> map= new HashMap<>();
            map.put("appId", appId);
            map.put("appKey", appKey);
            map.put("paymentUrl", paymentUrl);
            logger.info("【海懿支付定时任务---->发起获取Token任务】 请求cross工程请求参数：{}",JsonUtils.objectToJson(map));
            String result = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), tokenPath,JsonUtils.objectToJson(map));
            logger.info("【海懿支付定时任务---->发起获取Token任务】 请求cross工程返回结果：{}",result);
            BankResult bankResult = JsonUtils.jsonToPojo(result, BankResult.class);
            if(null == bankResult){
                logger.info("【海懿支付定时任务---->发起获取Token任务】 请求cross工程返回结果转换 <BankResult> 对象为空！");
                return false;
            }

            int status= bankResult.getStatus();
            if( status  != 0){//0 表示成功
                logger.info("【海懿支付定时任务---->发起获取Token任务】请求cross工程返回结果 <status> 不为成功！ ");
                return false;
            }
            String bankData=bankResult.getBankData();
            if(StringUtils.isEmpty(bankData)){
                logger.info("【海懿支付定时任务---->发起获取Token任务】请求cross工程返回结果 <token> 为空！ ");
                return false;
            }
            //更新token
            setToken(bankData);
            othersJsonObject.put("token",bankData);
            //将others重新转为String 进行保存
            others=JsonUtils.objectToJson(othersJsonObject);
            int how= channelInfoService.updateOthersInfo(others,channelInfo.getChannelId());
            if(how !=1 ){
                logger.info("【海懿支付定时任务---->发起获取Token任务】更新channelInfo数据库中token信息不正常！");
                return false;
            }
            return true;
        }catch (Exception e){
            logger.info("【海懿支付定时任务---->发起获取Token任务】 在执行任务过程中发生异常！");
            e.printStackTrace();
            return false;
        }
    }



    private String getToken() {
        return token;
    }

    private void setToken(String token) {
        this.token = token;
    }
}
