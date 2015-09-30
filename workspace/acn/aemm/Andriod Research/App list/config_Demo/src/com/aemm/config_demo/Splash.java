package com.aemm.config_demo;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
 
public class Splash extends Activity implements OnClickListener {

	private ListView listview;
	private ArrayList<ItemBO> mListItem;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		listview = (ListView) findViewById(R.id.list_view);

		mListItem = ItemBO.getItems();
		listview.setAdapter(new ListAdapter(Splash.this, R.id.list_view,
				mListItem));
	}

	@Override
	public void onClick(View v) {

	}

	// ***ListAdapter***
	private class ListAdapter extends ArrayAdapter<ItemBO> { // --
																// CloneChangeRequired
		private ArrayList<ItemBO> mList; // --CloneChangeRequired
		private Context mContext;

		public ListAdapter(Context context, int textViewResourceId,
				ArrayList<ItemBO> list) { // --CloneChangeRequired
			super(context, textViewResourceId, list);
			this.mList = list;
			this.mContext = context;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			try {
				if (view == null) {
					LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					view = vi.inflate(R.layout.list_item, null); // --
																	// CloneChangeRequired
																	// (
																	// list_item
																	// )
				}
				final ItemBO listItem = mList.get(position); // --
																// CloneChangeRequired
				if (listItem != null) {
					// setting list_item views
					((TextView) view.findViewById(R.id.tv_name))
							.setText(listItem.getName());
					view.setOnClickListener(new OnClickListener() {
						public void onClick(View arg0) { // --clickOnListItem
							Intent myIntent = new Intent(Splash.this,
									SMS.class);
							myIntent.putExtra("demo item", listItem.getName());
							startActivity(myIntent);
							finish();
						}
					});
				}
			} catch (Exception e) {
				Log.i(Splash.ListAdapter.class.toString(), e.getMessage());
			}
			return view;
		}
	}

}