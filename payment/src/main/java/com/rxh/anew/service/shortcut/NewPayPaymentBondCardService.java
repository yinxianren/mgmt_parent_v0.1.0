package com.rxh.anew.service.shortcut;

import com.rxh.anew.dto.CrossResponseMsgDTO;
import com.rxh.anew.dto.MerchantBondCardApplyDTO;
import com.rxh.anew.inner.InnerPrintLogObject;
import com.rxh.anew.inner.ParamRule;
import com.rxh.anew.service.CommonSerivceInterface;
import com.rxh.anew.table.business.MerchantCardTable;
import com.rxh.anew.table.business.RegisterCollectTable;
import com.rxh.anew.table.channel.ChannelInfoTable;
import com.rxh.exception.NewPayException;

import java.util.Map;

public interface NewPayPaymentBondCardService extends CommonSerivceInterface {
    /**
     *  绑卡申请接口
     * @return
     */
    Map<String, ParamRule> getParamMapByBC();

    /**
     *  判断订单是否重复
     * @param merOrderId
     * @param ipo
     */
    void multipleOrder(String merOrderId, InnerPrintLogObject ipo) throws NewPayException;


    /**
     *  获取进件成功的附属表
     * @param mbcaDTO
     * @param ipo
     * @return
     * @throws NewPayException
     */
    RegisterCollectTable getSuccessRegisterCollectInfo( MerchantBondCardApplyDTO mbcaDTO,InnerPrintLogObject ipo) throws NewPayException;

    /**
     *  保存绑卡申请记录
     * @param mbcaDTO
     * @param channelInfoTable
     * @param ipo
     * @return
     */
    MerchantCardTable saveCardInfo(MerchantBondCardApplyDTO mbcaDTO, ChannelInfoTable channelInfoTable, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *  更新绑卡申请记录
     * @param crossResponseMsgDTO
     * @param crossResponseMsg
     * @param merchantCardTable
     * @param ipo
     */
    void updateByBondCardInfo(CrossResponseMsgDTO crossResponseMsgDTO, String crossResponseMsg, MerchantCardTable merchantCardTable, InnerPrintLogObject ipo) throws NewPayException;
}
