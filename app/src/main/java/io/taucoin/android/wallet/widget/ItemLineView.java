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
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mofei.tau.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.taucoin.foundation.util.StringUtil;

public class ItemLineView extends RelativeLayout {
    private String rightText;
    private int rightTextColor;
    private int leftImage;

    public ItemLineView(Context context) {
        this(context, null);
    }

    public ItemLineView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData(attrs);

    }

    private void initData(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ItemLineView);
        this.leftImage = a.getResourceId(R.styleable.ItemLineView_leftImage, -1);
        this.rightText = a.getString(R.styleable.ItemLineView_rightText);
        this.rightTextColor = a.getColor(R.styleable.ItemLineView_rightTextColor, getResources().getColor(R.color.color_black));
        a.recycle();
        loadView();
    }

    private void loadView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_item_line, this, true);
        ViewHolder viewHolder = new ViewHolder(view);
        if(leftImage != -1){
            viewHolder.ivLeft.setImageResource(leftImage);
        }
        if(StringUtil.isNotEmpty(rightText)){
            viewHolder.tvRight.setText(rightText);
        }
        viewHolder.tvRight.setTextColor(rightTextColor);
    }

    class ViewHolder {
        @BindView(R.id.iv_left)
        ImageView ivLeft;
        @BindView(R.id.tv_right)
        TextView tvRight;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
