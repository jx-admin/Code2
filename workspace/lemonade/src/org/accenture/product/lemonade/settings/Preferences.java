package org.accenture.product.lemonade.settings;

import java.net.URISyntaxException;
import java.text.Collator;
import java.util.Comparator;

import org.accenture.product.lemonade.IconCache;
import org.accenture.product.lemonade.IconItemInfo;
import org.accenture.product.lemonade.Launcher;
import org.accenture.product.lemonade.R;
import org.accenture.product.lemonade.ShortcutInfo;
import org.accenture.product.lemonade.appdb.AppDB;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;

public class Preferences implements OnSharedPreferenceChangeListener {

	private final static String EmptyString = "";

	private static Preferences _Current = null;

	public static Preferences getInstance() {
		if (_Current == null)
			_Current = new Preferences();
		return _Current;
	}

	private SharedPreferences mPreferences = null;
	private Launcher mLauncher = null;


	public void setLauncher(Launcher launcher) {
		if (launcher == null && mPreferences != null) {
			mPreferences.unregisterOnSharedPreferenceChangeListener(this);
			mPreferences = null;
		} else if (launcher != null) {
			mPreferences = PreferenceManager.getDefaultSharedPreferences(launcher);
			mPreferences.registerOnSharedPreferenceChangeListener(this);
			PreferenceManager.setDefaultValues(launcher, R.xml.settings, false);
		}
		mLauncher = launcher;
	}

	private Intent getIntent(String key) {
		String val = mPreferences.getString(PREF_SWIPE_UP_ACTION, EmptyString);
		if (val.equals(EmptyString)) {
			try {
				return Intent.parseUri(val, 0);
			} catch (URISyntaxException e) {
				return null;
			}
		} else
			return null;
	}

	static final String PREF_HOMESCREEN_ENDLESS_LOOP = "EndlessHomescreenLoop";
	static final String PREF_SWIPE_UP_ACTION = "SwipeUpAction";
	static final String PREF_SWIPE_DOWN_ACTION = "SwipeDownAction";
	static final String PREF_CURRENT_DRAWER_SORT_ORDER = "CurrentDrawerSortOrder";

	public boolean getEndlessScrolling() {
//		return mPreferences.getBoolean(PREF_HOMESCREEN_ENDLESS_LOOP, true);
		return false;
	}

	public Intent getSwipeUpAction() {
		return getIntent(PREF_SWIPE_UP_ACTION);
	}

	public Intent getSwipeDownAction() {
		return getIntent(PREF_SWIPE_DOWN_ACTION);
	}

	private static final int SORT_BY_NAME = 1;
	private static final int SORT_BY_LAUNCH_COUNT = 2;

    private Comparator<IconItemInfo> mCurrentComparator = null;

    private Comparator<IconItemInfo> getAppNameComparator() {
		final IconCache myIconCache = mLauncher.getIconCache();
		final Collator sCollator = Collator.getInstance();
		return new Comparator<IconItemInfo>() {
			@Override
    		public final int compare(IconItemInfo a, IconItemInfo b) {
    			return sCollator.compare(a.getTitle(myIconCache), b.getTitle(myIconCache));
    		}
    	};
    }

    private Comparator<IconItemInfo> getLaunchCountComparator() {
		final AppDB myAppDB = mLauncher.getAppDB();
		return new Comparator<IconItemInfo>() {
			@Override
			public int compare(IconItemInfo a, IconItemInfo b) {
				int valA = Integer.MAX_VALUE;
				int valB = Integer.MAX_VALUE;
				if (a instanceof ShortcutInfo)
					valA = myAppDB.getLaunchCounter((ShortcutInfo)a);
				if (b instanceof ShortcutInfo)
					valB = myAppDB.getLaunchCounter((ShortcutInfo)b);
				return valB - valA;
			}
		};
    }

    public Comparator<IconItemInfo> getCurrentDrawerComparator() {
    	if (mCurrentComparator == null) {
	    	int currentMode = Integer.parseInt(mPreferences.getString(PREF_CURRENT_DRAWER_SORT_ORDER,
	    			String.valueOf(SORT_BY_NAME)));
	    	switch(currentMode) {
	    		case SORT_BY_NAME:
	    			mCurrentComparator = getAppNameComparator(); break;
	    		case SORT_BY_LAUNCH_COUNT:
	    			mCurrentComparator = getLaunchCountComparator(); break;
	    	}
    	}
    	return mCurrentComparator;
    }


	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if (key.equals(PREF_CURRENT_DRAWER_SORT_ORDER) && mLauncher != null) {
			mCurrentComparator = null;
			mLauncher.getAllAppsView().sort();
		}
	}

}
