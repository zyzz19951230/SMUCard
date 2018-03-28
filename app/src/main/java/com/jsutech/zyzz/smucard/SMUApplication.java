package com.jsutech.zyzz.smucard;


import com.jsutech.zyzz.smucard.network.SMUClient;

import org.litepal.LitePalApplication;

/**
 * Created by zyzz on 3/22/18.
 *
 */

public class SMUApplication extends LitePalApplication {
    private SMUClient client;

    @Override
    public void onCreate() {
        super.onCreate();
        client = new SMUClient();
    }

    public SMUClient getClient() {
        return client;
    }
}
