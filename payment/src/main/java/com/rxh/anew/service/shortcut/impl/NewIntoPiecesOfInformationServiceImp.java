package com.rxh.anew.service.shortcut.impl;

import com.alibaba.fastjson.JSON;
import com.rxh.anew.dto.*;
import com.rxh.anew.inner.InnerPrintLogObject;
import com.rxh.anew.inner.ParamRule;
import com.rxh.anew.service.CommonServiceAbstract;
import com.rxh.anew.service.shortcut.NewIntoPiecesOfInformationService;
import com.rxh.anew.table.business.RegisterCollectTable;
import com.rxh.anew.table.business.RegisterInfoTable;
import com.rxh.anew.table.channel.ChannelExtraInfoTable;
import com.rxh.anew.table.channel.ChannelInfoTable;
import com.rxh.anew.table.merchant.MerchantInfoTable;
import com.rxh.anew.table.system.MerchantSettingTable;
import com.rxh.anew.table.system.ProductSettingTable;
import com.rxh.enums.*;
import com.rxh.exception.NewPayException;
import com.rxh.tuple.Tuple2;
import com.rxh.tuple.Tuple4;
import com.rxh.utils.PayTreeMap;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/18
 * Time: 下午3:49
 * Description:
 */
@Service
public class NewIntoPiecesOfInformationServiceImp extends CommonServiceAbstract implements NewIntoPiecesOfInformationService {


    @Override
    public List<RegisterCollectTable> getRegisterCollectOnSuccess(InnerPrintLogObject ipo) {
        return  commonRPCComponent.apiRegisterCollectService.getList(
                new RegisterCollectTable()
                        .setMerchantId(ipo.getMerId())
                        .setTerminalMerId(ipo.getTerMerId()));
    }

    @Override
    public LinkedList<ChannelInfoTable> filtrationChannelInfoBySuccessRegisterCollect(Tuple2<ProductSettingTable,Set<ChannelInfoTable>> tuple2, List<RegisterCollectTable> registerCollectTableList, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="filtrationChannelInfoBySuccessRegisterCollect";
        LinkedList<ChannelInfoTable>  channelInfoTableSet = new LinkedList<>(tuple2._2);
        tuple2._2.forEach(channel->{
            registerCollectTableList.forEach(regCollect->{
                if(channel.getChannelId().equalsIgnoreCase(regCollect.getChannelId()))
                    channelInfoTableSet.remove(channel);
            });
        });
        isHasNotElement(channelInfoTableSet,//无更多可用通道
                ResponseCodeEnum.RXH00023.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00020.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00023.getMsg()));
//       //保留产品符合的通道
//        final String productId= tuple2._.getProductId();
//        Set<ChannelInfoTable> set = channelInfoTableSet.stream().filter(t->t.getProductId().equalsIgnoreCase(productId)).collect(Collectors.toSet());
//        isHasNotElement(channelInfoTableSet,//无更多可用通道
//                ResponseCodeEnum.RXH00023.getCode(),
//                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置（过滤产品符合的通道）：%s",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00020.getMsg(),localPoint),
//                format(" %s",ResponseCodeEnum.RXH00023.getMsg()));
        return channelInfoTableSet;
    }

    @Override
    public ChannelInfoTable filtrationChannelInfoByLevel(LinkedList<ChannelInfoTable> channelInfoTablesList, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="filtrationChannelInfoByLevel";
        ChannelInfoTable channelInfoTable = channelInfoTablesList
                .stream()
                .reduce((t1,t2)-> t1.getChannelLevel() > t2.getChannelLevel() ? t1 : t2 )
                .orElse(null);
        isNull(channelInfoTable,
                ResponseCodeEnum.RXH99999.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置(取到空通道信息)：%s",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH99999.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH99999.getMsg()));

        return channelInfoTable;
    }

    @Override
    public ChannelExtraInfoTable getAddCusChannelExtraInfo(ChannelInfoTable channelInfoTable, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="getAddCusChannelExtraInfo";
        ChannelExtraInfoTable channelExtraInfoTable =  commonRPCComponent.apiChannelExtraInfoService.getOne(
                new ChannelExtraInfoTable()
                        .setOrganizationId(channelInfoTable.getOrganizationId())
                        .setBussType(BussTypeEnum.ADDCUS.getBussType())
        );
        isNull(channelExtraInfoTable,
                ResponseCodeEnum.RXH00024.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00024.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00024.getMsg()));
        return channelExtraInfoTable;
    }

    @Override
    public Tuple2<RegisterInfoTable,RegisterCollectTable> saveByRegister(MerchantBasicInformationRegistrationDTO mbirDTO, ChannelInfoTable channelInfoTable,InnerPrintLogObject ipo) {
        final String localPoint="saveByRegister";
        RegisterInfoTable registerInfoTable = null;
        RegisterCollectTable registerCollectTable = null;
        try {
            registerInfoTable = commonRPCComponent.apiRegisterInfoService.getOne(
                    new RegisterInfoTable()
                            .setMerchantId(mbirDTO.getMerId())
                            .setTerminalMerId(mbirDTO.getTerminalMerId())
                            .setUserName(mbirDTO.getCardHolderName())
                            .setIdentityType(new Integer(mbirDTO.getIdentityType()))
                            .setIdentityNum(mbirDTO.getIdentityNum())
            );

            if (null == registerInfoTable){
                registerInfoTable = new RegisterInfoTable()
                        .setId(System.currentTimeMillis())
                        .setCreateTime(new Date());
            }
            registerInfoTable.setMerchantId(mbirDTO.getMerId())
                    .setTerminalMerId(mbirDTO.getTerminalMerId())
                    .setTerminalMerName(mbirDTO.getTerminalMerName())
                    .setUserName(mbirDTO.getCardHolderName())
                    .setUserShortName(mbirDTO.getUserShortName())
                    .setIdentityType(new Integer(mbirDTO.getIdentityType()))
                    .setIdentityNum(mbirDTO.getIdentityNum())
                    .setPhone(mbirDTO.getPhone())
                    .setMerchantType(mbirDTO.getMerchantType())
                    .setProvince(mbirDTO.getProvince())
                    .setCity(mbirDTO.getCity())
                    .setAddress(mbirDTO.getAddress())
                    .setUpdateTime(new Date());

            registerCollectTable = new RegisterCollectTable()
                    .setId(System.currentTimeMillis())
                    .setChannelId(channelInfoTable.getChannelId())
                    .setProductId(channelInfoTable.getProductId())
                    .setPlatformOrderId("RXH" + new Random(System.currentTimeMillis()).nextInt(1000000) + "-B1" + System.currentTimeMillis())
                    .setRitId(registerInfoTable.getId())
                    .setOrganizationId(channelInfoTable.getOrganizationId())
                    .setMerchantId(mbirDTO.getMerId())
                    .setTerminalMerId(mbirDTO.getTerminalMerId())
                    .setMerOrderId(mbirDTO.getMerOrderId())
                    .setCategory(mbirDTO.getCategory())
                    .setMiMerCertPic1(mbirDTO.getMiMerCertPic1())
                    .setMiMerCertPic2(mbirDTO.getMiMerCertPic2())
                    .setBankCode(mbirDTO.getBankCode())
                    .setBankCardType(new Integer(mbirDTO.getBankCardType()))
                    .setCardHolderName(mbirDTO.getCardHolderName())
                    .setBankCardNum(mbirDTO.getBankCardNum())
                    .setBankCardPhone(mbirDTO.getBankCardPhone())
                    .setPayFee(new BigDecimal(mbirDTO.getPayFee()))
                    .setBackFee(new BigDecimal(mbirDTO.getBackFee()))
                    .setChannelRespResult(null)
                    .setCrossRespResult(null)
                    .setStatus(StatusEnum._3.getStatus())
                    .setCreateTime(new Date())
                    .setBussType(BusinessTypeEnum.b1.getBusiType()) //基础信息登记
                    .setUpdateTime(new Date());
            //保持或更新
            commonRPCComponent.apiRegisterInfoService.replaceSave(registerInfoTable);
            commonRPCComponent.apiRegisterCollectService.save(registerCollectTable);
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：保存进件信息发生异常,异常信息:%s",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH99999.getMsg(),localPoint,e.getMessage()),
                    format(" %s",ResponseCodeEnum.RXH99999.getMsg())
            );
        }finally {
            return new Tuple2(registerInfoTable,registerCollectTable);
        }

    }

    public RequestCrossMsgDTO getRequestCrossMsgDTO(Tuple2 tuple) {
        Tuple4<ChannelInfoTable,ChannelExtraInfoTable, RegisterInfoTable,RegisterCollectTable> tuple4 = (Tuple4<ChannelInfoTable, ChannelExtraInfoTable,  RegisterInfoTable,RegisterCollectTable>) tuple;
        return new RequestCrossMsgDTO()
                .setChannelInfoTable(tuple4._)
                .setChannelExtraInfoTable(tuple4._2)
                .setRegisterInfoTable(tuple4._3)
                .setRegisterCollectTable(tuple4._4);
    }

    @Override
    public RegisterCollectTable updateByRegisterCollectTable(CrossResponseMsgDTO crossResponseMsgDTO, String crossResponseMsg, RegisterCollectTable registerCollectTable, InnerPrintLogObject ipo) {
        final String localPoint="updateByRegisterCollectTable";
        registerCollectTable
                .setStatus(Integer.valueOf(crossResponseMsgDTO.getCrossStatusCode()))
                .setCrossRespResult(crossResponseMsg)
                .setChannelRespResult(crossResponseMsgDTO.getChannelResponseMsg());
        try {
            commonRPCComponent.apiRegisterCollectService.updateByPrimaryKey(registerCollectTable);
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：更新进件信息发生异常,异常信息：%s",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH99999.getMsg(),localPoint,e.getMessage()),
                    format(" %s",ResponseCodeEnum.RXH99999.getMsg())
            );
        }finally {
            return registerCollectTable;
        }
    }




    @Override
    public List<ChannelInfoTable> getChannelInfoByMerSetting(List<MerchantSettingTable> list, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="getChannelInfoByMerSetting";
        Set<String>  channelIdSet = list.stream().map(MerchantSettingTable::getChannelId).collect(Collectors.toSet());
        List<ChannelInfoTable>   channelInfoTableList = commonRPCComponent.apiChannelInfoService.batchGetByChannelId(channelIdSet);
        isHasNotElement(channelInfoTableList,
                ResponseCodeEnum.RXH00020.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00020.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00020.getMsg()));
        return channelInfoTableList;
    }

    @Override
    public Tuple2<ProductSettingTable,Set<ChannelInfoTable>> filtrationChannelInfoByProductType(List<ChannelInfoTable> list, String productType, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="filtrationChannelInfoByProductType";
        ProductSettingTable productSettingTable = commonRPCComponent.apiProductTypeSettingService.getOne(new ProductSettingTable().setProductName(productType));
        isNull(productSettingTable,//产品类型不存在
                ResponseCodeEnum.RXH00021.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00021.getMsg()),
                format(" %s",ResponseCodeEnum.RXH00021.getMsg()));

        Set<ChannelInfoTable> channelInfoTableSet=list.stream()
                .filter(t->t.getProductId().equalsIgnoreCase(productSettingTable.getProductId()))
                .collect(Collectors.toSet());

        isHasNotElement(channelInfoTableSet,//通道不匹配
                ResponseCodeEnum.RXH00022.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00022.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00022.getMsg()));
        return new Tuple2(productSettingTable,channelInfoTableSet);
    }


    @Override
    public RegisterCollectTable getRegisterCollectTable(String platformOrderId,String busiType, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="getRegisterCollectTable";
        RegisterCollectTable rct = new RegisterCollectTable();
        rct.setMerchantId(ipo.getMerId());
        rct.setTerminalMerId(ipo.getTerMerId());
        rct.setPlatformOrderId(platformOrderId);
        rct.setBussType(busiType);
        rct.setStatus(StatusEnum._0.getStatus());
        rct = commonRPCComponent.apiRegisterCollectService.getOne(rct);
        isNull(rct,
                ResponseCodeEnum.RXH00025.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00025.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00025.getMsg()));
        return rct;
    }

    @Override
    public Tuple2<RegisterInfoTable,RegisterCollectTable> saveOnRegisterInfo(RegisterCollectTable registerCollectTable, MerchantBankCardBindingDTO mbcbDTO, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="saveOnRegisterInfo";
        RegisterInfoTable registerInfoTable = null;
        try{
            registerInfoTable = commonRPCComponent.apiRegisterInfoService.getOne(new RegisterInfoTable().setId(registerCollectTable.getRitId()));
            registerInfoTable
                    .setIdentityType(Integer.valueOf(mbcbDTO.getIdentityType()))
                    .setIdentityNum(mbcbDTO.getIdentityNum())
                    .setProvince(mbcbDTO.getProvince())
                    .setCity(mbcbDTO.getCity())
            ;
            registerCollectTable.setId(null)
                    .setStatus(StatusEnum._3.getStatus())
                    .setPlatformOrderId("RXH" + new Random(System.currentTimeMillis()).nextInt(1000000) + "-B2" + System.currentTimeMillis())
                    .setBankAccountProp(Integer.valueOf(mbcbDTO.getBankAccountProp()))
                    .setBankCode(mbcbDTO.getBankCode())
                    .setBankCardType(Integer.valueOf(mbcbDTO.getBankCardType()))
                    .setCardHolderName(mbcbDTO.getCardHolderName())
                    .setBankCardNum(mbcbDTO.getBankCardNum())
                    .setBankCardPhone(mbcbDTO.getBankCardPhone())
                    .setBussType(BusinessTypeEnum.b2.getBusiType())
                    .setCrossRespResult(null)
                    .setChannelRespResult(null)
            ;
            commonRPCComponent.apiRegisterInfoService.replaceSave(registerInfoTable);
            commonRPCComponent.apiRegisterCollectService.save(registerCollectTable);
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置B2：%s,异常根源：保存绑定银行卡发生异常,异常信息：%s", ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }finally {
            return new Tuple2<>(registerInfoTable,registerCollectTable);
        }
    }



    @Override
    public RegisterCollectTable saveRegisterCollectTableByB3(RegisterCollectTable registerCollectTable, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="saveRegisterCollectTableByB3";
        registerCollectTable.setId(null)
                .setBussType(BusinessTypeEnum.b3.getBusiType())
                .setPlatformOrderId("RXH" + new Random(System.currentTimeMillis()).nextInt(1000000) + "-B3" + System.currentTimeMillis())
                .setCrossRespResult(null)
                .setChannelRespResult(null)
                .setStatus(StatusEnum._3.getStatus());
        try{
            commonRPCComponent.apiRegisterCollectService.save(registerCollectTable);
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置B3：%s,异常根源：保存进件附属信息", ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }
        return registerCollectTable;
    }



    @Override
    public Map<String, ParamRule> getParamMapByIPOI() {
        return new HashMap<String, ParamRule>() {
            {
                put("charset", new ParamRule(ParamTypeEnum.STRING.getType(), 5, 5));//固定UTF-8
                put("signType", new ParamRule(ParamTypeEnum.STRING.getType(), 3, 3));//固定为MD5
                put("productType", new ParamRule(ParamTypeEnum.STRING.getType(), 3, 64));//产品类型
                put("merId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 32));//商户号
                put("merOrderId", new ParamRule(ParamTypeEnum.STRING.getType(), 16, 64));// 商户订单号
                put("merchantType", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 2));//商户类型 商户类型	00公司商户，01个体商户
                put("terminalMerId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 64));//子商户id
                put("terminalMerName", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 32));//终端客户名称
                put("userShortName", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 32));//  商户简称
                put("category", new ParamRule(ParamTypeEnum.STRING.getType(), 3,16));// 经营项目
                put("identityType", new ParamRule(ParamTypeEnum.STRING.getType(), 1, 1));//  证件类型 证件类型	1身份证、2护照、3港澳回乡证、4台胞证、5军官证
                put("identityNum", new ParamRule(ParamTypeEnum.STRING.getType(), 12, 32));//证件号码
                put("phone", new ParamRule(ParamTypeEnum.PHONE.getType(), 11, 11));// 手机号
                put("province", new ParamRule(ParamTypeEnum.STRING.getType(), 3, 16));// 省份
                put("city", new ParamRule(ParamTypeEnum.STRING.getType(), 3, 16));// 城市
                put("address", new ParamRule(ParamTypeEnum.STRING.getType(), 3, 128));// 详细地址
                put("bankCode", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 16));// 银行简称	如：中国农业银行： ABC，中国工商银行： ICBC
                put("bankCardType", new ParamRule(ParamTypeEnum.STRING.getType(), 1, 1));//  卡号类型	1借记卡  2信用卡	否	1
                put("bankCardNum", new ParamRule(ParamTypeEnum.STRING.getType(), 12, 32));//银行卡号
                put("cardHolderName", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 32));//银行卡持卡人
                put("bankCardPhone", new ParamRule(ParamTypeEnum.PHONE.getType(), 11,11));// 银行卡手机号
                put("payFee", new ParamRule(ParamTypeEnum.STRING.getType(), 1,8));//扣款手续费	用户扣款费率，单位： %，如2.8	否	8
                put("backFee", new ParamRule(ParamTypeEnum.STRING.getType(), 1,8));//代付手续费	用户还款费率,单位：元/笔,保留两位小数	否	8
                put("signMsg", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 256));//签名字符串
            }
        };
    }

    public boolean multipleOrder(String merOrderId,InnerPrintLogObject ipo) throws NewPayException{
        final String localPoint="multipleOrder";
        RegisterCollectTable rct = new RegisterCollectTable();
        rct.setMerchantId(ipo.getMerId());
        rct.setTerminalMerId(ipo.getTerMerId());
        rct.setMerOrderId(merOrderId);
        rct = commonRPCComponent.apiRegisterCollectService.getOne(rct);
        isNotNull(rct,
                ResponseCodeEnum.RXH00009.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00009.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00009.getMsg()));
        return false;
    }

    @Override
    public Map<String, ParamRule>  getParamMapByBCB(){

        return new HashMap<String, ParamRule>() {
            {
                put("charset", new ParamRule(ParamTypeEnum.STRING.getType(), 5, 5));//参数字符集编码 固定UTF-8
                put("signType", new ParamRule(ParamTypeEnum.STRING.getType(), 3, 3));//签名类型	固定为MD5
                put("merId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 32));//商户号
                put("merOrderId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 64));// 商户订单号
                put("platformOrderId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 64));// 平台流水号
                put("merchantType", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 2));//商户类型
                put("terminalMerId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 64));//子商户id
                put("bankAccountProp", new ParamRule(ParamTypeEnum.STRING.getType(), 1, 1));//账户属性	0：个人账户，1：对公账户
                put("identityType", new ParamRule(ParamTypeEnum.STRING.getType(), 1, 1));//  证件类型 证件类型	1身份证、2护照、3港澳回乡证、4台胞证、5军官证、	否	1
                put("identityNum", new ParamRule(ParamTypeEnum.STRING.getType(), 12, 32));//证件号码
                put("bankCode", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 16));// 银行简称	如：中国农业银行： ABC，中国工商银行： ICBC	否	16
                put("bankCardPhone", new ParamRule(ParamTypeEnum.PHONE.getType(), 11, 11));//银行卡手机号
                put("bankCardType", new ParamRule(ParamTypeEnum.STRING.getType(), 1, 1));// 卡号类型	1借记卡  2信用卡	否	1
                put("cardHolderName", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 32));//银行卡持卡人
                put("bankCardNum", new ParamRule(ParamTypeEnum.STRING.getType(), 12, 32));//银行卡号
                put("province", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 16));// 省份
                put("city", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 16));// 城市
                put("signMsg", new ParamRule(ParamTypeEnum.STRING.getType(), 16, 256));//签名字符串
            }
        };
    }

    @Override
    public  Map<String, ParamRule>  getParamMapBySF(){
        return new HashMap<String, ParamRule>() {
            {
                put("charset", new ParamRule(ParamTypeEnum.STRING.getType(), 5, 5));//参数字符集编码 固定UTF-8
                put("signType", new ParamRule(ParamTypeEnum.STRING.getType(), 3, 3));//签名类型	固定为MD5
                put("merId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 32));//商户号
                put("merOrderId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 64));// 商户订单号
                put("platformOrderId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 64));// 平台流水号
                put("merchantType", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 2));//商户类型
                put("terminalMerId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 64));//子商户id
                put("signMsg", new ParamRule(ParamTypeEnum.STRING.getType(), 16, 256));//签名字符串
            }
        };
    }
}
