package com.mofei.tau.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;


/**
 * Log统一管理类
 *
 * @author ly
 */
public class LogUtils {
    private static String L_TAG = "L_Tag";
    private static Toast toast;

    public static boolean isDebuggable() {
        return BuildConfig.DEBUG;
    }
    //tag为文件名
    public static void i(Object info) {
        if (!isDebuggable()) return;
        String msg = String.valueOf(info);
        StackTraceElement sElement = getStackTrace();
        Log.i(sElement.getFileName(), createLog(sElement, msg));
    }

    public static void i(String format, Object... args) {
        if (!isDebuggable()) return;
        String msg = String.format(format, args);
        StackTraceElement sElement = getStackTrace();
        Log.i(sElement.getFileName(), createLog(sElement, msg));
    }


    public static void d(Object info) {
        if (!isDebuggable()) return;
        String msg = String.valueOf(info);
        StackTraceElement sElement = getStackTrace();
        Log.d(sElement.getFileName(), createLog(sElement, msg));
    }

    public static void d(String format, Object... args) {
        if (!isDebuggable()) return;
        String msg = String.format(format, args);
        StackTraceElement sElement = getStackTrace();
        Log.d(sElement.getFileName(), createLog(sElement, msg));
    }


    public static void e(Object info) {
        if (!isDebuggable()) return;
        String msg = String.valueOf(info);
        StackTraceElement sElement = getStackTrace();
        Log.e(sElement.getFileName(), createLog(sElement, msg));
    }

    public static void e(String format, Object... args) {
        if (!isDebuggable()) return;
        String msg = String.format(format, args);
        StackTraceElement sElement = getStackTrace();
        Log.e(sElement.getFileName(), createLog(sElement, msg));
    }


    public static void w(Object info) {
        if (!isDebuggable()) return;
        String msg = String.valueOf(info);
        StackTraceElement sElement = getStackTrace();
        Log.w(sElement.getFileName(), createLog(sElement, msg));

    }

    public static void w(String format, Object... args) {
        if (!isDebuggable()) return;
        String msg = String.format(format, args);
        StackTraceElement sElement = getStackTrace();
        Log.w(sElement.getFileName(), createLog(sElement, msg));

    }

    public static void e(int hierarchy,String msg) {
        if (!isDebuggable()) return;
        StackTraceElement sElement = getStackTrace(hierarchy);
        Log.e(sElement.getFileName(), createLog(sElement, msg));
    }

    private static void e(int hierarchy, String tag, String msg) {
        if (!isDebuggable()) return;
        if (tag == null) {
            tag = L_TAG;
        }
        StackTraceElement sElement = getStackTrace(hierarchy);
        Log.e(tag, createLog(sElement, msg));
    }


    public static void v(Object info) {
        if (!isDebuggable()) return;
        String msg = String.valueOf(info);
        StackTraceElement sElement = getStackTrace();
        Log.v(sElement.getFileName(), createLog(sElement, msg));
    }

    public static void v(String format, Object... args) {
        if (!isDebuggable()) return;
        String msg = String.format(format, args);
        StackTraceElement sElement = getStackTrace();
        Log.v(sElement.getFileName(), createLog(sElement, msg));
    }

    //tag为全类名
    public static void iTagClassName(String msg) {
        if (!isDebuggable()) return;
        StackTraceElement sElement = getStackTrace();
        Log.i(sElement.getClassName(), createLog(sElement, msg));
    }

    public static void dTagClassName(String msg) {
        if (!isDebuggable()) return;
        StackTraceElement sElement = getStackTrace();
        Log.d(sElement.getClassName(), createLog(sElement, msg));
    }

    public static void dTagClassName1(String format, Object args) {
        if (!isDebuggable()) return;
        String msg = String.format(format, args);
        StackTraceElement sElement = getStackTrace();
        Log.d(sElement.getClassName(), createLog(sElement, msg));
    }

    public static void eTagClassName(String msg) {
        if (!isDebuggable()) return;
        StackTraceElement sElement = getStackTrace();
        Log.e(sElement.getClassName(), createLog(sElement, msg));
    }

    public static void vTagClassName(String msg) {
        if (!isDebuggable()) return;
        StackTraceElement sElement = getStackTrace();
        Log.v(sElement.getClassName(), createLog(sElement, msg));
    }

    public static void wTagClassName(String msg) {
        if (!isDebuggable()) return;
        StackTraceElement sElement = getStackTrace();
        Log.w(sElement.getClassName(), createLog(sElement, msg));

    }

    // 自定义tag
    public static void iTag(String tag, String msg) {
        if (!isDebuggable()) return;
        if (tag == null) {
            tag = L_TAG;
        }
        StackTraceElement sElement = getStackTrace();
        Log.i(tag, createLog(sElement, msg));
    }

    public static void dTag(String tag, String msg) {
        if (!isDebuggable()) return;
        if (tag == null) {
            tag = L_TAG;
        }
        StackTraceElement sElement = getStackTrace();
        Log.d(tag, createLog(sElement, msg));
    }

    public static void eTag(String tag, String msg) {
        if (!isDebuggable()) return;
        if (tag == null) {
            tag = L_TAG;
        }
        StackTraceElement sElement = getStackTrace();
        Log.e(tag, createLog(sElement, msg));
    }

    public static void vTag(String tag, String msg) {
        if (!isDebuggable()) return;
        if (tag == null) {
            tag = L_TAG;
        }
        StackTraceElement sElement = getStackTrace();
        Log.v(tag, createLog(sElement, msg));
    }


    public static void wTag(String tag, String msg) {
        if (!isDebuggable()) return;
        if (tag == null) {
            tag = L_TAG;
        }
        StackTraceElement sElement = getStackTrace();
        Log.w(tag, createLog(sElement, msg));

    }

    private static void w(int hierarchy, String tag, String msg) {
        if (!isDebuggable()) return;
        if (tag == null) {
            tag = L_TAG;
        }
        StackTraceElement sElement = getStackTrace(hierarchy);
        Log.w(tag, createLog(sElement, msg));

    }

    public static void e(Throwable throwable, String message) {
        message = buildMessage(throwable, message);
        e(3,message);
    }

    public static void e(Throwable throwable, String tag, String message) {
        message = buildMessage(throwable, message);
        e(3,tag, message);
    }

    public static void w(Throwable throwable, String tag ) {
        w(3,tag, buildMessage(throwable, null) );
    }

    public static void w(Throwable throwable, String tag, String message) {
        w(3,tag, buildMessage(throwable, message));
    }

    private static String buildMessage(Throwable throwable, String message) {
        if (throwable != null && message != null) {
            message += " : " + throwable.toString();
        }
        if (throwable != null && message == null) {
            message = throwable.toString();
        }
        if (message == null) {
            message = "No message/exception is set";
        }
        return message;
    }

    private static StackTraceElement getStackTrace() {
        StackTraceElement sElement = new Throwable().getStackTrace()[2];
        return sElement;
    }

    private static StackTraceElement getStackTrace(int hierarchy) {
        StackTraceElement sElement = new Throwable().getStackTrace()[hierarchy];
        return sElement;
    }

    private static String createLog(StackTraceElement sElement, String log) {

        StringBuffer buffer = new StringBuffer();
        buffer.append("[T:");
        buffer.append(Thread.currentThread().getName());
        buffer.append(" M:");
        buffer.append(sElement.getMethodName());
        buffer.append(" L:");
        buffer.append(sElement.getLineNumber());
        buffer.append("] ");
        buffer.append(log);
        return buffer.toString();
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLog(Context context, CharSequence message) {
        if (!isDebuggable()) return;
        if (null == toast) {
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            // toast.setGravity(Gravity.CENTER, 0, 0);
        }
        toast.setText(message);
        toast.show();
    }

}
