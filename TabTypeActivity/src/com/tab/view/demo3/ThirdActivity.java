package com.tab.view.demo3;

import java.util.ArrayList;
import java.util.List;

import com.tab.view.R;
import com.tab.view.demo2.Tab1Fragment;
import com.tab.view.demo2.Tab2Fragment;
import com.tab.view.demo2.Tab3Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

/**
 * 第三种tab界面的实现方式 --ViewPager + Fragment + FragmentPagerAdapter
 * 
 * @since 2015年1月5日
 * @author jacksen
 * 
 */
public class ThirdActivity extends FragmentActivity implements OnClickListener,
		OnPageChangeListener {

	// 三个textview
	private TextView tab1Tv, tab2Tv, tab3Tv;
	// 指示器
	private ImageView cursorImg;
	// viewpager
	private ViewPager viewPager;
	// fragment对象集合
	private ArrayList<Fragment> fragmentsList;
	// 记录当前选中的tab的index
	private int currentIndex = 0;
	// 指示器的偏移量
	private int offset = 0;
	// 左margin
	private int leftMargin = 0;
	// 屏幕宽度
	private int screenWidth = 0;
	// 屏幕宽度的三分之一
	private int screen1_3;
	//
	private LinearLayout.LayoutParams lp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_third);

		init();
	}

	/**
	 * 初始化操作
	 */
	private void init() {

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		screen1_3 = screenWidth / 3;

		cursorImg = (ImageView) findViewById(R.id.cursor);
		lp = (LayoutParams) cursorImg.getLayoutParams();
		leftMargin = lp.leftMargin;

		tab1Tv = (TextView) findViewById(R.id.tab1_tv);
		tab2Tv = (TextView) findViewById(R.id.tab2_tv);
		tab3Tv = (TextView) findViewById(R.id.tab3_tv);
		//
		tab1Tv.setOnClickListener(this);
		tab2Tv.setOnClickListener(this);
		tab3Tv.setOnClickListener(this);

		initViewPager();
	}

	/**
	 * 初始化viewpager
	 */
	private void initViewPager() {
		viewPager = (ViewPager) findViewById(R.id.third_vp);
		fragmentsList = new ArrayList<>();
		Fragment fragment = new Tab1Fragment();
		fragmentsList.add(fragment);
		fragment = new Tab2Fragment();
		fragmentsList.add(fragment);
		fragment = new Tab3Fragment();
		fragmentsList.add(fragment);

		viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(),
				fragmentsList));
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tab1_tv:
			viewPager.setCurrentItem(0);
			break;
		case R.id.tab2_tv:
			viewPager.setCurrentItem(1);
			break;
		case R.id.tab3_tv:
			viewPager.setCurrentItem(2);
			break;
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		offset = (screen1_3 - cursorImg.getLayoutParams().width) / 2;
		Log.d("111", position + "--" + positionOffset + "--"
				+ positionOffsetPixels);
		final float scale = getResources().getDisplayMetrics().density;
		if (position == 0) {// 0<->1
			lp.leftMargin = (int) (positionOffsetPixels / 3) + offset;
		} else if (position == 1) {// 1<->2
			lp.leftMargin = (int) (positionOffsetPixels / 3) + screen1_3 +offset;
		}
		cursorImg.setLayoutParams(lp);
		currentIndex = position;
	}

	@Override
	public void onPageSelected(int arg0) {
	}
}
