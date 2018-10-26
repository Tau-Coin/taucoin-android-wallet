package com.mofei.tau.activity;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mofei.tau.R;
import com.mofei.tau.entity.req_parameter.FBAddress;
import com.mofei.tau.entity.res_put.BuyCoins;
import com.mofei.tau.entity.res_put.BuyCoinsRet;
import com.mofei.tau.info.SharedPreferencesHelper;
import com.mofei.tau.net.ApiService;
import com.mofei.tau.net.NetWorkManager;
import com.mofei.tau.util.L;
import com.mofei.tau.util.ZXingUtils;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BuyCoinsAddressActivity extends BaseActivity implements View.OnClickListener{

    private ImageView mbtcReceiveAddressIV;
    private TextView mbtcReceiveAddressTV;
    private String mBtcadd;
    private Button mBackWalletHome;

    private static String TAG = "FacebookLoginDemo";
    String userId;
    String Address;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x133:

                    if(mBtcadd.equals(0)){
                        showToast("btcadd is generating...");
                    }else {
                        Bitmap addressIV= ZXingUtils.createQRImage(mBtcadd,300,300);
                        mbtcReceiveAddressIV.setImageBitmap(addressIV);
                        mbtcReceiveAddressTV.setText(mBtcadd+"");
                    }
                    break;
                case 0x188:

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_coins_address);

        initView();


        initData();

    }

    private void initView() {

        mbtcReceiveAddressIV=findViewById(R.id.btc_receive_address_iv);
        mbtcReceiveAddressTV=findViewById(R.id.btc_receive_address_tv);
        mBackWalletHome=findViewById(R.id.buy_coins_back_wallet_home);


    }

    private void initData() {

        userId= SharedPreferencesHelper.getInstance(BuyCoinsAddressActivity.this).getString("userId","userId");

        Address=SharedPreferencesHelper.getInstance(BuyCoinsAddressActivity.this).getString("Address","Address");

        mBackWalletHome.setOnClickListener(this);

        showWaitDialog();

        getBtcadd();
        //二维码生成


    }



    private void getBtcadd() {

        FBAddress fbAddress=new FBAddress();
        fbAddress.setFacebookid(userId );
        fbAddress.setAddress(Address );
        L.i("userId  "+userId+"  Address  "+Address);

        ApiService apiService= NetWorkManager.getApiService();
        Observable<BuyCoins<BuyCoinsRet>> observable=apiService.getBuyCoins(fbAddress);
        observable.subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BuyCoins<BuyCoinsRet>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BuyCoins<BuyCoinsRet> buyCoins) {

                        L.e("Message: "+buyCoins.getMessage().toString());

                        mBtcadd=buyCoins.getRet().getBtcadd();
                        L.e("mBtcadd: "+mBtcadd);

                        handler.sendEmptyMessage(0x133);



                    }

                    @Override
                    public void onError(Throwable e) {

                        e.printStackTrace();
                        L.e(""+e.toString());
                    }

                    @Override
                    public void onComplete() {

                        L.e(""+"onComplete");
                        hideWaitDialog();
                    }
                });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buy_coins_back_wallet_home:
                finish();
                break;
        }
    }
}
