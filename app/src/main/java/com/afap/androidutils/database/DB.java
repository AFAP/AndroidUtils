package com.afap.androidutils.database;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import com.afap.androidutils.model.City;
import com.afap.utils.ContextUtil;

public class DB extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 8;
    private static final String DATABASE_NAME = "test.db";
    private static final String CREATE_TABLE = "CREATE table IF NOT EXISTS ";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS ";

    private final Context mContext;

    public DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
        Log.i("database", "new一个DB");
        getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("database", "DB数据库onCreate,创建部分表");
        db.execSQL(CityTable.CREATE_TABLE_SQL);
        initData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("database", "DB数据库版本升级,调用onUpgrade");
        db.execSQL(CityTable.DROP_TABLE_SQL);
        onCreate(db);
    }

    // 初始化静态资源信息
    private void initData(SQLiteDatabase db) {
        String cityStr = ContextUtil.getStringFromAsset(mContext, "citys.json", null);

        try {

            JSONArray citys = new JSONObject(cityStr).optJSONArray("RECORDS");

            db.beginTransaction();
            try {
                HanyuPinyinOutputFormat outputFormat = new HanyuPinyinOutputFormat();
                outputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

                // 插入城市
                for (int i = 0; i < citys.length(); i++) {
                    JSONObject c = citys.optJSONObject(i);
                    String city_name = c.optString("city_name");

                    String city_pinyin;
                    if (TextUtils.equals(city_name, "厦门")) {
                        city_pinyin = "xiamen";
                    } else if (TextUtils.equals(city_name, "重庆")) {
                        city_pinyin = "chongqing";
                    } else {
                        city_pinyin = PinyinHelper.toHanyuPinyinString(city_name, outputFormat, "");
                    }

                    ContentValues values = new ContentValues();
                    values.put(CityTable.CITYCODE, c.optString("city_code"));
                    values.put(CityTable.CITYNAME, city_name);
                    values.put(CityTable.CITYPINYIN, city_pinyin);
                    values.put(CityTable.PROVINCECODE, c.optString("province_code"));
                    db.insert(CityTable.TABLE_NAME, null, values);
                }
                db.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("database", "初始化静态资源出错啦");
            } finally {
                db.endTransaction();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("database", "初始化静态资源结束");
    }


    public interface CityTable {
        String CITYCODE = "c_code";
        String CITYNAME = "c_name";
        String CITYPINYIN = "c_pinyin";
        String PROVINCECODE = "p_code";

        String TABLE_NAME = "citys";
        String DROP_TABLE_SQL = DROP_TABLE + TABLE_NAME;

        String CREATE_TABLE_SQL = CREATE_TABLE + TABLE_NAME + "(" + CITYCODE + " TEXT," + CITYNAME + " TEXT,"
                + CITYPINYIN + " TEXT," + PROVINCECODE + " TEXT" + ")";
    }

    // 获得所有市
    public List<City> getAllCitys() {
        List<City> list = new ArrayList<>();
        SQLiteDatabase sqlitedb = getWritableDatabase();
        Cursor c = sqlitedb.query(CityTable.TABLE_NAME, null, null, null, null, null, null, null);
        while (c.moveToNext()) {
            list.add(City.parseFromCursor(c));
        }
        c.close();
        return list;
    }


}