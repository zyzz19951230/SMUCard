package com.jsutech.zyzz.smucard.network;

import android.graphics.BitmapFactory;

import com.jsutech.zyzz.smucard.network.exceptions.ClientException;
import com.jsutech.zyzz.smucard.network.exceptions.NetworkException;
import com.jsutech.zyzz.smucard.network.exceptions.ServerException;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
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

    // 同步GET方法
    private Call get(String action, Headers headers, Object tag) {
        return request(Helpers.HttpMethod.GET, HttpUrl.parse(Helpers.getAbsUrl(BASE_URL, action)), headers, null, tag);
    }

    // 同步POST方法
    private Call post(String action, Headers headers, RequestBody requestBody, Object tag){
        return request(Helpers.HttpMethod.POST,  HttpUrl.parse(Helpers.getAbsUrl(BASE_URL, action)), headers, requestBody, tag);
    }

    public Call refreshCheckCode(){
        final Call call = get(Actions.CHECK_CODE, null, null);
        // 异步执行网络请求
        EXECUTOR_SERVICE.execute(new SMURequest(getSmuHandler()){
            @Override
            void doRequest() {
                try {
                    Response response = call.execute();
                    // 请求不成功
                    if (!response.isSuccessful()){
                        onException(new ServerException(response.code(), response.message()));
                    } else {
                        // 网络请求成功，读取验证码图片
                        onResponse(ClientMessages.RECEIVE_CHECK_CODE, BitmapFactory.decodeStream(response.body().byteStream()));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    // 网络请求发生错误
                    onException(new NetworkException(e));
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
        final Call loginCall = post(Actions.LOGIN, null, formBuilder.build(), null);
        // 发起异步请求
        EXECUTOR_SERVICE.execute(new SMURequest(getSmuHandler()) {
            @Override
            void doRequest() {
                // 先测试用户是否已登录
                Call isLoginCall = post(Actions.IS_LOGIN, null, null, null);
                try {
                    Response isLoginResponse = isLoginCall.execute();
                    if (!isLoginResponse.isSuccessful()){
                        onException(new ServerException(isLoginResponse.code(), isLoginResponse.message()));
                    } else {
                        if (isLoginResponse.body().string().endsWith("true")){
                            // 用户已登录
                            onException(new ClientException("用户已登录！", ClientMessages.ALREADY_LOGIN));

                        } else {
                            // 用户未登录，发送登录请求
                            Response loginResponse = loginCall.execute();
                            if (!loginResponse.isSuccessful()){
                                onException(new ServerException(loginResponse.code(), loginResponse.message()));
                            } else {
                                // 分析服务器返回的结果
                                String bodyText = loginResponse.body().string();
                                if (Helpers.isCheckCodeWrong(bodyText)){
                                    // 验证码错误
                                    onException(new ClientException("验证码错误！", ClientMessages.CHECK_CODE_WRONG));
                                } else if(!Helpers.isIndexPage(bodyText)){
                                    // 用户名或者密码错误
                                    onException(new ClientException("用户名或密码错误", ClientMessages.USR_OR_PWD_WRONG));
                                } else {
                                    // 登录成功
                                    onResponse(ClientMessages.LOGIN_SUCCESS, "登录成功");
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    onException(new NetworkException(e));
                }
            }
        });
        return loginCall;
    }

    // 内部类：客户端消息常量定义
    public static class ClientMessages {
        public final static int NETWORK_ERROR = -1;
        public final static int SERVER_ERROR = -2;
        public final static int UNKNOWN_ERROR = -3;

        public final static int NOT_LOGIN_YET = 0;
        public final static int ALREADY_LOGIN = 1;
        public final static int RECEIVE_CHECK_CODE = 2;
        public final static int USR_OR_PWD_WRONG = 3;
        public final static int CHECK_CODE_WRONG = 4;
        public static final int LOGIN_SUCCESS = 5;
        public static final int REQUEST_CANCELLED = 6;
    }
}
