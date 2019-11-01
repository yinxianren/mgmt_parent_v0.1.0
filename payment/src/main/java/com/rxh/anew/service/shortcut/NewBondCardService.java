package com.rxh.anew.service.shortcut;

import com.rxh.anew.dto.CrossResponseMsgDTO;
import com.rxh.anew.dto.MerBondCardApplyDTO;
import com.rxh.anew.dto.MerConfirmBondCardDTO;
import com.rxh.anew.dto.MerReGetBondCodeDTO;
import com.rxh.anew.inner.InnerPrintLogObject;
import com.rxh.anew.inner.ParamRule;
import com.rxh.anew.service.CommonSerivceInterface;
import com.rxh.anew.table.business.MerchantCardTable;
import com.rxh.anew.table.business.RegisterCollectTable;
import com.rxh.anew.table.channel.ChannelInfoTable;
import com.rxh.exception.NewPayException;

import java.util.List;
import java.util.Map;

public interface NewBondCardService extends CommonSerivceInterface {
    /**
     *  绑卡申请接口
     * @return
     */
    Map<String, ParamRule> getParamMapByB4();

    /**
     * 重新获取绑卡验证码
     * @return
     */
    Map<String, ParamRule> getParamMapByB5();

    /**
     * 确认短信
     * @return
     */
    Map<String, ParamRule> getParamMapByB6();
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
    MerchantCardTable saveCardInfoByB4(MerBondCardApplyDTO mbcaDTO, ChannelInfoTable channelInfoTable, RegisterCollectTable registerCollectTable, InnerPrintLogObject ipo) throws NewPayException;
    /**
     *
     * @param merchantCardTable
     * @param mrgbcDTO
     * @param ipo
     * @return
     */
    MerchantCardTable saveCardInfoByB5(MerchantCardTable merchantCardTable, MerReGetBondCodeDTO mrgbcDTO, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *
     * @param merchantCardTable
     * @param mcbcDTO
     * @param ipo
     * @return
     */
    MerchantCardTable saveCardInfoByB6(MerchantCardTable merchantCardTable, MerConfirmBondCardDTO mcbcDTO, InnerPrintLogObject ipo) throws NewPayException;
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
     *
     * @param mbcaDTO
     * @param ipo
     * @return
     */
    List<RegisterCollectTable> getRegCollectBySuccess(MerBondCardApplyDTO mbcaDTO, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *
     * @param registerCollectTableList
     * @param ipo
     * @return
     */
    ChannelInfoTable getChannelInfoByRegCollect(List<RegisterCollectTable> registerCollectTableList, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *
     * @param registerCollectTableList
     * @param channelId
     * @param ipo
     * @return
     */
    RegisterCollectTable filterRegCollectByChannelId(List<RegisterCollectTable> registerCollectTableList, String channelId, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *
     * @param mbcaDTO
     * @param ipo
     * @return
     */
    List<MerchantCardTable> getMerCartInfoBySuccess(MerBondCardApplyDTO mbcaDTO, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *
     * @param registerCollectTableList
     * @param merchantCardTableList
     * @param ipo
     * @return
     */
    List<RegisterCollectTable> filterRegCollectByBondCardSuccess(List<RegisterCollectTable> registerCollectTableList, List<MerchantCardTable> merchantCardTableList, InnerPrintLogObject ipo) throws NewPayException;
}
