package io.taucoin.foundation.util;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Status Bar Adaptation
 */

public class FitStateUI {
    /**
     * Status bar color font(Black characters on white background) modify MiUi6+
     */
    @SuppressWarnings("all")
    private static void setMiUiStatusBarDarkMode(Activity activity, boolean darkMode) {
        Class clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag ;
            Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method[] methods = clazz.getMethods();
            Method mMethod = null;
            for (Method method : methods) {
                if(method.getName().equals("setExtraFlags")){
                    mMethod = method;
                    break;
                }
            }
            if(mMethod != null){
                mMethod.invoke(activity.getWindow(), darkMode ? darkModeFlag : 0, darkModeFlag);
            }
        } catch (Exception ignored) {
        }
    }

    /**
     * Status bar color font(Black characters on white background) modify FlyMe4+
     */
    @SuppressWarnings("all")
    public static void setMeiZuStatusBarDarkIcon(Activity activity, boolean darkMode) {
        if (activity != null) {
            try {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meiZuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meiZuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meiZuFlags.getInt(lp);
                if (darkMode) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meiZuFlags.setInt(lp, value);
                activity.getWindow().setAttributes(lp);
            } catch (Exception ignored) {
            }
        }
    }
    /**
     * Status bar color font(Black characters on white background)
     */
    public static void setStatusBarDarkIcon(Activity activity, @SuppressWarnings("SameParameterValue") boolean lightStatusBar) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            Window window = activity.getWindow();
            ViewGroup decorViewGroup = (ViewGroup) window.getDecorView();
            int ui = decorViewGroup.getSystemUiVisibility();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (lightStatusBar) {
                    ui |=View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                } else {
                    ui &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                }
            }
            decorViewGroup.setSystemUiVisibility(ui);
        }
        // 兼容小米4.4.X手机状态栏
        setMiUiStatusBarDarkMode(activity, lightStatusBar);
    }
}
