/**
 * 版权声明：融信汇 版权所有 违者必究 2015
 * 日    期：15-11-18
 */
package com.rxh.pojo;

/**
 * <pre>
 * 风控控制结果对象
 * </pre>
 *
 * @author hul
 * @version 1.00
 */
public class RiskResult {
    // 风控结果代码
    private String code;

    // 风控结果说明
    private String result;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
