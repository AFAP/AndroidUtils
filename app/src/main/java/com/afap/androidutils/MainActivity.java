package com.afap.androidutils;


import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.afap.utils.DeviceUtils;
import com.afap.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ToastUtil.init(this);

//        Intent intent = new Intent()
//                .setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//                .setData(Uri.fromParts("package",
//                        this.getPackageName(), null));
//        startActivity(intent);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_check_notification:
                boolean flag = NotificationManagerCompat.from(this).areNotificationsEnabled();

                Log.i("UTILS", "当前应用通知是否可用：" + flag);

                ((Button) v).setText(getString(R.string.test_check_notification) + " (" + flag + ")");
                break;

            case R.id.btn_flash_switch:

DeviceUtils.switchFlash();

                break;
        }
    }
}
