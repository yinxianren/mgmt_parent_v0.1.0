package com.rxh.service.oldKuaijie;

import com.rxh.cache.ehcache.RiskQuotaDataCache;
import com.rxh.exception.PayException;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.pojo.payment.TradeObjectSquare;
import com.rxh.service.square.PaymentRecordSquareService;
import com.rxh.square.pojo.*;
import com.rxh.utils.JsonUtils;
import com.rxh.utils.SystemConstant;
import com.rxh.utils.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class GetOrderWalletService {


    private final RecordPaymentSquareService recordPaymentSquareService;
    private final RiskQuotaDataCache riskQuotaDataCache;
    private final PaymentRecordSquareService paymentRecordSquareService;

    @Autowired
    public GetOrderWalletService(RecordPaymentSquareService recordPaymentSquareService, RiskQuotaDataCache riskQuotaDataCache, PaymentRecordSquareService paymentRecordSquareService) {
        this.recordPaymentSquareService = recordPaymentSquareService;
        this.riskQuotaDataCache = riskQuotaDataCache;
        this.paymentRecordSquareService = paymentRecordSquareService;
    }

    public void updateWallet(SquareTrade trade) {
        synchronized (this) {
            TradeObjectSquare tradeObjectSquare = trade.getTradeObjectSquare();
            MerchantInfo merchantInfo = trade.getMerchantInfo();
            PayOrder payOrder = trade.getPayOrder();
            ChannelInfo channelInfo = trade.getChannelInfo();
            MerchantWallet merchantWallet = recordPaymentSquareService.getMerchantWallet(merchantInfo.getMerId());

            int merStatus = 0;
            if (merchantWallet == null) {
                merchantWallet = new MerchantWallet();
                merchantWallet.setMerId(payOrder.getMerId());
                merStatus = 1;
            }

            MerchantRate merchantRate = recordPaymentSquareService.getMerchantRateByIdAndPayType(merchantInfo.getMerId(), payOrder.getPayType());
            String settlecycle = merchantRate.getSettlecycle();
            BigDecimal hundred = new BigDecimal(100);

            ChannelWallet channelWallet = recordPaymentSquareService.getChannelWallet(payOrder.getChannelId().toString());

            int channelStatus = 0;
            if (channelWallet == null) {
                channelWallet = new ChannelWallet();
                channelWallet.setId(UUID.createKey("channel_wallet", ""));
                channelWallet.setChannelId(channelInfo.getChannelId());
                channelWallet.setChannelTransCode(channelInfo.getChannelTransCode());
                channelWallet.setPayType(channelInfo.getType().toString());
                channelStatus = 1;
            }

            //获取交易金额
            BigDecimal amount = payOrder.getAmount();
            //商户手续费
            BigDecimal merRate = merchantRate.getRateFee() == null ? new BigDecimal(0) : merchantRate.getRateFee();
            BigDecimal singlFee = merchantRate.getSingleFee() == null ? new BigDecimal(0) : merchantRate.getSingleFee();
            BigDecimal merBondRate = merchantRate.getBondRate() == null ? new BigDecimal(0) : merchantRate.getBondRate();
            // 订单金额/100 * 商户手续费 + 单笔
            BigDecimal merTransFee = amount.divide(hundred).multiply(merRate).add(singlFee).setScale(2, BigDecimal.ROUND_UP);
            BigDecimal merBondAmount = amount.divide(hundred).multiply(merBondRate).setScale(2, BigDecimal.ROUND_UP);
            BigDecimal payFee = tradeObjectSquare.getPayFee() == null ? new BigDecimal(0) : tradeObjectSquare.getPayFee();
            BigDecimal terPayFee = amount.divide(hundred).multiply(payFee).setScale(2, BigDecimal.ROUND_UP);
            BigDecimal merFeeProfit = terPayFee.subtract(merTransFee).setScale(2, BigDecimal.ROUND_UP);

            //商户交易钱包
            merchantWallet.setTotalAmount(merchantWallet.getTotalAmount() == null ? amount : merchantWallet.getTotalAmount().add(amount));
            //入账总额  原始表总余额+(订单金额-手续费)
            merchantWallet.setIncomeAmount(merchantWallet.getIncomeAmount() == null ? amount.subtract(merTransFee) : merchantWallet.getIncomeAmount().add(amount.subtract(merTransFee)).setScale(2, BigDecimal.ROUND_UP));
            //手续费成本：原始表手续费金额+（单笔手续费+订单金额*商户交易手续费）
            merchantWallet.setTotalFee(merchantWallet.getTotalFee() == null ? merTransFee : merchantWallet.getTotalFee().add(merTransFee).setScale(2, BigDecimal.ROUND_UP));
            //保证金：原始表保证金金额+ 订单金额* 商户保证金比例
            merchantWallet.setTotalBond(merchantWallet.getTotalBond() == null ? merBondAmount : merchantWallet.getTotalBond().add(merBondAmount).setScale(2, BigDecimal.ROUND_UP));
            //总余额 原始表总余额+订单金额-手续费
            merchantWallet.setTotalBalance(merchantWallet.getTotalBalance() == null ? amount.subtract(merTransFee) : merchantWallet.getTotalBalance().add(amount.subtract(merTransFee)).setScale(2, BigDecimal.ROUND_UP));
            //手续费利润  终端手续费- 商户手续费=商户利润
            merchantWallet.setFeeProfit(merchantWallet.getFeeProfit() == null ? merFeeProfit : merchantWallet.getFeeProfit().add(merFeeProfit));

            if (settlecycle.equals("D0")) {
                //总可用
                merchantWallet.setTotalAvailableAmount(merchantWallet.getTotalAvailableAmount() == null ? amount.subtract(merTransFee).setScale(2, BigDecimal.ROUND_UP) : merchantWallet.getTotalAvailableAmount().add(amount.subtract(merTransFee)).setScale(2, BigDecimal.ROUND_UP));
            } else {
                //总不可 原始表总不可用+ 订单金额-手续费-保证金
                merchantWallet.setTotalUnavailableAmount(merchantWallet.getTotalUnavailableAmount() == null ? amount.subtract(merTransFee).setScale(2, BigDecimal.ROUND_UP).subtract(merBondAmount) : merchantWallet.getTotalUnavailableAmount().add(amount.subtract(merTransFee).subtract(merBondAmount)).setScale(2, BigDecimal.ROUND_UP));
            }

            merchantWallet.setUpdateTime(new Date());

            MerchantsDetails merchantsDetails = new MerchantsDetails();
            merchantsDetails.setId(UUID.createKey("merchants_details", ""));
            merchantsDetails.setMerId(merchantWallet.getMerId());
            merchantsDetails.setType(channelInfo.getType().toString());
            merchantsDetails.setMerOrderId(payOrder.getMerOrderId());
            merchantsDetails.setOrderId(payOrder.getPayId());
            merchantsDetails.setAmount(payOrder.getAmount());
            merchantsDetails.setInAmount(amount.subtract(merTransFee));
            merchantsDetails.setFeeProfit(merFeeProfit);
            merchantsDetails.setFee(merTransFee);
            merchantsDetails.setTotalBalance(merchantWallet.getTotalBalance());
            merchantsDetails.setUpdateTime(new Date());

            //下级商户
            if (payOrder.getTerminalMerId() != null && !payOrder.getTerminalMerId().equals("")) {
                TerminalMerchantsWallet terminalMerchantsWallet = recordPaymentSquareService.getTerminalMerchantsWallet(payOrder.getMerId(), payOrder.getTerminalMerId());
                int status = 0;
                if (terminalMerchantsWallet == null) {
                    terminalMerchantsWallet = new TerminalMerchantsWallet();
                    terminalMerchantsWallet.setId(UUID.createKey("", ""));
                    terminalMerchantsWallet.setMerId(payOrder.getMerId());
                    terminalMerchantsWallet.setTerminalMerId(payOrder.getTerminalMerId());
                    status = 1;
                }


                terminalMerchantsWallet.setTotalAmount(terminalMerchantsWallet.getTotalAmount() == null ? amount : terminalMerchantsWallet.getTotalAmount().add(amount).setScale(2, BigDecimal.ROUND_UP));
                //入账
                terminalMerchantsWallet.setIncomeAmount(amount.subtract(terPayFee));
                //总余额
                terminalMerchantsWallet.setTotalBalance(terminalMerchantsWallet.getTotalBalance() == null ? amount.subtract(terPayFee).setScale(2, BigDecimal.ROUND_UP) : terminalMerchantsWallet.getTotalBalance().add(amount.subtract(terPayFee)));
                //
                if (settlecycle.equals("D0")) {
                    //总可用
                    terminalMerchantsWallet.setTotalAvailableAmount(terminalMerchantsWallet.getTotalAvailableAmount() == null ? amount.subtract(terPayFee).setScale(2, BigDecimal.ROUND_UP) : terminalMerchantsWallet.getTotalAvailableAmount().add(amount.subtract(terPayFee)).setScale(2, BigDecimal.ROUND_UP));
                } else {
                    //总不可 原始表总不可用+ 订单金额-手续费-保证金
                    terminalMerchantsWallet.setTotalUnavailableAmount(terminalMerchantsWallet.getTotalUnavailableAmount() == null ? amount.subtract(terPayFee).setScale(2, BigDecimal.ROUND_UP) : terminalMerchantsWallet.getTotalUnavailableAmount().add(amount.subtract(terPayFee)).setScale(2, BigDecimal.ROUND_UP));
                }

                terminalMerchantsWallet.setTotalFee(terminalMerchantsWallet.getTotalFee() == null ? terPayFee : terminalMerchantsWallet.getTotalFee().add(terPayFee).setScale(2, BigDecimal.ROUND_UP));
                terminalMerchantsWallet.setUpdateTime(new Date());

                TerminalMerchantsDetails terminalMerchantsDetails = new TerminalMerchantsDetails();
                terminalMerchantsDetails.setId(UUID.createKey("terminal_merchants_details", ""));
                terminalMerchantsDetails.setMerId(payOrder.getMerId());
                terminalMerchantsDetails.setTerminalMerId(payOrder.getTerminalMerId());
                terminalMerchantsDetails.setType(channelInfo.getType().toString());
                terminalMerchantsDetails.setMerOrderId(tradeObjectSquare.getMerOrderId());
                terminalMerchantsDetails.setOrderId(payOrder.getPayId());
                terminalMerchantsDetails.setAmount(amount);
                terminalMerchantsDetails.setInAmount(amount.subtract(terPayFee).setScale(2, BigDecimal.ROUND_UP));
                terminalMerchantsDetails.setFee(terPayFee);
                terminalMerchantsDetails.setTotalBalance(terminalMerchantsWallet.getTotalBalance());
                terminalMerchantsDetails.setUpdateTime(new Date());

                recordPaymentSquareService.insertTerminalMerchantsDetails(terminalMerchantsDetails);
                if (status == 0) {
                    recordPaymentSquareService.updateTerminalMerchantsWallet(terminalMerchantsWallet);
                } else {
                    recordPaymentSquareService.insertTerminalMerchantsWallet(terminalMerchantsWallet);
                }

            }


            //代理商钱包
            if (merchantInfo.getParentId() != null && !merchantInfo.getParentId().equals("")) {
                AgentWallet agentWallet = recordPaymentSquareService.getAgentMerchantWallet(merchantInfo.getParentId());
                int agentStatus = 0;
                if (agentWallet == null) {
                    agentWallet = new AgentWallet();
                    agentWallet.setAgentMerchantId(merchantInfo.getParentId());
                    agentStatus = 1;
                }
//                AgentMerchantSetting agentMerchantSetting=  recordPaymentSquareService.getAgentMerchantSetting(merchantInfo.getParentId());
                AgentMerchantSetting agentMerchantSetting = recordPaymentSquareService.getAgentMerchantSettingByParentIdAndPayType(merchantInfo.getParentId(), payOrder.getPayType());
                BigDecimal agentRateFee = agentMerchantSetting.getRateFee();
                BigDecimal agenetFee = amount.divide(hundred).multiply(agentRateFee).setScale(2, BigDecimal.ROUND_UP);
                //订单总额
                agentWallet.setTotalAmount(agentWallet.getTotalAmount() == null ? amount : agentWallet.getTotalAmount().add(amount).setScale(2, BigDecimal.ROUND_UP));
                //入账总额
                agentWallet.setIncomeAmount(agentWallet.getIncomeAmount() == null ? agenetFee : agentWallet.getIncomeAmount().add(agenetFee).setScale(2, BigDecimal.ROUND_UP));
                //总余额
                agentWallet.setTotalBalance(agentWallet.getTotalBalance() == null ? agenetFee : agentWallet.getTotalBalance().add(agenetFee).setScale(2, BigDecimal.ROUND_UP));

                if (settlecycle.equals("D0")) {
                    //总可用 原始表总额 + 订单金额*手续费
                    agentWallet.setTotalAvailableAmount(agentWallet.getTotalAvailableAmount() == null ? agenetFee : agentWallet.getTotalAvailableAmount().add(agenetFee).setScale(2, BigDecimal.ROUND_UP));
                } else {
                    //总不可 原始表总不可用+ 订单金额*手续费
                    agentWallet.setTotalUnavailableAmount(agentWallet.getTotalUnavailableAmount() == null ? agenetFee : agentWallet.getTotalUnavailableAmount().add(agenetFee).setScale(2, BigDecimal.ROUND_UP));
                }


                //手续费总额?????

                agentWallet.setUpdateTime(new Date());
                if (agentStatus == 0) {
                    recordPaymentSquareService.updateAgentWallet(agentWallet);
                } else {
                    recordPaymentSquareService.insertAgentWallet(agentWallet);
                }
                AgentMerchantsDetails agentMerchantsDetails = new AgentMerchantsDetails();
                agentMerchantsDetails.setId(UUID.createKey("agent_merchants_details", ""));
                agentMerchantsDetails.setAgentMerId(agentWallet.getAgentMerchantId());
                agentMerchantsDetails.setMerOrderId(payOrder.getMerOrderId());
                agentMerchantsDetails.setOrderId(payOrder.getPayId());
                agentMerchantsDetails.setAmount(amount);
                agentMerchantsDetails.setType(channelInfo.getType().toString());
                agentMerchantsDetails.setInAmount(agenetFee);
                agentMerchantsDetails.setUpdateTime(new Date());
                agentMerchantsDetails.setTotalBenifit(agentWallet.getTotalBalance());
                recordPaymentSquareService.insertAgentMerchantsDetails(agentMerchantsDetails);
            }

            //通道钱包
            BigDecimal channelRateFee = channelInfo.getChannelRateFee() == null ? new BigDecimal(0) : channelInfo.getChannelRateFee();
            BigDecimal channelSingleFee = channelInfo.getChannelSingleFee() == null ? new BigDecimal(0) : channelInfo.getChannelSingleFee();
            BigDecimal channelIncomeAmount = amount.subtract(channelSingleFee.add(amount.divide(hundred).multiply(channelRateFee))).setScale(2, BigDecimal.ROUND_UP);
            BigDecimal channelFee = channelSingleFee.add(amount.divide(hundred).multiply(channelRateFee)).setScale(2, BigDecimal.ROUND_UP);
            BigDecimal channelFeeProfit = merTransFee.subtract(channelFee);

//            订单总额
            channelWallet.setTotalAmount(channelWallet.getTotalAmount() == null ? amount : channelWallet.getTotalAmount().add(amount).setScale(2, BigDecimal.ROUND_UP));
//            入账总额
            channelWallet.setIncomeAmount(channelWallet.getIncomeAmount() == null ? channelIncomeAmount : channelWallet.getIncomeAmount().add(channelIncomeAmount).setScale(2, BigDecimal.ROUND_UP));
//            手续费总额
            channelWallet.setTotalFee(channelWallet.getTotalFee() == null ? channelFee : channelWallet.getTotalFee().add(channelFee).setScale(2, BigDecimal.ROUND_UP));
//            总余额
            channelWallet.setTotalBalance(channelWallet.getTotalBalance() == null ? channelIncomeAmount : channelWallet.getTotalBalance().add(channelIncomeAmount).setScale(2, BigDecimal.ROUND_UP));

//            通道手续费-商户手续费
            channelWallet.setFeeProfit(channelWallet.getFeeProfit() == null ? channelFeeProfit : channelWallet.getFeeProfit().add(channelFeeProfit));

            if (settlecycle.equals("D0")) {
                //总可用
                channelWallet.setTotalAvailableAmount(channelWallet.getTotalAvailableAmount() == null ? channelIncomeAmount : channelWallet.getTotalAvailableAmount().add(channelIncomeAmount).setScale(2, BigDecimal.ROUND_UP));
            } else {
                //总不可 原始表总不可用+ 订单金额-手续费
                channelWallet.setTotalUnavailableAmount(channelWallet.getTotalUnavailableAmount() == null ? channelIncomeAmount : channelWallet.getTotalUnavailableAmount().add(channelIncomeAmount).setScale(2, BigDecimal.ROUND_UP));
            }

            channelWallet.setUpdateTime(new Date());

            ChannelDetails channelDetails = new ChannelDetails();
            channelDetails.setId(UUID.createKey("channel_details", ""));
            channelDetails.setChannelId(channelInfo.getChannelId());
            channelDetails.setChannelTransCode(channelInfo.getChannelTransCode());
            channelDetails.setPayType(payOrder.getPayType());
            channelDetails.setMerOrderId(payOrder.getMerOrderId());
            channelDetails.setOrderId(payOrder.getPayId());
            channelDetails.setType(channelInfo.getType().toString());
            channelDetails.setAmount(amount);
            channelDetails.setFeeProfit(channelFeeProfit);
            channelDetails.setFee(channelFee);
            channelDetails.setInAmount(channelIncomeAmount);
            channelDetails.setTotalBalance(channelWallet.getTotalBalance());
            channelDetails.setUpdateTime(new Date());
            try {
                if (merStatus == 0) {
                    recordPaymentSquareService.updateMerchantWallet(merchantWallet);
                } else {
                    recordPaymentSquareService.insertMerchantWallet(merchantWallet);
                }
                if (channelStatus == 0) {
                    recordPaymentSquareService.updateChannelWallet(channelWallet);
                } else {
                    recordPaymentSquareService.insertChannelWallet(channelWallet);
                }
                recordPaymentSquareService.insertMerchantsDetails(merchantsDetails);
                recordPaymentSquareService.insertChannelDetails(channelDetails);
            } catch (Exception e) {
                throw e;
            }
        }
    }

    public void notifyUpdateWallet(BankResult bankResult) {
        synchronized (this) {
            Long orderId = bankResult.getOrderId();
            PayOrder payOrder = recordPaymentSquareService.getPayOrderById(orderId.toString());
            MerchantInfo merchantInfo = paymentRecordSquareService.getMerchantInfoByMerId(payOrder.getMerId());
            SystemOrderTrack systemOrderTrack = paymentRecordSquareService.getSystemOrderTrack(payOrder.getMerOrderId());
            TradeObjectSquare tradeObjectSquare = JsonUtils.jsonToPojo(systemOrderTrack.getTradeInfo(), TradeObjectSquare.class);
            ChannelInfo payOrderChannel = paymentRecordSquareService.getChannelInfo(payOrder.getChannelId());
            MerchantWallet merchantWallet = recordPaymentSquareService.getMerchantWallet(merchantInfo.getMerId());
            MerchantRate merchantRate = recordPaymentSquareService.getMerchantRateByIdAndPayType(merchantInfo.getMerId(), payOrder.getPayType());
            String settlecycle = merchantRate.getSettlecycle();
            ChannelInfo channelInfo = recordPaymentSquareService.getChannelInfo(payOrder.getChannelId());
            ChannelWallet channelWallet = recordPaymentSquareService.getChannelWallet(payOrderChannel.getChannelId());
            int merStatus = 0;
            if (merchantWallet == null) {
                merchantWallet = new MerchantWallet();
                merchantWallet.setMerId(payOrder.getMerId());
                merStatus = 1;
            }
            int channelStatus = 0;
            if (channelWallet == null) {
                channelWallet = new ChannelWallet();
                channelWallet.setId(UUID.createKey("channel_wallet", ""));
                channelWallet.setChannelId(payOrderChannel.getChannelId());
                channelWallet.setChannelTransCode(channelInfo.getChannelTransCode());
                channelWallet.setPayType(channelInfo.getType().toString());
                channelStatus = 1;
            }
            //获取实际交易金额
            BigDecimal hundred = new BigDecimal(100);
            BigDecimal orderAmount = payOrder.getAmount();

            BigDecimal terPayRate = tradeObjectSquare.getPayFee() == null ? new BigDecimal(0) : tradeObjectSquare.getPayFee();

            //商户手续费
            BigDecimal merRate = merchantRate.getRateFee() == null ? new BigDecimal(0) : merchantRate.getRateFee();
            BigDecimal singlFee = merchantRate.getSingleFee() == null ? new BigDecimal(0) : merchantRate.getSingleFee();
            //商户手续费
            BigDecimal merBondRate = merchantRate.getBondRate() == null ? new BigDecimal(0) : merchantRate.getBondRate();
            // 订单金额/100 * 商户手续费 + 单笔
            BigDecimal merTransFee = orderAmount.divide(hundred).multiply(merRate).add(singlFee).setScale(2, BigDecimal.ROUND_UP);
            BigDecimal merBondAmount = orderAmount.divide(hundred).multiply(merBondRate).setScale(2, BigDecimal.ROUND_UP);
            BigDecimal payFee = tradeObjectSquare.getPayFee() == null ? new BigDecimal(0) : tradeObjectSquare.getPayFee();
            BigDecimal terPayFee = orderAmount.divide(hundred).multiply(payFee).setScale(2, BigDecimal.ROUND_UP);
            BigDecimal merFeeProfit = terPayFee.subtract(merTransFee).setScale(2, BigDecimal.ROUND_UP);

            //商户交易钱包
            merchantWallet.setTotalAmount(merchantWallet.getTotalAmount() == null ? orderAmount : merchantWallet.getTotalAmount().add(orderAmount));
            //入账总额  原始表总余额+(订单金额-手续费)
            merchantWallet.setIncomeAmount(merchantWallet.getIncomeAmount() == null ? orderAmount.subtract(merTransFee) : merchantWallet.getIncomeAmount().add(orderAmount.subtract(merTransFee)).setScale(2, BigDecimal.ROUND_UP));
            //手续费成本：原始表手续费金额+（单笔手续费+订单金额*商户交易手续费）
            merchantWallet.setTotalFee(merchantWallet.getTotalFee() == null ? merTransFee : merchantWallet.getTotalFee().add(merTransFee).setScale(2, BigDecimal.ROUND_UP));
            //保证金：原始表保证金金额+ 订单金额* 商户保证金比例
            merchantWallet.setTotalBond(merchantWallet.getTotalBond() == null ? merBondAmount : merchantWallet.getTotalBond().add(merBondAmount).setScale(2, BigDecimal.ROUND_UP));
            //总余额 原始表总余额+订单金额-手续费
            merchantWallet.setTotalBalance(merchantWallet.getTotalBalance() == null ? orderAmount.subtract(merTransFee) : merchantWallet.getTotalBalance().add(orderAmount.subtract(merTransFee)).setScale(2, BigDecimal.ROUND_UP));
            //手续费利润  终端手续费- 商户手续费=商户利润
            merchantWallet.setFeeProfit(merchantWallet.getFeeProfit() == null ? merFeeProfit : merchantWallet.getFeeProfit().add(merFeeProfit));

            if (settlecycle.equals("D0")) {
                //总可用
                merchantWallet.setTotalAvailableAmount(merchantWallet.getTotalAvailableAmount() == null ? orderAmount.subtract(merTransFee).setScale(2, BigDecimal.ROUND_UP) : merchantWallet.getTotalAvailableAmount().add(orderAmount.subtract(merTransFee)).setScale(2, BigDecimal.ROUND_UP));
            } else {
                //总不可 原始表总不可用+ 订单金额-手续费-保证金
                merchantWallet.setTotalUnavailableAmount(merchantWallet.getTotalUnavailableAmount() == null ? orderAmount.subtract(merTransFee).setScale(2, BigDecimal.ROUND_UP).subtract(merBondAmount) : merchantWallet.getTotalUnavailableAmount().add(orderAmount.subtract(merTransFee).subtract(merBondAmount)).setScale(2, BigDecimal.ROUND_UP));
            }

            merchantWallet.setUpdateTime(new Date());

            MerchantsDetails merchantsDetails = new MerchantsDetails();
            merchantsDetails.setId(UUID.createKey("merchants_details", ""));
            merchantsDetails.setMerId(merchantWallet.getMerId());
            merchantsDetails.setType(channelInfo.getType().toString());
            merchantsDetails.setMerOrderId(payOrder.getMerOrderId());
            merchantsDetails.setOrderId(payOrder.getPayId());
            merchantsDetails.setAmount(payOrder.getAmount());
            merchantsDetails.setInAmount(orderAmount.subtract(merTransFee));
            merchantsDetails.setFeeProfit(merFeeProfit);
            merchantsDetails.setFee(merTransFee);
            merchantsDetails.setTotalBalance(merchantWallet.getTotalBalance());
            merchantsDetails.setUpdateTime(new Date());


            //下级商户
            if (payOrder.getTerminalMerId() != null && !payOrder.getTerminalMerId().equals("")) {
                TerminalMerchantsWallet terminalMerchantsWallet = recordPaymentSquareService.getTerminalMerchantsWallet(payOrder.getMerId(), payOrder.getTerminalMerId());
                int status = 0;
                if (terminalMerchantsWallet == null) {
                    terminalMerchantsWallet = new TerminalMerchantsWallet();
                    terminalMerchantsWallet.setId(UUID.createKey("", ""));
                    terminalMerchantsWallet.setMerId(payOrder.getMerId());
                    terminalMerchantsWallet.setTerminalMerId(payOrder.getTerminalMerId());
                    status = 1;
                }
                BigDecimal terTransFee = orderAmount.divide(hundred).multiply(terPayRate);
                //出账
                terminalMerchantsWallet.setOutAmount(terminalMerchantsWallet.getOutAmount() == null ? orderAmount : terminalMerchantsWallet.getOutAmount().add(orderAmount));
                //总余额 1-a-fee
                terminalMerchantsWallet.setTotalBalance(terminalMerchantsWallet.getTotalBalance().subtract(orderAmount));
                //总手续费
                terminalMerchantsWallet.setTotalFee(terminalMerchantsWallet.getTotalFee() == null ? terTransFee : terminalMerchantsWallet.getTotalFee().add(terTransFee));
                //总可用 1-a-fee
                terminalMerchantsWallet.setTotalAvailableAmount(terminalMerchantsWallet.getTotalAvailableAmount().subtract(orderAmount));

                terminalMerchantsWallet.setUpdateTime(new Date());

                TerminalMerchantsDetails terminalMerchantsDetails = new TerminalMerchantsDetails();
                terminalMerchantsDetails.setId(UUID.createKey("terminal_merchants_details", ""));
                terminalMerchantsDetails.setMerId(payOrder.getMerId());
                terminalMerchantsDetails.setTerminalMerId(payOrder.getTerminalMerId());
                terminalMerchantsDetails.setType(channelInfo.getType().toString());
                terminalMerchantsDetails.setMerOrderId(tradeObjectSquare.getMerOrderId());
                terminalMerchantsDetails.setOrderId(payOrder.getPayId());
                terminalMerchantsDetails.setAmount(orderAmount);
                terminalMerchantsDetails.setOutAmount(orderAmount);
                terminalMerchantsDetails.setFee(terTransFee);
                terminalMerchantsDetails.setTotalBalance(terminalMerchantsWallet.getTotalBalance());
                terminalMerchantsDetails.setUpdateTime(new Date());

                recordPaymentSquareService.insertTerminalMerchantsDetails(terminalMerchantsDetails);
                if (status == 0) {
                    recordPaymentSquareService.updateTerminalMerchantsWallet(terminalMerchantsWallet);
                } else {
                    recordPaymentSquareService.insertTerminalMerchantsWallet(terminalMerchantsWallet);
                }

            }


            //代理商钱包
            if (merchantInfo.getParentId() != null && !merchantInfo.getParentId().equals("")) {
                AgentWallet agentWallet = recordPaymentSquareService.getAgentMerchantWallet(merchantInfo.getParentId());
                int agentStatus = 0;
                if (agentWallet == null) {
                    agentWallet = new AgentWallet();
                    agentWallet.setAgentMerchantId(merchantInfo.getParentId());
                    agentStatus = 1;
                }
    //                AgentMerchantSetting agentMerchantSetting=  recordPaymentSquareService.getAgentMerchantSetting(merchantInfo.getParentId());
                AgentMerchantSetting agentMerchantSetting = recordPaymentSquareService.getAgentMerchantSettingByParentIdAndPayType(merchantInfo.getParentId(), payOrder.getPayType());
                BigDecimal agentRateFee = agentMerchantSetting.getRateFee();
                BigDecimal agenetFee = orderAmount.divide(hundred).multiply(agentRateFee).setScale(2, BigDecimal.ROUND_UP);
                //订单总额
                agentWallet.setTotalAmount(agentWallet.getTotalAmount() == null ? orderAmount : agentWallet.getTotalAmount().add(orderAmount).setScale(2, BigDecimal.ROUND_UP));
                //入账总额
                agentWallet.setIncomeAmount(agentWallet.getIncomeAmount() == null ? agenetFee : agentWallet.getIncomeAmount().add(agenetFee).setScale(2, BigDecimal.ROUND_UP));
                //总余额
                agentWallet.setTotalBalance(agentWallet.getTotalBalance() == null ? agenetFee : agentWallet.getTotalBalance().add(agenetFee).setScale(2, BigDecimal.ROUND_UP));

                if (settlecycle.equals("D0")) {
                    //总可用 原始表总额 + 订单金额*手续费
                    agentWallet.setTotalAvailableAmount(agentWallet.getTotalAvailableAmount() == null ? agenetFee : agentWallet.getTotalAvailableAmount().add(agenetFee).setScale(2, BigDecimal.ROUND_UP));
                } else {
                    //总不可 原始表总不可用+ 订单金额*手续费
                    agentWallet.setTotalUnavailableAmount(agentWallet.getTotalUnavailableAmount() == null ? agenetFee : agentWallet.getTotalUnavailableAmount().add(agenetFee).setScale(2, BigDecimal.ROUND_UP));
                }


                //手续费总额?????

                agentWallet.setUpdateTime(new Date());
                if (agentStatus == 0) {
                    recordPaymentSquareService.updateAgentWallet(agentWallet);
                } else {
                    recordPaymentSquareService.insertAgentWallet(agentWallet);
                }

                AgentMerchantsDetails agentMerchantsDetails = new AgentMerchantsDetails();
                agentMerchantsDetails.setId(UUID.createKey("agent_merchants_details", ""));
                agentMerchantsDetails.setAgentMerId(agentWallet.getAgentMerchantId());
                agentMerchantsDetails.setMerOrderId(payOrder.getMerOrderId());
                agentMerchantsDetails.setOrderId(payOrder.getPayId());
                agentMerchantsDetails.setAmount(orderAmount);
                agentMerchantsDetails.setType(channelInfo.getType().toString());
                agentMerchantsDetails.setInAmount(agenetFee);
                agentMerchantsDetails.setUpdateTime(new Date());
                recordPaymentSquareService.insertAgentMerchantsDetails(agentMerchantsDetails);
            }

            //通道钱包
            BigDecimal channelRateFee = channelInfo.getChannelRateFee() == null ? new BigDecimal(0) : channelInfo.getChannelRateFee();
            BigDecimal channelSingleFee = channelInfo.getChannelSingleFee() == null ? new BigDecimal(0) : channelInfo.getChannelSingleFee();
            BigDecimal channelFee = orderAmount.divide(hundred).multiply(channelRateFee).add(channelSingleFee).setScale(2, BigDecimal.ROUND_UP);
            BigDecimal channelFeeProfit = merTransFee.subtract(channelFee).setScale(2, BigDecimal.ROUND_UP);

            //通道出账成本 订单金额+通道手续费
            BigDecimal channelOutAmount = orderAmount.add(channelFee).setScale(2, BigDecimal.ROUND_UP);

//        channelWallet.setTotalAmount(channelWallet.getTotalAmount() == null ? amount : channelWallet.getTotalAmount().add(amount));
            channelWallet.setOutAmount(channelWallet.getOutAmount() == null ? channelOutAmount : channelWallet.getOutAmount().add(channelOutAmount));
            channelWallet.setTotalFee(channelWallet.getTotalFee() == null ? channelFee : channelWallet.getTotalFee().add(channelFee));
            channelWallet.setTotalBalance(channelWallet.getTotalBalance().subtract(channelOutAmount));
            channelWallet.setTotalAvailableAmount(channelWallet.getTotalAvailableAmount().subtract(channelOutAmount));
            channelWallet.setFeeProfit(channelWallet.getFeeProfit() == null ? channelFeeProfit : channelWallet.getFeeProfit().add(channelFeeProfit));
            channelWallet.setUpdateTime(new Date());

            ChannelDetails channelDetails = new ChannelDetails();
            channelDetails.setId(UUID.createKey("channel_details", ""));
            channelDetails.setChannelId(channelInfo.getChannelId());
            channelDetails.setChannelTransCode(channelInfo.getChannelTransCode());
            channelDetails.setPayType(payOrder.getPayType());
            channelDetails.setOrderId(payOrder.getPayId());
            channelDetails.setMerOrderId(payOrder.getMerOrderId());
            channelDetails.setType(channelInfo.getType().toString());
            channelDetails.setAmount(orderAmount);
            channelDetails.setFeeProfit(channelFeeProfit);
            channelDetails.setFee(channelFee);
            channelDetails.setOutAmount(channelOutAmount);
            channelDetails.setTotalBalance(channelWallet.getTotalBalance());
            channelDetails.setUpdateTime(new Date());


            if (merStatus == 0) {
                recordPaymentSquareService.updateMerchantWallet(merchantWallet);
            } else {
                recordPaymentSquareService.insertMerchantWallet(merchantWallet);
            }
            if (channelStatus == 0) {
                recordPaymentSquareService.updateChannelWallet(channelWallet);
            } else {
                recordPaymentSquareService.insertChannelWallet(channelWallet);
            }
            recordPaymentSquareService.insertMerchantsDetails(merchantsDetails);
            recordPaymentSquareService.insertChannelDetails(channelDetails);
        }
    }


    public void updateRiskQuotaData(PayOrder payOrder, ChannelInfo channelInfo, MerchantQuotaRisk merchantQuotaRisk) throws PayException {
        List<RiskQuotaData> quotaDataList = getRiskQuotaData(channelInfo, payOrder);
        // quotaTodoSquare(payOrder,quotaDataList,channelInfo,merchantQuotaRisk);
        quotaDataList.forEach(riskQuotaData -> {
            // if (StringUtils.equals(riskQuotaData.getType().toString(), payOrder.getPayType())) {
            riskQuotaData.setAmount(riskQuotaData.getAmount() == null ? payOrder.getAmount() : riskQuotaData.getAmount().add(payOrder.getAmount()));
            // }
        });
        insertRiskQuotaData(quotaDataList);
    }

    List<RiskQuotaData> getRiskQuotaData(ChannelInfo channelInfo, PayOrder payOrder) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String day = sdf.format(new Date());
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
        String month = sdf1.format(new Date());
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy");
        String year = sdf2.format(new Date());
        return riskQuotaDataCache.getQuotaData1(payOrder.getMerId(), channelInfo.getChannelId(), day, month, year);
    }

    public Integer insertRiskQuotaData(List<RiskQuotaData> quotaDataList) {
        return paymentRecordSquareService.insertOrUpdateRiskQuotaData(quotaDataList);
    }

    public void updateMerchantChannelHistory(SquareTrade trade) {
        MerchantChannelHistory merchantChannelHistory = trade.getMerchantChannelHistory();
        ChannelInfo channelInfo = trade.getChannelInfo();
        PayOrder payOrder = trade.getPayOrder();
        if (merchantChannelHistory != null) {

            if (channelInfo.getChannelId().equals(merchantChannelHistory.getChannelId())) {
                merchantChannelHistory.setAmount(merchantChannelHistory.getAmount().add(payOrder.getAmount()));
                merchantChannelHistory.setTradeCount(merchantChannelHistory.getTradeCount() + 1);
                paymentRecordSquareService.updateMerchantChannelHistory(merchantChannelHistory);
            } else {
                MerchantChannelHistory newData = new MerchantChannelHistory();
                newData.setId(UUID.createKey("merchant_channel_history", ""));
                newData.setOrganizationId(channelInfo.getOrganizationId());
                newData.setChannelId(channelInfo.getChannelId());
                newData.setMerId(payOrder.getMerId());
                newData.setChannelType(channelInfo.getType().toString());
                newData.setAmount(payOrder.getAmount());
                newData.setTradeCount(1);
                newData.setCreateTime(new Date());
                newData.setStatus(SystemConstant.SUCCESS);
                merchantChannelHistory.setReason("通道限额不足");
                merchantChannelHistory.setStatus(SystemConstant.FAIL);
                merchantChannelHistory.setChangeTime(new Date());
                paymentRecordSquareService.updateMerchantChannelHistory(merchantChannelHistory);
                paymentRecordSquareService.insertMerchantChannelHistory(newData);
            }

        } else {
            MerchantChannelHistory newData = new MerchantChannelHistory();
            newData.setId(UUID.createKey("merchant_channel_history", ""));
            newData.setOrganizationId(channelInfo.getOrganizationId());
            newData.setChannelId(channelInfo.getChannelId());
            newData.setMerId(payOrder.getMerId());
            newData.setChannelType(channelInfo.getType().toString());
            newData.setAmount(payOrder.getAmount());
            newData.setTradeCount(1);
            newData.setCreateTime(new Date());
            newData.setStatus(SystemConstant.SUCCESS);
            paymentRecordSquareService.insertMerchantChannelHistory(newData);
        }


    }

    public void notifyRevertWallet(BankResult bankResult) {
        Long orderId = bankResult.getOrderId();
        PayOrder payOrder = recordPaymentSquareService.getPayOrderById(orderId.toString());
        BigDecimal orderAmount = payOrder.getAmount();
        MerchantInfo merchantInfo = paymentRecordSquareService.getMerchantInfoByMerId(payOrder.getMerId());
        MerchantRate merchantRate = recordPaymentSquareService.getMerchantRateByIdAndPayType(merchantInfo.getMerId(), payOrder.getPayType());
        String settlecycle = merchantRate.getSettlecycle();
        MerchantsDetails merchantsDetails = recordPaymentSquareService.getMerchantDetails(payOrder.getPayId());
        BigDecimal merBondRate = merchantRate.getBondRate();
        BigDecimal hundred = new BigDecimal(100);
        BigDecimal merTransFee = merchantsDetails.getFee();
        BigDecimal merFeeProfit = merchantsDetails.getFeeProfit();
        BigDecimal merInAmount = merchantsDetails.getInAmount();
        BigDecimal merBondAmount = orderAmount.divide(hundred).multiply(merBondRate).setScale(2, BigDecimal.ROUND_UP);

        MerchantWallet merchantWallet = recordPaymentSquareService.getMerchantWallet(payOrder.getMerId());
        merchantWallet.setTotalAmount(merchantWallet.getTotalAmount().subtract(orderAmount));
        merchantWallet.setIncomeAmount(merchantWallet.getIncomeAmount().subtract(merInAmount));
        merchantWallet.setTotalFee(merchantWallet.getTotalFee().subtract(merTransFee));
        merchantWallet.setFeeProfit(merchantWallet.getFeeProfit().subtract(merFeeProfit));
        merchantWallet.setTotalBond(  merchantWallet.getTotalBond().subtract(merBondAmount).setScale(2, BigDecimal.ROUND_UP));
        merchantWallet.setTotalBalance(merchantWallet.getTotalBalance().subtract(merInAmount));
        if (settlecycle.equals("D0")) {
            merchantWallet.setTotalAvailableAmount( merchantWallet.getTotalAvailableAmount().subtract(merInAmount).setScale(2, BigDecimal.ROUND_UP));
        } else {
            //总不可 原始表总不可用 -  订单金额-手续费-保证金
            merchantWallet.setTotalUnavailableAmount( merchantWallet.getTotalUnavailableAmount().subtract(merInAmount.subtract(merBondAmount)).setScale(2, BigDecimal.ROUND_UP));
        }
        merchantWallet.setUpdateTime(new Date());
        recordPaymentSquareService.updateMerchantWallet(merchantWallet);

        MerchantsDetails merchantsDetail = new MerchantsDetails();
        merchantsDetail.setId(UUID.createKey("merchants_details", ""));
        merchantsDetail.setMerId(merchantWallet.getMerId());
        merchantsDetail.setType(SystemConstant.CORRECT);
        merchantsDetail.setMerOrderId(payOrder.getMerOrderId());
        merchantsDetail.setOrderId(payOrder.getPayId());
        merchantsDetail.setAmount(payOrder.getAmount().negate());
        merchantsDetail.setOutAmount(merInAmount);
        merchantsDetail.setFeeProfit(merFeeProfit.negate());
        merchantsDetail.setFee(merTransFee.negate());
        merchantsDetail.setTotalBalance(merchantWallet.getTotalBalance());
        merchantsDetail.setUpdateTime(new Date());
        recordPaymentSquareService.insertMerchantsDetails(merchantsDetail);

        //终端商户钱包回退
        if (payOrder.getTerminalMerId() != null && !payOrder.getTerminalMerId().equals("")) {
            TerminalMerchantsDetails terminalMerchantsDetails = recordPaymentSquareService.getTerminalMerchantsDetails(payOrder.getPayId());
            BigDecimal inAmount = terminalMerchantsDetails.getInAmount();
            BigDecimal terPayfee = terminalMerchantsDetails.getFee();
            TerminalMerchantsWallet terminalMerchantsWallet = recordPaymentSquareService.getTerminalMerchantsWallet(payOrder.getMerId(), payOrder.getTerminalMerId());
            terminalMerchantsWallet.setTotalAmount(terminalMerchantsWallet.getTotalAmount().subtract(orderAmount));
            terminalMerchantsWallet.setIncomeAmount(terminalMerchantsWallet.getIncomeAmount().subtract(inAmount));
            terminalMerchantsWallet.setTotalBalance(terminalMerchantsWallet.getTotalBalance().subtract(inAmount));
            if (settlecycle.equals("D0")) {
                terminalMerchantsWallet.setTotalAvailableAmount(  terminalMerchantsWallet.getTotalAvailableAmount().subtract(inAmount).setScale(2, BigDecimal.ROUND_UP));
            } else {
                terminalMerchantsWallet.setTotalUnavailableAmount(  terminalMerchantsWallet.getTotalUnavailableAmount().subtract(inAmount).setScale(2, BigDecimal.ROUND_UP));
            }
            terminalMerchantsWallet.setTotalFee( terminalMerchantsWallet.getTotalFee().subtract(terPayfee).setScale(2, BigDecimal.ROUND_UP));
            terminalMerchantsWallet.setUpdateTime(new Date());
            recordPaymentSquareService.updateTerminalMerchantsWallet(terminalMerchantsWallet);
            TerminalMerchantsDetails terminalMerchantsDetail = new TerminalMerchantsDetails();
            terminalMerchantsDetail.setId(UUID.createKey("terminal_merchants_details", ""));
            terminalMerchantsDetail.setMerId(payOrder.getMerId());
            terminalMerchantsDetail.setTerminalMerId(payOrder.getTerminalMerId());
            terminalMerchantsDetail.setType(SystemConstant.CORRECT);
            terminalMerchantsDetail.setMerOrderId(payOrder.getMerOrderId());
            terminalMerchantsDetail.setOrderId(payOrder.getPayId());
            terminalMerchantsDetail.setAmount(orderAmount.negate());
            terminalMerchantsDetail.setOutAmount(inAmount);
            terminalMerchantsDetail.setFee(terPayfee.negate());
            terminalMerchantsDetail.setTotalBalance(terminalMerchantsWallet.getTotalBalance());
            terminalMerchantsDetail.setUpdateTime(new Date());
            recordPaymentSquareService.insertTerminalMerchantsDetails(terminalMerchantsDetail);
        }


        //通道钱包回退
        ChannelDetails channelDetails = recordPaymentSquareService.getChannelDetails(payOrder.getPayId());
        ChannelWallet channelWallet = recordPaymentSquareService.getChannelWallet(payOrder.getChannelId());
        BigDecimal channelFee = channelDetails.getFee();
        BigDecimal channelInAmount = channelDetails.getInAmount();
        BigDecimal channelFeeProfit = channelDetails.getFeeProfit();
        channelWallet.setTotalAmount(channelWallet.getTotalAmount().subtract(orderAmount));
        channelWallet.setIncomeAmount(channelWallet.getIncomeAmount().subtract(orderAmount));
        channelWallet.setTotalFee(channelWallet.getTotalFee().subtract(channelFee).setScale(2, BigDecimal.ROUND_UP));
        channelWallet.setTotalBalance(channelWallet.getTotalBalance().subtract(channelInAmount).setScale(2, BigDecimal.ROUND_UP));
        channelWallet.setFeeProfit(channelWallet.getFeeProfit().subtract(channelFeeProfit));
        if (settlecycle.equals("D0")) {
            channelWallet.setTotalAvailableAmount(channelWallet.getTotalAvailableAmount().subtract(channelInAmount).setScale(2, BigDecimal.ROUND_UP));
        } else {
            channelWallet.setTotalUnavailableAmount(channelWallet.getTotalUnavailableAmount().subtract(channelInAmount).setScale(2, BigDecimal.ROUND_UP));
        }
        channelWallet.setUpdateTime(new Date());
        ChannelDetails channelReDetails = new ChannelDetails();
        channelReDetails.setId(UUID.createKey("channel_details", ""));
        channelReDetails.setChannelId(channelDetails.getChannelId());
        channelReDetails.setChannelTransCode(channelDetails.getChannelTransCode());
        channelReDetails.setPayType(payOrder.getPayType());
        channelReDetails.setMerOrderId(payOrder.getMerOrderId());
        channelReDetails.setOrderId(payOrder.getPayId());
        channelReDetails.setType(SystemConstant.CORRECT);
        channelReDetails.setAmount(orderAmount.negate());
        channelReDetails.setOutAmount(channelInAmount);
        channelReDetails.setFeeProfit(channelFeeProfit.negate());
        channelReDetails.setFee(channelFee.negate());
        channelReDetails.setTotalBalance(channelWallet.getTotalBalance());
        channelReDetails.setUpdateTime(new Date());
        recordPaymentSquareService.updateChannelWallet(channelWallet);
        recordPaymentSquareService.insertChannelDetails(channelReDetails);

        //代理商钱包回退
        if (merchantInfo.getParentId() != null && !merchantInfo.getParentId().equals("")) {
            AgentMerchantsDetails agentDetails = recordPaymentSquareService.getAgentMerchantsDetails(payOrder.getPayId());
            AgentWallet agentMerchantWallet = recordPaymentSquareService.getAgentMerchantWallet(merchantInfo.getParentId());
            BigDecimal inAmount = agentDetails.getInAmount();
            BigDecimal amount = agentDetails.getAmount();
            agentMerchantWallet.setTotalAmount(agentMerchantWallet.getTotalAmount().subtract(amount));
            agentMerchantWallet.setIncomeAmount(agentMerchantWallet.getIncomeAmount().subtract(inAmount));
            agentMerchantWallet.setTotalBalance(agentMerchantWallet.getTotalBalance().subtract(inAmount));
            if (settlecycle.equals("D0")) {
                //总可用 原始表总额 + 订单金额*手续费
                agentMerchantWallet.setTotalAvailableAmount(agentMerchantWallet.getTotalAvailableAmount().subtract(inAmount).setScale(2, BigDecimal.ROUND_UP));
            } else {
                //总不可 原始表总不可用+ 订单金额*手续费
                agentMerchantWallet.setTotalUnavailableAmount(agentMerchantWallet.getTotalUnavailableAmount().subtract(inAmount).setScale(2, BigDecimal.ROUND_UP));
            }
            recordPaymentSquareService.updateAgentWallet(agentMerchantWallet);
            AgentMerchantsDetails agentMerchantsDetails = new AgentMerchantsDetails();
            agentMerchantsDetails.setId(UUID.createKey("agent_merchants_details", ""));
            agentMerchantsDetails.setAgentMerId(agentMerchantWallet.getAgentMerchantId());
            agentMerchantsDetails.setMerOrderId(payOrder.getMerOrderId());
            agentMerchantsDetails.setOrderId(payOrder.getPayId());
            agentMerchantsDetails.setAmount(amount.negate());
            agentMerchantsDetails.setType(SystemConstant.CORRECT);
            agentMerchantsDetails.setOutAmount(inAmount);
            agentMerchantsDetails.setUpdateTime(new Date());
            agentMerchantsDetails.setTotalBenifit(agentMerchantWallet.getTotalBalance());
            recordPaymentSquareService.insertAgentMerchantsDetails(agentMerchantsDetails);
        }

    }
}
