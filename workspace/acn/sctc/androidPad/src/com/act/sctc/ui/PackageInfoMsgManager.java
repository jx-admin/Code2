package com.act.sctc.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.act.sctc.Constant;
import com.act.sctc.R;
import com.act.sctc.db.SqlUtils;

public class PackageInfoMsgManager implements OnClickListener {
	private View view;
	private Context context;

	private int selectedPhoneIndex = 0;
	private Button select_setmeal_btn;
	private Button add_to_shopping_cart_btn;
	private LinearLayout setmeal_content;
	private ImageView setmeal_content_arrow;

	private int type = Constant.TYPE_BROADBAND;
	private int phoneIds[];

	public PackageInfoMsgManager(Context context) {
		this.context = context;
		view = LayoutInflater.from(context).inflate(R.layout.package_info_msg_content, null);
		onCreate();
	}

	public View getView() {
		return view;
	}

	public void onCreate() {
		setmeal_content = (LinearLayout) view.findViewById(R.id.setmeal_content);
		select_setmeal_btn = (Button) view.findViewById(R.id.select_setmeal_btn);
		add_to_shopping_cart_btn = (Button) view.findViewById(R.id.add_to_shopping_cart_btn);
		setmeal_content_arrow = (ImageView) view.findViewById(R.id.setmeal_content_arrow);
		setmeal_content_arrow.setVisibility(View.GONE);
		setmeal_content.setVisibility(View.GONE);

		add_to_shopping_cart_btn.setEnabled(packagePackageId > 0);
		iniListener();
	}

	private void iniListener() {
		select_setmeal_btn.setOnClickListener(this);
		add_to_shopping_cart_btn.setOnClickListener(this);
	}

	public void selectPhone(int phoneId) {
		Cursor cursor = SqlUtils.getProductNameandPrice(context, Constant.CATEGORY_PHONE_PRODUCTLIST, phoneId);
		if (cursor != null && cursor.moveToFirst()) {
			TextView text = (TextView) view.findViewById(1000 + selectedPhoneIndex);
			text.setText(cursor.getString(1));
			phoneIds[selectedPhoneIndex] = phoneId;
		}
	}

	@Override
	public void onClick(View v) {
		if (v == select_setmeal_btn) {
			PackageActivity.start(((Activity) context), type, Constant.CATEGORY_PACKAGE_PACKAGE, packagePackageId,
					SETMEAL_RESULT_CODE);
		} else if (v == add_to_shopping_cart_btn) {
			if (packagePackageId > 0) {
				ShoppingCarActivity.addGoods(0, 0, packagePackageId, Constant.CATEGORY_PACKAGE_PACKAGE, 1, "");
				if (phoneIds != null) {
					for (int phoneId : phoneIds) {
						ShoppingCarActivity.addGoods(0, 0, phoneId, Constant.CATEGORY_PHONE_PRODUCTLIST, 1, "");
					}
				}
				// SqlUtils.insertShoppingCart(context, packagePackageId, 1,
				// Constant.CATEGORY_PACKAGE_PACKAGE, 1, "");
			}
		}
	}

	private int packagePackageId;
	private static final int SETMEAL_RESULT_CODE = 10;

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK) {
			return;
		}
		// 可以根据多个请求代码来作相应的操作
		if (requestCode == SETMEAL_RESULT_CODE) {
			setmeal_content.removeAllViews();
			if (data != null) {
				packagePackageId = data.getIntExtra("data", 0);
				if (packagePackageId > 0) {
					Cursor cursor = SqlUtils.getPackage(context, packagePackageId);
					if (cursor != null && cursor.moveToFirst()) {
						select_setmeal_btn.setText(cursor.getString(1));
						select_setmeal_btn.setBackgroundResource(R.drawable.yellow_blue_dark_radius_selector);

						int padding = context.getResources().getDimensionPixelOffset(R.dimen.margin_mid);
						String desc1 = cursor.getString(18);
						if (desc1 != null && desc1.length() > 0) {
							TextView tView = new TextView(context);
							tView.setText(desc1);
							tView.setBackgroundColor(0xffE1E1E1);
							tView.setPadding(padding, padding, padding, padding);
							setmeal_content.addView(tView);
						}

						String desc2 = cursor.getString(19);
						if (desc2 != null && desc2.length() > 0) {
							TextView tView = new TextView(context);
							tView.setText(desc2);
							tView.setBackgroundColor(0xffE1E1E1);
							tView.setPadding(padding, padding, padding, padding);
							setmeal_content.addView(tView);
						}

						int phoneNumber = cursor.getInt(20);
						phoneIds = new int[phoneNumber];
						for (int i = 0; i < phoneNumber; i++) {
							TextView tView = (TextView) LayoutInflater.from(context).inflate(
									R.layout.setmeal_select_phone_model, null);
							tView.setText("请选择第" + (i + 1) + "部手机");
							tView.setId(1000 + i);
							tView.setTag(i);
							setmeal_content.addView(tView);
							tView.setOnClickListener(new OnClickListener() {
								public void onClick(View v) {
									selectedPhoneIndex = (Integer) v.getTag();
									PhoneProductListActivity.start((Activity) context, -1, true);
								}
							});
						}

						setmeal_content_arrow.setVisibility(View.VISIBLE);
						setmeal_content.setVisibility(View.VISIBLE);
					}
					cursor.close();
				}
			} else {
				phoneIds = null;
				select_setmeal_btn.setText(R.string.click_select_setmeal);
				setmeal_content_arrow.setVisibility(View.GONE);
				setmeal_content.setVisibility(View.GONE);
				select_setmeal_btn.setBackgroundResource(R.drawable.blue_dark_yellow_radius_selector);
			}
			add_to_shopping_cart_btn.setEnabled(packagePackageId > 0);
		}
	}

}
