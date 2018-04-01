package com.jsutech.zyzz.smucard;


import com.jsutech.zyzz.smucard.db.DBManager;
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
        // 创建客户端实例
        client = new SMUClient();
        // 初始化数据库
        DBManager.initDB();
    }

    public SMUClient getClient() {
        return client;
    }
}
