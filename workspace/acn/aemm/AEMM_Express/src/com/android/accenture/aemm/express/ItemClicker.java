package com.android.accenture.aemm.express;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class ItemClicker implements android.view.View.OnClickListener{
	public static final String LOGCAT="itemClicker";
	AppItem appdb;
	Context context;
	public ItemClicker(Context context,AppItem item){
		this.context=context;
		appdb=item;
	}
	@Override
	public void onClick(View v) {
		if(!(appdb.isAble()&&((Main)context).isAble())){
			CustomDialog.createDialog(context,R.string.app_disable);
			return;
		}
		if(appdb.isLock()){
			return;
		}
		GridAdapter.GridHolder holder = (GridAdapter.GridHolder) appdb.getView().getTag();
		switch (appdb.getFlag()) {
		case Appdb.NEWAPP:
			if(!(appdb.isAble()&&((Main)context).isAble())){
				CustomDialog.createDialog(context,R.string.apk_disable);
				return ;
			}
			appdb.setFlag(context, AppItem.UNINSTALLED);
			holder.editImage.setVisibility(View.GONE);
		case Appdb.UNINSTALLED:// =3;
			Log.v(LOGCAT,"onclick "+appdb.isAble()+ " hall:"+((Main)context).isAble());
			if(!(appdb.isAble()&&((Main)context).isAble())){
				CustomDialog.createDialog(context,R.string.apk_disable);
				return ;
			}
			appdb.setLock(true);
			((Main)context).downloadApp(context,appdb);
			break;
		case Appdb.INSTALLED:// =2,
			if(!appdb.isAble()){
				CustomDialog.createDialog(context,R.string.app_disable);
				return ;
			}
			if (appdb.getPackageName() != null) {
				 PackageManager pm = context.getPackageManager();  
				 PackageInfo pi=null;
				try {
					pi = pm.getPackageInfo(appdb.getPackageName(), 0);
				} catch (NameNotFoundException e1) {
					e1.printStackTrace();
				}
				if (pi!= null) {
					Log.v("V", "activitys:"+ pi.activities.length);
					for (ActivityInfo ainfo : pi.activities) {
						try {
							context.startActivity(Utils.startApplication(ainfo));
						} catch (Exception e) {
						}
					}
					break;
				}
			}
			Log.v("V","³ÌÐò´íÎó:"+appdb.getApkName());
			Toast.makeText(context, context.getText(R.string.app_erro)+appdb.getApkName(), 0).show();
//			appdb.setFlag(context,Appdb.UNINSTALLED);
			break;
		}
	}
	
}
