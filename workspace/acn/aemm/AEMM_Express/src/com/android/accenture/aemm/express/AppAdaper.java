package com.android.accenture.aemm.express;


import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Gallery.LayoutParams;

import com.android.accenture.aemm.express.GridAdapter.GridHolder;
import com.android.accenture.aemm.express.app.ScrollLayout;
import com.android.accenture.aemm.express.updataservice.ProfileContent.ApkProfileContent;

/**
 * @author junxu.wang
 *
 */
public class AppAdaper extends GridAdapter {
	ScrollLayout installedSl;
	private boolean debug;
	public final static String LOGCAT="AppAdaper";
	private Context context;
	private LayoutInflater mInflater;
	
	private int iconItemWidth;
	private int iconItemHeight;
	private int itemAreaWidth;
	private int itemAreaHeight;
	private List<View>pageLs;
	private List<AppItem> list;
	
	private int pageLength;
	private int page_lines;
	private int page_column;
	private int paddingLR = 0;
	private int paddingTD = 0;
	private int pageIcons;
	private int hallMarginLR;
	private int pageIndexH;
	private AppItem editApp;
	public AppAdaper(Context c) {
		super();
		Log.v(LOGCAT,"AppAdaper");
		this.context = c;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		list=new ArrayList<AppItem>();
		pageLs=new ArrayList<View>();
		initIconDate();
	}
	/**
	 * icon date
	 */
	private void initIconDate(){
		hallMarginLR=(int) (context.getResources().getDimension(R.dimen.hall_margin_LR)/ ((Main)context).metric.density);
		itemAreaWidth=((Main)context).metric.widthPixels-hallMarginLR-hallMarginLR;
		float botominfoH=((context.getResources().getDimension(R.dimen.botem_info_h))/ ((Main)context).metric.density);
		pageIndexH=(int) ((context.getResources().getDimension(R.dimen.index_size)+(context.getResources().getDimension(R.dimen.index_margin_bottom)))/((Main)context).metric.density);
		itemAreaHeight=(int) ((context.getResources().getDimension(R.dimen.area_install_h)/((Main)context).metric.density)-pageIndexH);
		
		TextView tv = new TextView(context);
//		tv.setTextSize(context.getResources().getDimension(R.dimen.app_name_size)/ ((Main) context).metric.density);
		iconItemWidth = (int) (context.getResources().getDimension(R.dimen.app_item_w)/((Main)context).metric.density);
		iconItemHeight = (int) (((context.getResources().getDimension(R.dimen.app_icon_h)
				+(context.getResources().getDimension(R.dimen.app_icon_margin))
						+tv.getLineHeight()))/((Main)context).metric.density);

		Log.v(LOGCAT, "hallMarginLR:"
				+hallMarginLR
				+"icon_item_w:"
				+ iconItemWidth
				+ " iconItemHeight(cul):"
				+ iconItemHeight
				+" parentW:"+itemAreaWidth
				+" parentH:"+itemAreaHeight
				+ " tvLineH:"
				+ tv.getLineHeight()
				+ " text sp:"
				+ context.getResources()
						.getDimension(R.dimen.app_name_size));
//		page_column=itemAreaWidth/iconItemWidth;
//		page_lines=itemAreaHeight/iconItemHeight;
		page_column=(int) context.getResources().getDimension(R.dimen.install_page_columns);
		page_lines=(int) context.getResources().getDimension(R.dimen.install_page_lines);
		
		if (page_lines != 0) {
			paddingLR = ((itemAreaWidth-iconItemWidth*page_column) /(page_column-1)) >> 1;
			paddingTD = ((itemAreaHeight-iconItemHeight*page_lines) / page_lines) >> 1;
		}
		pageIcons=page_lines*page_column;
		Log.v(LOGCAT,"app page_lines:"+page_lines+" page_column:"+page_column+" paddingLR:"+paddingLR+" paddingTD:"+paddingTD);
	}

	public void onConfigurationChanged(){
		initIconDate();
		refreshPages();
	}
	public void addItem(AppItem item) {
		item.setView(null);
		addToList(item);
		addIconToPage(item);
	}

	public void initView(android.view.LayoutInflater mInflater,AppItem item){
		if (item.getView() == null) {
			View hview=mInflater.inflate(R.layout.app_item, null);
			GridHolder holder = new GridHolder();
			// holder.index=index;
			 holder.appImage =(ImageView)hview.findViewById(R.id.icon_iv);
			 holder.editImage = (ImageView)hview.findViewById(R.id.edit_iv);
			 holder.progressBar=(ProgressBar) hview.findViewById(R.id.load_pb);
			 holder.appName = (TextView)hview.findViewById(R.id.name_tv);
			 holder.appImage.setOnClickListener(new ItemClicker(context,item));
			 holder.appImage.setOnLongClickListener(new ItemLongClicker(context,item));
//			 holder.appImage.setOnTouchListener(new OnTouchListener() {
//				@Override
//				public boolean onTouch(View arg0, MotionEvent arg1) {
//					int action=arg1.getAction();
//					switch(action){
//					case MotionEvent.ACTION_DOWN:
//						Toast.makeText(context, "down", 0).show();
//						longClickerHandler.postDelayed(longclicker,2000);
//						longclicker.item=item;
////						longClickerTimer.schedule(longclicker, 2000);
//						break;
//					case MotionEvent.ACTION_UP:
//						Toast.makeText(context, "up", 0).show();
//						longclicker.click();
//						longclicker.item=null;
//						longClickerHandler.removeCallbacks(longclicker);
////						longClickerTimer.cancel();
//						break;
//					case MotionEvent.ACTION_MOVE:
//						Toast.makeText(context, "move", 0).show();
//						longclicker.item=null;
//						longClickerHandler.removeCallbacks(longclicker);
//					
//						break;
//					}
//					return false;
//				}
//			});
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
			View v=hview.findViewById(R.id.app_item_lin);
			((LinearLayout)hview).removeAllViews();
			v.setTag(holder);
			item.setView(v);
		}
	}
	private void addToList(final AppItem item){
		list.add(item);
		if (item.getView() == null) {
			View hview=mInflater.inflate(R.layout.app_item, null);
			GridHolder holder = new GridHolder();
			// holder.index=index;
			 holder.appImage =(ImageView)hview.findViewById(R.id.icon_iv);
			 holder.editImage = (ImageView)hview.findViewById(R.id.edit_iv);
			 holder.progressBar=(ProgressBar) hview.findViewById(R.id.load_pb);
			 holder.appName = (TextView)hview.findViewById(R.id.name_tv);
			 holder.appImage.setOnClickListener(new ItemClicker(context,item));
			 holder.appImage.setOnLongClickListener(new ItemLongClicker(context,item));
//			 holder.appImage.setOnTouchListener(new OnTouchListener() {
//				@Override
//				public boolean onTouch(View arg0, MotionEvent arg1) {
//					int action=arg1.getAction();
//					switch(action){
//					case MotionEvent.ACTION_DOWN:
//						Toast.makeText(context, "down", 0).show();
//						longClickerHandler.postDelayed(longclicker,2000);
//						longclicker.item=item;
////						longClickerTimer.schedule(longclicker, 2000);
//						break;
//					case MotionEvent.ACTION_UP:
//						Toast.makeText(context, "up", 0).show();
//						longclicker.click();
//						longclicker.item=null;
//						longClickerHandler.removeCallbacks(longclicker);
////						longClickerTimer.cancel();
//						break;
//					case MotionEvent.ACTION_MOVE:
//						Toast.makeText(context, "move", 0).show();
//						longclicker.item=null;
//						longClickerHandler.removeCallbacks(longclicker);
//					
//						break;
//					}
//					return false;
//				}
//			});
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
			View v=hview.findViewById(R.id.app_item_lin);
			((LinearLayout)hview).removeAllViews();
			v.setTag(holder);
			item.setView(v);
		}
	}
	public int deleteItem(String name){
		int location=-1;
		AppItem app;
		for(int i=0;i<list.size();i++){
			if(list.get(i).getApkName().equalsIgnoreCase(name)){
				app=list.get(i);
				list.remove(i);
				location=i;
				break;
			}
		}
		refreshPages();
		return location;
	}
	public AppItem getItem(String name){
		AppItem app=null;
		for(int i=0;i<list.size();i++){
			if(list.get(i).getApkName().equalsIgnoreCase(name)){
				app=list.get(i);
				break;
			}
		}
		return app;
	}
	private void removeAllPages(){
		for(View tl:pageLs){
			for(int i=((LinearLayout)tl).getChildCount()-1;i>=0;i--){
				LinearLayout v=(LinearLayout) ((LinearLayout)tl).getChildAt(i);
				v.removeAllViews();
			}
			((LinearLayout)tl).removeAllViews();
		}
	}
	private void refreshPages(){
		removeAllPages();
		int pageNum=(list.size()+pageIcons-1)/pageIcons;
		int iconNum=0;
		LinearLayout tl;// 布局
		for(int p=0;p<pageNum&&iconNum<list.size();p++){
			if(p<pageLs.size()){
				tl=(LinearLayout) pageLs.get(p);
				Log.v(LOGCAT,"add item to page");
			}else{
				tl=createPageView();
				pageLs.add(tl);
				Log.v(LOGCAT,"add item to new page");
			}
			iconNum=refreshPage(tl,iconNum);
		}
		for(int k=pageNum;k<pageLs.size();k++){
			pageLs.remove(k);
		}
	}
	
	private void addIconToPage(AppItem item){
		int last=list.size()%(pageIcons);
		// 布局
		LinearLayout tl;
		if(last==1){// the first one ,add it to a new page.
			tl=createPageView();
			pageLs.add(tl);
			Log.v(LOGCAT,"add item to new page");
		}else{
			if(last==0){//It's the latest one in the end page
				last=pageIcons;
			}
			tl=(LinearLayout) pageLs.get(pageLs.size()-1);
			for(int i=tl.getChildCount()-1;i>=0;i--){
				LinearLayout v=(LinearLayout) tl.getChildAt(i);
				v.removeAllViews();
			}
			tl.removeAllViews();
			Log.v(LOGCAT,"add item to page");
		}
		int num =list.size()-last;
		refreshPage(tl,num);
	}
	private int refreshPage(LinearLayout tl,int iconNum){
		for (int i = 0; i < page_lines; i++) {
			if(iconNum<list.size()){
				LinearLayout tr=new LinearLayout(context);
				LinearLayout.LayoutParams l=new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
				l.weight=1;
				tr.setLayoutParams(l);
				if(debug){
					tr.setBackgroundColor(0x55ff0000);
				}
				for (int j=0;j<page_column; j++,iconNum++) {
					View v ;
					if(iconNum<list.size()){
						v =list.get(iconNum).getView();// mInflater.inflate(R.layout.app_item, null);
					}else {//空itme
						View hview=mInflater.inflate(R.layout.app_item, null);
						v=hview.findViewById(R.id.app_item_lin);
						v.setVisibility(View.INVISIBLE);
						((LinearLayout)hview).removeAllViews();
					}
					LinearLayout.LayoutParams r=(android.widget.LinearLayout.LayoutParams) v.getLayoutParams();
					r.weight=1;
					v.setLayoutParams(r);
					tr.addView(v);
				}
				tl.addView(tr);
			}else{//空行
				LinearLayout tr=new LinearLayout(context);
				LinearLayout.LayoutParams l=new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
				l.weight=1;
				tr.setLayoutParams(l);
				if(debug){
					tr.setBackgroundColor(0x55ff0000);
				}
				//行中的空item
				View hview=mInflater.inflate(R.layout.app_item, null);
				View v=hview.findViewById(R.id.app_item_lin);
				v.setVisibility(View.INVISIBLE);
				((LinearLayout)hview).removeAllViews();
				
				tr.addView(v);
				tl.addView(tr);
			}
		}
		return iconNum;
	}
	public int getCount() {
		Log.v("VV","getCount:"+pageLs.size());
		return pageLs.size();
	}

	public Object getItem(int index) {
		Log.v("VV","getItem:"+index);
		return pageLs.get(index);
	}
	
	public AppItem get(int id){
		return list.get(id);
	}
	public AppItem getPackage(String packagename) {
		if(packagename==null){
			return null;
		}
		Appdb app;
		for(int i=0;i<list.size();i++){
			app=list.get(i);
			if(packagename.equals(app.getPackageName())){
				return list.get(i);
			}
		}
		return null;
	}
	public long getItemId(int index) {
		Log.v("VV","getItemId:"+index);
		return index;
	}

	public View getView(int index, View convertView, ViewGroup parent) {
		
		Log.v("VV","getView:"+index+(parent.getId()==R.id.installed_gl)+" w="+parent.getWidth()+" h="+parent.getHeight());
		convertView=pageLs.get(index);
		return convertView;
	}

	class EditorCliker implements android.view.View.OnClickListener{
		Appdb appdb;
		public EditorCliker(Appdb app){
			appdb=app;
		}
		public void onClick(View v) {
			if(appdb.getPackageName()!=null){
//				mApkManager.curEditApp=null;
				context.startActivity(Utils.uninstallApplication(appdb.getPackageName()));
			}else{
				Log.v(LOGCAT,"uninstall application nuknow");
			}
			v.setVisibility(View.GONE);
		}
	}
	public int getId(Appdb app) {
		for(int i=0;i<list.size();i++){
		if(app.getApkName().equalsIgnoreCase(list.get(i).getApkName())){
			return i;
		}
		}
		return -1;
	}
	public LinearLayout createPageView(){
		LinearLayout tl=new LinearLayout(context);
		tl.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
		tl.setPadding(hallMarginLR, 0,hallMarginLR,pageIndexH);
		tl.setOrientation(LinearLayout.VERTICAL);
		return tl;
	}
	public void toSave(Editor editor) {
		Log.v(LOGCAT,"to save...");
		if(editor!=null){
			editor.putInt("count", list.size());
			for (int i = 0; i < list.size(); i++) {
				list.get(i).toSharedPreferences(editor, i);
			}
			editor.commit();
		}else{
			ApkProfileContent apc;
			for (int i = 0; i < list.size(); i++) {
				Log.v(LOGCAT,"wrete to db:"+list.get(i).getInfo());
				apc=list.get(i).toApkProfileContent();
				int r=ApkProfileContent.updateApkContentwithRowId(context, apc.mId, apc.toContentValues());
			}
		}
	}
	public void toRead(SharedPreferences sharedPreferences) {
		if(sharedPreferences!=null){
			int count = sharedPreferences.getInt("count", 0);
			Log.v("VV", "preferences :" + count);
			if (count <= 0) {
				return;
			}
			if (list != null) {
				list.clear();
			}
			for (int i = 0; i < count; i++) {
				AppItem app = new AppItem();
				app.formSharedPreferences(sharedPreferences, i);
				addToList(app);
				Log.v("VV", "preferences :" + app.getInfo());
			}
			refreshPages();
		}else{
			Log.v(LOGCAT,"app to read db");
			List <ApkProfileContent> apkProList=ApkProfileContent.queryApkContentswithFlag(context,Appdb.INSTALLED);
			if(apkProList!=null&&apkProList.size()>0){
				if (list != null) {
					list.clear();
				}
				AppItem app;
				for(ApkProfileContent pro:apkProList){
					app=new AppItem(pro);
					app.setFlag(context,Appdb.INSTALLED);
					addToList(app);
					Log.v(LOGCAT,"read:"+app.getInfo());
//					addItem(app);
				}
				refreshPages();
			}
		}
	}
	public void setEdit(AppItem app){
		if(editApp!=null){
			if(!editApp.equals(app)){
				editApp.setMark(false);
				if(app!=null){
					app.setMark(true);
				}
				editApp=app;
			}
		}else{
			if(app!=null){
				app.setMark(true);
			}
			editApp=app;
		}
	}
	public void setAppAbled(String []ids,boolean[]enable){
		if(ids==null||enable==null){
			Log.e(LOGCAT,"setAppAbled->ids||enable is null");
			return ;
		}
		Log.e(LOGCAT,"setAppAbled ok");
		int idLength=ids.length;
		int ableLength=enable.length;
		idLength=idLength<ableLength?idLength:ableLength;
		int appSize=list.size();
		String id=null;
		for(int i=0;i<idLength;i++){
			id=ids[i];
			for(int j=0;j<appSize;j++){
				Log.v(LOGCAT,"app id:"+ list.get(j).getApkId());
				if(list.get(j).getApkId().equals(ids[i])){
					list.get(j).setAble(enable[i]);
					Log.v(LOGCAT,"setappAbled:"+id+" "+enable[i]);
					break;
				}
			}
		}
	}
	Handler longClickerHandler=new Handler();
	Timer longClickerTimer=new Timer();
	LongClicker longclicker=new LongClicker();
	class LongClicker extends TimerTask{
		AppItem item;
		public void setApp(AppItem app){
			item=app;
		}
		public void click(){
			if(item!=null){
//				Toast.makeText(context, "touch click", 0).show();

				if(editApp!=null){
					if(!item.equals(editApp)){
						editApp.setMark(false);
						editApp=null;
					}
					return ;
				}
				GridAdapter.GridHolder holder = (GridAdapter.GridHolder) item.getView().getTag();
				if(!item.isAble()){
					return;
				}
				switch (item.getFlag()) {
				case Appdb.UNINSTALLED:
					if(holder.progressBar.getVisibility()==View.VISIBLE){
						return;
					}
					holder.editImage.setVisibility(View.GONE);
					holder.progressBar.setVisibility(View.VISIBLE);
					new ApkLoader(context,item,((Main)context).handler).start();
					break;
				case Appdb.INSTALLED:// =2,
					if (item.getPackageName() != null) {
						 PackageManager pm = context.getPackageManager();  
						 PackageInfo pi=null;
						try {
							pi = pm.getPackageInfo(item.getPackageName(), 0);
						} catch (NameNotFoundException e1) {
							// TODO Auto-generated catch block
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
						Log.v("V","程序错误:"+item.getApkName());
						item.setFlag(context,Appdb.UNINSTALLED);
					}
					break;
				}
				// appGv.invalidate();
				// appGv.invalidateViews();
			
			}
		}
		public void run(){
//			Toast.makeText(context, "touch longcliker", 0).show();
			if(item==null){
				return;
			}
			if(!item.equals(editApp)){
				if(editApp!=null){
					editApp.setMark(false);
				}
				item.setMark(true);
				editApp=item;
			}
			item=null;
		}
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
			if(!item.equals(editApp)){
				if(editApp!=null){
					editApp.setMark(false);
				}
				item.setMark(true);
				editApp=item;
			}
			return false;
		}
	}
	public class ItemClicker implements android.view.View.OnClickListener{
		AppItem appdb;
		Context context;
		public ItemClicker(Context context,AppItem item){
			this.context=context;
			appdb=item;
		}
		@Override
		public void onClick(View v) {
			Log.v(LOGCAT,"is able ="+appdb.isAble());
			if(!appdb.isAble()){
				CustomDialog.createDialog(context,R.string.app_disable);
				return;
			}
			if(editApp!=null){
				if(!appdb.equals(editApp)){
					editApp.setMark(false);
					editApp=null;
				}
				return ;
			}
			GridAdapter.GridHolder holder = (GridAdapter.GridHolder) appdb.getView().getTag();
			switch (appdb.getFlag()) {
			case Appdb.UNINSTALLED:// =3;
				if(holder.progressBar.getVisibility()!=View.VISIBLE){
				holder.editImage.setVisibility(View.GONE);
				holder.progressBar.setVisibility(View.VISIBLE);
				new ApkLoader(context,appdb,((Main)context).handler).start();
				}
				break;
			case Appdb.INSTALLED:// =2,
				if (appdb.getPackageName()!= null) {
//					 PackageManager pm = context.getPackageManager();  
//					 PackageInfo pi=null;
//					try {
//						pi = pm.getPackageInfo(appdb.getPackageName(), 0);
//					} catch (NameNotFoundException e1) {
//						e1.printStackTrace();
//					}
//					
//					if (pi!= null) {
//						Log.v(LOGCAT,"pi.activities is null "+(pi.activities==null));
//						Log.v("V", "activitys:"+ pi.activities.length);
//						for (ActivityInfo ainfo : pi.activities) {
//							try {
//								context.startActivity(Utils.startApplication(ainfo));
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//						}
//						break;
//					}
					if(startApp(appdb.getPackageName())){
						
					}else{
						Log.v("V","程序错误:"+appdb.getApkName());
						appdb.setFlag(context,Appdb.UNINSTALLED);
					}
				}
				break;
			}
			// appGv.invalidate();
			// appGv.invalidateViews();
		}
		
	}
	private boolean startApp(String packageName){
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
	public void setScroll(ScrollLayout installedSl) {
		this.installedSl=installedSl;
	}
}

