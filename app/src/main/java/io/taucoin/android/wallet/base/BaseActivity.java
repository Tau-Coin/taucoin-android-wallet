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
package io.taucoin.android.wallet.base;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.Subscribe;

import io.taucoin.android.wallet.module.bean.MessageEvent;
import io.taucoin.android.wallet.module.view.SplashActivity;
import io.taucoin.android.wallet.util.ActivityUtil;
import io.taucoin.android.wallet.util.FixMemLeak;
import io.taucoin.android.wallet.util.NotchUtil;
import io.taucoin.android.wallet.util.EventBusUtil;
import io.taucoin.android.wallet.util.ProgressManager;
import io.taucoin.foundation.util.ActivityManager;

public abstract class BaseActivity extends RxAppCompatActivity implements OnLoadmoreListener, OnRefreshListener {

    public Dialog mDialog = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NotchUtil.handleNotchDisplay(this);
        NotchUtil.fullScreenAll(this);

        ActivityManager.getInstance().addActivity(this);
        if (!EventBusUtil.isRegistered(this)) {
            EventBusUtil.register(this);
        }
    }

    @Subscribe
    public void onEvent(MessageEvent event){

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        ProgressManager.closeProgressDialog();
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        ActivityUtil.startActivity(intent, this, SplashActivity.class);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ProgressManager.closeProgressDialog(this);
    }

    @Override
    protected void onDestroy() {
        try {
            if(mDialog != null){
                mDialog.dismiss();
                mDialog = null;
            }
            ProgressManager.closeProgressDialog();
            FixMemLeak.fixLeak(this);
        }catch (Exception ignore){

        }
        ActivityManager.getInstance().removeActivity(this);
        if (EventBusUtil.isRegistered(this)) {
            EventBusUtil.unregister(this);
        }
        super.onDestroy();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {

    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {

    }
}