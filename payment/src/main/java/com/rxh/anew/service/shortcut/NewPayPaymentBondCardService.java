package com.rxh.anew.service.shortcut;

import com.rxh.anew.dto.CrossResponseMsgDTO;
import com.rxh.anew.dto.MerchantBondCardApplyDTO;
import com.rxh.anew.dto.MerchantConfirmBondCardDTO;
import com.rxh.anew.dto.MerchantReGetBondCodeDTO;
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
     * 重新获取绑卡验证码
     * @return
     */
    Map<String, ParamRule> getParamMapByMRGBC();

    /**
     * 确认短信
     * @return
     */
    Map<String, ParamRule> getParamMapByCBC();
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
//    RegisterCollectTable getSuccessRegisterCollectInfo( MerchantBondCardApplyDTO mbcaDTO,InnerPrintLogObject ipo) throws NewPayException;

    /**
     *  保存绑卡申请记录
     * @param mbcaDTO
     * @param channelInfoTable
     * @param ipo
     * @return
     */
    MerchantCardTable saveCardInfoByB4(MerchantBondCardApplyDTO mbcaDTO, ChannelInfoTable channelInfoTable, RegisterCollectTable registerCollectTable, InnerPrintLogObject ipo) throws NewPayException;
    /**
     *
     * @param merchantCardTable
     * @param mrgbcDTO
     * @param ipo
     * @return
     */
    MerchantCardTable saveCardInfoByB5(MerchantCardTable merchantCardTable, MerchantReGetBondCodeDTO mrgbcDTO, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *
     * @param merchantCardTable
     * @param mcbcDTO
     * @param ipo
     * @return
     */
    MerchantCardTable saveCardInfoByB6(MerchantCardTable merchantCardTable, MerchantConfirmBondCardDTO mcbcDTO, InnerPrintLogObject ipo) throws NewPayException;
    /**
     *  更新绑卡申请记录
     * @param crossResponseMsgDTO
     * @param crossResponseMsg
     * @param merchantCardTable
     * @param ipo
     */
    void updateByBondCardInfo(CrossResponseMsgDTO crossResponseMsgDTO, String crossResponseMsg, MerchantCardTable merchantCardTable, InnerPrintLogObject ipo) throws NewPayException;

    /**
     * 根据平台流水号获取进件成功的附属表
     * @param platformOrderId
     * @param ipo
     * @return
     */
    RegisterCollectTable getRegisterInfoTableByPlatformOrderId(String platformOrderId, InnerPrintLogObject ipo) throws NewPayException;

    /**
     * 更加平台订单号获取B4操作记录
     * @param platformOrderId
     * @param ipo
     * @return
     */
    MerchantCardTable getMerchantCardInfoByPlatformOrderId(String platformOrderId, String busiType, InnerPrintLogObject ipo) throws NewPayException;



}
