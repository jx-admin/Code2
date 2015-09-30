package com.act.sctc.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.act.sctc.Constant;
import com.act.sctc.R;
import com.act.sctc.been.Phone;

public class PhoneProductItemUtils extends LinearLayout implements OnClickListener, TextWatcher {

	private TextView phone_name_tv, phone_price_tv, original_price_tv, contrast_tv;
	private ImageView phone_icon_iv, selling_iv;
	private Phone pp;
	private OncheckedListener mOncheckedListener;
	private long phoneId;

	private EditText phone_price_ev;
	private boolean isEdit;
	private boolean isSelectOnly;
	private TextView priceView;
	private int selectedNameId, normalNameId;
	boolean isShow = false;
	boolean isHide = false;

	public long getPhoneId() {
		return phoneId;
	}

	/**
	 * @param sm
	 */
	public void setData(Phone pp) {
		this.pp = pp;
		String str = pp.getName();
		if (str != null)
			phone_name_tv.setText(str);
		phone_price_tv.setText("￥" + pp.getPrice());
		original_price_tv.setText("官方价：￥" + pp.getOriginalPrice());
		original_price_tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); // 删除线
		contrast_tv.setText(selectedNameId);
		if (pp.getIcon() != null) {
			phone_icon_iv.setImageURI(Uri.parse(pp.getIcon()));
		}
	}

	public void setData(long id, String ad_desc, Uri iconUri, double price, double sale_price, boolean isHot,
			boolean isEdit, boolean isHide, boolean isSelectOnly) {
		// this.pp = pp;
		this.phoneId = id;
		if (ad_desc != null)
			phone_name_tv.setText(ad_desc);
		original_price_tv.setText("官方价：￥" + price);
		original_price_tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); // 删除线
		if (iconUri != null) {
			phone_icon_iv.setImageURI(iconUri);
		}
		if (isHot) {
			selling_iv.setVisibility(View.VISIBLE);
		} else {
			selling_iv.setVisibility(View.GONE);
		}
		this.isEdit = isEdit;
		this.isSelectOnly = isSelectOnly;
		if (isEdit) {
			priceView = phone_price_ev;
			selectedNameId = R.string.hided;
			normalNameId = R.string.hide;
			phone_price_tv.setVisibility(View.GONE);
			phone_price_ev.setVisibility(View.VISIBLE);
			priceView.setText(Double.toString(sale_price));
		} else {
			priceView = phone_price_tv;
			selectedNameId = R.string.added;
			normalNameId = R.string._contrast_price;
			phone_price_ev.setVisibility(View.GONE);
			phone_price_tv.setVisibility(View.VISIBLE);
			priceView.setText("￥" + sale_price);
		}
		this.isHide = isHide;
		show();

	}

	public PhoneProductItemUtils(Context context) {
		super(context);
	}

	public PhoneProductItemUtils(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public PhoneProductItemUtils(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void onCreate() {
		iniController();
		iniListener(this);
	}

	private void iniController() {
		// vs_iv.setTag(this);
		phone_name_tv = (TextView) findViewById(R.id.phone_name_tv);
		phone_price_tv = (TextView) findViewById(R.id.phone_price_tv);
		phone_price_ev = (EditText) findViewById(R.id.phone_price_ev);
		original_price_tv = (TextView) findViewById(R.id.original_price_tv);
		contrast_tv = (TextView) findViewById(R.id.contrast_tv);
		phone_icon_iv = (ImageView) findViewById(R.id.phone_icon_iv);
		selling_iv = (ImageView) findViewById(R.id.selling_iv);
		phone_price_ev.addTextChangedListener(this);
	}

	private void iniListener(OnClickListener l) {
		contrast_tv.setOnClickListener(l);
		setOnClickListener(l);
	}

	@Override
	public void onClick(View v) {
		if (v == contrast_tv) {
			if (isEdit) {
				taggole();
				if (mOncheckedListener != null) {
					mOncheckedListener.onChecked(isHide, this, phoneId);
				}
			} else {
				if (!isShow && mOncheckedListener != null && !mOncheckedListener.getEnable()) {
					return;
				}
				taggole();
				if (mOncheckedListener != null) {
					mOncheckedListener.onChecked(isShow, this, phoneId);
				}
			}
		} else {
			ProductDetailActivity.start(getContext(), Constant.TYPE_PHONE, phoneId, isSelectOnly);
			// Intent i=new Intent(getContext(),PhoneInfoActivity.class);
			// ((App)this.getContext().getApplicationContext()).setCurrentProductPhone(pp);;
			// this.getApplicationWindowToken();
			// i.putExtra("obj", pp);
			// getContext().startActivity(i);
		}
	}

	public void taggole() {
		if (isEdit) {
			isHide = !isHide;
		} else {
			isShow = !isShow;
		}
		show();
	}

	public void show() {
		if (isEdit) {
			if (isHide) {
				contrast_tv.setText(selectedNameId);
				contrast_tv.setTextColor(getResources().getColor(R.color.phone_product_item_blue));
			} else {
				contrast_tv.setText(normalNameId);
				contrast_tv.setTextColor(getResources().getColor(R.color.vs_yellow_dark));
			}
		} else {
			if (isShow) {
				contrast_tv.setText(selectedNameId);
				contrast_tv.setTextColor(getResources().getColor(R.color.phone_product_item_blue));
			} else {
				contrast_tv.setText(normalNameId);
				contrast_tv.setTextColor(getResources().getColor(R.color.vs_yellow_dark));
			}
		}
	}

	public void setOnCheckedListener(OncheckedListener mOncheckedListener) {
		this.mOncheckedListener = mOncheckedListener;
	}

	public interface OncheckedListener {
		public boolean getEnable();

		public void onChecked(boolean check, View v, long phoneId);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (mOnTextChangeListener != null) {
			mOnTextChangeListener.afterTextChanged(this, phone_price_ev);
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

	}

	private OnTextChangeListener mOnTextChangeListener;

	public void setOnTextChangeListener(OnTextChangeListener mOnTextChangeListener) {
		this.mOnTextChangeListener = mOnTextChangeListener;
	}

	public interface OnTextChangeListener {
		public void afterTextChanged(PhoneProductItemUtils mPhoneProductItemUtils, EditText mEditText);
	}
}
