package com.rxh.service.haiyi;


import com.alibaba.fastjson.JSONObject;
import com.rxh.exception.PayException;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.payment.ParamRule;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.pojo.payment.TradeObjectSquare;
import com.rxh.service.AbstractPayService;
import com.rxh.square.pojo.*;
import com.rxh.utils.UUID;
import com.rxh.utils.*;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 *  描述：海懿代付 service 层
 * @author panda
 * @date 20190710
 */
@Service
public class HaiYiPayService extends AbstractPayService {


    /**
     *  处理从队列获取的成功和失败的代付订单
     * @param transOrder
     */
    public  boolean haiYiPayHandleSucessAndFailTransOrder(TransOrder transOrder) {
        Assert.notNull(transOrder,"【海懿代付】处理队列任务(成功和失败订单)：从队列取到对象有异常！");
        Integer orderStatus=transOrder.getOrderStatus();
        //处理成功的订单
        if( orderStatus == 0 ) {
            try{
                lock.lock();
                return haiYiPayHandleSucessTrandsOrder(transOrder);
            }finally {
                lock.unlock();
            }
        }
        // 处理失败的订单
        else if(orderStatus == 1) {
            try{
                lock.lock();
                return haiYiPayHandleFailTrandsOrder(transOrder);
            }finally {
                lock.unlock();
            }
        }
        return  false;
    }

    /**
     *   处理成功的订单，更新插入带有事务
     * @param transOrder
     */
    public  boolean  haiYiPayHandleSucessTrandsOrder(TransOrder transOrder) {
        try {
            PayMap<String, Object> map= new PayMap<>();
            map= haiYiMerchantWalletComponent.handleMerchantWalletAndDetails(map,transOrder);
            map= haiYiAgentWalletComponent.handleAgentWalletAndDetails(map,transOrder);
            map= haiYiChannelWalletComponent.handleChannelWalletAndDetails(map,transOrder);
            map.put("transOrder",transOrder);
            haiYiPayWalletService.updateHaiYiPaySuccessTrandsOrderDetail(map);
            return true;
        }catch (Exception e){
            if(e instanceof  PayException){
                logger.warn(e.getMessage());
            }else {
                e.printStackTrace();
            }
            return false;
        }
    }

    /**
     *   处理失败的订单，更新带有事务
     * @param transOrder
     */
    public    boolean  haiYiPayHandleFailTrandsOrder(TransOrder transOrder){
        try {
            //判断订单是否重复更新
            String transId=transOrder.getTransId();
            TransOrder transOrder_task=transOrderService.getTransOrderByPrimaryId(transId);
            Assert.notNull(transOrder_task,"【海懿代付】处理队列任务(成功订单)：数据库没有此订单transId=["+transId+"]！");
            if(transOrder_task.getOrderStatus()==transOrder.getOrderStatus()){
                Assert.state(false,"【海懿代付】处理队列任务(成功订单)：此订单transId=["+transId+"]重复处理！");
            }
            //获取商户钱包
            MerchantWallet merchantWallet = paymentRecordSquareService.getMerchantWallet(transOrder.getMerId());
            BigDecimal amout = transOrder.getAmount();
            //2-1获取冻结资金
            BigDecimal totalFreezeAmount = merchantWallet.getTotalFreezeAmount();
            //2-2 获取总可用余额
            BigDecimal totalAvailableAmount = merchantWallet.getTotalAvailableAmount();
            // 总可用金额+总订单金额
            totalAvailableAmount = totalAvailableAmount.add(amout);
            //冻结金额-总订单金额
            //计算剩余的冻结金额，这个金额需要更新到数据库中
            totalFreezeAmount = totalFreezeAmount.subtract(amout);
            merchantWallet.setTotalFreezeAmount(totalFreezeAmount);
            merchantWallet.setTotalAvailableAmount(totalAvailableAmount);
            haiYiPayWalletService.updateHaiYiPayFailTrandsOrderDetail(transOrder, merchantWallet);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 描述 更新钱包、审核信息等;并且封装返回结果
     * @param trade
     * @param bankResult
     * @throws Exception
     */
    public  String  haiYiPayForAnotherToUpdataPayInfoAndPayWallet(SquareTrade trade, BankResult bankResult) throws Exception {
        try{
            TransAudit transAudit=trade.getTransAudit();
            TransOrder transOrder = trade.getTransOrder();
            transOrder.setTradeResult(bankResult.getParam());
            MerchantWallet  merchantWallet=null;
            ChannelWallet channelWallet=null;
            List<RiskQuotaData> riskQuotaDataList=null;
            if( bankResult.getStatus()==0){
                transOrder.setBankTime(bankResult.getBankTime());
                transOrder.setOrgOrderId(bankResult.getBankOrderId());
                merchantWallet =this.getPayWallet(trade);  //商户钱包
                channelWallet=getChannelWallet(trade);//平台钱包
                riskQuotaDataList = this.getRiskQuotaData(trade);
                transOrder.setOrderStatus(3);   //支付成功支付中
            }else{
                //支付失败的时候，未支付
                transOrder.setOrderStatus(2);
            }
            //更新订单审计表
            transAudit.setTransferer(trade.getTransAudit().getTransferer());
            transAudit.setTransferTime(new Date());
            transAudit.setStatus(0);// 已经审核

            //更新
            haiYiPayWalletService.updateHaiYiPayTrandsOrderAndTransAudit(new PayMap<String,Object>()
                    .lput("transOrder",transOrder)
                    .lput("transAudit",transAudit)
                    .lput("merchantWallet",merchantWallet)
                    .lput("riskQuotaDataList",riskQuotaDataList)
                    .lput("channelWallet",channelWallet)
            );
            if(bankResult.getStatus()==0) {
                transOrderMQ.sendObjectMessageToTransOderFirst(transOrder);
            }
            //封装返回结果
            return this. getReturnJson(trade,bankResult);
        }catch (Exception exception){
            throw exception;
        }

    }

    /**
     *   封装代付返回结果
     * @param trade
     * @param result
     * @return
     */
    public String getReturnJson(SquareTrade trade, BankResult result) {
        TransOrder transOrder = trade.getTransOrder();
        String merOrderId = transOrder.getMerOrderId();
        String transId = transOrder.getTransId();
        Short status = result.getStatus();

        Map<String, Object> resultMap = new TreeMap<>();

        resultMap.put("merOrderId",merOrderId != null ? merOrderId:"" );
        resultMap.put("status",status);
        resultMap.put("msg",result.getBankResult());
        resultMap.put("orderId",transId);
        resultMap.put("signMsg",getMd5SignWithKey(resultMap,trade.getMerchantInfo().getSecretKey()));
        String resultjson = JsonUtils.objectToJson(resultMap);
        return resultjson;
    }

    /**
     *   处理失败返回参数
     * @param systemOrderTrack
     * @param tradeObjectSquare
     * @param trade
     * @param bankResult
     * @param e
     * @return
     */
    public String getReturnJson_1(SystemOrderTrack systemOrderTrack,TradeObjectSquare tradeObjectSquare,SquareTrade trade,BankResult bankResult,Exception e){
        String merOrderId ="";
        int status=1;
        String msg="";
        String transId="";
        String secretKey="";
        if(null != systemOrderTrack) merOrderId=systemOrderTrack.getMerOrderId();
        status=(null == bankResult ? status : bankResult.getStatus());
        msg=(null == bankResult ? e.getMessage() : bankResult.getBankResult());
        if( null != trade){
            TransOrder transOrder = trade.getTransOrder();
            if(null != transOrder){
                transId = transOrder.getTransId();
            }
        }
        if(null != systemOrderTrack && systemOrderTrack.getMerId() != null){
            //获取商户信息
            try{
                MerchantInfo merchantInfo = recordSquareService.getMerchantInfoByMerId(systemOrderTrack.getMerId());
                secretKey= merchantInfo.getSecretKey();
            }catch (Exception exception){
                exception.printStackTrace();
            }

        }
        Map<String, Object> resultMap = new TreeMap<>();
        resultMap.put("merOrderId",merOrderId);
        resultMap.put("status",status);
        resultMap.put("msg",msg);
        resultMap.put("orderId",transId);
        resultMap.put("signMsg",null != secretKey ? getMd5SignWithKey(resultMap,secretKey) : null);
        String resultjson = JsonUtils.objectToJson(resultMap);
        return resultjson;
    }


    /**
     *
     * @param trade
     * @return
     */
    private List<RiskQuotaData> getRiskQuotaData(SquareTrade trade){
        TransOrder transOrder = trade.getTransOrder();
        BigDecimal amount=transOrder.getAmount();
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
        riskQuotaData.setRefId(transOrder.getMerId());
        riskQuotaData.setRefType((short)2);// 1:通道 2:商户
        riskQuotaDataList= riskQuotaDataService.getRiskQuotaDataByWhereCondition(riskQuotaData);
        if(null != riskQuotaDataList && riskQuotaDataList.size() == 0){
            //yyyy-MM 级别
            riskQuotaData=new RiskQuotaData();
            riskQuotaData.setId(null);
            riskQuotaData.setRefId(transOrder.getMerId());
            riskQuotaData.setRefType((short)2);//商户级别
            riskQuotaData.setType((short)2);//月级别
            riskQuotaData.setTradeTime(month);
            riskQuotaData.setAmount(amount);
            newRiskQuotaDataList.add(riskQuotaData);
            //yyyy-MM-dd 级别
            riskQuotaData=new RiskQuotaData();
            riskQuotaData.setRefId(transOrder.getMerId());
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
     * 更新商户钱包钱包，只更新总可用余额和总冻结金额
     */
    public  MerchantWallet getPayWallet(SquareTrade trade) throws Exception {
        //商户信息
        MerchantInfo merchantInfo = trade.getMerchantInfo();
        //订单信息
        TransOrder transOrder = trade.getTransOrder();
        String merId=transOrder.getMerId();
        String merOrderId = transOrder.getMerOrderId();
        //获取商户钱包
        MerchantWallet merchantWallet=paymentRecordSquareService.getMerchantWallet(merchantInfo.getMerId());
        //订单金额
        BigDecimal orderAmount = transOrder.getAmount();
        //总可用余额
        BigDecimal  totalAvailableAmount=merchantWallet.getTotalAvailableAmount();
        if(null == totalAvailableAmount || totalAvailableAmount.compareTo(orderAmount)== -1)
            throw  new PayException(format("【海懿代付】商户号：%s,订单号：%s，商户钱包为空！",merId,merOrderId));

        //总可用余额=总可用余额-订单金额
        totalAvailableAmount=totalAvailableAmount.subtract(orderAmount);
        merchantWallet.setTotalAvailableAmount(totalAvailableAmount);
        //总冻结金额
        BigDecimal totalFreezeAmount=merchantWallet.getTotalFreezeAmount();
        totalFreezeAmount= (null ==totalFreezeAmount ? new BigDecimal(0) : totalFreezeAmount);
        //总冻结金额=总冻结金额+订单金额
        totalFreezeAmount=totalFreezeAmount.add(orderAmount);
        merchantWallet.setTotalFreezeAmount(totalFreezeAmount);
        //更新时间
        merchantWallet.setUpdateTime(new Date());

        return merchantWallet;
    }

    /**
     *   设置平台冻结资金
     * @return
     */
    public ChannelWallet getChannelWallet(SquareTrade trade) throws PayException {
        //订单信息
        TransOrder transOrder = trade.getTransOrder();
        String merId=transOrder.getMerId();
        String merOrderId = transOrder.getMerOrderId();
        ChannelInfo channelInfo=trade.getChannelInfo();

        ChannelWallet channelWallet=paymentRecordSquareService.getChannelWallet(channelInfo.getChannelId());
        isNull(channelWallet,format("【海懿代付】商户号：%s,订单号：%s，平台钱包为空！",merId,merOrderId));


        //订单金额
        BigDecimal orderAmount = transOrder.getAmount();
        //总可用余额
        BigDecimal  totalAvailableAmount=channelWallet.getTotalAvailableAmount();
        if(null == totalAvailableAmount || totalAvailableAmount.compareTo(orderAmount)== -1)
            throw  new PayException(format("【海懿代付】商户号：%s,订单号：%s，平台钱包余额不足！",merId,merOrderId));

        //总可用余额=总可用余额-订单金额
        totalAvailableAmount=totalAvailableAmount.subtract(orderAmount);
        channelWallet.setTotalAvailableAmount(totalAvailableAmount);
        //总冻结金额
        BigDecimal totalFreezeAmount=channelWallet.getTotalFreezeAmount();
        totalFreezeAmount= (null ==totalFreezeAmount ? new BigDecimal(0) : totalFreezeAmount);
        //总冻结金额=总冻结金额+订单金额
        totalFreezeAmount=totalFreezeAmount.add(orderAmount);
        channelWallet.setTotalFreezeAmount(totalFreezeAmount);
        //更新时间
        channelWallet.setUpdateTime(new Date());

        return channelWallet;
    }


    /**
     *   描述：发起代付
     *
     * @param trade
     */
    public BankResult haiYiPayForAnotherToPay(SquareTrade trade) throws PayException {
        String merId=trade.getTransOrder().getMerId();
        String merOrderId = trade.getTransOrder().getMerOrderId();
        try{
            String result = HttpClientUtils.doPostJson(HttpClientUtils.getHttpClient(), trade.getChannelInfo().getPayUrl(), JsonUtils.objectToJsonNonNull(trade));
            logger.info("【海懿代付】 代付请求结果：{}",result);
            isBlank(result,format("【海懿代付】商户号：%s,订单号：%s，请求cross响应结果为空！",merId,merOrderId));
            BankResult bankResult = JsonUtils.jsonToPojo(result, BankResult.class);
            isNull(bankResult,format("【海懿代付】商户号：%s,订单号：%s，转换BankResult异常！",merId,merOrderId));
            return bankResult;
        }catch ( Exception exception){
            throw exception;
        }
    }



    /**
     *  保存订单请求
     *    包含多个cty  catch 结构，只是方便跟踪代码，性能有所降低
     * @param systemOrderTrack
     * @param tradeObjectSquare
     * @return SquareTrade
     */
    public  SquareTrade haiYiPayForAnotherToSaveOrder(SystemOrderTrack systemOrderTrack, TradeObjectSquare tradeObjectSquare) throws Exception {

        String merId = tradeObjectSquare.getMerId();
        String merOrderId = tradeObjectSquare.getMerOrderId();
        //获取商户信息
//        MerchantInfo merchantInfo = recordSquareService.getMerchantInfoByMerId(merId);
        MerchantInfo merchantInfo = redisCacheCommonCompoment.merchantInfoCache.getOne(merId);
        isNull(merchantInfo, format("【海懿代付】商户号：%s,订单号：%s：未获取到商户信息！", merId, merOrderId));
        //签名验证
        CheckMd5Utils.checkMd5(systemOrderTrack.getTradeInfo(), merchantInfo.getSecretKey());
        //判断交易是否重复提交
        TransOrder transOrder = new TransOrder();
        transOrder.setMerId(merId);
        transOrder.setMerOrderId(merOrderId);
        List<TransOrder> item = paymentRecordSquareService.getTransOrderByWhereCondition(transOrder);
        isHasElement(item, format("【海懿代付】商户号：%s,订单号：%s：订单号已经存在！", merId, merOrderId));
        //代付金额不小于10
        if (tradeObjectSquare.getAmount().compareTo(new BigDecimal(10)) == -1) {
            throw new PayException(format("【海懿代付】商户号：%s,订单号：%s：代付金额小于10 RMB！", merId, merOrderId));
        }
        //获取所有支付机构信息
//        List<OrganizationInfo> organizationInfoList = organizationService.selectAll();
        List<OrganizationInfo> organizationInfoList = redisCacheCommonCompoment.organizationInfoCache.getAll();
        isNotElement(organizationInfoList, format("【海懿代付】商户号：%s,订单号：%s：获取支付机构表为空！", merId, merOrderId));
        //获取海懿机构信息
        OrganizationInfo organizationInfo = organizationInfoList.stream()
                .distinct()
                .filter(t -> t.getOrganizationName().contains("海懿"))
                .findFirst()
                .orElse(null);
        isNull(organizationInfo, format("【海懿代付】商户号：%s,订单号：%s：未获取到海懿机构信息！", merId, merOrderId));
        // 获取商户配置
//        MerchantSetting merchantSetting = merchantSquareSettingCache.getMerchantSettingByMerId(merId);
        MerchantSetting merchantSetting = redisCacheCommonCompoment.merchantSettingCache.getOne(merId);
        isNull(merchantSetting, format("【海懿代付】商户号：%s,订单号：%s：未获取商户配置信息！", merId, merOrderId));
        String[] organizationIds = (merchantSetting.getOrganizationId() == null) ? null : merchantSetting.getOrganizationId().split(",");
        isNull(organizationIds, format("【海懿代付】商户号：%s,订单号：%s：该商户未配置海懿相关代付信息！", merId, merOrderId));
        //查看商户是否配置海懿通道信息
        String haiYiOrganizationId = Arrays.stream(organizationIds)
                .filter(organizationId -> organizationId.equals(organizationInfo.getOrganizationId()))
                .findFirst().orElse(null);
        isNull(haiYiOrganizationId, format("【海懿代付】商户号：%s,订单号：%s：该商户未配置海懿相关代付信息！", merId, merOrderId));

        //获取商户钱包
        MerchantWallet merchantWallet = paymentRecordSquareService.getMerchantWallet(merId);
        isNull(merchantWallet, format("【海懿代付】商户号：%s,订单号：%s：商户钱包余额不足！", merId, merOrderId));
        //代付金额
        BigDecimal TransOrderAmount = systemOrderTrack.getAmount();
        //判断商户金额是否充足
        BigDecimal totalAvailableAmount = merchantWallet.getTotalAvailableAmount();
        totalAvailableAmount = (totalAvailableAmount == null ? new BigDecimal(0) : totalAvailableAmount);
        if (TransOrderAmount.compareTo(totalAvailableAmount) > 0)
            throw new PayException(format("【海懿代付】商户号：%s,订单号：%s：代付金额大于商户可用余额！", merId, merOrderId));
        // 商户风控对象
//        MerchantQuotaRisk merchantQuotaRisk = recordSquareService.getMerchantQuotaRiskByMerId(tradeObjectSquare.getMerId());
        MerchantQuotaRisk merchantQuotaRisk = redisCacheCommonCompoment.merchantQuotaRiskCache.getOne(tradeObjectSquare.getMerId());
        isNull(merchantQuotaRisk, format("【海懿代付】商户号：%s,订单号：%s：该商户风控对象不存在！", merId, merOrderId));
        //执行商户风控
        optimalChannelStrategy.riskManagement(merchantQuotaRisk, tradeObjectSquare);
        //获取通道信息
        ChannelInfo channelInfo = new ChannelInfo();
        channelInfo.setOrganizationId(organizationInfo.getOrganizationId());
        channelInfo.setType(4);//4 代表代付支付
//        List<ChannelInfo> channelInfoList = channelInfoService.selectByWhereCondition(channelInfo);
        List<ChannelInfo> channelInfoList =  redisCacheCommonCompoment.channelInfoCache.getAll()
                .stream()
                .filter(t->
                        t.getOrganizationId().equals(organizationInfo.getOrganizationId())
                        && t.getType() == 4
                )
                .distinct()
                .collect(Collectors.toList());
        isNotElement(channelInfoList, format("【海懿代付】商户号：%s,订单号：%s：未获取到有关海懿支付相通道构信息！", merId, merOrderId));
        //执行通道风控并选择最优通道
        channelInfo = optimalChannelStrategy.strategy(channelInfoList, tradeObjectSquare);

        //封装交易参数
        SquareTrade trade = new SquareTrade();
        //setting
        trade.setChannelInfo(channelInfo);
        trade.setMerchantInfo(merchantInfo);
        trade.setTradeObjectSquare(tradeObjectSquare);
        //获取结算业务收款账号信息
        TransBankInfo transBankInfo = this.getTransBankInfo(tradeObjectSquare);
        // 获取商户费率对象
//        MerchantRate merchantRate = merchantSquareRateCache.getMerchantRateByMerIdAndPayType(merchantInfo.getMerId(), channelInfo.getType().toString());
        MerchantRate merchantRate = redisCacheCommonCompoment.merchantRateCache.getOne(merchantInfo.getMerId(), channelInfo.getType().toString());
        transOrder = this.getTransOrder(systemOrderTrack, tradeObjectSquare, channelInfo, merchantRate, merchantInfo);
        systemOrderTrack.setOrderId(transOrder.getTransId());
        trade.setTransOrder(transOrder);
        TransAudit transAudit = this.getTransAudit(tradeObjectSquare, transOrder, trade);
        trade.setTransAudit(transAudit);

        PayMap<String, Object> map = new PayMap<>();
        map.lput("transAudit", transAudit)
                .lput("transOrder", transOrder)
                .lput("transBankInfo", transBankInfo);
        //事务保存三者信息
        int count = haiYiPayWalletService.saveTransOrderInfo(map);
        return trade;

    }


    /**
     * @param tradeObjectSquare
     * @param transOrder
     *     paymentRecordSquareService.saveOrUpadateTransAudit(transAudit);
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
     *
     * @param systemOrderTrack
     * @param tradeObjectSquare
     * @param channelInfo
     * @param merchantRate
     * @param merchantInfo
     * @return
     * @throws PayException
     *     paymentRecordSquareService.saveOrUpadateTransOrder(transOrder);
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
        BigDecimal rateFee = merchantRate.getRateFee() != null ? merchantRate.getRateFee().multiply(tradeObjectSquare.getAmount().divide(hundred)) : new BigDecimal(0);
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
     *   插入
     * @param tradeObjectSquare
     * @return
     *   paymentRecordSquareService.saveOrUpadateTransBankInfo(transBankInfo);
     */
    public TransBankInfo  getTransBankInfo(TradeObjectSquare tradeObjectSquare) {
        TransBankInfo transBankInfo = new TransBankInfo();
        transBankInfo.setTransId(com.rxh.utils.UUID.createKey("trans_bank_info", ""));
        transBankInfo.setBenefitName(tradeObjectSquare.getBenefitName());
        transBankInfo.setBankName(tradeObjectSquare.getInAcctName());
        transBankInfo.setBankcardNum(tradeObjectSquare.getInAcctNo());
        transBankInfo.setBankcardType(tradeObjectSquare.getBankCardType());
        transBankInfo.setBankBranchName(tradeObjectSquare.getBankBranchName());
        transBankInfo.setBankBranchNum(tradeObjectSquare.getBankBranchNum());
        transBankInfo.setIdentityType(tradeObjectSquare.getIdentityType());
        transBankInfo.setIdentityNum(tradeObjectSquare.getIdentityNum());
        return transBankInfo;
    }





    /**
     * 参数列表格式
     *
     *
     */
    private final static Map<String, ParamRule> allianceTransOrderPayValidate= new HashMap<String, ParamRule>() {
        {
            // 交易数据
            put("bizType", new ParamRule(REQUIRED, 10, 1003));
            put("charset", new ParamRule(REQUIRED, 10, 1004));
            put("signType", new ParamRule(REQUIRED, 10, 1004));
            put("merId", new ParamRule(REQUIRED, 10, 1005));//
            put("merOrderId", new ParamRule(REQUIRED, 32, 1006));
            put("amount", new ParamRule(MONEY, 1007));//
            put("inAcctNo", new ParamRule(REQUIRED, 19, 1013));//银行卡号
            put("inAcctName", new ParamRule(REQUIRED, 32, 1029));//银行卡持卡人
            put("noticeUrl", new ParamRule(REQUIRED, 128, 1013));
            put("signMsg", new ParamRule(REQUIRED, 256, 1013));//
        }
    };

    /**
     *  验证参数
     * @param systemOrderTrack
     * @return
     */
    public TradeObjectSquare verifyMustParamOnHaiYiPayForAnother(SystemOrderTrack systemOrderTrack) {
        TradeObjectSquare tradeObjectSquare =null;
        try{
            tradeObjectSquare = JsonUtils.jsonToPojo(systemOrderTrack.getTradeInfo(), TradeObjectSquare.class);
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
        }catch (Exception e){
            throw e;
        }finally {
            systemOrderTrack.setMerOrderId(tradeObjectSquare.getMerOrderId());
            systemOrderTrack.setMerId(tradeObjectSquare.getMerId());
            systemOrderTrack.setAmount(tradeObjectSquare.getAmount());
            systemOrderTrack.setReturnUrl(tradeObjectSquare.getReturnUrl() != null ? tradeObjectSquare.getReturnUrl():"");
            systemOrderTrack.setNoticeUrl(tradeObjectSquare.getNoticeUrl() != null ? tradeObjectSquare.getNoticeUrl():"");
            return tradeObjectSquare;
        }
    }


    /**
     *  成功返回参数
     * @param bankResult
     * @return
     */
    public String resultToString(BankResult bankResult, MerchantInfo merchantInfo, SquareTrade squareTrade){
        Map map = new HashMap();
        map.put("merId",merchantInfo.getMerId());
        map.put("merOrderId",squareTrade.getMerOrderId());
        map.put("status",bankResult.getStatus());
        map.put("msg",bankResult.getBankResult());
        map.put("orderId",squareTrade.getPayOrder().getPayId());
        map.put("amount",squareTrade.getPayOrder().getAmount());
        map.put("signMsg",getMd5SignWithKey(map,merchantInfo.getSecretKey()));
        return JSONObject.toJSONString(map);
    }


}
