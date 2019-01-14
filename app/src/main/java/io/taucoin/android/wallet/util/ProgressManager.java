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

import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import com.github.naturs.logger.Logger;
import com.mofei.tau.R;

import java.lang.ref.WeakReference;

/**
 * Description: Progress Manager
 */
public class ProgressManager {

    private static volatile WeakReference<Dialog> mProgress;

    private static WeakReference<FragmentActivity> mWeakReference;

    public static synchronized void showProgressDialog(FragmentActivity activity){
        showProgressDialog(activity, true);
    }

    public static synchronized void showProgressDialog(FragmentActivity activity, boolean isCanCancel){
        closeProgressDialog();
        Logger.d("showProgressDialog");
        mWeakReference = new WeakReference<>(activity);
        if(activity != null && activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) != null){

            Dialog progress = new Dialog(activity, R.style.dialog_translucent);
            progress.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progress.setContentView(R.layout.dialog_waiting);
            progress.setCanceledOnTouchOutside(isCanCancel);
            progress.setOnCancelListener(dialog -> closeProgressDialog(activity));
            mProgress = new WeakReference<>(progress);
            if(!activity.isFinishing()){
                progress.show();
            }
        }
    }

    public static synchronized void closeProgressDialog(){
        if(mProgress != null && mProgress.get() != null){
            mProgress.get().dismiss();
            if(mProgress != null){
                Logger.d("closeProgressDialog");
                mProgress.clear();
            }
            if(mWeakReference != null){
                mWeakReference.clear();
            }
        }
    }

    public static synchronized void closeProgressDialog(FragmentActivity activity){
        try {
            if(mProgress != null && mWeakReference != null){
                FragmentActivity activityReference = mWeakReference.get();
                if(activityReference != null &&
                        activity.getClass().equals(activityReference.getClass())){
                    Logger.d("closeProgressDialog(FragmentActivity activity)");
                    closeProgressDialog();
                }
            }
        }catch (Exception ignore){}
    }

    public static boolean isShowing(){
        if(mProgress != null && mProgress.get() != null){
            return mProgress.get().isShowing();
        }
        return false;
    }
}