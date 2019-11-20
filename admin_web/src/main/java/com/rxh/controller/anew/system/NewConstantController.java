package com.rxh.controller.anew.system;


import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.table.system.SysConstantTable;
import com.internal.playment.common.table.system.SysGroupTable;
import com.rxh.pojo.base.Page;
import com.rxh.service.system.NewConstantService;
import com.rxh.service.system.NewSysGroupService;
import com.rxh.spring.annotation.SystemLogInfo;
import com.rxh.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
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
public class NewConstantController {
	
     @Autowired
     private NewConstantService newConstantService;
     @Autowired
     private NewSysGroupService newSysGroupService;

     @RequestMapping("/listPage")
     public ModelAndView listPage() {
    	
        return new ModelAndView("/views/constant/constantList");
    	
     }
     
    @RequestMapping("/getConstantList")
    @ResponseBody
    public ResponseVO getConstantList(@RequestBody SysConstantTable sysConstantTable) {
    	try {
            return newConstantService.getList(sysConstantTable);
        }catch (Exception e){
    	    e.printStackTrace();
    	    return new ResponseVO(StatusEnum._1.getStatus(),StatusEnum._1.getRemark());
        }

    }

    @SystemLogInfo(description = "系统常量查询")
    @RequestMapping("/getConstantInfo")
    @ResponseBody
    public ResponseVO getConstantInfo(@RequestBody Page page) {
         try {
             return newConstantService.page(page);
         }catch (Exception e){
             e.printStackTrace();
             return new ResponseVO(StatusEnum._1.getStatus(),StatusEnum._1.getRemark());
         }
    }
    
    @RequestMapping("/getGroupAll")
    @ResponseBody
    public ResponseVO getGroupAll() {
    	try {
    	    return newSysGroupService.getList(null);
        }catch (Exception e){
    	    e.printStackTrace();
    	    return new ResponseVO(StatusEnum._1.getStatus(),StatusEnum._1.getRemark());
        }

    }

    /**
     * 批量删除
     * 
     * @return
     */
    @SystemLogInfo(description = "系统常量删除")
    @RequestMapping("/batchDel")
    @ResponseBody
    public ResponseVO batchDel(@RequestBody Map<String, Object> paramMap) {
        try {
            String ids = paramMap.get("ids").toString();
            return newConstantService.delByIds(ids);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseVO(StatusEnum._1.getStatus(),StatusEnum._1.getRemark());
        }
    }
    
    /**
     *  进入添加界面
     * @return
     */
    @RequestMapping("/addPage")
    public ModelAndView addPage() {
        ModelAndView view = new ModelAndView("/views/constant/constantAdd");
    	List<SysGroupTable> groupList = (List)newSysGroupService.getList(null).getData();
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
    public ResponseVO save(@RequestBody SysConstantTable constant) {
    	try {
    	    constant.setCreateTime(new Date());
    	    return newConstantService.saveOrUpdate(constant);
        }catch (Exception e){
    	    return new ResponseVO(StatusEnum._1.getStatus(),StatusEnum._1.getRemark());
        }

    }
    
    /**
     *  进入编辑界面
     * @param id
     * @return
     */
    @RequestMapping("/updatePage")
    public ModelAndView updatePage(@RequestParam String id) {
        ModelAndView view = new ModelAndView("/views/constant/constantUpdate");
        SysConstantTable sysConstantTable = new SysConstantTable();
        sysConstantTable.setId(id);
    	List constants = (List)newConstantService.getList(sysConstantTable).getData();
        List<SysGroupTable> groupList = (List)newSysGroupService.getList(null).getData();
    	view.addObject("constant",constants.get(0));
    	view.addObject("groupList",groupList);
        return view;
    }
    
    
    /**
     *  更新数据
     * @return
     */
    @SystemLogInfo(description = "系统常量更新")
    @RequestMapping("/update")
    @ResponseBody
    public ResponseVO update(@RequestBody SysConstantTable constant) {
    	try {
    	    constant.setUpdateTime(new Date());
    	    return newConstantService.saveOrUpdate(constant);
        }catch (Exception e){
    	    e.printStackTrace();
    	    return new ResponseVO(StatusEnum._1.getStatus(),StatusEnum._1.getRemark());
        }

    }

}
