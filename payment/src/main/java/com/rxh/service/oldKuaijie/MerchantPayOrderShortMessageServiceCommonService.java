package com.rxh.service.oldKuaijie;


import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.merchant.MerchantPayOrderShortMessage;
import com.rxh.pojo.merchant.MerchantRegisterInfo;
import com.rxh.pojo.payment.ChannelInfoOtherParams;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.service.AbstractCommonService;
import com.rxh.square.pojo.*;
import com.rxh.utils.HttpClientUtils;
import com.rxh.utils.JsonUtils;
import com.rxh.utils.SystemConstant;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartException;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * 商户收单短信
 *
 * @author  zhanguanghuo
 * @date 9019.06.24
 */
@Service
public class MerchantPayOrderShortMessageServiceCommonService extends AbstractCommonService {



    /**
     *   确认商户短信
     * @param systemOrderTrack
     * @param merchantPayOrderShortMessage
     * @return
     *     通道信息
     *     收单信息
     *     进件
     *     绑卡
     *
     */
    public String confirmMerchantPayOrderShortMessage(SystemOrderTrack systemOrderTrack, MerchantPayOrderShortMessage merchantPayOrderShortMessage) throws NullPointerException,MultipartException,Exception {

        String merId=merchantPayOrderShortMessage.getMerId();
        String merOrderId=merchantPayOrderShortMessage.getMerOrderId();
        String terminalMerId=merchantPayOrderShortMessage.getTerminalMerId();

        //1.获取订单信息
        PayOrder payOrder=paymentRecordSquareService.getPayOrderByMerOrderIdAndMerIdAndTerMerId(merOrderId,merId,terminalMerId);
        if(null ==payOrder ) throw new NullPointerException("确认商户收单短信时，获取PayOrder信息为空");
        logger.info("【#########确认商户短信###########】根据-->merId={},merOrderId={},terminalMerId={};查询到的结果为：PayOrder=[{}]",merId,merOrderId,terminalMerId,payOrder.toString());

         // 获取交易订单信息
//        TransOrder transOrder=paymentRecordSquareService.getTransOrderByMerOrderIdAndMerIdAndTerMerId( merOrderId,  merId,  terminalMerId);
//        if(null ==transOrder ) throw new NullPointerException("确认商户收单短信时，获取TransOrder信息为空");
//        logger.info("【#########确认商户短信###########】根据-->merId={},merOrderId={},terminalMerId={};查询到的结果为：TransOrder=[{}]",merId,merOrderId,terminalMerId,transOrder.toString());


        //获取商户信息
//        MerchantInfo merchantInfo=merchantInfoService.selectByMerId(merId);
        MerchantInfo merchantInfo=redisCacheCommonCompoment.merchantInfoCache.getOne(merId);
        if(null == merchantInfo) throw new NullPointerException("确认商户收单短信时，获取MerchantInfo信息为空");
        logger.info("【###########确认商户短信###########】根据-->merId={};查询到的结果为：TransOrder=[{}]",merId,merchantInfo.toString());

        //2.获取通道信息
//        ChannelInfo channelInfo = recordPaymentSquareService.getChannelInfo(payOrder.getChannelId());
        ChannelInfo channelInfo = redisCacheCommonCompoment.channelInfoCache.getOne(payOrder.getChannelId());
        if(null == channelInfo) throw new NullPointerException("确认商户收单短信时，获取ChannelInfo信息为空");

        //根据组织id和绑卡获取附属通道信息
//        ExtraChannelInfo extraChannelInfo = recordPaymentSquareService.getExtraChannelInfoByOrgId(channelInfo.getOrganizationId(), SystemConstant.BONDCARD);
        ExtraChannelInfo extraChannelInfo = redisCacheCommonCompoment.extraChannelInfoCache.getAll()
                .stream()
                .filter(t->t.getOrganizationId().equals(channelInfo.getOrganizationId()) && t.getType().equals(SystemConstant.BONDCARD))
                .distinct()
                .findAny()
                .orElse(null);
        if(null == extraChannelInfo) throw new NullPointerException("确认商户收单短信时，获取 ExtraChannelInfo信息为空");
        //3.根据商户号、终端号、和附属通道id、状态为成功做条件获取绑卡信息
//        MerchantCard merchantCard = recordPaymentSquareService.getMerchantCardByMerIdAndTermIdAndExtraId(merId,terminalMerId,extraChannelInfo.getExtraChannelId());
        MerchantCard merchantCard =redisCacheCommonCompoment.merchantCardCache.getMore(merId,terminalMerId).stream()
                .filter( t-> t.getExtraChannelId().equals(extraChannelInfo.getExtraChannelId()))
                .distinct()
                .findAny()
                .orElse(null);
        if(null == merchantCard) throw new NullPointerException("确认商户收单短信时，获取MerchantCard信息为空");

        //4.获取进件信息
        //根据接口类型获取附属通道信息

//        List<MerchantRegisterInfo> merchantRegisterInfoList = paymentRecordSquareService.getMerchantRegisterInfos(merId,terminalMerId, SystemConstant.SUCCESS);
        List<MerchantRegisterInfo> merchantRegisterInfoList = redisCacheCommonCompoment.merchantRegisterInfoCache.getAll()
                .stream()
                .filter(t->
                        t.getMerId().equals(merId)
                                && t.getTerminalMerId().equals(terminalMerId)
                              )
                .distinct()
                .collect(Collectors.toList());
        if( null == merchantRegisterInfoList || merchantRegisterInfoList.size()==0 ) throw new NullPointerException("确认商户收单短信时，获取 List<MerchantRegisterInfo>信息为空");

        // List<ExtraChannelInfo>  extraChannelInfoList= extraChannelInfoService.getChannelInfosByPayType(Integer.valueOf(merchantPayOrderShortMessage.getBizType()));
        //if( null == extraChannelInfoList || extraChannelInfoList.size()==0 ) throw new NullPointerException("确认商户收单短信时，获取List<ExtraChannelInfo>信息为空");

        //过滤出ExtraChannelInfo
//        ExtraChannelInfo extraChannel = extraChannelInfoList.stream()
//                                                            .filter(eci ->
//                                                                     eci.getType().equals(SystemConstant.ADDCUS) &&
//                                                                         eci.getOrganizationId().equals(extraChannelInfo.getOrganizationId())
//                                                                    )
//                                                            .findFirst().get();
//        if(null == extraChannel) throw new NullPointerException("确认商户收单短信时，获取ExtraChannelInfo信息为空");

        //过滤出MerchantRegisterInfo
        MerchantRegisterInfo merchantRegisterInfo=merchantRegisterInfoList.stream()
                                     .filter(mrf ->
                                              mrf.getExtraChannelId().equals(extraChannelInfo.getExtraChannelId())
                                            )
                                     .findFirst().get();
        if(null == merchantRegisterInfo) throw new NullPointerException("确认商户收单短信时，获取MerchantRegisterInfo信息为空");

        //验证请求URL
        String  payURL=channelInfo.getPayUrl();
        String othersParams=channelInfo.getOthers();
        ChannelInfoOtherParams channelInfoOtherParams = JsonUtils.jsonToPojo(othersParams, ChannelInfoOtherParams.class);
        String paySms=channelInfoOtherParams.getPaySms();
        if(StringUtils.isEmpty(payURL) || StringUtils.isEmpty(paySms)) new NullPointerException("确认商户收单短信时，请求URL不完整，请查看！");

        //封装请求所需要的参数
        SquareTrade trade = new SquareTrade();
//        trade.setTransOrder(transOrder);
        trade.setMerchantInfo(merchantInfo);
        trade.setChannelInfo(channelInfo);
        trade.setPayOrder(payOrder);
        trade.setMerchantRegisterInfo(merchantRegisterInfo);
        trade.setMerchantCard(merchantCard);

        //验证url最后一个字符是否是 “/”
        String tab=payURL.substring(payURL.length()-1);
        if(!tab.equals("/")) payURL=payURL+"/";
        String URL=payURL+paySms;
        //发送请求
        String result=null;
        try{
            result=HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(),URL,JsonUtils.objectToJsonNonNull(trade));
        }catch (Exception e){
           throw  e;
        }
        if(StringUtils.isEmpty(result)) throw new NullPointerException("确认商户收单短信时，获取result信息为空");
        logger.info("【###########确认商户短信###########】请求结果为：result=[{}]",result);
        //取出必要的参数和设置必要的参数
        BankResult bankResult = JsonUtils.jsonToPojo(result, BankResult.class);
        Short status=bankResult.getStatus();
        String param=bankResult.getParam();
        String  saveResult=JsonUtils.objectToJson(bankResult);
        systemOrderTrack.setTradeResult(saveResult);
        systemOrderTrack.setOrderTrackStatus((int)status);
        //返回处理结果
        Map<String, Object> map = new TreeMap<>();
        map.put("merId", merId);
        map.put("merOrderId", merOrderId);
        map.put("status", status);
        map.put("msg", param);
        String signMsg = getMd5Sign(map);
        map.put("signMsg", signMsg);
        String returnJson = JsonUtils.objectToJson(map);
        return  returnJson;
    }




}
