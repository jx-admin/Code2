package com.custom.view.utils;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class VirtulScrollerMenu implements OnClickListener {
	public static final String TAG = VirtulScrollerMenu.class.getSimpleName();
	public Object obj;
	// title
	private List<View> items=new ArrayList<View>();
	private ImageView mImageView;
	private float mCurrentCheckedRadioLeft;// ��ǰ��ѡ�е�RadioButton�������ľ���
	private OnItemSelectedListener mOnItemSelectedListener;
	public interface OnItemSelectedListener{
		public void onSelected(VirtulScrollerMenu virtulMenuScroller,View view);
	}
	public VirtulScrollerMenu() {
	}
	
	public void addItem(View view){
		items.add(view);
		view.setOnClickListener(this);
	}
	
	public void removeItem(View view){
		view.setOnClickListener(null);
		items.remove(view);
	}
	
	public void setScrollerView(ImageView scrollerView){
		 this.mImageView=scrollerView;
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
	private void moImg(float tx,float towidth) {
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

	public void setOnItemSelectedListener( OnItemSelectedListener onItemSelectedListener) {
		this.mOnItemSelectedListener = onItemSelectedListener;
	}

	boolean hasMeasured = false;


	@Override
	public void onClick(View v) {
		if(mImageView!=null)
		moImg(v );
//		 mImageView.bringToFront();
		// mHorizontalScrollView.smoothScrollTo((int)mCurrentCheckedRadioLeft,0);
		if (mOnItemSelectedListener != null) {
			mOnItemSelectedListener.onSelected(this, v);
		}
	}

}
