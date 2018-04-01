package com.jsutech.zyzz.smucard;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jsutech.zyzz.smucard.network.ISMUClientReceiver;
import com.jsutech.zyzz.smucard.network.SMUClient;
import com.jsutech.zyzz.smucard.network.SMUHandler;
import com.jsutech.zyzz.smucard.ui.fragments.BaseFragment;
import com.jsutech.zyzz.smucard.ui.fragments.ICommunicator;

/**
 * Created by zyzz on 3/24/18.
 *
 */

abstract public class BaseSMUActivity extends AppCompatActivity implements ISMUClientReceiver, ICommunicator{

    abstract public void onClientMessageReceived(int msgId, Object data);
    abstract public SMUClient getClient();

    @Override
    public void sendMessage(ICommunicator receiver, int msgID, Object data) {
        receiver.onMessageReceived(msgID, data, this);
    }
}
