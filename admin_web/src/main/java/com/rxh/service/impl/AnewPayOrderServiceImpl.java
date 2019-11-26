package com.rxh.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.internal.playment.api.db.business.ApiPayOrderInfoService;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.page.PayCardholderInfo;
import com.internal.playment.common.table.business.PayOrderInfoTable;
import com.internal.playment.common.table.channel.ChannelInfoTable;
import com.internal.playment.common.table.system.SysConstantTable;
import com.internal.playment.common.page.Page;
import com.internal.playment.common.page.SearchInfo;
import com.rxh.service.AnewChannelService;
import com.rxh.service.AnewPayOrderService;
import com.rxh.service.system.NewSystemConstantService;
import com.internal.playment.common.enums.SystemConstant;
import com.internal.playment.common.page.ResponseVO;
import com.rxh.util.ExcelExportByPoiUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

@Service
public class AnewPayOrderServiceImpl implements AnewPayOrderService {

    @Autowired
    private ApiPayOrderInfoService apiPayOrderInfoService;
    @Autowired
    private AnewChannelService anewChannelService;
    @Autowired
    private NewSystemConstantService constantService;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public ResponseVO getList(Page page) {
        ResponseVO responseVO = new ResponseVO();
        try {
            PayOrderInfoTable payOrderInfoTable = new PayOrderInfoTable();
            payOrderInfoTable.setPageNum(page.getPageNum());
            payOrderInfoTable.setPageSize(page.getPageSize());
            SearchInfo searchInfo = page.getSearchInfo();
            if (searchInfo == null ) searchInfo = new SearchInfo();
            if (StringUtils.isNotBlank(searchInfo.getMerId())) payOrderInfoTable.setMerchantId(searchInfo.getMerId());
            if (StringUtils.isNotBlank(searchInfo.getPayId()))
                payOrderInfoTable.setPlatformOrderId(searchInfo.getPayId());
//            if (StringUtils.isNotEmpty(searchInfo.getOrganizationId())) payOrderInfoTable.setChannelId(searchInfo.getExpressName());
            if (null != (searchInfo.getOrderStatus())) payOrderInfoTable.setStatus(searchInfo.getOrderStatus());
            if (null != (searchInfo.getSettleStatus())) payOrderInfoTable.setSettleStatus(searchInfo.getSettleStatus());
            if (StringUtils.isNotBlank(searchInfo.getProductId()))
                payOrderInfoTable.setProductId(searchInfo.getProductId());
            if (null != (searchInfo.getStartDate()))
                payOrderInfoTable.setBeginTime(sdf2.format(searchInfo.getStartDate()));
            if (null != searchInfo.getEndDate()) {
                String date = sdf.format(searchInfo.getEndDate());
                date = date + " 23:59:59";
                payOrderInfoTable.setEndTime(date);
            }
            IPage<PayOrderInfoTable> ipage = apiPayOrderInfoService.page(payOrderInfoTable);
            List<ChannelInfoTable> channelInfoTables = (List<ChannelInfoTable>) anewChannelService.getAll(null).getData();
            Map<String, Object> organMap = new HashMap<>();
            for (ChannelInfoTable channelInfoTable : channelInfoTables) {
                organMap.put(channelInfoTable.getChannelId(), channelInfoTable.getOrganizationId());
            }
            BigDecimal totalMoney = new BigDecimal(0);
            BigDecimal totalChannelFee = new BigDecimal(0);
            BigDecimal totalFee = new BigDecimal(0);
            for (PayOrderInfoTable payOrder : ipage.getRecords()) {
                totalMoney = totalMoney.add(payOrder.getAmount());
                totalFee = totalFee.add(payOrder.getAmount().subtract(payOrder.getInAmount()));
                totalChannelFee = totalChannelFee.add(payOrder.getChannelFee());
                payOrder.setOrganizationId(organMap.get(payOrder.getChannelId()) != null ? organMap.get(payOrder.getChannelId()).toString() : null);
            }
            Map map = new HashMap();
            map.put("totalMoney", totalMoney.toString());
            map.put("totalFee", totalFee.toString());
            map.put("totalChannelFee", totalChannelFee.toString());
            // 返回结果
            responseVO.setData(ipage);
            responseVO.setCustomData(map);
            return responseVO;
        } catch (Exception e) {
            e.printStackTrace();
            responseVO.setCode(StatusEnum._1.getStatus());
            return responseVO;
        }
    }

    @Override
    public ResponseVO getCardHolderInfo(String payId) {
        ResponseVO responseVO = new ResponseVO();
        PayOrderInfoTable payOrderInfoTable = new PayOrderInfoTable();
        payOrderInfoTable.setPlatformOrderId(payId);
        PayOrderInfoTable payOrder = apiPayOrderInfoService.getOne(payOrderInfoTable);
        if (payOrder != null) {
            ChannelInfoTable channelInfo = new ChannelInfoTable();
            channelInfo.setChannelId(payOrder.getChannelId());
            List<ChannelInfoTable> channelInfos = (List<ChannelInfoTable>) anewChannelService.getAll(channelInfo).getData();
            if (!CollectionUtils.isEmpty(channelInfos)) {
                channelInfo = channelInfos.get(0);
            }
            PayCardholderInfo cardholderInfo = new PayCardholderInfo();
            Map idMap = constantService.getConstantsMapByGroupName(SystemConstant.IDENTITYTYPE);
            Map bankMap = constantService.getConstantsMapByGroupName(SystemConstant.BANKCARDTYPE);
            Map productMap = constantService.getConstantsMapByGroupName(SystemConstant.PRODUCTTYPE);
            SysConstantTable idtype = null;
            SysConstantTable banktype = null;
            SysConstantTable productType = null;
            if (idMap.get(payOrder.getIdentityType().toString()) != null) {
                idtype = (SysConstantTable) idMap.get(payOrder.getIdentityType().toString());
            }
            if (bankMap.get(payOrder.getBankCardType().toString()) != null) {
                banktype = (SysConstantTable) bankMap.get(payOrder.getBankCardType().toString());
            }
            if (productMap.get(payOrder.getProductId()) != null) {
                productType = (SysConstantTable) productMap.get(payOrder.getProductId());
            }
            //证件类型
            cardholderInfo.setIdentityNum(idtype != null ? idtype.getName() : "");
            //卡类型
            cardholderInfo.setBankcardNum(banktype != null ? banktype.getName() : "");
            //产品名称
            cardholderInfo.setProductName(productType != null ? productType.getName() : "");
            cardholderInfo.setChannelName(channelInfo == null ? "" : channelInfo.getChannelName());
            responseVO.setCode(StatusEnum._0.getStatus());
            responseVO.setMessage("获取持卡人详情成功");
            responseVO.setData(cardholderInfo);
        } else {
            responseVO.setCode(StatusEnum._1.getStatus());
            responseVO.setMessage("获取持卡人详情失败");
        }
        return responseVO;
    }

    @Override
    public void findPayOrderExcel(Page page, HttpServletRequest request, HttpServletResponse response){
        //创建Excel工作薄对象
        HSSFWorkbook workbook = new HSSFWorkbook();
        //创建Excel工作表对象
//        HSSFSheet sheet = workbook.createSheet("wj");
        //创建行的单元格，从0开始
//        HSSFRow row = sheet.createRow(0);
        page.setPageNum(1);
        page.setPageSize(Integer.MAX_VALUE);
        List<PayOrderInfoTable> list = ((IPage) getList(page).getData()).getRecords();
        //创建单元格
//        HSSFCell cell = row.createCell(0);
        insetRow(workbook,list,response);

       /* //设置值
        cell.setCellValue(new Date());
        //创建单元格样式
        HSSFCellStyle style=workbook.createCellStyle();
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
        cell.setCellStyle(style);
        //设置保留2位小数--使用Excel内嵌的格式
        HSSFCell cell1 = row.createCell(1);
        cell1.setCellValue(12.3456789);
        style=workbook.createCellStyle();
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
        cell1.setCellStyle(style);
        //设置货币格式--使用自定义的格式
        HSSFCell cell2 = row.createCell(2);
        cell2.setCellValue(12345.6789);
        style=workbook.createCellStyle();
        style.setDataFormat(workbook.createDataFormat().getFormat("￥#,##0"));
        cell2.setCellStyle(style);
        //设置百分比格式--使用自定义的格式
        HSSFCell cell3 = row.createCell(3);
        cell3.setCellValue(0.123456789);
        style=workbook.createCellStyle();
        style.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
        cell3.setCellStyle(style);
        //设置中文大写格式--使用自定义的格式
        HSSFCell cell4 = row.createCell(4);
        cell4.setCellValue(12345);
        style=workbook.createCellStyle();
        style.setDataFormat(workbook.createDataFormat().getFormat("[DbNum2][$-804]0"));
        cell4.setCellStyle(style);
        //设置科学计数法格式--使用自定义的格式
        HSSFCell cell5 = row.createCell(5);
        cell5.setCellValue(12345);
        style=workbook.createCellStyle();
        style.setDataFormat(workbook.createDataFormat().getFormat("0.00E+00"));
        cell5.setCellStyle(style);*/
    }

    private void insetRow(HSSFWorkbook workbook,List<PayOrderInfoTable> list,HttpServletResponse response) {
        String[] header = {
                "日期(年/月/日)",
                "商户号",
                "商户订单号",
                "平台订单号",
                "代理商名称",
                "通道交易流水号",
                "机构名称",
                "通道名称（通道标识符）",
                "支付方式",
                "订单币种",
                "订单金额",
                "实际支付金额",
                "平台手续费收入",
                "平台毛利润",
                "通道成本",
                "订单状态",
                "交易时间",
                "通道交易时间",
                "通道费率",
                "商户费率",
                "子商户费率",
                "子商户成本"
        };
       /* for (int i= 0;i<header.length;i++){
            HSSFCell cell = row.createCell(i);
            //设置值
            cell.setCellValue(header[i]);
        }*/
        //excel文件名
        String fileName = "支付订单信息表" + System.currentTimeMillis() + ".xls";
        //sheet名
        String sheetName = "支付订单信息表";

        String[][] content = new String[list.size() + 1][header.length];
        for (int i = 0; i < list.size(); i++) {
            PayOrderInfoTable r = list.get(i);
            content[i][0] = r.getUpdateTime().toString();
            content[i][1] = r.getMerchantId();
            content[i][2] = r.getMerOrderId();
            content[i][3] = r.getPlatformOrderId();
            content[i][4] = "系统代理商";
            content[i][5] = r.getChannelOrderId();
            content[i][6] = r.getOrganizationId();
            content[i][7] = r.getChannelId();
            content[i][8] = "收单";
            content[i][9] = r.getCurrency();
            content[i][10] = r.getAmount().toString();
            content[i][11] = r.getInAmount().toString();
            content[i][12] = r.getMerFee().toString();
            content[i][13] = r.getChannelFee().toString();
            content[i][14] = r.getMerFee().subtract(r.getChannelFee()).toString();
            content[i][15] = r.getStatus().toString();
            content[i][16] = r.getUpdateTime().toString();
            content[i][17] = r.getUpdateTime().toString();
            content[i][18] = r.getChannelRate().toString();
            content[i][19] = r.getMerRate().toString();
            content[i][20] = r.getTerFee().toString();
            content[i][21] = r.getPayFee().toString();
        }
        //创建HSSFWorkbook
        HSSFWorkbook wb = ExcelExportByPoiUtils.getHSSFWorkbook(sheetName, header, content, workbook);
        //响应到客户端
        try {
            this.setResponseHeader(response, fileName);
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
            //文档输出
            FileOutputStream out = new FileOutputStream("e:/home/" + fileName);
            wb.write(out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //发送响应流方法
    public void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(), "ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
