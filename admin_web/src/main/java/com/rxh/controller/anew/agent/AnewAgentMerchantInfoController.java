package com.rxh.controller.anew.agent;

import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.table.agent.AgentMerchantInfoTable;
import com.rxh.service.AnewAgentMerchantService;
import com.rxh.service.ConstantService;
import com.rxh.service.square.AgentMerchantInfoService;
import com.rxh.spring.annotation.SystemLogInfo;
import com.rxh.utils.GlobalConfiguration;
import com.rxh.utils.SystemConstant;
import com.rxh.vo.ResponseVO;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/agentMerchantInfo")
public class AnewAgentMerchantInfoController {

    @Resource
    private AgentMerchantInfoService agentMerchantInfoService;

    @Resource
    private GlobalConfiguration globalConfiguration;

    @Autowired
    private  ConstantService constantService;
    @Autowired
    private AnewAgentMerchantService anewAgentMerchantService;

    @RequestMapping(value = "/uploadAgentIdentityUrl")
    @ResponseBody
    public String uploadMerchantCertificate(@RequestParam("file") MultipartFile multipartFiles, @RequestParam("agentMerchantId") String agentMerchantId) throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String certificatePath = globalConfiguration.getFileUploadPath() + agentMerchantId + "/";
        File filePath = new File(certificatePath);
        if (!filePath.exists()) {
            filePath.mkdir();
        }
        certificatePath += dateFormat.format(new Date()) + multipartFiles.getOriginalFilename().substring(multipartFiles.getOriginalFilename().indexOf("."));
        File file = new File(certificatePath);
        multipartFiles.transferTo(file);
        return certificatePath.replace(globalConfiguration.getFileUploadPath(), "");
    }


    @RequestMapping(value = "/isAgentMerchantIdExist")
    @ResponseBody
    public Boolean isAgentMerchantIdExist(@RequestBody String agentMerchantId) {
        return agentMerchantInfoService.isAgentMerchantIdExist(agentMerchantId);
    }

    @SystemLogInfo(description = "代理商列表查询")
    @RequestMapping(value = "/getAllByVoAgentMerchantInfo")
    @ResponseBody
    public ResponseVO getAllByVoAgentMerchantInfo(@RequestBody AgentMerchantInfoTable agentMerchantInfoTable) {
        try {
            return anewAgentMerchantService.list(agentMerchantInfoTable);
        }catch (Exception e){
            e.printStackTrace();
            ResponseVO responseVO = new ResponseVO();
            responseVO.setCode(StatusEnum._1.getStatus());
            responseVO.setMessage("失败");
            return responseVO;
        }
    }

    @SystemLogInfo(description = "代理商删除")
    @RequestMapping(value = "/delete")
    @ResponseBody
    public String delete(@RequestBody String id){
        int num = agentMerchantInfoService.deleteAgentMerchantInfo(id);
        if(num>0){
            return SystemConstant.SUCCESS;
        }else {
            return SystemConstant.FAIL;
        }
    }

    @SystemLogInfo(description = "代理商更新")
    @RequestMapping(value = "/updateAgentMerchantInfo")
    @ResponseBody
    public ResponseVO updateAgentMerchantInfo(@RequestBody AgentMerchantInfoTable agentMerchantInfo){
        try {
            return anewAgentMerchantService.update(agentMerchantInfo);
        }catch (Exception e){
            e.printStackTrace();
            ResponseVO responseVO = new ResponseVO();
            responseVO.setCode(StatusEnum._1.getStatus());
            responseVO.setMessage("失败");
            return responseVO;
        }
    }

    @SystemLogInfo(description = "代理商新增")
    @RequestMapping(value = "/addAgentMerchantInfo")
    @ResponseBody
    public ResponseVO addAgentMerchantInfo(@RequestBody AgentMerchantInfoTable agentMerchantInfo){
        try {
            agentMerchantInfo.setCreateTime(new Date());
            agentMerchantInfo.setUpdateTime(new Date());
            agentMerchantInfo.setAgentMerchantId("A"+System.currentTimeMillis());
            return anewAgentMerchantService.save(agentMerchantInfo);
        }catch (Exception e){
            e.printStackTrace();
            ResponseVO responseVO = new ResponseVO();
            responseVO.setCode(StatusEnum._1.getStatus());
            responseVO.setMessage("失败");
            return responseVO;
        }

    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @SystemLogInfo(description = "代理商删除")
    @RequestMapping("/batchDel")
    @ResponseBody
    public ResponseVO batchDel(@RequestBody List<String> ids) {
        try {
            return anewAgentMerchantService.delByIds(ids);
        }catch (Exception e){
            e.printStackTrace();
            ResponseVO responseVO = new ResponseVO();
            responseVO.setCode(StatusEnum._1.getStatus());
            responseVO.setMessage("失败");
            return responseVO;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/certificateImg", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getImageAsResource(String certificate) throws IOException {
        File img = new File(globalConfiguration.getFileUploadPath() + certificate);
        if (img.exists()) {
            FileInputStream inputStream = new FileInputStream(img);
            byte[] media = IOUtils.toByteArray(inputStream);
            HttpHeaders headers = new HttpHeaders();
            headers.setCacheControl(CacheControl.noCache().getHeaderValue());
            return new ResponseEntity<>(media, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
        }
    }
    /**
     * 获取id
     */
    @ResponseBody
    @RequestMapping(value = "/getAgentMerchantIdIncre", method = RequestMethod.GET)
    public String getAgentMerchantIdIncre(){
        String agentMerchantId = agentMerchantInfoService.getMaxAgentMerchantId();
        return agentMerchantId;
    }

    @RequestMapping("/init")
    public Map<String, Object> init() {
        Map<String, Object> init = new HashMap<>();
        init.put("identityType", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.IDENTITYTYPE));
        init.put("agentMerchants", agentMerchantInfoService.getAllIdAndName());
        return init;
    }

}