


package org.accenture.product.lemonade;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;


public class LiveFolderIcon extends FolderIcon {
    public LiveFolderIcon(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LiveFolderIcon(Context context) {
        super(context);
    }

    public void update(LiveFolderInfo folderInfo) {
    	final Resources resources = mLauncher.getResources();
        Bitmap b = folderInfo.getIcon(mLauncher.getIconCache());
        if (b == null || folderInfo.usesDefaultIcon()) {
            b = Utilities.createIconBitmap(resources.getDrawable(R.drawable.ic_launcher_folder),
            		mLauncher);
        }
        setCompoundDrawablesWithIntrinsicBounds(null, new FastBitmapDrawable(b), null, null);
        setText(folderInfo.getTitle(mLauncher.getIconCache()));
        setTag(folderInfo);
        setOnClickListener(mLauncher);
    }

    static LiveFolderIcon fromXml(int resId, Launcher launcher, ViewGroup group,
            LiveFolderInfo folderInfo) {

        LiveFolderIcon icon = (LiveFolderIcon)
                LayoutInflater.from(launcher).inflate(resId, group, false);
        icon.mLauncher = launcher;
    	launcher.getResources();
    	icon.updateFromItemInfo(launcher.getIconCache(), folderInfo);
        icon.setTag(folderInfo);
        icon.setOnClickListener(launcher);
        return icon;
    }

    @Override
    public boolean acceptDrop(DragSource source, int x, int y, int xOffset, int yOffset,
            DragView dragView, Object dragInfo) {
        return false;
    }

    @Override
    public void onDrop(DragSource source, int x, int y, int xOffset, int yOffset,
            DragView dragView, Object dragInfo) {
    }

    @Override
    public void onDragEnter(DragSource source, int x, int y, int xOffset, int yOffset,
            DragView dragView, Object dragInfo) {
    }

    @Override
    public void onDragOver(DragSource source, int x, int y, int xOffset, int yOffset,
            DragView dragView, Object dragInfo) {
    }

    @Override
    public void onDragExit(DragSource source, int x, int y, int xOffset, int yOffset,
            DragView dragView, Object dragInfo) {
    }
}
