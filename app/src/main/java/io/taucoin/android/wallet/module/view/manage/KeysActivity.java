package io.taucoin.android.wallet.module.view.manage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.mofei.tau.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import io.taucoin.android.wallet.MyApplication;
import io.taucoin.android.wallet.base.BaseActivity;
import io.taucoin.android.wallet.db.entity.KeyValue;
import io.taucoin.android.wallet.module.presenter.UserPresenter;
import io.taucoin.android.wallet.module.view.manage.iview.IImportKeyView;
import io.taucoin.android.wallet.util.CopyManager;
import io.taucoin.android.wallet.util.ToastUtils;
import io.taucoin.foundation.util.StringUtil;

public class KeysActivity extends BaseActivity implements IImportKeyView {

    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_public_key)
    TextView tvPublicKey;
    @BindView(R.id.tv_private_key)
    TextView tvPrivateKey;

    private UserPresenter mUserPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keys);
        ButterKnife.bind(this);
        initView();
        mUserPresenter = new UserPresenter(this);
    }

    private void initView() {
        KeyValue keyValue = MyApplication.getKeyValue();
        if(keyValue != null){
            tvAddress.setText(keyValue.getAddress());
            tvPublicKey.setText(keyValue.getPubkey());
            tvPrivateKey.setText(keyValue.getPrivkey());
        }
    }

    @OnClick(R.id.btn_import_key)
    void onImportKeyClick() {
        Intent intent = new Intent(this, ImportKeyActivity.class);
        startActivity(intent);
        this.finish();
    }

    @OnClick(R.id.btn_generate_key)
    public void onBtnGenerateClicked() {
        mUserPresenter.showSureDialog(this);
    }

    @Override
    public void gotoKeysActivity() {
        initView();
    }

    @OnLongClick({R.id.tv_address, R.id.tv_public_key, R.id.tv_private_key})
    boolean copyData(View view) {
        CopyManager.copyText( StringUtil.getText((TextView) view));
        switch (view.getId()){
            case R.id.tv_address:
                ToastUtils.showShortToast(R.string.keys_address_copy);
                break;
            case R.id.tv_public_key:
                ToastUtils.showShortToast(R.string.keys_public_copy);
                break;
            case R.id.tv_private_key:
                ToastUtils.showShortToast(R.string.keys_private_copy);
                break;
            default:
                break;
        }
        return false;
    }
    @OnClick({R.id.iv_address_copy, R.id.iv_public_keys_copy, R.id.iv_private_key_copy})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_address_copy:
                copyData(tvAddress);
                break;
            case R.id.iv_public_keys_copy:
                copyData(tvPublicKey);
                break;
            case R.id.iv_private_key_copy:
                copyData(tvPrivateKey);
                break;
            default:
                break;
        }
    }
}