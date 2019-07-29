package io.taucoin.android.wallet.module.view.manage;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;

import com.mofei.tau.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.taucoin.android.wallet.base.BaseActivity;
import io.taucoin.android.wallet.module.view.main.MainActivity;

public class ChangePasswordActivity extends BaseActivity {

    private FragmentTransaction transaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        ButterKnife.bind(this);
        transaction=getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.container,new DefinePasswordFragment()).commit();



    }
    @OnClick({ R.id.iv_left_back})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_left_back:
                startActivity(new Intent(ChangePasswordActivity.this, MainActivity.class));
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ChangePasswordActivity.this, MainActivity.class));
        finish();

    }
}
