package io.taucoin.android.wallet.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mofei.tau.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.taucoin.android.wallet.util.KeyboardUtils;
import io.taucoin.foundation.util.StringUtil;

public class ToolbarView extends RelativeLayout {
    private String titleText;
    private int titleBackground;
    private int leftImage;

    public ToolbarView(Context context) {
        this(context, null);
    }

    public ToolbarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ToolbarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData(attrs);

    }

    private void initData(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ToolbarView);
        this.leftImage = a.getResourceId(R.styleable.ToolbarView_leftBackImage, -1);
        this.titleText = a.getString(R.styleable.ToolbarView_titleText);
        this.titleBackground = a.getColor(R.styleable.ToolbarView_titleBackground, -1);
        a.recycle();
        loadView();
    }

    private void loadView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_toolbar, this, true);
        ViewHolder viewHolder = new ViewHolder(view);
        if (leftImage != -1) {
            viewHolder.ivLeftBack.setImageResource(leftImage);
        } else {
            viewHolder.ivLeftBack.setVisibility(INVISIBLE);
        }
        if (StringUtil.isNotEmpty(titleText)) {
            viewHolder.tvTitle.setText(titleText);
        }
        if (titleBackground != -1) {
            view.setBackgroundColor(titleBackground);
        }
    }

    class ViewHolder {
        @BindView(R.id.iv_left_back)
        ImageButton ivLeftBack;
        @BindView(R.id.tv_title)
        TextView tvTitle;

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
