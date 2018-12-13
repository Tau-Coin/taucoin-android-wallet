package com.tau.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mofei.tau.R;
import com.tau.info.SharedPreferencesHelper;
import com.tau.info.key_address.taucoin.KeyGenerator;
import com.google.bitcoin.core.AddressFormatException;
import com.google.bitcoin.core.ECKey;
import com.google.bitcoin.core.Utils;
import com.tau.util.L;
import com.tau.util.UserInfoUtils;
import com.tau.util.ZXingUtils;
import com.tau.view.CustomToolBar;

import java.math.BigInteger;


public class DetailsActivity extends BaseActivity implements View.OnClickListener {

    private static String TAG = "FacebookLoginDemo";
    private TextView mPubkeyTV,mPrivkeyTV,mAddressTV;
    private Button mWalletBt;
    private ImageView mWalletAddressIv;
   // private Button mSavePrivateKey;
    private static KeyGenerator instance;
    private CustomToolBar mMainCustomToolBar;
    private CheckBox mCbDisplayPrivatekey;
    private CheckBox mCbDisplayAddress;
    private LinearLayout mSavaPrikeyLL;
    private EditText mSavePrivateKeyET;
    private Button mSavePrivateKeyBT;
    private ImageView mCopyImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        initView();

        initData();

    }


    private void initView() {
        titleBar();
        mPubkeyTV=findViewById(R.id.public_key);
        mPrivkeyTV=findViewById(R.id.private_key);
        mAddressTV=findViewById(R.id.address);
        mWalletBt=findViewById(R.id.back_wallet);
        mWalletAddressIv=findViewById(R.id.wallet_address_iv);
        mCbDisplayPrivatekey=findViewById(R.id.cbDisplayPrivatekey);
        mCbDisplayAddress=findViewById(R.id.cbDisplayPublkey);
       // mSavePrivateKey=findViewById(R.id.save_prikey);
        mSavaPrikeyLL=findViewById(R.id.save_prikey_key_ll);
        isPrivateKeyNull();
        mSavePrivateKeyET=findViewById(R.id.private_key_et);
        mSavePrivateKeyBT=findViewById(R.id.sava_private_key_bt);
        mSavePrivateKeyBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String privateKey_=mSavePrivateKeyET.getText().toString().trim();
                if (privateKey_ == null || privateKey_.length() == 0) {
                    showToast("Private key is empty");
                    return;
                }

                //The input private key is converted into a public key, which is compared with the public key transmitted by the server at the time of login. If the matching is equal, the private key can be saved only if the matching is successful.
                String newPrivKeyStr = null;
                try {
                    newPrivKeyStr = Utils.convertWIFPrivkeyIntoPrivkey(privateKey_);
                } catch (AddressFormatException e) {
                    L.e(e.toString());
                    showToast("The imported private key doesn't match with public key");
                    return;
                }
                ECKey key2 = new ECKey(new BigInteger(newPrivKeyStr, 16));
                String privakeyToPubkey=Utils.bytesToHexString(key2.getCompressedPubKey()).toUpperCase();
                L.e("Compressed key:" +privakeyToPubkey.toUpperCase());

                String pubkey=UserInfoUtils.getPublicKey(DetailsActivity.this);
                L.e("saved publickey: "+pubkey);
                if (pubkey==""){
                    showToast("The public key is null");
                    return;
                }
                if (privakeyToPubkey.equals(pubkey)){
                    //SharedPreferencesHelper.getInstance(DetailsActivity.this).putString("Privkey",privateKey_);
                    UserInfoUtils.setPrivateKey(DetailsActivity.this,privateKey_);
                    mPrivkeyTV.setText(UserInfoUtils.getPrivateKey(DetailsActivity.this));
                    showToast("Save private key successfully");
                    isPrivateKeyNull();
                }else {
                    showToast("The imported private key doesn't match with public key");
                }
            }
        });

        mCopyImageView=findViewById(R.id.copy_);
        mCopyImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCopy(mPrivkeyTV);
            }
        });

    }

    private void isPrivateKeyNull() {
        String privateKey = UserInfoUtils.getPrivateKey(DetailsActivity.this);
        if (privateKey.isEmpty()) {
            mSavaPrikeyLL.setVisibility(View.VISIBLE);
        }else {
            mSavaPrikeyLL.setVisibility(View.GONE);
        }
    }

    public void onClickCopy(TextView textView) {
        ClipboardManager cm = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setText(textView.getText());
        showToast("copy successfully");
    }


    private void titleBar() {
        mMainCustomToolBar = findViewById(R.id.details_titlebar);
        mMainCustomToolBar.getTitleTextView().setText("TAUcoin");
        mMainCustomToolBar.getTitleTextView().setTextColor(Color.WHITE);
        mMainCustomToolBar.getTitleTextView().setTextSize(22);
        mMainCustomToolBar.disableLeftTextView();
       // mMainCustomToolBar.disableRightView();

        mMainCustomToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailsActivity.this,SendAndReceiveActivity.class));
                finish();
            }
        });

    }
    private void initData() {


        mPubkeyTV.setText(UserInfoUtils.getPublicKey(DetailsActivity.this));
        mPrivkeyTV.setText(UserInfoUtils.getPrivateKey(DetailsActivity.this));
        mAddressTV.setText(UserInfoUtils.getAddress(DetailsActivity.this));

        L.i("Pubkey "+SharedPreferencesHelper.getInstance(DetailsActivity.this).getString("Pubkey","Pubkey")+" Privkey  "+SharedPreferencesHelper.getInstance(DetailsActivity.this).getString("Privkey","Privkey"));

        mWalletBt.setOnClickListener(this);

        Bitmap addressIV=ZXingUtils.createQRImage(UserInfoUtils.getAddress(DetailsActivity.this),300,300);
        mWalletAddressIv.setImageBitmap(addressIV);

        mWalletAddressIv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(DetailsActivity.this)
                        // .setIcon(R.mipmap.icon)//设置标题的图片
                        .setTitle("Do you want to save the address two-dimensional code?")
                        .setMessage("If you want to save, please click OK or click Cancel.")
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                saveImage(mWalletAddressIv);
                                dialog.dismiss();
                            }
                        }).create();

                dialog.show();

                return false;
            }
        });


        initListener();
        initListener1();



       // mSavePrivateKey.setOnClickListener(this);

    }
    public static KeyGenerator getInstance(){
        if (instance == null) {
            instance=new KeyGenerator();
        }
        return instance;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_wallet:
                finish();
                break;
        }
    }

    private void saveImage(ImageView imageView){
        imageView.setDrawingCacheEnabled(true);
        Bitmap bitmap = imageView.getDrawingCache();
        MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "这是title", "这是description");
        showToast("save success");
        imageView.setDrawingCacheEnabled(false);

    }

    private void initListener() {

        mCbDisplayPrivatekey.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               String privkey=UserInfoUtils.getPrivateKey(DetailsActivity.this);
                if(privkey.equals("null")){
                    showToast("Please re register the account to display the private key.");
                }
                if(isChecked) {
                    mPrivkeyTV.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    mPrivkeyTV.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }
    private void initListener1() {
        mCbDisplayAddress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    mPubkeyTV.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    mPubkeyTV.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }
}
