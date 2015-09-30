package org.accenture.product.lemonade.widget;

import org.accenture.product.lemonade.R;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.RemoteViews;

public class SystemWizardWidget extends AppWidgetProvider
{

	/**
	 * 小人的标题和内容
	 */
	private static String[][] WIZARD_TITLE_CONTENT = new String[6][2];
	private static int[] WIZARD_HEADS = new int[6];

	/**
	 * 
	 */
	private static final String ACTION = "action";
	private static final String UPDATE = "update";

	Handler handler = new Handler();

	private static int currentIndex = 0;

	Context context;
	AppWidgetManager appWidgetManager;

	@Override
	public void onEnabled(Context context)
	{

		super.onEnabled(context);

	}

	@Override
	public void onReceive(Context context, Intent intent)
	{
		// System.out.println(this);
		// System.out.println("onReceiver");
		super.onReceive(context, intent);
		this.context = context;
		initWizard();

		if (UPDATE.equals(intent.getStringExtra(ACTION)))
		{

			changeWizard();
		}
	}

	void initWizard()
	{
		if (WIZARD_TITLE_CONTENT[0][0] == null || "".equals(WIZARD_TITLE_CONTENT[0][0]))
		{

			WIZARD_TITLE_CONTENT[0][0] = context.getString(R.string.wizard_title_1);
			WIZARD_TITLE_CONTENT[1][0] = context.getString(R.string.wizard_title_2);
			WIZARD_TITLE_CONTENT[2][0] = context.getString(R.string.wizard_title_3);
			WIZARD_TITLE_CONTENT[3][0] = context.getString(R.string.wizard_title_4);
			WIZARD_TITLE_CONTENT[4][0] = context.getString(R.string.wizard_title_5);
			WIZARD_TITLE_CONTENT[5][0] = context.getString(R.string.wizard_title_6);

			WIZARD_TITLE_CONTENT[0][1] = context.getString(R.string.wizard_content_1);
			WIZARD_TITLE_CONTENT[1][1] = context.getString(R.string.wizard_content_2);
			WIZARD_TITLE_CONTENT[2][1] = context.getString(R.string.wizard_content_3);
			WIZARD_TITLE_CONTENT[3][1] = context.getString(R.string.wizard_content_4);
			WIZARD_TITLE_CONTENT[4][1] = context.getString(R.string.wizard_content_5);
			WIZARD_TITLE_CONTENT[5][1] = context.getString(R.string.wizard_content_6);

			// WIZARD_HEADS[0] =
			// context.getResources().getDrawable(R.drawable.head1);
			// WIZARD_HEADS[1] =
			// context.getResources().getDrawable(R.drawable.head2);
			// WIZARD_HEADS[2] =
			// context.getResources().getDrawable(R.drawable.head3);
			WIZARD_HEADS[0] = R.drawable.head1;
			WIZARD_HEADS[1] = R.drawable.head2;
			WIZARD_HEADS[2] = R.drawable.head3;
			WIZARD_HEADS[3] = R.drawable.head4;
			WIZARD_HEADS[4] = R.drawable.head5;
			WIZARD_HEADS[5] = R.drawable.head6;
		}
	}

	/**
	 * 换
	 */
	void changeWizard()
	{
		handler.post(new Runnable()
		{

			@Override
			public void run()
			{

				currentIndex++;
				setText(currentIndex);
			}
		});
	}

	private void setText(int index)
	{

		if (context != null)
		{
			RemoteViews r = new RemoteViews(context.getPackageName(), R.layout.system_wizard_layout);
			Intent intent = new Intent();
			intent.setAction("org.accenture.product.lemonade.UPDATE_TEXT");
			PendingIntent pendingIntent =
			// PendingIntent.getService(context, 0, intent, 0);
			PendingIntent.getBroadcast(context, 0, intent, 0);
			intent.putExtra(ACTION, UPDATE);
			r.setOnClickPendingIntent(R.id.layout, pendingIntent);
			r.setOnClickPendingIntent(R.id.messageButton, pendingIntent);
			if (currentIndex >= WIZARD_TITLE_CONTENT.length)
			{
				currentIndex = 0;
			}
			r.setTextViewText(R.id.wizard_title, WIZARD_TITLE_CONTENT[currentIndex][0]);

			r.setTextViewText(R.id.wizard_description, WIZARD_TITLE_CONTENT[currentIndex][1]);
			// % (WIZARD_TITLE_CONTENT.length / 2)
			r.setImageViewResource(R.id.wizard_head, WIZARD_HEADS[currentIndex]);
			StringBuffer sb = new StringBuffer();
			sb.append("(");
			sb.append(currentIndex + 1);
			sb.append("/");
			sb.append(WIZARD_TITLE_CONTENT.length);
			sb.append(")");
			r.setTextViewText(R.id.wizard_number, sb.toString());
			if (appWidgetManager != null)
			{
				appWidgetManager.updateAppWidget(new ComponentName(context, SystemWizardWidget.class), r);
			}
			else
			{
				appWidgetManager = AppWidgetManager.getInstance(context);
				appWidgetManager.updateAppWidget(new ComponentName(context, SystemWizardWidget.class), r);
			}
		}
		// appWidgetManager.updateAppWidget(appWidgetIds, r);
	}

	@Override
	public void onUpdate(Context c, AppWidgetManager appWidgetManager, int[] appWidgetIds)
	{
		// TODO Auto-generated method stub

		super.onUpdate(c, appWidgetManager, appWidgetIds);
		// System.out.println("onupdate");
		this.context = c;
		initWizard();

		RemoteViews r = new RemoteViews(context.getPackageName(), R.layout.system_wizard_layout);
		Intent intent = new Intent();
		intent.setAction("org.accenture.product.lemonade.UPDATE_TEXT");
		intent.putExtra(ACTION, UPDATE);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

		r.setOnClickPendingIntent(R.id.layout, pendingIntent);
		r.setOnClickPendingIntent(R.id.messageButton, pendingIntent);

		if (currentIndex >= WIZARD_TITLE_CONTENT.length)
		{
			currentIndex = 0;
		}
		r.setTextViewText(R.id.wizard_title, WIZARD_TITLE_CONTENT[currentIndex][0]);

		r.setTextViewText(R.id.wizard_description, WIZARD_TITLE_CONTENT[currentIndex][1]);
		r.setImageViewResource(R.id.wizard_head, WIZARD_HEADS[currentIndex]);
		StringBuffer sb = new StringBuffer();
		sb.append("(");
		sb.append(currentIndex + 1);
		sb.append("/");
		sb.append(WIZARD_TITLE_CONTENT.length);
		sb.append(")");

		r.setTextViewText(R.id.wizard_number, sb.toString());
		appWidgetManager.updateAppWidget(appWidgetIds, r);
		// appWidgetManager.updateAppWidget(new ComponentName(context,
		// MyWidget.class), r);
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds)
	{
		// TODO Auto-generated method stub
		// System.out.println("ondeleted");
		super.onDeleted(context, appWidgetIds);
	}

	@Override
	public void onDisabled(Context context)
	{
		// System.out.println("ondi");
		// TODO Auto-generated method stub
		super.onDisabled(context);
	}

}
