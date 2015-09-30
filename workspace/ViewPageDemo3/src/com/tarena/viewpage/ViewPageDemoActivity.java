package com.tarena.viewpage;

import java.util.ArrayList;
import java.util.List;

import android.R.anim;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ViewPageDemoActivity extends Activity {

	ViewPager pager;
	LayoutInflater inflater;
	private ImageView mCursor;
	private Bitmap mCursorImg;
	private int mOffset;
	private int mCursorImgWidth;
	private int mCurrentIndex = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		initTitle();
		initImageView();
		initPager();
	}

	private void initImageView() {
		mCursor = (ImageView) findViewById(R.id.cursor);
		
		mCursorImg = BitmapFactory.decodeResource(getResources(), R.drawable.a);
		mCursorImgWidth = mCursorImg.getWidth();

		/*
		 * 获取分辨率宽度
		 */
		// DisplayMetrics dm = new DisplayMetrics();
		// getWindowManager().getDefaultDisplay().getMetrics(dm);
		// int widthPixels = dm.widthPixels;
		// int offset = (widthPixels/3 - cursorImgWidth) / 2;
		//
		// Matrix matrix = new Matrix();
		// matrix.postTranslate(offset, 0);
		// mCursor.setImageMatrix(matrix);

		// test2:不使用分辨率宽度

		int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		mOffset = (screenWidth / 3 - mCursorImgWidth) / 2;

		Matrix matrix = new Matrix();
		matrix.postTranslate(mOffset, 0);
		mCursor.setImageMatrix(matrix);
	}

	private void initPager() {
		pager = (ViewPager) this.findViewById(R.id.view_pager);
		inflater = LayoutInflater.from(this);
		List<View> Views = new ArrayList<View>();
		View View1 = inflater.inflate(R.layout.layout1, null);
		View View2 = inflater.inflate(R.layout.layout2, null);
		View View3 = inflater.inflate(R.layout.layout3, null);
		Views.add(View1);
		Views.add(View2);
		Views.add(View3);

		ViewPagAdapter adapter = new ViewPagAdapter(this, Views);
		pager.setAdapter(adapter);
		pager.setCurrentItem(300);

		Button button = (Button) View2.findViewById(R.id.button1);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(ViewPageDemoActivity.this, "我是第二个布局的按钮", 3000)
						.show();
			}
		});

		Button back1 = (Button) View3.findViewById(R.id.back1);
		back1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pager.setCurrentItem(0);
			}
		});
		
		pager.setOnPageChangeListener(new ViewPageChangeListener());
	}

	private void initTitle() {
		TextView title1 = (TextView) findViewById(R.id.title1);
		TextView title2 = (TextView) findViewById(R.id.title2);
		TextView title3 = (TextView) findViewById(R.id.title3);

		title1.setOnClickListener(new PageClickerListener(0));
		title2.setOnClickListener(new PageClickerListener(1));
		title3.setOnClickListener(new PageClickerListener(2));
	}

	class PageClickerListener implements OnClickListener {

		private int index;

		public PageClickerListener(int position) {
			this.index = position;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			pager.setCurrentItem(index);
			mCurrentIndex = index;
		}

	}

	class ViewPageChangeListener implements OnPageChangeListener {

		int offsetOne = mOffset * 2 + mCursorImgWidth;
		int offsetTwo = offsetOne * 2;
		Animation animation = null;

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			if (arg0 >2) {
				arg0 = arg0 % 3;
			}
			switch (arg0) {
			case 0:
				if (mCurrentIndex == 1) {//即从1--->0
					animation = new TranslateAnimation(offsetOne, 0, 0, 0);
				} else if (mCurrentIndex == 2) {
					animation = new TranslateAnimation(offsetTwo, 0, 0, 0);
				}
				break;
			case 1:
				if (mCurrentIndex == 0) {
					animation = new TranslateAnimation(mOffset, offsetOne, 0, 0);
				} else if (mCurrentIndex == 2) {
					animation = new TranslateAnimation(offsetTwo, offsetOne, 0,
							0);
				}
				break;
			case 2:
				if (mCurrentIndex == 0) {
					animation = new TranslateAnimation(mOffset, offsetTwo, 0, 0);
				} else if (mCurrentIndex == 1) {
					animation = new TranslateAnimation(offsetOne, offsetTwo, 0,
							0);
				}
				break;
			}
			
			mCurrentIndex = arg0;
			animation.setFillAfter(true);//true：图片停在动画结束位置
			animation.setDuration(500);
			mCursor.startAnimation(animation);

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

	}

	
	
	// viewPager的适配器
	public class ViewPagAdapter extends PagerAdapter {

		private List<View> mListViews;
		Context mcContext;
		private int mCount;

		public ViewPagAdapter(Context context, List<View> views) {
			mListViews = views;
			mcContext = context;
			mCount = views.size();
		}

		@Override
		public void destroyItem(View collection, int position, Object arg2) {
//			if (position > 0) {
//				((ViewPager) collection).removeView(mListViews.get(position));
//			}
		}

		@Override
		public void finishUpdate(View arg0) {

		}

		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}

		@Override
		public Object instantiateItem(View collection, int position) {
			try {
				((ViewPager) collection).addView(mListViews.get(position % mCount), 0);
			} catch (Exception e) {
			}
			return mListViews.get(position % mCount);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == (object);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}

	}
}