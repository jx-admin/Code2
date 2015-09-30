package com.accenture.mbank.view;

import it.gruppobper.ams.android.bper.R;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.accenture.mbank.view.ExpandedContainer.ExpandBarResultChangeListener;

public class EnhanceItemExpander extends LinearLayout implements
		ExpandBarResultChangeListener, OnCheckedChangeListener {

	/**
	 * 展开后显示的那个 View
	 */
	public ExpandedContainer expandedContainer;

	private LinearLayout expandContainerLayout;

	private LinearLayout expandBar;

	public ToggleButton toggleButton;

	boolean expandable = true;

	private TextView item_name, item_lable, item_value;

	public int level = 0;
	public static final int level_1_width = 20;
	public ViewGroup owner;
	private ImageView investment_star_icon;
	public EnhanceItemExpander(Context context) {
		super(context);
	}

	public EnhanceItemExpander(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	private void init() {
		toggleButton = (ToggleButton) findViewById(R.id.expand_btn);
		toggleButton.setOnCheckedChangeListener(this);
		investment_star_icon = (ImageView) findViewById(R.id.investment_star_icon);
		expandBar = (LinearLayout) findViewById(R.id.expander_bar);
		expandBar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (expandable) {

					toggleButton.performClick();
				}
			}
		});

		expandContainerLayout = (LinearLayout) findViewById(R.id.expanded_container);
		item_name = (TextView) findViewById(R.id.item_name);
		item_lable = (TextView) findViewById(R.id.item_lable);
		item_value = (TextView) findViewById(R.id.item_value);
		performLevel();
	}

	public boolean getExpanded() {
		return toggleButton.isChecked();
	}

	public void performExpand() {
		if (!getExpanded()) {
			toggleButton.performClick();
		}
	}

	public void setLevel(int level) {
		this.level = level;
		performLevel();
	}

	private void performLevel() {
		if (toggleButton == null) {
			init();
		}
		if (toggleButton != null) {
			LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) toggleButton
					.getLayoutParams();
			LinearLayout.LayoutParams titlelayoutParams = (LinearLayout.LayoutParams) item_name
					.getLayoutParams();
			if (level == 0) {
				layoutParams.rightMargin = 0;
				titlelayoutParams.leftMargin = 0;
			}
			if (level == 1) {
				layoutParams.rightMargin = level_1_width;
				titlelayoutParams.leftMargin = level_1_width;
			}
			if (level == 2) {
				layoutParams.rightMargin = level_1_width * 2;
				titlelayoutParams.leftMargin = level_1_width * 2;
			}
		}
	}

	/**
	 * 展开
	 * 
	 * @param expand
	 */
	public void setExpand(boolean expand) {
		toggleButton.setChecked(expand);
		if (expand) {
			expand();
		} else {

			close();
		}
	}

	private void close() {
		if(resid != 0){
			expandBar.setBackgroundResource(0);
		}
		expandContainerLayout.setVisibility(View.GONE);
	}

	public void setStarImage(int res){
		investment_star_icon.setImageResource(res);
		investment_star_icon.setVisibility(View.VISIBLE);
	}
	
	public void setExpandButtonBackground(int res) {
		toggleButton.setBackgroundResource(res);
	}
	private int resid = 0;
	public void setExpandBarBackground(int resid){
		this.resid = resid;
//		expandBar.setBackgroundResource(resid);
	}
	
	private void expand() {
		if(resid != 0){
			expandBar.setBackgroundResource(resid);
		}
		closeOther();
		if (expandable && expandedContainer != null) {
			expandedContainer.onExpand();
			expandContainerLayout.setVisibility(View.VISIBLE);
		}
	}

	private void closeOther() {
		ViewGroup parent = (ViewGroup) getParent();
		for (int i = 0; i < parent.getChildCount(); i++) {
			View child = parent.getChildAt(i);
			if (child instanceof EnhanceItemExpander) {
				EnhanceItemExpander itemExpander = (EnhanceItemExpander) child;
				if (itemExpander != this && itemExpander.toggleButton != null
						&& itemExpander.toggleButton.isChecked()) {
					itemExpander.toggleButton.performClick();
				}
			}

		}

	}

	public void setExpandable(boolean expandable) {
		this.expandable = expandable;
		if (expandable) {
			toggleButton.setOnCheckedChangeListener(this);
			item_value.setTextColor(Color.BLACK);
		} else {
			item_value.setTextColor(Color.GRAY);
			toggleButton.setOnCheckedChangeListener(null);
			close();
		}
	}

	public void setTitle(String str) {
		if (item_name == null) {
			init();
		}
		item_name.setText(str);
	}

	public String getTitle() {
		return item_name.getText().toString();
	}

	public void setTypeface(Typeface tf) {
		if (item_name == null) {
			init();
		}
		item_name.setTypeface(tf);
	}

	public void setResultVisible(boolean isVisible) {
		if (isVisible) {
			item_value.setVisibility(View.VISIBLE);
		} else {
			item_value.setVisibility(View.GONE);
		}
	}

	public void setResult(String text) {
		if (text == null || text.equals("")) {
			text = getResources().getString(R.string.tap_to_set1);
		}
		item_value.setText(text);
	}

	public void setItem_lable(int resId) {
		item_lable.setText(resId);
	}
	
	private void setResultAndRequstFocus(String text) {
		setResult(text);
		item_value.requestFocus();
	}

	/**
	 * 拿到展开的那个view
	 * 
	 * @return
	 */
	public ExpandedContainer getExpandedContainer() {
		return expandedContainer;
	}

	public void setExpandedContainer(ExpandedContainer v) {
		if (expandContainerLayout == null) {
			init();
		}
		expandContainerLayout.removeAllViews();
		expandContainerLayout.addView(v);

		expandedContainer = v;
		v.setExpandBarResultChangeListener(this);
	}

	public void setPushExpandedContainer(ExpandedContainer v) {
		if (expandContainerLayout == null) {
			init();
		}
		// expandContainerLayout.removeAllViews();
		expandContainerLayout.addView(v);

		expandedContainer = v;
		v.setExpandBarResultChangeListener(this);

	}

	public Object getValue() {
		if (expandedContainer.init) {
			return null;
		} else {
			return expandedContainer.getValue();
		}
	}

	private void closeKeyboard() {
		InputMethodManager imm = (InputMethodManager) getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(item_value.getWindowToken(), 0);
	}

	@Override
	public void onChange(String str) {
		setResult(str);
	}

	@Override
	public void onFocusChange(String str) {
		setResultAndRequstFocus(str);

	}

	/**
     * 
     */
	public void setEditable(boolean flag) {
		setExpand(flag);
		expandedContainer.setEditable(flag);
	}

	@Override
	public ViewGroup getOwener() {
		return owner;
	}

	public void recover(String text) {
		expandedContainer.onRecover(text);
	}

	public void recover(Object object) {
		expandedContainer.onRecover(object);
	}

	public void reset() {
		setExpand(false);
		expandedContainer.resetResult();
	}

	public void onReset() {
		setResult("");
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			expand();
		} else {
			close();
		}
		item_value.requestFocus();
		closeKeyboard();

	}
}
