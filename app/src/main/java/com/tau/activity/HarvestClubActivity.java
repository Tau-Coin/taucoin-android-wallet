package com.mofei.tau.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.facebook.login.LoginManager;
import com.mofei.tau.R;
import com.mofei.tau.entity.HarvestClub;
import com.mofei.tau.entity.req_parameter.FBAddress;
import com.mofei.tau.entity.res_post.Club;
import com.mofei.tau.entity.res_post.ClubRet;
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

public class HarvestClubActivity extends BaseActivity implements View.OnClickListener {


    private Button mClubLogoutBt,mBackHomeBt;
    private TextView mClubIdTV,mClubHarvestPowerTV,mSelfHarvestPowerTV,mLeaderAddressTV;

    private static String TAG = "FacebookLoginDemo";
    private int clubId;
    private int cPower;
    private int sPower;
    private String clubAddress;
    private CustomToolBar mMainCustomToolBar;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x133:
                    mClubIdTV.setText(clubId);
                    mClubHarvestPowerTV.setText(cPower);
                    mSelfHarvestPowerTV.setText(sPower);
                  //  showToast("保存失败");
                    break;
                case 0x144:
                    showToast("保存成功");
                    finish();
                    // startActivity(new Intent(UsernameActivity.this,ActivityGuideDeviceList.class));
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harvest_club);

        initView();

        initData();


    }



    private void initView() {

        mClubLogoutBt=findViewById(R.id.club_logout);
        mBackHomeBt=findViewById(R.id.club_back_wallet_home);
        mClubIdTV=findViewById(R.id.club_id);
        mClubHarvestPowerTV=findViewById(R.id.club_harvest_power);
        mSelfHarvestPowerTV=findViewById(R.id.self_harvest_power);
        mLeaderAddressTV=findViewById(R.id.leader_address);
        titleBar();
    }

    private void titleBar() {
        //标题栏
        mMainCustomToolBar = findViewById(R.id.club_titlebar);
        mMainCustomToolBar.getTitleTextView().setText("Mining Club");
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

        mClubLogoutBt.setOnClickListener(this);

        mBackHomeBt.setOnClickListener(this);
        showWaitDialog();

        getClub();
    }


    public void getClub() {
        FBAddress fbAddress=new FBAddress();
        fbAddress.setFacebookid(SharedPreferencesHelper.getInstance(HarvestClubActivity.this).getString("userId","userId"));
        fbAddress.setAddress(SharedPreferencesHelper.getInstance(HarvestClubActivity.this).getString("Address","Address"));


        ApiService apiService= NetWorkManager.getApiService();
        Observable<Club<ClubRet>> observable=apiService.getClub(fbAddress);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Club<ClubRet>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Club<ClubRet> clubRetClub) {
                        L.i("Message: "+clubRetClub.getMessage().toString());
                        L.i("Clubname: "+clubRetClub.getRet().getClubname());
                        L.i("Clubid: "+clubRetClub.getRet().getClubid());
                        L.i("Cpower: "+clubRetClub.getRet().getCpower());
                        L.i("Spower: "+clubRetClub.getRet().getSpower());

                        clubId=clubRetClub.getRet().getClubid();
                        cPower=clubRetClub.getRet().getCpower();
                        sPower=clubRetClub.getRet().getSpower();
                        clubAddress=clubRetClub.getRet().getClubaddress();

                        mClubIdTV.setText(String.valueOf(clubId));
                        mClubHarvestPowerTV.setText(String.valueOf(cPower));
                        mSelfHarvestPowerTV.setText(String.valueOf(sPower));
                        mLeaderAddressTV.setText(clubAddress);
                       // handler.sendEmptyMessage(0x133);

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG,""+e.toString());
                    }

                    @Override
                    public void onComplete() {

                        hideWaitDialog();
                        L.i(""+"onComplete");
                    }
                });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.club_logout:
                logout();
                break;
            case R.id.club_back_wallet_home:
                finish();
                break;
        }

    }

    private void logout() {
        LoginManager.getInstance().logOut();
        SharedPreferencesHelper.getInstance(HarvestClubActivity.this).putBoolean("isLogin",false);
        L.i("logOut");
        startActivity(new Intent(HarvestClubActivity.this,LoginActivity.class));
    }
}
