package com.act.sctc.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.act.sctc.App;
import com.act.sctc.BaseActivity;
import com.act.sctc.Constant;
import com.act.sctc.R;
import com.act.sctc.db.DBHelper;
import com.act.sctc.util.Logger;
import com.act.sctc.util.Utils;
import com.custom.view.utils.CustomMediaController;
import com.custom.view.utils.LayoutSelector;

public class IntroActivity extends BaseActivity implements OnClickListener {
	public static final String TAG = IntroActivity.class.getSimpleName();
	private static final String TYPE = "TYPE";
	private static int tabFocuColorId = R.color.yellow;

	private int type;
	private CustomViewPagerAdapter customViewPagerAdapter = null;
	private List<Holder> arrayViewlist = new ArrayList<Holder>();
	private TitleContentManager mContentManager;
	LayoutSelector join_advertise_selector = new LayoutSelector();

	private ViewPager mViewPager;
	private ViewGroup tab_content;
	private TextView itv_feature1_text1, itv_feature1_text2;
	private ImageView tab_icon_iv;

	private int curSelect = -1;

	public static void start(Context context, int type) {
		Intent intent = new Intent(context, IntroActivity.class);
		intent.putExtra(TYPE, type);
		context.startActivity(intent);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		if (Logger.DEBUG) {
			Logger.debug("IntroActivity.onCreate IN");
		}

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phone_intro);
		type = getIntent().getIntExtra(TYPE, -1);

		InitTask task = new InitTask();
		task.execute();
	}

	private class InitTask extends AsyncTask<Object, Object, Object> {

		@Override
		protected Object doInBackground(Object... params) {
			return null;
		}

		@SuppressWarnings("deprecation")
		protected void onPostExecute(Object result) {
			tab_content = (ViewGroup) findViewById(R.id.tab_content);
			mViewPager = (ViewPager) findViewById(R.id.itv_intro_viewpager);
			join_advertise_selector.setLayout((ViewGroup) findViewById(R.id.join_advertise_selector_layout));
			join_advertise_selector.setOnclickListener(IntroActivity.this);
			mContentManager = new TitleContentManager(findViewById(R.id.include_layout));
			mContentManager.setUpdateVisibility(View.GONE);
			initViewPager();

			Cursor mCursor = DBHelper
					.getInstance(IntroActivity.this)
					.rawQuery(
							"select a.title,b.path,c.path,b.type,a.subtitle from business_detail a left join resource b on a.[resource_id] = b.[_id] left join resource c on a.[icon]=c.[_id] where a.[business_id]="
									+ type, null);
			if (mCursor == null || !mCursor.moveToFirst()) {
				Log.d(TAG, "null from db");
				return;
			} else {
				int i = 0;
				Resources resources = getResources();
				int tabFocusColor = (resources.getColor(tabFocuColorId)) & (0xb2ffffff);
				do {
					GradientDrawable gd = new GradientDrawable();
					gd.setCornerRadii(new float[] { 10f, 10f, 0, 0, 0, 0, 10f, 10f });
					if (App.tabColorIds.length > 0) {
						gd.setColor(resources.getColor(App.tabColorIds[i % App.tabColorIds.length]) & (0xb2ffffff));
					}

					GradientDrawable gd2 = new GradientDrawable();
					gd2.setCornerRadii(new float[] { 10f, 10f, 0, 0, 0, 0, 10f, 10f });
					gd2.setColor(tabFocusColor);

					StateListDrawable sld = new StateListDrawable();
					sld.addState(new int[] { android.R.attr.state_pressed, android.R.attr.state_enabled }, gd2);
					sld.addState(new int[] { android.R.attr.state_selected, android.R.attr.state_enabled }, gd2);
					sld.addState(new int[] {}, gd);

					// sd.addState(new int[]{android.R.attr.state_enabled,
					// android.R.attr.state_focused}, focus);
					// sd.addState(new int[]{android.R.attr.state_pressed,
					// android.R.attr.state_enabled}, pressed);
					// sd.addState(new int[]{android.R.attr.state_focused},
					// focus);
					// sd.addState(new int[]{android.R.attr.state_pressed},
					// pressed);
					// sd.addState(new int[]{android.R.attr.state_enabled},
					// normal);
					// sd.addState(new int[]{}, normal);

					View view = getLayoutInflater().inflate(R.layout.unit_home_tab_model, null);
					itv_feature1_text1 = (TextView) view.findViewById(R.id.itv_feature1_text1);
					itv_feature1_text2 = (TextView) view.findViewById(R.id.itv_feature1_text2);
					tab_icon_iv = (ImageView) view.findViewById(R.id.tab_icon_iv);
					tab_icon_iv.setImageURI(Uri.parse(App.IMG_DIR + mCursor.getString(2)));
					itv_feature1_text1.setText(mCursor.getString(0));
					itv_feature1_text2.setText(mCursor.getString(4));
					view.setBackgroundDrawable(sld);
					view.setId(i);
					// view.setBackgroundResource(gd);
					view.setOnClickListener(IntroActivity.this);
					LayoutParams lParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					lParams.topMargin = 10;
					lParams.bottomMargin = R.dimen.margin_xsmall;
					view.setLayoutParams(lParams);
					tab_content.addView(view, lParams);

					Holder holder = new Holder();
					holder.tabBtn = view;
					int picType = mCursor.getInt(3);
					if (picType == 1) {
						View iv4 = getLayoutInflater().inflate(R.layout.itv_feature_5_pic_layout, null);
						VideoView video_view = (VideoView) iv4.findViewById(R.id.video_view);
						video_view.setVideoURI(Uri.parse(App.VIDEO_DIR + mCursor.getString(1)));
						CustomMediaController mCustomMediaController = new CustomMediaController(
								iv4.findViewById(R.id.video_layout));
						mCustomMediaController.setVideoPlayer(video_view);
						mCustomMediaController.show();
						holder.view = iv4;
						holder.videoView = video_view;
						arrayViewlist.add(holder);
					} else {
						ImageView iv0 = new ImageView(IntroActivity.this);
						Utils.LazyLoadImage(iv0, App.IMG_DIR + mCursor.getString(1));
						holder.view = iv0;
						arrayViewlist.add(holder);
					}
					i++;
				} while (mCursor.moveToNext());
				curSelect = 0;
				Holder holder = arrayViewlist.get(0);
				holder.tabBtn.setSelected(true);
				if (holder.videoView != null && !holder.videoView.isPlaying()) {
					holder.videoView.start();
				}
			}
			mCursor.close();
		}
	}

	class Holder {
		View view;
		VideoView videoView;
		View tabBtn;
	}

	public void onPause() {
		super.onPause();
	}

	public void onResume() {
		super.onResume();
	}

	// initial the slide show
	public void initViewPager() {
		customViewPagerAdapter = new CustomViewPagerAdapter();
		mViewPager.setAdapter(customViewPagerAdapter);
		mViewPager.setCurrentItem(0);

		AnimationSet as = new AnimationSet(true);
		AlphaAnimation aa = new AlphaAnimation(0.1f, 1f);
		aa.setFillAfter(true);
		aa.setDuration(1000);
		as.addAnimation(aa);

		mViewPager.setAnimation(as);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				Holder holder;
				if (curSelect >= 0) {
					holder = arrayViewlist.get(curSelect);
					holder.tabBtn.setSelected(false);
					if (holder.videoView != null && holder.videoView.isPlaying()) {
						holder.videoView.pause();
					}
				}
				curSelect = arg0;
				holder = arrayViewlist.get(curSelect);
				holder.tabBtn.setSelected(true);
				if (holder.videoView != null && !holder.videoView.isPlaying()) {
					holder.videoView.start();
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

	}

	class CustomViewPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {

			return arrayViewlist.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {

			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(arrayViewlist.get(position).view);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(arrayViewlist.get(position).view, 0);
			return arrayViewlist.get(position).view;
		}

	}

	@Override
	public void onClick(View v) {
		if (v == join_advertise_selector.getView()) {
			switch (type) {
			case Constant.TYPE_ITV:
				ProductDetailActivity.start(this, Constant.TYPE_ITV, -1);
				break;
			case Constant.TYPE_BROADBAND:
				ProductDetailActivity.start(this, Constant.TYPE_BROADBAND, -1);
				break;
			case Constant.TYPE_PHONE:
				PhoneProductListActivity.start(this, 0);
				break;

			default:
				break;
			}
		} else {
			Holder holder;
			if (curSelect >= 0) {
				holder = arrayViewlist.get(curSelect);
				holder.tabBtn.setSelected(false);
				if (holder.videoView != null && holder.videoView.isPlaying()) {
					holder.videoView.pause();
				}
			}
			curSelect = v.getId();
			holder = arrayViewlist.get(curSelect);
			holder.tabBtn.setSelected(true);
			if (holder.videoView != null && !holder.videoView.isPlaying()) {
				holder.videoView.start();
			}
			mViewPager.setCurrentItem(curSelect);
		}
	}

}
