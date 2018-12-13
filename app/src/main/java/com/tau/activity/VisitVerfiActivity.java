package com.tau.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mofei.tau.R;
import com.tau.entity.req_parameter.FBAddressEmlVeri;
import com.tau.entity.res_post.StatusMessage;
import com.tau.info.SharedPreferencesHelper;
import io.taucoin.android.wallet.net.service.ApiService;
import io.taucoin.foundation.net.NetWorkManager;
import com.tau.util.L;
import com.tau.view.CustomToolBar;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class VisitVerfiActivity extends BaseActivity implements View.OnClickListener {


    private EditText mEmailET,mConfirmationET;
    private String mConfirmationCode;
    private Button mBindEmail;
   // private LinearLayout mConfirmationLayout,mBindingSuccessLayout;
    private TextView mBindEmailTV;
    private String email;
    private CustomToolBar mMainCustomToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_verfi);

        initView();
        initData();
    }

    private void initView() {
        mEmailET=findViewById(R.id.email_edittext);
        mConfirmationET=findViewById(R.id.email_confirmation_edittext);
        mBindEmail=findViewById(R.id.bind);
      //  mConfirmationLayout=findViewById(R.id.confirmation_layout);
      //  mBindingSuccessLayout=findViewById(R.id.binding_success_layout);
        mBindEmailTV=findViewById(R.id.binding_email);
        titleBar();

    }

    private void titleBar() {
        //标题栏
        mMainCustomToolBar = findViewById(R.id.visit2_titlebar);
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

    private void initData() {

        /* String email1= SharedPreferencesHelper.getInstance(VisitVerfiActivity.this).getString("email",null);
        if(email1!=null){
            mConfirmationLayout.setVisibility(View.GONE);
            mBindingSuccessLayout.setVisibility(View.VISIBLE);
            mBindEmailTV.setText(email1);

        }*/

        Intent intent=getIntent();
        email=intent.getStringExtra("email");
        L.e("email",email+"ee");
        if (email!=null){
            mEmailET.setText(email,null);
            SharedPreferencesHelper.getInstance(VisitVerfiActivity.this).putString("email",email);
        }else {
            showToast("邮箱为空");
            Log.e("email",email+"ff");
        }

        mBindEmail.setOnClickListener(this);
        findViewById(R.id.visit_verfi__back_wallet_home).setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bind:
                showWaitDialog();
                mConfirmationCode=mConfirmationET.getText().toString().trim();
                bindEmail(mConfirmationCode);
                break;
            case R.id.visit_verfi__back_wallet_home:
                finish();
                break;
        }

    }

    private void bindEmail(String confirmation) {
        String userId= SharedPreferencesHelper.getInstance(VisitVerfiActivity.this).getString("userId","userId");
        String address=SharedPreferencesHelper.getInstance(VisitVerfiActivity.this).getString("Address","Address");

        FBAddressEmlVeri fbAddressEmlVeri=new FBAddressEmlVeri();
        fbAddressEmlVeri.setFbid(userId);
        fbAddressEmlVeri.setAddress(address);
        fbAddressEmlVeri.setVerification(confirmation);

        ApiService apiService= NetWorkManager.createApiService(ApiService.class);
        Observable<StatusMessage> observable=apiService.getConfirmEmail(fbAddressEmlVeri);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<StatusMessage>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(StatusMessage statusMessage) {
                        L.i("Message: "+statusMessage.getMessage().toString()+" status  "+statusMessage.getStatus());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        hideWaitDialog();
                        L.i(""+"onComplete");

                       /* String email= SharedPreferencesHelper.getInstance(VisitVerfiActivity.this).getString("email",null);
                        if(email!=null){
                            mConfirmationLayout.setVisibility(View.GONE);
                            mBindingSuccessLayout.setVisibility(View.VISIBLE);
                            mBindEmailTV.setText(email);

                        }*/
                       Intent intent=new Intent(VisitVerfiActivity.this,VisitEmailBindActivity.class);
                       //intent.putExtra("email",email);
                       startActivity(intent);
                       showToast("bind email successfully");
                    }
                });
    }
}
