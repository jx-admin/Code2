package com.aess.aemm.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.aess.aemm.R;
import com.aess.aemm.db.ApkContent;
import com.aess.aemm.push.PushService;
import com.aess.aemm.view.ScrollLayout.onMoveListener;
import com.aess.aemm.view.data.Appdb;
import com.aess.aemm.view.log.Logger;
import com.aess.aemm.view.log.LoggerFactory;
import com.aess.aemm.view.sso.SSO;

public class ScrollAdapter extends GridAdapter {

	public final static String LOGCAT = "ScrollAdapter";
	public static final Logger Log = LoggerFactory.getLogger(LOGCAT);
	protected ScrollLayout installedSl;
	protected MainView context;
	protected LayoutInflater mInflater;
	List<View> list;
	private AppItem editApp;
	private boolean enbleClick = true;

	public ScrollAdapter(MainView c) {
		super();
		Log.d("ScrollAdapter structor");
		this.context = c;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		list = new ArrayList<View>();
	}

	// public void clickItemByPackageId(String id) {
	// if (null == id || id.length() < 1) {
	// return;
	// }
	// if (null == list) {
	// return;
	// }
	// for (int x = 0; x < list.size(); x++) {
	// View item = list.get(0);
	// GridHolder holder = (GridHolder) item.getTag();
	// if (null == holder) {
	// continue;
	// }
	// if (id.equals(holder.app.getApkPackageName())) {
	// ImageView iv = holder.appImage;
	// if (null != iv) {
	// mItemClicker.onClick(item);
	// }
	// }
	// }
	// }

	public void enbleClick(boolean value) {
		if (0 == PushService.autoinstall) {
			return;
		}
		enbleClick = value;
	}

	public void addItem(AppItem item) {
		setItemView(item);
		if (item.getApkFlag() == Appdb.NEWAPP) {
			list.add(0, item.getView());
			installedSl.addView(item.getView(), 0);
		} else {
			installedSl.addView(item.getView());
			list.add(item.getView());
		}
	}

	/**
	 * @param apkId
	 *            app package id from the server
	 * @param version
	 * @return
	 */
	public int deleteItemByApkId(String apkId, String version) {
		int location = -1;
		if (apkId == null || version == null) {
			return location;
		}
		AppItem app;
		int i = 0;
		int count = installedSl.getChildCount();
		for (; i < count; i++) {
			View v = installedSl.getChildAt(i);
			app = ((GridAdapter.GridHolder) v.getTag()).app;
			if (app.getApkId().equals(apkId)
					&& app.getApkVersion().equals(version)) {
				Log.d("count =" + count + "i " + i + app.getApkName()
						+ " is delete.");
				installedSl.removeViewAt(i);
				list.remove(i);
				location = i;
				break;
			}
		}
		return location;
	}

	/**
	 * @param pakage
	 *            the pakage of the application not from server
	 * @param version
	 * @return
	 */
	public int deleteItemByPackageVersion(String pakage, String version) {
		int location = -1;
		if (pakage == null || version == null) {
			return location;
		}
		AppItem app;
		int i = 0;
		int count = installedSl.getChildCount();
		for (; i < count; i++) {
			View v = installedSl.getChildAt(i);
			app = ((GridAdapter.GridHolder) v.getTag()).app;
			if (app.getApkPackageName().equals(pakage)
					&& app.getApkVersionClient().equals(version)) {
				Log.d("count =" + count + "i " + i + app.getApkName()
						+ " is delete.");
				installedSl.removeViewAt(i);
				list.remove(i);
				location = i;
				break;
			}
		}
		return location;
	}

	public int deleteItem(String packageName) {
		int location = -1;
		AppItem app;
		int count = installedSl.getChildCount();
		for (int i = 0; i < count; i++) {
			View v = installedSl.getChildAt(i);
			app = ((GridAdapter.GridHolder) v.getTag()).app;
			if (app.getApkPackageName().equals(packageName)) {
				Log.d("count =" + count + "i " + i + app.getApkName()
						+ " is delete.");
				installedSl.removeViewAt(i);
				list.remove(i);
				location = i;
				break;
			}
		}
		return location;
	}

	private void setItemView(AppItem item) {
		GridHolder holder;
		if (item.getView() != null) {
			holder = (GridHolder) item.getView().getTag();
		} else {
			View hview = mInflater.inflate(R.layout.apk_item, null);
			holder = new GridHolder();
			hview.setTag(holder);
			item.setView(hview);
			// holder.index=index;
			holder.app = item;
			holder.appImage = (ImageView) hview.findViewById(R.id.icon_iv);
			holder.editImage = (ImageView) hview.findViewById(R.id.edit_iv);
			holder.progressBar = (ProgressBar) hview.findViewById(R.id.load_pb);
			holder.appName = (TextView) hview.findViewById(R.id.name_tv);
			holder.appDecrip = (TextView) hview
					.findViewById(R.id.apk_descrip_tv);
			holder.appName.getLayoutParams().width = context.nameW;
			holder.appDecrip.getLayoutParams().width = context.apkDescriptionW;
			holder.appImage.setTag(holder);
			holder.appImage.setOnClickListener(mItemClicker);
			holder.appImage.setOnLongClickListener(mItemLongClicker);
			holder.appName.setText(item.getApkName());
			holder.editImage.setTag(holder);
			holder.editImage.setOnClickListener(mItemClicker);
		}
		switch (item.getApkFlag()) {
		case Appdb.NEWAPP:
			holder.editImage.setImageResource(R.drawable.star);
			holder.editImage.setVisibility(View.VISIBLE);
			holder.appImage.setImageBitmap(item.getBitmapGray());
			holder.appDecrip.setVisibility(View.VISIBLE);
			if (item.getApkDesc() == null) {
				holder.appDecrip.setText("");
			} else {
				holder.appDecrip.setText(item.getApkDesc());
			}
			break;
		case Appdb.UNINSTALLED:// =3;
			holder.appImage.setImageBitmap(item.getBitmapGray());
			holder.appDecrip.setVisibility(View.VISIBLE);
			if (item.getApkDesc() == null) {
				holder.appDecrip.setText("");
			} else {
				holder.appDecrip.setText(item.getApkDesc());
			}
			break;
		case Appdb.INSTALLED:
			if (item.isApkRunningEnabled()) {
				holder.appImage.setImageBitmap(item.getBitmapColor());
			} else {
				holder.appImage.setImageBitmap(item.getBitmapGray());
			}
			break;
		case Appdb.REMOTE_UNINSTALL:
			if (item.isApkRunningEnabled()) {
				holder.appImage.setImageBitmap(item.getBitmapColor());
			} else {
				holder.appImage.setImageBitmap(item.getBitmapGray());
			}
			if (ViewUtils.isProfessionalVertion(context)) {
				holder.editImage.setVisibility(View.VISIBLE);
				holder.editImage.setImageResource(R.drawable.del_silence);
			} else {
				holder.editImage.setVisibility(View.VISIBLE);
				holder.editImage.setImageResource(R.drawable.del_silence);
			}
			break;
		default:
			holder.appImage.setImageBitmap(item.getBitmapGray());
			break;
		}

	}

	public int getCount() {
		if (installedSl == null) {
			return 0;
		}
		int count = installedSl.getChildCount();
		Log.d("getCount:" + count);
		return count;
	}

	public Object getItem(int index) {
		if (index >= installedSl.getChildCount()) {
			Log.d("getItem out of index" + index);
			return null;
		}
		Log.d("getItem:" + index);
		View v = installedSl.getChildAt(index);
		AppItem app = ((GridAdapter.GridHolder) v.getTag()).app;
		return app;
	}

	public AppItem getItemById(String apkId, String version) {
		if (apkId == null || version == null) {
			return null;
		}
		AppItem app;
		View v;
		int count = installedSl.getChildCount();
		for (int i = 0; i < count; i++) {
			v = installedSl.getChildAt(i);
			app = ((GridAdapter.GridHolder) v.getTag()).app;
			if (app == null) {
				Log.d("ScrollAdapter getItem null " + i);
				continue;
			}
			if (apkId.equals(app.getApkId())
					&& version.equals(app.getApkVersion())) {
				return app;
			}
		}
		return null;
	}

	public AppItem getPackageItem(String pkg, String version) {
		if (pkg == null || version == null) {
			return null;
		}
		AppItem app;
		View v;
		int count = installedSl.getChildCount();
		for (int i = 0; i < count; i++) {
			v = installedSl.getChildAt(i);
			app = ((GridAdapter.GridHolder) v.getTag()).app;
			if (app == null) {
				Log.d("ScrollAdapter getItem null " + i);
				continue;
			}
			if (version.equals(app.getApkVersionClient())
					&& pkg.equals(app.getApkPackageName())) {
				return app;
			}
		}
		return null;
	}

	public AppItem getPackageItem(String packagename) {
		if (packagename == null) {
			return null;
		}
		AppItem app;
		View v;
		int count = installedSl.getChildCount();
		for (int i = 0; i < count; i++) {
			v = installedSl.getChildAt(i);
			app = ((GridAdapter.GridHolder) v.getTag()).app;
			if (app == null) {
				Log.d("ScrollAdapter getItem null " + i);
				continue;
			}
			if (packagename.equals(app.getApkPackageName())) {
				return app;
			}
		}
		return null;
	}

	public long getItemId(int index) {
		Log.d("getItemId:" + index);
		return index;
	}

	public View getView(int index, View convertView, ViewGroup parent) {
		Log.d("getView:" + index + " w=" + parent.getWidth() + " h="
				+ parent.getHeight());
		// convertView=pageLs.get(index);
		return list.get(index);
	}

	public void setAppAbled(String[] ids, boolean[] enable) {
		if (ids == null || enable == null) {
			Log.e("setAppAbled->ids||enable is null");
			return;
		}
		int idLength = ids.length;
		int ableLength = enable.length;
		idLength = idLength < ableLength ? idLength : ableLength;
		int count = installedSl.getChildCount();
		View v;
		AppItem app;
		for (int i = 0; i < idLength; i++) {
			Log.d(i + "--apKid:" + ids[i] + " enalbe:" + enable[i]);
			for (int j = 0; j < count; j++) {
				v = installedSl.getChildAt(j);
				app = ((GridAdapter.GridHolder) v.getTag()).app;
				if (app.getApkId().equals(ids[i])) {
					app.setApkRunningEnabled(enable[i]);
					// break;可能多个不同版本
				}
				Log.d(i + "  apKid:" + app.getApkId() + " enalbe:"
						+ app.isApkRunningEnabled());
			}
		}
	}

	// public void toSave() {
	// int count = installedSl.getChildCount();
	// View v;
	// AppItem app;
	// Log.d("apk to save db");
	// for (int i = 0; i < count; i++) {
	// v = installedSl.getChildAt(i);
	// app = ((GridAdapter.GridHolder) v.getTag()).app;
	// if (app == null) {
	// Log.d("app is null.");
	// continue;
	// }
	// Log.d("app:" + app.getInfo());
	// ViewUtils.toSaveApp(context, app);
	// }
	// }
	public void toReadApp() {
		Log.d("apk read from db");
		if (list != null) {
			list.clear();
		}
		if (installedSl != null) {
			installedSl.removeAllViews();
		}
		toRead(context, Appdb.INSTALLED);
		toRead(context, Appdb.REMOTE_UNINSTALL);
	}

	public void toReadApk() {
		Log.d("apk read from db");
		if (list != null) {
			list.clear();
		}
		if (installedSl != null) {
			installedSl.removeAllViews();
		}
		toRead(context, Appdb.NEWAPP);
		toRead(context, Appdb.UNINSTALLED);
		toRead(context, Appdb.DOWNLOAD);
	}

	public void toRead(Context context, byte flag) {

		List<AppItem> dataLs = null;
		List<ApkContent> apkUninstallList = ViewUtils.readAllApp(context, flag);
		if (apkUninstallList != null && apkUninstallList.size() > 0) {
			dataLs = new ArrayList<AppItem>();
			AppItem app;
			for (ApkContent pro : apkUninstallList) {
				if (flag != Appdb.INSTALLED && flag != Appdb.REMOTE_UNINSTALL
						&& pro.mApkPublished != 1) {
					continue;
				}
				app = new AppItem(pro);
				dataLs.add(app);
				addItem(app);
				Log.d("read uninstalled app:" + app.getInfo());
			}
		}
	}

	public int getNewppCnt() {
		int count = 0;
		AppItem app;
		int size = list.size();
		for (int i = 0; i < size; i++) {
			app = ((GridAdapter.GridHolder) list.get(i).getTag()).app;
			if (app.getApkFlag() == Appdb.NEWAPP) {
				count++;
			}
		}
		return count;
	}

	public void setScroll(ScrollLayout installedSl) {
		if (this.installedSl != null) {
			this.installedSl.removeAllViews();
		}
		this.installedSl = installedSl;
		this.installedSl.setOnClickListener(scrollClicker);
	}

	public void initData() {
		int count = list.size();
		Log.d("ScrollAdapter initData count=" + count);
		for (int i = 0; i < count; i++) {
			View v = list.get(i);
			@SuppressWarnings("unused")
			PagePosition mPagePosition = ((GridHolder) v.getTag()).app
					.getLocation();
			installedSl.addView(v);
		}
	}

	public void onDestroy() {
		AppItem app;
		View v;
		int count = list.size();
		for (int i = 0; i < count; i++) {
			v = list.get(i);
			app = ((GridAdapter.GridHolder) v.getTag()).app;
			if (app.getApkFlag() == AppItem.NEWAPP) {
				app.setApkFlag(context, AppItem.UNINSTALLED);
			}
		}
		list.clear();
		installedSl.removeAllViews();
	}

	private android.view.View.OnClickListener mItemClicker = new android.view.View.OnClickListener() {

		public void onClick(View view) {
			Log.d("onClick");
			// if(longCllick){
			// longCllick=false;
			// return;
			// }
			GridAdapter.GridHolder holder = (GridAdapter.GridHolder) view
					.getTag();
			AppItem app = holder.app;
			Log.d("" + app.getApkFlag());
			if (view.getId() == R.id.icon_iv) {
				switch (app.getApkFlag()) {
				case Appdb.NEWAPP:
					app.setApkFlag(context, AppItem.UNINSTALLED);
					holder.editImage.setVisibility(View.GONE);
					// ApkProfileContent apc=app.toApkProfileContent();
					// int
					// r=ApkProfileContent.updateApkContentwithRowId(context,
					// apc.mId, apc.toContentValues());
					ViewUtils.updateNewAppMessage(context);
				case Appdb.UNINSTALLED:
					if (false == enbleClick) {
						break;
					}
					if (!((MainView) context).isHallAbleInstalled()) {
						CustomDialog.createDialog(context,
								R.string.install_disable);
						return;
					}
					ProgressBar pbar = ((GridHolder) ((AppItem) app).getView()
							.getTag()).progressBar;
					if (pbar.getVisibility() != View.VISIBLE) {
						pbar.setProgress(0);
						pbar.setVisibility(View.VISIBLE);
						context.downloadApp(context, app);
					}
					break;
				case Appdb.REMOTE_UNINSTALL:
					if (editApp != null) {
						if (!app.equals(editApp)) {
							editApp.setEdit(false);
							editApp = null;
							return;
						}
						// installedSl.setView(app.getView());
						// editApp.setEdit(false);
						// editApp=null;
						return;
					}
					break;
				case Appdb.INSTALLED:// =2,
					if (editApp != null) {
						if (!app.equals(editApp)) {
							editApp.setEdit(false);
							editApp = null;
							return;
						}
						// installedSl.setView(app.getView());
						// editApp.setEdit(false);
						// editApp=null;
						return;
					}
					if (!app.isApkRunningEnabled()) {
						CustomDialog
								.createDialog(context, R.string.app_disable);
						return;
					}
					String apkPackageName = app.getApkPackageName();
					if (apkPackageName != null) {
						Intent mLaunchIntent = context.getPackageManager()
								.getLaunchIntentForPackage(apkPackageName);
						if (mLaunchIntent != null) {
							List<ResolveInfo> list = context
									.getPackageManager().queryIntentActivities(
											mLaunchIntent, 0);
							if (list != null && list.size() > 0) {

//								if (SSO.isSSO(apkPackageName)) {
//									SSO.startActivity(context);
//									break;
//								}
								String account = app.getApkAccount();
								if (null != account && account.length() > 1) {
									SSO.startActivity(context, account);
									break;
								}
								context.startActivity(mLaunchIntent);
								break;
							}
						} else {
							Intent mainIntent = new Intent(Intent.ACTION_MAIN,
									null);
							mainIntent.setPackage(app.getApkPackageName());

							List<ResolveInfo> list = context
									.getPackageManager().queryIntentActivities(
											mainIntent, 0);
							if (list != null && list.size() > 0) {
								Intent intent = new Intent();
								String packname = list.get(0).activityInfo.applicationInfo.packageName;
								intent.setClassName(packname,
										list.get(0).activityInfo.name);

//								if (SSO.isSSO(packname)) {
//									SSO.startActivity(context);
//									break;
//								}
								String account = app.getApkAccount();
								if (null != account && account.length() > 1) {
									SSO.startActivity(context, account);
									break;
								}
								context.startActivity(intent);
								break;
							}
						}
					}
					toMoveUninstall(app);
					break;
				}
			} else {
				if (app.getApkFlag() == Appdb.INSTALLED) {
					view.setVisibility(View.GONE);
					app.setEdit(false);
					editApp = null;
					if (app.getApkPackageName() != null) {
						context.isUninstall(context, app);
						// context.mViewUtils.uninstallApplication(app.getApkPackageName(),
						// null, app.getId());
					} else {
						toMoveUninstall(app);
					}
				} else if (app.getApkFlag() == Appdb.REMOTE_UNINSTALL) {
					app.setEdit(false);
					editApp = null;
					if (app.getApkPackageName() != null) {
						context.isUninstall(context, app);
						// context.mViewUtils.uninstallApplication(app.getApkPackageName(),
						// null, app.getId());
					} else {
						toMoveUninstall(app);
						view.setVisibility(View.GONE);
					}
				}
			}
		}

	};
	boolean longCllick;
	View v;
	Vibrator vibrator;
	private android.view.View.OnLongClickListener mItemLongClicker = new android.view.View.OnLongClickListener() {

		public boolean onLongClick(View view) {
			Log.d("onLongClick");
			AppItem app = ((GridHolder) view.getTag()).app;
			if (app.getApkFlag() == Appdb.NEWAPP
					|| app.getApkFlag() == Appdb.UNINSTALLED) {
				GridAdapter.GridHolder holder = (GridAdapter.GridHolder) view
						.getTag();
				view = holder.app.getView();
				Log.d("Long click");
				installedSl.setView(view);
				return true;
			}
			if (!app.equals(editApp)) {
				installedSl.mMoveListener = mMoveListener;
				if (mMoveListener.isMove) {
					return false;
				}
				if (editApp != null) {
					editApp.setEdit(false);
				}
				if (app.getApkFlag() == Appdb.INSTALLED) {
					app.setEdit(true);
					editApp = app;
				}
				v = view;

				longCllick = true;

				vibrator = (Vibrator) context
						.getSystemService(Activity.VIBRATOR_SERVICE);
				// long[] pattern = {800, 50, 400, 30}; // OFF/ON/OFF/ON...
				// vibrator.vibrate(pattern, -1);
				vibrator.vibrate(100);
				// -1不重复，非-1为从pattern的指定下标开始重复

				// GridAdapter.GridHolder holder = (GridAdapter.GridHolder)
				// view.getTag();
				// view=holder.app.getView();
				// Log.d("Long click");
				// installedSl.setView(view);
			}
			return false;
		}
	};

	abstract class MoveListener implements onMoveListener {
		public boolean isMove = false;
	}

	MoveListener mMoveListener = new MoveListener() {
		@Override
		public void move() {
			if (longCllick) {
				longCllick = false;
				if (null != editApp) {
					editApp.setEdit(false);
				}
				editApp = null;

				if (v == null) {
					return;
				}
				installedSl.setView(((GridHolder) v.getTag()).app.getView());
				isMove = true;
			}
		}

		@Override
		public void cancel() {
			isMove = false;
		}

	};

	OnClickListener scrollClicker = new OnClickListener() {
		public void onClick(View arg0) {

			if (editApp != null) {
				editApp.setEdit(false);
				editApp = null;

				installedSl.setView(null);
			}
		}
	};

	private void toMoveUninstall(final AppItem app) {
		final CustomDialog cd = new CustomDialog(context);
		cd.setCancelable(true);
		cd.show();
		cd.setMessage(R.string.app_unexsit);
		cd.addPositiveButton(R.string.sure, new View.OnClickListener() {
			public void onClick(View v) {
				if (app.getApkId() != null && app.getApkVersion() != null) {
					// context.delAppItem(app.getApkId(),app.getApkVersion());
					// context.addApkItem(app);
				} else {
					// ((Main)context).delAppItem(app.getApkId());
					// CustomDialog.createDialog(context,"程序启动错误:"+app.getApkName());
				}
				cd.dismiss();
			}
		});
		cd.addNegativeButton(R.string.cancel, new View.OnClickListener() {
			public void onClick(View v) {
				cd.dismiss();
			}
		});
	}
}
