package com.rxh.service.impl.trading;


import com.alibaba.fastjson.JSONObject;
import com.rxh.mapper.square.*;
import com.rxh.pojo.Result;
import com.rxh.pojo.base.Page;
import com.rxh.pojo.base.PageResult;
import com.rxh.pojo.base.SearchInfo;
import com.rxh.service.trading.TransOrderService;
import com.rxh.square.pojo.BatchData;
import com.rxh.square.pojo.TransBankInfo;
import com.rxh.square.pojo.TransOrder;
import com.rxh.square.pojo.TransProductDetail;
import com.rxh.utils.HttpClientUtils;
import com.rxh.utils.JsonUtils;
import com.rxh.utils.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @ClassName : TransOrderServiceImpl
 * @Author : zoe
 * @Date : 2019/5/19 16:01
 */
@Service
public class TransOrderServiceImpl implements TransOrderService {

    @Value("${HyPay.payUrl}")
    private String payurl;
    @Autowired
    private TransOrderMapper transOrderMapper;
    @Autowired
    private TransBankInfoMapper transBankInfoMapper;
    @Autowired
    private TransProductDetailMapper transProductDetailMapper;
    @Autowired
    private BatchRepayMapper batchRepayMapper;
    @Resource
    MerchantSquareInfoMapper merchantSquareInfoMapper;

    private  Logger logger = LoggerFactory.getLogger(TransOrderServiceImpl.class);

    @Override
    public PageResult findTransOrder(Page page) {
        try {
            int startPage = page.getPageNum()*page.getPageSize();
//        int startPage =0;
            int pageSize = page.getPageSize();
            SearchInfo searchInfo = page.getSearchInfo();
            Map<String, Object> paramMap = JsonUtils.objectToMap(searchInfo);
//        Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("startPage", startPage);
            paramMap.put("pageSize", pageSize);
            List<TransOrder> list;
            int totalCount = 0;
            BigDecimal totalMoney = new BigDecimal(0);
            BigDecimal totalFee = new BigDecimal(0);
            BigDecimal totalChannelFee = new BigDecimal(0);
            //totalCount  sql  方法
            List<TransOrder> transOrderList= transOrderMapper.selectSuccessOrderByParam(paramMap);
            if (!CollectionUtils.isEmpty(transOrderList)){
                totalCount = transOrderList.size();
            }
            for (TransOrder transOrder : transOrderList){
                totalMoney = totalMoney.add(transOrder.getAmount());
                totalFee = totalFee.add(transOrder.getFee());
                totalChannelFee = totalChannelFee.add(transOrder.getChannelFee());
            }
            Map map = new HashMap();
            map.put("totalMoney",totalMoney.setScale(2).toString());
            map.put("totalFee",totalFee.setScale(2).toString());
            map.put("totalChannelFee",totalChannelFee.setScale(2).toString());
            int allPage = (totalCount + pageSize - 1) / pageSize;
            list = transOrderMapper.selectOrderByParamMap(paramMap);
            // PageInfo<TransOrderDto> pageInfo = new PageInfo<>(list);
            PageResult pageResult = new PageResult();
            pageResult.setRows(list);
            pageResult.setTotal(totalCount);
            pageResult.setAllPage(allPage);
            pageResult.setCustomize(map);
            // 返回结果
            return pageResult;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public Result getTransBankInfo(String transId) {
        Result<TransBankInfo> result = new Result<TransBankInfo>();
        TransBankInfo bankInfo=  transBankInfoMapper.getTransBankInfo(transId);
        if (bankInfo!=null){
            result.setCode(Result.SUCCESS);
            result.setMsg("获取订单详情成功");
            result.setData(bankInfo);
        }else {
            result.setCode(Result.FAIL);
            result.setMsg("获取订单详情失败");
        }


        return result;
    }

    @Override
    public Result getProductInfo(String transId) {
        Result<TransProductDetail> result = new Result<>();
        TransProductDetail productInfo = transProductDetailMapper.getProductInfo(transId);
        if (productInfo!=null){
            result.setCode(Result.SUCCESS);
            result.setMsg("获取产品详情成功");
            result.setData(productInfo);
        }else {
            result.setCode(Result.FAIL);
            result.setMsg("获取产品详情失败");
        }
        return result;
    }

    @Override
    public TransOrder getTransOrderByPrimaryId(String transId) {
        return transOrderMapper.selectByPrimaryKey(transId);
    }


    @Override
    public TransOrder selectById(String orderId) {
        return transOrderMapper.selectByPrimaryKey(orderId);
    }

    @Override
    public List<TransOrder> getTransOrder(String channelId, Integer orderStatus,String bigenDate, String endDate) {
        Assert.notNull(channelId,"查询订单，通道id为空！");
        return transOrderMapper.getTransOrder(channelId,orderStatus,bigenDate,endDate);
    }

    @Override
    public TransOrder getTransOrderByMerOrderIdAndMerId(String merId, String merOrderId) {

        return transOrderMapper.getTransOrderByMerOrderIdAndMerId(merId,merOrderId);
    }

    /**
     * 获取初始查询信息
     *
     * @return 初始查询信息
     */
    @Override
    public Map<String, Object> getBatchRepayInit(String merId) {
        Map<String, Object> init = new HashMap<>();
        init.put("batchDatas", batchRepayMapper.selectAllBatchData(merId));
        return init;
    }

    /**
     * 获取分页数据
     *
     * @param page 分页信息
     * @return 分页数据
     */
    @Override
    public PageResult getBatchRepayList(Page page) {
        try {
            int startPage = page.getPageNum()*page.getPageSize();
            int pageSize = page.getPageSize();
            SearchInfo searchInfo = page.getSearchInfo();
            Map<String, Object> paramMap = JsonUtils.objectToMap(searchInfo);
            paramMap.put("startPage",startPage);
            paramMap.put("pageSize", pageSize);
            paramMap.put("merId",searchInfo.getMerId());
            List<BatchData> list;
            int totalCount;
            totalCount = batchRepayMapper.selectBatchDataCountByParam(paramMap);
            int allPage = (totalCount + pageSize - 1) / pageSize;
            list = batchRepayMapper.selectAllBatchDataByParam(paramMap);
            PageResult pageResult = new PageResult();
            pageResult.setRows(list);
            pageResult.setTotal(totalCount);
            pageResult.setAllPage(allPage);
            return pageResult;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 批量代付
     *
     * @param orderChanges 变更对象集合
     * @param type         变更类型
     */
    @Override
    public int batchRepay(List<BatchData> orderChanges, String type,String merId) {
        switch (type) {

            case "批量代付":
                return refundBatchOperation(orderChanges,merId);
              //  orderChangeMapper.insertBatch(orderChanges);

            default:
                break;
        }
        return 1;
    }

    @Override
    public List<TransOrder> getTransOrderByWhereCondition(TransOrder transOrder) {
        return transOrderMapper.getTransOrderByWhereCondition(transOrder);
    }

    @Override
    public int updateByPrimaryKey(TransOrder record) {
        return transOrderMapper.updateByPrimaryKey(record);
    }

    @Override
    public int insertBean(TransOrder transOrder) {
        return transOrderMapper.insertSelective(transOrder);
    }

    @Override
    public List<TransOrder> selectByMap(Map<String, Object> paramMap) {
        return transOrderMapper.selectByParam(paramMap);
    }

    /**
     * 代付批量操作
     *
     * @param orderChanges 变更对象集合
     */
    public int refundBatchOperation(List<BatchData> orderChanges,String merId) {
        List list = new ArrayList();
        String key =merchantSquareInfoMapper.selectByPrimaryKey(merId).getSecretKey();



        //获取文件中的订单号判断是否重复
        for (BatchData oc : orderChanges) {
            list.add(oc.getMerOrderId());
        }
        HashSet set = new HashSet<>(list);
        if(set.size()==list.size()){
            //判断订单号与数据库中是否重复
            List ls = new ArrayList();
            for (BatchData oc : batchRepayMapper.selectMerOrder(merId)) {
                ls.add(oc.getMerOrderId());
            }
            ls.addAll(list);
            HashSet set1 = new HashSet<>(ls);
            if(set1.size()==ls.size()){

        BatchData bd =new BatchData();
        for (BatchData oc : orderChanges) {

            try {
                Map<String, Object> map = new TreeMap<>();
                map.put("bizType","4");
                map.put("charset","utf-8");
                map.put("signType","MD5");
                map.put("merId",merId);
                map.put("merOrderId",oc.getMerOrderId());
                map.put("amount",oc.getAmount());
                map.put("bankCode",oc.getBankCode());
                map.put("inAcctNo",oc.getInAcctNo());
                map.put("inAcctName",oc.getInAcctName());
                map.put("returnUrl",oc.getReturnUrl());
                map.put("noticeUrl",oc.getNoticeUrl());
                map.put("remark",oc.getRemark());
                map.put("signMsg", getMd5SignWithKey(map,key));
                String tradeInfo = JsonUtils.objectToJson(map);
                tradeInfo= URLEncoder.encode(tradeInfo,"UTF-8");
                String postJson= new String( Base64.getEncoder().encode(tradeInfo.getBytes()),"UTF-8");
               String result = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), payurl, postJson);
                bd.setMerId(merId);
                bd.setMerOrderId(oc.getMerOrderId());
                bd.setAmount(oc.getAmount());
                bd.setBankCode(oc.getBankCode());
                bd.setInAcctNo(oc.getInAcctNo());
                bd.setInAcctName(oc.getInAcctName());
                bd.setReturnUrl(oc.getReturnUrl());
                bd.setNoticeUrl(oc.getNoticeUrl());
                bd.setRemark(oc.getRemark());
                bd.setCreateTime(new Date());
              if(result!=null) {
                  JSONObject resultMap = JSONObject.parseObject(result);
                  bd.setOrderId(resultMap.getString("orderId"));
                  bd.setStatus(Integer.parseInt(resultMap.getString("status")));
              }else {
                  bd.setOrderId("");
                  bd.setStatus(1);
              }

              batchRepayMapper.insertSelective(bd);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return 1;
            }
            // 获取订单信息

        }

            }else{

                return 3;
            }
        }else {
            return 2;
        }
         return 0;
    }

    public static String getMd5SignWithKey(Map<String,Object> param,String md5Key)  {
        StringBuffer sb = new StringBuffer();
        for (String key : param.keySet()){
            String value = param.get(key) != null ? param.get(key).toString() : null;
            if(!StringUtils.isEmpty(value)){
                sb.append(key+"="+value+"&");
            }
        }
        String sign = sb.substring(0,sb.lastIndexOf("&"))+md5Key;
        String md5Sign=  DigestUtils.md5Hex(sign.getBytes(StandardCharsets.UTF_8)).toUpperCase();
        return md5Sign;
    }
}

