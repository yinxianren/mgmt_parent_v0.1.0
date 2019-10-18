package com.rxh.db;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * Created with IntelliJ IDEA.
 * User: 陈俊雄
 * Date: 2018/3/14
 * Time: 16:00
 * Project: Management
 * Package: com.rxh.db
 * <p>
 * 继承AbstractRoutingDataSource并重写determineCurrentLookupKey方法
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceHolder.getDataSources();
    }
}
