package com.android.abt;

import java.util.List;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

class BtListAdapter extends BaseAdapter {
	List<BluetoothDevice> datas;
	Context context;
	LayoutInflater lf;

	public BtListAdapter(Context context, List<BluetoothDevice> data) {
		this.context = context;
		this.datas = data;
		lf = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return datas == null ? 0 : datas.size();
	}

	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = new TextView(context);
		}
		BluetoothDevice btd = datas.get(position);
		((TextView) convertView).setText(btd.getName() + "\n"
				+ btd.getAddress());
		return convertView;
	}

}