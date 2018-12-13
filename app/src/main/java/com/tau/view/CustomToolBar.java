package com.tau.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mofei.tau.R;


/**
 * @author ly
 * 创建时间：2018/9/28
 * 类说明: title bar
 */

public class CustomToolBar extends FrameLayout implements ToolBarInterface{
    private ViewGroup content;
    private ImageButton mRightImageButton;
    private TextView mTitleTextView;
    private ImageButton mLeftImageButton;
    private TextView mLeftTextView;
    private TextView mRightTextView;
//    private ImageView mRightImageView;

    public CustomToolBar(Context context) {
        this(context, null);
    }

    public CustomToolBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("RestrictedApi")
    public CustomToolBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                R.styleable.CustomToolBar, defStyleAttr, 0);
        this.content = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.customtoolbar, this, true);
        this.mTitleTextView = (TextView) content.findViewById(R.id.titleView);
        this.mRightImageButton = (ImageButton) content.findViewById(R.id.right_ImageButton);
        this.mLeftImageButton = (ImageButton) content.findViewById(R.id.left_ImageButton);
        this.mLeftTextView = (TextView) content.findViewById(R.id.left_textview);
        this.mRightTextView = (TextView) content.findViewById(R.id.right_textview);
//        this.mRightImageView= (ImageView) content.findViewById(R.id.right_imageView);
        if (a.getBoolean(R.styleable.CustomToolBar_disableRightView, true)) {
            disableRightView();
        }
        boolean enableLeftView = a.getBoolean(R.styleable.CustomToolBar_enableLeftTextView, true);
        if (enableLeftView) {
            enableLeftTextView();
        } else {
            disableLeftTextView();
        }
        if (a.getBoolean(R.styleable.CustomToolBar_enableRightTextView, false)) {
            enableRightTextView();
        } else {
            disableRightTextView();
        }
        if (a.getBoolean(R.styleable.CustomToolBar_enableGoBackEvent, false) && enableLeftView) {
            this.mLeftTextView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getContext() instanceof Activity) {
                        ((Activity) getContext()).finish();
                    }
                }
            });
        }
        a.recycle();
    }


    public void setNavigationIcon(@Nullable Drawable icon) {
        this.mLeftImageButton.setImageDrawable(icon);
    }

    public void setRightImageButtonIcon(@Nullable Drawable icon) {
        this.mRightImageButton.setImageDrawable(icon);
    }
    /*public void setNavigationIcon(int id) {
        this.mNavButtonView.setImageResource(id);
    }*/


    @Nullable
    public Drawable getNavigationIcon() {
        return mLeftImageButton != null ? mLeftImageButton.getDrawable() : null;
    }

    @Nullable
    public CharSequence getNavigationContentDescription() {
        return mLeftImageButton != null ? mLeftImageButton.getContentDescription() : null;
    }

    public void setNavigationContentDescription(@Nullable CharSequence description) {
        if (mLeftImageButton != null) {
            mLeftImageButton.setContentDescription(description);
        }
    }

    public void setNavigationOnClickListener(OnClickListener listener) {
        mLeftImageButton.setOnClickListener(listener);
    }

    public void setRightImageButtonOnClickListener(OnClickListener listener) {
        mRightImageButton.setOnClickListener(listener);
    }

    public void setRightTextViewClickListener(OnClickListener listener) {
        mRightTextView.setOnClickListener(listener);
    }

    public void setNavigationContentDescription(@StringRes int resId) {
        setNavigationContentDescription(resId != 0 ? getContext().getText(resId) : null);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    public void setTitle(@StringRes int resId) {
        mTitleTextView.setText(resId);
    }

    public void setTitle(String title) {
        mTitleTextView.setText(title);
    }

    public ImageButton getNavButtonView() {
        return mLeftImageButton;
    }

    public ImageButton getMenuView() {
        return mRightImageButton;
    }

    public TextView getTitleTextView() {
        return mTitleTextView;
    }

    public TextView getLeftTextView() {
        return this.mLeftTextView;
    }

    public TextView getRightTextView() {
        return this.mRightTextView;
    }

    public void setRightTextView(String text){
        this.mRightTextView.setText(text);
    }

    public void enableLeftTextView() {
        this.mLeftImageButton.setVisibility(View.GONE);
        this.mLeftTextView.setVisibility(View.VISIBLE);
    }

    public void disableLeftTextView() {
        this.mLeftImageButton.setVisibility(View.VISIBLE);
        this.mLeftTextView.setVisibility(View.GONE);
        this.mLeftTextView.setOnClickListener(null);
    }


    public void enableRightTextView() {
        this.mRightImageButton.setVisibility(View.GONE);
        this.mRightTextView.setVisibility(View.VISIBLE);
    }

    public void disableRightTextView() {
        this.mRightImageButton.setVisibility(View.VISIBLE);
        this.mRightTextView.setVisibility(View.GONE);
    }



    public void disableRightView() {
        this.mRightImageButton.setVisibility(View.GONE);
        this.mRightTextView.setVisibility(View.GONE);
    }

    public void disableLeftView(){
        this.mLeftImageButton.setVisibility(View.GONE);
        this.mLeftTextView.setVisibility(View.GONE);
    }


    public void disableRightImageButton() {
        this.mRightImageButton.setVisibility(View.GONE);
    }

    public void enableRightImageButton() {
        this.mRightImageButton.setVisibility(View.VISIBLE);
    }


}
