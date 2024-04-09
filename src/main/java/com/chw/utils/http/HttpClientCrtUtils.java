package com.chw.utils.http;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Map;

/**
 * 构建httpclient 安全的连接
 */
@Slf4j
public class HttpClientCrtUtils {

    //from-请求/响应类型
    public static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded;charset=UTF-8";

    //json-请求/响应类型
    public static final String CONTENT_TYPE_JSON = "application/json;charset=UTF-8";

    private static CloseableHttpClient httpClient;
    //连接池
    private static HttpClientConnectionManager poolingConnManager = new PoolingHttpClientConnectionManager();

    /**
     * 发送post请求
     *
     * @param url
     * @param param
     * @param headers
     * @param keyLocation 认证文件路径
     * @return
     */
    public static String sendPost(String url, String param, Map<String, String> headers, String keyLocation) {
        long startTime = System.currentTimeMillis();
        HttpEntity httpEntity = null;
        HttpEntityEnclosingRequestBase method = null;
        try {
            if (null == httpClient) {
                initClient(keyLocation);
            }
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

    /**
     * 获取需要安全认证的httpClient的实例
     *
     * @param keyLocation 认证文件路径
     * @return
     * @throws Exception
     */
    public static CloseableHttpClient initClient(String keyLocation) throws Exception {
        SSLContext sslcontext = getSSLContext(keyLocation);
        SSLConnectionSocketFactory factory = new SSLConnectionSocketFactory(sslcontext,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);

        httpClient = HttpClients.custom().setConnectionManager(poolingConnManager).setSSLSocketFactory(factory).build();
        return httpClient;
    }

    private static SSLContext getSSLContext(String keyLocation) throws KeyStoreException,
            NoSuchAlgorithmException, CertificateException, IOException, KeyManagementException {
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());

        FileInputStream instream = new FileInputStream(new File(keyLocation));
        try {
            trustStore.load(instream, "changeit".toCharArray());
        }
        finally {
            instream.close();
        }
        return SSLContexts.custom()
                .loadTrustMaterial(trustStore)
                .build();
    }

    public static void releaseInstance() {
        httpClient = null;
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


