package com.afap.androidutils.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public abstract class BaseActivity extends FragmentActivity {

	protected View titlebar;
	protected View titlebar_rsplit;
	protected ImageView titlebar_licon, titlebar_ricon, titlebar_moreicon;
	protected TextView titlebar_title, titlebar_rtext, tv_common_cart;
	protected RelativeLayout rl_common_cart;

	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
	}




}