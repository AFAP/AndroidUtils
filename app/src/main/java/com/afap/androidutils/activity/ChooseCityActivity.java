package com.afap.androidutils.activity;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.json.JSONObject;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;

import com.afap.androidutils.R;
import com.afap.androidutils.adapter.CityListAdapter;
import com.afap.androidutils.model.City;
import com.afap.widget.PinnedHeaderListView;
import com.afap.widget.QuickAlphabeticBar;

public class ChooseCityActivity extends BaseActivity {

	private List<City> mCityList;

	/** 以下为快速索引listview需要的 */
	private CityListAdapter mCityListAdapter;
	private PinnedHeaderListView mPinnedHeaderListView;
	private QuickAlphabeticBar mQuickAlphabeticBar; // 快速索引条
	private List<String> mSections; // 首字母集
	private Map<String, List<City>> mMap; // 根据首字母存放
	private List<Integer> mPositions; // 首字母位置集
	private Map<String, Integer> mIndexer; // 首字母对应的位置
	private Boolean isLocSuccess = false;
	private City locCity;// 定位获取的城市

	// 列表头部定位信息相关
	private ProgressBar p_loc_ing;
	private ImageView img_loc_fail;
	private TextView tv_loc_city, tv_loc_tip;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choosecity);

		// 这个初始化数据一定要在前面
		initData();
		initView();

	}

	private void initView() {
		// 由于mPinnedHeaderListView关联使用该对象，需要前置定义
		mQuickAlphabeticBar = (QuickAlphabeticBar) findViewById(R.id.choosecity_bladeview);
		mQuickAlphabeticBar.setOnItemClickListener(new OnLetterClickListener() {
			@Override
			public void onItemClick(String s) {
				if (mIndexer.get(s) != null) {
					// 此处+1就是为了消除顶部定位布局的影响
					mPinnedHeaderListView.setSelection(mIndexer.get(s) + 1);
				}
			}
		});

		mPinnedHeaderListView = (PinnedHeaderListView) findViewById(R.id.choosecity_pinned_header_list);
		View headerView = getLayoutInflater().inflate(
				R.layout.choosecity_list_headerview, mPinnedHeaderListView,
				false);
		mPinnedHeaderListView.addHeaderView(headerView);
		mCityListAdapter = new CityListAdapter(this, mCityList, mMap,
				mSections, mPositions, mQuickAlphabeticBar);
		mPinnedHeaderListView.setAdapter(mCityListAdapter);
		mPinnedHeaderListView.setOnScrollListener(mCityListAdapter);
		mPinnedHeaderListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				String cityCode = "";
				String cityName = "";
				// 点击定位信息
				if (position == 0) {
					// 定位成功且根据map表找到了对应的城市
					if (isLocSuccess) {
						cityCode = locCity.getCode();
						cityName = locCity.getName();
					} else {
						getLoc();
						return;
					}
				} else {
					City city = mCityListAdapter.getItem(position - 1);
					cityCode = city.getCode();
					cityName = city.getName();
				}
				setCity(cityCode, cityName);
			}
		});
		p_loc_ing = (ProgressBar) headerView
				.findViewById(R.id.choosecity_location_progressBar);
		img_loc_fail = (ImageView) headerView
				.findViewById(R.id.choosecity_location_fail);
		tv_loc_city = (TextView) headerView
				.findViewById(R.id.choosecity_location_city);
		tv_loc_tip = (TextView) headerView
				.findViewById(R.id.choosecity_location_tip);
	}

	/**
	 * 设置所在城市
	 * 
	 * @param cityCode
	 * @param cityName
	 */
	private void setCity(final String cityCode, String cityName) {

		SPUtils spu = new SPUtils(this);
		spu.setCityCode(cityCode);
		spu.setCityName(cityName);
		// 当前用户已登录，则调用设置登录城市接口。未登录，则先记录在本地
		if (getApp().getCurrentUser() != null
				&& getApp().getCurrentUser().isAuthed()) {
			ProgressUtils.show("正在设置", this, false);
			String url = "https://{host}:{port}/kidswantwebservices/v2/kidswant/users/{userId}/setlogincity";
			url = url.replace("{userId}", getApp().getCurrentUser().getUid());

			HashMap<String, String> params = new HashMap<String, String>();
			params.put("cityCode", cityCode);
			TransitHybrisRequest request = new TransitHybrisRequest(
					Method.POST, url, params, true,
					new Response.Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							ProgressUtils.dismiss();

							UserWsDTO mUser = getApp().getCurrentUser();
							mUser.setAreaCode(response.optString("areaCode"));
							mUser.setCityCode(cityCode);
							getApp().setCurrentUser(mUser);
							if (mOnCityChangeListener != null) {
								mOnCityChangeListener.refreshCity();
							}
							T.showShort(getApp(), "设置成功");
							setResult(RESULT_OK);
							finish();
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							ProgressUtils.dismiss();
							StatusUtils.handleHybrisError(true, error,
									ChooseCityActivity.this);
						}
					});
			getApp().addToRequestQueue(request, this);
		} else {
			if (mOnCityChangeListener != null) {
				mOnCityChangeListener.refreshCity();
			}
			finish();
		}
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
			mPositions.add(position);// 每组第一个员工在listview中位置，存入list中
			position += mMap.get(mSections.get(i)).size();// 计算下一个首字母在listview的位置
			// 联系人排序
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