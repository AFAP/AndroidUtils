package com.afap.androidutils.activity;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.afap.androidutils.R;
import com.afap.androidutils.adapter.CityListAdapter;
import com.afap.androidutils.database.DB;
import com.afap.androidutils.model.City;
import com.afap.widget.pinnedheaderlistview.PinnedHeaderListView;
import com.afap.widget.QuickAlphabetBar;

public class PinnedHeaderListViewActivity extends BaseActivity {

    private List<City> mCityList;

    /**
     * 以下为快速索引listview需要的
     */
    private CityListAdapter mCityListAdapter;
    private PinnedHeaderListView mPinnedHeaderListView;
    private QuickAlphabetBar mQuickAlphabeticBar; // 快速索引条
    private List<String> mSections; // 首字母集
    private Map<String, List<City>> mMap; // 根据首字母存放
    private List<Integer> mPositions; // 首字母位置集
    private Map<String, Integer> mIndexer; // 首字母对应的位置

    // 列表头部定位信息相关
    private DB mDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinned_header_listview);

        // 这个初始化数据一定要在前面
        initData();
        initView();

    }

    private void initView() {
        // 由于mPinnedHeaderListView关联使用该对象，需要前置定义
        mQuickAlphabeticBar = (QuickAlphabetBar) findViewById(R.id.choosecity_bladeview);


        mPinnedHeaderListView = (PinnedHeaderListView) findViewById(R.id.pinned_header_list);

        mCityListAdapter = new CityListAdapter(this, mCityList, mMap,
                mSections, mPositions, mIndexer, mPinnedHeaderListView, mQuickAlphabeticBar);
        mPinnedHeaderListView.setAdapter(mCityListAdapter);
        mPinnedHeaderListView.setOnScrollListener(mCityListAdapter);
        mPinnedHeaderListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // setCity(cityCode, cityName);
            }
        });

        mPinnedHeaderListView.addHeaderView(View.inflate(this, R.layout.header_listview, null));
        mPinnedHeaderListView.addHeaderView(View.inflate(this, R.layout.header_listview, null));

    }


    private void initData() {
        mDB = getDB();
        mCityList = mDB.getAllCitys();
        prepareDataForQuick();
    }

    /**
     * 为快速索引列表处理数据
     */
    private boolean prepareDataForQuick() {
        mSections = new ArrayList<String>();
        mMap = new HashMap<String, List<City>>();
        mPositions = new ArrayList<Integer>();
        mIndexer = new HashMap<String, Integer>();
        for (City city : mCityList) {
            String firstName = city.getFirstLetter(); // 全拼首字母
            if (mSections.contains(firstName)) { // 已存在该分组
                mMap.get(firstName).add(city);
            } else { // 不存在该分组，加入map
                mSections.add(firstName);
                List<City> list = new ArrayList<City>();
                list.add(city);
                mMap.put(firstName, list);
            }
        }
        Collections.sort(mSections);// 按照字母重新排序
        int position = 0;
        for (int i = 0; i < mSections.size(); i++) {
            mIndexer.put(mSections.get(i), position);// 存入map中，key为首字母字符串，value为首字母在listview中位置
            mPositions.add(position);// 每组第一个在listview中位置，存入list中
            position += mMap.get(mSections.get(i)).size();// 计算下一个首字母在listview的位置
            // 排序
            Collections.sort(mMap.get(mSections.get(i)),
                    new Comparator<City>() {
                        @Override
                        public int compare(City lhs, City rhs) {
                            return Collator.getInstance(Locale.getDefault())
                                    .compare(lhs.getPinyin(), rhs.getPinyin());
                        }
                    });
        }
        return true;
    }
}