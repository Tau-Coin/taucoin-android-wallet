package io.taucoin.android.wallet;

import android.app.Application;

import io.fabric.sdk.android.Fabric;
import io.taucoin.foundation.net.NetWorkManager;
import io.taucoin.foundation.util.AppUtil;
import io.taucoin.foundation.util.PropertyUtils;
import io.taucoin.android.wallet.db.GreenDaoManager;

import com.crashlytics.android.Crashlytics;
import com.facebook.stetho.Stetho;
import com.github.naturs.logger.Logger;
import com.github.naturs.logger.android.adapter.AndroidLogAdapter;
import com.github.naturs.logger.android.strategy.converter.AndroidLogConverter;
import com.mofei.tau.BuildConfig;
import com.squareup.leakcanary.LeakCanary;
import com.tau.util.UserInfoUtils;

public class MyApplication extends Application {

    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        // Prevent multiple init
        if (AppUtil.isNotMainProcess(this)) {
            return;
        }
        // Property init
        PropertyUtils.init(this);
        // NetWork init
        NetWorkManager.getInstance(this).init();

        UserInfoUtils.sharedPreferenceUpgrade(this);

        // GreenDao init
        GreenDaoManager.getInstance();

        // Logger init
        Logger.addLogAdapter(new AndroidLogAdapter("TAUCOIN", true));
        Logger.setLogConverter(new AndroidLogConverter());

        // Crashlytics
        Fabric.with(this, new Crashlytics());

        // Stetho DB Test
        if(BuildConfig.DEBUG){
            Stetho.initializeWithDefaults(this);
        }

        // Memory leak detection
        LeakCanary.install(this);
    }

    public static MyApplication getInstance() {
        return mInstance;
    }
}