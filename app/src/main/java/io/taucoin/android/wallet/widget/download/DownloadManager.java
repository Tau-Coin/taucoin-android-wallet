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
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.naturs.logger.Logger;
import com.mofei.tau.R;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.taucoin.android.wallet.MyApplication;
import io.taucoin.android.wallet.module.bean.VersionBean;
import io.taucoin.android.wallet.module.service.UpgradeService;
import io.taucoin.android.wallet.module.view.manage.UpgradeActivity;
import io.taucoin.android.wallet.util.FileUtil;
import io.taucoin.android.wallet.util.PermissionUtils;
import io.taucoin.foundation.util.ActivityManager;
import io.taucoin.foundation.util.AppUtil;
import io.taucoin.foundation.util.permission.EasyPermissions;

/**
 * Description: app download and update dialog manager
 * Author:yang
 * Date: 2017/11/17
 */
public class DownloadManager {

    private DownloadDialog mDialog;
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
        View view = LinearLayout.inflate(activity, R.layout.dialog_download, null);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.tvTitle.setText(R.string.app_upgrade_title);
        viewHolder.tvContent.setText(Html.fromHtml(version.getContent()));

        int leftButton = R.string.app_upgrade_not_upgrade;
        int rightButton = isDownload ? R.string.app_upgrade_install : R.string.app_upgrade_to_upgrade;
        mDialog = new DownloadDialog.Builder(activity)
            .setContentView(view)
            .setNegativeButton(activity.getString(leftButton), (dialog, which) -> {
                if (version.isForced()) {
                    dialog.dismiss();
                    ActivityManager.getInstance().finishAll();
                } else {
                    dialog.dismiss();
                    Intent intent = new Intent(activity, UpgradeService.class);
                    activity.stopService(intent);
                }
                activity.finish();
            })
            .setPositiveButton(activity.getString(rightButton), (dialog, which) -> {
                if (isDownload) {
                    installApk();
                } else {
                    String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
                    if (EasyPermissions.hasPermissions(activity, permission)) {
                        dialog.dismiss();
                        showProgressDialog(activity);
                        startDownload(activity);
                    } else {
                        EasyPermissions.requestPermissions(activity,
                            activity.getString(R.string.app_upgrade_permission_tip),
                            PermissionUtils.REQUEST_PERMISSIONS_RECORD_STORAGE, permission);
                    }
                }
            })
            .setCanceledOnTouchOutside(false)
            .create();
        mDialog.show();
    }

    /**
     * Check whether the file exists and is valid
     */
    private  boolean isFileExists(VersionBean version) {
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
    public void startDownload(UpgradeActivity activity) {
        Intent intent = new Intent(activity, UpgradeService.class);
        activity.bindService(intent, activity, Activity.BIND_AUTO_CREATE);
        activity.startService(intent);
    }

    /**
     * Install the downloaded APK file
     */
    private void installApk() {
        File file = new File(mVersionBean.getDownloadFilePath());
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
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Uri apkUri = FileUtil.getUriForFile(file);
        // over 7.0
        if (Build.VERSION.SDK_INT >= 24) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }
    /**
     * update download Progress
     */
    private void updateProgress(int progress) {
        mViewHolder.progressBar.setProgress(progress);
        String progressStr = progress + "%";
        mViewHolder.tvProgress.setText(progressStr);
        if(progress == 0){
            mViewHolder.tvFailMsg.setVisibility(View.INVISIBLE);
            mViewHolder.tvRetry.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * show Progress Dialog
     */
    private void showProgressDialog(FragmentActivity activity) {
        View view = LinearLayout.inflate(activity, R.layout.dialog_download_progress, null);
        mViewHolder = new ProgressViewHolder(view);
        mViewHolder.progressBar.setMax(100);
        updateProgress(0);
        DownloadDialog dialog = new DownloadDialog.Builder(activity)
                .setContentView(view)
                .setCanceledOnTouchOutside(false)
                .create();
        dialog.show();
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
            installApk(file);
            mViewHolder.tvRetry.setText(R.string.app_upgrade_install);
            mViewHolder.tvRetry.setVisibility(View.VISIBLE);
        }

        @Override
        public void handlerUpgradeFail() {
            Logger.d("handlerUpgradeFail");
            isDownload = false;
            mViewHolder.tvFailMsg.setVisibility(View.VISIBLE);
            mViewHolder.tvRetry.setVisibility(View.VISIBLE);
        }
    };

    class ViewHolder {
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_content)
        TextView tvContent;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    class ProgressViewHolder {
        @BindView(R.id.progress_bar)
        ProgressBar progressBar;
        @BindView(R.id.tv_progress)
        TextView tvProgress;
        @BindView(R.id.tv_fail_msg)
        TextView tvFailMsg;
        @BindView(R.id.tv_retry)
        TextView tvRetry;

        // retry download
        @OnClick(R.id.tv_retry)
        void onRetry(){
            if(isDownload){
                installApk();
            }else{
                updateProgress(0);
                Activity activity = ActivityManager.getInstance().currentActivity();
                if(activity instanceof UpgradeActivity){
                    startDownload((UpgradeActivity) activity);
                }
            }
        }
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