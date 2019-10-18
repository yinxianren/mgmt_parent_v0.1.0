package com.rxh.wallet;


import com.rxh.exception.PayException;
import com.rxh.square.pojo.PayOrder;
import com.rxh.square.pojo.TransOrder;
import com.rxh.utils.PayMap;
import com.rxh.utils.StringUtils;
import com.rxh.utils.SystemConstant;
import com.rxh.vo.OrderObjectToMQ;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * 描述： 处理钱包业务逻辑
 * @author panda
 * @date 20190721
 * @version
 *    version 0.0.2:20190729
 */

@Component
public class HandleWalletServe  extends AbstractWalletComponent  implements IPayOrderHandle<OrderObjectToMQ>
        ,ITransOrderHandle<OrderObjectToMQ>{




    /**
     *    更新收单的钱包
     * @param orderObjectToMQ
     */
    @Override
    public void payOrderToObject(OrderObjectToMQ orderObjectToMQ) {
       logger.info("我的收单钱包日志监控："+orderObjectToMQ.toString());
        try{
            isNull(orderObjectToMQ,format("【MQ队列任务-钱包处理】从MQ接收的的订单对象为null"));
            String merId=orderObjectToMQ.getMerId();
            PayMap<String,Object> payMap=new PayMap<>();
            //获取商户钱包对象以及钱包明细
            merchantWalletComponet.getMerchantWalletObject(orderObjectToMQ,payMap);
            merchantWalletComponet.getMerchantsDetailsObject(orderObjectToMQ,payMap);
            //通道钱包
            channelWalletComponet.getChannelWalletObject(orderObjectToMQ,payMap);
            channelWalletComponet.getChannelDetailsObject(orderObjectToMQ,payMap);
            //获取终端商户钱包以及钱包明细
            if(!StringUtils.isEmpty(orderObjectToMQ.getTerminalMerId())){
                terminalMerchantsWalletComponet.getTerminalMerchantsObject(orderObjectToMQ,payMap);
                terminalMerchantsWalletComponet.getTerminalMerchantsDetailsObject(orderObjectToMQ,payMap);
            }
            //获取代理商钱包以及明细
            if(!StringUtils.isEmpty(orderObjectToMQ.getParentId())){
                agentWalletComponet.getAgentWalletObject(orderObjectToMQ,payMap);
                agentWalletComponet.getAgentMerchantsDetailsObject(orderObjectToMQ,payMap);
            }
            //更新订单状态为成功
            PayOrder payOrder=paymentRecordSquareService.getPayOrderById(orderObjectToMQ.getPayId());
            payOrder.setOrderStatus(0);
            if (payOrder.getPayType().equals("6")){
                payOrder.setOrderStatus(1);
            }
            if (payMap.get("settleStatus") != null && payMap.get("settleStatus").equals("1")){
                payOrder.setSettleStatus(1);
            }
            payOrder.setPayType(orderObjectToMQ.getChannelType().toString());
            payMap.put("payOrder",payOrder);
            int count= kuaiJiePayService.saveAndUpdatePayOrderRecord(payMap);
            logger.info("【MQ队列任务-钱包处理】商户号：{},更新了{}条数据！",merId,count);
            logger.info("我的收单钱包日志监控结束");
        }catch (Exception e){
            if(e instanceof PayException) logger.info(e.getMessage());
            this.handleExceptionPayOrder(orderObjectToMQ);
            e.printStackTrace();
        }
    }


    private void  handleExceptionTransOrder(OrderObjectToMQ orderObjectToMQ){
        String merId=orderObjectToMQ.getMerId();
        TransOrder transOrder=paymentRecordSquareService.getTransOrderOrderId(orderObjectToMQ.getTransId());
        transOrder.setOrderStatus(SystemConstant.MQ_EXCEPITON_PROCESSED);//订单处理失败
        PayMap<String,Object> payMap=new PayMap<>();
        payMap.put("transOrder",transOrder);
        int count= kuaiJiePayService.saveAndUpdateTransOrderRecord(payMap);
        logger.info("【MQ队列任务-钱包处理，TransOrder异常处理状态】商户号：{},更新了{}条数据！",merId,count);
    }

    @Override
    public void payOrderToToCollect(Collection<OrderObjectToMQ> collection) {

    }

    /**
     *   更新代付的钱包
     * @param orderObjectToMQ
     */
    @Override
    public void transOrderToObject(OrderObjectToMQ orderObjectToMQ) {
        try{
            logger.info("我的代付钱包日志监控："+orderObjectToMQ.toString());
            isNull(orderObjectToMQ,format("【MQ队列任务-钱包处理】从MQ接收的的订单对象为null"));
            String merId=orderObjectToMQ.getMerId();
            PayMap<String,Object> payMap=new PayMap<>();
            //获取商户钱包对象以及钱包明细
            merchantWalletComponet.handleMerchantWalletAadDedails(orderObjectToMQ,payMap);
            //通道钱包
            channelWalletComponet.handleChannelWalletAadDedails(orderObjectToMQ,payMap);
            //获取终端商户钱包以及钱包明细
            if(!StringUtils.isEmpty(orderObjectToMQ.getTerminalMerId())){
                terminalMerchantsWalletComponet.handleTerminalMerchantWalletAadDedails(orderObjectToMQ,payMap);
            }
            //获取代理商钱包以及明细
            if(!StringUtils.isEmpty(orderObjectToMQ.getParentId())) {
                agentWalletComponet.handleAgentWalletAadDedails(orderObjectToMQ,payMap);
            }
            TransOrder transOrder=paymentRecordSquareService.getTransOrderOrderId(orderObjectToMQ.getTransId());
            transOrder.setOrderStatus(0);
            transOrder.setSettleStatus(1);
            payMap.put("transOrder",transOrder);
            int count= kuaiJiePayService.saveAndUpdateTransOrderRecord(payMap);
            logger.info("【MQ队列任务-钱包处理】商户号：{},更新了{}条数据！",merId,count);
            logger.info("我的代付钱包日志监控结束");
        }catch (Exception e){
            if(e instanceof PayException) logger.info(e.getMessage());
            this.handleExceptionTransOrder(orderObjectToMQ);
            e.printStackTrace();
        }
    }
    private void  handleExceptionPayOrder(OrderObjectToMQ orderObjectToMQ){
        String merId=orderObjectToMQ.getMerId();
        PayOrder payOrder=paymentRecordSquareService.getPayOrderById(orderObjectToMQ.getPayId());
        payOrder.setOrderStatus(SystemConstant.MQ_EXCEPITON_PROCESSED);//订单处理失败
        PayMap<String,Object> payMap=new PayMap<>();
        payMap.put("payOrder",payOrder);
        int count= kuaiJiePayService.saveAndUpdateTransOrderRecord(payMap);
        logger.info("【MQ队列任务-钱包处理，PayOrder异常处理状态】商户号：{},更新了{}条数据！",merId,count);
    }
    @Override
    public void transOrderToToCollect(Collection<OrderObjectToMQ> collection) {

    }
}
