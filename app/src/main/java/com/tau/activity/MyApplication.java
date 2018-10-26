package com.mofei.tau.activity;


import android.app.Application;
import android.content.Context;

import com.mofei.tau.db.GreenDaoManager;
import com.mofei.tau.net.NetWorkManager;


public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        //初始化greendao
        GreenDaoManager.getInstance();

    }

    public static Context getContext() {
        return context;
    }
}