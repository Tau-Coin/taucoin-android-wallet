package io.taucoin.android.wallet.module.view.user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.mofei.tau.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.taucoin.android.wallet.MyApplication;
import io.taucoin.android.wallet.base.BaseActivity;
import io.taucoin.android.wallet.db.entity.KeyValue;
import io.taucoin.android.wallet.module.presenter.TxPresenter;
import io.taucoin.android.wallet.util.TakePhoneUtil;
import io.taucoin.android.wallet.util.UserUtil;
import io.taucoin.android.wallet.widget.InputDialog;

public class ProfileActivity extends BaseActivity {

    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;

    private TxPresenter mTxPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        mTxPresenter = new TxPresenter();
        loadData();
    }

    private void loadData() {
        UserUtil.setAvatar(ivAvatar);
        UserUtil.setNickName(tvName);
    }

    @OnClick(R.id.rl_avatar)
    public void onAvatarClicked() {
        KeyValue keyValue = MyApplication.getKeyValue();
        if(keyValue != null){
            TakePhoneUtil.takePhotoForName(this, keyValue.getAddress());
        }
    }

    @OnClick(R.id.rl_name)
    public void onNameClicked() {
        new InputDialog.Builder(this)
            .setNegativeButton(R.string.common_cancel, (InputDialog.InputDialogListener) (dialog, text) -> dialog.dismiss())
            .setPositiveButton(R.string.common_confirm, (InputDialog.InputDialogListener) (dialog, text) -> {
                MyApplication.getKeyValue().setNickName(text);
                mTxPresenter.saveName(text);
                UserUtil.setNickName(tvName);
                dialog.dismiss();
            }).create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        TakePhoneUtil.onActivityResult(this, requestCode, resultCode, data);
        if(requestCode == TakePhoneUtil.CODE_RESULT_REQUEST){
            ivAvatar.setImageBitmap(TakePhoneUtil.getPhotoZoom());
            mTxPresenter.saveAvatar(TakePhoneUtil.getFileName());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}