package com.tab.view.demo2;

import com.tab.view.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

/**
 * 第二种tab界面的实现方式-- 　Fragment + FragmentManager
 * 
 * @since 2015年1月12日
 * @author jacksen
 * 
 */
public class SecondActivity extends FragmentActivity implements OnClickListener {

	// 三个选项卡
	private LinearLayout tab1Layout, tab2Layout, tab3Layout;
	// 默认选中第一个tab
	private int index = 1;
	// fragment管理类
	private FragmentManager fragmentManager;
	// 三个fragment
	private Fragment tab1Fragment, tab2Fragment, tab3Fragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second);
		fragmentManager = getSupportFragmentManager();
		init();
	}

	/**
	 * 初始化控件
	 */
	private void init() {
		tab1Layout = (LinearLayout) findViewById(R.id.tab1_layout);
		tab2Layout = (LinearLayout) findViewById(R.id.tab2_layout);
		tab3Layout = (LinearLayout) findViewById(R.id.tab3_layout);

		tab1Layout.setOnClickListener(this);
		tab2Layout.setOnClickListener(this);
		tab3Layout.setOnClickListener(this);
		//
		setDefaultFragment();
	}

	/**
	 * 设置默认显示的fragment
	 */
	private void setDefaultFragment() {
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		tab1Fragment = new Tab1Fragment();
		transaction.replace(R.id.content_layout, tab1Fragment);
		transaction.commit();
	}

	/**
	 *切换fragment
	 * @param newFragment
	 */
	private void replaceFragment(Fragment newFragment) {
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		if (!newFragment.isAdded()) {
			transaction.replace(R.id.content_layout, newFragment);
			transaction.commit();
		} else {
			transaction.show(newFragment);
		}
	}

	/**
	 * 改变现象卡的选中状态
	 */
	private void clearStatus() {
		if (index == 1) {
			tab1Layout.setBackgroundColor(getResources().getColor(R.color.tab));
		} else if (index == 2) {
			tab2Layout.setBackgroundColor(getResources().getColor(R.color.tab));
		} else if (index == 3) {
			tab3Layout.setBackgroundColor(getResources().getColor(R.color.tab));
		}
	}

	@Override
	public void onClick(View v) {
		clearStatus();
		switch (v.getId()) {
		case R.id.tab1_layout:
			if (tab1Fragment == null) {
				tab1Fragment = new Tab1Fragment();
			}
			replaceFragment(tab1Fragment);
			tab1Layout.setBackgroundColor(getResources().getColor(
					R.color.tab_down));
			index = 1;
			break;
		case R.id.tab2_layout:
			if (tab2Fragment == null) {
				tab2Fragment = new Tab2Fragment();
			}
			replaceFragment(tab2Fragment);
			tab2Layout.setBackgroundColor(getResources().getColor(
					R.color.tab_down));
			index = 2;
			break;
		case R.id.tab3_layout:
			if (tab3Fragment == null) {
				tab3Fragment = new Tab3Fragment();
			}
			replaceFragment(tab3Fragment);
			tab3Layout.setBackgroundColor(getResources().getColor(
					R.color.tab_down));
			index = 3;
			break;
		}
	}

}
