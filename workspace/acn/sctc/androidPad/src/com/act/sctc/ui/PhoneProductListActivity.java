package com.act.sctc.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.act.sctc.App;
import com.act.sctc.BaseActivity;
import com.act.sctc.R;
import com.act.sctc.been.Phone;
import com.act.sctc.db.DBHelper;
import com.act.sctc.db.PhoneColumn;
import com.act.sctc.db.PhoneLocalColumn;
import com.act.sctc.db.SqlUtils;
import com.act.sctc.ui.PhoneProductItemUtils.OnTextChangeListener;
import com.act.sctc.ui.PhoneProductItemUtils.OncheckedListener;
import com.act.sctc.util.Logger;
import com.custom.view.CustomPopu;
import com.custom.view.utils.MenuBottomArrow;
import com.custom.view.utils.PagerBaseAdapter;

public class PhoneProductListActivity extends BaseActivity implements OncheckedListener, OnItemClickListener,
		com.custom.view.utils.MenuLinearManager.OnItemClickListener, OnClickListener, OnDismissListener,
		OnTextChangeListener {
	// title
	private TitleContentManager mContentManager;
	private boolean isEditModel;

	// menu price ratio btn
	private ArrayList<String> priceRatioGoods;
	private ViewGroup menu_price_ratio_btnlin;
	private TextView menu_item_price_ratio_left, menu_item_price_ratio_right;
	// menu
	private LinearLayout menu_content;
	private Button sctc_contract_phones_btn, locoal_phones_btn;
	private MenuBottomArrow mCustemGroupView;
	private TextView result_count_tv, page_count_tv;
	private ViewPager mViewPager;
	private MyPagerAdapter mPagerAdapter;

	// menu
	ListView lv;
	PriceAdapter pa;
	CustomPopu popuWin;
	int drowMenuWidth = 150;
	private int productFilterItem;
	private Map<Integer, Integer> otherFilterItemMap;

	// data
	private boolean selectOnly;
	Cursor pCursor;
	private Map<Long, Phone> localPricePhones;

	private static final String FILTERID = "filterId";
	private static final String SELECTONLY = "selectOnly";

	public static void start(Activity activity, long filterId) {
		start(activity, filterId, false);
	}

	public static void start(Activity activity, long filterId, boolean selectOnly) {
		Intent intent = new Intent(activity, PhoneProductListActivity.class);
		intent.putExtra(FILTERID, filterId);
		intent.putExtra(SELECTONLY, selectOnly);
		activity.startActivity(intent);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		selectOnly = this.getIntent().getBooleanExtra(SELECTONLY, false);
		setContentView(R.layout.phone_product_page);
		iniController();
		iniListener();
		iniVariable();
	}

	protected void onResume() {
		super.onResume();

		if (selectOnly && ProductDetailActivity.selectedPhoneId != 0) {
			finish();
		}
	}

	private void iniController() {
		menu_price_ratio_btnlin = (ViewGroup) findViewById(R.id.menu_price_ratio_btnlin);
		menu_item_price_ratio_left = (TextView) findViewById(R.id.menu_item_price_ratio_left);
		menu_item_price_ratio_right = (TextView) findViewById(R.id.menu_item_price_ratio_right);
		menu_price_ratio_btnlin.setOnClickListener(this);
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mCustemGroupView = new MenuBottomArrow((ViewGroup) findViewById(R.id.radioGroup2));
		menu_content = (LinearLayout) findViewById(R.id.menu_content);
		mContentManager = new TitleContentManager(findViewById(R.id.title_content));
		mContentManager.setUpdateVisibility(View.GONE);
		result_count_tv = (TextView) findViewById(R.id.result_count_tv);
		page_count_tv = (TextView) findViewById(R.id.page_count_tv);
		// drowmenu
		popuWin = new CustomPopu(this);
		popuWin.setOutsideTouchable(true);
		popuWin.setFocusable(true);
		popuWin.setOnDismissListener(this);
		lv = (ListView) LayoutInflater.from(this).inflate(R.layout.drown_menu_list_model, null);
		pa = new PriceAdapter(this);
		lv.setAdapter(pa);
		popuWin.setContentView(lv);
		lv.setOnItemClickListener(this);

		popuWin.setWidth(drowMenuWidth);
		popuWin.setHeight(LayoutParams.WRAP_CONTENT);
		Drawable bk = getResources().getDrawable(R.drawable.donw_dialog_bk);
		popuWin.setBackgroundDrawable(bk);
	}

	private void iniListener() {
		mCustemGroupView.setOnItemClickListener(this);
		mContentManager.addSettingBtn(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isEditModel = !isEditModel;
				if (isEditModel) {
					if (localPricePhones == null) {
						localPricePhones = new HashMap<Long, Phone>();
					}
				} else {
					Set set = localPricePhones.keySet();
					ContentResolver mContentResolver = getContentResolver();
					for (Object key : set) {
						Phone phone = localPricePhones.get(key);
						ContentValues contentValues = new ContentValues();
						contentValues.put(PhoneLocalColumn._ID, phone.getId());
						contentValues.put(PhoneLocalColumn.PRICE, phone.getLocalPrice());
						contentValues.put(PhoneLocalColumn.VISIBILITY, phone.isVisibility());
						mContentResolver.delete(
								ContentUris.withAppendedId(PhoneLocalColumn.CONTENT_URI, phone.getId()), null, null);
						mContentResolver.insert(PhoneLocalColumn.CONTENT_URI, contentValues);
					}

				}
				updateDate();
				mViewPager.setAdapter(mPagerAdapter);
			}
		}, R.drawable.setting_selector);
	}

	private void initMenu() {
		Cursor menuCursor = SqlUtils.getFilter(this, 303);
		if (menuCursor != null) {
			if (menuCursor.moveToFirst()) {

				LayoutParams weiLp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				weiLp.weight = 1;
				// product menu 1
				sctc_contract_phones_btn = new Button(this);
				sctc_contract_phones_btn.setBackgroundResource(R.drawable.phone_product_active1_selector);
				sctc_contract_phones_btn.setText(menuCursor.getString(1));
				sctc_contract_phones_btn.setId(menuCursor.getInt(0));
				menu_content.addView(sctc_contract_phones_btn, weiLp);
				sctc_contract_phones_btn.setOnClickListener(this);

				// product menu 2
				menuCursor.moveToNext();
				LayoutParams weiLp2 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				weiLp2.weight = 1;
				locoal_phones_btn = new Button(this);
				locoal_phones_btn.setBackgroundResource(R.drawable.phone_product_active1_selector);
				locoal_phones_btn.setId(menuCursor.getInt(0));
				locoal_phones_btn.setText(menuCursor.getString(1));
				weiLp2.leftMargin = getResources().getDimensionPixelOffset(R.dimen.margin_xsmall);
				menu_content.addView(locoal_phones_btn, weiLp2);
				locoal_phones_btn.setOnClickListener(this);

				// other filter menu
				otherFilterItemMap = new HashMap<Integer, Integer>();
				if (menuCursor.getCount() - 2 == 1) {
					menuCursor.moveToNext();
					GradientDrawable gd = new GradientDrawable();
					gd.setCornerRadii(new float[] { 10f, 10f, 10f, 10f, 0, 0, 0, 0 });
					int i = 0;
					Button btn = (Button) LayoutInflater.from(this).inflate(R.layout.phone_product_menu_item_model,
							null);
					btn.setBackgroundDrawable(gd);
					gd.setColor(getResources().getColor(App.tabColorIds[i % App.tabColorIds.length]));
					btn.setTextColor(Color.WHITE);
					btn.setText(menuCursor.getString(1));
					btn.setId(menuCursor.getInt(0));
					mCustemGroupView.addView(btn, weiLp);
				} else if (menuCursor.getCount() - 2 > 1) {
					int i = 0;
					Button btn = (Button) LayoutInflater.from(this).inflate(R.layout.phone_product_menu_item_model,
							null);
					GradientDrawable gd = new GradientDrawable();
					gd.setCornerRadii(new float[] { 10f, 10f, 0, 0, 0, 0, 0, 0 });
					gd.setColor(getResources().getColor(App.tabColorIds[i % App.tabColorIds.length]));
					btn.setBackgroundDrawable(gd);
					btn.setTextColor(Color.WHITE);
					btn.setText(R.string.all);

					Resources resources = this.getResources();
					ColorDrawable cDrawable = new ColorDrawable(0x00ffffff);
					cDrawable.setBounds(0, 0, 7, 6);
					btn.setCompoundDrawables(null, null, null, cDrawable);

					btn.setId(0);
					mCustemGroupView.addView(btn, weiLp);

					while (menuCursor.moveToNext()) {
						i++;
						btn = (Button) LayoutInflater.from(this).inflate(R.layout.phone_product_menu_item_model, null);
						btn.setTextColor(Color.WHITE);
						btn.setText(menuCursor.getString(1));
						btn.setId(menuCursor.getInt(0));
						gd = new GradientDrawable();
						if (menuCursor.getPosition() == menuCursor.getCount() - 1) {
							gd.setCornerRadii(new float[] { 0, 0, 10f, 10f, 0, 0, 0, 0 });
						}
						gd.setColor(getResources().getColor(App.tabColorIds[i % App.tabColorIds.length]));
						btn.setBackgroundDrawable(gd);
						mCustemGroupView.addView(btn, weiLp);
					}
				}
			}
			menuCursor.close();
		}
	}

	private void iniVariable() {
		initMenu();
		mPagerAdapter = new MyPagerAdapter();
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				setPageIndex(mViewPager.getAdapter().getCount(), arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

		sctc_contract_phones_btn.performClick();
		priceRatioGoods = new ArrayList<String>();
		invalidatePriceRatioButton();
	}

	public void setResultCount(int count) {
		result_count_tv.setText(getString(R.string.setmeal_product_result_fomate, count));
	}

	public void setPageIndex(int pageCount, int current) {
		page_count_tv.setText(getString(R.string.setmeal_page_index_fomate, current + 1, pageCount));
	}

	private class MyPagerAdapter extends PagerBaseAdapter {
		private final static double PER_PAGE_COUNT = 6;

		@Override
		public void finishUpdate(View arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return pCursor == null ? 0 : (int) Math.ceil(pCursor.getCount() / PER_PAGE_COUNT);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
			// TODO Auto-generated method stub
		}

		@Override
		public Parcelable saveState() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public View getView(ViewGroup parent, View contentView, int position) {
			PhoneProductPageUtils spu;
			if (contentView == null) {
				spu = new PhoneProductPageUtils(PhoneProductListActivity.this);
				spu.setOncheckedListener(PhoneProductListActivity.this);
				contentView = spu.getView();
				contentView.setTag(spu);
			} else {
				spu = (PhoneProductPageUtils) contentView.getTag();
			}
			position = (int) (position * PER_PAGE_COUNT);
			for (int i = 0; i < PER_PAGE_COUNT; i++, position++) {
				if (pCursor.moveToPosition(position)) {
					double price = pCursor.getDouble(8);
					if (price <= 0) {
						price = pCursor.getDouble(5);
					}
					spu.ppItems[i].setData(pCursor.getLong(0), pCursor.getString(1),
							Uri.parse(App.IMG_DIR + pCursor.getString(2)), pCursor.getDouble(4), price,
							pCursor.getInt(3) != 0, isEditModel, pCursor.getInt(9) != 0, selectOnly);
					spu.ppItems[i].setVisibility(View.VISIBLE);
					spu.ppItems[i].setOnTextChangeListener(PhoneProductListActivity.this);
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
			// TODO Auto-generated method stub
			return mCursor == null ? 0 : mCursor.getCount() + 1;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
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
						android.view.ViewGroup.LayoutParams.FILL_PARENT,
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
			// TODO Auto-generated method stub
			return mCursor;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (view.getId() < 1) {
			if (otherFilterItemMap.containsKey(pa.filteId)) {
				otherFilterItemMap.remove(pa.filteId);
			}
		} else {
			otherFilterItemMap.put(pa.filteId, view.getId());
		}
		updateDate();
		popuWin.dismiss();
		Cursor cursor = pa.getDatas();
		if (cursor != null) {
			cursor.close();
		}
	}

	@Override
	public void onItemClick(ViewGroup layout, View childView) {
		if (childView.getId() < 1) {
			otherFilterItemMap.clear();
			// getSqlByObjectItemRelation(PhoneColumn.TABLE_NAME,otherFilterItemMap,productFilterItem);

			updateDate();
		} else {
			pa.filteId = childView.getId();
			Cursor chiCursor = SqlUtils.getItem(this, childView.getId());
			if (chiCursor != null && chiCursor.moveToFirst()) {
				pa.setDatas(chiCursor);
				pa.notifyDataSetChanged();
				// int[] loc = new int[2];
				// lv.getLocationOnScreen(loc);
				// popuWin.getWidth();
				// popuWin.showAsDropDown(rbtn);
				// popuWin.showAtLocation(rbtn, Gravity.LEFT |
				// Gravity.TOP,position[0]-rbtn.getWidth()/2,position[1]+rbtn.getHeight());
				// popuWin.showPopu(rooView, 0, 0,
				// position[0]-rbtn.getWidth()/2,position[1]+rbtn.getHeight());
				popuWin.showAsDropDown(childView, +childView.getWidth() / 2 - drowMenuWidth / 2, 0);
			}
		}
	}

	@Override
	public void onClick(View v) {
		if (v == menu_price_ratio_btnlin) {
			if (priceRatioGoods != null && priceRatioGoods.size() > 0) {
				String[] ids = new String[priceRatioGoods.size()];
				PhoneComparePage.start(this, priceRatioGoods.toArray(ids), phoneCompareCode);
			}
		} else if (v == sctc_contract_phones_btn) {
			sctc_contract_phones_btn.setActivated(true);
			locoal_phones_btn.setActivated(false);
			Cursor chiCursor = SqlUtils.getItem(this, v.getId());
			if (chiCursor != null && chiCursor.moveToFirst()) {
				productFilterItem = chiCursor.getInt(0);
				chiCursor.close();
			}

			updateDate();
		} else if (v == locoal_phones_btn) {
			sctc_contract_phones_btn.setActivated(false);
			locoal_phones_btn.setActivated(true);
			Cursor chiCursor = SqlUtils.getItem(this, v.getId());
			if (chiCursor != null && chiCursor.moveToFirst()) {
				productFilterItem = chiCursor.getInt(0);
				chiCursor.close();
			}
			updateDate();
		}
	}

	public void invalidatePriceRatioButton() {
		if (priceRatioGoods == null || priceRatioGoods.size() <= 0) {
			menu_item_price_ratio_right.setText("0");
			menu_item_price_ratio_left.setBackgroundResource(R.drawable.phone_product_menu_bk_price_ratio_left_disable);
			menu_item_price_ratio_right
					.setBackgroundResource(R.drawable.phone_product_menu_bk_price_ratio_right_disable);
		} else if (priceRatioGoods.size() >= 3) {
			menu_item_price_ratio_left.setBackgroundResource(R.drawable.phone_product_menu_bk_price_ratio_left);
			menu_item_price_ratio_right.setBackgroundResource(R.drawable.phone_product_menu_bk_price_ratio_right);
			menu_item_price_ratio_right.setText(R.string.full);
		} else {
			menu_item_price_ratio_left.setBackgroundResource(R.drawable.phone_product_menu_bk_price_ratio_left);
			menu_item_price_ratio_right.setBackgroundResource(R.drawable.phone_product_menu_bk_price_ratio_right);
			menu_item_price_ratio_right.setText("" + priceRatioGoods.size());
		}
	}

	@Override
	public void onDismiss() {
		mCustemGroupView.setSelectFalse(false);
	}

	@Override
	public void onChecked(boolean check, View v, long phoneId) {
		if (isEditModel) {
			Phone phone = localPricePhones.get(phoneId);
			if (phone == null) {
				phone = new Phone();
				localPricePhones.put(phoneId, phone);
			}
			phone.setId(phoneId);
			phone.setVisibility(check);
		} else {
			if (check) {
				priceRatioGoods.add(Long.toString(phoneId));
			} else {
				priceRatioGoods.remove(Long.toString(phoneId));
			}
			invalidatePriceRatioButton();
		}
	}

	@Override
	public boolean getEnable() {
		return (priceRatioGoods == null || priceRatioGoods.size() < 3);
	}

	private static final String getSqlByObjectItemRelation(String tableName, Map<Integer, Integer> map,
			int productFilterItem, boolean isShowHide) {
		Integer[] keys = map.keySet().toArray(new Integer[0]);
		Integer value = null;
		StringBuilder creator = new StringBuilder();
		int length = keys.length;
		creator.append(
				" inner join item_object_relation b on a.[_id]=b.[object_id] left join phone_local l on a.[_id]=l.[_id] where b.[item_id]=")
				.append(productFilterItem);
		if (!isShowHide) {
			creator.append(" and  (l.[visibility] is null or l.[visibility] =0)");
		}
		for (int i = 0; i < length; i++) {
			value = map.get(keys[i]);
			creator.insert(0, ".[object_id] ").insert(0, i).insert(0, " on a.[_id]=b").insert(0, i)
					.insert(0, " inner join item_object_relation b");
			creator.append(" and b").append(i).append(".[item_id]=").append(value);
		}
		creator.insert(0, " a inner join resource r on a.[thumbnail]=r.[_id]");
		creator.insert(0, tableName);
		creator.insert(0,
				"select  a._id,a.ad_desc,r.path,a.sale_icon,a.price,a.sale_price,a.brand,a.attr10,l.price,l.visibility from ");
		creator.append(";");
		String str = creator.toString();
		if (Logger.DEBUG) {
			Logger.debug(str);
		}

		return str;
	}

	private void updateDate() {
		if (pCursor != null) {
			pCursor.close();
		}
		pCursor = DBHelper.getInstance(this).rawQuery(
				getSqlByObjectItemRelation(PhoneColumn.TABLE_NAME, otherFilterItemMap, productFilterItem, isEditModel),
				null);
		mViewPager.setAdapter(mPagerAdapter);
		setResultCount(pCursor.getCount());
		setPageIndex(mPagerAdapter.getCount(), mViewPager.getCurrentItem());
	}

	@Override
	public void onBackPressed() {
		if (isEditModel) {
			isEditModel = false;
			updateDate();
			mViewPager.setAdapter(mPagerAdapter);
		} else {
			super.onBackPressed();
		}
	}

	private final int phoneCompareCode = 11;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && requestCode == phoneCompareCode) {
			if (priceRatioGoods != null) {
				priceRatioGoods.clear();
				invalidatePriceRatioButton();
				mViewPager.setAdapter(mPagerAdapter);
			}
		}
	}

	@Override
	public void afterTextChanged(PhoneProductItemUtils mPhoneProductItemUtils, EditText mEditText) {
		long phoneId = mPhoneProductItemUtils.getPhoneId();
		Phone phone = localPricePhones.get(phoneId);
		if (phone == null) {
			phone = new Phone();
			localPricePhones.put(phoneId, phone);
		}
		phone.setId(phoneId);
		try {
			phone.setLocalPrice(Double.parseDouble(mEditText.getText().toString()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
