package com.rxh.pojo.base;


import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class PageResult implements Serializable {

    private long total;
    private List rows;
    private long allPage;
    private Map customize;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }

    public long getAllPage() {
        return allPage;
    }

    public void setAllPage(long allPage) {
        this.allPage = allPage;
    }

    public Map getCustomize() {
        return customize;
    }

    public void setCustomize(Map customize) {
        this.customize = customize;
    }
}
