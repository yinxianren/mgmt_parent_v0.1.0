package com.rxh.service.wallet;

import com.rxh.square.pojo.*;
import com.rxh.utils.PayMap;

import java.util.List;

/**
 *  描述 ： 快捷支付mapper服务成
 * @author  panda
 * @date 20190716
 */
public interface KuaiJiePayService {

    /**
     *  收单信息保存以及更新
     * @param payOrder
     * @param payCardholderInfo
     * @param payProductDetail
     * @param riskQuotaDataList
     * @return
     */
    Integer saveAndUpdateTransPayRecord(PayOrder payOrder, PayCardholderInfo payCardholderInfo
                                               , PayProductDetail payProductDetail, List<RiskQuotaData> riskQuotaDataList);


    Integer saveAndUpdateTransOrderAndTransAudit( TransOrder transOrder,TransAudit transAudit);

    /**
     * 收单信息保存以及更新
     * @param payMap
     * @return
     */
    Integer saveAndUpdatePayOrderRecord( PayMap<String,Object> payMap);
    /**
     * 代付信息保存以及更新
     * @param payMap
     * @return
     */
    Integer saveAndUpdateTransOrderRecord( PayMap<String,Object> payMap);
    /**
     *   保存代付订单信息
     * @param payMap
     * @return
     */
    Integer saveTransOrderPayRecord(PayMap<String,Object> payMap);


    Integer saveAndUpdateWalletsAndRecord(PayMap<String,Object> payMap);
}
