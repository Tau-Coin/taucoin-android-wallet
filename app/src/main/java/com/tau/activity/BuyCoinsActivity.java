package com.mofei.tau.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.mofei.tau.R;
import com.mofei.tau.entity.req_parameter.FBAddress;
import com.mofei.tau.entity.res_put.BuyCoins;
import com.mofei.tau.entity.res_put.BuyCoinsRet;
import com.mofei.tau.info.SharedPreferencesHelper;
import com.mofei.tau.net.ApiService;
import com.mofei.tau.net.NetWorkManager;
import com.mofei.tau.util.ZXingUtils;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BuyCoinsActivity extends BaseActivity implements View.OnClickListener {
    private Button mBuyTauCoinsBt,mBackWalletHome;

    private static String TAG = "FacebookLoginDemo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_coins);

        initView();

        initData();
    }

    private void initData() {
        mBuyTauCoinsBt.setOnClickListener(this);
        mBackWalletHome.setOnClickListener(this);

    }

    private void initView() {
        mBuyTauCoinsBt=findViewById(R.id.buy_tau_coins);
        mBackWalletHome=findViewById(R.id.coins_back_wallet_home);
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.coins_back_wallet_home:
                finish();
                break;
            case R.id.buy_tau_coins:
                startActivity(new Intent(BuyCoinsActivity.this,BuyCoinsAddressActivity.class));
              //  getBtcadd();
              //  showWaitDialog();
                break;
        }

    }
}
