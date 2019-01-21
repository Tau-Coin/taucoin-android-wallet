package io.taucoin.android.wallet.module.view.manage;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.mofei.tau.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.taucoin.android.wallet.MyApplication;
import io.taucoin.android.wallet.base.BaseActivity;
import io.taucoin.android.wallet.module.presenter.UserPresenter;
import io.taucoin.android.wallet.module.view.SplashActivity;
import io.taucoin.android.wallet.util.ActivityUtil;
import io.taucoin.android.wallet.util.UserUtil;
import io.taucoin.android.wallet.widget.InputDialog;

public class ProfileActivity extends BaseActivity {

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
}