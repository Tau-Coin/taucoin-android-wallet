package com.tau.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.mofei.tau.R;
import com.tau.entity.req_parameter.FBAddress;
import com.tau.entity.res_post.Balance;
import com.tau.entity.res_post.StatusMessage;
import com.tau.entity.res_post.TalkUpdateRet;
import com.tau.info.SharedPreferencesHelper;
import io.taucoin.android.wallet.net.service.ApiService;
import io.taucoin.foundation.net.NetWorkManager;
import com.tau.util.L;
import com.tau.view.CustomToolBar;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class TalkingActivity extends BaseActivity implements View.OnClickListener{
    private Button mBackWalletHomeBt,mLogoutBt,mUploadBt;
    private TextView mLoadImageTV;
    private ImageView mImageView;
    //调用系统相册-选择图片
    private static final int IMAGE = 1;
    private static String TAG = "FacebookLoginDemo";
    private String imagePath;
    private TextView mSuccessfulTV,mFailedTV,mWaitingTV;
    private int successful,failed,reviewing;
    private String facebookId;
    private String address;
    private CustomToolBar mMainCustomToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talking);

        mBackWalletHomeBt=findViewById(R.id.talking_back_wallet_home);

        mBackWalletHomeBt.setOnClickListener(this);

        mLogoutBt=findViewById(R.id.talking_logout);
        mLogoutBt.setOnClickListener(this);

        mUploadBt=findViewById(R.id.upload);
        mUploadBt.setOnClickListener(this);


        mLoadImageTV=findViewById(R.id.load_image);
        mLoadImageTV.setOnClickListener(this);

        mImageView=findViewById(R.id.imageview);

        mSuccessfulTV=findViewById(R.id.successful);

        mFailedTV=findViewById(R.id.failed);

        mWaitingTV=findViewById(R.id.waiting);


        titleBar();
        initData();
    }

    private void titleBar() {
        //标题栏
        mMainCustomToolBar = findViewById(R.id.talking_titlebar);
        mMainCustomToolBar.getTitleTextView().setText("Talking TAU");
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

         facebookId= SharedPreferencesHelper.getInstance(TalkingActivity.this).getString("userId","userId");
         address=SharedPreferencesHelper.getInstance(TalkingActivity.this).getString("Address","Address");

         showWaitDialog();
         updateImageNum(facebookId,address);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.talking_back_wallet_home:
                finish();
               // startActivity(new Intent(TalkingActivity.this,MainActivity.class));
                break;
            case R.id.talking_logout:
               logout();
                break;
            case R.id.upload:
                if (imagePath!=null){
                    showWaitDialog();
                    upLoadImg(imagePath,facebookId,address);
                }else {
                    showToast("Please click image to select the photo album.");
                }

                break;
            case R.id.load_image:
                loadImage();

                break;
        }

    }

    private void updateImageNum(String facebookId,String address) {

        FBAddress fbAddress=new FBAddress();
        fbAddress.setFacebookid(facebookId );
        fbAddress.setAddress(address);

      //  Log.i(TAG,"userId"+userId+"  Address  "+Address);

        ApiService apiService=NetWorkManager.createApiService(ApiService.class);
        Observable<Balance<TalkUpdateRet>> observable=apiService.getTalkUpdate(fbAddress);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Balance<TalkUpdateRet>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Balance<TalkUpdateRet> balanceRet) {
                        L.i("Message: "+balanceRet.getMessage().toString());
                        L.i("Successful: "+balanceRet.getRet().getSuccessful());
                        L.i("Failed: "+balanceRet.getRet().getFailed());
                        L.i("Reviewing: "+balanceRet.getRet().getReviewing());


                        mSuccessfulTV.setText(String.valueOf(balanceRet.getRet().getSuccessful()));
                        mFailedTV.setText(String.valueOf(balanceRet.getRet().getFailed()));
                        mWaitingTV.setText(String.valueOf(balanceRet.getRet().getReviewing()));

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        L.i(""+e.toString());
                    }

                    @Override
                    public void onComplete() {
                        hideWaitDialog();
                        L.i(""+"onComplete");
                    }
                });
    }

    private void loadImage() {
        //调用相册
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取图片路径
        if (requestCode == IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUrl = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImageUrl, filePathColumns, null, null, null);
            if (cursor!=null){
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumns[0]);
                imagePath = cursor.getString(columnIndex);
                L.e("文件路径:"+imagePath);
                if(imagePath!=null){

                    showImage(imagePath);
                    cursor.close();

                }

            }


        }
    }

    private void upLoadImg(String imagePath,String facebookId,String address) {
        File file=new File(imagePath);



        FBAddress fbAddress=new FBAddress();
        fbAddress.setFacebookid(facebookId);
        fbAddress.setAddress(address);

        L.e(facebookId);
        L.e(address);
        // 创建RequestBody，传入参数："multipart/form-data"，String
        RequestBody requestFacebookid = RequestBody.create(MediaType.parse("multipart/form-data"), facebookId);
        RequestBody requestAddress = RequestBody.create(MediaType.parse("multipart/form-data"), address);
        // 创建RequestBody，传入参数："multipart/form-data"，File
        RequestBody requestImgFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        // 创建MultipartBody.Part，用于封装文件数据
        MultipartBody.Part requestImgPart =
                MultipartBody.Part.createFormData("image", file.getName(), requestImgFile);

        ApiService apiService= NetWorkManager.createApiService(ApiService.class);
        Observable<StatusMessage> observable=apiService.getTalkUpload(requestFacebookid,requestAddress,requestImgPart);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<StatusMessage>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(StatusMessage statusMessage) {
                        L.i("Message: "+statusMessage.getMessage().toString());

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        L.i(""+e.toString());
                    }

                    @Override
                    public void onComplete() {
                        hideWaitDialog();
                        showToast("Upload pictures successfully");
                        mImageView.setVisibility(View.INVISIBLE);
                        L.i(""+"onComplete");

                      /*  try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }*/

                       // updateImageNum(facebookId,address);
                    }
                });
    }

    //加载图片
    private void showImage(String imaePath){
        Bitmap bm = BitmapFactory.decodeFile(imaePath);
         mImageView.setImageBitmap(bm);
    }

    private void logout() {
        LoginManager.getInstance().logOut();
        SharedPreferencesHelper.getInstance(TalkingActivity.this).putBoolean("isLogin",false);
        L.i("logOut");
        startActivity(new Intent(TalkingActivity.this,LoginActivity.class));
    }
}
