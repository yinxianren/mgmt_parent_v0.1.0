package com.rxh.service.kuaijie;

import com.rxh.exception.PayException;
import com.rxh.pojo.Result;
import com.rxh.pojo.cross.BankResult;
import com.rxh.service.AbstractPayService;
import com.rxh.square.pojo.*;
import com.rxh.utils.SystemConstant;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class PaymentSquareNotifyService extends AbstractPayService {



    public Result updateOrderAndDoNotify(BankResult bankResult) throws PayException {
        logger.info("银行异步通知已返回=========");
        TransOrder transOrder = transOrderService.getTransOrderByPrimaryId(bankResult.getOrderId().toString());
        if (transOrder == null) {
            return new Result(Result.FAIL, "结算订单：" + bankResult.getOrderId() + "，不存在！");
        }
//        MerchantInfo merchantInfo = recordSquareService.getMerchantInfoByMerId(transOrder.getMerId());
        MerchantInfo merchantInfo =redisCacheCommonCompoment.merchantInfoCache.getOne(transOrder.getMerId());
        // 获取商户配置
//        MerchantSetting merchantSetting = merchantSquareSettingCache.getMerchantSettingByMerId(merchantInfo.getMerId());
        MerchantSetting merchantSetting =redisCacheCommonCompoment.merchantSettingCache.getOne(merchantInfo.getMerId());
        ChannelInfo channelInfo = recordSquareService.getAndDressChannelInfo(merchantInfo,merchantSetting,transOrder.getPayType());
        SystemOrderTrack systemOrderTrack = recordSquareService.getSystemOrderTrack(transOrder.getTransId());
//        MerchantQuotaRisk merchantQuotaRisk = recordSquareService.getMerchantQuotaRiskByMerId(transOrder.getMerId());
        MerchantQuotaRisk merchantQuotaRisk = redisCacheCommonCompoment.merchantQuotaRiskCache.getOne(transOrder.getMerId());
        if(!Objects.equals(transOrder.getOrderStatus(), Integer.valueOf(bankResult.getStatus()))&&bankResult.getStatus()==0){
            recordSquareService.notifyUpdateWallet(bankResult);
            recordSquareService.updateRiskQuotaData(transOrder,channelInfo,merchantQuotaRisk);
        }
        if (transOrder.getOrderStatus()==0&&bankResult.getStatus()!=0){
            recordSquareService.notifyRevertWallet(bankResult);
        }
//        if (bankResult.getRealAmount()!=null){
//            transOrder.setRealAmount(bankResult.getRealAmount());
//        }
        transOrder.setOrderStatus(Integer.valueOf(bankResult.getStatus()));
        recordSquareService.UpdateTransOrder(transOrder);


        merchantSquareNotifyService.sendMerchantNotify(transOrder,merchantInfo,bankResult,systemOrderTrack);
        logger.info("订单更新完成！");
        return new Result(Result.SUCCESS, "订单更新完成！");
    }

    public void updateOrder(BankResult bankResult) throws PayException {
        TransOrder transOrder = transOrderService.getTransOrderByPrimaryId(bankResult.getOrderId().toString());

        MerchantQuotaRisk merchantQuotaRisk = recordSquareService.getMerchantQuotaRiskByMerId(transOrder.getMerId());
        ChannelInfo channelInfo = recordSquareService.getChannelInfo(transOrder.getChannelId().toString());
        switch (bankResult.getStatus()) {
            case SystemConstant.BANK_STATUS_SUCCESS:
                recordSquareService.updateRiskQuotaData(transOrder,channelInfo,merchantQuotaRisk);
                break;
            case SystemConstant.BANK_STATUS_FAIL:
                break;
            case SystemConstant.BANK_STATUS_UNPAID:
                break;
            case SystemConstant.BANK_STATUS_PENDING_PAYMENT:
                break;
            case SystemConstant.BANK_STATUS_RESEND:
                break;
            case SystemConstant.BANK_STATUS_TIME_OUT:
                break;
            default:
                break;
        }
        // order.setBankInfo(bankResult.getBankResult());
        // order.setBankTime(bankResult.getBankTime());
        // order.setBankParam(bankResult.getParam());
        recordSquareService.UpdateTransOrder(transOrder);
    }
}