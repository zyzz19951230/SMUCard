package com.jsutech.zyzz.smucard.network.exceptions;

/**
 * Created by zyzz on 2018/3/24 0024.
 *
 */

public class ServerException extends Exception {
    public ServerException(int responseCode, String responseMsg){
        this("Unexpected Http Response Code：" + responseCode + ", " + responseMsg);
    }
    public ServerException(String message) {
        super(message);
    }
}
