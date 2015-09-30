package com.qin.ammp;

import java.util.List;

import wu.a.lib.app.ActivityManagerUtils;
import wu.a.lib.app.ProcessInfo;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class BrowseProcessInfoActivity extends Activity  implements OnItemClickListener{

	private static String TAG = "ProcessInfo";
	private static final int KILL_PORCESS = 1;
	private static final int SEARCH_RUNNING_APP = 2;

	private ActivityManager mActivityManager = null;
	// ProcessInfo Model类 用来保存所有进程信息
	private List<ProcessInfo> processInfoList = null;

	private ListView listviewProcess;
    private TextView tvTotalProcessNo ; 
	
    private String [] dialogItems  = new String[] {"杀死该进程","查看运行在该进程的应用程序"} ;
    
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.browse_process_list);

		listviewProcess = (ListView) findViewById(R.id.listviewProcess);
		listviewProcess.setOnItemClickListener(this);
		
		tvTotalProcessNo =(TextView)findViewById(R.id.tvTotalProcessNo);
		
		// 获得ActivityManager服务的对象
		mActivityManager = ActivityManagerUtils.getActivityManager(this);//(ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		// 获得系统进程信息
		processInfoList=ActivityManagerUtils.getRunningAppProcessInfo(mActivityManager);
		// 为ListView构建适配器对象
		BrowseProcessInfoAdapter mprocessInfoAdapter = new BrowseProcessInfoAdapter(this, processInfoList);
		listviewProcess.setAdapter(mprocessInfoAdapter);
	
		tvTotalProcessNo.setText("当前系统进程共有："+processInfoList.size());
	}
    //杀死该进程，并且刷新
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1,  final int position, long arg3) {
		// TODO Auto-generated method stub
	    new AlertDialog.Builder(this).setItems(dialogItems, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				//杀死进程
				if(which == 0) {
				   //杀死该进程，释放进程占用的空间
				   ActivityManagerUtils.killBackgroundProcesses(mActivityManager, processInfoList.get(position).getProcessName());
		           //刷新界面
				   processInfoList=ActivityManagerUtils.getRunningAppProcessInfo(mActivityManager);
				   BrowseProcessInfoAdapter mprocessInfoAdapter = new BrowseProcessInfoAdapter(
				   BrowseProcessInfoActivity.this, processInfoList);
				   listviewProcess.setAdapter(mprocessInfoAdapter);
				   tvTotalProcessNo.setText("当前系统进程共有："+processInfoList.size());
				}
				//查看运行在该进程的应用程序
				else if(which ==1){   
					ProcessInfo processInfo = processInfoList.get(position);
					
					Intent intent = new Intent() ;
				    intent.putExtra("EXTRA_PKGNAMELIST", processInfo.pkgnameList) ;
				    intent.putExtra("EXTRA_PROCESS_ID", processInfo.getPid());
				    intent.putExtra("EXTRA_PROCESS_NAME", processInfo.getProcessName());
				    intent.setClass(BrowseProcessInfoActivity.this, BrowseRunningAppActivity.class);
				    startActivity(intent);
				}
		   }
	    }).create().show() ;
	}
}
