package com.rxh.anew.service.shortcut.impl;

import com.rxh.anew.dto.MerchantPayOrderDTO;
import com.rxh.anew.dto.RequestCrossMsgDTO;
import com.rxh.anew.inner.InnerPrintLogObject;
import com.rxh.anew.inner.ParamRule;
import com.rxh.anew.service.CommonServiceAbstract;
import com.rxh.anew.service.shortcut.NewPayOrderService;
import com.rxh.anew.table.business.MerchantCardTable;
import com.rxh.anew.table.business.PayOrderInfoTable;
import com.rxh.anew.table.business.RegisterCollectTable;
import com.rxh.anew.table.channel.ChannelHistoryTable;
import com.rxh.anew.table.channel.ChannelInfoTable;
import com.rxh.anew.table.merchant.MerchantInfoTable;
import com.rxh.anew.table.merchant.MerchantQuotaRiskTable;
import com.rxh.anew.table.system.MerchantSettingTable;
import com.rxh.anew.table.system.RiskQuotaTable;
import com.rxh.enums.BusinessTypeEnum;
import com.rxh.enums.ParamTypeEnum;
import com.rxh.enums.ResponseCodeEnum;
import com.rxh.enums.StatusEnum;
import com.rxh.exception.NewPayException;
import com.rxh.tuple.Tuple2;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/24
 * Time: 下午6:24
 * Description:
 */
@Service
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
    public MerchantQuotaRiskTable getMerchantQuotaRiskByMerId(String merchantId, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="getMerchantQuotaRiskByMerId";
        MerchantQuotaRiskTable merchantQuotaRiskTable=null;
        try {
            merchantQuotaRiskTable = commonRPCComponent.apiMerchantQuotaRiskService.getOne(new MerchantQuotaRiskTable()
                    .setMerchantId(merchantId))
                    .setStatus(StatusEnum._0.getStatus());
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：查询商户风控表发生异常,异常信息：%s", ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }
        isNull(merchantQuotaRiskTable,
                ResponseCodeEnum.RXH00030.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00030.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00030.getMsg()));
        return merchantQuotaRiskTable;
    }

    @Override
    public List<RegisterCollectTable> getSuccessRegisterInfo(MerchantPayOrderDTO merchantPayOrderDTO, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="getSuccessRegisterInfo";
        List<RegisterCollectTable> registerCollectTableList=null;
        try {
            registerCollectTableList = commonRPCComponent.apiRegisterCollectService.getList(new RegisterCollectTable()
                    .setMerchantId(merchantPayOrderDTO.getMerId())
                    .setTerminalMerId(merchantPayOrderDTO.getTerminalMerId())
                    .setProductId(merchantPayOrderDTO.getProductType())
                    .setBussType(BusinessTypeEnum.b3.getBusiType())
                    .setStatus(StatusEnum._0.getStatus())
            );
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：申请支付时，查询成功进件附属表发生异常,异常信息：%s", ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }
        isHasNotElement(registerCollectTableList,
                ResponseCodeEnum.RXH00030.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00030.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00030.getMsg()));
        return registerCollectTableList;
    }

    @Override
    public List<MerchantCardTable> getSuccessMerchantCardInfo(List<RegisterCollectTable> registerCollectTableList, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="getSuccessMerchantCardInfo";
        List<MerchantCardTable> merchantCardTableList = null;
        Set<String> platformOrderIdSet = registerCollectTableList.stream().map(m->m.getPlatformOrderId()).collect(Collectors.toSet());
        try {
            merchantCardTableList = commonRPCComponent.apiMerchantCardService.getListByPlatformOrderId(platformOrderIdSet,
                    new MerchantCardTable()
                            .setMerchantId(ipo.getMerId())
                            .setTerminalMerId(ipo.getTerMerId())
                            .setBussType(BusinessTypeEnum.b6.getBusiType())
                            .setStatus(StatusEnum._0.getStatus()));
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：申请支付时，查询成功绑卡记录发生异常,异常信息：%s", ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }

        isHasNotElement(merchantCardTableList,
                ResponseCodeEnum.RXH00033.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00033.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00033.getMsg()));
        return merchantCardTableList;
    }

    @Override
    public ChannelHistoryTable getChannelHistoryInfo(MerchantPayOrderDTO merchantPayOrderDTO, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="getChannelHistoryInfo";
        ChannelHistoryTable channelHistoryTable=null;
        Calendar calendar= Calendar.getInstance();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
        try {
            channelHistoryTable = commonRPCComponent.apiChannelHistoryService.getOne(new ChannelHistoryTable()
                    .setMerchantId(merchantPayOrderDTO.getMerId())
                    .setTerminalMerId(merchantPayOrderDTO.getTerminalMerId())
                    .setProductId(merchantPayOrderDTO.getProductType())
                    .setCreateTime(dateFormat.format(calendar.getTime())));
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：申请支付时，查询通道使用历史记录异常,异常信息：%s", ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }
        return channelHistoryTable;
    }

    @Override
    public void checkBondCardByCardNum(List<MerchantCardTable> merchantCardTableList, MerchantPayOrderDTO merchantPayOrderDTO, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="checkBondCardByCardNum";
        MerchantCardTable merchantCardTable= merchantCardTableList.stream().filter(t->
                t.getBackCardNum().equalsIgnoreCase(merchantPayOrderDTO.getBankCardNum())
                        && t.getBackBankCode().equalsIgnoreCase(merchantPayOrderDTO.getBankCode())
                        && t.getBankCardType().equals(merchantPayOrderDTO.getBankCardType())
                        && t.getBankCardPhone().equalsIgnoreCase(merchantPayOrderDTO.getBankCardPhone())
        ).findAny().orElse(null);

        isNull(merchantCardTable,
                ResponseCodeEnum.RXH00034.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00034.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00034.getMsg()));
    }

    @Override
    public Tuple2<RiskQuotaTable, RiskQuotaTable> getRiskQuotaInfoByMer(MerchantInfoTable merInfoTable, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="getRiskQuotaInfoByMer";
        Calendar calendar= Calendar.getInstance();
        SimpleDateFormat dd= new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat mm= new SimpleDateFormat("yyyy-MM");
        final String d = dd.format(calendar.getTime());
        final String m = mm.format(calendar.getTime());
        Set<String>  timeTepy = new HashSet<>();
        timeTepy.add(d);
        timeTepy.add(m);

        List<RiskQuotaTable> riskQuotaTableList;
        try {
            riskQuotaTableList = commonRPCComponent.apiRiskQuotaService.getListByTimeType(timeTepy, new RiskQuotaTable()
                    .setMeridChannelid(merInfoTable.getMerchantId()).setBussType(BusinessTypeEnum.M.getBusiType()));
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：申请支付时，查询商户风控交易量统计数据发生异常,异常信息：%s", ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }

        if(isHasNotElement(riskQuotaTableList))
            return new Tuple2<>(null,null);

        RiskQuotaTable _d = riskQuotaTableList.stream().filter(t->t.getTimeType().equalsIgnoreCase(d)).findAny().orElse(null);
        RiskQuotaTable _m = riskQuotaTableList.stream().filter(t->t.getTimeType().equalsIgnoreCase(m)).findAny().orElse(null);
        return new Tuple2<>(_d,_m);
    }

    @Override
    public void checkSingleAmountRisk(MerchantPayOrderDTO merchantPayOrderDTO, MerchantQuotaRiskTable merchantQuotaRiskTable, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="checkSingleAmountRisk";
        BigDecimal amount = new BigDecimal(merchantPayOrderDTO.getAmount());
        state(amount.compareTo(merchantQuotaRiskTable.getSingleQuotaAmount())>0,
                ResponseCodeEnum.RXH00012.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00012.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00012.getMsg()));

    }

    @Override
    public void executePlatformRisk(MerchantPayOrderDTO merchantPayOrderDTO, MerchantQuotaRiskTable merchantQuotaRiskTable, Tuple2<RiskQuotaTable, RiskQuotaTable> merRiskQuota, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="getRiskQuotaInfo";
        //历史统计交易量
        RiskQuotaTable _dd = merRiskQuota._;
        RiskQuotaTable _mm = merRiskQuota._2;
        //日限额
        BigDecimal dayQuotaAmount = merchantQuotaRiskTable.getDayQuotaAmount();
        dayQuotaAmount = (null == dayQuotaAmount ? new BigDecimal(0) : dayQuotaAmount);
        //月限额
        BigDecimal monthQuotaAmount = merchantQuotaRiskTable.getMonthQuotaAmount();
        monthQuotaAmount = (null ==monthQuotaAmount ? new BigDecimal(0) : monthQuotaAmount);
        if( !isNull(_dd)) {
            //单日交易量风控判断
            BigDecimal d_Amount =  _dd.getAmount();
            d_Amount =(null == d_Amount ? new BigDecimal(0) : d_Amount );
            d_Amount = d_Amount.add( new BigDecimal(merchantPayOrderDTO.getAmount()) );
            state(d_Amount.compareTo(dayQuotaAmount) > 1,
                    ResponseCodeEnum.RXH00036.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;", ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH00036.getMsg(), localPoint),
                    format(" %s", ResponseCodeEnum.RXH00036.getMsg()));
        }
        if(!isNull(_mm)){
            BigDecimal m_Amount = _mm.getAmount();
            m_Amount = ( null == m_Amount ? new BigDecimal(0) : m_Amount );
            m_Amount.add( new BigDecimal(merchantPayOrderDTO.getAmount()) );
            state(m_Amount.compareTo(monthQuotaAmount) > 1,
                    ResponseCodeEnum.RXH00037.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;", ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH00037.getMsg(), localPoint),
                    format(" %s", ResponseCodeEnum.RXH00037.getMsg()));
        }
    }

    @Override
    public ChannelInfoTable getChannelInfoByChannelHistory( ChannelHistoryTable channelHistoryTable, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="getChannelInfoByChannelHistory";
        ChannelInfoTable channelInfoTable = null;
        //有通道记录时，直接获取通道信息
        try {
            channelInfoTable = commonRPCComponent.apiChannelInfoService.getOne(new ChannelInfoTable()
                    .setChannelId(channelHistoryTable.getChannelId())
                    .setProductId(channelHistoryTable.getProductId())
                    .setStatus(StatusEnum._0.getStatus()));
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：申请支付时，根据通道历史记录表获取通道信息发生异常,异常信息：%s", ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }
        isNull(channelInfoTable,
                ResponseCodeEnum.RXH00022.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;错误可能性判断：通道使用记录中的保存通道ID和产品ID有误，还有可能是通道被禁用了",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00022.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00022.getMsg()));
        return channelInfoTable;
    }

    @Override
    public Tuple2<RiskQuotaTable, RiskQuotaTable> getRiskQuotaInfoByChannel(ChannelInfoTable channelInfoTable, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="getRiskQuotaInfoByChannel";
        Calendar calendar= Calendar.getInstance();
        SimpleDateFormat dd= new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat mm= new SimpleDateFormat("yyyy-MM");
        final String d = dd.format(calendar.getTime());
        final String m = mm.format(calendar.getTime());
        Set<String>  timeTepy = new HashSet<>();
        timeTepy.add(d);
        timeTepy.add(m);
        List<RiskQuotaTable> riskQuotaTableList;
        try {
            riskQuotaTableList = commonRPCComponent.apiRiskQuotaService.getListByTimeType(timeTepy, new RiskQuotaTable()
                    .setMeridChannelid(channelInfoTable.getChannelId()).setBussType(BusinessTypeEnum.C.getBusiType()));
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：申请支付时，查询通道风控交易量统计数据发生异常,异常信息：%s", ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }

        if(isHasNotElement(riskQuotaTableList))
            return new Tuple2<>(null,null);

        RiskQuotaTable _d = riskQuotaTableList.stream().filter(t->t.getTimeType().equalsIgnoreCase(d)).findAny().orElse(null);
        RiskQuotaTable _m = riskQuotaTableList.stream().filter(t->t.getTimeType().equalsIgnoreCase(m)).findAny().orElse(null);
        return new Tuple2<>(_d,_m);
    }

    @Override
    public ChannelInfoTable executeChannelRisk(MerchantPayOrderDTO merchantPayOrderDTO, ChannelInfoTable channelInfoTable, Tuple2<RiskQuotaTable, RiskQuotaTable> channelRiskQuota, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="executeChannelRisk";
        //单前交易金额
        BigDecimal amount = new BigDecimal(merchantPayOrderDTO.getAmount());
        //通道单笔最小金额
        BigDecimal ch_mim_amount = channelInfoTable.getSingleMinAmount();
        ch_mim_amount = (null == ch_mim_amount ? new BigDecimal(0) : ch_mim_amount );
        state(amount.compareTo(ch_mim_amount) > -1 ,
                ResponseCodeEnum.RXH00038.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00038.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00038.getMsg()));
        //通道单笔最大金额
        BigDecimal ch_max_amount = channelInfoTable.getSingleMaxAmount();
        ch_max_amount = (null == ch_max_amount ? new BigDecimal(0) : ch_max_amount );
        state(amount.compareTo(ch_max_amount) > 0 ,
                ResponseCodeEnum.RXH00039.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00039.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00039.getMsg()));
        //日交易量
        BigDecimal d_Amount = channelRiskQuota._.getAmount();
        d_Amount = (null == d_Amount ? new BigDecimal(0) : d_Amount );
        d_Amount = d_Amount.add(amount);
        //通道日交易量限额
        BigDecimal ch_d_amount = channelInfoTable.getDayQuotaAmount();
        ch_d_amount = (null == ch_d_amount ? new BigDecimal(0) : ch_d_amount );
        if( d_Amount.compareTo(ch_d_amount) >0 )  return null; //执行通道切换
        //月交易量
        BigDecimal m_Amount = channelRiskQuota._2.getAmount();
        m_Amount = (null == m_Amount ? new BigDecimal(0) : m_Amount );
        m_Amount = m_Amount.add(amount);
        //通道月交易量限额
        BigDecimal ch_m_amount = channelInfoTable.getMonthQuotaAmount();
        ch_m_amount = (null == ch_m_amount ? new BigDecimal(0) : ch_m_amount );
        if( m_Amount.compareTo(ch_m_amount) > 0 ) return null; //执行通道切换
        return channelInfoTable;
    }

    @Override
    public List<ChannelInfoTable> getAllUsableChannel(MerchantPayOrderDTO merchantPayOrderDTO, ChannelHistoryTable channelHistoryTable, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="getRiskQuotaInfoByChannel";
        //获取商户配置通道信息
        List<MerchantSettingTable> merchantSettingTableList;
        try {
            merchantSettingTableList = commonRPCComponent.apiMerchantSettingService.getList(new MerchantSettingTable()
                    .setMerchantId(merchantPayOrderDTO.getMerId())
                    .setProductId(merchantPayOrderDTO.getProductType())
                    .setStatus(StatusEnum._0.getStatus()));

        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：申请支付时，获取商户配置通道信息发生异常,异常信息：%s", ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }
        isHasNotElement(merchantSettingTableList,
                ResponseCodeEnum.RXH00040.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;错误可能性：商户配置中，没有相关产品类型",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00040.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00040.getMsg()));
        //获取通道信息
        Set<String> channelIdSet = merchantSettingTableList.stream().map(t->t.getChannelId()).collect(Collectors.toSet());
        List<ChannelInfoTable> channelInfoTableList ;
        try {
            channelInfoTableList = commonRPCComponent.apiChannelInfoService.batchGetByChannelId(channelIdSet);
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：申请支付时，获取通道信息发生异常,异常信息：%s", ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }

        isHasNotElement(channelInfoTableList,
                ResponseCodeEnum.RXH00040.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;错误可能性：根据商户配置通道ID,获取通道信息为空，可能通道不存在或者被禁用了",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00040.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00040.getMsg()));

        return channelInfoTableList;
    }

    @Override
    public ChannelInfoTable getFeasibleChannel(MerchantPayOrderDTO merchantPayOrderDTO,List<ChannelInfoTable> channelInfoTableList, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="getFeasibleChannel";
        //获取匹配的通道历史交易
        Set<String> channelIdSet = channelInfoTableList.stream().map(t ->t.getChannelId() ).collect(Collectors.toSet());
        List<RiskQuotaTable> riskQuotaTableList;
        try {
           riskQuotaTableList = commonRPCComponent.apiRiskQuotaService.getListByChMerId(channelIdSet, new RiskQuotaTable().setBussType(BusinessTypeEnum.C.getBusiType()));
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：申请支付时，获取通道历史交易量信息发生异常,异常信息：%s", ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }
        LinkedList<ChannelInfoTable>  ll = new LinkedList<>(channelInfoTableList);
        channelInfoTableList.forEach(t->{

        });

        return null;
    }

}

