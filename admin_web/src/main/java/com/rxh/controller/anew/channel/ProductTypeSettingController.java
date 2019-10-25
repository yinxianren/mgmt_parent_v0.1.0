package com.rxh.controller.anew.channel;

import com.rxh.anew.table.system.ProductSettingTable;
import com.rxh.pojo.sys.SysConstant;
import com.rxh.service.ConstantService;
import com.rxh.service.ProductTypeSettingService;
import com.rxh.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/product")
public class ProductTypeSettingController {

    @Autowired
    private ProductTypeSettingService productTypeSettingService;
    @Autowired
    private ConstantService constantService;

    @RequestMapping(value = "/getProductList")
    public ResponseVO getProductTypeList(ProductSettingTable productSettingTable){
        log.info("请求获取机构支付产品接口");
        try {
            return productTypeSettingService.selectByOrganizationId(productSettingTable);
        }catch (Exception e){
            log.error(e.getMessage());
            ResponseVO responseVO = new ResponseVO();
            responseVO.setCode(1);
            responseVO.setMessage("失败");
            return  responseVO;
        }

    }

    @RequestMapping(value = "addProduct")
    public ResponseVO addProduct(@RequestBody ProductSettingTable productSettingTable){
        log.info("请求支付产品新增修改接口");
        try {
            return  productTypeSettingService.addProduct(productSettingTable);
        }catch (Exception e){
            log.error(e.getMessage());
            ResponseVO responseVO = new ResponseVO();
            responseVO.setCode(1);
            responseVO.setMessage("失败");
            return  responseVO;
        }

    }

    @RequestMapping(value = "/getProductAll")
    public ResponseVO getProductTypeAll(String id){
        log.info("请求获取所有支付产品接口");
        try {
            List<SysConstant> list = constantService.getConstantByGroupName("productType");
            ResponseVO responseVO = new ResponseVO();
            responseVO.setCode(0);
            responseVO.setData(list);
            return responseVO;
        }catch (Exception e){
            log.error(e.getMessage());
            ResponseVO responseVO = new ResponseVO();
            responseVO.setCode(1);
            responseVO.setMessage("失败");
            return  responseVO;
        }

    }



}
