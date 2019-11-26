package com.rxh.util;


import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 
 * @author xuzm
 * @date 20180411
 *
 */
public class ExcelExportByPoiUtils extends AbstractXlsxView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String filename;
		Date reconTime = (Date) request.getAttribute("reconTime");
		Short reconType = (Short) request.getAttribute("reconType");
		if(reconType == 1){
			SimpleDateFormat reconDateFmt = new SimpleDateFormat("yyyy-MM-dd");
			filename = "businessRecon_" + reconDateFmt.format(reconTime) + ".xls";
		}else{
			SimpleDateFormat reconDateFmt = new SimpleDateFormat("yyyy-MM");
			filename = "businessMonthRecon_" +  reconDateFmt.format(reconTime) + ".xls";
		}
    	response.reset();
        response.setContentType("application/vnd.ms-excel");     
        response.setHeader("Content-disposition", "attachment;filename=" + filename);     
        OutputStream ouputStream = response.getOutputStream();   
        workbook.write(ouputStream);
        ouputStream.flush();     
        ouputStream.close();
	}

	public static HSSFWorkbook getHSSFWorkbook(String sheetName, String[] title, String[][] values, HSSFWorkbook wb) {

		// 第一步，创建一个HSSFWorkbook，对应一个Excel文件
		if (wb == null) {
			wb = new HSSFWorkbook();
		}

		// 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet(sheetName);

		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
		HSSFRow row = sheet.createRow(0);

		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

		// 声明列对象
		HSSFCell cell = null;

		// 创建标题
		for (int i = 0; i < title.length; i++) {
			cell = row.createCell(i);
			cell.setCellValue(title[i]);
			cell.setCellStyle(style);
		}

		// 创建内容
		for (int i = 0; i < values.length; i++) {
			row = sheet.createRow(i + 1);
			for (int j = 0; j < values[i].length; j++) {
				// 将内容按顺序赋给对应的列对象
				row.createCell(j).setCellValue(values[i][j]);
			}
		}
		return wb;
	}
}
