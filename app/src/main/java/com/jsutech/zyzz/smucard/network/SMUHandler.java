package com.jsutech.zyzz.smucard.network;

import android.os.Handler;
import android.os.Message;

import com.jsutech.zyzz.smucard.SMUBaseActivity;

/**
 * Created by zyzz on 3/24/18.
 * SMUClient执行网络请求后与外界（UI线程）进行通信的机制
 */

public class SMUHandler extends Handler {
    private final static int HANDLER_ID = 1681937862;
    private SMUBaseActivity context;

    public SMUHandler(SMUBaseActivity context) {
        this.context = context;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (msg.what == HANDLER_ID && msg.arg1 == msg.arg2){
            context.onUIUpdateMessageReceived(msg.arg1, msg.obj);
        }
    }

    public void sendUIUpdateMessage(int msgId, Object data){
        Message message = obtainMessage(HANDLER_ID, msgId, msgId, data);
        sendMessage(message);
    }

    // 内部类
    public static class UIUpdateMessages{
        public final static int NETWORK_ERROR = -1;
        public final static int SERVER_ERROR = -2;
        public final static int UNKNOWN_ERROR = -3;

        public final static int NOT_LOGIN = 0;
        public final static int ALREADY_LOGIN = 1;
        public final static int RECEIVE_CHECK_CODE = 2;
        public final static int USR_OR_PWD_WRONG = 3;
        public final static int CHECK_CODE_WRONG = 4;
        public static final int LOGIN_SUCCESS = 5;
        public static final int REQUEST_CANCELLED = 6;
    }
}
