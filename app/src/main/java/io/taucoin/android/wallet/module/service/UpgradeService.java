package io.taucoin.android.wallet.module.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.NonNull;

import com.github.naturs.logger.Logger;
import com.mofei.tau.R;

import java.io.File;
import java.util.concurrent.TimeUnit;

import io.taucoin.android.wallet.MyApplication;
import io.taucoin.android.wallet.base.TransmitKey;
import io.taucoin.android.wallet.module.bean.MessageEvent;
import io.taucoin.android.wallet.module.bean.VersionBean;
import io.taucoin.android.wallet.module.model.AppModel;
import io.taucoin.android.wallet.module.model.IAppModel;
import io.taucoin.android.wallet.module.view.manage.UpgradeActivity;
import io.taucoin.android.wallet.net.callback.FileCallback;
import io.taucoin.android.wallet.net.callback.FileResponseBody;
import io.taucoin.android.wallet.net.callback.TAUObserver;
import io.taucoin.android.wallet.util.EventBusUtil;
import io.taucoin.android.wallet.util.FileUtil;
import io.taucoin.android.wallet.util.ProgressManager;
import io.taucoin.android.wallet.util.SharedPreferencesHelper;
import io.taucoin.android.wallet.util.ToastUtils;
import io.taucoin.android.wallet.util.UriUtil;
import io.taucoin.android.wallet.widget.download.DownloadManager;
import io.taucoin.foundation.net.NetWorkManager;
import io.taucoin.foundation.net.callback.DataResult;
import io.taucoin.foundation.util.ActivityManager;
import io.taucoin.foundation.util.AppUtil;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Description:version upgrade service
 * Author:yang
 * Date: 2019/1/5 13:46
 */

public class UpgradeService extends Service {

    private enum UpgradeStatus{
        CHECK,
        START,
        PROGRESS,
        SUCCESS,
        FAIL,
        DESTROY
    }

    /** Upgrade download APK package progress listener */
    private DownloadManager.OnUpgradeProgressListener mUpgradeProgressListener;

    /** service and Activity communication */
    private UpgradeServiceBinder mUpgradeServiceBinder;


    private Retrofit.Builder mRetrofitBuilder;
    private Call<ResponseBody> mCall;
    private IAppModel mAppModel;

    private UpgradeStatus mStatus;
    private VersionBean mVersionBean;

    @Override
    public void onCreate() {
        super.onCreate();
        mStatus = UpgradeStatus.CHECK;
        mVersionBean = null;
        mAppModel = new AppModel();
        mRetrofitBuilder = NetWorkManager.getRetrofit().newBuilder();
        mRetrofitBuilder.client(initOkHttpClient());
        SharedPreferencesHelper.getInstance().putBoolean(TransmitKey.UPGRADE, false);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean isShowTip = false;
        if(intent != null){
            isShowTip = intent.getBooleanExtra(TransmitKey.ISSHOWTIP, false);
        }
        switch (mStatus){
            case CHECK:
                if(mVersionBean == null){
                    checkAppVersion(isShowTip);
                }else {
                    showUpgradeDialog(mVersionBean, isShowTip);
                }
                break;
            case START:
            case FAIL:
                mStatus = UpgradeStatus.PROGRESS;
                initAndStartDownload();
                break;
            default:
                break;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void initAndStartDownload() {
        if(mVersionBean == null){
            stopSelf();
        }
        try {
            if(mUpgradeProgressListener != null){
                mUpgradeProgressListener.handlerUpgradeStart();
            }
            Uri uri = Uri.parse(mVersionBean.getLink());
            String destFileName = mVersionBean.getDownloadFileName();
            String destFileDir = mVersionBean.getDownloadFilePath();

            // delete old version install apk
            FileUtil.deleteFile(destFileDir);

            mFileCallback.setFileData(destFileDir, destFileName);
            mCall = mRetrofitBuilder
                .baseUrl(UriUtil.getBaseUrl(uri))
                .build()
                .create(IFileLoad.class)
                .loadFile(UriUtil.getPath(uri));
            startDownload();
        }catch (Exception e){
            Logger.e(e, "init download Failure");
        }
    }

    private void startDownload() {
        try {
            if(!mCall.isCanceled()){
                mCall.cancel();
            }
            mCall = mCall.clone();
            mCall.enqueue(mFileCallback);
        }catch (Exception e){
            Logger.e(e, "download Failure");
        }
    }

    private FileCallback mFileCallback = new FileCallback() {
        @Override
        public void onSuccess(File file) {
            Logger.d(time + "\ndownload apk success");
            if(mUpgradeProgressListener != null){
                mUpgradeProgressListener.handlerUpgradeSuccess(file);
            }
            mStatus = UpgradeStatus.SUCCESS;
            stopSelf();
        }

        @Override
        public void onLoading(long progress, long total) {
            int progressPercent = (int) (progress * 100 / total);
            if(mUpgradeProgressListener != null){
                mUpgradeProgressListener.handlerUpgradeProgress(progressPercent);
            }
            Logger.d(time + "\ndownload progress=" + progress +"----total=" + total + "--progressPercent=" + progressPercent + "--isListener" + mUpgradeProgressListener);
        }

        @Override
        public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
            super.onFailure(call, t);

            mStatus = UpgradeStatus.FAIL;
            if(mUpgradeProgressListener != null){
                mUpgradeProgressListener.handlerUpgradeFail();
            }
            Logger.d( time + "\ndownload Failure");
        }
    };

    private OkHttpClient initOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.retryOnConnectionFailure(false);
        builder.readTimeout(6, TimeUnit.HOURS);
        builder.writeTimeout(6, TimeUnit.HOURS);
        builder.connectTimeout(6, TimeUnit.HOURS);
        builder.networkInterceptors().add(chain -> {
            Response originalResponse = chain.proceed(chain.request());
            return originalResponse
                    .newBuilder()
                    .body(new FileResponseBody(originalResponse))
                    .build();
        });
        return builder.build();
    }

    public interface IFileLoad {
        @GET("{pathParam}")
        Call<ResponseBody> loadFile(@Path("pathParam") String pathParam);
    }

    private void checkAppVersion(boolean isShowTip) {
        mAppModel.checkAppVersion(new TAUObserver<DataResult<VersionBean>>() {
            @Override
            public void handleError(String msg, int msgCode) {
                super.handleError(msg, msgCode);
                ProgressManager.closeProgressDialog();
                Logger.d("UpgradeService.checkAppVersion.handleError=" + msg);
                stopSelf();
            }

            @Override
            public void handleData(DataResult<VersionBean> versionBean) {
                super.handleData(versionBean);
                Logger.d("UpgradeService.checkAppVersion.handleData=");
                if(versionBean != null && versionBean.getData() != null){
                    showUpgradeDialog(versionBean.getData(), isShowTip);
                }else{
                    stopSelf();
                }
            }
        });
    }

    private void showUpgradeDialog(VersionBean version, boolean isShowTip) {
        ProgressManager.closeProgressDialog();
        if(version == null){
            stopSelf();
            return;
        }
        boolean isNeedUpdate = version.getNumber() > AppUtil.getVersionCode(this);
        if(!isNeedUpdate){
            if(isShowTip){
                ToastUtils.showShortToast(R.string.app_upgrade_latest);
            }
            stopSelf();
            return;
        }
        if(!version.isForced()){
            version.setForced(version.getForcedNum() > 0);
        }

        SharedPreferencesHelper.getInstance().putBoolean(TransmitKey.UPGRADE, true);
        EventBusUtil.post(MessageEvent.EventCode.UPGRADE);
        String filePath = FileUtil.getDownloadFilePath();
        String fileName = getString(R.string.app_name);
        fileName += version.getNumber() + ".apk";
        version.setDownloadFilePath(filePath);
        version.setDownloadFileName(fileName);

        mStatus = UpgradeStatus.START;
        mVersionBean = version;
        Context context = ActivityManager.getInstance().currentActivity();
        Intent intent = new Intent(context, UpgradeActivity.class);
        intent.putExtra(TransmitKey.BEAN, mVersionBean);
        context.startActivity(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        if(mUpgradeServiceBinder == null){
            mUpgradeServiceBinder = new UpgradeService.UpgradeServiceBinder();
        }
        return new UpgradeService.UpgradeServiceBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mUpgradeServiceBinder = null;
        return super.onUnbind(intent);
    }

    /**
     * service and Activity communication
     */
    public class UpgradeServiceBinder extends Binder {

        public void setUpgradeProgressListener(DownloadManager.OnUpgradeProgressListener listener) {
            mUpgradeProgressListener = listener;
        }

        public void retryDownload() {
            startDownload();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mStatus = UpgradeStatus.DESTROY;
    }

    public static void startUpdateService(){
        Context context = MyApplication.getInstance();
        Intent intent = new Intent();
        intent.setClass(context, UpgradeService.class);
        context.startService(intent);
    }

    public static void startUpdateService(Intent intent){
        Context context = MyApplication.getInstance();
        intent.setClass(context, UpgradeService.class);
        context.startService(intent);
    }

    public static void stopUpdateService() {
        Context context = MyApplication.getInstance();
        Intent intent = new Intent();
        intent.setClass(context, UpgradeService.class);
        context.stopService(intent);
    }
}