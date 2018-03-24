package com.jsutech.zyzz.smucard.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by zyzz on 3/22/18.
 *
 */

public class SMUClient {

    static class Actions{
        final static String CHECK_CODE = "servlet/checkcode";
        final static String IS_LOGIN = "isLogin.action";
        final static String LOGIN = "login.action";
    }

    // 一卡通网站的网址
    private final static String BASE_URL = "https://card.swun.edu.cn/";
    // 线程池，用以执行异步请求任务
    private final static ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

    private OkHttpClient client;
    private SMUCookieJar sumCookieJar;
    private SMUHandler smuHandler;

    public SMUClient(int timeout){
        sumCookieJar = new SMUCookieJar();
        configClient(timeout);
    }

    public SMUClient(){
        this(0);
    }

    public SMUHandler getSmuHandler() {
        return smuHandler;
    }

    public void setSmuHandler(SMUHandler smuHandler) {
        this.smuHandler = smuHandler;
    }

    public void clearCookies(){
        sumCookieJar.clearCookies();
    }

    public void configClient(int timeout){
        OkHttpClient.Builder clientBuilder = null;

        if (this.client != null)
            clientBuilder = this.client.newBuilder();
        else
            // 首次创建Client对象
            clientBuilder = new OkHttpClient.Builder();

        // 配置Client
        if (timeout > 0) {
            clientBuilder.connectTimeout(timeout, TimeUnit.SECONDS).readTimeout(timeout, TimeUnit.SECONDS);
        }

        // 设置Cookie存储机制
        clientBuilder.cookieJar(sumCookieJar);

        // 创建Client对象
        // 先置空,再创建
        this.client = null;
        this.client = clientBuilder.build();
    }

    // 关闭客户端的方法，一定要在客户端不再使用的情况下调用
    // 以确保资源得到正确的释放
    public void shutdown(){
        EXECUTOR_SERVICE.shutdown();
    }

    // 同步请求接口
    // 注意：调用此方法并不会真正地执行网络请求，而是会返回一个Call对象，手动调用该对象的execute()方法后，网络请求才会真正地发出
    private Call request(Helpers.HttpMethod method, HttpUrl url, Headers headers, RequestBody requestBody, Object tag) {
        Request request = Helpers.prepareRequest(method, url, headers, requestBody, tag);
        return client.newCall(request);
    }

    // 同步GET方法
    private Call get(HttpUrl url, Headers headers, Object tag) {
        return request(Helpers.HttpMethod.GET, url, headers, null, tag);
    }

    // 同步POST方法
    private Call post(HttpUrl url, Headers headers, RequestBody requestBody, Object tag){
        return request(Helpers.HttpMethod.POST, url, headers, requestBody, tag);
    }

    public Call refreshCheckCode(){
        final Call call = get(HttpUrl.parse(Helpers.getAbsUrl(BASE_URL, Actions.CHECK_CODE)), null, null);
        // 异步执行网络请求
        EXECUTOR_SERVICE.execute(new AsyncRequest() {
            @Override
            public void doRequest() {
                try {
                    Response response = call.execute();
                    onRequestDone(response, null);
                } catch (IOException e) {
                    e.printStackTrace();
                    onRequestDone(null, e);
                }
            }

            @Override
            void onException(Exception e) {
                super.onException(e);
                if (smuHandler != null){
                    smuHandler.sendUIUpdateMessage(SMUHandler.UIUpdateMessages.NETWORK_ERROR, e);
                }
            }

            @Override
            void onResponse(Response response) {
                super.onResponse(response);
                if (smuHandler != null){
                    if (response.isSuccessful()){
                        InputStream inputStream = response.body().byteStream();
                        Bitmap checkCodeImg = BitmapFactory.decodeStream(inputStream);
                        smuHandler.sendUIUpdateMessage(SMUHandler.UIUpdateMessages.REQUEST_CHECK_CODE, checkCodeImg);
                    } else {
                        smuHandler.sendUIUpdateMessage(SMUHandler.UIUpdateMessages.SEVER_ERROR, response.message());
                    }
                }
            }
        });
        return call;
    }


    public Call login(String username, String password, String checkCode){
        // 构造登录表单
        FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add("username", username);
        formBuilder.add("password", password);
        formBuilder.add("checkCode", checkCode);
        formBuilder.add("pwd", Helpers.calcPWD(password));
        Call loginCall = post(HttpUrl.parse(Helpers.getAbsUrl(BASE_URL, Actions.LOGIN)), null, formBuilder.build(), null);
        // 发起异步请求
        EXECUTOR_SERVICE.execute(new AsyncRequest() {
            @Override
            void doRequest() {
                // 首先先测试用户是否已登录
                Call isLoginCall = post(HttpUrl.parse(Helpers.getAbsUrl(BASE_URL, Actions.IS_LOGIN)), null, null, null);
                try {
                    Response response = isLoginCall.execute();
                    if (!response.isSuccessful()){
                        onException(new Exception(""));
                    }

                } catch (IOException e) {
                    onException(e);
                    e.printStackTrace();

                }
            }
        });
        return loginCall;
    }

}
