package com.android.accenture.aemm.express;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.accenture.aemm.express.app.ScrollLayout;

public class AppScrollAdapter extends ScrollAdapter{

	private AppItem editApp;
	public AppScrollAdapter(Context c) {
		super(c);
	}
	void setItemView(AppItem item){
		if (item.getView() != null) {
			item.setView(null);
		}
			View hview=mInflater.inflate(R.layout.app_item, null);
			GridHolder holder = new GridHolder();
			// holder.index=index;
			 holder.appImage =(ImageView)hview.findViewById(R.id.icon_iv);
			 holder.editImage = (ImageView)hview.findViewById(R.id.edit_iv);
			 holder.progressBar=(ProgressBar) hview.findViewById(R.id.load_pb);
			 holder.appName = (TextView)hview.findViewById(R.id.name_tv);
			 holder.appImage.setTag(holder);
			 holder.appImage.setOnClickListener(mItemClicker);
			 holder.appImage.setOnLongClickListener(mItemLongClicker);
			holder.app=item;
			holder.appName.setText(item.getApkName());
//			holder.editImage.setImageResource(R.drawable.del);
			holder.editImage.setTag(holder);
			holder.editImage.setOnClickListener(mItemClicker);
//			holder.editImage.setVisibility(View.GONE);
			switch (item.getFlag()) {
			case Appdb.INSTALLED:// =2,
				if(item.isAble){
					holder.appImage.setImageBitmap(item.getBitmapColor());
				}else{
					holder.appImage.setImageBitmap(item.getBitmapGray());
				}
				break;
			case Appdb.UNINSTALLED:// =3;
				holder.appImage.setImageBitmap(item.getBitmapGray());
				break;
			default:
				holder.appImage.setImageBitmap(item.getBitmapGray());
				break;
			}
			View v=hview.findViewById(R.id.app_item_lin);
			((LinearLayout)hview).removeAllViews();
			v.setTag(holder);
			item.setView(v);
	}
	public void addItem(AppItem item) {
		if(item.getFlag()!=AppItem.INSTALLED){
			item.setFlag(context, AppItem.INSTALLED);
		}
		setItemView(item);
			installedSl.addView(item.getView());
			list.add(item.getView());
			Log.v(LOGCAT," mark befor refreshPages "+(item.getView()==null));
	}
	public int deleteItem(String packageName){
		int location=-1;
		AppItem app;
		int count=installedSl.getChildCount();
		for(int i=0;i<count;i++){
			View v=installedSl.getChildAt(i);
			app=((GridAdapter.GridHolder)v.getTag()).app;
			if(app.getPackageName().equals(packageName)){
				Log.d(LOGCAT,"count ="+count+"i "+i+app.getApkName()+" is delete.");
				installedSl.removeViewAt(i);
				list.remove(i);
				location=i;
				break;
			}
		}
		return location;
	}
	public AppItem getPackageItem(String packagename) {
		if(packagename==null){
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
			if(packagename.equals(app.getPackageName())){
				return app;
			}
		}
		return null;
	}
	public void setScroll(ScrollLayout installedSl) {
		super.setScroll(installedSl);
		installedSl.setOnClickListener(scrollClicker);
	}
	
	public void toRead() {
			Log.v(LOGCAT,"apk read from db");
			if(list!=null){
				list.clear();
			}if(installedSl!=null){
				installedSl.removeAllViews();
			}
			toRead(context,Appdb.INSTALLED);
	}
	
	public boolean startApp(String packageName){
		Intent mLaunchIntent = context.getPackageManager().getLaunchIntentForPackage(
				packageName);
         boolean enabled = false;
         if(mLaunchIntent != null) {
             List<ResolveInfo> list = context.getPackageManager().
                     queryIntentActivities(mLaunchIntent, 0);
             if (list != null && list.size() > 0) {
                 enabled = true;
                 context.startActivity(mLaunchIntent);
             }
         }
         return enabled;
	}
	private android.view.View.OnClickListener mItemClicker=new android.view.View.OnClickListener(){

		
		public void onClick(View view) {
			AppItem app=((GridHolder)view.getTag()).app;
			if(view.getId()==R.id.icon_iv){
				if(editApp!=null){
					if(!app.equals(editApp)){
						editApp.setMark(false);
						editApp=null;
					}
					return ;
				}
				switch (app.getFlag()) {
				case Appdb.INSTALLED:// =2,
					if(!app.isAble()){
						CustomDialog.createDialog(context,R.string.app_disable);
						return ;
					}
					if (app.getPackageName() != null) {
						Intent mLaunchIntent = context.getPackageManager().getLaunchIntentForPackage(
								app.getPackageName());
			            if(mLaunchIntent != null) {
			                List<ResolveInfo> list = context.getPackageManager().
			                        queryIntentActivities(mLaunchIntent, 0);
			                if (list != null && list.size() > 0) {
			                    context.startActivity(mLaunchIntent);
			                    break;
			                }
			            }
					}
					toMoveUninstall(app);
					break;
				}
			}else{
				Log.v(LOGCAT,"uninstall application packageName="+app.getPackageName());
				view.setVisibility(View.GONE);
				app.setMark(false);
				editApp=null;
				if(app.getPackageName()!=null){
					context.startActivity(Utils.uninstallApplication(app.getPackageName()));
				}else{
					toMoveUninstall(app);
				}
			}
		}
		
	};
	private android.view.View.OnLongClickListener mItemLongClicker=new android.view.View.OnLongClickListener() {
		
		
		public boolean onLongClick(View view) {
			AppItem app=((GridHolder)view.getTag()).app;
			if(!app.equals(editApp)){
				if(editApp!=null){
					editApp.setMark(false);
				}
				app.setMark(true);
				editApp=app;
			}
			return false;
		}
	};

	OnClickListener scrollClicker=new OnClickListener() {
		
		public void onClick(View arg0) {
			if(editApp!=null){
				editApp.setMark(false);
				editApp=null;
				
			}
		}
	};
	private void toMoveUninstall(final AppItem app){
		final CustomDialog cd=new CustomDialog(context);
        cd.setCancelable(true);
        cd.show();
        cd.setMessage(R.string.app_unexsit);
        cd.addPositiveButton(R.string.sure,  new View.OnClickListener() {
			
			public void onClick(View v) {
				if(app.getApkId()!=null&&app.getApkVersion()!=null){
					((Main)context).delAppItem(app.getApkId(),app.getApkVersion());
					((Main)context).addApkItem(app);
				}else{
//					((Main)context).delAppItem(app.getApkId());
//					CustomDialog.createDialog(context,"≥Ã–Ú∆Ù∂Ø¥ÌŒÛ:"+app.getApkName());
				}
			    cd.dismiss();
			}
	  });
        cd.addNegativeButton(R.string.cancel, new View.OnClickListener() {
			   
			   public void onClick(View v) {
			   		cd.dismiss();
			   }
		});
	}
}
