package com.custom.view.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;


public abstract class PagerBaseAdapter extends android.support.v4.view.PagerAdapter {
	List<View>chashViews=new ArrayList<View>(3);
	HashMap<Integer, View>viewMap=new HashMap<Integer, View>();

//	@Override
//	public int getCount() {
//		// TODO Auto-generated method stub
//		return 0;
//	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
//		Log.d(TAG,"isViewFromObject ");
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}
	
	@Override
	public void destroyItem(View container, int position, Object object) {
		View v=viewMap.get(position);
		((ViewPager) container).removeView(v);
		viewMap.remove(position);
		chashViews.add(v);
		// TODO Auto-generated method stub
//		super.destroyItem(container, position, object);
	}
	
	public abstract View getView(ViewGroup parent,View contentView,int position);

//	@Override
//	public void destroyItem(ViewGroup container, int position, Object object) {
//		// TODO Auto-generated method stub
//		super.destroyItem(container, position, object);
//	}

//	@Override
//	public void finishUpdate(View container) {
//		// TODO Auto-generated method stub
//		super.finishUpdate(container);
//	}

//	@Override
//	public void finishUpdate(ViewGroup container) {
//		// TODO Auto-generated method stub
//		super.finishUpdate(container);
//	}

//	@Override
//	public int getItemPosition(Object object) {
//		// TODO Auto-generated method stub
//		return super.getItemPosition(object);
//	}

//	@Override
//	public CharSequence getPageTitle(int position) {
//		// TODO Auto-generated method stub
//		return super.getPageTitle(position);
//	}

//	@Override
//	public float getPageWidth(int position) {
//		// TODO Auto-generated method stub
//		return super.getPageWidth(position);
//	}

	@Override
	public Object instantiateItem(View container, int position) {
		View view=null;
		if(chashViews.size()>0){
			view=chashViews.get(chashViews.size()-1);
			chashViews.remove(chashViews.size()-1);
		}
		ViewPager vp=((ViewPager) container);
		view=getView(vp,view, position);
		vp.addView(view);
		viewMap.put(position, view);
		// TODO Auto-generated method stub
		return view;
	}

//	@Override
//	public Object instantiateItem(ViewGroup container, int position) {
//		// TODO Auto-generated method stub
//		return super.instantiateItem(container, position);
//	}

//	@Override
//	public void notifyDataSetChanged() {
//		// TODO Auto-generated method stub
//		super.notifyDataSetChanged();
//	}

//	@Override
//	public void restoreState(Parcelable state, ClassLoader loader) {
//		// TODO Auto-generated method stub
//		super.restoreState(state, loader);
//	}

//	@Override
//	public Parcelable saveState() {
//		// TODO Auto-generated method stub
//		return super.saveState();
//	}

//	@Override
//	public void setPrimaryItem(View container, int position, Object object) {
//		// TODO Auto-generated method stub
//		super.setPrimaryItem(container, position, object);
//	}

//	@Override
//	public void setPrimaryItem(ViewGroup container, int position, Object object) {
//		// TODO Auto-generated method stub
//		super.setPrimaryItem(container, position, object);
//	}

//	@Override
//	public void startUpdate(View container) {
//		// TODO Auto-generated method stub
//		super.startUpdate(container);
//	}

//	@Override
//	public void startUpdate(ViewGroup container) {
//		// TODO Auto-generated method stub
//		super.startUpdate(container);
//	}

}
