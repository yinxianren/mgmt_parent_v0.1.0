package com.rxh.utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rxh.pojo.Excel;
import com.rxh.square.pojo.BatchData;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.Cell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * @author : 陈俊雄
 * Date: 2018/4/2
 * Time: 14:37
 * Project: Management
 * Package: com.rxh.utils
 */

@JsonIgnoreProperties(value = {"handler","hibernateLazyInitializer","fieldHandler"})
public class BatchOperationUtils {

    private final static int FIRST_SHEET = 0;
    private final static int FIRST_ROW = 0;
    private final static short VIRTUAL = 1;
    private final static short REALITY = 0;

    private static Integer indexMerOrderId = null;
    private static Integer indexAmount = null;
    private static Integer indexBankCode = null;
    private static Integer indexInAcctNo = null;
    private static Integer indexInAcctName = null;
    private static Integer indexRemark = null;
    private static Integer indexReturnUrl = null;
    private static Integer indexNoticeUrl = null;
    private static List<CSVRecord> records;
    private final static Logger logger = LoggerFactory.getLogger(BatchOperationUtils.class);
    private static Pattern pattern = Pattern.compile("\\s*|\t|\r|\n");
    private static Pattern WEBSITE_PATTERN = Pattern.compile("^(?=^.{3,255}$)[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+$");

    public static List<BatchData> getOperation(Excel excel, String userName) {
        String[] fileName = excel.getFileName().split("\\.");
        switch (fileName[0]) {
            case "批量代付":
                excel.setType("批量代付");
                return setChangeInfo(excel);
            default:
                return null;
        }
    }

    private static void getIndex(Excel excel) {
        if (excel.getWorkbook() != null) {
            for (Cell cell : excel.getWorkbook().getSheetAt(FIRST_SHEET).getRow(FIRST_ROW)) {
                switch (cell.getStringCellValue().trim()) {
                    case "商户订单号":
                        indexMerOrderId = cell.getColumnIndex();
                        break;
                    case "金额":
                        indexAmount = cell.getColumnIndex();
                        break;
                    case "银行编码":
                        indexBankCode = cell.getColumnIndex();
                        break;
                    case "收款卡号":
                        indexInAcctNo = cell.getColumnIndex();
                        break;
                    case "收款户名":
                        indexInAcctName = cell.getColumnIndex();
                        break;
                    case "备注":
                        indexRemark = cell.getColumnIndex();
                        break;
                    case "返回地址":
                        indexReturnUrl = cell.getColumnIndex();
                        break;
                    case "通知地址":
                        indexNoticeUrl = cell.getColumnIndex();
                        break;
                    default:
                        break;
                }
            }
        }
         else if (excel.getCsvParser() != null) {
            try {
                records = excel.getCsvParser().getRecords();
                CSVRecord record = records.get(0);
                for (int i = 0; i < record.size(); i++) {
                    switch (record.get(i)) {
                        case "商户订单号":
                            indexMerOrderId = i;
                            break;
                        case "金额":
                            indexAmount = i;
                            break;
                        case "银行编码":
                            indexBankCode = i;
                            break;
                        case "收款卡号":
                            indexInAcctNo =i;
                            break;
                        case "收款户名":
                            indexInAcctName = i;
                            break;
                        case "备注":
                            indexRemark = i;
                            break;
                        case "返回地址":
                            indexReturnUrl = i;
                            break;
                        case "通知地址":
                            indexNoticeUrl = i;
                            break;
                        default:
                            break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static List<BatchData> setChangeInfo(Excel excel) {
        List<BatchData> orderChanges = new ArrayList<>();
        getIndex(excel);
        try {
            if (indexMerOrderId != null && indexAmount != null&& indexBankCode != null&& indexInAcctNo != null&& indexInAcctName != null&& indexRemark != null&& indexReturnUrl != null&& indexNoticeUrl != null ) {
                if (excel.getWorkbook() != null) {
                    for (int i = 1; i <= excel.getWorkbook().getSheetAt(FIRST_SHEET).getLastRowNum(); i++) {
                        BatchData orderChange = new BatchData();

                        excel.getWorkbook().getSheetAt(FIRST_SHEET).getRow(i).getCell(indexMerOrderId).setCellType(Cell.CELL_TYPE_STRING);
                        orderChange.setMerOrderId(excel.getWorkbook().getSheetAt(FIRST_SHEET).getRow(i).getCell(indexMerOrderId).getStringCellValue().trim());

                        excel.getWorkbook().getSheetAt(FIRST_SHEET).getRow(i).getCell(indexAmount).setCellType(Cell.CELL_TYPE_STRING);
                        String amount =excel.getWorkbook().getSheetAt(FIRST_SHEET).getRow(i).getCell(indexAmount).getStringCellValue().trim();
                        Matcher matcher = pattern.matcher(amount);
                        orderChange.setAmount(new BigDecimal(matcher.replaceAll("")));;

                        orderChange.setBankCode(excel.getWorkbook().getSheetAt(FIRST_SHEET).getRow(i).getCell(indexBankCode).getStringCellValue().trim());

                        excel.getWorkbook().getSheetAt(FIRST_SHEET).getRow(i).getCell(indexInAcctNo).setCellType(Cell.CELL_TYPE_STRING);
                        orderChange.setInAcctNo(excel.getWorkbook().getSheetAt(FIRST_SHEET).getRow(i).getCell(indexInAcctNo).getStringCellValue().trim());

                        orderChange.setInAcctName(excel.getWorkbook().getSheetAt(FIRST_SHEET).getRow(i).getCell(indexInAcctName).getStringCellValue().trim());
                        orderChange.setRemark(excel.getWorkbook().getSheetAt(FIRST_SHEET).getRow(i).getCell(indexRemark).getStringCellValue().trim());
                        orderChange.setReturnUrl(excel.getWorkbook().getSheetAt(FIRST_SHEET).getRow(i).getCell(indexReturnUrl).getStringCellValue().trim());
                        orderChange.setNoticeUrl(excel.getWorkbook().getSheetAt(FIRST_SHEET).getRow(i).getCell(indexNoticeUrl).getStringCellValue().trim());
                        orderChanges.add(orderChange);
                    }
                    excel.getWorkbook().close();
                    return orderChanges;
                } else if (excel.getCsvParser() != null) {
                    for (int i = 1; i < records.size(); i++) {
                        BatchData orderChange = new BatchData();
                        orderChange.setMerOrderId(records.get(i).get(indexMerOrderId).trim());
                        Matcher matcher = pattern.matcher(records.get(i).get(indexAmount).trim());
                        orderChange.setAmount(new BigDecimal(matcher.replaceAll("")));
                        orderChange.setBankCode(records.get(i).get(indexBankCode).trim());
                        orderChange.setInAcctNo(records.get(i).get(indexInAcctNo).trim());
                        orderChange.setInAcctName(records.get(i).get(indexInAcctName).trim());
                        orderChange.setRemark(records.get(i).get(indexRemark).trim());
                        orderChange.setReturnUrl(records.get(i).get(indexReturnUrl).trim());
                        orderChange.setNoticeUrl(records.get(i).get(indexNoticeUrl).trim());
                        orderChanges.add(orderChange);
                    }
                    excel.getCsvParser().close();
                    return orderChanges;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }






}