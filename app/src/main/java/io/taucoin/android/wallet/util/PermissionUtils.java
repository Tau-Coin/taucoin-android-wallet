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
package io.taucoin.android.wallet.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;

import io.taucoin.foundation.util.StringUtil;

public class PermissionUtils {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    public static final int REQUEST_PERMISSIONS_CAMERA = 2;
    public static final int REQUEST_PERMISSIONS_RECORD_STORAGE = 3;
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
            ToastUtils.showLongToast("Taucoin requires you to turn on camera privileges");
            return false;
        }
    }


    @SuppressWarnings("deprecation")
    private static boolean checkPhotoPermission() {

        boolean isCanUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
            Camera.Parameters mParameters = mCamera.getParameters();
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


    /*Check if the user has completely prohibited pop-up permission requests*/
    public static void checkUserBanPermission(final FragmentActivity activity, final String permission) {
        if (StringUtil.isEmpty(permission)) {
            return;
        }
        boolean isTip = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
        String message = "";
        switch (permission) {
            case Manifest.permission.CAMERA:
                message = "Taucoin requires you to turn on camera privileges";
                break;
        }
        if (!isTip) {//Users completely prohibit pop-up permission requests
            ToastUtils.showLongToast(message);
        }
    }

    /*Check if the user has completely prohibited pop-up permission requests*/
    public void checkUserBanPermission(final FragmentActivity activity, final String permission, String message) {
        if (StringUtil.isEmpty(permission)) {
            return;
        }
        boolean isTip = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
        if (!isTip) {//Users completely prohibit pop-up permission requests
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ToastUtils.showLongToast(message);
            }
        }
    }
}
