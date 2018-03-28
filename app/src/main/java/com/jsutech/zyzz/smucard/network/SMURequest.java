package com.jsutech.zyzz.smucard.network;

import com.jsutech.zyzz.smucard.network.exceptions.ClientException;
import com.jsutech.zyzz.smucard.network.exceptions.NetworkException;
import com.jsutech.zyzz.smucard.network.exceptions.ServerException;

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
        smuHandler.sendClientMessage(code, data);
    }

    @Override
    void onException(Exception e) {
        if (e instanceof NetworkException){
            smuHandler.sendClientMessage(SMUClient.ClientMessages.NETWORK_ERROR, e);
        } else if (e instanceof ServerException){
            smuHandler.sendClientMessage(SMUClient.ClientMessages.SERVER_ERROR, e);
        } else if (e instanceof ClientException){
            ClientException exception = (ClientException) e;
            smuHandler.sendClientMessage(exception.getErrorCode(), e);
        } else {
            smuHandler.sendClientMessage(SMUClient.ClientMessages.UNKNOWN_ERROR, e);
        }
    }
}
