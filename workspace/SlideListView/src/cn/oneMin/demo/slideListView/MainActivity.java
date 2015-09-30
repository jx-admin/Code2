package cn.oneMin.demo.slideListView;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

public class MainActivity extends Activity {

	private SlideListView slideListView;
	private SlideAdapter adapter;
	private List<Info> list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slidelist_layout);
		addInfo();
//		setListView();
		setItemListView();
	}
	
	public void setListView(){
		findViewById(R.id.sListViewItem).setVisibility(View.GONE);
		slideListView=(SlideListView) findViewById(R.id.sListView);
		adapter=new SlideAdapter(this);
		adapter.setInfoList(list);
		slideListView.setAdapter(adapter);	
	}
	
	public void setItemListView(){
		ListView lv=(ListView) findViewById(R.id.sListViewItem);
		lv.setVisibility(View.VISIBLE);

		slideListView=(SlideListView) findViewById(R.id.sListView);
		slideListView.setVisibility(View.GONE);
		
		View headerView = LayoutInflater.from( this).inflate( R.layout.headlayout, null);
		lv.addHeaderView(headerView);
		ItemSlideAdapter adapter=new ItemSlideAdapter(this);
		adapter.setInfoList(list);
		lv.setAdapter(adapter);	
	}
	
	public void addInfo(){
		list=new ArrayList<Info>();
		for(int i=0;i<40;i++){
			Info info=new Info("����"+i," �ֻ�"+i);
			list.add(info);
		}
		
	}
	
	

}
