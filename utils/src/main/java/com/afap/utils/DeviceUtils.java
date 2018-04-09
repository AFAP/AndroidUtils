package com.afap.utils;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.media.AudioManager;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;

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


}
