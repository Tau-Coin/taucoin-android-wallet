package com.mofei.tau.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mofei.tau.R;
import com.mofei.tau.entity.req_parameter.Login;
import com.mofei.tau.entity.res_post.LoginRes;
import com.mofei.tau.entity.res_post.StatusMessage;
import com.mofei.tau.info.SharedPreferencesHelper;
import com.mofei.tau.net.ApiService;
import com.mofei.tau.net.NetWorkManager;
import com.mofei.tau.util.L;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity2 extends BaseActivity implements View.OnClickListener{

    private TextView registerTV,forgotPassTV;

    private ImageView emailImageView,passwordImageView;

    private EditText emailEditText,passwordEditText;

    private CheckBox mCbDisplayPass,mRemeberPass;

    private Button loginBt;

    private String status;

    private String user_nane;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x12:
                    L.e("handler");
                    if(status.equals("0")){
                        Intent intent=new Intent(LoginActivity2.this,MainActivity.class);
                        intent.putExtra("user_name",user_nane);
                        startActivity(intent);
                        showToast("login successful");
                       // finish();
                    }

                    break;
                case 0x15:
                    if(status=="401"){
                        showToast("email or password is wrong");
                    }

                    /*if(status.equals("401")){
                        showToast("email or password is wrong");
                    }*/
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        initView();

        initData();

        initEvent();
    }


    private void initView() {
        registerTV=findViewById(R.id.register);
        emailImageView=findViewById(R.id.img_email);
        passwordImageView=findViewById(R.id.img_password);

        emailEditText=findViewById(R.id.email_input);
        passwordEditText=findViewById(R.id.pass_input);
        loginBt=findViewById(R.id.login_bt);
        forgotPassTV=findViewById(R.id.forgotPass);
        mCbDisplayPass=findViewById(R.id.cbDisplay);
        mRemeberPass=findViewById(R.id.cbRememberPs);
    }

    private void initData() {
        registerTV.setOnClickListener(this);
        emailImageView.setOnClickListener(this);
        passwordImageView.setOnClickListener(this);
        forgotPassTV.setOnClickListener(this);
        mCbDisplayPass.setOnClickListener(this);
        loginBt.setOnClickListener(this);
        mRemeberPass.setOnClickListener(this);
    }

    private void initEvent() {
        initListener();

    }


    @Override
    protected void onResume() {
        super.onResume();

        emailEditText.setText(SharedPreferencesHelper.getInstance(LoginActivity2.this).getString("email",""));

        passwordEditText.setText(SharedPreferencesHelper.getInstance(this).getString("password",""));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register:
                 startActivity(new Intent(LoginActivity2.this,RegisterActivity.class));
                break;
            case R.id.img_email:
                cleanWord();
                break;
            case R.id.img_password:
                cleanWord();
                break;
            case R.id.forgotPass:
                startActivity(new Intent(this,ChangePasswActivity.class));
                break;
            case R.id.login_bt:
                String email=emailEditText.getText().toString().trim();
                String password=passwordEditText.getText().toString().trim();
                if (email == null || email.length() == 0) {
                    showToast("mailbox is empty");
                    return;
                }
                if (!isEmailValid(email)){
                    showToast("invalid email");
                    return;
                }
                if (password == null || password.length() == 0) {
                    showToast("password is empty");
                    return;
                }

                if(mRemeberPass.isChecked()){
                     SharedPreferencesHelper.getInstance(this).putString("password",password);
                }else {
                    SharedPreferencesHelper.getInstance(this).putString("password",null);
                }

                showWaitDialog();
                login(email,password);
                break;
        }

    }

    private void login(String email,String password) {
        Login login=new Login();
        login.setEmail(email);
        login.setPassword(password);
        ApiService apiService= NetWorkManager.getApiService();
        Observable<LoginRes> observable=apiService.login(login);
        observable.subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginRes>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(LoginRes loginRes) {

                        status=loginRes.getStatus();
                        L.e(status);
                        L.e(loginRes.getMessage());
                        L.e(loginRes.getEmail());
                        SharedPreferencesHelper.getInstance(LoginActivity2.this).putString("email",loginRes.getEmail());
                        user_nane=loginRes.getEmail();
                        SharedPreferencesHelper.getInstance(LoginActivity2.this).putString("user_nane",user_nane);
                        L.e("getAddress:"+loginRes.getAddress());
                        L.e("getPubkey: "+loginRes.getPubkey());

                        SharedPreferencesHelper.getInstance(LoginActivity2.this).putString("Pubkey", loginRes.getPubkey());
                        SharedPreferencesHelper.getInstance(LoginActivity2.this).putString("Address",loginRes.getAddress());
                    }

                    @Override
                    public void onError(Throwable e) {

                        handler.sendEmptyMessage(0x15);
                        L.e("onError");
                        e.printStackTrace();
                        hideWaitDialog();
                    }

                    @Override
                    public void onComplete() {

                        hideWaitDialog();
                        handler.sendEmptyMessage(0x12);
                        L.i("onComplete");
                        SharedPreferencesHelper.getInstance(LoginActivity2.this).putBoolean("isLogin",true);
                    }
                });
    }

    private void initListener() {

        mCbDisplayPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if(isChecked) {
                    //选择状态 显示明文--设置为可见的密码
                    //mEtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    /**
                     * 第二种
                     */
                    passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //默认状态显示密码--设置文本 要一起写才能起作用 InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
                    //mEtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    /**
                     * 第二种
                     */
                    passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }

    private TextWatcherAdapter emailWatcher  = new TextWatcherAdapter() {
        @Override
        public void afterTextChanged(Editable s) {
            String emailInput = emailEditText.getText().toString();
            if (TextUtils.isEmpty(emailInput)) {
                emailImageView.setVisibility(View.INVISIBLE);
            } else {
                emailImageView.setVisibility(View.VISIBLE);
            }
        }
    };
    private TextWatcherAdapter passwordWatcher  = new TextWatcherAdapter() {
        @Override
        public void afterTextChanged(Editable s) {
            String emailInput = passwordEditText.getText().toString();
            if (TextUtils.isEmpty(emailInput)) {
                passwordImageView.setVisibility(View.INVISIBLE);
            } else {
                passwordImageView.setVisibility(View.VISIBLE);
            }
        }
    };

    private void cleanWord() {
        emailEditText.addTextChangedListener(emailWatcher);
        emailImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailEditText.setText(null);
            }
        });
        passwordEditText.addTextChangedListener(passwordWatcher);
        passwordImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordEditText.setText(null);
            }
        });
    }
}
