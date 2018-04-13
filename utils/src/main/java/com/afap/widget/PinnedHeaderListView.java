package com.afap.widget;

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

	public interface PinnedHeaderAdapter {
		public static final int PINNED_HEADER_GONE = 0;
		public static final int PINNED_HEADER_VISIBLE = 1;
		public static final int PINNED_HEADER_PUSHED_UP = 2;

		int getPinnedHeaderState(int position);// 获取分组标签的状态：显示、消失、移动

		void configurePinnedHeader(View header, int position); // 可设置分组标签的相关信息
	}

	private PinnedHeaderAdapter mAdapter;
	private View mHeaderView; // 分组标签
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

	public PinnedHeaderListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		setDefaultPinnedHeaderView();
	}

	// 注意顺序：onMeasure、onLayout、onDraw、dispatchDraw
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (mHeaderView != null) {
			measureChild(mHeaderView, widthMeasureSpec, heightMeasureSpec);
			mHeaderViewWidth = mHeaderView.getMeasuredWidth();
			mHeaderViewHeight = mHeaderView.getMeasuredHeight();
		}
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		// if (mHeaderView != null) {
		// mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
		// }
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		drawChild(canvas, mHeaderView, getDrawingTime());
	}

	/**
	 * 设置顶部分组标签view。默认会设置一个，也可以在具体的activity中设置其他布局。
	 */
	private void setDefaultPinnedHeaderView() {
		setPinnedHeaderView(LayoutInflater.from(getContext()).inflate(
				R.layout.choosecity_pinned_header_listview_group, this, false));
	}

	public void setPinnedHeaderView(View view) {
		mHeaderView = view;
		mHeaderView.setVisibility(View.GONE);
		// 只要调用了requestlayout,
		// 那么measure,onMeasure,layout,onlayout,draw,onDraw都会被调用
		requestLayout();
	}

	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);
		mAdapter = (PinnedHeaderAdapter) adapter;
	}

	/**
	 * 设置顶部分组标签布局
	 */
	public void configureHeaderView(int position) {
		if (mHeaderView == null) {
			return;
		}
		// 由于添加了头部的定位布局，故如下处理
		if (position == 0) {
			mHeaderView.layout(0, 0, 0, 0);
			return;
		} else {
			position = position - 1;
			mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
		}
		int state = mAdapter.getPinnedHeaderState(position);
		switch (state) {
			case PinnedHeaderAdapter.PINNED_HEADER_GONE : // 滑过当前分组
				mAdapter.configurePinnedHeader(mHeaderView, position);
				break;

			case PinnedHeaderAdapter.PINNED_HEADER_VISIBLE : // 滑动至下一个分组
				mAdapter.configurePinnedHeader(mHeaderView, position);
				if (mHeaderView.getTop() != 0) {
					mHeaderView.layout(0, 0, mHeaderViewWidth,
							mHeaderViewHeight);
				}
				break;

			case PinnedHeaderAdapter.PINNED_HEADER_PUSHED_UP : // 当前分组和上、下一分组更换
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
				mAdapter.configurePinnedHeader(mHeaderView, position);
				if (mHeaderView.getTop() != y) {
					mHeaderView.layout(0, y, mHeaderViewWidth,
							mHeaderViewHeight + y);
				}
				break;
		}
	}
}