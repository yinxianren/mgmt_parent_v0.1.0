package com.rxh.service.wallet;

import com.rxh.square.pojo.*;
import com.rxh.utils.PayMap;

/**
 *   海懿支付钱包
 * @author  panda
 * @date 20190718
 */
public interface HaiYiPayWalletService {

    /**
     * 更新代付订单、商户和代理的钱包、商户和代理的明细以及平台钱包和明细
     * @param payMap
     * @return
     */
    Integer updateHaiYiPaySuccessTrandsOrderDetail(PayMap<String ,Object> payMap);
    /**
     *  更新代付 订单和商户钱包
     * @param transOrder
     * @param merchantWallet
     * @return
     *       都成功返回true
     *      只有失败一个就返回 false
     */
    void updateHaiYiPayFailTrandsOrderDetail(TransOrder transOrder,MerchantWallet merchantWallet);

    /**
     *  更新代付订单和审计
     * @return
     */
    Integer updateHaiYiPayTrandsOrderAndTransAudit(PayMap<String, Object> map);
    /**
     *   保存代付发起之前的交易信息
     * @param map
     * @return
     */
    Integer saveTransOrderInfo(PayMap<String, Object> map);
}
