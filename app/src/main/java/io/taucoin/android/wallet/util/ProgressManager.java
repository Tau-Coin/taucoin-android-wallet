package io.taucoin.android.wallet.util;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import com.mofei.tau.R;

/**
 * Description: Progress Manager
 */
public class ProgressManager {

    private static Dialog mProgress;

    public static void showProgressDialog(FragmentActivity activity){
        if(mProgress != null){
            mProgress.dismiss();
            mProgress = null;
        }
        if(activity != null && activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) != null){

            Dialog progress = new Dialog(activity, R.style.dialog_translucent);
            progress.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progress.setContentView(R.layout.dialog_waiting);
            progress.setCanceledOnTouchOutside(true);

            mProgress = progress;
            if(!activity.isFinishing()){
                progress.show();
            }
        }
    }

    public static void closeProgressDialog(){
        if(mProgress != null){
            mProgress.dismiss();
            mProgress = null;
        }
    }

    public static void closeProgressDialog(FragmentActivity activity){
        if(mProgress != null){
            FragmentActivity activityDialog = (FragmentActivity) mProgress.getContext();
            if(activityDialog.getClass().equals(activity.getClass())){
                mProgress.dismiss();
                mProgress = null;
            }
        }
    }
}