package io.taucoin.android.wallet.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import io.taucoin.android.wallet.MyApplication;

public class ResourcesUtil {

    public static String getText(int resId){
        Context context = MyApplication.getInstance();
        return context.getText(resId).toString();
    }

    public static int getColor(int resId){
        Context context = MyApplication.getInstance();
        return ContextCompat.getColor(context, resId);
    }

    public static Drawable getDrawable(int resId){
        Context context = MyApplication.getInstance();
        return ContextCompat.getDrawable(context, resId);
    }
}
