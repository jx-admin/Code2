package com.tab.view.demo5;

import java.util.ArrayList;
import java.util.List;

import com.tab.view.R;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;

/**
 * ViewPager + PagerTitleStrip
 * 
 * @author jacksen
 * 
 */
public class FifthActivity extends Activity {

	// viewpager
	private ViewPager viewPager;
	// viewpager的标题
	private PagerTitleStrip titleStrip;
	// viewpager的指示器
	private PagerTabStrip tabStrip;
	// view集合
	private List<View> viewList;
	// 标题集合
	private List<String> titleList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fifth);

		init();
	}

	/**
	 * 
	 */
	private void init() {
		viewList = new ArrayList<>();
		LayoutInflater inflater = getLayoutInflater();
		View view = inflater.inflate(R.layout.first_layout1, null);
		viewList.add(view);
		view = inflater.inflate(R.layout.first_layout2, null);
		viewList.add(view);
		view = inflater.inflate(R.layout.first_layout3, null);
		viewList.add(view);

		//
		titleList = new ArrayList<>();
		titleList.add("标题1");
		titleList.add("标题2");
		titleList.add("标题3");

		initViewPager();
	}

	private void initViewPager() {
		viewPager = (ViewPager) findViewById(R.id.fifth_vp);
		viewPager.setAdapter(pagerAdapter);

		// 修改指示器的颜色
		tabStrip = (PagerTabStrip) findViewById(R.id.fifth_strip);
		tabStrip.setTabIndicatorColor(Color.RED);
	}

	/**
	 * 适配器
	 */
	PagerAdapter pagerAdapter = new PagerAdapter() {

		/**
		 * 官方建议这么写
		 */
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getCount() {
			return viewList.size();
		}

		/**
		 * 实例化item
		 */
		@Override
		public Object instantiateItem(android.view.ViewGroup container,
				int position) {
			container.addView(viewList.get(position));
			return viewList.get(position);
		}

		/**
		 * 销毁item
		 */
		@Override
		public void destroyItem(android.view.ViewGroup container, int position,
				Object object) {
			container.removeView(viewList.get(position));
		}

		// 重写此方法即可显示标题
		@Override
		public CharSequence getPageTitle(int position) {
			return titleList.get(position);
		}
	};
}
