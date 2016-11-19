package com.tab.view.demo3;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class MyAdapter extends PagerAdapter {

	private List<View> list;

	public MyAdapter(List<View> list) {
		super();
		this.list = list;
	}

	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View child = list.get(position % list.size());
		if (child.getParent() != null) {
			container.removeView(child);
		}
		container.addView(child);
		return list.get(position % list.size());
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(list.get(position % list.size()));
	}

}
