package com.rxh.controller;

import com.internal.playment.common.enums.SystemConstant;
import com.internal.playment.common.page.GlobalConfiguration;
import com.internal.playment.common.page.ResponseVO;
import com.internal.playment.common.table.agent.AgentMerchantInfoTable;
import com.internal.playment.common.table.agent.AgentMerchantSettingTable;
import com.rxh.service.agent.AnewAgentMerchantService;
import com.rxh.service.agent.AnewAgentMerchantSettingService;
import com.rxh.service.system.NewSystemConstantService;
import com.rxh.spring.annotation.SystemLogInfo;
import com.rxh.util.UserInfoUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/agentMerchantInfo")
public class AgentMerchantInfoController {

    @Resource
    private AnewAgentMerchantService agentMerchantInfoService;

    @Resource
    private GlobalConfiguration globalConfiguration;

    @Autowired
    private NewSystemConstantService constantService;
    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AnewAgentMerchantSettingService agentMerchantSettingService;

    @SystemLogInfo(description = "代理商信息获取")
    @ResponseBody
    @RequestMapping("/getAgentInfo")
    public List<AgentMerchantInfoTable> getAgentInfo(){
        String merchantId = UserInfoUtils.getMerchantId();
       List<AgentMerchantInfoTable> agentMerchantInfos = new ArrayList<>();
       AgentMerchantInfoTable agentMerchantInfoTable = new AgentMerchantInfoTable();
       agentMerchantInfoTable.setAgentMerchantId(merchantId);
       agentMerchantInfos.addAll((List)agentMerchantInfoService.list(agentMerchantInfoTable).getData());

        return agentMerchantInfos;
    }

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
        return false;
    }

    @RequestMapping(value = "/getAllByVoAgentMerchantInfo")
    @ResponseBody
    public List<AgentMerchantInfoTable> getAllByVoAgentMerchantInfo(@RequestBody VoAgentMerchantInfo voAgentMerchantInfo) {
        List<AgentMerchantInfoTable> agentMerchantInfos = agentMerchantInfoService.getAllByVoAgentMerchantInfo(voAgentMerchantInfo);
        return agentMerchantInfos;
    }

    @RequestMapping(value = "/delete")
    @ResponseBody
    public String delete(@RequestBody String id){
        ResponseVO responseVO = agentMerchantInfoService.delByIds(new ArrayList<>());
        if(responseVO.getCode() == 0){
            return SystemConstant.SUCCESS;
        }else {
            return SystemConstant.FAIL;
        }
    }


    @RequestMapping(value = "/updateAgentMerchantInfo")
    @ResponseBody
    public String updateAgentMerchantInfo(@RequestBody AgentMerchantInfoTable agentMerchantInfo){
        agentMerchantInfo.setUpdateTime(new Date());
        ResponseVO responseVO = agentMerchantInfoService.update(agentMerchantInfo);
        if(responseVO.getCode()== 0){
            return SystemConstant.SUCCESS;
        }else {
            return SystemConstant.FAIL;
        }
    }

    @RequestMapping(value = "/addAgentMerchantInfo")
    @ResponseBody
    public String addAgentMerchantInfo(@RequestBody AgentMerchantInfoTable agentMerchantInfo){
        agentMerchantInfo.setCreateTime(new Date());
        agentMerchantInfo.setUpdateTime(new Date());
        ResponseVO num = agentMerchantInfoService.save(agentMerchantInfo);
        if(num.getCode() == 0){
            return SystemConstant.SUCCESS;
        }else {
            return SystemConstant.FAIL;
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

    @RequestMapping("/init")
    public Map<String, Object> init() {
        Map<String, Object> init = new HashMap<>();
        init.put("identityType", constantService.getConstantByGroupName(SystemConstant.IDENTITYTYPE).getData());
        init.put("agentMerchants", agentMerchantInfoService.list(null).getData());
        return init;
    }

    @RequestMapping("/searchAgentMerchantSetting")
    @ResponseBody
    public List<AgentMerchantSettingTable> search(@RequestBody AgentMerchantSettingTable agentMerchantSetting){
        List<AgentMerchantSettingTable> result = (List)agentMerchantSettingService.getList(agentMerchantSetting).getData();
        return result;
    }

}