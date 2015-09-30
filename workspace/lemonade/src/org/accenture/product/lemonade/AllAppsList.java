


package org.accenture.product.lemonade;

import java.util.ArrayList;
import java.util.List;

import org.accenture.product.lemonade.appdb.AppDB;

import android.content.ComponentName;


/**
 * Stores the list of all applications for the all apps view.
 */
class AllAppsList {
    public static final int DEFAULT_APPLICATIONS_NUMBER = 42;

    /** The list off all apps. */
    public ArrayList<ShortcutInfo> data =
            new ArrayList<ShortcutInfo>(DEFAULT_APPLICATIONS_NUMBER);
    /** The list of apps that have been added since the last notify() call. */
    public ArrayList<ShortcutInfo> added =
            new ArrayList<ShortcutInfo>(DEFAULT_APPLICATIONS_NUMBER);
    /** The list of apps that have been removed since the last notify() call. */
    public ArrayList<ShortcutInfo> removed = new ArrayList<ShortcutInfo>();
    /** The list of apps that have been modified since the last notify() call. */
    public ArrayList<ShortcutInfo> modified = new ArrayList<ShortcutInfo>();

    private final IconCache mIconCache;

    /**
     * Boring constructor.
     */
    public AllAppsList(IconCache iconCache) {
        mIconCache = iconCache;
    }

    /**
     * Add the supplied ApplicationInfo objects to the list, and enqueue it into the
     * list to broadcast when notify() is called.
     *
     * If the app is already in the list, doesn't add it.
     */
    public void add(ShortcutInfo info) {
        if (containsActivity(data, info.intent.getComponent())) {
            return;
        }
        data.add(info);
        added.add(info);
    }

    public void clear() {
        data.clear();
        added.clear();
        removed.clear();
        modified.clear();
    }

    public int size() {
        return data.size();
    }

    public ShortcutInfo get(int index) {
        return data.get(index);
    }

    /**
     * Remove the apps for the given apk identified by packageName.
     */
    public void removePackage(String packageName) {
    	if (packageName == null)
    		return;
        final List<ShortcutInfo> data = this.data;
        for (int i = data.size() - 1; i >= 0; i--) {
        	ShortcutInfo info = data.get(i);
        	final ComponentName component = info.intent.getComponent();
        	if (component != null) {
	            final String pack = component.getPackageName();
	            if (packageName.equals(pack)) {
	                removed.add(info);
	                data.remove(i);
	            }
        	}
        }
        // This is more aggressive than it needs to be.
        mIconCache.flush();
    }

    /**
     * Remove the apps for the given list of component names
     */
    public void removeComponentNames(String[] componentNames) {
        final List<ShortcutInfo> data = this.data;
        for (int i = data.size() - 1; i >= 0; i--) {
        	ShortcutInfo info = data.get(i);
        	final ComponentName component = info.intent.getComponent();
        	if (component != null) {
	            final String cname = component.flattenToString();
	            if (AppDB.arrayContains(componentNames, cname)) {
	                removed.add(info);
	                data.remove(i);
	            }
        	}
        }
        // This is more aggressive than it needs to be.
        mIconCache.flush();
    }

    /**
     * Add and remove icons for this package which has been updated.
     */
    public void updateFromShortcuts(List<ShortcutInfo> changedItems) {
    	for(ShortcutInfo updated : changedItems) {
    		if (updated.intent != null) {
    			ComponentName name = updated.intent.getComponent();
    			if (name != null) {
    				ShortcutInfo oldEntry = findActivity(data, name);
    				if (oldEntry != null) {
    					modified.add(oldEntry);
    				}
    			}
    		}
    	}
    }


    private static ShortcutInfo findActivity(ArrayList<ShortcutInfo> apps, ComponentName component) {
    	if (component == null)
    		return null;
        final int N = apps.size();
        for (int i=0; i<N; i++) {
            final ShortcutInfo info = apps.get(i);
            ComponentName cn = info.intent.getComponent();
            if (component.equals(cn)) {
                return info;
            }
        }
        return null;
    }

    /**
     * Returns whether <em>apps</em> contains <em>component</em>.
     */
    private static boolean containsActivity(ArrayList<ShortcutInfo> apps, ComponentName component) {
    	return findActivity(apps, component) != null;
    }
}
