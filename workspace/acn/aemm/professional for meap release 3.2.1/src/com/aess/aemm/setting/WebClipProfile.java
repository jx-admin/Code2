package com.aess.aemm.setting;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import com.aess.aemm.R;
import com.aess.aemm.db.WebclipContent;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

public class WebClipProfile extends Profile {

	public static final String TAG = "WebClipProfile";
	public static String node_value_icon = "Icon";
	public static String node_value_isremovable = "IsRemovable";
	public static String node_value_url = "URL";
	public static String node_value_name = "Label";
	public static String node_value_indentifier = "PayloadIdentifier";
	
	public static ArrayList<WebclipContent> newWebClipList 	= new ArrayList<WebclipContent>();
	public static ArrayList<WebclipContent> oldWebClipList 	= new ArrayList<WebclipContent>();

	public int setValue(String key, String value) {
		Log.i(TAG, String.format("%s = %s", key, value));

		if (key.equals(node_value_indentifier)) {
			wcpContent.mWebClipIdentifier = value;
		} else if (key.equals(node_value_name)) {
			wcpContent.mWebClipName = value;
		} else if (key.equals(node_value_url)) {
			wcpContent.mWebClipUrl = value;
		} else if (key.equals(node_value_isremovable)) {
			// setIsRemovable(value.equals("true") ? true : false);
		} else if (key.equals(node_value_icon)) {
			wcpContent.mWebClipIcon = value;
		} else if (key.equals(Profile.node_value_identifier)) {
			// wcpContent.mId = value;
		} else {
			super.setValue(key, value);
		}
		return 0;
	}

	public int saveProfile(Context context) {
		Log.i(TAG, "saveProfile");

		int ret = -1;

		clearWebClipProfile(context);

		if (wcpContent.mWebClipUrl != null && wcpContent.mWebClipName != null) {

			WebclipContent newWc = new WebclipContent();
			newWc.mWebClipIdentifier = wcpContent.mWebClipIdentifier;
			newWc.mWebClipIcon = wcpContent.mWebClipIcon;
			newWc.mWebClipName = wcpContent.mWebClipName;
			newWc.mWebClipUrl = wcpContent.mWebClipUrl;
			newWc.mWebClipVersion = version;
			newWc.mWebClipFlag = 1;
					
			if(newWc.mWebClipIcon == null)
			{
				BitmapDrawable bmpDraw=(BitmapDrawable)context.getResources().getDrawable(R.drawable.apk);				
				Bitmap icon=bmpDraw.getBitmap();
                newWc.mWebClipIcon = Bitmap2String(icon);
			}
			newWebClipList.add(newWc);
			Uri uri = newWc.add(context);
			if (uri != null){
				ret = 1;
			}
		}

		return ret;
	}
	
	private String Bitmap2String(Bitmap bm){
		 ByteArrayOutputStream baos = new ByteArrayOutputStream();  
		 bm.compress(Bitmap.CompressFormat.PNG, 100, baos);  
		 
		 return new String( Base64.encode(baos.toByteArray(),0));
	}
	

	public int clearProfile(Context context) {
		Log.i(TAG, "clearProfile");

		String ver = null;
		if (null != version) {
			ver = version;
		} else {
			Log.i(TAG, "Clear All");
			ver = VERSION;
		}

		ArrayList<WebclipContent> dblist = WebclipContent
				.getWebClipByUnVer(context, ver);

		if (null != dblist) {
			for (WebclipContent oldcontent : dblist) {
			     WebClipSetup.delWebClip(context, oldcontent.mWebClipName,oldcontent.mWebClipUrl);
			     WebclipContent.delWebclipById(context,oldcontent.mId);
			}
		}

		return 0;
	}
	
	public int clearWebClipProfile(Context context) {
		Log.i(TAG, "clearProfile");

		String ver = null;
		if (null != version) {
			ver = version;
		} else {
			Log.i(TAG, "Clear All");
			ver = VERSION;
		}

		ArrayList<WebclipContent> dblist = WebclipContent
				.restoreWebClipWithVersion(context, ver);

		if (null != dblist) {
			for (int i=0; i<dblist.size();i++) {
				WebclipContent item = dblist.get(i);
				oldWebClipList.add(item);
				WebclipContent.delWebclipById(context,item.mId);
			}
		}

		return 0;
	}
	
	public static void refreshWebClipShort(final Context context){
		Log.i(TAG, "refreshWebClipShort");
		Thread postWeb = new Thread(new Runnable() {
			@Override
			public void run() {
				if(newWebClipList != null){
					for(int i=0;i<oldWebClipList.size();i++)
					{
						WebclipContent oldItem = oldWebClipList.get(i);
						int j = 0;
						for( j=0;j<newWebClipList.size();j++){
							WebclipContent newItem = newWebClipList.get(j);
							if(oldItem.mWebClipIcon.equals(newItem.mWebClipIcon)&&
								oldItem.mWebClipName.equals(newItem.mWebClipName) &&
								oldItem.mWebClipUrl.equals(newItem.mWebClipUrl))
								{
								    newItem.mWebClipFlag = 0;
									break;
								}						
						}
						if(j == newWebClipList.size())
						{
							Log.i(TAG,"Webclip Profile:  delete WebClipShort "+ oldItem.mWebClipName + " " + oldItem.mWebClipUrl);
							WebClipSetup.delWebClip(context, oldItem.mWebClipName,oldItem.mWebClipUrl);
						}
						else
						{
							continue;
						}
						
					}
					
					for( int j=0;j<newWebClipList.size();j++){
						WebclipContent newItem = newWebClipList.get(j);
						if(newItem.mWebClipFlag != 0) 
						{
							if (!WebClipSetup.systemHaveSameConfig(context, newItem.mWebClipName,newItem.mWebClipUrl)) {
								Log.i(TAG,"Webclip Profile:  add WebClipShort "+ newItem.mWebClipName + " " + newItem.mWebClipUrl);
								WebClipSetup.addWebClip(context, newItem.mWebClipName,newItem.mWebClipUrl, newItem.mWebClipIcon);
							}
						}	
					}
					newWebClipList.clear();
					oldWebClipList.clear();
					}
			}
		});
		postWeb.setName("WebClib");
		postWeb.start();
	}

	private WebclipContent wcpContent = new WebclipContent();
}
