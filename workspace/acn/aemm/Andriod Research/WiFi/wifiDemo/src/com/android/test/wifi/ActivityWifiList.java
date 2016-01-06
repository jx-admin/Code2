package com.android.test.wifi;

import android.app.Activity;
import android.os.Bundle;

public class ActivityWifiList extends Activity{
	private WifiListManager mWifiListmanager;
	private WifiManagerWrap mWifiManagerWrap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mWifiListmanager=new WifiListManager(this);
		setContentView(mWifiListmanager.createView(null));
		mWifiManagerWrap=new WifiManagerWrap(this);
		mWifiListmanager.setData(mWifiManagerWrap.getScanResults());
	}
}
