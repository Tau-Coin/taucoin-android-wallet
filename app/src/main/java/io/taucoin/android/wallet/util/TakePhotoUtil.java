package io.taucoin.android.wallet.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;

import com.github.naturs.logger.Logger;
import com.mofei.tau.BuildConfig;
import com.mofei.tau.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import io.taucoin.android.wallet.MyApplication;
import io.taucoin.android.wallet.base.BaseFragment;
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
                .addSheetItem(R.string.profile_select_photo, which -> choseHeadImageFromGallery(context, fragment))
                .addSheetItem(R.string.profile_take_photo, which -> choseHeadImageFromCapture(context, fragment))
                .setCancel(R.string.common_cancel, which -> {})
                .show();
    }

    private static void choseHeadImageFromCapture(FragmentActivity context, BaseFragment fragment) {
        if (!EasyPermissions.hasPermissions(context, android.Manifest.permission.CAMERA,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            EasyPermissions.requestPermissions(context, "Taucoin needs access to your camera and storage rights",
                    PermisionUtils.REQUEST_PERMISSIONS_CAMERA, android.Manifest.permission.CAMERA,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE);
            return;
        }

        Logger.i("TakePhoneUtil.choseHeadImageFromCapture");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(fragment == null){
            context.startActivityForResult(intent, CODE_CAMERA_REQUEST);
        }else{
            fragment.startActivityForResult(intent, CODE_CAMERA_REQUEST);
        }
    }

    private static void choseHeadImageFromGallery(FragmentActivity context, BaseFragment fragment) {
        if (!EasyPermissions.hasPermissions(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            EasyPermissions.requestPermissions(context, "Taucoin needs access to storage rights",
                    PermisionUtils.REQUEST_PERMISSIONS_RECORD_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
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
        File file = new File(filepath);
        Uri uri = getUriForFile(file);
        tempUri = uri;
        startPhotoZoom(context, uri);
    }
    /**
     * Cut pictures
     */
    private static void startPhotoZoom(FragmentActivity context, Uri uri) {

        try {
            if (uri == null) {
                Logger.i("TakePhoneUtil.startPhotoZoom, The uri is not exist.");
            }
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(uri, "image/*");
            if (Build.VERSION.SDK_INT >= 24) {
                //Granting Temporary Permissions to a URI
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            // set cut
            intent.putExtra("crop", "true");
            // aspectX aspectY
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            // outputX outputY
            intent.putExtra("outputX", 250);
            intent.putExtra("outputY", 250);
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

    private static Uri getUriForFile(File file) {
        Context context = MyApplication.getInstance();
        if (context == null || file == null) {
            throw new NullPointerException();
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= 24) {
            String authority = BuildConfig.APPLICATION_ID + ".fileprovider";
            uri = FileProvider.getUriForFile(context.getApplicationContext(), authority, file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    private static String getExternalPath(){
        return "file://" + File.separator + Environment.getExternalStorageDirectory().getPath() +
                File.separator + BuildConfig.APPLICATION_ID + File.separator;
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
                if (Activity.RESULT_OK == resultCode && intent != null) {
                    if (TakePhotoUtil.isHasSDCard()) {
                        Bundle bundle = intent.getExtras();
                        Bitmap bm = (Bitmap) bundle.get("data");
                        String filepath = TakePhotoUtil.saveImage(bm);
                        TakePhotoUtil.startPhotoZoom(context, filepath);
                    } else {
                        ToastUtils.showShortToast("No sdcard");
                    }
                }
                break;
            case TakePhotoUtil.CODE_RESULT_REQUEST:
                break;

        }
    }
}