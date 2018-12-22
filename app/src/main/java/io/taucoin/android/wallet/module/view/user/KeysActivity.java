package io.taucoin.android.wallet.module.view.user;

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
import io.taucoin.android.wallet.util.CopyManager;
import io.taucoin.android.wallet.util.ToastUtils;
import io.taucoin.foundation.util.StringUtil;

public class KeysActivity extends BaseActivity {

    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_public_key)
    TextView tvPublicKey;
    @BindView(R.id.tv_private_key)
    TextView tvPrivateKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keys);
        ButterKnife.bind(this);
        initView();
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
}