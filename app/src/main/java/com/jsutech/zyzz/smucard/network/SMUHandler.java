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
            context.onClientMessageReceived(msg.arg1, msg.obj);
        }
    }

    public void sendClientMessage(int msgId, Object data){
        Message message = obtainMessage(HANDLER_ID, msgId, msgId, data);
        sendMessage(message);
    }

}
