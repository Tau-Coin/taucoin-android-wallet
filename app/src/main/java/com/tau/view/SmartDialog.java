package com.tau.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.mofei.tau.R;


/**
 * @author
 * @version
 * 类说明
 */
public class SmartDialog extends Dialog {
    private TextView mTextView = null;

    private Handler handler;

    private boolean displayimmediately = false;

    public SmartDialog(@NonNull Context context) {
        super(context, R.style.dialog_translucent);
        init();
    }

    public SmartDialog(@NonNull Context context, boolean displayimmediately) {
        this(context);
        this.displayimmediately = displayimmediately;

    }

    public SmartDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    public boolean isDisplayimmediately() {
        return displayimmediately;
    }

    public void setDisplayimmediately(boolean displayimmediately) {
        this.displayimmediately = displayimmediately;
    }

    private void init() {
        handler = new Handler(Looper.getMainLooper());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_waiting);
        mTextView = findViewById(R.id.waittingText);
    }

    @Override
    public void show() {
        DelayShowTask delayShowTask=new DelayShowTask(null);
        if (this.displayimmediately) {
            delayShowTask.run();
        } else {
            handler.postDelayed(delayShowTask, 1000L);
        }
    }

    public void show(final String hint) {
        DelayShowTask delayShowTask = new DelayShowTask(hint);
        if (this.displayimmediately) {
            delayShowTask.run();
        } else {
            handler.postDelayed(delayShowTask, 1000L);
        }
    }

    public void show(@StringRes int hint) {
       this.show(getContext().getResources().getString(hint));
    }

    @Override
    public void dismiss() {
        handler.removeCallbacksAndMessages(null);
        try {
            super.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void hide() {
        handler.removeCallbacksAndMessages(null);
        if(isShowing()) {
            super.hide();
        }
    }

    private class DelayShowTask implements Runnable {
        String hintMsg;

        public DelayShowTask(String hintMsg) {
            this.hintMsg = hintMsg;
        }

        @Override
        public void run() {
            mTextView.setText(hintMsg);
            try {
                SmartDialog.super.show();
            } catch (Exception e) {
                e.printStackTrace();
                if (e instanceof WindowManager.BadTokenException) {
                    //eat it
                } else {
                    throw e;
                }
            }
        }
    }

}
