package com.accenture.mbank.util;

import it.gruppobper.ams.android.bper.R;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class ScrollerLinearMenu implements OnClickListener {
	public static final String TAG = ScrollerLinearMenu.class.getSimpleName();
	protected Context context;
	protected View layout;
	LinearLayout.LayoutParams margin ;
	LayoutParams sizeLp;
	
	// title
	private LinearLayout menuLayout;
	private ImageView mImageView;
	private float mCurrentCheckedRadioLeft;// ��ǰ��ѡ�е�RadioButton�������ľ���
	private OnItemClickListener OnItemClickListener;
	private int indexBitmapId;
	private int defaultBitMapId=R.drawable.paging_account_notselected;
	private int position=-1;

	public ScrollerLinearMenu(View layout) {
		this( layout, -1);
	}

	public ScrollerLinearMenu(View layout, int dividerId) {
		this.layout = layout;
		context = layout.getContext();
		margin = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		margin.setMargins(5, 0, 5, 0);
		sizeLp=new LayoutParams(15, 15);
		
		iniController();
		iniListener();
	}

	public ViewGroup getMenuLayout(){
		return menuLayout;
	}
	
	public void setIndexBitmap(int id){
		indexBitmapId=id;
		mImageView.setImageResource(indexBitmapId);
	}

	private void moImg(View toView) {
		AnimationSet _AnimationSet = new AnimationSet(true);
		float tox = ((float) toView.getWidth()) / mImageView.getWidth();
		ScaleAnimation _ScaleAnimation = new ScaleAnimation(1f, tox, 1f, 1f,
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f);
		TranslateAnimation _TranslateAnimation;
		_TranslateAnimation = new TranslateAnimation(mCurrentCheckedRadioLeft, toView.getLeft(), 0f,
				0f);
		_AnimationSet.addAnimation(_ScaleAnimation);
		_AnimationSet.addAnimation(_TranslateAnimation);
		_AnimationSet.setFillBefore(true);
		_AnimationSet.setFillAfter(true);
		_AnimationSet.setDuration(100);
		mImageView.startAnimation(_AnimationSet);
		mCurrentCheckedRadioLeft=toView.getLeft();
	}

	private void moImg(float tx, float towidth) {
		AnimationSet _AnimationSet = new AnimationSet(true);
		float tox = towidth/ mImageView.getWidth();
		ScaleAnimation _ScaleAnimation = new ScaleAnimation(1f, tox, 1f, 1f,
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0.5f);
		TranslateAnimation _TranslateAnimation;
		_TranslateAnimation = new TranslateAnimation(mCurrentCheckedRadioLeft, tx, 0f,
				0f);
		_AnimationSet.addAnimation(_ScaleAnimation);
		_AnimationSet.addAnimation(_TranslateAnimation);
		_AnimationSet.setFillBefore(true);
		_AnimationSet.setFillAfter(true);
		_AnimationSet.setDuration(100);
		mImageView.startAnimation(_AnimationSet);
		mCurrentCheckedRadioLeft=tx;
	}


	public void setOnItemClickListener(OnItemClickListener onItemClickListener){
		this.OnItemClickListener=onItemClickListener;
	}

	private void iniController() {
		menuLayout = (LinearLayout) layout.findViewById(R.id.menu_content);
		mImageView = (ImageView) layout.findViewById(R.id.menu_index);
		mImageView.setImageResource(indexBitmapId);
		mImageView.setVisibility(View.GONE);
	}

	private void iniListener() {
		for(int i=menuLayout.getChildCount()-1;i>=0;i--){
			menuLayout.getChildAt(i).setOnClickListener(this);
		}
	}

	public void removeAllViews(){
		menuLayout.removeAllViews();
		mImageView.setVisibility(View.GONE);
		curNumber=0;
	}
	int curNumber;
	public void setAccount(int number){
		int i=curNumber;
		for(;i<number;i++){
			if(i<menuLayout.getChildCount()){
				View childV=menuLayout.getChildAt(i);
				childV.setVisibility(View.VISIBLE);
			}else{
				ImageView pointImage = new ImageView(context);
				// 设置每个小圆点的宽高
				pointImage.setLayoutParams(sizeLp);
				pointImage.setBackgroundResource(defaultBitMapId);
				menuLayout.addView(pointImage, margin);
				pointImage.setOnClickListener(this);
			}
		}
		for(;number<curNumber;number++){
			View childV=menuLayout.getChildAt(number);
			childV.setVisibility(View.GONE);
		}
		curNumber=i;
		setSelected(position);
	}

	public View getLayout() {
		return layout;
	}

	@Override
	public void onClick(View v) {
		float toX = v.getLeft();
		moImg(v);
		mImageView.bringToFront();
		mCurrentCheckedRadioLeft = toX;
		// mHorizontalScrollView.smoothScrollTo((int)mCurrentCheckedRadioLeft,
		// 0);
		if(OnItemClickListener!=null){
			OnItemClickListener.onItemClick(null, v, -1, v.getId());
		}
	}

	public void setSelected(int index) {
		if(index<0||index>=menuLayout.getChildCount()||position==index){
			return ;
		}
		ImageView indexImg;
		if(position>=0){
		indexImg=(ImageView)menuLayout.getChildAt(position);
		indexImg.setImageResource(defaultBitMapId);
		}
		position=index;
		indexImg=(ImageView)menuLayout.getChildAt(position);
		indexImg.setImageResource(indexBitmapId);
		if(OnItemClickListener!=null){
			OnItemClickListener.onItemClick(null, indexImg, position, indexImg.getId());
		}
//		menuLayout.getChildAt(index).performClick();
	}
	public void setVisibility(int visibility){
		menuLayout.setVisibility(visibility);
//		mImageView.setVisibility(visibility);
	}

}
