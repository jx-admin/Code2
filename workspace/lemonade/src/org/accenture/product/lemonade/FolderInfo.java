


package org.accenture.product.lemonade;

import java.util.ArrayList;

import org.accenture.product.lemonade.settings.LauncherSettings.Favorites;

import android.graphics.Bitmap;
import android.view.View;


/**
 * Represents a folder containing shortcuts or apps.
 */
class FolderInfo extends IconItemInfo {

    /**
     * Whether this folder has been opened
     */
    boolean opened;

    @Override
    public Bitmap getIcon(IconCache iconCache) {
    	if (mIcon == null)
    		return Utilities.createIconBitmap(
    				iconCache.getContext().getResources().getDrawable(R.drawable.ic_launcher_folder),
    				iconCache.getContext());
    	return super.getIcon(iconCache);
    }

    @Override
	public void executeAction(EditAction action, View view, Launcher launcher) {
		switch(action.getId()) {
			case ACTION_DELETE: {
				if (this.container == Favorites.CONTAINER_DRAWER) {
					ArrayList<IconItemInfo> lst = new ArrayList<IconItemInfo>();
					lst.add(this);
					launcher.getAllAppsView().removeApps(lst);
				}
				else
					launcher.removeDesktopItem(this);
				launcher.getModel().deleteItemFromDatabase(launcher, this);
			} break;
			default:
				super.executeAction(action, view, launcher);
		}
	}
}
