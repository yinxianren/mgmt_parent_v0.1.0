package com.rxh.pojo.base;

import com.rxh.vo.VoSysLog;

import java.io.Serializable;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: 陈俊雄
 * Date: 2018/3/2
 * Time: 9:34
 * Project: Management
 * Package: com.rxh.pojo
 */
public class Page implements Serializable {
    private int pageNum;
    private int pageSize;
    private Map<String, String> orderBy;
    private SearchInfo searchInfo;
    private VoSysLog logSearch;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Map<String, String> getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(Map<String, String> orderBy) {
        this.orderBy = orderBy;
    }

    public SearchInfo getSearchInfo() {
        return searchInfo;
    }

    public void setSearchInfo(SearchInfo searchInfo) {
        this.searchInfo = searchInfo;
    }

    public VoSysLog getLogSearch() {
        return logSearch;
    }

    public void setLogSearch(VoSysLog logSearch) {
        this.logSearch = logSearch;
    }
}
