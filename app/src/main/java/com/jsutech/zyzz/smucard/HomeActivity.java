package com.jsutech.zyzz.smucard;

import android.os.Bundle;
import android.app.Activity;

public class HomeActivity extends SMUBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    @Override
    public void onClientMessageReceived(int msgId, Object data) {

    }
}
