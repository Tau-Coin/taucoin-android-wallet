package io.taucoin.foundation.util;

import android.app.Activity;
import java.util.ArrayList;
import java.util.List;

public class ActivityManager {

    private static List<Activity> activities = new ArrayList<>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        if (activities.contains(activity)) {
            activities.remove(activity);
        }
    }

    public static void finishAll() {
        for (Activity activity : activities) {
            if (activity != null && !activity.isFinishing()) {
                activity.finish();
            }
        }
        activities.clear();
    }

    public static Activity getTopActivity() {
        if (activities.isEmpty()) {
            return null;
        } else {
            return activities.get(activities.size() - 1);
        }
    }

    public static boolean isTopActivity(Class<?> zClass) {
        if (activities.isEmpty()) {
            return false;
        } else {
            Activity topActivity = activities.get(activities.size() - 1);
            return zClass.equals(topActivity.getClass());
        }
    }
}