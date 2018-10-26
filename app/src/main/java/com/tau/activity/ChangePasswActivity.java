package com.mofei.tau.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mofei.tau.R;
import com.mofei.tau.entity.req_parameter.ChangePassword;
import com.mofei.tau.entity.req_parameter.Logout;
import com.mofei.tau.entity.res_post.StatusMessage;
import com.mofei.tau.info.SharedPreferencesHelper;
import com.mofei.tau.net.ApiService;
import com.mofei.tau.net.NetWorkManager;
import com.mofei.tau.util.L;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ChangePasswActivity extends BaseActivity implements View.OnClickListener{
    private TextView backTv,safeTv;
    private EditText mEmail,mPassword,mConfirmPass;
    private Button mSubmit;
    private String pass;
    private int status;
    private int safetyCodeStatus;
    private int count;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x16:
                    if(status==0){
                        finish();
                        showToast("change password successful");
                        SharedPreferencesHelper.getInstance(ChangePasswActivity.this).putString("password",pass);
                    }
                    if(status==402){
                        showToast("Invalid verification code");
                    }
                    if(status==403){
                        showToast("Code is wrong");
                    }

                    break;
                case 0x17:
                    if(status==401){
                        showToast("Email does not exist");
                    }

                    break;
                case 0x21:
                    if(safetyCodeStatus==0){
                        showToast("getting security code successfully");
                    }
                    startCountdown();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_passw);


        backTv=findViewById(R.id.back_to_login);
        mEmail=findViewById(R.id.email_in);
        mPassword=findViewById(R.id.new_password);
        mConfirmPass=findViewById(R.id.confirm_new_password);
        mSubmit=findViewById(R.id.submission);
        safeTv=findViewById(R.id.send_save_code);
        safeTv.setOnClickListener(this);
        backTv.setOnClickListener(this);
        mSubmit.setOnClickListener(this);

        mEmail.setText(SharedPreferencesHelper.getInstance(ChangePasswActivity.this).getString("email",""));
    }

    private void changePassword(String email,String password,String safetycode) {
        ChangePassword changePassword=new ChangePassword();
        changePassword.setEmail(email);
        changePassword.setNew_password(password);
        changePassword.setSafety_code(safetycode);
        ApiService apiService= NetWorkManager.getApiService();
        Observable<StatusMessage> observable=apiService.changePassword(changePassword);
        observable.subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<StatusMessage>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(StatusMessage statusMessage) {
                        status=statusMessage.getStatus();
                        L.i(""+statusMessage.getStatus());
                        L.i(statusMessage.getMessage());
                    }

                    @Override
                    public void onError(Throwable e) {
                        handler.sendEmptyMessage(0x17);
                        L.e("error");
                        e.printStackTrace();
                        hideWaitDialog();

                    }

                    @Override
                    public void onComplete() {
                        hideWaitDialog();
                        L.i("complete");
                        handler.sendEmptyMessage(0x16);
                    }
                });
    }






    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_to_login:
                finish();
                break;
            case R.id.submission:
                String email=mEmail.getText().toString().trim();
                pass=mPassword.getText().toString().trim();
                String confirm_pass=mConfirmPass.getText().toString().trim();

                if (email == null || email.length() == 0) {
                    showToast("mailbox is empty");
                    return;
                }
                if (pass == null || pass.length() == 0) {
                    showToast("password is empty");
                    return;
                }
                if (confirm_pass == null || confirm_pass.length() == 0) {
                    showToast("safety code is empty");
                    return;
                }
               /* if(!pass.equals(confirm_pass)){
                    showToast("the two input password is inconsistent.");
                    return;
                }*/
                showWaitDialog();
                changePassword(email,pass,confirm_pass);
                break;

            case R.id.send_save_code:
                String email_=mEmail.getText().toString().trim();
                if (email_ == null || email_.length() == 0) {
                    showToast("mailbox is empty");
                    return;
                }
                showWaitDialog();
                getSafetyCode(email_);
                break;
        }


    }

    private void getSafetyCode(String str) {

        Map<String,String> email=new HashMap<>();
        email.put("email",str);
        ApiService apiService= NetWorkManager.getApiService();
        Observable<StatusMessage> observable=apiService.verifyCode(email);
        observable.subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<StatusMessage>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(StatusMessage statusMessage) {

                        safetyCodeStatus=statusMessage.getStatus();

                        L.i("Status: "+statusMessage.getStatus());
                        L.i("Message: "+statusMessage.getMessage());
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideWaitDialog();
                        L.i("onError");
                        e.printStackTrace();


                    }

                    @Override
                    public void onComplete() {
                        hideWaitDialog();
                        handler.sendEmptyMessage(0x21);
                        L.i("onComplete");
                    }
                });

    }

    private void startCountdown() {
        count = 90;
        safeTv.setText(String.valueOf(count));
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (count > 1) {
                    safeTv.setEnabled(false);
                    count--;
                    safeTv.setText(String.valueOf(count));
                    handler.postDelayed(this,1000L);
                } else {
                    safeTv.setText("Reget");
                    safeTv.setEnabled(true);
                }
            }
        }, 1000L);
    }
}
