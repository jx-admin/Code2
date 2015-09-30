package com.example.draglistview;

import java.util.ArrayList;

import com.example.draglistview.CYDragListView.CYDragListener;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements CYDragListener {
	private ArrayList<String> array;
	private CYDragListView listView;
	MyAdapter adapter ;
	ArrayList<String> datas;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		listView = new CYDragListView(this);
		array = new ArrayList<String>();
		listView.setDragListener(this);
		String aa[] = { "items1", "item2", "items3", "item4", "items5",
				"item6", "items7", "item8", "items9", "item10", "items11",
				"item12" };
		for (int i = 0; i < aa.length; i++) {
			array.add(aa[i]);
		}
		datas = new ArrayList<String>();
		for(int i = 0; i < aa.length; i ++){
			datas.add(aa[i]);
			
		}
		adapter = new MyAdapter(this, datas);
		listView.setAdapter(adapter);
		setContentView(listView);

	}

	@Override
	public void onDragFinsh(int position) {
		Toast.makeText(MainActivity.this,
				 "delete:" + position, Toast.LENGTH_SHORT)
				.show();
		datas.remove(position);
		adapter.notifyDataSetChanged();
		
	}

}
