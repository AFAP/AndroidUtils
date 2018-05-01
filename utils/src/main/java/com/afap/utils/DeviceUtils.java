package com.afap.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.media.AudioManager;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class DeviceUtils {
    public static final String TAG = "DeviceUtils";
    public static boolean isTorched = false;


    /**
     * 开启/关闭闪光灯
     *
     * @param context 上下文
     */
    public static void switchFlash(Context context) {
        int flag = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        if (flag != PackageManager.PERMISSION_GRANTED) {
            Log.w(TAG, "打开闪光灯失败 -- Camera权限未获取!!!");
            return;
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            CameraManager manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
            try {
                String cameraId = manager.getCameraIdList()[0];
                manager.setTorchMode(cameraId, !isTorched);
                manager.getCameraCharacteristics(cameraId).get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                manager.registerTorchCallback(new CameraManager.TorchCallback() {
                    @Override
                    public void onTorchModeUnavailable(@NonNull String cameraId) {
                        super.onTorchModeUnavailable(cameraId);
                    }

                    @Override
                    public void onTorchModeChanged(@NonNull String cameraId, boolean enabled) {
                        super.onTorchModeChanged(cameraId, enabled);
                        isTorched = enabled;
                    }
                }, new Handler());
            } catch (CameraAccessException e) {
                Log.e(TAG, "打开闪光灯失败 -- 权限异常");
                e.printStackTrace();
            }
        } else {
            Camera mCamera = Camera.open();
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setFlashMode(isTorched ? Camera.Parameters.FLASH_MODE_OFF : Camera.Parameters.FLASH_MODE_ON);
            isTorched = !isTorched;
            mCamera.setParameters(parameters);
            mCamera.startPreview();
        }
    }


    public static void volumeUp(Context context) {
        volumeUp(context, AudioManager.STREAM_SYSTEM);
    }

    public static void volumeUp(Context context, int streamType) {
        volumeControl(context, "adjust", streamType, AudioManager.ADJUST_RAISE, 0);
    }

    public static void volumeDown(Context context) {
        volumeDown(context, AudioManager.STREAM_SYSTEM);
    }

    public static void volumeDown(Context context, int streamType) {
        volumeControl(context, "adjust", streamType, AudioManager.ADJUST_LOWER, 0);
    }

    /**
     * 音量控制
     *
     * @param context    上下文
     * @param type       adjust/set 步进+/-，设置指定值
     * @param streamType 系统/铃声/媒体
     * @param direction  音量增减方向
     * @param volume     音量值
     */
    public static void volumeControl(Context context, String type, int streamType, int direction, int volume) {
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (TextUtils.equals("adjust", type)) {
            am.adjustStreamVolume(streamType, direction, AudioManager.FLAG_SHOW_UI);
        } else {
            am.setStreamVolume(streamType, volume, AudioManager.FLAG_SHOW_UI);
        }
    }

    /**
     * 震动控制
     *
     * @param context 上下文
     */
    public static void vibrator(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        long[] p = {100, 3000};
        vibrator.vibrate(p, -1);
    }

    private static TextToSpeech textToSpeech = null;

    public static TextToSpeech getTextToSpeech(final Context context) {
        if (textToSpeech == null) {
            textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {

                @Override
                public void onInit(int status) {
                    if (status == TextToSpeech.SUCCESS) {
                        int result = textToSpeech.setLanguage(Locale.CHINA);
                    }
                }
            });
        }

        return textToSpeech;
    }


    /**
     * @param context    上下文
     * @param text       待朗读的文字，必须。
     * @param locale     国家地区，可为空。
     * @param speechRate 语速，可为空。1:正常，0.5:半速，2:倍速
     * @param pitch      音调，可为空。1:正常，低于1或者高于1，降低或增高音调
     * @param queueMode  任务队列策略，可为空。
     *                   1. TextToSpeech.QUEUE_FLUSH,0: 中断当前实例正在运行的任务，转而执行新的语音任务
     *                   2、TextToSpeech.QUEUE_ADD,1:添加至队列最后，待前面任务执行完毕后会执行
     * @param params     额外的参数，可为空.
     *                   支持以下参数:
     *                   1.TextToSpeech.Engine.KEY_PARAM_STREAM
     *                   2.TextToSpeech.Engine.KEY_PARAM_VOLUME
     *                   3.TextToSpeech.Engine.KEY_PARAM_PAN
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void speak(final Context context, final CharSequence text, final Locale locale, final float
            speechRate, final float pitch, final int queueMode, final Bundle params) {
        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                Log.w(TAG, "-------------->onInit");
                if (status == TextToSpeech.SUCCESS) {
                    if (locale != null) {
                        if (textToSpeech.isLanguageAvailable(locale) == TextToSpeech.LANG_AVAILABLE ||
                                textToSpeech.isLanguageAvailable(locale) == TextToSpeech.LANG_COUNTRY_AVAILABLE ||
                                textToSpeech.isLanguageAvailable(locale) == TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE) {
                            textToSpeech.setLanguage(locale);
                        } else {
                            Log.w(TAG, "不支持该语言：" + locale.getDisplayName());
                        }
                    }
                    textToSpeech.setPitch(pitch);
                    textToSpeech.setSpeechRate(speechRate);

                    int result = textToSpeech.speak(text, queueMode, params, null);
                    Log.i(TAG, "播放声音结果：" + result);
                }
            }
        });
    }

    /**
     * 打开手机浏览器并打开指定网页
     *
     * @param context 上下文
     * @param url     网站地址，可为空，当为空时使用“http://”。
     */
    public static void openBrowser(Context context, String url) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        if (TextUtils.isEmpty(url)) {
            url = "http://";
        }
        Uri uri = Uri.parse(url);
        intent.setData(uri);
        context.startActivity(intent);
    }


    /**
     * 获取手机连接过的wifi列表
     *
     * @param context 上下文
     * @return Wi-Fi 记录列表
     */
    public static List<WifiConfiguration> getHistoryWifis(Context context) {
        WifiManager manager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        List<WifiConfiguration> list = manager != null ? manager.getConfiguredNetworks() : null;
        if (list == null) {
            return new ArrayList<>();
        }
        return list;
    }

}
