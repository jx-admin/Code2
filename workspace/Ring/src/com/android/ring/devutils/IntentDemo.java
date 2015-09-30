package com.android.ring.devutils;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Contacts;

public class IntentDemo {
	
	/**返回HOME界面
	 * @param context
	 */
	public static void goHome(Context context){

		Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);
        mHomeIntent.addCategory(Intent.CATEGORY_HOME);
         mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                         | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
         context.startActivity(mHomeIntent);
	}

	public static void outgoing(Activity context,String phoneNumber){
		Uri uri = Uri.parse("tel:"+phoneNumber);   
		Intent intent =new Intent(Intent.ACTION_NEW_OUTGOING_CALL,uri);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		context.startActivityForResult(intent, 11);
	}
	/**通话记录
	 * @param context
	 */
	public static void callButton(Context context){
		Intent intent =new Intent();  
		  
		intent.setAction(Intent.ACTION_CALL_BUTTON);   
		
		context.startActivity(intent);
	}


	/**拨号界面
	 * @param context
	 * @param phoneNumber number or empty or null
	 */
	public static void call(Context context,String phoneNumber,boolean isDefault){
		Uri uri = Uri.parse("tel:"+phoneNumber);   
		
		Intent intent = new Intent(Intent.ACTION_CALL, uri); 
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if(isDefault){
			intent.setPackage("com.android.phone");
//			intent.setClassName("com.android.contacts","com.android.contacts.DialtactsActivity");     
		}    
		
		context.startActivity(intent);
	}

	/**系统拨号
	 * @param context
	 * @param phoneNumber phoneNumber number or empty or null
	 * @param isDefault system or selector
	 */
	public static void dial(Context context,String phoneNumber,boolean isDefault){
		Uri uri = Uri.parse("tel:"+phoneNumber);   
		
		Intent intent = new Intent(Intent.ACTION_DIAL, uri); 
		if(isDefault){
			intent.setClassName("com.android.contacts","com.android.contacts.DialtactsActivity");     
		}
		
		context.startActivity(intent);  
	}
	  

//	public static void peopleContent(Context context){
//		Intent intent = new Intent();   
//		
//		intent.setAction(Intent.ACTION_VIEW);   
//		
//		intent.setData(Contacts.People.CONTENT_URI);   
//		
//		context.startActivity(intent);
//		
//	}
//
//	public static void dialtacts(Context context){
//		
//		Intent intent= new Intent("com.android.contacts.action.LIST_STREQUENT");   
//		intent.setClassName("com.android.contacts","com.android.contacts.DialtactsActivity");
//		context.startActivity(intent);
//	}
//	  
//
//	public static void contactPick(Context context){
//		Intent intent = new Intent();   
//		intent.setAction(Intent.ACTION_PICK);   
//		
//		intent.setData(Contacts.People.CONTENT_URI);   
//		
//		context.startActivity(intent);   
//	}
//
//	public static void contact(Context context,int id){
//		Intent intent=new Intent(Intent.ACTION_EDIT,Uri.parse("content://com.android.contacts/contacts/"+"1"));  
//		
//		context.startActivity(intent);  
//		
//	}
//
//	public static void inserContact(Context context,String name,String company,String tel,int phoneType){
//
//		Intent intent = new Intent(Intent.ACTION_INSERT_OR_EDIT);  
//		
//		intent.setType("vnd.android.cursor.item/person");  
//		
//		intent.setType("vnd.android.cursor.item/contact");  
//		
//		intent.setType("vnd.android.cursor.item/raw_contact");  
//		
//		intent.putExtra(android.provider.ContactsContract.Intents.Insert.NAME, name);  
//		
//		intent.putExtra(android.provider.ContactsContract.Intents.Insert.COMPANY,company);  
//		
//		intent.putExtra(android.provider.ContactsContract.Intents.Insert.PHONE, tel);  
//		
//		intent.putExtra(android.provider.ContactsContract.Intents.Insert.PHONE_TYPE, phoneType);
//	}
//
//	public static void mms(Context context){
//	Intent intent = new Intent(Intent.ACTION_VIEW);  
//	  
//	                 intent.setType("vnd.android-dir/mms-sms");  
//	  
//	 //              intent.setData(Uri.parse("content://mms-sms/conversations/"));//此为号码  
//	  
//	                 context.startActivity(intent);  
//	}
//
//	public static void conversation(Context context){
//		Intent intent = new Intent("android.intent.action.CONVERSATION");  
//		
//		context.startActivity(intent);  
//	}
//
////	1.从google搜索内容
//	 public static void webSearch(Context context){
//		Intent intent = new Intent();   
//		
//		intent.setAction(Intent.ACTION_WEB_SEARCH);   
//		
//		intent.putExtra(SearchManager.QUERY,"searchString");  
//		
//		context.startActivity(intent);   
//		
//	}
//
////	2.浏览网页 http://www.google.com
//	 public static void url(Context context,String webUrl){
//		 Uri uri = Uri.parse(webUrl);   
//		 
//		 Intent it   = new Intent(Intent.ACTION_VIEW,uri);   
//		 
//		 context.startActivity(it);
//		 
//	 }
//
////	3.显示地图
//	 public static void map(Context context,double l,double g){
//		 Uri uri = Uri.parse("geo:38.899533,-77.036476");   
//		 
//		 Intent it = new Intent(Intent.Action_VIEW,uri);   
//		 
//		 context.startActivity(it);   
//		 
//	 }
//
//	 
////	4.路径规划
//	 
//public static void pathWay(Context context)
//	Uri uri = Uri.parse("http://maps.google.com/maps?f=dsaddr=startLat%20startLng&daddr=endLat%20endLng&hl=en");   
//	  
//	 Intent it = new Intent(Intent.ACTION_VIEW,URI);   
//	  
//	 context.startActivity(it); 
//}
//	 
//	//6.调用发短信的程序
//	 
//	public static void smsView(Context context,String sms){
//	Intent it = new Intent(Intent.ACTION_VIEW);     
//	  
//	 it.putExtra("sms_body", sms);     
//	  
//	 it.setType("vnd.android-dir/mms-sms");     
//	  
//	context.startActivity(it);
//	}
//	
//	public static void sms(Context contex,String phoneNumber){
//	uri = Uri.parse("smsto:"+phoneNumber);    
//	  
//	                Intent intent = new Intent(Intent.ACTION_SENDTO,uri);    
//	  
//	                 context.startActivity(intent);   
//	}
//public static void sms(Context context,String address){
//	Intent mIntent = new Intent(Intent.ACTION_VIEW);    
//	  
//	         mIntent.putExtra("address",address);    
//	  
//	         mIntent.setType("vnd.android-dir/mms-sms");    
//	  
//	         context.startActivity(mIntent);   
//}
//	
//	//7.发送短信
//	 
//public static void sms(Context context,String phoneNumber,String text){
//	Uri uri = Uri.parse("smsto:"+phoneNumber);     
//	  
//	 Intent it = new Intent(Intent.ACTION_SENDTO, uri);     
//	  
//	 it.putExtra("sms_body", text);     
//	  
//	context. startActivity(it);   
//	  
//}
//public static void sms(Context context,String phoneNumber,String text){
//	  
//	 Intent mmsintent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("smsto", phoneNumber, null));   
//	  
//	 mmsintent.putExtra(Messaging.KEY_ACTION_SENDTO_MESSAGE_BODY, text);   
//	  
//	 mmsintent.putExtra(Messaging.KEY_ACTION_SENDTO_COMPOSE_MODE, true);   
//	  
//	 mmsintent.putExtra(Messaging.KEY_ACTION_SENDTO_EXIT_ON_SENT, true);   
//	  
//	 context.startActivity(mmsintent);
//}
//
//
//	//8.发送彩信
//	 
//public static void mms(String context){
//	
//	
//	Uri uri = Uri.parse("content://media/external/images/media/23");     
//	  
//	 Intent it = new Intent(Intent.ACTION_SEND);     
//	  
//	 it.putExtra("sms_body", "some text");     
//	  
//	 it.putExtra(Intent.EXTRA_STREAM, uri);     
//	  
//	 it.setType("image/png");     
//	  
//	 context.startActivity(it); 
//}
//
//public static void mms(Context contex,String number,String subject,String body,File fd){
//	
//	
//	  
//	 StringBuilder sb = new StringBuilder();   
//	  
//	 sb.append("file://");   
//	  
//	 sb.append(fd.getAbsoluteFile());   
//	  
//	 Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mmsto", number, null));   
//	  
//	 // Below extra datas are all optional.   
//	  
//	 intent.putExtra(Messaging.KEY_ACTION_SENDTO_MESSAGE_SUBJECT, subject);   
//	  
//	 intent.putExtra(Messaging.KEY_ACTION_SENDTO_MESSAGE_BODY, body);   
//	  
//	 intent.putExtra(Messaging.KEY_ACTION_SENDTO_CONTENT_URI, sb.toString());   
//	  
//	 intent.putExtra(Messaging.KEY_ACTION_SENDTO_COMPOSE_MODE, composeMode);   
//	  
//	 intent.putExtra(Messaging.KEY_ACTION_SENDTO_EXIT_ON_SENT, exitOnSent);   
//	  
//	 Context.startActivity(intent); 
//}
//	 
////	9.发送Email
//	 
//
//	Uri uri = Uri.parse("mailto:xxx@abc.com");   
//	  
//	 Intent it = new Intent(Intent.ACTION_SENDTO, uri);   
//	  
//	 startActivity(it);   
//	  
//	 Intent it = new Intent(Intent.ACTION_SEND);     
//	  
//	 it.putExtra(Intent.EXTRA_EMAIL, "me@abc.com");     
//	  
//	 it.putExtra(Intent.EXTRA_TEXT, "The email body text");     
//	  
//	 it.setType("text/plain");     
//	  
//	 startActivity(Intent.createChooser(it, "Choose Email Client"));   
//	  
//	 Intent it=new Intent(Intent.ACTION_SEND);       
//	  
//	 String[] tos={"me@abc.com"};       
//	  
//	 String[] ccs={"you@abc.com"};       
//	  
//	 it.putExtra(Intent.EXTRA_EMAIL, tos);       
//	  
//	 it.putExtra(Intent.EXTRA_CC, ccs);       
//	  
//	 it.putExtra(Intent.EXTRA_TEXT, "The email body text");       
//	  
//	 it.putExtra(Intent.EXTRA_SUBJECT, "The email subject text");       
//	  
//	 it.setType("message/rfc822");       
//	  
//	 startActivity(Intent.createChooser(it, "Choose Email Client"));     
//	  
//	   
//	  
//	 Intent it = new Intent(Intent.ACTION_SEND);     
//	  
//	 it.putExtra(Intent.EXTRA_SUBJECT, "The email subject text");     
//	  
//	 it.putExtra(Intent.EXTRA_STREAM, "file:///sdcard/mysong.mp3");     
//	  
//	 sendIntent.setType("audio/mp3");     
//	  
//	 startActivity(Intent.createChooser(it, "Choose Email Client"));   
//	 复制代码 
//	10.播放多媒体   
//	 
//
//	Intent it = new Intent(Intent.ACTION_VIEW);   
//	  
//	 Uri uri = Uri.parse("file:///sdcard/song.mp3");   
//	  
//	 it.setDataAndType(uri, "audio/mp3");   
//	  
//	 startActivity(it);   
//	  
//	 Uri uri = Uri.withAppendedPath(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, "1");     
//	  
//	 Intent it = new Intent(Intent.ACTION_VIEW, uri);     
//	  
//	 startActivity(it);   
//	 复制代码 
//	11.uninstall apk
//	 
//
//	Uri uri = Uri.fromParts("package", strPackageName, null);     
//	  
//	 Intent it = new Intent(Intent.ACTION_DELETE, uri);     
//	  
//	 startActivity(it);  
//	 复制代码 
//	12.install apk
//	 
//
//	Uri installUri = Uri.fromParts("package", "xxx", null);   
//	  
//	 returnIt = new Intent(Intent.ACTION_PACKAGE_ADDED, installUri);  
//	 复制代码 
//	13. 打开照相机
//	 
//
//	<1>Intent i = new Intent(Intent.ACTION_CAMERA_BUTTON, null);   
//	  
//	           this.sendBroadcast(i);   
//	  
//	     <2>long dateTaken = System.currentTimeMillis();   
//	  
//	          String name = createName(dateTaken) + ".jpg";   
//	  
//	          fileName = folder + name;   
//	  
//	          ContentValues values = new ContentValues();   
//	  
//	          values.put(Images.Media.TITLE, fileName);   
//	  
//	          values.put("_data", fileName);   
//	  
//	          values.put(Images.Media.PICASA_ID, fileName);   
//	  
//	          values.put(Images.Media.DISPLAY_NAME, fileName);   
//	  
//	          values.put(Images.Media.DESCRIPTION, fileName);   
//	  
//	          values.put(Images.ImageColumns.BUCKET_DISPLAY_NAME, fileName);   
//	  
//	          Uri photoUri = getContentResolver().insert(   
//	  
//	                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);   
//	  
//	              
//	  
//	          Intent inttPhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);   
//	  
//	          inttPhoto.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);   
//	  
//	          startActivityForResult(inttPhoto, 10);
//	 复制代码 
//	14.从gallery选取图片
//	 
//
//	Intent i = new Intent();   
//	  
//	         i.setType("image/*");   
//	  
//	         i.setAction(Intent.ACTION_GET_CONTENT);   
//	  
//	         startActivityForResult(i, 11);   
//	 复制代码 
//	15. 打开录音机
//	 
//
//	Intent mi = new Intent(Media.RECORD_SOUND_ACTION);   
//	  
//	          startActivity(mi);
//	 复制代码 
//	16.显示应用详细列表
//	 
//
//	Uri uri = Uri.parse("market://details?id=app_id");          
//	  
//	 Intent it = new Intent(Intent.ACTION_VIEW, uri);          
//	  
//	 startActivity(it);          
//	  
//	 //where app_id is the application ID, find the ID           
//	  
//	 //by clicking on your application on Market home           
//	  
//	 //page, and notice the ID from the address bar<span style="font-family:Simsun;white-space: normal; background-color: rgb(255, 255, 255);">    </span>
//	 复制代码 
//	刚才找app id未果，结果发现用package name也可以 Uri uri = Uri.parse("market://details?id=<packagename>");
//	 这个简单多了 
//	 
//	17寻找应用     
//	 
//
//	Uri uri = Uri.parse("market://search?q=pname:pkg_name");          
//	  
//	 Intent it = new Intent(Intent.ACTION_VIEW, uri);          
//	  
//	 startActivity(it);   
//	  
//	 //where pkg_name is the full package path for an application<span style="font-family:Simsun;white-space: normal; background-color: rgb(255, 255, 255);">     </span>  
//	 复制代码 
//	18打开联系人列表
//	 
//
//	Intent i = new Intent();   
//	  
//	          i.setAction(Intent.ACTION_GET_CONTENT);   
//	  
//	          i.setType("vnd.android.cursor.item/phone");   
//	  
//	          startActivityForResult(i, REQUEST_TEXT);
//	 复制代码 
//
//	Uri uri = Uri.parse("content://contacts/people");   
//	  
//	          Intent it = new Intent(Intent.ACTION_PICK, uri);   
//	  
//	          startActivityForResult(it, REQUEST_TEXT);  
//	 复制代码 
//	19 打开另一程序
//	 
//
//	Intent i = new Intent();   
//	  
//	          ComponentName cn = new ComponentName("com.yellowbook.android2",   
//	  
//	                    "com.yellowbook.android2.AndroidSearch");   
//	  
//	          i.setComponent(cn);   
//	  
//	          i.setAction("android.intent.action.MAIN");   
//	  
//	          startActivityForResult(i, RESULT_OK);  
//	 复制代码 
//	20 添加到短信收件箱
//	 
//
//	ContentValues cv = new ContentValues();      
//	  
//	                 cv.put("type", "1");   
//	  
//	 cv.put("address","短信地址");  
//	  
//	 cv.put("body", "短信内容");   
//	  
//	 getContentResolver().insert(Uri.parse("content://sms/inbox"), cv);
//	 复制代码 
//	21 从sim卡或者联系人中查询
//	 
//
//	Cursor cursor;  
//	  
//	         Uri uri;  
//	  
//	         if (type == 1) {  
//	  
//	             Intent intent = new Intent();  
//	  
//	             intent.setData(Uri.parse("content://icc/adn"));  
//	  
//	             uri = intent.getData();  
//	  
//	         } else  
//	  
//	             uri = People.CONTENT_URI;  
//	  
//	   
//	  
//	         cursor = activity.getContentResolver().query(uri, null, null, null, null);  
//	  
//	 while (cursor.moveToNext()) {  
//	  
//	 int peopleId = cursor.getColumnIndex(People._ID);
//	  
//	 int nameId = cursor.getColumnIndex(People.NAME); 
//	  
//	 int phoneId = cursor.getColumnIndex(People.NUMBER);}
//	 复制代码 
//	查看某个联系人，当然这里是ACTION_VIEW，如果为选择并返回action改为ACTION_PICK，当然处理intent时返回需要用到 startActivityforResult 
//	 Uri personUri = ContentUris.withAppendedId(People.CONTENT_URI, ID);//最后的ID参数为联系人Provider中的数据库BaseID，即哪一行 
//	 Intent intent = new Intent(); intent.setAction(Intent.ACTION_VIEW); intent.setData(personUri); startActivity(intent); 
//	 
//
//
//	 
//	 
//	22 删除
//	 
//
//	uri = ContentUris.withAppendedId(People.CONTENT_URI, 联系人id);  
//	  
//	         int count = activity.getContentResolver().delete(uri, null, null
//	 复制代码 
//	23 添加到联系人：
//	 
//
//	ContentValues cv = new ContentValues();  
//	  
//	                     ArrayList<ContentProviderOperation> operationList = new ArrayList<ContentProviderOperation>();  
//	  
//	                     ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(RawContacts.CONTENT_URI);  
//	  
//	                     builder.withValues(cv);  
//	  
//	                     operationList.add(builder.build());  
//	  
//	                     builder = ContentProviderOperation.newInsert(Data.CONTENT_URI);  
//	  
//	                     builder.withValueBackReference(StructuredName.RAW_CONTACT_ID, 0);  
//	  
//	                     builder.withValue(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);  
//	  
//	                     builder.withValue(StructuredName.DISPLAY_NAME, "自定义联系人名");  
//	  
//	                     operationList.add(builder.build());  
//	  
//	                     builder = ContentProviderOperation.newInsert(Data.CONTENT_URI);  
//	  
//	                     builder.withValueBackReference(Phone.RAW_CONTACT_ID, 0);  
//	  
//	                     builder.withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);  
//	  
//	                     builder.withValue(Phone.NUMBER, "联系人的phonenumber");  
//	  
//	                     builder.withValue(Data.IS_PRIMARY, 1);  
//	  
//	                     operationList.add(builder.build());  
//	  
//	                     try {  
//	  
//	                         getContentResolver().applyBatch(ContactsContract.AUTHORITY, operationList);  
//	  
//	                     } catch (RemoteException e) {  
//	  
//	                         e.printStackTrace();  
//	  
//	                     } catch (OperationApplicationException e) {  
//	  
//	                         e.printStackTrace();  
//	  
//	                     }  
//	 复制代码 
//	23 选择一个图片
//	 
//
//	Intent intent = new Intent(Intent.ACTION_GET_CONTENT);   
//	  
//	 intent.addCategory(Intent.CATEGORY_OPENABLE);  
//	  
//	 intent.setType("image/*");  
//	  
//	 startActivityForResult(intent, 0);   
//	 复制代码 
//	24 调用
//	 
//	Android
//	 
//	设备的照相机，并设置拍照后存放位置
//	 
//
//	Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  
//	  
//	 intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment .getExternalStorageDirectory().getAbsolutePath()+"/cwj", android123 + ".jpg"))); //存放位置为sdcard卡上cwj文件夹，文件名为android123.jpg格式  
//	  
//	 startActivityForResult(intent, 0);  
//	 复制代码 
//	25 在market上搜索指定package name，比如搜索com.android123.cwj的写法如下
//	 
//
//	Uri uri = Uri.parse("market://search?q=pname:com.android123.cwj");  
//	  
//	 Intent intent = new Intent(Intent.ACTION_VIEW, uri); startActivity(intent);
//	 复制代码 
//	26获取文件信息，并使用相对应软件打开
//	 
//
//	private void openFile(File f)    
//	  
//	 {    
//	  
//	    Intent intent = new Intent();    
//	  
//	    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);    
//	  
//	    intent.setAction(android.content.Intent.ACTION_VIEW);    
//	  
//	    String type = getMIMEType(f);    
//	  
//	    intent.setDataAndType(Uri.fromFile(f), type);    
//	  
//	    startActivity(intent);    
//	  
//	 }    
//	  
//	    
//	  
//	 private String getMIMEType(File f){    
//	  
//	    String end = f    
//	  
//	        .getName()    
//	  
//	        .substring(f.getName().lastIndexOf(".") + 1,    
//	  
//	            f.getName().length()).toLowerCase();    
//	  
//	    String type = "";    
//	  
//	    if (end.equals("mp3") || end.equals("aac") || end.equals("aac")    
//	  
//	        || end.equals("amr") || end.equals("mpeg")    
//	  
//	        || end.equals("mp4"))    
//	  
//	    {    
//	  
//	      type = "audio";    
//	  
//	    } else if (end.equals("jpg") || end.equals("gif")    
//	  
//	        || end.equals("png") || end.equals("jpeg"))    
//	  
//	    {    
//	  
//	      type = "image";    
//	  
//	    } else    
//	  
//	    {    
//	  
//	      type = "*";    
//	  
//	    }    
//	  
//	    type += "/*";    
//	  
//	    return type;    
//	  
//	 }

}
