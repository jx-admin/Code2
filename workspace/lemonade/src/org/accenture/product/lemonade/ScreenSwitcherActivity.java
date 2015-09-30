package org.accenture.product.lemonade;

import java.util.ArrayList;

import org.accenture.product.lemonade.view.HomeScreenLayout;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;

/**
 * 
 * @author shaohu.zhu
 */
public class ScreenSwitcherActivity extends Activity
{
	public static ArrayList<Bitmap> thumbnailList;

	private static Launcher mLauncher;
	ImageView delete;
	ImageView add;
	LinearLayout line1;
	LinearLayout line2;
	LinearLayout line3;
	
//	private static final int VIEW_WIDTH=96;
//	private static final int VIEW_HEIGHT=110;
	
	private static final int VIEW_WIDTH=100;
	private static final int VIEW_HEIGHT=142;

	private int screenIndex;

	private boolean finishFlag = true;

	public static int numCanNotDelete = 3; // 不能被删除的屏幕数量

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.screenswitcher);

		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

		line1 = (LinearLayout) findViewById(R.id.line1);
		line2 = (LinearLayout) findViewById(R.id.line2);
		line3 = (LinearLayout) findViewById(R.id.line3);
		delete = (ImageView) findViewById(R.id.delete);
		add = (ImageView) findViewById(R.id.add);

		addButtonListener();

		if (numCanNotDelete == 3)
		{
			fillScreenCase3();
		}
		else
		{
			fillScreen();
		}

	}

	public void deleteScreen()
	{
		int size = thumbnailList.size();
		int frontCount = Launcher.FRONT_SCREEN_COUNT;
		if (size > 5)
		{
			switch (size)
			{
				case 6:
					if ((screenIndex == 0 && frontCount == 1) || (screenIndex == 5 && frontCount == 0))
					{
						mLauncher.removeOneScreen(screenIndex);
					}
					else
					{
						Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete,
								Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
					}
					break;
				case 7:
					if (frontCount == 0)
					{
						if (screenIndex == 5 || screenIndex == 6)
						{
							mLauncher.removeOneScreen(screenIndex);
						}
						else
						{
							Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete,
									Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
						}
					}
					else if (frontCount == 1)
					{
						if (screenIndex == 0 || screenIndex == 6)
						{
							mLauncher.removeOneScreen(screenIndex);
						}
						else
						{
							Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete,
									Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
						}
					}
					else if (frontCount == 2)
					{
						if (screenIndex == 0 || screenIndex == 1)
						{
							mLauncher.removeOneScreen(screenIndex);
						}
						else
						{
							Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete,
									Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
						}
					}
					else
					{
						Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete,
								Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
					}
					break;
				case 8:
					if (frontCount == 1)
					{
						if (screenIndex == 0 || screenIndex == 6 || screenIndex == 7)
						{
							mLauncher.removeOneScreen(screenIndex);
						}
						else
						{
							Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete,
									Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
						}
					}
					else if (frontCount == 2)
					{
						if (screenIndex == 0 || screenIndex == 1 || screenIndex == 7)
						{
							mLauncher.removeOneScreen(screenIndex);
						}
						else
						{
							Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete,
									Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
						}
					}
					else
					{
						Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete,
								Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
					}
					break;
				case 9:
					if (frontCount == 2)
					{
						if (screenIndex == 0 || screenIndex == 1 || screenIndex == 7 || screenIndex == 8)
						{
							mLauncher.removeOneScreen(screenIndex);
						}
						else
						{
							Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete,
									Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
						}
					}
					else
					{
						Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete,
								Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
					}
					break;
				default:
					Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete, Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					break;
			}

			line1.removeAllViews();
			line2.removeAllViews();
			line3.removeAllViews();
			fillScreen();
		}
		else
		{
			Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}

	}

	public void deleteScreenCase3()
	{
		int size = thumbnailList.size();
		int frontCount = Launcher.FRONT_SCREEN_COUNT;
		if (size > 3)
		{
			switch (size)
			{
				case 4:
					if ((screenIndex == 0 && frontCount == 1) || (screenIndex == 3 && frontCount == 0))
					{
						mLauncher.removeOneScreen(screenIndex);
					}
					else
					{
						Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete,
								Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
					}
					break;
				case 5:
					if (frontCount == 0)
					{
						if (screenIndex == 3 || screenIndex == 4)
						{
							mLauncher.removeOneScreen(screenIndex);
						}
						else
						{
							Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete,
									Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
						}
					}
					else if (frontCount == 1)
					{
						if (screenIndex == 0 || screenIndex == 4)
						{
							mLauncher.removeOneScreen(screenIndex);
						}
						else
						{
							Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete,
									Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
						}
					}
					else if (frontCount == 2)
					{
						if (screenIndex == 0 || screenIndex == 1)
						{
							mLauncher.removeOneScreen(screenIndex);
						}
						else
						{
							Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete,
									Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
						}
					}
					else
					{
						Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete,
								Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
					}
					break;
				case 6:
					if (frontCount == 0)
					{
						if (screenIndex == 3 || screenIndex == 4 || screenIndex == 5)
						{
							mLauncher.removeOneScreen(screenIndex);
						}
						else
						{
							Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete,
									Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
						}
					}
					else if (frontCount == 1)
					{
						if (screenIndex == 0 || screenIndex == 4 || screenIndex == 5)
						{
							mLauncher.removeOneScreen(screenIndex);
						}
						else
						{
							Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete,
									Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
						}
					}
					else if (frontCount == 2)
					{
						if (screenIndex == 0 || screenIndex == 1 || screenIndex == 5)
						{
							mLauncher.removeOneScreen(screenIndex);
						}
						else
						{
							Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete,
									Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
						}
					}
					else if (frontCount == 3)
					{
						if (screenIndex == 0 || screenIndex == 1 || screenIndex == 2)
						{
							mLauncher.removeOneScreen(screenIndex);
						}
						else
						{
							Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete,
									Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
						}
					}
					else
					{
						Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete,
								Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
					}
					break;
				case 7:
					if (frontCount == 1)
					{
						if (screenIndex == 0 || screenIndex == 4 || screenIndex == 5 || screenIndex == 6)
						{
							mLauncher.removeOneScreen(screenIndex);
						}
						else
						{
							Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete,
									Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
						}
					}
					else if (frontCount == 2)
					{
						if (screenIndex == 0 || screenIndex == 1 || screenIndex == 5 || screenIndex == 6)
						{
							mLauncher.removeOneScreen(screenIndex);
						}
						else
						{
							Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete,
									Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
						}
					}
					else if (frontCount == 3)
					{
						if (screenIndex == 0 || screenIndex == 1 || screenIndex == 2 || screenIndex == 6)
						{
							mLauncher.removeOneScreen(screenIndex);
						}
						else
						{
							Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete,
									Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
						}
					}
					else
					{
						Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete,
								Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
					}
					break;
				case 8:
					if (frontCount == 2)
					{
						if (screenIndex == 0 || screenIndex == 1 || screenIndex == 5 || screenIndex == 6 || screenIndex == 7)
						{
							mLauncher.removeOneScreen(screenIndex);
						}
						else
						{
							Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete,
									Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
						}
					}
					else if (frontCount == 3)
					{
						if (screenIndex == 0 || screenIndex == 1 || screenIndex == 2 || screenIndex == 6 || screenIndex == 7)
						{
							mLauncher.removeOneScreen(screenIndex);
						}
						else
						{
							Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete,
									Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
						}
					}
					else
					{
						Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete,
								Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
					}
					break;
				case 9:
					if (screenIndex == 0 || screenIndex == 1 || screenIndex == 2 || screenIndex == 6 || screenIndex == 7
							|| screenIndex == 8)
					{
						mLauncher.removeOneScreen(screenIndex);
					}
					else
					{
						Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete,
								Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
					}
					break;
				default:
					Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete, Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					break;
			}

			line1.removeAllViews();
			line2.removeAllViews();
			line3.removeAllViews();
			fillScreenCase3();
		}
		else
		{
			Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.screen_not_delete, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
	}

	private void addScreen()
	{
		int size = thumbnailList.size();
		if (size < 9)
		{
			switch (size)
			{
				case 5:
					mLauncher.addOneScreen(0);
					break;
				case 6:
					switch (Launcher.FRONT_SCREEN_COUNT)
					{
						case 0:
							mLauncher.addOneScreen(0);
							break;
						default:
							mLauncher.addOneScreen(-1);
							break;
					}
					break;
				case 7:
					switch (Launcher.FRONT_SCREEN_COUNT)
					{
						case 0:
						case 1:
							mLauncher.addOneScreen(0);
							break;
						case 2:
							mLauncher.addOneScreen(-1);
							break;
					}
					break;
				case 8:
					switch (Launcher.FRONT_SCREEN_COUNT)
					{
						case 0:
						case 1:
							mLauncher.addOneScreen(0);
							break;
						case 2:
							mLauncher.addOneScreen(-1);
							break;
					}
					break;
				default:
					break;
			}

			line1.removeAllViews();
			line2.removeAllViews();
			line3.removeAllViews();
			fillScreen();
		}
		else
		{
			Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.enough_screen, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}

	}

	private void addScreenCase3()
	{
		int size = thumbnailList.size();
		if (size < 9)
		{
			switch (size)
			{
				case 3:
					mLauncher.addOneScreen(0);
					break;
				case 4:
					switch (Launcher.FRONT_SCREEN_COUNT)
					{
						case 0:
							mLauncher.addOneScreen(0);
							break;
						default:
							mLauncher.addOneScreen(-1);
							break;
					}
					break;

				case 5:
					switch (Launcher.FRONT_SCREEN_COUNT)
					{
						case 0:
						case 1:
							mLauncher.addOneScreen(0);
							break;
						case 2:
							mLauncher.addOneScreen(-1);
							break;
					}
					break;
				case 6:
					switch (Launcher.FRONT_SCREEN_COUNT)
					{
						case 0:
						case 1:
							mLauncher.addOneScreen(0);
							break;
						case 2:
							mLauncher.addOneScreen(-1);
							break;
					}
					break;
				case 7:
					switch (Launcher.FRONT_SCREEN_COUNT)
					{
						case 1:
						case 2:
							mLauncher.addOneScreen(0);
							break;
						case 3:
							mLauncher.addOneScreen(-1);
							break;
					}
					break;
				case 8:
					switch (Launcher.FRONT_SCREEN_COUNT)
					{
						case 2:
							mLauncher.addOneScreen(0);
							break;
						case 3:
							mLauncher.addOneScreen(-1);
							break;
					}
					break;
				default:
					break;
			}

			line1.removeAllViews();
			line2.removeAllViews();
			line3.removeAllViews();
			fillScreenCase3();
		}
		else
		{
			Toast toast = Toast.makeText(ScreenSwitcherActivity.this, R.string.enough_screen, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}

	}

	private void addButtonListener()
	{

		add.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if (numCanNotDelete == 3)
				{
					addScreenCase3();
				}
				else
				{
					addScreen();
				}
			}
		});
	}

	private void fillScreen()
	{
		if (thumbnailList == null)
			return;

		final HomeScreenLayout mainLayout = (HomeScreenLayout) findViewById(R.id.homescreenlayout);

		int size = thumbnailList.size();
		if (size > 4)
		{
			for (int i = 0; i < size; i++)
			{
				final int j = i;
				ImageView imageView = new ImageView(this);
				imageView.setAdjustViewBounds(true);
				LayoutParams layoutParams = new LinearLayout.LayoutParams(VIEW_WIDTH, VIEW_HEIGHT);
				layoutParams.setMargins(5, 5, 5, 5);

				imageView.setLayoutParams(layoutParams);
				imageView.setImageBitmap(thumbnailList.get(i));
				imageView.setPadding(3, 5, 3, 5);
				// imageView.setBackgroundDrawable(resources.getDrawable(R.drawable.preview_background));
				// imageView.setBackgroundResource(R.drawable.preview_background);
				imageView.setBackgroundResource(R.drawable.screenswitcher_panel_background);

				imageView.setOnLongClickListener(new View.OnLongClickListener()
				{

					@Override
					public boolean onLongClick(View v)
					{
						finishFlag = false;
						screenIndex = j;
						mainLayout.setView(v, delete, add);
						mainLayout.startDragging();
						return false;
					}
				});

				imageView.setOnClickListener(new View.OnClickListener()
				{

					@Override
					public void onClick(View v)
					{
						if (finishFlag)
						{
							// gotoScreen(j);
							getIntent().putExtra("gotoIndex", j);
							finish();
						}
						else
						{
							finishFlag = true;
						}
					}
				});

				switch (size)
				{
					case 5:
						if (i == 0)
							addLine1View(imageView);
						else if (i == 1 || i == 2 || i == 3)
							addLine2View(imageView);
						else if (i == 4)
							addLine3View(imageView);
						break;
					case 6:
						if (Launcher.FRONT_SCREEN_COUNT == 0)
						{
							if (i == 0)
								addLine1View(imageView);
							else if (i == 1 || i == 2 || i == 3)
								addLine2View(imageView);
							else if (i == 4 || i == 5)
								addLine3View(imageView);
						}
						else
						{
							if (i == 0 || i == 1)
								addLine1View(imageView);
							else if (i == 2 || i == 3 || i == 4)
								addLine2View(imageView);
							else if (i == 5)
								addLine3View(imageView);
						}
						break;
					case 7:
						if (Launcher.FRONT_SCREEN_COUNT == 0)
						{
							if (i == 0)
								addLine1View(imageView);
							else if (i == 1 || i == 2 || i == 3)
								addLine2View(imageView);
							else if (i == 4 || i == 5 || i == 6)
								addLine3View(imageView);
						}
						else if (Launcher.FRONT_SCREEN_COUNT == 1)
						{
							if (i == 0 || i == 1)
								addLine1View(imageView);
							else if (i == 2 || i == 3 || i == 4)
								addLine2View(imageView);
							else if (i == 5 || i == 6)
								addLine3View(imageView);
						}
						else if (Launcher.FRONT_SCREEN_COUNT == 2)
						{
							if (i == 0 || i == 1 || i == 2)
								addLine1View(imageView);
							else if (i == 3 || i == 4 || i == 5)
								addLine2View(imageView);
							else if (i == 6)
								addLine3View(imageView);
						}
						break;
					case 8:
						if (Launcher.FRONT_SCREEN_COUNT == 1)
						{
							if (i == 0 || i == 1)
								addLine1View(imageView);
							else if (i == 2 || i == 3 || i == 4)
								addLine2View(imageView);
							else if (i == 5 || i == 6 || i == 7)
								addLine3View(imageView);
						}
						else if (Launcher.FRONT_SCREEN_COUNT == 2)
						{
							if (i == 0 || i == 1 || i == 2)
								addLine1View(imageView);
							else if (i == 3 || i == 4 || i == 5)
								addLine2View(imageView);
							else if (i == 6 || i == 7)
								addLine3View(imageView);
						}
						break;
					case 9:
						if (i == 0 || i == 1 || i == 2)
							addLine1View(imageView);
						else if (i == 3 || i == 4 || i == 5)
							addLine2View(imageView);
						else if (i == 6 || i == 7 || i == 8)
							addLine3View(imageView);
						break;
				}
			}
		}
	}

	private void fillScreenCase3()
	{
		if (thumbnailList == null)
			return;

		final HomeScreenLayout mainLayout = (HomeScreenLayout) findViewById(R.id.homescreenlayout);

		int size = thumbnailList.size();

		Launcher.FRONT_SCREEN_COUNT = (size - 3) % 2 == 0 ? (size - 3) / 2 : (size - 3) / 2 + 1;

		if (size > 2)
		{
			for (int i = 0; i < size; i++)
			{
				final int j = i;
				ImageView imageView = new ImageView(this);

				imageView.setAdjustViewBounds(false);
				
				imageView.setScaleType(ScaleType.CENTER_INSIDE);

				LayoutParams layoutParams = new LinearLayout.LayoutParams(VIEW_WIDTH, VIEW_HEIGHT);
//				layoutParams.setMargins(5, 7, 5, 0);
				
				imageView.setLayoutParams(layoutParams);
				// Bitmap bitmap=MyImg.createThumbtackImage(this,
				// thumbnailList.get(i));
				imageView.setImageBitmap(thumbnailList.get(i));
				// imageView.setBackgroundDrawable(resources.getDrawable(R.drawable.preview_background));
				// imageView.setBackgroundResource(R.drawable.preview_background);

				imageView.setOnLongClickListener(new View.OnLongClickListener()
				{

					@Override
					public boolean onLongClick(View v)
					{
						finishFlag = false;
						screenIndex = j;
						mainLayout.setView(v, delete, add);
						mainLayout.startDragging();
						return false;
					}
				});

				imageView.setOnClickListener(new View.OnClickListener()
				{

					@Override
					public void onClick(View v)
					{
						if (finishFlag)
						{
							// gotoScreen(j);
							getIntent().putExtra("gotoIndex", j);
							setResult(0, getIntent());
							finish();
						}
						else
						{
							finishFlag = true;
						}
					}
				});

				switch (size)
				{
					case 3:
						if (i == 0)
						{
							line1.addView(createEmptyImageView());
							line3.addView(createEmptyImageView());
						}
						imageView.setBackgroundResource(R.drawable.screenswitcher_panel_background);
						addLine2View(imageView);
						break;
					case 4:
						if (Launcher.FRONT_SCREEN_COUNT == 0)
						{
							if (i == 0)
							{
								line1.addView(createEmptyImageView());
							}
							if (i == 0 || i == 1 || i == 2)
							{
								addLine2View(imageView);
							}
							else
							{
								addLine3View(imageView);
							}
						}
						else
						{
							if (i == 0)
							{
								addLine1View(imageView);
								line3.addView(createEmptyImageView());
							}
							else
							{

								addLine2View(imageView);
							}
						}
						break;
					case 5:
						if (Launcher.FRONT_SCREEN_COUNT == 0)
						{
							if (i == 0)
							{
								line1.addView(createEmptyImageView());
							}
							if (i == 0 || i == 1 || i == 2)
							{
								addLine2View(imageView);
							}
							else if (i == 3 || i == 4)
							{
								addLine3View(imageView);
							}
						}
						else if (Launcher.FRONT_SCREEN_COUNT == 1)
						{
							if (i == 0)
							{
								addLine1View(imageView);
							}
							else if (i == 1 || i == 2 || i == 3)
							{
								addLine2View(imageView);
							}
							else if (i == 4)
							{
								addLine3View(imageView);
							}
						}
						else if (Launcher.FRONT_SCREEN_COUNT == 2)
						{
							if (i == 0)
							{
								line3.addView(createEmptyImageView());
							}
							if (i == 0 || i == 1)
								addLine1View(imageView);
							else if (i == 2 || i == 3 || i == 4)
							{
								addLine2View(imageView);
							}
						}
						break;
					case 6:
						if (Launcher.FRONT_SCREEN_COUNT == 0)
						{
							if (i == 0)
							{
								line1.addView(createEmptyImageView());
							}
							if (i == 0 || i == 1 || i == 2)
							{
								addLine2View(imageView);
							}
							else if (i == 3 || i == 4 || i == 5)
							{
								addLine3View(imageView);
							}
						}
						else if (Launcher.FRONT_SCREEN_COUNT == 1)
						{
							if (i == 0)
								addLine1View(imageView);
							else if (i == 1 || i == 2 || i == 3)
							{
								addLine2View(imageView);
							}
							else if (i == 4 || i == 5)
								addLine3View(imageView);
						}
						else if (Launcher.FRONT_SCREEN_COUNT == 2)
						{
							if (i == 0 || i == 1)
								addLine1View(imageView);
							else if (i == 2 || i == 3 || i == 4)
							{
								addLine2View(imageView);
							}
							else if (i == 5)
								addLine3View(imageView);
						}
						else if (Launcher.FRONT_SCREEN_COUNT == 3)
						{
							if (i == 0)
							{
								line3.addView(createEmptyImageView());
							}
							if (i == 0 || i == 1 || i == 2)
								addLine1View(imageView);
							else if (i == 3 || i == 4 || i == 5)
							{
								addLine2View(imageView);
							}
						}
						break;
					case 7:
						if (Launcher.FRONT_SCREEN_COUNT == 1)
						{
							if (i == 0)
								addLine1View(imageView);
							else if (i == 1 || i == 2 || i == 3)
							{
								addLine2View(imageView);
							}
							else if (i == 4 || i == 5 || i == 6)
								addLine3View(imageView);
						}
						else if (Launcher.FRONT_SCREEN_COUNT == 2)
						{
							if (i == 0 || i == 1)
								addLine1View(imageView);
							else if (i == 2 || i == 3 || i == 4)
								addLine2View(imageView);
							else if (i == 5 || i == 6)
								addLine3View(imageView);
						}
						else if (Launcher.FRONT_SCREEN_COUNT == 3)
						{
							if (i == 0 || i == 1 || i == 2)
								addLine1View(imageView);
							else if (i == 3 || i == 4 || i == 5)
								addLine2View(imageView);
							else if (i == 6)
								addLine3View(imageView);
						}
						break;
					case 8:
						if (Launcher.FRONT_SCREEN_COUNT == 2)
						{
							if (i == 0 || i == 1)
								addLine1View(imageView);
							else if (i == 2 || i == 3 || i == 4)
								addLine2View(imageView);
							else if (i == 5 || i == 6 || i == 7)
								addLine3View(imageView);
						}
						else if (Launcher.FRONT_SCREEN_COUNT == 3)
						{
							if (i == 0 || i == 1 || i == 2)
								addLine1View(imageView);
							else if (i == 3 || i == 4 || i == 5)
								addLine2View(imageView);
							else if (i == 6 || i == 7)
								addLine3View(imageView);
						}
						break;
					case 9:
						if (i == 0 || i == 1 || i == 2)
							addLine1View(imageView);
						else if (i == 3 || i == 4 || i == 5)
							addLine2View(imageView);
						else if (i == 6 || i == 7 || i == 8)
							addLine3View(imageView);
						break;
				}
			}
		}
	}
	
//	private void addLine2View(ImageView imageView){
//		imageView.setBackgroundResource(R.drawable.screenswitcher_panel_fixed_background);
//		imageView.setPadding(7, 8, 7, 4);

	private void addLine2View(ImageView imageView)
	{
		addThumbtack(imageView);
		line2.addView(imageView);
	}

	private void addThumbtack(ImageView imageView)
	{
		final Bitmap bitmap = Bitmap.createBitmap((int) 115, (int) 180, Bitmap.Config.ARGB_8888);
		final Canvas c = new Canvas(bitmap);
		Drawable drawable = imageView.getDrawable();
		drawable.setBounds(0, 8, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight() + 5);
		c.scale(0.95f, 0.95f);
		drawable.draw(c);
		final Bitmap bitmap1 = Bitmap.createBitmap((int) 115, (int) 180, Bitmap.Config.ARGB_8888);
		final Canvas c1 = new Canvas(bitmap1);
		c1.drawBitmap(bitmap, 0, 0, null);
		Drawable drawable2 = getResources().getDrawable(R.drawable.screenswitcher_lock_screen_title);
		drawable2.setBounds(0, 0, 118, drawable2.getIntrinsicHeight());
		drawable2.draw(c1);
		imageView.setImageBitmap(bitmap1);
	}

	private void formatImageSize(ImageView imageView)
	{

		final Bitmap result = Bitmap.createBitmap((int) 115, (int) 180, Bitmap.Config.ARGB_8888);
		final Canvas c = new Canvas(result);
		Drawable drawable = imageView.getDrawable();
		drawable.setBounds(0, 8, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight() + 5);
		c.scale(0.95f, 0.95f);
		drawable.draw(c);
		imageView.setImageBitmap(result);

		// return result;
	}

	private void addLine1View(ImageView imageView)
	{
		// imageView.setBackgroundResource(R.drawable.screenswitcher_panel_background);
		formatImageSize(imageView);
		line1.addView(imageView);
	}

	private void addLine3View(ImageView imageView)
	{
		// imageView.setBackgroundResource(R.drawable.screenswitcher_panel_background);
		formatImageSize(imageView);
		line3.addView(imageView);
	}

	private ImageView createEmptyImageView()
	{
		ImageView hide = new ImageView(this);
		hide.setBackgroundColor(Color.TRANSPARENT);
		LayoutParams hideLayoutParams = new LinearLayout.LayoutParams(VIEW_WIDTH, VIEW_HEIGHT);
		hideLayoutParams.setMargins(5, 7, 5, 0);
		hide.setLayoutParams(hideLayoutParams);
		return hide;
	}

	public static void initPic(ArrayList<Bitmap> thumbList)
	{
		thumbnailList = thumbList;
	}

	public static void setLauncher(Launcher launcher)
	{
		mLauncher = launcher;
	}

	// private void gotoScreen(int index)
	// {
	// mLauncher.setCurrentScreen(index);
	// }
}
