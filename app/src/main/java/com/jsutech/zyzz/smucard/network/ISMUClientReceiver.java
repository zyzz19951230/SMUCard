package com.jsutech.zyzz.smucard.network;

/**
 * Created by zyzz on 3/31/18.
 *
 */

public interface ISMUClientReceiver {
    void onClientMessageReceived(int msgId, Object data);
    SMUClient getSMUClient();
    void switchContext();
}
