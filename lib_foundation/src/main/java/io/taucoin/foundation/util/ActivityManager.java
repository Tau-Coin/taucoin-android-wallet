package io.taucoin.foundation.util;

import android.app.Activity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class ActivityManager {

    private static List<Activity> mActivityList = new ArrayList<>();
    private static ActivityManager mInstance;
    private WeakReference<Activity> mCurrentActivityWeakRef;

    private ActivityManager(){}
    
    public static synchronized ActivityManager getInstance(){
        if(mInstance == null){
            synchronized (ActivityManager.class){
                if(mInstance == null){
                    mInstance = new ActivityManager();
                }
                return mInstance;
            }
        }else{
            return mInstance;
        }
    }
    

    public void addActivity(Activity activity) {
        mActivityList.add(activity);
    }

    public int getActivitySize() {
        return mActivityList.size();
    }

    public void removeActivity(Activity activity) {
        if (activity != null)
            mActivityList.remove(activity);
    }

    public void finishAll() {
        for (Activity activity : mActivityList) {
            if (activity != null && !activity.isFinishing()) {
                activity.finish();
            }
        }
        mActivityList.clear();
    }

    public Activity getTopActivity() {
        if (mActivityList.isEmpty()) {
            return null;
        } else {
            return mActivityList.get(mActivityList.size() - 1);
        }
    }

    public boolean isTopActivity(Class<?> zClass) {
        if (mActivityList.isEmpty()) {
            return false;
        } else {
            Activity topActivity = mActivityList.get(mActivityList.size() - 1);
            return zClass.equals(topActivity.getClass());
        }
    }
    
    public Activity currentActivity() {
        Activity currentActivity = null;
        if (mCurrentActivityWeakRef != null) {
            currentActivity = mCurrentActivityWeakRef.get();
        }
        return currentActivity;
    }
    
    public void setCurrentActivity(Activity activity) {
        if (mCurrentActivityWeakRef == null || !activity.equals(mCurrentActivityWeakRef.get())) {
            mCurrentActivityWeakRef = new WeakReference<>(activity);
        }
    }
}