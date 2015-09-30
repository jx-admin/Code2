package com.act.sctc.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.act.sctc.R;
import com.act.sctc.been.SetMeal;

public class SetMealItem extends RelativeLayout implements OnClickListener {

	private View info_layout, vs_layout;
	private ImageButton vs_iv;
	private TextView name_tv, add_shopping_car_tv;
	private TextView vs_title_tv, setmeal_vs_dis_tv;

	private TextView tv_t1;
	private TextView tv_t2;
	private TextView tv_t3;
	private TextView tv_t4;
	private TextView tv_t5;
	private TextView tv_t6;

	private TextView content_1_1;
	private TextView content_2_1;
	private TextView content_3_1;
	private TextView content_4_1;
	private TextView content_5_1;
	private TextView content_6_1;

	private int dataId;
	private boolean isSelected;

	public int getDataId() {
		return dataId;
	}

	public void setVsVisible(int visibility) {
		vs_iv.setVisibility(visibility);
	}

	/**
	 * @param sm
	 */
	public void setData(int type, SetMeal sm) {
		if (isShow) {
			vs_iv.setBackgroundResource(R.drawable.vs_1);
			hideRecommend();
			isShow = false;
		}
		if (sm.getName() != null)
			name_tv.setText(sm.getName());
		// setViewStatus(sm.getStatus());

		if (sm.titles != null) {
			setGGM(sm.titles[0], sm.titles[1], sm.titles[2], sm.titles[3], sm.titles[4], sm.titles[5]);
		}

		if (sm.contents != null) {
			setGGMM2(sm.contents[0], sm.contents[1], sm.contents[2], sm.contents[3], sm.contents[4], sm.contents[5]);
		} else {
			// SpannableString string=new
			// SpannableString(sm.getMonthlyRent()+"元");
			// string.setSpan(new RelativeSizeSpan(0.6f), string.length()-1,
			// string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			content_1_1.setText(sm.getMonthlyRent() + "元");
			if (sm.getDomesticTraffic() == 0) {
				content_2_1.setText(R.string.null_);
			} else {
				content_2_1.setText(sm.getDomesticTraffic() + "MB");
			}
			if (0 == sm.getFreeWifiHour()) {
				content_3_1.setText(R.string.null_);
			} else {
				content_3_1.setText(sm.getFreeWifiHour() + "小时");
			}
			if (0 == sm.getFreeInlandCall()) {
				content_4_1.setText(R.string.null_);

			} else {
				content_4_1.setText(sm.getFreeInlandCall() + "分钟");

			}
			if (sm.getFreeSMS() == 0) {
				content_5_1.setText(R.string.null_);

			} else {
				content_5_1.setText(sm.getFreeSMS() + "条");

			}
			if (sm.getFreeMMS() == 0) {
				content_6_1.setText(R.string.null_);
			} else {
				content_6_1.setText(sm.getFreeMMS() + "条");
			}
			setmeal_vs_dis_tv.setText(sm.getVSDis());
		}
	}

	public void setPhoneContractPackage(boolean isSelected, int id, String name, String str1, String str2, String str3,
			String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11,
			String str12) {
		this.isSelected = isSelected;
		freshViewStatus();
		dataId = id;
		vs_iv.setVisibility(View.GONE);
		name_tv.setText(name);
		vs_title_tv.setVisibility(View.GONE);
		setmeal_vs_dis_tv.setVisibility(View.GONE);
		setGGM(str1, str2, str3, str4, str5, str6);
		setGGMM2(str7, str8, str9, str10, str11, str12);
	}

	public void setData(boolean isSelected, int id, String name, String vs_title, String vs_content, String str1,
			String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9,
			String str10, String str11, String str12) {
		if (isShow) {
			vs_iv.setBackgroundResource(R.drawable.vs_1);
			hideRecommend();
			isShow = false;
		}
		this.isSelected = isSelected;
		freshViewStatus();
		dataId = id;
		name_tv.setText(name);
		vs_title_tv.setText(vs_title);
		setmeal_vs_dis_tv.setText(vs_content);
		setGGM(str1, str2, str3, str4, str5, str6);
		setGGMM2(str7, str8, str9, str10, str11, str12);
	}

	private void setGGM(String str1, String str2, String str3, String str4, String str5, String str6
	// ,String str7,String str8,String str9,
	// String str10,String str11,String str12
	) {
		tv_t1.setText(str1);
		tv_t2.setText(str2);
		tv_t3.setText(str3);
		tv_t4.setText(str4);
		tv_t5.setText(str5);
		tv_t6.setText(str6);
	}

	private void setGGMM2(String str1, String str2, String str3, String str4, String str5, String str6
	// ,String str7,String str8,String str9,
	// String str10,String str11,String str12
	) {
		content_1_1.setText(str1);
		content_2_1.setText(str2);
		content_3_1.setText(str3);
		content_4_1.setText(str4);
		content_5_1.setText(str5);
		content_6_1.setText(str6);
	}

	public SetMealItem(Context context) {
		super(context);
	}

	public SetMealItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public SetMealItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void onCreate() {
		iniController();
		iniListener(this);
	}

	private void iniController() {
		vs_iv = (ImageButton) findViewById(R.id.vs_ibtn);
		vs_iv.setTag(this);
		info_layout = findViewById(R.id.info_layout);
		vs_layout = findViewById(R.id.vs_layout);
		name_tv = (TextView) findViewById(R.id.titel_tv);
		add_shopping_car_tv = (TextView) findViewById(R.id.add_car_tv);
		vs_title_tv = (TextView) findViewById(R.id.vs_title_tv);
		setmeal_vs_dis_tv = (TextView) findViewById(R.id.setmeal_vs_dis_tv);

		tv_t1 = (TextView) findViewById(R.id.tv_t1);
		tv_t2 = (TextView) findViewById(R.id.tv_t2);
		tv_t3 = (TextView) findViewById(R.id.tv_t3);
		tv_t4 = (TextView) findViewById(R.id.tv_t4);
		tv_t5 = (TextView) findViewById(R.id.tv_t5);
		tv_t6 = (TextView) findViewById(R.id.tv_t6);

		content_1_1 = (TextView) findViewById(R.id.content_1_1);
		content_2_1 = (TextView) findViewById(R.id.content_2_1);
		content_3_1 = (TextView) findViewById(R.id.content_3_1);
		content_4_1 = (TextView) findViewById(R.id.content_4_1);
		content_5_1 = (TextView) findViewById(R.id.content_5_1);
		content_6_1 = (TextView) findViewById(R.id.content_6_1);

	}

	private void iniListener(OnClickListener l) {
		vs_iv.setOnClickListener(l);
		add_shopping_car_tv.setOnClickListener(l);
	}

	public void setChecked(boolean checked) {
		isSelected = checked;
		freshViewStatus();
	}

	public void toggleSelectedState() {
		isSelected = !isSelected;
		freshViewStatus();
	}

	public void freshViewStatus() {
		if (isSelected) {
			add_shopping_car_tv.setText(R.string.selected);
			add_shopping_car_tv.setTextColor(getResources().getColor(R.color.blue));
		} else {
			add_shopping_car_tv.setText(R.string.select);
			add_shopping_car_tv.setTextColor(getResources().getColor(R.color.setmeal_title_yellow));
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.vs_ibtn:
			show();
			break;
		case R.id.add_car_tv:
			this.toggleSelectedState();
			if (onCheckedChangeListener != null) {
				onCheckedChangeListener.onCheckedChanged(this, dataId, isSelected);
			}
			break;
		}
	}

	public void show() {
		if (isShow) {
			vs_iv.setBackgroundResource(R.drawable.vs_1);
			hideRecommend();
		} else {
			vs_iv.setBackgroundResource(R.drawable.vs_2);
			showRecommend();
		}
		isShow = !isShow;
	}

	private boolean isShow = false;

	private void showRecommend() {
		vs_layout.setVisibility(View.VISIBLE);
		AnimationSet _AnimationSet = new AnimationSet(true);
		TranslateAnimation _TranslateAnimation;
		_TranslateAnimation = new TranslateAnimation(0f, 0f, info_layout.getBottom(), 0f);
		_AnimationSet.addAnimation(_TranslateAnimation);
		_AnimationSet.setFillBefore(true);
		_AnimationSet.setFillAfter(true);
		_AnimationSet.setDuration(500);
		vs_layout.startAnimation(_AnimationSet);
	}

	private void hideRecommend() {
		AnimationSet _AnimationSet = new AnimationSet(true);
		TranslateAnimation _TranslateAnimation;
		_TranslateAnimation = new TranslateAnimation(0f, 0f, vs_layout.getTop(), vs_layout.getBottom());
		_AnimationSet.addAnimation(_TranslateAnimation);
		_AnimationSet.setFillBefore(true);
		_AnimationSet.setFillAfter(true);
		_AnimationSet.setDuration(500);
		vs_layout.startAnimation(_AnimationSet);
	}

	private OnCheckedChangeListener onCheckedChangeListener;

	public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
		this.onCheckedChangeListener = onCheckedChangeListener;
	}

	public interface OnCheckedChangeListener {
		public void onCheckedChanged(SetMealItem view, int id, boolean checked);
	}

}
