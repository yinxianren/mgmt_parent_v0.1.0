package com.rxh.anew.service;

import com.rxh.anew.dto.RequestCrossMsgDTO;
import com.rxh.anew.inner.InnerPrintLogObject;
import com.rxh.anew.table.channel.ChannelExtraInfoTable;
import com.rxh.anew.table.merchant.MerchantInfoTable;
import com.rxh.anew.table.system.MerchantSettingTable;
import com.rxh.exception.NewPayException;
import com.rxh.pojo.cross.BankResult;
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
     *  判断多重订单
     * @return
     */
    boolean multipleOrder(String merOrderId,InnerPrintLogObject ipo) throws NewPayException;

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
     *   请求cross
     * @param requestCrossMsgDTO
     * @param extraInfoTable
     * @param ipo
     * @return
     */
    String doPostJson(RequestCrossMsgDTO requestCrossMsgDTO, ChannelExtraInfoTable extraInfoTable, InnerPrintLogObject ipo);

    /**
     *  将结果转对象
     * @param result
     * @param ipo
     * @return
     */
    BankResult jsonToPojo(String result, InnerPrintLogObject ipo);
}
