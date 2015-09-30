package org.accenture.product.lemonade;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.accenture.product.lemonade.appdb.AppDB;
import org.accenture.product.lemonade.content.WidgetDataBaseAdapter;
import org.accenture.product.lemonade.model.LemonadeBean;
import org.accenture.product.lemonade.model.SceneBean;
import org.accenture.product.lemonade.model.WidgetBean;
import org.accenture.product.lemonade.settings.LauncherSettings;
import org.accenture.product.lemonade.settings.LauncherSettings.Favorites;
import org.accenture.product.lemonade.util.ResourcesUtil;
import org.apache.http.util.LangUtils;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Parcelable;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;
import android.widget.SlidingDrawer;

/**
 * Maintains in-memory state of the Launcher. It is expected that there should
 * be only one LauncherModel object held in a static. Also provide APIs for
 * updating the database state for the Launcher.
 */
public class LauncherModel extends BroadcastReceiver
{
	static final String TAG = "Launcher.Model";

	private static final int ITEMS_CHUNK = 6; // batch size for the workspace
	// icons

	private final Object mLock = new Object();
	private final DeferredHandler mHandler = new DeferredHandler();
	private LoaderTask mLoaderTask;

	private static final HandlerThread sWorkerThread = new HandlerThread("launcher-loader");
	static
	{
		sWorkerThread.start();
	}
	private static final Handler sWorker = new Handler(sWorkerThread.getLooper());

	// We start off with everything not loaded. After that, we assume that
	// our monitoring of the package manager provides all updates and we never
	// need to do a requery. These are only ever touched from the loader thread.
	private boolean mAllAppsLoaded;

	private WeakReference<Callbacks> mCallbacks;

	private final AllAppsList mAllAppsList; // only access in worker thread
	private final IconCache mIconCache;
	final static ArrayList<ItemInfo> mItems = new ArrayList<ItemInfo>();
	final static ArrayList<IconItemInfo> mAdditionalDrawerItems = new ArrayList<IconItemInfo>();
	// final ArrayList<LauncherAppWidgetInfo> mAppWidgets = new
	// ArrayList<LauncherAppWidgetInfo>();
	Vector<LemonadeBean> mAppWidgets = new Vector<LemonadeBean>();
	final HashMap<Long, FolderInfo> mFolders = new HashMap<Long, FolderInfo>();

	private final Bitmap mDefaultIcon;

	public interface Callbacks
	{
		public boolean setLoadOnResume();

		public int getCurrentWorkspaceScreen();

		public void startBinding();

		public void bindItems(ArrayList<ItemInfo> shortcuts, int start, int end);

		public void bindFolders(HashMap<Long, FolderInfo> folders);

		public void finishBindingItems();

		public void bindAppWidget(WidgetBean widgetBean);

		public void bindAllApplications(ArrayList<ShortcutInfo> apps, ArrayList<IconItemInfo> otherItems);

		public void bindAppsAdded(ArrayList<? extends IconItemInfo> apps);

		public void bindAppsUpdated(ArrayList<ShortcutInfo> apps);

		public void bindAppsRemoved(ArrayList<ShortcutInfo> apps, boolean permanent);

		/**
		 * 修改widget位置信息写入数据库
		 * 
		 * @param widgetBean
		 */
		public void updateWidgetBySendBroadcast(WidgetBean widgetBean);

		public void deleteWidgetBySendBroadast(WidgetBean widgetBean);

		public boolean isAllAppsVisible();
	}

	LauncherModel(LauncherApplication app, IconCache iconCache)
	{
		mAllAppsList = new AllAppsList(iconCache);
		mIconCache = iconCache;

		mDefaultIcon = Utilities.createIconBitmap(app.getPackageManager().getDefaultActivityIcon(), app);
	}

	public Bitmap getFallbackIcon()
	{
		return Bitmap.createBitmap(mDefaultIcon);
	}

	/**
	 * Adds an item to the DB if it was not created previously, or move it to a
	 * new <container, screen, cellX, cellY>
	 */
	static void addOrMoveItemInDatabase(Context context, ItemInfo item, long container, int screen, int cellX, int cellY)
	{
		if (item.container == ItemInfo.NO_ID)
		{
			// From all apps
			addItemToDatabase(context, item, container, screen, cellX, cellY, false);
		}
		else
		{
			// From somewhere else
			moveItemInDatabase(context, item, container, screen, cellX, cellY);
		}
	}

	/**
	 * update Widget x y into database
	 * 
	 * @param context
	 * @param item
	 * @param container
	 * @param screen
	 * @param cellX
	 * @param cellY
	 */
	static void moveWidgetInDatabase(Callbacks context, WidgetBean widgetBean)
	{
		context.updateWidgetBySendBroadcast(widgetBean);

	}

	/**
	 * Move an item in the DB to a new <container, screen, cellX, cellY>
	 */
	static void moveItemInDatabase(Context context, ItemInfo item, long container, int screen, int cellX, int cellY)
	{
		item.container = container;
		item.screen = screen;
		item.cellX = cellX;
		item.cellY = cellY;

		final Uri uri = LauncherSettings.Favorites.getContentUri(item.id, false);
		final ContentValues values = new ContentValues();
		final ContentResolver cr = context.getContentResolver();

		values.put(LauncherSettings.Favorites.CONTAINER, item.container);
		values.put(LauncherSettings.Favorites.CELLX, item.cellX);
		values.put(LauncherSettings.Favorites.CELLY, item.cellY);
		values.put(LauncherSettings.Favorites.SCREEN, item.screen);

		sWorker.post(new Runnable()
		{
			public void run()
			{
				cr.update(uri, values, null, null);
			}
		});
	}

	/**
	 * Returns true if the shortcuts already exists in the database. we identify
	 * a shortcut by its title and intent.
	 */
	static boolean shortcutExists(Context context, String title, Intent intent)
	{
		final ContentResolver cr = context.getContentResolver();
		Cursor c = cr.query(LauncherSettings.Favorites.CONTENT_URI, new String[]
		{ "title", "intent" }, "title=? and intent=?", new String[]
		{ title, intent.toUri(0) }, null);
		boolean result = false;
		try
		{
			result = c.moveToFirst();
		}
		finally
		{
			c.close();
		}
		return result;
	}

	/**
	 * Find a folder in the db, creating the FolderInfo if necessary, and adding
	 * it to folderList.
	 */
	FolderInfo getFolderById(Context context, HashMap<Long, FolderInfo> folderList, long id)
	{
		final ContentResolver cr = context.getContentResolver();
		Cursor c = cr.query(LauncherSettings.Favorites.CONTENT_URI, null, "_id=? and (itemType=? or itemType=?)", new String[]
		{ String.valueOf(id), String.valueOf(LauncherSettings.Favorites.ITEM_TYPE_USER_FOLDER),
				String.valueOf(LauncherSettings.Favorites.ITEM_TYPE_LIVE_FOLDER) }, null);

		try
		{
			if (c.moveToFirst())
			{
				final int itemTypeIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites.ITEM_TYPE);
				final int titleIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites.TITLE);
				final int containerIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites.CONTAINER);
				final int screenIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites.SCREEN);
				final int cellXIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites.CELLX);
				final int cellYIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites.CELLY);

				FolderInfo folderInfo = null;
				switch (c.getInt(itemTypeIndex))
				{
					case LauncherSettings.Favorites.ITEM_TYPE_USER_FOLDER:
						folderInfo = findOrMakeUserFolder(folderList, id);
						break;
					case LauncherSettings.Favorites.ITEM_TYPE_LIVE_FOLDER:
						folderInfo = findOrMakeLiveFolder(folderList, id);
						break;
				}

				folderInfo.setTitle(c.getString(titleIndex));
				folderInfo.id = id;
				folderInfo.container = c.getInt(containerIndex);
				folderInfo.screen = c.getInt(screenIndex);
				folderInfo.cellX = c.getInt(cellXIndex);
				folderInfo.cellY = c.getInt(cellYIndex);

				return folderInfo;
			}
		}
		finally
		{
			c.close();
		}

		return null;
	}

	/**
	 * Add an item to the database in a specified container. Sets the container,
	 * screen, cellX and cellY fields of the item. Also assigns an ID to the
	 * item.
	 */
	static void addItemToDatabase(Context context, ItemInfo item, long container, int screen, int cellX, int cellY,
			boolean notify)
	{
		item.container = container;
		item.screen = screen;
		item.cellX = cellX;
		item.cellY = cellY;

		final ContentValues values = new ContentValues();
		final ContentResolver cr = context.getContentResolver();

		item.onAddToDatabase(values);

		Uri result = cr.insert(notify ? LauncherSettings.Favorites.CONTENT_URI
				: LauncherSettings.Favorites.CONTENT_URI_NO_NOTIFICATION, values);

		if (result != null)
		{
			item.id = Integer.parseInt(result.getPathSegments().get(1));
		}

		if (item instanceof IconItemInfo)
		{
			IconItemInfo iii = (IconItemInfo) item;
			if (container == Favorites.CONTAINER_DRAWER)
				mAdditionalDrawerItems.add(iii);
			else if (container == Favorites.CONTAINER_DESKTOP)
				mItems.add(iii);
		}
	}

	/**
	 * Update an item to the database in a specified container.
	 */
	static void updateItemInDatabase(Context context, ItemInfo item)
	{
		final ContentValues values = new ContentValues();
		final ContentResolver cr = context.getContentResolver();

		item.onAddToDatabase(values);

		cr.update(LauncherSettings.Favorites.getContentUri(item.id, false), values, null, null);
	}

	void deleteWidgetFromDatabase(Callbacks context, ItemInfo item)
	{

		long id = item.id;
		WidgetBean widgetBean = new WidgetBean();
		widgetBean.setId((int) id);
		context.deleteWidgetBySendBroadast(widgetBean);
		if (mItems.contains(item))
			mItems.remove(item);
		if (mAdditionalDrawerItems.contains(item))
			mAdditionalDrawerItems.remove(item);

	}

	/**
	 * Removes the specified item from the database
	 * 
	 * @param context
	 * @param item
	 */
	void deleteItemFromDatabase(Context context, ItemInfo item)
	{
		final ContentResolver cr = context.getContentResolver();
		final Uri uriToDelete = LauncherSettings.Favorites.getContentUri(item.id, false);

		sWorker.post(new Runnable()
		{
			public void run()
			{
				cr.delete(uriToDelete, null, null);
			}
		});
		if (mItems.contains(item))
			mItems.remove(item);
		if (mAdditionalDrawerItems.contains(item))
			mAdditionalDrawerItems.remove(item);
	}

	/**
	 * Remove the contents of the specified folder from the database
	 */
	static void deleteUserFolderContentsFromDatabase(Context context, UserFolderInfo info)
	{
		final ContentResolver cr = context.getContentResolver();

		cr.delete(LauncherSettings.Favorites.getContentUri(info.id, false), null, null);
		cr.delete(LauncherSettings.Favorites.CONTENT_URI, LauncherSettings.Favorites.CONTAINER + "=" + info.id, null);
	}

	public ItemInfo getItemInfoById(long id)
	{
		for (ItemInfo ii : mItems)
		{
			if (ii.id == id)
				return ii;
		}
		for (IconItemInfo ii : mAdditionalDrawerItems)
		{
			if (ii.id == id)
				return ii;
		}
		return null;
	}

	/**
	 * Set this as the current Launcher activity object for the loader.
	 */
	public void initialize(Callbacks callbacks)
	{
		synchronized (mLock)
		{
			mCallbacks = new WeakReference<Callbacks>(callbacks);
		}
	}

	void enqueuePackageUpdated(PackageUpdatedTask task)
	{
		sWorker.post(task);
	}

	/**
	 * Call from the handler for ACTION_PACKAGE_ADDED, ACTION_PACKAGE_REMOVED
	 * and ACTION_PACKAGE_CHANGED.
	 */
	@Override
	public void onReceive(Context context, Intent intent)
	{
		final String action = intent.getAction();

		if (AppDB.INTENT_DB_CHANGED.equals(action))
		{

			if (intent.hasExtra(AppDB.EXTRA_ADDED))
			{
				long[] added = intent.getLongArrayExtra(AppDB.EXTRA_ADDED);
				if (added != null && added.length > 0)
				{
					PackageUpdatedTask put = new PackageUpdatedTask(PackageUpdatedTask.OP_ADD, added, context);
					enqueuePackageUpdated(put);
				}
			}

			if (intent.hasExtra(AppDB.EXTRA_DELETED_COMPONENT_NAMES))
			{
				String[] removed = intent.getStringArrayExtra(AppDB.EXTRA_DELETED_COMPONENT_NAMES);
				if (removed != null)
				{
					PackageUpdatedTask put = new PackageUpdatedTask(PackageUpdatedTask.OP_REMOVE_CNAMES, removed, context);
					enqueuePackageUpdated(put);
				}
			}

			if (intent.hasExtra(AppDB.EXTRA_DELETED_PACKAGE))
			{
				String removed = intent.getStringExtra(AppDB.EXTRA_DELETED_PACKAGE);
				if (removed != null)
				{
					PackageUpdatedTask put = new PackageUpdatedTask(PackageUpdatedTask.OP_REMOVE_PACKAGE, removed, context);
					enqueuePackageUpdated(put);
				}
			}

			if (intent.hasExtra(AppDB.EXTRA_UPDATED))
			{
				long[] updated = intent.getLongArrayExtra(AppDB.EXTRA_UPDATED);
				if (updated != null && updated.length > 0)
				{
					PackageUpdatedTask put = new PackageUpdatedTask(PackageUpdatedTask.OP_UPDATE, updated, context);
					enqueuePackageUpdated(put);
				}
			}

		}
		// TODO_BOOMBULER
		/*
		 * else if
		 * (Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE.equals(action)) { //
		 * First, schedule to add these apps back in. String[] packages =
		 * intent.getStringArrayExtra(Intent.EXTRA_CHANGED_PACKAGE_LIST);
		 * enqueuePackageUpdated(new
		 * PackageUpdatedTask(PackageUpdatedTask.OP_ADD, packages)); // Then,
		 * rebind everything. boolean runLoader = true; if (mCallbacks != null)
		 * { Callbacks callbacks = mCallbacks.get(); if (callbacks != null) { //
		 * If they're paused, we can skip loading, because they'll do it again
		 * anyway if (callbacks.setLoadOnResume()) { runLoader = false; } } } if
		 * (runLoader) { startLoader(mApp, false); }
		 * 
		 * } else if
		 * (Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE.equals(action)) {
		 * String[] packages =
		 * intent.getStringArrayExtra(Intent.EXTRA_CHANGED_PACKAGE_LIST);
		 * enqueuePackageUpdated(new PackageUpdatedTask(
		 * PackageUpdatedTask.OP_UNAVAILABLE, packages));
		 * 
		 * }
		 */
	}

	private class PackageUpdatedTask implements Runnable
	{
		int mOp;
		long[] mAppIds;
		String[] mComponentNames;
		String mPackage;
		Context mContext;

		public static final int OP_ADD = 1;
		public static final int OP_UPDATE = 2;
		public static final int OP_REMOVE_CNAMES = 3;
		public static final int OP_REMOVE_PACKAGE = 4;
		public static final int OP_UNAVAILABLE = 5; // external media unmounted

		public PackageUpdatedTask(int op, long[] appIds, Context context)
		{
			mOp = op;
			mAppIds = appIds;
			mContext = context;
		}

		public PackageUpdatedTask(int op, String[] componentNames, Context context)
		{
			mOp = op;
			mComponentNames = componentNames;
			mContext = context;
		}

		public PackageUpdatedTask(int op, String packageName, Context context)
		{
			mOp = op;
			mPackage = packageName;
			mContext = context;
		}

		public void run()
		{
			AppDB appDB = new AppDB(mContext, mIconCache);

			switch (mOp)
			{
				case OP_ADD:
					List<ShortcutInfo> newApps = appDB.getApps(mAppIds);

					for (ShortcutInfo info : newApps)
					{
						mAllAppsList.add(info);
					}
					break;
				case OP_REMOVE_CNAMES:
				{
					mAllAppsList.removeComponentNames(mComponentNames);
				}
				case OP_REMOVE_PACKAGE:
				{
					mAllAppsList.removePackage(mPackage);
				}
				case OP_UPDATE:
					List<ShortcutInfo> updated = appDB.getApps(mAppIds);
					mAllAppsList.updateFromShortcuts(updated);
					break;
				/*
				 * case OP_UNAVAILABLE: for (int i=0; i<N; i++) {
				 * mAllAppsList.removePackage(packages[i]); } break;
				 */
			}

			ArrayList<ShortcutInfo> added = null;
			ArrayList<ShortcutInfo> removed = null;
			ArrayList<ShortcutInfo> modified = null;

			if (mAllAppsList.added.size() > 0)
			{
				added = mAllAppsList.added;
				mAllAppsList.added = new ArrayList<ShortcutInfo>();
			}
			if (mAllAppsList.removed.size() > 0)
			{
				removed = mAllAppsList.removed;
				mAllAppsList.removed = new ArrayList<ShortcutInfo>();
				for (ShortcutInfo info : removed)
				{
					mIconCache.remove(info.intent.getComponent());
				}
			}
			if (mAllAppsList.modified.size() > 0)
			{
				modified = mAllAppsList.modified;
				mAllAppsList.modified = new ArrayList<ShortcutInfo>();
			}

			final Callbacks callbacks = mCallbacks != null ? mCallbacks.get() : null;
			if (callbacks == null)
			{
				Log.w(TAG, "Nobody to tell about the new app.  Launcher is probably loading.");
				return;
			}

			if (added != null)
			{
				final ArrayList<ShortcutInfo> addedFinal = added;
				mHandler.post(new Runnable()
				{
					public void run()
					{
						if (callbacks == mCallbacks.get())
						{
							callbacks.bindAppsAdded(addedFinal);
						}
					}
				});
			}
			if (modified != null)
			{
				final ArrayList<ShortcutInfo> modifiedFinal = modified;
				mHandler.post(new Runnable()
				{
					public void run()
					{
						if (callbacks == mCallbacks.get())
						{
							callbacks.bindAppsUpdated(modifiedFinal);
						}
					}
				});
			}

			if (removed != null)
			{
				final boolean permanent = mOp != OP_UNAVAILABLE;
				final ArrayList<ShortcutInfo> removedFinal = removed;
				mHandler.post(new Runnable()
				{
					public void run()
					{
						if (callbacks == mCallbacks.get())
						{
							callbacks.bindAppsRemoved(removedFinal, permanent);
						}
					}
				});
			}
		}
	}

	public void startLoader(Context context, boolean isLaunching)
	{
		synchronized (mLock)
		{
			// Don't bother to start the thread if we know it's not going to do
			// anything
			if (mCallbacks != null && mCallbacks.get() != null)
			{
				// If there is already one running, tell it to stop.
				LoaderTask oldTask = mLoaderTask;
				if (oldTask != null)
				{
					if (oldTask.isLaunching())
					{
						// don't downgrade isLaunching if we're already running
						isLaunching = true;
					}
					oldTask.stopLocked();
				}
				mLoaderTask = new LoaderTask(context, isLaunching);
				sWorker.post(mLoaderTask);
			}
		}
	}

	public void stopLoader()
	{
		synchronized (mLock)
		{
			if (mLoaderTask != null)
			{
				mLoaderTask.stopLocked();
			}
		}
	}

	/**
	 * Runnable for the thread that loads the contents of the launcher: -
	 * workspace icons - widgets - all apps icons
	 */
	private class LoaderTask implements Runnable
	{
		private Context mContext;
		private final boolean mIsLaunching;
		private boolean mStopped;
		private boolean mLoadAndBindStepFinished;

		LoaderTask(Context context, boolean isLaunching)
		{
			mContext = context;
			mIsLaunching = isLaunching;
		}

		boolean isLaunching()
		{
			return mIsLaunching;
		}

		// kya //load it here
		private void loadAndBindWorkspace()
		{
			// Load the workspace

			// For now, just always reload the workspace. It's ~100 ms vs. the
			// binding which takes many hundreds of ms.
			// We can reconsider.

			loadWorkspace();
			if (mStopped)
			{
				return;
			}

			// Bind the workspace
			bindWorkspace();
		}

		private void waitForIdle()
		{
			// Wait until the either we're stopped or the other threads are
			// done.
			// This way we don't start loading all apps until the workspace has
			// settled
			// down.
			synchronized (LoaderTask.this)
			{
				mHandler.postIdle(new Runnable()
				{
					public void run()
					{
						synchronized (LoaderTask.this)
						{
							mLoadAndBindStepFinished = true;
							LoaderTask.this.notify();
						}
					}
				});

				while (!mStopped && !mLoadAndBindStepFinished)
				{
					try
					{
						this.wait();
					}
					catch (InterruptedException ex)
					{
						// Ignore
					}
				}
			}
		}

		public void run()
		{
			// Optimize for end-user experience: if the Launcher is up and //
			// running with the
			// All Apps interface in the foreground, load All Apps first.
			// Otherwise, load the
			// workspace first (default).
			final Callbacks cbk = mCallbacks.get();
			final boolean loadWorkspaceFirst = cbk != null ? (!cbk.isAllAppsVisible()) : true;

			keep_running:
			{
				// Elevate priority when Home launches for the first time to
				// avoid
				// starving at boot time. Staring at a blank home is not cool.
				synchronized (mLock)
				{
					android.os.Process.setThreadPriority(mIsLaunching ? Process.THREAD_PRIORITY_DEFAULT
							: Process.THREAD_PRIORITY_BACKGROUND);
				}

				if (loadWorkspaceFirst)
				{
					loadAndBindWorkspace();
				}
				else
				{
					loadAndBindAllApps();
				}

				if (mStopped)
				{
					break keep_running;
				}

				// Whew! Hard work done. Slow us down, and wait until the UI
				// thread has
				// settled down.
				synchronized (mLock)
				{
					if (mIsLaunching)
					{
						android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
					}
				}
				waitForIdle();

				// second step
				if (loadWorkspaceFirst)
				{
					loadAndBindAllApps();
				}
				else
				{
					loadAndBindWorkspace();
				}
			}

			// Clear out this reference, otherwise we end up holding it until
			// all of the
			// callback runnables are done.
			mContext = null;

			synchronized (mLock)
			{
				// If we are still the last one to be scheduled, remove
				// ourselves.
				if (mLoaderTask == this)
				{
					mLoaderTask = null;
				}
			}

			// Trigger a gc to try to clean up after the stuff is done, since
			// the
			// renderscript allocations aren't charged to the java heap.
			if (mStopped)
			{
				mHandler.post(new Runnable()
				{
					public void run()
					{
						System.gc();
					}
				});
			}
			else
			{
				mHandler.postIdle(new Runnable()
				{
					public void run()
					{
						System.gc();
					}
				});
			}
		}

		public void stopLocked()
		{
			synchronized (LoaderTask.this)
			{
				mStopped = true;
				this.notify();
			}
		}

		/**
		 * Gets the callbacks object. If we've been stopped, or if the launcher
		 * object has somehow been garbage collected, return null instead. Pass
		 * in the Callbacks object that was around when the deferred message was
		 * scheduled, and if there's a new Callbacks object around then also
		 * return null. This will save us from calling onto it with data that
		 * will be ignored.
		 */
		Callbacks tryGetCallbacks(Callbacks oldCallbacks)
		{
			synchronized (mLock)
			{
				if (mStopped)
				{
					return null;
				}

				if (mCallbacks == null)
				{
					return null;
				}

				final Callbacks callbacks = mCallbacks.get();
				if (callbacks != oldCallbacks)
				{
					return null;
				}
				if (callbacks == null)
				{
					Log.w(TAG, "no mCallbacks");
					return null;
				}

				return callbacks;
			}
		}

		// check & update map of what's occupied; used to discard
		// overlapping/invalid items
		private boolean checkItemPlacement(ItemInfo occupied[][][], ItemInfo item)
		{
			if (item.container != LauncherSettings.Favorites.CONTAINER_DESKTOP)
			{
				return true;
			}

			for (int x = item.cellX; x < (item.cellX + item.spanX); x++)
			{
				for (int y = item.cellY; y < (item.cellY + item.spanY); y++)
				{
					if (occupied[item.screen][x][y] != null)
					{
						Log.e(TAG, "Error loading shortcut " + item + " into cell (" + item.screen + ":" + x + "," + y
								+ ") occupied by " + occupied[item.screen][x][y]);
						return false;
					}
				}
			}
			for (int x = item.cellX; x < (item.cellX + item.spanX); x++)
			{
				for (int y = item.cellY; y < (item.cellY + item.spanY); y++)
				{
					occupied[item.screen][x][y] = item;
				}
			}
			return true;
		}

		private void loadWorkspace()
		{

			final Context context = mContext;
			final ContentResolver contentResolver = context.getContentResolver();
			final PackageManager manager = context.getPackageManager();
			final AppWidgetManager widgets = AppWidgetManager.getInstance(context);
			final boolean isSafeMode = manager.isSafeMode();

			mAdditionalDrawerItems.clear();
			mItems.clear();
			mAppWidgets.clear();
			mFolders.clear();

			final ArrayList<Long> itemsToRemove = new ArrayList<Long>();
			
			final Cursor c = contentResolver.query(LauncherSettings.Favorites.CONTENT_URI, null, "scene=?", new String[]
			{ String.valueOf(ResourcesUtil.getInstance()._CurrentSceneBean.getId()) }, null);

			final ItemInfo occupied[][][] = new ItemInfo[Launcher.SCREEN_COUNT][Launcher.NUMBER_CELLS_X][Launcher.NUMBER_CELLS_Y];

			try
			{
				final int idIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites._ID);
				final int intentIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites.INTENT);
				final int titleIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites.TITLE);
				final int iconTypeIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites.ICON_TYPE);
				final int iconIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites.ICON);
				c.getColumnIndexOrThrow(LauncherSettings.Favorites.ICON_PACKAGE);
				c.getColumnIndexOrThrow(LauncherSettings.Favorites.ICON_RESOURCE);
				final int containerIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites.CONTAINER);
				final int itemTypeIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites.ITEM_TYPE);
				final int appWidgetIdIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites.APPWIDGET_ID);
				final int screenIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites.SCREEN);
				final int cellXIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites.CELLX);
				final int cellYIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites.CELLY);
				final int spanXIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites.SPANX);
				final int spanYIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites.SPANY);
				final int uriIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites.URI);
				final int displayModeIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites.DISPLAY_MODE);

				ShortcutInfo info;
				String intentDescription;
				// LauncherAppWidgetInfo appWidgetInfo;
				int container;
				long id;
				Intent intent;

				while (!mStopped && c.moveToNext())
				{
					try
					{
						int itemType = c.getInt(itemTypeIndex);

						switch (itemType)
						{
							case LauncherSettings.Favorites.ITEM_TYPE_SHORTCUT:
								intentDescription = c.getString(intentIndex);
								try
								{
									intent = Intent.parseUri(intentDescription, 0);
								}
								catch (URISyntaxException e)
								{
									continue;
								}

								info = getShortcutInfo(c, context, iconIndex, titleIndex);

								if (info != null)
								{
									info.intent = intent;
									info.id = c.getLong(idIndex);
									container = c.getInt(containerIndex);
									info.container = container;
									info.screen = c.getInt(screenIndex);
									info.cellX = c.getInt(cellXIndex);
									info.cellY = c.getInt(cellYIndex);

									setDestopIcon(info);
									// check & update map of what's occupied
									if (!checkItemPlacement(occupied, info))
									{
										break;
									}

									switch (container)
									{
										case LauncherSettings.Favorites.CONTAINER_DESKTOP:
											mItems.add(info);
											break;
										default:
											// Item is in a user folder
											UserFolderInfo folderInfo = findOrMakeUserFolder(mFolders, container);
											folderInfo.add(info);
											break;
									}
								}
								else
								{
									// Failed to load the shortcut, probably
									// because the
									// activity manager couldn't resolve it
									// (maybe the app
									// was uninstalled), or the db row was
									// somehow screwed up.
									// Delete it.
									id = c.getLong(idIndex);
									Log.e(TAG, "Error loading shortcut " + id + ", removing it");
									contentResolver.delete(LauncherSettings.Favorites.getContentUri(id, false), null, null);
								}
								break;

							case LauncherSettings.Favorites.ITEM_TYPE_USER_FOLDER:
								id = c.getLong(idIndex);
								UserFolderInfo folderInfo = findOrMakeUserFolder(mFolders, id);

								folderInfo.setTitle(c.getString(titleIndex));

								folderInfo.id = id;
								container = c.getInt(containerIndex);
								folderInfo.container = container;
								folderInfo.screen = c.getInt(screenIndex);
								folderInfo.cellX = c.getInt(cellXIndex);
								folderInfo.cellY = c.getInt(cellYIndex);

								// check & update map of what's occupied
								if (!checkItemPlacement(occupied, folderInfo))
								{
									break;
								}

								switch (container)
								{
									case LauncherSettings.Favorites.CONTAINER_DESKTOP:
										mItems.add(folderInfo);
										break;
									case LauncherSettings.Favorites.CONTAINER_DRAWER:
										mAdditionalDrawerItems.add(folderInfo);
										break;
								}

								mFolders.put(folderInfo.id, folderInfo);
								break;

							case LauncherSettings.Favorites.ITEM_TYPE_LIVE_FOLDER:
								id = c.getLong(idIndex);
								Uri uri = Uri.parse(c.getString(uriIndex));

								// Make sure the live folder exists
								final ProviderInfo providerInfo = context.getPackageManager().resolveContentProvider(
										uri.getAuthority(), 0);

								if (providerInfo == null && !isSafeMode)
								{
									itemsToRemove.add(id);
								}
								else
								{
									LiveFolderInfo liveFolderInfo = findOrMakeLiveFolder(mFolders, id);

									intentDescription = c.getString(intentIndex);
									intent = null;
									if (intentDescription != null)
									{
										try
										{
											intent = Intent.parseUri(intentDescription, 0);
										}
										catch (URISyntaxException e)
										{
											// Ignore, a live folder might not
											// have a base intent
										}
									}

									liveFolderInfo.setTitle(c.getString(titleIndex));
									liveFolderInfo.id = id;
									liveFolderInfo.uri = uri;
									container = c.getInt(containerIndex);
									liveFolderInfo.container = container;
									liveFolderInfo.screen = c.getInt(screenIndex);
									liveFolderInfo.cellX = c.getInt(cellXIndex);
									liveFolderInfo.cellY = c.getInt(cellYIndex);
									liveFolderInfo.baseIntent = intent;
									liveFolderInfo.displayMode = c.getInt(displayModeIndex);

									// check & update map of what's occupied
									if (!checkItemPlacement(occupied, liveFolderInfo))
									{
										break;
									}

									loadLiveFolderIcon(context, c, iconTypeIndex, iconIndex, liveFolderInfo);

									switch (container)
									{
										case LauncherSettings.Favorites.CONTAINER_DESKTOP:
											mItems.add(liveFolderInfo);
											break;
										case LauncherSettings.Favorites.CONTAINER_DRAWER:
											mAdditionalDrawerItems.add(liveFolderInfo);
											break;
									}
									mFolders.put(liveFolderInfo.id, liveFolderInfo);
								}
								break;

							case LauncherSettings.Favorites.ITEM_TYPE_APPWIDGET:
								// Read all Launcher-specific widget details
								int appWidgetId = c.getInt(appWidgetIdIndex);
								id = c.getLong(idIndex);

								final AppWidgetProviderInfo provider = widgets.getAppWidgetInfo(appWidgetId);

								if (!isSafeMode
										&& (provider == null || provider.provider == null || provider.provider.getPackageName() == null))
								{
									Log.e(TAG, "Deleting widget that isn't installed anymore: id=" + id + " appWidgetId="
											+ appWidgetId);
									itemsToRemove.add(id);
								}

						}
					}
					catch (Exception e)
					{
						Log.w(TAG, "Desktop items loading interrupted:", e);
					}
				}
			}
			finally
			{
				c.close();
			}

			if (mAdditionalDrawerItems.size() > 0)
			{
				Callbacks cbs = mCallbacks.get();
				if (cbs != null)
				{
					cbs.bindAppsAdded(mAdditionalDrawerItems);
				}
			}

			if (itemsToRemove.size() > 0)
			{
				ContentProviderClient client = contentResolver
						.acquireContentProviderClient(LauncherSettings.Favorites.CONTENT_URI);
				// Remove dead items
				for (long id : itemsToRemove)
				{
					// Don't notify content observers
					try
					{
						client.delete(LauncherSettings.Favorites.getContentUri(id, false), null, null);
					}
					catch (RemoteException e)
					{
						Log.w(TAG, "Could not remove id = " + id);
					}
				}
			}
			loadWidget();
		}

		/**
		 * 设置桌面的Icon
		 * @param info
		 */
		private void setDestopIcon(ShortcutInfo info)
		{
			try
			{
				String path = IconConfig.config(((ShortcutInfo) info).intent);
				if (path == null)
				{
					Bitmap icon = info.getIcon(Launcher.luanchr.getIconCache());
					icon.setDensity(Bitmap.DENSITY_NONE);
				}
				else
				{
					InputStream picIs = Launcher.luanchr.getIconInputStream(path);
					BitmapDrawable picDrawable = new BitmapDrawable(picIs);
					info.setIcon(picDrawable.getBitmap());
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		private void loadWidget()
		{
			WidgetDataBaseAdapter adapter = WidgetDataBaseAdapter.getInstance();
			adapter.open();
			SceneBean sceneBean = Launcher.luanchr.getCurrentScene();

			mAppWidgets = adapter.select(sceneBean.getId());
			adapter.close();
			// System.out.println("mAppWidgets.size()->" + mAppWidgets.size());

		}

		/**
		 * Read everything out of our database.
		 */
		private void bindWorkspace()
		{

			// Don't use these two variables in any of the callback runnables.
			// Otherwise we hold a reference to them.
			final Callbacks oldCallbacks = mCallbacks.get();
			if (oldCallbacks == null)
			{
				// This launcher has exited and nobody bothered to tell us. Just
				// bail.
				Log.w(TAG, "LoaderTask running with no launcher");
				return;
			}

			int N;
			// Tell the workspace that we're about to start firing items at it
			mHandler.post(new Runnable()
			{
				public void run()
				{
					Callbacks callbacks = tryGetCallbacks(oldCallbacks);
					if (callbacks != null)
					{
						callbacks.startBinding();
					}
				}
			});
			// Add the items to the workspace.
			N = mItems.size();
			for (int i = 0; i < N; i += ITEMS_CHUNK)
			{
				final int start = i;
				final int chunkSize = (i + ITEMS_CHUNK <= N) ? ITEMS_CHUNK : (N - i);
				mHandler.post(new Runnable()
				{
					public void run()
					{
						Callbacks callbacks = tryGetCallbacks(oldCallbacks);
						if (callbacks != null)
						{
							callbacks.bindItems(mItems, start, start + chunkSize);
						}
					}
				});
			}
			mHandler.post(new Runnable()
			{
				public void run()
				{
					Callbacks callbacks = tryGetCallbacks(oldCallbacks);
					if (callbacks != null)
					{
						callbacks.bindFolders(mFolders);
					}
				}
			});
			// Wait until the queue goes empty.
			mHandler.post(new Runnable()
			{
				public void run()
				{
				}
			});
			final int currentScreen = oldCallbacks.getCurrentWorkspaceScreen();
			N = mAppWidgets.size();
			// once for the current screen
			for (int i = 0; i < N; i++)
			{
				LemonadeBean widget = mAppWidgets.get(i);
				if (widget instanceof WidgetBean)
				{
					final WidgetBean wb = (WidgetBean) widget;
					// if (wb.getScreen() == currentScreen)
					if (true)
					{
						mHandler.post(new Runnable()
						{
							public void run()
							{
								Callbacks callbacks = tryGetCallbacks(oldCallbacks);
								if (callbacks != null)
								{
									callbacks.bindAppWidget(wb);
								}
							}
						});
					}
				}
			}

			mHandler.post(new Runnable()
			{
				public void run()
				{
					Callbacks callbacks = tryGetCallbacks(oldCallbacks);
					if (callbacks != null)
					{
						callbacks.finishBindingItems();
					}
				}
			});
			// If we're profiling, this is the last thing in the queue.
			mHandler.post(new Runnable()
			{
				public void run()
				{
				}
			});
		}

		private void loadAndBindAllApps()
		{
			if (!mAllAppsLoaded)
			{
				loadAllAppsByBatch();
				if (mStopped)
				{
					return;
				}
				mAllAppsLoaded = true;
			}
			else
			{
				onlyBindAllApps();
			}
		}

		@SuppressWarnings("unchecked")
		private void onlyBindAllApps()
		{
			final Callbacks oldCallbacks = mCallbacks.get();
			if (oldCallbacks == null)
			{
				// This launcher has exited and nobody bothered to tell us. Just
				// bail.
				Log.w(TAG, "LoaderTask running with no launcher (onlyBindAllApps)");
				return;
			}

			// shallow copy
			final ArrayList<ShortcutInfo> list = (ArrayList<ShortcutInfo>) mAllAppsList.data.clone();
			final ArrayList<IconItemInfo> otherItems = (ArrayList<IconItemInfo>) mAdditionalDrawerItems.clone();
			mHandler.post(new Runnable()
			{
				public void run()
				{
					final Callbacks callbacks = tryGetCallbacks(oldCallbacks);
					if (callbacks != null)
					{
						callbacks.bindAllApplications(list, otherItems);
					}
				}
			});

		}

		private void loadAllAppsByBatch()
		{
			// Don't use these two variables in any of the callback runnables.
			// Otherwise we hold a reference to them.
			final Callbacks oldCallbacks = mCallbacks.get();
			if (oldCallbacks == null)
			{
				// This launcher has exited and nobody bothered to tell us. Just
				// bail.
				Log.w(TAG, "LoaderTask running with no launcher (loadAllAppsByBatch)");
				return;
			}
			long startTime = System.currentTimeMillis();

			List<ShortcutInfo> apps = new AppDB(mContext, mIconCache).getApps();

			long endTime = System.currentTimeMillis();

			Log.v(TAG, "found apps: " + apps.size());
			Log.v(TAG, "took: " + (endTime - startTime) + "ms");
			if (apps.size() == 0)
				return; // There are no apps?!?

			mAllAppsList.clear();
			for (ShortcutInfo info : apps)
				mAllAppsList.add(info);

			final Callbacks callbacks = tryGetCallbacks(oldCallbacks);
			final ArrayList<ShortcutInfo> added = mAllAppsList.added;
			mAllAppsList.added = new ArrayList<ShortcutInfo>();

			final ArrayList<IconItemInfo> others = new ArrayList<IconItemInfo>(mAdditionalDrawerItems);

			mHandler.post(new Runnable()
			{
				public void run()
				{
					if (callbacks != null)
					{
						callbacks.bindAllApplications(added, others);
					}
					else
					{
						Log.i(TAG, "not binding apps: no Launcher activity");
					}
				}
			});
		}
	}

	/**
	 * This is called from the code that adds shortcuts from the intent
	 * receiver. This doesn't have a Cursor, but
	 */
	public ShortcutInfo getShortcutInfo(Intent intent, Context context)
	{
		return getShortcutInfo(intent, context, null, -1, -1);
	}

	/**
	 * Make an ShortcutInfo object for a shortcut that is an application.
	 * 
	 * If c is not null, then it will be used to fill in missing data like the
	 * title and icon.
	 */
	public ShortcutInfo getShortcutInfo(Intent intent, Context context, Cursor c, int iconIndex, int titleIndex)
	{
		final ShortcutInfo info = new ShortcutInfo();

		ComponentName componentName = intent.getComponent();
		if (componentName == null)
		{
			return null;
		}

		// TODO: See if the PackageManager knows about this case. If it doesn't
		// then return null & delete this.

		// the resource -- This may implicitly give us back the fallback icon,
		// but don't worry about that. All we're doing with usingFallbackIcon is
		// to avoid saving lots of copies of that in the database, and most apps
		// have icons anyway.

		info.setIcon(getIconFromCursor(c, iconIndex));

		// from the db
		if (c != null)
		{
			info.setTitle(c.getString(titleIndex));
		}
		info.itemType = LauncherSettings.Favorites.ITEM_TYPE_SHORTCUT;
		return info;
	}

	/**
	 * Make an ShortcutInfo object for a shortcut that isn't an application.
	 */
	private ShortcutInfo getShortcutInfo(Cursor c, Context context, int iconIndex, int titleIndex)
	{

		final ShortcutInfo info = new ShortcutInfo();
		info.itemType = LauncherSettings.Favorites.ITEM_TYPE_SHORTCUT;

		if (c != null)
		{
			if (titleIndex >= 0)
				info.setTitle(c.getString(titleIndex));
			if (iconIndex >= 0)
				info.setIcon(getIconFromCursor(c, iconIndex));
		}

		return info;
	}

	static Bitmap getIconFromCursor(Cursor c, int iconIndex)
	{
		if (c != null && iconIndex >= 0)
		{
			byte[] data = c.getBlob(iconIndex);
			try
			{
				return BitmapFactory.decodeByteArray(data, 0, data.length);
			}
			catch (Exception e)
			{
				return null;
			}
		}
		return null;
	}

	ShortcutInfo addShortcut(Context context, Intent data, CellLayout.CellInfo cellInfo, boolean notify)
	{

		final ShortcutInfo info = infoFromShortcutIntent(context, data);
		addItemToDatabase(context, info, LauncherSettings.Favorites.CONTAINER_DESKTOP, cellInfo.screen, cellInfo.cellX,
				cellInfo.cellY, notify);

		return info;
	}

	private ShortcutInfo infoFromShortcutIntent(Context context, Intent data)
	{
		Intent intent = data.getParcelableExtra(Intent.EXTRA_SHORTCUT_INTENT);
		String name = data.getStringExtra(Intent.EXTRA_SHORTCUT_NAME);
		Parcelable bitmap = data.getParcelableExtra(Intent.EXTRA_SHORTCUT_ICON);

		Bitmap icon = null;
		ShortcutIconResource iconResource = null;

		if (bitmap != null && bitmap instanceof Bitmap)
		{
			icon = Utilities.createIconBitmap(new FastBitmapDrawable((Bitmap) bitmap), context);
		}
		else
		{
			Parcelable extra = data.getParcelableExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE);
			if (extra != null && extra instanceof ShortcutIconResource)
			{
				try
				{
					iconResource = (ShortcutIconResource) extra;
					final PackageManager packageManager = context.getPackageManager();
					Resources resources = packageManager.getResourcesForApplication(iconResource.packageName);
					final int id = resources.getIdentifier(iconResource.resourceName, null, null);
					icon = Utilities.createIconBitmap(resources.getDrawable(id), context);
				}
				catch (Exception e)
				{
					Log.w(TAG, "Could not load shortcut icon: " + extra);
				}
			}
		}

		final ShortcutInfo info = new ShortcutInfo();

		info.setIcon(icon);

		info.setTitle(name);
		info.intent = intent;

		return info;
	}

	private static void loadLiveFolderIcon(Context context, Cursor c, int iconTypeIndex, int iconBitmapIndex,
			LiveFolderInfo liveFolderInfo)
	{

		int iconType = c.getInt(iconTypeIndex);
		if (iconType == LauncherSettings.Favorites.ICON_TYPE_BITMAP)
			liveFolderInfo.setIcon(getIconFromCursor(c, iconBitmapIndex));
		else
			liveFolderInfo.setIcon(null);
	}

	/**
	 * Return an existing UserFolderInfo object if we have encountered this ID
	 * previously, or make a new one.
	 */
	private static UserFolderInfo findOrMakeUserFolder(HashMap<Long, FolderInfo> folders, long id)
	{
		// See if a placeholder was created for us already
		FolderInfo folderInfo = folders.get(id);
		if (folderInfo == null || !(folderInfo instanceof UserFolderInfo))
		{
			// No placeholder -- create a new instance
			folderInfo = new UserFolderInfo();
			folders.put(id, folderInfo);
		}
		return (UserFolderInfo) folderInfo;
	}

	/**
	 * Return an existing UserFolderInfo object if we have encountered this ID
	 * previously, or make a new one.
	 */
	private static LiveFolderInfo findOrMakeLiveFolder(HashMap<Long, FolderInfo> folders, long id)
	{
		// See if a placeholder was created for us already
		FolderInfo folderInfo = folders.get(id);
		if (folderInfo == null || !(folderInfo instanceof LiveFolderInfo))
		{
			// No placeholder -- create a new instance
			folderInfo = new LiveFolderInfo();
			folders.put(id, folderInfo);
		}
		return (LiveFolderInfo) folderInfo;
	}

	/**
	 * Resize an item in the DB to a new <container, screen, cellX, cellY>
	 */
	static void resizeItemInDatabase(Context context, ItemInfo item, long container, int screen, int cellX, int cellY,
			int spanX, int spanY)
	{
		item.container = container;
		item.screen = screen;
		item.cellX = cellX;
		item.cellY = cellY;
		item.spanX = spanX;
		item.spanY = spanY;

		final ContentValues values = new ContentValues();
		final ContentResolver cr = context.getContentResolver();

		values.put(LauncherSettings.Favorites.CONTAINER, item.container);
		values.put(LauncherSettings.Favorites.CELLX, item.cellX);
		values.put(LauncherSettings.Favorites.CELLY, item.cellY);
		values.put(LauncherSettings.Favorites.SPANX, item.spanX);
		values.put(LauncherSettings.Favorites.SPANY, item.spanY);
		values.put(LauncherSettings.Favorites.SCREEN, item.screen);

		cr.update(LauncherSettings.Favorites.getContentUri(item.id, false), values, null, null);
	}

}
