package com.afap.androidutils.adapter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.afap.androidutils.R;
import com.afap.androidutils.model.City;
import com.afap.widget.PinnedHeaderListView;
import com.afap.widget.QuickAlphabeticBar;

/**
 * 快速索引城市列表
 */
public class CityListAdapter extends BaseAdapter
		implements
			SectionIndexer,
		PinnedHeaderListView.PinnedHeaderAdapter,
			OnScrollListener {

	private List<City> mCitys;
	private Map<String, List<City>> mMap;
	private List<String> mSections;
	private List<Integer> mPositions;
	private LayoutInflater mInflater;
	private QuickAlphabeticBar mQuickAlphabeticBar;
	private int curFirstVisibleItem = -1;
	private int curSection = -1;

	public CityListAdapter(Context context, List<City> list,
			Map<String, List<City>> map, List<String> sections,
			List<Integer> positions, QuickAlphabeticBar bar) {
		mInflater = LayoutInflater.from(context);
		mCitys = list;
		mMap = map;
		mSections = sections;
		mPositions = positions;
		mQuickAlphabeticBar = bar;
	}

	@Override
	public int getCount() {
		return mCitys.size();
	}

	@Override
	public City getItem(int position) {
		int section = getSectionForPosition(position);
		return mMap.get(mSections.get(section)).get(
				position - getPositionForSection(section));
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.choosecity_pinned_header_listview_item, null);
		}
		int section = getSectionForPosition(position);
		TextView group = (TextView) convertView.findViewById(R.id.group_title);
		TextView tv_city = (TextView) convertView.findViewById(R.id.city_name);
		if (getPositionForSection(section) == position) {
			group.setVisibility(View.VISIBLE);
			group.setText(mSections.get(section));
		} else {
			group.setVisibility(View.GONE);
		}
		City item = mMap.get(mSections.get(section)).get(
				position - getPositionForSection(section));
		tv_city.setText(item.getName());
		return convertView;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (view instanceof PinnedHeaderListView) {
			((PinnedHeaderListView) view).configureHeaderView(firstVisibleItem);
		}
		// 改变快速索引条位置
		if (curFirstVisibleItem != firstVisibleItem) {
			curFirstVisibleItem = firstVisibleItem;
			int section = getSectionForPosition(firstVisibleItem);
			if (curSection != section) {
				curSection = section;
				String letter = (String) getSections()[curSection];
				mQuickAlphabeticBar.setPositionByLetter(letter);
			}
		}
	}

	@Override
	public int getPinnedHeaderState(int position) {
		int realPosition = position;
		if (realPosition <= 0 || position > getCount()) {
			return PINNED_HEADER_GONE;
		}
		int section = getSectionForPosition(realPosition);
		int nextSectionPosition = getPositionForSection(section + 1);
		if (nextSectionPosition != -1
				&& realPosition == nextSectionPosition - 1) {
			return PINNED_HEADER_PUSHED_UP;
		}
		return PINNED_HEADER_VISIBLE;
	}

	@Override
	public void configurePinnedHeader(View header, int position) {
		int realPosition = position;
		int section = getSectionForPosition(realPosition);
		String title = (String) getSections()[section];
		((TextView) header.findViewById(R.id.group_title)).setText(title);
	}

	@Override
	public Object[] getSections() {
		return mSections.toArray();
	}

	@Override
	public int getPositionForSection(int section) {
		if (section < 0 || section >= mPositions.size()) {
			return -1;
		}
		return mPositions.get(section);
	}

	@Override
	public int getSectionForPosition(int position) {
		if (position < 0 || position >= getCount()) {
			return -1;
		}
		int index = Arrays.binarySearch(mPositions.toArray(), position);
		return index >= 0 ? index : -index - 2;
	}
}