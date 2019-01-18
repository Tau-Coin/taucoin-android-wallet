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
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.github.naturs.logger.Logger;

import java.lang.reflect.Method;

public class NotchUtil {

    /**
     * handle Notch Display
     * */
    public static void handleNotchDisplay(FragmentActivity activity){
        handleGoogleNotchDisplay(activity);
    }

    /**
     * get status bar or notch height(get max value)
     * */
    public static int getStatusBarOrNotchHeight(Context context) {
        int statusBarHeight = 0;
        try {
            Resources resources = context.getResources();
            int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
            Logger.d("statusBarHeight.resourceId=" + resourceId);
            if (resourceId > 0) {
                statusBarHeight = resources.getDimensionPixelSize(resourceId);
                Logger.d("statusBarHeight=" + statusBarHeight);
            }
            // hua wei
            if(hasNotchOnHW(context)){
                int height = getNotchHeightOnHW(context);
                if(height > statusBarHeight){
                    statusBarHeight = height;
                }
            }
        }catch (Exception ignore){

        }
        return statusBarHeight;
    }

    /**
     * Google Notch Compatibility
     * */
    private static void handleGoogleNotchDisplay(FragmentActivity activity){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                Window window = activity.getWindow();
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
                window.setAttributes(lp);
            }
        }catch (Exception ignore){

        }
    }

    /**
     * Have Notch huawei phone
     * */
    @SuppressWarnings("unchecked")
    private static boolean hasNotchOnHW(Context context) {
        boolean ret = false;
        try {
            ClassLoader cl = context.getClassLoader();
            Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            if(HwNotchSizeUtil != null){
                Method get = HwNotchSizeUtil.getMethod("hasNotchInScreen");
                ret = (boolean) get.invoke(HwNotchSizeUtil);
            }
        } catch (ClassNotFoundException e) {
            Logger.e("test", "hasNotchInScreen ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            Logger.e("test", "hasNotchInScreen NoSuchMethodException");
        } catch (Exception e) {
            Logger.e("test", "hasNotchInScreen Exception");
        }
        return ret;
    }

    @SuppressWarnings("unchecked")
    private static int getNotchHeightOnHW(Context context) {
        int[] ret = new int[]{0, 0};
        try {
            ClassLoader cl = context.getClassLoader();
            Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("getNotchSize");
            ret = (int[]) get.invoke(HwNotchSizeUtil);
        } catch (ClassNotFoundException e) {
            Logger.e("test", "getNotchSize ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            Logger.e("test", "getNotchSize NoSuchMethodException");
        } catch (Exception e) {
            Logger.e("test", "getNotchSize Exception");
        }
        return ret[1];
    }

    /**
     * Status bar immersion all phone
     * */
    public static void fullScreenAll(AppCompatActivity activity) {
        Window window = activity.getWindow();
        View decorView = window.getDecorView();
        int systemUiVisibility = decorView.getSystemUiVisibility();
        int flags = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        systemUiVisibility |= flags;

        window.getDecorView().setSystemUiVisibility(systemUiVisibility);
        fullScreen(activity);
    }

    /**
     * Status bar immersion
     * */
    private static void fullScreen(AppCompatActivity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                View decorView = window.getDecorView();
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
//                window.setNavigationBarColor(Color.TRANSPARENT);
            } else {
                Window window = activity.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
//                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                attributes.flags |= flagTranslucentStatus;
//                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
            }
        }
    }
}