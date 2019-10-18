package com.rxh.haiyipay;

import com.alibaba.fastjson.JSONObject;
import com.rxh.utils.HttpClientUtils;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author monkey
 * @date 2019/7/9
 */
public class HttpClientUtil {

    private static int TIMEOUT = 60;
    private static final String UTF_8 = "UTF-8";
    private static Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

    public static int getTIMEOUT() {
        return TIMEOUT;
    }

    public static void setTIMEOUT(int TIMEOUT) {
        HttpClientUtil.TIMEOUT = TIMEOUT;
    }

    public static String doPostJson(CloseableHttpClient httpClient, String url, Map<String,String> jsonMap, String encoding) {
        Map<String, String> headers = new HashMap<String, String>() {
            {
                put("Content-type", "application/json");
                put("Token",jsonMap.get("token"));
            }
        };
        jsonMap.remove("token");
        return doPostJson(httpClient, url, JSONObject.toJSONString(jsonMap), headers, encoding);
    }

    public static String doPostJson(CloseableHttpClient httpClient, String url, String jsonString, Map<String, String> headers, String encoding) {
        HttpPost httpPost = new HttpPost(url);
        StringEntity stringEntity = new StringEntity(jsonString, encoding);
        headers.forEach((k, v) -> stringEntity.setContentType(v));
        httpPost.setEntity(stringEntity);
        return send(httpClient, httpPost, headers, encoding);
    }

    /**
     * 发送请求
     *
     * @param httpClient httpClient
     * @param request    request
     * @param headers    请求头
     * @param encoding   编码
     * @return 请求字符串结果
     */
    private static String send(CloseableHttpClient httpClient, HttpRequestBase request, Map<String, String> headers, String encoding) {
        // 设置请求头
        if (headers != null) {
            headers.forEach(request::setHeader);
        } else {
            Header header = new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
            request.setHeader(header);
        }
        // 设置超时时间
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(TIMEOUT * 1000)
                .setConnectionRequestTimeout(TIMEOUT * 1000)
                .setSocketTimeout(TIMEOUT * 1000).build();
        request.setConfig(config);
        // 获取请求返回消息
        String result = null;
        try {
            // 执行请求
            logger.info("RequestURI:" + request.getURI());
            CloseableHttpResponse response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() != HttpServletResponse.SC_OK) {
                logger.error("ResponseStatusCode:" + response.getStatusLine().getStatusCode());
            }
            // 获取请求返回消息
            result = EntityUtils.toString(response.getEntity(), UTF_8);
        } catch (Exception e) {
            logger.error("ErrorMsg:" + e.getMessage() + ",ErrorURI:" + request.getURI());
        }
        // 关闭请求
        try {
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 返回请求参数
        return result;
    }

}
