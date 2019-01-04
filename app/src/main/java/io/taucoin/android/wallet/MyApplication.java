/**
 * Copyright 2018 Taucoin Core Developers.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.taucoin.android.wallet;

import android.app.Application;

import io.fabric.sdk.android.Fabric;
import io.taucoin.android.wallet.db.entity.KeyValue;
import io.taucoin.android.wallet.module.presenter.UserPresenter;
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
                || LeakCanary.isInAnalyzerProcess(this)) {
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
        Fabric.with(this, new Crashlytics());

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
        UserPresenter userPresenter = new UserPresenter();
        userPresenter.initLocalData();
    }

    public static MyApplication getInstance() {
        return mInstance;
    }

    public static synchronized KeyValue getKeyValue() {
        return mKeyValue;
    }
    public static synchronized void setKeyValue(KeyValue entry) {
        mKeyValue = entry;
    }
}