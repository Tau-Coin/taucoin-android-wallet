package io.taucoin.foundation.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.ArrayRes;
import android.support.v4.content.ContextCompat;
import android.widget.FrameLayout;
import android.widget.TextView;

public class DrawablesUtil {

    public static void setStartDrawable(TextView view, int drawable, float with, float height) {
        Drawable mDrawable = getDrawable(view, drawable, with, height);
        view.setCompoundDrawables(mDrawable, null, null, null);
    }
    public static void setStartDrawable(TextView view, int drawable, float size) {
        Drawable mDrawable = getDrawable(view, drawable, size, size);
        view.setCompoundDrawables(mDrawable, null, null, null);
    }

    public static void setTopDrawable(TextView view, int drawable, float size) {
        Drawable mDrawable = getDrawable(view, drawable, size, size);
        view.setCompoundDrawables(null, mDrawable, null, null);
    }

    public static void setTopDrawable(TextView view, int drawable, float with, float height) {
        Drawable mDrawable = getDrawable(view, drawable, with, height);
        view.setCompoundDrawables(null, mDrawable, null, null);
    }

    public static void setEndDrawable(TextView view, int drawable, float size) {
        Drawable mDrawable = getDrawable(view, drawable, size, size);
        view.setCompoundDrawables(null, null, mDrawable, null);
    }
    public static void setEndDrawable(TextView view, int drawable, float with, float height) {
        Drawable mDrawable = getDrawable(view, drawable, with, height);
        view.setCompoundDrawables(null, null, mDrawable, null);
    }

    public static void setBottomDrawable(TextView view, int drawable, float size) {
        Drawable mDrawable = getDrawable(view, drawable, size, size);
        view.setCompoundDrawables(null, null, null, mDrawable);
    }
    public static void setBottomDrawable(TextView view, int drawable, float with, float height) {
        Drawable mDrawable = getDrawable(view, drawable, with, height);
        view.setCompoundDrawables(null, null, null, mDrawable);
    }
    public static void clearDrawable(TextView view) {
        view.setCompoundDrawables(null, null, null, null);
    }

    private static Drawable getDrawable(TextView view, int drawable, float width, float height) {
        Drawable mDrawable = ContextCompat.getDrawable(view.getContext(), drawable);
        width = DimensionsUtil.dp2px(view.getContext(), width);
        height = DimensionsUtil.dp2px(view.getContext(), height);
        int top = (int) width;
        int bottom = (int) height;
        mDrawable.setBounds(0, 0, top, bottom);
        return mDrawable;
    }
    private static Drawable getDrawable(Context context, int drawable, float width, float height) {
        Drawable mDrawable = ContextCompat.getDrawable(context, drawable);
        width = DimensionsUtil.dp2px(context, width);
        height = DimensionsUtil.dp2px(context, height);
        int top = (int) width;
        int bottom = (int) height;
        mDrawable.setBounds(0, 0, top, bottom);
        return mDrawable;
    }
    public static void setUnderLine(TextView view) {
        view.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        view.getPaint().setAntiAlias(true);//抗锯齿
    }
    public static int[] obtainTypedArray(Context context, @ArrayRes int arrayId) {
        TypedArray ar = context.getResources().obtainTypedArray(arrayId);
        int len = ar.length();
        int[] array = new int[len];
        for (int i = 0; i < len; i++){
            array[i] = ar.getResourceId(i, 0);
        }
        ar.recycle();
        return array;
    }
    public static void setForeground(FrameLayout view, int drawable, float size) {
        Drawable mDrawable = getDrawable(view.getContext(), drawable, size, size);
        view.setForeground(mDrawable);
    }
}
