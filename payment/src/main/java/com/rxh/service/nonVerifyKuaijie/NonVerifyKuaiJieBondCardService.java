package com.rxh.service.nonVerifyKuaijie;

import com.alibaba.fastjson.JSONObject;
import com.rxh.exception.PayException;
import com.rxh.pojo.Result;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.payment.ParamRule;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.pojo.payment.TradeObjectSquare;
import com.rxh.service.AbstractCommonService;
import com.rxh.square.pojo.*;
import com.rxh.utils.CheckMd5Utils;
import com.rxh.utils.PayTreeMap;
import com.rxh.utils.SystemConstant;
import com.rxh.utils.UUID;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 *  描述：绑卡相关服务类
 * @author  panda
 * @date 20190729
 */

@Service
public class NonVerifyKuaiJieBondCardService extends AbstractCommonService implements INonVerifyKuaiJieService {





    /**
     *   获取绑卡信息
     * @param systemOrderTrack
     * @param tradeObjectSquare
     * @return
     * @throws PayException
     */
    public SquareTrade getBondCardParam(SystemOrderTrack systemOrderTrack, TradeObjectSquare tradeObjectSquare) throws Exception {
        String merId=tradeObjectSquare.getMerId();
        String merOrderId=tradeObjectSquare.getMerOrderId();
        String terminalMerId=tradeObjectSquare.getTerminalMerId();

        MerchantInfo merchantInfo = redisCacheCommonCompoment.merchantInfoCache.getOne(merId);
        isNull(merchantInfo,format("【无验证快捷】商户号：%s,订单号：%s,未获取到商户信息",merId,merOrderId));
        //签名验证
        CheckMd5Utils.checkMd5(systemOrderTrack.getTradeInfo(),merchantInfo.getSecretKey());
        //获取商户配置
        MerchantSetting merchantSetting =redisCacheCommonCompoment.merchantSettingCache.getOne(tradeObjectSquare.getMerId());
        isNull(merchantSetting,format("【无验证快捷】商户号：%s,订单号：%s,未获取到商户配置信息",merId,merOrderId));
        //获取商户配置通道
        List<ChannelInfo>  channelInfoList=getMerchantSurpportChannel(merchantSetting,tradeObjectSquare);
        //获取商户绑卡信息
//        List<MerchantCard> merchantCardsList = paymentRecordSquareService.getAllMerchantCard(merId,terminalMerId, SystemConstant.SUCCESS);
        List<MerchantCard> merchantCardsList =  redisCacheCommonCompoment.merchantCardCache.getMore(merId,terminalMerId);

        //获取附属通道
        ExtraChannelInfo extraChannelInfo=new ExtraChannelInfo();
//        extraChannelInfo.setType(SystemConstant.BONDCARD);
//        List<ExtraChannelInfo> extraChannelInfoList=extraChannelInfoService.select(extraChannelInfo);
        List<ExtraChannelInfo> extraChannelInfoList=redisCacheCommonCompoment.extraChannelInfoCache.getAll()
                .stream()
                .filter(t->t.getType().equals(SystemConstant.BONDCARD))
                .distinct()
                .collect(Collectors.toList());
        isNull(extraChannelInfoList,format("【无验证快捷】商户号：%s,订单号：%s,未配置绑卡附属通道信息",merId,merOrderId));
        //筛选二次附属通道，过滤掉已经绑卡过得附属通道
        List<ExtraChannelInfo>  extraChannelInfo_two=new ArrayList<>();
       if(null != merchantCardsList && merchantCardsList.size()>0 ) {
           merchantCardsList.forEach(mcl -> {
               extraChannelInfoList.forEach(ecil -> {
                   if (!(mcl.getExtraChannelId().equals(ecil.getExtraChannelId()))) extraChannelInfo_two.add(ecil);
               });
           });
       }else{
           extraChannelInfo_two.addAll(extraChannelInfoList);
       }
        //筛选二次通道
        List<ChannelInfo>  channelInfoList_two=new ArrayList<>();
        extraChannelInfo_two.forEach(exit->{
            channelInfoList.forEach(cil->{
                if( exit.getOrganizationId().equals(cil.getOrganizationId()) ) channelInfoList_two.add(cil);
            });
        });
        isNotElement(channelInfoList_two,format("【无验证快捷】商户号：%s,订单号：%s,通道信息或附属通道信息不匹配！",merId,merOrderId));
        //1.获取通道策略-选择通道费率最低的通道
        ChannelInfo  channelInfo= lowestReteChannelStrategy(channelInfoList_two);
        isNull(channelInfo,format("【无验证快捷】商户号：%s,订单号：%s,没有找到合适的通道信息",merId,merOrderId));
       //2.获取最终的附属通道信息
        for (ExtraChannelInfo exit:extraChannelInfo_two){
            if(exit.getOrganizationId().equals(channelInfo.getOrganizationId()))
                extraChannelInfo=exit;
        }
        isNull(extraChannelInfo,format("【无验证快捷】商户号：%s,订单号：%s,确认附属通道信息失败",merId,merOrderId));
         //3.保存绑卡信息
        MerchantCard  merchantCards=settingMerchantCard(tradeObjectSquare,extraChannelInfo);
        tradeObjectSquare.setInnerType("C001");
        paymentRecordSquareService.saveMerchantCard(merchantCards);
        return new SquareTrade()
                .lsetExtraChannelInfo(extraChannelInfo)
                .lsetChannelInfo(channelInfo)
                .lsetMerchantInfo(merchantInfo)
                .lsetTradeObjectSquare(tradeObjectSquare)
                .lsetMerchantCard(merchantCards)
                .lsetPayOrder(new PayOrder().lsetPayId(UUID.createId()))
                ;
    }
    /**
     *
     * @param merchantCard
     */
    public void updateBondStatus(MerchantCard merchantCard) {
        recordPaymentSquareService.updateMerchantCard(merchantCard);
    }
    /**
     *
     * @param squareTrade
     * @param bankResult
     * @return
     */
    public String settingBondCardReturnJson(SquareTrade squareTrade, BankResult bankResult)  {
        MerchantInfo merchantInfo =squareTrade.getMerchantInfo();
        PayTreeMap<String,Object> map = new PayTreeMap();
        map.lput("merId",merchantInfo.getMerId())
                .lput("merOrderId",squareTrade.getMerOrderId())
                .lput("status",bankResult.getStatus())
                .lput("msg",bankResult.getBankResult())
                .lput("data",bankResult.getBankData())
                .lput("signMsg",getMd5SignWithKey(map,merchantInfo.getSecretKey()));
        return JSONObject.toJSONString(map);
    }
    /**
     *   封装绑卡信息
     * @return
     */
    private MerchantCard  settingMerchantCard(TradeObjectSquare tradeObjectSquare, ExtraChannelInfo extraChannelInfo){
        MerchantCard  param=new MerchantCard();
        param.setId(com.rxh.utils.UUID.createKey("merchant_card", ""));
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
        param.setCardType(tradeObjectSquare.getBankCardType().toString());
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
        return param;
    }


    /**
     *  绑卡必要的参数
     */
    public   Map<String, ParamRule> alliancePayBondCardValidate= new HashMap<String, ParamRule>() {
        {
            put("charset", new ParamRule(REQUIRED, 10, 1004));//
            put("signType", new ParamRule(REQUIRED, 10, 1004));//
            put("merId", new ParamRule(REQUIRED, 10, 1005));//
            put("merOrderId", new ParamRule(REQUIRED, 32, 1006));//
            put("userName", new ParamRule(REQUIRED, 10, 1006));//
            put("identityType", new ParamRule(REQUIRED, 2, 1014));//
            put("bankCardType", new ParamRule(REQUIRED, 2, 1014));//
            put("identityNum", new ParamRule(REQUIRED, 20, 1014));//
            put("bankCardNum", new ParamRule(REQUIRED, 19, 1013));//
            put("bankCardPhone", new ParamRule(REQUIRED, 19, 1013));//
            put("bankCode", new ParamRule(REQUIRED, 32, 1013));//
            put("validDate", new ParamRule(REQUIRED, 6, 1013));//
            put("securityCode", new ParamRule(REQUIRED, 4, 1013));//
            put("terminalMerId", new ParamRule(REQUIRED, 32, 1011));//
            put("terminalMerName", new ParamRule(REQUIRED, 32, 1013));//
            put("returnUrl", new ParamRule(REQUIRED, 128, 1013));//
            put("noticeUrl", new ParamRule(REQUIRED, 128, 1013));//
            put("signMsg", new ParamRule(REQUIRED, 256, 1013));//
        }
    };

    public Result bondCardNotify(BankResult bankResult) throws Exception {
        Result result = new Result();
        try {
            MerchantCard merchantCard = merchantCardService.selectById(bankResult.getBankOrderId());
            isNull(merchantCard,"没有找到相应绑卡信息");
            if (merchantCard.getStatus() != (int)bankResult.getStatus()){
                merchantCard.setStatus((int)bankResult.getStatus());
                merchantCard.setBackData(bankResult.getParam());
                merchantCardService.update(merchantCard);
                result.setMsg("success");
                result.setCode(Result.SUCCESS);
            }else {
                result.setMsg("success");
                result.setCode(Result.SUCCESS);
            }
            return result;
        }catch (Exception e){
            logger.error("绑卡异步处理失败："+e.getMessage());
            result.setMsg("fail");
            result.setCode(Result.FAIL);
            return result;
        }
    }
}
