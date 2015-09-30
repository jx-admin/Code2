package com.act.mbanking.manager.view.adapter;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.act.mbanking.R;
import com.act.mbanking.manager.NewRequestManager;

public class TitleDetailAdapter implements ListAdapter, OnClickListener {
	
	public TitleDetailAdapter(Context cxt, ListView lv ,ArrayList<HashMap<String, String>> infoList) {
		if (null != infoList) {
			list = infoList;
		} else {
			list = new ArrayList<HashMap<String, String>>();
		}
		this.cxt = cxt;
		
		parent = new SoftReference<ListView>(lv);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemViewType(int position) {

		return 1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (null == convertView) {
			LayoutInflater inflater = (LayoutInflater)cxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.show_list, null);
			
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.detail = (TextView) convertView.findViewById(R.id.detail);
			holder.iv = (ImageView) convertView.findViewById(R.id.imagehint);
			
			convertView.setOnClickListener(this);
			convertView.setTag(holder);
		}
		holder = (ViewHolder) convertView.getTag();
		holder.postion = position;

		HashMap<String, String> item = list.get(position);
		String title = item.get("title");
		String detail = item.get("detail");
		String eValue = item.get("expand");

		holder.title.setText(title);
		holder.detail.setText(detail);
		if ("1".equals(eValue)) {
			holder.detail.setVisibility(View.VISIBLE);
			holder.iv.setImageResource(R.drawable.btnarrow_up);
		} else {
			holder.detail.setVisibility(View.GONE);
			holder.iv.setImageResource(R.drawable.btnarrow_down);
		}
		return convertView;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isEmpty() {
		return list.size() < 1;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		dso.registerObserver(observer);
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		dso.unregisterObserver(observer);
	}

	@Override
	public boolean areAllItemsEnabled() {
		return list.size() > 0;
	}

	@Override
	public boolean isEnabled(int arg0) {

		return false;
	}
	
	public ArrayList<HashMap<String, String>> getList() {
		return list;
	}
	
	public static class ViewHolder {
        public TextView title = null;
        public TextView detail = null;
        public ImageView iv = null;
        public int postion = -1;
        public int expand = 0;
    }
	
	@Override
	public void onClick(View view) {
		
		ViewHolder holder = (ViewHolder) view.getTag();
		int newValue = holder.postion;
		
		if (expanded > -1) {
			HashMap<String, String> item = list.get(expanded);
			item.put("expand", "0");
		}

		if (newValue == expanded) {

			expanded = -1;
		} else {
			HashMap<String, String> item = list.get(newValue);
			item.put("expand", "1");
			expanded = newValue;
		}
		
		notifyChanged();
		
	};
	
	public void notifyChanged() {
		dso.notifyChanged();
		
		ListView lv = parent.get();
		if (null != lv) {
			setListViewHeightBasedOnChildren(lv);
		}
	}
	
	public static void setListViewHeightBasedOnChildren(ListView listView) {

		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));

		listView.setLayoutParams(params);
	}

	private ArrayList<HashMap<String, String>> list;
	private DataSetObservable dso = new DataSetObservable();
	private Context cxt;
	private int expanded = -1;
	private SoftReference<ListView> parent;
}
