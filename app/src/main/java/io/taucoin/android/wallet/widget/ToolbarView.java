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
package io.taucoin.android.wallet.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mofei.tau.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.taucoin.android.wallet.util.NotchUtil;
import io.taucoin.android.wallet.util.KeyboardUtils;
import io.taucoin.foundation.util.StringUtil;

public class ToolbarView extends RelativeLayout {
    private String titleText;
    private int titleBackground;
    private int leftImage;
    private int rightImage;
    private ViewHolder viewHolder;
    private int statusBarHeight;

    public ToolbarView(Context context) {
        this(context, null);
    }

    public ToolbarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ToolbarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        statusBarHeight = NotchUtil.getStatusBarOrNotchHeight(context);
        initData(attrs);
    }

    private void initData(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ToolbarView);
        this.leftImage = a.getResourceId(R.styleable.ToolbarView_leftBackImage, -1);
        this.rightImage = a.getResourceId(R.styleable.ToolbarView_rightImage, -1);
        this.titleText = a.getString(R.styleable.ToolbarView_titleText);
        this.titleBackground = a.getColor(R.styleable.ToolbarView_titleBackground, -1);
        a.recycle();
        loadView();
    }

    private void loadView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_toolbar, this, true);
        viewHolder = new ViewHolder(view);
        if(statusBarHeight > 0){
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewHolder.rlToolBar.getLayoutParams();
            layoutParams.topMargin = statusBarHeight;
            viewHolder.rlToolBar.setLayoutParams(layoutParams);
        }

        if (leftImage != -1) {
            viewHolder.ivLeftBack.setImageResource(leftImage);
        } else {
            viewHolder.ivLeftBack.setVisibility(INVISIBLE);
        }
        if (rightImage != -1) {
            viewHolder.ivRight.setImageResource(rightImage);
        } else {
            viewHolder.ivRight.setVisibility(INVISIBLE);
        }
        if (StringUtil.isNotEmpty(titleText)) {
            viewHolder.tvTitle.setText(titleText);
        }
        if (titleBackground != -1) {
            view.setBackgroundColor(titleBackground);
        }
        requestLayout();
    }

    public void setTitle(String title) {
        viewHolder.tvTitle.setText(title);
    }


    class ViewHolder {
        @BindView(R.id.iv_left_back)
        ImageButton ivLeftBack;
        @BindView(R.id.iv_right)
        ImageButton ivRight;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.rl_tool_bar)
        RelativeLayout rlToolBar;

        @OnClick(R.id.iv_left_back)
        void leftBack() {
            try {
                ((FragmentActivity)getContext()).finish();
                KeyboardUtils.hideSoftInput((FragmentActivity)getContext());
            }catch (Exception ignore){}
        }

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}
