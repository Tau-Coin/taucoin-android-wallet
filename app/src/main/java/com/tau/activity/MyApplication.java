package com.mofei.tau.activity;


import android.app.Application;
import android.content.Context;

import com.mofei.tau.R;
import com.mofei.tau.db.GreenDaoManager;
import com.mofei.tau.util.UserInfoUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import io.taucoin.android.wallet.Wallet;

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        UserInfoUtils.sharedPreferenceUpgrade(this);
        // init greendao
        GreenDaoManager.getInstance();
    }

    public static Context getContext() {
        return context;
    }
}

