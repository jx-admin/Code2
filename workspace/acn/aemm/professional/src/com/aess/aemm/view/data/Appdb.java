package com.aess.aemm.view.data;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.aess.aemm.db.ApkContent;
import com.aess.aemm.view.PagePosition;
import com.aess.aemm.view.ViewUtils;

public class Appdb {

	public static final byte NEWAPP           = 1;
	public static final byte UNINSTALLED      = 2;
	public static final byte DOWNLOAD         = 3;
	public static final byte INSTALLED        = 4;
	public static final byte REMOTE_UNINSTALL = 5;

	private boolean isBase64;
	private byte mApkFlag;
	ApkContent apkProfileContent;

	protected PagePosition mPagePosition=new PagePosition();
//	protected long mId; // unique identification
//	protected String mApkId;
//	protected String mApkName;
//	protected String mApkUrl;
//	protected String mApkDesc;
//	/**the version from server*/
//	protected String mApkVersion;
//	/**the version of the application not from server*/
//	protected String mApkVersionClient;
//	protected byte mApkFlag;
//	protected String mIconColor;
//	protected String mIconGrey;
//	protected String mApkFileName;
//	protected String mApkFilePath;
//	protected String mApkPackageName;
//	protected boolean mApkInstallEnabled=true;
//	protected boolean mApkRunningEnabled=true;
//	protected boolean undeploy;
//	protected int mApkPublished;
	
	public Appdb(){}
	public Appdb(ApkContent apc){
		this.apkProfileContent=apc;
		isBase64=true;
//		mPagePosition.index=apc.mApkScreen;
//		mPagePosition.page=apc.mApkScreen;
//		mPagePosition.row=apc.mApkRow;
//		mPagePosition.coloum=apc.mApkColumn;
		try{mApkFlag=Byte.parseByte(apkProfileContent.mApkFlag);}
		catch(Exception e){mApkFlag=NEWAPP;}
	}
	public Appdb(String name, String icon, String icon2, String url,String decrip) {
		if(apkProfileContent==null){
			apkProfileContent=new ApkContent();
			apkProfileContent.mId=-1;
		}
		apkProfileContent.mApkName=name;
		apkProfileContent.mIconColor=icon;
		apkProfileContent.mIconGrey=icon2;
		apkProfileContent.mApkUrl=url;
		apkProfileContent.mApkDesc=decrip;
	}
	public ApkContent toApkProfileContent(){
//		apkProfileContent.mApkScreen=mPagePosition.index;
//		apkProfileContent.mApkScreen=mPagePosition.page;
//		apkProfileContent.mApkRow=mPagePosition.row;
//		apkProfileContent.mApkColumn=mPagePosition.coloum;
		apkProfileContent.mApkFlag=Byte.toString(mApkFlag);
		return  apkProfileContent;
	}
	public boolean isBase64() {
		return isBase64;
	}
	public void setBase64(boolean isBase64) {
		this.isBase64 = isBase64;
	}
	public long getId() {
		return apkProfileContent.mId;
	}
	public void setId(long mId) {
		apkProfileContent.mId = mId;
	}
	public String getApkId() {
		if (null == apkProfileContent) {
			return null;
		}
		return apkProfileContent.mApkId;
	}
	public void setApkId(String mApkId) {
		apkProfileContent.mApkId = mApkId;
	}
	public String getApkName() {
		return apkProfileContent.mApkName;
	}
	public void setApkName(String mApkName) {
		apkProfileContent.mApkName = mApkName;
	}
	public String getApkUrl() {
		return apkProfileContent.mApkUrl;
	}
	public void setApkUrl(String mApkUrl) {
		apkProfileContent.mApkUrl = mApkUrl;
	}
	public String getApkDesc() {
		return apkProfileContent.mApkDesc;
	}
	public void setApkDesc(String mApkDesc) {
		apkProfileContent.mApkDesc = mApkDesc;
	}
	public String getApkVersion() {
		return apkProfileContent.mApkVersion;
	}
	public void setApkVersion(String mApkVersion) {
		apkProfileContent.mApkVersion = mApkVersion;
	}
	public String getApkAccount() {
		return apkProfileContent.mSSOAccount;
	}
	public String getApkVersionClient() {
		return apkProfileContent.mApkVersionClient;
	}
	public void setApkVersionClient(String mApkVersionClient) {
		apkProfileContent.mApkVersionClient = mApkVersionClient;
	}
	public byte getApkFlag() {
		return mApkFlag;
	}
	public void setApkFlag(Context context,byte mApkFlag) {
		if(this.mApkFlag==mApkFlag){
			return;
		}
		this.mApkFlag = mApkFlag;
		ViewUtils.toSaveApp(context, this);
	}
	public String getIconColor() {
		return apkProfileContent.mIconColor;
	}
	public void setIconColor(String mIconColor) {
		apkProfileContent.mIconColor = mIconColor;
	}
	public String getIconGrey() {
		return apkProfileContent.mIconGrey;
	}
	public void setIconGrey(String mIconGrey) {
		apkProfileContent.mIconGrey = mIconGrey;
	}
	public String getApkFileName() {
		return apkProfileContent.mApkFileName;
	}
	public void setApkFileName(String mApkFileName) {
		apkProfileContent.mApkFileName = mApkFileName;
	}
	public String getApkFilePath() {
		return apkProfileContent.mApkFilePath;
	}
	public void setApkFilePath(String mApkFilePath) {
		apkProfileContent.mApkFilePath = mApkFilePath;
	}
	public String getApkPackageName() {
		return apkProfileContent.mApkPackageName;
	}
	public void setApkPackageName(String mApkPackageName) {
		apkProfileContent.mApkPackageName = mApkPackageName;
	}
	public int getApkPublished(){
		return apkProfileContent.mApkPublished;
	}
//	public boolean isApkInstallEnabled() {
//		return apkProfileContent.mApkDisabled==0;
//	}
//	public void setApkInstallEnabled(boolean mApkInstallEnabled) {
//			apkProfileContent.mApkDisabled=mApkInstallEnabled?0:1;
//	}
	public boolean isApkRunningEnabled() {
		return apkProfileContent.mApkDisabled==0;
	}
	public void setApkRunningEnabled(boolean mApkRunningEnabled) {
		apkProfileContent.mApkDisabled=mApkRunningEnabled?0:1;
	}
	public boolean deleteApkFile(){
		String apkPath=getApkFullPath();
    	if(apkPath!=null){
			File apkFile=new File(apkPath);
    		if(apkFile.exists()){
    			return apkFile.delete();
    		}
    	}
    	return false;
	}
	public Bitmap getBitmapColor(){
		Bitmap icon=null;
		if(isBase64){
			if(apkProfileContent.mIconColor!=null){
				byte[] temp = Base64.decode(apkProfileContent.mIconColor,0);
				InputStream is  = new ByteArrayInputStream(temp);
				icon = BitmapFactory.decodeStream(is);
			}
		}else{
			icon=BitmapFactory.decodeFile(apkProfileContent.mIconColor);
		}
		return icon;
	}
	public Bitmap getBitmapGray(){
		Bitmap icon=null;
		if(isBase64){
			if(apkProfileContent.mIconGrey!=null){
				byte[] temp = Base64.decode(apkProfileContent.mIconGrey,0);
				InputStream is  = new ByteArrayInputStream(temp);
				icon = BitmapFactory.decodeStream(is);
			}
		}else{
			icon=BitmapFactory.decodeFile(apkProfileContent.mIconGrey);
		}
		return icon;
	}
	public String getApkFullPath(){
		String path=null;
		if(apkProfileContent.mApkFileName!=null){
			if(apkProfileContent.mApkFilePath!=null){
				path=apkProfileContent.mApkFilePath+"/"+apkProfileContent.mApkFileName;
			}else{
				path=apkProfileContent.mApkFileName;
			}
		}
		return path;
	}
	public void setLocation(int page,int row,int coloum){
		if(mPagePosition==null){
			mPagePosition=new PagePosition(page,row,coloum);
		}else{
			mPagePosition.page=page;
			mPagePosition.row=row;
			mPagePosition.coloum=coloum;
		}
	}
	public void setLocation(PagePosition p){
		if(p==null||p.equals(mPagePosition)){
			return;
		}
		mPagePosition=p;
	}
	public PagePosition getLocation(){
		return mPagePosition==null?new PagePosition():mPagePosition;
	}
	public String getInfo(){
		String info="AppStruct:"+"\nisBase64="+isBase64
		+"\nmId="+apkProfileContent.mId
		+"\nmApkId="+apkProfileContent.mApkId
		+"\nmApkName="+apkProfileContent.mApkName
		+"\nmApkUrl="+apkProfileContent.mApkUrl
		+"\nmApkDesc="+apkProfileContent.mApkDesc
		+"\nmApkVersion="+apkProfileContent.mApkVersion
		+"\nmApkVersionClient="+apkProfileContent.mApkVersionClient
		+"\nmApkFlag="+mApkFlag
		+"\nmIconColor="+apkProfileContent.mIconColor
		+"\nmIconGrey="+apkProfileContent.mIconGrey
		+"\nmApkFileName="+apkProfileContent.mApkFileName
		+"\nmApkFilePath="+apkProfileContent.mApkFilePath
		+"\nmApkPackageName="+apkProfileContent.mApkPackageName
//		+"\nmApkInstallEnabled="+apkProfileContent.mApkInstallEnabled
//		+"\nmApkRunningEnabled="+apkProfileContent.mApkRunningEnabled
		;
		return info;
	}
	public boolean equals(Appdb app){
		if(app!=null&&getApkId().equals(app.getApkId())&&getApkVersion().equals(app.getApkVersion())){
			return true;
		}
		return false;
	}
//	private void writeXml(XmlSerializer serializer){
////    	XmlSerializer serializer = Xml.newSerializer();
////    	StringWriter writer = new StringWriter();
//    	try{
////		serializer.setOutput(writer);
//
//    	// <?xml version=锟斤拷1.0锟斤拷 encoding=锟斤拷UTF-8锟斤拷 standalone=锟斤拷yes锟斤拷?>
////    	serializer.startDocument("UTF-8",true);
//
//    	// <blog number="1锟斤拷>
//    	serializer.startTag("","app");
//    	serializer.attribute("","app id",""+apkProfileContent.mId);
//    	serializer.attribute("","name",apkProfileContent.mApkName);
//     	serializer.attribute("","version",apkProfileContent.mApkVersion);
//    	serializer.attribute("","desc",apkProfileContent.mApkDesc);
//    	}
//    	catch(Exception e)
//    	{
//    	throw new RuntimeException(e);
//    	}
//    }
	public void toSharedPreferences(SharedPreferences.Editor editor,int position){
        //锟斤拷锟斤拷锟斤拷锟斤拷械锟街�
		if(mApkFlag==DOWNLOAD){
			mApkFlag=UNINSTALLED;
		}
        editor.putInt(position+"flag",mApkFlag);
        editor.putString(position+"apkName", apkProfileContent.mApkName);
        editor.putString(position+"apkIcon", apkProfileContent.mIconColor);
        editor.putString(position+"apkIcon2", apkProfileContent.mIconGrey);
        editor.putString(position+"apkFileName",apkProfileContent.mApkFileName);
        editor.putString(position+"apkFilePath", apkProfileContent.mApkFilePath);
        editor.putString(position+"apkUrl", apkProfileContent.mApkUrl);
        editor.putString(position+"apkDescription", apkProfileContent.mApkDesc);
        editor.putString(position + "packageName",apkProfileContent.mApkPackageName);
	}
	
	public void formSharedPreferences(SharedPreferences preferences,int position){
		if(apkProfileContent==null){
			apkProfileContent=new ApkContent();
		}
		mApkFlag=(byte) preferences.getInt(position+"flag", Appdb.UNINSTALLED);
		apkProfileContent.mApkName=preferences.getString(position+"apkName","");
		apkProfileContent.mIconColor=preferences.getString(position+"apkIcon","");
		apkProfileContent.mIconGrey=preferences.getString(position+"apkIcon2","");
		apkProfileContent.mApkFileName=preferences.getString(position+"apkFileName","");
		apkProfileContent.mApkFilePath=preferences.getString(position+"apkFilePath","");
		apkProfileContent.mApkUrl=preferences.getString(position+"apkUrl","");
		apkProfileContent.mApkDesc=preferences.getString(position+"apkDescription","");
		apkProfileContent.mApkPackageName=preferences.getString(position + "packageName",null);
	}
}
