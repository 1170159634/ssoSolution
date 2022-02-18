package me.xuanming.utils;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.bouncycastle.util.Strings;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


/**
 * 发送http请求的工具类
 *
 *
 * @Author: xingxuanming
 * @Date: 2021/12/28
 */
@Slf4j
public class HttpClientUtils {


    /**
     * get请求，忽略了tls校验
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static String get(String url, int timeout, TimeUnit timeUnit) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        return Objects.requireNonNull(buildHttpClient(timeout, timeUnit)
                .build()
                .newCall(request)
                .execute().body()).string();
    }

    public static String get(String url, HashMap<String, String> header, int timeout, TimeUnit timeUnit)
            throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .headers(Headers.of(header))
                .get()
                .build();
        OkHttpClient.Builder builder = buildHttpClient(timeout, timeUnit);
        return Objects.requireNonNull(builder
                .build()
                .newCall(request)
                .execute().body()).string();
    }

    public static String post(String url, String requestBody, HashMap<String, String> header, int timeout, TimeUnit timeUnit)
            throws IOException {
        RequestBody requestBody1 = RequestBody.Companion.create(requestBody, FORM_CONTENT_TYPE);
        Request request;
        Request.Builder build = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json;charset=utf-8")
                .post(requestBody1);
        if (Objects.nonNull(header)) {
            request = build.headers(Headers.of(header)).build();
        } else {
            request = build.build();
        }
        OkHttpClient.Builder builder = buildHttpClient(timeout, timeUnit);
        return Objects.requireNonNull(builder
                .build()
                .newCall(request)
                .execute().body()).string();
    }

    public static String delete(String url, HashMap<String, String> header, int timeout, TimeUnit timeUnit)
            throws IOException {

        Request request;
        Request.Builder build = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json;charset=utf-8")
                .delete();
        if (Objects.nonNull(header)) {
            request = build.headers(Headers.of(header)).build();
        } else {
            request = build.build();
        }
        OkHttpClient.Builder builder = buildHttpClient(timeout, timeUnit);
        return Objects.requireNonNull(builder
                .build()
                .newCall(request)
                .execute().body()).string();
    }

    public static String put(String url, String requestBody, HashMap<String, String> header, int timeout, TimeUnit timeUnit)
            throws IOException {

        Request request;
        Request.Builder build = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json;charset=utf-8")
                .put(RequestBody.create(Strings.toByteArray(requestBody)));
        if (Objects.nonNull(header)) {
            request = build.headers(Headers.of(header)).build();
        } else {
            request = build.build();
        }
        OkHttpClient.Builder builder = buildHttpClient(timeout, timeUnit);
        return Objects.requireNonNull(builder
                .build()
                .newCall(request)
                .execute()
                .body()).string();
    }

    public static OkHttpClient.Builder buildHttpClient(int timeout, TimeUnit timeUnit) {
        try {
            TrustManager[] trustAllCerts = buildTrustManagers();
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier((hostname, session) -> true);
            builder.connectTimeout(timeout, timeUnit).readTimeout(timeout, timeUnit).build();
            return builder;
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            log.error("buildHttpClient error", e);
            OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(timeout, timeUnit).readTimeout(timeout, timeUnit).build();
            return okHttpClient.newBuilder();
        }
    }

    private static TrustManager[] buildTrustManagers() {
        return new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };
    }

    public static final MediaType FORM_CONTENT_TYPE
            = MediaType.parse("application/json; charset=utf-8");
}
