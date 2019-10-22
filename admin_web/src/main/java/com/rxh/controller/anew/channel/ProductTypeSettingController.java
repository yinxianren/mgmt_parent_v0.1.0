package com.rxh.controller.anew.channel;

import com.rxh.anew.table.channel.ProductSettingTable;
import com.rxh.service.ProductTypeSettingService;
import com.rxh.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/product")
public class ProductTypeSettingController {

    @Autowired
    private ProductTypeSettingService productTypeSettingService;

    @RequestMapping(value = "/getProductList")
    public ResponseVO getProductTypeList(String id){
        log.info("请求获取结构产品接口");
        try {
            return productTypeSettingService.selectByOrganizationId(id);
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
        log.info("请求产品新增修改接口");
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



}
