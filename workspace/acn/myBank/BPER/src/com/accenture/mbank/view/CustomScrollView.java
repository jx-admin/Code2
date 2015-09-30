package com.accenture.mbank.view;


import com.accenture.mbank.util.LogManager;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Transformation;
import android.widget.ScrollView;


public class CustomScrollView extends ScrollView
{

	public interface OnScrollStoppedListener{
		void onScrollStopped();
	}

	private boolean touchDispatched = false;
	private boolean isAnimating = false;
	private int initialPosition;
	private int newCheck = 500;
	private int position = 0;
	private int currentY = 0;
	private OnScrollStoppedListener onScrollStoppedListener;
	private Runnable scrollerTask;

	public CustomScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public CustomScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CustomScrollView(Context context) {
		super(context);
		init();
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		currentY = t;
		ViewGroup layer = (ViewGroup) this.getChildAt(0);
		for(int i = 0; i < layer.getChildCount(); i++) {
			
		} 
	};

	private void init() {
		onScrollStoppedListener = new OnScrollStoppedListener() {

			@Override
			public void onScrollStopped() {
				//align();
			}
		};

		scrollerTask = new Runnable() {

			public void run() {

				if (touchDispatched) {

					int newPosition = getScrollY();
					if(initialPosition - newPosition == 0){//has stopped
						if(onScrollStoppedListener!=null){
							onScrollStoppedListener.onScrollStopped();
							LogManager.d( "onScrollStoppedListener");
						}
					}else{
						initialPosition = getScrollY();
						CustomScrollView.this.postDelayed(scrollerTask, newCheck);
					}
				}
			}
		};
	}

	public void startScrollerTask(){
		initialPosition = getScrollY();
		postDelayed(scrollerTask, newCheck);
	}

	public boolean isTouchDispatched() {
		return touchDispatched;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
//		Log.d("CustomScroll", "ev.getaction: " + ev.getAction());
		isAnimating = false;
		if(ev.getAction() == MotionEvent.ACTION_UP) {
			startScrollerTask();
			touchDispatched = true;
		}

		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//			Log.d("CustomScroll", "ACTION_DOWN");
			touchDispatched = false;
			CustomScrollView.this.clearAnimation();
		}

		boolean result;
		try {
			result = super.onTouchEvent(ev);
		} catch (Exception e) {
			result = false;
		}
		
		return result;
	}

	public void setTouchDispatched(boolean touchDispatched) {
		this.touchDispatched = touchDispatched;
	}

	private class ScrollTraslation extends Animation {
		private int finalPointToScroll;
		private int initialPointToScroll;
		private boolean annullated = false;
		public ScrollTraslation(int finalPointToScroll) {
			super();
			this.finalPointToScroll = finalPointToScroll;
			this.setDuration(500);
			isAnimating = true;
			initialPointToScroll = CustomScrollView.this.getScrollY();
		}

		@Override
		protected void applyTransformation(float interpolatedTime,
				Transformation t) {
			super.applyTransformation(interpolatedTime, t);
			if(!annullated) {
				if(!isAnimating) {
					annullated = true;
				} else {
					int destinationPoint = Math.round(initialPointToScroll - (initialPointToScroll - finalPointToScroll) * interpolatedTime);
					CustomScrollView.this.scrollTo(0, destinationPoint);
				}
			}
		}
	}

	/**
	 * @author florindo.di.vito
	 * method: align
	 * description: align child on left or on right
	 */
	public void align() {
		ViewGroup firstChild = (ViewGroup) CustomScrollView.this.getChildAt(0);
//		Log.d("CustomScroll", "stop");
		if(firstChild.getChildCount() > 0) {
			int maxScroll = firstChild.getHeight() - CustomScrollView.this.getHeight();
			int finalPointToScroll = Integer.MAX_VALUE;
			for(int index = 0; index < firstChild.getChildCount(); index++) {
				position = firstChild.getChildAt(index).getHeight() * index;
				if(Math.abs(position - CustomScrollView.this.getScrollY()) < Math.abs(finalPointToScroll - CustomScrollView.this.getScrollY())) {
					finalPointToScroll = position;
				}
				
			}
			if(Math.abs(maxScroll - CustomScrollView.this.getScrollY()) < Math.abs(finalPointToScroll - CustomScrollView.this.getScrollY())) {
				finalPointToScroll = maxScroll;
			}
			if(finalPointToScroll >= 0) {
				ScrollTraslation animation = new ScrollTraslation(finalPointToScroll);
				animation.setAnimationListener(new AnimationListener() {
					
					@Override
					public void onAnimationStart(Animation animation) {
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						touchDispatched = false;
						
					}
				});
				CustomScrollView.this.startAnimation(animation);
			} 
		}
	}

	/**
	 * @return the position
	 */
	public int getPosition() {
		return position;
	}
	
	/**
	 * @return the position of y parameter
	 */
	public int getCurrentY() {
		return currentY;
	}
	
	/**
	 * @set setOnScrollStoppedListener
	 */
	public void setOnScrollStoppedListener(OnScrollStoppedListener listener) {
		onScrollStoppedListener = listener;
	}
}