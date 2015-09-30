package org.accenture.product.lemonade.receiver;

import org.accenture.product.lemonade.content.WidgetDataBaseAdapter;
import org.accenture.product.lemonade.model.WidgetBean;

import android.content.Context;
import android.content.Intent;

public class WidgetReceiver extends android.content.BroadcastReceiver
{
	public static final String EXTRAS_KEY = "widget_key";
	public static final String ADD_WIDGET = "org.accenture.product.lemonade.add_widget";
	public static final String UPDATE_WIDGET = "org.accenture.product.lemonade.update_widget";
	public static final String DELETE_WIDGET = "org.accenture.product.lemonade.delete_widget";

	@Override
	public void onReceive(Context arg0, Intent arg1)
	{
		String action = arg1.getAction();
		WidgetBean bean = (WidgetBean) arg1.getExtras().get(EXTRAS_KEY);
		WidgetDataBaseAdapter adapter = WidgetDataBaseAdapter.getInstance();
		adapter.open();
		if (ADD_WIDGET.equals(action))
		{
			adapter.insert(bean);
			
		}
		else if (UPDATE_WIDGET.equals(action))
		{
			adapter.update(bean);
		}
		else if (DELETE_WIDGET.equals(action))
		{
			adapter.delete(bean.getId());
		}
		adapter.close();
	}
}
