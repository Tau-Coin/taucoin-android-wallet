package io.taucoin.android.wallet.module.view.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mofei.tau.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.taucoin.android.wallet.MyApplication;
import io.taucoin.android.wallet.base.BaseActivity;
import io.taucoin.android.wallet.db.entity.KeyValue;
import io.taucoin.android.wallet.module.presenter.TxPresenter;
import io.taucoin.android.wallet.util.ToastUtils;
import io.taucoin.android.wallet.widget.CommonDialog;
import io.taucoin.foundation.net.callback.LogicObserver;
import io.taucoin.foundation.util.DrawablesUtil;
import io.taucoin.foundation.util.StringUtil;
import io.taucoin.platform.adress.Key;
import io.taucoin.platform.adress.KeyManager;

public class ImportKeyActivity extends BaseActivity {

    @BindView(R.id.et_private_key)
    EditText etPrivateKey;
    @BindView(R.id.btn_import)
    Button btnImport;
    @BindView(R.id.btn_generate)
    Button btnGenerate;
    @BindView(R.id.tv_how_import)
    TextView tvHowImport;

    private TxPresenter mTxPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_key);
        ButterKnife.bind(this);
        DrawablesUtil.setUnderLine(tvHowImport);
        mTxPresenter = new TxPresenter();
    }

    @OnClick(R.id.btn_import)
    public void onBtnImportClicked() {
        String privateKey = etPrivateKey.getText().toString();
        if(StringUtil.isEmpty(privateKey)){
            ToastUtils.showShortToast(R.string.keys_private_empty);
            return;
        }
        Key key = KeyManager.validateKey(privateKey);
        if(key == null){
            ToastUtils.showShortToast(R.string.keys_private_error);
        }else{
            KeyValue keyValue = new KeyValue();
            keyValue.setPrivkey(privateKey);
            keyValue.setPubkey(key.getPubkey());
            keyValue.setAddress(key.getAddress());
            showSureDialog(keyValue);
        }
    }

    @OnClick(R.id.btn_generate)
    public void onBtnGenerateClicked() {
        showSureDialog(null);
    }

    private void showSureDialog(KeyValue keyValue) {
        KeyValue currKeyValue = MyApplication.getKeyValue();
        View view = LinearLayout.inflate(this, R.layout.view_dialog_keys, null);
        new CommonDialog.Builder(this)
                .setContentView(view)
                .setButtonWidth(240)
                .setPositiveButton(R.string.keys_dialog_yes, (dialog, which) -> {
                    dialog.dismiss();
                    saveKeyAndAddress(keyValue);
                }).setNegativeButton(R.string.keys_dialog_no, (dialog, which) -> {
                    dialog.dismiss();
                    gotoKeysActivity();
                }).isEnabledNegative(null != currKeyValue)
                .create().show();
    }

    private void gotoKeysActivity() {
        Intent intent = new Intent(this, KeysActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void saveKeyAndAddress(KeyValue keyValue) {
        mTxPresenter.saveKeyAndAddress(keyValue, new LogicObserver() {

            @Override
            public void handleData(Object o) {
                gotoKeysActivity();
            }
        });
    }
}
