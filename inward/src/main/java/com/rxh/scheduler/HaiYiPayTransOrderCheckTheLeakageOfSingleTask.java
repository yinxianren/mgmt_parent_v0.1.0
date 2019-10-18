package com.rxh.scheduler;

import com.rxh.activeMQ.TransOrderMQ;
import com.rxh.service.haiyi.HaiYiPayService;
import com.rxh.service.square.ChannelInfoService;
import com.rxh.service.square.OrganizationService;
import com.rxh.service.trading.TransOrderService;
import com.rxh.square.pojo.ChannelInfo;
import com.rxh.square.pojo.OrganizationInfo;
import com.rxh.square.pojo.TransOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 *  描述： 定时检测代付订单是否漏单，如果检测到有漏洞，则放到队列执行
 * @author  panda
 * @date 20190713
 */
//@Component
public class HaiYiPayTransOrderCheckTheLeakageOfSingleTask {

    private Logger logger = LoggerFactory.getLogger(getClass());
//    @Autowired
    private TransOrderMQ transOrderMQ;
//    @Autowired
    private TransOrderService transOrderService;
//    @Autowired
    private OrganizationService organizationService;
//    @Autowired
    private ChannelInfoService channelInfoService;


//    @Scheduled(cron = "0 0/5 * * * ?")
    public void checkTheLeakageOfSingleTask(){
//        logger.info("*************************【海懿支付定时任务---->查询漏单任务】定时执行海懿代付订单是否漏单更新！*************************");
        try{
            int count=this.run();
            logger.info("【海懿支付定时任务---->查询漏单任务】定时执行海懿代付订单是否漏单更新！已经结束！本次共查补了[{}]笔订单！！",count);
        }catch (Exception e){
            if(e instanceof NoSuchElementException || e instanceof IllegalArgumentException){
                logger.warn("【海懿支付定时任务---->查询漏单任务】 提示：{}", e.getMessage());
            }else{
                e.printStackTrace();
            }
        }
    }


    private int run(){
        // 1.获取支付机构表
        List<OrganizationInfo> organizationInfoList = organizationService.selectAll();
        Assert.notNull(organizationInfoList, "查询Organiz ationInfo结果：没有获取到有相关机构信息！");
        Optional<OrganizationInfo> organizationInfoOptional = organizationInfoList
                .stream()
                .distinct()
                .filter(t -> t.getOrganizationName().contains("海懿"))
                .findFirst();
        OrganizationInfo organizationInfo  = organizationInfoOptional.get();
        // 2.获取通道信息
        ChannelInfo channelInfo = new ChannelInfo();
        channelInfo.setOrganizationId(organizationInfo.getOrganizationId());
        List<ChannelInfo> channelInfoList = channelInfoService.selectByExample(channelInfo);
        Assert.notNull(channelInfoList,"获取海懿支付通道相关信息结果：没有获取到有关海懿支付相通道构信息！");
        Optional<ChannelInfo>  channelInfoOptional= channelInfoList
                .stream()
                .distinct()
                .filter(t->t.getType() == 4 )  //类型为4表示代付
                .findFirst();
        channelInfo=channelInfoOptional.get();
        //3.查询订单
        Instant currentTime = Instant.now();  //当前的时间
        Instant subtractAfterTime = currentTime.plusSeconds(-6*60);     //6分钟之前的订单
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<TransOrder> transOrderList=transOrderService.getTransOrder(channelInfo.getChannelId(),3,formatter.format( Date.from(subtractAfterTime)),formatter.format(Date.from(currentTime)));
        Assert.notNull(transOrderList,"没有获取海域代付订单，结束本次定时任务！");
        //4.向队列发送任务
        transOrderList.stream()
                .distinct()
                .forEach(t-> HaiYiPayService.pool.execute(()->transOrderMQ.sendObjectMessageToTransOderFirst(t)));
        return transOrderList.size();
    }




}
