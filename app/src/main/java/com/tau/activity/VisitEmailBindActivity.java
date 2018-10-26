package com.mofei.tau.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mofei.tau.R;
import com.mofei.tau.info.SharedPreferencesHelper;
import com.mofei.tau.view.CustomToolBar;

public class VisitEmailBindActivity extends BaseActivity implements View.OnClickListener {
    private TextView mEmailTV;

    private CustomToolBar mMainCustomToolBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_email_bind);
        mEmailTV=findViewById(R.id.email_tv);

        findViewById(R.id.visit_email__back_wallet_home).setOnClickListener(this);

        mEmailTV.setText( SharedPreferencesHelper.getInstance(VisitEmailBindActivity.this).getString("email",null));
       /* Intent intent=getIntent();
        String email=intent.getStringExtra("email");
      //  Log.e("email",email+"ee");
        if (email!=null){
            mEmailTV.setText(email);
            SharedPreferencesHelper.getInstance(VisitEmailBindActivity.this).putString("email",email);

        }else {
            showToast("邮箱为空");
           // Log.e("email",email+"ff");
        }*/
       titleBar();
    }

    private void titleBar() {
        //标题栏
        mMainCustomToolBar = findViewById(R.id.visit3_titlebar);
        mMainCustomToolBar.getTitleTextView().setText("Visit TAU");
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
        if(v.getId()==R.id.visit_email__back_wallet_home){
            finish();
            startActivity(new Intent(VisitEmailBindActivity.this,MainActivity.class));
        }
    }
}
