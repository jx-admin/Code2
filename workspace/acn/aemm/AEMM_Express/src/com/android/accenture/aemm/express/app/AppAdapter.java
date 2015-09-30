package com.android.accenture.aemm.express.app;

import java.util.List;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.accenture.aemm.express.AppItem;
import com.android.accenture.aemm.express.Appdb;
import com.android.accenture.aemm.express.ItemClicker;
import com.android.accenture.aemm.express.R;
import com.android.accenture.aemm.express.Utils;
import com.android.accenture.aemm.express.log.Logger;
import com.android.accenture.aemm.express.log.LoggerFactory;

public class AppAdapter extends GridAdapter {
	private List<AppItem> mList;
	private Context mContext;
	public final static int APP_PAGE_SIZE = 12;
	private int startIndex;
	private Logger log;
	public AppAdapter(Context context, List<AppItem> list, int page) {
		log=LoggerFactory.getLogger(this.getClass());
		log.i("create -> "+this.getClass().toString());
		mContext = context;
		mList = list;
		startIndex= page * APP_PAGE_SIZE;
	}
	
	public int getCount() {
		int count=mList.size()-startIndex;
		if(count>APP_PAGE_SIZE){
			count=APP_PAGE_SIZE;
		}else if(count<0){
			count=0;
		}
		return count;
	}

	public Object getItem(int position) {
		return mList.get(startIndex+position);
	}

	public long getItemId(int position) {
		return startIndex+position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		log.i("getView :"+position+" , list size:"+mList.size());
		position+=startIndex;
//		Button b=new Button(mContext);
//		b.setText("hell");
//		if(b!=null)
//		return b;
		if(position<mList.size()){
			convertView=mList.get(position).getView();
			if(convertView==null){
				AppItem appItem=mList.get(position);
				appItem.setView(createView(appItem));
				convertView=appItem.getView();
			}
		}else{
			log.e("appAdaper getView null in list.");
//			convertView=createView(index);
			return null;
		}
		return convertView;
	}
	
	private View createView(AppItem item){
		View view=item.getView();
		if (item.getView() == null) {
			View hview= LayoutInflater.from(mContext).inflate(R.layout.app_item, null);
			GridHolder holder = new GridHolder();
			 holder.appImage =(ImageView)hview.findViewById(R.id.icon_iv);
			 holder.editImage = (ImageView)hview.findViewById(R.id.edit_iv);
			 holder.progressBar=(ProgressBar) hview.findViewById(R.id.load_pb);
			 holder.appName = (TextView)hview.findViewById(R.id.name_tv);
			 holder.appImage.setOnClickListener(new ItemClicker(mContext,item));
			 holder.appImage.setOnLongClickListener(new ItemLongClicker(mContext,item));
			holder.appName.setText(item.getApkName());
//			holder.editImage.setImageResource(R.drawable.del);
			holder.editImage.setOnClickListener(new EditorCliker(item));
//			holder.editImage.setVisibility(View.GONE);
			switch (item.getFlag()) {
			case Appdb.INSTALLED:// =2,
				holder.appImage.setImageBitmap(item.getBitmapColor());
				break;
			case Appdb.UNINSTALLED:// =3;
				holder.appImage.setImageBitmap(item.getBitmapGray());
				break;
			default:
				holder.appImage.setImageBitmap(item.getBitmapGray());
				break;
			}
//			View v=hview.findViewById(R.id.app_item_lin);
//			((LinearLayout)hview).removeAllViews();
//			view=v;
			view=hview;
			view.setTag(holder);
		}
		return view;
	}
	
	class ItemLongClicker implements OnLongClickListener {
		Context context;
		AppItem item;

		public ItemLongClicker(Context context, AppItem item) {
			this.context=context;
			this.item=item;
		}

		@Override
		public boolean onLongClick(View v) {
			((GridHolder)item.getView().getTag()).editImage.setVisibility(View.VISIBLE);
			item.setMark(true);
			return false;
		}
	}

	class EditorCliker implements android.view.View.OnClickListener{
		Appdb appdb;
		public EditorCliker(Appdb app){
			appdb=app;
		}
		public void onClick(View v) {
			String packageName=appdb.getPackageName();
			if(packageName!=null){
				mContext.startActivity(Utils.uninstallApplication(packageName));
			}else{
				log.e("uninstall application nuknow");
			}
			v.setVisibility(View.GONE);
		}
	}
}
