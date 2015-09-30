


package org.accenture.product.lemonade;

import java.util.List;

import org.accenture.product.lemonade.settings.LauncherSettings;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

class LiveFolderInfo extends FolderInfo implements ItemInfo.ItemPackage {

    /**
     * The base intent, if it exists.
     */
    Intent baseIntent;

    /**
     * The live folder's content uri.
     */
    Uri uri;

    /**
     * The live folder's display type.
     */
    int displayMode;

    LiveFolderInfo() {
        itemType = LauncherSettings.Favorites.ITEM_TYPE_LIVE_FOLDER;
    }

    @Override
    void onAddToDatabase(ContentValues values) {
        super.onAddToDatabase(values);
        values.put(LauncherSettings.Favorites.URI, uri.toString());
        if (baseIntent != null) {
            values.put(LauncherSettings.Favorites.INTENT, baseIntent.toUri(0));
        }
        values.put(LauncherSettings.Favorites.DISPLAY_MODE, displayMode);
    }

    @Override
    public List<EditAction> getAvailableActions(View view, Launcher launcher) {
        List<EditAction> result = super.getAvailableActions(view, launcher);
        addAppInfoAction(view, result, launcher);
        addMarketActions(view, result, launcher);
        return result;
    }

    @Override
    public String getPackageName(Launcher launcher)
    {
        return launcher.getPackageNameFromIntent(baseIntent);
    }
}
