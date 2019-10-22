package com.rxh.anew.service.shortcut;


import com.rxh.anew.inner.InnerPrintLogObject;
import com.rxh.anew.inner.ParamRule;
import com.rxh.anew.service.CommonSerivceInterface;
import com.rxh.anew.table.business.RegisterCollectTable;
import com.rxh.anew.table.channel.ChannelInfoTable;
import com.rxh.anew.table.system.MerchantSettingTable;
import com.rxh.exception.NewPayException;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface NewIntoPiecesOfInformationService  extends CommonSerivceInterface {
    /**
     *
      * @return
     */
    Map<String, ParamRule>  getParamMapByIPOI();

    /**
     *  根据商户配置获取所有通道
     * @param list
     * @param ipo
     * @return
     * @throws NewPayException
     */
    List<ChannelInfoTable>   getChannelInfoByMerSetting(List<MerchantSettingTable> list, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *   根据产品类型过滤通道
     * @param list
     * @param productType
     * @param ipo
     * @return
     */
    Set<ChannelInfoTable> filtrationChannelInfoByProductType(List<ChannelInfoTable> list, String productType, InnerPrintLogObject ipo) throws NewPayException;


    List<RegisterCollectTable> getRegisterCollectOnSuccess(String bankCardNum,InnerPrintLogObject ipo);
}
