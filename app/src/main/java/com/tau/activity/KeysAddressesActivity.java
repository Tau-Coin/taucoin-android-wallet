package com.mofei.tau.activity;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.mofei.tau.R;
import com.mofei.tau.info.SharedPreferencesHelper;
import com.mofei.tau.info.key_address.taucoin.Key;
import com.mofei.tau.info.key_address.taucoin.KeyGenerator;
import com.mofei.tau.util.L;
import com.mofei.tau.util.ZXingUtils;
import com.mofei.tau.view.CustomToolBar;

public class KeysAddressesActivity extends BaseActivity implements View.OnClickListener {


    private static KeyGenerator instance;
    private Button mWalletHomeBt,mWalletLogoutBt;
    private TextView mDetailsTV;
    private TextView mWalletAddressTV;
    private ImageView mAddressTwoDimensionCodeIV;

    private CustomToolBar mMainCustomToolBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keys_addresses);

        initView();

        initData();

    }

    private void initView() {

        mWalletHomeBt=findViewById(R.id.address_back_wallet_home);
        mDetailsTV=findViewById(R.id.to_details);
        mWalletAddressTV= findViewById(R.id.wallet_address);
        mWalletLogoutBt=findViewById(R.id.walletLogout);
        mAddressTwoDimensionCodeIV=findViewById(R.id.address_two_dimension_code_img);
        titleBar();
    }

    private void titleBar() {
        //标题栏
        mMainCustomToolBar = findViewById(R.id.wallet_titlebar);
        mMainCustomToolBar.getTitleTextView().setText("Wallet");
        mMainCustomToolBar.getTitleTextView().setTextColor(Color.WHITE);
        mMainCustomToolBar.getTitleTextView().setTextSize(22);
        mMainCustomToolBar.disableLeftTextView();
      //  mMainCustomToolBar.disableRightView();

        mMainCustomToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initData() {

       String address=SharedPreferencesHelper.getInstance(KeysAddressesActivity.this).getString("Address","Address");
        mWalletAddressTV.setText(address);
        mWalletHomeBt. setOnClickListener(this);
        mWalletLogoutBt.setOnClickListener(this);

        mDetailsTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent( KeysAddressesActivity.this,DetailsActivity.class);
                startActivity(intent);
            }
        });

        //生成二维码
        Bitmap qrImage= ZXingUtils.createQRImage(address,400,400);
        mAddressTwoDimensionCodeIV.setImageBitmap(qrImage);
        mAddressTwoDimensionCodeIV.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                saveImage(mAddressTwoDimensionCodeIV);
                showToast("QR code saved to album");
                return false;
            }
        });

    }


    public static KeyGenerator getInstance(){
        if (instance == null) {
            instance = new KeyGenerator();
        }
        return instance;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.address_back_wallet_home:
                finish();
                break;
            case R.id.to_details:
                break;
        }
    }

    private void saveImage(ImageView imageView){
        imageView.setDrawingCacheEnabled(true);
        Bitmap bitmap = imageView.getDrawingCache();
        MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "这是title", "这是description");
        showToast("save success");
        imageView.setDrawingCacheEnabled(false);//关闭catch

    }

    private void SaveImageToSysAlbum() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Bitmap bitmap = convertViewToBitmap(mAddressTwoDimensionCodeIV);
            if (bitmap != null) {
                try {
                    ContentResolver cr = getContentResolver();
                    String url = MediaStore.Images.Media.insertImage(cr, bitmap,
                            String.valueOf(System.currentTimeMillis()), "");
                    showToast("save success");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                 showToast("save failure");
            }
        }else {
            showToast("save failure");
        }
    }

    public static Bitmap convertViewToBitmap(View view)
    {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }


}
