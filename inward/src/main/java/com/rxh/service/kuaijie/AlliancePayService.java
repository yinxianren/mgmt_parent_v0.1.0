package com.rxh.service.kuaijie;


import com.alibaba.fastjson.JSONObject;
import com.rxh.exception.PayException;
import com.rxh.pojo.AbstractParamModel;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.merchant.MerchantRegisterInfo;
import com.rxh.pojo.payment.ParamRule;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.pojo.payment.TradeObjectSquare;
import com.rxh.pojo.sys.SysConstant;
import com.rxh.service.AbstractPayService;
import com.rxh.square.pojo.*;
import com.rxh.utils.UUID;
import com.rxh.utils.*;
import com.rxh.vo.OrderObjectToMQ;
import com.rxh.vo.QueryOrderObjectToMQ;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AlliancePayService extends AbstractPayService {


    /**
     *
     * @param trade
     * @param bankResult
     * @return
     */

    public String kuaiJieToUpdataTransInfoAndPayWallet_MQ(SquareTrade trade, BankResult bankResult){

        TransOrder transOrder = trade.getTransOrder();
        transOrder.setTradeResult(bankResult.getParam().length()>=500?bankResult.getParam().substring(0,499):bankResult.getParam());
        OrderObjectToMQ orderObjectToMQ=null;

        //保存同步结果，推送到MQ做查询
        transOrder.setOrderStatus(Integer.valueOf(bankResult.getStatus()));
        trade.setTransOrder(transOrder);
        QueryOrderObjectToMQ queryOrderObjectToMQ = allinPayService.getQueryOrderObjectToMQ(trade);
        transOrderMQ.sendObjectMessageToQueryTransOderMQ(queryOrderObjectToMQ,20L);

        //订单状态为成功，推送到mq去处理
        if(bankResult.getStatus()==0){
            transOrder.setBankTime(bankResult.getBankTime());
            transOrder.setOrgOrderId(bankResult.getBankOrderId());
            //状态更改为30，表示mq处理中
            transOrder.setOrderStatus(SystemConstant.MQ2_PROCESSED);
            orderObjectToMQ=this.getOrderObjectToMQ(trade);
        }else{
            transOrder.setOrderStatus(Integer.valueOf(bankResult.getStatus()));
        }
        TransAudit transAudit= trade.getTransAudit();
        transAudit.setTransferTime(new Date());
        transAudit.setStatus(0);// 已经审核
        //保证先保存到数据库，在发送到队列去
        int count=kuaiJiePayService.saveAndUpdateTransOrderAndTransAudit(transOrder,transAudit);
        if(bankResult.getStatus()==0) transOrderMQ.sendObjectMessageToTransOderMQ(orderObjectToMQ);
        return getTradeReturnJson(trade,bankResult);
    }

    private OrderObjectToMQ getOrderObjectToMQ(SquareTrade trade){
        TransOrder transOrder= trade.getTransOrder();
        ChannelInfo  channelInfo=trade.getChannelInfo();
        MerchantInfo merchantInfo=trade.getMerchantInfo();
        PayOrder  payOrder=trade.getPayOrder();

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
     *  封装响应结果
     * @param trade
     * @param result
     * @return
     */
    private String getTradeReturnJson(SquareTrade trade, BankResult result)  {

        TransOrder  transOrder= trade.getTransOrder();
        MerchantInfo merchantInfo = trade.getMerchantInfo();
        String merId = transOrder.getMerId();
        String merOrderId =transOrder.getMerOrderId();
        String status = result.getStatus().toString();
        String msg = result.getBankResult();

        PayTreeMap<String, Object> map = new PayTreeMap<>();
        map.lput("merId", merId)
                .lput("merOrderId", merOrderId)
                .lput("orderId", transOrder.getTransId())
                .lput("amount", transOrder.getRealAmount())
                .lput("status", status)
                .lput("msg", msg);
        if (result.getStatus() == SystemConstant.BANK_STATUS_SUCCESS){
            map.lput("resultCode", "RXH00000");
        }else {
            map.lput("resultCode", "RXH00013");
        }
        map.lput("signMsg", CheckMd5Utils.getMd5SignWithKey(map,merchantInfo.getSecretKey()));
        return JsonUtils.objectToJson(map);
    }

    /**
     *
     * @param systemOrderTrack
     * @param abstractParamModel
     * @param message
     * @param squareTrade
     * @return
     */
    public String errorResult(SystemOrderTrack systemOrderTrack, AbstractParamModel abstractParamModel, String message, SquareTrade squareTrade)  throws PayException{
        PayTreeMap<String,Object> map= new PayTreeMap<>();
        String merId= systemOrderTrack.getMerId();
        String merOrderId=(null != abstractParamModel ? abstractParamModel.getMerOrderId() : "");
        BigDecimal amount= abstractParamModel.getAmount();
        String transId=(squareTrade==null?"":squareTrade.getTransOrder().getTransId());
        String orderId= org.springframework.util.StringUtils.isEmpty(transId) ? "": transId;
//        MerchantInfo merchantInfo =paymentRecordSquareService.getMerchantInfoByMerId(merId);
        MerchantInfo merchantInfo =redisCacheCommonCompoment.merchantInfoCache.getOne(merId);
        map.lput("merId",merId)
                .lput("merOrderId",merOrderId)
                .lput("status", 1)
                .lput("orderId",orderId)
                .lput("amount", amount)
                .lput("msg", message)
                .lput("resultCode","RXH00013")
                .lput("signMsg",getMd5SignWithKey(map,merchantInfo.getSecretKey()));
        return JSONObject.toJSONString(map);
    }

    public String errorResult(SystemOrderTrack systemOrderTrack, AbstractParamModel abstractParamModel, String message, SquareTrade squareTrade, String resultCode)  throws PayException{
        PayTreeMap<String,Object> map= new PayTreeMap<>();
        String merId= systemOrderTrack.getMerId();
        String merOrderId=(null != abstractParamModel ? abstractParamModel.getMerOrderId() : "");
        String terminalMerId = abstractParamModel.getTerminalMerId();
        BigDecimal amount= abstractParamModel.getAmount();
        String transId=(squareTrade==null?"":squareTrade.getTransOrder().getTransId());
        String orderId= org.springframework.util.StringUtils.isEmpty(transId) ? "": transId;
//        MerchantInfo merchantInfo =paymentRecordSquareService.getMerchantInfoByMerId(merId);
        MerchantInfo merchantInfo =redisCacheCommonCompoment.merchantInfoCache.getOne(merId);
        map.lput("merId",merId)
                .lput("merOrderId",merOrderId)
                .lput("status", 1)
                .lput("orderId",orderId)
                .lput("amount", amount)
                .lput("msg", message)
                .lput("terminalMerId",terminalMerId)
                .lput("resultCode",resultCode)
                .lput("signMsg",getMd5SignWithKey(map,merchantInfo.getSecretKey()));
        return JSONObject.toJSONString(map);
    }

    /**
     *
     * @param trade
     * @return
     * @throws Exception
     */
    public BankResult toPay(SquareTrade trade)  throws Exception{
        BankResult bankResult = null;
        try {
            String merId=trade.getTradeObjectSquare().getMerId();
            //获取银行名称系统常量
            SysConstant sysConstant = sysConstantService.getOneByFirstValueAndCode(trade.getTradeObjectSquare().getBankCode(),"bankName");
            if (sysConstant != null){
                trade.getTradeObjectSquare().setBankName(sysConstant.getName());
            }
            String result = HttpClientUtils.doPostJson(HttpClientUtils.getHttpClient(), trade.getChannelInfo().getPayUrl(), JsonUtils.objectToJsonNonNull(trade));
            logger.info("【代付】商户号：{},代付请求结果{}",merId,result);
            if (StringUtils.isBlank(result)) throw new PayException(format("【代付】商户号：{},Cross请求发生错误！", merId));
             bankResult = JsonUtils.jsonToPojo(result, BankResult.class);
            isNull(bankResult, format("【代付】商户号：{},BankResultJson转BankResult异常", merId));
        }catch ( Exception e){
            TransOrder transOrder=trade.getTransOrder();
            transOrder.setOrderStatus(1);
            transOrderService.updateByPrimaryKey(transOrder);
            throw  new PayException(e.getMessage());
        }finally {
            return bankResult;
        }

    }
    /**
     *
     * @param systemOrderTrack
     * @param tradeObjectSquare
     * @return
     * @throws Exception
     */
    public SquareTrade getSquareTrade (SystemOrderTrack systemOrderTrack, TradeObjectSquare tradeObjectSquare) throws  Exception{
        String merId=tradeObjectSquare.getMerId();
        String merOrderId=systemOrderTrack.getMerOrderId();
        String terminalMerId=tradeObjectSquare.getTerminalMerId();
//        MerchantInfo merchantInfo = paymentRecordSquareService.getMerchantInfoByMerId(merId);
        MerchantInfo merchantInfo = redisCacheCommonCompoment.merchantInfoCache.getOne(merId);
        isNull(merchantInfo,format("【代付】商户号：%s,未找到该商户信息",merId),"RXH00013");
        CheckMd5Utils.checkMd5(systemOrderTrack.getTradeInfo(),merchantInfo.getSecretKey());
        TransOrder tso=paymentRecordSquareService.checkTransOrderMul(merOrderId,merId,terminalMerId);
        isNotNull(tso,format("【代付】商户号：%s,订单号：%s,该笔代付订单已存在",merId,merOrderId),"RXH00009");
        MerchantRate merchantRate = redisCacheCommonCompoment.merchantRateCache.getOne(merId,tradeObjectSquare.getBizType());
        isNull(merchantRate,format("【代付】商户号：%s,未找到该商户费率信息",merId),"RXH00013");
        if (merchantRate.getSingleFee().compareTo(tradeObjectSquare.getBackFee()) == 1){
            throw new PayException(format("【代付】商户号：%s,代付手续费低于商户设置手续费",merId),"RXH00013");
        }
        List<String> orders = Arrays.asList(tradeObjectSquare.getOriginalMerOrderId().split("\\|"));
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("merId",merId);
        paramMap.put("terminalMerId",terminalMerId);
        paramMap.put("merOrderIds",orders);
        paramMap.put("status","0");
        List<PayOrder> payOrderList = payOrderService.selectByOrders(paramMap);
        //申请的原订单包含未支付订单
        if (payOrderList.size() != orders.size()){
            List<String> list = new ArrayList<>();
            for (PayOrder payOrder : payOrderList){
                list.add(payOrder.getMerOrderId());
            }
            StringJoiner stringJoiner =  new StringJoiner(",");
            for (String order : orders){
                if(!list.contains(order)){
                    stringJoiner.add(order);
                };
            }
            throw new PayException(format("【代付】商户号：%s,存在未付款订单【%s】",merId,stringJoiner.toString()),"RXH00013");
        }
        isNotElement(payOrderList,format("【代付】商户号：%s,不存在付款订单请先付款",merId),"RXH00013");
        PayOrder payOrder = payOrderList.get(0);
        BigDecimal total = new BigDecimal(0);
        for (PayOrder payOrder1 : payOrderList){
            total = total.add(payOrder1.getRealAmount());
        }
        payOrder.setRealAmount(total);
        //如果代付金額為0，則全額代付
        if (tradeObjectSquare.getAmount() == null ){
            tradeObjectSquare.setAmount(total);
            systemOrderTrack.setAmount(total);
        }
        //多筆代付，全額代付
        if (orders.size()>1){
            tradeObjectSquare.setAmount(total);
            systemOrderTrack.setAmount(total);
        }
        if (tradeObjectSquare.getAmount().compareTo(tradeObjectSquare.getBackFee()) == -1){
            throw new PayException(format("【代付】商户号：%s,代付金额低于上传手续费无法完成交易",merId),"RXH00013");
        }
//        PayOrder payOrder = paymentRecordSquareService.getPayOrderByMerOrderIdAndMerIdAndTerMerId(tradeObjectSquare.getOriginalMerOrderId(),merId,terminalMerId);
        isNull(payOrder,format("【代付】商户号：%s,不存在付款订单请先付款",merId),"RXH00013");
//        ChannelInfo payOrderChannel= paymentRecordSquareService.getChannelInfo(payOrder.getChannelId());
        ChannelInfo payOrderChannel= redisCacheCommonCompoment.channelInfoCache.getOne(payOrder.getChannelId());
        isNull(payOrderChannel,format("【代付】商户号：%s,未找到相关通道信息",merId),"RXH00013");
        ChannelInfo outChannel= paymentRecordSquareService.getChannelInfo(payOrderChannel.getOutChannelId());
//        ChannelInfo outChannel= redisCacheCommonCompoment.channelInfoCache.getAll().stream()
//                .filter(t->t.getOutChannelId().equals(payOrderChannel.getOutChannelId()))
//                .distinct()
//                .findAny()
//                .orElse(null);
//        ExtraChannelInfo extraChannelInfo =paymentRecordSquareService.searchExtraChannelInfo(payOrderChannel.getOrganizationId(), SystemConstant.BONDCARD);
        ExtraChannelInfo extraChannelInfo = redisCacheCommonCompoment.extraChannelInfoCache.getAll().stream()
                .filter(t->t.getOrganizationId().equals(payOrderChannel.getOrganizationId()) && t.getType().equals(SystemConstant.BONDCARD) )
                .distinct().findAny().orElse(null);
        isNull(extraChannelInfo,format("【代付】商户号：%s,未找到相关附属通道信息",merId),"RXH00013");
//        MerchantCard merchantCard=paymentRecordSquareService.searchMerchantCard(extraChannelInfo.getExtraChannelId(), merId, terminalMerId);
        MerchantCard merchantCard=redisCacheCommonCompoment.merchantCardCache.getMore(merId,terminalMerId).stream()
                .filter(t-> t.getExtraChannelId().equals(extraChannelInfo.getExtraChannelId()) && t.getCardNum().equals(tradeObjectSquare.getBankCardNum()))
                .distinct().findAny().orElse(null);

        isNull(merchantCard,format("【代付】商户号：%s,未找到相关商户卡信息",merId),"RXH00013");
        logger.info(format("[代付]请求协议号：%s",merchantCard.getResult()));
        MerchantRegisterCollect merchantRegisterCollect=this.searchMerchantRegisterCollect(extraChannelInfo,tradeObjectSquare);
        //封装交易参数
        SquareTrade trade = new SquareTrade();
        trade.lsetPayOrderChannel(payOrderChannel)
                .lsetMerchantRegisterCollect(merchantRegisterCollect)
                .lsetPayOrder(payOrder);
        //查询商户是否代付过
        this.checkTransAmount(systemOrderTrack,tradeObjectSquare,trade);
        // 获取商户配置
//        MerchantSetting merchantSetting = merchantSquareSettingCache.getMerchantSettingByMerId(tradeObjectSquare.getMerId());
        MerchantSetting merchantSetting = redisCacheCommonCompoment.merchantSettingCache.getOne(tradeObjectSquare.getMerId());
        // 获取商户费率对象
//        MerchantRate merchantRate = merchantSquareRateCache.getMerchantRateByMerIdAndPayType(merchantInfo.getMerId(),outChannel.getType().toString());
//        MerchantRate merchantRate = redisCacheCommonCompoment.merchantRateCache.getOne(merchantInfo.getMerId(),outChannel.getType().toString());
//        isNull(merchantRate,format("【代付】商户号：%s,未找到相关商户费率信息",merId),"RXH00013");
        //1. 保存结算业务收款账号信息
        TransBankInfo transBankInfo=this.getTransBankInfo(tradeObjectSquare);
        //2.  保存及获取订单对象
        TransOrder transOrder = this.getTransOrder(systemOrderTrack,tradeObjectSquare,outChannel,merchantRate,merchantInfo);
        //
        TransAudit transAudit=this.getTransAudit(tradeObjectSquare, transOrder, trade);
        //
        systemOrderTrack.setOrderId(transOrder.getTransId());
        // 商户风控对象
//        MerchantQuotaRisk merchantQuotaRisk = recordSquareService.getMerchantQuotaRiskByMerId(tradeObjectSquare.getMerId());
        MerchantQuotaRisk merchantQuotaRisk = redisCacheCommonCompoment.merchantQuotaRiskCache.getOne(tradeObjectSquare.getMerId());
        isNull(merchantQuotaRisk,format("【代付】商户号：%s,商户风控对象不存在",merId),"RXH00013");
        //执行风控
        recordSquareService.analysis(outChannel,transOrder,merchantQuotaRisk);
        MerchantRegisterInfo mri= redisCacheCommonCompoment.merchantRegisterInfoCache
                .getAll()
                .stream()
                .filter(t->
                        t.getTerminalMerId().equals(terminalMerId)
                                && t.getMerId().equals(merId))
                .distinct()
                .findAny()
                .orElse(null);
        PayMap<String,Object>  map= new PayMap<>();
        int count= kuaiJiePayService.saveTransOrderPayRecord(
                map .lput("transBankInfo",transBankInfo)
                        .lput("transOrder",transOrder)
                        .lput("transAudit",transAudit));

        logger.info("【代付】商户号：{},更新了{}条数据",merId,count);
        return trade.lsetMerOrderId(tradeObjectSquare.getMerOrderId())
                .lsetMerchantCard(merchantCard)
                .lsetExtraChannelInfo(extraChannelInfo)
                .lsetChannelInfo(outChannel)
                .lsetMerchantInfo(merchantInfo)
                .lsetTradeObjectSquare(tradeObjectSquare)
                .lsetTransOrder(transOrder)
                .lsetTransAudit(transAudit)
                .lsetPayOrder(payOrder)
                .lsetMerchantRegisterCollect(merchantRegisterCollect)
                .lsetMerchantRegisterInfo(mri);
    }

    /**
     *
     * @param tradeObjectSquare
     * @param transOrder
     * @param trade
     * @return
     */
    public TransAudit getTransAudit(TradeObjectSquare tradeObjectSquare, TransOrder transOrder, SquareTrade trade) {
        TransAudit transAudit = new TransAudit();
        transAudit.setTransId(transOrder.getTransId());
        transAudit.setId(UUID.createKey("trans_audit", ""));
        transAudit.setMerId(tradeObjectSquare.getMerId());
        transAudit.setMerOrderId(tradeObjectSquare.getMerOrderId());
        transAudit.setCurrency(tradeObjectSquare.getCurrency());
        transAudit.setAmount(tradeObjectSquare.getAmount());
        transAudit.setStatus(SystemConstant.NO_PAY);
        transAudit.setTrade(JsonUtils.objectToJsonNonNull(trade));

        return transAudit;
    }


    /**
     * @param systemOrderTrack
     * @param tradeObjectSquare
     * @param channelInfo
     * @param merchantRate
     * @param merchantInfo
     */
    public TransOrder getTransOrder(SystemOrderTrack systemOrderTrack, TradeObjectSquare tradeObjectSquare, ChannelInfo channelInfo,
                                    MerchantRate merchantRate, MerchantInfo merchantInfo) throws PayException {
        TransOrder transOrder = new TransOrder();
        transOrder.setOriginalMerOrderId(tradeObjectSquare.getOriginalMerOrderId());
        transOrder.setTransId(UUID.createKey("trans_order", ""));
        transOrder.setMerId(tradeObjectSquare.getMerId());
        transOrder.setMerOrderId(tradeObjectSquare.getMerOrderId());
        transOrder.setChannelId(channelInfo.getChannelId());
        transOrder.setOrganizationId(channelInfo.getOrganizationId());
        transOrder.setPayType(String.valueOf(channelInfo.getType()));
        transOrder.setChannelTransCode(channelInfo.getChannelTransCode());
        transOrder.setCurrency(tradeObjectSquare.getCurrency());
        transOrder.setAmount(tradeObjectSquare.getAmount());
        BigDecimal backFee = tradeObjectSquare.getBackFee()!=null?tradeObjectSquare.getBackFee(): new BigDecimal(0);
        transOrder.setRealAmount(tradeObjectSquare.getAmount().subtract(backFee));
        BigDecimal fee= tradeObjectSquare.getFee() !=null ? tradeObjectSquare.getFee() : new BigDecimal(0);
        transOrder.setTerminalFee(fee);
        BigDecimal hundred = new BigDecimal(100);
        // 平台手续费 = 单笔手续费 + 通道金额 * 手续费率
        BigDecimal singlefee = merchantRate.getSingleFee() != null ? merchantRate.getSingleFee() : new BigDecimal(0);
        BigDecimal rateFee = merchantRate.getRateFee() != null ?
                merchantRate.getRateFee().multiply(tradeObjectSquare.getAmount().divide(hundred))
                : new BigDecimal(0);
        transOrder.setFee(singlefee.add(rateFee));

        transOrder.setTerminalFee(backFee);
        // 代理商手续费：是否存在上级代理商
        if (StringUtils.isNotEmpty(merchantInfo.getParentId())) {
            transOrder.setAgmentId(merchantInfo.getParentId());
//            AgentMerchantSetting agentMerchantSetting = paymentRecordSquareService.getAgentMerchantSettingByParentIdAndPayType(merchantInfo.getParentId(),tradeObjectSquare.getBizType());
            AgentMerchantSetting agentMerchantSetting = redisCacheCommonCompoment.agentMerchantSettingCahce.getOne(merchantInfo.getParentId(),tradeObjectSquare.getBizType());
            if (agentMerchantSetting==null){
                throw  new PayException("代理商费率不存在","RXH00013");
            }
            // 代理商手续费 = 单笔手续费
            BigDecimal agnetSinglefee = agentMerchantSetting.getSingleFee() != null ? agentMerchantSetting.getSingleFee() : new BigDecimal(0);
            BigDecimal agnntRatefee = agentMerchantSetting.getRateFee() != null ? agentMerchantSetting.getRateFee().divide(new BigDecimal(100)) : new BigDecimal(0);
            transOrder.setAgentFee(agnetSinglefee.add(agnntRatefee.multiply(tradeObjectSquare.getAmount()).setScale(2, BigDecimal.ROUND_UP)));
        } else {
            transOrder.setAgentFee(new BigDecimal(0));
        }
        // 通道手续费
        BigDecimal channelSinglefee = channelInfo.getChannelSingleFee() != null ?channelInfo.getChannelSingleFee() : new BigDecimal(0);
        BigDecimal channelRateFee = channelInfo.getChannelRateFee() != null ? channelInfo.getChannelRateFee().multiply(tradeObjectSquare.getAmount().divide(hundred)) : new BigDecimal(0);
        transOrder.setChannelFee(channelSinglefee.add(channelRateFee));
        // 平台收入
        transOrder.setIncome(transOrder.getFee().subtract(transOrder.getChannelFee()));
        transOrder.setOrderStatus(SystemConstant.NO_PAY);
        transOrder.setSettleStatus(SystemConstant.NO_SETTLE);
        transOrder.setTradeTime(systemOrderTrack.getTradeTime());
        transOrder.setTerminalMerId(tradeObjectSquare.getTerminalMerId());
        transOrder.setTerminalMerName(tradeObjectSquare.getTerminalMerName());
        return transOrder;
    }


    /**
     * @param tradeObjectSquare
     */
    public TransBankInfo getTransBankInfo(TradeObjectSquare tradeObjectSquare) {
        TransBankInfo transBankInfo = new TransBankInfo();
        transBankInfo.setTransId(UUID.createKey("trans_bank_info", ""));
        transBankInfo.setBenefitName(tradeObjectSquare.getBenefitName());
        transBankInfo.setBankName(tradeObjectSquare.getBankName());
        transBankInfo.setBankcardNum(tradeObjectSquare.getBankCardNum());
        transBankInfo.setBankcardType(tradeObjectSquare.getBankCardType());
        transBankInfo.setBankBranchName(tradeObjectSquare.getBankBranchName());
        transBankInfo.setBankBranchNum(tradeObjectSquare.getBankBranchNum());
        transBankInfo.setIdentityType(tradeObjectSquare.getIdentityType());
        transBankInfo.setIdentityNum(tradeObjectSquare.getIdentityNum());
        return transBankInfo;
    }

    /**
     *
     * @param systemOrderTrack
     * @param tradeObjectSquare
     * @param trade
     * @throws PayException
     */
    private void checkTransAmount(SystemOrderTrack systemOrderTrack, TradeObjectSquare tradeObjectSquare, SquareTrade trade) throws PayException {
        PayOrder payOrder = trade.getPayOrder();
        String merId = tradeObjectSquare.getMerId();
        String terminalMerId= tradeObjectSquare.getTerminalMerId();
        String originalMerOrderId = tradeObjectSquare.getOriginalMerOrderId();
        String merOrderId=tradeObjectSquare.getMerOrderId();
        //代付订单金额
        BigDecimal transOrderAmount = systemOrderTrack.getAmount();
        //支付订单的金额
        BigDecimal payOrderAmount=payOrder.getRealAmount();
        //订单总可用代付金额, =支付订单的金额-支付订单的总成本
        BigDecimal availableTransOrderAmount=payOrder.getRealAmount();
        //判断代付金额是否大于支付订单金额
        if(transOrderAmount.compareTo(payOrderAmount)>0)
            throw new PayException(format("【快捷代付】商户号：%s,代付订单号：%s,支付订单号：%s,代付金额大于订单金额",merId,merOrderId,originalMerOrderId),"RXH00011");

        //获取历史代付订单
        List<String> orders = Arrays.asList(originalMerOrderId.split("\\|"));
        List<String> orderStatusList = new ArrayList<>();
        orderStatusList.add("0");
        orderStatusList.add("3");
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("merId",merId);
        paramMap.put("terminalMerId",terminalMerId);
        paramMap.put("orderStatusList",orderStatusList);
        paramMap.put("merOrderIds",orders);
        List<TransOrder>  transOrderList=transOrderService.selectByMap(paramMap);
        if(null != transOrderList && transOrderList.size()>0){
            //已经代付出去的金额汇总
            BigDecimal alreadyTransOrderAmount= transOrderList.stream()
                    .filter( t->t.getOrderStatus() != 1 )
                    .map(t->t.getAmount())
                    .reduce((amount_1,amount_2)->amount_1.add(amount_2))
                    .orElse(new BigDecimal(0));
            //订单总可用代付金额, =支付订单的金额-支付订单的总成本-已经代付出去的金额汇总
            availableTransOrderAmount=availableTransOrderAmount.subtract(alreadyTransOrderAmount);
        }

        //判断代付金额是否大于可用代付订单金额
        if(transOrderAmount.compareTo(availableTransOrderAmount) > 0)
            throw new PayException(format("【快捷代付】商户号：%s,代付订单号：%s,支付订单号：%s,代付金额大于订单剩余可代付金额",merId,merOrderId,originalMerOrderId),"RXH00011");
        //判断商户钱包金额是否支持代付
        MerchantWallet merchantWallet = recordSquareService.getMerchantWallet(merId);
        //下级商户钱包
        TerminalMerchantsWallet terminalMerchantsWallet = recordSquareService.getTerminalMerchantsWallet(merId,terminalMerId);

        BigDecimal terMertotalAvailableAmount = terminalMerchantsWallet.getTotalAvailableAmount();
        if (null==terMertotalAvailableAmount)  terMertotalAvailableAmount=new BigDecimal(0);
        if (transOrderAmount.compareTo(terMertotalAvailableAmount)>0)
            throw new PayException(format("【快捷代付】商户号：%s,代付订单号：%s,支付订单号：%s,代付金额大于终端商户可用余额",merId,merOrderId,originalMerOrderId),"RXH00011");

        BigDecimal totalAvailableAmount = merchantWallet.getTotalAvailableAmount();
        if (null==totalAvailableAmount)  totalAvailableAmount=new BigDecimal(0);
        if (transOrderAmount.compareTo(totalAvailableAmount)>0)
            throw new PayException(format("【快捷代付】商户号：%s,代付订单号：%s,支付订单号：%s,代付金额大于商户可用余额",merId,merOrderId,originalMerOrderId),"RXH00011");
    }


    /**
     *
     * @param extraChannelInfo
     * @param tradeObjectSquare
     * @return
     */
    private MerchantRegisterCollect searchMerchantRegisterCollect(ExtraChannelInfo extraChannelInfo, TradeObjectSquare tradeObjectSquare) {
//        List<MerchantRegisterCollect> merchantRegisterCollects=paymentRecordSquareService.getmMrchantRegisterCollectByMerIdAndTerminalMerId(
//                tradeObjectSquare.getMerId(), tradeObjectSquare.getTerminalMerId(),tradeObjectSquare.getBankCardNum());
        List<MerchantRegisterCollect> merchantRegisterCollects=redisCacheCommonCompoment.merchantRegisterCollectCache.getAll()
                .stream().filter(t->t.getMerId().equals(tradeObjectSquare.getMerId())
                        && t.getTerminalMerId().equals(tradeObjectSquare.getTerminalMerId())
//                        && t.getExtraChannelId().equals(extraChannelInfo.getExtraChannelId())
                        && t.getStatus() == 0
                ).distinct().collect(Collectors.toList());

        ExtraChannelInfo extraChannel = paymentRecordSquareService.getExtraChannelInfoByOrgId(extraChannelInfo.getOrganizationId(), SystemConstant.ADDCUS);
        return merchantRegisterCollects.stream().filter(merchantRegisterCollect ->
                merchantRegisterCollect.getExtraChannelId().equals(extraChannel.getExtraChannelId()))
                .findFirst()
                .get();
    }


    /**
     *
     */
    private final  Map<String, ParamRule> allianceTransOrderPayValidate= new HashMap<String, ParamRule>() {
        {
            // 交易数据
            put("bizType", new ParamRule(REQUIRED, 10, 1003));//
            put("charset", new ParamRule(REQUIRED, 10, 1004));//
            put("signType", new ParamRule(REQUIRED, 10, 1004));//
            put("merId", new ParamRule(REQUIRED, 10, 1005));//
            put("merOrderId", new ParamRule(REQUIRED, 400, 1006));//
            put("originalMerOrderId", new ParamRule(REQUIRED, 500, 1006));//
            put("currency", new ParamRule(REQUIRED, 10, 1006));//
//            put("amount", new ParamRule(MONEY, 1007));//
            put("terminalMerId", new ParamRule(REQUIRED, 32, 1011));//
            put("terminalMerName", new ParamRule(REQUIRED, 32, 1013));//
            put("returnUrl", new ParamRule(OTHER, 128, 1013));//
            put("noticeUrl", new ParamRule(REQUIRED, 128, 1013));//
            put("signMsg", new ParamRule(REQUIRED, 256, 1013));//

        }
    };

    /**
     *
     * @param systemOrderTrack
     * @return
     * @throws PayException
     */
    public  TradeObjectSquare alliancePay(SystemOrderTrack systemOrderTrack) throws PayException {
        try {

            List<String> tradeInfoKeys = new ArrayList<>();
            // 报文参数
            Map<String, Object> tradeInfoMap = JsonUtils.jsonToMap(systemOrderTrack.getTradeInfo());
            Set<String> keys = tradeInfoMap.keySet();
            for (String key : keys) {
                validateValue(allianceTransOrderPayValidate,key, tradeInfoMap.get(key).toString());
                tradeInfoKeys.add(key);
            }
            // 校验必要参数
            ValidateItem(allianceTransOrderPayValidate,tradeInfoKeys);

            TradeObjectSquare tradeObjectSquare = JsonUtils.jsonToPojo(systemOrderTrack.getTradeInfo(), TradeObjectSquare.class);


            systemOrderTrack.setMerId(tradeObjectSquare.getMerId());
            systemOrderTrack.setMerOrderId(tradeObjectSquare.getMerOrderId());
            systemOrderTrack.setAmount(tradeObjectSquare.getAmount());
            systemOrderTrack.setReturnUrl(tradeObjectSquare.getReturnUrl() != null ? tradeObjectSquare.getReturnUrl():"");
            systemOrderTrack.setNoticeUrl(tradeObjectSquare.getNoticeUrl() != null ? tradeObjectSquare.getNoticeUrl():"");
            return tradeObjectSquare;
        } catch (PayException e) {
            logger.error(systemOrderTrack.getTradeInfo(), e);
            throw new PayException("请求报文无法解析！" + e.getMessage(), "RXH00013");
        }
    }
}
