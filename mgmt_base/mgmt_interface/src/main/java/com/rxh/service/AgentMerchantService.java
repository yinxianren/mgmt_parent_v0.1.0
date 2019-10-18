package com.rxh.service;

import com.rxh.pojo.merchant.*;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: 陈俊雄
 * Date: 2018/3/21
 * Time: 14:48
 * Project: Management
 * Package: com.rxh.service
 */
public interface AgentMerchantService {
    /**
     * 商户ID是否已经存在
     *
     * @param merId 商户ID
     * @return true是/false否
     */
    Boolean isMerchantIdExist(Integer merId);

    /**
     * 添加商户
     *
     * @param merchant 商户对象包含：MerchantInfo MerchantPay MerchantSetting
     * @param userName 操作者用户名
     * @return 是否添加成功
     */
    Boolean addMerchant(Merchant merchant, String userName);

    /**
     * 删除商户：
     * 商户设置merchantSetting、
     * 商户信息merchantInfo、
     * 商户支付信息merchantPay、
     * 商户扣率merchantRate、
     * 商户用户merchantUser、
     * 网址merchantWebSite、
     * 网址支付信息merchantPay、
     * 网址扣率merchantRate、
     *
     * @param merchantId 商户ID
     * @return 在商户无任何交易的情况下成功true，否则false
     */
    Boolean deleteMerchantById(String merchantId);

    /**
     * 更新商户
     *
     * @param merchant 商户对象包含：MerchantInfo MerchantPay MerchantSetting
     * @param userName 操作者用户名
     * @return 是否更新成功
     */
    Boolean updateMerchant(Merchant merchant, String userName);

    /**
     * 添加商户信息
     *
     * @param merchantInfo 商户信息对象
     * @param userName     操作者用户名
     * @return 是否成功
     */
    Boolean addMerchantInfo(MerchantInfo merchantInfo, String userName);

    Boolean deleteMerchantInfo(MerchantInfo merchantInfo);

    Boolean updateMerchantInfo(MerchantInfo merchantInfo, String userName);

    Boolean updateMerchantRateByList(List<MerchantRate> merchantRateList, String userName);

    List<MerchantRate> getMerchantRateByRefIdAndRefType(Integer refId, Short refType);

    List<MerchantRate> selectAllMerchantRate();

    List<MerchantInfo> selectAllMerchantInfo();

    MerchantInfo selectMerchantInfoByMerchantId(String merchantId);

    List<MerchantWebSite> selectAllMerchantWebSite();

    List<MerchantWebSite> selectMerchantWebSiteByMerchantId(String merchantId);

    List<Integer> deleteMerchantWebSite(List<Integer> idList);

    Boolean addMerchantWebSite(MerchantWebSite merchantWebSite, String userName);

    Boolean updateMerchantWebSite(MerchantWebSite merchantWebSite, String userName);

    Map<String, Object> getMerchantSettingInit();

    Boolean addMerchantSetting(MerchantSetting merchantSetting);

    Boolean deleteMerchantSetting(MerchantSetting merchantSetting);

    Boolean updateMerchantSetting(MerchantSetting merchantSetting, String userName);

    List<MerchantSetting> selectAllMerchantSetting();

    Boolean addMerchantPay(MerchantPay merchantPay, String userName);

    Boolean deleteMerchantPay(MerchantPay merchantPay);

    Boolean updateMerchantPay(MerchantPay merchantPay, String userName);

    MerchantPay selectMerchantPayByRefIdAndRefType(Integer refId, Short refType);

    List<MerchantPay> selectAllMerchantPay();

    Map<String, Object> getAllMerchantInfoForView();

    Map<String, Object> getMgmtInit();

    Map<String, Object> getMerchantPayInit();

    Boolean addMerchantWebsiteProType(MerchantWebsiteProType merchantWebsiteProType, String userName);

    List<Long> deleteMerchantWebsiteProType(List<Long> idList);

    Boolean updateMerchantWebsiteProType(MerchantWebsiteProType merchantWebsiteProType, String userName);

    List<MerchantWebsiteProType> selectAllMerchantWebsiteProType();

    Map<String, Object> getWebSiteInit();

    Map<String, Object> getMerchantWebSiteInit(String merchantId);

    Boolean isSiteUrlExist(String siteUrl);

    Map<String, Object> getQuickTestInit();

    Map<String, Object> getMerchantSettingInit(String merchantId);

    Map<String, Object> getMerchantRateInit(String merchantId);


    /**
     * 网站域名批量审核
     * @param merchantWebSiteSuccessList
     * @return
     */
    void checkWebsiteBatch(List<MerchantWebSite> merchantWebSiteSuccessList);

    List<MerchantWebSite> selectAllIdAndMerIdAndWeb();
}
