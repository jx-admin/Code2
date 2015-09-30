package com.android.accenture.aemm.dome;

import java.util.ArrayList;
import java.util.List;

import com.android.accenture.aemm.dome.GridAdapter.GridHolder;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;


public class ApkManager {
	public static final byte FINISH_DOLOAD=1,ADD_APP=2,REMOVE_APP=3,INSTALL=4,UNINSTALL=5;
	private Logger log=LoggerFactory.getLogger(AppAdaper.class);
	Appdb curEditApp;
	private boolean curEditTouched;
    private GridView appGv ;
    private AppAdaper appAdapter;
    private Context context;
    public ApkManager(Context context,GridView gridView){
    	this.context=context;
    	appGv=gridView;
        initapkRoom();
    }
    private void initapkRoom(){ 
        appAdapter = new AppAdaper(context,this);
        appAdapter.setList(new ArrayList<Appdb>());
        Appdb test1=new Appdb("RSS",Utils.ICONPATH+"/temp01.png",Utils.ICONPATH+"/temp01.png",null);
        test1.setFlag(Appdb.ENABLE);
        appAdapter.add(test1);
        Appdb test2=new Appdb("Chat",Utils.ICONPATH+"/temp02.png",Utils.ICONPATH+"/temp02.png",null);
        test2.setFlag(Appdb.ENABLE);
        appAdapter.add(test2);
        
        
//        appLs.add(new Appdb("不要碰我",Utils.ICONPATH+"/biepengwo.png",Utils.ICONPATH+"/biepengwo_1.png",Utils.APKPATH+"/biepengwo.apk"));
//        appLs.add(new Appdb("火影五子棋",Utils.ICONPATH+"/huoyingfive.png",Utils.ICONPATH+"/huoyingfive_1.png",Utils.APKPATH+"/huoyingfive.apk"));
//        appLs.add(new Appdb("ACV",Utils.ICONPATH+"/net.androidcomics.acv.png",Utils.ICONPATH+"/net.androidcomics.acv_1.png",Utils.APKPATH+"/net.androidcomics.acv.apk"));
//        appLs.add(new Appdb("name4",Utils.ICONPATH+"/icon.png",Utils.ICONPATH+"/icon_1.png",Utils.APKPATH+"/net.androidcomics.acv.apk"));
//        appLs.add(new Appdb("name5",Utils.ICONPATH+"/icon.png",Utils.ICONPATH+"/icon_1.png",Utils.APKPATH+"/net.androidcomics.acv.apk"));
//        appLs.add(new Appdb("name6",Utils.ICONPATH+"/icon.png",Utils.ICONPATH+"/icon_1.png",Utils.APKPATH+"/net.androidcomics.acv.apk"));
//        appLs.add(new Appdb("name7",Utils.ICONPATH+"/icon.png",Utils.ICONPATH+"/icon_1.png",Utils.APKPATH+"/net.androidcomics.acv.apk"));
//        appLs.add(new Appdb("name8",Utils.ICONPATH+"/icon.png",Utils.ICONPATH+"/icon_1.png",Utils.APKPATH+"/net.androidcomics.acv.apk"));
//        appLs.add(new Appdb("name9",Utils.ICONPATH+"/icon.png",Utils.ICONPATH+"/icon_1.png",Utils.APKPATH+"/net.androidcomics.acv.apk"));
        
        toRread(context.getSharedPreferences(ApkHall.dataname, Activity.MODE_PRIVATE));
        appGv.setAdapter(appAdapter);
        appGv.setOnItemClickListener(itemClicker);
        appGv.setOnItemLongClickListener(itemLongClicker);
        appGv.setOnTouchListener(touchListener);
    }
    private android.view.View.OnTouchListener touchListener=new android.view.View.OnTouchListener(){

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			Log.v("VV","onTouch "+event.getAction());
			if(event.getAction()==android.view.MotionEvent.ACTION_UP){
				Log.v("VV","onTouch up");
				if(curEditTouched){
					curEditTouched=false;
				}else if(curEditApp!=null){
					((GridHolder)curEditApp.view.getTag()).editImage.setVisibility(View.GONE);
					curEditApp=null;
				}
			}
			return false;
		}
    	
    };
    private android.widget.AdapterView.OnItemClickListener  itemClicker=new android.widget.AdapterView.OnItemClickListener() {

		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long arg3) {
			Log.v("VV","onItemClick "+position);
			Appdb appdb = (Appdb) arg0.getAdapter().getItem(position);
			if(curEditApp!=null){
				curEditTouched = false;
				if (curEditApp.getApkName().equalsIgnoreCase(appdb.getApkName())) {
					curEditTouched = true;
				}
				return;
			}
			GridAdapter.GridHolder holder = (GridAdapter.GridHolder) view.getTag();
			switch (appdb.getFlag()) {
			case Appdb.UPDATA_NEW:// =0,
				holder.editImage.setVisibility(View.GONE);
			case Appdb.NEVER_SETUP:// =1,
			case Appdb.UNINSTALLED:// =3;
				appdb.setFlag(Appdb.DOLOAD);
				holder.progressBar.setVisibility(View.VISIBLE);
				new Loader(appdb).start();
				break;
			case Appdb.DOLOAD://
				break;
			case Appdb.INSTALLED:// =2,
				if (appdb.packageInfo != null) {
					if (appdb.packageInfo.activities != null) {
						Log.v("V", "activitys:"+ appdb.packageInfo.activities.length);
						for (ActivityInfo ainfo : appdb.packageInfo.activities) {
							try {
								context.startActivity(Utils.startApplication(ainfo));
							} catch (Exception e) {
							}
						}
						break;
					}
					Log.v("V","程序错误:"+appdb.getApkName());
					appdb.setFlag(Appdb.UNINSTALLED);
				}
				break;
			case Appdb.ENABLE:
				break;
			}
			// appGv.invalidate();
			// appGv.invalidateViews();
		}
	};
	 private android.widget.AdapterView.OnItemLongClickListener itemLongClicker=new android.widget.AdapterView.OnItemLongClickListener(){

			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				Appdb dApp=(Appdb)parent.getAdapter().getItem(position);
				Log.v("VV","onItemLongClick "+position);
				if(curEditApp!=null&&curEditApp.getApkName().equalsIgnoreCase(dApp.getApkName())){
					curEditTouched=true;
					return false;
				}else{
					if(dApp.getFlag()!=Appdb.INSTALLED){
						return false;
					}
					if(curEditApp!=null){
						((GridHolder)curEditApp.view.getTag()).editImage.setVisibility(View.GONE);
					}
					curEditApp=dApp;
					curEditTouched=true;
					GridHolder holder = (GridHolder)view.getTag();
					holder.editImage.setVisibility(View.VISIBLE);
				}
//				appGv.setFocusable(true);
//				appGv.requestFocus();
//				appGv.setFocusableInTouchMode(true);
//				appGv.setSelected(true);
//				appGv.setSelection(position);
				return false;
			}
	    };
	class Loader extends Thread {
		Appdb appdb;
		int current=0;
		int max=100;
		ProgressBar  pBar;
		GridAdapter.GridHolder mGridHolder;
		public Loader(Appdb app){
			appdb=app;
			mGridHolder=(GridHolder) appdb.view.getTag();
			this.pBar=mGridHolder.progressBar;
			if(pBar.getMax()!=0){
				max=pBar.getMax();
			}
			current=pBar.getProgress();
		}
		public void run(){
			while(current<max){
				current+=10;
				pBar.setProgress(current);
				Log.v("V","loader:"+current);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			Message message = new Message();  
            message.what =FINISH_DOLOAD;
            message.obj=appdb;
            appdb.setFlag(Appdb.UNINSTALLED);
            handler.sendMessage(message);
		}
	};
	Handler handler=new Handler(){
		public void handleMessage(Message msg) {  
            switch (msg.what) {  
            case FINISH_DOLOAD:
            	log.d("apkManager handler get message :download finished");
            	((GridHolder)((Appdb)msg.obj).view.getTag()).progressBar.setVisibility(View.GONE); 
                installApplication((Appdb)msg.obj);
                break;
            case ADD_APP:
            	log.d("apkManager handler get message :add app "+((Appdb) msg.obj).getApkName());
            	if(appAdapter.getId((Appdb) msg.obj)>=0){
            		log.d("apkManager app already exist");
            		break;
            	}
            	appAdapter.add((Appdb) msg.obj);
            	appGv.setAdapter(appAdapter);
                break;  
            case REMOVE_APP:
            	log.d("apkManager handler get message :remove app "+msg.arg1);
            	appAdapter.remove(msg.arg1);
                appGv.setAdapter(appAdapter);
                break;
            case INSTALL:
            	log.d("apkManager handler get message :installed app "+msg.arg1);
            	Appdb iApp=appAdapter.getPackage((String) msg.obj);
            	if(iApp!=null){
					iApp.setFlag(Appdb.INSTALLED);
					((GridHolder)iApp.view.getTag()).appImage.setImageBitmap(BitmapFactory.decodeFile(iApp.getApkIcon()));
				}
            	break;
            case UNINSTALL:
            	log.d("apkManager handler get message :uninstall app "+msg.arg1);
            	Appdb uApp=appAdapter.getPackage((String) msg.obj);
            	if(uApp!=null&&uApp.getFlag()==Appdb.INSTALLED){
            		if(uApp.packageInfo!=null&&((String) msg.obj).equals(uApp.packageInfo.packageName)){
            			uApp.setFlag(Appdb.UNINSTALLED);
            			((GridHolder)uApp.view.getTag()).appImage.setImageBitmap(BitmapFactory.decodeFile(uApp.getApkIcon2()));
            			int id=appAdapter.getId(uApp);
            			if(id>=0){
            				deleteApp(id);
            			}
            		}
            	}
            	break;
            }
		}
	};
	
	public void addApp(Appdb app){
		Message message = new Message();  
        message.what =ADD_APP;
        message.obj=app;
        handler.sendMessage(message);
	}
	
	public void deleteApp(int location){
		Message message = new Message();  
        message.what =REMOVE_APP;
        message.arg1=location;
        handler.sendMessage(message);
	}

	protected void installApplication(Appdb app) {
		if(app.getApkFile()!=null){
			if(app.packageInfo==null||app.packageInfo.packageName==null){
				app.packageInfo=Utils.getUninatllApkInfo(context,app.getApkFile());
			}
			if(app.packageInfo!=null&&app.packageInfo.packageName!=null){
				log.d("apkManager to install apk : "+app.getApkFile()+" "+app.packageInfo.packageName);
			}
			context.startActivity(Utils.installApplication(app.getApkFile()));
		}
	}

	public void toSave(SharedPreferences.Editor editor) {
		editor.putInt("count", appAdapter.getCount());
		for (int i = 0; i < appAdapter.getCount(); i++) {
			appAdapter.getItem(i).toSharedPreferences(editor, i);
		}
		editor.commit();
	}

	public void toRread(SharedPreferences preferences) {
		int count = preferences.getInt("count", 0);
		Log.v("VV", "preferences :" + count);
		if (count <= 0) {
			return;
		}
		if (appAdapter != null) {
			appAdapter.clear();
		}
		for (int i = 0; i < count; i++) {
			Appdb app = new Appdb();
			app.formSharedPreferences(preferences, i);
			appAdapter.add(app);
			Log.v("VV", "preferences :" + app.getInfo());
		}
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(curEditApp!=null){
			((GridHolder)curEditApp.view.getTag()).editImage.setVisibility(View.GONE);
			curEditApp=null;
			return true;
		}
		return false;
	}
	public void onConfigurationChanged(Configuration newConfig) {
		appGv.setAdapter(appAdapter);
	}
}
