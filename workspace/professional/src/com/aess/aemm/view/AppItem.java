package com.aess.aemm.view;

import android.util.Log;
import android.view.View;

import com.aess.aemm.R;
import com.aess.aemm.db.ApkContent;
import com.aess.aemm.view.GridAdapter.GridHolder;
import com.aess.aemm.view.data.Appdb;


public class AppItem extends Appdb {
	private boolean isMove;
	private int dx,dy;
	private int spx,spy;
	@SuppressWarnings("unused")
	private int sx,sy;
	private int stepCount=3;
	private View view;
	private boolean isEdit;
//	public AppItem(){
//	}
	public AppItem(String name,String icon,String icon2,String url,String decrip){
		super(name,icon,icon2,url,decrip);
	}
	public AppItem(ApkContent apc){
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

	public void setApkRunningEnabled(boolean able){
		if(able==isApkRunningEnabled()){
			return;
		}
		setApkRunningEnabled(able);
		GridHolder holder = (GridHolder) view.getTag();
		//change by fengyun for bug 2876
		if(able && getApkFlag() == INSTALLED){
			holder.appImage.setImageBitmap(getBitmapColor());
		}else{
			holder.appImage.setImageBitmap(getBitmapGray());
		}
	}
	public void setEdit(boolean edit) {
		if(isEdit==edit){
			return;
		}
		isEdit=edit;
		if(view!=null){
			if(isEdit){
				((GridHolder)view.getTag()).editImage.setVisibility(View.VISIBLE);
				((GridHolder)view.getTag()).editImage.setImageResource(R.drawable.del);
			}else{
				((GridHolder)view.getTag()).editImage.setVisibility(View.GONE);
			}
		}
	}
	public boolean isEdit(){
		return isEdit;
	}
	public boolean contains(int x,int y){
		return (view.getLeft()<x&&view.getRight()>x&&view.getTop()<y&&view.getBottom()>y);
	}
	public int getDx(){
		return dx;
	}
	public int getDy(){
		return dy;
	}
	
	public void setMove(int marginLeft,int marginTop,int width,int cWidth,int cHeight){
		Log.d("Item","l:"+view.getLeft()+" t:"+view.getTop()+" dx="+dx+" dy="+dy);
		this.dx=marginLeft+mPagePosition.page * width + mPagePosition.coloum * cWidth;
		this.dy=marginTop+mPagePosition.row * cHeight;
		if(!isMove){
			sx=view.getLeft();
			sy=view.getTop();
		}
		spx=(dx-view.getLeft())/stepCount;
		spy=(dy-view.getTop())/stepCount;
		isMove=true;
	}
	public boolean move(){
		if(!isMove){
			return isMove;
		}else if(Math.abs(dx-view.getLeft())<Math.abs(spx)||Math.abs(dy-view.getTop())<Math.abs(spy)){
			view.layout(dx, dy, dx+view.getWidth(), dy+view.getHeight());
			isMove=false;
		}else{
			view.layout(view.getLeft()+spx, view.getTop()+spy, view.getLeft()+spx+view.getWidth(), view.getTop()+spy+view.getHeight());
		}
		return isMove;
	}
	public void endMove(PagePosition pagePosition){
		if(isMove){
			view.layout(dx, dy, dx+view.getWidth(), dy+view.getHeight());
			isMove=false;
		}
		sx=dx;
		sy=dy;
		mPagePosition=pagePosition;
	}
}
