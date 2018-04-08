package com.afap.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.TypedValue;


import java.io.InputStream;

public class ContextUtil {

    /**
     * 获取当前应用版本号
     *
     * @param context 上下文
     * @return 版本号，发生异常时返回0
     */
    public static int getAppVersionCode(Context context) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            return 0;
        }
        return packageInfo.versionCode;
    }

    /**
     * 获取当前应用版本
     *
     * @param context 上下文
     * @return 版本名称，发生异常时返回null
     */
    public static String getAppVersionName(Context context) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
        return packageInfo.versionName;
    }

    /**
     * DP转PX
     *
     * @param context 上下文
     * @param dp      dp数值
     * @return 转化后的PX数值
     */
    public static float dp2px(Context context, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    /**
     * SP转PX
     *
     * @param context 上下文
     * @param sp      sp数值
     * @return 转化后的PX数值
     */
    public static float sp2px(Context context, float sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }


    /**
     * 从Assets资源文件获取String
     *
     * @param context  上下文内容
     * @param filePath assets文件夹中文件的名称
     * @param charset  字符编码，可传""或者null，默认使用UTF-8
     */
    public static String getStringFromAsset(Context context, String filePath, String charset) {
        // 未设置字符编码，默认使用UTF-8
        if (TextUtils.isEmpty(charset)) {
            charset = "UTF-8";
        }

        String result = "";
        try {
            InputStream in = context.getResources().getAssets().open(filePath); // 从Assets中的文件获取输入流
            int length = in.available(); // 获取文件的字节数
            byte[] buffer = new byte[length]; // 创建byte数组
            in.read(buffer); // 将文件中的数据读取到byte数组中
            result = new String(buffer, charset);
        } catch (Exception e) {
            e.printStackTrace(); // 捕获异常并打印
        }
        return result;
    }

    /**
     * 比较两个字符串类型的版本号
     *
     * @param version1 ;
     * @param version2 ;
     *                 version1更加新时返回1，相同返回0，否则返回-1 ;
     */
    public static int compareVersion(String version1, String version2) {
        if (TextUtils.equals(version1, version2)) {
            return 0;
        }
        String[] versionArray1 = version1.split("\\.");
        String[] versionArray2 = version2.split("\\.");
        int ret = 0;
        if (versionArray1 != null && versionArray2 != null) {
            int num = versionArray1.length < versionArray2.length ? versionArray1.length : versionArray2.length;
            for (int i = 0; i < num; i++) {
                String str1 = versionArray1[i];
                String str2 = versionArray2[i];
                if (str1 != null && str2 != null) {
                    int num1 = parseToInt(str1, 0);
                    int num2 = parseToInt(str2, 0);
                    if (num1 == num2) {
                        ret = 0;
                        continue;
                    } else if (num1 > num2) {
                        ret = 1;
                        return ret;
                    } else {
                        ret = -1;
                        return ret;
                    }
                }
            }
        }
        if (ret == 0 && (versionArray1.length != versionArray2.length)) {
            if (versionArray1.length > versionArray2.length) {
                ret = 1;
            } else if (versionArray1.length < versionArray2.length) {
                ret = -1;
            }
        }
        return ret;
    }

    /**
     * 转换为整型数据
     */
    private static int parseToInt(String s, int defaultvalue) {
        if (s == null || s.length() == 0) {
            return defaultvalue;
        }

        try {
            if (s.indexOf(".") != -1) {
                return (int) Float.parseFloat(s);
            } else {
                return Integer.parseInt(s);
            }
        } catch (Exception e) {
            return defaultvalue;
        }
    }



}