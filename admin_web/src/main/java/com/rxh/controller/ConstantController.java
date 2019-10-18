package com.rxh.controller;


import com.rxh.pojo.sys.SysConstant;
import com.rxh.pojo.sys.SysGroup;
import com.rxh.service.ConstantService;
import com.rxh.service.GroupService;
import com.rxh.spring.annotation.SystemLogInfo;
import com.rxh.utils.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;


/**
 * 
 * @author xuzm
 * @date 20180312
 *
 */
@Controller
@RequestMapping("/constant")
public class ConstantController {
	
     @Autowired
     private ConstantService constantService;
     
     @Autowired
     private GroupService groupService;


     @RequestMapping("/listPage")
     public ModelAndView listPage() {
    	
        return new ModelAndView("/views/constant/constantList");
    	
     }
     
    @RequestMapping("/getConstantList")
    @ResponseBody
    public List<SysConstant> getConstantList(@RequestBody Map<String, Object> paramMap) {
    	
    	return constantService.queryList(paramMap);
       
    }

    @SystemLogInfo(description = "系统常量查询")
    @RequestMapping("/getConstantInfo")
    @ResponseBody
    public Map<String,Object> getConstantInfo(@RequestBody Map<String, Object> paramMap) {
        return constantService.getConstantInfo(paramMap);

    }
    
    @RequestMapping("/getGroupAll")
    @ResponseBody
    public List<SysGroup> getGroupAll() {
    	
    	return groupService.queryList(null);
       
    }

    /**
     * 批量删除
     * 
     * @param ids
     * @return
     */
    @SystemLogInfo(description = "系统常量删除")
    @RequestMapping("/batchDel")
    @ResponseBody
    public Map<String, Object> batchDel(@RequestBody Map<String, Object> paramMap) {
    	Map<String, Object> result = constantService.deleteByIds(paramMap);
        return result;
    }    
    
    /**
     *  进入添加界面
     * @param id
     * @return
     */
    @RequestMapping("/addPage")
    public ModelAndView addPage() {
        ModelAndView view = new ModelAndView("/views/constant/constantAdd");
    	List<SysGroup> groupList = groupService.queryList(null);;
    	view.addObject("groupList",groupList);
        return view;
    }
    
    
    /**
     *  保存数据
     * @param
     * @return
     */
    @SystemLogInfo(description = "系统常量新增")
    @RequestMapping("/save")
    @ResponseBody
    public Map<String, Object> save(@RequestBody SysConstant constant) {
    	
    	constant.setId(UUID.createKey("sys_constant", ""));
    	return constantService.save(constant);

    }
    
    /**
     *  进入编辑界面
     * @param id
     * @return
     */
    @RequestMapping("/updatePage")
    public ModelAndView updatePage(@RequestParam String id) {
        ModelAndView view = new ModelAndView("/views/constant/constantUpdate");
    	SysConstant constant = constantService.selectById(id);
    	List<SysGroup> groupList = groupService.queryList(null);
    	view.addObject("constant",constant);
    	view.addObject("groupList",groupList);
        return view;
    }
    
    
    /**
     *  更新数据
     * @param id
     * @return
     */
    @SystemLogInfo(description = "系统常量更新")
    @RequestMapping("/update")
    @ResponseBody
    public Map<String, Object> update(@RequestBody SysConstant constant) {
    	
    	return constantService.update(constant);

    }

    

}
