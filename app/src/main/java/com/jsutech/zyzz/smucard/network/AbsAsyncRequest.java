package com.jsutech.zyzz.smucard.network;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by zyzz on 3/24/18.
 *
 */

abstract class AbsAsyncRequest implements Runnable{
    @Override
    public void run() {
        doRequest();
    }
    // 重写此方法，在此方法中执行网络请求
    abstract void doRequest();

    abstract void onResponse(int code, Object data);

    // 网络请求错误
    abstract void onException(Exception e);


}
