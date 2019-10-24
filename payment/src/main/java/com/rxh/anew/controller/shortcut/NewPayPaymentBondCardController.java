package com.rxh.anew.controller.shortcut;

import com.rxh.anew.component.Md5Component;
import com.rxh.anew.controller.NewAbstractCommonController;
import com.rxh.anew.dto.CrossResponseMsgDTO;
import com.rxh.anew.dto.MerchantBasicInformationRegistrationDTO;
import com.rxh.anew.dto.MerchantBondCardApplyDTO;
import com.rxh.anew.dto.RequestCrossMsgDTO;
import com.rxh.anew.inner.InnerPrintLogObject;
import com.rxh.anew.service.shortcut.NewPayPaymentBondCardService;
import com.rxh.anew.table.merchant.MerchantInfoTable;
import com.rxh.anew.table.system.SystemOrderTrackTable;
import com.rxh.enums.ResponseCodeEnum;
import com.rxh.enums.StatusEnum;
import com.rxh.exception.NewPayException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/23
 * Time: 下午9:51
 * Description:
 */
@AllArgsConstructor
@RestController
@RequestMapping("/shortcut")
public class NewPayPaymentBondCardController  extends NewAbstractCommonController {

    private  final  Md5Component md5Component;
    private final   NewPayPaymentBondCardService newPayPaymentBondCardService;


    /**
     *  绑卡申请接口
     * @param request
     * @param param
     * @return
     */
    @RequestMapping(value = "/bondCard", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String bondCard(HttpServletRequest request, @RequestBody(required = false) String param){
        final String bussType = "【基本信息登记】";
        String errorMsg = null,errorCode = null,printErrorMsg,respResult=null;
        SystemOrderTrackTable sotTable = null;
        MerchantBondCardApplyDTO mbcaDTO=null;
        MerchantInfoTable merInfoTable = null;
        RequestCrossMsgDTO requestCrossMsgDTO = null;
        CrossResponseMsgDTO crossResponseMsgDTO = null;
        InnerPrintLogObject ipo = null ;
        try{

        }catch (Exception e){
            if(e instanceof NewPayException){
                NewPayException npe = (NewPayException) e;
                errorMsg = npe.getResponseMsg();
                printErrorMsg = npe.getInnerPrintMsg();
                errorCode = npe.getCode();
            }else{
                e.printStackTrace();
                errorMsg = ResponseCodeEnum.RXH99999.getMsg();
                printErrorMsg = isBlank(e.getMessage()) ? "" : (e.getMessage().length()>=512 ? e.getMessage().substring(0,526) : e.getMessage());
                errorCode = ResponseCodeEnum.RXH99999.getCode();
            }
            respResult = newPayPaymentBondCardService.responseMsg(null != mbcaDTO ? mbcaDTO.getMerOrderId() : null ,merInfoTable,requestCrossMsgDTO,crossResponseMsgDTO,errorCode,errorMsg,ipo);
            sotTable.setPlatformPrintLog(printErrorMsg).setTradeCode( StatusEnum._1.getStatus());
        }finally {
            sotTable.setResponseResult(respResult).setCreateTime(new Date());
            newPayPaymentBondCardService.saveSysLog(sotTable);
            return null == respResult ? "系统内部错误！" : respResult;
        }
    }
}
