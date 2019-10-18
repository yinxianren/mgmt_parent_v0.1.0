package com.rxh.service.kuaijie;

import com.alibaba.fastjson.JSONObject;
import com.rxh.activeMQ.TransOrderMQ;
import com.rxh.exception.PayException;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.payment.ParamRule;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.pojo.payment.TradeObjectSquare;
import com.rxh.service.AbstractCommonService;
import com.rxh.service.square.MerchantInfoService;
import com.rxh.service.square.SystemOrderTrackService;
import com.rxh.service.trading.PayOrderService;
import com.rxh.service.trading.TransOrderService;
import com.rxh.square.pojo.*;
import com.rxh.utils.*;
import com.rxh.utils.UUID;
import com.rxh.vo.OrderObjectToMQ;
import com.rxh.vo.QueryOrderObjectToMQ;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

/**
 * 订单主动查询队列处理
 * @author monkey
 * @date 2019/7/27
 */
@Service
public class KuaijiePayQueryService extends AbstractCommonService {

    private final Logger logger = LoggerFactory.getLogger(KuaijiePayQueryService.class);

    @Autowired
    private PayOrderService payOrderService;
    @Resource
    private KuaiJiePayPaymentPayService kuaiJiePayPaymentPayService;
    @Autowired
    private MerchantInfoService merchantInfoService;
    @Autowired
    private TransOrderMQ transOrderMQ;
    @Autowired
    private TransOrderService transOrderService;
    @Autowired
    private SystemOrderTrackService systemOrderTrackService;
    @Autowired
    private JmsTemplate jmsTemplate;

    private Long[] times = new Long[]{0L,30L,60L,300L,6000L,6000L};
    private final String HAIYI_NOTIFY = "haiyiNotify";

    /**
     * 收单主动查询处理
     * @param queryOrderObjectToMQ
     */
   public void kuaijiePayQueryMq(QueryOrderObjectToMQ queryOrderObjectToMQ){
       try {
           PayOrder mqPayOrder = queryOrderObjectToMQ.getPayOrder();
           PayOrder order = payOrderService.selectByPayId(mqPayOrder.getPayId());
           if (order == null){
               logger.error("【快捷收单业务】商户号[{}]: 收单主动查询没有找到相应订单[{}]",mqPayOrder.getMerId(),mqPayOrder.getPayId());
               return;
           }
           if (order.getOrderStatus() == 20){
               //状态=20，表示钱包还在处理中，重新放入队列，最多重发5次
               if (queryOrderObjectToMQ.getTimes()<5){
                   queryOrderObjectToMQ.setTimes(queryOrderObjectToMQ.getTimes()+1);
                   transOrderMQ.sendObjectMessageToQueryPayOderMQ(queryOrderObjectToMQ,times[queryOrderObjectToMQ.getTimes()]);
               }
               return;
           }
           ChannelInfo channelInfo = queryOrderObjectToMQ.getChannelInfo();
           SquareTrade trade = new SquareTrade();
           trade.setChannelInfo(channelInfo);
           trade.setPayOrder(order);
           trade.setMerchantRegisterCollect(queryOrderObjectToMQ.getMerchantRegisterCollect());
           TradeObjectSquare tradeObjectSquare = new TradeObjectSquare();
           tradeObjectSquare.setInnerType("P004");
           trade.setTradeObjectSquare(tradeObjectSquare);
           String result;
           String URL = JSONObject.parseObject(channelInfo.getOthers()).getString("queryOrderUrl");
           try{
               //发起上游查询
               result = HttpClientUtils.doPostJson(HttpClientUtils.getHttpClient(),URL, JsonUtils.objectToJsonNonNull(trade));
           }catch ( Exception exception){
               logger.info("【快捷收单业务】 商户号[{}]:收单主动查询请求过程中抛出异常！",trade.getPayOrder().getMerId());
               throw exception;
           }
           logger.info("【快捷收单业务】商户号[{}]: 收单主动查询请求结果：{}",trade.getPayOrder().getMerId(),result);
           if (StringUtils.isBlank(result))  throw  new NullPointerException(String.format("商户号[%s]:响应结果为空",trade.getPayOrder().getMerId()));
           BankResult bankResult = JsonUtils.jsonToPojo(result, BankResult.class);
           Assert.notNull(bankResult,String.format("商户号[%s]:收单主动查询请求结果转换BankResult异常",trade.getPayOrder().getMerId()));
           boolean flag = true;
           MerchantInfo merchantInfo = merchantInfoService.selectByMerId(order.getMerId());
           String oldId = order.getPayId();
           if (!((int)bankResult.getStatus()==order.getOrderStatus())){
               //订单状态不一致，以查询为准
//               if (order.getOrderStatus() == SystemConstant.BANK_STATUS_PENDING_PAYMENT){
//                   flag = false;
//               }
               if (bankResult.getStatus() == SystemConstant.BANK_STATUS_FAIL || bankResult.getStatus() == SystemConstant.BANK_STATUS_SUCCESS){
//                   PayOrder payOrder = new PayOrder();
                   if (bankResult.getStatus() == SystemConstant.BANK_STATUS_SUCCESS){
                       //成功订单
                       SquareTrade squareTrade = new SquareTrade();
//                       payOrder = order;
                       order.setAmount(order.getAmount());
                       order.setOrderStatus(20);
//                       payOrder.setPayType("5");
                       order.setBankTime(bankResult.getBankTime());
                       order.setTradeTime(new Date());
                       TradeObjectSquare objectSquare = new TradeObjectSquare();
                       squareTrade.setPayOrder(order);
                       objectSquare.setPayFee(queryOrderObjectToMQ.getPayFee());
                       squareTrade.setTradeObjectSquare(objectSquare);
                       squareTrade.setChannelInfo(channelInfo);
                       squareTrade.setMerchantInfo(merchantInfo);
                       OrderObjectToMQ orderObjectToMQ = kuaiJiePayPaymentPayService.getOrderObjectToMQ(squareTrade);
                       transOrderMQ.sendObjectMessageToTransOderMQ(orderObjectToMQ);
                   }else if(order.getOrderStatus() == SystemConstant.BANK_STATUS_SUCCESS){
                       //失败订单，冲正
                       SquareTrade squareTrade = new SquareTrade();
                       BigDecimal bigDecimal = new BigDecimal(0);
//                       payOrder = order;
//                       payOrder.setAmount(bigDecimal.subtract(order.getAmount()));
                       order.setOrderStatus(20);
                       order.setPayType("6");
                       order.setTradeTime(new Date());
                       order.setBankTime(bankResult.getBankTime());
                       TradeObjectSquare objectSquare = new TradeObjectSquare();
                       squareTrade.setPayOrder(order);
                       objectSquare.setPayFee(queryOrderObjectToMQ.getPayFee());
                       squareTrade.setTradeObjectSquare(objectSquare);
                       squareTrade.setChannelInfo(channelInfo);
                       squareTrade.setMerchantInfo(merchantInfo);
                       OrderObjectToMQ orderObjectToMQ = kuaiJiePayPaymentPayService.getOrderObjectToMQ(squareTrade);
                       transOrderMQ.sendObjectMessageToTransOderMQ(orderObjectToMQ);
                   }

                   if (flag){
                       payOrderService.updateByPrimaryKey(order);
                   }else {
                       order.setPayId(String.valueOf(UUID.createKey("pay_order")));
                       payOrderService.insertBean(order);
                   }

                   //获取回调地址
                   Map parmap = new HashMap();
                   parmap.put("orderId",oldId);
                   parmap.put("startPage",0);
                   parmap.put("pageSize",1);
                   List<SystemOrderTrack> systemOrderTracks = systemOrderTrackService.selectAllSystemOrder(parmap);
                   if (null != systemOrderTracks) {
                       SystemOrderTrack systemOrderTrack = systemOrderTracks.get(0);
                       Map map = new TreeMap();
                       map.put("merId", order.getMerId());
                       map.put("merOrderId", order.getMerOrderId());
                       map.put("orderId", order.getPayId());
                       map.put("amount", order.getAmount().setScale(2));
                       map.put("status", bankResult.getStatus());
                       map.put("msg", bankResult.getBankResult());
                       map.put("terminalMerId",order.getTerminalMerId());
                       String signMsg = CheckMd5Utils.getMd5SignWithKey(map, merchantInfo == null ? "" : merchantInfo.getSecretKey());
                       map.put("signMsg", signMsg);
                       NotifyOrder notifyOrder = new NotifyOrder();
                       notifyOrder.setNotifyId(String.valueOf(UUID.createKey("notify_order")));
                       notifyOrder.setMerId(order.getMerId());
                       notifyOrder.setMerOrderId(order.getMerOrderId());
                       notifyOrder.setNotifyNum(1);
                       notifyOrder.setNotifyUrl(systemOrderTrack.getNoticeUrl());
                       notifyOrder.setNotifyResult(JSONObject.toJSONString(map));
                       notifyOrder.setNotifyStatus(String.valueOf(SystemConstant.BANK_STATUS_FAIL));
                       notifyOrder.setNotifyTime(new Date());
                       notifyOrder.setOriginalOrderId(order.getPayId());
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
                               ObjectMessage objectMessage = session.createObjectMessage(notifyOrder);
                               return objectMessage;
                           }
                       });
                   }
                   return;
               }else {
                   //查询状态不是成功或失败，重新查询
                   if (queryOrderObjectToMQ.getTimes()<5){
                       queryOrderObjectToMQ.setTimes(queryOrderObjectToMQ.getTimes()+1);
                       transOrderMQ.sendObjectMessageToQueryPayOderMQ(queryOrderObjectToMQ,times[queryOrderObjectToMQ.getTimes()]);
                   }
                   return;
               }
           }else if (bankResult.getStatus() == SystemConstant.BANK_STATUS_SUCCESS || bankResult.getStatus() == SystemConstant.BANK_STATUS_FAIL){
               //获取回调地址
               Map parmap = new HashMap();
               parmap.put("orderId",oldId);
               parmap.put("startPage",0);
               parmap.put("pageSize",1);
               List<SystemOrderTrack> systemOrderTracks = systemOrderTrackService.selectAllSystemOrder(parmap);
               if (null != systemOrderTracks) {
                   SystemOrderTrack systemOrderTrack = systemOrderTracks.get(0);
                   Map map = new TreeMap();
                   map.put("merId", order.getMerId());
                   map.put("merOrderId", order.getMerOrderId());
                   map.put("orderId", order.getPayId());
                   map.put("amount", order.getAmount().setScale(2));
                   map.put("status", bankResult.getStatus());
                   map.put("msg", bankResult.getBankResult());
                   map.put("terminalMerId",order.getTerminalMerId());
                   String signMsg = CheckMd5Utils.getMd5SignWithKey(map, merchantInfo == null ? "" : merchantInfo.getSecretKey());
                   map.put("signMsg", signMsg);
                   NotifyOrder notifyOrder = new NotifyOrder();
                   notifyOrder.setNotifyId(String.valueOf(UUID.createKey("notify_order")));
                   notifyOrder.setMerId(order.getMerId());
                   notifyOrder.setMerOrderId(order.getMerOrderId());
                   notifyOrder.setNotifyNum(1);
                   notifyOrder.setNotifyUrl(systemOrderTrack.getNoticeUrl());
                   notifyOrder.setNotifyResult(JSONObject.toJSONString(map));
                   notifyOrder.setNotifyStatus(String.valueOf(bankResult.getStatus()));
                   notifyOrder.setNotifyTime(new Date());
                   notifyOrder.setOriginalOrderId(order.getPayId());
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
                           ObjectMessage objectMessage = session.createObjectMessage(notifyOrder);
                           return objectMessage;
                       }
                   });
               }
               return;
           }
       }catch (Exception e){
           logger.warn("【快捷收单业务】 收单主动查询请求过程中抛出异常！");
           e.printStackTrace();
       }
   }


    /**
     * 代付订单查询
     * @param queryOrderObjectToMQ
     */
    public void kuaijieTransQueryMq(QueryOrderObjectToMQ queryOrderObjectToMQ){
        try {
            TransOrder mqTransOrder = queryOrderObjectToMQ.getTransOrder();
            TransOrder order = transOrderService.getTransOrderByPrimaryId(mqTransOrder.getTransId());
            PayOrder payOrder = payOrderService.seleteBymerOrderId(order.getMerId(),order.getOriginalMerOrderId(),order.getTerminalMerId());
            if (order == null){
                logger.error("【代付业务】 商户号[{}]:代付主动查询请求没有找到相应订单[{}]",mqTransOrder.getMerId(),mqTransOrder.getTransId());
                return;
            }
            if (order.getOrderStatus() == 30){
                //状态=30，表示钱包还在处理中，重新放入队列，最多重发5次
                if (queryOrderObjectToMQ.getTimes()<5){
                    queryOrderObjectToMQ.setTimes(queryOrderObjectToMQ.getTimes()+1);
                    transOrderMQ.sendObjectMessageToQueryTransOderMQ(queryOrderObjectToMQ,times[queryOrderObjectToMQ.getTimes()]);
                }
                return;
            }
            ChannelInfo channelInfo = queryOrderObjectToMQ.getChannelInfo();
            SquareTrade trade = new SquareTrade();
            trade.setChannelInfo(channelInfo);
            trade.setTransOrder(order);
            trade.setMerchantRegisterCollect(queryOrderObjectToMQ.getMerchantRegisterCollect());
            TradeObjectSquare tradeObjectSquare = new TradeObjectSquare();
            tradeObjectSquare.setInnerType("P005");
            trade.setTradeObjectSquare(tradeObjectSquare);
            String result;
            try{
                result = HttpClientUtils.doPostJson(HttpClientUtils.getHttpClient(), JSONObject.parseObject(channelInfo.getOthers()).getString("queryOrderUrl"), JsonUtils.objectToJsonNonNull(trade));
            }catch ( Exception exception){
                logger.info("【代付业务】 商户号[{}]:代付主动查询请求过程中抛出异常！",trade.getTransOrder().getMerId());
                throw exception;
            }
            logger.info("【代付业务】商户号[{}]: 代付主动查询请求结果：{}",trade.getTransOrder().getMerId(),result);
            if (StringUtils.isBlank(result))  throw  new NullPointerException(String.format("商户号[%s]:响应结果为空",trade.getPayOrder().getMerId()));
            BankResult bankResult = JsonUtils.jsonToPojo(result, BankResult.class);
            Assert.notNull(bankResult,String.format("商户号[%s]:代付主动查询请求结果转换BankResult异常",trade.getTransOrder().getMerId()));
            boolean flag = true;
            MerchantInfo merchantInfo = merchantInfoService.selectByMerId(order.getMerId());
            if (!((int)bankResult.getStatus()==order.getOrderStatus())){
                if (bankResult.getStatus() == SystemConstant.BANK_STATUS_FAIL || bankResult.getStatus() == SystemConstant.BANK_STATUS_SUCCESS) {
//                    if (order.getOrderStatus() == SystemConstant.BANK_STATUS_PENDING_PAYMENT){
//                        flag = false;
//                    }
//                    TransOrder transOrder = new TransOrder();
                    //状态不一致，以查询为准
                    if(bankResult.getStatus() == SystemConstant.BANK_STATUS_SUCCESS){
                        SquareTrade squareTrade = new SquareTrade();
//                        transOrder = order;
                        order.setAmount(order.getAmount());
                        order.setOrderStatus(30);
                        order.setPayType("4");
                        order.setTradeTime(new Date());
                        order.setBankTime(bankResult.getBankTime());
                        TradeObjectSquare objectSquare = new TradeObjectSquare();
                        squareTrade.setTransOrder(order);
                        objectSquare.setPayFee(queryOrderObjectToMQ.getPayFee());
                        squareTrade.setTradeObjectSquare(objectSquare);
                        squareTrade.setChannelInfo(channelInfo);
                        squareTrade.setMerchantInfo(merchantInfo);
                        squareTrade.setPayOrder(payOrder);
                        OrderObjectToMQ orderObjectToMQ = getOrderObjectToMQ(squareTrade);
                        transOrderMQ.sendObjectMessageToTransOderMQ(orderObjectToMQ);
                    }else if (order.getOrderStatus() == SystemConstant.BANK_STATUS_SUCCESS){
                        // 订单冲正
                        SquareTrade squareTrade = new SquareTrade();
                        BigDecimal bigDecimal = new BigDecimal(0);
//                        transOrder = order;
//                        transOrder.setAmount(bigDecimal.subtract(order.getAmount()));
                        order.setOrderStatus(30);
                        order.setPayType("6");
                        order.setTradeTime(new Date());
                        order.setBankTime(bankResult.getBankTime());
                        TradeObjectSquare objectSquare = new TradeObjectSquare();
                        squareTrade.setTransOrder(order);
                        objectSquare.setPayFee(queryOrderObjectToMQ.getPayFee());
                        squareTrade.setTradeObjectSquare(objectSquare);
                        squareTrade.setChannelInfo(channelInfo);
                        squareTrade.setMerchantInfo(merchantInfo);
                        squareTrade.setPayOrder(payOrder);
                        OrderObjectToMQ orderObjectToMQ = getOrderObjectToMQ(squareTrade);
                        transOrderMQ.sendObjectMessageToTransOderMQ(orderObjectToMQ);
                    }
                    transOrderService.updateByPrimaryKey(order);
//                    else {
//                        transOrder.setTransId(String.valueOf(UUID.createKey("trans_order")));
//                        transOrderService.insertBean(transOrder);
//                    }

                    //获取回调地址
                    Map parmap = new HashMap();
                    parmap.put("orderId",order.getTransId());
                    parmap.put("startPage",0);
                    parmap.put("pageSize",1);
                    List<SystemOrderTrack> systemOrderTracks = systemOrderTrackService.selectAllSystemOrder(parmap);
                    if (null != systemOrderTracks) {
                        SystemOrderTrack systemOrderTrack = systemOrderTracks.get(0);
                        //封装数据放入异步通知队列
                        Map map = new TreeMap();
                        map.put("merId", order.getMerId());
                        map.put("merOrderId", order.getMerOrderId());
                        map.put("orderId", order.getTransId());
                        map.put("amount", order.getAmount().setScale(2));
                        map.put("status", bankResult.getStatus());
                        map.put("msg", bankResult.getBankResult());
                        map.put("terminalMerId",order.getTerminalMerId());
                        String signMsg = CheckMd5Utils.getMd5SignWithKey(map, merchantInfo == null ? "" : merchantInfo.getSecretKey());
                        map.put("signMsg", signMsg);
                        NotifyOrder notifyOrder = new NotifyOrder();
                        notifyOrder.setNotifyId(String.valueOf(UUID.createKey("notify_order")));
                        notifyOrder.setMerId(order.getMerId());
                        notifyOrder.setMerOrderId(order.getMerOrderId());
                        notifyOrder.setNotifyNum(1);
                        notifyOrder.setNotifyUrl(systemOrderTrack.getNoticeUrl());
                        notifyOrder.setNotifyResult(JSONObject.toJSONString(map));
                        notifyOrder.setNotifyStatus(String.valueOf(SystemConstant.BANK_STATUS_FAIL));
                        notifyOrder.setNotifyTime(new Date());
                        notifyOrder.setOriginalOrderId(order.getTransId());
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
                                ObjectMessage objectMessage = session.createObjectMessage(notifyOrder);
                                return objectMessage;
                            }
                        });
                    }
                    return;
                }else {
                    //查询状态不是成功或失败，重新查询
                    if (queryOrderObjectToMQ.getTimes()<5){
                        queryOrderObjectToMQ.setTimes(queryOrderObjectToMQ.getTimes()+1);
                        transOrderMQ.sendObjectMessageToQueryTransOderMQ(queryOrderObjectToMQ,times[queryOrderObjectToMQ.getTimes()]);
                    }
                    return;
                }
            }else if (bankResult.getStatus() == SystemConstant.BANK_STATUS_SUCCESS || bankResult.getStatus() == SystemConstant.BANK_STATUS_FAIL){
                //获取回调地址
                Map parmap = new HashMap();
                parmap.put("orderId",order.getTransId());
                parmap.put("startPage",0);
                parmap.put("pageSize",1);
                List<SystemOrderTrack> systemOrderTracks = systemOrderTrackService.selectAllSystemOrder(parmap);
                if (null != systemOrderTracks) {
                    SystemOrderTrack systemOrderTrack = systemOrderTracks.get(0);
                    //封装数据放入异步通知队列
                    Map map = new TreeMap();
                    map.put("merId", order.getMerId());
                    map.put("merOrderId", order.getMerOrderId());
                    map.put("orderId", order.getTransId());
                    map.put("amount", order.getAmount().setScale(2));
                    map.put("status", bankResult.getStatus());
                    map.put("msg", bankResult.getBankResult());
                    map.put("terminalMerId",order.getTerminalMerId());
                    String signMsg = CheckMd5Utils.getMd5SignWithKey(map, merchantInfo == null ? "" : merchantInfo.getSecretKey());
                    map.put("signMsg", signMsg);
                    NotifyOrder notifyOrder = new NotifyOrder();
                    notifyOrder.setNotifyId(String.valueOf(UUID.createKey("notify_order")));
                    notifyOrder.setMerId(order.getMerId());
                    notifyOrder.setMerOrderId(order.getMerOrderId());
                    notifyOrder.setNotifyNum(1);
                    notifyOrder.setNotifyUrl(systemOrderTrack.getNoticeUrl());
                    notifyOrder.setNotifyResult(JSONObject.toJSONString(map));
                    notifyOrder.setNotifyStatus(String.valueOf(bankResult.getStatus()));
                    notifyOrder.setNotifyTime(new Date());
                    notifyOrder.setOriginalOrderId(order.getTransId());
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
                            ObjectMessage objectMessage = session.createObjectMessage(notifyOrder);
                            return objectMessage;
                        }
                    });
                }
                return;
            }
        }catch (Exception e){
            logger.warn("【代付业务】 代付主动查询请求过程中抛出异常！");
            e.printStackTrace();
        }
    }

    /**
     * 代付队列数据封装
     * @param trade
     * @return
     */
    public OrderObjectToMQ getOrderObjectToMQ(SquareTrade trade){
        TransOrder transOrder= trade.getTransOrder();
        ChannelInfo  channelInfo=trade.getChannelInfo();
        MerchantInfo merchantInfo=trade.getMerchantInfo();
        PayOrder payOrder=trade.getPayOrder();

        String merId=transOrder.getMerId();
        String merOrderId=transOrder.getMerOrderId();
        String channelId=channelInfo.getChannelId();
        String payType=transOrder.getPayType();
        BigDecimal realAmount=transOrder.getRealAmount();
        BigDecimal amount=transOrder.getAmount();
        BigDecimal terminalFee=transOrder.getTerminalFee();
        Integer channelType=channelInfo.getType();//
        String transId=transOrder.getTransId();
        String channelTransCode=channelInfo.getChannelTransCode();
        String terminalMerId=transOrder.getTerminalMerId();
        String parentId=merchantInfo.getParentId();
        String payId=payOrder.getPayId();

        return new OrderObjectToMQ()
                .lsetMerId(merId)
                .lsetMerOrderId(merOrderId)
                .lsetChannelId(channelId)
                .lsetChannelType(channelType)
                .lsetPayType(payType)
                .lsetRealAmount(realAmount)
                .lsetAmount(amount)
                .lsetTerminalFee(terminalFee)
                .lsetTransId(transId)
                .lsetChannelTransCode(channelTransCode)
                .lsetTerminalMerId(terminalMerId)
                .lsetParentId(parentId)
                .lsetOrderStatus(transOrder.getOrderStatus())
                .lsetPayId(payId)
                ;
    }

    /**
     * 收单和代付查询订单 封装队列对象
     * @param trade
     * @return
     */
    public QueryOrderObjectToMQ getQueryOrderObjectToMQ(SquareTrade trade){
        QueryOrderObjectToMQ queryOrderObjectToMQ = new QueryOrderObjectToMQ();
        queryOrderObjectToMQ.setChannelInfo(trade.getChannelInfo());
        MerchantRegisterCollect merchantRegisterCollect = new MerchantRegisterCollect();
        if(trade.getMerchantRegisterCollect() != null){
            merchantRegisterCollect.setBackData(trade.getMerchantRegisterCollect().getBackData());
        }
        queryOrderObjectToMQ.setPayFee(trade.getTradeObjectSquare().getPayFee());
        queryOrderObjectToMQ.setMerchantRegisterCollect(merchantRegisterCollect);
        queryOrderObjectToMQ.setPayOrder(trade.getPayOrder());
        queryOrderObjectToMQ.setTransOrder(trade.getTransOrder());
        return queryOrderObjectToMQ;
    }

    public String  kuaiJieToQueryOrder(SystemOrderTrack systemOrderTrack,TradeObjectSquare tradeObjectSquare) throws  Exception{
        String merId = tradeObjectSquare.getMerId();
        String merOrderId = tradeObjectSquare.getMerOrderId();
        String terminalMerId = tradeObjectSquare.getTerminalMerId();
        //验证签名
        MerchantInfo merchantInfo = redisCacheCommonCompoment.merchantInfoCache.getOne(merId);
        Assert.notNull(merchantInfo, format("【快捷收单业务】订单号：%s ,商户号:%s,未找到商户相关信息", merOrderId, merId));
        CheckMd5Utils.checkMd5(systemOrderTrack.getTradeInfo(), merchantInfo.getSecretKey());
//        try {
            //1.判断订单号是否已经存在
            TreeMap map = new TreeMap();
            if (tradeObjectSquare.getQueryType().equals("01")) {
                PayOrder po = new PayOrder();
                po.setMerId(merId);
                po.setMerOrderId(merOrderId);
                po.setTerminalMerId(terminalMerId);
                List<PayOrder> payOrderList = paymentRecordSquareService.getPayOrderByWhereCondition(po);
                isNotElement(payOrderList, format("【订单查询】订单号：%s ,商户号:%s, 订单号不存在", merOrderId, merId),"RXH00008");
                PayOrder payOrder = payOrderList.get(0);
                map.put("status", payOrder.getOrderStatus());
                map.put("amount", payOrder.getAmount());
                map.put("merId", payOrder.getMerId());
                map.put("merOrderId", payOrder.getMerOrderId());
                map.put("resultCode", "RXH00000");
                map.put("orderId",payOrder.getPayId());
                map.put("terminalMerId",payOrder.getTerminalMerId());
                if (payOrder.getOrderStatus() == 0) {
                    map.put("msg", "交易成功");

                } else if (payOrder.getOrderStatus() == 1) {
                    map.put("msg", "交易失败");
                    if (!StringUtils.isBlank(payOrder.getTradeResult())){
                        JSONObject jsonObject = JSONObject.parseObject(payOrder.getTradeResult());
                        map.put("msg", jsonObject.get("retmsg"));
                    }
                } else {
                    map.put("msg", "交易处理中，请稍后查询!");
                    map.put("status", "3");
                }
            } else if (tradeObjectSquare.getQueryType().equals("02")) {
                TransOrder po = new TransOrder();
                po.setMerId(merId);
                po.setMerOrderId(merOrderId);
                po.setTerminalMerId(terminalMerId);
                List<TransOrder> transOrderList = paymentRecordSquareService.getTransOrderByWhereCondition(po);
                isNotElement(transOrderList, format("【订单查询】订单号：%s ,商户号:%s, 订单号不存在", merOrderId, merId),"RXH00008");
                TransOrder transOrder = transOrderList.get(0);
                map.put("status", transOrder.getOrderStatus());
                map.put("amount", transOrder.getAmount());
                map.put("merId", transOrder.getMerId());
                map.put("merOrderId", transOrder.getMerOrderId());
                map.put("resultCode", "RXH00000");
                map.put("orderId",transOrder.getTransId());
                map.put("terminalMerId",transOrder.getTerminalMerId());
                if (transOrder.getOrderStatus() == 0) {
                    map.put("msg", "交易成功");
                } else if (transOrder.getOrderStatus() == 1) {
                    map.put("msg", "交易失败");
                    if (!StringUtils.isBlank(transOrder.getTradeResult())){
                        JSONObject jsonObject = JSONObject.parseObject(transOrder.getTradeResult());
                        map.put("msg", jsonObject.get("retmsg"));
                    }
                } else {
                    map.put("msg", "交易处理中，请稍后查询!");
                    map.put("status", "3");
                }
            } else {
                map.put("status", 1);
                map.put("amount", tradeObjectSquare.getAmount());
                map.put("merId", tradeObjectSquare.getMerId());
                map.put("merOrderId", tradeObjectSquare.getMerOrderId());
                map.put("terminalMerId",tradeObjectSquare.getTerminalMerId());
                map.put("resultCode", "RXH00013");
                map.put("msg", "查询失败,queryType类型错误");
            }
            String sign = CheckMd5Utils.getMd5SignWithKey(map, merchantInfo.getSecretKey());
            map.put("signMsg", sign);
            return JSONObject.toJSONString(map);
//        }catch (Exception e){
//            logger.info(format("订单查询发生异常：%s",e.getMessage()));
//            Map map = new HashMap();
//            map.put("status", 1);
//            map.put("amount", tradeObjectSquare.getAmount());
//            map.put("merId", tradeObjectSquare.getMerId());
//            map.put("merOrderId", tradeObjectSquare.getMerOrderId());
//            map.put("resultCode", "RXH00013");
//            map.put("msg", "查询异常");
//            String sign = CheckMd5Utils.getMd5SignWithKey(map, merchantInfo.getSecretKey());
//            map.put("signMsg", sign);
//            return JSONObject.toJSONString(map);
//        }
    }

    public TradeObjectSquare kuaijieGetOrderPay(SystemOrderTrack systemOrderTrack) throws Exception{
        TradeObjectSquare tradeObjectSquare = null;
        try {
            tradeObjectSquare = JsonUtils.jsonToPojo(systemOrderTrack.getTradeInfo(), TradeObjectSquare.class);
            List<String> tradeInfoKeys = new ArrayList<>();
            // 报文参数
            Map<String, Object> tradeInfoMap = JsonUtils.jsonToMap(systemOrderTrack.getTradeInfo());
            Set<String> keys = tradeInfoMap.keySet();
            for (String key : keys) {
                validateValue(kuaijieGetOrderPayValidate,key, tradeInfoMap.get(key).toString());
                tradeInfoKeys.add(key);
            }
            // 校验必要参数
            ValidateItem(kuaijieGetOrderPayValidate,tradeInfoKeys);
            return tradeObjectSquare;
        } catch (PayException e) {
            logger.error(systemOrderTrack.getTradeInfo(), e);
            throw new PayException("请求报文无法解析！" + e.getMessage(), 1001);
        }finally {
            if(null != tradeObjectSquare){
                setSystemOrderTrack(systemOrderTrack,tradeObjectSquare);
            }
        }
    }

    private final  Map<String, ParamRule> kuaijieGetOrderPayValidate= new HashMap<String, ParamRule>() {
        {
            // 交易数据
            put("charset", new ParamRule(REQUIRED, 10, 1004));//
            put("signType", new ParamRule(REQUIRED, 10, 1004));//
            put("merId", new ParamRule(REQUIRED, 10, 1005));//
            put("merOrderId", new ParamRule(REQUIRED, 32, 1006));//
            put("transType", new ParamRule(REQUIRED, 10, 1006));//
            put("signMsg", new ParamRule(REQUIRED, 256, 1013));//

        }
    };

    private final  Map<String, ParamRule> kuaijieQueryOrderValidate= new HashMap<String, ParamRule>() {
        {
            // 交易数据
            put("charset", new ParamRule(REQUIRED, 10, 1004));//
            put("signType", new ParamRule(REQUIRED, 10, 1004));//
            put("merId", new ParamRule(REQUIRED, 10, 1005));//
            put("merOrderId", new ParamRule(REQUIRED, 32, 1006));//
            put("terminalMerId", new ParamRule(REQUIRED, 32, 1006));//
            put("signMsg", new ParamRule(REQUIRED, 256, 1013));//
            put("queryType",new ParamRule(REQUIRED,2,1007));

        }
    };

    private final  Map<String, ParamRule> kuaijieQueryFulfillmentValidate= new HashMap<String, ParamRule>() {
        {
            // 交易数据
            put("charset", new ParamRule(REQUIRED, 10, 1004));//
            put("signType", new ParamRule(REQUIRED, 10, 1004));//
            put("merId", new ParamRule(REQUIRED, 10, 1005));//
//            put("merOrderId", new ParamRule(REQUIRED, 32, 1006));//
            put("terminalMerId", new ParamRule(REQUIRED, 32, 1006));//
            put("signMsg", new ParamRule(REQUIRED, 256, 1013));//
        }
    };

    private final  Map<String, ParamRule> kuaijieQueryBindCardValidate = new HashMap<String, ParamRule>() {
        {
            // 交易数据
            put("charset", new ParamRule(REQUIRED, 10, 1004));//
            put("signType", new ParamRule(REQUIRED, 10, 1004));//
            put("merId", new ParamRule(REQUIRED, 10, 1005));//
//            put("merOrderId", new ParamRule(REQUIRED, 32, 1006));//
            put("terminalMerId", new ParamRule(REQUIRED, 32, 1006));//
            put("bankCardNum", new ParamRule(OTHER, 32, 1006));//
            put("signMsg", new ParamRule(REQUIRED, 256, 1013));//
        }
    };

    public TradeObjectSquare kuaijieQueryOrder(SystemOrderTrack systemOrderTrack) throws Exception{
        TradeObjectSquare tradeObjectSquare = null;
        try {
            tradeObjectSquare = JsonUtils.jsonToPojo(systemOrderTrack.getTradeInfo(), TradeObjectSquare.class);
            List<String> tradeInfoKeys = new ArrayList<>();
            // 报文参数
            Map<String, Object> tradeInfoMap = JsonUtils.jsonToMap(systemOrderTrack.getTradeInfo());
            Set<String> keys = tradeInfoMap.keySet();
            for (String key : keys) {
                validateValue(kuaijieQueryOrderValidate,key, tradeInfoMap.get(key).toString());
                tradeInfoKeys.add(key);
            }
            // 校验必要参数
            ValidateItem(kuaijieQueryOrderValidate,tradeInfoKeys);
            return tradeObjectSquare;
        } catch (PayException e) {
            logger.error(systemOrderTrack.getTradeInfo(), e);
            throw new PayException("请求报文无法解析！" + e.getMessage(), 1001);
        }finally {
            if(null != tradeObjectSquare){
                setSystemOrderTrack(systemOrderTrack,tradeObjectSquare);
            }
        }
    }

    public TradeObjectSquare queryFulfillment(SystemOrderTrack systemOrderTrack) throws Exception{
        TradeObjectSquare tradeObjectSquare = null;
        try {
            tradeObjectSquare = JsonUtils.jsonToPojo(systemOrderTrack.getTradeInfo(), TradeObjectSquare.class);
            List<String> tradeInfoKeys = new ArrayList<>();
            // 报文参数
            Map<String, Object> tradeInfoMap = JsonUtils.jsonToMap(systemOrderTrack.getTradeInfo());
            Set<String> keys = tradeInfoMap.keySet();
            for (String key : keys) {
                validateValue(kuaijieQueryFulfillmentValidate,key, tradeInfoMap.get(key).toString());
                tradeInfoKeys.add(key);
            }
            // 校验必要参数
            ValidateItem(kuaijieQueryFulfillmentValidate,tradeInfoKeys);
            return tradeObjectSquare;
        } catch (PayException e) {
            logger.error(systemOrderTrack.getTradeInfo(), e);
            throw new PayException("请求报文无法解析！" + e.getMessage(), 1001);
        }finally {
            if(null != tradeObjectSquare){
                setSystemOrderTrack(systemOrderTrack,tradeObjectSquare);
            }
        }
    }

    public TradeObjectSquare queryBindCard(SystemOrderTrack systemOrderTrack) throws Exception{
        TradeObjectSquare tradeObjectSquare = null;
        try {
            tradeObjectSquare = JsonUtils.jsonToPojo(systemOrderTrack.getTradeInfo(), TradeObjectSquare.class);
            List<String> tradeInfoKeys = new ArrayList<>();
            // 报文参数
            Map<String, Object> tradeInfoMap = JsonUtils.jsonToMap(systemOrderTrack.getTradeInfo());
            Set<String> keys = tradeInfoMap.keySet();
            for (String key : keys) {
                validateValue(kuaijieQueryBindCardValidate,key, tradeInfoMap.get(key).toString());
                tradeInfoKeys.add(key);
            }
            // 校验必要参数
            ValidateItem(kuaijieQueryBindCardValidate,tradeInfoKeys);
            return tradeObjectSquare;
        } catch (PayException e) {
            logger.error(systemOrderTrack.getTradeInfo(), e);
            throw new PayException("请求报文无法解析！" + e.getMessage(), 1001);
        }finally {
            if(null != tradeObjectSquare){
                setSystemOrderTrack(systemOrderTrack,tradeObjectSquare);
            }
        }
    }


    public String kuaiJieToQueryFulfillment(SystemOrderTrack systemOrderTrack, TradeObjectSquare tradeObjectSquare) throws PayException {
        String merId = tradeObjectSquare.getMerId();
        String merOrderId = tradeObjectSquare.getMerOrderId();
        String terminalMerId = tradeObjectSquare.getTerminalMerId();
        //验证签名
        MerchantInfo merchantInfo = redisCacheCommonCompoment.merchantInfoCache.getOne(merId);
        Assert.notNull(merchantInfo, format("【业务开通查询】订单号：%s ,商户号:%s,未找到商户相关信息", merOrderId, merId));
        CheckMd5Utils.checkMd5(systemOrderTrack.getTradeInfo(), merchantInfo.getSecretKey());
        MerchantRegisterCollect collect = new MerchantRegisterCollect();
        collect.setStatus((int)SystemConstant.BANK_STATUS_SUCCESS);
        collect.setMerId(merId);
        collect.setTerminalMerId(terminalMerId);
        List<MerchantRegisterCollect> merchantRegisterCollects = merchantRegisterCollectService.selectByMeridAndterminalMerIdAndStatus(collect);
        TreeMap map = new TreeMap();
        map.put("terminalMerId", terminalMerId);
        map.put("merId", merId);
        map.put("resultCode", "RXH00000");
        if (CollectionUtils.isEmpty(merchantRegisterCollects)){
            map.put("status", SystemConstant.BANK_STATUS_FAIL);
            map.put("msg","该商户未开通支付业务");
        }else {
            map.put("status", SystemConstant.BANK_STATUS_SUCCESS);
            map.put("msg","该商户已开通支付业务");
        }
        String sign = CheckMd5Utils.getMd5SignWithKey(map, merchantInfo.getSecretKey());
        map.put("signMsg", sign);
        return JSONObject.toJSONString(map);
    }

    public String kuaiJieToQueryBindCard(SystemOrderTrack systemOrderTrack, TradeObjectSquare tradeObjectSquare) throws PayException {
        String merId = tradeObjectSquare.getMerId();
        String merOrderId = tradeObjectSquare.getMerOrderId();
        String terminalMerId = tradeObjectSquare.getTerminalMerId();
        //验证签名
        MerchantInfo merchantInfo = redisCacheCommonCompoment.merchantInfoCache.getOne(merId);
        Assert.notNull(merchantInfo, format("【绑卡查询】订单号：%s ,商户号:%s,未找到商户相关信息", merOrderId, merId));
        CheckMd5Utils.checkMd5(systemOrderTrack.getTradeInfo(), merchantInfo.getSecretKey());
        MerchantCard parm = new MerchantCard();
        if (!StringUtils.isBlank(tradeObjectSquare.getBankCardNum())){
            parm.setCardNum(tradeObjectSquare.getBankCardNum());
        }
        parm.setTerminalMerId(tradeObjectSquare.getTerminalMerId());
        parm.setMerId(merId);
//        parm.setStatus((int)SystemConstant.BANK_STATUS_SUCCESS);
        List<MerchantCard> merchantCards = merchantCardService.select(parm);
        List<String> cardNums = new ArrayList<>();
        List<Map> cardMap = new ArrayList<>();
        for (MerchantCard merchantCard : merchantCards){
            if (!cardNums.contains(merchantCard.getCardNum())){
                Map map = new HashMap();
                map.put("bankCardNum",merchantCard.getCardNum());
                map.put("status",merchantCard.getStatus());
                cardMap.add(map);
                cardNums.add(merchantCard.getCardNum());
            }
        }
        TreeMap map = new TreeMap();
        map.put("terminalMerId", terminalMerId);
        map.put("merId", merId);
        map.put("resultCode", "RXH00000");
        map.put("bondCardList",JSONObject.toJSONString(cardMap));
        map.put("status", SystemConstant.BANK_STATUS_SUCCESS);
        map.put("msg","查询成功");
        String sign = CheckMd5Utils.getMd5SignWithKey(map, merchantInfo.getSecretKey());
        map.put("signMsg", sign);
        return JSONObject.toJSONString(map);
    }
}
