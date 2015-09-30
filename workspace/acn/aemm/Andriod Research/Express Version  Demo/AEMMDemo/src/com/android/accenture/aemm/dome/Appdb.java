package com.android.accenture.aemm.dome;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.util.Log;
import android.view.View;

public class Appdb {
	public static final byte UPDATA_NEW=0,DOLOAD=4,NEVER_SETUP=1,INSTALLED=2,UNINSTALLED=3,ENABLE=5;
	private byte flag;
	private boolean isEdit;
	private String apkName;
	private String apkIcon;
	private String apkIcon2;
	private String apkFile;
	private String apkDescription;
	private ActivityInfo activityInfo;
	PackageInfo packageInfo;
	View view;
	public Appdb(String name,String icon,String icon2,String file){
		apkName=name;
		apkIcon=icon;
		apkIcon2=icon2;
		apkFile=file;
	}
	public Appdb(){}
	
	public void toSharedPreferences(SharedPreferences.Editor editor,int position){
		Log.v("VV","tosave："+position);
		
        //保存组件中的值
		if(flag==DOLOAD){
			flag=UNINSTALLED;
		}
        editor.putInt(position+"flag",flag);
        editor.putBoolean(position+"isEdit",isEdit);
        editor.putString(position+"apkName", apkName);
        editor.putString(position+"apkIcon", apkIcon);
        editor.putString(position+"apkIcon2", apkIcon2);
        editor.putString(position+"apkFile", apkFile);
        editor.putString(position+"apkDescription", apkDescription);
//		packageInfo=preferences.getString("flag", Appdb.UNINSTALLED);
		if (packageInfo != null) {
			if (packageInfo.packageName != null) {
				editor.putString(position + "packageName",packageInfo.packageName);
			}
			if (packageInfo.activities != null) {
				editor.putInt(position + "activities",packageInfo.activities.length);
				int index = 0;
				Log.v("VV", "count: " + packageInfo.activities.length);
				for (ActivityInfo act : packageInfo.activities) {
					editor.putString(position + "activities" + index+ "packageName", act.packageName);
					editor.putString(position + "activities" + index + "name",act.name);
					Log.v("VV", position + ":" + act.packageName + " "+ act.name);
					index++;
				}
			}
		}
	}
	
	public void formSharedPreferences(SharedPreferences preferences,int position){
		flag=(byte) preferences.getInt(position+"flag", Appdb.UNINSTALLED);
		isEdit=preferences.getBoolean(position+"isEdit",false);
		apkName=preferences.getString(position+"apkName","");
		apkIcon=preferences.getString(position+"apkIcon","");
		apkIcon2=preferences.getString(position+"apkIcon2","");
		apkFile=preferences.getString(position+"apkFile","");
		apkDescription=preferences.getString(position+"apkDescription","");
//		activityInfo=preferences.getString("flag", Appdb.UNINSTALLED);
//		packageInfo=preferences.getString("flag", Appdb.UNINSTALLED);
		String packageName=preferences.getString(position + "packageName",null);
		if (packageName != null) {
			if(packageInfo==null){
				packageInfo=new PackageInfo();
			}
			packageInfo.packageName=packageName;
		}
		
		int count=preferences.getInt(position+"activities", 0);
		if(count>0){
			if(packageInfo==null){
				packageInfo=new PackageInfo();
			}
			packageInfo.activities=new ActivityInfo[count];
			for(int i=0;i<count;i++){
				packageInfo.activities[i]=new ActivityInfo();
				packageInfo.activities[i].packageName=preferences.getString(position+"activities"+i+"packageName",null);
				packageInfo.activities[i].name=preferences.getString(position+"activities"+i+"name",null);
			}
		}
	}
	public String getInfo(){
		String info="flag:"+flag+
		" isEdit:"+isEdit
		+" apkName:"+apkName
		+" apkIcon:"+apkIcon
		+" apkIcon2:"+apkIcon2
		+" apkFile:"+apkFile
		+" apkDescription:"+apkDescription;
		if(packageInfo!=null&&packageInfo.activities!=null){
			int index=0;
			Log.v("VV","count: "+packageInfo.activities.length);
			for(ActivityInfo act:packageInfo.activities){
				Log.v("VV",index+":"+act.packageName+" "+act.name);
				index++;
			}
		}
		return info;
	}
	public byte getFlag() {
		return flag;
	}
	public void setFlag(byte flag) {
		this.flag = flag;
	}
	public boolean isEdit() {
		return isEdit;
	}
	public void setEdit(boolean isEdit) {
		this.isEdit = isEdit;
	}
	public String getApkName() {
		return apkName;
	}
	public void setApkName(String apkName) {
		this.apkName = apkName;
	}
	public String getApkIcon() {
		return apkIcon;
	}
	public void setApkIcon(String apkIcon) {
		this.apkIcon = apkIcon;
	}
	public String getApkIcon2() {
		return apkIcon2;
	}
	public void setApkIcon2(String apkIcon2) {
		this.apkIcon2 = apkIcon2;
	}
	public String getApkFile() {
		return apkFile;
	}
	public void setApkFile(String apkFile) {
		this.apkFile = apkFile;
	}
	public String getApkDescription() {
		return apkDescription;
	}
	public void setApkDescription(String apkDescription) {
		this.apkDescription = apkDescription;
	}
	public ActivityInfo getActivityInfo() {
		return activityInfo;
	}
	public void setActivityInfo(ActivityInfo activityInfo) {
		this.activityInfo = activityInfo;
	}
	public View getView(){
		return view;
	}
	public void setView(View v){
		view=v;
	}
}
