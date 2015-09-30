package com.android.accenture.aemm.express.app;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.GridView;

import com.android.accenture.aemm.express.AppItem;
import com.android.accenture.aemm.express.log.Logger;
import com.android.accenture.aemm.express.log.LoggerFactory;

public class AppManager {
	private Logger log;
	private Context mContext;
	ScrollLayout mScrollLayout;
	private List <AppItem>appList;
	private List <View>pageList;
	public AppManager(Context context,ScrollLayout scrollLayout){
		log=LoggerFactory.getLogger(this.getClass());
		mContext=context;
		mScrollLayout=scrollLayout;
		appList=new ArrayList<AppItem>();
		pageList=new ArrayList<View>();
	}
	
	public void addItem(AppItem item){
		log.i("addItem->"+item.toString());
		appList.add(item);
		int mode=appList.size()%AppAdapter.APP_PAGE_SIZE;
		if(mode==1){
			log.i("create new page -> "+mScrollLayout.getChildCount());
			
			GridView appPage = new GridView(mContext);
			// get the "i" page data
			appPage.setAdapter(new AppAdapter(mContext, appList,pageList.size()));
			appPage.setNumColumns(4);
			mScrollLayout.addView(appPage);
			pageList.add(appPage);
			refresh();
		}
	}
	
	public void deleteItem(String name){
		for(int i=appList.size()-1;i>=0;i--){
			if(appList.get(i).getApkName().equalsIgnoreCase(name)){
				appList.remove(i);
				if(appList.size()%AppAdapter.APP_PAGE_SIZE==0){
					pageList.remove(pageList.size()-1);
				}
				refresh();
				break;
			}
		}
		
	}
	
	public void refresh(){
		mScrollLayout.removeAllViews();
		int index=mScrollLayout.getCurScreen();
		for(int i=0;i<pageList.size();i++){
			GridView gv=(GridView)pageList.get(i);
//			gv.setAdapter(gv.getAdapter());
			mScrollLayout.addView(pageList.get(i));
		}
		if(index>=mScrollLayout.getChildCount()){
			mScrollLayout.snapToScreen(index-1);
		}
	}
	

}
