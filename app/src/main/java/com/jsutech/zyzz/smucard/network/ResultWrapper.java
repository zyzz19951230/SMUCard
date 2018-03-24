package com.jsutech.zyzz.smucard.network;


import okhttp3.Call;

/**
 * Created by Administrator on 2018/3/24 0024.
 * 请求结果包装器
 */

public class ResultWrapper {
    private Exception exception;
    private Object data;
    private Call call;

    public Call getCall() {
        return call;
    }

    public Exception getException() {
        return exception;
    }

    public Object getData() {
        return data;
    }

    public ResultWrapper(Exception exception, Object data, Call call) {
        this.exception = exception;
        this.data = data;
        this.call = call;
    }
}
