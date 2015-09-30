package org.accenture.product.lemonade.view;

import android.appwidget.AppWidgetHostView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * @author seekting.x.zhang
 * 
 */
public class LauncherAppWidgetHostView extends AppWidgetHostView
{

	private boolean mHasPerformedLongPress;

	private CheckForLongPress mPendingCheckForLongPress;

	private LayoutInflater mInflater;

	/**
	 */
	private int id;

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public LauncherAppWidgetHostView(Context context)
	{
		super(context);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev)
	{
		if (mHasPerformedLongPress)
		{
			mHasPerformedLongPress = false;
			return true;
		}

		// Watch for longpress events at this level to make sure
		// users can always pick up this widget
		switch (ev.getAction())
		{
			case MotionEvent.ACTION_DOWN:
			{
				postCheckForLongClick();
				break;
			}

			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				// if (mHasPerformedLongPress) {
				//
				// mHasPerformedLongPress = false;
				// return true;
				// }

				mHasPerformedLongPress = false;
				if (mPendingCheckForLongPress != null)
				{
					removeCallbacks(mPendingCheckForLongPress);
				}

				break;
			case MotionEvent.ACTION_MOVE:
			{

				break;
			}
		}
		return false;

	}

	class CheckForLongPress implements Runnable
	{
		private int mOriginalWindowAttachCount;

		public void run()
		{
			if (hasWindowFocus()
			// if ((mParent != null) && hasWindowFocus()
					&& mOriginalWindowAttachCount == getWindowAttachCount() && !mHasPerformedLongPress)
			{
				if (performLongClick())
				{
					mHasPerformedLongPress = true;
				}
			}
		}

		public void rememberWindowAttachCount()
		{
			mOriginalWindowAttachCount = getWindowAttachCount();
		}
	}

	private void postCheckForLongClick()
	{
		mHasPerformedLongPress = false;

		if (mPendingCheckForLongPress == null)
		{
			mPendingCheckForLongPress = new CheckForLongPress();
		}
		mPendingCheckForLongPress.rememberWindowAttachCount();
		postDelayed(mPendingCheckForLongPress, ViewConfiguration.getLongPressTimeout());
	}

	@Override
	public void cancelLongPress()
	{
		super.cancelLongPress();

		mHasPerformedLongPress = false;
		if (mPendingCheckForLongPress != null)
		{
			removeCallbacks(mPendingCheckForLongPress);
		}
	}
}
