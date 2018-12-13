package io.taucoin.android.wallet.util;

import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;

import com.mofei.tau.R;

import io.taucoin.android.wallet.MyApplication;
import io.taucoin.foundation.util.DimensionsUtil;

/**
 * 统一的Toast提示
 */
public class ToastUtils {

    public static void showLongToast(int resId) {
        showToast(resId, Toast.LENGTH_LONG);
    }

    public static void showShortToast(int resId) {
        showToast(resId, Toast.LENGTH_SHORT);
    }

    public static void showLongToast(CharSequence text) {
        showToast(text, Toast.LENGTH_LONG);
    }

    public static void showShortToast(CharSequence text) {
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
            sHandler.removeCallbacks(sCancleRunnable);
            showRunnable.message = text;
            showRunnable.duration = duration;
            sHandler.post(showRunnable);
        }
    }

    public static void cancleToast() {
        synchronized (sLock) {
            sHandler.removeCallbacks(showRunnable);
            sHandler.removeCallbacks(sCancleRunnable);
            sHandler.post(sCancleRunnable);
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
            TextView tv = (TextView) LayoutInflater.from(MyApplication.getInstance()).inflate(R.layout.toast_layout, null);
            tv.setText(message);
            sToast.setView(tv);
            sToast.setGravity(Gravity.BOTTOM, 0, DimensionsUtil.dip2px(MyApplication.getInstance(), 80));
            sToast.setDuration(duration);
            sToast.show();
        }
    }

    private static void cancel() {
        if (sToast != null) {
            sToast.cancel();
            sToast = null;
        }
    }

    private static Runnable sCancleRunnable = new Runnable() {

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
