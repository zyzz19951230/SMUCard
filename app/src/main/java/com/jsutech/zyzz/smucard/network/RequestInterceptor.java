package com.jsutech.zyzz.smucard.network;

import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 请求拦截器，修改请求header
 */
public class RequestInterceptor implements Interceptor {
    private static final String TAG = "RequestInterceptor";
    // 默认请求头
    private static final Map<String, String> defaultHeaders;
    static {
        defaultHeaders = new HashMap<>();
        defaultHeaders.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        defaultHeaders.put("Accept-Encoding", "gzip, deflate, br");
        defaultHeaders.put("Accept-Language", "zh-CN,zh;q=0.8");
        defaultHeaders.put("Connection", "keep-alive");
        defaultHeaders.put("Upgrade-Insecure-Requests", "1");
        defaultHeaders.put("Host", "card.swun.edu.cn");
        defaultHeaders.put("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 UBrowser/6.2.3964.2 Safari/537.36");
    }
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request()
                .newBuilder();
        // 添加默认请求头
        for (Map.Entry<String, String> entry : defaultHeaders.entrySet()){
            builder.addHeader(entry.getKey(), entry.getValue());
        }
        Request request = builder.build();
        Log.v(TAG, "request:" + request.toString());
        Log.v(TAG, "request headers:" + request.headers().toString());
        return chain.proceed(request);
    }
}