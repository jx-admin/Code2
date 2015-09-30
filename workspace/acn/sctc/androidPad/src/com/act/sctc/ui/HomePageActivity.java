package com.act.sctc.ui;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.act.sctc.App;
import com.act.sctc.BaseActivity;
import com.act.sctc.Constant;
import com.act.sctc.R;
import com.act.sctc.db.DBHelper;
import com.act.sctc.ui.TitleContentManager.DbRefreshListener;
import com.act.sctc.util.Logger;
import com.act.sctc.util.Utils;
import com.custom.view.utils.MenuScroller;
import com.custom.view.utils.PagerAdapterCursor;

public class HomePageActivity extends BaseActivity implements OnClickListener, DbRefreshListener {
	private MenuScroller mScrollerMenu;
	private TitleContentManager mContentManager;
	private ViewPager pages;
	private View hight_video_lin, fttx_lan_lin, smartphone_lin;
	private ArrayList<RadioButton> mViewsIndex;
	private MyHandler mHandler;
	private PicPagerCursorAdapter mPicPagerCursorAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_page);
		iniController();
		iniVariable();
		iniListener();
	}

	public void onResume() {
		super.onResume();
		mHandler.startPage(mHandler.getPosition() + 1);
	}

	public void onPause() {
		super.onPause();
		mHandler.stop();
	}

	private void iniController() {
		pages = (ViewPager) findViewById(R.id.pager);
		hight_video_lin = findViewById(R.id.hight_video_lin);
		fttx_lan_lin = findViewById(R.id.fttx_lan_lin);
		smartphone_lin = findViewById(R.id.smartphone_lin);
		mScrollerMenu = new MenuScroller((ViewGroup) findViewById(R.id.horizontalScrollView));
		mContentManager = new TitleContentManager(findViewById(R.id.title_content));
		mContentManager.setDbRefreshListener(this);
		mContentManager.setUpdateVisibility(View.VISIBLE);
		mContentManager.setShoppingCartEnabled(true);
		mViewsIndex = new ArrayList<RadioButton>();
		mHandler = new MyHandler();
	}

	private void iniListener() {
		hight_video_lin.setOnClickListener(this);
		fttx_lan_lin.setOnClickListener(this);
		smartphone_lin.setOnClickListener(this);
	}

	private void iniVariable() {
		mViewsIndex.clear();
		mScrollerMenu.removeAllViews();

		Cursor mCursor = DBHelper.getInstance(this).rawQuery(
				"select a._id,b.path from promotion a left join resource b on a.[img] = b.[_id] ", null);// getContentResolver().query(AppHomeColumn.CONTENT_URI,
																											// null,
																											// AppHomeColumn.TYPE+"="+AppHomeColumn.TYPE_BIG,
																											// null,
																											// null);
		LayoutInflater lif = LayoutInflater.from(this);
		for (int i = mCursor.getCount() - 1; i >= 0; i--) {
			RadioButton rb = (RadioButton) lif.inflate(R.layout.page_index_radiobutton_model, null);
			rb.setId(i);
			mViewsIndex.add(rb);
			mScrollerMenu.addView(rb);
		}
		if (mViewsIndex.size() > 0) {
			mViewsIndex.get(0).setChecked(true);
		}

		mPicPagerCursorAdapter = new PicPagerCursorAdapter(this, mCursor, true);
		pages.setAdapter(mPicPagerCursorAdapter);
		pages.setOnPageChangeListener(new MyPagerOnPageChangeListener());
		mHandler.position = 0;
		pages.setCurrentItem(mHandler.position);
		mHandler.startPage(1);

		mCursor = DBHelper.getInstance(this).rawQuery(
				"select b.path,a.name from business a left join resource b on a.[img] = b.[_id] ", null);// getContentResolver().query(BusinessColumn.CONTENT_URI,
																											// null,
																											// null,
																											// null,
																											// null);

		if (mCursor != null) {
			if (mCursor.moveToFirst()) {
				// home_small_video.setImageURI(Uri.parse(App.IMG_DIR+mCursor.getString(0)));
				((TextView) findViewById(R.id.home_small_video_title)).setText(mCursor.getString(1));
			}
			if (mCursor.moveToNext()) {
				// home_small_fttx_lan.setImageURI(Uri.parse(App.IMG_DIR+mCursor.getString(0)));
				((TextView) findViewById(R.id.home_small_fttx_lan_title)).setText(mCursor.getString(1));
			}

			if (mCursor.moveToNext()) {
				// home_small_smartphone.setImageURI(Uri.parse(App.IMG_DIR+mCursor.getString(0)));
				((TextView) findViewById(R.id.home_small_smartphone_title)).setText(mCursor.getString(1));
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.hight_video_lin:
			IntroActivity.start(this, Constant.TYPE_ITV);
			// Intent intent = new Intent(this, TvIntroActivity.class);
			// startActivity(intent);
			break;
		case R.id.fttx_lan_lin:
			IntroActivity.start(this, Constant.TYPE_BROADBAND);
			break;
		case R.id.smartphone_lin:
			IntroActivity.start(this, Constant.TYPE_PHONE);
			// intent = new Intent(this, PhoneProductListActivity.class);
			// intent = new Intent(this, PhoneIntroActivity.class);
			// startActivity(intent);
			break;
		default:
			SaleAdvertiseActivity.start(this, v.getId());
			break;
		}
	}

	private class PicPagerCursorAdapter extends PagerAdapterCursor {

		public PicPagerCursorAdapter(Context context, Cursor c, boolean autoRequery) {
			super(context, c, autoRequery);
		}

		@Override
		public int getCount() {
			return super.getCount();
		}

		public View getView(ViewGroup parent, View contentView, int position) {
			int index = position % super.getCount();
			return super.getView(parent, contentView, index);
		}

		@Override
		public Filter getFilter() {
			return null;
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			ImageView iv = new ImageView(HomePageActivity.this);
			iv.setScaleType(ScaleType.CENTER_CROP);
			iv.setOnClickListener(HomePageActivity.this);
			return iv;
		}

		@Override
		public void bindView(View contentView, Context context, Cursor cursor) {
			contentView.setId(cursor.getInt(0));
			((ImageView) contentView).setImageBitmap(Utils.getOptimizedBitmap(App.IMG_DIR + mCursor.getString(1)));

			// return contentView;
		}

	}

	/**
	 * ViewPager��PageChangeListener(ҳ��ı�ļ�����)
	 */
	@SuppressLint("NewApi")
	private class MyPagerOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		/**
		 * ����ViewPager��ʱ��,���Ϸ���HorizontalScrollView�Զ��л�
		 */
		@Override
		public void onPageSelected(int position) {
			position %= mViewsIndex.size();
			if (position < mViewsIndex.size()) {
				mViewsIndex.get(position).setChecked(true);
			}

			if (position != mHandler.getPosition()) {
				mHandler.startPage(position);
			}
		}
	}

	/**
	 * 接受消息,处理消息 ,此Handler会与当前主线程一块运行
	 * */

	class MyHandler extends Handler {
		public static final long DELAY = 3500;
		private int position = 0;

		public MyHandler() {
		}

		public MyHandler(Looper L) {
			super(L);
		}

		public int getPosition() {
			return position;
		}

		// 子类必须重写此方法,接受数据
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			// 此处可以更新UI
			int size = HomePageActivity.this.pages.getAdapter().getCount();
			if (size > 0 && msg.arg1 >= size) {
				msg.arg1 = msg.arg1 % size;
			}
			this.position = msg.arg1;
			startPage(position + 1);
			HomePageActivity.this.pages.setCurrentItem(msg.arg1, true);
		}

		public void startPage(int position) {
			this.removeMessages(1);
			Message newMsg = this.obtainMessage(1, position, 0);
			this.sendMessageDelayed(newMsg, DELAY);
		}

		public void stop() {
			this.removeMessages(1);
		}
	}

	@Override
	public void onDbRefreshed() {
		if (Logger.DEBUG) {
			Logger.debug("HomePageActivity.onDbRefreshed");
		}

		iniVariable();
	}

	long lastBackTime;
	final long DEYLAY_TIME = 2000;

	@Override
	public void onBackPressed() {
		if (System.currentTimeMillis() - lastBackTime > DEYLAY_TIME) {
			Toast.makeText(this, R.string.press_again_exit, Toast.LENGTH_SHORT).show();
			lastBackTime = System.currentTimeMillis();
		} else {
			finish();
			super.onBackPressed();
		}
	}

}
