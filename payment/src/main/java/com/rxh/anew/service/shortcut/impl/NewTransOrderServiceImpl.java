package com.rxh.anew.service.shortcut.impl;

import com.alibaba.fastjson.JSON;
import com.rxh.anew.dto.CrossResponseMsgDTO;
import com.rxh.anew.dto.MerTransOrderApplyDTO;
import com.rxh.anew.dto.RequestCrossMsgDTO;
import com.rxh.anew.inner.InnerPrintLogObject;
import com.rxh.anew.inner.ParamRule;
import com.rxh.anew.service.CommonServiceAbstract;
import com.rxh.anew.service.shortcut.NewTransOrderService;
import com.rxh.anew.table.agent.AgentMerchantSettingTable;
import com.rxh.anew.table.business.*;
import com.rxh.anew.table.channel.ChannelInfoTable;
import com.rxh.anew.table.merchant.MerchantInfoTable;
import com.rxh.anew.table.merchant.MerchantRateTable;
import com.rxh.anew.table.system.OrganizationInfoTable;
import com.rxh.enums.*;
import com.rxh.exception.NewPayException;
import com.rxh.tuple.Tuple2;
import com.rxh.tuple.Tuple5;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/11/5
 * Time: 下午7:29
 * Description:
 */
@Service
public class NewTransOrderServiceImpl extends CommonServiceAbstract implements NewTransOrderService {

    @Override
    public RequestCrossMsgDTO getRequestCrossMsgDTO(Tuple2 tuple) throws NewPayException {
        Tuple5<TransOrderInfoTable,ChannelInfoTable, RegisterCollectTable, RegisterInfoTable, MerchantCardTable> tuple5 = (Tuple5<TransOrderInfoTable, ChannelInfoTable, RegisterCollectTable, RegisterInfoTable, MerchantCardTable>) tuple;
        return new RequestCrossMsgDTO()
                .setTransOrderInfoTable(tuple5._1)
                .setChannelInfoTable(tuple5._2)
                .setRegisterCollectTable(tuple5._3)
                .setRegisterInfoTable(tuple5._4)
                .setMerchantCardTable(tuple5._5);
    }


    @Override
    public Map<String, ParamRule> getParamMapByB11() {
        return new HashMap<String, ParamRule>() {
            {
                put("charset", new ParamRule(ParamTypeEnum.STRING.getType(), 5, 5));//参数字符集编码 固定UTF-8
                put("signType", new ParamRule(ParamTypeEnum.STRING.getType(), 3, 3));//签名类型	固定为MD5
                put("productType", new ParamRule(ParamTypeEnum.STRING.getType(), 3, 64));//产品类型		否	64
                put("merId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 32));//商户号
                put("terMerId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 64));//子商户id	商户系统中商户的编码，要求唯一	否	64
                put("terMerName", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 32));//子商户名称	商户系统中商户的名称	否	32
                put("productCategory", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 32));// 商品类别	参考附件：商户入住字典表	否	32
                put("merOrderId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 64));// 商户订单号
                put("orgMerOrderId", new ParamRule(ParamTypeEnum.STRING.getType(), 12, 640));//原交易商户订单号	可传多笔,最多10笔，用”| ”隔开
                put("currency", new ParamRule(ParamTypeEnum.STRING.getType(), 3, 3));// 交易的币种
//                put("amount", new ParamRule(ParamTypeEnum.AMOUNT.getType(), 2, 12));// 代付金额	代付金额≤原订单总金额-代付手续费
                put("identityType", new ParamRule(ParamTypeEnum.STRING.getType(), 1, 1));//证件类型	1身份证、2护照、3港澳回乡证、4台胞证、5军官证、	否	1
                put("identityNum", new ParamRule(ParamTypeEnum.STRING.getType(), 12, 32));//证件号码		否	32
                put("bankCode", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 16));//银行名称	如：中国农业银行： ABC，中国工商银行： ICBC	否	16
                put("bankCardType", new ParamRule(ParamTypeEnum.STRING.getType(), 1, 1));//卡号类型	1借记卡  2信用卡	否	1
                put("bankCardNum", new ParamRule(ParamTypeEnum.STRING.getType(), 12, 32));//银行卡号		否	32
                put("bankCardPhone", new ParamRule(ParamTypeEnum.PHONE.getType(), 11, 11));//银行卡手机号		否	11
                put("backFee", new ParamRule(ParamTypeEnum.AMOUNT.getType(), 2, 8));//	代付手续费	单笔固定金额
                put("cardHolderName", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 32));// 持卡人姓名
                put("returnUrl", new ParamRule(ParamTypeEnum.URL.getType(), 16, 128));//签名字符串
                put("noticeUrl", new ParamRule(ParamTypeEnum.URL.getType(), 16, 128));//签名字符串
                put("signMsg", new ParamRule(ParamTypeEnum.STRING.getType(), 16, 256));//签名字符串
            }
        };
    }

    @Override
    public void multipleOrder(String merOrderId, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="multipleOrder";
        TransOrderInfoTable  transOrderInfoTable=null;
        try{
            transOrderInfoTable = commonRPCComponent.apiTransOrderInfoService.getOne(new TransOrderInfoTable()
                    .setMerchantId(ipo.getMerId())
                    .setTerminalMerId(ipo.getTerMerId())
                    .setMerOrderId(merOrderId));
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：查询订单是否重复发生异常,异常信息：%s",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg()) );
        }
        isNotNull(transOrderInfoTable,
                ResponseCodeEnum.RXH00009.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;",
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00009.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00009.getMsg()));
    }

    @Override
    public List<PayOrderInfoTable> getPayOrderInfoByList(MerTransOrderApplyDTO merTransOrderApplyDTO, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="getPayOrderInfoByList";
        String orgMerOrderIds = merTransOrderApplyDTO.getOrgMerOrderId().trim();
        List<String> orgMerOrderIdList = Arrays.asList(orgMerOrderIds.split("\\|"));
        Set<String> orgMerOrderIdSet = orgMerOrderIdList.stream().map(t->t.trim()).collect(Collectors.toSet());
        List<PayOrderInfoTable> payOrderInfoTableList = null;
        try{
            //***********************多笔代付***********************
            if(orgMerOrderIdSet.size() >1 ) {
                payOrderInfoTableList = commonRPCComponent.apiPayOrderInfoService.getList(new PayOrderInfoTable()
                        .setMerOrderIdCollect(orgMerOrderIdSet)
                        .setMerchantId(ipo.getMerId())
                        .setTerminalMerId(ipo.getTerMerId())
                        .setStatus(StatusEnum._0.getStatus()));

                isHasNotElement(payOrderInfoTableList,
                        ResponseCodeEnum.RXH00008.getCode(),
                        format("%s-->商户号：%s；终端号：%s；错误信息: %s ,%s；代码所在位置：%s;",
                                ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH00008.getMsg(), merTransOrderApplyDTO.getOrgMerOrderId(), localPoint),
                        format(" %s : %s", ResponseCodeEnum.RXH00008.getMsg(), merTransOrderApplyDTO.getOrgMerOrderId()));
                //判断哪些订单不存在
                LinkedList<String> linkedList = new LinkedList<>(orgMerOrderIdList);
                payOrderInfoTableList.forEach(pay -> {
                    orgMerOrderIdList.forEach(org -> {
                        if (pay.getMerOrderId().equalsIgnoreCase(org)) linkedList.remove(org);
                    });
                });

                isHasElement(linkedList,
                        ResponseCodeEnum.RXH00008.getCode(),
                        format("%s-->商户号：%s；终端号：%s；错误信息: %s ,%s；代码所在位置：%s;",
                                ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH00008.getMsg(), JSON.toJSONString(linkedList), localPoint),
                        format(" %s : %s", ResponseCodeEnum.RXH00008.getMsg(), JSON.toJSONString(linkedList)));

                return payOrderInfoTableList;
            }
            //***********************单笔代付***********************
            PayOrderInfoTable payOrderInfoTable = commonRPCComponent.apiPayOrderInfoService.getOne(new PayOrderInfoTable()
                    .setMerOrderId(orgMerOrderIdSet.stream().findAny().get())
                    .setMerchantId(ipo.getMerId())
                    .setTerminalMerId(ipo.getTerMerId())
                    .setStatusCollect( Arrays.asList(StatusEnum._0.getStatus(),StatusEnum._11.getStatus())));

            isNull(payOrderInfoTable,
                    ResponseCodeEnum.RXH00008.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH00008.getMsg(), localPoint),
                    format(" %s : %s", ResponseCodeEnum.RXH00008.getMsg(), merTransOrderApplyDTO.getOrgMerOrderId()));

            //查单笔是否有代付记录
            List<TransOrderInfoTable>  transOrderInfoTableList = commonRPCComponent.apiTransOrderInfoService.getList(new TransOrderInfoTable()
                    .setOrgMerOrderId(payOrderInfoTable.getMerOrderId())
                    .setMerchantId(payOrderInfoTable.getMerchantId())
                    .setTerminalMerId(payOrderInfoTable.getTerminalMerId())
                    .setStatus(StatusEnum._0.getStatus()));

            //收单入账金额
            BigDecimal payOrderAmount = payOrderInfoTable.getInAmount();
            //------------有代付记录-----------------
            if(isHasElement(transOrderInfoTableList)){
                //已经代付过的金额
                BigDecimal transOrderAmount = transOrderInfoTableList.stream()
                        .map(TransOrderInfoTable::getAmount)
                        .reduce((_1,_2)->_1.add(_2)).get();
                //剩余未代付金额
                BigDecimal  residueAmount = payOrderAmount.subtract(transOrderAmount);

                //不足 1RMB 禁止代付
                state(residueAmount.compareTo(TransAmountRiskManageEnum.T_SINGLE_MIN_1.getAmount()) == 0,
                        ResponseCodeEnum.RXH00052.getCode(),
                        format("%s-->商户号：%s；终端号：%s；错误信息: %s ,%s；代码所在位置：%s;",
                                ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH00052.getMsg(), localPoint),
                        format(" %s : %s", ResponseCodeEnum.RXH00052.getMsg(), merTransOrderApplyDTO.getOrgMerOrderId()));

                //比较订单金额是否超出
                String amountStr = merTransOrderApplyDTO.getAmount();
                if( !isBlank(amountStr) ){
                    BigDecimal amount = new BigDecimal(amountStr);

                    //不足 1RMB 禁止代付
                    state(amount.compareTo(TransAmountRiskManageEnum.T_SINGLE_MIN_1.getAmount()) == 0,
                            ResponseCodeEnum.RXH00053.getCode(),
                            format("%s-->商户号：%s；终端号：%s；错误信息: %s ,%s；代码所在位置：%s;",
                                    ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH00053.getMsg(), localPoint),
                            format(" %s : %s", ResponseCodeEnum.RXH00053.getMsg(), merTransOrderApplyDTO.getOrgMerOrderId()));

                    //代付金额大于可支付金额
                    state(amount.compareTo(residueAmount ) > 0,
                            ResponseCodeEnum.RXH00011.getCode(),
                            format("%s-->商户号：%s；终端号：%s；错误信息: %s ,%s；代码所在位置：%s;",
                                    ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH00011.getMsg(), localPoint),
                            format(" %s : %s", ResponseCodeEnum.RXH00011.getMsg(), merTransOrderApplyDTO.getOrgMerOrderId()));
                }
            }else{
                //------------无代付记录-----------------
                //比较订单金额是否超出
                String amountStr = merTransOrderApplyDTO.getAmount();
                if( !isBlank(amountStr) ){
                    BigDecimal amount = new BigDecimal(amountStr);

                    //不足 1RMB 禁止代付
                    state(amount.compareTo(TransAmountRiskManageEnum.T_SINGLE_MIN_1.getAmount()) == 0,
                            ResponseCodeEnum.RXH00053.getCode(),
                            format("%s-->商户号：%s；终端号：%s；错误信息: %s ,%s；代码所在位置：%s;",
                                    ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH00053.getMsg(), localPoint),
                            format(" %s : %s", ResponseCodeEnum.RXH00053.getMsg(), merTransOrderApplyDTO.getOrgMerOrderId()));

                    //代付金额大于可支付金额
                    state(amount.compareTo( payOrderAmount ) > 0,
                            ResponseCodeEnum.RXH00011.getCode(),
                            format("%s-->商户号：%s；终端号：%s；错误信息: %s ,%s；代码所在位置：%s;",
                                    ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH00011.getMsg(), localPoint),
                            format(" %s : %s", ResponseCodeEnum.RXH00011.getMsg(), merTransOrderApplyDTO.getOrgMerOrderId()));
                }
            }
            payOrderInfoTableList = new ArrayList<>();
            payOrderInfoTableList.add(payOrderInfoTable);
            return payOrderInfoTableList;
        }catch (Exception e) {
            e.printStackTrace();
            if (e instanceof NewPayException) {
                NewPayException npe = (NewPayException) e;
                throw npe;
            } else {
                e.printStackTrace();
                throw new NewPayException(
                        ResponseCodeEnum.RXH99999.getCode(),
                        format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：收单订单信息核实过程中，发生了未知错误,异常信息：%s",
                                ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint, e.getMessage()),
                        format(" %s", ResponseCodeEnum.RXH99999.getMsg()));
            }
        }
    }

    @Override
    public BigDecimal verifyOrderAmount(List<PayOrderInfoTable> payOrderInfoTableList, MerTransOrderApplyDTO merTransOrderApplyDTO, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="verifyOrderAmount";
        BigDecimal totalInAmount = payOrderInfoTableList.stream()
                .map(PayOrderInfoTable::getInAmount)
                .reduce((_1,_2)->_1.add(_2))
                .orElse(new BigDecimal(0));
        //****************单笔代付****************
        if(payOrderInfoTableList.size() == 1)
            return totalInAmount;
        //****************多笔代付****************
        String amountStr = merTransOrderApplyDTO.getAmount();
        if( !isBlank(amountStr) ) {
            BigDecimal orderAmount = new BigDecimal( null == amountStr || isBlank(amountStr) ? "0" : amountStr.trim() );
            //代付金额至少 10 RMB
            state(orderAmount.compareTo(TransAmountRiskManageEnum.T_MIN_10.getAmount())  == -1 ,
                    ResponseCodeEnum.RXH00048.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;",
                            ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00048.getMsg(),localPoint),
                    format(" %s",ResponseCodeEnum.RXH00048.getMsg()));
            //代付金额每次交易不超过 10W RMB
            state(orderAmount.compareTo(TransAmountRiskManageEnum.T_MAX_10W.getAmount())  > -1,
                    ResponseCodeEnum.RXH00049.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;",
                            ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00049.getMsg(),localPoint),
                    format(" %s",ResponseCodeEnum.RXH00049.getMsg()));
            //代付金额大于可支付金额
            state(orderAmount.compareTo(totalInAmount) > 0,
                    ResponseCodeEnum.RXH00011.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH00011.getMsg(), localPoint),
                    format(" %s", ResponseCodeEnum.RXH00011.getMsg()));
            //多笔代付必须全额代付
            state(orderAmount.compareTo(totalInAmount) != 0,
                    ResponseCodeEnum.RXH00051.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH00051.getMsg(), localPoint),
                    format(" %s", ResponseCodeEnum.RXH00051.getMsg()));

        }
        return totalInAmount;
    }

    @Override
    public Map<OrganizationInfoTable, List<PayOrderInfoTable>> groupPayOrderByOrganization(List<PayOrderInfoTable> payOrderInfoTableList, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint = "groupPayOrderByOrganization";
        Map<String, List<PayOrderInfoTable>> groupPayOrderByChannel = payOrderInfoTableList.stream().collect(Collectors.groupingBy(PayOrderInfoTable::getChannelId));
        Set<String> channelIdSet = groupPayOrderByChannel.keySet();
        Map<OrganizationInfoTable, List<PayOrderInfoTable>>  organizationInfoTableListMap  = new HashMap<>();;
        try {
            //1.根据订单保存测通道ID获取通道信息
            List<ChannelInfoTable> channelInfoTableList = commonRPCComponent.apiChannelInfoService
                    .batchGetByChannelId(channelIdSet);

            isHasNotElement(channelInfoTableList,
                    ResponseCodeEnum.RXH00022.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH00022.getMsg(), localPoint),
                    format(" %s", ResponseCodeEnum.RXH00022.getMsg()));
            //获取组织id
            Set<String> organizationIdSet = channelInfoTableList.stream()
                    .map(ChannelInfoTable::getOrganizationId).collect(Collectors.toSet());
            //2.根据通道ID获取机构信息
            List<OrganizationInfoTable> organizationInfoTableList = commonRPCComponent.apiOrganizationInfoService
                    .getAll(new OrganizationInfoTable()
                            .setOrganizationIdColl(organizationIdSet)
                            .setStatus(StatusEnum._0.getStatus()));

            isHasNotElement(organizationInfoTableList,
                    ResponseCodeEnum.RXH99995.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99995.getMsg(), localPoint),
                    format(" %s", ResponseCodeEnum.RXH99995.getMsg()));

            //根据组织机构，对通道进行分组
            Map<String, List<ChannelInfoTable>>  groupChannelByOrganization = channelInfoTableList.stream()
                    .collect(Collectors.groupingBy(ChannelInfoTable::getOrganizationId));

            groupChannelByOrganization.forEach((k,v)->{
                //groupChannelByOrganization 对应的 OrganizationInfoTable 信息
                OrganizationInfoTable organizationInfoTable = organizationInfoTableList.stream()
                        .filter(org->org.getOrganizationId().equalsIgnoreCase(k)).findAny().orElse(null);
                //将 ChannelInfoTable 信息简化为 ChannelId
                Set<String> channelIdSet2 = v.stream().map(ChannelInfoTable::getChannelId).collect(Collectors.toSet());
                //保存同机构组织下的订单-1
                List<PayOrderInfoTable>  list = new ArrayList<>();
                payOrderInfoTableList.forEach( payOrderList ->{
                    if(channelIdSet2.contains(payOrderList.getChannelId())) list.add(payOrderList);
                });
                //保存同机构组织下的订单-key:机构信息 ，value 订单列表
                organizationInfoTableListMap.put(organizationInfoTable,list);
            });

        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof NewPayException) {
                NewPayException npe = (NewPayException) e;
                throw npe;
            } else {
                throw new NewPayException(
                        ResponseCodeEnum.RXH99999.getCode(),
                        format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常信息：%s",
                                ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint, e.getMessage()),
                        format(" %s", ResponseCodeEnum.RXH99999.getMsg()));
            }
        }
        isHasNotElement(organizationInfoTableListMap,
                ResponseCodeEnum.RXH99994.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;错误根源：把订单信息，根据机构信息进件分组，发生无法预知异常",
                        ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99994.getMsg(), localPoint),
                format(" %s", ResponseCodeEnum.RXH99994.getMsg()));

        return organizationInfoTableListMap;
    }

    @Override
    public Tuple2<OrganizationInfoTable, ChannelInfoTable> verifyOrderChannelTab(List<PayOrderInfoTable> payOrderInfoTableList,MerTransOrderApplyDTO merTransOrderApplyDTO, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint = "verifyOrderOrganization";
        Map<String, List<PayOrderInfoTable>> groupPayOrderByChannel = payOrderInfoTableList.stream().collect(Collectors.groupingBy(PayOrderInfoTable::getChannelId));
        state(groupPayOrderByChannel.size() != 1 ,
                ResponseCodeEnum.RXH00050.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;",
                        ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH00050.getMsg(), localPoint),
                format(" %s", ResponseCodeEnum.RXH00050.getMsg()));
        OrganizationInfoTable organizationInfoTable = null;
        ChannelInfoTable channelInfoTable2 = null;
        try {
            ChannelInfoTable   channelInfoTable = commonRPCComponent.apiChannelInfoService.getOne(new ChannelInfoTable()
                    .setChannelId(payOrderInfoTableList.get(0).getChannelId()));
            isNull(channelInfoTable,
                    ResponseCodeEnum.RXH00022.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH00022.getMsg(), localPoint),
                    format(" %s", ResponseCodeEnum.RXH00022.getMsg()));
            //2.根据通道ID获取机构信息
            organizationInfoTable = commonRPCComponent.apiOrganizationInfoService.getOne(new OrganizationInfoTable()
                    .setOrganizationId(channelInfoTable.getOrganizationId())
                    .setStatus(StatusEnum._0.getStatus()));

            isNull(organizationInfoTable,
                    ResponseCodeEnum.RXH99995.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99995.getMsg(), localPoint),
                    format(" %s", ResponseCodeEnum.RXH99995.getMsg()));

            channelInfoTable2 = commonRPCComponent.apiChannelInfoService.getOne(new ChannelInfoTable()
                    .setOrganizationId(organizationInfoTable.getOrganizationId())
                    .setBusiType(BusinessTypeEnum.TRANS.getBusiType())
                    .setProductId(merTransOrderApplyDTO.getProductType())
                    .setStatus(StatusEnum._0.getStatus()));

            isNull(channelInfoTable2,
                    ResponseCodeEnum.RXH00022.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH00022.getMsg(), localPoint),
                    format(" %s", ResponseCodeEnum.RXH00022.getMsg()));

        } catch (Exception e) {
            if (e instanceof NewPayException) {
                NewPayException npe = (NewPayException) e;
                throw npe;
            } else {
                e.printStackTrace();
                throw new NewPayException(
                        ResponseCodeEnum.RXH99999.getCode(),
                        format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常信息：%s",
                                ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint, e.getMessage()),
                        format(" %s", ResponseCodeEnum.RXH99999.getMsg()));
            }
        }
        return new Tuple2<>(organizationInfoTable,channelInfoTable2);
    }

    @Override
    public TransOrderInfoTable saveOrder(MerTransOrderApplyDTO merTransOrderApplyDTO, ChannelInfoTable channelInfoTable, MerchantInfoTable merInfoTable, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="saveOrder";
        TransOrderInfoTable transOrderInfoTable =null;
        try {

            AgentMerchantSettingTable   agentMerchantSettingTable = commonRPCComponent.apiAgentMerchantSettingService.getOne(new AgentMerchantSettingTable()
                    .setAgentMerchantId(merInfoTable.getAgentMerchantId())
                    .setProductId(merTransOrderApplyDTO.getProductType())
                    .setStatus(StatusEnum._0.getStatus()));

//            isNull(agentMerchantSettingTable,
//                    ResponseCodeEnum.RXH00044.getCode(),
//                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;异常根源：代理费率配置信息为null",
//                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH00044.getMsg(), localPoint),
//                    format(" %s", ResponseCodeEnum.RXH00044.getMsg()));

            MerchantRateTable  merchantRateTable = commonRPCComponent.apiMerchantRateService.getOne(new MerchantRateTable()
                    .setMerchantId(merTransOrderApplyDTO.getMerId())
                    .setProductId(merTransOrderApplyDTO.getProductType())
                    .setStatus(StatusEnum._0.getStatus()));

            isNull(merchantRateTable,
                    ResponseCodeEnum.RXH00044.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;异常根源：商户费率配置信息为null",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH00044.getMsg(), localPoint),
                    format(" %s", ResponseCodeEnum.RXH00044.getMsg()));


            //订单金额
            BigDecimal amount = new BigDecimal(merTransOrderApplyDTO.getAmount());
            //终端代付费用
            String backFee = merTransOrderApplyDTO.getBackFee();
            BigDecimal terFee = new BigDecimal( null == backFee ? "0" : backFee);
            //实际到帐金额
            BigDecimal outAmount = amount.subtract(terFee).setScale(2,BigDecimal.ROUND_UP);

            //商户费用
            BigDecimal merRateFee = merchantRateTable.getRateFee();
            merRateFee = null== merRateFee ? new BigDecimal(0) : merRateFee;
            merRateFee = merRateFee.divide(new BigDecimal(0));

            BigDecimal merFee = amount.multiply(merRateFee).setScale(2,BigDecimal.ROUND_UP);

            BigDecimal merSingleFee = merchantRateTable.getSingleFee();
            merSingleFee = null == merSingleFee ? new BigDecimal(0) : merSingleFee;

            BigDecimal totalMerFee = merFee.add(merSingleFee).setScale(2,BigDecimal.ROUND_UP);

            //商户收取的费用，不大于终端收取的费用
            state(totalMerFee.compareTo(terFee) == 1,
                    ResponseCodeEnum.RXH00042.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s; 终端费用= %s,商户费用 = %s",
                            ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00042.getMsg(),localPoint,
                            terFee,totalMerFee),
                    format(" %s",ResponseCodeEnum.RXH00042.getMsg()));

            //通道费用
            BigDecimal channelRate = channelInfoTable.getChannelRateFee();
            channelRate = null == channelRate ? new BigDecimal(0) : channelRate;
            channelRate =channelRate.divide(new BigDecimal(100));
            BigDecimal channelFee = amount.multiply(channelRate).setScale(2,BigDecimal.ROUND_UP);
            BigDecimal singleFee = channelInfoTable.getChannelSingleFee();
            singleFee = null == singleFee ? new BigDecimal(0) : singleFee;
            BigDecimal totalChannelFee = channelFee.add(singleFee).setScale(2,BigDecimal.ROUND_UP);

            state(totalChannelFee.compareTo(terFee) == 1,
                    ResponseCodeEnum.RXH00043.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;终端费用 = %s,通道费率 = %s",
                            ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00043.getMsg(),localPoint,
                            terFee,totalChannelFee),
                    format(" %s",ResponseCodeEnum.RXH00043.getMsg()));

            state(totalChannelFee.compareTo(totalMerFee) == 1,
                    ResponseCodeEnum.RXH00044.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;错误问题：通道费用大于平台收取商户的费用；通道费用 = %s,商户费用 = %s",
                            ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00044.getMsg(),localPoint,
                            totalChannelFee,totalMerFee),
                    format(" %s",ResponseCodeEnum.RXH00044.getMsg()));

            BigDecimal merAddChTotalFee = totalMerFee.add(totalChannelFee);
            state(merAddChTotalFee.compareTo(terFee) == 1,
                    ResponseCodeEnum.RXH00044.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;错误问题：通道费用（%s）+商户费用(%s)>终端费用（%s）"
                            ,ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00044.getMsg(),localPoint,
                            totalChannelFee,totalMerFee,terFee),
                    format(" %s",ResponseCodeEnum.RXH00044.getMsg()));

            //代理商费用
            BigDecimal agentTotalFee = null;
            if( !isNull(agentMerchantSettingTable)) {
                BigDecimal agentRateFee = agentMerchantSettingTable.getRateFee();
                agentRateFee = null == agentRateFee ? new BigDecimal(0) : agentRateFee;
                agentRateFee = agentRateFee.divide(new BigDecimal(100));

                BigDecimal agentFee = amount.multiply(agentRateFee).setScale(2, BigDecimal.ROUND_UP);

                BigDecimal agentSingleFee = agentMerchantSettingTable.getSingleFee();
                agentSingleFee = null == agentSingleFee ? new BigDecimal(0) : agentSingleFee;

                agentTotalFee = agentFee.add(agentSingleFee).setScale(2, BigDecimal.ROUND_UP);

                state(agentTotalFee.compareTo(terFee) == 1,
                        ResponseCodeEnum.RXH00044.getCode(),
                        format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;代理商费用 = %s,终端费用 = %s",
                                ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH00044.getMsg(), localPoint,
                                agentTotalFee, terFee),
                        format(" %s", ResponseCodeEnum.RXH00044.getMsg()));

                BigDecimal allTotalFee = merAddChTotalFee.add(agentTotalFee);

                state(allTotalFee.compareTo(terFee) == 1,
                        ResponseCodeEnum.RXH00044.getCode(),
                        format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;错误问题：通道费用（%s）+商户费用(%s)+代理商费用（%s）>终端费用（%s）",
                                ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH00044.getMsg(), localPoint,
                                totalChannelFee, totalMerFee, agentTotalFee, terFee),
                        format(" %s", ResponseCodeEnum.RXH00044.getMsg()));
            }

            //平台收入 = 商户费用-通道费用-代理商费用
            agentTotalFee = null == agentTotalFee ? new BigDecimal(0) : agentTotalFee;
            BigDecimal platformIncome = terFee.subtract(merAddChTotalFee).subtract(agentTotalFee);
            state( platformIncome.compareTo(new BigDecimal(0)) == -1,
                    ResponseCodeEnum.RXH00044.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s; 平台利润为负（%s）=商户费用（%s）-通道费用（%s）-代理商费用（%s）",
                            ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00044.getMsg(),localPoint,
                            platformIncome, totalMerFee,totalChannelFee,agentTotalFee),
                    format(" %s",ResponseCodeEnum.RXH00044.getMsg()));

            transOrderInfoTable = new TransOrderInfoTable()
                    .setId(System.currentTimeMillis())
                    .setPlatformOrderId("RXH" + new Random(System.currentTimeMillis()).nextInt(1000000) + "-B11" + System.currentTimeMillis())
                    .setMerchantId(merTransOrderApplyDTO.getMerId())                              .setTerminalMerId(merTransOrderApplyDTO.getTerMerId())
                    .setMerOrderId(merTransOrderApplyDTO.getMerOrderId())                         .setOrgMerOrderId(merTransOrderApplyDTO.getOrgMerOrderId())
                    .setIdentityType(Integer.valueOf(merTransOrderApplyDTO.getIdentityType()))    .setIdentityNum(merTransOrderApplyDTO.getIdentityNum())
                    .setBankCode(merTransOrderApplyDTO.getBankCode())                             .setBankCardType(Integer.valueOf(merTransOrderApplyDTO.getBankCardType()))
                    .setBankCardNum(merTransOrderApplyDTO.getBankCardNum())                       .setBankCardPhone(merTransOrderApplyDTO.getBankCardPhone())
                    .setChannelId(channelInfoTable.getChannelId())                                .setBusiType(BusinessTypeEnum.b11.getBusiType())
                    .setProductId(channelInfoTable.getProductId())                                .setProductFee(new BigDecimal(0))
                    .setCurrency(merTransOrderApplyDTO.getCurrency())                             .setAmount(amount)
                    .setOutAmount(outAmount)                                                      .setBackFee(terFee)
                    .setTerRate(new BigDecimal(0))                                            .setTerFee(terFee)
                    .setChannelRate(channelInfoTable.getChannelRateFee())                         .setChannelFee(totalChannelFee)
                    .setAgentRate(agentMerchantSettingTable.getRateFee())                         .setAgentFee(agentTotalFee)
                    .setMerRate(merchantRateTable.getRateFee())                                   .setMerFee(totalMerFee)
                    .setPlatformIncome(platformIncome)
                    .setSettleCycle(merchantRateTable.getSettleCycle())                           .setSettleStatus(StatusEnum._2.getStatus())
                    .setChannelRespResult(null)                                                   .setCrossRespResult(null)
                    .setStatus(StatusEnum._2.getStatus())
                    .setCreateTime(new Date())                                                    .setUpdateTime(new Date());

            commonRPCComponent.apiTransOrderInfoService.save(transOrderInfoTable);
        }catch (Exception e){
            if (e instanceof NewPayException) {
                NewPayException npe = (NewPayException) e;
                throw npe;
            }else{
                e.printStackTrace();
                throw new NewPayException(
                        ResponseCodeEnum.RXH99999.getCode(),
                        format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：申代付时，保存订单前，查询商户代理商信息/查询商户费率设置信息/保存订单信息时发生异常,异常信息：%s",
                                ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                        format(" %s", ResponseCodeEnum.RXH99999.getMsg())
                );
            }
        }
        return transOrderInfoTable;
    }

    @Override
    public TransOrderInfoTable updateOrder(TransOrderInfoTable transOrderInfoTable, String crossResponseMsg, CrossResponseMsgDTO crossResponseMsgDTO, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="updateOrder";
        transOrderInfoTable.setCrossRespResult(crossResponseMsg)
                .setChannelRespResult(crossResponseMsgDTO.getChannelResponseMsg())
                .setChannelOrderId(crossResponseMsgDTO.getChannelOrderId())
                .setStatus(crossResponseMsgDTO.getCrossStatusCode());
        if(crossResponseMsgDTO.getCrossStatusCode() == StatusEnum._0.getStatus()){
            transOrderInfoTable.setStatus(StatusEnum._7.getStatus());
            return transOrderInfoTable;
        }
        try {
            commonRPCComponent.apiTransOrderInfoService.updateById(transOrderInfoTable);
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：申请代付时，更新订单时发生异常,异常信息：%s",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }
        return  transOrderInfoTable;
    }

    @Override
    public List<PayOrderInfoTable> updatePayOrderStatus(List<PayOrderInfoTable> payOrderInfoTableList) {
        return payOrderInfoTableList.stream().map(t-> t.setStatus(StatusEnum._9.getStatus())).collect(Collectors.toList());
    }

    @Override
    public void batchUpdateTransOderCorrelationInfo(TransOrderInfoTable transOrderInfoTable, List<PayOrderInfoTable> payOrderInfoTableList, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="batchUpdateTransOderCorrelationInfo";
        try {
            commonRPCComponent.apiTransOrderBusinessTransactionService.updateByPayOrderCorrelationInfo(transOrderInfoTable, payOrderInfoTableList);
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：申请代付时，批量事务更新发生未知错误,异常信息：%s",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }
    }
}
