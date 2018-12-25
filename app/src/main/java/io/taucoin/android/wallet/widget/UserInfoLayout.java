/*
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
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class UserInfoLayout extends RelativeLayout {
    public UserInfoLayout(Context context) {
        this(context, null);
    }

    public UserInfoLayout(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public UserInfoLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData(attrs);
    }

    private void initData(AttributeSet attrs) {
//        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.UserInfoLayout);
//        this.emptyText = a.getString(R.styleable.UserInfoLayout_viewBackground);
//        this.emptyTip = a.getString(R.styleable.);
//        a.recycle();
//        loadView();
    }
//
//    private void loadView() {
//        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_layout_empty_merge, this, true);
//        viewHolder = new ViewHolder(view);
//        if(StringUtil.isNotEmpty(emptyText)){
//            viewHolder.btnEmptyAction.setText(emptyText);
//            viewHolder.btnEmptyAction.setVisibility(View.VISIBLE);
//        }else{
//            viewHolder.btnEmptyAction.setVisibility(View.GONE);
//        }
//        if(StringUtil.isNotEmpty(emptyTip)){
//            viewHolder.tvEmptyTips.setText(emptyTip);
//            viewHolder.tvEmptyTips.setVisibility(View.VISIBLE);
//        }else{
//            viewHolder.tvEmptyTips.setVisibility(View.GONE);
//        }
//    }
//
//    public void setEmptyText(String emptyText){
//        viewHolder.btnEmptyAction.setText(emptyText);
//        viewHolder.btnEmptyAction.setVisibility(View.VISIBLE);
//    }
//
//    public void setEmptyTip(String emptyTip){
//        viewHolder.tvEmptyTips.setText(emptyTip);
//        viewHolder.tvEmptyTips.setVisibility(View.VISIBLE);
//    }
//    public void hideLogo(){
//        viewHolder.ivEmptyIcon.setVisibility(View.GONE);
//    }
//
//    class ViewHolder {
//        @BindView(R.id.tv_empty_tips)
//        TextView tvEmptyTips;
//        @BindView(R.id.btn_empty_action)
//        Button btnEmptyAction;
//        @BindView(R.id.iv_empty_icon)
//        ImageView ivEmptyIcon;
//
//        ViewHolder(View view) {
//            ButterKnife.bind(this, view);
//        }
//    }
}
