package com.act.sctc.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.act.sctc.BaseActivity;
import com.act.sctc.Constant;
import com.act.sctc.R;
import com.act.sctc.db.DBHelper;
import com.act.sctc.db.PackageColumn;
import com.act.sctc.db.PhoneContractColumn;
import com.act.sctc.db.SqlUtils;
import com.act.sctc.util.Logger;
import com.custom.view.CustomPopu;
import com.custom.view.utils.PagerBaseAdapter;
import com.custom.view.utils.VirtulScrollerMenu;
import com.custom.view.utils.VirtulScrollerMenu.OnItemSelectedListener;

public class PackageActivity extends BaseActivity implements OnDismissListener, OnItemClickListener, OnClickListener,
		OnItemSelectedListener, com.act.sctc.ui.SetMealItem.OnCheckedChangeListener {
	private TitleContentManager mContentManager;
	private ViewPager mViewPager;
	private TextView result_count_tv, page_count_tv;
	protected MyPagerAdapter mPagerAdapter;
	// menu
	private SparseIntArray otherFilterItemMap = new SparseIntArray();
	private LinearLayout menu_content;
	private Button customer_categery;
	ListView lv;
	PriceAdapter pa;
	CustomPopu popuWin;
	int drowMenuWidth = 150;
	protected int categoryId;
	protected int phoneId;
	protected int type;
	protected int selectedId;
	protected SetMealItem selectedView;

	// data
	Cursor pCursor;
	public static final String ID = "id";
	public static final String PHONE_ID = "phone_id";
	public static final String TYPE = "type";
	public static final String CATEGORY_ID = "categoryId";

	public static void start(Activity context, int type, int categoryId, int id, int resultCode) {
		Intent intent = new Intent(context, PackageActivity.class);
		intent.putExtra(TYPE, type);
		intent.putExtra(CATEGORY_ID, categoryId);
		intent.putExtra(ID, id);
		context.startActivityForResult(intent, resultCode);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setmeal_page);
		Intent intent = getIntent();
		type = intent.getIntExtra(TYPE, 0);
		categoryId = intent.getIntExtra(CATEGORY_ID, 0);
		phoneId = intent.getIntExtra(PHONE_ID, 0);
		selectedId = intent.getIntExtra(ID, 0);
		new InitTask().execute();
	}

	private class InitTask extends AsyncTask<Object, Object, Object> {

		protected Object doInBackground(Object... params) {
			return null;
		}

		protected void onPostExecute(Object result) {
			iniController();
			iniListener();
			iniVariable();
		}
	}

	protected void iniController() {
		mPagerAdapter = new MyPagerAdapter();
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mContentManager = new TitleContentManager(findViewById(R.id.title_content));
		mContentManager.setUpdateVisibility(View.GONE);
		result_count_tv = (TextView) findViewById(R.id.result_count_tv);
		page_count_tv = (TextView) findViewById(R.id.page_count_tv);
		menu_content = (LinearLayout) findViewById(R.id.menu_content);
		// drowmenu
		popuWin = new CustomPopu(this);
		popuWin.setOutsideTouchable(true);
		popuWin.setFocusable(true);
		popuWin.setOnDismissListener(this);
		// popuWin.setOnClickListener(this);
		lv = (ListView) LayoutInflater.from(this).inflate(R.layout.drown_menu_list_model, null);
		pa = new PriceAdapter(this);
		lv.setAdapter(pa);
		popuWin.setContentView(lv);
		lv.setOnItemClickListener(this);

		// int[] position = new int[2];
		// rbtn.getLocationInWindow(position);
		popuWin.setWidth(drowMenuWidth);
		popuWin.setHeight(LayoutParams.WRAP_CONTENT);
		Drawable bk = getResources().getDrawable(R.drawable.donw_dialog_bk);
		popuWin.setBackgroundDrawable(bk);
	}

	private void iniListener() {
	}

	private void iniVariable() {
		if (Logger.DEBUG) {
			Logger.debug("iniVariable IN");
		}
		{
			Cursor menuCursor = SqlUtils.getFilter(this, categoryId);
			if (menuCursor != null) {
				if (menuCursor.moveToFirst()) {
					otherFilterItemMap.clear();
					;
					LayoutParams weiLp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					if (categoryId == Constant.CATEGORY_PHONE_PACKAGE) {
						// product menu 1
						customer_categery = new Button(this);
						customer_categery.setBackgroundResource(R.drawable.yellow_radius_bk);
						customer_categery.setText(menuCursor.getString(1));
						customer_categery.setId(menuCursor.getInt(0));
						setSelectFalse(customer_categery, false);
						menu_content.addView(customer_categery, 0, weiLp);
						customer_categery.setOnClickListener(this);
						menuCursor.moveToNext();
					}
					if (menuCursor.getPosition() < menuCursor.getCount()) {
						LinearLayout menu_content_2 = (LinearLayout) findViewById(R.id.menu_content_2);
						LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
						LayoutParams weight_lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
						weight_lp.weight = 1;
						TextView groupMenuTitleTv = (TextView) LayoutInflater.from(this).inflate(
								R.layout.setmeal_menu_left_title, null);
						groupMenuTitleTv.setText(menuCursor.getString(1));
						menu_content_2.addView(groupMenuTitleTv, lp);

						RelativeLayout menu_group = (RelativeLayout) LayoutInflater.from(this).inflate(
								R.layout.divider_linear, null);
						LinearLayout menu_group_content = (LinearLayout) menu_group.findViewById(R.id.menu_content);
						ImageView im1 = (ImageView) menu_group.findViewById(R.id.img1);
						menu_content_2.addView(menu_group, weight_lp);
						VirtulScrollerMenu mVirtulMenuScroller = new VirtulScrollerMenu();
						mVirtulMenuScroller.setScrollerView(im1);
						mVirtulMenuScroller.setOnItemSelectedListener(this);
						mVirtulMenuScroller.obj = menuCursor.getInt(0);

						int firstGroupItemCount = 1;
						Cursor itemCursor = SqlUtils.getItem(this, menuCursor.getInt(0));
						if (itemCursor != null) {
							firstGroupItemCount = itemCursor.getCount() + 2;
							if (itemCursor.moveToFirst()) {
								RadioButton rb = (RadioButton) LayoutInflater.from(this).inflate(
										R.layout.menu_item_model, null);
								rb.setText(R.string.all);
								menu_group_content.addView(rb, weight_lp);
								mVirtulMenuScroller.addItem(rb);
								do {
									rb = (RadioButton) LayoutInflater.from(this)
											.inflate(R.layout.menu_item_model, null);
									rb.setText(itemCursor.getString(1));
									rb.setId(itemCursor.getInt(0));
									menu_group_content.addView(rb, weight_lp);
									mVirtulMenuScroller.addItem(rb);
								} while (itemCursor.moveToNext());
							}

							itemCursor.close();
						}

						while (menuCursor.moveToNext()) {
							groupMenuTitleTv = (TextView) LayoutInflater.from(this).inflate(
									R.layout.setmeal_menu_mid_title, null);
							groupMenuTitleTv.setText(menuCursor.getString(1));
							menu_content_2.addView(groupMenuTitleTv, lp);

							menu_group = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.divider_linear,
									null);
							menu_group_content = (LinearLayout) menu_group.findViewById(R.id.menu_content);
							im1 = (ImageView) menu_group.findViewById(R.id.img1);
							LayoutParams new_weight_lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
									LayoutParams.MATCH_PARENT);
							new_weight_lp.weight = 1;
							menu_content_2.addView(menu_group, new_weight_lp);
							mVirtulMenuScroller = new VirtulScrollerMenu();
							mVirtulMenuScroller.setScrollerView(im1);
							mVirtulMenuScroller.setOnItemSelectedListener(this);
							mVirtulMenuScroller.obj = menuCursor.getInt(0);

							itemCursor = SqlUtils.getItem(this, menuCursor.getInt(0));
							if (itemCursor != null) {
								int currentGroupItemCount = itemCursor.getCount() + 2;
								float new_weight = 1.0f + (firstGroupItemCount - currentGroupItemCount) * 0.75f
										/ firstGroupItemCount;
								if (new_weight < 0.5f) {
									new_weight = 0.5f;
								}
								if (new_weight > 1.5f) {
									new_weight = 1.5f;
								}
								new_weight_lp.weight = new_weight;

								if (itemCursor.moveToFirst()) {
									RadioButton rb = (RadioButton) LayoutInflater.from(this).inflate(
											R.layout.menu_item_model, null);
									rb.setText(R.string.all);
									menu_group_content.addView(rb, weight_lp);
									mVirtulMenuScroller.addItem(rb);
									do {
										rb = (RadioButton) LayoutInflater.from(this).inflate(R.layout.menu_item_model,
												null);
										rb.setText(itemCursor.getString(1));
										rb.setId(itemCursor.getInt(0));
										menu_group_content.addView(rb, weight_lp);
										mVirtulMenuScroller.addItem(rb);
									} while (itemCursor.moveToNext());
								}

								itemCursor.close();
							}
						}
					}

				}
				menuCursor.close();
			}

			updatePager();
		}

		AnimationSet as = new AnimationSet(true);
		AlphaAnimation aa = new AlphaAnimation(0.1f, 1f);
		aa.setFillAfter(true);
		aa.setDuration(1000);
		as.addAnimation(aa);

		mViewPager.setAnimation(as);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				setPageIndex(mViewPager.getAdapter().getCount(), arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});

		if (Logger.DEBUG) {
			Logger.debug("iniVariable OUT");
		}
	}

	public void setResultCount(int count) {
		result_count_tv.setText(getString(R.string.setmeal_product_result_fomate, count));
	}

	public void setPageIndex(int pageCount, int current) {
		page_count_tv.setText(getString(R.string.setmeal_page_index_fomate, current + 1, pageCount));
	}

	protected class MyPagerAdapter extends PagerBaseAdapter {// PagerAdapter {
		protected final static double PER_PAGE_COUNT = 4;

		// @Override
		// public void destroyItem(View v, int position, Object obj) {
		// if (position < pages.size())
		// ((ViewPager) v).removeView(pages.get(position));
		// }

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public int getCount() {
			return pCursor == null ? 0 : (int) Math.ceil(pCursor.getCount() / PER_PAGE_COUNT);
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

		@Override
		public View getView(ViewGroup parent, View contentView, int position) {
			SetmealPageUtils spu;
			if (contentView == null) {
				spu = new SetmealPageUtils(categoryId, PackageActivity.this);
				contentView = spu.getView();
				contentView.setTag(spu);
				spu.setOnCheckedChangeListener(PackageActivity.this);
			} else {
				spu = (SetmealPageUtils) contentView.getTag();
			}
			position = (int) (position * PER_PAGE_COUNT);
			for (int i = 0; i < PER_PAGE_COUNT; i++, position++) {
				if (pCursor.moveToPosition(position)) {
					boolean selected = (selectedId == pCursor.getInt(0));
					if (selected) {
						selectedView = spu.ppItems[i];
					}
					spu.ppItems[i].setData(selected, pCursor.getInt(0), pCursor.getString(1), pCursor.getString(16),
							pCursor.getString(17), pCursor.getString(4), pCursor.getString(5), pCursor.getString(6),
							pCursor.getString(7), pCursor.getString(8), pCursor.getString(9), pCursor.getString(10),
							pCursor.getString(11), pCursor.getString(12), pCursor.getString(13), pCursor.getString(14),
							pCursor.getString(15));
					spu.ppItems[i].setVisibility(View.VISIBLE);
				} else {
					spu.ppItems[i].setVisibility(View.GONE);
				}
			}
			return contentView;
		}

	}

	class PriceAdapter extends BaseAdapter {
		int filteId;
		Cursor mCursor;
		private Context context;

		public PriceAdapter(Context context) {
			this.context = context;
		}

		public void setDatas(Cursor mCursor) {
			this.mCursor = mCursor;
		}

		@Override
		public int getCount() {
			return mCursor == null ? 0 : mCursor.getCount() + 1;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = new TextView(context);
				convertView.setPadding(0, 10, 0, 10);
				((TextView) convertView).setGravity(Gravity.CENTER);
				((TextView) convertView).setTextColor(Color.WHITE);
				convertView.setLayoutParams(new android.widget.AbsListView.LayoutParams(
						android.view.ViewGroup.LayoutParams.MATCH_PARENT,
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
			}
			if (position == 0) {
				((TextView) convertView).setText(R.string.all);
				convertView.setId(0);
			} else {
				mCursor.moveToPosition(position - 1);
				((TextView) convertView).setText(mCursor.getString(1));
				convertView.setId(mCursor.getInt(0));
			}
			return convertView;
		}

		public Cursor getDatas() {
			return mCursor;
		}

	}

	@Override
	public void onClick(View v) {
		if (v == customer_categery) {
			setSelectFalse(customer_categery, true);
			pa.filteId = v.getId();
			Cursor chiCursor = SqlUtils.getItem(this, v.getId());
			if (chiCursor != null && chiCursor.moveToFirst()) {
				pa.setDatas(chiCursor);
				pa.notifyDataSetChanged();
				popuWin.showAsDropDown(customer_categery, +customer_categery.getWidth() / 2 - drowMenuWidth / 2, 0);
			}
		}
	}

	@Override
	public void onDismiss() {
		setSelectFalse(customer_categery, false);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		if (view.getId() < 1) {
			if (otherFilterItemMap.get(pa.filteId) != 0) {
				otherFilterItemMap.delete(pa.filteId);
			}
		} else {
			otherFilterItemMap.put(pa.filteId, view.getId());
		}
		updatePager();
		popuWin.dismiss();
		Cursor cursor = pa.getDatas();
		if (cursor != null) {
			cursor.close();
		}

		// setShowData();
	}

	public void setSelectFalse(TextView view, boolean focus) {
		if (focus) {
			Resources resources = view.getResources();
			Drawable downDrawable = resources.getDrawable(R.drawable.down_menu_arrow_up);
			downDrawable.setBounds(0, 0, 7, 6);
			(view).setCompoundDrawables(null, null, null, downDrawable);
		} else {
			Resources resources = view.getResources();
			Drawable downDrawable = resources.getDrawable(R.drawable.down_menu_arrow_donw);
			downDrawable.setBounds(0, 0, 7, 6);
			(view).setCompoundDrawables(null, null, null, downDrawable);
		}
	}

	@Override
	public void onCheckedChanged(SetMealItem view, int id, boolean checked) {
		SetmealPageUtils spu = (SetmealPageUtils) ((View) view.getParent()).getTag();
		for (SetMealItem item : spu.ppItems) {
			if (item == null) {
				continue;
			}
			if (item.getDataId() != id) {
				item.setChecked(false);
			}
		}

		if (checked) {
			// if (this.selectedView != null) {
			// if (this.selectedView.getDataId() != id) {
			// this.selectedView.setChecked(false);
			// }
			// }
			this.selectedView = view;
			selectedId = id;
		} else {
			selectedId = -1;
			view = null;
		}

		// mPagerAdapter.notifyDataSetChanged();
	}

	@Override
	public void finish() {
		Intent intent = getIntent();
		intent.putExtra("data", selectedId);
		setResult(Activity.RESULT_OK, intent);

		super.finish();
	}

	@Override
	public void onSelected(VirtulScrollerMenu virtulMenuScroller, View view) {
		if (view.getId() < 1) {
			if (otherFilterItemMap.get((Integer) virtulMenuScroller.obj) != 0) {
				otherFilterItemMap.delete((Integer) virtulMenuScroller.obj);
			}
		} else {
			otherFilterItemMap.put((Integer) virtulMenuScroller.obj, view.getId());
		}
		updatePager();
	}

	protected String getSqlByObjectItemRelation(String tableName, int type, SparseIntArray map) {
		Integer value = null;
		StringBuilder creator = new StringBuilder();
		int length = map.size();
		creator.append(" where a.[business_id]=").append(type);
		for (int i = 0; i < length; i++) {
			value = map.valueAt(i);
			if (i == 0) {
				creator.insert(0, " inner join item_object_relation b on a.[_id]=b.[object_id]");
				creator.append(" and b.[item_id]=").append(value);
			} else {
				creator.insert(0, ".[object_id] ").insert(0, i).insert(0, " on a.[_id]=b").insert(0, i)
						.insert(0, " inner join item_object_relation b");
				creator.append(" and b").append(i).append(".[item_id]=").append(value);
			}
		}
		creator.insert(0, " a ");
		creator.insert(0, tableName);
		creator.insert(
				0,
				"select a._id,a.name,a.business_id,a.price,a.attr_name1,a.attr_name2,a.attr_name3,a.attr_name4,a.attr_name5,a.attr_name6,a.attr_value1,a.attr_value2,a.attr_value3,a.attr_value4,a.attr_value5,a.attr_value6,a.vs_name,a.vs_value from ");
		creator.append(";");
		String str = creator.toString();
		if (Logger.DEBUG) {
			Logger.debug(str);
		}
		return str;
	}

	private void updatePager() {
		if (Logger.DEBUG) {
			Logger.debug("updatePager");
		}
		if (pCursor != null) {
			pCursor.close();
		}
		if (categoryId == Constant.CATEGORY_PHONE_CONTRACT) {
			pCursor = DBHelper.getInstance(this).rawQuery(
					getSqlByObjectItemRelation(PhoneContractColumn.TABLE_NAME, type, otherFilterItemMap), null);
		} else {
			pCursor = DBHelper.getInstance(this).rawQuery(
					getSqlByObjectItemRelation(PackageColumn.TABLE_NAME, type, otherFilterItemMap), null);
		}
		mViewPager.setAdapter(mPagerAdapter);
		setResultCount(pCursor.getCount());
		setPageIndex(mPagerAdapter.getCount(), mViewPager.getCurrentItem());
	}
}
