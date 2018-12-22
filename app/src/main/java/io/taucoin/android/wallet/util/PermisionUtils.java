package io.taucoin.android.wallet.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;

import com.google.zxing.common.StringUtils;

import io.taucoin.foundation.util.StringUtil;

public class PermisionUtils {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    public static final int REQUEST_PERMISSIONS_CAMERA = 2; // 访问相机权限
    public static final int REQUEST_PERMISSIONS_RECORD_AUDIO = 3; // 访问录音权限
    public static final int REQUEST_PERMISSIONS_RECORD_STORAGE = 4; // 访问存储权限
    public static final int REQUEST_PERMISSIONS_CALL_PHONE = 5; // 访问电话权限
    public static final int REQUEST_PERMISSIONS_LOCATION = 6; // 访问定位权限
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static void verifyStoragePermissions(Activity activity) {

        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }

    public static boolean checkCameraPermission(Context context) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            return true;
        }
        if (checkPhotoPermission()) {
            return true;
        } else {
            ToastUtils.showLongToast("“记忆大师”需要您开启相机权限");
            return false;
        }
    }


    @SuppressWarnings("deprecation")
    private static boolean checkPhotoPermission() {

        boolean isCanUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
            Camera.Parameters mParameters = mCamera.getParameters(); //针对魅族手机
            mCamera.setParameters(mParameters);
        } catch (Exception e) {
            isCanUse = false;
        }

        if (mCamera != null) {
            try {
                mCamera.release();
            } catch (Exception e) {
                e.printStackTrace();
                return isCanUse;
            }
        }
        return isCanUse;
    }


    /*校验用户有没有彻底禁止弹出权限请求*/
    public static void checkUserBanPermission(final FragmentActivity activity, final String permission) {
        if (StringUtil.isEmpty(permission)) {
            return;
        }
        boolean isTip = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
        String message = "";
        switch (permission) {
            case Manifest.permission.CAMERA:
                message = "“记忆大师”需要您开启相机权限";
                break;
        }
        if (!isTip) {//用户彻底禁止弹出权限请求
            ToastUtils.showLongToast(message);
        }
    }

    /*校验用户有没有彻底禁止弹出权限请求*/
    public void checkUserBanPermission(final FragmentActivity activity, final String permission, String message) {
        if (StringUtil.isEmpty(permission)) {
            return;
        }
        boolean isTip = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
        if (!isTip) {//用户彻底禁止弹出权限请求
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ToastUtils.showLongToast(message);
            }
        }
    }
}
