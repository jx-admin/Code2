package com.aess.aemm.view.popu;

import com.aess.aemm.R;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;


public class OLAdapter implements ListAdapter {

	public OLAdapter(Context cxt) {
		_cxt = cxt;
	}
	
	@Override
	public int getCount() {
		return images.length;
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public int getItemViewType(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		OLItem item = null;
		if (null == convertView) {
			item = new OLItem(_cxt);
		} else {
			item = (OLItem)convertView;
		}
		
		init(position, item);
		return item;
	}

	@Override
	public int getViewTypeCount() {
		return images.length;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver arg0) {
		;
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver arg0) {
		;
	}

	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}

	@Override
	public boolean isEnabled(int arg0) {
		return true;
	}
	
	public int getItemId() {
		return id;
	}
	
	private void init(int position, OLItem item) {
		if (position < 0 || position > images.length - 1) {
			return;
		}
		item.iv.setImageResource(images[position]);
		item.iv.setImageResource(titles[position]);
		id = position;
	}

	private int[] images = {R.drawable.update1, R.drawable.message1, R.drawable.userinfo1, R.drawable.psword1, R.drawable.update1};
	private int[] titles = {R.string.update3,   R.string.msg,        R.string.userinfo,    R.string.psword};
	private Context _cxt;
	private int id = 0;
}
