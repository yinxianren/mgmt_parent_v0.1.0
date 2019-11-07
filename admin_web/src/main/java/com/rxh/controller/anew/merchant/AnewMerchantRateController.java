package com.rxh.controller.anew.merchant;

import com.rxh.anew.table.merchant.MerchantRateTable;
import com.rxh.enums.StatusEnum;
import com.rxh.service.AnewMerchantRateService;
import com.rxh.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/merchantSquareRate")
public class AnewMerchantRateController {

    @Autowired
    private AnewMerchantRateService anewMerchantRateService;

    @RequestMapping("/search")
    public ResponseVO search(@RequestBody MerchantRateTable merchantRateTable){
        try {
            return  anewMerchantRateService.getList(merchantRateTable);
        }catch (Exception e){
            e.printStackTrace();
            ResponseVO responseVO = new ResponseVO();
            responseVO.setCode(StatusEnum._1.getStatus());
            responseVO.setMessage("失败");
            return responseVO;
        }
    }

    @RequestMapping("/update")
    public ResponseVO update(@RequestBody  List<MerchantRateTable> param ){
        try {
            return anewMerchantRateService.batchUpdate(param);
        }catch (Exception e){
            e.printStackTrace();
            ResponseVO responseVO = new ResponseVO();
            responseVO.setCode(StatusEnum._1.getStatus());
            responseVO.setMessage("失败");
            return responseVO;
        }

    }

    @RequestMapping("/init")
    public ResponseVO init(){
        try {
            return anewMerchantRateService.init();
        }catch (Exception e){
            e.printStackTrace();
            ResponseVO responseVO = new ResponseVO();
            responseVO.setCode(StatusEnum._1.getStatus());
            responseVO.setMessage("失败");
            return responseVO;
        }

    }
}
