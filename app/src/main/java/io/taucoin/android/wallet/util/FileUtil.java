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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import com.mofei.tau.BuildConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import io.taucoin.android.wallet.MyApplication;
import io.taucoin.foundation.util.StringUtil;

public class FileUtil {

    public static final String authority = BuildConfig.APPLICATION_ID + ".fileprovider";

    static Bitmap getExternalBitmap(String fileName){
        boolean isSdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        if (isSdCardExist) {
            String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            String filepath = sdPath + File.separator + BuildConfig.APPLICATION_ID + File.separator + "temp" + File.separator + fileName;
            File file = new File(filepath);
            if (file.exists()) {
                return BitmapFactory.decodeFile(filepath);
            }
        }
        return null;
    }

    public static void deleteExternalBitmap(){
        boolean isSdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        if (isSdCardExist) {
            String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            String filepath = sdPath + File.separator + BuildConfig.APPLICATION_ID + File.separator + "temp";
            File file = new File(filepath);
            deleteFile(file);
        }
    }

    /**
     * Delete File
     * @param file target file
     */
    public static void deleteFile(File file) {
        if (file == null || !file.exists())
            return;

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                deleteFile(f);
            }
//            file.delete();
        } else if (file.exists()) {
            file.delete();
        }
    }

    static Bitmap getFilesDirBitmap(String filename){
        Bitmap bitmap = null;
        try {
            Context context = MyApplication.getInstance();
            FileInputStream fis = context.openFileInput(filename);
            bitmap = BitmapFactory.decodeStream(fis);
            fis.close();
        }  catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static void saveFilesDirBitmap(String filename, Bitmap bitmap){
        try {
            Context context = MyApplication.getInstance();
            FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,fos);
            fos.close();
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Get download directory
     * @return  path
     */
    public static String getDownloadFilePath() {
        String path;
        path = Environment.getExternalStorageDirectory() + File.separator + BuildConfig.APPLICATION_ID;
        path = path + File.separator + "download";
        createDir(path);

        return path + File.separator;
    }

    /**
     * Create Directory
     * @param file target file
     * @return  Directory
     */
    private static File createDir(File file){
        if (file != null && (file.exists() || file.mkdirs())){
            return file;
        }
        return null;
    }

    /**
     * Create Directory
     * @param path target file path
     * @return  Directory
     */
    private static File createDir(String path){
        if (StringUtil.isEmpty(path)){
            return null;
        }
        return createDir(new File(path));
    }

    public static Uri getUriForFile(File file) {
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
}