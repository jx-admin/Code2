


package org.accenture.product.lemonade;

import org.accenture.product.lemonade.appdb.AppDB;
import org.accenture.product.lemonade.settings.LauncherSettings;
import org.accenture.product.lemonade.settings.Preferences;

import android.app.Application;
import android.content.ContentResolver;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.Handler;

public class LauncherApplication extends Application {
	private static final String ACTION_EXTERNAL_APPLICATIONS_AVAILABLE = "android.intent.action.EXTERNAL_APPLICATIONS_AVAILABLE";
	private static final String ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE = "android.intent.action.EXTERNAL_APPLICATIONS_UNAVAILABLE";

	private LauncherModel mModel;
	private IconCache mIconCache;
    private AppDB mAppDB;

    @Override
    public void onCreate() {
        //VMRuntime.getRuntime().setMinimumHeapSize(4 * 1024 * 1024);

        super.onCreate();

        mIconCache = new IconCache(this);
        mAppDB = new AppDB(this, mIconCache);
        mModel = new LauncherModel(this, mIconCache);

        // Register intent receivers
        IntentFilter filter = new IntentFilter(AppDB.INTENT_DB_CHANGED);
        registerReceiver(mModel, filter);
        filter = new IntentFilter();
        filter.addAction(ACTION_EXTERNAL_APPLICATIONS_AVAILABLE);
        filter.addAction(ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE);
        registerReceiver(mModel, filter);

        // Register for changes to the favorites
        ContentResolver resolver = getContentResolver();
        resolver.registerContentObserver(LauncherSettings.Favorites.CONTENT_URI, true,
                mFavoritesObserver);
    }

    /**
     * There's no guarantee that this function is ever called.
     */
    @Override
    public void onTerminate() {
        super.onTerminate();

        unregisterReceiver(mModel);

        ContentResolver resolver = getContentResolver();
        resolver.unregisterContentObserver(mFavoritesObserver);
        Preferences.getInstance().setLauncher(null);
    }

    /**
     * Receives notifications whenever the user favorites have changed.
     */
    private final ContentObserver mFavoritesObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            mModel.startLoader(LauncherApplication.this, false);
        }
    };

    LauncherModel setLauncher(Launcher launcher) {
        mModel.initialize(launcher);
        return mModel;
    }

    IconCache getIconCache() {
        return mIconCache;
    }

    LauncherModel getModel() {
        return mModel;
    }

    AppDB getAppDB() {
    	return mAppDB;
    }
}
