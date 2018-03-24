package com.jsutech.zyzz.smucard.network.exceptions;

/**
 * Created by Administrator on 2018/3/24 0024.
 */

public class ClientException extends Exception {
    private int errorCode;
    public ClientException(String message, int errorCode){
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
