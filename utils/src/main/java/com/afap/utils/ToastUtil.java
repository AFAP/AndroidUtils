package com.afap.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast统一管理
 */
public class ToastUtil {
	private static Toast toast;

	/**
	 * 短时间显示Toast
	 */
	public static void showShort(Context context, CharSequence message) {
		if (null == toast) {
			toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
		} else {
			toast.setDuration(Toast.LENGTH_SHORT);
			toast.setText(message);
		}
		toast.show();
	}

	public static void showShort(Context context, int message) {
		if (null == toast) {
			toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
		} else {
			toast.setDuration(Toast.LENGTH_SHORT);
			toast.setText(message);
		}
		toast.show();
	}
//
//	/**
//	 * 长时间显示Toast
//	 */
//	public static void showLong(Context context, CharSequence message) {
//		if (null == toast) {
//			toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
//		} else {
//			toast.setDuration(Toast.LENGTH_LONG);
//			toast.setText(message);
//		}
//		toast.show();
//	}
//
//	public static void showLong(Context context, int message) {
//		if (null == toast) {
//			toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
//		} else {
//			toast.setDuration(Toast.LENGTH_LONG);
//			toast.setText(message);
//		}
//		toast.show();
//	}
//
//	/**
//	 * 自定义显示Toast时间
//	 */
//	public static void show(Context context, CharSequence message, int duration) {
//		if (null == toast) {
//			toast = Toast.makeText(context, message, duration);
//		} else {
//			toast.setDuration(duration);
//			toast.setText(message);
//		}
//		toast.show();
//	}
//
//	public static void show(Context context, int message, int duration) {
//		if (null == toast) {
//			toast = Toast.makeText(context, message, duration);
//		} else {
//			toast.setDuration(duration);
//			toast.setText(message);
//		}
//		toast.show();
//	}

//	public static void hideToast() {
//		if (null != toast) {
//			toast.cancel();
//		}
//	}
}