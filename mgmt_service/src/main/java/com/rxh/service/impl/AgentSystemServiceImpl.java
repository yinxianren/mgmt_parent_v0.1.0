package com.rxh.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rxh.mapper.base.IdMapper;
import com.rxh.mapper.sys.AgentSysLogMapper;
import com.rxh.mapper.sys.SysAreaMapper;
import com.rxh.pojo.base.Page;
import com.rxh.pojo.base.PageResult;
import com.rxh.pojo.sys.SysArea;
import com.rxh.pojo.sys.SysLog;
import com.rxh.service.AgentSystemService;
import com.rxh.service.ConstantService;
import com.rxh.service.SystemService;
import com.rxh.utils.SystemConstant;
import com.rxh.utils.UUID;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class AgentSystemServiceImpl implements AgentSystemService {
    @Resource
    private AgentSysLogMapper agentSysLogMapper;

    @Resource
    private SysAreaMapper areaMapper;


    @Resource
    private ConstantService constantService;

    @Resource
    private IdMapper idMapper;

    @Override
    public void saveSystemLog(SysLog log) {
        log.setId(UUID.createKey("agent_sys_log"));
        agentSysLogMapper.insertSelective(log);
    }

    @Override
    public PageResult getSystemLog(Page page) {
        // 设置分页信息
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        if (page.getOrderBy() == null || page.getOrderBy().size() == 0) {
            PageHelper.orderBy("id DESC");
        } else {
            page.getOrderBy().forEach((key, value) -> PageHelper.orderBy(key + " " + value));
        }
        if (page.getLogSearch() != null && page.getLogSearch().getEndTime() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(page.getLogSearch().getEndTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            page.getLogSearch().setEndTime(calendar.getTime());
        }
        List<SysLog> logs = agentSysLogMapper.selectBySearchInfo(page.getLogSearch());
        PageInfo<SysLog> pageInfo = new PageInfo<>(logs);
        // 获取查询结果
        PageResult result = new PageResult();
        result.setRows(logs);
        result.setTotal(pageInfo.getTotal());
        result.setAllPage(pageInfo.getPages());
        return result;
    }

    @Override
    public List<SysArea> getAreaInfoByCountryCode(String countryCode) {
        // 获取对应国家编码的所有地镇信息
        List<SysArea> areas = areaMapper.selectAreaByCountryCode(countryCode);
        // 获取省List对象
        List<SysArea> firstLevelAreaList = areas
                .stream()
                .filter(sysArea -> sysArea.getLevel() == 0)
                .collect(Collectors.toList());
        // 获取市List对象
        List<SysArea> secondLevelAreaList = areas
                .stream()
                .filter(sysArea -> sysArea.getLevel() == 1)
                .collect(Collectors.toList());
        // 获取县List对象
        List<SysArea> thirdLevelAreaList = areas
                .stream()
                .filter(sysArea -> sysArea.getLevel() == 2)
                .collect(Collectors.toList());
        // 获取镇List对象
        List<SysArea> fourthLevelAreaList = areas
                .stream()
                .filter(sysArea -> sysArea.getLevel() == 3)
                .collect(Collectors.toList());
        // 获取市Map对象（以父级ID为Key，Value为List）
        Map<Integer, List<SysArea>> secondAreaListMap = secondLevelAreaList
                .stream()
                .collect(Collectors.groupingBy(SysArea::getPid));
        // 获取县Map对象（以父级ID为Key，Value为List）
        Map<Integer, List<SysArea>> thirdAreaListMap = thirdLevelAreaList
                .stream()
                .collect(Collectors.groupingBy(SysArea::getPid));
        // 获取镇Map对象（以父级ID为Key，Value为List）
        Map<Integer, List<SysArea>> fourthAreaListMap = fourthLevelAreaList
                .stream()
                .collect(Collectors.groupingBy(SysArea::getPid));
        // 循环县，根据ID从镇Map对象获取对应List
        thirdLevelAreaList.forEach(sysArea -> sysArea.setChildArea(fourthAreaListMap.get(sysArea.getId())));
        // 循环市，根据ID从县Map对象获取对应List
        secondLevelAreaList.forEach(sysArea -> sysArea.setChildArea(thirdAreaListMap.get(sysArea.getId())));
        // 循环省，根据ID从市/镇Map对象获取对应List
        firstLevelAreaList.forEach(sysArea -> {
            sysArea.setChildArea(secondAreaListMap.get(sysArea.getId()));
            if (sysArea.getChildArea() == null) {
                sysArea.setChildArea(thirdAreaListMap.get(sysArea.getId()));
            }
        });
        // 循环
        return firstLevelAreaList;
    }

    @Override
    public Map<String, Object> getMerchantOpinionInit() {
        Map<String, Object> init = new HashMap<>();
        init.put("questionType", constantService.getConstantByGroupName(SystemConstant.QUESTION_TYPE));
        init.put("questionStatus", constantService.getConstantByGroupName(SystemConstant.QUESTION_STATUS));
        return init;
    }



    @Override
    public String getLastId(String tableName) {
        return idMapper.selectLastId(tableName);
    }
}