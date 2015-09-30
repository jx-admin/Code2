package com.aess.aemm.view;

import java.io.IOException;
import java.io.Serializable;

import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.aess.aemm.R;
import com.aess.aemm.appmanager.AutoInstall;
import com.aess.aemm.commonutils.CommUtils;
import com.aess.aemm.commonutils.HelpUtils;
import com.aess.aemm.db.NewsContent;
import com.aess.aemm.function.ProfessionalFunction;
import com.aess.aemm.push.PushService;
import com.aess.aemm.receiver.AemmDeviceAdminReceiver;
import com.aess.aemm.update.Update;
import com.aess.aemm.view.GridAdapter.GridHolder;
import com.aess.aemm.view.data.Appdb;
import com.aess.aemm.view.data.User;
import com.aess.aemm.view.evaluate.EnvaluateDialog;
import com.aess.aemm.view.log.Logger;
import com.aess.aemm.view.log.LoggerFactory;
import com.aess.aemm.view.menu.MenuDialog;
import com.aess.aemm.view.msg.SendMsgActivity;
import com.aess.aemm.view.popu.AemmPopu;
import com.ccssoft.framework.base.BaseActivity;
import com.ccssoft.gpscamera.GpsCameraActivity;
import com.aess.aemm.view.quit.UserQuit;

/**
 * @author junxu.wang
 * 
 */
@SuppressWarnings("rawtypes")
public class MainView extends Activity implements OnClickListener,
		android.accounts.AccountManagerCallback {

	public final static int SetPasswordFlag = 1;
	public final static String TAG = "MainView";
	public final static String DOWNAPK = "com.aess.aemm.DOWNAPK";
	
//	public static final int weight = 260;
//	public static final int height = 420;
	
	public static final String ACTION_MSG = "android.intent.action.AEMMMSG";

	public static void startHall(Context c) {
		Intent intent = new Intent(c, MainView.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		c.startActivity(intent);
	}
	
	public static void sendAutoInsBegMessage() {
		if (MainView.mHall != null) {
			MainView.mHall.handler.sendEmptyMessage(MainView.A_INSTALL_BEG);
		}
	}
	
	public static void sendAccentCheck() {
		if (MainView.mHall != null) {
			MainView.mHall.handler.sendEmptyMessage(MainView.A_ACCENT_CHK);
		}
	}
	
	public static void sendMsgCheck() {
		if (MainView.mHall != null) {
			MainView.mHall.handler.sendEmptyMessage(MainView.A_MSG_CHK);
		}
	}
	
	public static void sendDelUserEnd() {
		if (MainView.mHall != null) {
			MainView.mHall.handler.sendEmptyMessage(MainView.A_DEL_USER_END);
		}
	}
	
	public static void sendAutoInsEndMessage() {
		if (MainView.mHall != null) {
			MainView.mHall.handler.sendEmptyMessage(MainView.A_INSTALL_END);
		}
	}
	
	public static void sendHintMes(int arg1, int arg2) {
		if (MainView.mHall != null) {
			Message msg = new Message();
			msg.what = MainView.A_TOAST_HINT;
			msg.arg1 = arg1;
			msg.arg2 = arg2;
			MainView.mHall.handler.sendMessage(msg);
		}
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d("onCreate");
		super.onCreate(savedInstanceState);

		initVelue();
		initView();
		initData();

		debugView();
	}

	private void initVelue() {
		mHall = this;
		inflater = mHall.getLayoutInflater();
		resources = mHall.getResources();
		mUpdateMessageHandler = new UpdateMessageHandler(this);
		mViewUtils = new ViewUtils(this);

		TextView tv = new TextView(this);
		float fontSize = tv.getTextSize();
		FontMetrics fm = tv.getPaint().getFontMetrics();
		float tvH = fm.bottom - fm.top + 0.9f;
		titleHeight = (int) (resources.getDimension(R.dimen.title_t_margin)
				+ resources.getDimension(R.dimen.title_d_margin) + tvH);
		nameW = (int) (fontSize * resources
				.getInteger(R.integer.icon_name_length));
		int iconW = (int) (resources.getDimension(R.dimen.app_icon_size) + resources
				.getDimension(R.dimen.edit_half) * 2);
		iconWidth = nameW > iconW ? nameW : iconW;
		iconHeight = (int) (iconW - resources.getDimension(R.dimen.edit_half)
				+ tvH + resources.getDimension(R.dimen.icon_name_t_margin));
		apkDescriptionW = iconWidth * 2;
		apkIconWidth = (int) (iconWidth + apkDescriptionW + resources
				.getDimension(R.dimen.apk_descrip_l_margin));
		indexHeight = (int) (resources.getDimension(R.dimen.index_td_margin) * 2 + resources
				.getDimension(R.dimen.index_size));
		lineHeight = (int) resources.getDimension(R.dimen.line_h);
		Bitmap logo_bmp = BitmapFactory.decodeResource(resources,R.drawable.logo);
		@SuppressWarnings("unused")
		int logoHeight = logo_bmp.getHeight();
		Bitmap updata_bmp = BitmapFactory.decodeResource(resources,
				R.drawable.updata_drunning);
		int updateBmpHeight = updata_bmp.getHeight();
		Bitmap button_bk_bmp = BitmapFactory.decodeResource(resources,
				R.drawable.updata_pressed);
		int buttonHeight = button_bk_bmp.getHeight();
		buttonHeight = buttonHeight > updateBmpHeight ? buttonHeight
				: updateBmpHeight;
		bottomHeight = (int) (buttonHeight
				+ resources.getDimension(R.dimen.button_d_margin) + resources
				.getDimension(R.dimen.button_t_margin));
		extraH = ((titleHeight + indexHeight) << 1) + bottomHeight;
		DisplayMetrics dm2 = getResources().getDisplayMetrics();
		int width = 0, height = 0;
		try {
			height = (int) (dm2.heightPixels - resources
					.getDimension(com.android.internal.R.dimen.status_bar_height));
			width = (int) (dm2.widthPixels - resources
					.getDimension(com.android.internal.R.dimen.status_bar_height));
		} catch (Exception e) {
			height = (int) (dm2.heightPixels - resources
					.getDimension(com.android.internal.R.dimen.status_bar_icon_size));
			width = (int) (dm2.widthPixels - resources
					.getDimension(com.android.internal.R.dimen.status_bar_icon_size));
		}
		int slH = height / 2, slW = width / 2;
		Log.d("TextSize:" + fontSize + " fontH:" + tvH + " titleHeight:"
				+ titleHeight + " nameW:" + nameW + " iconWidth:" + iconWidth
				+ " iconHeight:" + iconHeight + " apkWidth:" + apkIconWidth
				+ " indexheight:" + indexHeight + " lineHeight:" + lineHeight
				+ " button_bk_bmp:" + buttonHeight + " bottomHeight:"
				+ bottomHeight + " width:" + width + " height:" + height
				+ " extraH:" + extraH + " slW:" + slW + " slH:" + slH
				+ "icon size:"
				+ (resources.getDimension(android.R.dimen.app_icon_size)));
	}

	private void debugView() {
		if (VIEWDEBUG) {
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

			findViewById(R.id.debug_lin).setVisibility(View.VISIBLE);

			install_tv.setBackgroundColor(0xffff0000);
			install_sl.setBackgroundColor(0xffff00ff);
			install_index_lin.setBackgroundColor(0xff0000ff);

			uninstall_tv.setBackgroundColor(0xffffff00);
			uninstall_sl.setBackgroundColor(0xff00ff00);
			uninstall_index_lin.setBackgroundColor(0xff0000ff);
			message_vf.setBackgroundColor(0x55555500);
		}
	}

	private void initView() {
		mHall.setContentView(R.layout.hall_layout);

		hall_layout=(LinearLayout) findViewById(R.id.hall_layout);
		install_tv = (TextView) mHall.findViewById(R.id.install_title_tv);
		install_sl = (ScrollLayout) findViewById(R.id.installed_sl);
		install_sl.iconWidth = iconWidth;
		install_sl.isApp = true;
		install_index_lin = (LinearLayout) findViewById(R.id.install_index_linear);
		uninstall_tv = (TextView) mHall.findViewById(R.id.uninstall_title_tv);
		uninstall_sl = (ScrollLayout) findViewById(R.id.uninstalled_sl);
		uninstall_sl.iconWidth = iconWidth;
		uninstall_index_lin = (LinearLayout) findViewById(R.id.uninstall_index_linear);
		if (ISRC) {
			mHall.install_sl.isSetRC = true;
			mHall.uninstall_sl.isSetRC = true;
			mHall.install_sl.isApp = true;
			mHall.uninstall_sl.isApp = false;
		}
		update_iv = (ImageView) findViewById(R.id.update_iv);
		update_btn = (Button) findViewById(R.id.update_btn);
		update_btn.setOnClickListener(mClick);
		if (animation == null) {
			animation = AnimationUtils.loadAnimation(mHall, R.anim.rotate);
			animation.setFillBefore(false);
		}

		install_sl
				.setPageHandler(MainView.APP_INDEX_REFRESH, APP_PAGE, handler);
		uninstall_sl.setPageHandler(MainView.APK_INDEX_REFRESH, APK_PAGE,
				handler);

		if (uninstallPager == null) {
			installPager = new PageIndexManager(this);
			uninstallPager = new PageIndexManager(this);
		}
		installPager.setLinearView(install_index_lin);
		uninstallPager.setLinearView(uninstall_index_lin);

		message_vf = mUpdateMessageHandler.getTextFlipper();
		if (message_vf != null) {
			TextFlipper message = (TextFlipper) findViewById(R.id.message_flipper);
			int count = message_vf.getChildCount();
			View v;
			for (int i = 0; i < count; i++) {
				v = message_vf.getChildAt(0);
				message_vf.removeViewAt(0);
				message.addView(v);
			}
			// message.mReceiver=message_vf.mReceiver;
			message_vf = message;
		} else {
			message_vf = (TextFlipper) findViewById(R.id.message_flipper);
		}
		message_vf.startFlipping();
		mUpdateMessageHandler.setTextFlipper(message_vf);
		if (ISRC) {
			iconSort.activity = MainView.this;

			if (!iconSort.toRread(this.getSharedPreferences("dataname",
					Activity.MODE_PRIVATE))) {
				new Handler().post(new Runnable() {
					public void run() {
						Rect frame = new Rect();
						mHall.getWindow().getDecorView()
								.getWindowVisibleDisplayFrame(frame);
						Display display = getWindowManager()
								.getDefaultDisplay();
						int titleBarH = 0;
						if (frame.bottom > 0 && frame.right > 0) {
							titleBarH = display.getHeight()
									- (frame.bottom - frame.top);
							if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
								iconSort.width = display.getHeight();
								iconSort.height = (display.getWidth()
										- titleBarH - extraH) >> 1;
								iconSort.width2 = frame.right;
								iconSort.height2 = (frame.bottom - frame.top - extraH) >> 1;
							} else {
								iconSort.width = frame.right;
								iconSort.height = (frame.bottom - frame.top - extraH) >> 1;
								iconSort.width2 = display.getHeight();
								iconSort.height2 = (display.getWidth()
										- titleBarH - extraH) >> 1;
							}
							iconSort.apkIconWidth = apkIconWidth;
							iconSort.iconWidth = iconWidth;
							iconSort.iconHeight = iconHeight;
							iconSort.marginH = 20;
							iconSort.marginW = 40;
							iconSort.find();
						}
						Log.d("titlebarH:" + titleBarH
								+ new iconSort().getInfo());
					}
				});
			} else {
				iconSort.setScroller();
			}
		}
	}

	private void initData() {
		if (metric == null) {
			metric = new DisplayMetrics();
		}
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		Log.d(" density=" + metric.density + " densityDpi=" + metric.densityDpi
				+ " heightPixels=" + metric.heightPixels + " widthPixels="
				+ metric.widthPixels + " scaledDensity=" + metric.scaledDensity
				+ " xdpi=" + metric.xdpi + " ydpi=" + metric.ydpi
				+ " toString=" + metric.toString());

		if (apkAdapterSl == null) {
			apkAdapterSl = new ScrollAdapter(mHall);
			appAdapterSl = new ScrollAdapter(mHall);
			appAdapterSl.setScroll(install_sl);
			apkAdapterSl.setScroll(uninstall_sl);
			apkAdapterSl.toReadApk();
			appAdapterSl.toReadApp();
		} else {
			appAdapterSl.setScroll(install_sl);
			apkAdapterSl.setScroll(uninstall_sl);
			apkAdapterSl.initData();
			appAdapterSl.initData();
		}
		installPager.refreshUindex(install_sl.getScreen(),
				install_sl.getCurScreen());
		uninstallPager.refreshUindex(uninstall_sl.getScreen(),
				uninstall_sl.getCurScreen());
		ViewUtils.updateNewAppMessage(this);
	}

	@Override
	protected void onResume() {
		atvState = RESUME;
		Log.d("apkHall onResume");
		ViewUtils.cancelNotification(this);
		updataHall();
		if (null == mHall) {
			mHall = this;
			update();
		}
		changeDerma(CommUtils.getDermaId(this));
		updataMsgHit();
		super.onResume();
	}

	protected void onPause() {
		atvState = PAUSE;
		Log.d("apkHall onPause");
		// appAdapterSl.toSave(null);
		// apkAdapterSl.toSave(null);

		mMenuDialog=null;
		super.onPause();
	}

	protected void onStart() {
		atvState = START;
		Log.d("apkHall onStart");
		super.onStart();

		int state = AutoInstall.state();
		if (state > 0) {
			apkAdapterSl.enbleClick(false);
		} else {
			apkAdapterSl.enbleClick(true);
		}
		
		int psa = CommUtils.getPasswordRequire(this);
		if (psa > 0) {
			sendIntentPsActiviy(1);
		}

		mUpdateMessageHandler.register(this);
		ViewUtils.checkAccount(this);
		ViewUtils.onStart(this);
		if (cdLoader != null) {
			cdLoader.dismiss();
		}

		ViewUtils.updateNewAppMessage(this);
		mViewUtils.refreshUpdata(apkAdapterSl);
		test();

		MainView.this
				.startService(new Intent(MainView.this, PushService.class));

		// Intent intent = getIntent();
		// if (DOWNAPK.equals(intent.getAction())) {
		// if (null == apkAdapterSl) {
		// return;
		// }
		// String name = intent.getStringExtra("packageName");
		// apkAdapterSl.clickItemByPackageId(name);
		// }
	}

	protected void onStop() {
		atvState = STOP;
		Log.d("apkHall onStop");
		super.onStop();
		ViewUtils.onStop(this);
	}

	@Override
	protected void onDestroy() {
		atvState = DESTROY;
		Log.d("apkHall onDestroy");
		mUpdateMessageHandler.unregister(this);
		apkAdapterSl.onDestroy();
		mHall = null;
		// Log.v(LOGCAT, "kill");
		// android.os.Process.killProcess(android.os.Process.myPid());
		// ActivityManager manager =
		// (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
		// manager.restartPackage(getPackageName());
		// manager.forceStopPackage(getPackageName());
		mViewUtils.onDestroy();
		super.onDestroy();
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (null != popuWin && popuWin.isAboveAnchor()) {
			popuWin.dismiss();
		}
		return super.onKeyUp(keyCode, event);
	}

	public void run(AccountManagerFuture future) {
		String authtoken = null;
		try {
			Bundle mBundle = (Bundle) future.getResult();
			authtoken = mBundle.getString(AccountManager.KEY_AUTHTOKEN);
		} catch (OperationCanceledException e) {
			ViewUtils.checkAccount(this);
			e.printStackTrace();
		} catch (AuthenticatorException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		if (authtoken == null) {
			// this.finish();
		}
	}

	public void onConfigurationChanged(Configuration newConfig) {
		Log.d("onConfigurationChanged");
		super.onConfigurationChanged(newConfig);
		int index1 = installPager.getCurrent();
		int index2 = uninstallPager.getCurrent();
		boolean btnisBusy = isBusy;
		isBusy = false;
		//
		// initView();
		// initData();
		install_sl.invalidate();
		uninstall_sl.invalidate();
		//
		if (index1 != -1) {
			install_sl.setToScreen(index1);
		}
		if (index2 != -1) {
			uninstall_sl.setToScreen(index2);
		}
		setBusy(btnisBusy);
		debugView();
		changeDerma(CommUtils.getDermaId(this));
		if(null != popuWin && popuWin.isShowing()) {
			popuWin.dismiss();
		}
	}

	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case CLIENT_UPDATE:
				updataHall();

			case CLIENT_LOCK:
				ServiceDia.showHallLockScreenOn(MainView.this,
						R.string.lock_client);
				break;
			case App_UPDATE:
				update();
				break;
			case A_INSTALL_BEG: {
				if (1 == PushService.autoinstall) {
					apkAdapterSl.enbleClick(false);
				}
				break;
			}
			case A_INSTALL_END: {
				apkAdapterSl.enbleClick(true);
				break;
			}
			case A_ACCENT_CHK: {
				ViewUtils.checkAccount(MainView.this);
				break;
			}
			case A_MSG_CHK: {
				updataMsgHit();
				break;
			}
			case A_DEL_USER_END: {
				if (null != mDelUser) {
					mDelUser.cancel();
					mDelUser = null;
				}
				break;
			}
			case A_TOAST_HINT: {
				ToastHelp.showToast(MainView.this, msg.arg1, msg.arg2);
				break;
			}
			case PROGRESSBAR_UPDATE:
				if (null == msg.obj) {
					break;
				}
				AppItem app = null;
				if (msg.obj instanceof AppItem) {
					app = (AppItem) msg.obj;
				} else if (msg.obj instanceof Appdb) {
					Appdb db = (Appdb) msg.obj;
					app = apkAdapterSl.getItemById(db.getApkId(), db.getApkVersion());
				}
				if (null == app) {
					break;
				}
				GridAdapter.GridHolder mGridHolder = (GridHolder) ((AppItem) app)
						.getView().getTag();
				ProgressBar pBar = mGridHolder.progressBar;
				if (pBar.getVisibility() != View.VISIBLE) {
					try {
						pBar.setVisibility(View.VISIBLE);
					} catch (Exception e) {
						e.printStackTrace();
					}
					pBar.setMax(msg.arg1);
				}
				pBar.setProgress(msg.arg2);
				break;

			case DOWNLOAD_FINISH:
				if (null == msg.obj) {
					break;
				}
				app = null;
				if (msg.obj instanceof AppItem) {
					app = (AppItem) msg.obj;
				} else if (msg.obj instanceof Appdb) {
					Appdb db = (Appdb) msg.obj;
					app = apkAdapterSl.getItemById(db.getApkId(), db.getApkVersion());
				}
				if (null == app) {
					break;
				}
				mGridHolder = (GridHolder) ((AppItem) app).getView().getTag();
				pBar = mGridHolder.progressBar;
				pBar.setVisibility(View.GONE);
				break;
			case APP_PAGE:
				installPager.setCurrentUindex(msg.arg1);
				break;
			case APK_PAGE:
				uninstallPager.setCurrentUindex(msg.arg1);
				break;
			case APP_INDEX_REFRESH:
				installPager.refreshUindex(install_sl.getScreen(),
						install_sl.getCurScreen());
				break;
			case APK_INDEX_REFRESH:
				uninstallPager.refreshUindex(uninstall_sl.getScreen(),
						uninstall_sl.getCurScreen());
				break;
			case SET_PASSWORD:
				sendIntentPsActiviy(2);
				break;
			case AUTHEN_RESULT:
				Log.i("authen result");
				MainView.this.startService(new Intent(MainView.this,
						PushService.class));
				break;
			}

		}
	};

	private void sendIntentPsActiviy() {
		Intent intent = new Intent(DevicePolicyManager.ACTION_SET_NEW_PASSWORD);
		startActivityForResult(intent, SetPasswordFlag);
		Log.i("start activiyt = " + "Error Password");
	}

	private void sendIntentPsActiviy(int x) {
		String name = HelpUtils.getTopActivityPackageName(this);
		if (!HelpUtils.PSACTIVE.equals(name)) {
			Intent intent = new Intent(
					DevicePolicyManager.ACTION_SET_NEW_PASSWORD);
			startActivityForResult(intent, SetPasswordFlag);
			if (x == 1) {
				Log.i("start activiyt = " + "Main View start");
			} else if (x == 2) {
				Log.i("start activiyt = " + "Deal message");
			}
		}
	}

	public void finishUpdata() {
		setBusy(false);
		ViewUtils.updateNewAppMessage(this);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("rsult=" + requestCode + " " + resultCode);
		if (requestCode == SetPasswordFlag) {
			// pscount = 0;
			DevicePolicyManager dpm = (DevicePolicyManager) this
					.getSystemService(Context.DEVICE_POLICY_SERVICE);

			ComponentName mDeviceComponentName = new ComponentName(this,
					AemmDeviceAdminReceiver.class);

			boolean active = dpm.isAdminActive(mDeviceComponentName);
			boolean result = false;

			if (active) {
				result = dpm.isActivePasswordSufficient();
			} else {
				Intent intent = new Intent(
						DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
				intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
						mDeviceComponentName);
				intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
						R.string.aemm_device_admin_description);
				startActivityForResult(intent, SetPasswordFlag);
			}

			if (result == false) {
				sendIntentPsActiviy();
			} else {
				CommUtils.setPasswordRequire(this, 0);
			}
		}
	}
	private void changeDerma(int id){
		if(id>0){
			this.resources.getDrawable(id);
			hall_layout.setBackgroundDrawable(this.resources.getDrawable(id));
//		hall_layout.setBackgroundResource(id);
		}
	}

	@SuppressWarnings("deprecation")
	private void test() {

		if (DEBUG) {

			ViewUtils
					.addMessage(
							this,
							new HallMessagedb(
									"dfsfsdfsfasdfsafasfasfsafsfsafsfdfadsfa閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷锋伅2閿熸枻鎷峰仠閿熸枻鎷穌fsfsdfsfasdfsafasfasfsafsfsafsfdfadsfadadfsfsdfsfasdfsaffadfsfsdfsfasdfsafasfasfsafsfsaf",
									1000, 10000, 1000, 2));
			ViewUtils
					.addMessage(
							this,
							new HallMessagedb(
									"鎭�閿熸枻鎷峰仠閿熸枻鎷锋伅2閿熸枻鎷峰仠閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹鎭�閿熸枻鎷峰仠閿熸枻鎷锋伅2閿熸枻鎷峰仠閿熸枻鎷锋伅2閿熸枻鎷峰仠閿熸枻鎷锋伅2閿熸枻鎷峰仠閿熸枻鎷锋伅2閿熸枻鎷峰仠閿熸枻鎷锋伅2閿熸枻鎷峰仠閿熸枻鎷锋伅2閿熸枻鎷峰仠閿熸枻鎷锋伅2閿熸枻鎷峰仠閿熸枻鎷锋伅2閿熸枻鎷峰仠閿熸枻鎷锋伅2閿熸枻鎷峰仠閿熸枻鎷锋伅2閿熸枻鎷峰仠閿熸枻鎷锋伅2閿熸枻鎷峰仠閿熸枻鎷锋伅2閿熸枻鎷峰仠閿熸枻鎷锋伅2閿熸枻鎷峰仠閿熸枻鎷锋伅2閿熸枻鎷峰仠閿熸枻鎷�",
									1000, -1, 1000, 23));
			ViewUtils
					.addMessage(
							this,
							new HallMessagedb(
									"re閿熸枻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鎻唻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鎻唻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鎻唻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鎻唻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鎻唻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鎻唻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鎻唻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鎻唻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鎻唻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鎻唻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鎻唻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鎻唻鎷烽敓鏂ゆ嫹閿熸枻鎷�",
									1000, -1, 5000, 12));
			// ViewUtils.addMessage(this,new HallMessagedb(
			// "閿熸枻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鎻唻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鎻唻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鎻唻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鎻唻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鎻唻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鎻唻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鎻唻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鎻唻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鎻唻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鎻唻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鎻唻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鎻唻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鎻唻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鎻唻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鎻唻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鎻唻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鎻唻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鎻唻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鎻唻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鎻唻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鎻唻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鏂ゆ嫹remoall",
			// 14));
			// ViewUtils.addMessage(this,new HallMessagedb(
			// "remoall閿熸枻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鎻唻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鎻唻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鎻唻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鎻唻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鎻唻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鎻唻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鎻唻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鎻唻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鎻唻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鎻唻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鎻唻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鎻唻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鎻唻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鎻唻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鎻唻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鎻唻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鎻唻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鎻唻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鎻唻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鎻唻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鎻唻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鏂ゆ嫹remoall",
			// 11));
			for (int i = 0; i < 40; i++) {
				AppItem app = new AppItem(
						i
								+ "閿熸枻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鎻唻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鏂ゆ嫹",
						Utils.ICONPATH + "/biepengwo.png", Utils.ICONPATH
								+ "/biepengwo_1.png",
						"http://aemm.imolife.com/qq.apk", "hhh");
				app.setApkFlag(this, Appdb.INSTALLED);
				appAdapterSl.addItem(app);
				app.setBase64(false);
				app.setApkVersion("a" + i);
				app.setApkId("a" + i);

				// app.setLocation(i/3,1,i%3);
				app = new AppItem(
						(40 + i) + "閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷�",
						Utils.ICONPATH + "/biepengwo.png",
						Utils.ICONPATH + "/biepengwo_1.png",
						"http://aemm.imolife.com/qq.apk",
						"閿熸枻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鎻唻鎷烽敓鏂ゆ嫹閿熸彮鎲嬫嫹閿熸枻鎷烽敓鏂ゆ嫹DescreiptDescreiptDescreiptDescreiptDescreiptDescreiptDescreiptDescreiptDescreiptDescreiptDescreiptDescreipt");
				app.setApkFlag(this, Appdb.UNINSTALLED);
				app.setBase64(false);
				app.setLocation(i / 3, 0, i % 3);
				app.setApkVersion("a" + i);
				app.setApkId("a" + i);

				apkAdapterSl.addItem(app);
			}
		}

	}

	int i = 0;

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_app_btn:
			AppItem app = new AppItem("閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷�", Utils.ICONPATH
					+ "/biepengwo.png", Utils.ICONPATH + "/biepengwo_1.png",
					"http://aemm.imolife.com/qq.apk", "hhh");
			app.setApkFlag(this, Appdb.INSTALLED);
			app.setApkVersion("a");
			app.setApkId("a");
			appAdapterSl.addItem(app);
			test();
			break;
		case R.id.del_app_btn:
			appAdapterSl.deleteItemByApkId("閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷�", null);
			break;
		case R.id.unable_app_btn:
			// AppItem appItem = null;
			// appAdapterSl.getPackageItem(appItem.getPackageName());
			// if (appItem != null) {
			// appItem.setAble(false);
			// }
			break;
		case R.id.add_apk_btn:
			app = new AppItem(
					"閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷�",
					Utils.ICONPATH + "/biepengwo.png",
					Utils.ICONPATH + "/biepengwo_1.png",
					"http://aemm.imolife.com/qq.apk",
					"Descreiption");
			app.setApkFlag(this, Appdb.NEWAPP);
			apkAdapterSl.addItem(app);
			break;
		case R.id.del_apk_btn:
			apkAdapterSl.deleteItemByApkId("閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷�", null);
			break;
		}
	}

	public final Button.OnClickListener mClick = new Button.OnClickListener() {
		public void onClick(View v) {
//			NotifyResult nr = new NotifyResult();
//			nr.args = new HashMap<String, String>();
//			nr.args.put("message", " ");
//			nr.args.put("acctivityName", " ");
//			NotificationUtils.sentNotificationForNotity(MainView.this, nr);
			if(mMenuDialog==null){
				mMenuDialog=new MenuDialog(MainView.this);
				mMenuDialog.setOnItemClickListener(menuListener);
			}
			mMenuDialog.show();
//			if (1 == AEMMConfig.isPopu) {
//				if (popuWin != null && popuWin.isShowing()) {
//					popuWin.update();
//					return;
//				}
//				popuWin = new AemmPopu(MainView.this);
//				popuWin.setOutsideTouchable(true);
//				popuWin.setFocusable(false);
//				popuWin.setOnClickListener(popuClicklistener);
//				popuWin.showPopu(v, weight, height);
//			} else {
//				clickUpdate();
//			}

		}
	};
	OnItemClickListener menuListener=new OnItemClickListener() {   
		  
        @Override  
        public void onItemClick(AdapterView<?> adp, View view, int location,   
                long arg3) {   
//        	Toast.makeText(MainView.this, location+" click adp "+adp.getAdapter().getItem(location), 0).show();
        	switch(mMenuDialog.getLocalId(location)){
        	case 0:
				clickUpdate();
        		break;
        	case 1:
				User.loadUser(MainView.this);
        		break;
        	case 2:
				InfoMainView.start(MainView.this, InfoMainView.REPAIR_PASSWORD);
        		break;
        	case 3:
				Intent intent = new Intent();
				intent.setAction(ACTION_MSG);
				intent.addCategory(Intent.CATEGORY_DEFAULT);
				MainView.this.startActivity(intent);
        		break;
        	case 4:
				Intent sendmsg = new Intent();
				sendmsg.setAction(SendMsgActivity.ACTION);
				sendmsg.addCategory(Intent.CATEGORY_DEFAULT);
				MainView.this.startActivity(sendmsg); 
        		break;
        	case 5:
				InfoMainView.start(MainView.this, InfoMainView.CHANGE_DERMA);
        		break;
        	case 6:
        		startActivity(new Intent(MainView.this,GpsCameraActivity.class));
        		break;
        	case 7:
        		Intent i=new Intent(Intent.ACTION_MAIN);
        		i.setPackage("com.android.calendar");
        		startActivity(i);
        		break;
        	case 8:
        		EnvaluateDialog m =new EnvaluateDialog(MainView.this);
        		m.show();
        		break;
            case 9:
            	if (null == mDelUser) {
                	mDelUser = new UserQuit(MainView.this).CreateDailog();
            	} else {
            		mDelUser.show();
            	}
        		break;
            }
        	MainView.this.mMenuDialog.dismiss();
        }   
    };
	
	private void clickUpdate() {

		new Thread() {
            @Override
            public void run() {
        		setBusy(true);
        		int result = Update.startUpdate(MainView.this, Update.MANUAL);
        		if (result > 0) {
        			ViewUtils.startUpdate(MainView.this);
        		}
            }
		}.start();
	}
	
	OnClickListener popuClicklistener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (v instanceof TextView ) {
				
				if (null != popuWin) {
					popuWin.dismiss();
				}
				
				TextView tv = (TextView)v;
				
				sendIntent(tv.getId());
			}
		}
		
		private void sendIntent(int id) {
			if (R.id.pmessage == id) {
				Intent intent = new Intent();
				intent.setAction(ACTION_MSG);
				intent.addCategory(Intent.CATEGORY_DEFAULT);
				MainView.this.startActivity(intent);
			}else
			if (R.id.pupdate == id) {
				clickUpdate();
			} else if (R.id.pinfo == id) {
				User.loadUser(MainView.this);
			} else if (R.id.ppsword == id) {
				InfoMainView.start(MainView.this, InfoMainView.REPAIR_PASSWORD);
			}else if(R.id.pderma==id){
				InfoMainView.start(MainView.this, InfoMainView.CHANGE_DERMA);
			}
		}
	};

	/**
	 * btn handle
	 */

	Handler handler_btn = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == STOP_ROTATION_MSG) {

				update_btn.setEnabled(true);
				update_iv.clearAnimation();
				update_iv.setVisibility(View.INVISIBLE);
			} else if (msg.what == START_POTATiON_MSG) {

				update_btn.setEnabled(false);
				update_iv.setVisibility(View.VISIBLE);
				animation.reset();
				update_iv.startAnimation(animation);
			}
			super.handleMessage(msg);
		}
	};

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
			AlertDialog.Builder builder = new Builder(MainView.this);
			builder.setMessage(msg);

			builder.setTitle(title);

			builder.setPositiveButton(R.string.update2,
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							Intent it = new Intent(Intent.ACTION_VIEW, Uri
									.parse(url));
							MainView.this.startActivity(it);
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
			cd.addPositiveButton(R.string.update2, new View.OnClickListener() {
				public void onClick(View v) {
					Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					MainView.this.startActivity(it);
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

		mViewUtils.startDownloadFile(app);
	}

	public void isUninstall(final Context context, final AppItem app) {
		mViewUtils.uninstallApplication(app.getApkPackageName(), null,
				app.getId());
		/*
		 * final CustomDialog cd = new CustomDialog(this);
		 * cd.setCancelable(false); cd.show();
		 * cd.setTitle(R.string.is_uninstall); cd.setMessage( "\""+
		 * app.getApkName()+ "\"?"); cd.addPositiveButton(R.string.uninstall,
		 * new View.OnClickListener() { public void onClick(View v) {
		 * mViewUtils.uninstallApplication(app.getApkPackageName(), null,
		 * app.getId()); cd.dismiss(); } });
		 * cd.addNegativeButton(android.R.string.cancel, new
		 * View.OnClickListener() { public void onClick(View v) { cd.dismiss();
		 * } });
		 */
	}

	private void updataHall() {
		String url = ViewUtils.getHallUpdateAdrress(this);
		if (null != url) {
			ShowUpdata(R.string.hall_updata_title,
					R.string.hall_updata_content, url,
					ViewUtils.isHallupdataMust(this));
		}
		Log.d("is hall need update " + (url != null)
				+ ViewUtils.isHallupdataMust(this));
	}
	
	private void updataMsgHit() {
		ImageView iv = (ImageView)findViewById(R.id.msg_hit);
		if (null != iv) {
			int count = NewsContent.getUnReadCount(this);
			if (count > 0) {
				iv.setVisibility(View.VISIBLE);
			} else {
				iv.setVisibility(View.GONE);
			}
		}
	}

	/**
	 * 閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓锟� * 
	 * @return
	 */
	public boolean isHallAbleInstalled() {
		return ProfessionalFunction.isAllApkInstalledEnabled(this);
	}

	public void setAppEnables(String[] ids, boolean[] enable) {
		apkAdapterSl.setAppAbled(ids, enable);
		appAdapterSl.setAppAbled(ids, enable);
	}

	public void update() {
		apkAdapterSl.toReadApk();
		appAdapterSl.toReadApp();
		ViewUtils.updateNewAppMessage(this);
	}

	LayoutInflater inflater;
	Resources resources;

	public byte atvState;
	public static final byte START = 1, RESUME = 2, PAUSE = 3, STOP = 4,
			DESTROY = 5;
	public int nameW;
	int extraH;
	int titleHeight;
	int indexHeight;
	int lineHeight;
	int bottomHeight;
	int apkDescriptionW;
	static int iconWidth;
	static int iconHeight;
	static int apkIconWidth;
	static int iconsW, iconsH, iconsW2, iconsH2;
	// private int pscount = 0;
	
	LinearLayout hall_layout;
	TextView install_tv;
	ScrollLayout install_sl;
	LinearLayout install_index_lin;

	TextView uninstall_tv;
	ScrollLayout uninstall_sl;
	LinearLayout uninstall_index_lin;

	TextFlipper message_vf;
	ImageView update_iv;
	Button update_btn;
	MenuDialog mMenuDialog;
	AlertDialog mDelUser;

	private final int STOP_ROTATION_MSG = 1;
	private final int START_POTATiON_MSG = 2;
	boolean isBusy;
	private Animation animation;

	public ViewUtils mViewUtils;
	private boolean isDefault = false;
	private static final boolean ISRC = true;
	private ScrollAdapter apkAdapterSl;
	private ScrollAdapter appAdapterSl;
	private CustomDialog cdLoader;
	private PageIndexManager uninstallPager;
	private PageIndexManager installPager;
	private UpdateMessageHandler mUpdateMessageHandler;
	public static DisplayMetrics metric;
	Button add_app_btn, del_app_btn, unable_app_btn;
	Button add_apk_btn, del_apk_btn;
	public static MainView mHall;
	public int lockcount = 0;
	public final static String LOGCAT = "MainView";
	private AemmPopu popuWin;
	public static final Logger Log = LoggerFactory.getLogger(LOGCAT);
	public static final boolean VIEWDEBUG = false;
	public static final boolean DEBUG = false;
	
	public static final byte APP_PAGE = 9;
	public static final byte APK_PAGE = 10;
	
	public static final byte SET_PASSWORD = 12;
	public static final byte APP_INDEX_REFRESH = 13;
	public static final byte APK_INDEX_REFRESH = 14;
	public static final byte AUTHEN_RESULT = 15;
	public static final byte App_UPDATE = 16;
	public static final byte PROGRESSBAR_UPDATE = 17;
	public static final byte DOWNLOAD_FINISH = 18;
	
	public static final byte CLIENT_UPDATE = 19;
	public static final byte CLIENT_LOCK = 20;
	public static final byte A_INSTALL_END = 21;
	public static final byte A_INSTALL_BEG = 22;
	public static final byte A_ACCENT_CHK = 23;
	public static final byte A_MSG_CHK = 24;
	public static final byte A_DEL_USER_END = 25;
	public static final byte A_TOAST_HINT = 26;
}

class iconSort implements Serializable {
	public static String TAG = "MainView";
	private static final long serialVersionUID = -6746352348773246954L;
	static int screen[];
	static int iconWidth, iconHeight, apkIconWidth;
	static int width, height, width2, height2;
	static int marginW, marginH;
	static iconSort best;

	static MainView activity;
	static int psset = 0;
	static int iw_index = 0;
	static int ih_index = 1;
	static int iw2_index = 2;
	static int ih2_index = 3;
	static int uw_index = 4;
	static int uh_index = 5;
	static int uw2_index = 6;
	static int uh2_index = 7;
	public int rcNum[];
	int margin[] = new int[8];
	float mMargin, nMargin;
	int scalNum;
	int scalNum2;
	int count;

	public iconSort() {
	}

	public void calculateMargin(int num[]) {
		if (iconSort.screen == null) {
			iconSort.screen = new int[] { width, height, width2, height2,
					width, height, width2, height2 };
		}
		// new int[]{ic,ir,ic2,ir2,uc,ur,uc2,ur2}
		rcNum = num;
		margin[iw_index] = screen[iw_index] / rcNum[iw_index] - iconWidth;
		margin[ih_index] = screen[ih_index] / rcNum[ih_index] - iconHeight;
		margin[iw2_index] = screen[iw2_index] / rcNum[iw2_index] - iconWidth;
		margin[ih2_index] = screen[ih2_index] / rcNum[ih2_index] - iconHeight;
		margin[uw_index] = screen[uw_index] / rcNum[uw_index] - apkIconWidth;
		margin[uh_index] = screen[uh_index] / rcNum[uh_index] - iconHeight;
		margin[uw2_index] = screen[uw2_index] / rcNum[uw2_index] - apkIconWidth;
		margin[uh2_index] = screen[uh2_index] / rcNum[uh2_index] - iconHeight;
		// Log.d(Hall_Layout_fessional.TAG,"margin["+margin[iw_index]+","+margin[ih_index]+","+margin[iw2_index]+","+margin[ih2_index]
		// +","+margin[uw_index]+","+margin[uh_index]+","+margin[uw2_index]+","+margin[uh2_index]+"]");
		// mMargin=Math.abs(margin[iw_index]/marginH-1);

		// float temp=0;
		// for(int v:margin){
		// temp+=v;
		// }
		// for(int i=margin.length-1;i>0;i--){
		// if((i&1)==1){
		// temp+=Math.abs(margin[i]/marginW-1);
		// }else{
		// temp+=Math.abs(margin[i]/marginH-1);
		// }
		// }
		// if(best==null||best.mMargin>=temp){
		// best=this;
		// best.mMargin=temp;
		// }
		if (best == null || best.count < count) {
			best = this;
		}
	}

	public String getInfo() {
		String info = "size[" + width + "," + height + "," + width2 + ","
				+ height2 + "]\n";
		if (rcNum != null) {
			info += "install:[" + rcNum[iw_index] + "," + rcNum[ih_index] + ","
					+ rcNum[iw2_index] + "," + rcNum[ih2_index] + "]="
					+ (rcNum[iw_index] * rcNum[ih_index]) + "uninstall:["
					+ rcNum[uw_index] + "," + rcNum[uh_index] + ","
					+ rcNum[uw2_index] + "," + rcNum[uh2_index] + "]="
					+ (rcNum[uw_index] * rcNum[uh_index]);
		}
		if (margin != null) {
			info += "margin" + margin[iw_index] + " " + margin[ih_index] + " "
					+ margin[iw2_index] + " " + margin[ih2_index] + "\n"
					+ margin[uw_index] + " " + margin[uh_index] + " "
					+ margin[uw2_index] + " " + margin[uh2_index];
		}
		return info;
	}

	public static void find() {
		int _ir = (int) (iconSort.height / (iconSort.iconHeight * 1.2));

		int _ic = (int) (iconSort.width / (iconWidth * 1.4));
		int _ir2 = (int) (iconSort.height2 / (iconHeight * 1.2));
		int _ic2 = (int) (iconSort.width2 / (iconWidth * 1.4));
		int _ur = (int) (iconSort.height / (iconHeight * 1.2));
		int _uc = (int) (iconSort.width / (apkIconWidth * 1.2));
		int _ur2 = (int) (iconSort.height2 / (iconHeight * 1.2));
		int _uc2 = (int) (iconSort.width2 / (apkIconWidth * 1.2));
		if (_ir == 0) {
			_ir = 1;
		}
		if (_ic == 0) {
			_ic = 1;
		}
		if (_ir2 == 0) {
			_ir2 = 1;
		}
		if (_ic2 == 0) {
			_ic2 = 1;
		}
		if (_ur == 0) {
			_ur = 1;
		}
		if (_uc == 0) {
			_uc = 1;
		}
		if (_ur2 == 0) {
			_ur2 = 1;
		}
		if (_uc2 == 0) {
			_uc2 = 1;
		}
		Log.d(TAG, "find start: [" + _ir + "," + _ic + "," + _ir2 + "," + _ic2
				+ "," + _ur + "," + _uc + "," + _ur2 + "," + _uc2 + "]");
		iconSort.best = null;
		int ucount = 0;
		int icount = 0;
		int scalNum, scalNum2;
		for (int ur = _ur; ur > 0; ur--) {
			for (int uc = _uc; uc > 0; uc--) {
				ucount = ur * uc;
				if (ucount == 1) {
					continue;
				}
				for (int ur2 = _ur2; ur2 > 0; ur2--) {
					for (int uc2 = _uc2; uc2 > 0; uc2--) {
						if (ucount == ur2 * uc2) {
							for (int ir = _ir; ir > 0; ir--) {
								for (int ic = _ic; ic > 0; ic--) {
									if (ic % uc != 0 || ic / uc <= 1) {
										continue;
									}
									scalNum = ic / uc;
									icount = ic * ir;
									for (int ir2 = _ir2; ir2 > 0; ir2--) {
										for (int ic2 = _ic2; ic2 > 0; ic2--) {
											if (ic2 % uc2 != 0
													|| ic2 / uc2 <= 1) {
												continue;
											}
											scalNum2 = ic2 / uc2;
											if (icount == 1) {
												continue;
											}
											if (icount == ir2 * ic2) {
												iconSort is = new iconSort();
												is.count = icount + ucount;
												is.scalNum = scalNum;
												is.scalNum2 = scalNum2;
												is.calculateMargin(new int[] {
														ic, ir, ic2, ir2, uc,
														ur, uc2, ur2 });
												// writeFileData(is.getInfo());
												Log.d(TAG, is.getInfo());
												// Layout_2.start(this, ir, ic,
												// ir2, ic2, ur, uc, ur2, uc2,
												// nameW, apkDescriptionW);
											}
										}
									}
								}
							}
						}
					}
				}

			}
		}
		// close();
		if (iconSort.best != null) {
			Log.d(TAG, "find best case:\n" + iconSort.best.getInfo());
			// writeFileData("best case:\n"+iconSort.best.getInfo());
			// Layout_2.start(this,iconSort.best);
			setScroller();
			iconSort.toSave(activity.getSharedPreferences("dataname",
					Activity.MODE_PRIVATE).edit());
		}
	}

	public static void setScroller() {
		activity.install_sl.ir = iconSort.best.rcNum[iconSort.ih_index];
		activity.install_sl.ic = iconSort.best.rcNum[iconSort.iw_index];
		activity.install_sl.ir2 = iconSort.best.rcNum[iconSort.ih2_index];
		activity.install_sl.ic2 = iconSort.best.rcNum[iconSort.iw2_index];
		activity.install_sl.iconWidth = iconSort.iconWidth;

		activity.uninstall_sl.ir = iconSort.best.rcNum[iconSort.ih_index];
		activity.uninstall_sl.ic = iconSort.best.rcNum[iconSort.iw_index];
		activity.uninstall_sl.ir2 = iconSort.best.rcNum[iconSort.ih2_index];
		activity.uninstall_sl.ic2 = iconSort.best.rcNum[iconSort.iw2_index];
		activity.uninstall_sl.ur = iconSort.best.rcNum[iconSort.uh_index];
		activity.uninstall_sl.uc = iconSort.best.rcNum[iconSort.uw_index];
		activity.uninstall_sl.ur2 = iconSort.best.rcNum[iconSort.uh2_index];
		activity.uninstall_sl.uc2 = iconSort.best.rcNum[iconSort.uw2_index];
		activity.uninstall_sl.iconWidth = iconSort.iconWidth;
		activity.uninstall_sl.scalNum = iconSort.best.scalNum;
		activity.uninstall_sl.scalNum2 = iconSort.best.scalNum2;
		activity.install_sl.isSetRC = true;
		activity.install_sl.isApp = true;
		activity.uninstall_sl.isApp = false;
		activity.uninstall_sl.isSetRC = true;
		Log.d(TAG, "set scroller:\n" + iconSort.best.getInfo());
	}

	// toRread(context.getSharedPreferences(ApkHall.dataname,
	// Activity.MODE_PRIVATE));
	public static boolean toRread(SharedPreferences preferences) {
		boolean result = true;
		iconWidth = preferences.getInt("iconWidth", -1);
		iconHeight = preferences.getInt("iconHeight", -1);
		apkIconWidth = preferences.getInt("apkIconWidth", -1);
		width = preferences.getInt("width", -1);
		height = preferences.getInt("height", -1);
		width2 = preferences.getInt("width2", -1);
		height2 = preferences.getInt("height2", -1);
		marginW = preferences.getInt("marginW", -1);
		marginH = preferences.getInt("marginH", -1);
		best = new iconSort();
		best.rcNum = new int[8];
		for (int i = best.rcNum.length - 1; i >= 0; i--) {
			best.rcNum[i] = preferences.getInt("rcNum" + i, -1);
		}
		best.scalNum = preferences.getInt("scalNum", -1);
		best.scalNum2 = preferences.getInt("scalNum2", -1);
		Log.d(TAG, "to Read:" + best.getInfo());
		if (best.rcNum[0] <= 0) {
			best = null;
			result = false;
		}
		return result;
	}

	public static void toSave(SharedPreferences.Editor editor) {
		editor.putInt("iconWidth", iconWidth);
		editor.putInt("iconHeight", iconHeight);
		editor.putInt("apkIconWidth", apkIconWidth);
		editor.putInt("width", width);
		editor.putInt("height", height);
		editor.putInt("width2", width2);
		editor.putInt("height2", height2);
		editor.putInt("marginW", marginW);
		editor.putInt("marginH", marginH);
		if (best != null) {
			for (int i = best.rcNum.length - 1; i >= 0; i--) {
				editor.putInt("rcNum" + i, best.rcNum[i]);
			}
			editor.putInt("scalNum", best.scalNum);
			editor.putInt("scalNum2", best.scalNum2);
		}
		editor.commit();
		Log.d(TAG, "to save:" + best.getInfo());
	}
}