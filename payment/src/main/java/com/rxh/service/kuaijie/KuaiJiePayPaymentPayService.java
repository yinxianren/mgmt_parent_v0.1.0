package com.rxh.service.kuaijie;

import com.rxh.exception.PayException;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.merchant.MerchantRegisterInfo;
import com.rxh.pojo.payment.ParamRule;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.pojo.payment.TradeObjectSquare;
import com.rxh.service.AbstractCommonService;
import com.rxh.square.pojo.*;
import com.rxh.utils.UUID;
import com.rxh.utils.*;
import com.rxh.vo.OrderObjectToMQ;
import com.rxh.vo.QueryOrderObjectToMQ;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import sun.reflect.generics.tree.Tree;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 *  描述： 重新整合收单业务
 * @author panda
 * @date 20190716
 */
@Service
public class KuaiJiePayPaymentPayService extends AbstractCommonService {



    /**
     *  确认收单
     * @param systemOrderTrack
     * @param tradeObjectSquare
     * @return
     * @throws PayException
     */
    public SquareTrade getConfirmPayParam(SystemOrderTrack systemOrderTrack, TradeObjectSquare tradeObjectSquare) throws PayException {
        String merId=systemOrderTrack.getMerId();

        MerchantInfo merchantInfo =redisCacheCommonCompoment.merchantInfoCache.getOne(merId);
        isNull(merchantInfo,format("【确认收单业务】商户号[%s],未获得商户信息",merId),"RXH00013");
        //验签
        CheckMd5Utils.checkMd5(systemOrderTrack.getTradeInfo(),merchantInfo.getSecretKey());
        PayOrder payOrder = recordPaymentSquareService.getProcessingPayOrderByMerOrderId(tradeObjectSquare,tradeObjectSquare.getMerOrderId());
        isNull(payOrder,format("【确认收单业务】商户号[%s],未获得订单信息",merId),"RXH00013");

//        ChannelInfo channelInfo = recordPaymentSquareService.getChannelInfo(payOrder.getChannelId());
        ChannelInfo channelInfo =redisCacheCommonCompoment.channelInfoCache.getOne(payOrder.getChannelId());
        isNull(channelInfo,format("【确认收单业务】商户号[%s],未获得通道信息",merId),"RXH00013");

        ExtraChannelInfo extraChannelInfo = recordPaymentSquareService.getExtraChannelInfoByOrgId(channelInfo.getOrganizationId(),SystemConstant.BONDCARD);
        isNull(extraChannelInfo,format("【确认收单业务】商户号[%s],未获得附属通道信息",merId),"RXH00013");

        List<MerchantCard> merchantCardList=redisCacheCommonCompoment.merchantCardCache.getMore(merId,tradeObjectSquare.getTerminalMerId());
        isNull(merchantCardList,format("【确认收单业务】商户号[%s],未获得持卡人信息",merId),"RXH00013");
        MerchantCard merchantCard = merchantCardList.stream()
                .filter(t-> t.getExtraChannelId().equals(extraChannelInfo.getExtraChannelId()) && t.getCardNum().equals(payOrder.getBankCardNum()))
                .findAny().orElse(null);
        isNull(merchantCard,format("【确认收单业务】商户号[%s],未获得持卡人信息",merId),"RXH00013");
//        MerchantCard merchantCard = recordPaymentSquareService.getMerchantCardByMerIdAndTermIdAndExtraId(tradeObjectSquare.getMerId(),tradeObjectSquare.getTerminalMerId(),extraChannelInfo.getExtraChannelId());
//        Assert.notNull(merchantCard,String.format("【确认收单业务】商户号[%s],未获得持卡人信息",merId));

        MerchantRegisterCollect merchantRegisterCollect = recordPaymentSquareService.searchMerchantRegisterCollectToSms(extraChannelInfo,payOrder,tradeObjectSquare);
        isNull(merchantRegisterCollect,String.format("【确认收单业务】商户号[%s],未获得进件记录成功信息",merId),"RXH00013");

        MerchantChannelHistory wasUseChannel = recordPaymentSquareService.getLastUseChannel(tradeObjectSquare);

        SquareTrade trade = new SquareTrade();
        trade.setMerchantChannelHistory(wasUseChannel);
        trade.setMerchantRegisterCollect(merchantRegisterCollect);
        trade.setMerchantCard(merchantCard);
        trade.setExtraChannelInfo(extraChannelInfo);
        trade.setChannelInfo(channelInfo);
        trade.setTradeObjectSquare(tradeObjectSquare);
        trade.setPayOrder(payOrder);
        trade.setMerchantInfo(merchantInfo);

        return trade;
    }

    /**
     *   MQ版本
     * @param trade
     * @param bankResult
     * @return
     * @throws Exception
     *
     */
    public String kuaiJieToUpdataPayInfoAndPayWallet_MQ(SquareTrade trade, BankResult bankResult){
        PayMap<String,Object>  map = new PayMap<>();
        PayOrder payOrder = trade.getPayOrder();
        OrderObjectToMQ orderObjectToMQ=null;

        //订单状态为成功，推送到mq去处理
        if(bankResult.getStatus()==0){
            payOrder.setTradeResult(bankResult.getParam());
            MerchantChannelHistory merchantChannelHistory=trade.getMerchantChannelHistory();
            if(null == merchantChannelHistory) merchantChannelHistory=new  MerchantChannelHistory();
            merchantChannelHistory=this.renewalMerchantChannelHistory(trade,merchantChannelHistory,bankResult);
            map.lput("merchantChannelHistory",merchantChannelHistory);
            List<RiskQuotaData> riskQuotaDataList=this.getRiskQuotaData(trade);
            map.lput("riskQuotaDataList",riskQuotaDataList);
            payOrder.setBankTime(bankResult.getBankTime());
            payOrder.setOrgOrderId(bankResult.getBankOrderId());
            //状态更改为20，表示mq处理中
            payOrder.setOrderStatus(SystemConstant.MQ_PROCESSED);
            orderObjectToMQ=this.getOrderObjectToMQ(trade);
            payOrderService.updateByPrimaryKey(payOrder);
        }else{
            payOrder.setOrderStatus(Integer.valueOf(bankResult.getStatus()));
            payOrder.setBankTime(bankResult.getBankTime());
            if (bankResult.getBankOrderId() !=  null){
                payOrder.setOrgOrderId(bankResult.getBankOrderId());
            }
            payOrderService.updateByPrimaryKey(payOrder);
        }
        //保证先保存到数据库，在发送到队列去
        map.lput("payOrder",payOrder);
        int count= kuaiJiePayService.saveAndUpdateWalletsAndRecord(map);
        if(bankResult.getStatus() == 0) transOrderMQ.sendObjectMessageToTransOderMQ(orderObjectToMQ);
        if(bankResult.getStatus() != 3 ) {
            //保存同步结果，推送到MQ做查询
            payOrder.setTradeResult(bankResult.getParam());
            payOrder.setOrderStatus(Integer.valueOf(bankResult.getStatus()));
            trade.setPayOrder(payOrder);
            QueryOrderObjectToMQ queryOrderObjectToMQ = kuaijiePayQueryService.getQueryOrderObjectToMQ(trade);
            transOrderMQ.sendObjectMessageToQueryPayOderMQ(queryOrderObjectToMQ, 20L);
        }
        trade.setPayOrder(payOrder);
        return getTradeReturnJson1(trade,bankResult);
    }

    private String getTradeReturnJson1(SquareTrade trade, BankResult result) {
        MerchantInfo merchantInfo = trade.getMerchantInfo();
        String merId = trade.getTradeObjectSquare().getMerId();
        String merOrderId = trade.getTradeObjectSquare().getMerOrderId();
        String status = result.getStatus().toString();
        String resultCode = "RXH00013";
        if (result.getStatus() == 3){
            status = "0";
        }
        if (!StringUtils.isBlank(result.getBankCode())){
            if (result.getBankCode().equals("300")){
                status = String.valueOf(SystemConstant.BANK_STATUS_FAIL);
                resultCode = "RXH00004";
            }else if (result.getBankCode().equals("301")){
                status = String.valueOf(SystemConstant.BANK_STATUS_FAIL);
                resultCode = "RXH00003";
            }else if (result.getBankCode().equals("302")){
                status = String.valueOf(SystemConstant.BANK_STATUS_FAIL);
                resultCode = "RXH00005";
            }
        }
        String msg = result.getBankResult();
        PayTreeMap<String, Object> map = new PayTreeMap<>();
        map.lput("merId", merId)
                .lput("merOrderId", merOrderId)
                .lput("orderId", trade.getPayOrder().getPayId())
                .lput("amount", trade.getPayOrder().getAmount())
//                .lput("", trade.getPayOrder().getTradeTime())
                .lput("status", status)
                .lput("msg", msg);
        if (status.equals(String.valueOf(SystemConstant.BANK_STATUS_SUCCESS))) map.lput("resultCode","RXH00000");
        else map.lput("resultCode",resultCode);
        map.lput("signMsg", CheckMd5Utils.getMd5SignWithKey(map,merchantInfo.getSecretKey()));
        return JsonUtils.objectToJson(map);

    }


    /**
     * 封装MerchantChannelHistory信息
     * @param trade
     * @return
     */
    private MerchantChannelHistory  renewalMerchantChannelHistory(SquareTrade trade,MerchantChannelHistory merchantChannelHistory,BankResult bankResult){

        ChannelInfo channelInfo = trade.getChannelInfo();
        PayOrder payOrder = trade.getPayOrder();
        //id
        String id = merchantChannelHistory.getId();
        merchantChannelHistory.setId( null == id ? UUID.createKey("merchant_channel_history", "") : id );
        //organizationId
        String organizationId=merchantChannelHistory.getOrganizationId();
        merchantChannelHistory.setOrganizationId( null == organizationId ? channelInfo.getOrganizationId() : organizationId );
        //channelId
        String channelId=merchantChannelHistory.getChannelId();
        merchantChannelHistory.setChannelId( null == channelId ? channelInfo.getChannelId() : channelId );
        //merId
        String merId=merchantChannelHistory.getMerId();
        merchantChannelHistory.setMerId( null == merId ? payOrder.getMerId() : merId );
        //channelType
        String channelType=merchantChannelHistory.getChannelType();
        merchantChannelHistory.setChannelType( null == channelType ? channelInfo.getType().toString() : channelType );
        //amount
        BigDecimal amount=merchantChannelHistory.getAmount();
        amount= (null == amount ? new BigDecimal(0) : amount);
        if(bankResult.getStatus() == 0) amount=amount.add(trade.getPayOrder().getAmount());
        merchantChannelHistory.setAmount(amount);
        //count
        Integer count=merchantChannelHistory.getTradeCount();
        count =(null == count ? 1 : count+1);
        merchantChannelHistory.setTradeCount(count);

        merchantChannelHistory.setCreateTime(new Date());

        merchantChannelHistory.setStatus(bankResult.getStatus()+"");

        return merchantChannelHistory;
    }



    /**
     *  封装发送到 mq对象参数
     * @return
     */
    public OrderObjectToMQ getOrderObjectToMQ(SquareTrade trade){
        PayOrder  payOrder= trade.getPayOrder();//平台订单对象
        TradeObjectSquare  tradeObjectSquare= trade.getTradeObjectSquare();//商户发起的订单对象
        ChannelInfo channelInfo=trade.getChannelInfo();
        MerchantInfo merchantInfo=trade.getMerchantInfo();


        BigDecimal amount= payOrder.getAmount();
        BigDecimal payFee=tradeObjectSquare.getPayFee();//支付费率
        String merId=payOrder.getMerId();
        String merOrderId=payOrder.getMerOrderId();
        String payType=payOrder.getPayType();
        String channelId=channelInfo.getChannelId();
        Integer channelType=channelInfo.getType();//
        String payId=payOrder.getPayId();
        String terminalMerId=payOrder.getTerminalMerId();
        String parentId=merchantInfo.getParentId();
        String channelTransCode=channelInfo.getChannelTransCode();
        Integer  orderStatus=payOrder.getOrderStatus();
        return new OrderObjectToMQ()
                .lsetAmount(amount)
                .lsetPayFee(payFee)
                .lsetMerId(merId)
                .lsetMerOrderId(merOrderId)
                .lsetPayType(payType)
                .lsetChannelId(channelId)
                .lsetChannelType(channelType)
                .lsetPayId(payId)
                .lsetTerminalMerId(terminalMerId)
                .lsetParentId(parentId)
                .lsetChannelTransCode(channelTransCode)
                .lsetOrderStatus(orderStatus)
                ;
    }

    /**
     *  封装响应结果
     * @param trade
     * @param result
     * @return
     */
    private String getTradeReturnJson(SquareTrade trade, BankResult result)  {
        MerchantInfo merchantInfo = trade.getMerchantInfo();
        String merId = trade.getTradeObjectSquare().getMerId();
        String merOrderId = trade.getTradeObjectSquare().getMerOrderId();
        String status = result.getStatus().toString();
        String msg = result.getBankResult();

        PayTreeMap<String, Object> map = new PayTreeMap<>();
        map.lput("merId", merId)
                .lput("merOrderId", merOrderId)
                .lput("orderId", trade.getPayOrder().getPayId())
                .lput("amount", trade.getPayOrder().getAmount())
//                .lput("tradeTime", trade.getPayOrder().getTradeTime())
                .lput("status", status)
                .lput("msg", msg);
        if (result.getStatus() == SystemConstant.BANK_STATUS_SUCCESS) map.lput("resultCode","RXH00000");
        else map.lput("resultCode","RXH00013");
        map.lput("signMsg", CheckMd5Utils.getMd5SignWithKey(map,merchantInfo.getSecretKey()));
        return JsonUtils.objectToJson(map);
    }

    /**
     *
     * @param systemOrderTrack
     * @param trade
     * @param bankResult
     * @param message
     * @return
     */
    public String getReturnJson_1(SystemOrderTrack systemOrderTrack, SquareTrade trade, BankResult bankResult, String message) {

        String merOrderId ="";
        int status=1;
        String msg="";
        String transId="";
        String secretKey="";
        if(null != systemOrderTrack) merOrderId=systemOrderTrack.getMerOrderId();
        status=(null == bankResult ? status : bankResult.getStatus());
        msg=(null == bankResult ? message : bankResult.getBankResult());
        if( null != trade){
            TransOrder transOrder = trade.getTransOrder();
            if(null != transOrder){
                transId = transOrder.getTransId();
            }
        }
        if(null != systemOrderTrack){
            //获取商户信息
            try{
                MerchantInfo merchantInfo = paymentRecordSquareService.getMerchantInfoByMerId(systemOrderTrack.getMerId());
                secretKey= merchantInfo.getSecretKey();
            }catch (Exception exception){
                exception.printStackTrace();
            }
        }
        TreeMap<String, Object> resultMap = new TreeMap<>();
        resultMap.put("merId",systemOrderTrack.getMerId());
        resultMap.put("merOrderId",merOrderId);
        resultMap.put("orderId",transId);
        resultMap.put("amount",systemOrderTrack.getAmount());
        resultMap.put("status",status);
        resultMap.put("msg",msg);
        resultMap.put("resultCode","RXH00013");
        resultMap.put("signMsg",null != secretKey ? CheckMd5Utils.getMd5SignWithKey(resultMap,secretKey) : null);
        String resultjson = JsonUtils.objectToJson(resultMap);
        return resultjson;
    }

    /**
     *
     * @param systemOrderTrack
     * @param trade
     * @param bankResult
     * @param message
     * @return
     */
    public String getReturnJson_1(SystemOrderTrack systemOrderTrack, SquareTrade trade, BankResult bankResult, String message,String resultCode) {

        String merOrderId ="";
        int status=1;
        String msg="";
        String transId="";
        String secretKey="";
        if(null != systemOrderTrack) merOrderId=systemOrderTrack.getMerOrderId();
        status=(null == bankResult ? status : bankResult.getStatus());
        msg=(null == bankResult ? message : bankResult.getBankResult());
        if( null != trade){
            TransOrder transOrder = trade.getTransOrder();
            if(null != transOrder){
                transId = transOrder.getTransId();
            }
        }
        if(null != systemOrderTrack){
            //获取商户信息
            try{
                MerchantInfo merchantInfo = paymentRecordSquareService.getMerchantInfoByMerId(systemOrderTrack.getMerId());
                secretKey= merchantInfo.getSecretKey();
            }catch (Exception exception){
                exception.printStackTrace();
            }
        }
        TreeMap<String, Object> resultMap = new TreeMap<>();
        resultMap.put("merId",systemOrderTrack.getMerId());
        resultMap.put("merOrderId",merOrderId);
        resultMap.put("orderId",transId);
        resultMap.put("amount",systemOrderTrack.getAmount());
        resultMap.put("status",status);
        resultMap.put("resultCode",resultCode);
        resultMap.put("msg",msg);
        resultMap.put("signMsg",null != secretKey ? getMd5SignWithKey(resultMap,secretKey) : null);
        String resultjson = JsonUtils.objectToJson(resultMap);
        return resultjson;
    }

    /**
     * @param trade
     * @return
     */
    private List<RiskQuotaData> getRiskQuotaData(SquareTrade trade){
        PayOrder paysOrder = trade.getPayOrder();
        BigDecimal amount=paysOrder.getAmount();
        ChannelInfo channelInfo = trade.getChannelInfo();
        String channelId=channelInfo.getChannelId();

        SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
        String day = sdfDay.format(new Date());
        SimpleDateFormat sdfMonth = new SimpleDateFormat("yyyy-MM");
        String month = sdfMonth.format(new Date());

        //通道
        RiskQuotaData riskQuotaData=new RiskQuotaData();
        riskQuotaData.setTradeTimeList(Arrays.asList(day,month));
        riskQuotaData.setRefId(channelId);
        riskQuotaData.setRefType((short)1);// 1:通道 2:商户
        List<RiskQuotaData> riskQuotaDataList= riskQuotaDataService.getRiskQuotaDataByWhereCondition(riskQuotaData);
        List<RiskQuotaData> newRiskQuotaDataList=new ArrayList<>();

        RiskQuotaData riskQuotaData_month=riskQuotaDataList.stream()
                .filter(t->t.getTradeTime().equals(month))
                .distinct()
                .findFirst()
                .orElse(null);
        if(null == riskQuotaData_month)   {
            //yyyy-MM 级别
            riskQuotaData=new RiskQuotaData();
            riskQuotaData.setId(null);
            riskQuotaData.setRefId(channelId);
            riskQuotaData.setRefType((short)1);//通道级别
            riskQuotaData.setType((short)2);//月级别
            riskQuotaData.setTradeTime(month);
            riskQuotaData.setAmount(amount);
            newRiskQuotaDataList.add(riskQuotaData);
        }else{
            riskQuotaData_month.setAmount((riskQuotaData_month.getAmount().add(amount)));//增加当前月增额
            newRiskQuotaDataList.add(riskQuotaData_month);
        }

        RiskQuotaData riskQuotaData_day=riskQuotaDataList.stream()
                .filter(t->t.getTradeTime().equals(day))
                .distinct()
                .findFirst()
                .orElse(null);
        if(null == riskQuotaData_day){
            //yyyy-MM-dd 级别
            riskQuotaData=new RiskQuotaData();
            riskQuotaData.setRefId(channelId);
            riskQuotaData.setRefType((short)1);//通道级别
            riskQuotaData.setType((short)1);//日级别
            riskQuotaData.setTradeTime(day);
            riskQuotaData.setAmount(amount);
            riskQuotaData.setId(null);
            newRiskQuotaDataList.add(riskQuotaData);
        }else{
            riskQuotaData_day.setAmount((riskQuotaData_day.getAmount().add(amount)));//增加当前日增额
            newRiskQuotaDataList.add(riskQuotaData_day);
        }

        //商户
        riskQuotaData=new RiskQuotaData();
        riskQuotaData.setTradeTimeList(Arrays.asList(day,month));
        riskQuotaData.setRefId(paysOrder.getMerId());
        riskQuotaData.setRefType((short)2);// 1:通道 2:商户
        riskQuotaDataList= riskQuotaDataService.getRiskQuotaDataByWhereCondition(riskQuotaData);

        riskQuotaData_month=riskQuotaDataList.stream()
                .filter(t->t.getTradeTime().equals(month))
                .distinct()
                .findFirst()
                .orElse(null);

        if(null == riskQuotaData_month)   {
            //yyyy-MM 级别
            riskQuotaData=new RiskQuotaData();
            riskQuotaData.setId(null);
            riskQuotaData.setRefId(paysOrder.getMerId());
            riskQuotaData.setRefType((short)2);//商户级别
            riskQuotaData.setType((short)2);//月级别
            riskQuotaData.setTradeTime(month);
            riskQuotaData.setAmount(amount);
            newRiskQuotaDataList.add(riskQuotaData);
        }else{
            riskQuotaData_month.setAmount((riskQuotaData_month.getAmount().add(amount)));//增加当前月增额
            newRiskQuotaDataList.add(riskQuotaData_month);
        }

        riskQuotaData_day=riskQuotaDataList.stream()
                .filter(t->t.getTradeTime().equals(day))
                .distinct()
                .findFirst()
                .orElse(null);
        if(null == riskQuotaData_day){
            //yyyy-MM-dd 级别
            riskQuotaData=new RiskQuotaData();
            riskQuotaData.setRefId(paysOrder.getMerId());
            riskQuotaData.setRefType((short)2);//商户级别
            riskQuotaData.setType((short)1);//月级别
            riskQuotaData.setTradeTime(day);
            riskQuotaData.setAmount(amount);
            riskQuotaData.setId(null);
            newRiskQuotaDataList.add(riskQuotaData);
        }else{
            riskQuotaData_day.setAmount((riskQuotaData_day.getAmount().add(amount)));//增加当前日增额
            newRiskQuotaDataList.add(riskQuotaData_day);
        }
        return newRiskQuotaDataList;
    }



    /**
     *   描述：发起收单
     *
     * @param trade
     */
    public BankResult kuaiJieToPay(SquareTrade trade) throws Exception {
        try{
            logger.info("【快捷收单业务】商户号[{}]:  发送收单请求：{}",trade.getPayOrder().getMerId(),JsonUtils.objectToJson(trade));
            InetAddress address = InetAddress.getLocalHost();
            String ip = address.getHostAddress();
            trade.setIp(ip);
            String  result = HttpClientUtils.doPostJson(HttpClientUtils.getHttpClient(), trade.getChannelInfo().getPayUrl(), JsonUtils.objectToJsonNonNull(trade));
            logger.info("【快捷收单业务】商户号[{}]: 收单请求结果：{}",trade.getPayOrder().getMerId(),result);
            if (StringUtils.isBlank(result))  throw  new NullPointerException(String.format("商户号[%s]:响应结果为空",trade.getPayOrder().getMerId()));
            BankResult bankResult = JsonUtils.jsonToPojo(result, BankResult.class);
            isNull(bankResult,format("【快捷收单业务】商户号[%s]:转换BankResult异常",trade.getPayOrder().getMerId()),"RXH00013");
            return bankResult;
        }catch ( Exception exception){
            logger.info("【快捷收单业务】 商户号[{}]:收单请求过程中抛出异常！",trade.getPayOrder().getMerId());
            throw exception;
        }
    }

    /**
     *   描述：发起收单
     *
     * @param trade
     */
    public BankResult kuaiJieToFeePay(SquareTrade trade) throws UnknownHostException {
        try{
            logger.info("【快捷收单业务】商户号[{}]:  发送收单请求：{}",trade.getPayOrder().getMerId(),JsonUtils.objectToJson(trade));
            InetAddress address = InetAddress.getLocalHost();
            String ip = address.getHostAddress();
            trade.setIp(ip);
            String  result = HttpClientUtils.doPostJson(HttpClientUtils.getHttpClient(), trade.getChannelInfo().getPayUrl(), JsonUtils.objectToJsonNonNull(trade));
            logger.info("【快捷收单业务】商户号[{}]: 收单请求结果：{}",trade.getPayOrder().getMerId(),result);
            if (StringUtils.isBlank(result))  throw  new NullPointerException(String.format("商户号[%s]:响应结果为空",trade.getPayOrder().getMerId()));
            BankResult bankResult = JsonUtils.jsonToPojo(result, BankResult.class);
            Assert.notNull(bankResult,format("【快捷收单业务】商户号[%s]:转换BankResult异常",trade.getPayOrder().getMerId()));
            return bankResult;
        }catch ( Exception exception){
            logger.info("【快捷收单业务】 商户号[{}]:收单请求过程中抛出异常！",trade.getPayOrder().getMerId());
            throw exception;
        }
    }


    /**
     *   保存收单订单
     * @param systemOrderTrack
     * @param tradeObjectSquare
     * @return
     * @throws Exception
     */
    public SquareTrade kuaiJieToSaveOrder(SystemOrderTrack systemOrderTrack, TradeObjectSquare tradeObjectSquare) throws Exception {
        String merId=tradeObjectSquare.getMerId();
        String merOrderId=tradeObjectSquare.getMerOrderId();
        String terminalMerId=tradeObjectSquare.getTerminalMerId();
        //验证签名
        MerchantInfo merchantInfo =redisCacheCommonCompoment.merchantInfoCache.getOne(merId);
        isNull(merchantInfo,format("【快捷收单业务】订单号：%s ,商户号:%s,未找到商户相关信息",merOrderId,merId),"RXH00013");
        CheckMd5Utils.checkMd5(systemOrderTrack.getTradeInfo(),merchantInfo.getSecretKey());
        //1.判断订单号是否已经存在
        PayOrder po=new PayOrder();
        po.setMerId(merId);
        po.setMerOrderId(merOrderId);
        po.setTerminalMerId(terminalMerId);
        List<PayOrder>  payOrderList=paymentRecordSquareService.getPayOrderByWhereCondition(po);
        isHasElement(payOrderList,format("【快捷收单业务】订单号：%s ,商户号:%s, 订单号已经存在",merOrderId,merId),"RXH00009");
        MerchantRate merchantRate = redisCacheCommonCompoment.merchantRateCache.getOne(merId,tradeObjectSquare.getBizType());
        isNull(merchantRate,format("【快捷收单业务】订单号：%s ,商户号:%s,未找到商户相关费率信息",merOrderId,merId),"RXH00013");
        if (merchantRate.getRateFee().compareTo(tradeObjectSquare.getPayFee()) == 1){
             throw new PayException(format("【快捷收单业务】订单号：%s ,商户号:%s,支付费率小于商户设置费率",merOrderId,merId),"RXH00013");
        }
        //0.判断是否有交易记录，如有不做任何判断
        SquareTrade trade = new SquareTrade();
        MerchantChannelHistory wasUseChannel = recordPaymentSquareService.getLastUseChannel(tradeObjectSquare);
        trade.setMerchantChannelHistory(wasUseChannel);

        //2-1.判断商户是否进件
//        MerchantRegisterInfo merchantRegisterInfo=paymentRecordSquareService.getMerchantRegisterInfoByMerIdAndTerminalMerId(merId,terminalMerId);
        MerchantRegisterInfo merchantRegisterInfo=redisCacheCommonCompoment.merchantRegisterInfoCache.getAll()
                .stream()
                .filter(t-> t.getMerId().equals(merId) && t.getTerminalMerId().equals(terminalMerId) && t.getIdentityNum().equals(tradeObjectSquare.getIdentityNum()))
                .distinct()
                .findAny()
                .orElse(null);

        isNull(merchantRegisterInfo,format("【快捷收单业务】订单号：%s ,商户号:%s, 商户未进行进件！",merOrderId,merId),"RXH00013");
        //2-2 .获取进件附属信息
//        List<MerchantRegisterCollect> merchantRegisterCollects=paymentRecordSquareService.getmMrchantRegisterCollectByMerIdAndTerminalMerId(merId,terminalMerId,tradeObjectSquare.getBankCardNum());
        List<MerchantRegisterCollect> merchantRegisterCollects=redisCacheCommonCompoment.merchantRegisterCollectCache.getAll()
                .stream()
                .filter(t->
                        t.getMerId().equals(merId)
                                && t.getTerminalMerId().equals(terminalMerId)
                                && t.getStatus() == 0)
                .distinct()
                .collect(Collectors.toList());
        isNotElement(merchantRegisterCollects,format("【快捷收单业务】订单号：%s ,商户号:%s, 商户没有成功进件信息！",merOrderId,merId),"RXH00013");
        //3.执行商户风险控制
        MerchantQuotaRisk merchantQuotaRisk = paymentRecordSquareService.getMerchantQuotaRiskByMerId(merId);
        isNull(merchantQuotaRisk,format("【快捷收单业务】订单号：%s ,商户号:%s, 未找到风控对象",merOrderId,merId),"RXH00013");
        payOrderOptimalChannelStrategy.riskManagement(merchantQuotaRisk,tradeObjectSquare);
        //4-1获取商户配置
//        MerchantSetting merchantSetting = merchantSettingService.getMerchantSettingByMerId(merId);
        MerchantSetting merchantSetting = redisCacheCommonCompoment.merchantSettingCache.getOne(merId);
        isNull(merchantSetting,format("【快捷收单业务】订单号：%s ,商户号:%s,未进行商户通道配置！",merOrderId,merId),"RXH00013");
        //4-2.获取商户所支持的通道
        List<ChannelInfo> channelInfoList=this.getMerchantSurpportChannel(merchantSetting,tradeObjectSquare);
        isNotElement(channelInfoList,format("【快捷收单业务】订单号：%s ,商户号:%s, 未找到风控对象",merOrderId,merId),"RXH00013");
        //4-3.获取附属通道信息，通道类型为bizType 且 通道的组织id=附属通道的组织id
//        List<ExtraChannelInfo> extraChannelInfoList = paymentRecordSquareService.getChannelInfosByPayType(tradeObjectSquare);
        List<ExtraChannelInfo> extraChannelInfoList = redisCacheCommonCompoment.extraChannelInfoCache.getAll()
                .stream()
                .filter( t->{
                    if(null !=tradeObjectSquare.getBizType()){
                        if(t.getType().equals("1"))  return true;
                        else return  false;
                    }
                    return true;
                })
                .distinct()
                .collect(Collectors.toList());
        isNotElement(extraChannelInfoList,format("【快捷收单业务】订单号：%s ,商户号:%s, 未找任何附属通道信息",merOrderId,merId),"RXH00013");
        //4-4.获取绑卡的附属通道
        List<ExtraChannelInfo>  extraChannelInfoList_bondCard = new ArrayList<>();
        channelInfoList.forEach(channelInfo->{
            extraChannelInfoList_bondCard.addAll(
                    extraChannelInfoList.stream()
                            .filter(extraChannelInfo->(extraChannelInfo.getOrganizationId().equals(channelInfo.getOrganizationId()))
                                    && (extraChannelInfo.getType().equals(SystemConstant.BONDCARD)))
                            .collect(Collectors.toList())
            );
        });
        isNotElement(extraChannelInfoList_bondCard,format("【快捷收单业务】订单号：%s ,商户号:%s, 未找任何绑卡附属通道信息",merOrderId,merId),"RXH00013");
        //4-5.获取商户持卡信息列表
//        List<MerchantCard> merchantCards = paymentRecordSquareService.getAllMerchantCard(merId, tradeObjectSquare.getTerminalMerId(), SystemConstant.SUCCESS);
        List<MerchantCard> merchantCards=redisCacheCommonCompoment.merchantCardCache.getMore(merId,tradeObjectSquare.getTerminalMerId());
        isNotElement(merchantCards,format("【快捷收单业务】订单号：%s ,商户号:%s,未找到绑卡卡信息",merOrderId,merId),"RXH00013");
        //4-6.获取商户成功绑卡的附属通道
        List<ExtraChannelInfo>  extraChannelInfoList_merchanBondCard = new ArrayList<>();
        merchantCards.forEach( merchantCard->{
            extraChannelInfoList_merchanBondCard.addAll(  extraChannelInfoList_bondCard.stream()
                    .filter( extraChannelInfo->
                            extraChannelInfo.getExtraChannelId().equals(merchantCard.getExtraChannelId()) )
                    .collect(Collectors.toList()) );
        });
        isNull(extraChannelInfoList_merchanBondCard,format("【快捷收单业务】订单号：%s ,商户号:%s, 未找到商户成功绑卡的附属通道",merOrderId,merId),"RXH00013");
        //4-7.确认备用通道，附属通道和通道做交集
        List<ChannelInfo>  ChannelInfo_repeated = new ArrayList<>();
        extraChannelInfoList_merchanBondCard.forEach(extraChannelInfo->{
            ChannelInfo_repeated.addAll(
                    channelInfoList.stream()
                            .filter(channelInfo->(
                                    channelInfo.getOrganizationId().equals(extraChannelInfo.getOrganizationId()))
                            )
                            .distinct()
                            .collect(Collectors.toList())
            );
        });
        isNotElement(ChannelInfo_repeated,format("【快捷收单业务】订单号：%s ,商户号:%s, 没有找到可用通道！",merOrderId,merId),"RXH00013");
        //4-8.执行通道策略，选择最优通道
        ChannelInfo  channelInfo = payOrderOptimalChannelStrategy.strategy(ChannelInfo_repeated,tradeObjectSquare);
        isNull(channelInfo,format("【快捷收单业务】订单号：%s ,商户号:%s, 执行通道策略，没找到可用通道！",merOrderId,merId),"RXH00013");
        //5. 确认附属通道
        ExtraChannelInfo extraChannelInfo= extraChannelInfoList_merchanBondCard.stream()
                .filter(eci->(eci.getOrganizationId().equals(channelInfo.getOrganizationId())))
                .findFirst()
                .orElse(null);
        //5-1.获取进件的附属通道
//        ExtraChannelInfo extraChannelInfo_addCus=new ExtraChannelInfo();
//        extraChannelInfo_addCus.setOrganizationId(extraChannelInfo.getOrganizationId());
//        extraChannelInfo_addCus.setType(SystemConstant.ADDCUS);
//        List<ExtraChannelInfo> extraChannelInfoList_addCus= extraChannelInfoService.select(extraChannelInfo_addCus);
        List<ExtraChannelInfo> extraChannelInfoList_addCus= redisCacheCommonCompoment.extraChannelInfoCache.getAll()
                .stream()
                .filter(t-> t.getType().equals(SystemConstant.ADDCUS))
                .distinct()
                .collect(Collectors.toList());
        //5-2.确认商户进件附属信息
        MerchantRegisterCollect merchantRegisterCollect=null;
        for(MerchantRegisterCollect mrc :merchantRegisterCollects){
            for(ExtraChannelInfo exiacs:extraChannelInfoList_addCus){
                if(mrc.getExtraChannelId().equals(exiacs.getExtraChannelId())) merchantRegisterCollect=mrc;
            }
        }
        isNull(merchantRegisterCollect,format("【快捷收单业务】订单号：%s ,商户号:%s, 未匹配到商户进件附属信息",merOrderId,merId),"RXH00013");
        //6.获取绑卡信息
        MerchantCard merchantCard =merchantCards.stream()
                .filter(t -> t.getExtraChannelId().equals(extraChannelInfo.getExtraChannelId()) && t.getCardNum().equals(tradeObjectSquare.getBankCardNum()))
                .findAny()
                .orElse(null);
        isNull(merchantCard,format("【快捷收单业务】订单号：%s ,商户号:%s, 未匹配到商户绑卡信息",merOrderId,merId),"RXH00013");
        //7. 获取商户费率对象
//        MerchantRate merchantRate = merchantSquareRateCache.getMerchantRateByMerIdAndPayType(merId, channelInfo.getType().toString());
//        MerchantRate merchantRate =redisCacheCommonCompoment.merchantRateCache.getOne(merId, channelInfo.getType().toString());
//        isNull(merchantRate,format("【快捷收单业务】商户号:%s, 商户费率未配置",merId),"RXH00013");
        //8. 生成代付业务订单主表
        PayOrder payOrder =this.generatorPayOrder(systemOrderTrack, tradeObjectSquare, channelInfo, merchantRate, merchantInfo);
        isNull(payOrder,String.format("【快捷收单业务】商户[%s]生成订单信息，发生内部系统异常",merId),"RXH00013");
        systemOrderTrack.setOrderId(payOrder.getPayId());
        //9.生成代收付款人信息表
        PayCardholderInfo payCardholderInfo=this.generatorPayCardholderInfo(tradeObjectSquare,payOrder);
        //10.生成支付产品细节
        PayProductDetail payProductDetail=this.generatorPayProductDetail(tradeObjectSquare,payOrder);
        //11.将数据保存到数据中
        int count= kuaiJiePayService.saveAndUpdateTransPayRecord(payOrder,payCardholderInfo,payProductDetail,null);
        logger.info("【快捷收单业务】订单号：[{}] ,商户号:[{}], 成功插入了[{}] 条数据",merOrderId,merId,count);
        //12.封装参数
        trade.setPayOrder(payOrder);
        trade.setMerchantRegisterInfo(merchantRegisterInfo);
        trade.setMerchantRegisterCollect(merchantRegisterCollect);
        trade.setMerchantCard(merchantCard);
        trade.setExtraChannelInfo(extraChannelInfo);
        trade.setTradeObjectSquare(tradeObjectSquare);
        trade.setChannelInfo(channelInfo);
        trade.setMerchantInfo(merchantInfo);
        return trade;
    }




    /**
     *  生成支付产品细节
     * @param tradeObjectSquare
     * @param payOrder
     * @return
     */
    private PayProductDetail generatorPayProductDetail(TradeObjectSquare tradeObjectSquare, PayOrder payOrder){
        PayProductDetail payProductDetail = new PayProductDetail();
        payProductDetail.setId(UUID.createKey("pay_product_detail", ""));
        payProductDetail.setPayId(payOrder.getPayId());
        payProductDetail.setMerOrderId(payOrder.getMerOrderId());
        payProductDetail.setPrice(payOrder.getAmount());
        payProductDetail.setProductName(tradeObjectSquare.getProductName());
        return payProductDetail;
    }


    /**
     *  生成代收付款人信息表
     * @param tradeObjectSquare
     * @param payOrder
     * @return
     */
    private PayCardholderInfo generatorPayCardholderInfo(TradeObjectSquare tradeObjectSquare, PayOrder payOrder){
        PayCardholderInfo payCardholderInfo = new PayCardholderInfo();
        payCardholderInfo.setPayId(payOrder.getPayId());
        if (tradeObjectSquare.getCardholderName() != null) {
            payCardholderInfo.setCardholderName(tradeObjectSquare.getCardholderName());
        }
        if (tradeObjectSquare.getCardholderPhone() != null) {
            payCardholderInfo.setCardholderPhone(tradeObjectSquare.getCardholderPhone());
        }
        payCardholderInfo.setIdentityType(tradeObjectSquare.getIdentityType());
        payCardholderInfo.setIdentityNum(tradeObjectSquare.getIdentityNum());
        payCardholderInfo.setBankName(tradeObjectSquare.getBankName());
        payCardholderInfo.setBankBranchName(tradeObjectSquare.getBankBranchName());
        payCardholderInfo.setBankBranchNum(tradeObjectSquare.getBankBranchNum());
        if (tradeObjectSquare.getBankCardNum() != null) {
            payCardholderInfo.setBankcardNum(tradeObjectSquare.getBankCardNum());
        }
        payCardholderInfo.setBankcardType(tradeObjectSquare.getBankCardType());
        if (tradeObjectSquare.getValidDate() != null) {
            String validDate = tradeObjectSquare.getValidDate();
            String year = validDate.substring(0, 4);
            String date = validDate.substring(4);
            payCardholderInfo.setExpiryYear(year);
            payCardholderInfo.setExpiryMonth(date);
        }
        if (tradeObjectSquare.getCvv() != null) {
            payCardholderInfo.setCvv(tradeObjectSquare.getCvv());
        }

        return payCardholderInfo;
    }

    /**
     *   生成订单信息
     * @param systemOrderTrack
     * @param tradeObjectSquare
     * @param channelInfo
     * @param merchantRate
     * @param merchantInfo
     * @return
     */
    private PayOrder generatorPayOrder(SystemOrderTrack systemOrderTrack, TradeObjectSquare tradeObjectSquare,
                                       ChannelInfo channelInfo, MerchantRate merchantRate, MerchantInfo merchantInfo){

        PayOrder payOrder = new PayOrder();
        payOrder.setPayId(UUID.createKey("pay_order", ""));
        payOrder.setMerId(tradeObjectSquare.getMerId());
        payOrder.setMerOrderId(tradeObjectSquare.getMerOrderId());
        payOrder.setChannelId(channelInfo.getChannelId());
        payOrder.setOrganizationId(channelInfo.getOrganizationId());
        payOrder.setPayType(String.valueOf(channelInfo.getType()));
        payOrder.setChannelTransCode(channelInfo.getChannelTransCode());
        payOrder.setCurrency(tradeObjectSquare.getCurrency());
        payOrder.setAmount(tradeObjectSquare.getAmount());
        payOrder.setBankCardNum(tradeObjectSquare.getBankCardNum());
        // 平台手续费 = 单笔手续费 + 通道金额 * 手续费率
        BigDecimal hundred = new BigDecimal(100);
        BigDecimal singlefee = merchantRate.getSingleFee() != null ? merchantRate.getSingleFee() : new BigDecimal(0);
        BigDecimal rateFee = merchantRate.getRateFee() != null ? merchantRate.getRateFee().divide(hundred).multiply(tradeObjectSquare.getAmount()).setScale(2,BigDecimal.ROUND_UP) : new BigDecimal(0);
        payOrder.setFee(singlefee.add(rateFee));
        //终端客户手续费
        BigDecimal payFee = tradeObjectSquare.getPayFee() != null ? tradeObjectSquare.getPayFee() : new BigDecimal(0);
        payOrder.setTerminalFee((payFee).divide(hundred).multiply(tradeObjectSquare.getAmount()).setScale(2,BigDecimal.ROUND_UP));
        // 代理商手续费：是否存在上级代理商
        if (StringUtils.isNotEmpty(merchantInfo.getParentId())) {
            payOrder.setAgmentId(merchantInfo.getParentId());
//            AgentMerchantSetting agentMerchantSetting = paymentRecordSquareService.getAgentMerchantSettingByParentId(merchantInfo.getParentId());
            AgentMerchantSetting agentMerchantSetting = paymentRecordSquareService.getAgentMerchantSettingByParentIdAndPayType(merchantInfo.getParentId(),tradeObjectSquare.getBizType());
            Assert.notNull(agentMerchantSetting,String.format("【快捷收单业务】商户[%s]未找到代理商费率信息",tradeObjectSquare.getMerId()));

            // 代理商手续费 =  通道金额 * 手续费率
            BigDecimal agnetSinglefee = agentMerchantSetting.getSingleFee() != null ? agentMerchantSetting.getSingleFee() : new BigDecimal(0);
            BigDecimal agnetRateFee = agentMerchantSetting.getRateFee() != null ? agentMerchantSetting.getRateFee().divide(hundred).multiply(tradeObjectSquare.getAmount().setScale(2,BigDecimal.ROUND_UP)) : new BigDecimal(0);
            payOrder.setAgentFee(agnetSinglefee.add(agnetRateFee));

        } else {
            payOrder.setAgentFee(new BigDecimal(0));
        }
        BigDecimal channelRateFee = channelInfo.getChannelRateFee() == null ? new BigDecimal(0) : channelInfo.getChannelRateFee();
        BigDecimal channelSingleFee = channelInfo.getChannelSingleFee() == null ? new BigDecimal(0) : channelInfo.getChannelSingleFee();
        payOrder.setChannelFee(channelSingleFee.add(channelRateFee.divide(hundred).multiply(tradeObjectSquare.getAmount()).setScale(2,BigDecimal.ROUND_UP)));
        // 平台收入
        payOrder.setIncome(payOrder.getFee().subtract(payOrder.getChannelFee()));
        payOrder.setOrderStatus(SystemConstant.NO_PAY);
        payOrder.setSettleStatus(SystemConstant.NO_SETTLE);
        payOrder.setTradeTime(systemOrderTrack.getTradeTime());
        payOrder.setTerminalMerId(tradeObjectSquare.getTerminalMerId());
        payOrder.setTerminalMerName(tradeObjectSquare.getTerminalMerName());
        payOrder.setRealAmount(tradeObjectSquare.getAmount().subtract(payOrder.getTerminalFee()));
        return payOrder;
    }



    private final  Map<String, ParamRule> allianceGetOrderPayValidate= new HashMap<String, ParamRule>() {
        {
            // 交易数据
            put("bizType", new ParamRule(REQUIRED, 10, 1003));//
            put("charset", new ParamRule(REQUIRED, 10, 1004));//
            put("signType", new ParamRule(REQUIRED, 10, 1004));//
            put("merId", new ParamRule(REQUIRED, 10, 1005));//
            put("merOrderId", new ParamRule(REQUIRED, 32, 1006));//
            put("currency", new ParamRule(REQUIRED, 10, 1006));//
            put("amount", new ParamRule(MONEY, 1007));//
            put("identityType", new ParamRule(REQUIRED, 2, 1014));//
            put("bankCardType", new ParamRule(REQUIRED, 2, 1014));//
            put("identityNum", new ParamRule(REQUIRED, 20, 1014));//
            put("bankCardNum", new ParamRule(REQUIRED, 19, 1013));//
            put("bankCardPhone", new ParamRule(REQUIRED, 19, 1013));//
            put("bankCode", new ParamRule(REQUIRED, 32, 1013));//
            put("validDate", new ParamRule(REQUIRED, 6, 1013));//
            put("securityCode", new ParamRule(REQUIRED, 4, 1013));//
            put("payFee", new ParamRule(MONEY, 1007));//
            put("terminalMerId", new ParamRule(REQUIRED, 32, 1011));//
            put("terminalMerName", new ParamRule(REQUIRED, 32, 1013));//
            put("returnUrl", new ParamRule(OTHER, 128, 1013));//
            put("noticeUrl", new ParamRule(REQUIRED, 128, 1013));//
            put("deviceId", new ParamRule(REQUIRED, 50, 1013));//交易设备号
            put("deviceType", new ParamRule(REQUIRED, 2, 1013));//付款用户设备类型
            put("macAddress", new ParamRule(REQUIRED, 30, 1013));//mac地址
            put("signMsg", new ParamRule(REQUIRED, 256, 1013));//

        }
    };

    public  TradeObjectSquare allianceGetOrderPay(SystemOrderTrack systemOrderTrack) throws PayException {
        TradeObjectSquare tradeObjectSquare = null;
        try {
            tradeObjectSquare = JsonUtils.jsonToPojo(systemOrderTrack.getTradeInfo(), TradeObjectSquare.class);
            List<String> tradeInfoKeys = new ArrayList<>();
            // 报文参数
            Map<String, Object> tradeInfoMap = JsonUtils.jsonToMap(systemOrderTrack.getTradeInfo());
            Set<String> keys = tradeInfoMap.keySet();
            for (String key : keys) {
                validateValue(allianceGetOrderPayValidate,key, tradeInfoMap.get(key).toString());
                tradeInfoKeys.add(key);
            }
            // 校验必要参数
            ValidateItem(allianceGetOrderPayValidate,tradeInfoKeys);
            return tradeObjectSquare;
        } catch (PayException  e) {
            logger.error(systemOrderTrack.getTradeInfo(), e);
            throw new PayException("请求报文无法解析！" + e.getMessage(),"RXH00013");
        }finally {
            if(null != tradeObjectSquare){
                setSystemOrderTrack(systemOrderTrack,tradeObjectSquare);
            }
        }
    }

    private final static Map<String, ParamRule> allianceGetOrderPayConfirmValidate= new HashMap<String, ParamRule>() {
        {
            // 交易数据
            put("bizType", new ParamRule(REQUIRED, 10, 1003));//
            put("charset", new ParamRule(REQUIRED, 10, 1004));//
            put("signType", new ParamRule(REQUIRED, 10, 1004));//
            put("merId", new ParamRule(REQUIRED, 10, 1005));//
            put("merOrderId", new ParamRule(REQUIRED, 32, 1006));//
            put("smsCode", new ParamRule(REQUIRED, 10, 1030));//
            put("terminalMerId", new ParamRule(REQUIRED, 32, 1011));//
            put("terminalMerName", new ParamRule(REQUIRED, 32, 1013));//
            put("returnUrl", new ParamRule(OTHER, 128, 1013));//
            put("noticeUrl", new ParamRule(REQUIRED, 128, 1013));//
            put("payFee", new ParamRule(MONEY, 1008));// 手续费率
            put("signMsg", new ParamRule(REQUIRED, 256, 1013));//
        }
    };

    private final static Map<String, ParamRule> alliancePayConfirmValidate= new HashMap<String, ParamRule>() {
        {
            // 交易数据
            put("bizType", new ParamRule(REQUIRED, 10, 1003));//
            put("charset", new ParamRule(REQUIRED, 10, 1004));//
            put("signType", new ParamRule(REQUIRED, 10, 1004));//
            put("merId", new ParamRule(REQUIRED, 10, 1005));//
            put("merOrderId", new ParamRule(REQUIRED, 32, 1006));//
            put("terminalMerId", new ParamRule(REQUIRED, 32, 1011));//
            put("terminalMerName", new ParamRule(REQUIRED, 32, 1013));//
            put("returnUrl", new ParamRule(OTHER, 128, 1013));//
            put("noticeUrl", new ParamRule(REQUIRED, 128, 1013));//
            put("payFee", new ParamRule(MONEY, 1008));// 手续费率
            put("signMsg", new ParamRule(REQUIRED, 256, 1013));//
        }
    };

    public  TradeObjectSquare alliancePayConfirmPay(SystemOrderTrack systemOrderTrack) throws PayException {
        TradeObjectSquare tradeObjectSquare = null;
        try {
            tradeObjectSquare = JsonUtils.jsonToPojo(systemOrderTrack.getTradeInfo(), TradeObjectSquare.class);
            List<String> tradeInfoKeys = new ArrayList<>();
            // 报文参数
            Map<String, Object> tradeInfoMap = JsonUtils.jsonToMap(systemOrderTrack.getTradeInfo());
            Set<String> keys = tradeInfoMap.keySet();
            String value = "";
            for (String key : keys) {
                if (tradeInfoMap.get(key) != null){
                    value = tradeInfoMap.get(key).toString();
                }
                validateValue(allianceGetOrderPayConfirmValidate,key, value);
                tradeInfoKeys.add(key);
            }
            // 校验必要参数
            ValidateItem(allianceGetOrderPayConfirmValidate,tradeInfoKeys);
            return tradeObjectSquare;
        } catch (PayException  e) {
            logger.error(systemOrderTrack.getTradeInfo(), e);
            throw new PayException("请求报文无法解析！" + e.getMessage(), 1001);
        }finally {
            if(null != tradeObjectSquare){
                setSystemOrderTrack(systemOrderTrack,tradeObjectSquare);
            }
        }
    }


    /**
     * 免短信支付
     * @param systemOrderTrack
     * @return
     * @throws PayException
     */
    public  TradeObjectSquare alliancePayConfirmFeePay(SystemOrderTrack systemOrderTrack) throws PayException {
        TradeObjectSquare tradeObjectSquare = null;
        try {
            tradeObjectSquare = JsonUtils.jsonToPojo(systemOrderTrack.getTradeInfo(), TradeObjectSquare.class);
            List<String> tradeInfoKeys = new ArrayList<>();
            // 报文参数
            Map<String, Object> tradeInfoMap = JsonUtils.jsonToMap(systemOrderTrack.getTradeInfo());
            Set<String> keys = tradeInfoMap.keySet();
            for (String key : keys) {
                validateValue(alliancePayConfirmValidate,key, tradeInfoMap.get(key).toString());
                tradeInfoKeys.add(key);
            }
            // 校验必要参数
            ValidateItem(alliancePayConfirmValidate,tradeInfoKeys);
            return tradeObjectSquare;
        } catch (PayException  e) {
            logger.error(systemOrderTrack.getTradeInfo(), e);
            throw new PayException("请求报文无法解析！" + e.getMessage(), 1001);
        }finally {
            if(null != tradeObjectSquare){
                setSystemOrderTrack(systemOrderTrack,tradeObjectSquare);
            }
        }
    }

}
