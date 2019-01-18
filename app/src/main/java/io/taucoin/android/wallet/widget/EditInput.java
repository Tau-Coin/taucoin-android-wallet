package io.taucoin.android.wallet.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mofei.tau.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditInput extends LinearLayout {

    private static final float PADDING_DEFAULT = 0;
    private static final float PADDING_DEFAULT_LEFT = 0;
    private static final float ANDROID_SDK_NUM = 19;
    @BindView(R.id.et_input)
    SelectionEditText mEtInput;
    @BindView(R.id.tv_et_hint)
    TextView mTvEtHint;
    @BindView(R.id.ll)
    LinearLayout mLl;
    
    private String emptyStr = " ";

    public EditInput(Context context) {
        this(context, null);
    }

    public EditInput(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EditInput(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        View.inflate(context, R.layout.layout_et, this);
        ButterKnife.bind(this);
        try {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EditInput);
            int paddingBottom = a.getDimensionPixelSize(R.styleable.EditInput_ei_paddingBottom,
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, PADDING_DEFAULT,
                            getResources().getDisplayMetrics()));
            int paddingLeft = a.getDimensionPixelSize(R.styleable.EditInput_ei_paddingLeft,
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, PADDING_DEFAULT_LEFT,
                            getResources().getDisplayMetrics()));
            int paddingRight = a.getDimensionPixelSize(R.styleable.EditInput_ei_paddingRight,
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, PADDING_DEFAULT_LEFT,
                            getResources().getDisplayMetrics()));
            int paddingTop = a.getDimensionPixelSize(R.styleable.EditInput_ei_paddingTop,
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, PADDING_DEFAULT,
                            getResources().getDisplayMetrics()));
            mLl.setPadding(paddingLeft, 0, 0, 0);
            mEtInput.setPadding(0, paddingTop, paddingRight, paddingBottom);
            mTvEtHint.setPadding(0, paddingTop, 5, paddingBottom);
            a.recycle();
        } catch (Exception ignore) {
        }
        //EditText get focus，cover TextView disappear
        mEtInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mTvEtHint.setVisibility(GONE);
            } else {
                if (!TextUtils.isEmpty(mEtInput.getText().toString().trim())) {
                    mTvEtHint.setVisibility(GONE);
                } else {
                    mTvEtHint.setVisibility(VISIBLE);
                }
            }
        });

        // Execute this code above Android 7.0
        if (Build.VERSION.SDK_INT >= ANDROID_SDK_NUM) {
            //Initially, EditText adds spaces, and the cursor is positioned in front of the spaces.
            mEtInput.setText(emptyStr);
            mEtInput.setSelection(0);
            //EditText monitors cursor changes, forcing the cursor to change to the front of the space when the cursor is at the end
            mEtInput.setOnSelectionChange((selStart, selEnd) -> {
                String etText = mEtInput.getText().toString();
                if (etText.endsWith(emptyStr)) {
                    if (selStart == mEtInput.getText().toString().length()) {
                        mEtInput.setSelection(mEtInput.getText().toString().length() - 1);
                    }
                }
            });
            //Ensure that EditText enters text in front of the space and cursor in front of the space
//            mEtInput.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//                    if (!mEtInput.getText().toString().endsWith(emptyStr)) {
//                        String text = s + emptyStr;
//                        mEtInput.setText(text);
//                    }
//                    if (mEtInput.getSelectionStart() == s.length()) {
//                        mEtInput.setSelection(s.length() - 1);
//                    }
//                }
//            });
        }
    }

    /**
     * Initialize title and EditText prompt text
     *
     * @param contentHint hit
     */
    public void initText(String contentHint) {
        mTvEtHint.setText(contentHint);
    }

    public void setText(int resId) {
        setText(getContext().getString(resId));
    }

    /**
     * set EditText text
     *
     * @param text text
     */
    public void setText(String text) {
        // Add space at the end of the high version and set the cursor before the space
        if (!TextUtils.isEmpty(text)) {
            if (Build.VERSION.SDK_INT >= ANDROID_SDK_NUM) {
                text = text + emptyStr;
                mEtInput.setText(text);
                mEtInput.setSelection(text.length() - 1);
            } else {
                mEtInput.setText(text);
                mEtInput.setSelection(text.length());
            }
            mTvEtHint.setVisibility(GONE);
        } else {
            if (Build.VERSION.SDK_INT >= ANDROID_SDK_NUM) {
                mEtInput.setText(emptyStr);
                mEtInput.setSelection(0);
            }
        }
    }

    public String getText() {
        return mEtInput.getText().toString().trim();
    }

    public SelectionEditText getEditText() {
        return mEtInput;
    }
}