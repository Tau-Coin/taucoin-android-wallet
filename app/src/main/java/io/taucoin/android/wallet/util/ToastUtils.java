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

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;

import com.mofei.tau.R;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import io.taucoin.android.wallet.MyApplication;
import io.taucoin.foundation.util.DimensionsUtil;

/**
 * Unified Toast Tips
 */
public class ToastUtils {

    private static Object iNotificationManagerObj;
    private static int mGravity = Gravity.BOTTOM;
    private static int mMsgGravity = Gravity.CENTER;

    public static void showLongToast(int resId) {
        mGravity = Gravity.BOTTOM;
        mMsgGravity = Gravity.CENTER;
        showToast(resId, Toast.LENGTH_LONG);
    }

    public static void showLongToast(int resId, int gravity, int msgGravity) {
        mGravity = gravity;
        mMsgGravity = msgGravity;
        showToast(resId, Toast.LENGTH_LONG);
    }

    public static void showShortToast(int resId, int gravity, int msgGravity) {
        mGravity = gravity;
        mMsgGravity = msgGravity;
        showToast(resId, Toast.LENGTH_SHORT);
    }

    public static void showShortToast(int resId) {
        mGravity = Gravity.BOTTOM;
        mMsgGravity = Gravity.CENTER;
        showToast(resId, Toast.LENGTH_SHORT);
    }

    public static void showLongToast(CharSequence text) {
        mGravity = Gravity.BOTTOM;
        mMsgGravity = Gravity.CENTER;
        showToast(text, Toast.LENGTH_LONG);
    }

    public static void showShortToast(CharSequence text) {
        mGravity = Gravity.BOTTOM;
        mMsgGravity = Gravity.CENTER;
        showToast(text, Toast.LENGTH_SHORT);
    }

    private static void showToast(int resId, int duration) {
        String text = MyApplication.getInstance().getResources().getString(resId);
        showToast(text, duration);
    }

    private static final Handler sHandler = new Handler(Looper.getMainLooper());

    private static void showToast(CharSequence text, int duration) {
        synchronized (sLock) {
            sHandler.removeCallbacks(showRunnable);
            sHandler.removeCallbacks(sCancelRunnable);
            showRunnable.message = text;
            showRunnable.duration = duration;
            sHandler.post(showRunnable);
        }
    }

    public static void cancleToast() {
        synchronized (sLock) {
            sHandler.removeCallbacks(showRunnable);
            sHandler.removeCallbacks(sCancelRunnable);
            sHandler.post(sCancelRunnable);
        }
    }

    private static Toast sToast = null;

    private static class ShowRunnable implements Runnable {
        private CharSequence message;
        private int duration;

        @Override
        public void run() {
            synchronized (sLock) {
                cancel();
                show();
            }
        }

        private void show() {
            sToast = new Toast(MyApplication.getInstance());

            sToast = Toast.makeText(MyApplication.getInstance(), null, duration);

            TextView tv = (TextView) LayoutInflater.from(MyApplication.getInstance()).inflate(R.layout.toast_layout, null);
            tv.setText(message);
            tv.setGravity(mMsgGravity);
            sToast.setView(tv);
            if(mGravity == Gravity.BOTTOM){
                sToast.setGravity(mGravity, 0, DimensionsUtil.dip2px(MyApplication.getInstance(), 80));
            }else{
                sToast.setGravity(mGravity, 0, 0);
            }
            sToast.setDuration(duration);
            if(PermissionUtils.isNotificationEnabled()){
                sToast.show();
            }else {
                showSystemToast();
            }
        }
    }

    private static void showSystemToast(){
        try{
            @SuppressLint("PrivateApi")
            Method getServiceMethod = Toast.class.getDeclaredMethod("getService");
            getServiceMethod.setAccessible(true);
            // hook INotificationManager
            if(iNotificationManagerObj == null){
                iNotificationManagerObj = getServiceMethod.invoke(null);

                @SuppressLint("PrivateApi")
                Class iNotificationManagerCls = Class.forName("android.app.INotificationManager");
                Object iNotificationManagerProxy = Proxy.newProxyInstance(sToast.getClass().getClassLoader(), new Class[]{iNotificationManagerCls}, new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        // Mandatory use of system Toast
                        // hua wei p20 pro enqueueToastEx
                        if("enqueueToast".equals(method.getName())
                                || "enqueueToastEx".equals(method.getName())){
                            args[0] = "android";
                        }
                        return method.invoke(iNotificationManagerObj, args);
                    }
                });
                Field sServiceFiled = Toast.class.getDeclaredField("sService");
                sServiceFiled.setAccessible(true);
                sServiceFiled.set(null, iNotificationManagerProxy);
            }
            sToast.show();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void cancel() {
        if (sToast != null) {
            sToast.cancel();
            sToast = null;
        }
    }

    private static Runnable sCancelRunnable = new Runnable() {

        @Override
        public void run() {
            synchronized (sLock) {
                cancel();
            }
        }
    };
    private static ShowRunnable showRunnable = new ShowRunnable();
    private static final Object sLock = new Object();
}