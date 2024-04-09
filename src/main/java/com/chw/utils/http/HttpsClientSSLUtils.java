package com.chw.utils.http;


import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

/**
 * http和https都可以兼容
 */
@Slf4j
public class HttpsClientSSLUtils {

    //from-请求/响应类型
    public static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded;charset=UTF-8";

    //json-请求/响应类型
    public static final String CONTENT_TYPE_JSON = "application/json;charset=UTF-8";

    public static HttpClient httpClient;

    public static String doGet(String url) {
        return doGet(url, null);
    }

    public static String doGet(String url, Map<String, String> headers) {
        long startTime = System.currentTimeMillis();
        try {
            if (null == httpClient) {
                httpClient = new SSLClient();
            }
            HttpGet httpGet = (HttpGet) getRequest(url, HttpGet.METHOD_NAME, headers);
            HttpResponse response = httpClient.execute(httpGet);
            if (null != response) {
                String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                log.info("statusCode:{},responseBody:{}", response.getStatusLine().getStatusCode(), responseBody);
                return responseBody;
            }
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        finally {
            log.info("cost time:{}", (System.currentTimeMillis() - startTime));
        }
        return "";
    }

    public static String doPost(String url, String param) {
        return doPost(url, param, null);
    }

    public static String doPost(String url, String param, Map<String, String> headers) {
        long startTime = System.currentTimeMillis();
        try {
            if (null == httpClient) {
                httpClient = new SSLClient();
            }
            HttpPost httpPost = (HttpPost) getRequest(url, HttpPost.METHOD_NAME, headers);
            httpPost.setEntity(new StringEntity(param, StandardCharsets.UTF_8));
            HttpResponse response = httpClient.execute(httpPost);
            if (null != response) {
                String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                log.info("statusCode:{},responseBody:{}", response.getStatusLine().getStatusCode(), responseBody);
                return responseBody;
            }
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        finally {
            log.info("cost time:{}", (System.currentTimeMillis() - startTime));
        }
        return "";
    }

    static class SSLClient extends DefaultHttpClient {
        public SSLClient() throws NoSuchAlgorithmException, KeyManagementException {
            super();
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[]{};
                }
            };
            ctx.init(null, new TrustManager[]{tm}, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager ccm = this.getConnectionManager();
            SchemeRegistry sr = ccm.getSchemeRegistry();
            sr.register(new Scheme("https", 443, ssf));
        }
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

    public static void releaseClient() {
        httpClient = null;
    }

}
