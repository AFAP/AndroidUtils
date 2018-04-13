package com.afap.widget.pinnedheaderlistview;

import com.afap.utils.R;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * 索引固定listView
 */
public class PinnedHeaderListView extends ListView {

    // 固定的分组标签监听器
    public interface PinnedHeaderListener {
        int PINNED_HEADER_GONE = 0;
        int PINNED_HEADER_VISIBLE = 1;
        int PINNED_HEADER_PUSHED_UP = 2;

        int getPinnedHeaderState(int position);// 获取分组标签的状态：显示、消失、移动

        void configurePinnedHeader(View header, int position); // 可设置分组标签的相关信息
    }

    private PinnedHeaderListener mPinnedHeaderListener;
    private View mPinnedHeaderView; // 分组标签
    private int mHeaderViewWidth; // 分组标签宽度
    private int mHeaderViewHeight; // 分组标签高度

    public PinnedHeaderListView(Context context) {
        super(context);
        setDefaultPinnedHeaderView();
    }

    public PinnedHeaderListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDefaultPinnedHeaderView();
    }

    public PinnedHeaderListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setDefaultPinnedHeaderView();
    }

    // 注意顺序：onMeasure、onLayout、onDraw、dispatchDraw
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mPinnedHeaderView != null) {
            measureChild(mPinnedHeaderView, widthMeasureSpec, heightMeasureSpec);
            mHeaderViewWidth = mPinnedHeaderView.getMeasuredWidth();
            mHeaderViewHeight = mPinnedHeaderView.getMeasuredHeight();
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        drawChild(canvas, mPinnedHeaderView, getDrawingTime());
    }

    /**
     * 设置顶部分组标签view。默认会设置一个，也可以在具体的activity中设置其他布局。
     */
    private void setDefaultPinnedHeaderView() {
        setPinnedHeaderView(LayoutInflater.from(getContext()).inflate(
                R.layout.pinned_header_listview_group, this, false));
    }

    public void setPinnedHeaderView(View view) {
        mPinnedHeaderView = view;
        mPinnedHeaderView.setVisibility(View.GONE);
        // 只要调用了requestLayout,那么measure,onMeasure,layout,onLayout,draw,onDraw都会被调用
        requestLayout();
    }

    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        mPinnedHeaderListener = (PinnedHeaderListener) adapter;
    }

    /**
     * 设置顶部分组标签布局
     */
    public void configureHeaderView(int position) {
        if (mPinnedHeaderView == null) {
            return;
        }
        // 如果ListView通过addHeaderView添加了头部布局，显示头部布局时隐藏分组标签
        if (position < getHeaderViewsCount()) {
            mPinnedHeaderView.layout(0, 0, 0, 0);
            return;
        } else {
            position = position - getHeaderViewsCount();
            mPinnedHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
        }


        int state = mPinnedHeaderListener.getPinnedHeaderState(position);
        switch (state) {
            case PinnedHeaderListener.PINNED_HEADER_GONE: // 滑过当前分组
                mPinnedHeaderListener.configurePinnedHeader(mPinnedHeaderView, position);
                break;

            case PinnedHeaderListener.PINNED_HEADER_VISIBLE: // 滑动至下一个分组
                mPinnedHeaderListener.configurePinnedHeader(mPinnedHeaderView, position);
                if (mPinnedHeaderView.getTop() != 0) {
                    mPinnedHeaderView.layout(0, 0, mHeaderViewWidth,
                            mHeaderViewHeight);
                }
                break;

            case PinnedHeaderListener.PINNED_HEADER_PUSHED_UP: // 当前分组和上、下一分组更换
                View firstView = getChildAt(0);
                int bottom = 0;
                if (null != firstView) {
                    bottom = firstView.getBottom();
                }
                int y;
                if (bottom < mHeaderViewHeight) {
                    y = (bottom - mHeaderViewHeight); // 负值，可模拟出分组标签滑动的效果
                } else {
                    y = 0;
                }
                mPinnedHeaderListener.configurePinnedHeader(mPinnedHeaderView, position);
                if (mPinnedHeaderView.getTop() != y) {
                    mPinnedHeaderView.layout(0, y, mHeaderViewWidth, mHeaderViewHeight + y);
                }
                break;
        }
    }
}