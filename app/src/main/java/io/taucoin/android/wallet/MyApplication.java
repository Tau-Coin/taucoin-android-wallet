package io.taucoin.android.wallet;

import android.app.Application;

import io.fabric.sdk.android.Fabric;
import io.taucoin.android.wallet.base.TransmitKey;
import io.taucoin.android.wallet.db.entity.KeyValue;
import io.taucoin.android.wallet.db.util.KeyValueDaoUtils;
import io.taucoin.android.wallet.util.SharedPreferencesHelper;
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

public class MyApplication extends Application {

    private static MyApplication mInstance;
    private static volatile KeyValue mKeyValue;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        // Prevent multiple init
        if (AppUtil.isNotMainProcess(this)
                // This process is dedicated to LeakCanary for heap analysis.
                && LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }

//        FontUtil.replaceSystemDefaultFont(this, "fonts/test.ttf");

        // Property init
        PropertyUtils.init(this);
        // NetWork init
        NetWorkManager.getInstance(this).init();

        // GreenDao init
        GreenDaoManager.getInstance();

        // Logger init
        Logger.addLogAdapter(new AndroidLogAdapter("TAUCOIN", true));
        Logger.setLogConverter(new AndroidLogConverter());

        // Crashlytics
        if(!BuildConfig.DEBUG){
            Fabric.with(this, new Crashlytics());
        }

        // Stetho DB Test
        if(BuildConfig.DEBUG){
            Stetho.initializeWithDefaults(this);
        }
        // Normal app init code...
        initKeyValue();

        // Memory leak detection
        LeakCanary.install(this);
    }

    private void initKeyValue() {
        String publicKey = SharedPreferencesHelper.getInstance().getString(TransmitKey.PUBLIC_KEY, "0332BE723E222C0137917435EF6B128BD373CE9F8E0DCF65A9E9406EB240ED9D11");
        mKeyValue = KeyValueDaoUtils.getInstance().queryByPubicKey(publicKey);
    }

    public static MyApplication getInstance() {
        return mInstance;
    }

    public static KeyValue getKeyValue() {
        return mKeyValue;
    }
    public static void setKeyValue(KeyValue entry) {
        mKeyValue = entry;
    }
}