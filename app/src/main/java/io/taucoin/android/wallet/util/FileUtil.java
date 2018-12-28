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
import android.os.Environment;

import com.mofei.tau.BuildConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import io.taucoin.android.wallet.MyApplication;

public class FileUtil {

    static Bitmap getExternalBitmap(String fileName){
        boolean isSdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        if (isSdCardExist) {
            String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            String filepath = sdPath + File.separator + BuildConfig.APPLICATION_ID + File.separator + fileName;
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
            String filepath = sdPath + File.separator + BuildConfig.APPLICATION_ID;
            File file = new File(filepath);
            deleteFile(file);
        }
    }

    private static void deleteFile(File file) {
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
}