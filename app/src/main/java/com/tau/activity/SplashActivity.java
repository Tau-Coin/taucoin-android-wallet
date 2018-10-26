package com.mofei.tau.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.mofei.tau.R;
import com.mofei.tau.info.SharedPreferencesHelper;
import com.mofei.tau.util.L;


public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //设置全屏幕
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        //new个Handler
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                splashJump();
            }
        }, 3000);//3秒后跳转

    }



    //跳转
    private void splashJump() {
        boolean isLogin= SharedPreferencesHelper.getInstance(this).getBoolean("isLogin",false);
        if (isLogin==true){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            L.i("MainActivity");
        }else {
            Intent intent = new Intent(this, LoginActivity2.class);
            startActivity(intent);
            L.i("LoginActivity");
        }

        finish();
    }

}
