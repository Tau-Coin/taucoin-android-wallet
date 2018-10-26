package com.mofei.tau.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.facebook.login.LoginManager;
import com.mofei.tau.R;
import com.mofei.tau.info.SharedPreferencesHelper;
import com.mofei.tau.util.L;
import com.mofei.tau.view.CustomToolBar;

public class BountyActivity extends BaseActivity implements View.OnClickListener {

    private Button mVisitButton,mReferringButton,mTalkingButton,mBuildingButton,mWalletHomeButton,mBountyLogoutBt,mAirdropBt,mReferralBt;

    private CustomToolBar mMainCustomToolBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bounty);

        initView();

        initData();
    }




    private void initView() {
        mVisitButton=findViewById(R.id.visit);
        mReferringButton=findViewById(R.id.referring);
        mTalkingButton=findViewById(R.id.talking);
        mBuildingButton=findViewById(R.id.building);
        mWalletHomeButton=findViewById(R.id.bounty_back_wallet_home);
        mBountyLogoutBt=findViewById(R.id.bounty_logout);
        mAirdropBt=findViewById(R.id.airdrop);
        mReferralBt=findViewById(R.id.referralURL);

        titleBar();
    }

    private void titleBar() {
        //标题栏
        mMainCustomToolBar = findViewById(R.id.getCoin_titlebar);
        mMainCustomToolBar.getTitleTextView().setText("Get Coins");
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

        mVisitButton.setOnClickListener(this);

        mReferringButton.setOnClickListener(this);

        mTalkingButton.setOnClickListener(this);

        mBuildingButton.setOnClickListener(this);

        mWalletHomeButton.setOnClickListener(this);

        mBountyLogoutBt.setOnClickListener(this);

        mAirdropBt.setOnClickListener(this);

        mReferralBt.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.visit:

                String email= SharedPreferencesHelper.getInstance(BountyActivity.this).getString("email",null);
                L.e(email+"");
                if( email!=null){
                    startActivity(new Intent(BountyActivity.this,VisitEmailBindActivity.class));
                }else {
                    startActivity(new Intent(BountyActivity.this,VisitActivity.class));
                }
               // startActivity(new Intent(BountyActivity.this,VisitActivity.class));
                break;
            case R.id.referring:
                startActivity(new Intent(BountyActivity.this,ReferringActivity.class));
                break;
            case R.id.talking:
                startActivity(new Intent(BountyActivity.this,TalkingActivity.class));
                break;
            case R.id.building:
                startActivity(new Intent(BountyActivity.this,BuildingActivity.class));
                break;
            case R.id.bounty_back_wallet_home:
                finish();
                break;
            case R.id.bounty_logout:
                logout();
                break;
            case R.id.airdrop:
                startActivity(new Intent(BountyActivity.this,AirdropActivity.class));
                break;
            case R.id.referralURL:
                startActivity(new Intent(BountyActivity.this,ReferringActivity.class));
                break;
        }

    }

    private void logout() {
        LoginManager.getInstance().logOut();
        SharedPreferencesHelper.getInstance(BountyActivity.this).putBoolean("isLogin",false);
        L.i("logOut");
        startActivity(new Intent(BountyActivity.this,LoginActivity.class));
    }
}
