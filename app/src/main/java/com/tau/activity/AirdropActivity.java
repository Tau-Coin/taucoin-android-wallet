package com.tau.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.mofei.tau.R;
import com.tau.view.CustomToolBar;

public class AirdropActivity extends BaseActivity implements View.OnClickListener {

    private CustomToolBar mMainCustomToolBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airdrop);

        findViewById(R.id.airdrop_back_wallet_home).setOnClickListener(this);
        findViewById(R.id.airdrop_tutorial).setOnClickListener(this);
        titleBar();
    }

    private void titleBar() {
        //标题栏
        mMainCustomToolBar = findViewById(R.id.airdrop_titlebar);
        mMainCustomToolBar.getTitleTextView().setText("TAU Airdrop");
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.airdrop_back_wallet_home:
                finish();
                break;
            case R.id.airdrop_tutorial:
                Intent githubIntent = new Intent();
                githubIntent.setAction(Intent.ACTION_VIEW);
                String github_url = getResources().getString(R.string.airdrop_tutorial);
                Uri git_uri = Uri.parse(github_url);
                githubIntent.setData(git_uri);
                startActivity(githubIntent);
                break;
        }
    }
}
