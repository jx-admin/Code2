package org.accenture.product.lemonade;

import org.accenture.product.lemonade.settings.LauncherSettings;

import android.appwidget.AppWidgetHostView;
import android.content.ContentValues;

/**
 * Represents a widget, which just contains an identifier.
 */
class LauncherAppWidgetInfo extends ItemInfo
{

	/**
	 * Identifier for this widget when talking with
	 * {@link android.appwidget.AppWidgetManager} for updates.
	 */
	int appWidgetId;

	/**
	 * View that holds this widget after it's been created. This view isn't
	 * created until Launcher knows it's needed.
	 */
	AppWidgetHostView hostView = null;

	LauncherAppWidgetInfo(int appWidgetId)
	{
		itemType = LauncherSettings.Favorites.ITEM_TYPE_APPWIDGET;
		this.appWidgetId = appWidgetId;
	}

	@Override
	void onAddToDatabase(ContentValues values)
	{
		super.onAddToDatabase(values);
		values.put(LauncherSettings.Favorites.APPWIDGET_ID, appWidgetId);
	}

	@Override
	public String toString()
	{
		return "AppWidget(id=" + Integer.toString(appWidgetId) + ")";
	}

	@Override
	void unbind()
	{
		super.unbind();
		hostView = null;
	}
}