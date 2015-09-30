package org.accenture.product.lemonade.appdb;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.accenture.product.lemonade.IconCache;
import org.accenture.product.lemonade.ItemInfo;
import org.accenture.product.lemonade.ShortcutInfo;
import org.accenture.product.lemonade.Utilities;
import org.accenture.product.lemonade.settings.LauncherSettings;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

public class AppDB extends BroadcastReceiver {
	private static final long INVALID_ID = -1;
	private static final String PACKAGE_SEPERATOR = "/";
	public static final String INTENT_DB_CHANGED = "org.accenture.product.lemonade.app_db_changed";
	public static final String EXTRA_ADDED = "added";
	public static final String EXTRA_DELETED_PACKAGE = "deleted_package";
	public static final String EXTRA_DELETED_COMPONENT_NAMES = "deleted_cnames";
	public static final String EXTRA_UPDATED = "updated";

	private Context mContext;
	private final IconCache mIconCache;

	private final HashMap<String, LaunchInfo> mLaunchInfos = new HashMap<String, LaunchInfo>();

	public AppDB(Context context, IconCache iconCache) {
		mContext = context;
		mIconCache = iconCache;
	}

	@Deprecated
	public AppDB() {
		mIconCache = null;
		// Only for Broadcast reciever!
	}


	public long getId(ComponentName name) {
		ContentResolver cr = mContext.getContentResolver();
		Cursor c = cr.query(AppInfos.CONTENT_URI,
				new String[] { AppInfos.ID },
				AppInfos.COMPONENT_NAME + "=?",
				new String[] { name.flattenToString() }, null);
		try {
			c.moveToFirst();
			if (!c.isAfterLast()) {
				return c.getLong(0);
			}
		}
		finally {
			c.close();
		}
		return INVALID_ID;
	}

	public boolean incrementLaunchCounter(Intent intent) {
		String action = intent.getAction();
		if (Intent.ACTION_MAIN.equals(action) &&
				intent.hasCategory(Intent.CATEGORY_LAUNCHER)) {
			incrementLaunchCounter(intent.getComponent());
			return true;
		}
		return false;
	}

	private void incrementLaunchCounter(ComponentName name) {
		String cnStr = name.flattenToString();
		if (mLaunchInfos.containsKey(cnStr)) {
			// Update local
			mLaunchInfos.get(cnStr).launched();
		}

		ContentResolver cr = mContext.getContentResolver();
		Cursor c = cr.query( // Update within the DB
				AppInfos.APP_LAUNCHED_URI.buildUpon()
					.appendEncodedPath(cnStr).build(),
				null, null, null, null);
		if (c != null)
			c.close();
	}

	public int getLaunchCounter(ShortcutInfo info) {
		if (info != null && info.getIntent() != null) {
			String action = info.getIntent().getAction();
			if (Intent.ACTION_MAIN.equals(action) &&
					info.getIntent().hasCategory(Intent.CATEGORY_LAUNCHER))
				return getLaunchCounter(info.getIntent().getComponent());
		}
		return -1;
	}

	public int getLaunchCounter(ComponentName name) {
		LaunchInfo info = getLaunchInfo(name);
		if (info != null)
			return info.getCount();
		return -1;
	}

	private LaunchInfo getLaunchInfo(ComponentName name) {
		String cnStr = name.flattenToString();
		if (!mLaunchInfos.containsKey(cnStr)) {
			ContentResolver cr = mContext.getContentResolver();
			Cursor c = cr.query(AppInfos.CONTENT_URI,
					new String[] {
						AppInfos.LAUNCH_COUNT,
						AppInfos.LAST_LAUNCHED },
					AppInfos.COMPONENT_NAME + "=?",
					new String[] { cnStr }, null);
			try {
				c.moveToFirst();

				if (!c.isAfterLast()) {
					LaunchInfo li = new LaunchInfo(
							(int)c.getLong(c.getColumnIndex(AppInfos.LAUNCH_COUNT)),
							c.getLong(c.getColumnIndex(AppInfos.LAST_LAUNCHED)));

					mLaunchInfos.put(cnStr, li);
					return li;
				}
				else
					return null;
			}
			finally {
				c.close();
			}

		}
		return mLaunchInfos.get(cnStr);
	}

	//kya
	//ok, the receiver
	@Override
	public void onReceive(Context context, Intent intent) {
		if (mContext == null)
			mContext = context;
		// This code will run outside of the launcher process!!!!!
		final String action = intent.getAction();
        if (Intent.ACTION_PACKAGE_CHANGED.equals(action)
                || Intent.ACTION_PACKAGE_REMOVED.equals(action)
                || Intent.ACTION_PACKAGE_ADDED.equals(action)) {
            final String packageName = intent.getData().getSchemeSpecificPart();
            final boolean replacing = intent.getBooleanExtra(Intent.EXTRA_REPLACING, false);

            if (packageName == null || packageName.length() == 0) {
                // they sent us a bad intent
                return;
            }

            if (Intent.ACTION_PACKAGE_CHANGED.equals(action)) {
                PackageChanged(packageName);
            } else if (Intent.ACTION_PACKAGE_REMOVED.equals(action)) {
                if (!replacing) {
                    PackageRemoved(packageName);
                }
                // else, we are replacing the package, so a PACKAGE_ADDED will be sent
                // later, we will update the package at this time
            } else if (Intent.ACTION_PACKAGE_ADDED.equals(action)) {
                if (!replacing) {
                	PackageAdded(packageName);
                } else {
                    PackageChanged(packageName);
                }
            }
        }
	}
	//kya
	//things got changed
	private void PackageAdded(String aPackage) {
		final PackageManager packageManager = mContext.getPackageManager();
        AddResolveInfos(packageManager, findActivitiesForPackage(packageManager, aPackage));
	}

	private void PackageChanged(String aPackage) {
		final PackageManager packageManager = mContext.getPackageManager();

		List<ExtResolveInfo> addedApps = new LinkedList<ExtResolveInfo>();
		List<DBInfo> removedApps = new LinkedList<DBInfo>();
		HashMap<ExtResolveInfo, DBInfo> updatedApps = new HashMap<ExtResolveInfo, DBInfo>();

        List<ExtResolveInfo> pmInfos = toExtInfos(findActivitiesForPackage(packageManager, aPackage));
        ContentResolver cr = mContext.getContentResolver();
        Cursor c = queryAppsFromPackage(
				new String[] {AppInfos.ID, AppInfos.COMPONENT_NAME, AppInfos.TITLE_CHANGED, AppInfos.ICON_CHANGED},
				aPackage);
        List<DBInfo> dbInfos = toDBInfos(c);
        c.close();

        // find removed / updated apps
        for(DBInfo dbi : dbInfos) {
        	boolean found = false;
        	for (ExtResolveInfo pmi : pmInfos) {
	        	if (pmi.getComponentName().equals(dbi.getComponentName())) {
	        		found = true;
	        		updatedApps.put(pmi, dbi); // update dbi from pmi later
	        		break;
	        	}
        	}
        	if (!found) {
        		removedApps.add(dbi); // app is no longer installed!
        	}
        }
        for (ExtResolveInfo pmi : pmInfos) {
        	if (updatedApps.containsKey(pmi))
        		continue; // alread in updateable apps
        	addedApps.add(pmi); // not updated, not removed so it must be added ;-)
        }
        Intent modelIntent = new Intent(INTENT_DB_CHANGED);
        boolean sendIntent = removedApps.size() > 0 || updatedApps.size() > 0;

        // Ok we got all needed infos so lets start the party:
        AddResolveInfos(packageManager, addedApps); // adding is easy!

    	// removing is a little harder:
    	DestroyItems(removedApps);
    	if (sendIntent)
    		modelIntent.putExtra(EXTRA_DELETED_COMPONENT_NAMES, getPackageNames(removedApps));

    	// ok then updating is left:
    	long[] updatedIds = new long[updatedApps.size()];
    	int i = 0;
    	for (ExtResolveInfo pmInfo : updatedApps.keySet()) {
    		DBInfo dbinfo = updatedApps.get(pmInfo);

    		boolean iconChanged = dbinfo.isIconChanged();
    		boolean titleChanged = dbinfo.isTitleChanged();
    		if ( !titleChanged || !iconChanged )
    		{
        		ResolveInfo rInfo = pmInfo.getResolveInfo();
                Bitmap icon = Utilities.createIconBitmap(
                        rInfo.loadIcon(packageManager), mContext);

                ContentValues values = new ContentValues();
                if ( !titleChanged )
                {
                    values.put(AppInfos.TITLE, rInfo.loadLabel(packageManager).toString());
                }

                if ( !iconChanged )
                {
                    ItemInfo.writeBitmap(values, icon);
                }

        		cr.update(AppInfos.CONTENT_URI, values, AppInfos.ID + " = ?",
        				new String[] { String.valueOf(dbinfo.getId()) } );
        		updatedIds[i++] = dbinfo.getId();
    		}
    	}
    	if (i > 0)
    		modelIntent.putExtra(EXTRA_UPDATED, updatedIds);

        // Notify Model:
        mContext.sendBroadcast(modelIntent);
	}

	private List<ExtResolveInfo> toExtInfos(List<ResolveInfo> list) {
		List<ExtResolveInfo> result = new ArrayList<ExtResolveInfo>(list.size());
		for(ResolveInfo info : list) {
			result.add(new ExtResolveInfo(info));
		}
		return result;
	}

	private List<DBInfo> toDBInfos(Cursor c) {
		List<DBInfo> result = new LinkedList<DBInfo>();
		if (c.moveToFirst()) {
			while(!c.isAfterLast()) {
				result.add(new DBInfo(c));
				c.moveToNext();
			}
		}
		return result;
	}

	private void PackageRemoved(String aPackage) {
	    List<DBInfo> infos = new LinkedList<DBInfo>();
	    Cursor c = queryAppsFromPackage(new String[] { AppInfos.ID, AppInfos.COMPONENT_NAME }, aPackage);
	    try {
	    	c.moveToFirst();
			c.getColumnIndex(AppInfos.ID);
            c.getColumnIndex(AppInfos.COMPONENT_NAME);
            c.getColumnIndex(AppInfos.TITLE_CHANGED);
            c.getColumnIndex(AppInfos.ICON_CHANGED);
			while(!c.isAfterLast()) {
				DBInfo info = new DBInfo(c);
				infos.add(info);
				c.moveToNext();
			}
		} finally {
			c.close();
		}

		DestroyItems( infos);
		// notify the LauncherModel too!
		Intent deleteIntent = new Intent(INTENT_DB_CHANGED);
		// remove all items from the package!
		deleteIntent.putExtra(EXTRA_DELETED_PACKAGE, aPackage);
		mContext.sendBroadcast(deleteIntent);
	}

	private void DestroyItems(List<DBInfo> infos) {
		if (infos.size() > 0) {
			String deleteFlt = getAppIdFilter(getIds(infos));
			ContentResolver cr = mContext.getContentResolver();

			cr.delete(AppInfos.CONTENT_URI, deleteFlt, null);
			RemoveShortcutsFromWorkspace(infos);
		}
	}

	public static boolean arrayContains(String[] array, String value) {
		for (String itm : array) {
			if (itm.equals(value))
				return true;
		}
		return false;
	}

	private static boolean InfosContains(List<DBInfo> infos, String value) {
		for (DBInfo itm : infos) {
			if (value.equals(itm.getComponentName()))
				return true;
		}
		return false;
	}

	private static long[] getIds(List<DBInfo> infos) {
		long[] result = new long[infos.size()];
		for (int i = 0; i < infos.size(); i++) {
			result[i] = infos.get(i).getId();
		}
		return result;
	}

	private static String[] getPackageNames(List<DBInfo> infos) {
		String[] result = new String[infos.size()];
		for (int i = 0; i < infos.size(); i++) {
			result[i] = infos.get(i).getComponentName();
		}
		return result;
	}

	private void RemoveShortcutsFromWorkspace(List<DBInfo> infos) {
		final ContentResolver cr = mContext.getContentResolver();

        Cursor c = cr.query(LauncherSettings.Favorites.CONTENT_URI,
            new String[] { LauncherSettings.Favorites._ID,
        		LauncherSettings.Favorites.INTENT },
        		LauncherSettings.Favorites.INTENT + " is not null", null, null);
        long[] ids = null;
        try {
	        if (c != null && c.moveToFirst()) {
	        	// prepare the dirty work!
	        	ids = new long[c.getCount()];
	        	int IDColumnIndex = c.getColumnIndex(LauncherSettings.Favorites._ID);
	        	int IntentColumnIndex = c.getColumnIndex(LauncherSettings.Favorites.INTENT);
	        	int idx = 0;
	        	while (!c.isAfterLast())
	        	{
	        		String intentStr = c.getString(IntentColumnIndex);
	        		try {
		        		Intent intent = Intent.parseUri(intentStr, 0);
		        		if (intent != null) {

		        			ComponentName cname = intent.getComponent();
		        			if (cname != null ) {
			        			String cnameStr = cname.flattenToString();
			        			if (InfosContains(infos, cnameStr)) {
			        				c.getLong(IDColumnIndex);
			        				ids[idx++] = c.getLong(IDColumnIndex);
			        			}else
			        				ids[idx++] = INVALID_ID;
		        			} else {
		        				ids[idx++] = INVALID_ID;
		        			}
		        		} else
		        			ids[idx++] = INVALID_ID;
	        		}
	        		catch(URISyntaxException expt) {
	        			ids[idx++] = INVALID_ID;
	        		}

	        		c.moveToNext();
	        	}
	        }
        } finally {
        	c.close();
        }
        if (ids != null) {
        	for (long id : ids) {
        		if (id != INVALID_ID)
        			cr.delete(LauncherSettings.Favorites.getContentUri(id, false), null, null);
        	}
        }
	}

	private Cursor queryAppsFromPackage(String[] columns, String aPackage) {
		aPackage = aPackage + PACKAGE_SEPERATOR;
		String pkgflt = "substr("+AppInfos.COMPONENT_NAME + ",1,"+ aPackage.length() +") = ?";
		final ContentResolver cr = mContext.getContentResolver();
		return cr.query(AppInfos.CONTENT_URI,
				columns, pkgflt, new String[] { aPackage }, null);
	}

	private static Bitmap getIconFromCursor(Cursor c, int iconIndex) {
        byte[] data = c.getBlob(iconIndex);
        try {
            return BitmapFactory.decodeByteArray(data, 0, data.length);
        } catch (Exception e) {
            return null;
        }
    }

	public List<ShortcutInfo> getApps() {
		return getApps(null);
	}

	private String getAppIdFilter(long[] appIds) {
		if (appIds == null)
			return null;
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < appIds.length; i++) {
			if (i > 0)
				sb.append(" or ");
			sb.append(AppInfos.ID);
			sb.append("=");
			sb.append(appIds[i]);
		}
		return sb.toString();
	}

	public List<ShortcutInfo> getApps(long[] appIds) {
		ArrayList<ShortcutInfo> result = new ArrayList<ShortcutInfo>();
		ContentResolver cr = mContext.getContentResolver();

		Cursor c = cr.query(AppInfos.CONTENT_URI, new String[] {
		        AppInfos.ID,
		        AppInfos.COMPONENT_NAME,
                AppInfos.ICON,
                AppInfos.ICON_CHANGED,
                AppInfos.TITLE,
                AppInfos.TITLE_CHANGED,
				AppInfos.LAST_LAUNCHED,
				AppInfos.LAUNCH_COUNT
		}, getAppIdFilter(appIds), null, null);
		try {
			c.moveToFirst();
            final int idIdx = c.getColumnIndex(AppInfos.ID);
            final int iconIdx = c.getColumnIndex(AppInfos.ICON);
            final int iconChangedIdx = c.getColumnIndex(AppInfos.ICON_CHANGED);
			final int cnIdx = c.getColumnIndex(AppInfos.COMPONENT_NAME);
            final int titleIdx = c.getColumnIndex(AppInfos.TITLE);
            final int titleChangedIdx = c.getColumnIndex(AppInfos.TITLE_CHANGED);
			final int launchcntIdx = c.getColumnIndex(AppInfos.LAUNCH_COUNT);
			final int lastlaunchIdx = c.getColumnIndex(AppInfos.LAST_LAUNCHED);

			while(!c.isAfterLast()) {
				Bitmap icon = getIconFromCursor(c, iconIdx);
				String cnStr = c.getString(cnIdx);
				String title = c.getString(titleIdx);

				if (mLaunchInfos.containsKey(cnStr))
					mLaunchInfos.remove(cnStr);

				mLaunchInfos.put(cnStr,
						new LaunchInfo(
								(int)c.getLong(launchcntIdx),
								c.getLong(lastlaunchIdx)));

				ComponentName cname = ComponentName.unflattenFromString(cnStr);
				if (mIconCache != null)
					mIconCache.addToCache(cname, title, icon);
				ShortcutInfo info = new ShortcutInfo(c.getLong(idIdx), cname);
                info.setTitleInAppsDb(c.getInt( titleChangedIdx ) == 1);
                info.setIconInAppsDb( c.getInt( iconChangedIdx ) == 1 );
				result.add(info);
				c.moveToNext();
			}
		}
		finally {
			c.close();
		}
		return result;
	}

	public void updateLocale(String newLocale) {
		ContentResolver resolver = mContext.getContentResolver();
		PackageManager pm = mContext.getPackageManager();
		// Query all infos with a different locale:
		final Cursor c = resolver.query(AppInfos.CONTENT_URI,
				new String[] { AppInfos.ID, AppInfos.COMPONENT_NAME }, AppInfos.LOCALE + " <> ?",
				new String[] { newLocale }, null);
		try {
			if (c.moveToFirst()) {
				long[] updatedIds = new long[c.getCount()];
				int idx = 0;


				int idCol = c.getColumnIndex(AppInfos.ID);
				int cnCol = c.getColumnIndex(AppInfos.COMPONENT_NAME);

				while(!c.isAfterLast()) {
					long id = c.getLong(idCol);
					ComponentName cn = ComponentName.unflattenFromString(c.getString(cnCol));
					if (cn != null) {
				        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
				        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
				        mainIntent.setComponent(cn);
				        ResolveInfo ri = pm.resolveActivity(mainIntent, 0);

						ContentValues cv = new ContentValues();
						String title = ri.loadLabel(pm).toString();
						cv.put(AppInfos.TITLE, title);
						cv.put(AppInfos.LOCALE, newLocale);
						resolver.update(AppInfos.getContentUri(id), cv, null, null);
						updatedIds[idx++] = id;
					}
					c.moveToNext();
				}

				Intent updateIntent = new Intent(INTENT_DB_CHANGED);
				updateIntent.putExtra(EXTRA_UPDATED, updatedIds);
				mContext.sendBroadcast(updateIntent);
			}
		} finally {
			c.close();
		}
	}


    private List<ResolveInfo> findActivitiesForPackage(PackageManager packageManager, String packageName) {
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        mainIntent.setPackage(packageName);

        final List<ResolveInfo> apps = packageManager.queryIntentActivities(mainIntent, 0);
        return apps != null ? apps : new ArrayList<ResolveInfo>();
    }
    //kya
    //push appinfo into db
    static ContentValues[] ResolveInfosToContentValues(Context context, List<?> infos) {
    	PackageManager packageManager = context.getPackageManager();
    	String curLocale = Locale.getDefault().toString();
    	ContentValues[] result = new ContentValues[infos.size()];
    	int i = 0;
    	for(Object oinfo : infos) {
    		final ResolveInfo info;
    		if (oinfo instanceof ResolveInfo)
    			info = (ResolveInfo)oinfo;
    		else if (oinfo instanceof ExtResolveInfo)
    			info = ((ExtResolveInfo)oinfo).getResolveInfo();
    		else
    			continue;

        	ComponentName componentName = new ComponentName(
                    info.activityInfo.applicationInfo.packageName,
                    info.activityInfo.name);

        	String title = info.loadLabel(packageManager).toString();

            if (title == null) {
                title = info.activityInfo.name;
            }
            Bitmap icon = Utilities.createIconBitmap(
                    info.activityInfo.loadIcon(packageManager), context);

            ContentValues values = new ContentValues();
            values.put(AppInfos.TITLE, title);
            values.put(AppInfos.LOCALE, curLocale);
            ItemInfo.writeBitmap(values, icon);
            values.put(AppInfos.COMPONENT_NAME, componentName.flattenToString());
			values.put(AppInfos.LAUNCH_COUNT, 0);
            result[i++] = values;
    	}
    	return result;
    }

    private void AddResolveInfos(PackageManager packageManager, List<?> infos) {
    	ContentResolver cr = mContext.getContentResolver();

    	long[] added = new long[infos.size()];

    	int i = 0;
    	for(ContentValues values : ResolveInfosToContentValues(mContext, infos)) {
    		added[i++] = ContentUris.parseId(cr.insert(AppInfos.CONTENT_URI, values));
    	}

		Intent updateIntent = new Intent(INTENT_DB_CHANGED);
		updateIntent.putExtra("added", added);
		mContext.sendBroadcast(updateIntent);
    }

    private class ExtResolveInfo {
    	private final ResolveInfo mResolveInfo;
    	private final String mComponentName;

    	public ExtResolveInfo(ResolveInfo info) {
    		mResolveInfo = info;
        	mComponentName = new ComponentName(
                    info.activityInfo.applicationInfo.packageName,
                    info.activityInfo.name).flattenToString();
    	}

    	public String getComponentName() {
    		return mComponentName;
    	}
    	public ResolveInfo getResolveInfo() {
    		return mResolveInfo;
    	}
    }

    private class DBInfo {
    	private final long mId;
    	private final String mComponentName;
        private final boolean mTitleChanged;
        private final boolean mIconChanged;

    	public DBInfo(Cursor c) {
    		mId = c.getLong(c.getColumnIndex(AppInfos.ID));
            mComponentName = c.getString(c.getColumnIndex(AppInfos.COMPONENT_NAME));
            int tcidx = c.getColumnIndex(AppInfos.TITLE_CHANGED);
            mTitleChanged = (tcidx >= 0) && (c.getInt(tcidx) == 1);
            int icidx = c.getColumnIndex(AppInfos.ICON_CHANGED);
            mIconChanged = (icidx >= 0) && (c.getInt(icidx) == 1);
    	}

    	public long getId(){
    		return mId;
    	}

    	public String getComponentName() {
    		return mComponentName;
    	}

        public boolean isTitleChanged()
        {
            return mTitleChanged;
        }

        public boolean isIconChanged()
        {
            return mIconChanged;
        }
    }

    public static final String APPINFOS = "appinfos";

	public static class AppInfos {
		public static final String ID = "_id";
		public static final String COMPONENT_NAME = "componentname";
		public static final String LAUNCH_COUNT = "launchcount";
		public static final String LAST_LAUNCHED = "lastlaunched";
		public static final String TITLE = "title";
        public static final String TITLE_CHANGED = "titlechanged";
		public static final String ICON = "icon";
        public static final String ICON_CHANGED = "iconchanged";
		public static final String LOCALE = "locale";

        static final Uri CONTENT_URI = Uri.parse("content://" +
                AppDBProvider.AUTHORITY + "/" + APPINFOS);

        static final Uri APP_LAUNCHED_URI = Uri.parse("content://"+
        		AppDBProvider.AUTHORITY + "/launched");

        /**
         * The content:// style URL for a given row, identified by its id.
         *
         * @param id The row id.
         * @param notify True to send a notification is the content changes.
         *
         * @return The unique content URL for the specified row.
         */
        static Uri getContentUri(long id) {
            return Uri.parse("content://" + AppDBProvider.AUTHORITY +
                    "/" + APPINFOS + "/" + id);
        }

	}

    public void updateAppDisplay(long id, String title, Bitmap icon) {
        ContentResolver cr = mContext.getContentResolver();
        final Uri uri = AppInfos.getContentUri(id);
        final ContentValues values = new ContentValues();

        byte[] data = null;
        if ( icon != null )
        {
            data = Utilities.flattenBitmap(icon);
        }
        values.put(AppInfos.ICON, data);
        values.put(AppInfos.ICON_CHANGED, data == null?0:1);

        values.put(AppInfos.TITLE, title);
        values.put(AppInfos.TITLE_CHANGED, title == null?0:1);

        cr.update(uri, values, null, null);
    }

}
