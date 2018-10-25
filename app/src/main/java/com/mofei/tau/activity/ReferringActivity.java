package com.mofei.tau.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.mofei.tau.R;
import com.mofei.tau.entity.req_parameter.FBAddress;
import com.mofei.tau.entity.res_post.ReferralURL;
import com.mofei.tau.info.SharedPreferencesHelper;
import com.mofei.tau.net.ApiService;
import com.mofei.tau.net.NetWorkManager;
import com.mofei.tau.util.L;
import com.mofei.tau.view.CustomToolBar;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
/**
 * @author ly
 * @version 1.0
 * 创建时间：2018/9/12
 * 类说明:
 */

public class ReferringActivity extends BaseActivity implements View.OnClickListener {

    private Button mBackWalletHomeBt,mLogoutBt,mGenerateReferralURLBt;
    private TextView mReferralURLTV;

    private CustomToolBar mMainCustomToolBar;
    /*String userId;
    String Address;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referring);


        initView();

        initData();

    }


    private void initView() {
        mBackWalletHomeBt=findViewById(R.id.referring_back_wallet_home);
        mLogoutBt=findViewById(R.id.referring_logout);
        mReferralURLTV=findViewById(R.id.referral_url);
        mGenerateReferralURLBt=findViewById(R.id.generate_url);
        titleBar();
    }

    private void titleBar() {
        //标题栏
        mMainCustomToolBar = findViewById(R.id.referring_titlebar);
        mMainCustomToolBar.getTitleTextView().setText("Referring TAU");
        mMainCustomToolBar.getTitleTextView().setTextColor(Color.WHITE);
        mMainCustomToolBar.getTitleTextView().setTextSize(22);
        mMainCustomToolBar.disableLeftTextView();
        mMainCustomToolBar.disableRightView();

        mMainCustomToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initData() {
        mBackWalletHomeBt.setOnClickListener(this);
        mLogoutBt.setOnClickListener(this);
        mGenerateReferralURLBt.setOnClickListener(this);

        showWaitDialog();
        referralURL();

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.referring_back_wallet_home:
                finish();
          //  startActivity(new Intent(ReferringActivity.this,MainActivity.class));
            break;
            case R.id.referring_logout:
                logout();
                break;
            case R.id.generate_url:
                showWaitDialog();
                referralURL();
                break;
        }
    }

    private void referralURL() {

        FBAddress fbAddress=new FBAddress();
        fbAddress.setFacebookid(userId );
        fbAddress.setAddress( address);
        L.i("userId"+userId+"  Address  "+address);

        ApiService apiService= NetWorkManager.getApiService();
        Observable<ReferralURL> observable=apiService.getreferralURL(fbAddress);
        observable.subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ReferralURL>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ReferralURL referralURL) {
                        L.i("Message: "+referralURL.getMessage());
                        L.i("Status: "+referralURL.getStatus());
                        L.i("Referral_url: "+referralURL.getReferral_url());

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(referralURL.getMessage().equals("success")){
                                    mGenerateReferralURLBt.setVisibility(View.VISIBLE);
                                }else {
                                    mReferralURLTV.setText( referralURL.getReferral_url().toString());
                                    mReferralURLTV.setBackgroundColor(Color.parseColor("#4C4C4C"));
                                    mReferralURLTV.setTextColor(Color.WHITE);
                                    mGenerateReferralURLBt.setVisibility(View.GONE);
                                }

                            }
                        });


                    }

                    @Override
                    public void onError(Throwable e) {
                        L.i("onError");
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        L.i(""+"onComplete");
                        hideWaitDialog();

                    }
                });
    }

    private void logout() {
        LoginManager.getInstance().logOut();
        SharedPreferencesHelper.getInstance(ReferringActivity.this).putBoolean("isLogin",false);
        L.i("logOut");
        startActivity(new Intent(ReferringActivity.this,LoginActivity.class));
    }
}
