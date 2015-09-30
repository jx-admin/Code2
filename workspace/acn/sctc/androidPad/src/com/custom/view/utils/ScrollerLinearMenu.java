package com.custom.view.utils;

import android.R.menu;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.act.sctc.R;

public class ScrollerLinearMenu extends BaseManager implements
		OnClickListener {
	public static final String TAG = ScrollerLinearMenu.class.getSimpleName();
	// title
	private LinearLayout menuLayout;
	private ImageView mImageView;
	private float mCurrentCheckedRadioLeft;// ��ǰ��ѡ�е�RadioButton�������ľ���
	private OnItemClickListener OnItemClickListener;
	private int layoutId;

	public ScrollerLinearMenu(ViewGroup layout) {
		this( layout, -1);
	}

	public ScrollerLinearMenu(ViewGroup layout, int dividerId) {
		super(layout);
		iniController();
		iniListener();
	}
	public ViewGroup getMenuLayout(){
		return menuLayout;
	}
	private void moImg(float xs, float xe, float ys, float ye) {
		AnimationSet _AnimationSet = new AnimationSet(true);
		TranslateAnimation _TranslateAnimation;
		_TranslateAnimation = new TranslateAnimation(xs, xe, ys, ye);
		_AnimationSet.addAnimation(_TranslateAnimation);
		_AnimationSet.setFillBefore(true);
		_AnimationSet.setFillAfter(true);
		_AnimationSet.setDuration(100);
		mImageView.startAnimation(_AnimationSet);
	}


//	/**
//	 * return radiomenu left px
//	 */
//	private float getCurrentCheckedRadioLeft(int checkedId) {
//		for (int i = mRadioGroup.getChildCount() - 1; i >= 0; i--) {
//			View v = mRadioGroup.getChildAt(i);
//			if (((RadioButton) v).isChecked()) {
//				return v.getLeft();
//			}
//		}
//		return 0;
//	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener){
		this.OnItemClickListener=onItemClickListener;
	}

	private void iniController() {
		menuLayout = (LinearLayout) layout.findViewById(R.id.menu_content);
		mImageView = (ImageView) layout.findViewById(R.id.img1);

//		ViewTreeObserver vto = layout.getViewTreeObserver();
//		vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//			public boolean onPreDraw() {
//				if (hasMeasured == false) {
//					Log.d(TAG, " add on pre draw listener");
//					// 获取到宽度和高度后，可用于计算
//					// int height = mHorizontalScrollView.getMeasuredHeight();
//					int width = layout.getMeasuredWidth()
//							/ mRadioGroup.getChildCount();
//					LayoutParams lp = mImageView.getLayoutParams();
//					lp.width = width;
//					mImageView.setLayoutParams(lp);
//					for (int i = mRadioGroup.getChildCount() - 1; i >= 0; i--) {
//						View childView = mRadioGroup.getChildAt(i);
//						LayoutParams lp2 = childView.getLayoutParams();
//						lp2.width = width;
//						childView.setLayoutParams(lp2);
//						if (((RadioButton) childView).isChecked()) {
//							moImg(mCurrentCheckedRadioLeft, width * i, 0f, 0f);
//							mCurrentCheckedRadioLeft = width * i;
//						}
//					}
//					hasMeasured = true;
//				}
//				return true;
//			}
//		});
	}

	private void iniListener() {
		for(int i=menuLayout.getChildCount()-1;i>=0;i--){
			menuLayout.getChildAt(i).setOnClickListener(this);
		}
	}

	public void removeAllViews(){
		menuLayout.removeAllViews();
	}
	public void addView(View child,LayoutParams lp) {
//		hasMeasured = false;
		menuLayout.addView(child,lp);
		child.setOnClickListener(this);
	}

	public void addView(View child, int index) {
//		hasMeasured = false;
		menuLayout.addView(child, index);
		child.setOnClickListener(this);
	}

	public View getLayout() {
		return layout;
	}

	@Override
	public void onClick(View v) {

		float toX = v.getLeft();//getCurrentCheckedRadioLeft(checkedId);

		moImg(mCurrentCheckedRadioLeft, toX, 0f, 0f);
		// 
		mImageView.bringToFront();
		// }

		mCurrentCheckedRadioLeft = toX;
		// mHorizontalScrollView.smoothScrollTo((int)mCurrentCheckedRadioLeft,
		// 0);
		if(OnItemClickListener!=null){
			OnItemClickListener.onItemClick(null, v, -1, v.getId());
		}
	}

	public void setSelected(int index) {
		menuLayout.getChildAt(index).performClick();
		
	}

}
