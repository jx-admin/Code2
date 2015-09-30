package com.baidu.lbsapi.panoramaviewdemo;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class BMapApiDemoMain extends Activity {
	
	
	public static final int PID = 1; //Pid方式
	public static final int GEO = 2; //经纬度方式
	public static final int MERCATOR = 3;//墨卡托方式
	public static final int MARKER = 4; // 标注
	public static final int INDOOR = 5;//内景
	public int mType;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		ListView mListView = (ListView)findViewById(R.id.listView); 
		// 添加ListItem，设置事件响应
        mListView.setAdapter(new DemoListAdapter());
        mListView.setOnItemClickListener(new OnItemClickListener() {  
            public void onItemClick(AdapterView<?> arg0, View v, int index, long arg3) {  
            	onListItemClick(index);
            }  
        });  
    }

    void onListItemClick(int index) {
		Intent intent = null;
		intent = new Intent(BMapApiDemoMain.this, demos[index].demoClass);
		intent.putExtra("type", demos[index].type);
		this.startActivity(intent);
    }
	
	private static final DemoInfo[] demos = {
		
        new DemoInfo(PID, R.string.demo_title_panorama,
                     R.string.demo_desc_panorama1, PanoramaDemoActivityMain.class),
        new DemoInfo(GEO, R.string.demo_title_panorama,
        		R.string.demo_desc_panorama2, PanoramaDemoActivityMain.class),
        new DemoInfo(MERCATOR, R.string.demo_title_panorama,
        		R.string.demo_desc_panorama3, PanoramaDemoActivityMain.class),
        new DemoInfo(MARKER, R.string.demo_title_panorama,
        		R.string.demo_desc_panorama4, PanoramaDemoActivityMain.class),
        new DemoInfo(INDOOR, R.string.demo_title_panorama,
        		R.string.demo_desc_panorama5, PanoramaDemoActivityMain.class)        
	};
	
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	// 建议在APP整体退出之前调用MapApi的destroy()函数，不要在每个activity的OnDestroy中调用，
    // 避免MapApi重复创建初始化，提高效率
	protected void onDestroy() {
	    DemoApplication app = (DemoApplication)this.getApplication();
		if (app.mBMapManager != null) {
			app.mBMapManager.destroy();
			app.mBMapManager = null;
		}
		super.onDestroy();
		System.exit(0);
	}
	
	private  class DemoListAdapter extends BaseAdapter {
		public DemoListAdapter() {
			super();
		}

		@Override
		public View getView(int index, View convertView, ViewGroup parent) {
			convertView = View.inflate(BMapApiDemoMain.this, R.layout.demo_info_item, null);
			TextView title = (TextView)convertView.findViewById(R.id.title);
			TextView desc = (TextView)convertView.findViewById(R.id.desc);
			
			title.setText(demos[index].title);
			desc.setText(demos[index].desc);
			return convertView;
		}
		@Override
		public int getCount() {
			return demos.length;
		}
		@Override
		public Object getItem(int index) {
			return  demos[index];
		}

		@Override
		public long getItemId(int id) {
			return id;
		}
	}
	
	
   private static class DemoInfo{
		private final int title;
		private final int desc;
		private final int type;
		private final Class<? extends Activity> demoClass;

		public DemoInfo(int type, int title , int desc,Class<? extends Activity> demoClass) {
			this.title = title;
			this.desc  = desc;
			this.type = type;
			this.demoClass = demoClass;
		}
	}
}