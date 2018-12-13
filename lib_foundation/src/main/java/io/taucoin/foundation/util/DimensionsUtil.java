package io.taucoin.foundation.util;

import android.content.Context;
import android.util.TypedValue;

public class DimensionsUtil {
    /**
     * dp to px
     */
    public static int dp2px(Context content, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                content.getResources().getDisplayMetrics());
    }

    /**
     * dip or dp ro px
     *
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dp(Context content, float px) {
        final float scale =  content.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

}
