package com.rxh.service.oldKuaijie;

import com.rxh.cache.ehcache.MerchantSquareRateCache;
import com.rxh.cache.ehcache.MerchantSquareSettingCache;
import com.rxh.cache.ehcache.RiskQuotaDataCache;
import com.rxh.cache.ehcache.SysConstantCache;
import com.rxh.exception.PayException;
import com.rxh.pojo.Result;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.pojo.payment.TradeObjectSquare;
import com.rxh.pojo.sys.SysConstant;
import com.rxh.service.square.PaymentRecordSquareService;
import com.rxh.square.pojo.*;
import com.rxh.utils.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class SweepCodePaymentService {
    @Autowired
    private PaymentRecordSquareService paymentRecordSquareService;

    private final static Logger logger = LoggerFactory.getLogger(SweepCodePaymentService.class);
    private  final RecordPaymentSquareService recordPaymentSquareService;
    private final MerchantSquareRateCache merchantSquareRateCache;
    private final MerchantSquareSettingCache merchantSquareSettingCache;
    private final GetOrderWalletService getOrderWalletService;
    private final RiskQuotaDataCache riskQuotaDataCache;
    private final SysConstantCache sysConstantCache;
    private final SweepCodeNotifyService sweepCodeNotifyService;
    @Autowired
    public SweepCodePaymentService(RecordPaymentSquareService recordPaymentSquareService, MerchantSquareRateCache merchantSquareRateCache, MerchantSquareSettingCache merchantSquareSettingCache, GetOrderWalletService getOrderWalletService, RiskQuotaDataCache riskQuotaDataCache, SysConstantCache sysConstantCache, SweepCodeNotifyService sweepCodeNotifyService) {
        this.recordPaymentSquareService = recordPaymentSquareService;
        this.merchantSquareRateCache = merchantSquareRateCache;
        this.merchantSquareSettingCache = merchantSquareSettingCache;
        this.getOrderWalletService = getOrderWalletService;
        this.riskQuotaDataCache = riskQuotaDataCache;
        this.sysConstantCache = sysConstantCache;
        this.sweepCodeNotifyService = sweepCodeNotifyService;
    }






    public String payment(SystemOrderTrack systemOrderTrack, TradeObjectSquare tradeObjectSquare) throws PayException {
        SquareTrade trade = getTrade(systemOrderTrack, tradeObjectSquare);
        BankResult result= toPay(trade);
        afterPay(trade,result);
        String returnJson=getReturnJson(trade,result);

        return returnJson;
    }

    public void afterPay(SquareTrade trade, BankResult result) throws PayException {
        MerchantInfo merchantInfo = trade.getMerchantInfo();
        PayOrder payOrder = trade.getPayOrder();
        ChannelInfo channelInfo = trade.getChannelInfo();
        MerchantQuotaRisk merchantQuotaRisk = recordPaymentSquareService.getMerchantQuotaRiskByMerId(payOrder.getMerId());
        if(!Objects.equals(payOrder.getOrderStatus(), Integer.valueOf(result.getStatus())) &&result.getStatus()==0){
            getOrderWalletService.updateWallet(trade);
            this.updateRiskQuotaData(payOrder,channelInfo,merchantQuotaRisk);
        }
        payOrder.setOrderStatus(Integer.valueOf(result.getStatus()));
        recordPaymentSquareService.UpdatePayOrder(payOrder);


    }


    private String getReturnJson(SquareTrade trade, BankResult result) {

        PayOrder payOrder = trade.getPayOrder();
        MerchantInfo merchantInfo = trade.getMerchantInfo();
        String merId = merchantInfo.getMerId();
        String merOrderId = trade.getMerOrderId();
        String currency = payOrder.getCurrency();
        String amount = payOrder.getAmount().toString();
        String transId = payOrder.getPayId();
        Short status = result.getStatus();
        String secretKey = merchantInfo.getSecretKey();
        String md5Str=merId+merOrderId+currency+amount+transId+status+secretKey;
        md5Str=DigestUtils.md5Hex(md5Str);
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("MerId",merId);
        resultMap.put("MerOrderId",merOrderId);
        if(trade.getAuthCode()!=null){
            resultMap.put("AuthCode;",trade.getAuthCode());
        }
        resultMap.put("TradeTime;",payOrder.getTradeTime());
        resultMap.put("Currency;",currency);
        resultMap.put("Amount;",amount);
        resultMap.put("OrderId;",transId);
        resultMap.put("Status;",status);
        resultMap.put("Msg;",result.getBankResult());
        resultMap.put("SignMsg;",md5Str);
        String resultjson = JsonUtils.objectToJson(resultMap);


        return resultjson;
    }

    public BankResult toPay(SquareTrade trade)  throws PayException{
        logger.info(JsonUtils.objectToJsonNonNull(trade));
        // 发送请求至接口
//        String result = HttpClientUtils.doPostJson(HttpClientUtils.getHttpClient(), trade.getChannelInfo().getPayUrl()+"trade", JsonUtils.objectToJsonNonNull(trade));
        String result = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), "http://localhost:8200/KuaiQianPay/trade", JsonUtils.objectToJsonNonNull(trade));
//        String result = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), "http://boc.zrtcnet.com/cross/KuaiQianPay/trade", JsonUtils.objectToJsonNonNull(trade));
        // 处理接口返回信息
        if (StringUtils.isBlank(result)) {
            throw new PayException("Cross请求发生错误！URL：" + trade.getChannelInfo().getPayUrl(), 4001);
        }
        BankResult bankResult = JsonUtils.jsonToPojo(result, BankResult.class);
        if (bankResult != null) {
            return bankResult;
        } else {
            throw new PayException("BankResultJson转BankResult异常", 6000);
        }
    }


    public SquareTrade getTrade (SystemOrderTrack systemOrderTrack, TradeObjectSquare tradeObjectSquare) throws PayException{

        List<PayOrder> item= recordPaymentSquareService.getPayOrderByMerOrderId(tradeObjectSquare, systemOrderTrack.getMerOrderId());
        if(item.size()>0){
            throw new PayException("该笔订单已存在",3003);
        }
        // 查询商户信息
        MerchantInfo merchantInfo = recordPaymentSquareService.getMerchantInfoByMerId(tradeObjectSquare.getMerId());
//        checkMd5(tradeObjectSquare,merchantInfo.getSecretKey());
        // 获取商户配置
        MerchantSetting merchantSetting = merchantSquareSettingCache.getMerchantSettingByMerId(tradeObjectSquare.getMerId());
        // 获取商户通道并进行栓选
        ChannelInfo channelInfo = getAndDressChannelInfo(merchantInfo,merchantSetting,tradeObjectSquare);
        // 获取商户费率对象
        MerchantRate merchantRate = merchantSquareRateCache.getMerchantRateByMerIdAndPayType(merchantInfo.getMerId(),channelInfo.getType().toString());
        // 保存代付业务订单主表
        PayOrder payOrder = recordPaymentSquareService.saveOrUpadatePayOrder(systemOrderTrack,tradeObjectSquare,channelInfo,merchantRate,merchantInfo);
        // 保存结算业务收款账号信息
        recordPaymentSquareService.saveOrUpadateCardHolderInfo(tradeObjectSquare,payOrder);
        recordPaymentSquareService.saveOrUpadatePayProductDetail(tradeObjectSquare,payOrder);
        // 商户风控对象
        MerchantQuotaRisk merchantQuotaRisk = recordPaymentSquareService.getMerchantQuotaRiskByMerId(tradeObjectSquare.getMerId());
        analysis(channelInfo,payOrder,merchantQuotaRisk);

        SquareTrade trade=new SquareTrade();
        if (tradeObjectSquare.getBankCardType()==2){
            SysConstant bank= sysConstantCache.getBankCodeByBankName(tradeObjectSquare.getBankName());
            trade.setBankCode(bank.getFirstValue());
            trade.setBankCvv(tradeObjectSquare.getCvv());
//            trade.setBankYxq(tradeObjectSquare.gety);
        }
        //封装交易参数
        trade.setTradeObjectSquare(tradeObjectSquare);
        trade.setPayOrder(payOrder);
        trade.setChannelInfo(channelInfo);
        trade.setMerOrderId(tradeObjectSquare.getMerOrderId());
        trade.setMerchantInfo(merchantInfo);
        trade.setAuthCode(tradeObjectSquare.getAuthCode());
        return trade;
    }



    private void quotaTodoSquare(PayOrder payOrder, List<RiskQuotaData> quotaDataList,ChannelInfo channelInfo,MerchantQuotaRisk merchantQuotaRisk) throws  PayException{
        // 当前交易人民币金额
        BigDecimal rmbAmount = payOrder.getAmount()!= null ? payOrder.getAmount():new BigDecimal(0);
        // 限定日最小额度
        BigDecimal singleMinAmount = channelInfo.getSingleMinAmount()!= null ? channelInfo.getSingleMinAmount():new BigDecimal(0);
        // 限定日最大额度
        BigDecimal getSingleMaxAmount = channelInfo.getSingleMaxAmount() != null? channelInfo.getSingleMaxAmount():new BigDecimal(0);
        if (isExceed(singleMinAmount, rmbAmount)) {
            throw new PayException("不足通道单笔最小限额：" + singleMinAmount + "，当前交易人民币金额：" + rmbAmount, 3004);
        }
        if (isExceed(rmbAmount, getSingleMaxAmount)) {
            throw new PayException("通道超出单笔最大限额：" + getSingleMaxAmount + "，当前交易人民币金额：" + rmbAmount, 3005);
        }
        // 限定日最小额度
        BigDecimal singleQuotaAmount = merchantQuotaRisk.getSingleQuotaAmount()!= null ? merchantQuotaRisk.getSingleQuotaAmount(): new BigDecimal(0);
        if (isExceed(rmbAmount, singleQuotaAmount)) {
            throw new PayException("商户超出单笔最大限额：" + singleQuotaAmount + "，当前交易人民币金额：" + rmbAmount, 3005);
        }
        if (quotaDataList == null) {
            return;
        }

        // 单笔额度
        for (RiskQuotaData riskQuotaData : quotaDataList){
            // 通道
            if(riskQuotaData.getRefType() == RiskQuotaData.CHANNEL_REF_TYPE && riskQuotaData.getType() != RiskQuotaData.LIMIT_TYPE_ORDER_AMOUNT){
                switch (riskQuotaData.getType()) {
                    case 1:
                        if(isExceed(riskQuotaData.getAmount()!=null?riskQuotaData.getAmount().add(rmbAmount):rmbAmount, merchantQuotaRisk.getDayQuotaAmount()!= null ? merchantQuotaRisk.getDayQuotaAmount(): new BigDecimal(0))) {
                            // 超出限定额度的值
                            BigDecimal excessAmount = riskQuotaData.getAmount().add(rmbAmount).subtract(channelInfo.getDayQuotaAmount());
                            throw new PayException("通道每日最大限额：" + channelInfo.getDayQuotaAmount() + "，超出：" + excessAmount, 3006);
                        }
                    case 2:
                        if(isExceed(riskQuotaData.getAmount() !=null?riskQuotaData.getAmount().add(rmbAmount):rmbAmount, merchantQuotaRisk.getDayQuotaAmount() != null ? merchantQuotaRisk.getDayQuotaAmount(): new BigDecimal(0))) {
                            // 超出限定额度的值
                            BigDecimal excessAmount = riskQuotaData.getAmount().add(rmbAmount).subtract(channelInfo.getMonthQuotaAmount());
                            throw new PayException("通道每月最大限额：" + channelInfo.getMonthQuotaAmount()+ "，超出：" + excessAmount, 3007);
                        }
                    default:
                        break;
                }
            }
            // 商户
            if(riskQuotaData.getRefType() == RiskQuotaData.MERCHANT_REF_TYPE && riskQuotaData.getType() != RiskQuotaData.LIMIT_TYPE_ORDER_AMOUNT){
                switch (riskQuotaData.getType()) {
                    case 1:
                        if(isExceed(riskQuotaData.getAmount()!=null?riskQuotaData.getAmount().add(rmbAmount):rmbAmount, merchantQuotaRisk.getDayQuotaAmount()!= null ? merchantQuotaRisk.getDayQuotaAmount(): new BigDecimal(0))){
                            // 超出限定额度的值
                            BigDecimal excessAmount = riskQuotaData.getAmount().add(rmbAmount).subtract(merchantQuotaRisk.getDayQuotaAmount());
                            throw new PayException("商户每日最大限额：" + merchantQuotaRisk.getDayQuotaAmount() + "，超出：" + excessAmount, 3006);
                        }
                    case 2:
                        if(isExceed(riskQuotaData.getAmount()!=null?riskQuotaData.getAmount().add(rmbAmount):rmbAmount, merchantQuotaRisk.getMonthQuotaAmount() != null ? merchantQuotaRisk.getMonthQuotaAmount(): new BigDecimal(0))){
                            // 超出限定额度的值
                            BigDecimal excessAmount = riskQuotaData.getAmount().add(rmbAmount).subtract(merchantQuotaRisk.getMonthQuotaAmount());
                            throw new PayException("商户每月最大限额：" + merchantQuotaRisk.getMonthQuotaAmount()+ "，超出：" + excessAmount, 3007);
                        }
                    default:
                        break;
                }
            }
        }
    }

    public void analysis(ChannelInfo channelInfo,PayOrder payOrder,MerchantQuotaRisk merchantQuotaRisk) throws PayException {
        List<RiskQuotaData> quotaDataList = getRiskQuotaData(channelInfo, payOrder);
        insertRiskQuotaDataNew(quotaDataList,channelInfo,merchantQuotaRisk);
        quotaTodoSquare(payOrder,quotaDataList,channelInfo,merchantQuotaRisk);
    }
    private void initRisk(ChannelInfo channelInfo, MerchantQuotaRisk merchantQuotaRisk, List<String> noriskList, List<RiskQuotaData> riskQuotaDataList) {
        for (String ss : noriskList){
            String[] ssArray = ss.split("-");
            Short refType = Short.parseShort(ssArray[0]);
            Short type = Short.parseShort(ssArray[1]);
            RiskQuotaData riskQuotaData = new RiskQuotaData();
            riskQuotaData.setAmount(new BigDecimal(0));
            riskQuotaData.setType(type);
            if (type == 1){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String day = sdf.format(new Date());
                riskQuotaData.setTradeTime(day);
            }else if (type == 2){
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
                String month = sdf1.format(new Date());
                riskQuotaData.setTradeTime(month);
            }else  {
                break;
            }
            switch (refType){
                case 1: // 通道
                    riskQuotaData.setRefId(channelInfo.getChannelId());
                    riskQuotaData.setRefType(refType);
                    riskQuotaDataList.add(riskQuotaData);
                    break;
                case 2: // 商户
                    riskQuotaData.setRefId(merchantQuotaRisk.getMerId());
                    riskQuotaData.setRefType(refType);
                    riskQuotaDataList.add(riskQuotaData);
                    break;
                default:
                    break;
            }
        }
    }

    List<RiskQuotaData> getRiskQuotaData(ChannelInfo channelInfo, PayOrder payOrder) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String day = sdf.format(new Date());
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
        String month = sdf1.format(new Date());
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy");
        String year = sdf2.format(new Date());
        return riskQuotaDataCache.getQuotaData1(payOrder.getMerId(),channelInfo.getChannelId(),day,month,year);
    }




    // private int insertRiskQuotaDataNew(List<RiskQuotaData> quotaDataList, ChannelInfo channelInfo, MerchantQuotaRisk merchantQuotaRisk) {
    //     List<String> typeList = new ArrayList<>();
    //     typeList.add("1-1");
    //     typeList.add("1-2");
    //     // typeList.add("1-4");
    //     typeList.add("2-1");
    //     typeList.add("2-2");
    //     // typeList.add("2-4");
    //     if (quotaDataList == null){
    //         List<RiskQuotaData> riskQuotaDataList = new ArrayList<>();
    //         initRisk(channelInfo, merchantQuotaRisk, typeList, riskQuotaDataList);
    //         insertRiskQuotaData(riskQuotaDataList);
    //         return 1;
    //     }
    //     // 不为空
    //     List<String> riskList = new ArrayList<>();
    //     for (RiskQuotaData riskQuotaData : quotaDataList){
    //         riskList.add(riskQuotaData.getRefType()+"-"+riskQuotaData.getType());
    //     }
    //     List<String> noriskList = new ArrayList<>();
    //     for (String str : typeList){
    //         if (!riskList.contains(str)){
    //             noriskList.add(str);
    //         }
    //     }
    //     if (noriskList.size()>0){
    //         List<RiskQuotaData> riskQuotaDataList = new ArrayList<>();
    //         initRisk(channelInfo, merchantQuotaRisk, noriskList, riskQuotaDataList);
    //         insertRiskQuotaData(riskQuotaDataList);
    //         return 1;
    //     }
    //     return 1;
    // }

    private int insertRiskQuotaDataNew(List<RiskQuotaData> quotaDataList, ChannelInfo channelInfo, MerchantQuotaRisk merchantQuotaRisk) {
        List<String> typeList = new ArrayList<>();
        typeList.add("1-1");
        typeList.add("1-2");
        // typeList.add("1-4");
        typeList.add("2-1");
        typeList.add("2-2");
        // typeList.add("2-4");
        if (quotaDataList.size()<=0 || quotaDataList == null){
            List<RiskQuotaData> riskQuotaDataList = new ArrayList<>();
            initRisk(channelInfo, merchantQuotaRisk, typeList, riskQuotaDataList);
            insertRiskQuotaData(riskQuotaDataList);
            return 1;
        }
        // 不为空
        List<String> riskList = new ArrayList<>();
        for (RiskQuotaData riskQuotaData : quotaDataList){
            riskList.add(riskQuotaData.getRefType()+"-"+riskQuotaData.getType());
        }
        List<String> noriskList = new ArrayList<>();
        for (String str : typeList){
            if (!riskList.contains(str)){
                noriskList.add(str);
            }
        }
        if (noriskList.size()>0){
            List<RiskQuotaData> riskQuotaDataList = new ArrayList<>();
            initRisk(channelInfo, merchantQuotaRisk, noriskList, riskQuotaDataList);
            insertRiskQuotaData(riskQuotaDataList);
            return 1;
        }
        return 1;
    }

    public Integer insertRiskQuotaData(List<RiskQuotaData> quotaDataList) {
        return paymentRecordSquareService.insertOrUpdateRiskQuotaData(quotaDataList);
    }


    private boolean isExceed(BigDecimal amount, BigDecimal target) {
        return amount.compareTo(target) > 0;
    }

    private  void checkMd5(TradeObjectSquare tradeObjectSquare, String secretKey) throws PayException {
        String str =
                tradeObjectSquare.getBizType() +
                        tradeObjectSquare.getMerId() +
                        tradeObjectSquare.getMerOrderId() +
                        tradeObjectSquare.getCurrency() +
                        tradeObjectSquare.getAmount() +
                        secretKey;
        if (!StringUtils.equalsAnyIgnoreCase(tradeObjectSquare.getSignMsg(), DigestUtils.md5Hex(str).toUpperCase())) {
            throw new PayException("SignMsg校验错误！", 1300);
        }
    }
    private List<String> getAllPayType(MerchantInfo merchantInfo) throws PayException {
        // 获取所有的支付方式
        List<MerchantRate> merchantRates = merchantSquareRateCache.getByMerId(merchantInfo.getMerId());
        List<String> strings = new ArrayList<>();
        for (MerchantRate merchantRate : merchantRates){
            strings.add(merchantRate.getPayType());
        }
        return  strings;
    }

    /**
     * 优先级：通道>机构
     * @param merchantInfo
     * @param merchantSetting
     * @return
     * @throws PayException
     */
    private ChannelInfo getAndDressChannelInfo(MerchantInfo merchantInfo, MerchantSetting merchantSetting, TradeObjectSquare tradeObjectSquare) throws PayException{
        // 第一步： 获取用户所有的支付方式所对应的通道
//        List<String> channelIds = getAllPayType(merchantInfo);
        String organizationId = merchantSetting.getOrganizationId();
        String channelId = merchantSetting.getChannelId();
        String[] ogIds =organizationId==null?null: organizationId.split(",");
        String[] chIds = channelId==null?null:channelId.split(",");



        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("payType",tradeObjectSquare.getBizType());
        paramMap.put("ogIds",ogIds==null?null:Arrays.asList(ogIds));
        paramMap.put("chIds",chIds==null?null:Arrays.asList(chIds));

        List<ChannelInfo> channelInfos = recordPaymentSquareService.getChannelByPayType(paramMap);


        //第二步：
        if(channelInfos.size()>0){
            //不为空，直接取第一个
            for (ChannelInfo channelInfo : channelInfos){

                return channelInfo;
            }
        }else {
            throw new PayException("商户没有对应的通道！",2005);
        }

        return  null;
    }

    public Result updateOrderAndDoNotify(BankResult bankResult) throws PayException {
            Long orderId = bankResult.getOrderId();
            PayOrder payOrder= recordPaymentSquareService.getPayOrderById(orderId.toString());
            ChannelInfo channelInfo = recordPaymentSquareService.getChannelInfo(payOrder.getChannelId().toString());
            MerchantQuotaRisk merchantQuotaRisk = recordPaymentSquareService.getMerchantQuotaRiskByMerId(payOrder.getMerId());
            MerchantInfo merchantInfo = recordPaymentSquareService.getMerchantInfoByMerId(payOrder.getMerId());
            SystemOrderTrack systemOrderTrack= recordPaymentSquareService.getSystemOrderTrack(payOrder.getMerOrderId());
            payOrder.setOrderStatus(Integer.valueOf(bankResult.getStatus()));
            recordPaymentSquareService.UpdatePayOrder(payOrder);
        if(!Objects.equals(payOrder.getOrderStatus(), Integer.valueOf(bankResult.getStatus()))&&bankResult.getStatus()==0){
            getOrderWalletService.notifyUpdateWallet(bankResult);
            updateRiskQuotaData(payOrder,channelInfo,merchantQuotaRisk);
        }
        payOrder.setOrderStatus(Integer.valueOf(bankResult.getStatus()));
        recordPaymentSquareService.UpdatePayOrder(payOrder);
      /*  PayOrder payOrder = new PayOrder();
        MerchantInfo merchantInfo = new MerchantInfo();
        SystemOrderTrack systemOrderTrack = new SystemOrderTrack();*/

        sweepCodeNotifyService.sendMerchantNotify(payOrder,merchantInfo,bankResult,systemOrderTrack);
        return new Result(Result.SUCCESS, "订单更新完成！");
    }



    /**
     * 获取风控前置表数据
     * @param req
     * @param tradeInfo
     * @return
     */
    public SystemOrderTrack getSystemOrderTrack(HttpServletRequest req, String tradeInfo, SystemOrderTrack systemOrderTrack) throws PayException {
//        if (req.getRemotePort() != 80) {
//            logger.error("请求端口非法：" + req.getServerPort());
//            throw new PayException("请求端口非80端口！当前端口：" + req.getServerPort(), 1100);
//        }
        String reqUrl = req.getHeader(HttpHeaders.REFERER) == null ?
                req.getRequestURL().toString() :
                req.getHeader(HttpHeaders.REFERER);
        if (StringUtils.isBlank(tradeInfo)) {
            logger.error("提交参数为空！");
            logger.info("来源站点：" + reqUrl + "，IP：" + IpUtils.getReallyIpForRequest(req));
            throw new PayException("提交参数为空！", 1000);
        }
//        String tradeInfoDecode = new String(Base64.getDecoder().decode(tradeInfo.getBytes()));
//        try {
//            tradeInfo = URLDecoder.decode(tradeInfoDecode, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            throw new PayException("提交参数无法解析", 1001);
//        }
        systemOrderTrack.setTradeInfo(tradeInfo);
        systemOrderTrack.setRefer(reqUrl);
        systemOrderTrack.setTradeTime(new Date());
        return systemOrderTrack;
    }


    public Result updateNoCardPayAndDoNotify(BankResult bankResult) throws PayException {

        Long orderId = bankResult.getOrderId();
        PayOrder payOrder= recordPaymentSquareService.getPayOrderById(orderId.toString());
        ChannelInfo channelInfo = recordPaymentSquareService.getChannelInfo(payOrder.getChannelId().toString());
        MerchantInfo merchantInfo = recordPaymentSquareService.getMerchantInfoByMerId(payOrder.getMerId());
        MerchantQuotaRisk merchantQuotaRisk = recordPaymentSquareService.getMerchantQuotaRiskByMerId(payOrder.getMerId());
        SystemOrderTrack systemOrderTrack= recordPaymentSquareService.getSystemOrderTrack(payOrder.getMerOrderId());
        if(!Objects.equals(payOrder.getOrderStatus(), Integer.valueOf(bankResult.getStatus()))&&bankResult.getStatus()==0){
            getOrderWalletService.updateWallet(bankResult.getTrade());
            updateRiskQuotaData(payOrder,channelInfo,merchantQuotaRisk);
        }
        payOrder.setOrderStatus(Integer.valueOf(bankResult.getStatus()));
        recordPaymentSquareService.UpdatePayOrder(payOrder);
      /*  PayOrder payOrder = new PayOrder();
        MerchantInfo merchantInfo = new MerchantInfo();
        SystemOrderTrack systemOrderTrack = new SystemOrderTrack();*/

        sweepCodeNotifyService.sendNoCardPayNotify(payOrder,merchantInfo,bankResult,systemOrderTrack);
        return new Result(Result.SUCCESS, "订单更新完成！");
    }
    public void updateRiskQuotaData(PayOrder payOrder, ChannelInfo channelInfo,MerchantQuotaRisk merchantQuotaRisk) throws  PayException{
        List<RiskQuotaData> quotaDataList = getRiskQuotaData(channelInfo, payOrder);
        // quotaTodoSquare(payOrder,quotaDataList,channelInfo,merchantQuotaRisk);
        quotaDataList.forEach(riskQuotaData -> {
            // if (StringUtils.equals(riskQuotaData.getType().toString(), payOrder.getPayType())) {
                riskQuotaData.setAmount(riskQuotaData.getAmount() == null ? payOrder.getAmount() : riskQuotaData.getAmount().add(payOrder.getAmount()));
            // }
        });
        insertRiskQuotaData(quotaDataList);
    }


}


