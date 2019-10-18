package com.rxh.activeMQ;

import com.alibaba.fastjson.JSONObject;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.service.haiyi.HaiYiPayService;
import com.rxh.service.kuaijie.MerchantSquareNotifyService;
import com.rxh.service.square.*;
import com.rxh.square.pojo.*;
import com.rxh.utils.UUID;
import com.rxh.utils.*;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

@Component
public class TransOrderMQService {

    private Logger logger = LoggerFactory.getLogger(TransOrderMQService.class);

    public final static String HAIYI_NOTIFY = "haiyiNotify";

    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private ChannelInfoService channelInfoService;
    @Autowired
    private SystemOrderTrackService systemOrderTrackService;
    @Autowired
    private MerchantInfoService merchantInfoService;
    @Autowired
    private HaiYiPayService haiYiPayService;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private MerchantSquareNotifyService merchantSquareNotifyService;
    @Autowired
    private NotifyOrderService notifyOrderService;
    @Autowired
    private TransOrderMQ transOrderMQ;

    long[] s = new long[]{0,0,60L,300L,600L,6000L};

    public void doTransOrderFirst(TransOrder transOrder){
        //获取系统所有支付机构
        List<OrganizationInfo> organizationInfoList = organizationService.selectAll();
        if (CollectionUtils.isEmpty(organizationInfoList)) {
            logger.info("获取系统支付机构返回空");
            return;
        }
        //筛选出海懿支付机构
        Optional<OrganizationInfo> organizationInfoOptional = organizationInfoList.stream()
                .filter(organization -> organization.getOrganizationName().contains("海懿"))
                .findFirst();
        OrganizationInfo organizationInfo = organizationInfoOptional.get();
        ChannelInfo channelInfo = new ChannelInfo();
        channelInfo.setOrganizationId(organizationInfo.getOrganizationId());
        List<ChannelInfo> channelInfoList = channelInfoService.selectByExample(channelInfo);
        //筛选出海懿通道
        Optional<ChannelInfo> channelInfoOptional = channelInfoList.stream()
                .filter(channel -> channel.getType() == 4).findFirst();
        ChannelInfo channelInfo1 = channelInfoOptional.get();
        HaiYiPayService.pool.execute(
            ()->{
                MerchantInfo merchantInfo = merchantInfoService.selectByMerId(transOrder.getMerId());
                SquareTrade squareTrade = new SquareTrade();
                squareTrade.setChannelInfo(channelInfo1);
                squareTrade.setTransOrder(transOrder);
                String result = HttpClientUtils.doPostJson(HttpClientUtils.getHttpClient(), JSONObject.parseObject(channelInfo1.getOthers()).getString("haiyiQueryUrl"), JsonUtils.objectToJsonNonNull(squareTrade));
                logger.info("*****************（【海懿代付查询】 查询返回结果：[{}]）*****************", result);
                BankResult bankResult = JSONObject.parseObject(result, BankResult.class);
                if (bankResult == null) {
                    logger.warn("*****************（【海懿代付查询】 返回结果bankResult转换失败）*****************");
                    //将查询失败的订单重新放入队列，继续查询
                    transOrderMQ.sendObjectMessageToTransOderFirst(transOrder);
                    return;
                }
                if (bankResult.getStatus() == 1 || bankResult.getStatus() == 0) {
                    transOrder.setOrderStatus(Integer.valueOf(bankResult.getStatus()));
                    transOrder.setTradeResult(bankResult.getParam());
                    //将查询成功的订单放入队列
                    transOrderMQ.sendObjectMessageToTransOderTwo(transOrder);
                    //获取回调地址
                    SystemOrderTrack systemOrderTrack = systemOrderTrackService.findOneByMerOrderId(transOrder.getTransId());
                    if (null != systemOrderTrack) {
                        Map map = new TreeMap();
                        map.put("merId", transOrder.getMerId());
                        map.put("merOrderId", transOrder.getMerOrderId());
                        map.put("orderId", transOrder.getTransId());
                        map.put("amount", transOrder.getAmount().setScale(2));
                        map.put("status", bankResult.getStatus());
                        map.put("msg", bankResult.getBankResult());
                        String signMsg = haiYiPayService.getMd5SignWithKey(map, merchantInfo == null ? "" : merchantInfo.getSecretKey());
                        map.put("signMsg", signMsg);
                        NotifyOrder notifyOrder = new NotifyOrder();
                        notifyOrder.setNotifyId(UUID.createId());
                        notifyOrder.setMerId(transOrder.getMerId());
                        notifyOrder.setMerOrderId(transOrder.getMerOrderId());
                        notifyOrder.setNotifyNum(1);
                        notifyOrder.setNotifyUrl(systemOrderTrack.getNoticeUrl());
                        notifyOrder.setNotifyResult(JSONObject.toJSONString(map));
                        notifyOrder.setNotifyStatus(String.valueOf(SystemConstant.BANK_STATUS_FAIL));
                        notifyOrder.setNotifyTime(new Date());
                        notifyOrder.setOriginalOrderId(transOrder.getTransId());
                        notifyOrder.setRemark(null);
                        InetAddress address = null;
                        try {
                            address = InetAddress.getLocalHost();
                        } catch (UnknownHostException e) {
                            e.getMessage();
                        }
                        String ip = address.getHostAddress();
                        notifyOrder.setIp(ip);
                        //将需要异步通知的订单放入队列
                        jmsTemplate.send(HAIYI_NOTIFY, new MessageCreator() {
                            @Override
                            public Message createMessage(Session session) throws JMSException {
                                ObjectMessage  objectMessage = session.createObjectMessage(notifyOrder);
                                return objectMessage;
                            }
                        });
                    }
                }else{
                    transOrder.setMQTimes(transOrder.getMQTimes()+1);
                    if (transOrder.getMQTimes() < 3){
                        transOrderMQ.reSendObjectMessageToTransOderFirst(transOrder,60L);
                    }
                }
            }
        );
    }

    public void doSendHaiyiNotify(NotifyOrder notifyOrder){
        HaiYiPayService.pool.execute(
            ()->{
                logger.info("通知商户参数：" + notifyOrder.getNotifyResult());
                String result = HttpClientUtils.doPostJson(HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build(), notifyOrder.getNotifyUrl(),notifyOrder.getNotifyResult());
                // 通知返回结果为空或不含有“success”字符则重新通知
                if (StringUtils.isBlank(result) || !result.contains("success")) {
                    logger.warn("通知商户失败！" + "剩余通知商户次数：" + (6-notifyOrder.getNotifyNum()));
                    if (StringUtils.isNotBlank(result) && result.length()>255){
                        result = result.substring(0,255);
                    }
                    //通知结果入库
                    if (notifyOrder.getNotifyNum() == 1){
                        notifyOrder.setRemark(result);
                        notifyOrder.setNotifyTime(new Date());
                        notifyOrder.setNotifyStatus(String.valueOf(SystemConstant.BANK_STATUS_FAIL));
                        notifyOrderService.insert(notifyOrder);
                    }else {
                        notifyOrder.setRemark(result);
                        notifyOrder.setNotifyTime(new Date());
                        notifyOrder.setNotifyStatus(String.valueOf(SystemConstant.BANK_STATUS_FAIL));
                        notifyOrderService.update(notifyOrder);
                    }
                    // 判断是否超过最大通知次数
                    notifyOrder.setNotifyNum(notifyOrder.getNotifyNum() +1);
                    if (notifyOrder.getNotifyNum() < 6) {
                        merchantSquareNotifyService.reSendHaiyiNotify(notifyOrder,s[notifyOrder.getNotifyNum()]);
                    }
                }else{
                    //通知结果入库
                    if (notifyOrder.getNotifyNum() == 1){
                        notifyOrder.setRemark(result);
                        notifyOrder.setNotifyTime(new Date());
                        notifyOrder.setNotifyStatus(String.valueOf(SystemConstant.BANK_STATUS_SUCCESS));
                        notifyOrderService.insert(notifyOrder);
                    }else {
                        notifyOrder.setRemark(result);
                        notifyOrder.setNotifyTime(new Date());
                        notifyOrder.setNotifyStatus(String.valueOf(SystemConstant.BANK_STATUS_SUCCESS));
                        notifyOrderService.update(notifyOrder);
                    }
                }
            }
        );
    }
}
