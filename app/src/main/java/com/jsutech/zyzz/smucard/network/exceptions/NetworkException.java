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
}
