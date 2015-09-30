


package org.accenture.product.lemonade;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.accenture.product.lemonade.settings.LauncherSettings;
import org.accenture.product.lemonade.util.ResourcesUtil;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;

/**
 * Represents an item in the launcher.
 */
public class ItemInfo {
    // external apps
	private static final String ANDROID_MARKET_URI_BASE = "https://market.android.com/search?q=pname:";
    private static final String ANDROID_SETTINGS_PACKAGE = "com.android.settings";
    private static final String ANDROID_MARKET_PACKAGE = "com.android.vending";
    private static CharSequence mAppInfoLabel;
    private static Drawable mMarketIcon;
    private static CharSequence mMarketLabel;

    // more common actions
    protected static final int ACTION_DELETE = -1;
    protected static final int ACTION_APPINFO = -2;
    protected static final int ACTION_MARKET = -3;
    protected static final int ACTION_SHARE_APP = -4;

    static final int NO_ID = -1;

    /**
     * The id in the settings database for this item
     */
    long id = NO_ID;

    /**
     * One of {@link LauncherSettings.Favorites#ITEM_TYPE_APPLICATION},
     * {@link LauncherSettings.Favorites#ITEM_TYPE_SHORTCUT},
     * {@link LauncherSettings.Favorites#ITEM_TYPE_USER_FOLDER}, or
     * {@link LauncherSettings.Favorites#ITEM_TYPE_APPWIDGET}.
     */
    int itemType;

    /**
     * The id of the container that holds this item. For the desktop, this will be
     * {@link LauncherSettings.Favorites#CONTAINER_DESKTOP}. For the all applications folder it
     * will be {@link #NO_ID} (since it is not stored in the settings DB). For user folders
     * it will be the id of the folder.
     */
    long container = NO_ID;

    /**
     * 此处经常出空指针异常
     */
    int scene=ResourcesUtil.getInstance()._CurrentSceneBean.getId();
    
    /**
     * Iindicates the screen in which the shortcut appears.
     */
    int screen = -1;

    /**
     * Indicates the X position of the associated cell.
     */
    int cellX = -1;

    /**
     * Indicates the Y position of the associated cell.
     */
    int cellY = -1;

    /**
     * Indicates the X cell span.
     */
    int spanX = 1;

    /**
     * Indicates the Y cell span.
     */
    int spanY = 1;

    /**
     * Indicates whether the item is a gesture.
     */
    boolean isGesture = false;

    ItemInfo() {
    }

    ItemInfo(ItemInfo info) {
        id = info.id;
        cellX = info.cellX;
        cellY = info.cellY;
        spanX = info.spanX;
        spanY = info.spanY;
        screen = info.screen;
        itemType = info.itemType;
        container = info.container;
    }

    /**
     * Write the fields of this item to the DB
     *
     * @param values
     */
    void onAddToDatabase(ContentValues values) {
        values.put(LauncherSettings.BaseLauncherColumns.ITEM_TYPE, itemType);
        if (!isGesture) {
            values.put(LauncherSettings.Favorites.CONTAINER, container);
            values.put(LauncherSettings.Favorites.SCREEN, screen);
            values.put(LauncherSettings.Favorites.SCENE, scene);
            values.put(LauncherSettings.Favorites.CELLX, cellX);
            values.put(LauncherSettings.Favorites.CELLY, cellY);
            values.put(LauncherSettings.Favorites.SPANX, spanX);
            values.put(LauncherSettings.Favorites.SPANY, spanY);
        }
    }

    static byte[] flattenBitmap(Bitmap bitmap) {
        // Try go guesstimate how much space the icon will take when serialized
        // to avoid unnecessary allocations/copies during the write.
        int size = bitmap.getWidth() * bitmap.getHeight() * 4;
        ByteArrayOutputStream out = new ByteArrayOutputStream(size);
        try {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            return out.toByteArray();
        } catch (IOException e) {
            Log.w("Favorite", "Could not write icon");
            return null;
        }
    }

    public static void writeBitmap(ContentValues values, Bitmap bitmap) {
        if (bitmap != null) {
            byte[] data = flattenBitmap(bitmap);
            values.put(LauncherSettings.Favorites.ICON, data);
        }
    }

    void unbind() {
    }

    @Override
    public String toString() {
        return "Item(id=" + this.id + " type=" + this.itemType + ")";
    }


    class EditAction
    {
        private final int mId;
        private int mTitle;
        private int mIcon;
        private CharSequence mTitleString;
        private Drawable mIconDrawable;

        public EditAction(int id, int icon, int title)
        {
            mId = id;
            mIcon = icon;
            mTitle = title;
        }

        public EditAction(int id, int icon, CharSequence title)
        {
            mId = id;
            mIcon = icon;
            mTitleString = title;
        }

        public EditAction(int id, Drawable icon, CharSequence title)
        {
            mId = id;
            mIconDrawable = icon;
            mTitleString = title;
        }

        public int getIconResourceId()
        {
            return mIcon;
        }

        public Drawable getIconDrawable()
        {
            return mIconDrawable;
        }

        public CharSequence getTitleString()
        {
            return mTitleString;
        }

        public int getTitleResourceId()
        {
            return mTitle;
        }

        public int getId()
        {
            return mId;
        }
    }

    interface ItemPackage
    {
        public String getPackageName(Launcher launcher);
    }

	public List<EditAction> getAvailableActions(View view, Launcher launcher) {
        ArrayList<EditAction> result = new ArrayList<EditAction>();
        if ( container != ItemInfo.NO_ID )
        {
            result.add(new EditAction(ACTION_DELETE,
                    android.R.drawable.ic_menu_delete,
                    R.string.menu_delete));
        }
        return result;
	}

	public void executeAction(EditAction action, View view, Launcher launcher) {
        switch(action.getId()) {
            case ACTION_APPINFO: {
                try
                {
                    String appPackage = ((ItemPackage) this).getPackageName(launcher);
                    if ( appPackage != null )
                    {
                        Intent intent = new Intent();
                        final int apiLevel = Build.VERSION.SDK_INT;
                        if (apiLevel >= 9)
                        { // above 2.3
                            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                            Uri uri = Uri.fromParts("package", appPackage, null);
                            intent.setData(uri);
                        }
                        else
                        { // below 2.3
                            final String appPkgName = (apiLevel == 8 ? "pkg" : "com.android.settings.ApplicationPkgName");
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setClassName(ANDROID_SETTINGS_PACKAGE, "com.android.settings.InstalledAppDetails");
                            intent.putExtra(appPkgName, appPackage);
                        }
                        launcher.startActivity(intent);
                    }
                }
                catch (Exception e)
                {
                    // failed to tell start app info
                }
            } break;
            case ACTION_MARKET: {
                try
                {
                    String appPackage = ((ItemPackage) this).getPackageName(launcher);
                    if ( appPackage != null )
                    {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(ANDROID_MARKET_URI_BASE + appPackage));
                        launcher.startActivity(intent);
                    }
                }
                catch (Exception e)
                {
                    // failed to tell market to find the app
                }
            } break;
            case ACTION_SHARE_APP: {
            	try
                {
                    String appPackage = ((ItemPackage) this).getPackageName(launcher);
                    if ( appPackage != null )
                    {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT, ANDROID_MARKET_URI_BASE+appPackage);
                        ;
                        launcher.startActivity(Intent.createChooser(intent, launcher.getString(R.string.menu_choose_share_app)));
                    }
                }
                catch (Exception e)
                {
                }
            } break;
        }
	}

    protected void addAppInfoAction(View view, List<EditAction> result, Launcher launcher)
    {
        // get the application info label and if found show the option
        if (mAppInfoLabel == null)
        {
            try
            {
                Resources resources = view.getContext().createPackageContext(ANDROID_SETTINGS_PACKAGE, Context.CONTEXT_IGNORE_SECURITY).getResources();
                int nameID = resources.getIdentifier("application_info_label", "string", ANDROID_SETTINGS_PACKAGE);
                if (nameID != 0)
                {
                    mAppInfoLabel = resources.getString(nameID);
                }
            }
            catch (Exception e)
            {
                // can't find the settings label
            }
        }
        if (mAppInfoLabel != null && this instanceof ItemPackage)
        {
            if ( ((ItemPackage) this).getPackageName(launcher) != null )
            {
                result.add(new EditAction(ACTION_APPINFO,
                        android.R.drawable.ic_menu_info_details,
                        mAppInfoLabel));
            }
        }
    }
    protected void addMarketActions(View view, List<EditAction> result, Launcher launcher)
    {
        // get the market icon and label
        if (mMarketIcon == null && mMarketLabel == null)
        {
            try
            {
                PackageManager packageManager = view.getContext().getPackageManager();
                android.content.pm.ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ANDROID_MARKET_PACKAGE, 0);
                mMarketIcon = applicationInfo.loadIcon(packageManager);
                mMarketLabel = applicationInfo.loadLabel(packageManager);
                if (mMarketLabel == null)
                {
                    mMarketLabel = applicationInfo.name;
                }
            }
            catch (Exception e)
            {
                // would appear there is no market
                mMarketIcon = null;
                mMarketLabel = "no-market";
            }
        }

        // if market, show it as an option
        if (mMarketIcon != null && mMarketLabel != null && this instanceof ItemPackage)
        {
            if ( ((ItemPackage) this).getPackageName(launcher) != null )
            {
                result.add(new EditAction(ACTION_MARKET, mMarketIcon,mMarketLabel));
                result.add(new EditAction(ACTION_SHARE_APP, android.R.drawable.ic_menu_share, R.string.menu_share));
            }
        }
    }
}
