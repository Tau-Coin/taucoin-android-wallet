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

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mofei.tau.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.taucoin.foundation.util.DimensionsUtil;
import io.taucoin.foundation.util.StringUtil;

public class ActionSheetDialog {
    private Context context;
    private Dialog dialog;
    private List<SheetItem> sheetItemList = new ArrayList<>();
    private Display display;
    private ViewHolder mViewHolder;
    private String mSelectValue;

    public ActionSheetDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public ActionSheetDialog builder() {

        View view = View.inflate(context,
                R.layout.view_actionsheet, null);
        view.setMinimumWidth(display.getWidth());
        mViewHolder = new ViewHolder(view);

        dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);

        return this;
    }

    public ActionSheetDialog setTitle(int title, int tip) {
        mViewHolder.tvTitle.setText(title);
        mViewHolder.tvTitle.setVisibility(View.VISIBLE);
        mViewHolder.tvTips.setText(tip);
        mViewHolder.tvTips.setVisibility(View.VISIBLE);
        return this;
    }

    public ActionSheetDialog setCancel(int cancel) {
        mViewHolder.tvCancel.setText(cancel);
        mViewHolder.tvCancel.setVisibility(View.VISIBLE);
        return this;
    }

    public ActionSheetDialog setCancel(int cancel, OnSheetItemClickListener listener) {
        mViewHolder.tvCancel.setText(cancel);
        mViewHolder.tvCancel.setVisibility(View.VISIBLE);
        mViewHolder.listener = listener;
        return this;
    }


    public ActionSheetDialog setSelectValue(String value) {
        mSelectValue = value;
        return this;
    }

    public ActionSheetDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public ActionSheetDialog setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    public ActionSheetDialog addSheetItem(int intName, int intViceName, int intId,
                                          OnSheetItemClickListener listener) {
        String name = context.getResources().getString(intName);
        String viceName = context.getResources().getString(intViceName);
        String id = context.getResources().getString(intId);
        sheetItemList.add(new SheetItem(name, viceName, id, listener));
        return this;
    }

    public ActionSheetDialog addSheetItem(int intName, OnSheetItemClickListener listener) {
        String name = context.getResources().getString(intName);
        sheetItemList.add(new SheetItem(name, "", "", listener));
        return this;
    }

    private void setSheetItems() {
        if (sheetItemList.size() <= 0) {
            return;
        }

        int size = sheetItemList.size();

//        if (size >= 7) {
//            LayoutParams params = (LayoutParams) mViewHolder.sLayoutContent
//                    .getLayoutParams();
//            params.height = display.getHeight() / 2;
//            mViewHolder.sLayoutContent.setLayoutParams(params);
//        }

        for (int i = 1; i <= size; i++) {
            SheetItem sheetItem = sheetItemList.get(i - 1);
            final OnSheetItemClickListener listener = sheetItem.itemClickListener;
            View view = LayoutInflater.from(context).inflate(
                    R.layout.item_actionsheet, mViewHolder.lLayoutContent, false);
            ItemViewHolder itemViewHolder = new ItemViewHolder(view);

            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            layoutParams.topMargin = DimensionsUtil.dip2px(dialog.getContext(), 1);
            view.setLayoutParams(layoutParams);

            if(StringUtil.isNotEmpty(sheetItem.name)){
                itemViewHolder.tvLeft.setText(sheetItem.name);
            }

            if(StringUtil.isNotEmpty(sheetItem.viceName)){
                itemViewHolder.tvRight.setText(sheetItem.viceName);
            }
            boolean isShow = StringUtil.isNotEmpty(mSelectValue) && StringUtil.isSame(mSelectValue, sheetItem.id);
            itemViewHolder.ivSelect.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);

            view.setOnClickListener(v -> {
                listener.onClick(sheetItem);
                dialog.cancel();
            });
            mViewHolder.lLayoutContent.addView(view);
        }
    }

    public void show() {
        setSheetItems();
        dialog.show();
    }

    public interface OnSheetItemClickListener {
        void onClick(SheetItem which);
    }

    public class SheetItem {
        CharSequence name;
        CharSequence viceName;
        public String id;
        OnSheetItemClickListener itemClickListener;

        SheetItem(CharSequence name, CharSequence viceName, String id,
                  OnSheetItemClickListener itemClickListener) {
            this.name = name;
            this.viceName = viceName;
            this.id = id;
            this.itemClickListener = itemClickListener;
        }
    }

    class ViewHolder {
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_tips)
        TextView tvTips;
        @BindView(R.id.lLayout_content)
        LinearLayout lLayoutContent;
        @BindView(R.id.sLayout_content)
        ScrollView sLayoutContent;
        @BindView(R.id.tv_cancel)
        TextView tvCancel;

        OnSheetItemClickListener listener;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.tv_cancel)
        void onCancel() {
            if (dialog != null) {
                dialog.cancel();
            }
            if(listener != null){
                listener.onClick(null);
            }
        }
    }

    class ItemViewHolder {
        @BindView(R.id.tv_left)
        TextView tvLeft;
        @BindView(R.id.tv_right)
        TextView tvRight;
        @BindView(R.id.iv_select)
        ImageView ivSelect;

        ItemViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
