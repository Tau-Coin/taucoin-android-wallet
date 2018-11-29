package com.mofei.tau.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.mofei.tau.R;
import com.mofei.tau.db.greendao.UTXORecordDaoUtils;
import com.mofei.tau.entity.res_post.Balance;
import com.mofei.tau.entity.res_post.BalanceRet;
import com.mofei.tau.entity.res_post.UTXOList;
import com.mofei.tau.info.SharedPreferencesHelper;
import com.mofei.tau.net.ApiService;
import com.mofei.tau.net.NetWorkManager;
import com.mofei.tau.transaction.ScriptPubkey;
import com.mofei.tau.transaction.UTXORecord;
import com.mofei.tau.util.L;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class SplashActivity extends BaseActivity {

    private List<UTXORecord> utxoRecordList;
    public static List<UTXOList.UtxosBean> utxo_list;
    public static double balance;
    public static double utxo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

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

    private void splashJump() {
        boolean isLogin= SharedPreferencesHelper.getInstance(this).getBoolean("isLogin",false);
        if (isLogin==true){
            Intent intent = new Intent(this, SendAndReceiveActivity.class);
            startActivity(intent);
            L.i("SendAndReceiveActivity");
        }else {
            Intent intent = new Intent(this, LoginActivity2.class);
            startActivity(intent);
            L.i("LoginActivity");
        }
    }
}
