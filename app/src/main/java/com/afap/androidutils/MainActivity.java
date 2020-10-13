package com.afap.androidutils;


import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import androidx.core.app.NotificationManagerCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.afap.androidutils.activity.PinnedHeaderListViewActivity;
import com.afap.utils.DeviceUtils;
import com.afap.utils.ToastUtil;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ToastUtil.init(this);


        findViewById(R.id.btn_goto_settings).setOnClickListener(this);
        findViewById(R.id.btn_check_notification).setOnClickListener(this);
        findViewById(R.id.btn_flash_switch).setOnClickListener(this);
        findViewById(R.id.btn_volume_up).setOnClickListener(this);

        findViewById(R.id.btn_volume_down).setOnClickListener(this);
        findViewById(R.id.btn_vibrate).setOnClickListener(this);

        findViewById(R.id.btn_pinned_list).setOnClickListener(this);
        findViewById(R.id.btn_text_to_speech).setOnClickListener(this);
        findViewById(R.id.btn_open_browser).setOnClickListener(this);
        findViewById(R.id.btn_dial_phone).setOnClickListener(this);
        findViewById(R.id.btn_call_phone).setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_goto_settings:
                Intent intent = new Intent()
                        .setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        .setData(Uri.fromParts("package",
                                this.getPackageName(), null));
                startActivity(intent);
                break;
            case R.id.btn_check_notification:
                boolean flag = NotificationManagerCompat.from(this).areNotificationsEnabled();

                Log.i("UTILS", "当前应用通知是否可用：" + flag);

                ((Button) v).setText(getString(R.string.test_check_notification) + " (" + flag + ")");
                break;

            case R.id.btn_flash_switch:

                DeviceUtils.switchFlash(this);

                break;
            case R.id.btn_volume_up:

                DeviceUtils.volumeUp(this);

                break;
            case R.id.btn_volume_down:

                DeviceUtils.volumeDown(this);

                break;
            case R.id.btn_vibrate:

                DeviceUtils.vibrator(this);

                break;
            case R.id.btn_pinned_list:

                Intent intent1 = new Intent(this, PinnedHeaderListViewActivity.class);
                startActivity(intent1);

                break;
            case R.id.btn_text_to_speech:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    DeviceUtils.speak(this, "hello every", Locale.US, 1, 1, 0, null);
                } else {
                    Log.w("MainActivity", "最低只支持到21");
                }

                break;
            case R.id.btn_open_browser:
                    DeviceUtils.openBrowser(this,null);

                break;
            case R.id.btn_dial_phone:
                DeviceUtils.dialPhone(this,"10086");

                break;
            case R.id.btn_call_phone:
                DeviceUtils.callPhone(this,"10086");

                break;

        }
    }
}
