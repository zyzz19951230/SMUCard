package com.jsutech.zyzz.smucard;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jsutech.zyzz.smucard.network.SMUHandler;

/**
 * Created by zyzz on 3/24/18.
 *
 */

abstract public class SMUBaseActivity extends AppCompatActivity {
    protected SMUHandler smuHandler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        smuHandler = new SMUHandler(this);
    }

    abstract public void onUIUpdateMessageReceived(int msgId, Object data);
}
