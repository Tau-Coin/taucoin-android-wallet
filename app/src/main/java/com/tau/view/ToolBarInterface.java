package com.mofei.tau.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.View;

/**
 * Created by uid11497 on 2018/3/8.
 */

public interface ToolBarInterface {
    public void setNavigationIcon(@Nullable Drawable icon);

    @Nullable
    public Drawable getNavigationIcon();

    @Nullable
    public CharSequence getNavigationContentDescription();

    public void setNavigationContentDescription(@Nullable CharSequence description);
    public void setNavigationContentDescription(@StringRes int resId);
    public void setNavigationOnClickListener(View.OnClickListener listener);
    public Context getContext();
}
