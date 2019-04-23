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

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.text.style.ReplacementSpan;
import android.widget.TextView;

import com.github.naturs.logger.Logger;

import java.util.ArrayList;
import java.util.List;

public class BreakTextSpan extends ReplacementSpan {
    private int mSize;
    private TextView mTextView;
    private String mText;
    private boolean isRefresh;
    private int oneCharWidth;
    private int mY;
    private List<String> textList = new ArrayList<>();
    public BreakTextSpan(TextView textView, String text) {
        isRefresh = true;
        mY = 0;
        mText = text;
        mTextView = textView;
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        mSize = (int) (paint.measureText(text, start, end));
        if (fm != null) {
            Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
            int fontHeight = fmPaint.bottom - fmPaint.top;
            int top = fontHeight - fontHeight / 2;

            fm.ascent = -fontHeight;
            fm.top = -fontHeight;
            fm.bottom = top;
            fm.descent = top;
        }
        return mSize;
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        parseText(text, paint);

        if(isRefresh){
            isRefresh = false;
            mY = y;
            mTextView.setHeight((textList.size() + 1 ) * y);
        }

        int color = paint.getColor();
        paint.setAntiAlias(true);
        paint.setColor(color);

        canvas.save();
        int height = 0;
        for(String txt : textList){
            if(mY != y){
                break;
            }
            height += mY;
            Logger.d("text.height===" + height + "==y="+y);
            canvas.drawText(txt, start, txt.length(), x + oneCharWidth / 2, height, paint);
        }
        canvas.restore();
    }

    private synchronized void parseText(CharSequence text, Paint paint) {
        if(textList.size() > 0){
            return;
        }
        char[] chars = mText.toCharArray();
        oneCharWidth = mSize / chars.length;
        int viewWidth = mTextView.getWidth() - mTextView.getPaddingStart() - mTextView.getPaddingEnd() - 2 * oneCharWidth;
        StringBuilder newText = new StringBuilder();
        newText.append(chars[0]);
        int startPos = 0;
        for (int i = 1; i < chars.length; i++) {
            int size = (int) (paint.measureText(text, startPos, i));
            if(size <= viewWidth){
                newText.append(chars[i]);
                if(i == chars.length - 1){
                    textList.add(newText.toString());
                }
            }else{
                textList.add(newText.toString());
                newText.delete(0, newText.length());

                newText.append(chars[i]);
                startPos = i;
            }
        }
    }
}