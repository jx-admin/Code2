


package org.accenture.product.lemonade;

import java.util.HashMap;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

/**
 * Cache of application icons.  Icons can be made from any thread.
 */
public class IconCache {

    private static final int INITIAL_ICON_CACHE_CAPACITY = 50;

    private static class CacheEntry {
        public Bitmap icon;
        public String title;
    }

    private final Bitmap mDefaultIcon;
    private final Context mContext;
    private final PackageManager mPackageManager;
    private final HashMap<ComponentName, CacheEntry> mCache =
            new HashMap<ComponentName, CacheEntry>(INITIAL_ICON_CACHE_CAPACITY);

    public IconCache(Context context) {
        mContext = context;
        mPackageManager = context.getPackageManager();
        mDefaultIcon = makeDefaultIcon();
    }

    public Bitmap getDefaultIcon() {
    	return mDefaultIcon;
    }

    public Context getContext() {
    	return mContext;
    }

    private Bitmap makeDefaultIcon() {
        Drawable d = mPackageManager.getDefaultActivityIcon();
        Bitmap b = Bitmap.createBitmap(Math.max(d.getIntrinsicWidth(), 1),
                Math.max(d.getIntrinsicHeight(), 1),
                Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        d.setBounds(0, 0, b.getWidth(), b.getHeight());
        d.draw(c);
        return b;
    }

    /**
     * Remove any records for the supplied ComponentName.
     */
    public void remove(ComponentName componentName) {
        synchronized (mCache) {
        	if (componentName != null)
        		mCache.remove(componentName);
        }
    }

    /**
     * Empty out the cache.
     */
    public void flush() {
        synchronized (mCache) {
            mCache.clear();
        }
    }

    public void addToCache(ComponentName componentName, String title, Bitmap icon) {
        CacheEntry entry = mCache.get(componentName);
        if (entry == null) {
            entry = new CacheEntry();
        }
        mCache.put(componentName, entry);

        entry.title = title;
        entry.icon = icon;
    }

    public Bitmap getIcon(Intent intent) {
        synchronized (mCache) {
            ComponentName component = intent.getComponent();
            CacheEntry entry = mCache.get(component);
            if (component == null || entry == null || entry.icon == null) {
            	final ResolveInfo resolveInfo = mPackageManager.resolveActivity(intent, 0);

	            if (resolveInfo == null || component == null) {
	                return mDefaultIcon;
	            }

	            return cacheLocked(component, resolveInfo).icon;
            } else {
	            return entry.icon;
            }
        }
    }

    public Bitmap getIcon(ComponentName component, ResolveInfo resolveInfo) {
        synchronized (mCache) {
            CacheEntry entry = mCache.get(component);
            if (component == null ||entry == null || entry.icon == null) {
	            if (resolveInfo == null || component == null) {
	                return mDefaultIcon;
	            }
	            return cacheLocked(component, resolveInfo).icon;
            } else {
	            return entry.icon;
            }
        }
    }

    public CharSequence getTitle(Intent intent) {
        synchronized (mCache) {
            ComponentName component = intent.getComponent();
            CacheEntry entry = mCache.get(component);
            if (component == null || entry == null || entry.title == null ) {
            	final ResolveInfo resolveInfo = mPackageManager.resolveActivity(intent, 0);

	            if (resolveInfo == null || component == null) {
	                return "";
	            }

	            return cacheLocked(component, resolveInfo).title;
            } else {
	            return entry.title;
            }
        }
    }

    public CharSequence getTitle(ComponentName component, ResolveInfo resolveInfo) {
        synchronized (mCache) {
            CacheEntry entry = mCache.get(component);
            if (component == null || entry == null || entry.title == null) {
	            if (resolveInfo == null || component == null) {
	                return component.getClassName();
	            }
	            return cacheLocked(component, resolveInfo).title;
            } else {
	            return entry.title;
            }
        }
    }

    public boolean isDefaultIcon(Bitmap icon) {
        return mDefaultIcon == icon;
    }

    private CacheEntry cacheLocked(ComponentName componentName, ResolveInfo info) {
        CacheEntry entry = mCache.get(componentName);
        if (entry == null) {
            entry = new CacheEntry();

            mCache.put(componentName, entry);

        }
        
        if ( entry.title == null ) {
            entry.title = info.loadLabel(mPackageManager).toString();
            if (entry.title == null) {
                entry.title = info.activityInfo.name;
            }
        }
        
        if ( entry.icon == null ) {
            entry.icon = Utilities.createIconBitmap(
                    info.activityInfo.loadIcon(mPackageManager), mContext);
        }
        return entry;
    }
}
