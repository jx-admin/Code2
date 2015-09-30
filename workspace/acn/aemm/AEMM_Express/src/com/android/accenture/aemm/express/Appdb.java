package com.android.accenture.aemm.express;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import org.xmlpull.v1.XmlSerializer;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.android.accenture.aemm.express.GridAdapter.GridHolder;
import com.android.accenture.aemm.express.updataservice.ProfileContent.ApkProfileContent;

public class Appdb {
	public static final String LOGCAT="Appdb";
	public static final byte NEWAPP =-1;
	public static final byte UNINSTALLED=0;
//	public static final byte DOLOAD=1;
	public static final byte INSTALLED=2;
	public static final byte INSTALLING=3;
	private boolean isLock = false;
	private Object lockObject = new Object();
	boolean isBase64;
	private boolean isMark;
	
	private byte flag;
	boolean isAble=true;
	private long Id; // unique identification
	private String apkId;
	private String apkName;
	private String apkIconColor;
	private String apkIconGrey;
	private String apkFileName;
	private String apkFilePath;
	private String apkUrl;
	private String apkVersion;//add by fengyun
	private String apkVersionClient;
	private String apkDescription;
	private PackageInfo packageInfo;
	private String apkPackageName;
	
	
	public ApkProfileContent toApkProfileContent(){
		ApkProfileContent apc=new ApkProfileContent();
		apc.mId=Id;
		apc.mApkId=apkId;
		apc.mApkName=apkName;
		apc.mApkUrl=apkUrl;
		apc.mApkDesc=apkDescription;
		apc.mApkVersion=apkVersion;
		apc.mApkVersionClient=apkVersionClient;
		apc.mApkFlag=""+flag;
		apc.mIconColor=apkIconColor;
		apc.mIconGrey=apkIconGrey;
		apc.mApkFileName=apkFileName;
		apc.mApkFilePath=apkFilePath;
		apc.mApkInstallEnabled=""+isAble;
		apc.mApkPackageName=getPackageName();
		return apc;
	}
	public Appdb(ApkProfileContent apc){
		set(apc);
	}
	
	public Appdb(String name,String icon,String icon2,String url){
		this(name,icon,icon2,url,null);
	}
	public Appdb(String name,String icon,String icon2,String url,String decrip){
		apkName=name;
		apkIconColor=icon;
		apkIconGrey=icon2;
		apkUrl=url;
		apkDescription=decrip;
//		apkFilePath=Utils.APKPATH;
	}
	public Appdb(){}
	public void set(ApkProfileContent apc){
		Id=apc.mId; // unique identification
		apkId=apc.mApkId;
		apkName=apc.mApkName;
		try{flag=Byte.parseByte(apc.mApkFlag);}
		catch(Exception e){flag=NEWAPP;}
		apkIconColor=apc.mIconColor;
		apkIconGrey=apc.mIconGrey;
		apkFileName=apc.mApkFileName;
		apkFilePath=apc.mApkFilePath;
		apkUrl=apc.mApkUrl;
		apkVersion=apc.mApkVersion;//add by fengyun
		apkVersionClient=apc.mApkVersionClient;
		apkDescription=apc.mApkDesc;
		setPackageName(apc.mApkPackageName);
		try{isAble=Boolean.parseBoolean(apc.mApkInstallEnabled);}
		catch(Exception e){isAble=true;}
		isBase64=true;
	}
	public void toSharedPreferences(SharedPreferences.Editor editor,int position){
		Log.v("VV","tosave："+position);
		
        //保存组件中的值
        editor.putInt(position+"flag",flag);
        editor.putBoolean(position+"isEdit",isMark);
        editor.putString(position+"apkName", apkName);
        editor.putString(position+"apkIcon", apkIconColor);
        editor.putString(position+"apkIcon2", apkIconGrey);
        editor.putString(position+"apkFileName", apkFileName);
        editor.putString(position+"apkFilePath", apkFilePath);
        editor.putString(position+"apkUrl", apkUrl);
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
		isMark=preferences.getBoolean(position+"isEdit",false);
		apkName=preferences.getString(position+"apkName","");
		apkIconColor=preferences.getString(position+"apkIcon","");
		apkIconGrey=preferences.getString(position+"apkIcon2","");
		apkFileName=preferences.getString(position+"apkFileName","");
		apkFilePath=preferences.getString(position+"apkFilePath","");
		apkUrl=preferences.getString(position+"apkUrl","");
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
	private void writeXml(XmlSerializer serializer){
//    	XmlSerializer serializer = Xml.newSerializer();
//    	StringWriter writer = new StringWriter();
    	try{
//		serializer.setOutput(writer);

    	// <?xml version=”1.0″ encoding=”UTF-8″ standalone=”yes”?>
//    	serializer.startDocument("UTF-8",true);

    	// <blog number="1″>
    	serializer.startTag("","app");
    	serializer.attribute("","app id",this.apkId);
    	serializer.attribute("","name",this.apkName);
//     	serializer.attribute("","version",this.version);
    	serializer.attribute("","desc",this.apkDescription);
    	}
    	catch(Exception e)
    	{
    	throw new RuntimeException(e);
    	}
    }
	public String getInfo(){
		String info="flag:"+flag+
		" isEdit:"+isMark
		+" Id:"+Id
		+" apkId:"+apkId
		+" apkName:"+apkName
		+" isAble:"+isAble
		+" apkUrl:"+apkUrl
		+" apkFilePath:"+apkFilePath
		+" apkVersion:"+apkVersion
		+" apkVersionClient:"+apkVersionClient
		+" apkIcon:"+apkIconColor
		+" apkIcon2:"+apkIconGrey
		+" apkFile:"+apkFileName
		+" apkDescription:"+apkDescription
		+" packageName:"+getPackageName();
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
	public void setFlag(Context context,byte flag) {
		if(flag==this.flag){
			return;
		}
		this.flag = flag;
		toSave(context);
	}
	public void toSave(Context context){
		ApkProfileContent.updateApkContentwithRowId(context,Id,toApkProfileContent().toContentValues());
	}
	public boolean isMark() {
		return isMark;
	}
	public void setMark(boolean isEdit) {
		this.isMark = isEdit;
	}
	
	public String getApkName() {
		return apkName;
	}
	public void setApkName(String apkName) {
		this.apkName = apkName;
	}
	public String getApkIcon() {
		return apkIconColor;
	}
	public void setApkIcon(String apkIcon) {
		this.apkIconColor = apkIcon;
	}
	public String getApkIcon2() {
		return apkIconGrey;
	}
	public void setApkIcon2(String apkIcon2) {
		this.apkIconGrey = apkIcon2;
	}
	public String getApkFileName() {
		return apkFileName;
	}
	public void setApkFileName(String apkFile) {
		this.apkFileName = apkFile;
	}
	public String getApkDescription() {
		return apkDescription;
	}
	public void setApkDescription(String apkDescription) {
		this.apkDescription = apkDescription;
	}
	public void setApkUrl(String apkUrl) {
		this.apkUrl = apkUrl;
	}
	public String getApkUrl() {
		return apkUrl;
	}
	public void setApkFilePath(String apkFilePath) {
		this.apkFilePath = apkFilePath;
	}
	public String getApkFilePath() {
		return apkFilePath;
	}
	public String getApkFullPath(){
		String path=apkFilePath;
		if(path!=null){
			path+="/"+apkFileName;
		}else{
			path=apkFileName;
		}
		return path;
	}
	
	public void setApkId(String apkId) {
		this.apkId = apkId;
	}
	public String getApkId() {
		return apkId;
	}
	public void setId(long uid) {
		this.Id = uid;
	}
	public long getId()
	{
		return Id;
	}
	public void setApkVersion(String apkVersion) {
		this.apkVersion = apkVersion;
	}
	public String getApkVersion() {
		return apkVersion;
	}
	public void setApkVersionClient(String apkVersionClient){
		this.apkVersionClient=apkVersionClient;
	}
	public String getApkVersionClient(){
		return apkVersionClient;
	}
	public String getPackageName(){
		if(packageInfo!=null){
			return packageInfo.packageName;
		}else if(apkPackageName!=null){
			return apkPackageName;
		}
		return null;
	}
	public void setPackageName(String pk){
		apkPackageName=pk;
	}
	public Bitmap getBitmapColor(){
		Bitmap icon;
		if(isBase64){
			Log.v(LOGCAT,"base64 color icon file:"+apkIconColor);
			byte[] temp = Base64.decode(apkIconColor,0);
			InputStream is  = new ByteArrayInputStream(temp);
			icon = BitmapFactory.decodeStream(is);
		}else{
			Log.v(LOGCAT,"color icon file:"+apkIconColor);
			icon=BitmapFactory.decodeFile(apkIconColor);
		}
		return icon;
	}
	public Bitmap getBitmapGray(){
		Bitmap icon=null;
		if(isBase64){
			if(apkIconGrey!=null){
				Log.v(LOGCAT,"base64 aGrey icon file:"+apkIconGrey);
				byte[] temp = Base64.decode(apkIconGrey,0);
				InputStream is  = new ByteArrayInputStream(temp);
				icon = BitmapFactory.decodeStream(is);
			}
		}else{
			Log.v(LOGCAT,"Grey icon file:"+apkIconGrey);
			icon=BitmapFactory.decodeFile(apkIconGrey);
		}
		return icon;
	}
	public void deleteApkFile(){
		String apkPath=getApkFullPath();
		Log.v(LOGCAT,getApkName()+" path: "+apkPath);
    	if(apkPath!=null){
			File apkFile=new File(apkPath);
    		if(apkFile.exists()){
    			boolean delete=apkFile.delete();
    			Log.v(LOGCAT,apkPath+" deleted is "+delete);
    		}
    	}
	}
	public boolean equals(Appdb app){
		if(app!=null&&getApkId().equals(app.getApkId())&&getApkVersion().equals(app.getApkVersion())){
			return true;
		}
		return false;
	}
	
	public void setLock(boolean b){
		synchronized(lockObject) {
			isLock=b;
		}
	}
	
	public boolean isLock(){
		boolean rlt = false;
		synchronized(lockObject) {
			rlt = isLock;
		}
		return rlt;
	}
}
