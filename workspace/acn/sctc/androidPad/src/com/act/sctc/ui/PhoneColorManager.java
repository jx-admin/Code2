package com.act.sctc.ui;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.act.sctc.App;
import com.act.sctc.R;
import com.act.sctc.db.DBHelper;
import com.act.sctc.util.Utils;
import com.custom.view.utils.BaseManager;
import com.custom.view.utils.ScrollerLinearMenu;

public class PhoneColorManager extends BaseManager implements OnCheckedChangeListener, OnItemClickListener,
		OnClickListener {
	private TextView info_tv_1, info_tv_2, info_tv_3, info_tv_4;
	private ImageView phone_img1, phone_img2, phone_img3, phone_img4;

	private ScrollerLinearMenu mScrollerLinearMenu;

	// private ProductPhone pp;
	private Cursor phoneColorCursor;
	// private List<PhoneColor> colors;
	private int currentColorIndex;
	MyColorAdapter mMyColorAdapter;

	public static PhoneColorManager getPhoneColorManager(Context context) {
		return new PhoneColorManager(LayoutInflater.from(context).inflate(R.layout.phone_baseinfo_page, null));
	}

	private PhoneColorManager(View layout) {
		super(layout);
		iniController();
		iniListener();
		iniVariable();
	}

	public void setPhoneColor(long pcId) {
		mScrollerLinearMenu.removeAllViews();
		this.phoneColorCursor = DBHelper
				.getInstance(context)
				.rawQuery(
						"select a._id,a.color,a.value,b.path,c.path,d.path,e.path from phone_color a left join resource b on a.[img1]=b.[_id] left join resource c on a.[img2]=c.[_id] left join resource d on a.[img3]=d.[_id] left join resource e on a.[img4]=e.[_id] where a.[phone_id]="
								+ pcId, null);
		// colors=pp.getColors();
		int size = mMyColorAdapter.getCount();
		LayoutParams lp = new LayoutParams(130, LayoutParams.WRAP_CONTENT);
		for (int i = 0; i < size; i++) {
			mScrollerLinearMenu.addView(mMyColorAdapter.getView(i, null, null), lp);
		}
		setColor(0);
		setInfoPoint(pcId);
	}

	public void setSelected(int index) {
		mScrollerLinearMenu.setSelected(index);
	}

	public View getLayout() {
		return layout;
	}

	private void iniController() {
		info_tv_1 = (TextView) layout.findViewById(R.id.info_tv_1);
		info_tv_2 = (TextView) layout.findViewById(R.id.info_tv_2);
		info_tv_3 = (TextView) layout.findViewById(R.id.info_tv_3);
		info_tv_4 = (TextView) layout.findViewById(R.id.info_tv_4);
		phone_img1 = (ImageView) layout.findViewById(R.id.phone_img1);
		phone_img2 = (ImageView) layout.findViewById(R.id.phone_img2);
		phone_img3 = (ImageView) layout.findViewById(R.id.phone_img3);
		phone_img4 = (ImageView) layout.findViewById(R.id.phone_img4);
		phone_img1.setOnClickListener(this);
		phone_img2.setOnClickListener(this);
		phone_img3.setOnClickListener(this);
		phone_img4.setOnClickListener(this);
		mMyColorAdapter = new MyColorAdapter();
		mScrollerLinearMenu = new ScrollerLinearMenu((ViewGroup) layout.findViewById(R.id.color_menulayout));
	}

	private void iniListener() {
		mScrollerLinearMenu.setOnItemClickListener(this);
	}

	private void iniVariable() {
	}

	private void setInfoPoint(long phoneId) {
		Cursor ad4Cursor = DBHelper.getInstance(context).rawQuery(
				"select a.ad_desc1,a.ad_desc2,a.ad_desc3,a.ad_desc4 from phone a where a.[_id]=" + phoneId, null);
		if (ad4Cursor != null) {
			if (ad4Cursor.moveToFirst()) {
				info_tv_1.setText(ad4Cursor.getString(0));
				info_tv_2.setText(ad4Cursor.getString(1));
				info_tv_3.setText(ad4Cursor.getString(2));
				info_tv_4.setText(ad4Cursor.getString(3));
			}
			ad4Cursor.close();
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub

	}

	private class MyColorAdapter extends BaseAdapter {
		// int curSelected = 0;
		// View curSelectView = null;

		@Override
		public int getCount() {
			return phoneColorCursor == null ? 0 : phoneColorCursor.getCount();
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
				convertView = LayoutInflater.from(context).inflate(R.layout.phone_color_btn_model2, null);
			}
			phoneColorCursor.moveToPosition(position);
			// PhoneColor pc=colors.get(position);
			TextView tv = (TextView) convertView.findViewById(R.id.name_color_tv);
			tv.setText(phoneColorCursor.getString(1));
			LinearLayout colorll = (LinearLayout) convertView.findViewById(R.id.bottom_color);
			String color = phoneColorCursor.getString(2);
			if (color != null && color.length() == 7) {
				int colorValue = (Integer.valueOf(color.substring(1), 16) | 0xFF000000);
				colorll.setBackgroundColor(colorValue);
			}
			convertView.setTag((Integer) position);
			// if(position==curSelected){
			// convertView.findViewById(R.id.selector_v).setVisibility(View.VISIBLE);
			// // convertView.setBackgroundResource(R.drawable.color_select_bk);
			// int []pics=pc.getPicIds()[position];
			// phone_img1.setImageResource(pics[0]);
			// phone_img2.setImageResource(pics[1]);
			// phone_img3.setImageResource(pics[2]);
			// phone_img4.setImageResource(pics[3]);
			// curSelectView=convertView;
			// }
			return convertView;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		Integer p = (Integer) v.getTag();
		setColor(p);
	}

	private void setColor(int position) {
		currentColorIndex = position;
		if (phoneColorCursor != null && phoneColorCursor.moveToPosition(position)) {
			Utils.LazyLoadImage(phone_img1, App.IMG_DIR + phoneColorCursor.getString(3));
			Utils.LazyLoadImage(phone_img2, App.IMG_DIR + phoneColorCursor.getString(4));
			Utils.LazyLoadImage(phone_img3, App.IMG_DIR + phoneColorCursor.getString(5));
			Utils.LazyLoadImage(phone_img4, App.IMG_DIR + phoneColorCursor.getString(6));
		}
	}

	@Override
	public void onClick(View v) {
		if (phoneColorCursor != null && phoneColorCursor.moveToPosition(currentColorIndex)) {
			switch (v.getId()) {
			case R.id.phone_img1:
				ImageBrowserActivity.start(context, App.IMG_DIR + phoneColorCursor.getString(3));
				break;
			case R.id.phone_img2:
				ImageBrowserActivity.start(context, App.IMG_DIR + phoneColorCursor.getString(4));
				break;
			case R.id.phone_img3:
				ImageBrowserActivity.start(context, App.IMG_DIR + phoneColorCursor.getString(5));
				break;
			case R.id.phone_img4:
				ImageBrowserActivity.start(context, App.IMG_DIR + phoneColorCursor.getString(6));
				break;
			}
		}
	}
}
