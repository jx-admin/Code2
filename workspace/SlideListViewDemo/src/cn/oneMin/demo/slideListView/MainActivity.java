package cn.oneMin.demo.slideListView;

import java.util.ArrayList;
import java.util.List;

import cn.oneMin.demo.slideListView.Info;
import cn.oneMin.demo.slideListView.SlideAdapter;
import cn.oneMin.demo.slideListView.SlideListView;

import com.android.wu.slidelistview.R;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.app.Activity;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {

	private SlideListView slideListView;
	private SlideAdapter adapter;
	private List<Info> list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slidelist_layout);
		addInfo();
		slideListView=(SlideListView) findViewById(R.id.sListView);
		adapter=new SlideAdapter(this);
		adapter.setInfoList(list);
		slideListView.setAdapter(adapter);	
	}
	
	public void addInfo(){
		list=new ArrayList<Info>();
		for(int i=0;i<40;i++){
			Info info=new Info("bbbbbb"+i," aaaaa"+i);
			list.add(info);
		}
		
	}
	
	

}
