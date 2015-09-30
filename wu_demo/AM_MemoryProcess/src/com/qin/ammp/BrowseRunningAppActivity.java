package com.qin.ammp;

import java.util.ArrayList;
import java.util.List;

import wu.a.lib.app.ActivityManagerUtils;
import wu.a.lib.app.PackageManagerUtils;
import wu.a.lib.app.RunningAppInfo;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

public class BrowseRunningAppActivity extends Activity {

	private static String TAG = "BrowseRunningAppActivity";

	private ListView listview = null;

	private List<RunningAppInfo> mlistAppInfo = null;
	private TextView tvInfo = null;

	private PackageManager pm;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.browse_app_list);

		listview = (ListView) findViewById(R.id.listviewApp);
		tvInfo = (TextView) findViewById(R.id.tvInfo);

		mlistAppInfo = new ArrayList<RunningAppInfo>();

		// 查询某一特定进程的所有应用程序
		Intent intent = getIntent();
		// 是否查询某一特定pid的应用程序
		int pid = intent.getIntExtra("EXTRA_PROCESS_ID", -1);

		if (pid != -1) {
			// 某一特定经常里所有正在运行的应用程序
			mlistAppInfo = querySpecailPIDRunningAppInfo(intent, pid);
		} else {
			// 查询所有正在运行的应用程序信息： 包括他们所在的进程id和进程名
			tvInfo.setText("所有正在运行的应用程序有-------");
			mlistAppInfo = ActivityManagerUtils.queryAllRunningAppInfo(PackageManagerUtils.getPackageManager(this),
					ActivityManagerUtils.getActivityManager(this));
		}
		BrowseRunningAppAdapter browseAppAdapter = new BrowseRunningAppAdapter(this, mlistAppInfo);
		listview.setAdapter(browseAppAdapter);
	}

	// 某一特定经常里所有正在运行的应用程序
	private List<RunningAppInfo> querySpecailPIDRunningAppInfo(Intent intent, int pid) {

		String[] pkgNameList = intent.getStringArrayExtra("EXTRA_PKGNAMELIST");
		String processName = intent.getStringExtra("EXTRA_PROCESS_NAME");

		// update ui
		tvInfo.setText("进程id为" + pid + " 运行的应用程序共有  :  " + pkgNameList.length);
		return ActivityManagerUtils.querySpecailPIDRunningAppInfo(pm, pkgNameList, processName, pid);
	}
}