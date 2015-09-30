package org.accenture.product.lemonade.view;

import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;

/**
 * @author seekting.x.zhang
 * 
 */
public class LauncherAppWidgetHost extends AppWidgetHost
{

	public LauncherAppWidgetHost(Context context, int hostId)
	{
		super(context, hostId);
	}

	@Override
	protected AppWidgetHostView onCreateView(Context context, int appWidgetId, AppWidgetProviderInfo appWidget)
	{
		// TODO Auto-generated method stub
		// return super.onCreateView(context, appWidgetId, appWidget);
		LauncherAppWidgetHostView appWidgetHostView = new LauncherAppWidgetHostView(context);
		appWidgetHostView.setId(appWidgetId);
		return appWidgetHostView;

	}

}
