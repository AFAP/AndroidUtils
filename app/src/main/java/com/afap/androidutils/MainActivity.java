package com.afap.androidutils;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.afap.utils.ContextUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        TextView textView = (TextView) findViewById(R.id.textView);
//
//        textView.setText(ContextUtil.getStringFromAsset(this,"dddd.txt",null));
    }
}
