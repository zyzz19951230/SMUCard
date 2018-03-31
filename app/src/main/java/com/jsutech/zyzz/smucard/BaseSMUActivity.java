package com.jsutech.zyzz.smucard;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jsutech.zyzz.smucard.network.ISMUClientReceiver;
import com.jsutech.zyzz.smucard.network.SMUClient;

/**
 * Created by zyzz on 3/24/18.
 *
 */

public class BaseSMUActivity extends AppCompatActivity implements ISMUClientReceiver{
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    public void onClientMessageReceived(int msgId, Object data) {

    }

    @Override
    public SMUClient getSMUClient() {
        return null;
    }

    @Override
    public void switchContext() {

    }
}
