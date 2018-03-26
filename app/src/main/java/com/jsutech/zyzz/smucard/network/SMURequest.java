package com.jsutech.zyzz.smucard.network;

import com.jsutech.zyzz.smucard.network.exceptions.ClientException;
import com.jsutech.zyzz.smucard.network.exceptions.NetworkException;
import com.jsutech.zyzz.smucard.network.exceptions.ServerException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/3/24 0024.
 *
 */

abstract class SMURequest extends AbsAsyncRequest {
    private SMUHandler smuHandler;
    SMURequest(SMUHandler smuHandler){
        this.smuHandler = smuHandler;
    }
    abstract void doRequest();

    @Override
    void onResponse(int code, Object data) {
        smuHandler.sendUIUpdateMessage(code, data);
    }

    @Override
    void onException(Exception e) {
        if (e instanceof NetworkException){
            smuHandler.sendUIUpdateMessage(SMUHandler.UIUpdateMessages.NETWORK_ERROR, e);
        } else if (e instanceof ServerException){
            smuHandler.sendUIUpdateMessage(SMUHandler.UIUpdateMessages.SERVER_ERROR, e);
        } else if (e instanceof ClientException){
            ClientException exception = (ClientException) e;
            smuHandler.sendUIUpdateMessage(exception.getErrorCode(), e);
        } else {
            smuHandler.sendUIUpdateMessage(SMUHandler.UIUpdateMessages.UNKNOWN_ERROR, e);
        }
    }
}
