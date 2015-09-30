


package org.accenture.product.lemonade;

import org.accenture.product.lemonade.settings.LauncherSettings;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;


/**
 * An icon that can appear on in the workspace representing an {@link UserFolder}.
 */
public class FolderIcon extends BubbleTextView implements DropTarget {
    private UserFolderInfo mInfo;
    protected Launcher mLauncher;
    private Drawable mCloseIcon;
    private Drawable mOpenIcon;

    public FolderIcon(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FolderIcon(Context context) {
        super(context);
    }

    @Override
    public void updateFromItemInfo(IconCache iCache, IconItemInfo info) {
    	super.updateFromItemInfo(iCache, info);
    	final Resources resources = mLauncher.getResources();
    	mCloseIcon = new FastBitmapDrawable(info.getIcon(iCache));
        if (info.usesDefaultIcon())
        	mOpenIcon = resources.getDrawable(R.drawable.ic_launcher_folder_open);
        else
        	mOpenIcon = mCloseIcon;
        setCompoundDrawablesWithIntrinsicBounds(null, mCloseIcon, null, null);
    }

    static FolderIcon fromXml(int resId, Launcher launcher, ViewGroup group,
            UserFolderInfo folderInfo) {
        FolderIcon icon = (FolderIcon) LayoutInflater.from(launcher).inflate(resId, group, false);
        icon.mLauncher = launcher;
        icon.updateFromItemInfo(launcher.getIconCache(), folderInfo);
        icon.setTag(folderInfo);
        icon.setOnClickListener(launcher);
        icon.mInfo = folderInfo;

        return icon;
    }

    public boolean acceptDrop(DragSource source, int x, int y, int xOffset, int yOffset,
            DragView dragView, Object dragInfo) {
        final ItemInfo item = (ItemInfo) dragInfo;
        final int itemType = item.itemType;
        return itemType == LauncherSettings.Favorites.ITEM_TYPE_SHORTCUT
                && item.container != mInfo.id;
    }

    public Rect estimateDropLocation(DragSource source, int x, int y, int xOffset, int yOffset,
            DragView dragView, Object dragInfo, Rect recycle) {
        return null;
    }

    public void onDrop(DragSource source, int x, int y, int xOffset, int yOffset,
            DragView dragView, Object dragInfo) {
        ShortcutInfo item;
        if (dragInfo instanceof ShortcutInfo) {
        	item = (ShortcutInfo)dragInfo;
        	mInfo.add(item);
        	mLauncher.getModel().addOrMoveItemInDatabase(mLauncher, item, mInfo.id, 0, 0, 0);
        }
    }

    public void onDragEnter(DragSource source, int x, int y, int xOffset, int yOffset,
            DragView dragView, Object dragInfo) {
        setCompoundDrawablesWithIntrinsicBounds(null, mOpenIcon, null, null);
    }

    public void onDragOver(DragSource source, int x, int y, int xOffset, int yOffset,
            DragView dragView, Object dragInfo) {
    }

    public void onDragExit(DragSource source, int x, int y, int xOffset, int yOffset,
            DragView dragView, Object dragInfo) {
        setCompoundDrawablesWithIntrinsicBounds(null, mCloseIcon, null, null);
    }
}
