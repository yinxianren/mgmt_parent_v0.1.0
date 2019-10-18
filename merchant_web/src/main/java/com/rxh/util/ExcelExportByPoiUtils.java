package com.rxh.util;


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
}
