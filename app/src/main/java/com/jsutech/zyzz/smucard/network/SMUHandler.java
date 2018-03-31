package com.jsutech.zyzz.smucard.network;

import android.os.Handler;
import android.os.Message;

/**
 * Created by zyzz on 3/24/18.
 * SMUClient执行网络请求后与外界（UI线程）进行通信的机制
 */

public class SMUHandler extends Handler {
    private final static int HANDLER_ID = 1681937862;
    private ISMUClientReceiver receiver;

    public SMUHandler(ISMUClientReceiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (msg.what == HANDLER_ID && msg.arg1 == msg.arg2){
            receiver.onClientMessageReceived(msg.arg1, msg.obj);
        }
    }

    public void sendClientMessage(int msgId, Object data){
        Message message = obtainMessage(HANDLER_ID, msgId, msgId, data);
        sendMessage(message);
    }

}
