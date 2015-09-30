package com.android.accenture.aemm.express;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

import com.android.accenture.aemm.express.app.ScrollLayout;
import com.android.accenture.aemm.express.updataservice.ProfileContent.ApkProfileContent;

public abstract class ScrollAdapter extends GridAdapter{
	public final static String APPLOCK="APPLOCK"; 
	ScrollLayout installedSl;
	public final static String LOGCAT="ScrollAdapter";
	Context context;
	LayoutInflater mInflater;
	List <View>list;
	
	public ScrollAdapter(Context c) {
		super();
		Log.v(LOGCAT,"ScrollAdapter");
		this.context = c;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		list=new ArrayList<View>();
	}
	public void addItem(AppItem item) {
			setItemView(item);
			if(item.getFlag()==Appdb.NEWAPP){
//				int i=0;
//				int count=installedSl.getChildCount();
//				for(;i<count;i++){
//					View v=installedSl.getChildAt(i);
//					AppItem app=((GridAdapter.GridHolder)v.getTag()).app;
//					Log.v(LOGCAT,"count ="+count+"i "+i+app.getApkName());
//					if(app.getFlag()!=Appdb.NEWAPP){
//						break;
//					}
//				}
//				installedSl.addView(item.getView(), i);
//				list.add(item.getView());
//				Log.v(LOGCAT,"size="+count+" index="+i+" mark befor refreshPages "+(item.getView()==null));
				list.add(0,item.getView());
				installedSl.addView(item.getView(),0);
			}else{
				Log.v(LOGCAT," befor refreshPages "+(item.getView()==null));
				installedSl.addView(item.getView());
				list.add(item.getView());
			}
	}
	public int deleteById(long id){
		int location=-1;
		AppItem app;
		int i=0;
		int count=installedSl.getChildCount();
		for(;i<count;i++){
			View v=installedSl.getChildAt(i);
			app=((GridAdapter.GridHolder)v.getTag()).app;
			if(app.getId()==id){
				Log.v(LOGCAT,"count ="+count+"i "+i+app.getApkName()+" is delete.");
				installedSl.removeViewAt(i);
				list.remove(i);
				location=i;
				break;
			}
		}
		return location;
	}
	public int deleteItemByApkId(String apkId,String version){
		int location=-1;
		if(apkId==null||version==null){
			return location;
		}
		AppItem app;
		int i=0;
		int count=installedSl.getChildCount();
		for(;i<count;i++){
			View v=installedSl.getChildAt(i);
			app=((GridAdapter.GridHolder)v.getTag()).app;
			if(app.getApkId().equals(apkId)&&app.getApkVersion().equals(version)){
				Log.v(LOGCAT,"count ="+count+"i "+i+app.getApkName()+" is delete.");
				installedSl.removeViewAt(i);
				list.remove(i);
				location=i;
				break;
			}
		}
		return location;
	}
	public int deleteItemByPackageVersion(String pkgName,String version){
		int location=-1;
		if(pkgName==null||version==null){
			return location;
		}
		AppItem app;
		int i=0;
		int count=installedSl.getChildCount();
		for(;i<count;i++){
			View v=installedSl.getChildAt(i);
			app=((GridAdapter.GridHolder)v.getTag()).app;
			if(app.getPackageName().equals(pkgName)&&app.getApkVersionClient().equals(version)){
				Log.v(LOGCAT,"count ="+count+"i "+i+app.getApkName()+" is delete.");
				installedSl.removeViewAt(i);
				list.remove(i);
				location=i;
				break;
			}
		}
		return location;
	}
	abstract void setItemView(AppItem app);

	public int getCount() {
		if(installedSl==null){
			return 0;
		}
		int count=installedSl.getChildCount();
		Log.v("VV","getCount:"+count);
		return count;
	}

	public Object getItem(int index) {
		if(index>=installedSl.getChildCount()){
			Log.v("VV","getItem out of index"+index);
			return null;
		}
		Log.v("VV","getItem:"+index);
		View v=installedSl.getChildAt(index);
		AppItem app=((GridAdapter.GridHolder)v.getTag()).app;
		return app;
	}
	public AppItem getItemById(String apkId) {
		if(apkId==null){
			return null;
		}
		AppItem app;
		View v;
		int count=installedSl.getChildCount();
		for(int i=0;i<count;i++){
			v=installedSl.getChildAt(i);
			app=((GridAdapter.GridHolder)v.getTag()).app;
			if(app==null){
				Log.d(LOGCAT,"ScrollAdapter getItem null "+i);
				continue;
			}
			if(apkId.equals(app.getApkId())){
				return app;
			}
		}
		return null;
	}
	public AppItem getItemById(String apkId,String version) {
		if(apkId==null&&version==null){
			return null;
		}
		AppItem app;
		View v;
		int count=installedSl.getChildCount();
		for(int i=0;i<count;i++){
			v=installedSl.getChildAt(i);
			app=((GridAdapter.GridHolder)v.getTag()).app;
			if(app==null){
				Log.d(LOGCAT,"ScrollAdapter getItem null "+i);
				continue;
			}
			if(apkId.equals(app.getApkId())&&version.equals(app.getApkVersion())){
				return app;
			}
		}
		return null;
	}
	public AppItem getPackageItem(String pkg){
		if(pkg==null){
			return null;
		}
		AppItem app;
		View v;
		int count=installedSl.getChildCount();
		for(int i=0;i<count;i++){
			v=installedSl.getChildAt(i);
			app=((GridAdapter.GridHolder)v.getTag()).app;
			if(app==null){
				Log.d(LOGCAT,"ScrollAdapter getItem null "+i);
				continue;
			}
			if(pkg.equals(app.getPackageName())){
				return app;
			}
		}
		return null;
	}
	public AppItem getPackageItem(String pkg,String version){
		if(pkg==null||version==null){
			return null;
		}
		AppItem app;
		View v;
		int count=installedSl.getChildCount();
		for(int i=0;i<count;i++){
			v=installedSl.getChildAt(i);
			app=((GridAdapter.GridHolder)v.getTag()).app;
			if(app==null){
				Log.d(LOGCAT,"ScrollAdapter getItem null "+i);
				continue;
			}
			if(version.equals(app.getApkVersionClient())&&pkg.equals(app.getPackageName())){
				return app;
			}
		}
		return null;
	}
	
	public long getItemId(int index) {
		Log.v("VV","getItemId:"+index);
		return index;
	}

	public View getView(int index, View convertView, ViewGroup parent) {
		
		Log.v("VV","getView:"+index+(parent.getId()==R.id.installed_gl)+" w="+parent.getWidth()+" h="+parent.getHeight());
//		convertView=pageLs.get(index);
		return list.get(index);
	}

	public void setAppAbled(String []ids,boolean[]enable){
		if(ids==null||enable==null){
			Log.e(LOGCAT,"setAppAbled->ids||enable is null");
			return ;
		}
		int idLength=ids.length;
		int ableLength=enable.length;
		idLength=idLength<ableLength?idLength:ableLength;
		int count=installedSl.getChildCount();
		View v;
		AppItem app;
		for(int i=0;i<idLength;i++){
			Log.v(APPLOCK,i+"--apKid:"+ids[i]+" enalbe:"+enable[i]);
			for(int j=0;j<count;j++){
				v=installedSl.getChildAt(j);
				app=((GridAdapter.GridHolder)v.getTag()).app;
				if(app.getApkId().equals(ids[i])){
					app.setAble(enable[i]);
//					break;可能多个不同版本
				}
//				Log.v(APPLOCK,i+"  apKid:"+app.getApkId()+" enalbe:"+app.isAble());
			}
		}
	}
	public void toSave(Editor editor) {
		int count=installedSl.getChildCount();
		View v;
		AppItem app;
		if(editor!=null){
			editor.putInt("count", count);
			for (int i = 0; i < count; i++) {
				v=installedSl.getChildAt(i);
				app=((GridAdapter.GridHolder)v.getTag()).app;
				app.toSharedPreferences(editor, i);
			}
			editor.commit();
		}else{
			Log.v(LOGCAT,"apk to save db");
			ApkProfileContent apc;
			
			for (int i = 0; i <count; i++) {
				v=installedSl.getChildAt(i);
				app=((GridAdapter.GridHolder)v.getTag()).app;
				if(app==null){
					Log.v(LOGCAT,"app is null.");
					continue;
				}
//				Log.v(LOGCAT,"app:"+app.getInfo());
				apc=app.toApkProfileContent();
//				int r=
				ApkProfileContent.updateApkContentwithRowId(context, apc.mId, apc.toContentValues());
			}
			
		}
	}
	public abstract void toRead();
	
	public void toRead(Context context,byte flag){
		List <ApkProfileContent> apkUninstallList=ApkProfileContent.queryApkContentswithFlag(context,flag);
		if(apkUninstallList!=null&&apkUninstallList.size()>0){
			AppItem app;
			for(ApkProfileContent pro:apkUninstallList){
				app=new AppItem(pro);
				addItem(app);
//				Log.v(LOGCAT,"read uninstalled app:"+app.getInfo());
			}
		}
	}
	
	public void exit(){
		AppItem app;
		int count=list.size();
		View v;
		for(int i=0;i<count;i++){
			v=list.get(i);
			app=((GridAdapter.GridHolder)v.getTag()).app;
			if(app.isMark()){
				app.setMark(false);
//				installedSl.removeViewAt(i);
				list.remove(i);
//				installedSl.addView(v);
				list.add(v);
			}else{
				break;
			}
		}
	}
	public int getNewppCnt(){
		int count=0;
		AppItem app;
		int size=list.size();
		for(int i=0;i<size;i++){
			app=((GridAdapter.GridHolder)list.get(i).getTag()).app;
			if(app.getFlag()==Appdb.NEWAPP){
				count++;
			}
		}
		return count;
	}
	public void setScroll(ScrollLayout installedSl) {
		if(this.installedSl!=null){
			this.installedSl.removeAllViews();
		}
		this.installedSl=installedSl;
		installedSl.setOnClickListener(scrollClicker);
	}

	OnClickListener scrollClicker=new OnClickListener() {
		
		public void onClick(View arg0) {}
	};
	public void initData(){
		installedSl.removeAllViews();
		int count=list.size();
		Log.d(LOGCAT,"ScrollAdapter initData count="+count);
		for(int i=0;i<count;i++){
			View v=list.get(i);
			installedSl.addView(v);
		}
	}
	public void onDestroy() {
		AppItem app;
		View v;
		int count=list.size();
		for(int i=0;i<count;i++){
			v=list.get(i);
			app=((GridAdapter.GridHolder)v.getTag()).app;
			if(app.getFlag()==AppItem.NEWAPP){
				app.setFlag(context, AppItem.UNINSTALLED);
			}
		}
		list.clear();
//		installedSl.removeAllViews();
	}
}
