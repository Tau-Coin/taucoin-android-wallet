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
package io.taucoin.android.wallet.widget.download;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatDialog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.mofei.tau.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.taucoin.foundation.util.DimensionsUtil;
import io.taucoin.foundation.util.StringUtil;

public class DownloadDialog extends AppCompatDialog {

    private static boolean isCanCancel = true;


    private DownloadDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private String positiveButtonText;
        private String negativeButtonText;
        private boolean isEnabledPositive = true;
        private boolean isEnabledNegative = true;
        private int btnWidth;
        private OnClickListener positiveButtonClickListener;
        private OnClickListener negativeButtonClickListener;
        private View mContentView;

        public Builder(Context context) {
            isCanCancel = true;
            this.context = context;
        }

        public DownloadDialog.Builder setPositiveButton(int positiveButtonText,
                                                                                       OnClickListener listener) {
            this.positiveButtonText = (String) context
                    .getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public DownloadDialog.Builder setPositiveButton(OnClickListener listener) {
            this.positiveButtonClickListener = listener;
            return this;
        }

        public DownloadDialog.Builder setPositiveButton(String positiveButtonText,
                                                                                       OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public DownloadDialog.Builder setButtonWidth(int dpWidth) {
            this.btnWidth = dpWidth;
            return this;
        }

        public DownloadDialog.Builder isEnabledPositive(boolean isEnabled) {
            this.isEnabledNegative = isEnabled;
            return this;
        }

        public DownloadDialog.Builder isEnabledNegative(boolean isEnabled) {
            this.isEnabledNegative = isEnabled;
            return this;
        }

        public DownloadDialog.Builder setCanceledOnTouchOutside(boolean cancel) {
            isCanCancel = cancel;
            return this;
        }

        public DownloadDialog.Builder setContentView(View view) {
            this.mContentView = view;
            return this;
        }

        public DownloadDialog.Builder setNegativeButton(int negativeButtonText,
                                                                                       OnClickListener listener) {
            this.negativeButtonText = (String) context
                    .getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public DownloadDialog.Builder setNegativeButton(String negativeButtonText,
                                                                                       OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public DownloadDialog create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final DownloadDialog dialog = new DownloadDialog(context, R.style.CommonDialog);
            ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.dialog_common_layout, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            if(mContentView != null){
                layout.addView(mContentView, 0);
            }
            resetDialogWidth(layout, dialog);
            DownloadDialog.Builder.ViewHolder viewHolder = new DownloadDialog.Builder.ViewHolder(layout);
            viewHolder.positiveButton.setText(positiveButtonText);
            if (StringUtil.isEmpty(positiveButtonText)) {
                viewHolder.positiveButton.setVisibility(View.GONE);
            }
            viewHolder.positiveButton.setEnabled(isEnabledPositive);
            viewHolder.negativeButton.setEnabled(isEnabledNegative);

            if(!isEnabledPositive){
                viewHolder.positiveButton.setBackgroundResource(R.drawable.grey_rect_round_bg);
            }
            if(!isEnabledNegative){
                viewHolder.negativeButton.setBackgroundResource(R.drawable.grey_rect_round_bg);
            }

            viewHolder.negativeButton.setText(negativeButtonText);
            if (StringUtil.isEmpty(negativeButtonText)) {
                viewHolder.negativeButton.setVisibility(View.GONE);
            }

            if (positiveButtonClickListener != null) {
                viewHolder.positiveButton.setOnClickListener(v -> positiveButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE));
            }
            if (negativeButtonClickListener != null) {
                viewHolder.negativeButton.setOnClickListener(v -> negativeButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE));
            }

            if(btnWidth > 0){
                viewHolder.negativeButton.setWidth(DimensionsUtil.dip2px(context, btnWidth));
                viewHolder.positiveButton.setWidth(DimensionsUtil.dip2px(context, btnWidth));
            }
            dialog.setCanceledOnTouchOutside(isCanCancel);
            return dialog;
        }

        private void resetDialogWidth(ViewGroup layout, DownloadDialog dialog) {
            WindowManager windowManager = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            layout.setMinimumWidth((int) (display.getWidth() * 0.85));
        }

        class ViewHolder {
            @BindView(R.id.positiveButton)
            Button positiveButton;
            @BindView(R.id.negativeButton)
            Button negativeButton;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }
}