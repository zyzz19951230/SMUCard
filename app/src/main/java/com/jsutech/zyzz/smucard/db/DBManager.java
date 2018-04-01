package com.jsutech.zyzz.smucard.db;

import com.jsutech.zyzz.smucard.db.models.UserProfile;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

public class DBManager {
    public static void initDB(){
        // 初始化数据库
        Connector.getDatabase();
    }

    // 查询用户信息
    public static UserProfile findUserProfile(String suid){
        return DataSupport.where("SUID = ?", suid).findFirst(UserProfile.class);
    }

    public static void saveOrUpdateUserProfile(UserProfile userProfile){
        userProfile.saveOrUpdate("SUID = ?", userProfile.getSUID());
    }

}
