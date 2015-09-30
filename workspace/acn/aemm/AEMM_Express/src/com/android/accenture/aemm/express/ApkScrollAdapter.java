package com.android.accenture.aemm.express;


import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ApkScrollAdapter extends ScrollAdapter{
	
	public ApkScrollAdapter(Context c) {
		super(c);
	}
	void setItemView(AppItem item){
		View hview=mInflater.inflate(R.layout.apk_item, null);
		GridHolder holder = new GridHolder();
		// holder.index=index;
		holder.appImage = (ImageView) hview.findViewById(R.id.icon_iv);
		holder.editImage = (ImageView) hview.findViewById(R.id.edit_iv);
		holder.progressBar = (ProgressBar) hview.findViewById(R.id.load_pb);
		holder.appName = (TextView) hview.findViewById(R.id.name_tv);
		holder.appDecrip = (TextView) hview.findViewById(R.id.apk_descrip_tv);
		holder.appImage.setTag(holder);
		holder.appImage.setOnClickListener(mItemClicker);
		holder.appName.setText(item.getApkName());
		holder.app=item;
		holder.editImage.setVisibility(View.GONE);
//		holder.editImage.setImageResource(R.drawable.star);
//		holder.editImage.setOnClickListener(new EditorCliker(item));
		if(item.getApkDescription()==null){
			holder.appDecrip.setText("");
		}else{
			holder.appDecrip.setText(item.getApkDescription());
		}
		switch (item.getFlag()) {
		case Appdb.INSTALLED:// =2,
				holder.appImage.setImageBitmap(item.getBitmapColor());
			break;
		case Appdb.NEWAPP:
			holder.editImage.setVisibility(View.VISIBLE);
			holder.editImage.setImageResource(R.drawable.star);
			holder.appImage.setImageBitmap(item.getBitmapGray());
			break;
		case Appdb.UNINSTALLED:// =3;
			holder.appImage.setImageBitmap(item.getBitmapGray());
			break;
		default:
			holder.appImage.setImageBitmap(item.getBitmapGray());
			break;
		}
		View v=hview.findViewById(R.id.apk_item_lin);
		((LinearLayout)hview).removeAllViews();
		v.setTag(holder);
		item.setView(v);
	//	}
	}

	public void toRead() {
			Log.v(LOGCAT,"apk read from db");
			if(list!=null){
				list.clear();
			}
			if(installedSl!=null){
				installedSl.removeAllViews();
			}
			toRead(context,Appdb.NEWAPP);
			toRead(context,Appdb.UNINSTALLED);
	}
	public void updataNewAppMessage() {
		int count;
			count = getNewppCnt();
		if (count > 0) {
			HallMessageManager.addMessage(context, new HallMessagedb(String
					.format((String) context.getText(R.string.updata_app_push), count), 0, -1,
					10000, HallMessagedb.APPMSG));
		} else {
			HallMessageManager.addMessage(context, new HallMessagedb("", 0, 0, 0,
					HallMessagedb.APPMSG));
		}
	}
	private android.view.View.OnClickListener mItemClicker=new android.view.View.OnClickListener(){

		@Override
		public void onClick(View view) {
			GridAdapter.GridHolder holder = (GridAdapter.GridHolder) view.getTag();
			AppItem app=holder.app;
			if(app.isLock()){
				return;
			}
			switch (app.getFlag()) {
			case Appdb.NEWAPP:
				app.setFlag(context, AppItem.UNINSTALLED);
				holder.editImage.setVisibility(View.GONE);
				updataNewAppMessage();
			case Appdb.UNINSTALLED:// =3;
				if(!((Main)context).isAble()){
					CustomDialog.createDialog(context,R.string.install_disable);
					return ;
				}
//				if(!app.isAble){
//					CustomDialog.createDialog(context,R.string.apk_disable);
//					return ;
//				}
				app.setLock(true);
				((Main)context).downloadApp(context,app);
				break;
			case Appdb.INSTALLING:
//				Main.installApplication(app, context);
				break;
			case Appdb.INSTALLED:// =2,
				break;
			}
		}
	};
}
