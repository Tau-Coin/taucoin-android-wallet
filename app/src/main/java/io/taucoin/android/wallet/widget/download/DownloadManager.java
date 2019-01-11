/**
 * Copyright 2018 Taucoin Core Developers.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.taucoin.android.wallet.widget.download;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.naturs.logger.Logger;
import com.mofei.tau.R;

import java.io.File;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.taucoin.android.wallet.MyApplication;
import io.taucoin.android.wallet.module.bean.VersionBean;
import io.taucoin.android.wallet.module.service.UpgradeService;
import io.taucoin.android.wallet.module.view.manage.UpgradeActivity;
import io.taucoin.android.wallet.util.FileUtil;
import io.taucoin.android.wallet.util.PermissionUtils;
import io.taucoin.foundation.net.callback.LogicObserver;
import io.taucoin.foundation.util.ActivityManager;
import io.taucoin.foundation.util.AppUtil;
import io.taucoin.foundation.util.permission.EasyPermissions;

/**
 * Description: app download and update dialog manager
 * Author:yang
 * Date: 2017/11/17
 */
public class DownloadManager {

    private AlertDialog mDialog;
    private boolean isDownload = false;
    private ProgressViewHolder mViewHolder;
    private VersionBean mVersionBean;

    /**
     * show UpGrade Dialog
     */
    public void showUpGradeDialog(UpgradeActivity activity, VersionBean version) {
        mVersionBean = version;
        isDownload = isFileExists(version);
        if (mDialog != null) {
            mDialog.dismiss();
        }
        int leftButton = R.string.common_cancel;
        int rightButton = isDownload ? R.string.app_upgrade_install : R.string.common_ok;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity)
            .setTitle(R.string.app_upgrade_title)
            .setMessage(Html.fromHtml(version.getContent()))
            .setPositiveButton(rightButton, null)
            .setCancelable(false);


        builder.setNegativeButton(leftButton, (dialog, which) -> {
            if (version.isForced()) {
                ActivityManager.getInstance().finishAll();
            }
            else {
                Intent intent = new Intent(activity, UpgradeService.class);
                activity.stopService(intent);
            }
            activity.finish();
        });

        mDialog = builder.create();
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);
        mDialog.show();

        mDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            if (isDownload) {
                installApk();
            } else {
                String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
                if (EasyPermissions.hasPermissions(activity, permission)) {
                    mDialog.dismiss();
                    showProgressDialog(activity);
                    startDownload(activity);
                } else {
                    EasyPermissions.requestPermissions(activity,
                            activity.getString(R.string.permission_tip_upgrade_denied),
                            PermissionUtils.REQUEST_PERMISSIONS_STORAGE, permission);
                }
            }
        });
        if(mVersionBean.isForced()){
            mDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setVisibility(View.GONE);
        }
        resetButtonCaps();
    }

    private void resetButtonCaps() {
        if(mDialog != null){
            mDialog.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
            mDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);
        }
    }

    /**
     * Check whether the file exists and is valid
     */
    private boolean isFileExists(VersionBean version) {
        String filePath = version.getDownloadFilePath();
        String fileName = version.getDownloadFileName();
        String allPath = filePath + fileName;
        File file = new File(allPath);

        boolean isFileExists = file.exists();
        if(isFileExists){
            Context context = MyApplication.getInstance();
            isFileExists = AppUtil.getUnInstallApkInfo(context, allPath);
        }
        return isFileExists;
    }

    /**
     * start server and bindService to update app
     */
    private void startDownload(UpgradeActivity activity) {
        Intent intent = new Intent(activity, UpgradeService.class);
        activity.bindService(intent, activity, Activity.BIND_AUTO_CREATE);
        activity.startService(intent);
    }
    /**
     * retry download
     */
    private void retryDownload(UpgradeActivity activity) {
        activity.mUpgradeServiceBinder.retryDownload();
    }

    /**
     * Install the downloaded APK file
     */
    private void installApk() {
        if(mVersionBean == null) return;
        String allPath = mVersionBean.getDownloadFilePath() + mVersionBean.getDownloadFileName();
        File file = new File(allPath);
        installApk(file);
    }

    private void installApk(File file) {
        if(file == null){
            Logger.d("installApk is empty");
            return;
        }
        Logger.d("installApk start" + file.getAbsolutePath());
        Context context = ActivityManager.getInstance().currentActivity();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Uri apkUri = FileUtil.getUriForFile(file);
        //over8.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            boolean hasInstallPermission = context.getPackageManager().canRequestPackageInstalls();
            if (!hasInstallPermission) {
                startInstallPermissionSettingActivity();
                return;
            }
        }
        // over 7.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    private void startInstallPermissionSettingActivity() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Context context = ActivityManager.getInstance().currentActivity();
            Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    /**
     * update download Progress
     */
    private void updateProgress(int progress) {
        if(mViewHolder == null){
            return;
        }
        mViewHolder.progressBar.setProgress(progress);
        String progressStr = progress + "%";
        mViewHolder.tvProgress.setText(progressStr);
        if(progress == 0){
            mViewHolder.tvFailMsg.setVisibility(View.INVISIBLE);
            mDialog.getButton(AlertDialog.BUTTON_POSITIVE).setVisibility(View.GONE);
            mDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setVisibility(View.GONE);
        }
    }

    /**
     * show Progress Dialog
     */
    private void showProgressDialog(FragmentActivity activity) {
        if (mDialog != null) {
            mDialog.dismiss();
        }

        View view = LinearLayout.inflate(activity, R.layout.dialog_download_progress, null);
        mViewHolder = new ProgressViewHolder(view);
        mViewHolder.progressBar.setMax(100);
        updateProgress(0);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                .setCancelable(false)
                .setTitle(R.string.app_upgrade_progress)
                .setView(view);

        builder.setNegativeButton(R.string.common_cancel, (dialog, which) -> {
            if (mVersionBean.isForced()) {
                ActivityManager.getInstance().finishAll();
            } else {
                Intent intent = new Intent(activity, UpgradeService.class);
                activity.stopService(intent);
            }
            activity.finish();
        });
        mDialog = builder.create();
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);
        mDialog.show();
        Observable.create((ObservableOnSubscribe<View>)
            e -> mDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(e::onNext))
            .throttleFirst(2, TimeUnit.SECONDS)
            .subscribe(new LogicObserver<View>() {
                @Override
                public void handleData(View view) {
                    if(isDownload){
                        installApk();
                    }else{
                        updateProgress(0);
                        Activity currentActivity = ActivityManager.getInstance().currentActivity();
                        if(currentActivity instanceof UpgradeActivity){
                            retryDownload((UpgradeActivity) currentActivity);
                        }
                    }
                }
            });
        mDialog.getButton(AlertDialog.BUTTON_POSITIVE).setVisibility(View.GONE);
        mDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setVisibility(View.GONE);
        resetButtonCaps();
    }

    public void closeDialog() {
        if(mDialog != null && mDialog.isShowing()){
            mDialog.dismiss();
        }
    }

    public OnUpgradeProgressListener getOnUpgradeProgressListener() {
        return mOnUpgradeProgressListener;
    }

    private OnUpgradeProgressListener mOnUpgradeProgressListener = new OnUpgradeProgressListener() {


        @Override
        public void handlerUpgradeStart() {
            Logger.d("handlerUpgradeStart");
        }

        /**
         * handler Upgrade Progress
         *
         * @param progress (max 100)
         */
        @Override
        public void handlerUpgradeProgress(int progress) {
            Logger.d("handlerUpgradeProgress progress=" +progress);
            if (progress == 100) {
                isDownload = true;
            }
            updateProgress(progress);
        }

        @Override
        public void handlerUpgradeSuccess(File file) {
            Logger.d("handlerUpgradeSuccess.path=" + file.getAbsolutePath());
            isDownload = true;
            updateProgress(100);
//            installApk(file);
            installApk();
            if(null != mDialog){
                mDialog.getButton(AlertDialog.BUTTON_POSITIVE).setText(R.string.app_upgrade_install);
                mDialog.getButton(AlertDialog.BUTTON_POSITIVE).setVisibility(View.VISIBLE);
                if(!mVersionBean.isForced()){
                    mDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        public void handlerUpgradeFail() {
            Logger.d("handlerUpgradeFail");
            isDownload = false;
            if(null != mViewHolder && null != mDialog){
                mViewHolder.tvFailMsg.setVisibility(View.VISIBLE);
                mDialog.getButton(AlertDialog.BUTTON_POSITIVE).setText(R.string.app_upgrade_retry);
                mDialog.getButton(AlertDialog.BUTTON_POSITIVE).setVisibility(View.VISIBLE);
                if(!mVersionBean.isForced()){
                    mDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setVisibility(View.VISIBLE);
                }
            }
        }
    };

    public void onRequestPermissionsResult(UpgradeActivity activity, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionUtils.REQUEST_PERMISSIONS_STORAGE:
                if (grantResults.length > 0) {
                    if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                        isDownload = isFileExists(mVersionBean);
                        if(isDownload){
                            installApk();
                        }else{
                            startDownload(activity);
                        }
                    }else{
                        PermissionUtils.checkUserBanPermission(activity, permissions[0], R.string.permission_tip_upgrade_never_ask_again);
                    }
                }
                break;

            default:
                break;
        }
    }

    class ProgressViewHolder {
        @BindView(R.id.progress_bar)
        ProgressBar progressBar;
        @BindView(R.id.tv_progress)
        TextView tvProgress;
        @BindView(R.id.tv_fail_msg)
        TextView tvFailMsg;

        ProgressViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


    /**
     * Download Upgrade Pack Progress Listener
     */
    public interface OnUpgradeProgressListener{
        /** handler Upgrade Start */
        void handlerUpgradeStart();

        /** handler Upgrade Progress */
        void handlerUpgradeProgress(int progress);

        /** handler Upgrade Success*/
        void handlerUpgradeSuccess(File file);

        /** handler Upgrade Fail*/
        void handlerUpgradeFail();
    }
}