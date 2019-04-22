package io.taucoin.android.wallet.widget;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.style.ReplacementSpan;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BreakTextSpan extends ReplacementSpan {
    private int mSize;
    private TextView mTextView;
    private String mText;
    private boolean isRefresh;
    private long isDrawTime;
    private int oneCharWidth;
    private List<String> textList = new ArrayList<>();
    public BreakTextSpan(TextView textView, String text) {
        isRefresh = true;
        isDrawTime = 0;
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
        isDrawTime = 0;
        return mSize;
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        isDrawTime += 1;
        parseText(text, paint);

        int color = paint.getColor();
        paint.setAntiAlias(true);
        paint.setColor(color);

        int height = 0;
        for(String txt : textList){
            height += y;
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                canvas.drawText(txt, start, txt.length(), x + oneCharWidth / 2, height, paint);
            }else{
                if(isDrawTime % 2 == 1){
                    canvas.drawText(txt, start, txt.length(), x + oneCharWidth / 2, height, paint);
                }
            }
        }
        if(isRefresh){
            isRefresh = false;
            mTextView.setHeight(height + y);
        }
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