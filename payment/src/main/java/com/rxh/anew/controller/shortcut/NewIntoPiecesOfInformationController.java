package com.rxh.anew.controller.shortcut;

import com.alibaba.dubbo.common.json.JSON;
import com.rxh.anew.component.Md5Component;
import com.rxh.anew.controller.NewAbstractCommonController;
import com.rxh.anew.dto.MerchantBasicInformationRegistrationDTO;
import com.rxh.anew.inner.InnerPrintLogObject;
import com.rxh.anew.inner.ParamRule;
import com.rxh.anew.service.shortcut.NewIntoPiecesOfInformationService;
import com.rxh.anew.table.channel.ChannelInfoTable;
import com.rxh.anew.table.merchant.MerchantInfoTable;
import com.rxh.anew.table.system.MerchantSettingTable;
import com.rxh.anew.table.system.SystemOrderTrackTable;
import com.rxh.utils.CheckMd5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/18
 * Time: 下午2:40
 * Description:
 */

@RestController
@RequestMapping("/shortcut")
public class NewIntoPiecesOfInformationController extends NewAbstractCommonController {

    @Autowired
    private NewIntoPiecesOfInformationService newIntoPiecesOfInformationService;
    @Autowired
    private Md5Component md5Component;

    /**
     *
     * @param request
     * @param param
     * @return
     *
     *
     */
    @PostMapping(value = "/addCusInfo" ,produces = "text/html;charset=UTF-8")
    public String intoPiecesOfInformation(HttpServletRequest request, @RequestBody(required = false) String param){
        final String bussType = "【基本信息登记】";
        String respResult=null;
        SystemOrderTrackTable sotTable = null;
        MerchantBasicInformationRegistrationDTO mbirDTO=null;
        try{
            //解析 以及 获取SystemOrderTrackTable对象
            sotTable = this.getSystemOrderTrackTable(request,param,bussType);
            //类型转换
            mbirDTO = JSON.parse(sotTable.getRequestMsg(),MerchantBasicInformationRegistrationDTO.class);
            //获取必要参数
            Map<String, ParamRule> paramRuleMap =newIntoPiecesOfInformationService.getParamMapByIPOI();
            //创建日志打印对象
            InnerPrintLogObject ipo = new InnerPrintLogObject(mbirDTO.getMerId(),mbirDTO.getMerOrderId(),bussType);
            //参数校验
            this.verify(paramRuleMap,mbirDTO,MerchantBasicInformationRegistrationDTO.class,ipo);
            //获取商户信息
            MerchantInfoTable merInfoTable = newIntoPiecesOfInformationService.getOneMerInfo(ipo);
             //验证签名
            md5Component.checkMd5(sotTable.getRequestMsg(),merInfoTable.getSecretKey(),ipo);
            //查看是否重复订单
            newIntoPiecesOfInformationService.multipleOrder(mbirDTO.getMerOrderId(),ipo);
            // 获取商户配置
            List<MerchantSettingTable>  merchantSettingTableList=newIntoPiecesOfInformationService.getMerchantSetting(ipo);
            //获取配置的所有通道
            List<ChannelInfoTable>  channelInfoTableList = newIntoPiecesOfInformationService.getChannelInfoByMerSetting(merchantSettingTableList,ipo);
            //根据产品类型进行过滤
            Set<ChannelInfoTable>  filterChannelInfoTableSet=newIntoPiecesOfInformationService.filtrationChannelInfoByProductType(channelInfoTableList,mbirDTO.getProductType(),ipo);
            //获取商户成功进件的信息


        }catch (Exception e){


        }finally {


            return respResult;
        }
    }



}
