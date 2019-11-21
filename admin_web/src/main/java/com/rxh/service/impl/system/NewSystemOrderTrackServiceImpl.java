package com.rxh.service.impl.system;

import com.internal.playment.api.db.system.ApiSystemOrderTrackService;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.table.system.SystemOrderTrackTable;
import com.internal.playment.common.page.Page;
import com.internal.playment.common.page.SearchInfo;
import com.rxh.service.system.NewSystemOrderTrackService;
import com.rxh.vo.ResponseVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;

@Service
public class NewSystemOrderTrackServiceImpl implements NewSystemOrderTrackService {

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private ApiSystemOrderTrackService apiSystemOrderTrackService;

    @Override
    public ResponseVO page(Page page) {
        SystemOrderTrackTable systemOrderTrackTable = new SystemOrderTrackTable();
        SearchInfo searchInfo = page.getSearchInfo();
        if (StringUtils.isNotBlank(searchInfo.getMerId())) systemOrderTrackTable.setMerId(searchInfo.getMerId());
        if (StringUtils.isNotBlank(searchInfo.getMerOrderId())) systemOrderTrackTable.setMerOrderId(searchInfo.getMerOrderId());
        if (StringUtils.isNotBlank(searchInfo.getOrderId())) systemOrderTrackTable.setPlatformOrderId(searchInfo.getOrderId());
        if (null != (searchInfo.getStartDate())) systemOrderTrackTable.setBeginTime(sdf2.format(searchInfo.getStartDate()));
        if (null != searchInfo.getEndDate()){
            String date = sdf.format(searchInfo.getEndDate());
            date = date + " 23:59:59";
            systemOrderTrackTable.setEndTime(date);
        }
        com.baomidou.mybatisplus.extension.plugins.pagination.Page page1 = new com.baomidou.mybatisplus.extension.plugins.pagination.Page();
        page1.setSize(page.getPageSize());
        page1.setCurrent(page.getPageNum());
        ResponseVO responseVO = new ResponseVO();
        responseVO.setCode(StatusEnum._0.getStatus());
        responseVO.setMessage(StatusEnum._0.getRemark());
        responseVO.setData(apiSystemOrderTrackService.page(systemOrderTrackTable,page1));
        return responseVO;
    }
}
