package io.taucoin.android.wallet.module.view.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.mofei.tau.R;

import io.taucoin.android.wallet.base.BaseActivity;
import io.taucoin.android.wallet.module.view.manage.ChangePasswordActivity;
import io.taucoin.android.wallet.util.SharedPreferencesHelper;

public class LockActivity extends BaseActivity {

    private PinLockView mPinLockView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_code);

        initView();
    }

    private void initView() {
        mPinLockView = (PinLockView) findViewById(R.id.pin_lock_view);

        IndicatorDots mIndicatorDots = (IndicatorDots) findViewById(R.id.indicator_dots);
        mPinLockView.attachIndicatorDots(mIndicatorDots);

        mIndicatorDots.setIndicatorType(IndicatorDots.IndicatorType.FIXED);

        mPinLockView.setPinLockListener(new PinLockListener() {
            @Override
            public void onComplete(String pin) {
               if (SharedPreferencesHelper.getInstance().getString("code","cvcvcxv").equals(pin))
               {

                   handelRefer();

               }else
               {
                   AlertDialog.Builder builder=new AlertDialog.Builder(LockActivity.this);
                   builder.setMessage("The Password is Wrong");
                   builder.setTitle("Try Again");
                   builder.setNegativeButton("ok", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           dialog.cancel();
                           dialog.dismiss();
                       }
                   });
                   AlertDialog alertDialog=builder.create();
                   alertDialog.show();

                   mPinLockView.resetPinLockView();
               }
            }

            @Override
            public void onEmpty() {
                Log.i("CODE","empty");

            }

            @Override
            public void onPinChange(int pinLength, String intermediatePin) {
                Log.i("CODE","change");

            }
        });



    }

    private void handelRefer() {
        Bundle ex=getIntent().getExtras();
        if (ex!=null)
        {
            if (ex.getString("refer")!=null)
            {
                    if (ex.getString("refer").equals("pass"))
                    {
                        startActivity(new Intent(LockActivity.this, ChangePasswordActivity.class));
                        finish();
                    }else
                    {
                        startActivity(new Intent(LockActivity.this,MainActivity.class));
                        finish();
                    }
            }else
            {
                startActivity(new Intent(LockActivity.this,MainActivity.class));
                finish();
            }
        }else
        {
            startActivity(new Intent(LockActivity.this,MainActivity.class));
            finish();
        }
    }

}
