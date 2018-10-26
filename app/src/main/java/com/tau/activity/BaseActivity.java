package com.mofei.tau.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.mofei.tau.R;
import com.mofei.tau.info.SharedPreferencesHelper;
import com.mofei.tau.net.NetWorkManager;
import com.mofei.tau.view.DialogWaitting;
import com.mofei.tau.view.SmartDialog;

public class BaseActivity extends AppCompatActivity {

    private DialogWaitting mWaitDialog = null;
    private Toast mToast = null;
    protected static String TAG = "FacebookLoginDemo";

    protected String userId;
    protected String address;
    protected String privkey;
    protected String pubkey;

    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);


        NetWorkManager.getInstance(this).init();

        userId= SharedPreferencesHelper.getInstance(BaseActivity.this).getString("userId","userId");

        address=SharedPreferencesHelper.getInstance(BaseActivity.this).getString("Address","Address");

        privkey=SharedPreferencesHelper.getInstance(BaseActivity.this).getString("Privkey","null");

        pubkey= SharedPreferencesHelper.getInstance(BaseActivity.this).getString("Pubkey","null");
    }


    public DialogWaitting showWaitDialog() {
        if (null == mWaitDialog) {
            mWaitDialog = new DialogWaitting(this);
        }
        mWaitDialog.show();
        return mWaitDialog;
    }

    public SmartDialog newWaitDialog(){
        return new SmartDialog(this);
    }


    public void showWaitDialog(int resid) {
        if (null == mWaitDialog) {
            mWaitDialog = new DialogWaitting(this);
        }
        mWaitDialog.show(resid);
    }

    public void showWaitDialog(String text) {
        if (null == mWaitDialog) {
            mWaitDialog = new DialogWaitting(this);
        }
        mWaitDialog.show(text);
    }

    public void hideWaitDialog() {
        if (null != mWaitDialog) {
            mWaitDialog.dismiss();
        }
    }


    public void showToast(String text) {
        if (null != text) {
            if (null != mToast) {
                mToast.cancel();
            }
            mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
            mToast.show();
        }
    }

    public void showToast(int resid) {
        if (resid > 0) {
            if (null != mToast) {
                mToast.cancel();
            }
            mToast = Toast.makeText(this, resid, Toast.LENGTH_SHORT);
            mToast.show();
        }
    }

    /**
     * 验证邮箱格式是否正确
     */
    public boolean isEmailValid(String email) {
        // String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        String regex = "^[a-zA-Z0-9_.%+-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
        return email.matches(regex);
    }
}
