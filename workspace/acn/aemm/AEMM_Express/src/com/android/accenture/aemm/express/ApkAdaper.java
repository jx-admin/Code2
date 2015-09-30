package com.android.accenture.aemm.express;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Gallery.LayoutParams;

import com.android.accenture.aemm.express.app.ScrollLayout;
import com.android.accenture.aemm.express.updataservice.ProfileContent.ApkProfileContent;

public class ApkAdaper extends GridAdapter {
	ScrollLayout installedSl;
	private boolean isDebug;
	public final static String LOGCAT="ApkAdaper";
	private Context context;
	private LayoutInflater mInflater;
	
	private int itemAreaWidth;
	private int itemAreaHeight;
	private int iconItemWidth;
	private int iconItemHeight;
	private List<View>pageLs;
	private List<AppItem> list;
	
	private int page_lines;
	private int page_column;
	private int paddingLR = 0;
	private int paddingTD = 0;
	private int pageIcons;
	private int hallMarginLR;
	private int pageIndexH;
	public ApkAdaper(Context c) {
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
		itemAreaHeight=(int) ((context.getResources().getDimension(R.dimen.area_uninstall_h)/((Main)context).metric.density)-pageIndexH);
		
		TextView tv = new TextView(context);
		tv.setTextSize(context.getResources().getDimension(R.dimen.app_name_size)/ ((Main) context).metric.density);
		int minMarginLR=(int) (context.getResources().getDimension(R.dimen.app_item_margin_L_R));
		iconItemWidth = (int) ((context.getResources().getDimension(R.dimen.app_item_w)
				+context.getResources().getDimension(R.dimen.apk_descrip_margin_left)
				+context.getResources().getDimension(R.dimen.apk_descrip_w))/((Main)context).metric.density);
		int descripH=(int) (context.getResources().getDimension(R.dimen.apk_descrip_h)/((Main)context).metric.density);
		iconItemHeight = (int) ((context.getResources().getDimension(R.dimen.app_icon_h)
				+ context.getResources().getDimension(R.dimen.app_icon_margin)
				+ context.getResources().getDimension(R.dimen.app_item_download_progressbar_h)
				+tv.getLineHeight())/((Main)context).metric.density);
		if(iconItemHeight<descripH){
			iconItemHeight=descripH;
		}

		Log.v(LOGCAT, "apk icon_item_w:"
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
//		page_column=parentWidth/(iconItemWidth);
//		page_lines=parentHeight/iconItemHeight;
		page_column=(int) context.getResources().getDimension(R.dimen.uninstall_page_columns);
		page_lines=(int) context.getResources().getDimension(R.dimen.uninstall_page_lines);
		
		if(page_column<1){
			page_column=1;
		}
		if(page_lines<1){
			page_lines=1;
		}

		Log.v(LOGCAT,"apk page_lines:"+page_lines+" page_column:"+page_column);
		if (page_lines >1) {
			paddingTD = ((itemAreaHeight- iconItemHeight*page_lines) / (page_lines)) >> 1;
		}else{
			paddingTD = 0;
		}
		if(page_column>1){
			paddingLR = ((itemAreaWidth -iconItemWidth*page_column) / (page_column-1)) >> 1;
//			if(paddingLR>20){
//				paddingLR=20;
//			}
		}else{
			paddingLR = 0;
		}
		pageIcons=page_lines*page_column;
	}
	public void onConfigurationChanged(){
		initIconDate();
		refreshPages();
	}
	public void addItem(AppItem item) {
		if(Main.isScrollLayout){
			setItemView(item);
			if(item.isMark()){
				int i=0;
				for(;i<list.size();i++){
					Log.v(LOGCAT,"i "+i+list.get(i).getApkName());
					if(!list.get(i).isMark()){
						break;
					}
				}
				list.add(i,item);
				View v=item.getView();
				installedSl.addView(v,i);
				Log.v(LOGCAT,"size="+list.size()+" index="+i+" mark befor refreshPages "+(item.getView()==null));
			}else{
				Log.v(LOGCAT," befor refreshPages "+(item.getView()==null));
				list.add(item);
				View v=item.getView();
				v.setTag(1,item);
				installedSl.addView(v);
			}
			return;
		}
		item.setView(null);
		setItemView(item);
		
		if(item.isMark()){
			int i=0;
			for(;i<list.size();i++){
				Log.v(LOGCAT,"i "+i+list.get(i).getApkName());
				if(!list.get(i).isMark()){
					break;
				}
			}
			list.add(i,item);
			Log.v(LOGCAT,"size="+list.size()+" index="+i+" mark befor refreshPages "+(item.getView()==null));
			refreshPages();
		}else{
			Log.v(LOGCAT," befor refreshPages "+(item.getView()==null));
			list.add(item);
			addIconToPage(item);
		}
	}
	private void setItemView(AppItem item){
//		if (item.getView() == null) {
			View hview=mInflater.inflate(R.layout.apk_item, null);
			GridHolder holder = new GridHolder();
			// holder.index=index;
			holder.appImage = (ImageView) hview.findViewById(R.id.icon_iv);
			holder.editImage = (ImageView) hview.findViewById(R.id.edit_iv);
			holder.progressBar = (ProgressBar) hview.findViewById(R.id.load_pb);
			holder.appName = (TextView) hview.findViewById(R.id.name_tv);
			holder.appDecrip = (TextView) hview.findViewById(R.id.apk_descrip_tv);
			holder.appImage.setOnClickListener(new ItemClicker(context,item));
			holder.appName.setText(item.getApkName());
//			holder.editImage.setImageResource(R.drawable.star);
//			holder.editImage.setOnClickListener(new EditorCliker(item));
			if(item.isMark()){
				holder.editImage.setVisibility(View.VISIBLE);
			}else{
				holder.editImage.setVisibility(View.GONE);
			}
			if(item.getApkDescription()==null){
				holder.appDecrip.setText("");
			}else{
				holder.appDecrip.setText(item.getApkDescription());
			}
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
			View v=hview.findViewById(R.id.apk_item_lin);
			((LinearLayout)hview).removeAllViews();
			v.setTag(holder);
			item.setView(v);
//		}
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
	
	/**把新下载的apk移到最后。
	 * @param isReset 是否新排序分页
	 * @param name 名字
	 * @return 图标原位置
	 */
	public int cancelNew(String name,boolean isReset){
		int location=-1;
		AppItem app;
		for(int i=0;i<list.size();i++){
			if(list.get(i).getApkName().equalsIgnoreCase(name)){
				app=list.get(i);
				app.setMark(false);
				list.remove(i);
				LinearLayout tl=(LinearLayout) pageLs.get(pageLs.size()-1);
				for(int j=tl.getChildCount()-1;j>=0;j--){
					LinearLayout v=(LinearLayout) tl.getChildAt(j);
					v.removeAllViews();
				}
				tl.removeAllViews();
				list.add(app);
				location=i;
				if(isReset){
					refreshPages();
				}
				break;
			}
		}
		return location;
	}

	/**
	 * 重新分页
	 */
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
			for (int i = 0; i < page_lines&&iconNum<list.size(); i++) {
				LinearLayout tr=new LinearLayout(context);
				tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
				for (int j=0;j<page_column; j++,iconNum++) {
					View v=null;
					if(iconNum < list.size()){
						v=list.get(iconNum).getView();
						Log.v(LOGCAT,"iconNum="+iconNum+" v is null="+(v==null));
						Log.v(LOGCAT,"iconNum<list.size");
					}else{
						Log.v(LOGCAT,"iconNum>list.size");
						v=inflaterItem();
						v.setVisibility(View.INVISIBLE);
					}
//					if(j==0){
//						v.setPadding(0, paddingTD, paddingLR, paddingTD);
//					}else if(j==page_column-1){
//						v.setPadding(paddingLR, paddingTD, 0, paddingTD);
//					}else{
//						v.setPadding(paddingLR, paddingTD, paddingLR, paddingTD);
//					}
					if(tr==null){
						Log.v(LOGCAT,"tr is null");
					}
					if(v==null){
						Log.v(LOGCAT,"v is null");
					}
					tr.addView(v);
				}
				tl.addView(tr);
			}
		}
		for(int k=pageNum;k<pageLs.size();k++){
			pageLs.remove(k);
		}
	
	}
	
	private void addIconToPage(AppItem item){
		int last=list.size()%(pageIcons);
		LinearLayout tl;
		if(last==1||pageIcons==1){// the first one ,add it to a new page.
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
				if(isDebug){
					tr.setBackgroundColor(0x55ff0000);
				}
				for (int j=0;j<page_column; j++,iconNum++) {
					View v ;
					if(iconNum<list.size()){
						v =list.get(iconNum).getView();// mInflater.inflate(R.layout.app_item, null);
					}else {//空itme
						View hview=mInflater.inflate(R.layout.apk_item, null);
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
				if(isDebug){
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

	public void setAppAbled(String []ids,boolean[]enable){
		if(ids==null||enable==null){
			Log.e(LOGCAT,"setAppAbled->ids||enable is null");
			return ;
		}
		int idLength=ids.length;
		int ableLength=enable.length;
		idLength=idLength<ableLength?idLength:ableLength;
		int appSize=list.size();
		String id=null;
		for(int i=0;i<idLength;i++){
			id=ids[i];
			for(int j=0;j<appSize;j++){
				if(list.get(j).getApkId().equals(ids[i])){
					list.get(j).setAble(enable[i]);
					break;
				}
			}
		}
	}
//	class EditorCliker implements android.view.View.OnClickListener{
//		Appdb appdb;
//		public EditorCliker(Appdb app){
//			appdb=app;
//		}
//		public void onClick(View v) {
//			if(appdb.getPackageName()!=null){
////				mApkManager.curEditApp=null;
//				context.startActivity(Utils.uninstallApplication(appdb.getPackageName()));
//			}else{
//				Log.v(LOGCAT,"uninstall application nuknow");
//			}
//			v.setVisibility(View.GONE);
//		}
//	}
	public LinearLayout createPageView(){
		LinearLayout tl=new LinearLayout(context);
		tl.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
		tl.setPadding(hallMarginLR, 0,hallMarginLR, 0);
		tl.setOrientation(LinearLayout.VERTICAL);
		if(isDebug){
			tl.setBackgroundColor(0x550000bb);
		}
		return tl;
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
	private LinearLayout inflaterItem(){
		View hview=mInflater.inflate(R.layout.apk_item, null);
		LinearLayout item=(LinearLayout) hview.findViewById(R.id.apk_item_lin);
		((LinearLayout)hview).removeAllViews();
		return item;
	}
	public LinearLayout inflaterWeight1(){
		LinearLayout rv=(LinearLayout) mInflater.inflate(R.layout.weight_1,null);
		LinearLayout v=(LinearLayout) rv.findViewById(R.id.weight_1);
		rv.removeAllViews();
		return v;
	}
	public void toSave(Editor editor) {
		if(editor!=null){
			editor.putInt("count", list.size());
			for (int i = 0; i < list.size(); i++) {
				list.get(i).toSharedPreferences(editor, i);
			}
			editor.commit();
		}else{
			Log.v(LOGCAT,"apk to save db");
			ApkProfileContent apc;
			for (int i = 0; i < list.size(); i++) {
				Log.v(LOGCAT,"app:"+list.get(i).getInfo());
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
				setItemView(app);
				list.add(app);
				Log.v("VV", "preferences :" + app.getInfo());
			}
			refreshPages();
		}else{
			Log.v(LOGCAT,"apk read from db");
			if(list!=null){
				list.clear();
			}
			/*List <ApkProfileContent> apkProList=ApkProfileContent.queryApkContentswithFlag(context,Appdb.NEWAPP);
			if(apkProList!=null&&apkProList.size()>0){
				AppItem app;
				for(ApkProfileContent pro:apkProList){
					app=new AppItem(pro);
					app.setFlag(context,Appdb.UNINSTALLED);
					addItem(app);
					Log.v(LOGCAT,"new "+app.getInfo());
				}
			}*/
			List <ApkProfileContent> apkUninstallList=ApkProfileContent.queryApkContentswithFlag(context,Appdb.UNINSTALLED);
			if(apkUninstallList!=null&&apkUninstallList.size()>0){
				AppItem app;
				for(ApkProfileContent pro:apkUninstallList){
					app=new AppItem(pro);
					app.isBase64=true;
					app.setFlag(context,Appdb.UNINSTALLED);
					addItem(app);
					Log.v(LOGCAT,"app:"+app.getInfo());
				}
			}
		}
	}
	public void exit(){
		AppItem app;
		for(int i=0;i<list.size();i++){
			app=list.get(i);
			if(app.isMark()){
				list.remove(i);
				list.add(app);
			}else{
				break;
			}
		}
	}
	public int getMarks(){
		int count=0;
		AppItem app;
		for(int i=0;i<list.size();i++){
			app=list.get(i);
			if(app.isMark()){
				count++;
			}
//			else{
//				break;
//			}
		}
		return count;
	}
	public void setScroll(ScrollLayout installedSl) {
		this.installedSl=installedSl;
		
	}
}
