package com.mofei.tau.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mofei.tau.R;
import com.mofei.tau.info.SharedPreferencesHelper;
import com.mofei.tau.info.key_address.taucoin.KeyGenerator;
import com.mofei.tau.util.L;
import com.mofei.tau.util.ZXingUtils;
import com.mofei.tau.view.CustomToolBar;


public class DetailsActivity extends BaseActivity implements View.OnClickListener {

    private static String TAG = "FacebookLoginDemo";
    private TextView mPubkeyTV,mPrivkeyTV,mAddressTV;
    private Button mWalletBt;
    private ImageView mWalletAddressIv;

    private static KeyGenerator instance;
    private CustomToolBar mMainCustomToolBar;

    private CheckBox mCbDisplayPrivatekey;
    private CheckBox mCbDisplayAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        initView();

        initData();

    }


    private void initView() {
        mPubkeyTV=findViewById(R.id.public_key);
        mPrivkeyTV=findViewById(R.id.private_key);
        mAddressTV=findViewById(R.id.address);
        mWalletBt=findViewById(R.id.back_wallet);
        mWalletAddressIv=findViewById(R.id.wallet_address_iv);
        mCbDisplayPrivatekey=findViewById(R.id.cbDisplayPrivatekey);
        mCbDisplayAddress=findViewById(R.id.cbDisplayPublkey);

        titleBar();
    }


    private void titleBar() {
        //标题栏
        mMainCustomToolBar = findViewById(R.id.details_titlebar);
        mMainCustomToolBar.getTitleTextView().setText("Wallet");
        mMainCustomToolBar.getTitleTextView().setTextColor(Color.WHITE);
        mMainCustomToolBar.getTitleTextView().setTextSize(22);
        mMainCustomToolBar.disableLeftTextView();
       // mMainCustomToolBar.disableRightView();

        mMainCustomToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    private void initData() {


        mPubkeyTV.setText(  pubkey);
        mPrivkeyTV.setText(  privkey);
       // String address=SharedPreferencesHelper.getInstance(DetailsActivity.this).getString("Address","null");
        mAddressTV.setText(address);

        L.i("Pubkey "+SharedPreferencesHelper.getInstance(DetailsActivity.this).getString("Pubkey","Pubkey")+" Privkey  "+SharedPreferencesHelper.getInstance(DetailsActivity.this).getString("Privkey","Privkey"));
        /*Intent detailsIntent=getIntent();
        String pubkey=detailsIntent.getStringExtra("Pubkey");
        mPubkeyTV.setText(pubkey);
        String privkey=detailsIntent.getStringExtra("Privkey");
        mPrivkeyTV.setText(privkey);
        String address=detailsIntent.getStringExtra("Address");
        mAddressTV.setText(address);*/


        mWalletBt.setOnClickListener(this);


        //二维码生成
        Bitmap addressIV=ZXingUtils.createQRImage(address,300,300);
        mWalletAddressIv.setImageBitmap(addressIV);

        mWalletAddressIv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // SaveImageToSysAlbum();  //保存到相册
                AlertDialog dialog = new AlertDialog.Builder(DetailsActivity.this)
                        // .setIcon(R.mipmap.icon)//设置标题的图片
                        .setTitle("Do you want to save the address two-dimensional code?")//设置对话框的标题
                        .setMessage("If you want to save, please click OK or click Cancel.")//设置对话框的内容
                        //设置对话框的按钮
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
    }
    public static KeyGenerator getInstance(){
        if (instance == null) {
            instance=new KeyGenerator();
        }
        return instance;
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.back_wallet){
            finish();
        }
    }

    private void saveImage(ImageView imageView){
        imageView.setDrawingCacheEnabled(true);//开启catch，开启之后才能获取ImageView中的bitmap
        Bitmap bitmap = imageView.getDrawingCache();//获取imageview中的图像
        MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "这是title", "这是description");
        showToast("save success");
        imageView.setDrawingCacheEnabled(false);//关闭catch

    }

    private void initListener() {

        mCbDisplayPrivatekey.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(privkey.equals("null")){
                    showToast("Please re register the account to display the private key.");
                }

                if(isChecked) {
                    //选择状态 显示明文--设置为可见的密码
                    //mEtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    /**
                     * 第二种
                     */
                    mPrivkeyTV.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //默认状态显示密码--设置文本 要一起写才能起作用 InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
                    //mEtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    /**
                     * 第二种
                     */
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
                    //选择状态 显示明文--设置为可见的密码
                    //mEtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    /**
                     * 第二种
                     */
                    mPubkeyTV.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //默认状态显示密码--设置文本 要一起写才能起作用 InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
                    //mEtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    /**
                     * 第二种
                     */
                    mPubkeyTV.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }
}
