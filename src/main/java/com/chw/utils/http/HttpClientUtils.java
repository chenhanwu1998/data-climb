package com.chw.utils.http;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
public class HttpClientUtils {

    private static CloseableHttpClient httpClient;

    //from-请求/响应类型
    public static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded;charset=UTF-8";

    //json-请求/响应类型
    public static final String CONTENT_TYPE_JSON = "application/json;charset=UTF-8";

    //所以起作用的设置是DefaultMaxPerRoute,真正的并发http请求数量
    private static final int DEFAULT_MAX_PER_ROUTE = 150;

    //设置连接池的大小
    private static final int MAX_TOTAL = 150;

    //设置链接超时
    private static final int CONNECTION_TIMEOUT = 5000;

    //设置等待数据超时时间
    private static final int SOCKET_TIMEOUT = 15000;

    //从连接池获取连接实例的超时时间
    public static final int REQUEST_TIMEOUT = 2000;

    private static synchronized void initPool() {
        if (null == httpClient) {
            PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
            cm.setDefaultMaxPerRoute(DEFAULT_MAX_PER_ROUTE);
            cm.setMaxTotal(MAX_TOTAL);
            RequestConfig.Builder configBuilder = RequestConfig.custom();
            configBuilder.setConnectTimeout(CONNECTION_TIMEOUT);
            configBuilder.setSocketTimeout(SOCKET_TIMEOUT);
            configBuilder.setConnectionRequestTimeout(REQUEST_TIMEOUT);
            RequestConfig requestConfig = configBuilder.build();
            httpClient = HttpClients.custom().setConnectionManager(cm).setDefaultRequestConfig(requestConfig).build();
        }
    }

    public static String sendGet(String url) {
        return sendGet(url, null, 1000);
    }

    public static String sendGet(String url, Integer limit) {
        return sendGet(url, null, limit);
    }

    public static String sendGet(String url, Map<String, String> headers, Integer limit) {
        long startTime = System.currentTimeMillis();
        HttpEntity httpEntity = null;
        HttpRequestBase method = null;
        try {
            if (null == httpClient) {
                initPool();
            }
            method = getRequest(url, HttpGet.METHOD_NAME, headers);
            HttpContext context = HttpClientContext.create();
            CloseableHttpResponse response = httpClient.execute(method, context);
            httpEntity = response.getEntity();
            if (null != httpEntity) {
                String responseBody = EntityUtils.toString(httpEntity, StandardCharsets.UTF_8);
                limit = responseBody.length() > limit ? limit : responseBody.length();
                log.info("statusCode:{},responseBody:{}", response.getStatusLine().getStatusCode(), responseBody.substring(0, limit));
                return responseBody;
            }
        }
        catch (Exception e) {
            if (null != method) {
                method.abort();
            }
            log.error(e.getMessage(), e);

        }
        finally {
            if (null != httpEntity) {
                EntityUtils.consumeQuietly(httpEntity);
            }
            log.info("cost time:{}", (System.currentTimeMillis() - startTime));
        }
        return "";
    }

    public static String sendPost(String url, String param) {
        return sendPost(url, param, null);
    }

    public static String sendPost(String url, String param, Map<String, String> headers) {
        long startTime = System.currentTimeMillis();
        HttpEntity httpEntity = null;
        HttpEntityEnclosingRequestBase method = null;
        if (null == httpClient) {
            initPool();
        }
        try {
            method = (HttpEntityEnclosingRequestBase) getRequest(url, HttpPost.METHOD_NAME, headers);
            method.setEntity(new StringEntity(param, StandardCharsets.UTF_8));
            HttpContext context = HttpClientContext.create();
            CloseableHttpResponse response = httpClient.execute(method, context);
            httpEntity = response.getEntity();
            if (null != httpEntity) {
                String responseBody = EntityUtils.toString(httpEntity, StandardCharsets.UTF_8);
                log.info("statusCode:{},responseBody:{}", response.getStatusLine().getStatusCode(), responseBody);
                return responseBody;
            }
        }
        catch (Exception e) {
            if (null != method) {
                method.abort();
            }
            log.error(e.getMessage(), e);
        }
        finally {
            if (null != httpEntity) {
                EntityUtils.consumeQuietly(httpEntity);
            }
            log.info("cost time:{}", (System.currentTimeMillis() - startTime));
        }
        return "";
    }

    public static HttpRequestBase getRequest(String url, String methodName, Map<String, String> headers) {
        HttpRequestBase method = null;
        if (HttpGet.METHOD_NAME.equals(methodName)) {
            method = new HttpGet(url);
        }
        else {
            method = new HttpPost(url);
        }
        method.addHeader("Content-Type", CONTENT_TYPE_FORM);
        method.addHeader("Accept", CONTENT_TYPE_JSON);
        if (null != headers) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                method.addHeader(entry.getKey(), entry.getValue());
            }
        }
        return method;
    }

}
