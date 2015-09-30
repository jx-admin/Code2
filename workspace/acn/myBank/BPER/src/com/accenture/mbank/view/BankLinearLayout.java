package com.accenture.mbank.view;

import it.gruppobper.ams.android.bper.R;

import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.accenture.mbank.model.AccountsModel;

public class BankLinearLayout extends LinearLayout {

	public InnerScrollView innerScrollView;

	/**
	 * 上半圆长宽比
	 */
	float close_widht_height = (float) (395f / 294f);

	/**
	 * 下半圆长宽比
	 */
	float show_widht_height = (float) (395f / 101f);

	double cycle_top_height = 105 / 360d;

	/**
	 * 上半圆与下半圆的比例
	 */
	public static final double cycle_bottom_height = 101d / 294;

	public BankLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	protected void setAccountsUI(LinearLayout root, List<AccountsModel> list) {
		root.removeAllViews();
		if (list != null && list.size() > 0) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			for (AccountsModel amAccountsModel : list) {
				ItemExpander itemExpander = (ItemExpander) inflater.inflate(R.layout.item_expander, null);
				String title = amAccountsModel.getAccountAlias();
				itemExpander.setTitle(title);
				itemExpander.setTypeface(Typeface.DEFAULT);
				itemExpander.setResultVisible(false);
				root.addView(itemExpander);
				setExpandedContainer(itemExpander, amAccountsModel);
			}
		} else {
			TextView textView = new TextView(getContext());
			textView.setGravity(Gravity.CENTER);
			textView.setBackgroundResource(R.drawable.box_details);
			textView.setText(getResources().getString(R.string.not_available));
			root.addView(textView);
		}
	}

	protected void setExpandedContainer(ItemExpander itemExpander,
			AccountsModel accountsModel) {
	}

	public void parentScrollViewScrollTo(int y) {
		if (innerScrollView != null) {
			innerScrollView.scrollTo(y);
		}
	}
}
