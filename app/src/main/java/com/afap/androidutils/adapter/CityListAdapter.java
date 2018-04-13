package com.afap.androidutils.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afap.androidutils.R;
import com.afap.androidutils.model.City;
import com.afap.widget.pinnedheaderlistview.PinnedHeaderListView;
import com.afap.widget.pinnedheaderlistview.PinnedHeaderListViewAdapter;
import com.afap.widget.QuickAlphabetBar;

/**
 * 快速索引城市列表
 */
public class CityListAdapter extends PinnedHeaderListViewAdapter<City> {

    private List<City> mCitys;
    private LayoutInflater mInflater;
    private QuickAlphabetBar mQuickAlphabeticBar;
    private int curFirstVisibleItem = -1;
    private int curSection = -1;

    public CityListAdapter(Context context, List<City> list,
                           Map<String, List<City>> map, List<String> sections,
                           List<Integer> positions, Map<String, Integer> indexeMap, PinnedHeaderListView listView, QuickAlphabetBar bar) {
        super(list, map, sections, positions,indexeMap, listView, bar);
        mInflater = LayoutInflater.from(context);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(
                    R.layout.pinned_header_listview_item, null);
        }
        int section = getSectionForPosition(position);
        TextView group = (TextView) convertView.findViewById(R.id.pinned_header_listview_group);
        TextView tv_city = (TextView) convertView.findViewById(R.id.pinned_header_listview_child);
        if (getPositionForSection(section) == position) {
            group.setVisibility(View.VISIBLE);
            group.setText( String.valueOf(getSections()[section]) );
        } else {
            group.setVisibility(View.GONE);
        }
        City item = getItem(position);
        tv_city.setText(item.getName());
        return convertView;
    }

}