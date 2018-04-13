package com.afap.widget.pinnedheaderlistview;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.afap.utils.R;
import com.afap.widget.QuickAlphabetBar;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PinnedHeaderListViewAdapter<T> extends BaseAdapter implements SectionIndexer,
        PinnedHeaderListView.PinnedHeaderListener, AbsListView.OnScrollListener {

    private List<T> mData;
    private Map<String, List<T>> mMap;
    private List<Integer> mPositions; // 首字母位置集
    private List<String> mSections; // 首字母集
    private Map<String, Integer> mIndexMap; // 首字母对应的位置
    private PinnedHeaderListView mPinnedHeaderListView;
    private QuickAlphabetBar mQuickAlphabetBar;
    private int curFirstVisibleItem = -1;
    private int curSection = -1;

    public PinnedHeaderListViewAdapter(List<T> list, Map<String, List<T>> map, List<String> sections,
                                       List<Integer> positions, Map<String, Integer> indexeMap, PinnedHeaderListView
                                               listView, QuickAlphabetBar bar) {
        mData = list;
        mMap = map;
        mSections = sections;
        mPositions = positions;
        mIndexMap = indexeMap;
        mPinnedHeaderListView = listView;
        mQuickAlphabetBar = bar;
        if (mQuickAlphabetBar.getOnItemClickListener() == null) {
            mQuickAlphabetBar.setOnItemClickListener(new QuickAlphabetBar.OnLetterClickListener() {
                @Override
                public void onItemClick(String s) {
                    if (mIndexMap.get(s) != null) {
                        mPinnedHeaderListView.setSelection(
                                mIndexMap.get(s) + mPinnedHeaderListView.getHeaderViewsCount()
                        );
                    }
                }
            });
        }
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public T getItem(int position) {
        int section = getSectionForPosition(position);
        return mMap.get(mSections.get(section)).get(position - getPositionForSection(section));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 需要自己实现！！！
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.pinned_header_listview_item, null);
        }
        int section = getSectionForPosition(position);
        TextView group = (TextView) convertView.findViewById(R.id.pinned_header_listview_group);
        TextView tv_city = (TextView) convertView.findViewById(R.id.pinned_header_listview_child);
        if (getPositionForSection(section) == position) {
            group.setVisibility(View.VISIBLE);
            group.setText(String.valueOf(getSections()[section]));
        } else {
            group.setVisibility(View.GONE);
        }
        T item = getItem(position);
        tv_city.setText(item.toString()); // 按需要修改
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
                mQuickAlphabetBar.setPositionByLetter(letter);
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
        ((TextView) header.findViewById(R.id.pinned_header_listview_group)).setText(title);
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