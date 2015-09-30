package com.android.accenture.aemm.dome;


import java.util.List;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * @author junxu.wang
 *
 */
public class AppAdaper extends GridAdapter {
	private Logger log=LoggerFactory.getLogger(AppAdaper.class);
	private Context context;
	private LayoutInflater mInflater;

	ApkManager mApkManager;
	private List<Appdb> list;
	public AppAdaper(Context c,ApkManager apkManager) {
		super();
		log.d("create appDaper");
		this.context = c;
		mApkManager=apkManager;
	}

	public void setList(List<Appdb> list) {
		this.list = list;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		for(int i=0;i<list.size();i++){
			list.get(i).view=createView(i);
		}
	}

	public int getCount() {
		return list.size();
	}

	public Appdb getItem(int index) {
		return list.get(index);
	}

	public long getItemId(int index) {
		return index;
	}
	
	public View createView(int index){
		View convertView = mInflater.inflate(R.layout.apk_info, null);   
		GridHolder holder = new GridHolder();
		holder.index=index;
		holder.appImage = (ImageView)convertView.findViewById(R.id.icon_iv);
		holder.editImage = (ImageView)convertView.findViewById(R.id.edit_iv);
		holder.progressBar=(ProgressBar) convertView.findViewById(R.id.load_pb);
		holder.appName = (TextView)convertView.findViewById(R.id.name_tv);
		convertView.setTag(holder);   
		Appdb appdb = list.get(index);
		if (appdb != null) {
			holder.appName.setText(appdb.getApkName());
			holder.editImage.setImageResource(R.drawable.del);
			holder.editImage.setOnClickListener(new EditorCliker(appdb));
			holder.editImage.setVisibility(View.GONE);
			switch (appdb.getFlag()) {
			case Appdb.UPDATA_NEW:// =0,
				holder.appImage.setImageBitmap(BitmapFactory.decodeFile(appdb.getApkIcon2()));
				break;
			case Appdb.NEVER_SETUP:// =1,
				holder.appImage.setImageBitmap(BitmapFactory.decodeFile(appdb.getApkIcon2()));
				break;
			case Appdb.INSTALLED:// =2,
				holder.appImage.setImageBitmap(BitmapFactory.decodeFile(appdb.getApkIcon()));
				break;
			case Appdb.UNINSTALLED:// =3;
				holder.appImage.setImageBitmap(BitmapFactory.decodeFile(appdb.getApkIcon2()));
				break;
			case Appdb.ENABLE:
				holder.appImage.setImageBitmap(BitmapFactory.decodeFile(appdb.getApkIcon2()));
				break;
			default:
				holder.appImage.setImageBitmap(BitmapFactory.decodeFile(appdb.getApkIcon2()));
				break;
			}
		}
		return convertView;
	}
	public View getView(int index, View convertView, ViewGroup parent) {
		log.d("appAdaper getView :"+index+" & list size:"+list.size());
		if(index<list.size()){
			convertView=list.get(index).view;
		}else{
			log.e("appAdaper getView null in list & create.");
			convertView=createView(index);
		}
		return convertView;
	}
	class EditorCliker implements android.view.View.OnClickListener{
		Appdb appdb;
		public EditorCliker(Appdb app){
			appdb=app;
		}
		public void onClick(View v) {
			if(appdb.packageInfo!=null){
				mApkManager.curEditApp=null;
				context.startActivity(Utils.uninstallApplication(appdb.packageInfo.packageName));
			}else{
				log.e("uninstall application nuknow");
			}
			v.setVisibility(View.GONE);
		}
	}
	public void add(Appdb app) {
		log.d("appAdaper to add appdb");
		if(app.getApkFile()!=null){
			app.packageInfo=Utils.getUninatllApkInfo(context,app.getApkFile());
		}
		Utils.checkIntalled(context, app);
		list.add(app);
		log.d("appAdaper to create appdb view");
		app.view=createView(list.size()-1);
	}

	public void clear() {
		list.clear();
	}

	public int getId(Appdb app) {
		for(int i=0;i<list.size();i++){
		if(app.getApkName().equalsIgnoreCase(list.get(i).getApkName())){
			return i;
		}
		}
		return -1;
	}

	public void remove(int location) {
		list.remove(location);
	}

	public Appdb getPackage(String packagename) {
		if(packagename==null){
			return null;
		}
		Appdb app;
		for(int i=0;i<list.size();i++){
			app=list.get(i);
			if(app.packageInfo!=null&&app.packageInfo.packageName!=null
					&&packagename.equals(app.packageInfo.packageName)){
				return list.get(i);
			}
		}
		return null;
	}
}

