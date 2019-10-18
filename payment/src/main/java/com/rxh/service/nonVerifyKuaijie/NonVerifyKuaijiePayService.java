package com.rxh.service.nonVerifyKuaijie;

import com.rxh.pojo.cross.BankResult;
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

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class NonVerifyKuaijiePayService extends AbstractCommonService {


    public  Map<String, ParamRule> creditCardPayValidate= new HashMap<String, ParamRule>() {
        {
            // 交易数据
            put("charset", new ParamRule(REQUIRED, 10, 1004));//
            put("signType", new ParamRule(REQUIRED, 10, 1004));//
            put("merId", new ParamRule(REQUIRED, 10, 1005));//
            put("amount", new ParamRule(REQUIRED, 12, 1013));//
            put("merOrderId", new ParamRule(REQUIRED, 32, 1006));//
            put("identityNum", new ParamRule(REQUIRED, 18, 1030));//
            put("terminalMerId", new ParamRule(REQUIRED, 32, 1011));//
            put("terminalMerName", new ParamRule(REQUIRED, 32, 1013));//
            put("returnUrl", new ParamRule(REQUIRED, 128, 1013));//
            put("noticeUrl", new ParamRule(REQUIRED, 128, 1013));//
            put("payFee", new ParamRule(REQUIRED, 8,1008));// 手续费率
            put("bankCardNum", new ParamRule(REQUIRED, 19,1008));// 消费账号
            put("currency", new ParamRule(REQUIRED, 32, 1008));//
            put("identityType", new ParamRule(REQUIRED, 2, 1013));//
            put("signMsg", new ParamRule(REQUIRED, 256, 1013));//

        }
    };


    /**
     *   保存收单订单
     * @param systemOrderTrack
     * @param tradeObjectSquare
     * @return
     * @throws Exception
     */
    public SquareTrade nonVerifyKuaiJieToSaveOrder(SystemOrderTrack systemOrderTrack, TradeObjectSquare tradeObjectSquare) throws Exception {
        String merId=tradeObjectSquare.getMerId();
        String merOrderId=tradeObjectSquare.getMerOrderId();
        String terminalMerId=tradeObjectSquare.getTerminalMerId();
        //验证签名
        MerchantInfo merchantInfo = redisCacheCommonCompoment.merchantInfoCache.getOne(merId);
        Assert.notNull(merchantInfo,format("【快捷收单业务】订单号：%s ,商户号:%s,未找到商户相关信息",merOrderId,merId));
        CheckMd5Utils.checkMd5(systemOrderTrack.getTradeInfo(),merchantInfo.getSecretKey());
        //1.判断订单号是否已经存在
        PayOrder po=new PayOrder();
        po.setMerId(merId);
        po.setMerOrderId(merOrderId);
        po.setTerminalMerId(terminalMerId);
        List<PayOrder>  payOrderList=paymentRecordSquareService.getPayOrderByWhereCondition(po);
        isHasElement(payOrderList,format("【快捷收单业务】订单号：%s ,商户号:%s, 订单号已经存在",merOrderId,merId));
        //0.判断是否有交易记录，如何不做任何判断
        SquareTrade trade = new SquareTrade();
        MerchantChannelHistory wasUseChannel = recordPaymentSquareService.getLastUseChannel(tradeObjectSquare);
        trade.setMerchantChannelHistory(wasUseChannel);

        //1.执行商户风险控制
        MerchantQuotaRisk merchantQuotaRisk =  redisCacheCommonCompoment.merchantQuotaRiskCache.getOne(merId);
//        MerchantQuotaRisk merchantQuotaRisk = paymentRecordSquareService.getMerchantQuotaRiskByMerId(merId);
        isNull(merchantQuotaRisk,format("【快捷收单业务】订单号：%s ,商户号:%s, 未找到风控对象",merOrderId,merId));
        payOrderOptimalChannelStrategy.riskManagement(merchantQuotaRisk,tradeObjectSquare);
        //2-1获取商户配置
//        MerchantSetting merchantSetting = merchantSettingService.getMerchantSettingByMerId(merId);
        MerchantSetting merchantSetting =redisCacheCommonCompoment.merchantSettingCache.getOne(merId);
        isNull(merchantSetting,format("【快捷收单业务】订单号：%s ,商户号:%s,未进行商户通道配置！",merOrderId,merId));
        //2-2.获取商户所支持的通道
        List<ChannelInfo> channelInfoList=this.getMerchantSurpportChannel(merchantSetting,tradeObjectSquare);
        isNotElement(channelInfoList,format("【快捷收单业务】订单号：%s ,商户号:%s, 未找到风控对象",merOrderId,merId));
        //2-3.获取附属通道信息，通道类型为bizType 且 通道的组织id=附属通道的组织id
//        List<ExtraChannelInfo> extraChannelInfoList = paymentRecordSquareService.getChannelInfosByPayType(tradeObjectSquare);
        List<ExtraChannelInfo> extraChannelInfoList = redisCacheCommonCompoment.extraChannelInfoCache.getAll()
                .stream()
                .filter(t->{
                    if(null != tradeObjectSquare.getBizType()){
                        if(t.getType().equals("1")) return true;
                        else return false;
                    }
                    return true;
                }).distinct()
                .collect(Collectors.toList());

        isNotElement(extraChannelInfoList,format("【快捷收单业务】订单号：%s ,商户号:%s, 未找任何附属通道信息",merOrderId,merId));
        //2-4.获取绑卡的附属通道
        List<ExtraChannelInfo>  extraChannelInfoList_bondCard = new ArrayList<>();
        channelInfoList.forEach(channelInfo->{
            extraChannelInfoList_bondCard.addAll(
                    extraChannelInfoList.stream()
                            .filter(extraChannelInfo->(extraChannelInfo.getOrganizationId().equals(channelInfo.getOrganizationId()))
                                    && (extraChannelInfo.getType().equals(SystemConstant.BONDCARD)))
                            .collect(Collectors.toList())
            );
        });
        isNotElement(extraChannelInfoList_bondCard,format("【快捷收单业务】订单号：%s ,商户号:%s, 未找任何绑卡附属通道信息",merOrderId,merId));
        //4-5.获取商户持卡信息列表
//        List<MerchantCard> merchantCards = paymentRecordSquareService.getAllMerchantCard(merId, tradeObjectSquare.getTerminalMerId(), SystemConstant.SUCCESS);
        List<MerchantCard> merchantCards =redisCacheCommonCompoment.merchantCardCache.getMore(merId,tradeObjectSquare.getTerminalMerId());

        isNotElement(merchantCards,format("【快捷收单业务】订单号：%s ,商户号:%s,未找到绑卡卡信息",merOrderId,merId));
        //4-6.获取商户成功绑卡的附属通道
        List<ExtraChannelInfo>  extraChannelInfoList_merchanBondCard = new ArrayList<>();
        merchantCards.forEach( merchantCard->{
            extraChannelInfoList_merchanBondCard.addAll(  extraChannelInfoList_bondCard.stream()
                    .filter( extraChannelInfo->
                            extraChannelInfo.getExtraChannelId().equals(merchantCard.getExtraChannelId()) )
                    .collect(Collectors.toList()) );
        });
        isNull(extraChannelInfoList_merchanBondCard,format("【快捷收单业务】订单号：%s ,商户号:%s, 未找商户成功绑卡的附属通道",merOrderId,merId));
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
        isNull(ChannelInfo_repeated,format("【快捷收单业务】订单号：%s ,商户号:%s, 没有找到可用通道！",merOrderId,merId));
        //4-8.执行通道策略，选择最优通道
        ChannelInfo  channelInfo = payOrderOptimalChannelStrategy.strategy(ChannelInfo_repeated,tradeObjectSquare);
        isNull(channelInfo,format("【快捷收单业务】订单号：%s ,商户号:%s, 执行通道策略，没找到可用通道！",merOrderId,merId));
        //5. 确认附属通道
        ExtraChannelInfo extraChannelInfo= extraChannelInfoList_merchanBondCard.stream()
                .filter(eci->(eci.getOrganizationId().equals(channelInfo.getOrganizationId())))
                .findFirst()
                .orElse(null);

        //6.获取绑卡信息
        MerchantCard merchantCard = recordPaymentSquareService.chooseMerchantCard(extraChannelInfo, tradeObjectSquare, merchantCards);
        //7. 获取商户费率对象
//        MerchantRate merchantRate = merchantSquareRateCache.getMerchantRateByMerIdAndPayType(merId, channelInfo.getType().toString());
        MerchantRate merchantRate =redisCacheCommonCompoment.merchantRateCache.getOne(merId, channelInfo.getType().toString());
        isNull(merchantRate,"商户费率未配置");
        //8. 生成代付业务订单主表
        PayOrder payOrder =this.generatorPayOrder(systemOrderTrack, tradeObjectSquare, channelInfo, merchantRate, merchantInfo);
        Assert.notNull(payOrder,String.format("【快捷收单业务】商户[%s]生成订单信息，发生内部系统异常",merId));
        systemOrderTrack.setOrderId(payOrder.getPayId());
        //9.生成代收付款人信息表
        PayCardholderInfo payCardholderInfo=this.generatorPayCardholderInfo(tradeObjectSquare,payOrder);
        //10.生成支付产品细节
        PayProductDetail payProductDetail=this.generatorPayProductDetail(tradeObjectSquare,payOrder);
        //11.将数据保存到数据中
        int count= kuaiJiePayService.saveAndUpdateTransPayRecord(payOrder,payCardholderInfo,payProductDetail,null);
        logger.info("【快捷收单业务】订单号：%s ,商户号:%s, 成功插入了[{}] 条数据",merOrderId,merId,count);
        //12.封装参数
        tradeObjectSquare.setInnerType("C002");
        trade.setPayOrder(payOrder);
        trade.setMerchantCard(merchantCard);
        trade.setExtraChannelInfo(extraChannelInfo);
        trade.setTradeObjectSquare(tradeObjectSquare);
        trade.setChannelInfo(channelInfo);
        trade.setMerchantInfo(merchantInfo);
        return trade;
    }

    /**
     *   描述：发起收单
     *
     * @param trade
     */
    public BankResult nonVerifyKuaiJieToPay(SquareTrade trade) {
        try{
            logger.info("【快捷收单业务】商户号[{}]:  发送收单请求：{}",trade.getPayOrder().getMerId(),JsonUtils.objectToJson(trade));
            String result= HttpClientUtils.doPostJson(HttpClientUtils.getHttpClient(), trade.getChannelInfo().getPayUrl(), JsonUtils.objectToJsonNonNull(trade));
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
     * 获取商户所支持的通道
     * @param merchantSetting
     * @param tradeObjectSquare
     * @return
     */
    protected List<ChannelInfo>  getMerchantSurpportChannel(MerchantSetting merchantSetting, TradeObjectSquare tradeObjectSquare) throws Exception{
        String merId=tradeObjectSquare.getMerId();
        String merOrderId=tradeObjectSquare.getMerOrderId();
        String organizetionIdStr=merchantSetting.getOrganizationId();
        isBlank(organizetionIdStr,format("【获取商户所支持的通道】商户号：%s,订单号：%s,商户通道未配置",merId,merOrderId));
        String channelIdStr= merchantSetting.getChannelId();
        isBlank(channelIdStr,format("【获取商户所支持的通道】商户号：%s,订单号：%s,商户通道未配置",merId,merOrderId));
        ChannelInfo channelInfo=new ChannelInfo();
        channelInfo.setChannelIds(Arrays.asList(channelIdStr.trim().split(",")));
        channelInfo.setOrganizationIds(Arrays.asList(organizetionIdStr.trim().split(",")));
        channelInfo.setStatus(0);
        if(null !=tradeObjectSquare.getBizType() )   channelInfo.setType(Integer.valueOf(tradeObjectSquare.getBizType()));
        List<ChannelInfo>  channelInfoList=channelInfoService.selectByWhereCondition(channelInfo);
        isNotElement(channelInfoList,format("【获取商户所支持的通道】商户号：%s,订单号：%s,商户通道配置错误",merId,merOrderId));
        return channelInfoList;
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
        // 平台手续费 = 单笔手续费 + 通道金额 * 手续费率
        BigDecimal hundred = new BigDecimal(100);
        BigDecimal singlefee = merchantRate.getSingleFee() != null ? merchantRate.getSingleFee() : new BigDecimal(0);
        BigDecimal rateFee = merchantRate.getRateFee() != null ? merchantRate.getRateFee().multiply(tradeObjectSquare.getAmount().divide(hundred)) : new BigDecimal(0);
        payOrder.setFee(singlefee.add(rateFee));
        //终端客户手续费
        BigDecimal payFee = tradeObjectSquare.getPayFee() != null ? tradeObjectSquare.getPayFee() : new BigDecimal(0);
        payOrder.setTerminalFee(tradeObjectSquare.getAmount().divide(hundred).multiply(payFee));
        // 代理商手续费：是否存在上级代理商
        if (StringUtils.isNotEmpty(merchantInfo.getParentId())) {
            payOrder.setAgmentId(merchantInfo.getParentId());
//            AgentMerchantSetting agentMerchantSetting = paymentRecordSquareService.getAgentMerchantSettingByParentId(merchantInfo.getParentId());
            AgentMerchantSetting agentMerchantSetting = paymentRecordSquareService.getAgentMerchantSettingByParentIdAndPayType(merchantInfo.getParentId(),tradeObjectSquare.getBizType());
            Assert.notNull(agentMerchantSetting,String.format("【快捷收单业务】商户[%s]未找到代理商费率信息",tradeObjectSquare.getMerId()));

            // 代理商手续费 =  通道金额 * 手续费率
            BigDecimal agnetSinglefee = agentMerchantSetting.getSingleFee() != null ? agentMerchantSetting.getSingleFee() : new BigDecimal(0);
            BigDecimal agnetRateFee = agentMerchantSetting.getRateFee() != null ? agentMerchantSetting.getRateFee().multiply(tradeObjectSquare.getAmount().divide(hundred)) : new BigDecimal(0);
            payOrder.setAgentFee(agnetSinglefee.add(agnetRateFee));

        } else {
            payOrder.setAgentFee(new BigDecimal(0));
        }
        BigDecimal channelRateFee = channelInfo.getChannelRateFee() == null ? new BigDecimal(0) : channelInfo.getChannelRateFee();
        BigDecimal channelSingleFee = channelInfo.getChannelSingleFee() == null ? new BigDecimal(0) : channelInfo.getChannelSingleFee();
        payOrder.setChannelFee(channelSingleFee.add(tradeObjectSquare.getAmount().divide(hundred).multiply(channelRateFee)));
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

    public String nonVerifyKuaiJieToUpdataPayAndPayWallet_MQ(SquareTrade trade, BankResult bankResult) {
        {
            PayMap<String,Object>  map = new PayMap<>();
            PayOrder payOrder = trade.getPayOrder();
            payOrder.setTradeResult(bankResult.getParam());
            OrderObjectToMQ orderObjectToMQ=null;

            //保存同步结果，推送到MQ做查询
            payOrder.setOrderStatus(Integer.valueOf(bankResult.getStatus()));
            payOrder.setBankTime(bankResult.getBankTime());
            payOrder.setOrgOrderId(bankResult.getBankOrderId());
            trade.setPayOrder(payOrder);
            QueryOrderObjectToMQ queryOrderObjectToMQ = kuaijiePayQueryService.getQueryOrderObjectToMQ(trade);
            transOrderMQ.sendObjectMessageToQueryPayOderMQ(queryOrderObjectToMQ,20L);

            //订单状态为成功，推送到mq去处理
            if(bankResult.getStatus()==0){
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
            }else{
                payOrder.setOrderStatus(Integer.valueOf(bankResult.getStatus()));
            }
            //保证先保存到数据库，在发送到队列去
            map.lput("payOrder",payOrder);
            int count= kuaiJiePayService.saveAndUpdateWalletsAndRecord(map);
            if(bankResult.getStatus()==0) transOrderMQ.sendObjectMessageToTransOderMQ(orderObjectToMQ);
            return getTradeReturnJson(trade,bankResult);
        }
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
        if(null != riskQuotaDataList && riskQuotaDataList.size() == 0){
            //yyyy-MM 级别
            riskQuotaData=new RiskQuotaData();
            riskQuotaData.setId(null);
            riskQuotaData.setRefId(channelId);
            riskQuotaData.setRefType((short)1);//通道级别
            riskQuotaData.setType((short)2);//月级别
            riskQuotaData.setTradeTime(month);
            riskQuotaData.setAmount(amount);
            newRiskQuotaDataList.add(riskQuotaData);
            //yyyy-MM-dd 级别
            riskQuotaData=new RiskQuotaData();
            riskQuotaData.setRefId(channelId);
            riskQuotaData.setRefType((short)1);//通道级别
            riskQuotaData.setType((short)1);//月级别
            riskQuotaData.setTradeTime(day);
            riskQuotaData.setAmount(amount);
            riskQuotaData.setId(null);
            newRiskQuotaDataList.add(riskQuotaData);

        }

        riskQuotaDataList.forEach(t->{
            //月级别的
            if(t.getTradeTime().equals(month)){
                t.setAmount((t.getAmount().add(amount)));//增加当前月增额
                newRiskQuotaDataList.add(t);
            }
            //日级别
            else if(t.getTradeTime().equals(day)){
                t.setAmount((t.getAmount().add(amount)));//增加当前日增额
                newRiskQuotaDataList.add(t);
            }
        });

        //商户
        riskQuotaData=new RiskQuotaData();
        riskQuotaData.setTradeTimeList(Arrays.asList(day,month));
        riskQuotaData.setRefId(paysOrder.getMerId());
        riskQuotaData.setRefType((short)2);// 1:通道 2:商户
        riskQuotaDataList= riskQuotaDataService.getRiskQuotaDataByWhereCondition(riskQuotaData);
        if(null != riskQuotaDataList && riskQuotaDataList.size() == 0){
            //yyyy-MM 级别
            riskQuotaData=new RiskQuotaData();
            riskQuotaData.setId(null);
            riskQuotaData.setRefId(paysOrder.getMerId());
            riskQuotaData.setRefType((short)2);//商户级别
            riskQuotaData.setType((short)2);//月级别
            riskQuotaData.setTradeTime(month);
            riskQuotaData.setAmount(amount);
            newRiskQuotaDataList.add(riskQuotaData);
            //yyyy-MM-dd 级别
            riskQuotaData=new RiskQuotaData();
            riskQuotaData.setRefId(paysOrder.getMerId());
            riskQuotaData.setRefType((short)2);//商户级别
            riskQuotaData.setType((short)1);//月级别
            riskQuotaData.setTradeTime(day);
            riskQuotaData.setAmount(amount);
            riskQuotaData.setId(null);
            newRiskQuotaDataList.add(riskQuotaData);
        }
        riskQuotaDataList.forEach(t->{
            //月级别的
            if(t.getTradeTime().equals(month)){
                t.setAmount((t.getAmount().add(amount)));//增加当前月增额
                newRiskQuotaDataList.add(t);
            }
            //日级别
            else if(t.getTradeTime().equals(day)){
                t.setAmount((t.getAmount().add(amount)));//增加当前日增额
                newRiskQuotaDataList.add(t);
            }
        });
        return newRiskQuotaDataList;
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
        count =(null == count ? 1 : count++);
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
        MerchantCard merchantCard = trade.getMerchantCard();
        MerchantInfo merchantInfo = trade.getMerchantInfo();
        String merId = merchantCard.getMerId();
        String merOrderId = merchantCard.getMerOrderId();
        String status = result.getStatus().toString();
        String msg = result.getBankResult();

        PayTreeMap<String, Object> map = new PayTreeMap<>();
        map.lput("merId", merId)
                .lput("merOrderId", merOrderId)
                .lput("orderId", trade.getPayOrder().getPayId())
                .lput("amount", trade.getPayOrder().getAmount())
                .lput("tradeTime", trade.getPayOrder().getTradeTime())
                .lput("status", status)
                .lput("msg", msg)
                .lput("signMsg", CheckMd5Utils.getMd5SignWithKey(map,merchantInfo.getSecretKey()));
        return JsonUtils.objectToJson(map);
    }

    /**
     *
     * @param systemOrderTrack
     * @param tradeObjectSquare
     * @param trade
     * @param bankResult
     * @param message
     * @return
     */
    public String getReturnJson_1(SystemOrderTrack systemOrderTrack, TradeObjectSquare tradeObjectSquare, SquareTrade trade, BankResult bankResult, String message) {

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
                MerchantInfo merchantInfo = redisCacheCommonCompoment.merchantInfoCache.getOne(systemOrderTrack.getMerId());
                secretKey= merchantInfo.getSecretKey();
            }catch (Exception exception){
                exception.printStackTrace();
            }
        }
        Map<String, Object> resultMap = new TreeMap<>();
        resultMap.put("merId",systemOrderTrack.getMerId());
        resultMap.put("merOrderId",merOrderId);
        resultMap.put("orderId",transId);
        resultMap.put("amount",systemOrderTrack.getAmount());
        resultMap.put("status",status);
        resultMap.put("msg",msg);
        resultMap.put("signMsg",null != secretKey ? getMd5SignWithKey(resultMap,secretKey) : null);
        String resultjson = JsonUtils.objectToJson(resultMap);
        return resultjson;
    }
}
