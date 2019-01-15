package io.taucoin.android.wallet.module.view.manage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.TextView;

import com.mofei.tau.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.taucoin.android.wallet.MyApplication;
import io.taucoin.android.wallet.base.BaseActivity;
import io.taucoin.android.wallet.db.entity.KeyValue;
import io.taucoin.android.wallet.module.presenter.UserPresenter;
import io.taucoin.android.wallet.module.view.SplashActivity;
import io.taucoin.android.wallet.util.ActivityUtil;
import io.taucoin.android.wallet.util.TakePhotoUtil;
import io.taucoin.android.wallet.util.UserUtil;
import io.taucoin.android.wallet.widget.InputDialog;

public class ProfileActivity extends BaseActivity {

    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;

    private UserPresenter mUserPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(null != savedInstanceState){
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            ActivityUtil.startActivity(intent, this, SplashActivity.class);
        }
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        mUserPresenter = new UserPresenter();
        loadData();
    }

    private void loadData() {
        UserUtil.setNickName(tvName);
        UserUtil.setAvatar(ivAvatar);
    }

    @OnClick(R.id.rl_avatar)
    public void onAvatarClicked() {
        KeyValue keyValue = MyApplication.getKeyValue();
        if(keyValue != null){
            TakePhotoUtil.takePhotoForName(this, keyValue.getAddress());
        }
    }

    @OnClick(R.id.rl_name)
    public void onNameClicked() {
        new InputDialog.Builder(this)
            .setNegativeButton(R.string.common_cancel, (InputDialog.InputDialogListener) (dialog, text) -> dialog.cancel())
            .setPositiveButton(R.string.common_done, (InputDialog.InputDialogListener) (dialog, text) -> {
                MyApplication.getKeyValue().setNickName(text);
                mUserPresenter.saveName(text);
                UserUtil.setNickName(tvName);
                dialog.cancel();
            }).create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        TakePhotoUtil.onActivityResult(this, requestCode, resultCode, data);
        if(requestCode == TakePhotoUtil.CODE_RESULT_REQUEST && resultCode == RESULT_OK){
            Bitmap bitmap = TakePhotoUtil.getPhotoZoom();
            if(bitmap != null){
                ivAvatar.setImageBitmap(bitmap);
                mUserPresenter.saveAvatar(TakePhotoUtil.getFileName(), bitmap);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            TakePhotoUtil.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}