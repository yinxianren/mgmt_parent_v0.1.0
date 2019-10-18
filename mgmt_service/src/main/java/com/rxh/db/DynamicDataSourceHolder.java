package com.rxh.db;

/**
 * Created with IntelliJ IDEA.
 * User: 陈俊雄
 * Date: 2018/3/14
 * Time: 16:00
 * Project: Management
 * Package: com.rxh.db
 * <p>
 * 用于持有当前线程中使用的数据源标识
 */
class DynamicDataSourceHolder {

    private static ThreadLocal<String> dataSources = new ThreadLocal<>();

    private static String lastDataSource;

    static void setDataSources(String dataSource) {
        dataSources.set(dataSource);
    }

    static String getDataSources() {
        return dataSources.get();
    }

    static String getLastDataSource() {
        return lastDataSource;
    }

    static void setLastDataSource(String lastDataSource) {
        DynamicDataSourceHolder.lastDataSource = lastDataSource;
    }
}
