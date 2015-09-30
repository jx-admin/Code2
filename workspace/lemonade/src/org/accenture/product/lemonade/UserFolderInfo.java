


package org.accenture.product.lemonade;

import java.util.ArrayList;

import org.accenture.product.lemonade.settings.LauncherSettings;

/**
 * Represents a folder containing shortcuts or apps.
 */
class UserFolderInfo extends FolderInfo {
    /**
     * The apps and shortcuts
     */
    ArrayList<ShortcutInfo> contents = new ArrayList<ShortcutInfo>();

    UserFolderInfo() {
        itemType = LauncherSettings.Favorites.ITEM_TYPE_USER_FOLDER;
    }

    /**
     * Add an app or shortcut
     *
     * @param item
     */
    public void add(ShortcutInfo item) {
        contents.add(item);
    }

    /**
     * Remove an app or shortcut. Does not change the DB.
     *
     * @param item
     */
    public void remove(ShortcutInfo item) {
        contents.remove(item);
    }
}
