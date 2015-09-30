package org.accenture.product.lemonade;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

import org.accenture.product.lemonade.settings.Preferences;

import android.content.ComponentName;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AllApps2D extends RelativeLayout implements AllAppsView, AdapterView.OnItemClickListener,
		AdapterView.OnItemLongClickListener, View.OnKeyListener, DragSource
{

	private static final String TAG = "Launcher.AllApps2D";
	private static final boolean DEBUG = false;

	private Launcher mLauncher;
	private DragController mDragController;

	private GridView mGrid;

	private final ArrayList<IconItemInfo> mAllAppsList = new ArrayList<IconItemInfo>();

	// preserve compatibility with 3D all apps:
	// 0.0 -> hidden
	// 1.0 -> shown and opaque
	// intermediate values -> partially shown & partially opaque
	private float mZoom;

	private final AppsAdapter mAppsAdapter;

	public static final int ICON_WIDTH = 50;
	public static final int ICON_HEIGHT = 50;

	// ------------------------------------------------------------

	public static class HomeButton extends ImageButton
	{
		public HomeButton(Context context, AttributeSet attrs)
		{
			super(context, attrs);
		}

		@Override
		public View focusSearch(int direction)
		{
			if (direction == FOCUS_UP)
				return super.focusSearch(direction);
			return null;
		}
	}

	public class AppsAdapter extends ArrayAdapter<IconItemInfo>
	{
		private final LayoutInflater mInflater;

		public AppsAdapter(Context context, ArrayList<IconItemInfo> apps)
		{
			super(context, 0, apps);
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			final IconItemInfo info = getItem(position);

			if (convertView == null)
			{
				convertView = mInflater.inflate(R.layout.application_boxed, parent, false);
			}

			final TextView textView = (TextView) convertView;

			// Bitmap icon = info.getIcon(mLauncher.getIconCache());
			// if (DEBUG) {
			// Log.d(TAG, "icon bitmap = " + icon
			// + " density = " + icon.getDensity());
			// }
			// icon.setDensity(Bitmap.DENSITY_NONE);
			// textView.setCompoundDrawablesWithIntrinsicBounds(null, new
			// BitmapDrawable(icon), null, null);
			// textView.setText(info.getTitle(mLauncher.getIconCache()));
			//            
			// try
			// {
			// FileWriter fw = new FileWriter("/sdcard/bb.txt",true);
			// fw.write(((ShortcutInfo)info).intent.getComponent().toShortString()+"\n");
			// fw.flush();
			// fw.close();
			// }
			// catch (IOException e1)
			// {
			// // TODO Auto-generated catch block
			// e1.printStackTrace();
			// }

			// IconItemInfo shortCut =
			// (IconItemInfo)getItemAtPosition(position);
			// if (info instanceof ShortcutInfo) {
			// ShortcutInfo app = (ShortcutInfo)getItemAtPosition(position);
			// mLauncher.startActivitySafely(app.intent, app);
			// } else if (info instanceof FolderInfo) {
			// mLauncher.openFolder((FolderInfo)info);
			// }

			try
			{
				String path = IconConfig.config(((ShortcutInfo) info).intent);
				if (path == null)
				{
					Bitmap icon = info.getIcon(mLauncher.getIconCache());
					icon.setDensity(Bitmap.DENSITY_NONE);
					textView.setCompoundDrawablesWithIntrinsicBounds(null, new BitmapDrawable(icon), null, null);
					textView.setText(info.getTitle(mLauncher.getIconCache()));
				}
				else
				{
					InputStream picIs = getResources().getAssets().open(path);
					BitmapDrawable picDrawable = new BitmapDrawable(picIs);
					picDrawable.setBounds(0, 0, ICON_WIDTH, ICON_HEIGHT);
					textView.setCompoundDrawables(null, picDrawable, null, null);
					textView.setText(info.getTitle(mLauncher.getIconCache()));
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			return convertView;
		}
	}

	public AllApps2D(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		setVisibility(View.GONE);
		setSoundEffectsEnabled(false);

		mAppsAdapter = new AppsAdapter(getContext(), mAllAppsList);
		mAppsAdapter.setNotifyOnChange(false);
	}

	@Override
	protected void onFinishInflate()
	{
		setBackgroundColor(Color.BLACK);

		try
		{
			mGrid = (GridView) findViewWithTag("all_apps_2d_grid");
			if (mGrid == null)
				throw new Resources.NotFoundException();
			mGrid.setOnItemClickListener(this);
			// kya drag icon out
			mGrid.setOnItemLongClickListener(this);
			mGrid.setBackgroundColor(Color.BLACK);
			mGrid.setCacheColorHint(Color.BLACK);

			ImageButton homeButton = (ImageButton) findViewWithTag("all_apps_2d_home");
			if (homeButton == null)
				throw new Resources.NotFoundException();
			homeButton.setOnClickListener(new View.OnClickListener()
			{
				public void onClick(View v)
				{
					mLauncher.closeAllApps(true);
				}
			});
		}
		catch (Resources.NotFoundException e)
		{
			Log.e(TAG, "Can't find necessary layout elements for AllApps2D");
		}

		setOnKeyListener(this);
	}

	public AllApps2D(Context context, AttributeSet attrs, int defStyle)
	{
		this(context, attrs);
	}

	public void setLauncher(Launcher launcher)
	{
		mLauncher = launcher;
	}

	public boolean onKey(View v, int keyCode, KeyEvent event)
	{
		if (!isVisible())
			return false;

		switch (keyCode)
		{
			case KeyEvent.KEYCODE_BACK:
				mLauncher.closeAllApps(true);
				break;
			default:
				return false;
		}

		return true;
	}

	public void onItemClick(AdapterView<?> parent, View v, int position, long id)
	{
		IconItemInfo info = (IconItemInfo) parent.getItemAtPosition(position);
		if (info instanceof ShortcutInfo)
		{
			ShortcutInfo app = (ShortcutInfo) parent.getItemAtPosition(position);

			Log.e("app.intent:", app.intent.getComponent().toShortString());

			mLauncher.startActivitySafely(app.intent, app);

//			try
//			{
//				FileWriter fw = new FileWriter("/sdcard/bb.txt", false);
//				fw.write(app.intent.getComponent().toShortString());
//				fw.flush();
//				fw.close();
//			}
//			catch (IOException e1)
//			{
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}

		}
		else if (info instanceof FolderInfo)
		{
			mLauncher.openFolder((FolderInfo) info);
		}
	}

	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
	{
		if (!view.isInTouchMode())
		{
			return false;
		}

		IconItemInfo info = (IconItemInfo) parent.getItemAtPosition(position);
		if (info instanceof ShortcutInfo)
			info = new ShortcutInfo((ShortcutInfo) info);

		// mLauncher.showActions(info, view, new PopupWindow.OnDismissListener()
		// {
		// @Override
		// public void onDismiss()
		// {
		// mLauncher.closeAllApps(true);
		// }
		// });
		mDragController.startDrag(view, this, info, DragController.DRAG_ACTION_COPY);
		mLauncher.closeAllApps(true);
		return true;
	}

	@Override
	protected void onFocusChanged(boolean gainFocus, int direction, android.graphics.Rect prev)
	{
		if (gainFocus)
		{
			mGrid.requestFocus();
		}
	}

	public void setDragController(DragController dragger)
	{
		mDragController = dragger;
	}

	public void onDropCompleted(View target, boolean success)
	{
	}

	/**
	 * Zoom to the specifed level.
	 * 
	 * @param zoom
	 *            [0..1] 0 is hidden, 1 is open
	 */
	public void zoom(float zoom, boolean animate)
	{
		// Log.d(TAG, "zooming " + ((zoom == 1.0) ? "open" : "closed"));
		cancelLongPress();

		mZoom = zoom;

		if (isVisible())
		{
			getParent().bringChildToFront(this);
			setVisibility(View.VISIBLE);
			mGrid.setAdapter(mAppsAdapter);
			if (animate)
			{
				startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.all_apps_2d_fade_in));
			}
			else
			{
				onAnimationEnd();
			}
		}
		else
		{
			if (animate)
			{
				startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.all_apps_2d_fade_out));
			}
			else
			{
				onAnimationEnd();
			}
		}
	}

	@Override
	protected void onAnimationEnd()
	{
		if (!isVisible())
		{
			setVisibility(View.GONE);
			mGrid.setAdapter(null);
			mZoom = 0.0f;
		}
		else
		{
			mZoom = 1.0f;
		}

		mLauncher.zoomed(mZoom);
	}

	public boolean isVisible()
	{
		return mZoom > 0.001f;
	}

	@Override
	public boolean isOpaque()
	{
		return mZoom > 0.999f;
	}

	public void setApps(ArrayList<? extends IconItemInfo> list)
	{
		mAllAppsList.clear();
		addApps(list);
	}

	public void addApps(ArrayList<? extends IconItemInfo> list)
	{
		// Log.d(TAG, "addApps: " + list.size() + " apps: " + list.toString());

		final int N = list.size();

		for (int i = 0; i < N; i++)
		{
			final IconItemInfo item = list.get(i);
			int index = Collections.binarySearch(mAllAppsList, item, Preferences.getInstance().getCurrentDrawerComparator());
			if (index < 0)
			{
				index = -(index + 1);
			}
			mAllAppsList.add(index, item);
		}
		mAppsAdapter.notifyDataSetChanged();
	}

	public void sort()
	{
		Collections.sort(mAllAppsList, Preferences.getInstance().getCurrentDrawerComparator());
		mAppsAdapter.notifyDataSetChanged();
	}

	public void removeApps(ArrayList<? extends IconItemInfo> list)
	{
		final int N = list.size();
		for (int i = 0; i < N; i++)
		{
			IconItemInfo iii = list.get(i);
			final int index;
			if (iii instanceof ShortcutInfo)
			{
				final ShortcutInfo item = (ShortcutInfo) iii;
				index = findAppByComponent(mAllAppsList, item);
			}
			else
			{ // everything else should have an id...
				index = findAppById(mAllAppsList, iii);
			}

			if (index >= 0)
			{
				mAllAppsList.remove(index);
			}
			else
			{
				Log.w(TAG, "couldn't find a match for item \"" + iii + "\"");
				// Try to recover. This should keep us from crashing for now.
			}
		}
		mAppsAdapter.notifyDataSetChanged();
	}

	public void updateApps(ArrayList<? extends IconItemInfo> list)
	{
		// Just remove and add, because they may need to be re-sorted.
		removeApps(list);
		addApps(list);
	}

	private static int findAppByComponent(ArrayList<IconItemInfo> list, ShortcutInfo item)
	{
		ComponentName component = item.intent.getComponent();
		if (component == null)
			return -1;
		final int N = list.size();
		for (int i = 0; i < N; i++)
		{
			IconItemInfo curItm = list.get(i);
			if (curItm instanceof ShortcutInfo)
			{
				ShortcutInfo x = (ShortcutInfo) curItm;
				if (component.equals(x.intent.getComponent()))
				{
					return i;
				}
			}
		}
		return -1;
	}

	private static int findAppById(ArrayList<IconItemInfo> list, IconItemInfo item)
	{
		final long id = item.id;
		if (id == ItemInfo.NO_ID)
			return -1;
		final int N = list.size();
		for (int i = 0; i < N; i++)
		{
			IconItemInfo curItm = list.get(i);
			if (curItm.id == id)
				return i;
		}
		return -1;
	}

	public void dumpState()
	{
		ShortcutInfo.dumpShortcutInfoList(TAG, "mAllAppsList", mAllAppsList);
	}

	public void surrender()
	{
	}
}
