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
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;

import com.github.naturs.logger.Logger;
import com.mofei.tau.BuildConfig;
import com.mofei.tau.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.taucoin.android.wallet.MyApplication;
import io.taucoin.android.wallet.base.BaseFragment;
import io.taucoin.android.wallet.module.view.manage.UpgradeActivity;
import io.taucoin.android.wallet.widget.ActionSheetDialog;
import io.taucoin.foundation.util.StringUtil;
import io.taucoin.foundation.util.permission.EasyPermissions;

/**
 * Take Phone
 * @author yang
 */

public class TakePhotoUtil {
    /* Request Identification Code */
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    public static final int CODE_RESULT_REQUEST = 0xa2;
    private static Uri tempUri;
    private static String tempName = "image.png";
    private static String imageName;
    private static String imagePath;

    public static void takePhotoForName(FragmentActivity context, String name){
        TakePhotoUtil.imageName = name;
        takePhoto(context, null);
    }

    public static void takePhoto(FragmentActivity context){
        TakePhotoUtil.imageName = "image";
        takePhoto(context, null);
    }

    public static void takePhoto(BaseFragment fragment){
        TakePhotoUtil.imageName = "image";
        takePhoto(null, fragment);
    }

    public static void setImageName(String imageName){
        TakePhotoUtil.imageName = imageName;
    }

    private static void takePhoto(FragmentActivity context, BaseFragment fragment){

        new ActionSheetDialog(context)
                .builder()
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .addSheetItem(R.string.profile_take_photo, which -> choseHeadImageFromCapture(context, fragment))
                .addSheetItem(R.string.profile_select_photo, which -> choseHeadImageFromGallery(context, fragment))
                .setCancel(R.string.common_cancel, which -> {})
                .show();
    }

    private static void choseHeadImageFromCapture(FragmentActivity context, BaseFragment fragment) {
        if (!EasyPermissions.hasPermissions(context, android.Manifest.permission.CAMERA,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            EasyPermissions.requestPermissions(context, context.getString(R.string.permission_tip_capture_denied),
                    PermissionUtils.REQUEST_PERMISSIONS_CAMERA, android.Manifest.permission.CAMERA,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE);
            return;
        }

        Logger.i("TakePhoneUtil.choseHeadImageFromCapture");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = getSaveFile();
        if(file == null){
            return;
        }
        Uri uri = FileUtil.getUriForFile(file);
        imagePath = uri.toString();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        if(fragment == null){
            context.startActivityForResult(intent, CODE_CAMERA_REQUEST);
        }else{
            fragment.startActivityForResult(intent, CODE_CAMERA_REQUEST);
        }
    }

    private static void choseHeadImageFromGallery(FragmentActivity context, BaseFragment fragment) {
        if (!EasyPermissions.hasPermissions(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            EasyPermissions.requestPermissions(context, context.getString(R.string.permission_tip_gallery_denied),
                    PermissionUtils.REQUEST_PERMISSIONS_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE);
            return;
        }
        Logger.i("TakePhoneUtil.choseHeadImageFromGallery");
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        if(fragment == null){
            context.startActivityForResult(intent, CODE_GALLERY_REQUEST);
        }else{
            fragment.startActivityForResult(intent, CODE_GALLERY_REQUEST);
        }
    }
    private static void startPhotoZoom(FragmentActivity context, String filepath) {
        Uri uri = Uri.parse(filepath);
        tempUri = uri;
        startPhotoZoom(context, uri, true);
    }

    private static void startPhotoZoom(FragmentActivity context, Uri uri) {
        startPhotoZoom(context, uri, false);
    }
    /**
     * Cut pictures
     */
    private static void startPhotoZoom(FragmentActivity context, Uri uri, boolean isNeedPermissions) {

        try {
            if (uri == null) {
                Logger.i("TakePhoneUtil.startPhotoZoom, The uri is not exist.");
            }
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(uri, "image/*");
            if (Build.VERSION.SDK_INT >= 24 && isNeedPermissions) {
                //Granting Temporary Permissions to a URI
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            // set cut
            intent.putExtra("crop", "true");
            // aspectX aspectY
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            // outputX outputY
            int size = 300;
            intent.putExtra("outputX", size);
            intent.putExtra("outputY", size);
//          intent.putExtra("return-data", true);

            tempUri = Uri.parse(getAllPath());
            Logger.d("TakePhoneUtil.startPhotoZoom.tempUri: " + tempUri.getPath());
            intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            context.startActivityForResult(intent, CODE_RESULT_REQUEST);
        }catch (Exception e){
            Logger.e(e, "TakePhoneUtil.startPhotoZoom, error");
        }
    }

    private static String getExternalPath(){
        return "file://" + File.separator + Environment.getExternalStorageDirectory().getPath() +
                File.separator + BuildConfig.APPLICATION_ID + File.separator + "temp"  + File.separator;
    }

    private static String getAllPath(){
        return getExternalPath() + getFileName();
    }

    public static String getFileName(){
        return imageName.toLowerCase() + ".jpg";
    }

    public static Bitmap getPhotoZoom(){
        Bitmap bitmap = null;
        try {
            Context context = MyApplication.getInstance();
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(tempUri));
            tempUri = null;
        } catch (Exception e) {
            Logger.e(e, "TakePhoneUtil.getPhotoZoom");
        }
        return bitmap;
    }

    private static boolean isHasSDCard() {
        String state= Environment.getExternalStorageState();
        return StringUtil.isSame(state, Environment.MEDIA_MOUNTED);
    }

    /**
     * save image
     */
    private static String saveImage(Bitmap photoBitmap) {
        String path = getSavePath();
        String localPath = null;
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File photoFile = new File(path,  tempName);
            Logger.d("TakePhoneUtil.photoFile.getAbsolutePath(): " + photoFile.getAbsolutePath());
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(photoFile);
                if (photoBitmap != null) {
                    if (photoBitmap.compress(Bitmap.CompressFormat.PNG, 100,  fileOutputStream)) {
                        localPath = photoFile.getPath();
                        fileOutputStream.flush();
                    }
                }
            }catch (Exception e) {
                photoFile.delete();
                localPath = null;
                e.printStackTrace();
                Logger.e(e, "TakePhoneUtil.saveImage");
            } finally {
                try {
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                } catch (IOException e) {
                    Logger.e(e, "TakePhoneUtil.saveImage.finally");
                    e.printStackTrace();
                }
            }
        }
        return localPath;
    }

    private static String getSavePath() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        path += File.separator + BuildConfig.APPLICATION_ID + File.separator + "temp";
        return path;
    }

    private static File getSaveFile() {
        String path = getSavePath();
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            return new File(path,  tempName);
        }else{
            ToastUtils.showShortToast("No sdcard");
        }
        return null;
    }

    public static void onActivityResult(FragmentActivity context, int requestCode, int resultCode, Intent intent) {
        if (requestCode == Activity.RESULT_CANCELED) {
            ToastUtils.showShortToast("Canceled");
            return;
        }
        switch (requestCode) {
            case TakePhotoUtil.CODE_GALLERY_REQUEST:
                if (intent != null)
                    TakePhotoUtil.startPhotoZoom(context, intent.getData());
                break;
            case TakePhotoUtil.CODE_CAMERA_REQUEST:
                if (Activity.RESULT_OK == resultCode) {
                    TakePhotoUtil.startPhotoZoom(context, imagePath);
                }
                break;

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void onRequestPermissionsResult(FragmentActivity activity, int requestCode, String[] permissions, int[] grantResults) {
        onRequestPermissionsResult(activity, null, requestCode, permissions, grantResults);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void onRequestPermissionsResult(BaseFragment fragment, int requestCode, String[] permissions, int[] grantResults) {
        onRequestPermissionsResult(null, fragment, requestCode, permissions, grantResults);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private static void onRequestPermissionsResult(FragmentActivity activity, BaseFragment fragment, int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != PermissionUtils.REQUEST_PERMISSIONS_CAMERA &&
                requestCode != PermissionUtils.REQUEST_PERMISSIONS_STORAGE) {
            return;
        }
        boolean isCamera = requestCode == PermissionUtils.REQUEST_PERMISSIONS_CAMERA;
        int message = isCamera ? R.string.permission_tip_capture_never_ask_again : R.string.permission_tip_gallery_never_ask_again;
        if (grantResults.length > 0) {
            List<String> deniedPermissionList = new ArrayList<>();
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    deniedPermissionList.add(permissions[i]);
                }
            }

            if (deniedPermissionList.isEmpty()) {
                permissionAllGranted(activity, fragment, requestCode);
            } else {
                for (String deniedPermission : deniedPermissionList) {
                    boolean flag = activity.shouldShowRequestPermissionRationale(deniedPermission);
                    if (!flag) {
                        PermissionUtils.checkUserBanPermission(activity, deniedPermissionList, message);
                        return;
                    }
                }
                PermissionUtils.checkUserBanPermission(activity, deniedPermissionList, message);
            }
        }
    }

    private static void permissionAllGranted(FragmentActivity activity, BaseFragment fragment, int requestCode) {

        switch (requestCode){
            case PermissionUtils.REQUEST_PERMISSIONS_CAMERA:
                choseHeadImageFromCapture(activity, fragment);
                break;
            case PermissionUtils.REQUEST_PERMISSIONS_STORAGE:
                choseHeadImageFromGallery(activity, fragment);
                break;
            default:
                break;
        }
    }
}