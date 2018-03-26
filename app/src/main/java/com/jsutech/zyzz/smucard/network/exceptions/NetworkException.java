package com.jsutech.zyzz.smucard.network.exceptions;

import java.io.IOException;

/**
 * Created by zyzz on 2018/3/24 0024.
 *
 */

public class NetworkException extends IOException {
    public NetworkException(IOException e){
        super(e);
    }

    public NetworkException(String message){
        super(message);
    }

    @Override
    public String getLocalizedMessage() {
        String msg = getMessage();
        if (msg.contains("timed out") || msg.contains("timeout") || msg.contains("time out")){
            return "网络连接超时!";
        }
        else {
            return super.getLocalizedMessage();
        }
    }
}
