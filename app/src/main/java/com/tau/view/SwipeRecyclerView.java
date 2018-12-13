package com.tau.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.mofei.tau.R;
import com.tau.adapter.HistoryEventRecycleAdapter;

/**
 * Created by ly on 18-11-2
 *
 * @version 1.0
 * @description:
 */
public class SwipeRecyclerView extends RecyclerView {
    private static final String TAG = "RecycleView";
    private int maxLength, mTouchSlop;
    private int xDown, yDown, xMove, yMove;
    /**
     * 当前选中的item索引（这个很重要）
     * The currently selected item index (this is very important).
     */
    private int curSelectPosition;
    private Scroller mScroller;

    private LinearLayout mCurItemLayout, mLastItemLayout;
    private LinearLayout mLlHidden;//隐藏部分
    private TextView mItemContent;
    private LinearLayout mItemDelete;

    /**
     * 隐藏部分长度
     * Hidden part length
     */
    private int mHiddenWidth;
    /**
     * 记录连续移动的长度
     */
    private int mMoveWidth = 0;
    /**
     * 是否是第一次touch
     * Record continuous movement length
     */
    private boolean isFirst = true;
    private Context mContext;

    /**
     * 删除的监听事件
     * Deleted listening events
     */
    private OnRightClickListener mRightListener;

    public void setRightClickListener(OnRightClickListener listener){
        this.mRightListener = listener;
    }


    public SwipeRecyclerView(Context context) {
        this(context, null);
    }

    public SwipeRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        /**
         * 滑动到最小距离
         * Slipping to minimum distance
         */
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        /**
         * 滑动的最大距离
         *Maximum distance of sliding
         */
        maxLength = ((int) (180 * context.getResources().getDisplayMetrics().density + 0.5f));
        /**
         * 初始化Scroller
         *Initialize Scroller
         */
        mScroller = new Scroller(context, new LinearInterpolator(context, null));
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        int x = (int)e.getX();
        int y = (int)e.getY();
        switch (e.getAction()){
            case MotionEvent.ACTION_DOWN:
                /**
                 * 记录当前按下的坐标
                 *Record the currently pressed coordinates
                 */
                xDown = x;
                yDown = y;
                /**
                 * 计算选中哪个Item
                 * Calculate which Item to choose.
                 */
                int firstPosition = ((LinearLayoutManager)getLayoutManager()).findFirstVisibleItemPosition();
                Rect itemRect = new Rect();

                final int count = getChildCount();
                for (int i=0; i<count; i++){
                    final View child = getChildAt(i);
                    if (child.getVisibility() == View.VISIBLE){
                        child.getHitRect(itemRect);
                        if (itemRect.contains(x, y)){
                            curSelectPosition = firstPosition + i;
                            break;
                        }
                    }
                }

                if (isFirst){
                    /**
                     * 第一次时，不用重置上一次的Item
                     * For the first time, do not reset the last Item.
                     */
                    isFirst = false;
                }else {
                    /**
                     * 屏幕再次接收到点击时，恢复上一次Item的状态
                     * Once the screen is clicked again, the state of the last Item is resumed.
                     */
                    if (mLastItemLayout != null && mMoveWidth > 0) {
                        /**
                         * 将Item右移，恢复原位
                         *Move the Item right, and restore it to its original position.
                         */
                        scrollRight(mLastItemLayout, (0 - mMoveWidth));
                        /**
                         * 清空变量
                         * clear
                         */
                        mHiddenWidth = 0;
                        mMoveWidth = 0;
                    }

                }

                /**
                 * 取到当前选中的Item，赋给mCurItemLayout，以便对其进行左移
                 *Gets the currently selected Item and assigns it to mCurItemLayout to move left.
                 */
                View item = getChildAt(curSelectPosition - firstPosition);
                if (item != null) {
                    /**
                     * 获取当前选中的Item
                     * Gets the currently selected Item
                     */
                    HistoryEventRecycleAdapter.HistoryViewHolder viewHolder=(HistoryEventRecycleAdapter.HistoryViewHolder)getChildViewHolder(item);
                    mCurItemLayout = viewHolder.linearLayout;
                    /**
                     * 找到具体元素（这与实际业务相关了~~）
                     * Find specific elements (which are related to actual business ~ ~).
                     */
                    mLlHidden = (LinearLayout)mCurItemLayout.findViewById(R.id.ll_hidden);
                    mItemDelete = (LinearLayout)mCurItemLayout.findViewById(R.id.ll_hidden);
                    mItemDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mRightListener != null){
                                //删除
                                mRightListener.onRightClick(curSelectPosition, "");
                            }
                        }
                    });

                    //这里将删除按钮的宽度设为可以移动的距离
                    //Here we delete the width of the button as the remotable distance.
                  //mHiddenWidth = mLlHidden.getWidth();
                    //禁止左移删除
                    mHiddenWidth = 0;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                xMove = x;
                yMove = y;
                int dx = xMove - xDown;//为负时：手指向左滑动；为正时：手指向右滑动。这与Android的屏幕坐标定义有关
                int dy = yMove - yDown;//When it is negative: fingers slide to the left; right timing: fingers slide to the right. This is related to the definition of the screen coordinates of Android.

                //左滑
                if (dx < 0 && Math.abs(dx) > mTouchSlop && Math.abs(dy) < mTouchSlop){
                    int newScrollX = Math.abs(dx);
                    if (mMoveWidth >= mHiddenWidth){//超过了，不能再移动了
                        newScrollX = 0;
                    } else if (mMoveWidth + newScrollX > mHiddenWidth){//这次要超了，
                        newScrollX = mHiddenWidth - mMoveWidth;
                    }
                    //左滑，每次滑动手指移动的距离
                    //Left sliding, the distance of finger movement per slide.
                    scrollLeft(mCurItemLayout, newScrollX);
                    //对移动的距离叠加
                    //Superposition of moving distance
                    mMoveWidth = mMoveWidth + newScrollX;
                }else if (dx > 0){//右滑
                    //执行右滑，这里没有做跟随，瞬间恢复
                    //Execute right slide. There is no follow up, instant recovery.
                    scrollRight(mCurItemLayout, 0 - mMoveWidth);
                    mMoveWidth = 0;
                }

                break;
            case MotionEvent.ACTION_UP://手抬起时
                int scrollX = mCurItemLayout.getScrollX();

                if (mHiddenWidth > mMoveWidth) {
                    int toX = (mHiddenWidth - mMoveWidth);
                    if (scrollX > mHiddenWidth / 2) {//超过一半长度时松开，则自动滑到左侧(When more than half of the length is released, it automatically slides to the left side.)
                        scrollLeft(mCurItemLayout, toX);
                        mMoveWidth = mHiddenWidth;
                    } else {//不到一半时松开，则恢复原状(When less than half of it is released, it will be restored to its original state.)
                        scrollRight(mCurItemLayout, 0 - mMoveWidth);
                        mMoveWidth = 0;
                    }
                }
                mLastItemLayout = mCurItemLayout;
                break;


        }
        return super.onTouchEvent(e);
    }


    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {

           // L.e(TAG, "computeScroll getCurrX ->" + mScroller.getCurrX());
            mCurItemLayout.scrollBy(mScroller.getCurrX(), 0);
            invalidate();
        }
    }

    /**
     * 向左滑动
     * Slide to the left
     */
    private void scrollLeft(View item, int scorllX){
       // L.e(TAG, " scroll left -> " + scorllX);
        item.scrollBy(scorllX, 0);
    }

    /**
     * 向右滑动
     * Slide to the right
     */
    private void scrollRight(View item, int scorllX){
       // L.e(TAG, " scroll right -> " + scorllX);
        item.scrollBy(scorllX, 0);
    }

    public interface OnRightClickListener{
        void onRightClick(int position, String id);
    }
}
