package com.android.accenture.aemm.express;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

import com.android.accenture.aemm.express.GridAdapter.GridHolder;
import com.android.accenture.aemm.express.app.ScrollLayout;
import com.android.accenture.aemm.express.updataservice.DeviceAdminLocalSetup;
import com.android.accenture.aemm.express.updataservice.ListenerService;
import com.android.accenture.aemm.express.updataservice.MessageNotification;
import com.android.accenture.aemm.express.updataservice.configPreference;
import com.android.accenture.aemm.express.updataservice.ProfileContent.ApkProfileContent;

/**
 * @author junxu.wang
 * 
**/
public class Main extends Activity implements OnItemSelectedListener,
		OnClickListener, OnItemClickListener {

	public static void startHall(Context c) {
		c.startActivity(new Intent(c, Main.class));
	}
	
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		switch (keyCode) {
//
//		case KeyEvent.KEYCODE_BACK: {
//			if (true == ApkLoader.apkLoaderIsRuning()) {
//				//Toast.makeText(Main.this, getString(R.string.checkupdate), 100);
//				HallMessageManager.sendMessage(Main.this, getString(R.string.stopexit), HallMessagedb.RESULTMSG);
//				return true;
//			}
//		}
//		}
//		return super.onKeyDown(keyCode, event);
//	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (false == enableStart()) {
			this.stopService(new Intent(this, ListenerService.class));
			this.finish();
		}
		
		setContentView(R.layout.hall_linear);
//		startActivity(new Intent(this,DBtest.class));
		mHall = this;
		mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		initView();
		initData();
		
		message_vf.onCreate();

		apkAdapterSl.updataNewAppMessage();
//		registerReceiver();
		// startActivity(new Intent(this,SimplePOST.class));
		// ShowUpdata("更新","是否更新？","http://www.baiud.com",true);
		// startActivity(new Intent(this,Login.class));
		// Login.showLogin(this, "aa", "bb", "消息测试哈哈", false);
		// MDialog.showMessage(this,"mmmmm",false);
		// HttpUtils http=new HttpUtils();
		// http.http();

		// applist测试
		// new AppList(this).onDemoAppList();
		// 上传测试
		/*
		 * Upload upload=new Upload("http://aemm.imolife.com/android.aspx"); try
		 * { upload.connect(new FileInputStream(new File("/sdcard/app.xml"))); }
		 * catch (FileNotFoundException e) { e.printStackTrace(); }
		 */
		// 硬件参数上传
		// MoshinInformation m=new MoshinInformation(this);
		// m.onStart();
		// m.write("/sdcard/moshinInformation.xml");
		// m.onDestroy();

		// aemm插件测试
		// AppListener al=new AppListener();
		// al.receiver(this);

		// appListenerManager test
		// AppListenerManager am=new AppListenerManager();
		// Log.v(LOGCAT,"am:"+am.getEvent("com.android.appnew", "id"));

		// LocationInfo location=new LocationInfo(this);
		// location.init();
		// dialog
		// CustomDialog.createDialog(this,"这是对话框测试");
		if(debugView){
			message_vf.test();
		}
//		ServiceDia.test(this);
	}

	private void initDebugButton() {
		if (debug) {

			add_app_btn = (Button) findViewById(R.id.add_app_btn);
			del_app_btn = (Button) findViewById(R.id.del_app_btn);
			unable_app_btn = (Button) findViewById(R.id.unable_app_btn);
			add_apk_btn = (Button) findViewById(R.id.add_apk_btn);
			del_apk_btn = (Button) findViewById(R.id.del_apk_btn);

			add_app_btn.setOnClickListener(this);
			del_app_btn.setOnClickListener(this);
			unable_app_btn.setOnClickListener(this);
			add_apk_btn.setOnClickListener(this);
			del_apk_btn.setOnClickListener(this);

			add_app_btn.setVisibility(View.VISIBLE);
			del_app_btn.setVisibility(View.VISIBLE);
			unable_app_btn.setVisibility(View.VISIBLE);
			add_apk_btn.setVisibility(View.VISIBLE);
			del_apk_btn.setVisibility(View.VISIBLE);
		}
	}

//	private boolean adjustLayout()
//	{
//		try {
//			InputStream is = getAssets().open("layout.xml");
//			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//			DocumentBuilder builder;
//			builder = factory.newDocumentBuilder();
//			Document dom;
//			dom = builder.parse(is);
//			Element root = dom.getDocumentElement();
//			int w = this.getWindowManager().getDefaultDisplay().getWidth();
//			int h = this.getWindowManager().getDefaultDisplay().getHeight();
//	
//			if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//				String tag = "config_" + String.valueOf(w) + "_" + String.valueOf(h) + "_lan";
//				NodeList configNodes = root.getElementsByTagName(tag);
//				if(configNodes.getLength() == 0)
//					return false;
//				Element configNode = (Element)configNodes.item(0);    
//
//				int install_row = 2;
//				int install_col = 6;
//				int uninstall_row = 1;
//				int uninstall_col = 2;
//
//				LinearLayout layout;
//				LinearLayout.LayoutParams params;
//				layout = (LinearLayout)findViewById(R.id.linear_sl);
//				params = (LinearLayout.LayoutParams)layout.getLayoutParams();
//				params.height = Integer.parseInt(configNode.getAttribute("uninstall_area_h"));
//				layout.setLayoutParams(params);
//				layout.setBackgroundColor(0x55ff0000);
//
//				layout = (LinearLayout)findViewById(R.id.uninstalled_app_aera);
//				params = (LinearLayout.LayoutParams)layout.getLayoutParams();
//				params.height = Integer.parseInt(configNode.getAttribute("install_area_h"));
//				layout.setLayoutParams(params);
//				layout.setBackgroundColor(0x5500ffff);
//			} else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//				String tag = "config_" + String.valueOf(w) + "_" + String.valueOf(h) + "_por";
//				NodeList configNodes = root.getElementsByTagName(tag);
//				if(configNodes.getLength() == 0)
//					return false;
//				Element configNode = (Element)configNodes.item(0);    
//
//				int install_row = 2;
//				int install_col = 6;
//				int uninstall_row = 1;
//				int uninstall_col = 2;
//
//				LinearLayout layout;
//				LinearLayout.LayoutParams params;
//				layout = (LinearLayout)findViewById(R.id.linear_sl);
//				params = (LinearLayout.LayoutParams)layout.getLayoutParams();
//				params.height = Integer.parseInt(configNode.getAttribute("uninstall_area_h"));
//				layout.setLayoutParams(params);
//				layout = (LinearLayout)findViewById(R.id.installed_index_linear);
//				params = (LinearLayout.LayoutParams)layout.getLayoutParams();
//				params.height = Integer.parseInt(configNode.getAttribute("install_area_h"));
//				layout.setLayoutParams(params);
//			}
//			return true;
//		} catch (IOException e) {
//		} catch (ParserConfigurationException e) {
//		} catch (SAXException e) {
//		}
//		return false;
//	}

	private void initView() {
		initButton();
		initDebugButton();
		hall_linear = (LinearLayout) findViewById(R.id.hall_linear);
		

		if (isScrollLayout) {
			installed_sl = (ScrollLayout) findViewById(R.id.installed_sl);
			uninstalled_sl = (ScrollLayout) findViewById(R.id.uninstalled_sl);
			installed_sl.setVisibility(View.VISIBLE);
			uninstalled_sl.setVisibility(View.VISIBLE);
			installed_sl.setPageHandler(APP_PAGE, handler);
			uninstalled_sl.setPageHandler(APK_PAGE, handler);
			if (debugView) {
				installed_sl.setBackgroundColor(0x5500ff00);
				uninstalled_sl.setBackgroundColor(0x550000ff);
			}
		} else {
			// installed
			installed_gl = (Gallery) findViewById(R.id.installed_gl);
			// uninstalled
			uninstalled_gl = (Gallery) findViewById(R.id.uninstalled_gl);
			installed_gl.setVisibility(View.VISIBLE);
			uninstalled_gl.setVisibility(View.VISIBLE);

			installed_gl.setOnItemSelectedListener(this);
			installed_gl.setOnItemClickListener(this);

			uninstalled_gl.setOnItemSelectedListener(this);
		}

		installed_index_linear = (LinearLayout) findViewById(R.id.installed_index_linear);
		uninstalled_index_linear = (LinearLayout) findViewById(R.id.uninstalled_index_linear);
		if (uninstalledPager == null) {
			installedPager=new PageIndexManager(this);
			uninstalledPager=new PageIndexManager(this);
		}
		installedPager.setLinearView(installed_index_linear);
		uninstalledPager.setLinearView(uninstalled_index_linear);

		if (message_vf != null) {
			HallMessageManager message = (HallMessageManager) findViewById(R.id.message_flipper);
			int count = message_vf.getChildCount();
			View v;
			for (int i = 0; i < count; i++) {
				v = message_vf.getChildAt(0);
				message_vf.removeViewAt(0);
				message.addView(v);
			}
			message_vf.onDestroyed();
//			message.mReceiver=message_vf.mReceiver;
			message_vf = message;
		} else {
			message_vf = (HallMessageManager) findViewById(R.id.message_flipper);
		}
		message_vf.onCreate();
		message_vf.startFlipping();
		if (debugView) {
			installed_index_linear.setBackgroundColor(0x55ff0000);
			uninstalled_index_linear.setBackgroundColor(0x5500ffff);
			message_vf.setBackgroundColor(0x55555500);
		}
	}
	
	private boolean enableStart() {
		boolean rlt = true;
		boolean timelimit = false;
		
		if (true == timelimit) {
			final int year = 2012;
			final int month = 11;
			final int day = 30;

			Calendar calendar = Calendar.getInstance();
			calendar.clear();
			calendar.set(year, month, day);
			Date limitDate = calendar.getTime();
			Date curDate = new Date(System.currentTimeMillis());
			if (true == curDate.after(limitDate)) {
				rlt = false;
			}
		}
		return rlt;
	}

	private void initData() {
		if (metric == null) {
			metric = new DisplayMetrics();
		}
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		Log.d(LOGCAT," density="+metric.density+" densityDpi="+metric.densityDpi+" heightPixels="+metric.heightPixels+" widthPixels="+metric.widthPixels+" scaledDensity="+metric.scaledDensity+" xdpi="+metric.xdpi+" ydpi="+metric.ydpi+" toString="+metric.toString());

		setIsAble(configPreference.getHallEnalbed(this));

		if (!isScrollLayout) {
			installed_gl.setAdapter(appAdaper);
			installed_gl.setSelection(0);
			uninstalled_gl.setAdapter(apkAdaper);
			uninstalled_gl.setSelection(0);
			if (appAdaper == null) {
				appAdaper = new AppAdaper(this);
				apkAdaper = new ApkAdaper(this);
				// appAdaper.toRead(getSharedPreferences(Main.appDataname,
				// Activity.MODE_PRIVATE));
				// apkAdaper.toRead(getSharedPreferences(Main.apkDataname,
				// Activity.MODE_PRIVATE));
				appAdaper.toRead(null);
				apkAdaper.toRead(null);
			}
		} else {

			if (apkAdapterSl == null) {
				apkAdapterSl = new ApkScrollAdapter(this);
				appAdapterSl = new AppScrollAdapter(this);
				appAdapterSl.setScroll(installed_sl);
				apkAdapterSl.setScroll(uninstalled_sl);
				apkAdapterSl.toRead();
				appAdapterSl.toRead();
			} else {
				appAdapterSl.setScroll(installed_sl);
				apkAdapterSl.setScroll(uninstalled_sl);
				apkAdapterSl.initData();
				appAdapterSl.initData();
			}
			installedPager.refreshUindex(installed_sl.getScreen(), installed_sl.getCurScreen());
			uninstalledPager.refreshUindex(uninstalled_sl.getScreen(), uninstalled_sl.getCurScreen());
		}

		if (!isScrollLayout) {
			installedPager.refreshUindex(appAdaper.getCount(),0);
			uninstalledPager.refreshUindex(apkAdaper.getCount(),0);
		}
	}

//	private void registerReceiver() {
//		if (mBootReceiver == null) {
//			mBootReceiver = new ApkReceiver(this);
//			IntentFilter mIntentFilter = new IntentFilter();
//			mIntentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
//			mIntentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
//			mIntentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
//			mIntentFilter.addAction(ListenerService.NEW_APP_PUSH);
//			mIntentFilter.addDataScheme("package");
//			registerReceiver(mBootReceiver, mIntentFilter);
//		}
//	}

	@Override
	protected void onResume() {
		Log.v(LOGCAT, "apkHall onResume");
		isPause = false;
		MessageNotification instance = MessageNotification.getInstance(this);
		instance.cancelNotification();
		super.onResume();
	}

	protected void onPause() {
		Log.v(LOGCAT, "apkHall onPause");
		isPause = true;
		// SharedPreferences appPreferences =
		// getSharedPreferences(Main.appDataname, Activity.MODE_PRIVATE);
		// SharedPreferences.Editor appeditor=appPreferences.edit();
		// appeditor.clear();
		// appAdaper.toSave(appeditor);
		// appeditor.commit();
		//		
		// SharedPreferences apkPreferences =
		// getSharedPreferences(Main.apkDataname, Activity.MODE_PRIVATE);
		// SharedPreferences.Editor apkeditor=apkPreferences.edit();
		// apkeditor.clear();
		// apkAdaper.toSave(apkeditor);
		// apkeditor.commit();
//		if (!isScrollLayout) {
//			appAdaper.toSave(null);
//			apkAdaper.toSave(null);
//		} else {
//			appAdapterSl.toSave(null);
//			apkAdapterSl.toSave(null);
//		}
		super.onPause();
	}

	protected void onStart() {
		Log.v(LOGCAT, "apkHall onStart");
		super.onStart();
		startRegiste();
		if(cdLoader!=null){
			cdLoader.dismiss();
			appLoader.setLock(false);
		}
		
		boolean psa = configPreference.getPSInput(this);
		if (true == psa) {
			DeviceAdminLocalSetup.SendSetPsIntent(this);;
		}
	}

	protected void onStop() {
		Log.v(LOGCAT, "apkHall onStop");
		super.onStop();
		closeRegiste();
	}

	@Override
	protected void onDestroy() {
		Log.v(LOGCAT, "apkHall onDestroy");
//		unregisterReceiver(mBootReceiver);
		message_vf.onDestroyed();
		apkAdapterSl.onDestroy();
		Log.v("VV", "kill");
		mHall = null;
		// android.os.Process.killProcess(android.os.Process.myPid());
		// ActivityManager manager =
		// (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
		// manager.restartPackage(getPackageName());
		// manager.forceStopPackage(getPackageName());
		super.onDestroy();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		int index1=installedPager.getCurrent();
		int index2=uninstalledPager.getCurrent();
		boolean btnisBusy=isBusy;
		isBusy=false;
		//
		Log.v(LOGCAT, "onConfigurationChanged");
		setContentView(R.layout.hall_linear);
		initView();
		initData();
		if (!isScrollLayout) {
			appAdaper.onConfigurationChanged();
			apkAdaper.onConfigurationChanged();
		}
		//
		if(index1!=-1){
			installed_sl.setToScreen(index1);
		}
		if(index2!=-1){
			uninstalled_sl.setToScreen(index2);
		}
		setBusy(btnisBusy);
	}
	
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		if (parent.equals(installed_gl)) {
			installedPager.setCurrentUindex(position);
		} else {
			uninstalledPager.setCurrentUindex(position);
		}
	}

	
	public void onNothingSelected(AdapterView<?> parent) {

	}

	
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		appAdaper.setEdit(null);
	}

	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case FINISH_DOLOAD:
				AppItem app=(AppItem) msg.obj;
				Log.d(LOGCAT,"apkManager handler get message :download finished");
				((GridHolder) app.getView().getTag()).progressBar.setVisibility(View.GONE);
				if (msg.arg1 == ApkLoader.CANTSAVE) {
					Toast.makeText(Main.this, R.string.sys_closeusb, Toast.LENGTH_LONG).show();
					return;
				}
				installApplication((AppItem) msg.obj, Main.this);
				((AppItem) msg.obj).setFlag(Main.this, Appdb.UNINSTALLED);
				break;
			case Fail_DOLOAD: 
				Log.i(LOGCAT,"apkManager handler get message :download fail");
				app=(AppItem) msg.obj;
				((GridHolder)app.getView().getTag()).progressBar.setVisibility(View.GONE);
				((GridHolder) app.getView().getTag()).progressBar.setProgress(0);
				app.setFlag(Main.this, AppItem.UNINSTALLED);
//				CustomDialog.createDialog(Main.mHall,R.string.apk_load_fail);
				Toast.makeText(Main.this, R.string.apk_load_fail, 1).show();
				break;
			case ADD_APP:
				app=(AppItem) msg.obj;
				Log.i(LOGCAT, "apkManager handler get message :add app "
						+ app.getApkName());
				if (!isScrollLayout) {
					if (appAdaper.getId(app) >= 0) {
						Log.i(LOGCAT, "apkManager app already exist");
						break;
					}
				} else {
					if (appAdapterSl.getItemById(app.getApkId(),app.getApkVersion())!=null){
						Log.i(LOGCAT, "apkManager app already exist");
						break;
					}
				}
				addAppItem(app);
				break;
			case REMOVE_APP:
				Log.i(LOGCAT, "apkManager handler get message :remove app "
						+ msg.arg1);
				if (!isScrollLayout) {
					app=appAdaper.get(msg.arg1);
					delAppItem(app.getApkName(),app.getApkVersion());
				} else {
					app=(AppItem) appAdapterSl.getItem(msg.arg1);
					delAppItem(app.getApkId(),app.getApkVersion());
				}
				break;
			case UNINSTALL:
				Log.i(LOGCAT, "apkManager handler get message :uninstall app "
						+ msg.arg1);
				if (!isScrollLayout) {
					app = appAdaper.getPackage((String) msg.obj);
				} else {
					app = appAdapterSl.getPackageItem((String) msg.obj);
					if (app != null) {
//						int id = 
						delAppItem(app.getApkId(),app.getApkVersion());
					}
					if(isVersionOn){
						PackageManager pm=Main.this.getPackageManager();
						String version=null;
						try {
							version=pm.getPackageInfo((String) msg.obj, 0).versionName;
						} catch (NameNotFoundException e) {
							e.printStackTrace();
						}
						if (app != null) {
							addApkItem(app);
						}
					}else{
						if (app != null) {
							if(apkAdapterSl.getItemById(app.getApkId())==null){
								addApkItem(app);
							}else{
								ApkProfileContent.deleteApkContentwithNameandFlag(Main.this, app.getId());
							}
						}
					}
				}
				break;
			case REPLACED:
			case INSTALL:
				String version=null;
				AppItem oldApp=null;
				PackageManager pm=Main.this.getPackageManager();
				try {
					version=pm.getPackageInfo((String) msg.obj, 0).versionName;
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}
				app = apkAdapterSl.getPackageItem((String) msg.obj,version);
				
				if(app!=null){
					app.deleteApkFile();
					delApkItem(app.getApkId(),app.getApkVersion());
	//				oldApp=appAdapterSl.getPackageItem(app.getPackageName());
					oldApp=appAdapterSl.getItemById(app.getApkId());
					if(oldApp!=null){
						delAppItem(oldApp.getApkId(),oldApp.getApkVersion());
						if(isVersionOn){
							addApkItem(oldApp);
						}else{
							ApkProfileContent.deleteApkContentwithNameandFlag(Main.this, oldApp.getId());
						}
					}
					addAppItem(app);
				}

				break;
			case NEWAPPPUSH:
				Log.i(LOGCAT, "NEWAPPPUSH here");
				if(isVersionOn){
					readUdata();
				}
				break;
			case HALL_ENABLE_MESSAGE:
				boolean b = ((Boolean) msg.obj).booleanValue();
				setIsAble(b);
				Log.i(LOGCAT, "HALL_ENABLE_MESSAGE here" + b);
				break;
			case APP_PAGE:
				installedPager.setCurrentUindex(msg.arg1);
				break;
			case APK_PAGE:
				uninstalledPager.setCurrentUindex(msg.arg1);
				break;
			}
		}
	};
	/**
	 * btn handle
	 */
	 Handler handler_btn = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == STOP_ROTATION_MSG) {

				updata_ib.setEnabled(true);
				updata_dup.setVisibility(View.VISIBLE);
				updata_dpress.setVisibility(View.GONE);
				updata_drun.setVisibility(View.GONE);
				updata_drun.clearAnimation();
			} else if (msg.what == START_POTATiON_MSG) {

				updata_ib.setEnabled(false);
				updata_dup.setVisibility(View.GONE);
				updata_dpress.setVisibility(View.GONE);
				updata_drun.setVisibility(View.VISIBLE);
				animation.reset();
				updata_drun.startAnimation(animation);
			}
			super.handleMessage(msg);
		}
	};

	/**
	 * updata button
	 */
	public void initButton() {
		animation = AnimationUtils.loadAnimation(this, R.anim.rotate);
		animation.setFillBefore(false);

		updata_ib = (ImageButton) findViewById(R.id.updata_btn);
		updata_dpress = (ImageView) findViewById(R.id.dpress);
		updata_dup = (ImageView) findViewById(R.id.dup);
		updata_drun = (ImageView) findViewById(R.id.drun);

		// updata_ib.setOnClickListener(mClick);
		updata_ib.setOnTouchListener(mOnTouchListener);
		// updata——ib.seton
		updata_dup.setVisibility(View.VISIBLE);
	}

	public final android.view.View.OnTouchListener mOnTouchListener = new android.view.View.OnTouchListener() {

		
		public boolean onTouch(View v, MotionEvent event) {
			int action = event.getAction();
			switch (action) {
			case MotionEvent.ACTION_DOWN:
				if (updata_ib.isClickable()) {
					// updata_ib.setEnabled(true);
					updata_dup.setVisibility(View.GONE);
					updata_dpress.setVisibility(View.VISIBLE);
					updata_drun.setVisibility(View.GONE);
					// updata_drun.startAnimation(animation);
				}
				break;
			case MotionEvent.ACTION_UP:
				if (updata_ib.isClickable()) {
					setBusy(true);
					// 开启新任务，任务结束会发出hander，已通知结束任务忙
					// new Thread(new MyThread()).start();
					Log.v("click", "gagagagagagaga");
					Intent i = new Intent(ListenerService.USER_UPDATE_ACTION);
					i.setClass(Main.this, ListenerService.class);
					startService(i);
					Log.v("click", "gagagagagagaga");

				}
				break;
			case MotionEvent.ACTION_OUTSIDE:

				break;
			}
			return true;
		}

	};
	public final Button.OnClickListener mClick = new Button.OnClickListener() {
		public void onClick(View v) {
			Log.v("click", "gagagagagagaga1");
			setBusy(true);
			// 开启新任务，任务结束会发出hander，已通知结束任务忙
			// new Thread(new MyThread()).start();

		}
	};

	public static void finishUpdata() {
		if (mHall != null) {
			mHall.setBusy(false);
		}
	}

	public void setBusy(boolean busy) {
		if (busy != isBusy) {
			isBusy = busy;
			Message message = new Message();
			if (isBusy) {
				message.what = START_POTATiON_MSG;
			} else {
				message.what = STOP_ROTATION_MSG;
			}
			handler_btn.sendMessage(message);
		}
	}

	class MyThread implements Runnable {
		
		public void run() {
			while (true) {
				try {
					Thread.sleep(36000);
					setBusy(false);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("RE","rsult="+requestCode+" "+resultCode);
//		switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
//		case RESULT_OK:
//			Bundle b=data.getExtras();  //data为B中回传的Intent
//			String str=b.getString("ListenB");//str即为回传的值"Hello, this is B speaking"
//			/* 得到B回传的数据后做什么... 略 */
//                      break;
//		default:
//	          break;
//		}
	}

	public static void installApplication(AppItem app, Context context) {
		if (app.getApkFileName() != null) {
			Log.i(LOGCAT, "apkManager to install apk : " + app.getApkFullPath());
			if (app.getPackageName() == null||app.getApkVersion()==null) {
				Log.i(LOGCAT, "app is null:" + (app == null) + " context is null:" + (context == null));
				PackageInfo pi = Utils.getUninatllApkInfo(context, app .getApkFullPath());
				Log.i(LOGCAT, "pi is null " + (pi == null));
				if (pi != null) {
					Log.i(LOGCAT, "package name is null" + (pi.packageName));
					app.setPackageName(pi.packageName);
					app.setApkVersionClient(pi.versionName);
					app.toSave(context);
				}
			}
			if (app.getPackageName() == null||app.getApkVersionClient()==null) {
				Toast.makeText(context,R.string.apk_file_erro, Toast.LENGTH_LONG).show();
				return;
			}
			Log.i(LOGCAT, "apkManager to install apk : "
					+ app.getPackageName()+" version="+app.getApkVersionClient());
				context.startActivity(Utils.installApplication(app.getApkFullPath()));
		}
	}
	public void addAppItem(AppItem item) {
		if (item == null) {
			return;
		}
		item.setFlag(this, Appdb.INSTALLED);
		if (!isScrollLayout) {
			int index = installed_gl.getSelectedItemPosition();
			appAdaper.addItem(item);
			installed_gl.setAdapter(appAdaper);
			installed_gl.setSelection(index, false);
			installedPager.refreshUindex(appAdaper.getCount(),index);
			
		} else {
			appAdapterSl.addItem(item);
			installedPager.refreshUindex(installed_sl.getScreen(), installed_sl.getCurScreen());
		}
	}

	public int delAppItem(String apkId,String version) {
		int id = -1;
		if (isScrollLayout) {
			id = appAdapterSl.deleteItemByApkId(apkId,version);
			if (id >= 0) {
				int index = installed_sl.getCurScreen();
				if (index >= installed_sl.getScreen()&&index>=0) {
					installed_sl.snapToScreen(index - 1);
				}
				installedPager.refreshUindex(installed_sl.getScreen(), installed_sl.getCurScreen());
			}
		} else {
			id = appAdaper.deleteItem(apkId);
			if (id >= 0) {
				int index = installed_gl.getSelectedItemPosition();
				installed_gl.setAdapter(appAdaper);
				if (index >= installed_gl.getCount()) {
					index = installed_gl.getCount() - 1;
				}
				installed_gl.setSelection(index, false);
				installedPager.refreshUindex(installed_gl.getCount(),index);
			}
		}
		return id;
	}

	public void addApkItem(AppItem item) {
		if(item.isMark()){
			item.setMark(false);
			item.toSave(Main.this);
		}
		if(item.getFlag()==AppItem.INSTALLED){
			item.setFlag(this, Appdb.UNINSTALLED);
		}
		if (!isScrollLayout) {
			int index = uninstalled_gl.getSelectedItemPosition();
			apkAdaper.addItem(item);
			uninstalled_gl.setAdapter(apkAdaper);
			uninstalled_gl.setSelection(index, false);
			uninstalledPager.refreshUindex(apkAdaper.getCount(),index);
		} else {
			apkAdapterSl.addItem(item);
			uninstalledPager.refreshUindex(uninstalled_sl.getScreen(), uninstalled_sl.getCurScreen());
		}
	}

	public int delApkItem(String apkId,String version) {
		int id;
		if (!isScrollLayout) {
			id = apkAdaper.deleteItem(apkId);
			if (id >= 0) {
				int index = uninstalled_gl.getSelectedItemPosition();
				uninstalled_gl.setAdapter(apkAdaper);
				if (index >= uninstalled_gl.getCount()) {
					index = uninstalled_gl.getCount() - 1;
				}
				uninstalled_gl.setSelection(index, false);
				uninstalledPager.refreshUindex(apkAdaper.getCount(),index);
				
			}
		} else {
			id = apkAdapterSl.deleteItemByApkId(apkId,version);
			if (id >= 0) {
				int index = uninstalled_sl.getCurScreen();
				if (index >= uninstalled_sl.getScreen()&&index>=0) {
					uninstalled_sl.snapToScreen(index - 1);
				}
				uninstalledPager.refreshUindex(uninstalled_sl.getScreen(), uninstalled_sl.getCurScreen());
			}
		}
		return id;
	}

	@Deprecated
	private void test(){
		for(int i=0;i<40;i++){
			AppItem app=new AppItem(i+"别碰我别碰我别碰我别碰我",Utils.ICONPATH+"/biepengwo.png",Utils.ICONPATH+"/biepengwo_1.png","http://aemm.imolife.com/qq.apk","hhh");
			app.setFlag(this, Appdb.INSTALLED);																							
			app.setApkId("id"+i);
			app.setApkVersion("i"+i);
			addAppItem(app);
			app.isBase64=false;
			if(i<10){
			app=new AppItem((40+i)+"别碰我",Utils.ICONPATH+"/biepengwo.png",Utils.ICONPATH+"/biepengwo_1.png","http://aemm.imolife.com/qq.apk","Descreipt");
			app.setFlag(this,Appdb.UNINSTALLED);
			app.isBase64=false;
			addApkItem(app);
			}
		}
		 add_app_btn.setVisibility(View.GONE);
	        del_app_btn.setVisibility(View.GONE);
	        unable_app_btn.setVisibility(View.GONE);
	        add_apk_btn.setVisibility(View.GONE);
	        del_apk_btn.setVisibility(View.GONE);
	}
	int i=0;
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_app_btn:
			AppItem app = new AppItem("别碰我", Utils.ICONPATH + "/biepengwo.png",
					Utils.ICONPATH + "/biepengwo_1.png",
					"http://aemm.imolife.com/qq.apk", "hhh");
			app.setFlag(this, Appdb.INSTALLED);
			addAppItem(app);
			test();
			break;
		case R.id.del_app_btn:
			delAppItem("别碰我",null);
			break;
		case R.id.unable_app_btn:
			AppItem appItem = null;
			if (!isScrollLayout) {
				appItem = appAdaper.getItem("别碰我");
			} else {
				if (null != appItem) {
					appAdapterSl.getPackageItem(appItem.getPackageName());
				}
			}
			if (appItem != null) {
				appItem.setAble(false);
			}
			break;
		case R.id.add_apk_btn:
			app = new AppItem(
					"别碰我",
					Utils.ICONPATH + "/biepengwo.png",
					Utils.ICONPATH + "/biepengwo_1.png",
					"http://aemm.imolife.com/qq.apk",
					"Descreiption TextView01第三方的,会计法斯,科拉的房,间上的机，房接受对方即可舒服点地方就爱上了第三方Descreiption TextView01第三方的会计法斯科拉,的房间上的机房接,受对方即可舒服点地方就爱上了第三方");
			app.setFlag(this, Appdb.NEWAPP);
			addApkItem(app);
			break;
		case R.id.del_apk_btn:
			delApkItem("别碰我",null);
			break;
		}
	}

	public static void installedApplicationSilence(Context context,
			String apkFile) {
		Utils.installApplicationSilence(context, apkFile);
	}

	public static void unInstalledApplicationSilence(Context context,
			String packageName) {
		Utils.unInstallApplicationSilence(context, packageName);
	}

	/**
	 * updata dialog
	 * 
	 * @param title
	 * @param msg
	 * @param isCancel
	 */
	public void ShowUpdata(int title, int msg, final String url,
			boolean isCancel) {
		if (isDefault) {
			AlertDialog.Builder builder = new Builder(Main.this);
			builder.setMessage(msg);

			builder.setTitle(title);

			builder.setPositiveButton(R.string.updata,
					new DialogInterface.OnClickListener() {

						
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							Intent it = new Intent(Intent.ACTION_VIEW, Uri
									.parse(url));
							Main.this.startActivity(it);
						}
					});
			if (isCancel) {
				builder.setNegativeButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {
							
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
			}
			AlertDialog alertDialog = builder.create();
			alertDialog.setCancelable(false);
			alertDialog.show();
		} else {
			final CustomDialog cd = new CustomDialog(this);
			cd.setCancelable(false);
			cd.show();
			cd.setMessage(title);
			cd.addPositiveButton(R.string.updata,
					new View.OnClickListener() {
						
						public void onClick(View v) {
							Intent it = new Intent(Intent.ACTION_VIEW, Uri
									.parse(url));
							Main.this.startActivity(it);
							cd.dismiss();
						}
					});
			if (!isCancel) {
				cd.addNegativeButton(android.R.string.cancel,
						new View.OnClickListener() {
							
							public void onClick(View v) {
								cd.dismiss();
							}
						});
			}
		}
	}

	public void downloadApp(final Context context, final AppItem app) {
		if (isDefault) {
			AlertDialog.Builder builder = new Builder(Main.this);
			builder.setMessage(context.getText(R.string.is_install) + "\""+app.getApkName() +"\"");

			builder.setPositiveButton(R.string.install/* "安装" */,
					new DialogInterface.OnClickListener() {

						
						public void onClick(DialogInterface dialog, int which) {

							//app.setLock(false);
							new ApkLoader(Main.this,app, ((Main) context).handler)
									.start();
							dialog.dismiss();

						}
					});

			builder.setNegativeButton(android.R.string.cancel,
					new DialogInterface.OnClickListener() {
						
						public void onClick(DialogInterface dialog, int which) {
							app.setLock(false);
							dialog.dismiss();
						}
					});
			AlertDialog alertDialog = builder.create();
			alertDialog.show();
		} else {
			final CustomDialog cd = new CustomDialog(this);
			cdLoader=cd;
			appLoader=app;
			cd.setCancelable(false);
			cd.show();
			cd.setTitle(R.string.is_install);
			cd.setMessage( "\""+ app.getApkName()+ "\"?");
			cd.addPositiveButton(R.string.install,
					new View.OnClickListener() {
						
						public void onClick(View v) {
							//app.setLock(false);
							new ApkLoader(Main.this,app, ((Main) context).handler)
									.start();
							cd.dismiss();
							cdLoader=null;
						}
					});
			cd.addNegativeButton(android.R.string.cancel,
					new View.OnClickListener() {
						
						public void onClick(View v) {
							app.setLock(false);
							cd.dismiss();
							cdLoader=null;
						}
					});
		}
	}
	
	//读数据
//	public void readAll(){
//		ApkProfileContent.queryAllApkContents(this);
//	}
	public void readUpdata(long ids[]){
		setBusy(true);
		for(int i=ids.length-1;i>=0;i--){
			ApkProfileContent mApkProfileContent=ApkProfileContent.restoreApkProfileContentWithId(this,ids[i]);
			if(mApkProfileContent==null){
				apkAdapterSl.deleteById(ids[i]);
				Log.d("appUpdata","ids="+ids[i]+" delete.");
			}else{
				AppItem app=apkAdapterSl.getItemById(mApkProfileContent.mApkId);
				if(app==null){
					Log.d("appUpdata","new  pgk="+mApkProfileContent.mApkId+"  version="+mApkProfileContent.mApkVersion);
					addApkItem(new AppItem(mApkProfileContent));
				}else{
					Log.d("appUpdata","updata  pgk="+mApkProfileContent.mApkId+"  version="+mApkProfileContent.mApkVersion);
					app.updataView(mApkProfileContent);
				}
			}
		}

		apkAdapterSl.updataNewAppMessage();
		setBusy(false);
	}
	public void readUdata(){
		setBusy(true);
		List <ApkProfileContent> apkProList=ApkProfileContent.queryApkContentswithFlag(this,Appdb.NEWAPP);
		if (apkProList == null||apkProList.size()<=0){
			return;
		}
		AppItem app;
		for (ApkProfileContent pro : apkProList) {
			if (pro.mApkId.equals(AEMM_EXPRESS)) {
				ShowUpdata(R.string.hall_updata_title, R.string.hall_updata_content, pro.mApkUrl, true);
			}
			app = new AppItem(pro);
			if ("false".equals(pro.mApkInstallEnabled)) {
				app.setAble(false); //fix bug2642 by cuixiaowei 20110722
			}
			// Log.v(LOGCAT,"readUdata "+pro.getInfo());
			// Log.v(LOGCAT,"readUdata2 "+app.getInfo());
			if(apkAdapterSl.getItemById(app.getApkId(),app.getApkVersion())==null){
				addApkItem(app);
			}
		}
		apkAdapterSl.updataNewAppMessage();
		setBusy(false);
	}

	/**
	 * 前台注册
	 * 
	 * @param context
	 * @param msg
	 */
	private void startRegiste() {
		Intent serviceInfo = new Intent(ListenerService.HALL_START_ACTION);
		serviceInfo.setClass(this, ListenerService.class);
		startService(serviceInfo);
	}

	/**
	 * 后台注册
	 * 
	 * @param context
	 */
	private void closeRegiste() {
		Intent serviceInfo = new Intent(ListenerService.HALL_CLOSE_ACTION);
		serviceInfo.setClass(this, ListenerService.class);
		startService(serviceInfo);
	}

//	public void appLastTime() {
//		ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//	}

	/**
	 * 大厅可用
	 * 
	 * @return
	 */
	public boolean isAble() {
		return isAble;
	}

	/**
	 * 大厅可用
	 * 
	 * @param b
	 */
	public void setIsAble(boolean b) {
		isAble = b;
	}
	
	public void setAppEnables(String[] ids, boolean[] enable) {
		if (!isScrollLayout) {
			apkAdaper.setAppAbled(ids, enable);
			appAdaper.setAppAbled(ids, enable);
		} else {
			apkAdapterSl.setAppAbled(ids, enable);
			appAdapterSl.setAppAbled(ids, enable);
		}
	}

	private boolean isDefault = false;
	private boolean isAble = false;
	// ApkReceiver mBootReceiver;
	LayoutInflater mInflater;
	AppAdaper appAdaper;
	ApkAdaper apkAdaper;
	ApkScrollAdapter apkAdapterSl;
	AppScrollAdapter appAdapterSl;
	CustomDialog cdLoader;
	AppItem appLoader;
	LinearLayout hall_linear;
	private Gallery installed_gl;
	private Gallery uninstalled_gl;
	private ScrollLayout installed_sl;
	private ScrollLayout uninstalled_sl;
	LinearLayout installed_index_linear;
	LinearLayout uninstalled_index_linear;
	PageIndexManager uninstalledPager;
	PageIndexManager installedPager;
	private ImageButton updata_ib;
	private ImageView updata_dpress;
	private ImageView updata_dup;
	private ImageView updata_drun;
	private Animation animation;
	private HallMessageManager message_vf;
	DisplayMetrics metric;
	private final int STOP_ROTATION_MSG = 1;
	private final int START_POTATiON_MSG = 2;
	private boolean isBusy;
	public static final String LOGCAT = "Main";
	Button add_app_btn, del_app_btn, unable_app_btn;
	Button add_apk_btn, del_apk_btn;
	public static Main mHall;
	public static final int TITLEH = 30;
	public boolean isPause = true;
	public static final boolean isVersionOn=false;
	public static final byte FINISH_DOLOAD = 1, ADD_APP = 2, REMOVE_APP = 3,
			INSTALL = 4, UNINSTALL = 5, Fail_DOLOAD = 6, NEWAPPPUSH = 7,
			HALL_ENABLE_MESSAGE = 8,APP_PAGE = 9, APK_PAGE = 10,REPLACED=11;
	public static final String AEMM_EXPRESS ="aemm_express";// "com.accenture.aemm_express";
//	public static final int SetPasswordFlag = SET_PASSWORD;
	public static final String DEBUGLOGCAT = "debuglogcat";
	public static final boolean debugView = false;
	public static final boolean debug=false;
	public static final boolean userDebug=false;
	public static final boolean isScrollLayout = true;
//	private static final int UNORIENTATION = -1000;
    public static final String userName="wjx";
//    public static final String passWord="JZ@Q4h2T";
	public static final String passWord="eD6@Wh9Q";
    public static final String imei="wjx123456789";
//	public static final String userName="abc_phone";
//	public static final String imei="00000000";
//    public static final String userName="a1987";
//    public static final String passWord="g97*bstE";
}
