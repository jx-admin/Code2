package com.android.accenture.aemm.express;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class PageIndexManager {
	public void refreshUindex(int count,int index){
		if(uninstalled_index_linear==null){
			Log.d(LOGCAT,"uninstalled_index_linear is null");
			return ;
		}
		int pages=count-uninstalled_index_linear.getChildCount();
			while(pages>0){
				addUindex();
				pages--;
			}
			while(pages<0){
				delUindex();
				pages++;
			}
			setCurrentUindex(index);
	}
	public void cleanUindex() {
		uninstalled_index_linear.removeAllViews();
		//需要清理内存
		unindexLs.clear();
	}
	public void addUindex() {
		LinearLayout v = (LinearLayout) mInflater.inflate(R.layout.index_item,
				null);
		ImageView iv = (ImageView) v.findViewById(R.id.index_iv);
		v.removeAllViews();
		unindexLs.add(iv);
		uninstalled_index_linear.addView(iv);
	}
	public void delUindex() {
		if (unindexLs.size() > 0) {
			unindexLs.remove(unindexLs.size() - 1);
			uninstalled_index_linear.removeViewAt(uninstalled_index_linear
					.getChildCount() - 1);
		}
	}
	public void setCurrentUindex(int pages){
		Log.d(LOGCAT,"pages="+pages+" uncurentPage="+uncurentPage);
		if(pages!=uncurentPage){
			if(pages>=0&&pages<uninstalled_index_linear.getChildCount()){
				if (uncurentPage >= 0&&uncurentPage<uninstalled_index_linear.getChildCount()) {
					unindexLs.get(uncurentPage).setImageResource(R.drawable.index2);
				}
				unindexLs.get(pages).setImageResource(R.drawable.index1);
				uncurentPage = pages;
			}
		}
		
	}
	public void setLinearView(LinearLayout index_linear){
		unindexLs.clear();
		uncurentPage=-1;
		uninstalled_index_linear=index_linear;
	}
	public PageIndexManager(Context context){
			unindexLs = new ArrayList<ImageView>();
			mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	public int getCurrent(){
		return uncurentPage;
	}
	LayoutInflater mInflater;
	LinearLayout uninstalled_index_linear;
	private int uncurentPage = -1;
	private List<ImageView> unindexLs;
	public static final String LOGCAT="PageIndexManager";
}
