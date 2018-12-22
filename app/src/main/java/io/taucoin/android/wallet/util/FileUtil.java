package io.taucoin.android.wallet.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;

class FileUtil {

    static Bitmap getBitmap(String filepath){
        boolean isSdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        if (isSdCardExist) {
            String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            filepath = sdPath + File.separator +filepath;
            File file = new File(filepath);
            if (file.exists()) {
                return BitmapFactory.decodeFile(filepath);
            }
        }
        return null;
    }
}