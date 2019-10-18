package com.rxh.service.kuaijie;

import com.alibaba.fastjson.JSONObject;
import com.rxh.exception.PayException;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.merchant.MerchantBankCardBinding;
import com.rxh.pojo.merchant.MerchantBasicInformationRegistration;
import com.rxh.pojo.merchant.MerchantRegisterInfo;
import com.rxh.pojo.merchant.MerchantServiceFulfillment;
import com.rxh.pojo.payment.ParamRule;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.pojo.payment.TradeObjectSquare;
import com.rxh.service.AbstractCommonService;
import com.rxh.square.pojo.*;
import com.rxh.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.lang.model.util.ElementScanner6;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 进件相关信息的服务类
 * @author  zhanguanghuo
 * @version 0.0.1
 * @date 20190625
 *
 */
@Service
public class IntoPiecesOfInformationService extends  AbstractCommonService {




    //进件基本信息
    private final static Map<String, ParamRule> addCusParam=new HashMap<String, ParamRule>() {
        {
            put("charset", new ParamRule(REQUIRED, 10, 1004));//请求使用的编码格式，固定UTF-8
            put("signType", new ParamRule(REQUIRED, 10, 1004));//固定为MD5
            put("merId", new ParamRule(REQUIRED, 10, 1005));//商户号
            put("merOrderId", new ParamRule(REQUIRED, 32, 1006));// 商户订单号
            put("merchantType", new ParamRule(REQUIRED, 6, 1024));//商户类型
            put("terminalMerId", new ParamRule(REQUIRED, 32, 1025));//子商户id
            put("terminalMerName", new ParamRule(REQUIRED, 32, 1013));//终端客户名称
            put("userShortName", new ParamRule(REQUIRED, 3, 1015));//  商户简称
            put("identityType", new ParamRule(REQUIRED, 2, 1014));//  证件类型
            put("identityNum", new ParamRule(REQUIRED, 20, 1014));//证件号码
            put("phone", new ParamRule(REQUIRED, 11, 1013));// 手机号
            put("province", new ParamRule(REQUIRED, 10, 1018));// 省份
            put("city", new ParamRule(REQUIRED, 10, 1020));// 城市
            put("address", new ParamRule(REQUIRED, 64, 1021));// 详细地址
            put("bankCode", new ParamRule(REQUIRED, 32, 1027));// 银行名称
            put("bankCardType", new ParamRule(REQUIRED, 2, 1017));// 卡号类型
            put("bankCardNum", new ParamRule(REQUIRED, 19, 1013));//银行卡号
            put("cardHolderName", new ParamRule(REQUIRED, 32, 1029));//银行卡持卡人
            put("bankCardPhone", new ParamRule(REQUIRED, 11,1007));// 银行卡手机号
            put("category", new ParamRule(REQUIRED, 10,1007));// 经营项目
            put("payFee", new ParamRule(REQUIRED, 8,1007));//
            put("backFee", new ParamRule(REQUIRED, 8,1007));//
            put("signMsg", new ParamRule(REQUIRED, 256, 1013));//签名字符串
        }
    };

    //银行办卡必要参数
    private final static Map<String, ParamRule> bankCardBind=new HashMap<String, ParamRule>() {
        {
//            put("bizType", new ParamRule(REQUIRED, 10, 1003));//用于区分不同的业务接口（进件接口ID）
            put("charset", new ParamRule(REQUIRED, 10, 1004));//请求使用的编码格式，固定UTF-8
            put("signType", new ParamRule(REQUIRED, 10, 1004));//固定为MD5
            put("merId", new ParamRule(REQUIRED, 10, 1005));//商户号
            put("merOrderId", new ParamRule(REQUIRED, 32, 1006));// 商户订单号
            put("terminalMerId", new ParamRule(REQUIRED, 32, 1025));//子商户id
            put("bankaccProp", new ParamRule(REQUIRED, 2, 1028));//账户属性
            put("identityType", new ParamRule(REQUIRED, 2, 1014));//  证件类型
            put("identityNum", new ParamRule(REQUIRED, 20, 1014));//证件号码
            put("bankCardType", new ParamRule(REQUIRED, 2, 1017));// 卡号类型
            put("cardHolderName", new ParamRule(REQUIRED, 32, 1029));//持卡人姓名
            put("bankCardNum", new ParamRule(REQUIRED, 19, 1013));//银行卡号
            put("bankCardPhone", new ParamRule(REQUIRED, 11,1007));// 银行卡手机号
            put("bankCode", new ParamRule(REQUIRED, 32, 1018));// 省份
            put("province", new ParamRule(REQUIRED, 20, 1018));// 省份
            put("city", new ParamRule(REQUIRED, 20, 1020));// 城市
            put("signMsg", new ParamRule(REQUIRED, 256, 1013));//签名字符串
        }
    };

    //业务开通接口
    private final static Map<String, ParamRule>  serviceFulfillment=new HashMap<String, ParamRule>() {
        {
//            put("bizType", new ParamRule(REQUIRED, 10, 1003));//用于区分不同的业务接口（进件接口ID）
            put("charset", new ParamRule(REQUIRED, 10, 1004));//请求使用的编码格式，固定UTF-8
            put("signType", new ParamRule(REQUIRED, 10, 1004));//固定为MD5
            put("merId", new ParamRule(REQUIRED, 10, 1005));//商户号
            put("merOrderId", new ParamRule(REQUIRED, 32, 1006));// 商户订单号
            put("terminalMerId", new ParamRule(REQUIRED, 32, 1025));//子商户id
//            put("mimerCertPic1", new ParamRule(REQUIRED,  1005));//
//            put("mimerCertPic2", new ParamRule(REQUIRED,  1006));//
            put("signMsg", new ParamRule(REQUIRED, 256, 1013));//签名字符串

        }
    };

    /**
     *  AddCusInfo 验证必要的参数
     * @param systemOrderTrack
     * @return
     */
    public MerchantBasicInformationRegistration verifyMustParamOnAddCusInfo(SystemOrderTrack systemOrderTrack) throws  PayException {
        MerchantBasicInformationRegistration merchantBasicInformationRegistration = JsonUtils.jsonToPojo(systemOrderTrack.getTradeInfo(), MerchantBasicInformationRegistration.class);
        List<String> tradeInfoKeys = new ArrayList<>();
        Map<String, Object> tradeInfoMap = JsonUtils.jsonToMap(systemOrderTrack.getTradeInfo());
        Set<String> keys = tradeInfoMap.keySet();
        String value = "";
        try{
            for (String key : keys) {
                if (tradeInfoMap.get(key) != null){
                    value = tradeInfoMap.get(key).toString();
                }
                validateValue(addCusParam,key, value);
                tradeInfoKeys.add(key);
            }
            ValidateItem(addCusParam,tradeInfoKeys);  // 校验必要参数
            return merchantBasicInformationRegistration;
        }catch (Exception e){
            throw new PayException(String.format("进件请求报文无法解析！错误信息：%s",e.getMessage()),"RXH00013" );
        }finally {
            systemOrderTrack.setMerId(merchantBasicInformationRegistration.getMerId());
            systemOrderTrack.setMerOrderId(merchantBasicInformationRegistration.getMerOrderId());
        }
    }


    /**
     * BankCardBinding  验证必要的参数
     * @param systemOrderTrack
     * @return
     */
    public MerchantBankCardBinding verifyMustParamOnBankCardBinding(SystemOrderTrack systemOrderTrack) throws  PayException{
        MerchantBankCardBinding merchantBankCardBinding = JsonUtils.jsonToPojo(systemOrderTrack.getTradeInfo(), MerchantBankCardBinding.class);
        List<String> tradeInfoKeys = new ArrayList<>();
        Map<String, Object> tradeInfoMap = JsonUtils.jsonToMap(systemOrderTrack.getTradeInfo());
        Set<String> keys = tradeInfoMap.keySet();
        String value = "";
        try{
            for (String key : keys) {
                if (tradeInfoMap.get(key) != null){
                    value = tradeInfoMap.get(key).toString();
                }
                validateValue(bankCardBind,key, value);
                tradeInfoKeys.add(key);
            }
            ValidateItem(bankCardBind,tradeInfoKeys);  // 校验必要参数
            return merchantBankCardBinding;
        }catch (Exception e){
//            logger.error("进件请求时，bankCardBind参数校验抛出异常:"+systemOrderTrack.getTradeInfo(), e);
            throw new PayException(String.format("进件请求报文无法解析！错误信息：%s",e.getMessage()),"RXH00013");
        }finally {
            systemOrderTrack.setMerId(merchantBankCardBinding.getMerId());
            systemOrderTrack.setMerOrderId(merchantBankCardBinding.getMerOrderId());
//            return merchantBankCardBinding;
        }
    }


    /**
     *  保存进件基础信息
     * @param systemOrderTrack
     * @param merchantBasicInformationRegistration
     * @return
     */

    public String saveIntoPiecesOfInformation(SystemOrderTrack systemOrderTrack,
                                              MerchantBasicInformationRegistration merchantBasicInformationRegistration) throws Exception {
        String merId=systemOrderTrack.getMerId();
        // 查询商户信息
//        MerchantInfo merchantInfo = recordPaymentSquareService.getMerchantInfoByMerId(merId);
        MerchantInfo merchantInfo =redisCacheCommonCompoment.merchantInfoCache.getOne(merId);
        isNull(merchantInfo,format("【保存进件基础信息】商户号：%s,未获取到商户信息",merId),"RXH00013");
        // 获取商户配置
        CheckMd5Utils.checkMd5(systemOrderTrack.getTradeInfo(),merchantInfo.getSecretKey());
        //查看是否重复订单
        boolean isExist=searchMultipleOrderOnMerchantRegisterCollect(merchantBasicInformationRegistration);
        if(isExist) throw  new PayException(format("【保存进件基础信息】商户号：%s,订单号重复",merId),"RXH00009");
        // 获取商户配置
        MerchantSetting merchantSetting = redisCacheCommonCompoment.merchantSettingCache.getOne(merId);
        isNull(merchantSetting,format("【保存进件基础信息】商户号：%s,,未获取到商户配置",merId),"RXH00013");
        //获取通道信息
        TradeObjectSquare tradeObjectSquare=new TradeObjectSquare();
        tradeObjectSquare.setMerId(merId);
        tradeObjectSquare.setMerOrderId(merchantBasicInformationRegistration.getMerOrderId());
        List<ChannelInfo> channelInfos = this.getMerchantSurpportChannel(merchantSetting,tradeObjectSquare);
        isNotElement(channelInfos,format("【保存进件基础信息】商户号：%s,未获取到通道信息",merId),"RXH00013");
        //获取附属通道信息
        List<ExtraChannelInfo> extraChannelInfoList=redisCacheCommonCompoment.extraChannelInfoCache
                .getAll()
                .stream()
                .filter(t->t.getType().equals(SystemConstant.ADDCUS))
                .distinct()
                .collect(Collectors.toList());
        isNotElement(extraChannelInfoList,format("【保存进件基础信息】商户号：%s,未找到附属接口配置相关信息",merId),"RXH00013");
        //获取成功进件的信息
        List<MerchantRegisterCollect>  merchantRegisterCollectList= redisCacheCommonCompoment.merchantRegisterCollectCache
                .getAll()
                .stream()
                .filter(t->
                        t.getStatus()==0
                                && t.getMerId().equals(merId)
                                && t.getTerminalMerId().equals(merchantBasicInformationRegistration.getTerminalMerId())
                                && t.getCardNum().equals(merchantBasicInformationRegistration.getBankCardNum()))
                .distinct()
                .collect(Collectors.toList());


        //过滤已经进件成功的附属通道
        List<ExtraChannelInfo> extraChannelInfoList_1=new ArrayList<>();
        if(isHasElement(merchantRegisterCollectList)) {
            extraChannelInfoList.forEach(wcil -> {
                merchantRegisterCollectList.forEach(mrcl -> {
                    if (!(wcil.getExtraChannelId().equalsIgnoreCase(mrcl.getExtraChannelId()))){
                        if( ! extraChannelInfoList_1.contains(wcil))
                            extraChannelInfoList_1.add(wcil);
                    }
                });
            });
        }else{
            extraChannelInfoList_1.addAll(extraChannelInfoList);
        }
        //过滤附属通道
        List<ExtraChannelInfo> extraChannelInfoList_2=new ArrayList<>();
        extraChannelInfoList_1.forEach(t1->{
            channelInfos.forEach(t2->{
                if(t1.getOrganizationId().equals(t2.getOrganizationId()))
                    if( !extraChannelInfoList_2.contains(t1))
                        extraChannelInfoList_2.add(t1);
            });
        });
        extraChannelInfoList.clear();
        extraChannelInfoList=extraChannelInfoList_2;
        isNotElement(extraChannelInfoList,format("【保存进件基础信息】商户号：%s,附属接口配置表中没有找到合适进件通道",merId),"RXH00013");
        //查看目标商户历史进件情况，以及筛选未进件的通道
        extraChannelInfoList=screeningOfChannel(extraChannelInfoList,merchantBasicInformationRegistration);
        isNotElement(extraChannelInfoList,format("【保存进件基础信息】商户号：%s,没有可用通道",merId),"RXH00013");

        //执行进件通道筛选策略
        ExtraChannelInfo extraChannelInfo=getChannelInfoStrategy(extraChannelInfoList);
        isNull(extraChannelInfo,format("【保存进件基础信息】商户号：%s,没有可用通道",merId),"RXH00013");

        //根据附属通道，获取通道
        ChannelInfo channelInfo=null;
        for( ChannelInfo obj:channelInfos){
            if(obj.getOrganizationId().equals(extraChannelInfo.getOrganizationId())){
                channelInfo=obj;
            }
        }
        //发起进件请求
        String result=sendScreeningOfChannelRequest(channelInfo,merchantBasicInformationRegistration,extraChannelInfo);
        logger.info("【保存进件基础信息】保存进件基础信息,cross返回结果：{}"+result);
        //根据结果修改数据进件状态
        BankResult bankResult = JsonUtils.jsonToPojo(result, BankResult.class);
        isNull(bankResult,format("【保存进件基础信息】商户号：%s,请求交互失败",merId),"RXH00013");
        //查看 商务号+终端号   是否重复
        MerchantRegisterInfo mri= redisCacheCommonCompoment.merchantRegisterInfoCache
                .getAll()
                .stream()
                .filter(t->
                        t.getTerminalMerId().equals(merchantBasicInformationRegistration.getTerminalMerId())
                                && t.getMerId().equals(merId))
                .distinct()
                .findAny()
                .orElse(null);

        //封装主要记录商户进件信息
        MerchantRegisterInfo merchantRegisterInfo= new MerchantRegisterInfo();
        merchantRegisterInfo=packagingMerchantRegisterInfo(merchantRegisterInfo,merchantBasicInformationRegistration);
        //保存主要记录商户进件信息
        if( null == mri )  merchantRegisterInfoService.insert(merchantRegisterInfo);
            //更新主要记录商户进件信息
        else merchantRegisterInfoService.updateByMerId(merchantRegisterInfo);
        Short status=bankResult.getStatus();
        String param=bankResult.getParam();
        String  saveResult=JsonUtils.objectToJson(bankResult);
        //封装商户进件附属信息表
        MerchantRegisterCollect merchantRegisterCollect=new MerchantRegisterCollect();
        merchantRegisterCollect=pakagingMerchantRegisterCollectOnSaveIntoPiecesOfInformation(merchantRegisterCollect,
                merchantBasicInformationRegistration,
                status,param,saveResult,extraChannelInfo);
        //保存商户进件附属信息表
        merchantRegisterCollectService.insert(merchantRegisterCollect);
        systemOrderTrack.setTradeResult(saveResult.length()>=500 ? saveResult.substring(0,499) : saveResult);
        systemOrderTrack.setOrderTrackStatus((int)status);

        PayTreeMap<String,Object> map= new PayTreeMap<>();
        map.lput("merId",merId)
                .lput("merOrderId",merchantBasicInformationRegistration.getMerOrderId())
                .lput("status", status)
                .lput("msg", bankResult.getBankResult());
        if (bankResult.getStatus() == SystemConstant.BANK_STATUS_SUCCESS){
            map.lput("resultCode","RXH00000");
        }
        else{
            map.lput("resultCode","RXH00013");
        }

        map.lput("signMsg",getMd5SignWithKey(map,merchantInfo.getSecretKey()));
        logger.info("【保存进件基础信息】基础信息登记返回商户："+JsonUtils.objectToJson(map));
        return JsonUtils.objectToJson(map);
    }



    private MerchantRegisterCollect pakagingMerchantRegisterCollectOnSaveIntoPiecesOfInformation(MerchantRegisterCollect merchantRegisterCollect,
                                                                                                 MerchantBasicInformationRegistration merchantBasicInformationRegistration,
                                                                                                 Short status,String param ,String  saveResult,ExtraChannelInfo extraChannelInfo){
        String bankCardType=merchantBasicInformationRegistration.getBankCardType();
        String bankCardPhome=merchantBasicInformationRegistration.getBankCardPhone();
        String bankCardNum=merchantBasicInformationRegistration.getBankCardNum();
        String bankCode= merchantBasicInformationRegistration.getBankCode();
        String merOrderId=merchantBasicInformationRegistration.getMerOrderId();
        String terminalMerId=merchantBasicInformationRegistration.getTerminalMerId();
        String terminalMerName=merchantBasicInformationRegistration.getTerminalMerName();
        String key=com.rxh.utils.UUID.createKey("merchant_register_collect","MRC");
        String merId=merchantBasicInformationRegistration.getMerId();

        merchantRegisterCollect.setMerId(merId);
        merchantRegisterCollect.setExtraChannelId(extraChannelInfo.getExtraChannelId());
        merchantRegisterCollect.setId(key);
        merchantRegisterCollect.setTime(new Date());
        merchantRegisterCollect.setMerOrderId(merOrderId);
        merchantRegisterCollect.setTerminalMerId(terminalMerId);
        merchantRegisterCollect.setTerminalMerName(terminalMerName);
        merchantRegisterCollect.setCardType(bankCardType);
        merchantRegisterCollect.setBankCardPhone(bankCardPhome);
        merchantRegisterCollect.setCardNum(bankCardNum);
        merchantRegisterCollect.setBankCode(bankCode);
//        merchantRegisterCollect.setStatus((int)status);
        merchantRegisterCollect.setStatus(SystemConstant.CUS_PROCESSD);
        merchantRegisterCollect.setResult(saveResult);
        merchantRegisterCollect.setBackData(param);
        merchantRegisterCollect.setTradeFee(new BigDecimal(merchantBasicInformationRegistration.getPayFee()));
        merchantRegisterCollect.setBackFee(new BigDecimal(merchantBasicInformationRegistration.getBackFee()));
        return merchantRegisterCollect;
    }

    /**
     *   封装msg
     * @param map
     * @param msg
     * @return
     */
    private  Map<String, Object>  setResult(Map<String, Object> map,String msg){
        map.put("msg", msg);
        map.put("signMsg",getMd5Sign(map));
        return map;
    }

    /**
     *   查询订单是否存在
     * @param merchantBasicInformationRegistration
     * @return
     */
    private boolean  searchMultipleOrderOnMerchantRegisterCollect(MerchantBasicInformationRegistration merchantBasicInformationRegistration){
        MerchantRegisterCollect merchantRegisterCollect=new MerchantRegisterCollect();
        merchantRegisterCollect.setMerOrderId(merchantBasicInformationRegistration.getMerOrderId());
        List<MerchantRegisterCollect> merchantRegisterCollectList= merchantRegisterCollectService.selectByExample(merchantRegisterCollect);
        if(merchantRegisterCollectList.size()>0){
            return true;
        }
        return false;
    }


    /**
     *  进件发起
     * @param channelInfo
     * @return
     *  SquareTrade
     */
    private String  sendScreeningOfChannelRequest(ChannelInfo channelInfo,
                                                  MerchantBasicInformationRegistration merchantBasicInformationRegistration,
                                                  ExtraChannelInfo extraChannelInfo){
        SquareTrade squareTrade=new SquareTrade();
        squareTrade.setMerchantBasicInformationRegistration(merchantBasicInformationRegistration);
        squareTrade.setChannelInfo(channelInfo);
        squareTrade.setExtraChannelInfo(extraChannelInfo);
        BankCode bankCode = new BankCode();
        if (!StringUtils.isBlank(merchantBasicInformationRegistration.getBankCode())){
            BankCode bankInfo = bankCodeService.getBankInfo(merchantBasicInformationRegistration.getBankCode());
            if (bankInfo != null){
                bankCode = bankInfo;
            }
        }

        TradeObjectSquare tradeObjectSquare=new TradeObjectSquare();
        tradeObjectSquare.setInnerType( SystemConstant.INFORMATION_REGISTRATION);
        squareTrade.setTradeObjectSquare(tradeObjectSquare);
        squareTrade.setBankInfo(bankCode);
        return  HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(),
                extraChannelInfo.getUrl(),
                JsonUtils.objectToJsonNonNull(squareTrade));
    }


    /**
     * 筛选出可用的通道
     * @param extraChannelInfo
     * @param  merchantBasicInformationRegistration
     * @return
     */
    private  List<ExtraChannelInfo>  screeningOfChannel(List<ExtraChannelInfo> extraChannelInfo,
                                                        MerchantBasicInformationRegistration merchantBasicInformationRegistration){

        MerchantRegisterCollect merchantRegisterCollect=new MerchantRegisterCollect();
        merchantRegisterCollect.setTerminalMerId(merchantBasicInformationRegistration.getTerminalMerId());
        merchantRegisterCollect.setStatus(SystemConstant.CUS_USABLE);
        merchantRegisterCollect.setMerId(merchantBasicInformationRegistration.getMerId());
        List<MerchantRegisterCollect> merchantRegisterCollectList= merchantRegisterCollectService.selectByMeridAndterminalMerIdAndStatus(merchantRegisterCollect);
        //如果没有进件信息，返回所有可以进件的通道信息
        if(merchantRegisterCollectList.size() == 0){
            return extraChannelInfo;
        }
        //筛选出可用进件的通道
        for(int j=0;j<merchantRegisterCollectList.size();j++){
            for(int i=0;i<extraChannelInfo.size();i++){
                if(extraChannelInfo.get(i).getExtraChannelId().equals(merchantRegisterCollectList.get(j).getExtraChannelId())){
                    extraChannelInfo.remove(i);
                }
            }
        }
        return extraChannelInfo;
    }
    /**
     *
     *  进件通道选择策略，目前采取随机策略
     * @param extraChannelInfo
     * @return
     */
    private ExtraChannelInfo getChannelInfoStrategy(  List<ExtraChannelInfo>  extraChannelInfo){
        Integer size=extraChannelInfo.size();
        Random rand = new Random();
        size=rand.nextInt(size);
        return extraChannelInfo.get(size);
    }
    /**
     *   封装进件基础信息
     * @param merchantRegisterInfo
     * @param merchantBasicInformationRegistration
     * @return
     */
    private MerchantRegisterInfo packagingMerchantRegisterInfo(MerchantRegisterInfo merchantRegisterInfo,MerchantBasicInformationRegistration merchantBasicInformationRegistration){

        String address=merchantBasicInformationRegistration.getAddress();
        String city=merchantBasicInformationRegistration.getCity();
        String provicnce=merchantBasicInformationRegistration.getProvince();
        String phone=merchantBasicInformationRegistration.getPhone();
        String identityNum=merchantBasicInformationRegistration.getIdentityNum();
        Integer identityType=merchantBasicInformationRegistration.getIdentityType();
        String merchantType=merchantBasicInformationRegistration.getMerchantType();
        String merId=merchantBasicInformationRegistration.getMerId();
        String userShortName=merchantBasicInformationRegistration.getUserShortName();
        String terminalMerName=merchantBasicInformationRegistration.getTerminalMerName();
        String terminalMerId=merchantBasicInformationRegistration.getTerminalMerId();
        String bankCardName=merchantBasicInformationRegistration.getCardHolderName();

        merchantRegisterInfo.setMerId(merId);
        merchantRegisterInfo.setTerminalMerId(terminalMerId);

        merchantRegisterInfo.setCity(city);
        String key=com.rxh.utils.UUID.createKey("merchant_register_info","MRI");
        merchantRegisterInfo.setId(key);
        merchantRegisterInfo.setIdentityNum(identityNum);
        merchantRegisterInfo.setIdentityType(identityType);
        merchantRegisterInfo.setMerchantAddress(address);
        merchantRegisterInfo.setMerchantType(merchantType);
        merchantRegisterInfo.setPhone(phone);
        merchantRegisterInfo.setProvince(provicnce);
        merchantRegisterInfo.setTerminalMerName(terminalMerName);
        merchantRegisterInfo.setTime(new Date());
        merchantRegisterInfo.setUserName(bankCardName);
        merchantRegisterInfo.setUserShortName(userShortName);
        return merchantRegisterInfo;
    }

    /**
     *  封装商户进件附属信息表
     * @param merchantRegisterCollect
     * @return

     */
    private MerchantRegisterCollect packagingMerchantRegisterCollect(MerchantRegisterCollect merchantRegisterCollect,
                                                                     MerchantBankCardBinding merchantBankCardBinding){

        String  bankCardNum=merchantBankCardBinding.getBankCardNum();
        String  bankcardType=merchantBankCardBinding.getBankcardType();
        String  merOrderId=merchantBankCardBinding.getMerOrderId();
        String  merId= merchantBankCardBinding.getMerId();
        String bankCardPhone=merchantBankCardBinding.getBankCardPhone();

        String bankaccProp=merchantBankCardBinding.getBankaccProp();

        merchantRegisterCollect.setMerId(merId);
        merchantRegisterCollect.setMerOrderId(merOrderId);
        if(! org.springframework.util.StringUtils.isEmpty(bankcardType)){
            merchantRegisterCollect.setCardType(bankcardType);//卡号类型	bankcard_type	int(2)	1借记卡  2信用卡
        }
        if(! org.springframework.util.StringUtils.isEmpty(bankCardNum)){
            merchantRegisterCollect.setSettleCardNum(bankCardNum);//  银行卡号	bankCardNum	String(19)
        }
        if(! org.springframework.util.StringUtils.isEmpty(bankCardNum)){
            merchantRegisterCollect.setCardNum(bankCardNum);//  银行卡号	bankCardNum	String(19)
        }
        if(! org.springframework.util.StringUtils.isEmpty(bankCardPhone)){
            merchantRegisterCollect.setBankCardPhone(bankCardPhone);// 手机号	bankCardPhone	String(11)
        }
        if(! org.springframework.util.StringUtils.isEmpty(bankaccProp)){
            merchantRegisterCollect.setCardProp(bankaccProp);
        }
        return merchantRegisterCollect;
    }



    /**
     *  保存银行卡绑卡信息
     * @param systemOrderTrack
     * @param merchantBankCardBinding
     * @return
     */
    public String saveBankCardBinding(SystemOrderTrack systemOrderTrack, MerchantBankCardBinding merchantBankCardBinding) throws PayException {
        //获取必要的参数
        String merId=merchantBankCardBinding.getMerId();
        String merOrderId=merchantBankCardBinding.getMerOrderId();
        // 查询商户信息
        MerchantInfo merchantInfo = redisCacheCommonCompoment.merchantInfoCache.getOne(merId);
        isNull(merchantInfo,format("【保存银行卡绑卡信息】商户号：%s,未获得该商户信息",merId),"RXH00013");
        // 获取商户配置
        CheckMd5Utils.checkMd5(systemOrderTrack.getTradeInfo(),merchantInfo.getSecretKey());
        MerchantRegisterCollect merchantRegisterCollect=new MerchantRegisterCollect();
        merchantRegisterCollect.setTerminalMerId(merchantBankCardBinding.getTerminalMerId());
        merchantRegisterCollect.setCardNum(merchantBankCardBinding.getBankCardNum());
        merchantRegisterCollect.setMerId(merId);
        merchantRegisterCollect.setStatus(0);
        List<MerchantRegisterCollect> merchantRegisterCollectList1 = merchantRegisterCollectService.selectByWhereCondition(merchantRegisterCollect);
        if (!CollectionUtils.isEmpty(merchantRegisterCollectList1)){
            BankResult bankResult = new BankResult();
            bankResult.setBankResult("该卡已成功进件，无需重复进件");
            bankResult.setStatus(SystemConstant.BANK_STATUS_SUCCESS);
            SquareTrade squareTrade = new SquareTrade();
            squareTrade.setMerOrderId(merOrderId);
            return this.resultToString(bankResult,merchantInfo,squareTrade,1);
        }
        //根据订单号获取商户进件附属信息表
        merchantRegisterCollect=new MerchantRegisterCollect();
        merchantRegisterCollect.setMerOrderId(merOrderId);
        merchantRegisterCollect.setMerId(merId);

        List<MerchantRegisterCollect> merchantRegisterCollectList= merchantRegisterCollectService.selectByMerOrderIdAndMerId(merchantRegisterCollect);
        if(null == merchantRegisterCollectList || merchantRegisterCollectList.size()!=1)
            throw new PayException(format("【银行卡登记接口】商户号：%s,订单号异常",merId),"RXH00013");

        //MerOrderId 具有唯一性
        merchantRegisterCollect = merchantRegisterCollectList.get(0);
        if (null == merchantRegisterCollect || org.springframework.util.StringUtils.isEmpty(merchantRegisterCollect.getExtraChannelId()))
            throw new PayException(format("【保存银行卡绑卡信息】商户号：%s,获取商户进件附属信息表",merId),"RXH00013");
        //获取附属通道id
        ExtraChannelInfo extraChannelInfo =redisCacheCommonCompoment.extraChannelInfoCache.getOne(merchantRegisterCollect.getExtraChannelId());
        isNull(extraChannelInfo,format("【保存银行卡绑卡信息】商户号：%s,未获取到商户进件附属信息表",merId),"RXH00013");
        merchantRegisterCollect = packagingMerchantRegisterCollect(merchantRegisterCollect, merchantBankCardBinding);
        // 更新商户进件附属信息表
        merchantRegisterCollectService.updateByMerOrderIdAndMerId(merchantRegisterCollect);
        //封装更新的字段
        final String TermianlMerId=merchantRegisterCollect.getTerminalMerId();
        MerchantRegisterInfo merchantRegisterInfo =redisCacheCommonCompoment.merchantRegisterInfoCache.getAll()
                .stream()
                .filter(t-> t.getMerId().equals(merId) && t.getTerminalMerId().equals(TermianlMerId))
                .distinct()
                .findAny()
                .orElse(null);
        merchantRegisterInfo = packagingMerchantRegisterInfoOnSaveBankCardBinding(merchantRegisterInfo, merchantBankCardBinding, merchantRegisterCollect);
        //更新
        merchantRegisterInfoService.updateByMerId(merchantRegisterInfo);

        //封装发送请求参数
        SquareTrade squareTrade = new SquareTrade();
        squareTrade.setMerchantBankCardBinding(merchantBankCardBinding);
        squareTrade.setExtraChannelInfo(extraChannelInfo);
        TradeObjectSquare tradeObjectSquare = new TradeObjectSquare();
        tradeObjectSquare.setInnerType(SystemConstant.BANKCARD_REGISTRATION);
        squareTrade.setTradeObjectSquare(tradeObjectSquare);
        squareTrade.setMerOrderId(merOrderId);
        squareTrade.setMerchantRegisterCollect(merchantRegisterCollect);
        squareTrade.setMerchantRegisterInfo(merchantRegisterInfo);
        BankCode bankCode = new BankCode();
        if (!StringUtils.isBlank(merchantRegisterCollect.getBankCode())){
            BankCode bankInfo = bankCodeService.getBankInfo(merchantRegisterCollect.getBankCode());
            if (bankInfo != null){
                bankCode = bankInfo;
            }
        }
        squareTrade.setBankInfo(bankCode);
        //发送请求
        String result = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(),
                extraChannelInfo.getUrl(),
                JsonUtils.objectToJsonNonNull(squareTrade));
        logger.info("【保存银行卡绑卡信息】商户号：{}, cross返回结果：{}",merId,result);
        //根据结果修改数据进件状态
        BankResult bankResult = JsonUtils.jsonToPojo(result, BankResult.class);
        isNull(bankResult,format("【保存银行卡绑卡信息】商户号：%s,交互保存银行卡绑卡信息失败",merId),"RXH00013");

        Short status = bankResult.getStatus();
        String param = bankResult.getParam();
        String saveResult = JsonUtils.objectToJson(bankResult);

        //更新结果
        merchantRegisterCollect=new MerchantRegisterCollect();
        merchantRegisterCollect.setMerOrderId(merOrderId);
        merchantRegisterCollect.setMerId(merId);
        merchantRegisterCollect.setResult(saveResult);
        merchantRegisterCollect.setBackData(param);
        merchantRegisterCollect.setStatus(SystemConstant.CUS_PROCESSD);//处理中
        merchantRegisterCollectService.updateByMerOrderIdAndMerId(merchantRegisterCollect);
        systemOrderTrack.setTradeResult(saveResult.length() >= 500 ? saveResult.substring(0, 499) : saveResult);
        systemOrderTrack.setOrderTrackStatus((int) status);
        return this.resultToString(bankResult,merchantInfo,squareTrade,1);

    }

    /**
     *  业务开通接口
     * @param systemOrderTrack
     * @param merchantServiceFulfillment
     * @return
     */
    public String saveServiceFulfillment(SystemOrderTrack systemOrderTrack, MerchantServiceFulfillment merchantServiceFulfillment) throws PayException {
        //获取必要的参数
        String merId=merchantServiceFulfillment.getMerId();
        String merOrderId=merchantServiceFulfillment.getMerOrderId();

        // 查询商户信息
        MerchantInfo merchantInfo =redisCacheCommonCompoment.merchantInfoCache.getOne(merId);
        isNull(merchantInfo,format("【业务开通接口】商户号：%s,未获得该商户信息",merId),"RXH00013");
        // 获取商户配置
        CheckMd5Utils.checkMd5(systemOrderTrack.getTradeInfo(),merchantInfo.getSecretKey());

//        //根据订单号获取商户进件附属信息表
//        List<MerchantRegisterCollect> merchantRegisterCollectList= redisCacheCommonCompoment.merchantRegisterCollectCache.getAll()
//                .stream()
//                .filter(t-> t.getMerOrderId().equals(merOrderId)  &&  t.getMerId().equals(merId))
//                .distinct()
//                .collect(Collectors.toList());
//        if(null == merchantRegisterCollectList || merchantRegisterCollectList.size()!=1)
//            throw new PayException(format("【业务开通接口】商户号：%s,订单号异常",merId));

        //根据订单号获取商户进件附属信息表
        MerchantRegisterCollect merchantRegisterCollect=new MerchantRegisterCollect();
        merchantRegisterCollect.setMerOrderId(merOrderId);
        merchantRegisterCollect.setMerId(merId);

        List<MerchantRegisterCollect> merchantRegisterCollectList= merchantRegisterCollectService.selectByMerOrderIdAndMerId(merchantRegisterCollect);
        if(null == merchantRegisterCollectList || merchantRegisterCollectList.size()!=1)
            throw new PayException(format("【业务开通接口】商户号：%s,订单号异常",merId),"RXH00013");

        //MerOrderId 具有唯一性
        merchantRegisterCollect=merchantRegisterCollectList.get(0);
        if(null == merchantRegisterCollect || org.springframework.util.StringUtils.isEmpty(merchantRegisterCollect.getExtraChannelId()))
            throw new PayException(format("【业务开通接口】商户号：%s,获取商户进件附属信息表",merId),"RXH00013");

        //获取主要记录商户进件信息
        final String terminalMerId=merchantRegisterCollect.getTerminalMerId();
        MerchantRegisterInfo mri=redisCacheCommonCompoment.merchantRegisterInfoCache.getAll()
                .stream()
                .filter(t-> t.getMerId().equals(merId)  &&  t.getTerminalMerId().equals(terminalMerId))
                .distinct()
                .findAny()
                .orElse(null);
        isNull(mri,format("【业务开通接口】商户号：%s,没有找到主要记录商户进件信息",merId),"RXH00013");


        MerchantRegisterCollect merchantRegisterCollect1=new MerchantRegisterCollect();
        merchantRegisterCollect1.setTerminalMerId(merchantServiceFulfillment.getTerminalMerId());
        merchantRegisterCollect1.setCardNum(mri.getCardNum());
        merchantRegisterCollect1.setMerId(merId);
        merchantRegisterCollect1.setStatus(0);
        List<MerchantRegisterCollect> merchantRegisterCollectList1 = merchantRegisterCollectService.selectByWhereCondition(merchantRegisterCollect1);
        if (!CollectionUtils.isEmpty(merchantRegisterCollectList1)){
            BankResult bankResult = new BankResult();
            bankResult.setBankResult("该卡已成功业务开通，无需重复业务开通");
            bankResult.setStatus(SystemConstant.BANK_STATUS_SUCCESS);
            SquareTrade squareTrade = new SquareTrade();
            squareTrade.setMerOrderId(merOrderId);
            return this.resultToString(bankResult,merchantInfo,squareTrade,1);
        }
        //获取附属通道id
        ExtraChannelInfo extraChannelInfo=redisCacheCommonCompoment.extraChannelInfoCache.getOne(merchantRegisterCollect.getExtraChannelId());

        isNull(extraChannelInfo,format("【业务开通接口】商户号：%s,未取到附属通道信息表",merId),"RXH00013");
        //封装发送请求参数
        SquareTrade squareTrade=new SquareTrade();
        squareTrade.setExtraChannelInfo(extraChannelInfo);
        squareTrade.setMerchantRegisterCollect(merchantRegisterCollect);
        squareTrade.setMerchantRegisterInfo(mri);
        TradeObjectSquare tradeObjectSquare=new TradeObjectSquare();
        tradeObjectSquare.setInnerType( SystemConstant.SERVICE_FULFILLMENT);
        tradeObjectSquare.setMimerCertPic1(merchantServiceFulfillment.getMimerCertPic1());
        tradeObjectSquare.setMimerCertPic2(merchantServiceFulfillment.getMimerCertPic2());
        ChannelInfo channelInfo = new ChannelInfo();
        channelInfo.setOrganizationId(extraChannelInfo.getOrganizationId());
        List<ChannelInfo> channelInfoList  = channelInfoService.selectByExample(channelInfo);
        //支付
        MerchantRate payMerRate =  merchantSquareRateCache.getMerchantRateByMerIdAndPayType(merId,"5");
        tradeObjectSquare.setPayFee(payMerRate.getRateFee());
        //代付
        MerchantRate transMerRate =  merchantSquareRateCache.getMerchantRateByMerIdAndPayType(merId,"4");
        tradeObjectSquare.setBackFee(transMerRate.getSingleFee());
        squareTrade.setTradeObjectSquare(tradeObjectSquare);
        squareTrade.setMerOrderId(merOrderId);

        //发送请求
        String result=HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(),
                extraChannelInfo.getUrl(),
                JsonUtils.objectToJsonNonNull(squareTrade));
        logger.info("【业务开通接口】业务开通接口cross返回结果：{}",result);
        //根据结果修改数据进件状态
        BankResult bankResult = JsonUtils.jsonToPojo(result, BankResult.class);
        isNull(bankResult,format("【业务开通接口】商户号：%s,交互业务开通接口失败",merId),"RXH00013");
        Short status=bankResult.getStatus();
        String param=bankResult.getParam();

        //更新结果
        merchantRegisterCollect=new MerchantRegisterCollect();
        merchantRegisterCollect.setMerOrderId(merOrderId);
        merchantRegisterCollect.setMerId(merId);
        merchantRegisterCollect.setBackData(param);
        merchantRegisterCollect.setResult(result);
        merchantRegisterCollect.setStatus((int)status);
        merchantRegisterCollectService.updateByMerOrderIdAndMerId(merchantRegisterCollect);
        systemOrderTrack.setOrderTrackStatus((int)status);
        String resultJson = this.resultToString(bankResult,merchantInfo,squareTrade,1);
        systemOrderTrack.setTradeResult(resultJson);
        return resultJson;
    }



    /**
     *   封装MerchantRegisterInfo
     * @param merchantRegisterInfo
     * @param merchantBankCardBinding
     * @return
     */

    private MerchantRegisterInfo packagingMerchantRegisterInfoOnSaveBankCardBinding(MerchantRegisterInfo merchantRegisterInfo,
                                                                                    MerchantBankCardBinding merchantBankCardBinding,
                                                                                    MerchantRegisterCollect merchantRegisterCollect){
        String  identityType= merchantBankCardBinding.getIdentityType();
        String  identityNum=  merchantBankCardBinding.getIdentityNum();
        String  cardHolderName= merchantBankCardBinding.getCardHolderName();
        String  province= merchantBankCardBinding.getProvince();
        String  city= merchantBankCardBinding.getCity();
        String terminalMerId=merchantRegisterCollect.getTerminalMerId();

        merchantRegisterInfo.setMerOrderId(merchantBankCardBinding.getMerOrderId());
        merchantRegisterInfo.setMerId(merchantBankCardBinding.getMerId());
        merchantRegisterInfo.setTerminalMerId(terminalMerId);

        if(! org.springframework.util.StringUtils.isEmpty(identityType)){
            merchantRegisterInfo.setIdentityType( Integer.valueOf(identityType));
        }
        if( !org.springframework.util.StringUtils.isEmpty(identityNum)){
            merchantRegisterInfo.setIdentityNum(identityNum);
        }
        if( !org.springframework.util.StringUtils.isEmpty(cardHolderName)){
            merchantRegisterInfo.setUserName(cardHolderName);
        }
        if( !org.springframework.util.StringUtils.isEmpty(province)){
            merchantRegisterInfo.setProvince(province);
        }
        if( !org.springframework.util.StringUtils.isEmpty(city)){
            merchantRegisterInfo.setCity(city);
        }
        return merchantRegisterInfo;
    }




    /**
     *  验证必要参数
     * @param systemOrderTrack
     * @return
     * @throws PayException
     */
    public MerchantServiceFulfillment verifyMustParamOnServiceFulfillment(SystemOrderTrack systemOrderTrack) throws PayException {
        MerchantServiceFulfillment merchantServiceFulfillment = JsonUtils.jsonToPojo(systemOrderTrack.getTradeInfo(), MerchantServiceFulfillment.class);
        List<String> tradeInfoKeys = new ArrayList<>();
        Map<String, Object> tradeInfoMap = JsonUtils.jsonToMap(systemOrderTrack.getTradeInfo());
        Set<String> keys = tradeInfoMap.keySet();
        String value = "";
        try{
            for (String key : keys) {
                if (tradeInfoMap.get(key) != null){
                    value = tradeInfoMap.get(key).toString();
                }
                validateValue(serviceFulfillment,key, value);
                tradeInfoKeys.add(key);
            }
            ValidateItem(serviceFulfillment,tradeInfoKeys);  // 校验必要参数
            return merchantServiceFulfillment;
        }catch (Exception e){
            throw new PayException(format("进件请求报文无法解析：%s" ,e.getMessage()),"RXH00013");
        }finally {
            systemOrderTrack.setMerId(merchantServiceFulfillment.getMerId());
            systemOrderTrack.setMerOrderId(merchantServiceFulfillment.getMerOrderId());
        }

    }

    public static void main(String[] args) {
        String j = "{\n" +
                "  \"merOrderId\": \"20190909155753372750015201372750\",\n" +
                "  \"merchantType\": \"01\",\n" +
                "  \"terminalMerId\": \"6e9a90d82ac54f1c9517f236d557977d\",\n" +
                "  \"terminalMerName\": \"0c6e37c12ffc41dd89865f505d7ed04a\",\n" +
                "  \"userShortName\": \"17\",\n" +
                "  \"category\": \"4131\",\n" +
                "  \"identityType\": 1,\n" +
                "  \"identityNum\": \"430124198712212937\",\n" +
                "  \"phone\": \"18874280031\",\n" +
                "  \"province\": \"430000\",\n" +
                "  \"city\": \"430100\",\n" +
                "  \"address\": \"湖南省长沙市岳麓区\",\n" +
                "  \"bankCode\": \"ICBC\",\n" +
                "  \"bankCardType\": 1,\n" +
                "  \"cardHolderName\": \"王健雄\",\n" +
                "  \"bankCardNum\": \"6212261901018491777\",\n" +
                "  \"bankCardPhone\": \"18874280031\",\n" +
                "  \"payFee\": \"0\",\n" +
                "  \"backFee\": \"0\",\n" +
                "  \"merId\": \"M201009\",\n" +
                "  \"charset\": \"UTF-8\",\n" +
                "  \"signType\": \"MD5\",\n" +
                "}";
        TreeMap map = JSONObject.parseObject(j,TreeMap.class);
        System.out.println(CheckMd5Utils.getMd5SignWithKey(map,"vvK2424b"));

    }

}
