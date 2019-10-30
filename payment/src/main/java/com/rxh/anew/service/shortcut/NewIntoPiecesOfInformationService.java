package com.rxh.anew.service.shortcut;


import com.rxh.anew.dto.CrossResponseMsgDTO;
import com.rxh.anew.dto.MerBankCardBindDTO;
import com.rxh.anew.dto.MerBasicInfoRegDTO;
import com.rxh.anew.inner.InnerPrintLogObject;
import com.rxh.anew.inner.ParamRule;
import com.rxh.anew.service.CommonSerivceInterface;
import com.rxh.anew.table.business.RegisterCollectTable;
import com.rxh.anew.table.business.RegisterInfoTable;
import com.rxh.anew.table.channel.ChannelExtraInfoTable;
import com.rxh.anew.table.channel.ChannelInfoTable;
import com.rxh.anew.table.system.MerchantSettingTable;
import com.rxh.anew.table.system.ProductSettingTable;
import com.rxh.exception.NewPayException;
import com.rxh.tuple.Tuple2;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface NewIntoPiecesOfInformationService  extends CommonSerivceInterface {
    /**
     *
      * @return
     */
    Map<String, ParamRule> getParamMapByB1();


    /**
     *
     * @return
     */
    Map<String, ParamRule> getParamMapByB2();

    /**
     *
     * @return
     */
    Map<String, ParamRule> getParamMapByB3();
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
    Tuple2<ProductSettingTable,Set<ChannelInfoTable>> filtrationChannelInfoByProductType(List<ChannelInfoTable> list, String productType, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *  获取子商户成功进件的所有记录
     * @param ipo
     * @return
     */
    List<RegisterCollectTable> getRegisterCollectOnSuccess(InnerPrintLogObject ipo);

    /**
     *  过滤出已经成功进件的通道
     * @param tuple2
     * @param registerCollectTableList
     * @param ipo
     * @return
     */
    LinkedList<ChannelInfoTable> filtrationChannelInfoBySuccessRegisterCollect(Tuple2<ProductSettingTable,Set<ChannelInfoTable>> tuple2, List<RegisterCollectTable> registerCollectTableList, InnerPrintLogObject ipo) throws NewPayException;

    /**
     * 获取星级最高的通道，如果相同，取最后一个
     * @param channelInfoTablesList
     * @param ipo
     * @return
     */
    ChannelInfoTable filtrationChannelInfoByLevel(LinkedList<ChannelInfoTable> channelInfoTablesList, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *  获取进件附属通道信息
     * @param channelInfoTable
     * @param ipo
     * @return
     */
    ChannelExtraInfoTable getAddCusChannelExtraInfo(ChannelInfoTable channelInfoTable, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *  保存进件信息
     * @param mbirDTO
     * @param channelInfoTable
     */
    Tuple2<RegisterInfoTable,RegisterCollectTable> saveByRegister(MerBasicInfoRegDTO mbirDTO, ChannelInfoTable channelInfoTable, InnerPrintLogObject ipo);

    /**
     * 更新进件新
     * @param crossResponseMsgDTO
     * @param registerCollectTable
     * @return
     */
    RegisterCollectTable updateByRegisterCollectTable(CrossResponseMsgDTO crossResponseMsgDTO, String crossResponseMsg, RegisterCollectTable registerCollectTable, InnerPrintLogObject ipo);

    /**
     *  判断多重订单
     * @return
     */
    boolean multipleOrder(String merOrderId,InnerPrintLogObject ipo) throws NewPayException;
    /**
     * 查看订单是否存在
     * @param platformOrderId
     * @param busiType
     * @param ipo
     * @return
     * @throws NewPayException
     */
    RegisterCollectTable getRegisterCollectTable(String platformOrderId,String busiType, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *  更新b2z值
     * @param registerCollectTable
     * @param mbcbDTO
     * @param ipo
     * @return
     */
    Tuple2<RegisterInfoTable,RegisterCollectTable> saveOnRegisterInfo(RegisterCollectTable registerCollectTable, MerBankCardBindDTO mbcbDTO, InnerPrintLogObject ipo) throws NewPayException;



    /**
     *
     * @param registerCollectTable
     * @param ipo
     * @return
     */
    RegisterCollectTable saveRegisterCollectTableByB3(RegisterCollectTable registerCollectTable, InnerPrintLogObject ipo) throws NewPayException;


}
