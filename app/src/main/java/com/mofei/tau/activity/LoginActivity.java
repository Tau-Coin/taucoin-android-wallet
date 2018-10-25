package com.mofei.tau.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.gson.JsonObject;
import com.mofei.tau.R;
import com.mofei.tau.constant.TAU_BaseURL;
import com.mofei.tau.entity.MessageEvent;
import com.mofei.tau.entity.req_parameter.FBAddress;
import com.mofei.tau.entity.req_parameter.FBAddressPubKey;
import com.mofei.tau.entity.res_post.Login1;
import com.mofei.tau.entity.res_post.Login1Ret;
import com.mofei.tau.entity.res_post.Login1RetSerializer;
import com.mofei.tau.entity.res_put.Login0;
import com.mofei.tau.entity.res_put.Login0Ret;
import com.mofei.tau.info.SharedPreferencesHelper;
import com.mofei.tau.info.key_address.taucoin.Key;
import com.mofei.tau.info.key_address.taucoin.KeyGenerator;
import com.mofei.tau.net.ApiService;
import com.mofei.tau.net.NetWorkManager;
import com.mofei.tau.util.L;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends BaseActivity implements View.OnClickListener{



    private static KeyGenerator instance;
    private LinearLayout mFbLoginLt;
    private CallbackManager callbackManager;
    private String facebookid;
    private String  address;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x133:
                 login_0();

                   /* new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            login1(facebookid);
                        }
                    }.start();*/
                    break;
                case 0x188:

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();

        loginFacebook();


    }

    private void initViews() {
        mFbLoginLt = findViewById(R.id.login);
        mFbLoginLt.setOnClickListener(this);

        EventBus.getDefault().register(this);
    }

    private void loginFacebook() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Toast.makeText(LoginActivity.this, "facebook_account_oauth_Success", Toast.LENGTH_SHORT);

            //    SharedPreferencesHelper.getInstance(LoginActivity.this).putBoolean("isLogin",true);

                L.e("token",loginResult.getAccessToken().getToken());
                L.e("applicationId",loginResult.getAccessToken().getApplicationId());
                L.e("facebookId",loginResult.getAccessToken().getUserId());

                facebookid=loginResult.getAccessToken().getUserId();

                showWaitDialog();

                login1(facebookid);
              //  login1_okhttp();

            }

            @Override
            public void onCancel() {
                L.e("登录取消");
                Toast.makeText(LoginActivity.this, "facebook_account_oauth_Cancel", Toast.LENGTH_SHORT);
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, "facebook_account_oauth_Error", Toast.LENGTH_SHORT);
                L.e("登录错误 e: ", error.toString());
            }
        });
    }

    private void login1_okhttp() {
        OkHttpClient client = new OkHttpClient.Builder().build();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("facebookid", "100028344854914");

            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());

            URL url = new URL( TAU_BaseURL.BASE_URL_TEXT+ "login1/");
            Request requestBuilder = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            Call call = client.newCall(requestBuilder);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.i("fail", "fail");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {

                        //Log.e(TAG, "message： " + response.body().string());
                        try {
                            JSONObject jsonObject=new JSONObject(response.body().string());
                            String isUserExist= (String) jsonObject.get("message");

                            Log.e(TAG, "message： " + isUserExist);

                            if(isUserExist.equals("fail")){

                              //  handler.sendEmptyMessage(0x133);
                              //  login_0();
                            }else {
                                JsonObject ret= (JsonObject) jsonObject.get("ret");
                                JsonObject serializer_account= (JsonObject) ret.get("serializer_account");
                                String address=serializer_account.get("address").toString();
                                String pubkey=serializer_account.get("pubkey").toString();
                                L.e("第二次登录返回address： " + address);
                                L.e("第二次登录返回pubkey： " + pubkey);
                                //保存公私钥及地址
                                SharedPreferencesHelper.getInstance(LoginActivity.this).putString("Pubkey",pubkey);
                                SharedPreferencesHelper.getInstance(LoginActivity.this).putString("Address",address);
                                toMainActivity();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {

                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void login1(String facebookid) {

        SharedPreferencesHelper.getInstance(LoginActivity.this).putString("userId",facebookid);

        FBAddress fbAddress=new FBAddress();
        fbAddress.setFacebookid(facebookid);

        ApiService apiService=NetWorkManager.getApiService();
        Observable<Login1<Login1Ret<Login1RetSerializer>>> observable=apiService.getLogin1(fbAddress);
        observable.subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Login1<Login1Ret<Login1RetSerializer>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                        L.e("Subscribe: " +d);
                    }

                    @Override
                    public void onNext(Login1<Login1Ret<Login1RetSerializer>> login1RetLogin1) {

                        String Message=login1RetLogin1.getMessage();
                        L.e("Message: " +Message);
                        int Status=login1RetLogin1.getStatus();
                        L.e("Status: " +Status);

                        String isUserExist=login1RetLogin1.getMessage();
                        L.e("isUserExist: " +isUserExist);

                        if(isUserExist.equals("fail")){

                            EventBus.getDefault().post(new MessageEvent(true,""));
                            L.e("新用户");
                         //   login_0();
                        }else {
                            String pubkey=login1RetLogin1.getRet().getSerializer_account().getPubkey();
                            address= login1RetLogin1.getRet().getSerializer_account().getAddress();
                            L.e("第二次登录返回address： " + address);
                            L.e( "第二次登录返回pubkey： " + pubkey);
                            L.e("第二次登录返回privkey："+privkey);
                            //保存公私钥及地址
                            SharedPreferencesHelper.getInstance(LoginActivity.this).putString("Pubkey",pubkey);
                            SharedPreferencesHelper.getInstance(LoginActivity.this).putString("Address",address);
                            toMainActivity();

                            SharedPreferencesHelper.getInstance(LoginActivity.this).putBoolean("isLogin",true);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        L.e("login1 Error: " );
                        e.printStackTrace();

                    }

                    @Override
                    public void onComplete() {

                        L.i(""+"onComplete");
                    }
                });

    }

    @Subscribe
    public void onEventAsync(MessageEvent messageEvent){
        boolean success= messageEvent.isSuccess();
        if(success){
           login_0();
        }
    }


    public void login_0() {

        generatekeyAddress();

        SharedPreferencesHelper.getInstance(LoginActivity.this).putString("userId",facebookid);

        FBAddressPubKey fbAddressPubKey=new FBAddressPubKey();

        String userId= SharedPreferencesHelper.getInstance(LoginActivity.this).getString("userId","userId");
        fbAddressPubKey.setFacebookid(userId);

        fbAddressPubKey.setAddress(SharedPreferencesHelper.getInstance(LoginActivity.this).getString("Address","Address"));
        fbAddressPubKey.setPubkey(SharedPreferencesHelper.getInstance(LoginActivity.this).getString("Pubkey","Pubkey"));

        ApiService apiService=NetWorkManager.getApiService();
        Observable<Login0<Login0Ret>> observable=apiService.getLogin0(fbAddressPubKey);

        observable.subscribeOn(Schedulers.io()) // 在子线程中进行Http访问
                .observeOn(AndroidSchedulers.mainThread()) // UI线程处理返回接口
                .subscribe(new Observer<Login0<Login0Ret>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Login0<Login0Ret> login0RetLogin0) {
                        L.i("Message: "+login0RetLogin0.getMessage().toString()+"   Status:"+login0RetLogin0.getStatus());
                    }

                    @Override
                    public void onError(Throwable e) {
                        L.i(TAG,""+e.toString());
                    }

                    @Override
                    public void onComplete() {
                        toMainActivity();
                        SharedPreferencesHelper.getInstance(LoginActivity.this).putBoolean("isLogin",true);
                        L.i("onComplete");
                    }
                });

        //新用户邮箱为空
        SharedPreferencesHelper.getInstance(LoginActivity.this).putString("email",null);
    }

    private void toMainActivity() {
        finish();
        startActivity(new Intent( LoginActivity.this,MainActivity.class));

        hideWaitDialog();
    }

    private void generatekeyAddress() {
        //生成公私钥及地址
        final Key key = new Key();
        key.Reset();
        if (getInstance().GenerateKey(key)) {
            L.e("Privkey :"+key.getPrivkey());
            L.e("Public: "+key.getPubkey());
            L.e("Address: "+key.getAddress());
            //保存公私钥及地址
            SharedPreferencesHelper.getInstance(LoginActivity.this).putString("Pubkey",key.getPubkey());
            SharedPreferencesHelper.getInstance(LoginActivity.this).putString("Privkey",key.getPrivkey());
            SharedPreferencesHelper.getInstance(LoginActivity.this).putString("Address",key.getAddress());
        } else {
            L.i("Generate key error...");
        }
    }


    public static KeyGenerator getInstance(){
        if (instance == null) {
            instance = new KeyGenerator();
        }
        return instance;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);


    }

    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        AppEventsLogger.deactivateApp(this);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.login) {

            AccessToken accessToken = AccessToken.getCurrentAccessToken();

            L.i( "accessToken"+accessToken);

            if (accessToken == null || accessToken.isExpired()) {

                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends"));

            }
        }


    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
       EventBus.getDefault().unregister(this);
    }
}