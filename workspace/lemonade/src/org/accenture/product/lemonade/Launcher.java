package org.accenture.product.lemonade;

import static android.util.Log.d;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.accenture.product.lemonade.ItemInfo.EditAction;
import org.accenture.product.lemonade.actions.DefaultAction;
import org.accenture.product.lemonade.actions.LauncherActions;
import org.accenture.product.lemonade.appdb.AppDB;
import org.accenture.product.lemonade.content.SceneDateBaseAdapter;
import org.accenture.product.lemonade.content.WidgetDataBaseAdapter;
import org.accenture.product.lemonade.model.SceneBean;
import org.accenture.product.lemonade.model.TypeBean;
import org.accenture.product.lemonade.model.WallpapersBean;
import org.accenture.product.lemonade.model.WidgetBean;
import org.accenture.product.lemonade.model.WidgetBean.WidgetEnum;
import org.accenture.product.lemonade.quickactionbar.ActionItem;
import org.accenture.product.lemonade.quickactionbar.QuickAction;
import org.accenture.product.lemonade.receiver.WidgetReceiver;
import org.accenture.product.lemonade.settings.LauncherSettings;
import org.accenture.product.lemonade.settings.Preferences;
import org.accenture.product.lemonade.settings.LauncherSettings.Favorites;
import org.accenture.product.lemonade.util.ResourcesUtil;
import org.accenture.product.lemonade.view.LauncherAppWidgetHost;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.app.WallpaperInfo;
import android.app.WallpaperManager;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Intent.ShortcutIconResource;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.LiveFolders;
import android.text.Selection;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.TextKeyListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.HapticFeedbackConstants;
import android.view.InflateException;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.LayoutInflater.Factory;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;

import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Default launcher application.
 */
public final class Launcher extends Activity implements View.OnClickListener, OnLongClickListener, LauncherModel.Callbacks,
		AllAppsView.Watcher
{
	public static Launcher luanchr;

	static final String TAG = "Launcher";
	static final boolean LOGD = false;

	static final boolean PROFILE_STARTUP = false;

	private static final int WALLPAPER_SCREENS_SPAN = 2;

	private static final int MENU_GROUP_HOMESCREEN = 1;
	private static final int MENU_GROUP_DRAWER = MENU_GROUP_HOMESCREEN + 1;
	private static final int MENU_GROUP_ADD = MENU_GROUP_DRAWER + 1;

	private static final int MENU_ADD = Menu.FIRST + 1;
	private static final int MENU_WALLPAPER_SETTINGS = MENU_ADD + 1;
	private static final int MENU_SEARCH = MENU_WALLPAPER_SETTINGS + 1;
	private static final int MENU_DRAWER_ADD_FOLDER = MENU_SEARCH + 1;
	private static final int MENU_PERSONALIZATION = MENU_DRAWER_ADD_FOLDER + 1;
	private static final int MENU_NOTIFICATIONS = MENU_PERSONALIZATION + 1;

	private static final int REQUEST_CREATE_SHORTCUT = 1;
	private static final int REQUEST_CREATE_LIVE_FOLDER = 4;
	private static final int REQUEST_CREATE_APPWIDGET = 5;
	private static final int REQUEST_PICK_APPLICATION = 6;
	private static final int REQUEST_PICK_SHORTCUT = 7;
	private static final int REQUEST_PICK_LIVE_FOLDER = 8;
	private static final int REQUEST_PICK_APPWIDGET = 9;
	private static final int REQUEST_PICK_WALLPAPER = 10;
	private static final int REQUEST_PICK_ANYCUT = 11;
	static final int REQUEST_EDIT_SHIRTCUT = 12;
	private static final int REQUEST_PICK_LIVE_FOLDER_DRAWER = 13;
	private static final int REQUEST_CREATE_LIVE_FOLDER_DRAWER = 14;
	private static final int REQUEST_HOME_SWITCHER = 15;

	static final String EXTRA_SHORTCUT_DUPLICATE = "duplicate";

	static int SCREEN_COUNT = 5;
	static final int DEFAULT_SCREEN = 2;
	static final int NUMBER_CELLS_X = 4;
	static final int NUMBER_CELLS_Y = 4;

	static final int DIALOG_CREATE_SHORTCUT = 1;
	static final int DIALOG_RENAME_FOLDER = 2;

	private static final String PREFERENCES = "launcher.preferences";

	// Type: int
	private static final String RUNTIME_STATE_CURRENT_SCREEN = "launcher.current_screen";
	// Type: boolean
	private static final String RUNTIME_STATE_ALL_APPS_FOLDER = "launcher.all_apps_folder";
	// Type: long
	private static final String RUNTIME_STATE_USER_FOLDERS = "launcher.user_folder";
	// Type: int
	private static final String RUNTIME_STATE_PENDING_ADD_SCREEN = "launcher.add_screen";
	// Type: int
	private static final String RUNTIME_STATE_PENDING_ADD_CELL_X = "launcher.add_cellX";
	// Type: int
	private static final String RUNTIME_STATE_PENDING_ADD_CELL_Y = "launcher.add_cellY";
	// Type: int
	private static final String RUNTIME_STATE_PENDING_ADD_SPAN_X = "launcher.add_spanX";
	// Type: int
	private static final String RUNTIME_STATE_PENDING_ADD_SPAN_Y = "launcher.add_spanY";
	// Type: int
	private static final String RUNTIME_STATE_PENDING_ADD_COUNT_X = "launcher.add_countX";
	// Type: int
	private static final String RUNTIME_STATE_PENDING_ADD_COUNT_Y = "launcher.add_countY";
	// Type: int[]
	private static final String RUNTIME_STATE_PENDING_ADD_OCCUPIED_CELLS = "launcher.add_occupied_cells";
	// Type: boolean
	private static final String RUNTIME_STATE_PENDING_FOLDER_RENAME = "launcher.rename_folder";
	// Type: long
	private static final String RUNTIME_STATE_PENDING_FOLDER_RENAME_ID = "launcher.rename_folder_id";

	static final int APPWIDGET_HOST_ID = 1024;

	private static final Object sLock = new Object();
	private static int sScreen = DEFAULT_SCREEN;

	private final BroadcastReceiver mCloseSystemDialogsReceiver = new CloseSystemDialogsIntentReceiver();
	// private final ContentObserver mWidgetObserver = new
	// AppWidgetResetObserver();

	private LayoutInflater mInflater;

	private DragController mDragController;
	private Workspace mWorkspace;
	public static int FRONT_SCREEN_COUNT;

	// private AppWidgetManager mAppWidgetManager;
	// private LauncherAppWidgetHost mAppWidgetHost;

	private CellLayout.CellInfo mAddItemCellInfo;
	private CellLayout.CellInfo mMenuAddInfo;
	private final int[] mCellCoordinates = new int[2];
	private FolderInfo mFolderInfo;

	private DeleteZone mDeleteZone;
	private HandleView mHandleView;
	private AllAppsView mAllAppsGrid;

	private Bundle mSavedState;

	private SpannableStringBuilder mDefaultKeySsb = null;

	private boolean mWorkspaceLoading = true;
	private boolean mIsWidgetEditMode = false;
	private ResizeViewHandler mScreensEditor = null;
	// private LauncherAppWidgetInfo mEditingAppWidget = null;

	private boolean mPaused = true;
	private boolean mRestoring;
	private boolean mWaitingForResult;
	private boolean mOnResumeNeedsLoad;

	private Bundle mSavedInstanceState;

	private LauncherModel mModel;
	private IconCache mIconCache;
	private AppDB mAppDB;

	private DragLayer mDragLayer;
	private static LocaleConfiguration sLocaleConfiguration = null;

	private final ArrayList<ItemInfo> mDesktopItems = new ArrayList<ItemInfo>();
	private static HashMap<Long, FolderInfo> sFolders = new HashMap<Long, FolderInfo>();

	private ImageView mPreviousView;
	private ImageView mNextView;

	private final int mHotseatConfig = 2;
	private Intent[] mHotseats = null;// do not delete this..
	private Drawable[] mHotseatIcons = null;
	private CharSequence[] mHotseatLabels = null;
	private LauncherAppWidgetHost mAppWidgetHost;
	private AppWidgetManager mAppWidgetManager;
	private static final String EXTRA_CUSTOM_WIDGET = "custom_widget";

	private static final String SHARE_WIDGET_ID = "widgetId";

	private static final String SHARE_WIDGET_ = "_";

	private Object mStatusBarManager = new Object();
	private Method expandStatusBar;

	LockReceiver lockReceiver;

	// KeyguardManager keyguardManager;
	// KeyguardLock keyguardLock;

	private boolean mWorkspaceLoaded = false;
	
	
	Dialog menuDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		luanchr = this;
		// if (org.accenture.product.lemonade.util.Configuration.debug)
		// {
		// // 测试获取当前主题
		//
		// SceneBean sceneBean = getCurrentScene();
		//
		//
		// }

		ResourcesUtil.getInstance().refresh();

		SCREEN_COUNT = ResourcesUtil.getInstance()._CurrentSceneBean.getScreenCount();

		LauncherApplication app = ((LauncherApplication) getApplication());
		mModel = app.setLauncher(this);
		mIconCache = app.getIconCache();
		mAppDB = app.getAppDB();
		mDragController = new DragController(this);
		mInflater = getLayoutInflater();
		Preferences.getInstance().setLauncher(this);

		mAppWidgetManager = AppWidgetManager.getInstance(this);
		mAppWidgetHost = new LauncherAppWidgetHost(this, APPWIDGET_HOST_ID);
		mAppWidgetHost.startListening();

		if (PROFILE_STARTUP)
		{
			android.os.Debug.startMethodTracing(Environment.getExternalStorageDirectory() + "/launcher");
		}

		loadHotseats();
		checkForLocaleChange();
		setWallpaperDimension();

		setContentView(R.layout.launcher);
		setupViews();

		// registerContentObservers();

		lockAllApps();

		mSavedState = savedInstanceState;
		restoreState(mSavedState);

		if (PROFILE_STARTUP)
		{
			android.os.Debug.stopMethodTracing();
		}

		if (!mRestoring)
		{
			mModel.startLoader(this, true);
		}

		// For handling default keys
		mDefaultKeySsb = new SpannableStringBuilder();
		Selection.setSelection(mDefaultKeySsb, 0);

		IntentFilter filter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
		registerReceiver(mCloseSystemDialogsReceiver, filter);
		LauncherActions.getInstance().init(this);

		// 启动锁屏服务
		Intent intent = new Intent(this, LockService.class);
		startService(intent);

	}

	/**
	 * 得到当前场景
	 * 
	 * @return
	 */
	public SceneBean getCurrentScene()
	{
		SceneDateBaseAdapter adapter = SceneDateBaseAdapter.getInstance();
		adapter.open();
		SceneBean sceneBean = adapter.getCurrentScene();
		// System.out.println(sceneBean.getInfo());
		adapter.close();
		return sceneBean;
	}

	private void checkForLocaleChange()
	{
		if (sLocaleConfiguration == null)
		{
			new AsyncTask<Void, Void, LocaleConfiguration>()
			{
				@Override
				protected LocaleConfiguration doInBackground(Void... unused)
				{
					LocaleConfiguration localeConfiguration = new LocaleConfiguration();
					readConfiguration(Launcher.this, localeConfiguration);
					return localeConfiguration;
				}

				@Override
				protected void onPostExecute(LocaleConfiguration result)
				{
					sLocaleConfiguration = result;
					checkForLocaleChange(); // recursive, but now with a locale
					// configuration
				}
			}.execute();
			return;
		}

		final Configuration configuration = getResources().getConfiguration();

		final String previousLocale = sLocaleConfiguration.locale;
		final String locale = configuration.locale.toString();

		final int previousMcc = sLocaleConfiguration.mcc;
		final int mcc = configuration.mcc;

		final int previousMnc = sLocaleConfiguration.mnc;
		final int mnc = configuration.mnc;

		boolean localeChanged = !locale.equals(previousLocale) || mcc != previousMcc || mnc != previousMnc;

		if (localeChanged)
		{
			sLocaleConfiguration.locale = locale;
			sLocaleConfiguration.mcc = mcc;
			sLocaleConfiguration.mnc = mnc;

			mIconCache.flush();
			loadHotseats();

			final LocaleConfiguration localeConfiguration = sLocaleConfiguration;
			new Thread("WriteLocaleConfiguration")
			{
				@Override
				public void run()
				{
					mIconCache.flush();
					mAppDB.updateLocale(locale);
					writeConfiguration(Launcher.this, localeConfiguration);
				}
			}.start();
		}
	}

	private static class LocaleConfiguration
	{
		public String locale;
		public int mcc = -1;
		public int mnc = -1;
	}

	private static void readConfiguration(Context context, LocaleConfiguration configuration)
	{
		DataInputStream in = null;
		try
		{
			in = new DataInputStream(context.openFileInput(PREFERENCES));
			configuration.locale = in.readUTF();
			configuration.mcc = in.readInt();
			configuration.mnc = in.readInt();
		}
		catch (FileNotFoundException e)
		{
			// Ignore
		}
		catch (IOException e)
		{
			// Ignore
		}
		finally
		{
			if (in != null)
			{
				try
				{
					in.close();
				}
				catch (IOException e)
				{
					// Ignore
				}
			}
		}
	}

	private static void writeConfiguration(Context context, LocaleConfiguration configuration)
	{
		DataOutputStream out = null;
		try
		{
			out = new DataOutputStream(context.openFileOutput(PREFERENCES, MODE_PRIVATE));
			out.writeUTF(configuration.locale);
			out.writeInt(configuration.mcc);
			out.writeInt(configuration.mnc);
			out.flush();
		}
		catch (FileNotFoundException e)
		{
			// Ignore
		}
		catch (IOException e)
		{
			// noinspection ResultOfMethodCallIgnored
			context.getFileStreamPath(PREFERENCES).delete();
		}
		finally
		{
			if (out != null)
			{
				try
				{
					out.close();
				}
				catch (IOException e)
				{
					// Ignore
				}
			}
		}
	}

	public IconCache getIconCache()
	{
		return mIconCache;
	}

	public LauncherModel getModel()
	{
		return mModel;
	}

	public AppDB getAppDB()
	{
		return mAppDB;
	}

	public AllAppsView getAllAppsView()
	{
		return mAllAppsGrid;
	}

	static int getScreen()
	{
		synchronized (sLock)
		{
			return sScreen;
		}
	}

	static void setScreen(int screen)
	{
		synchronized (sLock)
		{
			sScreen = screen;
		}
	}

	private void setWallpaperDimension()
	{
		WallpaperManager wpm = (WallpaperManager) getSystemService(WALLPAPER_SERVICE);

		Display display = getWindowManager().getDefaultDisplay();
		boolean isPortrait = display.getWidth() < display.getHeight();

		final int width = isPortrait ? display.getWidth() : display.getHeight();
		final int height = isPortrait ? display.getHeight() : display.getWidth();
		wpm.suggestDesiredDimensions(width * WALLPAPER_SCREENS_SPAN, 800);
		// wpm.suggestDesiredDimensions(480, 800);
	}

	private void loadHotseats()
	{
		mHotseats = new Intent[mHotseatConfig];
		mHotseatLabels = new CharSequence[mHotseatConfig];
		mHotseatIcons = new Drawable[mHotseatConfig];

		TypedArray hotseatIconDrawables = getResources().obtainTypedArray(R.array.hotseat_icons);
		for (int i = 0; i < mHotseatConfig; i++)
		{
			// load icon for this slot; currently unrelated to the actual
			// activity
			try
			{
				mHotseatIcons[i] = hotseatIconDrawables.getDrawable(i);
			}
			catch (ArrayIndexOutOfBoundsException ex)
			{
				Log.w(TAG, "Missing hotseat_icons array item #" + i);
				mHotseatIcons[i] = null;
			}
		}
		hotseatIconDrawables.recycle();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{

		if (requestCode == REQUEST_HOME_SWITCHER)
		{
			mWorkspace.mCurrentScreen = SCREEN_COUNT / 2;
			mWorkspace.mDefaultScreen = SCREEN_COUNT / 2;

			if (data == null)
				mWorkspace.moveToDefaultScreen(true);
			else
			{
				int index = data.getIntExtra("gotoIndex", -1);

				if (index == -1)
					mWorkspace.moveToDefaultScreen(true);
				else
					setCurrentScreen(index);
			}
		}

		mWaitingForResult = false;

		// The pattern used here is that a user PICKs a specific application,
		// which, depending on the target, might need to CREATE the actual
		// target.

		// For example, the user would PICK_SHORTCUT for "Music playlist", and
		// we
		// launch over to the Music app to actually CREATE_SHORTCUT.

		if (resultCode == RESULT_OK && requestCode == REQUEST_EDIT_SHIRTCUT)
		{
			completeEditShirtcut(data);
			return;
		}
		if (resultCode == RESULT_OK
				&& (mAddItemCellInfo != null || requestCode == REQUEST_PICK_LIVE_FOLDER_DRAWER
						|| requestCode == REQUEST_CREATE_LIVE_FOLDER_DRAWER || requestCode == REQUEST_PICK_APPWIDGET || requestCode == REQUEST_CREATE_APPWIDGET))
		{
			switch (requestCode)
			{
				case REQUEST_PICK_APPLICATION:
					completeAddApplication(this, data, mAddItemCellInfo);
					break;
				case REQUEST_PICK_SHORTCUT:
					processShortcut(data);
					break;
				case REQUEST_PICK_ANYCUT:
				case REQUEST_CREATE_SHORTCUT:
					completeAddShortcut(data, mAddItemCellInfo);
					break;
				case REQUEST_PICK_LIVE_FOLDER_DRAWER:
					addLiveFolder(data, true);
					break;
				case REQUEST_PICK_LIVE_FOLDER:
					addLiveFolder(data, false);
					break;
				case REQUEST_CREATE_LIVE_FOLDER_DRAWER:
					completeAddLiveFolderDrawer(data);
					break;
				case REQUEST_CREATE_LIVE_FOLDER:
					completeAddLiveFolder(data, mAddItemCellInfo);
					break;
				case REQUEST_PICK_APPWIDGET:
					addAppWidget(data);
					break;
				case REQUEST_CREATE_APPWIDGET:
					completeAddAppWidget(data, mAddItemCellInfo);
					break;
				case REQUEST_PICK_WALLPAPER:
					// We just wanted the activity result here so we can clear
					// mWaitingForResult
					break;

			}
		}
		else if ((requestCode == REQUEST_PICK_APPWIDGET || requestCode == REQUEST_CREATE_APPWIDGET)
				&& resultCode == RESULT_CANCELED && data != null)
		{
			// Clean up the appWidgetId if we canceled
			int appWidgetId = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
			if (appWidgetId != -1)
			{
				mAppWidgetHost.deleteAppWidgetId(appWidgetId);
			}
		}
	}

	/**
	 */
	private void addWidget()
	{

		int appWidgetId = mAppWidgetHost.allocateAppWidgetId();

		Intent pickIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_PICK);
		pickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

		// add the search widget
		ArrayList<AppWidgetProviderInfo> customInfo = new ArrayList<AppWidgetProviderInfo>();
		AppWidgetProviderInfo info = new AppWidgetProviderInfo();
		info.provider = new ComponentName(getPackageName(), "XXX.YYY");
		info.label = "Search";
		info.icon = R.drawable.icon;
		// info.configure=new ComponentName(in);
		customInfo.add(info);
		pickIntent.putParcelableArrayListExtra(AppWidgetManager.EXTRA_CUSTOM_INFO, customInfo);
		ArrayList<Bundle> customExtras = new ArrayList<Bundle>();
		Bundle b = new Bundle();
		b.putString(EXTRA_CUSTOM_WIDGET, "search_widget");
		customExtras.add(b);
		pickIntent.putParcelableArrayListExtra(AppWidgetManager.EXTRA_CUSTOM_EXTRAS, customExtras);
		// start the pick activity

		startActivityForResult(pickIntent, REQUEST_PICK_APPWIDGET);

	}

	/**
	 * Add the views for a widget to the workspace.
	 * 
	 * Implementation of the method from LauncherModel.Callbacks.
	 */
	public void bindAppWidget(WidgetBean item)
	{
		final Workspace workspace = mWorkspace;

		final int appWidgetId = item.getWidgetId();
		final AppWidgetProviderInfo appWidgetInfo = mAppWidgetManager.getAppWidgetInfo(appWidgetId);

		AppWidgetHostView hostView = mAppWidgetHost.createView(this, appWidgetId, appWidgetInfo);

		workspace.addInScreen(hostView, item.getScreen(), item.getX(), item.getY(), item.getSpanX(), item.getSpanY(), false);

		workspace.requestLayout();
		LauncherAppWidgetInfo launcherAppWidgetInfo = new LauncherAppWidgetInfo(item.getWidgetId());
		launcherAppWidgetInfo.id = item.getId();
		launcherAppWidgetInfo.hostView = hostView;
		launcherAppWidgetInfo.hostView.setAppWidget(appWidgetId, appWidgetInfo);
		launcherAppWidgetInfo.hostView.setTag(launcherAppWidgetInfo);
		mDesktopItems.add(launcherAppWidgetInfo);

	}

	public void updateWidgetBySendBroadcast(WidgetBean widgetBean)
	{

		Intent intent = new Intent();
		intent.setAction(WidgetReceiver.UPDATE_WIDGET);
		Bundle bundle = new Bundle();
		bundle.putSerializable(WidgetReceiver.EXTRAS_KEY, widgetBean);
		intent.putExtras(bundle);

		sendBroadcast(intent);

	}

	public void deleteWidgetBySendBroadast(WidgetBean widgetBean)
	{
		Intent intent = new Intent();
		intent.setAction(WidgetReceiver.DELETE_WIDGET);
		Bundle bundle = new Bundle();
		bundle.putSerializable(WidgetReceiver.EXTRAS_KEY, widgetBean);
		intent.putExtras(bundle);

		sendBroadcast(intent);

	}

	/**
	 * 
	 * @param data
	 */
	private void addAppWidget(Intent data)
	{
		int appWidgetId = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);

		String customWidget = data.getStringExtra(EXTRA_CUSTOM_WIDGET);
		d("addAppWidget", "data:" + customWidget);
		if ("search_widget".equals(customWidget))
		{
			mAppWidgetHost.deleteAppWidgetId(appWidgetId);
		}
		else
		{
			AppWidgetProviderInfo appWidget = mAppWidgetManager.getAppWidgetInfo(appWidgetId);

			d("addAppWidget", "configure:" + appWidget.configure);
			if (appWidget.configure != null)
			{
				Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
				intent.setComponent(appWidget.configure);
				intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

				startActivityForResult(intent, REQUEST_CREATE_APPWIDGET);
			}
			else
			{
				onActivityResult(REQUEST_CREATE_APPWIDGET, Activity.RESULT_OK, data);
			}
		}
	}

	private void completeAddAppWidget(Intent data, CellLayout.CellInfo cellInfo)
	{
		Bundle extras = data.getExtras();
		int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);

		if (LOGD)
			Log.d(TAG, "dumping extras content=" + extras.toString());

		AppWidgetProviderInfo appWidgetInfo = mAppWidgetManager.getAppWidgetInfo(appWidgetId);

		// Calculate the grid spans needed to fit this widget
		CellLayout layout = (CellLayout) mWorkspace.getChildAt(cellInfo.screen);
		int[] spans = layout.rectToCell(appWidgetInfo.minWidth, appWidgetInfo.minHeight);

		// Try finding open space on Launcher screen
		final int[] xy = mCellCoordinates;
		if (!findSlot(cellInfo, xy, spans[0], spans[1]))
		{
			if (appWidgetId != -1)
				mAppWidgetHost.deleteAppWidgetId(appWidgetId);
			return;
		}

		// Build Launcher-specific widget info and save to database
		LauncherAppWidgetInfo launcherInfo = new LauncherAppWidgetInfo(appWidgetId);

		launcherInfo.spanX = spans[0];
		launcherInfo.spanY = spans[1];

		// LauncherModel.addItemToDatabase(this, launcherInfo,
		// LauncherSettings.Favorites.CONTAINER_DESKTOP, mWorkspace
		// .getCurrentScreen(), xy[0], xy[1], false);
		// WidgetDataBaseAdapter widgetDataBaseAdapter
		WidgetDataBaseAdapter adapter = WidgetDataBaseAdapter.getInstance();
		WidgetBean widgetBean = new WidgetBean();
		widgetBean.setWidgetId(appWidgetId);
		widgetBean.setSpanX(spans[0]);
		widgetBean.setSpanY(spans[1]);
		widgetBean.setX(xy[0]);
		widgetBean.setY(xy[1]);
		widgetBean.setScreen(mWorkspace.getCurrentScreen());
		widgetBean.setType(WidgetEnum.WidgetItem);
		widgetBean.setScene(getCurrentScene().getId());
		// TODO: 请加上 widget type 取费 widget 还是 app

		// Intent intent = new Intent();
		// intent.setAction(WidgetReceiver.ADD_WIDGET);
		// Bundle bundle = new Bundle();
		// bundle.putSerializable(WidgetReceiver.EXTRAS_KEY, widgetBean);
		// intent.putExtras(bundle);
		// sendBroadcast(intent);

		adapter.open();
		long id = adapter.insert(widgetBean);

		adapter.close();

		// 测试添加widget到场景

		launcherInfo.id = id;

		if (!mRestoring)
		{
			mDesktopItems.add(launcherInfo);

			// Perform actual inflation because we're live
			launcherInfo.hostView = mAppWidgetHost.createView(this, appWidgetId, appWidgetInfo);

			launcherInfo.hostView.setAppWidget(appWidgetId, appWidgetInfo);
			launcherInfo.hostView.setTag(launcherInfo);

			mWorkspace.addInCurrentScreen(launcherInfo.hostView, xy[0], xy[1], launcherInfo.spanX, launcherInfo.spanY,
					isWorkspaceLocked());
		}

	}

	@Override
	protected void onResume()
	{

		super.onResume();
		mPaused = false;
		if (mRestoring || mOnResumeNeedsLoad)
		{
			mWorkspaceLoading = true;
			mModel.startLoader(this, true);
			mRestoring = false;
			mOnResumeNeedsLoad = false;
		}
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		mPaused = true;
		dismissPreview(mPreviousView);
		dismissPreview(mNextView);
		mDragController.cancelDrag();
	}

	@Override
	public Object onRetainNonConfigurationInstance()
	{
		// Flag the loader to stop early before switching
		mModel.stopLoader();
		mAllAppsGrid.surrender();
		return Boolean.TRUE;
	}

	private void completeEditShirtcut(Intent data)
	{
		if (!data.hasExtra(CustomShirtcutActivity.EXTRA_APPLICATIONINFO))
			return;
		long appInfoId = data.getLongExtra(CustomShirtcutActivity.EXTRA_APPLICATIONINFO, 0);
		if (data.hasExtra(CustomShirtcutActivity.EXTRA_DRAWERINFO))
		{
			List<ShortcutInfo> apps = mAppDB.getApps(new long[]
			{ appInfoId });
			if (apps.size() == 1)
			{
				IconItemInfo info = apps.get(0);
				Bitmap bitmap = data.getParcelableExtra(Intent.EXTRA_SHORTCUT_ICON);
				if (bitmap != null)
				{
					info.setIcon(bitmap);
				}
				else
				{
					bitmap = info.mIcon;
				}
				String title = data.getStringExtra(Intent.EXTRA_SHORTCUT_NAME);
				info.setTitle(title);
				mAppDB.updateAppDisplay(appInfoId, title, bitmap);

				// Notify Model:
				Intent modelIntent = new Intent(AppDB.INTENT_DB_CHANGED);
				modelIntent.putExtra(AppDB.EXTRA_UPDATED, new long[]
				{ info.id });
				sendBroadcast(modelIntent);
			}
		}
		else
		{
			ItemInfo ii = mModel.getItemInfoById(appInfoId);
			if (ii != null && ii instanceof IconItemInfo)
			{
				IconItemInfo info = (IconItemInfo) ii;
				Bitmap bitmap = data.getParcelableExtra(Intent.EXTRA_SHORTCUT_ICON);

				if (bitmap != null)
				{
					info.setIcon(bitmap);
				}
				info.setTitle(data.getStringExtra(Intent.EXTRA_SHORTCUT_NAME));
				if (data.hasExtra(Intent.EXTRA_SHORTCUT_INTENT) && info instanceof ShortcutInfo)
					((ShortcutInfo) info).intent = data.getParcelableExtra(Intent.EXTRA_SHORTCUT_INTENT);
				LauncherModel.updateItemInDatabase(this, info);
				// Need to update the icon here!
				if (ii.container == Favorites.CONTAINER_DRAWER)
				{
					ArrayList<IconItemInfo> lst = new ArrayList<IconItemInfo>();
					lst.add(info);
					mAllAppsGrid.updateApps(lst);
				}
				else
				{
					View v = mWorkspace.findViewWithTag(info);
					if (v instanceof BubbleTextView)
						((BubbleTextView) v).updateFromItemInfo(mIconCache, info);
				}
			}
		}
	}

	// We can't hide the IME if it was forced open. So don't bother
	/*
	 * @Override public void onWindowFocusChanged(boolean hasFocus) {
	 * super.onWindowFocusChanged(hasFocus);
	 * 
	 * if (hasFocus) { final InputMethodManager inputManager =
	 * (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	 * WindowManager.LayoutParams lp = getWindow().getAttributes();
	 * inputManager.hideSoftInputFromWindow(lp.token, 0, new
	 * android.os.ResultReceiver(new android.os.Handler()) { protected void
	 * onReceiveResult(int resultCode, Bundle resultData) { Log.d(TAG,
	 * "ResultReceiver got resultCode=" + resultCode); } }); Log.d(TAG,
	 * "called hideSoftInputFromWindow from onWindowFocusChanged"); } }
	 */

	private boolean acceptFilter()
	{
		final InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		return !inputManager.isFullscreenMode();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		boolean handled = super.onKeyDown(keyCode, event);
		if (!handled && acceptFilter() && keyCode != KeyEvent.KEYCODE_ENTER)
		{
			boolean gotKey = TextKeyListener.getInstance().onKeyDown(mWorkspace, mDefaultKeySsb, keyCode, event);
			if (gotKey && mDefaultKeySsb != null && mDefaultKeySsb.length() > 0)
			{
				// something usable has been typed - start a search
				// the typed text will be retrieved and cleared by
				// showSearchDialog()
				// If there are multiple keystrokes before the search dialog
				// takes focus,
				// onSearchRequested() will be called for every keystroke,
				// but it is idempotent, so it's fine.
				return onSearchRequested();
			}
		}

		// Eat the long press event so the keyboard doesn't come up.
		if (keyCode == KeyEvent.KEYCODE_MENU && event.isLongPress())
		{
			return true;
		}

		if (keyCode == KeyEvent.KEYCODE_HOME && !event.isLongPress())
		{

		}
		return handled;
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event)
	{

		boolean handle = super.onKeyUp(keyCode, event);
		if (keyCode == KeyEvent.KEYCODE_HOME)
		{
			if (!mWorkspace.isDefaultScreenShowing())
			{
				mWorkspace.moveToDefaultScreen(true);
				return handle;
			}

			if (event.getRepeatCount() == 0 && !event.isLongPress())
			{
				if (!mLongPressOnHome)
				{
					if (!isAllAppsVisible())
					{
						ScreenSwitcherActivity.initPic(getThumbList(0, mWorkspace.getChildCount()));
						ScreenSwitcherActivity.setLauncher(this);

						Intent intent = new Intent(Launcher.this, ScreenSwitcherActivity.class);
						startActivityForResult(intent, REQUEST_HOME_SWITCHER);
					}
					else
					{
						closeAllApps(true);
					}
				}
				mLongPressOnHome = false;

			}
		}
		return handle;
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event)
	{
		if (event.getAction() == KeyEvent.ACTION_DOWN)
		{
			switch (event.getKeyCode())
			{
				case KeyEvent.KEYCODE_HOME:
					if (event.getRepeatCount() > 0)
						mLongPressOnHome = true;

					return true;
			}
		}
		else if (event.getAction() == KeyEvent.ACTION_UP)
		{
			switch (event.getKeyCode())
			{
				case KeyEvent.KEYCODE_HOME:
					if (event.getRepeatCount() > 0)

						return true;
			}
		}

		return super.dispatchKeyEvent(event);
	}

	private boolean mLongPressOnHome = false;

	@Override
	public boolean onKeyLongPress(int keyCode, KeyEvent event)
	{
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_HOME)
		{
			mLongPressOnHome = true;
			return true;

		}
		return super.onKeyLongPress(keyCode, event);
	}

	private String getTypedText()
	{
		return mDefaultKeySsb.toString();
	}

	private void clearTypedText()
	{
		mDefaultKeySsb.clear();
		mDefaultKeySsb.clearSpans();
		Selection.setSelection(mDefaultKeySsb, 0);
	}

	/**
	 * Restores the previous state, if it exists.
	 * 
	 * @param savedState
	 *            The previous state.
	 */
	private void restoreState(Bundle savedState)
	{
		if (savedState == null)
		{
			mAddItemCellInfo = new CellLayout.CellInfo();
			int centerIndex = SCREEN_COUNT / 2;
			mWorkspace.setCurrentScreen(centerIndex);
			return;
		}

		final boolean allApps = savedState.getBoolean(RUNTIME_STATE_ALL_APPS_FOLDER, false);
		if (allApps)
		{
			showAllApps(false);
		}

		final int currentScreen = savedState.getInt(RUNTIME_STATE_CURRENT_SCREEN, -1);
		if (currentScreen > -1)
		{
			mWorkspace.setCurrentScreen(currentScreen);
		}

		final int addScreen = savedState.getInt(RUNTIME_STATE_PENDING_ADD_SCREEN, -1);
		if (addScreen > -1)
		{
			mAddItemCellInfo = new CellLayout.CellInfo();
			final CellLayout.CellInfo addItemCellInfo = mAddItemCellInfo;
			addItemCellInfo.valid = true;
			addItemCellInfo.screen = addScreen;
			addItemCellInfo.cellX = savedState.getInt(RUNTIME_STATE_PENDING_ADD_CELL_X);
			addItemCellInfo.cellY = savedState.getInt(RUNTIME_STATE_PENDING_ADD_CELL_Y);
			addItemCellInfo.spanX = savedState.getInt(RUNTIME_STATE_PENDING_ADD_SPAN_X);
			addItemCellInfo.spanY = savedState.getInt(RUNTIME_STATE_PENDING_ADD_SPAN_Y);
			addItemCellInfo.findVacantCellsFromOccupied(savedState.getBooleanArray(RUNTIME_STATE_PENDING_ADD_OCCUPIED_CELLS),
					savedState.getInt(RUNTIME_STATE_PENDING_ADD_COUNT_X), savedState.getInt(RUNTIME_STATE_PENDING_ADD_COUNT_Y));
			mRestoring = true;
		}

		boolean renameFolder = savedState.getBoolean(RUNTIME_STATE_PENDING_FOLDER_RENAME, false);
		if (renameFolder)
		{
			long id = savedState.getLong(RUNTIME_STATE_PENDING_FOLDER_RENAME_ID);
			mFolderInfo = mModel.getFolderById(this, sFolders, id);
			mRestoring = true;
		}
	}

	/**
	 * Finds all the views we need and configure them properly.
	 */
	private void setupViews()
	{
		DragController dragController = mDragController;

		mDragLayer = (DragLayer) findViewById(R.id.drag_layer);
		mDragLayer.setDragController(dragController);

		mAllAppsGrid = (AllAppsView) mDragLayer.findViewById(R.id.all_apps_view);
		mAllAppsGrid.setLauncher(this);
		mAllAppsGrid.setDragController(dragController);
		((View) mAllAppsGrid).setWillNotDraw(false); // We don't want a hole
		// punched in our
		// window.
		// Manage focusability manually since this thing is always visible
		((View) mAllAppsGrid).setFocusable(false);

		mWorkspace = (Workspace) mDragLayer.findViewById(R.id.workspace);
		FRONT_SCREEN_COUNT = mWorkspace.getFrontScreenCount();

		final Workspace workspace = mWorkspace;
		workspace.setHapticFeedbackEnabled(false);

		DeleteZone deleteZone = (DeleteZone) mDragLayer.findViewById(R.id.delete_zone);
		mDeleteZone = deleteZone;

		mHandleView = (HandleView) findViewById(R.id.all_apps_button);
		mHandleView.setLauncher(this);
		mHandleView.setOnClickListener(this);
		mHandleView.setOnLongClickListener(this);

		ImageView hotseatLeft = (ImageView) findViewById(R.id.hotseat_left);
		hotseatLeft.setContentDescription(mHotseatLabels[0]);
		hotseatLeft.setImageDrawable(mHotseatIcons[0]);
		ImageView hotseatRight = (ImageView) findViewById(R.id.hotseat_right);
		hotseatRight.setContentDescription(mHotseatLabels[1]);
		hotseatRight.setImageDrawable(mHotseatIcons[1]);

		mPreviousView = (ImageView) mDragLayer.findViewById(R.id.previous_screen);
		mNextView = (ImageView) mDragLayer.findViewById(R.id.next_screen);

		Drawable previous = mPreviousView.getDrawable();
		Drawable next = mNextView.getDrawable();
		mWorkspace.setIndicators(previous, next);

		mPreviousView.setHapticFeedbackEnabled(false);
		mPreviousView.setOnLongClickListener(this);
		mNextView.setHapticFeedbackEnabled(false);
		mNextView.setOnLongClickListener(this);
		workspace.setOnLongClickListener(this);
		workspace.setDragController(dragController);
		workspace.setLauncher(this);

		deleteZone.setLauncher(this);
		deleteZone.setDragController(dragController);
		deleteZone.setHandle(findViewById(R.id.all_apps_button_cluster));

		dragController.setDragScoller(workspace);
		dragController.setDragListener(deleteZone);
		dragController.setScrollView(mDragLayer);
		dragController.setMoveTarget(workspace);

		// The order here is bottom to top.
		dragController.addDropTarget(workspace);
		dragController.addDropTarget(deleteZone);
	}

	public void previousScreen(View v)
	{
		Log.e("previousScreen", "previousScreen");

		if (!isAllAppsVisible())
		{
			mWorkspace.scrollLeft();
		}
	}

	public void nextScreen(View v)
	{
		Log.e("nextScreen", "nextScreen");

		if (!isAllAppsVisible())
		{
			mWorkspace.scrollRight();
		}
	}

	public void launchHotSeat(View v)
	{
		if (isAllAppsVisible())
			return;

		int index = -1;
		if (v.getId() == R.id.hotseat_left)
		{
			index = 0;
		}
		else if (v.getId() == R.id.hotseat_right)
		{
			index = 1;
		}

		// reload these every tap; you never know when they might change
		loadHotseats();
		// kya to start dial &message
		if (index == 0)
		{
			Intent intent = new Intent("android.intent.action.DIAL");
			startActivity(intent);
		}
		else if (index == 1)
		{
			Intent intent = new Intent();
			// intent.setAction(Intent.ACTION_MAIN);
			// intent.addCategory(Intent.CATEGORY_DEFAULT);
			// intent.setType("vnd.android-dir/mms-sms");
			// // or modify like this:
			// // intent.setType("vnd.android.cursor.dir/mms");
			intent.setComponent(new ComponentName("com.android.browser", "com.android.browser.BrowserActivity"));
			startActivity(intent);
		}

	}

	/**
	 * Creates a view representing a shortcut.
	 * 
	 * @param info
	 *            The data structure describing the shortcut.
	 * 
	 * @return A View inflated from R.layout.application.
	 */
	View createShortcut(ShortcutInfo info)
	{
		return createShortcut(R.layout.application, (ViewGroup) mWorkspace.getChildAt(mWorkspace.getCurrentScreen()), info);
	}

	/**
	 * Creates a view representing a shortcut inflated from the specified
	 * resource.
	 * 
	 * @param layoutResId
	 *            The id of the XML layout used to create the shortcut.
	 * @param parent
	 *            The group the shortcut belongs to.
	 * @param info
	 *            The data structure describing the shortcut.
	 * 
	 * @return A View inflated from layoutResId.
	 */
	View createShortcut(int layoutResId, ViewGroup parent, ShortcutInfo info)
	{
		TextView favorite = (TextView) mInflater.inflate(layoutResId, parent, false);

		if (info.mIcon != null)
		{
			BitmapDrawable picDrawable = new BitmapDrawable(info.mIcon);
			picDrawable.setBounds(0, 0, 50, 50);
			favorite.setCompoundDrawables(null, picDrawable, null, null);
		}
		else
		{
			favorite
					.setCompoundDrawablesWithIntrinsicBounds(null, new FastBitmapDrawable(info.getIcon(mIconCache)), null, null);
		}
		favorite.setText(info.getTitle(mIconCache));
		favorite.setTag(info);
		favorite.setOnClickListener(this);

		return favorite;
	}

	/**
	 * Add an application shortcut to the workspace.
	 * 
	 * @param data
	 *            The intent describing the application.
	 * @param cellInfo
	 *            The position on screen where to create the shortcut.
	 */
	void completeAddApplication(Context context, Intent data, CellLayout.CellInfo cellInfo)
	{
		cellInfo.screen = mWorkspace.getCurrentScreen();
		if (!findSingleSlot(cellInfo))
			return;

		final ShortcutInfo info = mModel.getShortcutInfo(data, context);

		if (info != null)
		{
			info.setActivity(data.getComponent(), Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
			info.container = ItemInfo.NO_ID;
			mWorkspace.addApplicationShortcut(info, cellInfo, isWorkspaceLocked());
		}
		else
		{
			Log.e(TAG, "Couldn't find ActivityInfo for selected application: " + data);
		}
	}

	/**
	 * Add a shortcut to the workspace.
	 * 
	 * @param data
	 *            The intent describing the shortcut.
	 * @param cellInfo
	 *            The position on screen where to create the shortcut.
	 */
	private void completeAddShortcut(Intent data, CellLayout.CellInfo cellInfo)
	{
		cellInfo.screen = mWorkspace.getCurrentScreen();
		if (!findSingleSlot(cellInfo))
			return;

		final ShortcutInfo info = mModel.addShortcut(this, data, cellInfo, false);

		if (!mRestoring)
		{
			final View view = createShortcut(info);
			mWorkspace.addInCurrentScreen(view, cellInfo.cellX, cellInfo.cellY, 1, 1, isWorkspaceLocked());
		}
	}

	/**
	 * Remove an item from the desktop
	 * 
	 * @param info
	 */
	void removeDesktopItem(ItemInfo info)
	{

		if (mDesktopItems != null)
			mDesktopItems.remove(info);
		View view = mWorkspace.getViewForTag(info);
		if (view != null)
			((ViewGroup) view.getParent()).removeView(view);

	}

	public String getPackageNameFromIntent(Intent intent)
	{
		String pName = intent.getPackage();
		if (pName == null)
		{
			ComponentName cName = intent.getComponent();
			if (cName != null)
				pName = cName.getPackageName();
		}

		if (pName == null)
		{
			PackageManager mgr = getPackageManager();
			ResolveInfo res = mgr.resolveActivity(intent, 0);
			if (res != null)
				pName = res.activityInfo.packageName;
		}

		if (pName == null)
		{
			Uri data = intent.getData();
			if (data != null)
			{
				String host = data.getHost();
				PackageManager mgr = getPackageManager();
				ProviderInfo packageInfo = mgr.resolveContentProvider(host, 0);
				if (packageInfo != null)
					pName = packageInfo.packageName;
			}
		}

		return pName;
	}

	public boolean UninstallPackage(String aPackage)
	{
		if (aPackage == null || this.getClass().getPackage().getName().equals(aPackage))
			return false;
		Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, Uri.parse("package:" + aPackage));
		startActivity(uninstallIntent);
		return true;
	}

	public void removeAppWidget(LauncherAppWidgetInfo launcherInfo)
	{
		mDesktopItems.remove(launcherInfo);
		launcherInfo.hostView = null;
	}

	public LauncherAppWidgetHost getAppWidgetHost()
	{
		return mAppWidgetHost;
	}

	void closeSystemDialogs()
	{
		getWindow().closeAllPanels();

		try
		{
			dismissDialog(DIALOG_CREATE_SHORTCUT);
			// Unlock the workspace if the dialog was showing
		}
		catch (Exception e)
		{
			// An exception is thrown if the dialog is not visible, which is
			// fine
		}

		try
		{
			dismissDialog(DIALOG_RENAME_FOLDER);
			// Unlock the workspace if the dialog was showing
		}
		catch (Exception e)
		{
			// An exception is thrown if the dialog is not visible, which is
			// fine
		}

		// Whatever we were doing is hereby canceled.
		mWaitingForResult = false;
	}

	@Override
	protected void onNewIntent(Intent intent)
	{

		super.onNewIntent(intent);

		// Close the menu
		if (Intent.ACTION_MAIN.equals(intent.getAction()))
		{

			// also will cancel mWaitingForResult.
			closeSystemDialogs();

			boolean alreadyOnHome = ((intent.getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
			boolean allAppsVisible = isAllAppsVisible();
			if (!mWorkspace.isDefaultScreenShowing())
			{
				mWorkspace.moveToDefaultScreen(alreadyOnHome && !allAppsVisible);
			}
			closeAllApps(alreadyOnHome && allAppsVisible);

			final View v = getWindow().peekDecorView();
			if (v != null && v.getWindowToken() != null)
			{
				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			}
		}
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		// Do not call super here
		mSavedInstanceState = savedInstanceState;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		outState.putInt(RUNTIME_STATE_CURRENT_SCREEN, mWorkspace.getCurrentScreen());

		final ArrayList<Folder> folders = mWorkspace.getOpenFolders();
		if (folders.size() > 0)
		{
			final int count = folders.size();
			long[] ids = new long[count];
			for (int i = 0; i < count; i++)
			{
				final FolderInfo info = folders.get(i).getInfo();
				ids[i] = info.id;
			}
			outState.putLongArray(RUNTIME_STATE_USER_FOLDERS, ids);
		}
		else
		{
			super.onSaveInstanceState(outState);
		}

		// TODO should not do this if the drawer is currently closing.
		if (isAllAppsVisible())
		{
			outState.putBoolean(RUNTIME_STATE_ALL_APPS_FOLDER, true);
		}

		if (mAddItemCellInfo != null && mAddItemCellInfo.valid && mWaitingForResult)
		{
			final CellLayout.CellInfo addItemCellInfo = mAddItemCellInfo;
			final CellLayout layout = (CellLayout) mWorkspace.getChildAt(addItemCellInfo.screen);

			outState.putInt(RUNTIME_STATE_PENDING_ADD_SCREEN, addItemCellInfo.screen);
			outState.putInt(RUNTIME_STATE_PENDING_ADD_CELL_X, addItemCellInfo.cellX);
			outState.putInt(RUNTIME_STATE_PENDING_ADD_CELL_Y, addItemCellInfo.cellY);
			outState.putInt(RUNTIME_STATE_PENDING_ADD_SPAN_X, addItemCellInfo.spanX);
			outState.putInt(RUNTIME_STATE_PENDING_ADD_SPAN_Y, addItemCellInfo.spanY);
			outState.putInt(RUNTIME_STATE_PENDING_ADD_COUNT_X, layout.getCountX());
			outState.putInt(RUNTIME_STATE_PENDING_ADD_COUNT_Y, layout.getCountY());
			outState.putBooleanArray(RUNTIME_STATE_PENDING_ADD_OCCUPIED_CELLS, layout.getOccupiedCells());
		}

		if (mFolderInfo != null && mWaitingForResult)
		{
			outState.putBoolean(RUNTIME_STATE_PENDING_FOLDER_RENAME, true);
			outState.putLong(RUNTIME_STATE_PENDING_FOLDER_RENAME_ID, mFolderInfo.id);
		}
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();

		TextKeyListener.getInstance().release();

		mModel.stopLoader();

		unbindDesktopItems();

		// getContentResolver().unregisterContentObserver(mWidgetObserver);

		dismissPreview(mPreviousView);
		dismissPreview(mNextView);

		unregisterReceiver(mCloseSystemDialogsReceiver);
		// kya
		// mWorkspace.unregisterProvider();
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode)
	{
		if (requestCode >= 0)
			mWaitingForResult = true;
		super.startActivityForResult(intent, requestCode);
	}

	@Override
	public void startSearch(String initialQuery, boolean selectInitialQuery, Bundle appSearchData, boolean globalSearch)
	{

		closeAllApps(true);

		if (initialQuery == null)
		{
			// Use any text typed in the launcher as the initial query
			initialQuery = getTypedText();
			clearTypedText();
		}
		// TODO_BOOMBULER:
		/*
		 * if (appSearchData == null) { appSearchData = new Bundle();
		 * appSearchData.putString(Search.SOURCE, "launcher-search"); }
		 */

		final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		searchManager.startSearch(initialQuery, selectInitialQuery, getComponentName(), appSearchData, globalSearch);
	}

	// kya add menu item here
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		if (isWorkspaceLocked())
		{
			return false;
		}

//		super.onCreateOptionsMenu(menu);
//
//		menu.add(MENU_GROUP_ADD, MENU_ADD, 0, R.string.menu_add).setIcon(android.R.drawable.ic_menu_add).setAlphabeticShortcut(
//				'A');
//
//		menu.add(MENU_GROUP_HOMESCREEN, MENU_PERSONALIZATION, 0, R.string.menu_personalization).setIcon(
//				R.drawable.ic_menu_customize).setAlphabeticShortcut('P');
//
//		DefaultAction.getAction(this, DefaultAction.ACTION_MANAGE_APPS).addToMenu(menu, MENU_GROUP_HOMESCREEN);
//		//
//		// menu.add(MENU_GROUP_DRAWER, MENU_DRAWER_ADD_FOLDER, 0, "Add Folder");
//		//
//		//
//
//		// menu.add(MENU_GROUP_HOMESCREEN, MENU_WALLPAPER_SETTINGS, 0,
//		// R.string.menu_wallpaper)
//		// .setIcon(android.R.drawable.ic_menu_gallery);
//		// .setAlphabeticShortcut('W');
//		menu.add(MENU_GROUP_HOMESCREEN, MENU_NOTIFICATIONS, 0, R.string.menu_notifications).setIcon(
//				R.drawable.ic_menu_notifications).setAlphabeticShortcut('N');
//
//		// menu.add(MENU_GROUP_HOMESCREEN, MENU_SEARCH, 0, R.string.menu_search)
//		// .setIcon(android.R.drawable.ic_search_category_default)
//		// .setAlphabeticShortcut(SearchManager.MENU_KEY);
//		//
//		// DefaultAction.getAction(this,
//		// DefaultAction.ACTION_SHOW_NOTIFICATIONS).addToMenu(menu,
//		// MENU_GROUP_HOMESCREEN);
//		DefaultAction.getAction(this, DefaultAction.ACTION_SYSTEM_SETTINGS).addToMenu(menu, MENU_GROUP_HOMESCREEN);
//
//		return true;
		
		
		
		menu.add("menu");// 必须创建一项
		return super.onCreateOptionsMenu(menu);
		
	}

	protected void setMenuBackground()
	{

		getLayoutInflater().setFactory(new Factory()
		{

			@Override
			public View onCreateView(String name, Context context, AttributeSet attrs)
			{
				System.out.println(name);
				if (name.equalsIgnoreCase("com.android.internal.view.menu.IconMenuItemView"))
				{

					try
					{

						LayoutInflater f = getLayoutInflater();

						final View view = f.createView(name, null, attrs); // 尝试创建我们自己布局

						new Handler().post(new Runnable()
						{

							public void run()
							{

								view.setBackgroundResource(R.drawable.menu_bar_background); // 设置背景为我们自定义的图片，替换cwj_bg文件即可

							}

						});

						return view;

					}

					catch (InflateException e)
					{
					}

					catch (ClassNotFoundException e)
					{
					}

				}

				return null;

			}
		});

	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		super.onPrepareOptionsMenu(menu);
		if (mIsWidgetEditMode)
			return false;
		// If all apps is animating, don't show the menu, because we don't know
		// which one to show.
		if (mAllAppsGrid.isVisible() && !mAllAppsGrid.isOpaque())
		{
			return false;
		}

		// Only show the add and wallpaper options when we're not in all apps.
		boolean visible = !mAllAppsGrid.isOpaque();
		menu.setGroupVisible(MENU_GROUP_HOMESCREEN, visible);
		menu.setGroupVisible(MENU_GROUP_DRAWER, !visible);

		// Disable add if the workspace is full.
		if (visible)
		{
			mMenuAddInfo = mWorkspace.findAllVacantCells(null);
			menu.setGroupEnabled(MENU_GROUP_ADD, mMenuAddInfo != null && mMenuAddInfo.valid);
		}

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case MENU_ADD:
			{

				addItems();

				return true;
			}
			case MENU_WALLPAPER_SETTINGS:
				startWallpaper();
				return true;
			case MENU_SEARCH:
				onSearchRequested();
				return true;
			case MENU_DRAWER_ADD_FOLDER:
				requestPickLiveFolder(true);
				return true;
			case MENU_PERSONALIZATION:
			{
				// for shaohu

				Intent intent = new Intent(this, PersonalizationActivity.class);
				startActivity(intent);

				return true;
			}
			case MENU_NOTIFICATIONS:
			{

				mStatusBarManager = this.getSystemService("statusbar");
				Method[] methods = mStatusBarManager.getClass().getDeclaredMethods();
				for (Method method : methods)
				{
					if (method.getName().compareTo("expand") == 0)
					{
						expandStatusBar = method;
					}
				}
				try
				{
					expandStatusBar.invoke(mStatusBarManager);
				}
				catch (Exception e)
				{
					// TODO: handle exception
				}

				return true;
			}
		}

		return super.onOptionsItemSelected(item);
	}

	private ArrayList<Bitmap> getThumbList(int start, int end)
	{
		final Workspace workspace = mWorkspace;

		CellLayout cell = ((CellLayout) workspace.getChildAt(start));
		int count = end - start;

		ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>(count);

		float max = workspace.getChildCount();

		final Rect r = new Rect();
		int extraW = (int) ((r.left + r.right) * max);
		int extraH = r.top + r.bottom;

		int aW = cell.getWidth() - extraW;
		float w = aW / max;

		int width = cell.getWidth();
		int height = cell.getHeight();
		if (0 >= width)
			width = 320;
		if (0 >= height)
			height = 508;
		int x = cell.getLeftPadding();
		int y = cell.getTopPadding();
		width -= (x + cell.getRightPadding());
		height -= (y + cell.getBottomPadding());

		// float scale = 0.3;
		float scale = w / width;

		final float sWidth = width * scale;
		float sHeight = height * scale;

		// for (int i = start; i < end; i++) {
		// cell = (CellLayout) workspace.getChildAt(i);
		//
		// final Bitmap bitmap = Bitmap.createBitmap((int) sWidth, (int)
		// sHeight,
		// Bitmap.Config.ARGB_8888);
		//
		// final Canvas c = new Canvas(bitmap);
		// c.scale(scale, scale);
		// c.translate(-cell.getLeftPadding(), -cell.getTopPadding());
		// cell.dispatchDraw(c);
		//
		// bitmaps.add(bitmap);
		// }

		for (int i = start; i < end; i++)
		{
			cell = (CellLayout) workspace.getChildAt(i);

			final Bitmap bitmap = Bitmap.createBitmap((int) 115, (int) 180, Bitmap.Config.ARGB_8888);
			final Canvas c = new Canvas(bitmap);
			c.scale((float) 0.35, (float) 0.35);
			c.translate(0, 0);
			Drawable drawable = getWallpaper();
			WallpaperManager manager = WallpaperManager.getInstance(Launcher.this);
			WallpaperInfo wallpaperInfo = manager.getWallpaperInfo();
			if (wallpaperInfo != null)
			{
				c.scale((float) 4.5, (float) 4.5);
				PackageManager packagemanager = this.getPackageManager();
				// drawable = wallpaperInfo.loadIcon(packagemanager);
				drawable = wallpaperInfo.loadThumbnail(packagemanager);

				drawable.setBounds(-60, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
				drawable.draw(c);
				// c.translate(0, 5);
				c.scale((float) 0.23, (float) 0.23);
			}
			else
			{

				int scal = (640 - 480) / end;

				drawable.setBounds(-scal * i, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
				drawable.draw(c);
				c.scale((float) 1.05, (float) 1.05);
			}
			cell.dispatchDraw(c);

			int begin_middle = ((end - 2) / 2);
			int end_middle = begin_middle + 2;
			c.translate(0, 0);
			if(false)
//			if (i >= begin_middle && i <= end_middle)
			{
				final Bitmap bitmap1 = Bitmap.createBitmap((int) 130, (int) 180, Bitmap.Config.ARGB_8888);
				final Canvas c1 = new Canvas(bitmap1);

				Drawable drawable2 = getResources().getDrawable(R.drawable.screenswitcher_lock_screen_title);

				c1.drawBitmap(bitmap, 0, (float)8, null);
				drawable2.setBounds(0, 0, 125, drawable2.getIntrinsicHeight());
				// c1.scale((float)0, (float)
				drawable2.draw(c1);
				bitmaps.add(bitmap1);
			}
			else
			{
				bitmaps.add(bitmap);
			}
		}

		return bitmaps;
	}

	/**
	 * Indicates that we want global search for this activity by setting the
	 * globalSearch argument for {@link #startSearch} to true.
	 */

	@Override
	public boolean onSearchRequested()
	{
		startSearch(null, false, null, true);
		return true;
	}

	public boolean isWorkspaceLocked()
	{
		return mWorkspaceLoading || mWaitingForResult;
	}

	public void addItems()
	{
		closeAllApps(true);
		showAddDialog(mMenuAddInfo);
	}

	void processShortcut(Intent intent)
	{
		// Handle case where user selected "Applications"
		String applicationName = getResources().getString(R.string.group_applications);
		String shortcutName = intent.getStringExtra(Intent.EXTRA_SHORTCUT_NAME);

		if (applicationName != null && applicationName.equals(shortcutName))
		{
			Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
			mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

			Intent pickIntent = new Intent(Intent.ACTION_PICK_ACTIVITY);
			pickIntent.putExtra(Intent.EXTRA_INTENT, mainIntent);
			pickIntent.putExtra(Intent.EXTRA_TITLE, getText(R.string.title_select_application));
			startActivityForResultSafely(pickIntent, REQUEST_PICK_APPLICATION);
		}
		else
		{
			startActivityForResultSafely(intent, REQUEST_CREATE_SHORTCUT);
		}
	}

	void addLiveFolder(Intent intent, boolean toDrawer)
	{
		// Handle case where user selected "Folder"
		String folderName = getResources().getString(R.string.group_folder);
		String shortcutName = intent.getStringExtra(Intent.EXTRA_SHORTCUT_NAME);

		if (folderName != null && folderName.equals(shortcutName))
		{
			if (toDrawer)
				addFolderToDrawer();
			else
				addFolder();
		}
		else
		{
			startActivityForResultSafely(intent, toDrawer ? REQUEST_CREATE_LIVE_FOLDER_DRAWER : REQUEST_CREATE_LIVE_FOLDER);
		}
	}

	void addFolder()
	{
		UserFolderInfo folderInfo = new UserFolderInfo();
		folderInfo.setTitle(getText(R.string.folder_name));

		CellLayout.CellInfo cellInfo = mAddItemCellInfo;
		cellInfo.screen = mWorkspace.getCurrentScreen();
		if (!findSingleSlot(cellInfo))
			return;

		// Update the model
		LauncherModel.addItemToDatabase(this, folderInfo, LauncherSettings.Favorites.CONTAINER_DESKTOP, mWorkspace
				.getCurrentScreen(), cellInfo.cellX, cellInfo.cellY, false);
		sFolders.put(folderInfo.id, folderInfo);

		// Create the view
		FolderIcon newFolder = FolderIcon.fromXml(R.layout.folder_icon, this, (ViewGroup) mWorkspace.getChildAt(mWorkspace
				.getCurrentScreen()), folderInfo);
		mWorkspace.addInCurrentScreen(newFolder, cellInfo.cellX, cellInfo.cellY, 1, 1, isWorkspaceLocked());
	}

	void removeFolder(FolderInfo folder)
	{
		sFolders.remove(folder.id);
	}

	private void completeAddLiveFolderDrawer(Intent data)
	{
		final LiveFolderInfo info = addLiveFolder(this, data, null, false);
		if (!mRestoring)
		{
			ArrayList<IconItemInfo> list = new ArrayList<IconItemInfo>();
			list.add(info);
			getAllAppsView().addApps(list);
		}
	}

	private void completeAddLiveFolder(Intent data, CellLayout.CellInfo cellInfo)
	{
		cellInfo.screen = mWorkspace.getCurrentScreen();
		if (!findSingleSlot(cellInfo))
			return;

		final LiveFolderInfo info = addLiveFolder(this, data, cellInfo, false);

		if (!mRestoring)
		{
			final View view = LiveFolderIcon.fromXml(R.layout.live_folder_icon, this, (ViewGroup) mWorkspace
					.getChildAt(mWorkspace.getCurrentScreen()), info);
			mWorkspace.addInCurrentScreen(view, cellInfo.cellX, cellInfo.cellY, 1, 1, isWorkspaceLocked());
		}
	}

	LiveFolderInfo addLiveFolder(Context context, Intent data, CellLayout.CellInfo cellInfo, boolean notify)
	{

		Intent baseIntent = data.getParcelableExtra(LiveFolders.EXTRA_LIVE_FOLDER_BASE_INTENT);
		String name = data.getStringExtra(LiveFolders.EXTRA_LIVE_FOLDER_NAME);

		Drawable icon = null;
		Intent.ShortcutIconResource iconResource = null;

		Parcelable extra = data.getParcelableExtra(LiveFolders.EXTRA_LIVE_FOLDER_ICON);
		if (extra != null && extra instanceof Intent.ShortcutIconResource)
		{
			try
			{
				iconResource = (Intent.ShortcutIconResource) extra;
				final PackageManager packageManager = context.getPackageManager();
				Resources resources = packageManager.getResourcesForApplication(iconResource.packageName);
				final int id = resources.getIdentifier(iconResource.resourceName, null, null);
				icon = resources.getDrawable(id);
			}
			catch (Exception e)
			{
				Log.w(TAG, "Could not load live folder icon: " + extra);
			}
		}

		if (icon == null)
		{
			icon = context.getResources().getDrawable(R.drawable.ic_launcher_folder);
		}

		final LiveFolderInfo info = new LiveFolderInfo();
		info.setIcon(Utilities.createIconBitmap(icon, context));
		info.setTitle(name);
		info.uri = data.getData();
		info.baseIntent = baseIntent;
		info.displayMode = data.getIntExtra(LiveFolders.EXTRA_LIVE_FOLDER_DISPLAY_MODE, LiveFolders.DISPLAY_MODE_GRID);

		if (cellInfo != null)
		{
			LauncherModel.addItemToDatabase(context, info, Favorites.CONTAINER_DESKTOP, cellInfo.screen, cellInfo.cellX,
					cellInfo.cellY, notify);
			sFolders.put(info.id, info);
		}
		else
		{
			LauncherModel.addItemToDatabase(context, info, Favorites.CONTAINER_DRAWER, ItemInfo.NO_ID, 0, 0, notify);
		}

		return info;
	}

	private boolean findSingleSlot(CellLayout.CellInfo cellInfo)
	{
		final int[] xy = new int[2];
		if (findSlot(cellInfo, xy, 1, 1))
		{
			cellInfo.cellX = xy[0];
			cellInfo.cellY = xy[1];
			return true;
		}
		return false;
	}

	private boolean findSlot(CellLayout.CellInfo cellInfo, int[] xy, int spanX, int spanY)
	{
		if (!cellInfo.findCellForSpan(xy, spanX, spanY))
		{
			boolean[] occupied = mSavedState != null ? mSavedState.getBooleanArray(RUNTIME_STATE_PENDING_ADD_OCCUPIED_CELLS)
					: null;
			cellInfo = mWorkspace.findAllVacantCells(occupied);
			if (!cellInfo.findCellForSpan(xy, spanX, spanY))
			{
				Toast.makeText(this, getString(R.string.out_of_space), Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		return true;
	}

	private void addFolderToDrawer()
	{
		UserFolderInfo folderInfo = new UserFolderInfo();
		folderInfo.setTitle(getText(R.string.folder_name));

		LauncherModel.addItemToDatabase(this, folderInfo, Favorites.CONTAINER_DRAWER, -1, 0, 0, false);
		ArrayList<IconItemInfo> list = new ArrayList<IconItemInfo>();
		list.add(folderInfo);
		mAllAppsGrid.addApps(list);
	}

	public void startWallpaper()
	{
		closeAllApps(true);
		final Intent pickWallpaper = new Intent(Intent.ACTION_SET_WALLPAPER);
		Intent chooser = Intent.createChooser(pickWallpaper, getText(R.string.chooser_wallpaper));
		// NOTE: Adds a configure option to the chooser if the wallpaper
		// supports it
		// Removed in Eclair MR1
		// WallpaperManager wm = (WallpaperManager)
		// getSystemService(Context.WALLPAPER_SERVICE);
		// WallpaperInfo wi = wm.getWallpaperInfo();
		// if (wi != null && wi.getSettingsActivity() != null) {
		// LabeledIntent li = new LabeledIntent(getPackageName(),
		// R.string.configure_wallpaper, 0);
		// li.setClassName(wi.getPackageName(), wi.getSettingsActivity());
		// chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { li });
		// }
		startActivityForResult(chooser, REQUEST_PICK_WALLPAPER);
	}

	@Override
	public void onAttachedToWindow()
	{
		// TODO Auto-generated method stub
		this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);
		super.onAttachedToWindow();
	}

	@Override
	public void onBackPressed()
	{
		if (isAllAppsVisible())
		{
			closeAllApps(true);
			dismissPreview(mPreviousView);
			dismissPreview(mNextView);
		}
		else
		{
			// super.onBackPressed();
		}

	}

	private void closeFolder()
	{
		Folder folder = mWorkspace.getOpenFolder();
		if (folder != null)
		{
			closeFolder(folder);
		}
	}

	void closeFolder(Folder folder)
	{
		folder.getInfo().opened = false;
		ViewGroup parent = (ViewGroup) folder.getParent();
		if (parent != null)
		{
			parent.removeView(folder);
			if (folder instanceof DropTarget)
			{
				// Live folders aren't DropTargets.
				mDragController.removeDropTarget((DropTarget) folder);
			}
		}
		folder.onClose();
	}

	// /**
	// * Re-listen when widgets are reset.
	// */
	// private void onAppWidgetReset() {
	// mAppWidgetHost.startListening();
	// }

	/**
	 * Go through the and disconnect any of the callbacks in the drawables and
	 * the views or we leak the previous Home screen on orientation change.
	 */
	private void unbindDesktopItems()
	{
		for (ItemInfo item : mDesktopItems)
		{
			item.unbind();
		}
	}

	/**
	 * Launches the intent referred by the clicked shortcut.
	 * 
	 * @param v
	 *            The view representing the clicked shortcut.
	 */
	public void onClick(View v)
	{
		Object tag = v.getTag();
		if (tag instanceof ShortcutInfo)
		{
			// Open shortcut
			final Intent intent = ((ShortcutInfo) tag).intent;
			int[] pos = new int[2];
			v.getLocationOnScreen(pos);
			intent.setSourceBounds(new Rect(pos[0], pos[1], pos[0] + v.getWidth(), pos[1] + v.getHeight()));
			startActivitySafely(intent, tag);
		}
		else if (tag instanceof FolderInfo)
		{
			openFolder((FolderInfo) tag);
		}
		else if (v == mHandleView)
		{

			// for juhuizhe
			// open app-lists here
			if (isAllAppsVisible())
			{
				closeAllApps(true);
			}
			else
			{
				showAllApps(true);
			}

			// Intent intent = new Intent(this,)
			// startActivity(intent)
		}
	}

	void startActivitySafely(Intent intent, Object tag)
	{
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try
		{
			startActivity(intent);
		}
		catch (ActivityNotFoundException e)
		{
			Toast.makeText(this, R.string.activity_not_found, Toast.LENGTH_SHORT).show();
			Log.e(TAG, "Unable to launch. tag=" + tag + " intent=" + intent, e);
		}
		catch (SecurityException e)
		{
			Toast.makeText(this, R.string.activity_not_found, Toast.LENGTH_SHORT).show();
			Log.e(TAG, "Launcher does not have the permission to launch " + intent
					+ ". Make sure to create a MAIN intent-filter for the corresponding activity "
					+ "or use the exported attribute for this activity. " + "tag=" + tag + " intent=" + intent, e);
		}
		if (mAppDB.incrementLaunchCounter(intent))
		{
			mAllAppsGrid.sort();
		}
	}

	void startActivityForResultSafely(Intent intent, int requestCode)
	{
		try
		{
			startActivityForResult(intent, requestCode);
		}
		catch (ActivityNotFoundException e)
		{
			Toast.makeText(this, R.string.activity_not_found, Toast.LENGTH_SHORT).show();
		}
		catch (SecurityException e)
		{
			Toast.makeText(this, R.string.activity_not_found, Toast.LENGTH_SHORT).show();
			Log.e(TAG, "Launcher does not have the permission to launch " + intent
					+ ". Make sure to create a MAIN intent-filter for the corresponding activity "
					+ "or use the exported attribute for this activity.", e);
		}
	}

	public void openFolder(FolderInfo folderInfo)
	{
		if (!folderInfo.opened)
		{
			// Close any open folder
			closeFolder();
			// Open the requested folder
			doOpenFolder(folderInfo);
		}
		else
		{
			// Find the open folder...
			Folder openFolder = mWorkspace.getFolderForTag(folderInfo);
			int folderScreen;
			if (openFolder != null)
			{
				folderScreen = mWorkspace.getScreenForView(openFolder);
				// .. and close it
				closeFolder(openFolder);
				if (folderScreen != mWorkspace.getCurrentScreen())
				{
					// Close any folder open on the current screen
					closeFolder();
					// Pull the folder onto this screen
					openFolder(folderInfo);
				}
			}
		}
	}

	/**
	 * Opens the user fodler described by the specified tag. The opening of the
	 * folder is animated relative to the specified View. If the View is null,
	 * no animation is played.
	 * 
	 * @param folderInfo
	 *            The FolderInfo describing the folder to open.
	 */
	private void doOpenFolder(FolderInfo folderInfo)
	{
		Folder openFolder;

		if (folderInfo instanceof UserFolderInfo)
		{
			openFolder = UserFolder.fromXml(this);
		}
		else if (folderInfo instanceof LiveFolderInfo)
		{
			openFolder = org.accenture.product.lemonade.LiveFolder.fromXml(this, folderInfo);
		}
		else
		{
			return;
		}
		if (isAllAppsVisible())
			closeAllApps(false);

		openFolder.setDragController(mDragController);
		openFolder.setLauncher(this);

		openFolder.bind(folderInfo);
		folderInfo.opened = true;

		int screen = folderInfo.screen;
		if (screen < 0)
			screen = mWorkspace.getCurrentScreen();

		mWorkspace.addInScreen(openFolder, screen, 0, 0, 4, 4);
		openFolder.onOpen();
	}

	public boolean onLongClick(View v)
	{
		switch (v.getId())
		{
			case R.id.previous_screen:

				return true;
			case R.id.next_screen:

				return true;
			case R.id.all_apps_button:

				return true;
		}

		if (isWorkspaceLocked())
		{
			return false;
		}

		if (!(v instanceof CellLayout))
		{
			v = (View) v.getParent();
		}

		CellLayout.CellInfo cellInfo = (CellLayout.CellInfo) v.getTag();

		// This happens when long clicking an item with the dpad/trackball
		if (cellInfo == null)
		{
			return true;
		}

		if (mWorkspace.allowLongPress())
		{
			if (cellInfo.cell == null)
			{
				if (cellInfo.valid)
				{
					// User long pressed on empty space
					mWorkspace.setAllowLongPress(false);
					mWorkspace.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS,
							HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING);
					showAddDialog(cellInfo);
				}
			}
			else
			{
				if (!(cellInfo.cell instanceof Folder))
				{
					// User long pressed on an item
					mWorkspace.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS,
							HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING);
					mWorkspace.startDrag(cellInfo);
				}
			}
		}
		return true;
	}

	@SuppressWarnings(
	{ "unchecked" })
	private void dismissPreview(final View v)
	{
		final PopupWindow window = (PopupWindow) v.getTag();
		if (window != null)
		{
			window.setOnDismissListener(new PopupWindow.OnDismissListener()
			{
				public void onDismiss()
				{
					ViewGroup group = (ViewGroup) v.getTag(R.id.workspace);
					int count = group.getChildCount();
					for (int i = 0; i < count; i++)
					{
						((ImageView) group.getChildAt(i)).setImageDrawable(null);
					}
					ArrayList<Bitmap> bitmaps = (ArrayList<Bitmap>) v.getTag(R.id.icon);
					for (Bitmap bitmap : bitmaps)
						bitmap.recycle();

					v.setTag(R.id.workspace, null);
					v.setTag(R.id.icon, null);
					window.setOnDismissListener(null);
				}
			});
			window.dismiss();
		}
		v.setTag(null);
	}

	private void showPreviews(View anchor)
	{
		showPreviews(anchor, 0, mWorkspace.getChildCount());
	}

	private void showPreviews(final View anchor, int start, int end)
	{
		final Resources resources = getResources();
		final Workspace workspace = mWorkspace;

		CellLayout cell = ((CellLayout) workspace.getChildAt(start));

		float max = workspace.getChildCount();

		final Rect r = new Rect();
		resources.getDrawable(R.drawable.preview_background).getPadding(r);
		int extraW = (int) ((r.left + r.right) * max);
		int extraH = r.top + r.bottom;

		int aW = cell.getWidth() - extraW;
		float w = aW / max;

		int width = cell.getWidth();
		int height = cell.getHeight();
		int x = cell.getLeftPadding();
		int y = cell.getTopPadding();
		width -= (x + cell.getRightPadding());
		height -= (y + cell.getBottomPadding());

		float scale = w / width;

		int count = end - start;

		final float sWidth = width * scale;
		float sHeight = height * scale;

		LinearLayout preview = new LinearLayout(this);

		PreviewTouchHandler handler = new PreviewTouchHandler(anchor);
		ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>(count);

		for (int i = start; i < end; i++)
		{
			ImageView image = new ImageView(this);
			cell = (CellLayout) workspace.getChildAt(i);

			final Bitmap bitmap = Bitmap.createBitmap((int) sWidth, (int) sHeight, Bitmap.Config.ARGB_8888);

			final Canvas c = new Canvas(bitmap);
			c.scale(scale, scale);
			c.translate(-cell.getLeftPadding(), -cell.getTopPadding());
			cell.dispatchDraw(c);

			image.setBackgroundDrawable(resources.getDrawable(R.drawable.preview_background));
			image.setImageBitmap(bitmap);
			image.setTag(i);
			image.setOnClickListener(handler);
			image.setOnFocusChangeListener(handler);
			image.setFocusable(true);
			if (i == mWorkspace.getCurrentScreen())
				image.requestFocus();

			preview.addView(image, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

			bitmaps.add(bitmap);
		}

		final PopupWindow p = new PopupWindow(this);
		p.setContentView(preview);
		p.setWidth((int) (sWidth * count + extraW));
		p.setHeight((int) (sHeight + extraH));
		p.setAnimationStyle(R.style.AnimationPreview);
		p.setOutsideTouchable(true);
		p.setFocusable(true);
		p.setBackgroundDrawable(new ColorDrawable(0));
		p.showAsDropDown(anchor, 0, 0);

		p.setOnDismissListener(new PopupWindow.OnDismissListener()
		{
			public void onDismiss()
			{
				dismissPreview(anchor);
			}
		});

		anchor.setTag(p);
		anchor.setTag(R.id.workspace, preview);
		anchor.setTag(R.id.icon, bitmaps);
	}

	class PreviewTouchHandler implements View.OnClickListener, Runnable, View.OnFocusChangeListener
	{
		private final View mAnchor;

		public PreviewTouchHandler(View anchor)
		{
			mAnchor = anchor;
		}

		public void onClick(View v)
		{
			mWorkspace.snapToScreen((Integer) v.getTag());
			v.post(this);
		}

		public void run()
		{
			dismissPreview(mAnchor);
		}

		public void onFocusChange(View v, boolean hasFocus)
		{
			if (hasFocus)
			{
				mWorkspace.snapToScreen((Integer) v.getTag());
			}
		}
	}

	Workspace getWorkspace()
	{
		return mWorkspace;
	}

	@Override
	protected Dialog onCreateDialog(int id)
	{
		switch (id)
		{
			case DIALOG_CREATE_SHORTCUT:
				return new CreateShortcut().createDialog();
			case DIALOG_RENAME_FOLDER:
				return new RenameFolder().createDialog();
		}

		return super.onCreateDialog(id);
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog)
	{
		switch (id)
		{
			case DIALOG_CREATE_SHORTCUT:
				break;
			case DIALOG_RENAME_FOLDER:
				if (mFolderInfo != null)
				{
					EditText input = (EditText) dialog.findViewById(R.id.folder_name);
					final CharSequence text = mFolderInfo.getTitle(mIconCache);
					input.setText(text);
					input.setSelection(0, text.length());
				}
				break;
		}
	}

	void showRenameDialog(FolderInfo info)
	{
		mFolderInfo = info;
		mWaitingForResult = true;
		showDialog(DIALOG_RENAME_FOLDER);
	}

	private void showAddDialog(CellLayout.CellInfo cellInfo)
	{
		mAddItemCellInfo = cellInfo;
		mWaitingForResult = true;
		showDialog(DIALOG_CREATE_SHORTCUT);
	}

	private void pickShortcut()
	{
		Bundle bundle = new Bundle();

		ArrayList<String> shortcutNames = new ArrayList<String>();
		shortcutNames.add(getString(R.string.group_applications));
		bundle.putStringArrayList(Intent.EXTRA_SHORTCUT_NAME, shortcutNames);

		ArrayList<ShortcutIconResource> shortcutIcons = new ArrayList<ShortcutIconResource>();
		shortcutIcons.add(ShortcutIconResource.fromContext(Launcher.this, R.drawable.ic_launcher_application));
		bundle.putParcelableArrayList(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, shortcutIcons);

		Intent pickIntent = new Intent(Intent.ACTION_PICK_ACTIVITY);
		pickIntent.putExtra(Intent.EXTRA_INTENT, new Intent(Intent.ACTION_CREATE_SHORTCUT));
		pickIntent.putExtra(Intent.EXTRA_TITLE, getText(R.string.title_select_shortcut));
		pickIntent.putExtras(bundle);

		startActivityForResult(pickIntent, REQUEST_PICK_SHORTCUT);
	}

	private class RenameFolder
	{
		private EditText mInput;

		Dialog createDialog()
		{
			mWaitingForResult = true;
			final View layout = View.inflate(Launcher.this, R.layout.rename_folder, null);
			mInput = (EditText) layout.findViewById(R.id.folder_name);

			AlertDialog.Builder builder = new AlertDialog.Builder(Launcher.this);
			builder.setIcon(0);
			builder.setTitle(getString(R.string.rename_folder_title));
			builder.setCancelable(true);
			builder.setOnCancelListener(new Dialog.OnCancelListener()
			{
				public void onCancel(DialogInterface dialog)
				{
					cleanup();
				}
			});
			builder.setNegativeButton(getString(R.string.cancel_action), new Dialog.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int which)
				{
					cleanup();
				}
			});
			builder.setPositiveButton(getString(R.string.rename_action), new Dialog.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int which)
				{
					changeFolderName();
				}
			});
			builder.setView(layout);

			final AlertDialog dialog = builder.create();

			return dialog;
		}

		private void changeFolderName()
		{
			final String name = mInput.getText().toString();
			if (!TextUtils.isEmpty(name))
			{
				// Make sure we have the right folder info
				if (sFolders.containsKey(mFolderInfo.id))
					mFolderInfo = sFolders.get(mFolderInfo.id);
				mFolderInfo.setTitle(name);
				LauncherModel.updateItemInDatabase(Launcher.this, mFolderInfo);

				if (mWorkspaceLoading)
				{
					lockAllApps();
					mModel.startLoader(Launcher.this, false);
				}
				else
				{
					final FolderIcon folderIcon = (FolderIcon) mWorkspace.getViewForTag(mFolderInfo);
					if (folderIcon != null)
					{
						folderIcon.setText(name);
						getWorkspace().requestLayout();
					}
					else
					{
						lockAllApps();
						mWorkspaceLoading = true;
						mModel.startLoader(Launcher.this, false);
					}
				}
			}
			cleanup();
		}

		private void cleanup()
		{
			dismissDialog(DIALOG_RENAME_FOLDER);
			mWaitingForResult = false;
			mFolderInfo = null;
		}
	}

	// Now a part of LauncherModel.Callbacks. Used to reorder loading steps.
	public boolean isAllAppsVisible()
	{
		return (mAllAppsGrid != null) ? mAllAppsGrid.isVisible() : false;
	}

	// AllAppsView.Watcher
	public void zoomed(float zoom)
	{
		if (zoom == 1.0f)
		{
			mWorkspace.setVisibility(View.GONE);
		}
	}

	private void requestPickLiveFolder(boolean toDrawer)
	{
		// Insert extra item to handle inserting folder
		Bundle bundle = new Bundle();

		ArrayList<String> shortcutNames = new ArrayList<String>();
		shortcutNames.add(getString(R.string.group_folder));
		bundle.putStringArrayList(Intent.EXTRA_SHORTCUT_NAME, shortcutNames);

		ArrayList<ShortcutIconResource> shortcutIcons = new ArrayList<ShortcutIconResource>();
		shortcutIcons.add(ShortcutIconResource.fromContext(Launcher.this, R.drawable.ic_launcher_folder));
		bundle.putParcelableArrayList(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, shortcutIcons);

		Intent pickIntent = new Intent(Intent.ACTION_PICK_ACTIVITY);
		pickIntent.putExtra(Intent.EXTRA_INTENT, new Intent(LiveFolders.ACTION_CREATE_LIVE_FOLDER));
		pickIntent.putExtra(Intent.EXTRA_TITLE, getText(R.string.title_select_live_folder));
		pickIntent.putExtras(bundle);

		startActivityForResult(pickIntent, toDrawer ? REQUEST_PICK_LIVE_FOLDER_DRAWER : REQUEST_PICK_LIVE_FOLDER);
	}

	public void showAllApps(boolean animated)
	{
		// to show all apps
		mAllAppsGrid.zoom(1.0f, animated);

		((View) mAllAppsGrid).setFocusable(true);
		((View) mAllAppsGrid).requestFocus();

		// TODO: fade these two too
		mDeleteZone.setVisibility(View.GONE);
	}

	/**
	 * Things to test when changing this code. - Home from workspace - from
	 * center screen - from other screens - Home from all apps - from center
	 * screen - from other screens - Back from all apps - from center screen -
	 * from other screens - Launch app from workspace and quit - with back -
	 * with home - Launch app from all apps and quit - with back - with home -
	 * Go to a screen that's not the default, then all apps, and launch and app,
	 * and go back - with back -with home - On workspace, long press power and
	 * go back - with back - with home - On all apps, long press power and go
	 * back - with back - with home - On workspace, power off - On all apps,
	 * power off - Launch an app and turn off the screen while in that app - Go
	 * back with home key - Go back with back key TODO: make this not go to
	 * workspace - From all apps - From workspace - Enter and exit car mode
	 * (becuase it causes an extra configuration changed) - From all apps - From
	 * the center workspace - From another workspace
	 */
	public void closeAllApps(boolean animated)
	{
		if (mAllAppsGrid.isVisible())
		{
			mWorkspace.setVisibility(View.VISIBLE);
			mAllAppsGrid.zoom(0.0f, animated);
			((View) mAllAppsGrid).setFocusable(false);
			mWorkspace.getChildAt(mWorkspace.getCurrentScreen()).requestFocus();
		}
	}

	void lockAllApps()
	{
		// TODO
	}

	void unlockAllApps()
	{
		// TODO
	}

	/**
	 * Displays the shortcut creation dialog and launches, if necessary, the
	 * appropriate activity.
	 */
	private class CreateShortcut implements DialogInterface.OnClickListener, DialogInterface.OnCancelListener,
			DialogInterface.OnDismissListener
	{

		private AddAdapter mAdapter;

		Dialog createDialog()
		{
			mWaitingForResult = true;
			mAdapter = new AddAdapter(Launcher.this);

			final AlertDialog.Builder builder = new AlertDialog.Builder(Launcher.this);
			builder.setTitle(getString(R.string.menu_item_add_item));
			builder.setAdapter(mAdapter, this);

			builder.setInverseBackgroundForced(true);

			AlertDialog dialog = builder.create();
			dialog.setOnCancelListener(this);
			dialog.setOnDismissListener(this);

			return dialog;
		}

		public void onCancel(DialogInterface dialog)
		{
			mWaitingForResult = false;
			cleanup();
		}

		public void onDismiss(DialogInterface dialog)
		{
		}

		private void cleanup()
		{
			try
			{
				dismissDialog(DIALOG_CREATE_SHORTCUT);
			}
			catch (Exception e)
			{
				// An exception is thrown if the dialog is not visible, which is
				// fine
			}
		}

		/**
		 * Handle the action clicked in the "Add to home" dialog.
		 */
		public void onClick(DialogInterface dialog, int which)
		{
			getResources();
			cleanup();

			switch (which)
			{
				case AddAdapter.ITEM_SHORTCUT:
				{
					// Insert extra item to handle picking application
					pickShortcut();
					break;
				}
				case AddAdapter.ITEM_APPWIDGET:
				{
					int appWidgetId = Launcher.this.mAppWidgetHost.allocateAppWidgetId();

					Intent pickIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_PICK);
					pickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
					// start the pick activity
					startActivityForResult(pickIntent, REQUEST_PICK_APPWIDGET);
					break;
				}
					// case AddAdapter.ITEM_LIVE_FOLDER:
					// {
					// Launcher.this.requestPickLiveFolder(false);
					// break;
					// }
					// case AddAdapter.ITEM_ANYCUT:
					// {
					// Intent anycutIntent = new Intent();
					// anycutIntent.setClass(Launcher.this,
					// CustomShirtcutActivity.class);
					// startActivityForResult(anycutIntent,
					// REQUEST_PICK_ANYCUT);
					// break;
					// }
				case AddAdapter.ITEM_WALLPAPER:
				{
					startWallpaper();
					break;
				}
			}
		}
	}

	/**
	 * Receives notifications when applications are added/removed.
	 */
	private class CloseSystemDialogsIntentReceiver extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			closeSystemDialogs();
			String reason = intent.getStringExtra("reason");
			if (!"homekey".equals(reason))
			{
				boolean animate = true;
				if (mPaused || "lock".equals(reason))
				{
					animate = false;
				}
				closeAllApps(animate);
			}
		}
	}

	/**
	 * If the activity is currently paused, signal that we need to re-run the
	 * loader in onResume.
	 * 
	 * This needs to be called from incoming places where resources might have
	 * been loaded while we are paused. That is becaues the Configuration might
	 * be wrong when we're not running, and if it comes back to what it was when
	 * we were paused, we are not restarted.
	 * 
	 * Implementation of the method from LauncherModel.Callbacks.
	 * 
	 * @return true if we are currently paused. The caller might be able to skip
	 *         some work in that case since we will come back again.
	 */
	public boolean setLoadOnResume()
	{
		if (mPaused)
		{
			Log.i(TAG, "setLoadOnResume");
			mOnResumeNeedsLoad = true;
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Implementation of the method from LauncherModel.Callbacks.
	 */
	public int getCurrentWorkspaceScreen()
	{
		if (mWorkspace != null)
		{
			return mWorkspace.getCurrentScreen();
		}
		else
		{
			return SCREEN_COUNT / 2;
		}
	}

	/**
	 * Refreshes the shortcuts shown on the workspace.
	 * 
	 * Implementation of the method from LauncherModel.Callbacks.
	 */
	public void startBinding()
	{
		final Workspace workspace = mWorkspace;
		int count = workspace.getChildCount();
		for (int i = 0; i < count; i++)
		{
			// Use removeAllViewsInLayout() to avoid an extra requestLayout()
			// and invalidate().
			((ViewGroup) workspace.getChildAt(i)).removeAllViewsInLayout();
		}
	}

	/**
	 * Bind the items start-end from the list.
	 * 
	 * Implementation of the method from LauncherModel.Callbacks.
	 */
	public void bindItems(ArrayList<ItemInfo> shortcuts, int start, int end)
	{

		setLoadOnResume();

		final Workspace workspace = mWorkspace;

		for (int i = start; i < end; i++)
		{
			final ItemInfo item = shortcuts.get(i);
			mDesktopItems.add(item);
			switch (item.itemType)
			{
				case LauncherSettings.Favorites.ITEM_TYPE_SHORTCUT:
					final View shortcut = createShortcut((ShortcutInfo) item);
					workspace.addInScreen(shortcut, item.screen, item.cellX, item.cellY, 1, 1, false);
					break;
				case LauncherSettings.Favorites.ITEM_TYPE_USER_FOLDER:
					final FolderIcon newFolder = FolderIcon.fromXml(R.layout.folder_icon, this, (ViewGroup) workspace
							.getChildAt(workspace.getCurrentScreen()), (UserFolderInfo) item);
					workspace.addInScreen(newFolder, item.screen, item.cellX, item.cellY, 1, 1, false);
					break;
				case LauncherSettings.Favorites.ITEM_TYPE_LIVE_FOLDER:
					final FolderIcon newLiveFolder = LiveFolderIcon.fromXml(R.layout.live_folder_icon, this,
							(ViewGroup) workspace.getChildAt(workspace.getCurrentScreen()), (LiveFolderInfo) item);
					workspace.addInScreen(newLiveFolder, item.screen, item.cellX, item.cellY, 1, 1, false);
					break;
			}
		}

		workspace.requestLayout();
	}

	/**
	 * Implementation of the method from LauncherModel.Callbacks.
	 */
	public void bindFolders(HashMap<Long, FolderInfo> folders)
	{
		setLoadOnResume();
		sFolders.clear();
		sFolders.putAll(folders);
	}

	/**
	 * Callback saying that there aren't any more items to bind.
	 * 
	 * Implementation of the method from LauncherModel.Callbacks.
	 */
	public void finishBindingItems()
	{
		setLoadOnResume();

		if (mSavedState != null)
		{
			if (!mWorkspace.hasFocus())
			{
				mWorkspace.getChildAt(mWorkspace.getCurrentScreen()).requestFocus();
			}

			final long[] userFolders = mSavedState.getLongArray(RUNTIME_STATE_USER_FOLDERS);
			if (userFolders != null)
			{
				for (long folderId : userFolders)
				{
					final FolderInfo info = sFolders.get(folderId);
					if (info != null)
					{
						doOpenFolder(info);
					}
				}
				final Folder openFolder = mWorkspace.getOpenFolder();
				if (openFolder != null)
				{
					openFolder.requestFocus();
				}
			}

			mSavedState = null;
		}

		if (mSavedInstanceState != null)
		{
			// sometimes on rotating the phone,
			// some widgets fail to restore its states.... so... damn.
			// In more detail: The scrollable widget fail.
			// Would be nice to have a good workaround here!
			try
			{
				super.onRestoreInstanceState(mSavedInstanceState);
			}
			catch (Exception e)
			{
			}
			mSavedInstanceState = null;
		}

		mWorkspaceLoading = false;
	}

	/**
	 * Add the icons for all apps.
	 * 
	 * Implementation of the method from LauncherModel.Callbacks.
	 */
	public void bindAllApplications(ArrayList<ShortcutInfo> apps, ArrayList<IconItemInfo> otherItems)
	{
		mAllAppsGrid.setApps(apps);
		mAllAppsGrid.addApps(otherItems);
	}

	/**
	 * A package was installed.
	 * 
	 * Implementation of the method from LauncherModel.Callbacks.
	 */
	public void bindAppsAdded(ArrayList<? extends IconItemInfo> apps)
	{
		setLoadOnResume();
		removeDialog(DIALOG_CREATE_SHORTCUT);
		mAllAppsGrid.addApps(apps);
	}

	/**
	 * A package was updated.
	 * 
	 * Implementation of the method from LauncherModel.Callbacks.
	 */
	public void bindAppsUpdated(ArrayList<ShortcutInfo> apps)
	{
		setLoadOnResume();
		removeDialog(DIALOG_CREATE_SHORTCUT);
		mWorkspace.updateShortcuts(apps);
		mAllAppsGrid.updateApps(apps);
	}

	/**
	 * A package was uninstalled.
	 * 
	 * Implementation of the method from LauncherModel.Callbacks.
	 */
	public void bindAppsRemoved(ArrayList<ShortcutInfo> apps, boolean permanent)
	{
		removeDialog(DIALOG_CREATE_SHORTCUT);
		if (permanent)
		{
			mWorkspace.removeItems(apps);
		}
		mAllAppsGrid.removeApps(apps);
	}

	public void showActions(ItemInfo info, View view, PopupWindow.OnDismissListener onDismissListener)
	{
		if (info == null || view == null)
			return;

		List<EditAction> actions = info.getAvailableActions(view, this);
		if (actions.size() <= 0)
			return;
		final View finalview = view;
		final ItemInfo finalInfo = info;
		int[] xy = new int[2];
		// fills the array with the computed coordinates
		view.getLocationInWindow(xy);
		new Rect(xy[0], xy[1], xy[0] + view.getWidth(), xy[1] + view.getHeight());

		// a new QuickActionWindow object
		final QuickAction qa = new QuickAction(view);
		view.setTag(org.accenture.product.lemonade.R.id.TAG_PREVIEW, qa);
		if (onDismissListener != null)
		{
			qa.setOnDismissListener(onDismissListener);
		}

		// adds an item to the badge and defines the quick action to be
		// triggered
		// when the item is clicked on
		for (EditAction action : actions)
		{
			final EditAction finalaction = action;
			Drawable icon;
			CharSequence title;
			if (action.getIconResourceId() == 0)
			{
				icon = action.getIconDrawable();
			}
			else
			{
				icon = getResources().getDrawable(action.getIconResourceId());
			}
			if (action.getTitleResourceId() == 0)
			{
				title = action.getTitleString();
			}
			else
			{
				title = getResources().getString(action.getTitleResourceId());
			}

			ActionItem ai = new ActionItem(icon);
			if (title != null)
				ai.setTitle(title.toString());

			ai.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					qa.dismiss();
					finalInfo.executeAction(finalaction, finalview, Launcher.this);
				}
			});

			// qa.addActionItem(ai);
		}
		// shows the quick action window on the screen
		qa.show();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
		mWorkspaceLoading = true;
		final int currScreen = mWorkspace.getCurrentScreen();
		checkForLocaleChange();
		setWallpaperDimension();
		final int count = mWorkspace.getChildCount();
		// get icons properties
		final Resources r = getResources();
		final int margintop = r.getDimensionPixelSize(R.dimen.icon_layout_marginTop);
		final int marginbottom = r.getDimensionPixelSize(R.dimen.icon_layout_marginBottom);
		final int marginleft = r.getDimensionPixelSize(R.dimen.icon_layout_marginLeft);
		final int marginright = r.getDimensionPixelSize(R.dimen.icon_layout_marginRight);
		final int paddingtop = r.getDimensionPixelSize(R.dimen.icon_paddingTop);
		final int paddingbottom = r.getDimensionPixelSize(R.dimen.icon_paddingBottom);
		final int paddingleft = r.getDimensionPixelSize(R.dimen.icon_paddingLeft);
		final int paddingright = r.getDimensionPixelSize(R.dimen.icon_paddingRight);
		final int drawablePadding = r.getDimensionPixelSize(R.dimen.icon_drawablePadding);

		for (int i = 0; i < count; i++)
		{
			CellLayout screen = (CellLayout) mWorkspace.getChildAt(i);
			for (int j = 0; j < screen.getChildCount(); j++)
			{
				if (screen.getChildAt(j) instanceof BubbleTextView)
				{
					final BubbleTextView v = (BubbleTextView) screen.getChildAt(j);
					CellLayout.LayoutParams lp = (CellLayout.LayoutParams) v.getLayoutParams();
					lp.topMargin = margintop;
					lp.bottomMargin = marginbottom;
					lp.leftMargin = marginleft;
					lp.rightMargin = marginright;
					v.setLayoutParams(lp);
					v.setPadding(paddingleft, paddingtop, paddingright, paddingbottom);
					v.setCompoundDrawablePadding(drawablePadding);
				}
			}
			screen.reMeasure(this);
		}

		final Workspace workspace = mWorkspace;

		workspace.requestLayout();
	}

	boolean ocuppiedArea(int screen, long ignoreItemId, Rect rect)
	{
		final ArrayList<ItemInfo> desktopItems = mDesktopItems;
		Rect r = new Rect();
		for (ItemInfo it : desktopItems)
		{
			if (it.screen == screen && it.id != ignoreItemId)
			{
				r.set(it.cellX, it.cellY, it.cellX + it.spanX, it.cellY + it.spanY);
				if (rect.intersect(r))
				{
					return true;
				}
			}
		}
		return false;
	}

	public void setCurrentScreen(int index)
	{
		// TODO Auto-generated method stub
		mWorkspace.setCurrentScreen(index);
	}

	public void setWallpapers(WallpapersBean bean)
	{
		// TODO Auto-generated method stub
		try
		{
			InputStream ins = getAssets().open(bean.get_PagePath());
			// BitmapDrawable bitmapDrawable=new BitmapDrawable(ins);
			// Bitmap bitmap = bitmapDrawable.getBitmap();
			if (null != ins)
				setWallpaper(ins);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void setScene()
	{

		SceneDateBaseAdapter scene = SceneDateBaseAdapter.getInstance();
		scene.open();
		ResourcesUtil.getInstance()._CurrentSceneBean = scene.getCurrentScene();
		scene.close();

		SCREEN_COUNT = ResourcesUtil.getInstance()._CurrentSceneBean.getScreenCount();

		setContentView(R.layout.launcher);
		setupViews();

		int centerIndex = SCREEN_COUNT / 2;

		mWorkspace.setCurrentScreen(centerIndex);

		mModel.startLoader(this, false);

	}

	/**
	 * 换icon包后刷新桌面
	 */
	public void refreshDesttopforIconChange()
	{

		// setContentView(R.layout.launcher);
		setupViews();

		mModel.startLoader(this, false);

	}

	public void setWallpapers(TypeBean bean)
	{
		// TODO Auto-generated method stub
		try
		{
			InputStream ins = getAssets().open(bean.getPagePath());

			SceneBean s = getCurrentScene();

			s.setWallpaperId(bean.getId());

			// BitmapDrawable bitmapDrawable=new BitmapDrawable(ins);
			// Bitmap bitmap = bitmapDrawable.getBitmap();
			if (null != ins)
				setWallpaper(ins);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void addOneScreen(int index)
	{

		final Workspace workspace = mWorkspace;
		workspace.addOneScreen(index);
		SCREEN_COUNT = mWorkspace.getChildCount();
		FRONT_SCREEN_COUNT = mWorkspace.getFrontScreenCount();
		ScreenSwitcherActivity.initPic(getThumbList(0, mWorkspace.getChildCount()));

		SceneDateBaseAdapter sceneDateBaseAdapter = SceneDateBaseAdapter.getInstance();
		SceneBean sceneBean = getCurrentScene();
		sceneDateBaseAdapter.open();
		sceneDateBaseAdapter.addScreen(sceneBean);
		sceneDateBaseAdapter.close();

		// 如果是往前插
		if (index == 0)
		{

			WidgetDataBaseAdapter adapter = WidgetDataBaseAdapter.getInstance();
			adapter.open();
			adapter.addScreenByScene(sceneBean.getId());
			adapter.close();
			// 修改app代码
			LauncherProvider.DatabaseHelper databaseHelper = new LauncherProvider.DatabaseHelper(this);
			databaseHelper.addScreenByScene(sceneBean.getId());

		}
	}

	public void removeOneScreen(int index)
	{
		final Workspace workspace = mWorkspace;
		workspace.removeScreen(index);

		SCREEN_COUNT = mWorkspace.getChildCount();
		FRONT_SCREEN_COUNT = mWorkspace.getFrontScreenCount();
		ScreenSwitcherActivity.initPic(getThumbList(0, mWorkspace.getChildCount()));

		SceneBean sceneBean = getCurrentScene();
		SceneDateBaseAdapter adapter = SceneDateBaseAdapter.getInstance();
		// scene屏数减一
		adapter.open();
		adapter.setScreenValue(sceneBean, sceneBean.getScreenCount() - 1);
		adapter.close();

		WidgetDataBaseAdapter widgetDataBaseAdapter = WidgetDataBaseAdapter.getInstance();
		widgetDataBaseAdapter.open();
		widgetDataBaseAdapter.deleteByScreenAndScene(sceneBean.getScreenCount(), sceneBean.getId(), index);
		widgetDataBaseAdapter.close();
		LauncherProvider.DatabaseHelper databaseHelper = new LauncherProvider.DatabaseHelper(this);
		databaseHelper.deleteByScreenAndScene(sceneBean.getScreenCount(), sceneBean.getId(), index);

	}

	public InputStream getIconInputStream(String path)
	{

		InputStream inputStream = null;
		try
		{
			inputStream = getResources().getAssets().open(path);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return inputStream;
	}
	 
	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
//		if (menuDialog == null) {
//			menuDialog = new MenuDialog(this);
//			menuDialog.show();
////			View menuView=View.inflate(this, R.layout.menu, null);
////			menuDialog = new AlertDialog.Builder(this).setView(menuView).show();
//			
//			
//
//
//		}else
//			menuDialog.show();
		
		Intent it=new Intent(this,MenuActivity.class);
		startActivity(it);
		
		return false;// 返回为true 则显示系统menu
	}

}
