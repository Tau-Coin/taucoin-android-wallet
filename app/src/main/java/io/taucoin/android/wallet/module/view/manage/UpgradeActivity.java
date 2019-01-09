package io.taucoin.android.wallet.module.view.manage;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.naturs.logger.Logger;

import io.taucoin.android.wallet.MyApplication;
import io.taucoin.android.wallet.base.BaseActivity;
import io.taucoin.android.wallet.base.TransmitKey;
import io.taucoin.android.wallet.module.bean.VersionBean;
import io.taucoin.android.wallet.module.service.UpgradeService;
import io.taucoin.android.wallet.module.view.SplashActivity;
import io.taucoin.android.wallet.util.ActivityUtil;
import io.taucoin.android.wallet.widget.download.DownloadManager;
import io.taucoin.foundation.util.ActivityManager;
import io.taucoin.foundation.util.AppUtil;

/**
 * Description: version upgrade activity
 * Author: yang
 * Date: 2019/1/5 09:08
 */

public class UpgradeActivity extends BaseActivity implements ServiceConnection {

    private DownloadManager mDownloadManager;
    private VersionBean mVersion;
    public UpgradeService.UpgradeServiceBinder mUpgradeServiceBinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(null != savedInstanceState){
            if(null != mDownloadManager ){
                mDownloadManager.closeDialog();
            }
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            ActivityUtil.startActivity(intent, this, SplashActivity.class);

        }
        mVersion = getIntent().getParcelableExtra(TransmitKey.BEAN);
        Logger.d("Upgrade show dialog start");
        if(mVersion != null && ActivityManager.getInstance().getActivitySize() > 0 &&
                AppUtil.isOnForeground(MyApplication.getInstance())){
            mDownloadManager = new DownloadManager();
            showUpGradeDialog();
        }else{
            Logger.d("Upgrade stop on onCreate");
            UpgradeService.stopUpdateService();
            finish();
        }
    }

    private void showUpGradeDialog() {
        Logger.d("show upgrade dialog for user");
        mDownloadManager.showUpGradeDialog(this, mVersion);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mDownloadManager.onRequestPermissionsResult(UpgradeActivity.this, requestCode, permissions, grantResults);
    }

    /**
     * activity and service connected
     */
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        Logger.d("activity and service connected");
        mUpgradeServiceBinder = (UpgradeService.UpgradeServiceBinder) service;
        mUpgradeServiceBinder.setUpgradeProgressListener(mDownloadManager.getOnUpgradeProgressListener());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != mUpgradeServiceBinder){
            mUpgradeServiceBinder.setUpgradeProgressListener(null);
            unbindService(this);
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        unbindService(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}