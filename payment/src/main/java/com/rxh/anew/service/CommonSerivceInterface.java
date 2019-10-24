package com.rxh.anew.service;

import com.rxh.anew.dto.CrossResponseMsgDTO;
import com.rxh.anew.dto.RequestCrossMsgDTO;
import com.rxh.anew.inner.InnerPrintLogObject;
import com.rxh.anew.table.business.RegisterInfoTable;
import com.rxh.anew.table.channel.ChannelExtraInfoTable;
import com.rxh.anew.table.channel.ChannelInfoTable;
import com.rxh.anew.table.merchant.MerchantInfoTable;
import com.rxh.anew.table.system.MerchantSettingTable;
import com.rxh.anew.table.system.SystemOrderTrackTable;
import com.rxh.exception.NewPayException;
import com.rxh.tuple.Tuple2;

import java.util.List;

public interface CommonSerivceInterface {
    /**
     *  获取商户信息
     * @param ipo
     * @return
     * @throws NewPayException
     */
    MerchantInfoTable  getOneMerInfo(InnerPrintLogObject ipo) throws NewPayException;



    /**
     *  获取商户所有通道配置信息
     * @param ipo
     * @return
     */
    List<MerchantSettingTable> getMerchantSetting(InnerPrintLogObject ipo) throws NewPayException;

    /**
     * 封装请求cross必要参数
     * @param tuple
     * @return
     */
    RequestCrossMsgDTO getRequestCrossMsgDTO(Tuple2 tuple);

    /**
     * 封装响应结果
     * @param merInfoTable
     * @param requestCrossMsgDTO
     * @param crossResponseMsgDTO
     * @param errorCode
     * @param errorMsg
     * @return
     */
    String responseMsg(String merOrderId,MerchantInfoTable merInfoTable, RequestCrossMsgDTO  requestCrossMsgDTO, CrossResponseMsgDTO crossResponseMsgDTO,String errorCode,String errorMsg,InnerPrintLogObject ipo) throws NewPayException;

    /**
     *   请求cross
     * @param requestCrossMsgDTO
     * @param extraInfoTable
     * @param ipo
     * @return
     */
    String doPostJson(RequestCrossMsgDTO requestCrossMsgDTO, ChannelExtraInfoTable extraInfoTable, InnerPrintLogObject ipo);

    /**
     *  将结果转对象
     * @param crossResponseMsg
     * @param ipo
     * @return
     */
    CrossResponseMsgDTO jsonToPojo(String crossResponseMsg, InnerPrintLogObject ipo);

    /**
     *
     * @param systemOrderTrackTable
     * @return
     */
    boolean saveSysLog(SystemOrderTrackTable systemOrderTrackTable);

    /**
     *  获取通道信息
     * @param channelId
     * @param ipo
     * @return
     */
    ChannelInfoTable getChannelInfoByChannelId(String channelId, InnerPrintLogObject ipo)throws NewPayException;

    /**
     *  获取附属通道信息
     * @param organizationId
     * @param bussType
     * @param ipo
     * @return
     */
    ChannelExtraInfoTable getChannelExtraInfoByOrgId(String organizationId, String bussType, InnerPrintLogObject ipo)throws NewPayException;

    /**
     *  获取进件主表
     * @param ritId
     * @param ipo
     * @return
     */
    RegisterInfoTable getRegisterInfoTable(Long ritId, InnerPrintLogObject ipo) throws NewPayException;
}
