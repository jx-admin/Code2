package com.android.study.abc.examples;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.study.abc.examples.exit.ExitApplication;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class ExamplesActivity extends Activity implements OnItemClickListener {
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.examples);
	        
//	        Notification n = new Notification(R.drawable.icon, "Service启动", System.currentTimeMillis());   
//	        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, ExitApplication.class), 0);   
//	        n.setLatestEventInfo(this, "任务标题", "任务内容", contentIntent);   
//	        nManager.notify(NOTIFICATION_ID, n); // 任务栏启动
	        
	        Intent intent = getIntent();
	        String path = intent.getStringExtra("com.example.android.apis.Path");
	        
	        if (path == null) {
	            path = "com.android.study.abc";
	        }
	        
	        ListView lView=(ListView) findViewById(R.id.lv_intent);
	        lView.setOnItemClickListener(this);
//	        Log.v("[mylog]","path:"+path);
	        lView.setAdapter(new SimpleAdapter(this, getData(path),
	                android.R.layout.simple_list_item_1, new String[] { "title" },
	                new int[] { android.R.id.text1 }));
	        lView.setTextFilterEnabled(true);
	    }

	    /**生成path下acitivtyIntent列表.
	     * @param prefix path 
	     * @return Intent列表
	     */
	    protected List getData(String prefix) {
	    	//Inent列表
	        List<Map> myData = new ArrayList<Map>();

	        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
//	        mainIntent.addCategory(Intent.CATEGORY_SAMPLE_CODE);
	        mainIntent.addCategory("com.android.stuly.abc.examples");

	        PackageManager pm = getPackageManager();
	        List<ResolveInfo> list = pm.queryIntentActivities(mainIntent, 0);

	        if (null == list)
	            return myData;

	        String[] prefixPath;
	        
	        if (prefix.equals("")) {
	            prefixPath = null;
	        } else {
	            prefixPath = prefix.split("\\.");
	        }
	        //用于存放路径，以防重复路径出现.(如当前某个路径多个子级Activity,那么只需要一个父目录即可)
	        Map<String, Boolean> entries = new HashMap<String, Boolean>();
	        //遍历所有ResolveInfo
	        for(ResolveInfo info:list){
	        	String panStr=info.activityInfo.name;
	        	if(panStr==null){
	        		panStr="";
	        	}
	        	String activityPackage=panStr.substring(0, panStr.lastIndexOf("."));
	        	if(prefix.equals(activityPackage)){//判断当前是路径还是activity页面
//	        		Log.v("[mylog]","panStr:"+panStr);
	        		CharSequence labelSeq = info.loadLabel(pm);
		            String label = labelSeq != null? labelSeq.toString(): info.activityInfo.name;
		            addItem(myData, label, activityIntent(
                            info.activityInfo.applicationInfo.packageName,
                            info.activityInfo.name));
	        		
	        	}else if(prefix.length()==0||panStr.startsWith(prefix)){//如果是根目录或是当前子级目录
		        	String []panArrStr=panStr.split("\\.");
	        		
	        		String nextLabel=prefixPath==null?panArrStr[0]:panArrStr[prefixPath.length];
	        		 if (entries.get(nextLabel) == null) {
	        			//如果该目录级不存在则加入该目录级
	        			 addItem(myData, nextLabel, browseIntent(prefix.equals("") ? nextLabel : prefix + "." + nextLabel));
	                        entries.put(nextLabel, true);//记录该目录级存在
	                    }
	        	}
	        }
	        Collections.sort(myData, sDisplayNameComparator);
	        return myData;
	    }

	    /**
	     * 比较器
	     */
	    private final static Comparator<Map> sDisplayNameComparator = new Comparator<Map>() {
	        private final Collator   collator = Collator.getInstance();
	        public int compare(Map map1, Map map2) {
	            return collator.compare(map1.get("title"), map2.get("title"));
	        }
	    };

	    /**生成一个新的Activity
	     * @param pkg Application包名
	     * @param componentName activityInfo名字
	     * @return ActivityIntent
	     */
	    protected Intent activityIntent(String pkg, String componentName) {
	        Intent result = new Intent();
	        result.setClassName(pkg, componentName);
	        return result;
	    }
	    
	    /**生成一个新的Activity浏览ApiDemos指定path目录下的activity
	     * @param path 包路径
	     * @return 一个新Acivitylist的initent
	     */
	    protected Intent browseIntent(String path) {
	        Intent result = new Intent();
	        result.setClass(this, ExamplesActivity.class);
	        result.putExtra("com.example.android.apis.Path", path);
	        return result;
	    }

	    /**插入一个intent子项到列表.
	     * 子项包括：title--name,intent--Intent.
	     * @param data 存放子项的集合.
	     * @param name title.
	     * @param intent title对应的Intent.
	     */
	    protected void addItem(List<Map> data, String name, Intent intent) {
	        Map<String, Object> temp = new HashMap<String, Object>();
	        temp.put("title", name);
	        temp.put("intent", intent);
	        data.add(temp);
	    }

	    /* ListView组件监听事件,进入子activity.
	     * (non-Javadoc)
	     * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int, long)
	     */
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Map map = (Map) arg0.getItemAtPosition(arg2);
	        Intent intent = (Intent) map.get("intent");
	        startActivity(intent);
		}
}