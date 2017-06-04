package com.afap.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast统一管理
 */
public class ToastUtil {
    private static Toast toast;

    public static void init(Context context) {
        if (null == toast) {
            toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        }
    }

    public static void showShort(String message) {
        if (null != toast) {
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setText(message);
            toast.show();
        }
    }

    public static void showShort(int message) {
        if (null != toast) {
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setText(message);
            toast.show();
        }
    }

    public static void hideToast() {
        if (null != toast) {
            toast.cancel();
        }
    }
}