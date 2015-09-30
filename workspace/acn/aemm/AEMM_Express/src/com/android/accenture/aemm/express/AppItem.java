package com.android.accenture.aemm.express;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.accenture.aemm.express.GridAdapter.GridHolder;
import com.android.accenture.aemm.express.updataservice.ProfileContent.ApkProfileContent;

public class AppItem extends Appdb {
	private View view;
	public AppItem(){
	}
	public AppItem(String name,String icon,String icon2,String url,String decrip){
		super(name,icon,icon2,url,decrip);
	}
	public AppItem(ApkProfileContent apc){
		super(apc);
	}
	public View getView(){
		return view;
	}
	public void setView(View v){
		if(v==null){
			return ;
		}
		view=v;
//		if(getFlag()==INSTALLED){
//			GridHolder holder = (GridHolder) view.getTag();
//			if(holder.appDecrip!=null)
//			holder.appDecrip.setVisibility(View.GONE);
//		}else{
//			GridHolder holder = (GridHolder) view.getTag();
//			if(holder.appDecrip!=null)
//			holder.appDecrip.setVisibility(View.VISIBLE);
//		}
	}

	public boolean isAble(){
		return isAble;
	}
	public void setAble(boolean able){
		if(able==isAble){
			return;
		}
		isAble=able;
		GridHolder holder = (GridHolder) view.getTag();
		//change by fengyun for bug 2876
		if(isAble && getFlag() == INSTALLED){
			holder.appImage.setImageBitmap(getBitmapColor());
		}else{
			holder.appImage.setImageBitmap(getBitmapGray());
		}
	}
	public void setMark(boolean isEdit) {
		super.setMark(isEdit);
		if(view!=null){
			if(isEdit){
				((GridHolder)view.getTag()).editImage.setVisibility(View.VISIBLE);
			}else{
				((GridHolder)view.getTag()).editImage.setVisibility(View.GONE);
			}
		}
	}
	public void updataView(ApkProfileContent mApkProfileContent) {
		set(mApkProfileContent);
		if(view!=null){
			GridHolder holder = (GridHolder) view.getTag();
			holder.appName.setText(getApkName());
			if(getApkDescription()==null){
				holder.appDecrip.setText("");
			}else{
				holder.appDecrip.setText(getApkDescription());
			}
			switch (getFlag()) {
			case Appdb.INSTALLED:// =2,
					holder.appImage.setImageBitmap(getBitmapColor());
				break;
			case Appdb.NEWAPP:
				holder.editImage.setVisibility(View.VISIBLE);
				holder.editImage.setImageResource(R.drawable.star);
				holder.appImage.setImageBitmap(getBitmapGray());
				break;
			case Appdb.UNINSTALLED:// =3;
				holder.appImage.setImageBitmap(getBitmapGray());
				holder.editImage.setVisibility(View.GONE);
				break;
			default:
				holder.appImage.setImageBitmap(getBitmapGray());
				break;
			}
		}
	}
}
