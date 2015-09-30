package com.act.sctc.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.act.sctc.Constant;
import com.act.sctc.R;
import com.act.sctc.db.DBHelper;
import com.act.sctc.db.SqlUtils;
import com.act.sctc.util.Logger;

public class PhoneInfoMsgManager implements OnClickListener, OnCheckedChangeListener {
	private View view;
	private int phoneId;
	private boolean selectOnly;
	private int phonePackageId;
	private int phoneContractPackageId;
	private Context context;

	private PhoneColorManager mPhoneColorManager;
	private PhoneParameterManager mPhoneParameterManager;

	// data
	private Cursor phoneCursor;
	private int currentColor;
	private String contractDisc;

	private GridView colors_gv;
	private MyColorAdapter mMyColorAdapter = new MyColorAdapter();

	private TextView title_tv, subtitle_tv;
	private TextView select_setmeal_contract_phone_tv;
	private Button select_setmeal_contract_phone_btn, select_setmeal_btn;
	private TextView if_select_tv, if_select_contract_tv;
	private Switch if_select_contract_sth;
	private Button add_to_shopping_cart_btn;

	private int type = Constant.TYPE_PHONE;

	public PhoneInfoMsgManager(Context context, int phontId, boolean selectOnly) {
		this.phoneId = phontId;
		this.context = context;
		this.selectOnly = selectOnly;
		view = LayoutInflater.from(context).inflate(R.layout.phone_info_msg_content, null);
		phonePackageId = 0;
		phoneContractPackageId = 0;
	}

	public View getView() {
		return view;
	}

	public void onCreate() {
		mPhoneColorManager = PhoneColorManager.getPhoneColorManager(context);
		mPhoneColorManager.setPhoneColor(phoneId);

		mPhoneParameterManager = new PhoneParameterManager(LayoutInflater.from(context).inflate(
				R.layout.phone_parameter_page, null));
		mPhoneParameterManager.setPhoneId((int) phoneId);

		colors_gv = (GridView) view.findViewById(R.id.colors_gv);
		select_setmeal_contract_phone_btn = (Button) view.findViewById(R.id.select_setmeal_contract_phone_btn);
		select_setmeal_contract_phone_tv = (TextView) view.findViewById(R.id.select_setmeal_contract_phone_tv);
		select_setmeal_btn = (Button) view.findViewById(R.id.select_setmeal_btn);
		if_select_tv = (TextView) view.findViewById(R.id.if_select_tv);
		if_select_contract_tv = (TextView) view.findViewById(R.id.if_select_contract_tv);
		if_select_contract_sth = (Switch) view.findViewById(R.id.if_select_contract_sth);
		title_tv = (TextView) view.findViewById(R.id.title_tv);
		subtitle_tv = (TextView) view.findViewById(R.id.subtitle_tv);
		add_to_shopping_cart_btn = (Button) view.findViewById(R.id.add_to_shopping_cart_btn);

		phoneCursor = DBHelper.getInstance(context).rawQuery(
				"select a._id,a.ad_desc,a.brand,a.attr10 from phone a where a.[_id]=" + phoneId, null);
		Cursor phoneContractCursor = DBHelper
				.getInstance(context)
				.rawQuery(
						"select a._id,a.attr_name1 from phone_contract a inner join phone_contract_relation c on a.[_id]=c.[contract_id] where c.[phone_id]="
								+ phoneId + " limit 1", null);
		if (phoneContractCursor == null || !phoneContractCursor.moveToFirst()) {
			if_select_contract_sth.setVisibility(View.GONE);
			if_select_tv.setVisibility(View.GONE);
			if_select_contract_tv.setVisibility(View.GONE);
			select_setmeal_contract_phone_tv.setVisibility(View.GONE);
			select_setmeal_contract_phone_btn.setVisibility(View.GONE);
		} else if (phoneContractCursor.getCount() > 0) {
			if_select_contract_sth.setVisibility(View.GONE);
			if_select_tv.setVisibility(View.GONE);
			if_select_contract_tv.setVisibility(View.GONE);
		} else if (phoneContractCursor.getCount() == 0) {
			// TODO: need more investigation
			select_setmeal_contract_phone_tv.setVisibility(View.GONE);
			select_setmeal_contract_phone_btn.setVisibility(View.GONE);
			contractDisc = phoneContractCursor.getString(1);
			phoneContractPackageId = phoneContractCursor.getInt(0);
		}
		if (phoneCursor != null && phoneCursor.moveToFirst()) {
			title_tv.setText(phoneCursor.getString(1));
			subtitle_tv.setText(phoneCursor.getString(3));
		}
		add_to_shopping_cart_btn.setEnabled(!selectOnly);

		if (selectOnly) {
			view.findViewById(R.id.select_btn).setVisibility(View.VISIBLE);
			view.findViewById(R.id.textSetMeal).setVisibility(View.GONE);
			select_setmeal_btn.setVisibility(View.GONE);
			select_setmeal_contract_phone_tv.setVisibility(View.GONE);
			select_setmeal_contract_phone_btn.setVisibility(View.GONE);
			if_select_contract_sth.setVisibility(View.GONE);
			if_select_tv.setVisibility(View.GONE);
			if_select_contract_tv.setVisibility(View.GONE);
			add_to_shopping_cart_btn.setVisibility(View.GONE);
		} else {
			view.findViewById(R.id.select_btn).setVisibility(View.GONE);
		}
		iniListener();
	}

	private void iniListener() {
		select_setmeal_contract_phone_btn.setOnClickListener(this);
		select_setmeal_btn.setOnClickListener(this);
		if_select_contract_sth.setOnCheckedChangeListener(this);
		add_to_shopping_cart_btn.setOnClickListener(this);
		view.findViewById(R.id.select_btn).setOnClickListener(this);
	}

	public View getView(int poistion) {
		View view = null;
		if (poistion == 0) {
			view = mPhoneColorManager.getLayout();
			mMyColorAdapter.setPhoneId(phoneId);
			colors_gv.setAdapter(mMyColorAdapter);
		} else if (poistion == 1) {
			view = mPhoneParameterManager.getLayout();
		}
		return view;
	}

	@Override
	public void onClick(View v) {
		if (v == select_setmeal_btn) {
			PackageActivity.start(((Activity) context), type, Constant.CATEGORY_PHONE_PACKAGE, phonePackageId,
					SETMEAL_RESULT_CODE);
		} else if (v == select_setmeal_contract_phone_btn) {
			PhoneContractSetmealSelecorActivity.start(((Activity) context), type, Constant.CATEGORY_PHONE_CONTRACT,
					phoneContractPackageId, phoneId, CONTRACT_PHONE_SETMEAL_RESULT_CODE);
		} else if (v == add_to_shopping_cart_btn) {
			if (phoneId > 0) {
				ShoppingCarActivity.addGoods(0, 0, phoneId, Constant.CATEGORY_PHONE_PRODUCTLIST, 1,
						mMyColorAdapter.getSelectedColorName());
			}
			if (phonePackageId > 0) {
				ShoppingCarActivity.addGoods(0, 0, phonePackageId, Constant.CATEGORY_PHONE_PACKAGE, 1, "");
			}
			if (phoneContractPackageId > 0) {
				ShoppingCarActivity.addGoods(0, 0, phoneContractPackageId, Constant.CATEGORY_PHONE_CONTRACT, 1, "");
			}
		} else if (v.getId() == R.id.select_btn) {
			Activity activity = (Activity) context;
			activity.finish();
			ProductDetailActivity.selectedPhoneId = phoneId;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			if_select_contract_tv.setText(contractDisc);
		} else {
			if_select_contract_tv.setText("说明：只买手机");
		}

	}

	private static final int SETMEAL_RESULT_CODE = 10, CONTRACT_PHONE_SETMEAL_RESULT_CODE = 11;

	public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
		if (Logger.DEBUG) {
			Logger.debug("Result: " + requestCode + " " + resultCode + " "
					+ (data == null ? "" : data.getIntExtra("data", 0)));
		}
		if (resultCode != Activity.RESULT_OK) {
			return false;
		}

		// 可以根据多个请求代码来作相应的操作
		if (CONTRACT_PHONE_SETMEAL_RESULT_CODE == requestCode) {
			if (data != null) {
				phoneContractPackageId = data.getIntExtra("data", 0);
				if (phoneContractPackageId > 0) {
					Cursor cursor = SqlUtils.getPhonePackage(context, phoneContractPackageId);
					if (cursor != null && cursor.moveToFirst()) {
						select_setmeal_contract_phone_btn.setText(cursor.getString(1));
					}
					cursor.close();
				} else {
					select_setmeal_contract_phone_btn.setText(R.string.click_select_setmeal);
				}
			} else {
				select_setmeal_contract_phone_btn.setText(R.string.click_select_setmeal);
			}
		} else if (requestCode == SETMEAL_RESULT_CODE) {
			if (data != null) {
				phonePackageId = data.getIntExtra("data", -1);
				if (phonePackageId > 0) {
					Cursor cursor = SqlUtils.getPackage(context, phonePackageId);
					if (cursor != null && cursor.moveToFirst()) {
						select_setmeal_btn.setText(cursor.getString(1));
					}
					cursor.close();
				} else {
					select_setmeal_btn.setText(R.string.click_select_setmeal);
				}
			} else {
				select_setmeal_btn.setText(R.string.click_select_setmeal);
			}
		}
		add_to_shopping_cart_btn.setEnabled(!selectOnly);
		return true;
	}

	private class MyColorAdapter extends BaseAdapter implements OnClickListener {
		private int curSelected = -1;
		private String colorNames[];
		private int colors[];
		private View views[];

		public void setPhoneId(int id) {
			Cursor cursor = DBHelper.getInstance(context).rawQuery(
					"select a._id,a.color,a.value,a.img1,a.img2,a.img3,a.img4 from phone_color a where a.[phone_id]="
							+ id, null);
			if (cursor != null && cursor.moveToFirst()) {
				int count = cursor.getCount();
				colorNames = new String[count];
				colors = new int[count];
				views = new View[count];
				int index = 0;
				do {
					colorNames[index] = cursor.getString(1);
					String color = cursor.getString(2);
					if (color != null && color.length() == 7) {
						colors[index] = (Integer.valueOf(color.substring(1), 16) | 0xFF000000);
					}
					index++;
				} while (cursor.moveToNext());
				curSelected = 0;
			} else {
				colorNames = new String[0];
				colors = new int[0];
				views = new View[0];
			}

			if (cursor != null) {
				cursor.close();
			}
		}

		public String getSelectedColorName() {
			if (colorNames.length > currentColor) {
				return colorNames[currentColor];
			}
			return "";
		}

		@Override
		public int getCount() {
			return colorNames.length;
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
			Logger.debug("getView => " + position);
			TextView tv = null;
			convertView = views[position];
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(R.layout.phone_color_btn_model, null);

				convertView.setTag(position);
				tv = (TextView) convertView.findViewById(R.id.name_color_tv);
				tv.setText(colorNames[position]);
				LinearLayout colorll = (LinearLayout) convertView.findViewById(R.id.bottom_color);
				colorll.setBackgroundColor(colors[position]);
				convertView.setOnClickListener(this);
				views[position] = convertView;
			} else {
				tv = (TextView) convertView.findViewById(R.id.name_color_tv);
			}
			if (position == curSelected) {
				tv.setBackgroundColor(0xffDDEBF6);
				convertView.setBackgroundColor(0xff78CCEE);
			} else {
				convertView.setBackgroundColor(0);
				tv.setBackgroundColor(0x00000000);
			}

			return convertView;
		}

		@Override
		public void onClick(View v) {
			int pos = (Integer) v.getTag();

			if (curSelected == pos) {
				curSelected = -1;
			} else {
				curSelected = pos;
			}
			this.notifyDataSetChanged();
		}

	}
}
