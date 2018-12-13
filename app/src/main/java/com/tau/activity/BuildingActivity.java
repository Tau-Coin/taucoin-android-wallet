package com.tau.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mofei.tau.R;
import com.tau.info.SharedPreferencesHelper;
import com.tau.view.CustomToolBar;


public class BuildingActivity extends BaseActivity implements View.OnClickListener {
    private Button mBackWalletHomeBt;
    private TextView mEmailTV,mTelegramTV,mGithubTV,mForumTV;
    private CustomToolBar mMainCustomToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building);

        initView();

        initData();
    }



    private void initView() {

        mBackWalletHomeBt=findViewById(R.id.building_back_wallet_home);
        mEmailTV=findViewById(R.id.email);
        mTelegramTV=findViewById(R.id.telegram);
        mGithubTV=findViewById(R.id.github);
        mForumTV=findViewById(R.id.forum);

        titleBar();

    }

    private void titleBar() {
        //标题栏
        mMainCustomToolBar = findViewById(R.id.building_titlebar);
        mMainCustomToolBar.getTitleTextView().setText("Building TAU");
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
       // mEmailTV.setText(SharedPreferencesHelper.getInstance(BuildingActivity.this).getString("email",null));

        mTelegramTV.setOnClickListener(this);
        mGithubTV.setOnClickListener(this);
        mForumTV.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
           case  R.id.building_back_wallet_home:
            finish();
           // startActivity(new Intent(BuildingActivity.this,MainActivity.class));
            break;
            case R.id.telegram:
                Intent telegramIntent = new Intent();
                telegramIntent.setAction(Intent.ACTION_VIEW);
                String telegram_url = getResources().getString(R.string.telegram);
                Uri tel_uri = Uri.parse(telegram_url);
                telegramIntent.setData(tel_uri);
                startActivity(telegramIntent);
                break;
            case R.id.github:
                Intent githubIntent = new Intent();
                githubIntent.setAction(Intent.ACTION_VIEW);
                String github_url = getResources().getString(R.string.github);
                Uri git_uri = Uri.parse(github_url);
                githubIntent.setData(git_uri);
                startActivity(githubIntent);
                break;
            case R.id.forum:
                Intent forumIntent = new Intent();
                forumIntent.setAction(Intent.ACTION_VIEW);
                String forum_url = getResources().getString(R.string.forum);
                Uri for_url = Uri.parse(forum_url);
                forumIntent.setData(for_url);
                startActivity(forumIntent);
                break;
        }
    }
}
