package com.rxh.anew.service.shortcut;

import com.rxh.anew.dto.MerchantPayOrderDTO;
import com.rxh.anew.inner.InnerPrintLogObject;
import com.rxh.anew.inner.ParamRule;
import com.rxh.anew.service.CommonSerivceInterface;
import com.rxh.anew.table.business.MerchantCardTable;
import com.rxh.anew.table.business.RegisterCollectTable;
import com.rxh.anew.table.channel.ChannelHistoryTable;
import com.rxh.anew.table.channel.ChannelInfoTable;
import com.rxh.anew.table.merchant.MerchantInfoTable;
import com.rxh.anew.table.merchant.MerchantQuotaRiskTable;
import com.rxh.anew.table.system.RiskQuotaTable;
import com.rxh.exception.NewPayException;
import com.rxh.tuple.Tuple2;

import java.util.List;
import java.util.Map;

public interface NewPayOrderService extends CommonSerivceInterface {
    /**
     * 支付下单申请
     * @return
     */
    Map<String, ParamRule> getParamMapByB7();

    /**
     * 查看是否重复订单
     * @param merOrderId
     * @param ipo
     */
    void multipleOrder(String merOrderId, InnerPrintLogObject ipo) throws NewPayException;
    /**
     *  获取商户风控信息
     * @param merchantId
     * @param ipo
     * @return
     */
    MerchantQuotaRiskTable getMerchantQuotaRiskByMerId(String merchantId, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *  获取成功进件记录
     * @param merchantPayOrderDTO
     * @param ipo
     * @return
     */
    List<RegisterCollectTable> getSuccessRegisterInfo(MerchantPayOrderDTO merchantPayOrderDTO, InnerPrintLogObject ipo) throws NewPayException;

    /**
     * 根据进件信息获取邦卡记录
     * @param registerCollectTableList
     * @param ipo
     * @return
     */
    List<MerchantCardTable> getSuccessMerchantCardInfo(List<RegisterCollectTable> registerCollectTableList, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *  查询通道使用记录
     * @param merchantPayOrderDTO
     * @param ipo
     * @return
     */
    ChannelHistoryTable getChannelHistoryInfo(MerchantPayOrderDTO merchantPayOrderDTO, InnerPrintLogObject ipo) throws NewPayException;

    /**
     * 判断该卡是否进行绑卡操作
     * @param merchantCardTableList
     * @param merchantPayOrderDTO
     * @param ipo
     */
    void checkBondCardByCardNum(List<MerchantCardTable> merchantCardTableList, MerchantPayOrderDTO merchantPayOrderDTO, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *  获取风控交易量统计数据
     * @param merInfoTable
     * @param ipo
     * @return
     */
    Tuple2<RiskQuotaTable, RiskQuotaTable> getRiskQuotaInfoByMer(MerchantInfoTable merInfoTable, InnerPrintLogObject ipo) throws NewPayException;
    /**
     *  单笔风控
     * @param merchantPayOrderDTO
     * @param merchantQuotaRiskTable
     * @param ipo
     */
    void checkSingleAmountRisk(MerchantPayOrderDTO merchantPayOrderDTO, MerchantQuotaRiskTable merchantQuotaRiskTable, InnerPrintLogObject ipo) throws NewPayException;
    /**
     *  执行平台风控
     * @param merchantQuotaRiskTable
     * @param merRiskQuota
     */
    void executePlatformRisk(MerchantPayOrderDTO merchantPayOrderDTO, MerchantQuotaRiskTable merchantQuotaRiskTable, Tuple2<RiskQuotaTable, RiskQuotaTable> merRiskQuota, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *  获取通道信息
     * @param channelHistoryTable
     * @param ipo
     * @return
     */
    ChannelInfoTable getChannelInfoByChannelHistory(ChannelHistoryTable channelHistoryTable, InnerPrintLogObject ipo) throws NewPayException;

    /**
     * 获取该通道历史统计交易量
     * @param channelInfoTable
     * @param ipo
     * @return
     */
    Tuple2<RiskQuotaTable, RiskQuotaTable> getRiskQuotaInfoByChannel(ChannelInfoTable channelInfoTable, InnerPrintLogObject ipo) throws NewPayException;

    /**
     * 执行通道风控
     * @param merchantPayOrderDTO
     * @param channelInfoTable
     * @param channelRiskQuota
     * @param ipo
     */
    ChannelInfoTable executeChannelRisk(MerchantPayOrderDTO merchantPayOrderDTO, ChannelInfoTable channelInfoTable, Tuple2<RiskQuotaTable, RiskQuotaTable> channelRiskQuota, InnerPrintLogObject ipo) throws NewPayException;

    /**
     * 获取商户可用支付的所有通道
     * @param merchantPayOrderDTO
     * @param channelHistoryTable
     * @param ipo
     * @return
     */
    List<ChannelInfoTable> getAllUsableChannel(MerchantPayOrderDTO merchantPayOrderDTO, ChannelHistoryTable channelHistoryTable, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *  获取一个可行性的通道
     * @param channelInfoTableList
     * @param ipo
     * @return
     */
    ChannelInfoTable getFeasibleChannel(MerchantPayOrderDTO merchantPayOrderDTO,List<ChannelInfoTable> channelInfoTableList, InnerPrintLogObject ipo) throws NewPayException;
}
