package org.accenture.product.lemonade;

import java.util.ArrayList;
import java.util.List;

import org.accenture.product.lemonade.settings.LauncherSettings;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;

/**
 * Represents a launchable icon on the workspaces and in folders.
 */
public class ShortcutInfo extends IconItemInfo implements ItemInfo.ItemPackage
{
	/**
	 * The intent used to start the application.
	 */
	Intent intent;

	ShortcutInfo()
	{
		itemType = LauncherSettings.BaseLauncherColumns.ITEM_TYPE_SHORTCUT;
	}

	public ShortcutInfo(ShortcutInfo info)
	{
		super(info);
		intent = new Intent(info.intent);
	}

	public ShortcutInfo(long id, ComponentName componentName)
	{
		this.id = id;
		this.container = ItemInfo.NO_ID;
		this.mTitle = null;
		this.setActivity(componentName, Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
	}

	public Intent getIntent()
	{
		return intent;
	}

	@Override
	public Bitmap getIcon(IconCache iconCache)
	{
		if (mIcon == null)
		{
			System.out.println("mIcon==null");
			return iconCache.getIcon(this.intent);
		}
		System.out.println("mIcon!=null");
		return mIcon;
	}

	/**
	 * Creates the application intent based on a component name and various
	 * launch flags. Sets {@link #itemType} to
	 * {@link LauncherSettings.BaseLauncherColumns#ITEM_TYPE_APPLICATION}.
	 * 
	 * @param className
	 *            the class name of the component representing the intent
	 * @param launchFlags
	 *            the launch flags
	 */
	final void setActivity(ComponentName className, int launchFlags)
	{
		intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setComponent(className);
		intent.setFlags(launchFlags);
		itemType = LauncherSettings.BaseLauncherColumns.ITEM_TYPE_SHORTCUT;
	}

	@Override
	void onAddToDatabase(ContentValues values)
	{
		super.onAddToDatabase(values);

		String uri = intent != null ? intent.toUri(0) : null;
		values.put(LauncherSettings.BaseLauncherColumns.INTENT, uri);
	}

	@Override
	public String toString()
	{
		return "ShortcutInfo(title=" + mTitle + ")";
	}

	@Override
	void unbind()
	{
		super.unbind();
	}

	@Override
	public CharSequence getTitle(IconCache mIconCache)
	{
		if (mTitle == null)
			return mIconCache.getTitle(this.intent);
		return mTitle;
	}

	public static void dumpShortcutInfoList(String tag, String label, ArrayList<IconItemInfo> list)
	{
		Log.d(tag, label + " size=" + list.size());
		for (IconItemInfo info : list)
		{
			Log.d(tag, "   title=\"" + info.mTitle + " icon=" + info.mIcon);
		}
	}

	@Override
	public void executeAction(EditAction action, View view, Launcher launcher)
	{
		switch (action.getId())
		{
			case ACTION_DELETE:
			{
				launcher.removeDesktopItem(this);
				launcher.getModel().deleteItemFromDatabase(launcher, this);
			}
				break;
			default:
				super.executeAction(action, view, launcher);
		}
	}

	@Override
	public List<EditAction> getAvailableActions(View view, Launcher launcher)
	{
		List<EditAction> result = super.getAvailableActions(view, launcher);
		addAppInfoAction(view, result, launcher);
		addMarketActions(view, result, launcher);
		return result;
	}

	@Override
	public String getPackageName(Launcher launcher)
	{
		return launcher.getPackageNameFromIntent(intent);
	}
}
