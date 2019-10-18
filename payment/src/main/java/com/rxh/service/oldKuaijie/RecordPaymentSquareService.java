package com.rxh.service.oldKuaijie;

import com.rxh.exception.PayException;
import com.rxh.pojo.merchant.MerchantRegisterInfo;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.pojo.payment.TradeObjectSquare;
import com.rxh.pojo.sys.SysConstant;
import com.rxh.service.AbstractCommonService;
import com.rxh.square.pojo.*;
import com.rxh.utils.UUID;
import com.rxh.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: hul
 * Date: 2019/5/24
 * Time: 9:16
 * Project: Management
 * Package: com.rxh.service
 */
@Service
public class RecordPaymentSquareService extends AbstractCommonService {
    private Logger log = LoggerFactory.getLogger(getClass());




    /**
     * 获取商户，并判断商户是否是启用和禁用
     *
     * @param merId
     * @return
     */
    public MerchantInfo getMerchantInfoByMerId(String merId) throws PayException {
        MerchantInfo merchantInfo = paymentRecordSquareService.getMerchantInfoByMerId(merId);
        isNull(merchantInfo,format("商户号：%s,,未获取到商户卡信息",merId));
        if (SystemConstant.DIS_ENABLE.equals(merchantInfo.getStatus()))
            throw new PayException (format("商户号：%s,该商户处于禁用状态",merId));
        if (SystemConstant.DIS_ENABLE.equals(merchantInfo.getStatus()))
            throw new PayException(format("商户号：%s,该商户未审核",merId));
        return merchantInfo;
    }

    /**
     * @param systemOrderTrack
     * @param tradeObjectSquare
     * @param channelInfo
     * @param merchantRate
     * @param merchantInfo
     */
    public TransOrder saveOrUpadateTransOrder(SystemOrderTrack systemOrderTrack, TradeObjectSquare tradeObjectSquare, ChannelInfo channelInfo,
                                              MerchantRate merchantRate, MerchantInfo merchantInfo) {
        TransOrder transOrder = new TransOrder();
        transOrder.setTransId(UUID.createKey("trans_order", ""));
        transOrder.setMerId(tradeObjectSquare.getMerId());
        transOrder.setMerOrderId(tradeObjectSquare.getMerOrderId());
        transOrder.setChannelId(channelInfo.getChannelId());
        transOrder.setOrganizationId(channelInfo.getChannelId());
        transOrder.setPayType(String.valueOf(channelInfo.getType()));
        transOrder.setChannelTransCode(channelInfo.getChannelTransCode());
        transOrder.setCurrency(tradeObjectSquare.getCurrency());
        transOrder.setAmount(tradeObjectSquare.getAmount());
        transOrder.setRealAmount(tradeObjectSquare.getAmount());
        // 平台手续费 = 单笔手续费 + 通道金额 * 手续费率
        BigDecimal singlefee = merchantRate.getSingleFee() != null ? merchantRate.getSingleFee() : new BigDecimal(0);
        BigDecimal rateFee = merchantRate.getRateFee() != null ? merchantRate.getRateFee().multiply(tradeObjectSquare.getAmount()) : new BigDecimal(0);
        transOrder.setFee(singlefee.add(rateFee));
        // 代理商手续费：是否存在上级代理商
        if (StringUtils.isNotEmpty(merchantInfo.getParentId())) {
            AgentMerchantSetting agentMerchantSetting = paymentRecordSquareService.getAgentMerchantSettingByParentId(merchantInfo.getParentId());
            // 代理商手续费 = 单笔手续费 + 通道金额 * 手续费率
            BigDecimal agnetSinglefee = agentMerchantSetting.getSingleFee() != null ? agentMerchantSetting.getSingleFee(): new BigDecimal(0);
            BigDecimal agnetRateFee = agentMerchantSetting.getRateFee() != null ? agentMerchantSetting.getRateFee().multiply(tradeObjectSquare.getAmount()) : new BigDecimal(0);
            transOrder.setAgentFee(agnetSinglefee.add(agnetRateFee));
        } else {
            transOrder.setAgentFee(new BigDecimal(0));
        }
        // 通道手续费
        BigDecimal channelSinglefee = channelInfo.getChannelSingleFee() != null ? channelInfo.getChannelSingleFee() : new BigDecimal(0);
        BigDecimal channelRateFee = channelInfo.getChannelRateFee() != null ? channelInfo.getChannelRateFee().multiply(tradeObjectSquare.getAmount()) : new BigDecimal(0);
        transOrder.setChannelFee(channelSinglefee.add(channelRateFee));
        // 平台收入
        transOrder.setIncome(singlefee.add(rateFee));
        transOrder.setOrderStatus(SystemConstant.NO_PAY);
        transOrder.setSettleStatus(SystemConstant.NO_SETTLE);
        transOrder.setTradeTime(systemOrderTrack.getTradeTime());
        paymentRecordSquareService.saveOrUpadateTransOrder(transOrder);
        return transOrder;
    }

    /**
     * @param tradeObjectSquare
     */
    public void saveOrUpadateTransBankInfo(TradeObjectSquare tradeObjectSquare) {
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
        paymentRecordSquareService.saveOrUpadateTransBankInfo(transBankInfo);
    }

    /**
     * @param tradeObjectSquare
     * @param transOrder
     */
    public void saveOrUpadateTransAudit(TradeObjectSquare tradeObjectSquare, TransOrder transOrder) {
        TransAudit transAudit = new TransAudit();
        transAudit.setTransId(transOrder.getTransId());
        transAudit.setId(UUID.createKey("trans_audit", ""));
        transAudit.setMerId(tradeObjectSquare.getMerId());
        transAudit.setMerOrderId(tradeObjectSquare.getMerOrderId());
        transAudit.setCurrency(tradeObjectSquare.getCurrency());
        transAudit.setAmount(tradeObjectSquare.getAmount());
        transAudit.setStatus(SystemConstant.NO_PAY);
        paymentRecordSquareService.saveOrUpadateTransAudit(transAudit);
    }

    public void saveOrUpdateSystemOrderTrack(SystemOrderTrack systemOrderTrack) {
        paymentRecordSquareService.saveOrUpdateSystemOrderTrack(systemOrderTrack);
    }

    public List<ChannelInfo> getChannelByPayType(Map<String, Object> paramMap) {
        return paymentRecordSquareService.getChannelByPayType(paramMap);
    }


    public MerchantQuotaRisk getMerchantQuotaRiskByMerId(String merId) {
        return paymentRecordSquareService.getMerchantQuotaRiskByMerId(merId);
    }

    public void UpdateTransOrder(TransOrder transOrder) {
        paymentRecordSquareService.UpdateTransOrder(transOrder);

    }

    public Integer insertOrUpdateRiskQuotaData(List<RiskQuotaData> quotaDataList) {
        return paymentRecordSquareService.insertOrUpdateRiskQuotaData(quotaDataList);
    }

    public Integer insertRiskQuotaData(List<RiskQuotaData> quotaDataList) {
        return paymentRecordSquareService.insertRiskQuotaData(quotaDataList);
    }

    public SystemOrderTrack getSystemOrderTrack(String orderId) {
        return paymentRecordSquareService.getSystemOrderTrack(orderId);
    }

    public PayCardholderInfo getPayCardholderInfo(String merId) {
        return paymentRecordSquareService.getPayCardholderInfo(merId);
    }

    public SquareTrade getTrade(SystemOrderTrack systemOrderTrack, TradeObjectSquare tradeObjectSquare) throws PayException {
        // 查询商户信息
        MerchantInfo merchantInfo = getMerchantInfoByMerId(tradeObjectSquare.getMerId());
        // checkMd5(tradeObjectSquare,merchantInfo.getSecretKey());

        // 获取商户配置
        MerchantSetting merchantSetting = merchantSquareSettingCache.getMerchantSettingByMerId(tradeObjectSquare.getMerId());
        // 获取商户通道并进行栓选
        ChannelInfo channelInfo = getAndDressChannelInfo(merchantInfo);
        // 获取商户费率对象
        MerchantRate merchantRate = merchantSquareRateCache.getMerchantRateByMerIdAndPayType(merchantInfo.getMerId(), channelInfo.getType().toString());
        // 保存代付业务订单主表
        TransOrder transOrder = saveOrUpadateTransOrder(systemOrderTrack, tradeObjectSquare, channelInfo, merchantRate, merchantInfo);
        // 保存结算业务收款账号信息
        saveOrUpadateTransBankInfo(tradeObjectSquare);
        // 保存记录代付审核的信息
        saveOrUpadateTransAudit(tradeObjectSquare, transOrder);
        // 商户风控对象
        MerchantQuotaRisk merchantQuotaRisk = getMerchantQuotaRiskByMerId(tradeObjectSquare.getMerId());
        // analysis(channelInfo,transOrder,merchantQuotaRisk);
        //封装交易参数
        SquareTrade trade = new SquareTrade();
        trade.setChannelInfo(channelInfo);
        trade.setMerOrderId(tradeObjectSquare.getMerOrderId());
        trade.setMerchantInfo(merchantInfo);
        trade.setTransOrder(transOrder);

        return trade;
    }

    ;

    /**
     * 优先级：通道>机构
     *
     * @param merchantInfo
     * @return
     * @throws PayException
     */
    public ChannelInfo getAndDressChannelInfo(MerchantInfo merchantInfo) throws PayException {
        // 第一步： 获取用户所有的支付方式所对应的通道
        List<String> channelIds = getAllPayType(merchantInfo);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("merId", merchantInfo.getMerId());
        paramMap.put("channelIds", channelIds);
        List<ChannelInfo> channelInfos = getChannelByPayType(paramMap);

        //第二步：
        if (channelInfos.size() > 0) {
            //不为空，直接取第一个
            for (ChannelInfo channelInfo : channelInfos) {
                return channelInfo;
            }
        } else {
            throw new PayException("商户没有对应的通道！", 2006);
        }
        return null;
    }

    private List<String> getAllPayType(MerchantInfo merchantInfo) throws PayException {
        List<MerchantRate> merchantRates = merchantSquareRateCache.getByMerId(merchantInfo.getMerId());
        List<String> strings = new ArrayList<>();
        for (MerchantRate merchantRate : merchantRates) {
            strings.add(merchantRate.getPayType());
        }
        return strings;
    }


    public PayOrder saveOrUpadatePayOrder(SystemOrderTrack systemOrderTrack, TradeObjectSquare tradeObjectSquare, ChannelInfo channelInfo, MerchantRate merchantRate, MerchantInfo merchantInfo) throws PayException {

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
            if (agentMerchantSetting==null){
                throw  new PayException("代理商费率不存在","RXH00013");
            }
            // 代理商手续费 =  通道金额 * 手续费率
            BigDecimal agnetRateFee = agentMerchantSetting.getRateFee() != null ? agentMerchantSetting.getRateFee().multiply(tradeObjectSquare.getAmount().divide(hundred)) : new BigDecimal(0);
            payOrder.setAgentFee(agnetRateFee);
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
        payOrder.setRealAmount(payOrder.getAmount().subtract(payOrder.getTerminalFee()));
        payOrder.setTerminalMerId(tradeObjectSquare.getTerminalMerId());

        payOrder.setTerminalMerName(tradeObjectSquare.getTerminalMerName());

        paymentRecordSquareService.saveOrUpadatePayOrder(payOrder);
        return payOrder;
    }

    public void saveOrUpadateCardHolderInfo(TradeObjectSquare tradeObjectSquare, PayOrder payOrder) {

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
        paymentRecordSquareService.saveOrUpadatePayCardholderInfo(payCardholderInfo);
    }

    public MerchantWallet getMerchantWallet(String merId) {
        return paymentRecordSquareService.getMerchantWallet(merId);
    }

    public AgentWallet getAgentMerchantWallet(String parentId) {
        return paymentRecordSquareService.getAgentMerchantWallet(parentId);
    }

    public ChannelWallet getChannelWallet(String channelId) {
        return paymentRecordSquareService.getChannelWallet(channelId);
    }

    public MerchantRate getMerchantRate(String merId) {
        return paymentRecordSquareService.getMerchantRate(merId);
    }

    public AgentMerchantSetting getAgentMerchantSetting(String parentId) {
        return paymentRecordSquareService.getAgentMerchantSetting(parentId);
    }

    public ChannelInfo getChannelInfo(String channelId) {
        return paymentRecordSquareService.getChannelInfo(channelId);
    }

    public void UpdatePayOrder(PayOrder payOrder) {
        paymentRecordSquareService.UpdatePayOrder(payOrder);
    }

    public PayOrder getPayOrderById(String id) {
        return paymentRecordSquareService.getPayOrderById(id);
    }


    public void updateMerchantWallet(MerchantWallet merchantWallet) {
        paymentRecordSquareService.updateMerchantWallet(merchantWallet);
    }

    public void updateAgentWallet(AgentWallet agentWallet) {
        paymentRecordSquareService.updateAgentWallet(agentWallet);
    }

    public void updateChannelWallet(ChannelWallet channelWallet) {
        paymentRecordSquareService.updateChannelWallet(channelWallet);
    }

    public void saveOrUpadatePayProductDetail(TradeObjectSquare tradeObjectSquare, PayOrder payOrder) {
        PayProductDetail payProductDetail = new PayProductDetail();
        payProductDetail.setId(UUID.createKey("pay_product_detail", ""));
        payProductDetail.setPayId(payOrder.getPayId());
        payProductDetail.setMerOrderId(payOrder.getMerOrderId());
        payProductDetail.setPrice(payOrder.getAmount());
        payProductDetail.setProductName(tradeObjectSquare.getProductName());
    }

    public  List<PayOrder> getPayOrderByMerOrderId(TradeObjectSquare tradeObjectSquare, String merOrderId) {
        return paymentRecordSquareService.getPayOrderByMerOrderId(tradeObjectSquare.getMerId(), merOrderId);
    }
    public PayOrder getProcessingPayOrderByMerOrderId(TradeObjectSquare tradeObjectSquare, String merOrderId) {
        return paymentRecordSquareService.getProcessingPayOrderByMerOrderId(tradeObjectSquare.getMerId(), merOrderId);
    }

    public MerchantRate getMerchantRateByIdAndPayType(String merId, String payType) {
        return paymentRecordSquareService.getMerchantRateByIdAndPayType(merId, payType);
    }


    public MerchantCard checkBizType(TradeObjectSquare tradeObjectSquare) {

        return paymentRecordSquareService.checkBizType(tradeObjectSquare.getMerId(), tradeObjectSquare.getBizType());
    }

    public ExtraChannelInfo getChannelInfoByBizType(TradeObjectSquare tradeObjectSquare) {
        return paymentRecordSquareService.getChannelInfoByBizType(tradeObjectSquare.getBizType());
    }

    public MerchantCard saveMerchantCard(TradeObjectSquare tradeObjectSquare, ExtraChannelInfo extraChannelInfo) {
        MerchantCard param = new MerchantCard();
//        extraChannelInfo.
        param.setId(UUID.createKey("merchant_card", ""));
        param.setMerId(tradeObjectSquare.getMerId());
        param.setMerOrderId(tradeObjectSquare.getMerOrderId());
        param.setTerminalMerId(tradeObjectSquare.getTerminalMerId());
        param.setTerminalMerName(tradeObjectSquare.getTerminalMerName());
        param.setTradeFee(tradeObjectSquare.getPayFee());
        param.setBackFee(tradeObjectSquare.getBackFee());
        param.setName(tradeObjectSquare.getUserName());
        param.setIdentityNum(tradeObjectSquare.getIdentityNum());
        param.setPhone(tradeObjectSquare.getBankCardPhone());
        SysConstant bank = sysConstantCache.getBankCodeByBankName(tradeObjectSquare.getBankName());
        param.setCardType(tradeObjectSquare.getBankCardType().toString());
        param.setBankCode(bank.getFirstValue());
        if (tradeObjectSquare.getSecurityCode() != null) {
            param.setCvv(tradeObjectSquare.getSecurityCode());
        }
        if (tradeObjectSquare.getValidDate() != null) {
            param.setExpireDate(tradeObjectSquare.getValidDate());
        }
        param.setExtraChannelId(extraChannelInfo.getExtraChannelId());
        param.setPayType(Integer.valueOf(extraChannelInfo.getType()));
        param.setBackAcountNum(tradeObjectSquare.getBackCardNum());
        param.setBackAcountPhone(tradeObjectSquare.getBankCardPhone());
        param.setStatus(Integer.valueOf(SystemConstant.DIS_ENABLE));
        param.setTime(new Date());
        paymentRecordSquareService.saveMerchantCard(param);
        return param;
    }

    public void updateMerchantCard(MerchantCard merchantCard) {
        paymentRecordSquareService.updateMerchantCard(merchantCard);
    }



    public  List<ChannelInfo> getChannelInfos(MerchantSetting merchantSetting, TradeObjectSquare tradeObjectSquare) {
        String organizationId = ( merchantSetting.getOrganizationId() == null || merchantSetting.getOrganizationId().equals("") ) ?
                null : merchantSetting.getOrganizationId();
        String channelId = ( merchantSetting.getChannelId() == null || merchantSetting.getChannelId().equals("") ) ?
                null : merchantSetting.getChannelId();
        String[] ogIds = organizationId == null ? null : organizationId.split(",");
        String[] chIds = channelId == null ? null : channelId.split(",");
        String bizType = ( tradeObjectSquare.getBizType()==null||tradeObjectSquare.getBizType().equals("") )? null : tradeObjectSquare.getBizType();

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("payType", bizType);
        paramMap.put("ogIds", ogIds == null ? null : Arrays.asList(ogIds));
        paramMap.put("chIds", chIds == null ? null : Arrays.asList(chIds));
        List<ChannelInfo> channelInfos = this.getChannelByPayType(paramMap);
        channelInfos = checkChannelInfo(channelInfos);
        return channelInfos;
    }

    /**
     *
     * @param merchantSetting
     * @param tradeObjectSquare
     * @param allMerchantCard
     * @return
     * @throws PayException
     */
    public  Map<String, Object> getChannelInfosByPayType(MerchantSetting merchantSetting, TradeObjectSquare tradeObjectSquare, List<MerchantCard> allMerchantCard) throws PayException {

        String merId=tradeObjectSquare.getMerId();
        String terminalMerId=tradeObjectSquare.getTerminalMerId();
        //
        PayMap<String, Object> map = new PayMap<>();
        List<ExtraChannelInfo> extraChannelInfoList = paymentRecordSquareService.getChannelInfosByPayType(tradeObjectSquare);

        isNotElement(extraChannelInfoList,format("商户号：%s,未获取到附属通道信息",merId));
        //获取所有的绑的信息
        List<MerchantCard>  merchantCardList=redisCacheCommonCompoment.merchantCardCache.getMore(merId,terminalMerId,tradeObjectSquare.getBankCardNum());
        if(!isHasElement(merchantCardList)){
            MerchantCard record=new  MerchantCard();
            record.setMerId(merId);
            record.setTerminalMerId(terminalMerId);
            record.setCardNum(tradeObjectSquare.getBankCardNum());
            record.setStatus(0);
            merchantCardList=merchantCardService.select(record);
        }
        List<ExtraChannelInfo> list = new ArrayList<>();
        if(isHasElement(merchantCardList)){
            for(MerchantCard mcl:merchantCardList){
                for(ExtraChannelInfo ecil:extraChannelInfoList) {
                    if( !(mcl.getExtraChannelId().equals(ecil.getExtraChannelId()) ))
                        list.add(ecil);
                }
            }
        }else{
            list.addAll(extraChannelInfoList);
        }
        isNotElement(list,format("商户号：%s,未获取到可绑卡的附属通道信息！",merId));
        List<ExtraChannelInfo> paramList = new ArrayList<>();
        //进件记录
        List<ExtraChannelInfo> returnList = new ArrayList<>();
        //获取通道信息
        List<ChannelInfo> channelInfos = getChannelInfos(merchantSetting, tradeObjectSquare);
        isNotElement(channelInfos,format("商户号：%s,未获取到相关通道",merId));
        List<ChannelInfo> channellist = new ArrayList<>();
//        MerchantRegisterInfo merchantRegisterInfo=paymentRecordSquareService.getMerchantRegisterInfoByMerIdAndTerminalMerId(merId,terminalMerId);
        MerchantRegisterInfo merchantRegisterInfo=redisCacheCommonCompoment.merchantRegisterInfoCache.getAll()
                .stream()
                .filter( t-> t.getMerId().equals(merId) && t.getTerminalMerId().equals(terminalMerId))
                .distinct()
                .findAny()
                .orElse(null);

        isNull(merchantRegisterInfo,format("商户号：%s,未获取到相关进件信息",merId));
//        List<MerchantRegisterCollect> merchantRegisterCollects=paymentRecordSquareService.getmMrchantRegisterCollectByMerIdAndTerminalMerId(merId,terminalMerId,tradeObjectSquare.getBankCardNum());
        List<MerchantRegisterCollect> merchantRegisterCollects= redisCacheCommonCompoment.merchantRegisterCollectCache.getAll()
                .stream()
                .filter(t-> t.getMerId().equals(merId)
                        && t.getTerminalMerId().equals(terminalMerId)
//                        && t.getCardNum().equals(tradeObjectSquare.getBankCardNum())
                        && t.getStatus() ==0 )
                .distinct()
                .collect(Collectors.toList());

        isNotElement(merchantRegisterCollects,format("商户号：%s,未获取到已成功的进件信息",merId));
        map.lput("merchantRegisterInfo", merchantRegisterInfo)
                .lput("accessChannel", merchantRegisterCollects)
                .lput("allExtChanne", list);
        //过滤为已经进件成功的通道
        List<ExtraChannelInfo> registerList = new ArrayList<>();
        merchantRegisterCollects.forEach(item->{
            registerList.addAll(
                    list.stream().
                            filter(extraChannelInfo ->
                                    extraChannelInfo.getExtraChannelId().equals(item.getExtraChannelId()) &&
                                            extraChannelInfo.getType().equals(SystemConstant.ADDCUS))
                            .distinct().collect(Collectors.toList()));
        });
        //进件成功对应的支付通道
        registerList.forEach(extraChannelInfo->{
            channellist.addAll(channelInfos.stream().
                    filter(channelInfo ->
                            channelInfo.getOrganizationId().equals(extraChannelInfo.getOrganizationId()))
                    .distinct().collect(Collectors.toList()));
        });
        isNotElement(channellist,format("商户号：%s,无可用通道",merId));
        map.put("channelInfos", channellist);
        //所有绑卡通道
        channellist.forEach(item->{
            paramList.addAll(
                    list.stream().
                            filter(extraChannelInfo ->
                                    extraChannelInfo.getOrganizationId().equals(item.getOrganizationId())
                                            && extraChannelInfo.getType().equals(SystemConstant.BONDCARD))
                            .distinct().collect(Collectors.toList()));
        });
        if (allMerchantCard.size() == 0)  returnList.addAll(paramList);
        //过滤绑卡信息
        allMerchantCard.forEach(item->{
            returnList.addAll(
                    paramList.stream().
                            filter(extraChannelInfo ->
                                   ! (extraChannelInfo.getExtraChannelId().equals(item.getExtraChannelId())
                                            && item.getCardNum().equals(tradeObjectSquare.getBankCardNum())))
                            .distinct().collect(Collectors.toList()));

        });
        isNotElement(returnList,format("商户号：%s,无可用绑定通道",merId));
        map.put("extraChannelInfos", returnList);
        return map;
    }

    /**
     *
     * @param merchantSetting
     * @param tradeObjectSquare
     * @param allMerchantCard
     * @return
     * @throws PayException
     */
    public Map<String, Object> getChannelInfosByMerchantCards(MerchantSetting merchantSetting, TradeObjectSquare tradeObjectSquare, List<MerchantCard> allMerchantCard) throws PayException {


        Map<String, Object> map = new HashMap<>();
        List<ExtraChannelInfo> list = paymentRecordSquareService.getChannelInfosByPayType(tradeObjectSquare);
        List<ExtraChannelInfo> paramList = new ArrayList<>();
        List<ExtraChannelInfo> returnList = new ArrayList<>();
        List<ChannelInfo> channelInfos = getChannelInfos(merchantSetting, tradeObjectSquare);

        channelInfos=checkTradeChannel(channelInfos,tradeObjectSquare.getAmount());

        MerchantRegisterInfo merchantRegisterInfo=paymentRecordSquareService.getMerchantRegisterInfoByMerIdAndTerminalMerId(tradeObjectSquare.getMerId(), tradeObjectSquare.getTerminalMerId());

        List<MerchantRegisterCollect> merchantRegisterCollects=paymentRecordSquareService.getmMrchantRegisterCollectByMerIdAndTerminalMerId(tradeObjectSquare.getMerId(), tradeObjectSquare.getTerminalMerId(),tradeObjectSquare.getBankCardNum());
        if (merchantRegisterCollects.size() == 0) {
            throw new PayException("没有已成功的进件信息", 1000);
        }
        map.put("merchantRegisterInfo", merchantRegisterInfo);
        map.put("channelInfos", channelInfos);
        map.put("allExtChanne", list);
        if (allMerchantCard.size() == 0) {
            throw new PayException("无可用支付通道", 1000);
        }
        if (channelInfos.size() > 0) {
            for (ChannelInfo item : channelInfos) {
                paramList.addAll(list.stream().filter(extraChannelInfo ->
                        extraChannelInfo.getOrganizationId().equals(item.getOrganizationId())
                                && extraChannelInfo.getType().equals(SystemConstant.BONDCARD))
                        .collect(Collectors.toList()));
            }
            for (MerchantCard item : allMerchantCard) {
                returnList.addAll(paramList.stream().filter(extraChannelInfo ->
                        extraChannelInfo.getExtraChannelId().equals(item.getExtraChannelId()))
                        .collect(Collectors.toList()));
            }
            if (returnList.size() > 0) {
                map.put("extraChannelInfos", returnList);
                return map;
            } else {
                throw new PayException("无可用支付通道", 1000);
            }
        } else {
            throw new PayException("无可用通道", 1000);
        }
    }



    private List<ChannelInfo> checkTradeChannel(List<ChannelInfo> channelInfos, BigDecimal amount) {
        List<RiskQuotaData> list = new ArrayList<>();
        List<ChannelInfo> channelInfoList = new ArrayList<>();
        for (ChannelInfo item : channelInfos) {
            List<RiskQuotaData> channelQuotaData = riskQuotaDataCache.getChannelQuotaData(item.getChannelId());
            list.addAll(channelQuotaData);
        }
        if (list.size() == 0) {
            return channelInfos;
        }
        for (RiskQuotaData riskQuotaData : list) {
            channelInfoList.addAll(channelInfos.stream().filter(channelInfo -> channelInfo.getChannelId().equals(riskQuotaData.getRefId()) &&
                    (
                            (riskQuotaData.getType().equals(Short.valueOf("1")) && isExceed(channelInfo.getDayQuotaAmount().subtract(new BigDecimal(0.8)), channelInfo.getDayQuotaAmount().subtract(riskQuotaData.getAmount())))
                                    || (riskQuotaData.getType().equals(Short.valueOf("2")) && isExceed(channelInfo.getDayQuotaAmount().subtract(new BigDecimal(0.8)), channelInfo.getDayQuotaAmount().subtract(riskQuotaData.getAmount())))
                    )
                    &&isExceed(amount,channelInfo.getSingleMinAmount())&&isExceed(channelInfo.getSingleMaxAmount(),amount)
            ).collect(Collectors.toList()));
        }
        return channelInfoList;
    }

    List<ChannelInfo> checkChannelInfo(List<ChannelInfo> channelInfos) {
        List<RiskQuotaData> list = new ArrayList<>();
        List<ChannelInfo> channelInfoList = new ArrayList<>();
        for (ChannelInfo item : channelInfos) {
            List<RiskQuotaData> channelQuotaData = riskQuotaDataCache.getChannelQuotaData(item.getChannelId());
            list.addAll(channelQuotaData);
        }
        if (list.size() == 0) {
            return channelInfos;
        }
        for (RiskQuotaData riskQuotaData : list) {
            channelInfoList.addAll(channelInfos.stream().filter(channelInfo -> channelInfo.getChannelId().equals(riskQuotaData.getRefId()) &&
                    (
                            (riskQuotaData.getType().equals(Short.valueOf("1")) && isExceed(channelInfo.getDayQuotaAmount().subtract(new BigDecimal(0.8)), channelInfo.getDayQuotaAmount().subtract(riskQuotaData.getAmount())))
                                    || (riskQuotaData.getType().equals(Short.valueOf("2")) && isExceed(channelInfo.getDayQuotaAmount().subtract(new BigDecimal(0.8)), channelInfo.getDayQuotaAmount().subtract(riskQuotaData.getAmount())))
                    )
            ).collect(Collectors.toList()));
        }
        return channelInfoList;
    }



    private boolean isExceed(BigDecimal amount, BigDecimal target) {
        return amount.compareTo(target) > 0;
    }

    /**
     *  保存绑卡信息
     * @param tradeObjectSquare
     * @param map
     * @return
     */
    public  MerchantCard insertMerchantCard(TradeObjectSquare tradeObjectSquare, Map<String, Object> map) {
        List<ExtraChannelInfo> extraChannelInfos = (List<ExtraChannelInfo>) map.get("extraChannelInfos");
//        String merId = tradeObjectSquare.getMerId();
//        List<MerchantCard> allMerchantCard = getAllMerchantCard(merId, tradeObjectSquare.getTerminalMerId(), SystemConstant.SUCCESS);
        ExtraChannelInfo extraChannelInfo = extraChannelInfos.get(new Random().nextInt(extraChannelInfos.size()));
        MerchantCard param = new MerchantCard();
        param.setId(UUID.createKey("merchant_card", ""));
        param.setMerId(tradeObjectSquare.getMerId());
        param.setMerOrderId(tradeObjectSquare.getMerOrderId());
        param.setTerminalMerId(tradeObjectSquare.getTerminalMerId());
        param.setTerminalMerName(tradeObjectSquare.getTerminalMerName());
        param.setTradeFee(tradeObjectSquare.getPayFee());
        param.setBackFee(tradeObjectSquare.getBackFee());
        param.setName(tradeObjectSquare.getUserName());
        param.setIdentityNum(tradeObjectSquare.getIdentityNum());
        param.setPhone(tradeObjectSquare.getBankCardPhone());
        param.setCardNum(tradeObjectSquare.getBankCardNum());
//        SysConstant bank = sysConstantCache.getBankCodeByBankName(tradeObjectSquare.getBankName());
        param.setCardType(tradeObjectSquare.getBankCardType().toString());
        param.setBankCode(tradeObjectSquare.getBankCode());
        if (tradeObjectSquare.getSecurityCode() != null) {
            param.setCvv(tradeObjectSquare.getSecurityCode());
        }
        if (tradeObjectSquare.getValidDate() != null) {
            param.setExpireDate(tradeObjectSquare.getValidDate());
        }
        param.setExtraChannelId(extraChannelInfo.getExtraChannelId());
        param.setPayType(Integer.valueOf(extraChannelInfo.getType()));
        param.setBackAcountNum(tradeObjectSquare.getBackCardNum());
        param.setBackAcountPhone(tradeObjectSquare.getBankCardPhone());
        param.setStatus(Integer.valueOf(SystemConstant.BANK_STATUS_FAIL));
        param.setTime(new Date());
        paymentRecordSquareService.saveMerchantCard(param);
        return param;
    }

    public List<MerchantCard> getAllMerchantCard(String merId, String terminalMerId, String status) {
        return paymentRecordSquareService.getAllMerchantCard(merId, terminalMerId, status);
    }

    public ChannelInfo chooseChannel(MerchantSetting merchantSetting, TradeObjectSquare tradeObjectSquare, Map<String, Object> param, List<MerchantCard> merchantCards) throws PayException {

        List<ChannelInfo> channelInfos = (List<ChannelInfo>) param.get("channelInfos");
        List<ExtraChannelInfo> extraChannelInfos = (List<ExtraChannelInfo>) param.get("extraChannelInfos");
        ArrayList<ChannelInfo> list = new ArrayList<>();


        if (channelInfos.size() > 0) {
            for (ExtraChannelInfo item : extraChannelInfos) {
                list.addAll(channelInfos.stream().filter(channelInfo -> channelInfo.getOrganizationId().equals(item.getOrganizationId())).collect(Collectors.toList()));
            }
            if (list.size() > 0) {
                return list.get(0);
            } else {
                throw new PayException("没有可用支付通道", 1006);
            }
        } else {
            throw new PayException("没有可用支付通道", 1006);
        }

    }

    public ExtraChannelInfo chooseExtraChannelInfo(ChannelInfo channelInfo, Map<String, Object> param) throws PayException {
        List<ExtraChannelInfo> extraChannelInfos = (List<ExtraChannelInfo>) param.get("extraChannelInfos");
        return extraChannelInfos.stream().filter(extraChannelInfo -> extraChannelInfo.getOrganizationId().equals(channelInfo.getOrganizationId())).findFirst().orElseThrow(() -> new PayException("通道不存在或未启用", 2005));
    }

    public MerchantCard chooseMerchantCard(ExtraChannelInfo extraChannelInfo, TradeObjectSquare tradeObjectSquare, List<MerchantCard> merchantCards) throws PayException {
        return merchantCards.stream().filter(merchantCard ->
                merchantCard.getExtraChannelId().equals(extraChannelInfo.getExtraChannelId())
                        && merchantCard.getMerId().equals(tradeObjectSquare.getMerId())
                        && merchantCard.getCardNum().equals(tradeObjectSquare.getBankCardNum())
                 )
                .findFirst()
                .orElseThrow(() -> new PayException("通道不存在或未启用", 2005));
    }

    public TerminalMerchantsWallet getTerminalMerchantsWallet(String merId, String terminalMerId) {
        return paymentRecordSquareService.getTerminalMerchantsWallet(merId, terminalMerId);
    }

    public ExtraChannelInfo getExtraChannelInfo(Map<String, Object> extraChannelInfos, MerchantCard merchantCard) {
        List<ExtraChannelInfo> extraChannelInfolist = (List<ExtraChannelInfo>) extraChannelInfos.get("extraChannelInfos");
        return extraChannelInfolist.stream()
                .filter(extraChannelInfo ->
                        extraChannelInfo.getExtraChannelId().equals(merchantCard.getExtraChannelId()))
                .findFirst()
                .get();
    }

    public void insertTerminalMerchantsDetails(TerminalMerchantsDetails terminalMerchantsDetails) {
        paymentRecordSquareService.insertTerminalMerchantsDetails(terminalMerchantsDetails);
    }

    public void updateTerminalMerchantsWallet(TerminalMerchantsWallet terminalMerchantsWallet) {
        paymentRecordSquareService.updateTerminalMerchantsWallet(terminalMerchantsWallet);
    }

    public void insertTerminalMerchantsWallet(TerminalMerchantsWallet terminalMerchantsWallet) {
        paymentRecordSquareService.insertTerminalMerchantsWallet(terminalMerchantsWallet);
    }

    public void insertMerchantsDetails(MerchantsDetails merchantsDetails) {
        paymentRecordSquareService.insertMerchantsDetails(merchantsDetails);
    }

    public void insertChannelDetails(ChannelDetails channelDetails) {
        paymentRecordSquareService.insertChannelDetails(channelDetails);
    }

    public void insertAgentMerchantsDetails(AgentMerchantsDetails agentMerchantsDetails) {
        paymentRecordSquareService.insertAgentMerchantsDetails(agentMerchantsDetails);
    }


    public MerchantCard getMerchantCardByMerIdAndMerOrderId(String merId, String merOrderId) {
        return paymentRecordSquareService.getMerchantCardByMerIdAndMerOrderId(merId, merOrderId);
    }


    public ExtraChannelInfo getExtraChannelInfoByExtraChannelId(String extraChannelId) {

        return paymentRecordSquareService.getExtraChannelInfoByExtraChannelId(extraChannelId);
    }

    public MerchantCard getMerchantCardByMerIdAndMerOrderIdAndTermId(String merId, String merOrderId, String terminalMerId, short status) {
        return paymentRecordSquareService.getMerchantCardByMerIdAndMerOrderIdAndTermId(merId, merOrderId, terminalMerId, status);
    }

    public ExtraChannelInfo getExtraChannelInfoByInChannelId(String channelId) {
        return paymentRecordSquareService.getExtraChannelInfoByInChannelId(channelId);
    }

    public MerchantCard getMerchantCardByMerIdAndTermIdAndExtraId(String merId, String terminalMerId, String extraChannelId) {
        return paymentRecordSquareService.getMerchantCardByMerIdAndTermIdAndExtraId(merId, terminalMerId, extraChannelId);
    }

    public MerchantRegisterInfo getMerchantRegisterInfo(ExtraChannelInfo channelInfo, Map<String, Object> extraChannelInfos) {
        return (MerchantRegisterInfo) extraChannelInfos.get("merchantRegisterInfo");

    }

    public MerchantRegisterInfo getMerchantRegisterInfoByParam( ExtraChannelInfo exChannel, TradeObjectSquare tradeObjectSquare) {
        List<ExtraChannelInfo> list = paymentRecordSquareService.getChannelInfosByPayType(tradeObjectSquare);
        List<MerchantRegisterInfo> merchantRegisterInfos = paymentRecordSquareService.getMerchantRegisterInfos(tradeObjectSquare.getMerId(), tradeObjectSquare.getTerminalMerId(), SystemConstant.SUCCESS);

        ExtraChannelInfo extraChannel = list.stream().filter(extraChannelInfo -> extraChannelInfo.getType().equals(SystemConstant.ADDCUS) && extraChannelInfo.getOrganizationId().equals(exChannel.getOrganizationId())).findFirst().get();
        return merchantRegisterInfos.stream().filter(merchantRegisterInfo -> merchantRegisterInfo.getExtraChannelId().equals(extraChannel.getExtraChannelId())).findFirst().get();

    }

    public MerchantChannelHistory getLastUseChannel(TradeObjectSquare tradeObjectSquare) {
        return paymentRecordSquareService.getLastUseChannel(tradeObjectSquare);
    }

//    public Boolean checkChannel(ChannelInfo oldChannnel) {
//        List<RiskQuotaData> channelQuotaData = riskQuotaDataCache.getChannelQuotaData(oldChannnel.getChannelId());
//        if (channelQuotaData.size() == 0) {
//            return true;
//        }
//
//        int size = channelQuotaData.size();
//        int i=0;
//        for (RiskQuotaData riskQuotaData : channelQuotaData) {
//          if (oldChannnel.getChannelId().equals(riskQuotaData.getRefId()) &&
//        (
//        (riskQuotaData.getType().equals(Short.valueOf("1")) && isExceed(oldChannnel.getDayQuotaAmount().subtract(new BigDecimal(0.8)), oldChannnel.getDayQuotaAmount().subtract(riskQuotaData.getAmount())))
//        || (riskQuotaData.getType().equals(Short.valueOf("2")) && isExceed(oldChannnel.getDayQuotaAmount().subtract(new BigDecimal(0.8)), oldChannnel.getDayQuotaAmount().subtract(riskQuotaData.getAmount())))
//        )
//            ){
//             i++;
//          };
//        }
//        if (i==size){
//            return true;
//        }
//
//        return false;
//    }

    public Boolean checkChannelByParam(ChannelInfo oldChannnel,BigDecimal amount) {
        List<RiskQuotaData> channelQuotaData = riskQuotaDataCache.getChannelQuotaData(oldChannnel.getChannelId());
        if (channelQuotaData.size() == 0) {
            return true;
        }
        int size = channelQuotaData.size();
        int i=0;
        for (RiskQuotaData riskQuotaData : channelQuotaData) {
            if (oldChannnel.getChannelId().equals(riskQuotaData.getRefId()) &&
                    ((riskQuotaData.getType().equals(Short.valueOf("1")) && isExceed(oldChannnel.getDayQuotaAmount().subtract(new BigDecimal(0.8)), oldChannnel.getDayQuotaAmount().subtract(riskQuotaData.getAmount())))
                            || (riskQuotaData.getType().equals(Short.valueOf("2")) && isExceed(oldChannnel.getDayQuotaAmount().subtract(new BigDecimal(0.8)), oldChannnel.getDayQuotaAmount().subtract(riskQuotaData.getAmount()))))
                    &&isExceed(amount,oldChannnel.getSingleMinAmount())&&isExceed(oldChannnel.getSingleMaxAmount(),amount) ){
                i++;
            }
        }
        if (i==size){
            return true;
        }
        return false;
    }


    public ExtraChannelInfo getExtraChannelInfoByOrgId(String orgId, String type) {
        return paymentRecordSquareService.getExtraChannelInfoByOrgId(orgId,type);
    }


    /**
     *
     * @param extraChannelInfos
     * @param extraChannelInfo
     * @return
     */
    public MerchantRegisterCollect getMerchantRegisterCollectByMap(Map<String, Object> extraChannelInfos,ExtraChannelInfo extraChannelInfo) {
        //进件成功的通道
        List<MerchantRegisterCollect> merchantRegisterCollects= (List<MerchantRegisterCollect>) extraChannelInfos.get("accessChannel");
//        List<ChannelInfo> channelInfos= (List<ChannelInfo>) extraChannelInfos.get("channelInfos");
        List<ExtraChannelInfo> allExtChanne= (List<ExtraChannelInfo>) extraChannelInfos.get("allExtChanne");

        //通过绑卡获取进件信息
        ExtraChannelInfo ExtChanne= allExtChanne.stream()
                .filter(extraChannelInfo1 ->
                        extraChannelInfo1.getOrganizationId().equals(extraChannelInfo.getOrganizationId())
                                && extraChannelInfo1.getType().equals(SystemConstant.ADDCUS))
                .findFirst()
                .get();
        return merchantRegisterCollects.stream()
                .filter(merchantRegisterCollect ->
                        merchantRegisterCollect.getExtraChannelId().equals(ExtChanne.getExtraChannelId()))
                .findFirst()
                .get();
    }

    public MerchantRegisterInfo searchMerchantRegisterInfoByParam(TradeObjectSquare tradeObjectSquare) {
        return    paymentRecordSquareService.getMerchantRegisterInfoByMerIdAndTerminalMerId(tradeObjectSquare.getMerId(),tradeObjectSquare.getTerminalMerId());
    }

    /**
     *
     * @param extraChannelInfo
     * @param merchantRegisterInfo
     * @param tradeObjectSquare
     * @return
     */
    public MerchantRegisterCollect searchMerchantRegisterCollect( ExtraChannelInfo extraChannelInfo,MerchantRegisterInfo merchantRegisterInfo, TradeObjectSquare tradeObjectSquare) {
//        List<MerchantRegisterCollect> merchantRegisterCollects=paymentRecordSquareService .getmMrchantRegisterCollectByMerIdAndTerminalMerId(
//                        tradeObjectSquare.getMerId(),
//                        tradeObjectSquare.getTerminalMerId(),
//                        tradeObjectSquare.getBankCardNum());
        List<MerchantRegisterCollect> merchantRegisterCollects=redisCacheCommonCompoment.merchantRegisterCollectCache.getAll().stream()
                .filter( t-> t.getMerId().equals(tradeObjectSquare.getMerId())
                        &&   t.getTerminalMerId().equals(tradeObjectSquare.getTerminalMerId())
//                        &&   t.getCardNum().equals(tradeObjectSquare.getBankCardNum())
                        &&   t.getStatus() == 0
                )
                .distinct()
                .collect(Collectors.toList());

        //根据绑卡id获取进件id
        String organizationId = extraChannelInfo.getOrganizationId();
//        ExtraChannelInfo extraChannel = paymentRecordSquareService.getExtraChannelInfoByOrgId(organizationId, SystemConstant.ADDCUS);
        ExtraChannelInfo extraChannel =redisCacheCommonCompoment.extraChannelInfoCache.getAll()
                .stream()
                .filter(t-> t.getOrganizationId().equals(organizationId) && t.getType().equals( SystemConstant.ADDCUS))
                .findAny()
                .orElse(null);

        return merchantRegisterCollects.stream()
                .filter(merchantRegisterCollect ->
                        merchantRegisterCollect.getExtraChannelId().equals(extraChannel.getExtraChannelId()))
                .findFirst()
                .get();
    }

    public PayOrder getPayOrderByMerOrderIdAndMerIdAndTerMerId(String merOrderId, String merId, String terminalMerId) {
        return paymentRecordSquareService.getPayOrderByMerOrderIdAndMerIdAndTerMerId(merOrderId,merId,terminalMerId);
    }

    public MerchantRegisterCollect getMerchantRegisterCollectbyParam(String extraChannelId, String merId, String merOrderId) {

        return paymentRecordSquareService.getMerchantRegisterCollectbyParam(extraChannelId,merId,merOrderId);

    }

    public MerchantRegisterCollect searchMerchantRegisterCollectToSms(ExtraChannelInfo extraChannelInfo, PayOrder payOrder, TradeObjectSquare tradeObjectSquare) {
        PayCardholderInfo payCardholderInfo = paymentRecordSquareService.getPayCardholderInfoByPayId(payOrder.getPayId());
        List<MerchantRegisterCollect> merchantRegisterCollects=redisCacheCommonCompoment.merchantRegisterCollectCache.getAll()
                .stream()
                .filter(t->
                        t.getMerId().equals(tradeObjectSquare.getMerId())
                        && t.getTerminalMerId().equals(tradeObjectSquare.getTerminalMerId())
                        && t.getStatus() == 0
//                        && t.getCardNum().equals(payCardholderInfo.getBankcardNum())
                )
                .distinct()
                .collect(Collectors.toList());
//        List<MerchantRegisterCollect> merchantRegisterCollects=paymentRecordSquareService.getmMrchantRegisterCollectByMerIdAndTerminalMerId(
//                tradeObjectSquare.getMerId(),
//                tradeObjectSquare.getTerminalMerId(),payCardholderInfo.getBankcardNum());
        //根据绑卡id获取进件id
        String organizationId = extraChannelInfo.getOrganizationId();
//        ExtraChannelInfo extraChannel = paymentRecordSquareService.getExtraChannelInfoByOrgId(organizationId, SystemConstant.ADDCUS);
        ExtraChannelInfo extraChannel = redisCacheCommonCompoment.extraChannelInfoCache.getAll()
                .stream()
                .filter(t-> t.getOrganizationId().equals(organizationId) && t.getType().equals( SystemConstant.ADDCUS))
                .distinct()
                .findAny()
                .orElse(null);

        return merchantRegisterCollects
                .stream()
                .filter(merchantRegisterCollect ->
                        merchantRegisterCollect.getExtraChannelId().equals(extraChannel.getExtraChannelId()))
                .findFirst()
                .get();
    }

    public AgentMerchantSetting getAgentMerchantSettingByParentIdAndPayType(String parentId, String payType) {
        return paymentRecordSquareService.getAgentMerchantSettingByParentIdAndPayType(parentId,payType);
    }

    public void insertMerchantWallet(MerchantWallet merchantWallet) {
        paymentRecordSquareService.insertMerchantWallet(merchantWallet);
    }

    public void insertChannelWallet(ChannelWallet channelWallet) {
        paymentRecordSquareService.insertChannelWallet(channelWallet);
    }

    public void insertAgentWallet(AgentWallet agentWallet) {
        paymentRecordSquareService.insertAgentWallet(agentWallet);
    }

    public AgentMerchantsDetails getAgentMerchantsDetails(String payId) {
        return paymentRecordSquareService.getAgentMerchantsDetails(payId);
    }

    public ChannelDetails getChannelDetails(String orderId) {
        return paymentRecordSquareService.getChannelDetails(orderId);
    }

    public TerminalMerchantsDetails getTerminalMerchantsDetails(String orderId) {
        return paymentRecordSquareService.getTerminalMerchantsDetails(orderId);
    }

    public MerchantsDetails getMerchantDetails(String orderId) {
        return paymentRecordSquareService.getMerchantDetails(orderId);
    }

    public MerchantCard getMerchantCardByMerIdAndMerOrderIdAndTermIdAndCardNum(String merId, String merOrderId, String terminalMerId, String bankCardNum, short status) {
        return paymentRecordSquareService.getMerchantCardByMerIdAndMerOrderIdAndTermIdAndCardNum(merId, merOrderId, terminalMerId, bankCardNum,status);
    }

    public ChannelInfo getChannelInfosByMerchantSettingAndType(MerchantSetting merchantSetting, TradeObjectSquare tradeObjectSquare, Integer channelXinsheng) throws PayException {
        String organizationId = ( merchantSetting.getOrganizationId() == null || merchantSetting.getOrganizationId().equals("") ) ?
                null : merchantSetting.getOrganizationId();
        String channelId = ( merchantSetting.getChannelId() == null || merchantSetting.getChannelId().equals("") ) ?
                null : merchantSetting.getChannelId();
        String[] ogIds = organizationId == null ? null : organizationId.split(",");
        String[] chIds = channelId == null ? null : channelId.split(",");
        String bizType = ( tradeObjectSquare.getBizType()==null||tradeObjectSquare.getBizType().equals("") )? null : tradeObjectSquare.getBizType();
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("payType", bizType);
        paramMap.put("ogIds", ogIds == null ? null : Arrays.asList(ogIds));
        paramMap.put("chIds", chIds == null ? null : Arrays.asList(chIds));
        List<ChannelInfo> channelInfos = this.getChannelByPayType(paramMap);
        logger.info("【无验证快捷余额查询通道集合】 :"+JsonUtils.objectToJson(channelInfos));

        if (channelInfos.size()==0){
            throw new PayException("无验证快捷余额查询获取道的通道集合为空",1100);
        }
        ChannelInfo channel = channelInfos.stream().filter(channelInfo -> channelInfo.getType().equals(channelXinsheng)).findFirst().get();
        return channel;
    }
}
