package com.wjx.study;

import java.io.File;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Contacts.People;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Audio.Media;
import android.webkit.URLUtil;

public class Intent_Examples {
    /**run cativityInfo.
     * @param activityInfo
     * @return
     */
    public static Intent startApplication(final ActivityInfo activityInfo){
    	if(activityInfo==null){
    		return null;
    	}
      Intent i = new Intent(); 
      ComponentName cn = new ComponentName(activityInfo.packageName, 
      		activityInfo.name); 
      i.setComponent(cn); 
      i.setAction("android.intent.action.MAIN"); 
//      startActivityForResult(i, RESULT_OK); 
      return i;
    }
    /**install Application
     * @param apkFile
     * @return
     */
    public static Intent installApplication(String apkFile){
    	Uri uri = Uri.parse(apkFile);        
    	Intent it = new Intent(Intent.ACTION_VIEW, uri);        
    	it.setData(uri);
    	it.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);        
    	it.setClassName("com.android.packageinstaller",        
    	               "com.android.packageinstaller.PackageInstallerActivity");        
    	return it;
    	//make sure the url_of_apk_file is readable for all users   
    	/*Uri installUri = Uri.fromParts("package", "", null);
    	returnIt = new Intent(Intent.ACTION_PACKAGE_ADDED, installUri);
*/

    }
    /**uninstall application
     * @param activityInfo 
     * @return is the activityInfo is null.
     * true if the param activityInfo isn't null else return false.
     */
    public static Intent uninstallApplication(ActivityInfo activityInfo){
    	if(activityInfo==null){
    		return null;
    	}
    	Uri uri = Uri.fromParts("package", activityInfo.packageName, null);         
    	Intent it = new Intent(Intent.ACTION_DELETE, uri);         
    	return it;
    }
    
    /**openURl
     * @param url
     * @return
     */
    public static Intent openURl(String url){
    	if(url==null||!URLUtil.isNetworkUrl(url)){
    		return null;
    	}
    	Uri uri = Uri.parse(url);        
    	Intent it = new Intent(Intent.ACTION_VIEW, uri);        
    	return it;
    }
    /**叫 出撥號程式
     * @param telnum
     */
    public static Intent openDial(String telnum){
    	Uri uri = Uri.parse("tel:"+telnum);        
    	Intent it = new Intent(Intent.ACTION_DIAL, uri);        
    	return it;  
    }
    /**直接打電話出 去
     * @param telNum
     */
    public static Intent call(String telNum){
    	Uri uri = Uri.parse("tel:"+telNum);        
    	Intent it = new Intent(Intent.ACTION_CALL, uri);        
    	return it;
    	//用這個，要 在 AndroidManifest.xml 中，加上        
    	//<uses-permission id="android.permission.CALL_PHONE" />   
    }
    
    /**调用发送短信的程序
     * @param message
     */
    public static Intent openSms(String message){
    	//需 写号码SMS       
    	Intent it = new Intent(Intent.ACTION_VIEW);        
    	it.putExtra("sms_body", message);         
    	it.setType("vnd.android-dir/mms-sms");        
    	return it;
    }
    /**发送 SMS 
     * <p>SMS 是一种存储和转发服务。
     * 也就是说，短消息并不是直接从发送人发送到接收人，而始终通过 SMS 中心进行转发。
     * 如果接收人处于未连接状态（可能电话已关闭），则消息将在接收人再次连接时发送。
     * @param num
     * @param message
     */
    public static Intent openSms(String num,String message){
    	//发送 SMS        
    	Uri uri = Uri.parse("smsto:"+num);        
    	Intent it = new Intent(Intent.ACTION_SENDTO, uri);        
    	it.putExtra("sms_body", message);        
    	return it;
    }
    /**
     * 发送彩信,没成功
     * <p>mms是英文缩写，它可以是Membership Management System的缩写，
     * 中文译名为会员管理系统。也可以是Multimedia Messaging Service的缩写，
     * 中文译为多媒体短信服务。
     */
    public static Intent openMMS(){//该Uri根据实际情况修改，external代表外部存储即sdcard
//    	Uri uri = Uri.parse("content://media/external/images/media/23");
    	Uri uri = Uri.parse("content://media/external/23.png");
    	Intent it = new Intent(Intent.ACTION_SEND);
    	it.putExtra("sms_body", "some text");
    	it.putExtra(Intent.EXTRA_STREAM, uri);
    	it.setType("image/png");
    	return it;
    }
    /**open the mail editor with accept address
     * @param mailto accept mail address
     * @param context
     */
    public static Intent email(String mailto,String context){
    	Uri uri = Uri.parse("mailto:"+mailto);        
    	Intent it = new Intent(Intent.ACTION_SENDTO, uri);    
    	it.putExtra(Intent.EXTRA_TEXT, context);
    	return it; 
    }
    /**可选择发送方式：sms、email.
     * @param mailto
     * @param context
     */
    public static Intent email2(String mailto,String context){
    	Intent it = new Intent(Intent.ACTION_SEND);        
    	it.putExtra(Intent.EXTRA_EMAIL, new String[]{mailto});
    	it.putExtra(Intent.EXTRA_TEXT, context);        
    	it.setType("text/plain");  
        
//    	startActivity(it); 
    	return Intent.createChooser(it, "选择发送方式"); 
    }
    /**
     * @param mail the accepter
     * @param mailto the carbon copied
     * @param context 
     * @param subject
     */
   public static Intent email(String mailto,String mailcarbon,String context,String subject){
    	Intent it=new Intent(Intent.ACTION_SEND);          
    	String[] tos={mailto};          
    	String[] ccs={mailcarbon};          
    	it.putExtra(Intent.EXTRA_EMAIL, tos);          
    	it.putExtra(Intent.EXTRA_CC, ccs);          
    	it.putExtra(Intent.EXTRA_TEXT, context);          
    	it.putExtra(Intent.EXTRA_SUBJECT, subject);
    	//使用时没效果
//    	it.setType("message/rfc822"); //编码类型 
    	it.setType("text/plain");  
    	return it;
//    	startActivity(Intent.createChooser(it, "Choose Email Client"));
    }
    /**
     * 一闪而过
     */
    public static Intent mail(){
    	//传送附 件        
    	Intent it = new Intent(Intent.ACTION_SEND);        
    	it.putExtra(Intent.EXTRA_SUBJECT, "The email subject text");        
    	it.putExtra(Intent.EXTRA_STREAM, "file:///sdcard/mysong.mp3");        
    	it.setType("audio/mp3");
    	return Intent.createChooser(it, "Choose Email Client");
    }
    
    /**
     * 没有成功
     */
    public static Intent openMap(){
    	Uri uri = Uri.parse("geo:38.899533,-77.036476");        
    	Intent it = new Intent(Intent.ACTION_VIEW, uri);         
    	return it;         
    	//其 他 geo URI 範例        
    	//geo:latitude,longitude        
    	//geo:latitude,longitude?z=zoom        
    	//geo:0,0?q=my+street+address        
    	//geo:0,0?q=business+near+city        
    	//google.streetview:cbll=lat,lng&cbp=1,yaw,,pitch,zoom&mz=mapZoom      

    }
    
    public static Intent openMapPath(){
    	Uri uri = Uri.parse("http://maps.google.com/maps?f=d&saddr=startLat%20startLng&daddr=endLat%20endLng&hl=en");
    	Intent it = new Intent(Intent.ACTION_VIEW,uri);
    	return it;
    }
    
    /**前台播放
     * @param fileName
     */
    public static Intent playMp3(String fileName){
    	Intent it = new Intent(Intent.ACTION_VIEW);        
    	Uri uri = Uri.parse("file://"+fileName);        
    	it.setDataAndType(uri, "audio/mp3");        
    	return it;
    	//或者,失败
//    	Uri uri2 = Uri.parse(fileName);
//    		Intent it2 = new Intent(Intent.ACTION_VIEW, uri2);
//    		 it2.addFlags(it2.FLAG_ACTIVITY_NEW_TASK);//非必须选项
//    		it2.setDataAndType(uri2,"audio/mp3");
//    		startActivity(it2);
    }
    /**播放铃声,从系统内部的MediaProvider索引中调用播放
     * @param num
     */
   public static Intent playRing(String num){
    	Uri uri = Uri.withAppendedPath(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, "1");        
    	Intent it = new Intent(Intent.ACTION_VIEW, uri);        
    	return it;
    }
    
    /**没有成功
     * @param activityInfo
     */
    private void market(ActivityInfo activityInfo){
    	//寻 找应用       
//    	Uri uri = Uri.parse("market://search?q=pname:"+activityInfo.packageName);
//    	Uri uri = Uri.parse("market://search?q=<"+activityInfo.packageName+">");
//    	Uri uri = Uri.parse("http://market.android.com/search?q=pname:<"+activityInfo.packageName+">");        
//    	Intent it = new Intent(Intent.ACTION_VIEW, uri);        
//    	startActivity(it);        
    	//where pkg_name is the full package path for an application       
    	//显示应用详细列 表      
//    	Uri uri = Uri.parse("market://details?id=app_id");        
//    	Intent it = new Intent(Intent.ACTION_VIEW, uri);        
//    	startActivity(it);        
    	//where app_id is the application ID, find the ID         
    	//by clicking on your application on Market home         
    	//page, and notice the ID from the address bar 
    }
    /**
     * SIM card Contact
     */
    public static Intent openSIMContact(){
    	Intent importIntent = new Intent(Intent.ACTION_VIEW);
    	importIntent.setType("vnd.android.cursor.item/sim-contact");
    	importIntent.setClassName("com.android.phone", "com.android.phone.SimContacts");
    	return importIntent;
    }
    
    /**进入联系人界面
     * 
     */
   public static Intent openPeople(){
    	  Intent intent = new Intent();
    	  intent.setAction(Intent.ACTION_VIEW);
    	  intent.setData(People.CONTENT_URI);
    	  return intent;
    	
//    	18打开联系人列表 
//        <1>           
//         Intent i = new Intent(); 
//         i.setAction(Intent.ACTION_GET_CONTENT); 
//         i.setType("vnd.android.cursor.item/phone"); 
//         startActivityForResult(i, 11); 

//        <2> 
//        Uri uri = Uri.parse("content://contacts/people"); 
//        Intent it = new Intent(Intent.ACTION_PICK, uri); 
//        startActivityForResult(it, 111); 
    }
    public static Intent openPeople(int ID){
    	 Uri personUri = ContentUris.withAppendedId(People.CONTENT_URI, ID);//最后的ID参数为联系人Provider中的数据库BaseID，即哪一行
    	 Intent intent = new Intent();
    	 intent.setAction(Intent.ACTION_VIEW);
    	 intent.setData(personUri);
    	 return intent;
    }
    /**
     * 打开所有图片View
     * <p>use as:startActivityForResult(i, 11);
     */
    public static Intent selectPictrue(){
//    	 选择一个图片
//    	 Intent intent = new Intent(Intent.ACTION_GET_CONTENT);  
//    	 intent.addCategory(Intent.CATEGORY_OPENABLE);  
//    	 intent.setType("image/*");
//    	 startActivityForResult(intent, 0);
    	 Intent i = new Intent(); 
         i.setType("image/*"); 
         i.setAction(Intent.ACTION_GET_CONTENT); 
         return i;
    }
    /**<p>use as:startActivityForResult(i, 0);
     * @return
     */
    public static Intent camera(){
//    	 调用Android设备的照相机，并设置拍照后存放位置
    	 Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  
    	intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment
    	.getExternalStorageDirectory().getAbsolutePath(), "android123" + ".jpg"))); //存放位置为sdcard卡上cwj文件夹，文件名为android123.jpg格式
    	return intent;
    }
    /**
     * use as:startActivityForResult(inttPhoto, 10);
     */
    public static Intent camera2(Context cotext){
    	long dateTaken = System.currentTimeMillis(); 
        String name = dateTaken + ".jpg"; 
       String fileName = "/sdcard/" + name; 
        ContentValues values = new ContentValues(); 
        values.put(Images.Media.TITLE, fileName); 
        values.put("_data", fileName); 
        values.put(Images.Media.PICASA_ID, fileName); 
        values.put(Images.Media.DISPLAY_NAME, fileName); 
        values.put(Images.Media.DESCRIPTION, fileName); 
        values.put(Images.ImageColumns.BUCKET_DISPLAY_NAME, fileName); 
        Uri photoUri = cotext.getContentResolver().insert( 
                  MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values); 
          
        Intent inttPhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
        inttPhoto.putExtra(MediaStore.EXTRA_OUTPUT, photoUri); 
        return inttPhoto; 
    }
    /**从google搜索内容 
     * @param searchString
     */
    public static Intent searchGoogle(String searchString){
    	Intent intent = new Intent(); 
    	intent.setAction(Intent.ACTION_WEB_SEARCH); 
    	intent.putExtra(SearchManager.QUERY,searchString); 
    	return intent; 
    }
    /**
     * 打开录音机
     */
   public static Intent openRecord(){
    	Intent mi = new Intent(Media.RECORD_SOUND_ACTION); 
    	return mi; 
    }
}
