package com.rxh.pojo;

import org.apache.commons.csv.CSVParser;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: 陈俊雄
 * Date: 2018/4/2
 * Time: 10:52
 * Project: Management
 * Package: com.rxh.pojo
 */
public class Excel implements Serializable {

    private String fileName;

    private Workbook workbook;

    private String type;

    private CSVParser csvParser;

    public Excel(String fileName, Workbook workbook) {
        this.fileName = fileName;
        this.workbook = workbook;
    }

    public Excel(String fileName, CSVParser csvParser) {
        this.fileName = fileName;
        this.csvParser = csvParser;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public void setWorkbook(Workbook workbook) {
        this.workbook = workbook;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public CSVParser getCsvParser() {
        return csvParser;
    }

    public void setCsvParser(CSVParser csvParser) {
        this.csvParser = csvParser;
    }
}
