package com.tau.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.facebook.login.LoginManager;
import com.mofei.tau.R;
import com.tau.entity.res_post.Balance;
import com.tau.entity.res_post.BalanceRet;
import com.tau.info.SharedPreferencesHelper;
import io.taucoin.android.wallet.net.service.ApiService;
import io.taucoin.foundation.net.NetWorkManager;

import com.tau.util.L;
import com.tau.view.CustomToolBar;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;

public class BalanceActivity extends BaseActivity implements View.OnClickListener {

    private Button mBlaLogoutBt,mBackWalletHome;
    private TextView mBalanceTauTV,mRewardTauTV;

    private CustomToolBar mMainCustomToolBar;
    private int status;
    private double double_coins;
    private double reward;

    Handler hand = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x20:
                    if(status==401){
                        showToast("Email does not exist");
                    }else if(status==402){
                        showToast("Fail to get balance");
                    }
                    break;
                case 0x21:
                    Double double_8=new Double("100000000");
                    Double coin_double=new Double(double_coins);
                    L.e("转换后的数据：　"+coin_double/double_8);

                    mBalanceTauTV.setText(""+coin_double/double_8);
                    mRewardTauTV.setText(String.valueOf(reward));
                    break;

            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);

        initView();

        initData();

    }

    private void initView() {
        mBlaLogoutBt=findViewById(R.id.balanceLogout);

        mBackWalletHome=findViewById(R.id.back_wallet_home);

        mBalanceTauTV=findViewById(R.id.balance_tau);

        mRewardTauTV=findViewById(R.id.reward_tau);

        NetWorkManager.getInstance(this).init();

        titleBar();
    }

    private void titleBar() {
        //标题栏
        mMainCustomToolBar = findViewById(R.id.balance_titlebar);
        mMainCustomToolBar.getTitleTextView().setText("Balance");
        mMainCustomToolBar.getTitleTextView().setTextColor(Color.WHITE);
        mMainCustomToolBar.getTitleTextView().setTextSize(22);
        mMainCustomToolBar.disableLeftTextView();
        //mMainCustomToolBar.disableRightView();

        mMainCustomToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initData() {
        mBlaLogoutBt.setOnClickListener(this);
        mBackWalletHome.setOnClickListener(this);
        showWaitDialog();
        getBalanceData(SharedPreferencesHelper.getInstance(BalanceActivity.this).getString("email",""));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.balanceLogout:
                logout();
                break;
            case R.id.back_wallet_home:
                finish();
                break;
        }
    }

    private void logout() {
        LoginManager.getInstance().logOut();
        SharedPreferencesHelper.getInstance(BalanceActivity.this).putBoolean("isLogin",false);
        L.i("logOut");
        startActivity(new Intent(BalanceActivity.this,LoginActivity.class));
    }

    public void getBalanceData(String email) {
        Map<String,String> emailMap=new HashMap<>();
        emailMap.put("email",email);
        ApiService apiService=NetWorkManager.createApiService(ApiService.class);
        Observable<Balance<BalanceRet>> observable=apiService.getBalance2(emailMap);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Balance<BalanceRet>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Balance<BalanceRet> balanceRetBalance) {

                        L.e(balanceRetBalance.getStatus()+"");
                        L.e(balanceRetBalance.getMessage());
                        status=balanceRetBalance.getStatus();
                        double_coins=balanceRetBalance.getRet().getCoins();
                        reward= balanceRetBalance.getRet().getRewards();

                        SharedPreferencesHelper.getInstance(BalanceActivity.this).putString("balance",""+double_coins);
                        SharedPreferencesHelper.getInstance(BalanceActivity.this).putString("reward",""+reward);
                        SharedPreferencesHelper.getInstance(BalanceActivity.this).putString("utxo",""+balanceRetBalance.getRet().getUtxo());

                        L.e("Coins"+balanceRetBalance.getRet().getCoins());
                        L.e(balanceRetBalance.getRet().getPubkey());
                        L.e(balanceRetBalance.getRet().getUtxo()+"");
                        L.e(balanceRetBalance.getRet().getRewards()+"");

                    }

                    @Override
                    public void onError(Throwable e) {
                        hideWaitDialog();
                        L.e("error");
                        e.printStackTrace();
                        hand.sendEmptyMessage(0x20);
                    }

                    @Override
                    public void onComplete() {
                        hand.sendEmptyMessage(0x21);
                        hideWaitDialog();
                        L.e("complete");
                    }
                });
    }



    /**
     *通过okhttpClient来设置证书
     * @param clientBuilder OKhttpClient.builder
     * @param certificates 读取证书的InputStream
     * */
    public void setCertificates(OkHttpClient.Builder clientBuilder, InputStream... certificates) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory .generateCertificate(certificate));
                try {
                    if (certificate != null)
                        certificate.close();
                } catch (IOException e) {

                }
            }
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance( TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
            }
            X509TrustManager trustManager = (X509TrustManager) trustManagers[0];
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            clientBuilder.sslSocketFactory(sslSocketFactory, trustManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
