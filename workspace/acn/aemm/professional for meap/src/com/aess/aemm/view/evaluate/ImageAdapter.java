package com.aess.aemm.view.evaluate;


import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.aess.aemm.R;

public class ImageAdapter extends BaseAdapter{
	private List<String> imgUrl;
	private Context context;
	LayoutInflater inflater;
	public static final String LOGCAT="AsyncAdapter";
	
	public ImageAdapter(Context context,List<String> imgUrl){
		this.imgUrl = imgUrl;
		this.context = context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
//		Log.d(LOGCAT,"getCount:"+imgUrl.size());
		return imgUrl.size();
	}

	public Object getItem(int position) {
//		Log.d(LOGCAT,"getItem:"+position+imgUrl.get(position));
		return imgUrl.get(position);
	}

	public long getItemId(int position) {
//		Log.d(LOGCAT,"getItemId:"+position);
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
//		Log.d(LOGCAT,"getView:"+position);
		ImageView imageView;
		ProgressBar pb;
		if(convertView==null){
			convertView=inflater.inflate(R.layout.async_loader_item, null);
		}
		pb=(ProgressBar) convertView.findViewById(R.id.loader_pb);
		imageView = (ImageView) convertView.findViewById(R.id.imgae);
		//从前面定义的cache中找到该路径下的图片
		Bitmap map = EnvaluateEditView.cache.get(imgUrl.get(position));
		if(map == null){
			//如果没加载的，则显示默认图片
			imageView.setImageResource(R.drawable.icon);
			//如果没有加载，则启动线程其加载
			((EnvaluateEditView)context).loadImage(imgUrl.get(position));
			pb.setVisibility(View.VISIBLE);
		}else{
			//如果加载了的，则设置到Imageview
			imageView.setImageBitmap(map);
			pb.setVisibility(View.GONE);
		}
			
		return convertView;
	}

}