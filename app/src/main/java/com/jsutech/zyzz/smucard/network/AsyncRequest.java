package com.jsutech.zyzz.smucard.network;

import okhttp3.Response;

/**
 * Created by zyzz on 3/24/18.
 *
 */

abstract class AsyncRequest implements Runnable{
    @Override
    public void run() {
        doRequest();
    }
    // 重写此方法，在此方法中执行网络请求
    abstract void doRequest();
    // 返回响应或者网络出错时的回调
    void onRequestDone(Response response, Exception exception){
        if (response != null)
            onResponse(response);
        else
            onException(exception);
    }
    void onResponse(Response response){

    }

    // 网络请求错误
    void onException(Exception e){

    }


}
