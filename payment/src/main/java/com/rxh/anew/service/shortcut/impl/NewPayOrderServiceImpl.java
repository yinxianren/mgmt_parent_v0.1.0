package com.rxh.anew.service.shortcut.impl;

import com.rxh.anew.dto.RequestCrossMsgDTO;
import com.rxh.anew.inner.InnerPrintLogObject;
import com.rxh.anew.inner.ParamRule;
import com.rxh.anew.service.CommonServiceAbstract;
import com.rxh.anew.service.shortcut.NewPayOrderService;
import com.rxh.anew.table.business.PayOrderInfoTable;
import com.rxh.anew.table.merchant.MerchantQuotaRiskTable;
import com.rxh.enums.ParamTypeEnum;
import com.rxh.enums.ResponseCodeEnum;
import com.rxh.exception.NewPayException;
import com.rxh.tuple.Tuple2;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/24
 * Time: 下午6:24
 * Description:
 */
public class NewPayOrderServiceImpl  extends CommonServiceAbstract  implements NewPayOrderService {

    @Override
    public RequestCrossMsgDTO getRequestCrossMsgDTO(Tuple2 tuple) {








        return null;
    }

    @Override
    public Map<String, ParamRule> getParamMapByB7() {
        return new HashMap<String, ParamRule>() {
            {
                put("charset", new ParamRule(ParamTypeEnum.STRING.getType(), 5, 5));//参数字符集编码 固定UTF-8
                put("signType", new ParamRule(ParamTypeEnum.STRING.getType(), 3, 3));//签名类型	固定为MD5
                put("productType", new ParamRule(ParamTypeEnum.STRING.getType(), 3, 64));//产品类型		否	64
                put("merId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 32));//商户号
                put("productCategory", new ParamRule(ParamTypeEnum.STRING.getType(), 3, 32));// 商品类别	参考附件：商户入住字典表	否	32
                put("merOrderId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 64));// 商户订单号
                put("platformOrderId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 64));// 平台流水号
                put("currency", new ParamRule(ParamTypeEnum.STRING.getType(), 3, 3));// 交易的币种
                put("amount", new ParamRule(ParamTypeEnum.AMOUNT.getType(), 2, 12));// 支付金额
                put("identityType", new ParamRule(ParamTypeEnum.STRING.getType(), 1, 1));//证件类型	1身份证、2护照、3港澳回乡证、4台胞证、5军官证、	否	1
                put("identityNum", new ParamRule(ParamTypeEnum.STRING.getType(), 12, 32));//证件号码		否	32
                put("bankCode", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 16));//银行名称	如：中国农业银行： ABC，中国工商银行： ICBC	否	16
                put("bankCardType", new ParamRule(ParamTypeEnum.STRING.getType(), 1, 1));//卡号类型	1借记卡  2信用卡	否	1
                put("bankCardNum", new ParamRule(ParamTypeEnum.STRING.getType(), 12, 32));//银行卡号		否	32
                put("bankCardPhone", new ParamRule(ParamTypeEnum.PHONE.getType(), 11, 11));//银行卡手机号		否	11
                put("validDate", new ParamRule(ParamTypeEnum.STRING.getType(), 4, 4));// 有效期	信用卡必填，格式：MMYY	选填	4
                put("payFee", new ParamRule(ParamTypeEnum.AMOUNT.getType(), 2, 8));//扣款手续费		否	8
                put("terminalMerId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 64));//子商户id	商户系统中商户的编码，要求唯一	否	64
                put("terminalMerName", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 32));//子商户名称	商户系统中商户的名称	否	32
                put("province", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 16));// 省份
                put("city", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 16));// 城市
                put("cardHolderName", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 32));// 持卡人姓名
                put("returnUrl", new ParamRule(ParamTypeEnum.STRING.getType(), 16, 128));//签名字符串
                put("noticeUrl", new ParamRule(ParamTypeEnum.STRING.getType(), 16, 128));//签名字符串
                put("signMsg", new ParamRule(ParamTypeEnum.STRING.getType(), 16, 256));//签名字符串
                put("deviceId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 64));//交易设备号	交易设备号 (包含但不限于POS终端号或者手机IMEI) 字符串类型，最大长度50位,IMEI：国际移动设备识别码（International Mobile Equipment Identity，IMEI），即通常所说的手机序列号、手机“串号”，用于在移动电话网络中识别每一部独立的手机等移动通信设备，相当于移动电话的身份证。	否	64
                put("deviceType", new ParamRule(ParamTypeEnum.STRING.getType(), 1, 1));//	付款用户设备类型	交易设备类型(1-电脑;2-手机;3-其他) 字符串类型	否	1
                put("macAddress", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 64));//MAC地址	MAC地址 (直译为媒体访问控制地址，也称为局域网地址（LAN Address），以太网地址（Ethernet Address）或物理地址（Physical Address），它是一个用来确认网上设备位置的地址。)字符串类型 最大长度30	否	64
            }
        };
    }

    @Override
    public void multipleOrder(String merOrderId, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="multipleOrder";
        PayOrderInfoTable payOrderInfoTable;
        try {
            payOrderInfoTable = commonRPCComponent.apiPayOrderInfoService.getOne(new PayOrderInfoTable()
                    .setMerOrderId(merOrderId)
                    .setMerchantId(ipo.getMerId())
                    .setTerminalMerId(ipo.getTerMerId())
            );
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：查找订单号是否重复,异常信息：%s", ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }
        isNotNull(payOrderInfoTable,
                ResponseCodeEnum.RXH00009.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00009.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00009.getMsg()));
    }

    @Override
    public MerchantQuotaRiskTable getMerchantQuotaRiskByMerId(String merchantId, InnerPrintLogObject ipo) {

        return null;
    }
}
