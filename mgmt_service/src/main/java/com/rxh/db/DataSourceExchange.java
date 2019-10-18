package com.rxh.db;

import org.aspectj.lang.JoinPoint;
import org.springframework.util.PatternMatchUtils;

/**
 * Created with IntelliJ IDEA.
 * User: 陈俊雄
 * Date: 2018/3/28
 * Time: 11:26
 * Project: Management
 * Package: com.rxh.db
 * <p>
 * 进行读写分离
 */
public class DataSourceExchange {

    // 读法名开头字符串数组
    private String[] readMethodList;
    // 写方法名开头字符串数组
    private String[] writeMethodList;

    public void before(JoinPoint point) {
        DynamicDataSourceHolder.setLastDataSource(DynamicDataSourceHolder.getDataSources());
        if (PatternMatchUtils.simpleMatch(readMethodList, point.getSignature().getName())) {
            DynamicDataSourceHolder.setDataSources(DataSourceEnum.DS1.getKey());
        } else {
            DynamicDataSourceHolder.setDataSources(DataSourceEnum.DS2.getKey());
        }
    }

    /**
     * 执行后将数据源置为空
     */
    public void after() {
        DynamicDataSourceHolder.setDataSources(DynamicDataSourceHolder.getLastDataSource());
        DynamicDataSourceHolder.setLastDataSource(null);
    }

    /*public void afterThrowing(JoinPoint joinPoint, Throwable e) {
        StringBuilder stringBuilder = new StringBuilder();
        StackTraceElement[] stackTraceElements = e.getStackTrace();
        for (int i = 0; i < 10; i++) {
            stringBuilder.append(stackTraceElements[i]);
            stringBuilder.append("\r\n");
        }
        SysLog log = new SysLog();
        log.setId(UUID.createKey("sys_log"));
        log.setOperator("ERROR_SERVICE");
        log.setType((short) 9);
        log.setStartTime(new Date());
        log.setMethodName(joinPoint.getSignature().getName());
        log.setMethodDescription(e.getMessage());
        log.setMessage(stringBuilder.toString());
        DynamicDataSourceHolder.setDataSources(DataSourceEnum.DS2.getKey());
        sysLogMapper.insertSelective(log);
        after();
    }*/

    public String[] getReadMethodList() {
        return readMethodList;
    }

    public void setReadMethodList(String[] readMethodList) {
        this.readMethodList = readMethodList;
    }

    public String[] getWriteMethodList() {
        return writeMethodList;
    }

    public void setWriteMethodList(String[] writeMethodList) {
        this.writeMethodList = writeMethodList;
    }
}
