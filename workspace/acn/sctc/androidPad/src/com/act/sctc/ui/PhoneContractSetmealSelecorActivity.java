package com.act.sctc.ui;

import com.act.sctc.util.Logger;

import android.app.Activity;
import android.content.Intent;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;

public class PhoneContractSetmealSelecorActivity extends PackageActivity {

	public static void start(Activity context, int type, int categoryId, long id, int phoneId, int resultCode) {
		Intent intent = new Intent(context, PhoneContractSetmealSelecorActivity.class);
		intent.putExtra(TYPE, type);
		intent.putExtra(CATEGORY_ID, categoryId);
		intent.putExtra(ID, id);
		intent.putExtra(PHONE_ID, phoneId);
		context.startActivityForResult(intent, resultCode);
	}

	protected void iniController() {
		super.iniController();
		mPagerAdapter = new MyPagerAdapter();
	}

	@Override
	public void finish() {
		super.finish();
		Intent intent = getIntent();
		intent.putExtra("data", selectedId);
		setResult(Activity.RESULT_OK, intent);

	}

	protected String getSqlByObjectItemRelation(String tableName, int type, SparseIntArray map) {
		Integer value = null;
		StringBuilder creator = new StringBuilder();
		int length = map.size();
		creator.append(" where c.[phone_id]=").append(phoneId);
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
		creator.insert(0, " a inner join phone_contract_relation c on c.[contract_id]=a.[_id] ");
		creator.insert(0, tableName);
		creator.insert(
				0,
				"select a._id,a.name,a.type,a.price,a.attr_name1,a.attr_name2,a.attr_name3,a.attr_name4,a.attr_name5,a.attr_name6,a.attr_value1,a.attr_value2,a.attr_value3,a.attr_value4,a.attr_value5,a.attr_value6,null,null from ");
		creator.append(";");
		String str = creator.toString();
		if (Logger.DEBUG) {
			Logger.debug(str);
		}
		return str;
	}

	protected class MyPagerAdapter extends PackageActivity.MyPagerAdapter {// PagerAdapter
																			// {

		@Override
		public View getView(ViewGroup parent, View contentView, int position) {
			SetmealPageUtils spu;
			if (contentView == null) {
				spu = new SetmealPageUtils(categoryId, PhoneContractSetmealSelecorActivity.this);
				contentView = spu.getView();
				spu.setOnCheckedChangeListener(PhoneContractSetmealSelecorActivity.this);
			} else {
				spu = (SetmealPageUtils) contentView.getTag();
			}
			position = (int) (position * PER_PAGE_COUNT);
			for (int i = 0; i < PER_PAGE_COUNT; i++, position++) {
				if (pCursor.moveToPosition(position)) {
					spu.ppItems[i].setPhoneContractPackage(selectedId == pCursor.getLong(0), pCursor.getInt(0),
							pCursor.getString(1), pCursor.getString(4), pCursor.getString(5), pCursor.getString(6),
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
}
