package com.tau.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.mofei.tau.R;
import com.tau.util.L;
import com.tau.view.CustomToolBar;

public class AboutActivity extends BaseActivity implements View.OnClickListener {

    private CustomToolBar mMainCustomToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);


        titleBar();
    }


    private void titleBar() {
        //标题栏
        mMainCustomToolBar = findViewById(R.id.about_titlebar);
        mMainCustomToolBar.getTitleTextView().setText("About");
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

    @Override
    public void onClick(View v) {

    }
}
