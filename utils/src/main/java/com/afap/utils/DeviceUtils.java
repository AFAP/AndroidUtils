package com.afap.utils;

import android.Manifest;
import android.content.Context;
import android.hardware.Camera;
import android.util.Log;

public class DeviceUtils {

    public static void switchFlash() {



        // Assume thisActivity is the current activity
        int permissionCheck = ContextCompat.checkSelfPermission(null, Manifest.permission.CAMERA);

//        其中，如果已经注册权限，此方法会返回一个int值PackageManager.PERMISSION_GRANTED,此值为1.如果没有申请权限，将返回PackageManager.PERMISSION_DENIED，值为-1.



        try {
            Log.e("~~~~~", "open2: ```````````````````````````````````````````````````````````" );
            Camera mCamera = Camera.open();
//            mCamera = Camera.open(cameraPosition);
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            mCamera.setParameters(parameters);
            mCamera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
