package com.act.sctc.ui;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.LayoutParams;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.act.sctc.App;
import com.act.sctc.BaseActivity;
import com.act.sctc.Constant;
import com.act.sctc.R;
import com.act.sctc.been.ContractPhoneSetmeal;
import com.act.sctc.been.SetMeal;
import com.act.sctc.db.DBHelper;
import com.act.sctc.util.Logger;
import com.act.sctc.util.Utils;
import com.custom.view.utils.MenuScroller;

public class ProductDetailActivity extends BaseActivity implements OnClickListener, OnCheckedChangeListener {
	public static final String OBJ = "obj";
	private MenuScroller mScrollerMenu;
	private TitleContentManager mContentManager;
	// title
	private RadioGroup radioGroup;
	private ImageButton recommend_btn;
	private LinearLayout recommend_layout;

	// Content
	private ViewPager mViewPager; // �·��Ŀɺ����϶��Ŀؼ�
	private ArrayList<View> mViews;// ��������·�������layout(layout_1,layout_2,layout_3)

	public static int selectedPhoneId;
	private PhoneInfoMsgManager mPhoneInfoMsgManager;
	private TVInfoMsgManager mTVInfoMsgManager;
	private PackageInfoMsgManager mPackageInfoMsgManager;

	private int type;
	private long parmarId;
	private boolean selectOnly;

	private static final String TYPE = "type", ID = "id", SELECTONLY = "selectOnly";

	public static void start(Context context, int type, long id) {
		start(context, type, id, false);
	}

	public static void start(Context context, int type, long id, boolean selectOnly) {
		Intent intent = new Intent(context, ProductDetailActivity.class);
		intent.putExtra(TYPE, type);
		intent.putExtra(ID, id);
		intent.putExtra(SELECTONLY, selectOnly);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Logger.debug("ProductDetailActivity.onCreate IN");
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		type = intent.getIntExtra(TYPE, -1);
		parmarId = intent.getLongExtra(ID, -1);
		selectOnly = intent.getBooleanExtra(SELECTONLY, false);
		selectedPhoneId = 0;

		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
		setContentView(R.layout.phone_info_page);
		mContentManager = new TitleContentManager(findViewById(R.id.title_content));

		InitTask task = new InitTask();
		task.execute();
		Logger.debug("ProductDetailActivity.onCreate OUT");
	}

	private class InitTask extends AsyncTask<Object, Object, Object> {
		protected Object doInBackground(Object... params) {
			return null;
		}

		protected void onPostExecute(Object result) {
			mScrollerMenu = new MenuScroller((ViewGroup) findViewById(R.id.horizontalScrollView));
			mContentManager.setUpdateVisibility(View.GONE);

			iniController();
			iniListener();
			iniVariable();

			mViewPager.setCurrentItem(0);
		}
	}

	protected void onResume() {
		super.onResume();

		if (selectedPhoneId != 0) {
			if (type == Constant.TYPE_BROADBAND) {
				mPackageInfoMsgManager.selectPhone(selectedPhoneId);
			}
			selectedPhoneId = 0;
		}
	}

	private void iniVariable() {
		mViews = new ArrayList<View>();
		String sql = "select a.title,b.path from product_detail a left join resource b on a.[img_id] = b.[_id] where a.[business_id]="
				+ type;
		if (type == Constant.TYPE_PHONE) {
			addTabContent("基本信息", null, 0, 2);
			sql += " and a.[phone_id]=" + parmarId;
		}
		Cursor mCursor = DBHelper.getInstance(this).rawQuery(sql, null);
		if (mCursor != null && mCursor.moveToFirst()) {
			int count = mCursor.getCount();
			if (type == Constant.TYPE_PHONE) {
				count += 2;
				addTabContent("设备参数", null, 1, 3);
			}
			do {
				int position = mCursor.getPosition();
				addTabContent(mCursor.getString(0), mCursor.getString(1), position, count);
			} while (mCursor.moveToNext());
			radioGroup.check(0);
		} else {
			if (type == Constant.TYPE_PHONE) {
				addTabContent("设备参数", null, 1, 2);
			}
		}
		mViewPager.setAdapter(new MyPagerAdapter());// ����ViewPager��������
	}

	private void addTabContent(String title, String path, int position, int count) {
		GradientDrawable gd = new GradientDrawable();
		if (position == 0) {
			gd.setCornerRadii(new float[] { 10f, 10f, 0, 0, 0, 0, 0, 0 });
		} else if (position == count - 1) {
			gd.setCornerRadii(new float[] { 0, 0, 10f, 10f, 0, 0, 0, 0 });
		}
		if (App.tabColorIds.length > 0) {
			gd.setColor(getResources().getColor(App.tabColorIds[position % App.tabColorIds.length]));
		}
		RadioButton rbtn = (RadioButton) LayoutInflater.from(this).inflate(R.layout.info_radiobutton_model, null);
		rbtn.setBackgroundDrawable(gd);
		rbtn.setId(position);
		LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		radioGroup.addView(rbtn, layoutParams);
		rbtn.setText(title);

		if (path == null && type == Constant.TYPE_PHONE) {
			View view = mPhoneInfoMsgManager.getView(position);
			if (view != null) {
				mViews.add(view);
			}
		} else {
			View view = getLayoutInflater().inflate(R.layout.phone_introduce_page, null);
			mViews.add(view);
			ImageView imView = (ImageView) view.findViewById(R.id.imageView1);
			Utils.LazyLoadImage(imView, App.IMG_DIR + path);
		}
	}

	/**
	 * RadioGroup���CheckedChanged����
	 */
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		mViewPager.setCurrentItem(checkedId, true);// ���·�ViewPager���������HorizontalScrollView�л�
	}

	private void iniListener() {
		mViewPager.setOnPageChangeListener(new MyPagerOnPageChangeListener());
		mScrollerMenu.setOnCheckedChangeListener(this);
		recommend_btn.setOnClickListener(this);
	}

	boolean hasMeasured = false;

	private void iniController() {
		radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

		mViewPager = (ViewPager) findViewById(R.id.pager);

		recommend_btn = (ImageButton) findViewById(R.id.recommend_btn);

		recommend_layout = (LinearLayout) findViewById(R.id.recommend_layout);
		recommend_layout.setVisibility(View.GONE);

		android.widget.LinearLayout.LayoutParams lParams = new LayoutParams(
				android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
				android.widget.LinearLayout.LayoutParams.MATCH_PARENT);
		if (type == Constant.TYPE_PHONE) {
			mPhoneInfoMsgManager = new PhoneInfoMsgManager(this, (int) parmarId, selectOnly);
			mPhoneInfoMsgManager.onCreate();
			((ViewGroup) findViewById(R.id.info_content)).addView(mPhoneInfoMsgManager.getView(), lParams);
		} else if (type == Constant.TYPE_ITV) {
			mTVInfoMsgManager = new TVInfoMsgManager(this);
			((ViewGroup) findViewById(R.id.info_content)).addView(mTVInfoMsgManager.getView(), lParams);
		} else if (type == Constant.TYPE_BROADBAND) {
			mPackageInfoMsgManager = new PackageInfoMsgManager(this);
			((ViewGroup) findViewById(R.id.info_content)).addView(mPackageInfoMsgManager.getView(), lParams);
		}
	}

	/**
	 * ViewPager��������
	 */
	private class MyPagerAdapter extends PagerAdapter {

		@Override
		public void destroyItem(View v, int position, Object obj) {
			((ViewPager) v).removeView(mViews.get(position));
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public int getCount() {
			return mViews.size();
		}

		@Override
		public Object instantiateItem(View v, int position) {
			((ViewPager) v).addView(mViews.get(position));
			return mViews.get(position);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
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
			radioGroup.check(position);
		}
	}

	@Override
	public void onClick(View v) {
		if (v == recommend_btn) {
			// if (isShow) {
			// hideRecommend();
			// recommend_btn.setImageResource(R.drawable.recommend_1);
			// } else {
			// showRecommend();
			// recommend_btn.setImageResource(R.drawable.recommend_2);
			// }
		}
	}

	boolean isShow = false;

	private void showRecommend() {
		if (!isShow) {
			recommend_layout.setVisibility(View.VISIBLE);
			AnimationSet _AnimationSet = new AnimationSet(true);
			TranslateAnimation _TranslateAnimation;
			_TranslateAnimation = new TranslateAnimation(-recommend_layout.getWidth(), 0, 0f, 0f);
			_AnimationSet.addAnimation(_TranslateAnimation);
			_AnimationSet.setFillBefore(true);
			_AnimationSet.setFillAfter(true);
			_AnimationSet.setDuration(100);
			recommend_layout.startAnimation(_AnimationSet);
			isShow = !isShow;
		}
	}

	private void hideRecommend() {
		if (isShow) {
			AnimationSet _AnimationSet = new AnimationSet(true);
			TranslateAnimation _TranslateAnimation;
			_TranslateAnimation = new TranslateAnimation(recommend_layout.getLeft(), -recommend_layout.getWidth(), 0f,
					0f);
			_AnimationSet.addAnimation(_TranslateAnimation);
			_AnimationSet.setFillBefore(true);
			_AnimationSet.setFillAfter(true);
			_AnimationSet.setDuration(100);
			recommend_layout.startAnimation(_AnimationSet);
			isShow = !isShow;
		}
	}

	private SetMeal curSetMeal;
	private ContractPhoneSetmeal curContractPhoneSetmeal;

	// private static final int
	// SETMEAL_RESULT_CODE=10,CONTRACT_PHONE_SETMEAL_RESULT_CODE=11;
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("Result", requestCode + " " + resultCode + " " + (data == null ? "" : data.getIntExtra("data", 0)));
		if (resultCode != RESULT_OK) {
			super.onActivityResult(requestCode, resultCode, data);
			return;
		}
		if (type == Constant.TYPE_PHONE) {
			mPhoneInfoMsgManager.onActivityResult(requestCode, resultCode, data);
		} else if (type == Constant.TYPE_ITV) {
			mTVInfoMsgManager.onActivityResult(requestCode, resultCode, data);
		} else if (type == Constant.TYPE_BROADBAND) {
			mPackageInfoMsgManager.onActivityResult(requestCode, resultCode, data);
		}
		// 可以根据多个请求代码来作相应的操作
		// if(CONTRACT_PHONE_SETMEAL_RESULT_CODE==requestCode){
		// if (data!=null) {
		// curContractPhoneSetmeal=(ContractPhoneSetmeal)
		// data.getSerializableExtra("data");
		// //
		// select_setmeal_contract_phone_btn.setText(curContractPhoneSetmeal.getTitle());
		// }else{
		// //
		// select_setmeal_contract_phone_btn.setText(R.string.click_select_contract_phone_setmeal);
		// }
		// } else if(requestCode==SETMEAL_RESULT_CODE){
		// if(data!=null){
		// curSetMeal=(SetMeal) data.getSerializableExtra("data");
		// // select_setmeal_btn.setText(curSetMeal.getName());
		// }else{
		// // select_setmeal_btn.setText(R.string.click_select_setmeal);
		// }
		// }
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void finish() {
		if (curSetMeal != null) {
			curSetMeal.setStatus(ContractPhoneSetmeal.IN_MAKEY);
		}
		if (curContractPhoneSetmeal != null) {
			curContractPhoneSetmeal.setStatus(ContractPhoneSetmeal.IN_MAKEY);
		}
		super.finish();
	}

}
