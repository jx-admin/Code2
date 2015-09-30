package com.example.draglistview;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter{
	private ArrayList<String> datas;
	private Context mContext;
	public MyAdapter(Context mContext, ArrayList<String> datas){
		this.mContext = mContext;
		this.datas = datas;
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_main, null);
		TextView txt = (TextView) convertView.findViewById(R.id.txt);
		txt.setText(datas.get(position));
		return convertView;
	}

}
