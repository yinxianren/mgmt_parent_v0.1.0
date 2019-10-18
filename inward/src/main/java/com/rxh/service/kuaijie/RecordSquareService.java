package com.rxh.service.kuaijie;

import com.rxh.exception.PayException;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.merchant.MerchantRegisterInfo;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.pojo.payment.TradeObjectSquare;
import com.rxh.service.AbstractPayService;
import com.rxh.square.pojo.*;
import com.rxh.utils.JsonUtils;
import com.rxh.utils.StringUtils;
import com.rxh.utils.SystemConstant;
import com.rxh.utils.UUID;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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
public class RecordSquareService extends AbstractPayService {




    /**
     * 获取商户，并判断商户是否是启用和禁用
     *
     * @param merId
     * @return
     */
    public MerchantInfo getMerchantInfoByMerId(String merId) throws PayException {
        MerchantInfo merchantInfo = paymentRecordSquareService.getMerchantInfoByMerId(merId);
        if (merchantInfo == null) {
            throw new PayException("该商户不存在", 2000);
        }
        if (SystemConstant.DIS_ENABLE.equals(merchantInfo.getStatus())) {
            throw new PayException("该商户处于禁用状态", 2001);
        }
        if (SystemConstant.DIS_ENABLE.equals(merchantInfo.getStatus())) {
            throw new PayException("该商户未审核", 2002);
        }
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
            AgentMerchantSetting agentMerchantSetting = paymentRecordSquareService.getAgentMerchantSettingByParentIdAndPayType(merchantInfo.getParentId(),tradeObjectSquare.getBizType());
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

        paymentRecordSquareService.saveOrUpadateTransOrder(transOrder);
        return transOrder;
    }

    /**
     * @param tradeObjectSquare
     */
    public int saveOrUpadateTransBankInfo(TradeObjectSquare tradeObjectSquare) {
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
        return paymentRecordSquareService.saveOrUpadateTransBankInfo(transBankInfo);
    }

    /**
     * @param tradeObjectSquare
     * @param transOrder
     */
    public TransAudit saveOrUpadateTransAudit(TradeObjectSquare tradeObjectSquare, TransOrder transOrder, SquareTrade trade) {
        TransAudit transAudit = new TransAudit();
        transAudit.setTransId(transOrder.getTransId());
        transAudit.setId(UUID.createKey("trans_audit", ""));
        transAudit.setMerId(tradeObjectSquare.getMerId());
        transAudit.setMerOrderId(tradeObjectSquare.getMerOrderId());
        transAudit.setCurrency(tradeObjectSquare.getCurrency());
        transAudit.setAmount(tradeObjectSquare.getAmount());
        transAudit.setStatus(SystemConstant.NO_PAY);
        transAudit.setTrade(JsonUtils.objectToJsonNonNull(trade));
        paymentRecordSquareService.saveOrUpadateTransAudit(transAudit);
        return transAudit;
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

    public int  batchUpdateSuccessOrderStatus(List<String> merOrderIds){
        return paymentRecordSquareService.batchUpdateSuccessOrderStatus(merOrderIds);
    }

    public int  batchUpdateFailOrderStatus(List<String> merOrderIds){
        return paymentRecordSquareService.batchUpdateFailOrderStatus(merOrderIds);
    }

    public Integer insertOrUpdateRiskQuotaData(List<RiskQuotaData> quotaDataList) {
        return paymentRecordSquareService.insertOrUpdateRiskQuotaData(quotaDataList);
    }

    public Integer insertRiskQuotaData(List<RiskQuotaData> quotaDataList) {
        return paymentRecordSquareService.insertRiskQuotaData(quotaDataList);
    }

    public SystemOrderTrack getSystemOrderTrack(String merOrderId) {
        return paymentRecordSquareService.getSystemOrderTrack(merOrderId);
    }

    public PayCardholderInfo getPayCardholderInfo(String merId) {
        return paymentRecordSquareService.getPayCardholderInfo(merId);
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

    public void payment(SystemOrderTrack systemOrderTrack, TradeObjectSquare tradeObjectSquare) throws PayException {

        TransOrder item = paymentRecordSquareService.getTransOrderByMerOrderId(tradeObjectSquare.getMerId(), systemOrderTrack.getMerOrderId());
        if (item != null) {
            throw new PayException("该笔订单已存在", 3003);
        }

        // 查询商户信息
        MerchantInfo merchantInfo = getMerchantInfoByMerId(tradeObjectSquare.getMerId());
        // checkMd5(tradeObjectSquare,merchantInfo.getSecretKey());
        // 获取商户配置
        MerchantSetting merchantSetting = merchantSquareSettingCache.getMerchantSettingByMerId(tradeObjectSquare.getMerId());
        // 获取商户通道并进行栓选
        ChannelInfo channelInfo = getAndDressChannelInfo(merchantInfo, merchantSetting, tradeObjectSquare.getBizType());
        // 获取商户费率对象
        MerchantRate merchantRate = merchantSquareRateCache.getMerchantRateByMerIdAndPayType(merchantInfo.getMerId(), channelInfo.getType().toString());
        // 保存代付业务订单主表
        TransOrder transOrder = saveOrUpadateTransOrder(systemOrderTrack, tradeObjectSquare, channelInfo, merchantRate, merchantInfo);
        // 保存结算业务收款账号信息
        saveOrUpadateTransBankInfo(tradeObjectSquare);
        //封装交易参数
        SquareTrade trade = new SquareTrade();
        trade.setChannelInfo(channelInfo);
        trade.setMerOrderId(tradeObjectSquare.getMerOrderId());
        trade.setMerchantInfo(merchantInfo);
        trade.setTransOrder(transOrder);
        trade.setTradeObjectSquare(tradeObjectSquare);
        // 保存记录代付审核的信息
        saveOrUpadateTransAudit(tradeObjectSquare, transOrder, trade);
        // 商户风控对象
        MerchantQuotaRisk merchantQuotaRisk = getMerchantQuotaRiskByMerId(tradeObjectSquare.getMerId());
        analysis(channelInfo, transOrder, merchantQuotaRisk);
    }

    ;

    /**
     * 优先级：通道>机构
     *
     * @param merchantInfo
     * @return
     * @throws PayException
     */
    public ChannelInfo getAndDressChannelInfo(MerchantInfo merchantInfo, MerchantSetting merchantSetting, String payType) throws PayException {
        // 第一步： 获取用户所有的支付方式所对应的通道
        String organizationId = merchantSetting.getOrganizationId();
        String channelId = merchantSetting.getChannelId();
        String[] ogIds = organizationId == null ? null : organizationId.split(",");
        String[] chIds = channelId == null ? null : channelId.split(",");
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("payType", payType);
        paramMap.put("ogIds", ogIds == null ? null : Arrays.asList(ogIds));
        paramMap.put("chIds", chIds == null ? null : Arrays.asList(chIds));
        List<ChannelInfo> channelInfos = getChannelByPayType(paramMap);

        //第二步：
        if (channelInfos.size() > 0) {
            //不为空，直接取第一个
            for (ChannelInfo channelInfo : channelInfos) {
                return channelInfo;
            }
        } else {
            throw new PayException("商户没有对应的通道！", 2004);
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

    public void updateWallet(SquareTrade trade) {
        MerchantInfo merchantInfo = trade.getMerchantInfo();
        TransOrder transOrder = trade.getTransOrder();
        ChannelInfo payOrderChannel = trade.getPayOrderChannel();
        TradeObjectSquare tradeObjectSquare = trade.getTradeObjectSquare();
        MerchantWallet merchantWallet = this.getMerchantWallet(merchantInfo.getMerId());
        MerchantRate merchantRate = this.getMerchantRateByIdAndPayType(merchantInfo.getMerId(), transOrder.getPayType());
        String settlecycle = merchantRate.getSettlecycle();
        ChannelWallet channelWallet = this.getChannelWallet(payOrderChannel.getChannelId());
        ChannelInfo channelInfo = this.getChannelInfo(transOrder.getChannelId().toString());

        //获取实际交易金额
        BigDecimal hundred = new BigDecimal(100);
        BigDecimal amount = transOrder.getRealAmount();
        BigDecimal orderAmount = transOrder.getAmount();

        BigDecimal backFee = tradeObjectSquare.getBackFee()==null?new BigDecimal(0):tradeObjectSquare.getBackFee();

        //商户手续费
        BigDecimal merRate = merchantRate.getRateFee() == null ? new BigDecimal(0) : merchantRate.getRateFee();
        BigDecimal singlFee = merchantRate.getSingleFee() == null ? new BigDecimal(0) : merchantRate.getSingleFee();
        BigDecimal merTransFee = orderAmount.divide(hundred).multiply(merRate).add(singlFee).setScale(2, BigDecimal.ROUND_UP);
        BigDecimal merFeeProfit = backFee.subtract(merTransFee).setScale(2, BigDecimal.ROUND_UP);


        //商户出账成本  实际出账金额+商户手续费
        BigDecimal merOutAmount = amount.add(merTransFee).setScale(2, BigDecimal.ROUND_UP);


        //商户交易钱包
//        merchantWallet.setTotalAmount(merchantWallet.getTotalAmount() == null ? amount : merchantWallet.getTotalAmount().add(amount));
        //出账金额
        merchantWallet.setOutAmount(merchantWallet.getOutAmount()==null?merOutAmount:merchantWallet.getOutAmount().add(merOutAmount));
        //手续费成本
        merchantWallet.setTotalFee(merchantWallet.getTotalFee() == null ? merTransFee : merchantWallet.getTotalFee().add(merTransFee));
        //总可用
        merchantWallet.setTotalAvailableAmount(merchantWallet.getTotalAvailableAmount().subtract(merOutAmount));
        //总余额
        merchantWallet.setTotalBalance( merchantWallet.getTotalBalance().subtract(merOutAmount) );
        //商户利润  下级手续费-商户手续费
        merchantWallet.setFeeProfit(merchantWallet.getFeeProfit()==null?merFeeProfit:merchantWallet.getFeeProfit().add(merFeeProfit));
        merchantWallet.setUpdateTime(new Date());


        MerchantsDetails merchantsDetails = new MerchantsDetails();
        merchantsDetails.setId(UUID.createKey("merchants_details",""));
        merchantsDetails.setMerId(merchantWallet.getMerId());
        merchantsDetails.setType(channelInfo.getType().toString());
        merchantsDetails.setOrderId(transOrder.getTransId());
        merchantsDetails.setMerOrderId(transOrder.getMerOrderId());
        merchantsDetails.setAmount(orderAmount);
        merchantsDetails.setFeeProfit(merFeeProfit);
        merchantsDetails.setOutAmount(merOutAmount);
        merchantsDetails.setFee(merTransFee);
        merchantsDetails.setTotalBalance(merchantWallet.getTotalBalance());
        merchantsDetails.setUpdateTime(new Date());


        //下级商户
        if (transOrder.getTerminalMerId()!=null&&!transOrder.getTerminalMerId().equals("")){
            TerminalMerchantsWallet terminalMerchantsWallet=this.getTerminalMerchantsWallet(transOrder.getMerId(),transOrder.getTerminalMerId());
            int status=0;
            if (terminalMerchantsWallet==null){
                terminalMerchantsWallet=new TerminalMerchantsWallet();
                terminalMerchantsWallet.setId(UUID.createKey("",""));
                terminalMerchantsWallet.setMerId(transOrder.getMerId());
                terminalMerchantsWallet.setTerminalMerId(transOrder.getTerminalMerId());
                status=1;
            }

            //出账
            terminalMerchantsWallet.setOutAmount(terminalMerchantsWallet.getOutAmount()==null?orderAmount:terminalMerchantsWallet.getOutAmount().add(orderAmount));
            //总余额 1-a-fee
            terminalMerchantsWallet.setTotalBalance(terminalMerchantsWallet.getTotalBalance().subtract(orderAmount) );
            //总手续费
            terminalMerchantsWallet.setTotalFee(terminalMerchantsWallet.getTotalFee()==null?backFee:terminalMerchantsWallet.getTotalFee().add(backFee));
            //总可用 1-a-fee
            terminalMerchantsWallet.setTotalAvailableAmount(terminalMerchantsWallet.getTotalAvailableAmount().subtract(orderAmount) );

            terminalMerchantsWallet.setUpdateTime(new Date());

            TerminalMerchantsDetails terminalMerchantsDetails = new TerminalMerchantsDetails();
            terminalMerchantsDetails.setId(UUID.createKey("terminal_merchants_details",""));
            terminalMerchantsDetails.setMerId(transOrder.getMerId());
            terminalMerchantsDetails.setTerminalMerId(transOrder.getTerminalMerId());
            terminalMerchantsDetails.setType(channelInfo.getType().toString());
            terminalMerchantsDetails.setMerOrderId(tradeObjectSquare.getMerOrderId());
            terminalMerchantsDetails.setOrderId(transOrder.getTransId());
            terminalMerchantsDetails.setAmount(orderAmount);
            terminalMerchantsDetails.setOutAmount(orderAmount);
            terminalMerchantsDetails.setFee(backFee);
            terminalMerchantsDetails.setTotalBalance(terminalMerchantsWallet.getTotalBalance());
            terminalMerchantsDetails.setUpdateTime(new Date());

            this.insertTerminalMerchantsDetails(terminalMerchantsDetails);
            if (status==0){
                this.updateTerminalMerchantsWallet(terminalMerchantsWallet);
            }else {
                this.insertTerminalMerchantsWallet(terminalMerchantsWallet);
            }

        }

        if( null !=channelWallet){
            //通道钱包
            BigDecimal channelRateFee = channelInfo.getChannelRateFee() == null ? new BigDecimal(0) : channelInfo.getChannelRateFee();
            BigDecimal channelSingleFee = channelInfo.getChannelSingleFee() == null ? new BigDecimal(0) : channelInfo.getChannelSingleFee();
            BigDecimal channelFee = orderAmount.divide(hundred).multiply(channelRateFee).add(channelSingleFee).setScale(BigDecimal.ROUND_HALF_UP,2);
            BigDecimal channelFeeProfit =merTransFee.subtract(channelFee).setScale(BigDecimal.ROUND_HALF_UP,2);

            //通道出账成本 订单金额+通道手续费
            BigDecimal channelOutAmount = amount.add(channelFee).setScale(BigDecimal.ROUND_HALF_UP,2);

//        channelWallet.setTotalAmount(channelWallet.getTotalAmount() == null ? amount : channelWallet.getTotalAmount().add(amount));
            channelWallet.setOutAmount(channelWallet.getOutAmount()==null?channelOutAmount:channelWallet.getOutAmount().add(channelOutAmount));
            channelWallet.setTotalFee(channelWallet.getTotalFee() == null ? channelFee : channelWallet.getTotalFee().add(channelFee));
            channelWallet.setTotalBalance( channelWallet.getTotalBalance().subtract(channelOutAmount) );
            channelWallet.setTotalAvailableAmount(channelWallet.getTotalAvailableAmount().subtract(channelOutAmount) );
            channelWallet.setFeeProfit(channelWallet.getFeeProfit()==null?channelFeeProfit:channelWallet.getFeeProfit().add(channelFeeProfit));
            channelWallet.setUpdateTime(new Date());
            //更新通道钱包
            this.updateChannelWallet(channelWallet);

            ChannelDetails channelDetails = new ChannelDetails();
            channelDetails.setId(UUID.createKey("channel_details",""));
            channelDetails.setChannelId(channelInfo.getChannelId());
            channelDetails.setChannelTransCode(channelInfo.getChannelTransCode());
            channelDetails.setPayType(transOrder.getPayType());
            channelDetails.setOrderId(transOrder.getTransId());
            channelDetails.setMerOrderId(transOrder.getMerOrderId());
            channelDetails.setType(channelInfo.getType().toString());
            channelDetails.setAmount(orderAmount);
            channelDetails.setFeeProfit(channelFeeProfit);
            channelDetails.setFee(channelFee);
            channelDetails.setOutAmount(channelOutAmount);
            channelDetails.setTotalBalance(channelWallet.getTotalBalance());
            channelDetails.setUpdateTime(new Date());
            //更新通道钱包明细
            this.insertChannelDetails(channelDetails);
        }


        //代理商钱包
        if ( merchantInfo.getParentId() != null && !merchantInfo.getParentId().equals("")) {
            AgentWallet agentWallet = this.getAgentMerchantWallet(merchantInfo.getParentId());
            if(null != agentWallet){
//            AgentMerchantSetting agentMerchantSetting = this.getAgentMerchantSetting(merchantInfo.getParentId());
                AgentMerchantSetting agentMerchantSetting=this.getAgentMerchantSettingByParentIdAndPayType(merchantInfo.getParentId(),transOrder.getPayType());

                BigDecimal agentSingleFee = agentMerchantSetting.getSingleFee();
                //订单总额
//            agentWallet.setTotalAmount(agentWallet.getTotalAmount() == null ? agentWallet.getTotalAmount() : agentWallet.getTotalAmount().add(amount));
                //入账总额
                agentWallet.setIncomeAmount(agentWallet.getIncomeAmount()==null?agentSingleFee:agentWallet.getIncomeAmount().add(agentSingleFee));
                //总余额
                agentWallet.setTotalBalance(agentWallet.getTotalBalance()==null?agentSingleFee:agentWallet.getTotalBalance().add(agentSingleFee));

                if (settlecycle.equals("D0")){
                    //总可用 原始表总额 + 订单金额*手续费
                    agentWallet.setTotalAvailableAmount(agentWallet.getTotalAvailableAmount()==null?agentSingleFee:agentWallet.getTotalAvailableAmount().add(agentSingleFee));
                }else {
                    //总不可 原始表总不可用+ 订单金额*手续费
                    agentWallet.setTotalUnavailableAmount(agentWallet.getTotalUnavailableAmount()==null?agentSingleFee:agentWallet.getTotalUnavailableAmount().add(agentSingleFee));
                }

                AgentMerchantsDetails agentMerchantsDetails = new AgentMerchantsDetails();
                agentMerchantsDetails.setId(UUID.createKey("agent_merchants_details",""));
                agentMerchantsDetails.setAgentMerId(agentWallet.getAgentMerchantId());
                agentMerchantsDetails.setMerOrderId(transOrder.getMerOrderId());
                agentMerchantsDetails.setOrderId(transOrder.getTransId());
                agentMerchantsDetails.setAmount(orderAmount);
                agentMerchantsDetails.setType(channelInfo.getType().toString());
                agentMerchantsDetails.setInAmount(agentSingleFee);
                agentMerchantsDetails.setTotalBenifit(agentWallet.getTotalBalance());
                agentMerchantsDetails.setUpdateTime(new Date());
                this.insertAgentMerchantsDetails(agentMerchantsDetails);
                this.updateAgentWallet(agentWallet);
            }
        }
        this.updateMerchantWallet(merchantWallet);
        this.insertMerchantsDetails(merchantsDetails);
    }
    public void notifyUpdateWallet(BankResult bankResult) {
        synchronized (this) {
            Long orderId = bankResult.getOrderId();
            TransOrder transOrder= paymentRecordSquareService.getTransOrderOrderId(orderId.toString());
            MerchantInfo merchantInfo = paymentRecordSquareService.getMerchantInfoByMerId(transOrder.getMerId());
            SystemOrderTrack systemOrderTrack = paymentRecordSquareService.getSystemOrderTrack(transOrder.getMerOrderId());
            TradeObjectSquare tradeObjectSquare = JsonUtils.jsonToPojo(systemOrderTrack.getTradeInfo(),TradeObjectSquare.class) ;
            PayOrder payOrder = paymentRecordSquareService.getPayOrderByMerOrderIdAndMerIdAndTerMerId(tradeObjectSquare.getOriginalMerOrderId(),tradeObjectSquare.getMerId(),tradeObjectSquare.getTerminalMerId());
            ChannelInfo payOrderChannel = paymentRecordSquareService.getChannelInfo(payOrder.getChannelId());
            MerchantWallet merchantWallet = this.getMerchantWallet(merchantInfo.getMerId());
            MerchantRate merchantRate = this.getMerchantRateByIdAndPayType(merchantInfo.getMerId(), transOrder.getPayType());
            String settlecycle = merchantRate.getSettlecycle();
            ChannelWallet channelWallet = this.getChannelWallet(payOrderChannel.getChannelId());
            ChannelInfo channelInfo = this.getChannelInfo(transOrder.getChannelId());
            //获取实际交易金额
            BigDecimal hundred = new BigDecimal(100);
            BigDecimal amount = transOrder.getRealAmount();
            BigDecimal orderAmount = transOrder.getAmount();

            BigDecimal backFee = tradeObjectSquare.getBackFee()==null?new BigDecimal(0):tradeObjectSquare.getBackFee();

            //商户手续费
            BigDecimal merRate = merchantRate.getRateFee() == null ? new BigDecimal(0) : merchantRate.getRateFee();
            BigDecimal singlFee = merchantRate.getSingleFee() == null ? new BigDecimal(0) : merchantRate.getSingleFee();
            BigDecimal merTransFee = orderAmount.divide(hundred).multiply(merRate).add(singlFee).setScale(2, BigDecimal.ROUND_UP);
            BigDecimal merFeeProfit = backFee.subtract(merTransFee).setScale(2, BigDecimal.ROUND_UP);


            //商户出账成本  实际出账金额+商户手续费
            BigDecimal merOutAmount = amount.add(merTransFee).setScale(2, BigDecimal.ROUND_UP);


            //商户交易钱包
//        merchantWallet.setTotalAmount(merchantWallet.getTotalAmount() == null ? amount : merchantWallet.getTotalAmount().add(amount));
            //出账金额
            merchantWallet.setOutAmount(merchantWallet.getOutAmount()==null?merOutAmount:merchantWallet.getOutAmount().add(merOutAmount));
            //手续费成本
            merchantWallet.setTotalFee(merchantWallet.getTotalFee() == null ? merTransFee : merchantWallet.getTotalFee().add(merTransFee));
            //总可用
            merchantWallet.setTotalAvailableAmount(merchantWallet.getTotalAvailableAmount().subtract(merOutAmount));
            //总余额
            merchantWallet.setTotalBalance( merchantWallet.getTotalBalance().subtract(merOutAmount) );
            //商户利润  下级手续费-商户手续费
            merchantWallet.setFeeProfit(merchantWallet.getFeeProfit()==null?merFeeProfit:merchantWallet.getFeeProfit().add(merFeeProfit));
            merchantWallet.setUpdateTime(new Date());


            MerchantsDetails merchantsDetails = new MerchantsDetails();
            merchantsDetails.setId(UUID.createKey("merchants_details",""));
            merchantsDetails.setMerId(merchantWallet.getMerId());
            merchantsDetails.setType(channelInfo.getType().toString());
            merchantsDetails.setOrderId(transOrder.getTransId());
            merchantsDetails.setMerOrderId(transOrder.getMerOrderId());
            merchantsDetails.setAmount(orderAmount);
            merchantsDetails.setFeeProfit(merFeeProfit);
            merchantsDetails.setOutAmount(merOutAmount);
            merchantsDetails.setFee(merTransFee);
            merchantsDetails.setTotalBalance(merchantWallet.getTotalBalance());
            merchantsDetails.setUpdateTime(new Date());


            //下级商户
            if (transOrder.getTerminalMerId()!=null&&!transOrder.getTerminalMerId().equals("")){
                TerminalMerchantsWallet terminalMerchantsWallet=this.getTerminalMerchantsWallet(transOrder.getMerId(),transOrder.getTerminalMerId());
                int status=0;
                if (terminalMerchantsWallet==null){
                    terminalMerchantsWallet=new TerminalMerchantsWallet();
                    terminalMerchantsWallet.setId(UUID.createKey("",""));
                    terminalMerchantsWallet.setMerId(transOrder.getMerId());
                    terminalMerchantsWallet.setTerminalMerId(transOrder.getTerminalMerId());
                    status=1;
                }

                //出账
                terminalMerchantsWallet.setOutAmount(terminalMerchantsWallet.getOutAmount()==null?orderAmount:terminalMerchantsWallet.getOutAmount().add(orderAmount));
                //总余额 1-a-fee
                terminalMerchantsWallet.setTotalBalance(terminalMerchantsWallet.getTotalBalance().subtract(orderAmount) );
                //总手续费
                terminalMerchantsWallet.setTotalFee(terminalMerchantsWallet.getTotalFee()==null?backFee:terminalMerchantsWallet.getTotalFee().add(backFee));
                //总可用 1-a-fee
                terminalMerchantsWallet.setTotalAvailableAmount(terminalMerchantsWallet.getTotalAvailableAmount().subtract(orderAmount) );
                terminalMerchantsWallet.setUpdateTime(new Date());
                TerminalMerchantsDetails terminalMerchantsDetails = new TerminalMerchantsDetails();
                terminalMerchantsDetails.setId(UUID.createKey("terminal_merchants_details",""));
                terminalMerchantsDetails.setMerId(transOrder.getMerId());
                terminalMerchantsDetails.setTerminalMerId(transOrder.getTerminalMerId());
                terminalMerchantsDetails.setType(channelInfo.getType().toString());
                terminalMerchantsDetails.setMerOrderId(tradeObjectSquare.getMerOrderId());
                terminalMerchantsDetails.setOrderId(transOrder.getTransId());
                terminalMerchantsDetails.setAmount(orderAmount);
                terminalMerchantsDetails.setOutAmount(orderAmount);
                terminalMerchantsDetails.setFee(backFee);
                terminalMerchantsDetails.setTotalBalance(terminalMerchantsWallet.getTotalBalance());
                terminalMerchantsDetails.setUpdateTime(new Date());
                this.insertTerminalMerchantsDetails(terminalMerchantsDetails);
                if (status==0){
                    this.updateTerminalMerchantsWallet(terminalMerchantsWallet);
                }else {
                    this.insertTerminalMerchantsWallet(terminalMerchantsWallet);
                }

            }


            //代理商钱包
            if (merchantInfo.getParentId() != null && !merchantInfo.getParentId().equals("")) {
                AgentWallet agentWallet = this.getAgentMerchantWallet(merchantInfo.getParentId());
                //            AgentMerchantSetting agentMerchantSetting = this.getAgentMerchantSetting(merchantInfo.getParentId());
                AgentMerchantSetting agentMerchantSetting=this.getAgentMerchantSettingByParentIdAndPayType(merchantInfo.getParentId(),transOrder.getPayType());
                BigDecimal agentSingleFee = agentMerchantSetting.getSingleFee();
                //入账总额
                agentWallet.setIncomeAmount(agentWallet.getIncomeAmount()==null?agentSingleFee:agentWallet.getIncomeAmount().add(agentSingleFee));
                //总余额
                agentWallet.setTotalBalance(agentWallet.getTotalBalance()==null?agentSingleFee:agentWallet.getTotalBalance().add(agentSingleFee));

                if (settlecycle.equals("D0")){
                    //总可用 原始表总额 + 订单金额*手续费
                    agentWallet.setTotalAvailableAmount(agentWallet.getTotalAvailableAmount()==null?agentSingleFee:agentWallet.getTotalAvailableAmount().add(agentSingleFee));
                }else {
                    //总不可 原始表总不可用+ 订单金额*手续费
                    agentWallet.setTotalUnavailableAmount(agentWallet.getTotalUnavailableAmount()==null?agentSingleFee:agentWallet.getTotalUnavailableAmount().add(agentSingleFee));
                }

                AgentMerchantsDetails agentMerchantsDetails = new AgentMerchantsDetails();
                agentMerchantsDetails.setId(UUID.createKey("agent_merchants_details",""));
                agentMerchantsDetails.setAgentMerId(agentWallet.getAgentMerchantId());
                agentMerchantsDetails.setMerOrderId(transOrder.getMerOrderId());
                agentMerchantsDetails.setOrderId(transOrder.getTransId());
                agentMerchantsDetails.setAmount(orderAmount);
                agentMerchantsDetails.setType(channelInfo.getType().toString());
                agentMerchantsDetails.setInAmount(agentSingleFee);
                agentMerchantsDetails.setTotalBenifit(agentWallet.getTotalBalance());
                agentMerchantsDetails.setUpdateTime(new Date());
                this.insertAgentMerchantsDetails(agentMerchantsDetails);
                this.updateAgentWallet(agentWallet);
            }

            //通道钱包
            BigDecimal channelRateFee = channelInfo.getChannelRateFee() == null ? new BigDecimal(0) : channelInfo.getChannelRateFee();
            BigDecimal channelSingleFee = channelInfo.getChannelSingleFee() == null ? new BigDecimal(0) : channelInfo.getChannelSingleFee();
            BigDecimal channelFee = orderAmount.divide(hundred).multiply(channelRateFee).add(channelSingleFee).setScale(BigDecimal.ROUND_HALF_UP,2);
            BigDecimal channelFeeProfit =merTransFee.subtract(channelFee).setScale(BigDecimal.ROUND_HALF_UP,2);

            //通道出账成本 订单金额+通道手续费
            BigDecimal channelOutAmount = amount.add(channelFee).setScale(BigDecimal.ROUND_HALF_UP,2);

//        channelWallet.setTotalAmount(channelWallet.getTotalAmount() == null ? amount : channelWallet.getTotalAmount().add(amount));
            channelWallet.setOutAmount(channelWallet.getOutAmount()==null?channelOutAmount:channelWallet.getOutAmount().add(channelOutAmount));
            channelWallet.setTotalFee(channelWallet.getTotalFee() == null ? channelFee : channelWallet.getTotalFee().add(channelFee));
            channelWallet.setTotalBalance( channelWallet.getTotalBalance().subtract(channelOutAmount) );
            channelWallet.setTotalAvailableAmount(channelWallet.getTotalAvailableAmount().subtract(channelOutAmount) );
            channelWallet.setFeeProfit(channelWallet.getFeeProfit()==null?channelFeeProfit:channelWallet.getFeeProfit().add(channelFeeProfit));
            channelWallet.setUpdateTime(new Date());

            ChannelDetails channelDetails = new ChannelDetails();
            channelDetails.setId(UUID.createKey("channel_details",""));
            channelDetails.setChannelId(channelInfo.getChannelId());
            channelDetails.setChannelTransCode(channelInfo.getChannelTransCode());
            channelDetails.setPayType(transOrder.getPayType());
            channelDetails.setOrderId(transOrder.getTransId());
            channelDetails.setMerOrderId(transOrder.getMerOrderId());
            channelDetails.setType(channelInfo.getType().toString());
            channelDetails.setAmount(orderAmount);
            channelDetails.setFeeProfit(channelFeeProfit);
            channelDetails.setFee(channelFee);
            channelDetails.setOutAmount(channelOutAmount);
            channelDetails.setTotalBalance(channelWallet.getTotalBalance());
            channelDetails.setUpdateTime(new Date());


            this.updateMerchantWallet(merchantWallet);

            this.updateChannelWallet(channelWallet);

            this.insertMerchantsDetails(merchantsDetails);

            this.insertChannelDetails(channelDetails);
        }
    }

    private AgentMerchantSetting getAgentMerchantSettingByParentIdAndPayType(String parentId, String payType) {
        return paymentRecordSquareService.getAgentMerchantSettingByParentIdAndPayType(parentId,payType);
    }

    private void insertChannelDetails(ChannelDetails channelDetails) {
        paymentRecordSquareService.insertChannelDetails(channelDetails);
    }

    private void insertMerchantsDetails(MerchantsDetails merchantsDetails) {
        paymentRecordSquareService.insertMerchantsDetails(merchantsDetails);
    }

    private void insertAgentMerchantsDetails(AgentMerchantsDetails agentMerchantsDetails) {
        paymentRecordSquareService.insertAgentMerchantsDetails(agentMerchantsDetails);
    }

    public void updateTerminalMerchantsWallet(TerminalMerchantsWallet terminalMerchantsWallet) {
        paymentRecordSquareService.updateTerminalMerchantsWallet(terminalMerchantsWallet);
    }

    public void insertTerminalMerchantsDetails(TerminalMerchantsDetails terminalMerchantsDetails) {
        paymentRecordSquareService.insertTerminalMerchantsDetails(terminalMerchantsDetails);
    }

    public void insertTerminalMerchantsWallet(TerminalMerchantsWallet terminalMerchantsWallet) {
        paymentRecordSquareService.insertTerminalMerchantsWallet(terminalMerchantsWallet);
    }

    public TerminalMerchantsWallet getTerminalMerchantsWallet(String merId, String terminalMerId) {
        return    paymentRecordSquareService.getTerminalMerchantsWallet(merId,terminalMerId);
    }

    public MerchantRate getMerchantRateByIdAndPayType(String merId, String payType) {
        return paymentRecordSquareService.getMerchantRateByIdAndPayType(merId, payType);
    }

    public void analysis(ChannelInfo channelInfo, TransOrder transOrder, MerchantQuotaRisk merchantQuotaRisk) throws PayException {
        List<RiskQuotaData> quotaDataList = getRiskQuotaData(channelInfo, transOrder);
        insertRiskQuotaDataNew(quotaDataList, channelInfo, merchantQuotaRisk);
        // List<RiskQuotaData> quotaDataList = riskQuotaDataCache.getQuotaData(transOrder.getMerId(),channelInfo.getChannelId());
        quotaTodoSquare(transOrder, quotaDataList, channelInfo, merchantQuotaRisk);
    }

    private int insertRiskQuotaDataNew(List<RiskQuotaData> quotaDataList, ChannelInfo channelInfo, MerchantQuotaRisk merchantQuotaRisk) {
        List<String> typeList = new ArrayList<>();
        typeList.add("1-1");
        typeList.add("1-2");
        // typeList.add("1-4");
        typeList.add("2-1");
        typeList.add("2-2");
        // typeList.add("2-4");
        if (quotaDataList.size() <= 0 || quotaDataList == null) {
            List<RiskQuotaData> riskQuotaDataList = new ArrayList<>();
            initRisk(channelInfo, merchantQuotaRisk, typeList, riskQuotaDataList);
            insertRiskQuotaData(riskQuotaDataList);
            return 1;
        }
        // 不为空
        List<String> riskList = new ArrayList<>();
        for (RiskQuotaData riskQuotaData : quotaDataList) {
            riskList.add(riskQuotaData.getRefType() + "-" + riskQuotaData.getType());
        }
        List<String> noriskList = new ArrayList<>();
        for (String str : typeList) {
            if (!riskList.contains(str)) {
                noriskList.add(str);
            }
        }
        if (noriskList.size() > 0) {
            List<RiskQuotaData> riskQuotaDataList = new ArrayList<>();
            initRisk(channelInfo, merchantQuotaRisk, noriskList, riskQuotaDataList);
            insertRiskQuotaData(riskQuotaDataList);
            return 1;
        }
        return 1;
    }

    private void initRisk(ChannelInfo channelInfo, MerchantQuotaRisk merchantQuotaRisk, List<String> noriskList, List<RiskQuotaData> riskQuotaDataList) {
        for (String ss : noriskList) {
            String[] ssArray = ss.split("-");
            Short refType = Short.parseShort(ssArray[0]);
            Short type = Short.parseShort(ssArray[1]);
            RiskQuotaData riskQuotaData = new RiskQuotaData();
            riskQuotaData.setAmount(new BigDecimal(0));
            riskQuotaData.setType(type);
            if (type == 1) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String day = sdf.format(new Date());
                riskQuotaData.setTradeTime(day);
            } else if (type == 2) {
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
                String month = sdf1.format(new Date());
                riskQuotaData.setTradeTime(month);
            } else {
                break;
            }
            // if (type == 4){
            //     SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH");
            //     String db = sdf1.format(new Date());
            //     riskQuotaData.setTradeTime(db);
            // }
            switch (refType) {
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

    List<RiskQuotaData> getRiskQuotaData(ChannelInfo channelInfo, TransOrder transOrder) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String day = sdf.format(new Date());
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
        String month = sdf1.format(new Date());
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy");
        String year = sdf2.format(new Date());
        return riskQuotaDataCache.getQuotaData1(transOrder.getMerId(), channelInfo.getChannelId(), day, month, year);
    }

    private void quotaTodoSquare(TransOrder transOrder, List<RiskQuotaData> quotaDataList, ChannelInfo channelInfo, MerchantQuotaRisk merchantQuotaRisk) throws PayException {
        // 当前交易人民币金额
        BigDecimal rmbAmount = transOrder.getAmount() != null ? transOrder.getAmount() : new BigDecimal(0);
        // 限定日最小额度
        BigDecimal singleMinAmount = channelInfo.getSingleMinAmount() != null ? channelInfo.getSingleMinAmount() : new BigDecimal(0);
        // 限定日最大额度
        BigDecimal getSingleMaxAmount = channelInfo.getSingleMaxAmount() != null ? channelInfo.getSingleMaxAmount() : new BigDecimal(0);
        if (isExceed(singleMinAmount, rmbAmount)) {
            throw new PayException("通道不足单笔最小限额：" + singleMinAmount + "，当前交易人民币金额：" + rmbAmount, "RXH00012");
        }
        if (isExceed(rmbAmount, getSingleMaxAmount)) {
            throw new PayException("通道超出单笔最大限额：" + getSingleMaxAmount + "，当前交易人民币金额：" + rmbAmount, "RXH00012");
        }
        // 限定日最小额度
        BigDecimal singleQuotaAmount = merchantQuotaRisk.getSingleQuotaAmount() != null ? merchantQuotaRisk.getSingleQuotaAmount() : new BigDecimal(0);
        if (isExceed(rmbAmount, singleQuotaAmount)) {
            throw new PayException("商户超出单笔最大限额：" + singleQuotaAmount + "，当前交易人民币金额：" + rmbAmount, "RXH00012");
        }
        if (quotaDataList == null) {
            return;
        }

        // 单笔额度
        for (RiskQuotaData riskQuotaData : quotaDataList) {
            // 通道
            if (riskQuotaData.getRefType() == RiskQuotaData.CHANNEL_REF_TYPE && riskQuotaData.getType() != RiskQuotaData.LIMIT_TYPE_ORDER_AMOUNT) {
                switch (riskQuotaData.getType()) {
                    case 1:
                        if (isExceed(riskQuotaData.getAmount() != null ? riskQuotaData.getAmount().add(rmbAmount) : rmbAmount, channelInfo.getDayQuotaAmount() != null ? channelInfo.getDayQuotaAmount() : new BigDecimal(0))) {
                            // 超出限定额度的值
                            BigDecimal excessAmount = riskQuotaData.getAmount().add(rmbAmount).subtract(channelInfo.getDayQuotaAmount());
                            throw new PayException("通道每日最大限额：" + channelInfo.getDayQuotaAmount() + "，超出：" + excessAmount, "RXH00012");
                        }
                    case 2:
                        if (isExceed(riskQuotaData.getAmount() != null ? riskQuotaData.getAmount().add(rmbAmount) : rmbAmount, channelInfo.getMonthQuotaAmount() != null ? channelInfo.getMonthQuotaAmount() : new BigDecimal(0))) {
                            // 超出限定额度的值
                            BigDecimal excessAmount = riskQuotaData.getAmount().add(rmbAmount).subtract(channelInfo.getMonthQuotaAmount());
                            throw new PayException("通道每月最大限额：" + channelInfo.getMonthQuotaAmount() + "，超出：" + excessAmount, "RXH00012");
                        }
                    default:
                        break;
                }
            }
            // 商户
            if (riskQuotaData.getRefType() == RiskQuotaData.MERCHANT_REF_TYPE && riskQuotaData.getType() != RiskQuotaData.LIMIT_TYPE_ORDER_AMOUNT) {
                switch (riskQuotaData.getType()) {
                    case 1:
                        if (isExceed(riskQuotaData.getAmount() != null ? riskQuotaData.getAmount().add(rmbAmount) : rmbAmount, merchantQuotaRisk.getDayQuotaAmount() != null ? merchantQuotaRisk.getDayQuotaAmount() : new BigDecimal(0))) {
                            // 超出限定额度的值
                            BigDecimal excessAmount = riskQuotaData.getAmount().add(rmbAmount).subtract(merchantQuotaRisk.getDayQuotaAmount());
                            throw new PayException("商户每日最大限额：" + merchantQuotaRisk.getDayQuotaAmount() + "，超出：" + excessAmount, "RXH00012");
                        }
                    case 2:
                        if (isExceed(riskQuotaData.getAmount() != null ? riskQuotaData.getAmount().add(rmbAmount) : rmbAmount, merchantQuotaRisk.getMonthQuotaAmount() != null ? merchantQuotaRisk.getMonthQuotaAmount() : new BigDecimal(0))) {
                            // 超出限定额度的值
                            BigDecimal excessAmount = riskQuotaData.getAmount().add(rmbAmount).subtract(merchantQuotaRisk.getMonthQuotaAmount());
                            throw new PayException("商户每月最大限额：" + merchantQuotaRisk.getMonthQuotaAmount() + "，超出：" + excessAmount, "RXH00012");
                        }
                    default:
                        break;
                }
            }
        }
    }

    private boolean isExceed(BigDecimal amount, BigDecimal target) {
        return amount.compareTo(target) > 0;
    }

    public void updateRiskQuotaData(TransOrder transOrder, ChannelInfo channelInfo, MerchantQuotaRisk merchantQuotaRisk) throws PayException {
        List<RiskQuotaData> quotaDataList = getRiskQuotaData(channelInfo, transOrder);
        // quotaTodoSquare(transOrder,quotaDataList,channelInfo,merchantQuotaRisk);
        quotaDataList.forEach(riskQuotaData -> {
            // if (StringUtils.equals(riskQuotaData.getType().toStringquotaTodoSquare(), transOrder.getPayType())) {
            riskQuotaData.setAmount(riskQuotaData.getAmount() == null ? transOrder.getAmount() : riskQuotaData.getAmount().add(transOrder.getAmount()));
            // }
        });
        insertOrUpdateRiskQuotaData(quotaDataList);
    }


    public TransAudit getTransAuditByTransId(String transId) {
        TransAudit transAudit = paymentRecordSquareService.getTransAuditByTransId(transId);
        return transAudit;
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

    public int updateTransAudit(TransAudit transAudit) {
        return paymentRecordSquareService.updateTransAudit(transAudit);
    }

    public MerchantCard getMerchanCardByMerchantIdAndBizType(TradeObjectSquare tradeObjectSquare) {
        return paymentRecordSquareService.getMerchanCardByMerchantIdAndBizType(tradeObjectSquare.getMerId(), tradeObjectSquare.getBizType());
    }

    public ExtraChannelInfo getChannelInfoByBizType(TradeObjectSquare tradeObjectSquare) {
        return paymentRecordSquareService.getChannelInfoByBizType(tradeObjectSquare.getBizType());
    }

    public ExtraChannelInfo searchExtraChannelInfo(String channelId,String type) {

        return paymentRecordSquareService.searchExtraChannelInfo(channelId,type);
    }

    public MerchantCard searchMerchantCard(ExtraChannelInfo extraChannelInfo, TradeObjectSquare tradeObjectSquare) {
        return paymentRecordSquareService.searchMerchantCard(extraChannelInfo.getExtraChannelId(), tradeObjectSquare.getMerId(), tradeObjectSquare.getTerminalMerId());
    }


    public BigDecimal getTransOrderAmount(String merId, String terminalMerId, String originalMerOrderId) {
        return paymentRecordSquareService.getTransOrderAmount(merId,terminalMerId,originalMerOrderId);
    }

    public MerchantRegisterInfo getMerchantRegisterInfoByParam(ChannelInfo channelInfo, TradeObjectSquare tradeObjectSquare) {
        List<ExtraChannelInfo> list = paymentRecordSquareService.getChannelInfosByPayType(tradeObjectSquare);
        List<MerchantRegisterInfo> merchantRegisterInfos = paymentRecordSquareService.getMerchantRegisterInfos(tradeObjectSquare.getMerId(), tradeObjectSquare.getTerminalMerId(),SystemConstant.SUCCESS);

        ExtraChannelInfo extraChannel = list.stream().filter(extraChannelInfo -> extraChannelInfo.getType().equals(SystemConstant.ADDCUS) && extraChannelInfo.getOrganizationId().equals(channelInfo.getOrganizationId())).findFirst().get();
        return merchantRegisterInfos.stream().filter(merchantRegisterInfo -> merchantRegisterInfo.getExtraChannelId().equals(extraChannel.getExtraChannelId())).findFirst().get();

    }

    public MerchantRegisterCollect searchMerchantRegisterCollect(ExtraChannelInfo extraChannelInfo, TradeObjectSquare tradeObjectSquare) {
        List<MerchantRegisterCollect> merchantRegisterCollects=redisCacheCommonCompoment.merchantRegisterCollectCache.getAll()
                .stream()
                .filter(t->
                        t.getMerId().equals(tradeObjectSquare.getMerId())
                                &&  t.getTerminalMerId().equals(tradeObjectSquare.getTerminalMerId())
                                && t.getCardNum().equals(tradeObjectSquare.getBankCardNum())

                )
                .distinct()
                .collect(Collectors.toList());
//        List<MerchantRegisterCollect> merchantRegisterCollects=paymentRecordSquareService.getmMrchantRegisterCollectByMerIdAndTerminalMerId(
//                tradeObjectSquare.getMerId(), tradeObjectSquare.getTerminalMerId(),tradeObjectSquare.getBankCardNum());
        //根据绑卡id获取进件id
        String organizationId = extraChannelInfo.getOrganizationId();
//        ExtraChannelInfo extraChannel = paymentRecordSquareService.getExtraChannelInfoByOrgId(organizationId, SystemConstant.ADDCUS);
        ExtraChannelInfo extraChannel = redisCacheCommonCompoment.extraChannelInfoCache.getAll()
                .stream()
                .filter(t-> t.getOrganizationId().equals(organizationId) && t.getType().equals(SystemConstant.ADDCUS))
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

    public void notifyRevertWallet(BankResult bankResult) {
        synchronized (this) {
            Long orderId = bankResult.getOrderId();
            TransOrder transOrder= paymentRecordSquareService.getTransOrderOrderId(orderId.toString());
            BigDecimal orderAmount = transOrder.getAmount();
            MerchantInfo merchantInfo = paymentRecordSquareService.getMerchantInfoByMerId(transOrder.getMerId());
            PayOrder payOrder = paymentRecordSquareService.getPayOrderByMerOrderIdAndMerIdAndTerMerId(transOrder.getOriginalMerOrderId(),transOrder.getMerId(),transOrder.getTerminalMerId());
            ChannelInfo payOrderChannel = paymentRecordSquareService.getChannelInfo(payOrder.getChannelId());
            MerchantRate merchantRate = this.getMerchantRateByIdAndPayType(merchantInfo.getMerId(), transOrder.getPayType());
            String settlecycle = merchantRate.getSettlecycle();
            //商户钱包回退
            MerchantWallet merchantWallet = this.getMerchantWallet(merchantInfo.getMerId());
            MerchantsDetails merchantDetails = paymentRecordSquareService.getMerchantDetails(transOrder.getTransId());
            BigDecimal merOutAmount = merchantDetails.getOutAmount();
            BigDecimal merTransFee = merchantDetails.getFee();
            BigDecimal merFeeprofit = merchantDetails.getFeeProfit();
            merchantWallet.setOutAmount(merchantWallet.getOutAmount().subtract(merOutAmount));
            merchantWallet.setTotalFee(merchantWallet.getTotalFee().subtract(merTransFee));
            merchantWallet.setTotalAvailableAmount(merchantWallet.getTotalAvailableAmount().add(merOutAmount));
            merchantWallet.setTotalBalance(merchantWallet.getTotalBalance().add(merOutAmount));
            merchantWallet.setFeeProfit(merchantWallet.getFeeProfit().subtract(merFeeprofit));
            merchantWallet.setUpdateTime(new Date());

            MerchantsDetails merchantsDetail = new MerchantsDetails();
            merchantsDetail.setId(UUID.createKey("merchants_details",""));
            merchantsDetail.setMerId(merchantWallet.getMerId());
            merchantsDetail.setType(SystemConstant.CORRECT);
            merchantsDetail.setOrderId(transOrder.getTransId());
            merchantsDetail.setMerOrderId(transOrder.getMerOrderId());
            merchantsDetail.setAmount(orderAmount);
            merchantsDetail.setFeeProfit(merFeeprofit.negate());
            merchantsDetail.setInAmount(merOutAmount);
            merchantsDetail.setFee(merTransFee.negate());
            merchantsDetail.setTotalBalance(merchantWallet.getTotalBalance());
            merchantsDetail.setUpdateTime(new Date());
            this.updateMerchantWallet(merchantWallet);
            this.insertMerchantsDetails(merchantsDetail);


            //子商户钱包回退
            if (transOrder.getTerminalMerId()!=null&&!transOrder.getTerminalMerId().equals("")){
                TerminalMerchantsWallet terminalMerchantsWallet = this.getTerminalMerchantsWallet(transOrder.getMerId(), transOrder.getTerminalMerId());
                TerminalMerchantsDetails terminalMerchantsDetails = paymentRecordSquareService.getTerminalMerchantsDetails(transOrder.getTransId());
                BigDecimal backFee = terminalMerchantsDetails.getFee();
                BigDecimal outAmount = terminalMerchantsDetails.getOutAmount();
                terminalMerchantsWallet.setOutAmount(terminalMerchantsWallet.getOutAmount().subtract(outAmount));
                terminalMerchantsWallet.setTotalBalance(terminalMerchantsWallet.getTotalBalance().add(outAmount));
                terminalMerchantsWallet.setTotalAvailableAmount(terminalMerchantsWallet.getTotalAvailableAmount().add(outAmount));
                terminalMerchantsWallet.setTotalFee(terminalMerchantsWallet.getTotalFee().subtract(backFee));
                terminalMerchantsWallet.setUpdateTime(new Date());
                TerminalMerchantsDetails terminalMerchantsDetail = new TerminalMerchantsDetails();
                terminalMerchantsDetail.setId(UUID.createKey("terminal_merchants_details",""));
                terminalMerchantsDetail.setMerId(transOrder.getMerId());
                terminalMerchantsDetail.setTerminalMerId(transOrder.getTerminalMerId());
                terminalMerchantsDetail.setType(SystemConstant.CORRECT);
                terminalMerchantsDetail.setMerOrderId(transOrder.getMerOrderId());
                terminalMerchantsDetail.setOrderId(transOrder.getTransId());
                terminalMerchantsDetail.setAmount(orderAmount);
                terminalMerchantsDetail.setInAmount(orderAmount);
                terminalMerchantsDetail.setFee(backFee.negate());
                terminalMerchantsDetail.setTotalBalance(terminalMerchantsWallet.getTotalBalance());
                terminalMerchantsDetail.setUpdateTime(new Date());
                this.insertTerminalMerchantsDetails(terminalMerchantsDetail);
                this.updateTerminalMerchantsWallet(terminalMerchantsWallet);
            }
            //平台钱包回退
            ChannelWallet channelWallet = this.getChannelWallet(payOrderChannel.getChannelId());
            ChannelDetails channelDetails = paymentRecordSquareService.getChannelDetails(transOrder.getTransId());
            BigDecimal channelOutAmount = channelDetails.getOutAmount();
            BigDecimal channelFee = channelDetails.getFee();
            BigDecimal channelFeeProfit = channelDetails.getFeeProfit();
            channelWallet.setOutAmount(channelWallet.getOutAmount().add(channelOutAmount));
            channelWallet.setTotalFee(channelWallet.getTotalFee().subtract(channelFee));
            channelWallet.setTotalBalance(channelWallet.getTotalBalance().add(channelOutAmount));
            channelWallet.setTotalAvailableAmount(channelWallet.getTotalAvailableAmount().add(channelOutAmount));
            channelWallet.setFeeProfit(channelWallet.getFeeProfit().subtract(channelFeeProfit));
            channelWallet.setUpdateTime(new Date());
            ChannelDetails channelDetail = new ChannelDetails();
            channelDetail.setId(UUID.createKey("channel_details",""));
            channelDetail.setChannelId(channelDetails.getChannelId());
            channelDetail.setChannelTransCode(channelDetails.getChannelTransCode());
            channelDetail.setPayType(transOrder.getPayType());
            channelDetail.setOrderId(transOrder.getTransId());
            channelDetail.setMerOrderId(transOrder.getMerOrderId());
            channelDetail.setType(SystemConstant.CORRECT);
            channelDetail.setAmount(orderAmount);
            channelDetail.setFeeProfit(channelFeeProfit.negate());
            channelDetail.setFee(channelFee.negate());
            channelDetail.setInAmount(channelOutAmount);
            channelDetail.setTotalBalance(channelWallet.getTotalBalance());
            channelDetail.setUpdateTime(new Date());
            this.updateChannelWallet(channelWallet);
            this.insertChannelDetails(channelDetail);
            //代理商钱包回退
            if (merchantInfo.getParentId()!=null&&!merchantInfo.getParentId().equals("")){
                AgentWallet agentMerchantWallet = paymentRecordSquareService.getAgentMerchantWallet(merchantInfo.getParentId());
                AgentMerchantsDetails agentMerchantsDetails = paymentRecordSquareService.getAgentMerchantsDetails(transOrder.getTransId());
                BigDecimal inAmount = agentMerchantsDetails.getInAmount();
                agentMerchantWallet.setIncomeAmount(agentMerchantWallet.getIncomeAmount().subtract(inAmount));
                agentMerchantWallet.setTotalBalance(agentMerchantWallet.getTotalBalance().subtract(inAmount));
                if (settlecycle.equals("D0")){
                    //总可用 原始表总额 + 订单金额*手续费
                    agentMerchantWallet.setTotalAvailableAmount(agentMerchantWallet.getTotalAvailableAmount().subtract(inAmount));
                }else {
                    //总不可 原始表总不可用+ 订单金额*手续费
                    agentMerchantWallet.setTotalUnavailableAmount(agentMerchantWallet.getTotalUnavailableAmount().subtract(inAmount));
                }
                AgentMerchantsDetails agentMerchantsDetail = new AgentMerchantsDetails();
                agentMerchantsDetail.setId(UUID.createKey("agent_merchants_details",""));
                agentMerchantsDetail.setAgentMerId(agentMerchantWallet.getAgentMerchantId());
                agentMerchantsDetail.setMerOrderId(transOrder.getMerOrderId());
                agentMerchantsDetail.setOrderId(transOrder.getTransId());
                agentMerchantsDetail.setAmount(orderAmount.negate());
                agentMerchantsDetail.setType(SystemConstant.CORRECT);
                agentMerchantsDetail.setOutAmount(inAmount);
                agentMerchantsDetail.setTotalBenifit(agentMerchantWallet.getTotalBalance());
                agentMerchantsDetail.setUpdateTime(new Date());
                this.insertAgentMerchantsDetails(agentMerchantsDetail);
                this.updateAgentWallet(agentMerchantWallet);
            }
        }
    }
}
