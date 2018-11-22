package com.mofei.tau.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mofei.tau.R;
import com.mofei.tau.info.SharedPreferencesHelper;
import com.mofei.tau.info.key_address.taucoin.KeyGenerator;
import com.mofei.tau.util.L;
import com.mofei.tau.util.OpenFileUtil;
import com.mofei.tau.util.ZXingUtils;
import com.mofei.tau.view.CustomToolBar;


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
        if(privkey.equals("null")){
            mSavaPrikeyLL.setVisibility(View.VISIBLE);
        }else {
            mSavaPrikeyLL.setVisibility(View.GONE);
        }
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

                //???
                SharedPreferencesHelper.getInstance(DetailsActivity.this).putString("Privkey",privateKey_);

                mPrivkeyTV.setText(SharedPreferencesHelper.getInstance(DetailsActivity.this).getString("Privkey",""));
            }
        });

    }


    private void titleBar() {
        //标题栏
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


        mPubkeyTV.setText( pubkey);
        mPrivkeyTV.setText( privkey);
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
        /*if (v.getId()==R.id.back_wallet){
            finish();
        }*/
        switch (v.getId()){
            case R.id.back_wallet:
                finish();
                break;
           /* case R.id.save_prikey:
                savePrivateKeyToFile();
                break;*/
        }
    }

//    private void savePrivateKeyToFile() {
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("*/*");
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        startActivityForResult(intent, 1);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == Activity.RESULT_OK) {
//            Uri uri = data.getData();
//            if ("file".equalsIgnoreCase(uri.getScheme())){//使用第三方应用打开
//                Toast.makeText(this,uri.getPath()+"11111",Toast.LENGTH_SHORT).show();
//                L.e("选择的文件路径为："+uri.getPath());
//
//              /* L.e("apk "+OpenFileUtil.getApkFileIntent(uri.getPath()));
//
//                if("file".equalsIgnoreCase(OpenFileUtil.getApkFileIntent(uri.getPath()).getData().getScheme())){
//                    L.e("apk下的文件路径为："+uri.getPath());
//                }*/
//                return;
//            }
//            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
//                String path = getPath(this, uri);
//                Toast.makeText(this,path.toString(),Toast.LENGTH_SHORT).show();
//                L.e("path "+path.toString());
//            } else {//4.4一下系统调用方法
//                Toast.makeText(DetailsActivity.this, getRealPathFromURI(uri)+"222222", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    public String getRealPathFromURI(Uri contentUri) {
//        String res = null;
//        String[] proj = { MediaStore.Images.Media.DATA };
//        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
//        if(null!=cursor&&cursor.moveToFirst()){;
//            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            res = cursor.getString(column_index);
//            cursor.close();
//        }
//        return res;
//    }
//
//
//    /**
//     * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
//     */
//    @SuppressLint("NewApi")
//    public String getPath(final Context context, final Uri uri) {
//
//        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
//
//        // DocumentProvider
//        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
//            // ExternalStorageProvider
//            if (isExternalStorageDocument(uri)) {
//                final String docId = DocumentsContract.getDocumentId(uri);
//                final String[] split = docId.split(":");
//                final String type = split[0];
//
//                if ("primary".equalsIgnoreCase(type)) {
//                    return Environment.getExternalStorageDirectory() + "/" + split[1];
//                }
//            }
//            // DownloadsProvider
//            else if (isDownloadsDocument(uri)) {
//
//                final String id = DocumentsContract.getDocumentId(uri);
//                final Uri contentUri = ContentUris.withAppendedId(
//                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
//
//                return getDataColumn(context, contentUri, null, null);
//            }
//            // MediaProvider
//            else if (isMediaDocument(uri)) {
//                final String docId = DocumentsContract.getDocumentId(uri);
//                final String[] split = docId.split(":");
//                final String type = split[0];
//
//                Uri contentUri = null;
//                if ("image".equals(type)) {
//                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//                } else if ("video".equals(type)) {
//                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
//                } else if ("audio".equals(type)) {
//                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//                }
//
//                final String selection = "_id=?";
//                final String[] selectionArgs = new String[]{split[1]};
//
//                return getDataColumn(context, contentUri, selection, selectionArgs);
//            }
//        }
//        // MediaStore (and general)
//        else if ("content".equalsIgnoreCase(uri.getScheme())) {
//            return getDataColumn(context, uri, null, null);
//        }
//        // File
//        else if ("file".equalsIgnoreCase(uri.getScheme())) {
//            return uri.getPath();
//        }
//        return null;
//    }
//
//
//    /**
//     * Get the value of the data column for this Uri. This is useful for
//     * MediaStore Uris, and other file-based ContentProviders.
//     *
//     * @param context       The context.
//     * @param uri           The Uri to query.
//     * @param selection     (Optional) Filter used in the query.
//     * @param selectionArgs (Optional) Selection arguments used in the query.
//     * @return The value of the _data column, which is typically a file path.
//     */
//    public String getDataColumn(Context context, Uri uri, String selection,
//                                String[] selectionArgs) {
//
//        Cursor cursor = null;
//        final String column = "_data";
//        final String[] projection = {column};
//
//        try {
//            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
//                    null);
//            if (cursor != null && cursor.moveToFirst()) {
//                final int column_index = cursor.getColumnIndexOrThrow(column);
//                return cursor.getString(column_index);
//            }
//        } finally {
//            if (cursor != null)
//                cursor.close();
//        }
//        return null;
//    }
//
//    /**
//     * @param uri The Uri to check.
//     * @return Whether the Uri authority is ExternalStorageProvider.
//     */
//    public boolean isExternalStorageDocument(Uri uri) {
//        return "com.android.externalstorage.documents".equals(uri.getAuthority());
//    }
//
//    /**
//     * @param uri The Uri to check.
//     * @return Whether the Uri authority is DownloadsProvider.
//     */
//    public boolean isDownloadsDocument(Uri uri) {
//        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
//    }
//
//    /**
//     * @param uri The Uri to check.
//     * @return Whether the Uri authority is MediaProvider.
//     */
//    public boolean isMediaDocument(Uri uri) {
//        return "com.android.providers.media.documents".equals(uri.getAuthority());
//    }



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
