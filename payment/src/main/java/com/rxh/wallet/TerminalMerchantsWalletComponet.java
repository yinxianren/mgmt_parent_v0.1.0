package com.rxh.wallet;

import com.rxh.exception.PayException;
import com.rxh.square.pojo.MerchantRate;
import com.rxh.square.pojo.TerminalMerchantsDetails;
import com.rxh.square.pojo.TerminalMerchantsWallet;
import com.rxh.utils.PayMap;
import com.rxh.utils.UUID;
import com.rxh.vo.OrderObjectToMQ;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 描述：终端钱包业务
 * @author  panda
 * @date 20190721
 */
@Component
public class TerminalMerchantsWalletComponet extends AbstractWalletComponent {




    /**
     *    支付终端商户钱包
     * @param orderObjectToMQ
     * @param payMap
     * @return
     * @throws PayException
     */
    public PayMap<String,Object> getTerminalMerchantsObject(OrderObjectToMQ orderObjectToMQ, PayMap<String,Object> payMap) throws PayException {
        String merId= orderObjectToMQ.getMerId();
        String terminalMerId=orderObjectToMQ.getTerminalMerId();
        String payType=orderObjectToMQ.getChannelType().toString();

        TerminalMerchantsWallet terminalMerchantsWallet = paymentRecordSquareService.getTerminalMerchantsWallet(merId,terminalMerId);
        boolean isInsert=false;
        if (null == terminalMerchantsWallet) {
            terminalMerchantsWallet = new TerminalMerchantsWallet();
            terminalMerchantsWallet.setId(UUID.createKey("terminal_merchants_details", ""));
            terminalMerchantsWallet.setMerId(merId);
            terminalMerchantsWallet.setTerminalMerId(terminalMerId);
            isInsert=true;
        }

        //获取商户费率
        MerchantRate merchantRate= redisCacheCommonCompoment.merchantRateCache.getOne(merId, payType);
        isNull(merchantRate,format("【MQ队列任务-钱包处理】商户号：%s,支付类型：%s,获取商户费率对象为null",merId,payType));

        //获取交易金额
        BigDecimal amount = orderObjectToMQ.getAmount();
        if (orderObjectToMQ.getPayType().equals("6")){
            amount = new BigDecimal(0).subtract(amount);
        }
        BigDecimal hundred = new BigDecimal(100);
        BigDecimal payFee = orderObjectToMQ.getPayFee() == null ? new BigDecimal(0) : orderObjectToMQ.getPayFee();
        BigDecimal terPayFee = amount.divide(hundred).multiply(payFee).setScale(2, BigDecimal.ROUND_UP);

        terminalMerchantsWallet.setTotalAmount(terminalMerchantsWallet.getTotalAmount() == null ?
                amount : terminalMerchantsWallet.getTotalAmount().add(amount).setScale(2, BigDecimal.ROUND_UP));
        //入账
        terminalMerchantsWallet.setIncomeAmount(terminalMerchantsWallet.getIncomeAmount() == null?
                amount.subtract(terPayFee):
                terminalMerchantsWallet.getIncomeAmount().add(amount.subtract(terPayFee)));

        //总余额
        terminalMerchantsWallet.setTotalBalance(terminalMerchantsWallet.getTotalBalance() == null ?
                amount.subtract(terPayFee).setScale(2, BigDecimal.ROUND_UP) :
                terminalMerchantsWallet.getTotalBalance().add(amount.subtract(terPayFee)));
        //总可用
        String settlecycle = merchantRate.getSettlecycle();//结算周期
        if (settlecycle.equalsIgnoreCase("D0") || settlecycle.equalsIgnoreCase("T0")) terminalMerchantsWallet.setTotalAvailableAmount(terminalMerchantsWallet.getTotalAvailableAmount() == null ?
                amount.subtract(terPayFee).setScale(2, BigDecimal.ROUND_UP) :
                terminalMerchantsWallet.getTotalAvailableAmount().add(amount.subtract(terPayFee)).setScale(2, BigDecimal.ROUND_UP));
            //总不可 原始表总不可用+ 订单金额-手续费-保证金
        else terminalMerchantsWallet.setTotalUnavailableAmount(terminalMerchantsWallet.getTotalUnavailableAmount() == null ?
                amount.subtract(terPayFee).setScale(2, BigDecimal.ROUND_UP) :
                terminalMerchantsWallet.getTotalUnavailableAmount().add(amount.subtract(terPayFee)).setScale(2, BigDecimal.ROUND_UP));

        terminalMerchantsWallet.setTotalFee(terminalMerchantsWallet.getTotalFee() == null ?
                terPayFee : terminalMerchantsWallet.getTotalFee().add(terPayFee).setScale(2, BigDecimal.ROUND_UP));

        terminalMerchantsWallet.setUpdateTime(new Date());

        if(isInsert)  payMap.put("insertTerminalMerchantsWallet",terminalMerchantsWallet);
        else payMap.put("updateTerminalMerchantsWallet",terminalMerchantsWallet);
        return payMap;
    }



    /**
     *
     *  支付终端商户钱包明细
     * @param orderObjectToMQ
     * @param payMap
     * @return
     * @throws PayException
     */
    public PayMap<String,Object> getTerminalMerchantsDetailsObject(OrderObjectToMQ orderObjectToMQ, PayMap<String,Object> payMap) throws PayException {

        //获取交易金额
        BigDecimal amount = orderObjectToMQ.getAmount();
        if (orderObjectToMQ.getPayType().equals("6")){
            amount = new BigDecimal(0).subtract(amount);
        }
        BigDecimal hundred = new BigDecimal(100);
        BigDecimal payFee = orderObjectToMQ.getPayFee() == null ? new BigDecimal(0) : orderObjectToMQ.getPayFee();
        BigDecimal terPayFee = amount.divide(hundred).multiply(payFee).setScale(2, BigDecimal.ROUND_UP);

        String merId= orderObjectToMQ.getMerId();
        String terminalMerId=orderObjectToMQ.getTerminalMerId();

        //获取终端商户钱包
        TerminalMerchantsWallet  terminalMerchantsWallet = paymentRecordSquareService.getTerminalMerchantsWallet(merId,terminalMerId);
        if (null == terminalMerchantsWallet) {
            terminalMerchantsWallet = new TerminalMerchantsWallet();
            terminalMerchantsWallet.setId(UUID.createKey("terminal_merchants_details", ""));
            terminalMerchantsWallet.setMerId(merId);
            terminalMerchantsWallet.setTerminalMerId(terminalMerId);
        }

        TerminalMerchantsDetails terminalMerchantsDetails = new TerminalMerchantsDetails();
        terminalMerchantsDetails.setTimestamp(String.valueOf(System.currentTimeMillis()));
        terminalMerchantsDetails.setId(UUID.createKey("terminal_merchants_details", ""));
        terminalMerchantsDetails.setMerId(orderObjectToMQ.getMerId());
        terminalMerchantsDetails.setTerminalMerId(orderObjectToMQ.getTerminalMerId());
        terminalMerchantsDetails.setType(orderObjectToMQ.getPayType());
        terminalMerchantsDetails.setMerOrderId(orderObjectToMQ.getMerOrderId());
        terminalMerchantsDetails.setOrderId(orderObjectToMQ.getPayId());
        terminalMerchantsDetails.setAmount(orderObjectToMQ.getAmount());
        if (orderObjectToMQ.getPayType().equals("6")){
            terminalMerchantsDetails.setOutAmount(new BigDecimal(0).subtract(amount.subtract(terPayFee).setScale(2, BigDecimal.ROUND_UP)));
            terminalMerchantsDetails.setInAmount(new BigDecimal(0));
        }else {
            terminalMerchantsDetails.setInAmount(amount.subtract(terPayFee).setScale(2, BigDecimal.ROUND_UP));
            terminalMerchantsDetails.setOutAmount(new BigDecimal(0));
        }
        terminalMerchantsDetails.setFee(terPayFee);

        terminalMerchantsDetails.setTotalBalance(   (null == terminalMerchantsWallet.getTotalBalance()
                ? new BigDecimal(0).add(terminalMerchantsDetails.getInAmount().subtract(terminalMerchantsDetails.getOutAmount()))
                : terminalMerchantsWallet.getTotalBalance().add(terminalMerchantsDetails.getInAmount()).subtract(terminalMerchantsDetails.getOutAmount()) )
        );
        terminalMerchantsDetails.setUpdateTime(new Date());

        return payMap.lput("terminalMerchantsDetails",terminalMerchantsDetails);
    }
    //------------------------------------------------------------------------------------------------------------------

    /**
     *   代付终端商户钱包和明细
     * @param orderObjectToMQ
     * @param payMap
     * @return
     * @throws PayException
     */
    public PayMap<String,Object>  handleTerminalMerchantWalletAadDedails(OrderObjectToMQ orderObjectToMQ, PayMap<java.lang.String, java.lang.Object> payMap)  {
        String merId= orderObjectToMQ.getMerId();
        String terminalMerId=orderObjectToMQ.getTerminalMerId();
        BigDecimal orderAmount = orderObjectToMQ.getAmount();
        if (orderObjectToMQ.getPayType().equals("6")){
            orderAmount = new BigDecimal(0).subtract(orderAmount);
        }
        BigDecimal backFee = orderObjectToMQ.getTerminalFee()==null?new BigDecimal(0):orderObjectToMQ.getTerminalFee();

        //获取子商户钱包
        TerminalMerchantsWallet terminalMerchantsWallet = paymentRecordSquareService.getTerminalMerchantsWallet(merId,terminalMerId);
        if (terminalMerchantsWallet==null){
            terminalMerchantsWallet=new TerminalMerchantsWallet();
            terminalMerchantsWallet.setId(UUID.createKey("terminal_merchants_wallet",""));
            terminalMerchantsWallet.setMerId(orderObjectToMQ.getMerId());
            terminalMerchantsWallet.setTerminalMerId(orderObjectToMQ.getTerminalMerId());
        }

        //出账
        terminalMerchantsWallet.setOutAmount(terminalMerchantsWallet.getOutAmount()==null?orderAmount.subtract(backFee):terminalMerchantsWallet.getOutAmount().add(orderAmount.subtract(backFee)));
        //总余额
        terminalMerchantsWallet.setTotalBalance(terminalMerchantsWallet.getTotalBalance().subtract(orderAmount) );
        //总手续费
        terminalMerchantsWallet.setTotalFee(terminalMerchantsWallet.getTotalFee()==null?backFee:terminalMerchantsWallet.getTotalFee().add(backFee));
        //总可用
        terminalMerchantsWallet.setTotalAvailableAmount(terminalMerchantsWallet.getTotalAvailableAmount().subtract(orderAmount) );

        terminalMerchantsWallet.setUpdateTime(new Date());
        //组装子商户钱包明细
        TerminalMerchantsDetails terminalMerchantsDetails = new TerminalMerchantsDetails();
        terminalMerchantsDetails.setTimestamp(String.valueOf(System.currentTimeMillis()));
        terminalMerchantsDetails.setId(UUID.createKey("terminal_merchants_details",""));
        terminalMerchantsDetails.setMerId(orderObjectToMQ.getMerId());
        terminalMerchantsDetails.setTerminalMerId(orderObjectToMQ.getTerminalMerId());
        terminalMerchantsDetails.setType(orderObjectToMQ.getPayType());
        terminalMerchantsDetails.setMerOrderId(orderObjectToMQ.getMerOrderId());
        terminalMerchantsDetails.setOrderId(orderObjectToMQ.getTransId());
        terminalMerchantsDetails.setAmount(orderObjectToMQ.getAmount());
        if (orderObjectToMQ.getPayType().equals("6")){
            terminalMerchantsDetails.setInAmount(new BigDecimal(0).subtract(orderAmount.subtract(backFee)));
        }else {
            terminalMerchantsDetails.setOutAmount(orderObjectToMQ.getAmount().subtract(backFee));
        }

        terminalMerchantsDetails.setFee(backFee);
        terminalMerchantsDetails.setTotalBalance(terminalMerchantsWallet.getTotalBalance());
        terminalMerchantsDetails.setUpdateTime(new Date());

        return payMap
                .lput("terminalMerchantsDetails",terminalMerchantsDetails)
                .lput("terminalMerchantsWallet",terminalMerchantsWallet);
    }

}
