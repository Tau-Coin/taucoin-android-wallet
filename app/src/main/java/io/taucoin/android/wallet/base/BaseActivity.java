package io.taucoin.android.wallet.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import io.taucoin.android.wallet.util.KeyboardUtils;
import io.taucoin.foundation.util.ActivityManager;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.addActivity(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Subscribe
    public void onEvent(String event){

    }

    @Override
    protected void onDestroy() {
        try {
            KeyboardUtils.hideSoftInput(this);
        }catch (Exception ignore){

        }
        ActivityManager.removeActivity(this);
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }
}
