package com.rxh.allinpay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rxh.anew.dto.RequestCrossMsgDTO;
import com.rxh.anew.table.business.RegisterCollectTable;
import com.rxh.anew.table.channel.ChannelInfoTable;
import com.rxh.enums.StatusEnum;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.merchant.MerchantRegisterInfo;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.square.pojo.ChannelInfo;
import com.rxh.utils.AlinPayUtils;
import com.rxh.utils.HttpClientUtils;
import com.rxh.utils.SystemConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.TreeMap;

/**
 * 余额查询 /balanceQuery
 */
@Controller
@RequestMapping("/allinPay")
public class AllinPayBalance {

    private Logger logger = LoggerFactory.getLogger(AllinPayBalance.class);

    @RequestMapping("/balanceQuery")
    @ResponseBody
    public BankResult balance(@RequestBody RequestCrossMsgDTO trade) throws UnsupportedEncodingException {
        BankResult bankResult = new BankResult();
        Map<String, Object> bondParam = getBondParam(trade);
        String others = trade.getChannelInfoTable().getChannelParam();
        JSONObject json = JSON.parseObject(others);
        logger.info("余额查询请求参数："+JSONObject.toJSONString(bondParam));
        // String content = HttpClientUtils.doPost(HttpClientUtils.getHttpsClient(), "https://ipay.allinpay.com/apiweb/acct/balance", bondParam); //生产环境
        String content = HttpClientUtils.doPost(HttpClientUtils.getHttpsClient(), json.getString("balanceQueryUrl"), bondParam);// 测试环境
        logger.info("余额查询银行返回result："+content);
        JSONObject result = (JSONObject) JSON.parse(content);
        String resultCode = result.getString("retcode");
        if("SUCCESS".equals(resultCode)){
            bankResult.setStatus(StatusEnum._0.getStatus());
            bankResult.setBankResult("余额查询成功");
            bankResult.setBankData(result.getString("balance"));
            bankResult.setParam(content);
        }else {
            bankResult.setStatus(StatusEnum._1.getStatus());
            bankResult.setBankResult("余额查询失败");
            bankResult.setParam(content);
        }
        return bankResult;
    }


    private Map<String, Object> getBondParam(RequestCrossMsgDTO trade) throws UnsupportedEncodingException {
        ChannelInfoTable channelInfo = trade.getChannelInfoTable();
        RegisterCollectTable merchantRegisterInfo = trade.getRegisterCollectTable();
        JSONObject registerInfo = JSON.parseObject(merchantRegisterInfo.getChannelRespResult());
        JSONObject param = JSON.parseObject(channelInfo.getChannelParam());
        Map<String, Object> postData = new TreeMap<>();
        String orgid = param.getString("orgid");// 公共必填
        String appid = param.getString("appid");// 公共必填
        String cusid = registerInfo.getString("cusid"); //商户号
        postData.put("appid",appid);
        postData.put("cusid",cusid);
        postData.put("orgid",orgid);
        postData.put("randomstr", AlinPayUtils.getRandomSecretkey());
        postData.put("key", param.getString("key"));
        postData.put("sign",AlinPayUtils.getMd5Sign(postData));
        return postData;
    }

}
