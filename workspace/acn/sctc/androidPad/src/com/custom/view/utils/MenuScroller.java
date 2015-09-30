package com.custom.view.utils;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.act.sctc.R;
import com.act.sctc.util.Logger;

public class MenuScroller extends BaseManager implements OnCheckedChangeListener {
	public static final String TAG = MenuScroller.class.getSimpleName();
	// title
	private RadioGroup mRadioGroup;
	private ImageView mImageView;
	private float mCurrentCheckedRadioLeft;// ��ǰ��ѡ�е�RadioButton�������ľ���
	private RadioGroup.OnCheckedChangeListener onCheckedChangeListener;
	private int radioGroupId = -1;

	public MenuScroller(ViewGroup layout) {
		this(layout, R.id.radioGroup);
	}

	public MenuScroller(ViewGroup layout, int radioGroupId) {
		super(layout);
		this.radioGroupId = radioGroupId;
		iniController();
		iniListener();
	}

	public RadioGroup getRadioGroup() {
		return mRadioGroup;
	}

	private void moImg(View toView) {
		AnimationSet _AnimationSet = new AnimationSet(true);
		float tox = ((float) toView.getWidth()) / mImageView.getWidth();
		ScaleAnimation _ScaleAnimation = new ScaleAnimation(1f, tox, 1f, 1f, Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, 0f);
		TranslateAnimation _TranslateAnimation;
		_TranslateAnimation = new TranslateAnimation(mCurrentCheckedRadioLeft, toView.getLeft(), 0f, 0f);
		_AnimationSet.addAnimation(_ScaleAnimation);
		_AnimationSet.addAnimation(_TranslateAnimation);
		_AnimationSet.setFillBefore(true);
		_AnimationSet.setFillAfter(true);
		_AnimationSet.setDuration(100);
		mImageView.startAnimation(_AnimationSet);
		mCurrentCheckedRadioLeft = toView.getLeft();
	}

	private void moImg(float tx, float towidth) {
		AnimationSet _AnimationSet = new AnimationSet(true);
		float tox = towidth / mImageView.getWidth();
		ScaleAnimation _ScaleAnimation = new ScaleAnimation(1f, tox, 1f, 1f, Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		TranslateAnimation _TranslateAnimation;
		_TranslateAnimation = new TranslateAnimation(mCurrentCheckedRadioLeft, tx, 0f, 0f);
		_AnimationSet.addAnimation(_ScaleAnimation);
		_AnimationSet.addAnimation(_TranslateAnimation);
		_AnimationSet.setFillBefore(true);
		_AnimationSet.setFillAfter(true);
		_AnimationSet.setDuration(100);
		mImageView.startAnimation(_AnimationSet);
		mCurrentCheckedRadioLeft = tx;
	}

	/**
	 * RadioGroup���CheckedChanged����
	 */
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		View v = getCurrentCheckedView(checkedId);
		if (v == null) {
			return;
		}
		if (mImageView != null)
			moImg(v);
		if (onCheckedChangeListener != null) {
			onCheckedChangeListener.onCheckedChanged(group, checkedId);
		}
	}

	/**
	 * return radiomenu left px
	 */
	private View getCurrentCheckedView(int checkedId) {
		for (int i = mRadioGroup.getChildCount() - 1; i >= 0; i--) {
			RadioButton v = (RadioButton) mRadioGroup.getChildAt(i);
			if (v.isChecked()) {
				return v;
			}
		}
		return null;
	}

	public void setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener onCheckedChangeListener) {
		this.onCheckedChangeListener = onCheckedChangeListener;// mRadioGroup.setOnCheckedChangeListener(onCheckedChangeListener);
	}

	boolean hasMeasured = false;

	private void iniController() {
		mRadioGroup = (RadioGroup) layout.findViewById(radioGroupId);
		mImageView = (ImageView) layout.findViewById(R.id.img1);

		ViewTreeObserver vto = layout.getViewTreeObserver();
		vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			public boolean onPreDraw() {
				if (hasMeasured == false) {
					int moreWidth = (int) Math.ceil((layout.getMeasuredWidth() - mRadioGroup.getMeasuredWidth())
							/ (float) mRadioGroup.getChildCount());
					if (moreWidth > 0) {
						if (mRadioGroup.getCheckedRadioButtonId() <= 0) {
							moImg(0, 0);
						}
						for (int i = mRadioGroup.getChildCount() - 1; i >= 0; i--) {
							View childView = mRadioGroup.getChildAt(i);
							LayoutParams lp2 = childView.getLayoutParams();
							lp2.width = childView.getWidth() + moreWidth;
							childView.setLayoutParams(lp2);
							if (((RadioButton) childView).isChecked()) {
								moImg(childView.getLeft(), childView.getWidth() + moreWidth);
							}
						}
					} else {
						hasMeasured = true;
					}
				}
				return true;
			}
		});
	}

	private void iniListener() {
		mRadioGroup.setOnCheckedChangeListener(this);
	}

	public void addView(View child) {
		hasMeasured = false;
		mRadioGroup.addView(child);
	}

	public void removeAllViews() {
		mRadioGroup.removeAllViews();
	}

	public void addView(View child, int index) {
		hasMeasured = false;
		mRadioGroup.addView(child, index);
	}

	public void addView(View child, ViewGroup.LayoutParams params) {
		hasMeasured = false;
		mRadioGroup.addView(child, params);
	}

	public View getLayout() {
		return layout;
	}

}
