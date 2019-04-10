package io.taucoin.android.wallet.widget;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.github.naturs.logger.Logger;

import io.taucoin.foundation.util.StringUtil;

/**
 * A single word can be displayed on two lines
 */
public class BreakTextView extends AppCompatTextView {

    private boolean mEnabled = false;
    private CharSequence mRawText;
    private BreakTextView mView;

    public BreakTextView(Context context) {
        this(context, null);
    }

    public BreakTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BreakTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mView = this;
    }

    private void addOnGlobalLayoutListener(){
        ViewGroup viewGroup = (ViewGroup) mView.getParent().getParent();
        viewGroup.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                viewGroup.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                loadView();
            }
        });
    }

    private synchronized void loadView() {
        if(!mEnabled){
            return;
        }
        Logger.d("loadView====success===" + mEnabled);
        mEnabled = false;
        CharSequence mText = autoSplitText(this);
        if (!TextUtils.isEmpty(mText)) {
            setText(mText);
        }
    }

    public CharSequence getRawText() {
        return mRawText;
    }

    public void setAutoSplitText(CharSequence text) {
        mEnabled = true;
        mRawText = text;
        addOnGlobalLayoutListener();
    }

    /**
     * Auto-Breaking Words
     */
    private String autoSplitText(final TextView tv) {
        final String rawText = StringUtil.getString(mRawText);
        final Paint tvPaint = tv.getPaint();
        final float tvWidth = tv.getWidth() - tv.getPaddingLeft() - tv.getPaddingRight();
        String[] rawTextLines = rawText.replaceAll("\r", "").split("\n");
        StringBuilder sbNewText = new StringBuilder();
        for (String rawTextLine : rawTextLines) {
            if (tvPaint.measureText(rawTextLine) <= tvWidth) {
                sbNewText.append(rawTextLine);
            } else {
                float lineWidth = 0;
                for (int cnt = 0; cnt != rawTextLine.length(); ++cnt) {
                    char ch = rawTextLine.charAt(cnt);
                    lineWidth += tvPaint.measureText(String.valueOf(ch));
                    if (lineWidth <= tvWidth) {
                        sbNewText.append(ch);
                    } else {
                        sbNewText.append("\n");
                        lineWidth = 0;
                        --cnt;
                    }
                }
            }
            sbNewText.append("\n");
        }
        if (!rawText.endsWith("\n")) {
            sbNewText.deleteCharAt(sbNewText.length() - 1);
        }
        return sbNewText.toString();
    }
}