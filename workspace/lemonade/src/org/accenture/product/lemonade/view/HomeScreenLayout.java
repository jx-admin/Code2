package org.accenture.product.lemonade.view;

import org.accenture.product.lemonade.R;
import org.accenture.product.lemonade.ScreenSwitcherActivity;
import org.accenture.product.lemonade.util.MyImg;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

public class HomeScreenLayout extends LinearLayout
{

	private View mDragItem;
	
	private ImageView delete;
	private ImageView add;
	
	private ImageView mDragView;
	
	private int mDragPointY; // 当前鼠标相对于子元素的坐标
	private int mDragPointX;			//	
	private int mouseY;
	private int mouseX;
	int mCoordOffsetY;		//the difference betweenscreen coordinates and coordinates in this view   （绝对坐标和相对坐标的偏移量）
	int mCoordOffsetX;
	
//	private Rect mTempRect = new Rect();
	private WindowManager mWindowManager;
	private WindowManager.LayoutParams mWindowParams;
	private int dragndropBackgroundColor = 0x00000000;
	private Bitmap mDragBitmap;				//
	
	private ListMoveHandler mListMoveHandler;
	
	private MotionEvent evtCache;
	
	private boolean shouldDelete;
	
	private ScreenSwitcherActivity activity;
	
	private final static int DELETE_OFFSET=50;
			
	public HomeScreenLayout(Context context)
	{
		super(context);
		
	}
	
	public HomeScreenLayout(Context context, AttributeSet attrs) {
		super(context,attrs);
		mListMoveHandler = new ListMoveHandler();
		activity=(ScreenSwitcherActivity)context;
//		handler=activity.new HomeScreenHandler();
	}
	
	public void setView(View view,ImageView delete,ImageView add){
		mDragItem=view;
		this.delete=delete;
		this.add=add;
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev)
	{		
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			
			evtCache=null;
			evtCache=ev;
			
			mouseX = (int) ev.getX();
			
			mouseY = (int) ev.getY();
			
			if(mDragItem==null)
				return false;			
			
			int location[]=new int[2];
			mDragItem.getLocationInWindow(location);
			
			mDragPointY = ((int) ev.getRawY()) - location[1];		//getRawX相对于屏幕位置坐标
			mDragPointX = ((int) ev.getRawX()) - location[0];
			
			mCoordOffsetY = (int)ev.getRawY()-mouseY;
			mCoordOffsetX = (int) ev.getRawX()-mouseX;
			break;
		case MotionEvent.ACTION_MOVE:
			
			if(mDragItem==null)
				return false;
			mouseX = (int) ev.getX();
			mouseY = (int) ev.getY();	
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
//			mDragView.getDrawingRect(r);
			if(mDragItem==null)
				return false;
			
			if(shouldDelete){
				shouldDelete=false;
				if(ScreenSwitcherActivity.numCanNotDelete==5){
					activity.deleteScreen();
				}else if(ScreenSwitcherActivity.numCanNotDelete==3){
					activity.deleteScreenCase3();
				}
			}
			
			stopDragging();
			
			if (mListMoveHandler.mIsStart) {
				mListMoveHandler.stop();
			}
		}		
		
		return super.onInterceptTouchEvent(ev);
	}
	
	public void startDragging() {
		stopDragging();
		
		mDragItem.setDrawingCacheEnabled(true);
		Bitmap bitmap = Bitmap.createBitmap(mDragItem.getDrawingCache());
		
		mWindowParams = new WindowManager.LayoutParams();
		mWindowParams.gravity = Gravity.TOP|Gravity.LEFT;
		
//		mWindowParams.x = mouseX - mCoordOffsetX + mDragPointX;
//		mWindowParams.y = mouseY - mCoordOffsetY + mDragPointY;
		
		int location[]=new int[2];
		mDragItem.getLocationInWindow(location);
		
		mDragPointY = ((int) evtCache.getRawY()) - location[1];		//getRawX相对于屏幕位置坐标
		mDragPointX = ((int) evtCache.getRawX()) - location[0];
		
		mCoordOffsetY = (int)evtCache.getRawY()-mouseY;
		mCoordOffsetX = (int) evtCache.getRawX()-mouseX;
		
		mWindowParams.y =mouseY-mDragPointY+mCoordOffsetY;
		mWindowParams.x =mouseX-mDragPointX+mCoordOffsetX;
				
		mWindowParams.height = (int)(mDragItem.getHeight()*1.1);
		mWindowParams.width = (int)(mDragItem.getWidth()*1.1);
		mWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
		mWindowParams.format = PixelFormat.TRANSLUCENT;
		mWindowParams.windowAnimations = 0;
		
//		mWindowParams.alpha=0;
		
		mDragView = new ImageView(getContext());
		mDragView.setScaleType(ScaleType.FIT_XY);
		mDragView.setBackgroundColor(dragndropBackgroundColor);
		mDragView.setImageBitmap(bitmap);
		mDragBitmap = bitmap;
		
		mWindowManager = (WindowManager) getContext()
				.getSystemService("window");
		
//		Log.e("mWindowParams.y1:","mWindowParams.y1:"+mWindowParams.y);
		
		mWindowManager.addView(mDragView, mWindowParams);
		
		mDragItem.setVisibility(View.INVISIBLE);
		delete.setVisibility(View.VISIBLE);
		add.setVisibility(View.GONE);
		
		mListMoveHandler.start();
		
	}
	
	private void stopDragging() {
		
//		delete.setBackgroundColor(Color.WHITE);
		delete.setBackgroundResource(R.drawable.ic_screenswitcher_trash);
		
		mDragItem.setVisibility(View.VISIBLE);
		delete.setVisibility(View.GONE);
		add.setVisibility(View.VISIBLE);
		
		if (mDragView != null) {
			WindowManager wm = (WindowManager) getContext().getSystemService(
					"window");
			wm.removeView(mDragView);
			mDragView.setImageDrawable(null);
			mDragView = null;
		}
		if (mDragBitmap != null) {
			mDragBitmap.recycle();
			mDragBitmap = null;
		}
	}
	
	private void dragView() {
		if(mDragView==null)
			return;
		
		mWindowParams.alpha = 1.0f;
		
		mWindowParams.y =mouseY-mDragPointY+mCoordOffsetY;
		mWindowParams.x =mouseX-mDragPointX+mCoordOffsetX;	
		
		if((mouseY+mDragView.getHeight()-mDragPointY>delete.getTop()+DELETE_OFFSET)
			&& ((mouseX+mDragView.getWidth()-mDragPointX>delete.getLeft()) && (mouseX-mDragPointX<delete.getRight()))
		){
//			delete.setBackgroundColor(Color.RED);
			mDragView.setImageBitmap(MyImg.createRedBitmap(mDragBitmap));
			delete.setBackgroundResource(R.drawable.ic_screenswitcher_trash_pressed);
			shouldDelete=true;
		}else{
//			delete.setBackgroundColor(Color.WHITE);
			mDragView.setImageBitmap(mDragBitmap);
			delete.setBackgroundResource(R.drawable.ic_screenswitcher_trash);
			shouldDelete=false;
		}
		
		
//		Log.e("mWindowParams.y2:","mWindowParams.y2:"+mWindowParams.y);
		mWindowManager.updateViewLayout(mDragView, mWindowParams);
		
		
	}
	
	private class ListMoveHandler extends Handler {
		
		private final int MESSAGEWHAT = 111;
		private final int MESSAGEDELAY = 100;

		private boolean mIsStart = false;

		public void start() {
			this.mIsStart = true;
			this.sendEmptyMessageDelayed(MESSAGEWHAT, MESSAGEDELAY);
		}

		public void stop() {
			this.mIsStart = false;
			this.removeMessages(MESSAGEWHAT);
		}
		
		private void myDragView()
		{
			HomeScreenLayout.this.dragView();
		}
		
		@Override
		public void handleMessage(Message msg) {
			
			super.handleMessage(msg);

			myDragView();

			if (mIsStart) {
				this.sendEmptyMessageDelayed(MESSAGEWHAT, MESSAGEDELAY);
			}
		}
	}
}
