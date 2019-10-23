package com.rxh.anew.service.shortcut.impl;

import com.alibaba.fastjson.JSON;
import com.rxh.anew.dto.MerchantBasicInformationRegistrationDTO;
import com.rxh.anew.dto.RequestCrossMsgDTO;
import com.rxh.anew.inner.InnerPrintLogObject;
import com.rxh.anew.inner.ParamRule;
import com.rxh.anew.service.CommonServiceAbstract;
import com.rxh.anew.service.shortcut.NewIntoPiecesOfInformationService;
import com.rxh.anew.table.business.RegisterCollectTable;
import com.rxh.anew.table.business.RegisterInfoTable;
import com.rxh.anew.table.channel.ChannelExtraInfoTable;
import com.rxh.anew.table.channel.ChannelInfoTable;
import com.rxh.anew.table.system.MerchantSettingTable;
import com.rxh.anew.table.system.ProductSettingTable;
import com.rxh.enums.BussTypeEnum;
import com.rxh.enums.ParamTypeEnum;
import com.rxh.enums.ResponseCodeEnum;
import com.rxh.enums.StatusEnum;
import com.rxh.exception.NewPayException;
import com.rxh.pojo.cross.BankResult;
import com.rxh.tuple.Tuple2;
import com.rxh.tuple.Tuple4;
import org.springframework.stereotype.Service;

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
                    new RegisterInfoTable().setMerchantId(mbirDTO.getMerId())
                            .setTerminalMerId(mbirDTO.getTerminalMerId())
                            .setUserName(mbirDTO.getCardHolderName())
                            .setIdentityType(new Integer(mbirDTO.getIdentityType()))
                            .setIdentityNum(mbirDTO.getIdentityNum())
            );

            if (null == registerInfoTable) registerInfoTable = new RegisterInfoTable();
            if (isNull(registerInfoTable.getCreateTime())) registerInfoTable.setCreateTime(new Date());
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

            //保持或更新
            commonRPCComponent.apiRegisterInfoService.replaceSave(registerInfoTable);
            registerCollectTable = new RegisterCollectTable();
            commonRPCComponent.apiRegisterCollectService.save(registerCollectTable
                    .setChannelId(channelInfoTable.getChannelId())
                    .setProductId(channelInfoTable.getProductId())
                    .setPlatformOrderId("RXH" + new Random(System.currentTimeMillis()).nextInt(1000000) + "-" + System.currentTimeMillis())
                    .setRitId(registerInfoTable.getId())
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
                    .setUpdateTime(new Date())
            );
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：保存进件信息发生异常",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH99999.getMsg(),localPoint),
                    format(" %s",ResponseCodeEnum.RXH99999.getMsg())
            );
        }finally {
            return new Tuple2(registerInfoTable,registerCollectTable);
        }

    }

    @Override
    public RegisterCollectTable updataByRegisterCollectTable(BankResult bankResult, RegisterCollectTable registerCollectTable) {
        return registerCollectTable
                .setStatus(Integer.valueOf(bankResult.getStatus()))
                .setCrossRespResult()
                ;
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
    public Map<String, ParamRule> getParamMapByIPOI() {
        return new HashMap<String, ParamRule>() {
            {
                put("productType", new ParamRule(ParamTypeEnum.STRING.getType(), 3, 32));//产品类型
                put("signType", new ParamRule(ParamTypeEnum.STRING.getType(), 3, 3));//固定为MD5
                put("merId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 64));//商户号
                put("merOrderId", new ParamRule(ParamTypeEnum.STRING.getType(), 32, 64));// 商户订单号
                put("merchantType", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 2));//商户类型
                put("terminalMerId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 64));//子商户id
                put("terminalMerName", new ParamRule(ParamTypeEnum.STRING.getType(), 3, 32));//终端客户名称
                put("userShortName", new ParamRule(ParamTypeEnum.STRING.getType(), 3, 32));//  商户简称
                put("identityType", new ParamRule(ParamTypeEnum.STRING.getType(), 1, 1));//  证件类型
                put("identityNum", new ParamRule(ParamTypeEnum.STRING.getType(), 12, 32));//证件号码
                put("phone", new ParamRule(ParamTypeEnum.PHONE.getType(), 11, 11));// 手机号
                put("province", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 10));// 省份
                put("city", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 10));// 城市
                put("address", new ParamRule(ParamTypeEnum.STRING.getType(), 3, 128));// 详细地址
                put("bankCode", new ParamRule(ParamTypeEnum.STRING.getType(), 5, 32));// 银行名称
                put("bankCardType", new ParamRule(ParamTypeEnum.STRING.getType(), 1, 1));// 卡号类型
                put("bankCardNum", new ParamRule(ParamTypeEnum.STRING.getType(), 12, 24));//银行卡号
                put("cardHolderName", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 12));//银行卡持卡人
                put("bankCardPhone", new ParamRule(ParamTypeEnum.PHONE.getType(), 11,11));// 银行卡手机号
                put("category", new ParamRule(ParamTypeEnum.STRING.getType(), 2,10));// 经营项目
                put("payFee", new ParamRule(ParamTypeEnum.STRING.getType(), 1,5));//
                put("backFee", new ParamRule(ParamTypeEnum.STRING.getType(), 1,5));//
                put("signMsg", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 1024));//签名字符串
            }
        };
    }


    @Override
    public RequestCrossMsgDTO getRequestCrossMsgDTO(Tuple2 tuple) {
        Tuple4<ChannelInfoTable,ChannelExtraInfoTable, RegisterInfoTable,RegisterCollectTable> tuple4 = (Tuple4<ChannelInfoTable, ChannelExtraInfoTable,  RegisterInfoTable,RegisterCollectTable>) tuple;
        return new RequestCrossMsgDTO()
                .setChannelInfoTable(tuple4._)
                .setChannelExtraInfoTable(tuple4._2)
                .setRegisterInfoTable(tuple4._3)
                .setRegisterCollectTable(tuple4._4);
    }

}
