package com.tau.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.login.LoginManager;
import com.mofei.tau.R;
import com.tau.entity.req_parameter.FBAddressEmail;
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


public class VisitActivity extends BaseActivity implements View.OnClickListener {

    private Button mWalletHomeBt,mLogoutBt,mSubmitBt;
    private EditText mInputEmailET;

    private String inputEmail;
    private static String TAG = "FacebookLoginDemo";

    private CustomToolBar mMainCustomToolBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit);

        initView();

        initData();


    }

    private void initView() {
        mWalletHomeBt=findViewById(R.id.visit_back_wallet_home);
        mSubmitBt=findViewById(R.id.submit);
        mInputEmailET=findViewById(R.id.email_edittext);


      /*  mEmailTV=findViewById(R.id.email_textview);
        mConfirmEmail=findViewById(R.id.confirm_textview);
        mEmail=findViewById(R.id.email);*/
        mLogoutBt=findViewById(R.id.visit_logout);

        titleBar();
    }

    private void titleBar() {
        //标题栏
        mMainCustomToolBar = findViewById(R.id.visit_titlebar);
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

        mWalletHomeBt.setOnClickListener(this);
        mSubmitBt.setOnClickListener(this);
      //  mEmailTV.setOnClickListener(this);
       // mEmailET.setOnClickListener(this);
        mLogoutBt.setOnClickListener(this);

      //  inputEmail=mEmailET.getText().toString().trim();

       /* mEmailET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                Log.i(TAG,"beforeTextChanged");

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                mEmailTV.setVisibility(View.VISIBLE);
                Log.i(TAG,"onTextChanged");

            }

            @Override
            public void afterTextChanged(Editable s) {
                inputEmail=mEmailET.getText().toString().trim();
                if(TextUtils.isEmpty(inputEmail)){
                    mEmailTV.setVisibility(View.INVISIBLE);
                }else {
                    mEmailTV.setVisibility(View.VISIBLE);
                }


                Log.i(TAG,"afterTextChanged");
            }
        });*/

      /*  String email= SharedPreferencesHelper.getInstance(VisitActivity.this).getString("email",null);
        if(email!=null){
            mEmail.setText(email);
            mConfirmEmail.setVisibility(View.VISIBLE);
            mEmailET.setVisibility(View.INVISIBLE);
        }else {
            mEmailET.setVisibility(View.VISIBLE);
        }
*/

    }




    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.visit_back_wallet_home:
                finish();
               // startActivity(new Intent(VisitActivity.this,MainActivity.class));
                break;
            case R.id.visit_logout:
                logout();
                break;
            case R.id.submit:

                String userId=SharedPreferencesHelper.getInstance(VisitActivity.this).getString("userId","userId");
                String address=SharedPreferencesHelper.getInstance(VisitActivity.this).getString("Address","Address");

                inputEmail=mInputEmailET.getText().toString().trim();
                if(inputEmail!=null){
                    if(isEmailValid(inputEmail)){
                        showWaitDialog();
                        submitEmailToSever(userId,address,inputEmail);
                    }else {
                        showToast("Error in mailbox format");
                    }

                }else {
                    showToast("emailbox is empty");
                }

                break;
        }
    }


    private void logout() {
        LoginManager.getInstance().logOut();
        SharedPreferencesHelper.getInstance(VisitActivity.this).putBoolean("isLogin",false);
        L.i("logOut");
        startActivity(new Intent(VisitActivity.this,LoginActivity.class));
    }

    public void submitEmailToSever(String userId,String address,String email) {
        FBAddressEmail fbAddressEmail=new FBAddressEmail();
        fbAddressEmail.setFbid(userId);
        fbAddressEmail.setAddress(address);
        fbAddressEmail.setEmail(email);

        ApiService apiService= NetWorkManager.createApiService(ApiService.class);
        Observable<StatusMessage> observable= apiService.getEmailVerification(fbAddressEmail);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<StatusMessage>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(StatusMessage statusMessage) {
                        showToast("submit email successfully");
                        L.i("Message: "+statusMessage.getMessage().toString());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        hideWaitDialog();
                        Intent intent=new Intent(VisitActivity.this,VisitVerfiActivity.class);
                        intent.putExtra("email",inputEmail);
                        startActivity(intent);
                    }
                });
    }

    /**
     * 验证邮箱格式是否正确
     */
    public boolean isEmailValid(String email) {
        // String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        String regex = "^[a-zA-Z0-9_.%+-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
        return email.matches(regex);
    }
}
