package io.taucoin.android.wallet.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.EditText;

@SuppressLint("AppCompatCustomView")
public class SelectionEditText extends EditText {
    OnSelectionChange mOnSelectionChange;
    public SelectionEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        if (mOnSelectionChange !=null){
            mOnSelectionChange.onSelectionChange(selStart,selEnd);
        }
    }

    public void setOnSelectionChange(OnSelectionChange onSelectionChange){
        mOnSelectionChange = onSelectionChange;
    }

    public interface OnSelectionChange{
        void onSelectionChange(int selStart, int selEnd);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @SuppressLint("ClickableViewAccessibility")
    public void shieldingEvent() {
        setOnTouchListener((v, event) -> true);
    }
}


