package com.aess.aemm.view;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.ImageView.ScaleType;

import com.aess.aemm.R;
import com.aess.aemm.view.GridAdapter.GridHolder;

public class ScrollLayout extends ViewGroup {
	private final String namespace = "http://www.test.scrolllayout.com";
//	private static final String TAG = "ScrollLayout";
	public int iconWidth;
	public boolean isApp;
	/**
	 * page index changed handler
	 */
	private Handler pageHandler;
	/**
	 * page index handler's what flag
	 */
	private int pageWhat;
	private int indexNumWhat;
	
	/**
	 * is allow the move tag location
	 */
	private boolean locationEnable = true;
	
	private Scroller mScroller;
	private VelocityTracker mVelocityTracker;

	private List<View> childList = new ArrayList<View>();
	private int screenCnt;
	private int mCurScreen;
	private int mDefaultScreen = 0;
	private int row = 2;
	private int coloum = 4;
	int width,height;
	private int cWidth, cHeight;
	int wMargin,hMargin;
	int marginLeft=0,marginRight=0,marginTop,marginBottom;
	float scalMarginw=1.4f;
	float scalMarginh=1.1f;
	int scalNum=2,scalNum2=2;
	boolean isAdded;
	
	public int ir,ic,ir2,ic2,ur,uc,ur2,uc2;
	public boolean isSetRC;
	
	private static final int TOUCH_STATE_REST = 0;
	private static final int TOUCH_STATE_SCROLLING = 1;
	private static final int SNAP_VELOCITY = 600;

	private int mTouchState = TOUCH_STATE_REST;
	private int mTouchSlop;
	private float mLastMotionX;
	@SuppressWarnings("unused")
	private float mLastMotionY;

	public ScrollLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ScrollLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
		mScroller = new Scroller(context);

		mCurScreen = mDefaultScreen;
		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
		marginLeft=attrs.getAttributeResourceValue(namespace,"marginLeft", -1);
		marginRight=attrs.getAttributeResourceValue(namespace,"marginRight",-1);
		marginTop=attrs.getAttributeResourceValue(namespace,"marginTop", -1);
		marginBottom=attrs.getAttributeResourceValue(namespace,"marginBottom", -1);
		marginLeft=marginLeft<0?0:(int) getResources().getDimension(marginLeft);
		marginRight=marginRight<0?0:(int) getResources().getDimension(marginRight);
		marginTop=marginTop<0?0:(int) getResources().getDimension(marginTop);
		marginBottom=marginBottom<0?0:(int) getResources().getDimension(marginBottom);
		
		try{
		scalMarginw=Float.parseFloat(getContext().getString(R.string.scalMarginw));
		scalMarginh=Float.parseFloat(getContext().getString(R.string.scalMarginh));
		}catch(Exception e){}
//		row = attrs.getAttributeResourceValue(namespace,"row", 1);
//		coloum = attrs.getAttributeResourceValue(namespace,"coloum", 1);
//		row=(int) getResources().getDimension(row);
//		coloum=(int) getResources().getDimension(coloum);
//		log.d("row="+row+" coloum="+coloum);
	}

	@Override
	public void addView(View child, int index) {
		isAdded=true;
//		Log.d(TAG,"addView");
		if(locationEnable){
			PagePosition position = ((GridHolder) child.getTag()).app.getLocation();
		
			if(position.getIndex()<0){
				if (index <0) {
					position.setIndex(getChildCount());
				}else{
					position.setIndex(index);
				}
			}
//			Log.d(TAG,"new index:"+position.getIndex());
			PagePosition tmp;
			for(int i=0;i<childList.size();i++){
				tmp= ((GridHolder)( childList.get(i).getTag())).app.getLocation();
				if(position==null){
					tmp.setIndex(tmp.getIndex()+1);
//					Log.d(TAG,i+"move index:"+tmp.getIndex());
				}else
				if(position.getIndex()<=tmp.getIndex()){
					childList.add(i,child);
					position=null;
				}
//				Log.d(TAG,i+" index:"+tmp.getIndex());
				
			}
			if(position!=null){
				childList.add(child);
			}
		
//		PagePosition pagePosition=new PagePosition();
//		pagePosition.index=position;
//		pagePosition.set(position,row,coloum);
//		app.setLocation(pagePosition);
//		moveToStep(position, 1);
//		childList.add(position, child);
		}
		super.addView(child, index);
	}

	@Override
	public void addView(View child) {
//		Log.d(TAG,"addView");
		// AppItem app=((GridHolder) child.getTag()).app;
		// int index=childList.size();
		// int p=index/coloum/row;
		// int r=p<=0?index/coloum:(index%p)/coloum;
		// int c=index%coloum;
		// app.setLocation(p,r,c);
		// childList.add(child);
		super.addView(child);
	}
	public void removeViewAt(int index){
		super.removeViewAt(index);
		childList.remove(index);
		moveToStep(index,-1);
//		mMoveHandler.moveTo(index,childList.size()-1,-1);
//		if(!mMoveHandler.mIsStart){
//			mMoveHandler.startNow();
//		}
	}
	public void removeAllViews(){
		super.removeAllViews();
		childList.clear();
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if(!changed&&r-l!=width){
			changed=true;
		}

		width=r-l;
		height=b-t;
		marginTop = 0;

		final int childCount = getChildCount();
		View child=getChildAt(0);
		if(child!=null){
			if(isSetRC){
				cWidth=child.getMeasuredWidth();
				cHeight=child.getMeasuredHeight();
				if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
					if(isApp){
						coloum=ic2;
						row=ir2;
					}else{
						coloum=uc2;
						row=ur2;
					}
				}else{
					if(isApp){
						coloum=ic;
						row=ir;
					}else{
						coloum=uc;
						row=ur;
					}
				}
				
				if(coloum<=1){
					coloum=1;
					int mLeft=(width-cWidth)>>1;
					wMargin=0;
					if(!isApp){
						int appColoum;
						int appMarginLeft;
						if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
							appColoum=ic2;
						}else{
							appColoum=ic;
						}
						if(appColoum<=1){
							appColoum=1;
							appMarginLeft=(width-iconWidth)>>1;
							if(mLeft>appMarginLeft){
								marginLeft=appMarginLeft;
							}
						}
					}
				}else{
					if(isApp){
						wMargin=((width-marginLeft-marginRight)-coloum*cWidth)/(coloum-1);
					}else{
						if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
							wMargin=(((width-marginLeft-marginRight)-ic2*iconWidth)/(ic2-1)+iconWidth)*scalNum-cWidth;
						}else{
							wMargin=(((width-marginLeft-marginRight)-ic*iconWidth)/(ic-1)+iconWidth)*scalNum-cWidth;
						}
					}
				}
				if(row<=1){
					row=1;
					hMargin=0;
					marginTop=(height-cHeight)>>1;
				}else{
					hMargin=(height-row*cHeight)/(row-1);
				}
			}else{
				cWidth=iconWidth;
				coloum=(int) ((width-marginLeft-marginRight)/(cWidth*scalMarginw));
				if(coloum>scalNum){
					coloum=coloum-coloum%scalNum;
				}
				if(coloum<=1){
					coloum=1;
					marginLeft=(width-cWidth)>>1;
					wMargin=0;
				}else{
					wMargin=((width-marginLeft-marginRight)-coloum*cWidth)/(coloum-1);
				}
				if(!isApp){
					int appWidth=wMargin;
					cWidth=child.getMeasuredWidth();
					
					coloum=coloum/scalNum;
					if(coloum<=1){
						coloum=1;
						wMargin=0;
						if(marginLeft>(width-cWidth)>>1){
						marginLeft=(width-cWidth)>>1;
						}
					}else{
						wMargin=(appWidth+iconWidth)*scalNum-cWidth;
					}
				}
				cHeight=child.getMeasuredHeight();
				row=(int) (height/(cHeight*scalMarginh));
				if(row<=1){
					row=1;
					hMargin=0;
					marginTop=(height-cHeight)>>1;
				}else{
					hMargin=(height-row*cHeight)/(row-1);
				}
			}
//			Log.d(TAG,"onLayout size["+width+","+height+"] child size["+cWidth+","+cHeight+"] margin["+wMargin+","+hMargin+"]"+"num["+row+","+coloum+"]");
			
		}
		if (locationEnable) {
			for (int i = 0; i < childCount; i++) {
				child= childList.get(i);
				if (child.getVisibility() != View.GONE) {
					PagePosition mPagePosition=((GridHolder) child.getTag()).app.getLocation();
					if(isAdded||changed){
						mPagePosition.set(mPagePosition.getIndex(),row,coloum);
						((GridHolder) child.getTag()).app.setLocation(mPagePosition);
//						moveToStep(i+1, 1);
					}
					if (mPagePosition.page >= screenCnt) {
//						log.d("getPage:" + mPagePosition.page);
						screenCnt = mPagePosition.page + 1;
					}
					int sLeft=marginLeft+mPagePosition.page * width + mPagePosition.coloum* (cWidth+wMargin);
					int sTop=marginTop+mPagePosition.row * (cHeight+hMargin);
//					Log.d(TAG,i+"--"+mPagePosition.index+" layout:"+sLeft+","+
//							sTop+"," +
//							(sLeft+cWidth)+","+
//							(sTop + cHeight));
					child.layout(sLeft,
							sTop, 
							sLeft+cWidth,
							sTop + cHeight);

					if (MainView.VIEWDEBUG) {
						child.setBackgroundColor(0x55ffffff << ((i % 5) * 3));
					}
				}
			}
			isAdded=false;
			if (pageHandler != null) {
				Message message = new Message();
				message.what =indexNumWhat;// MainView.APP_INDEX_REFRESH;
				message.arg1 = getScreen();
				pageHandler.sendMessage(message);sendPage(mCurScreen);
//				Message message_apk = new Message();
//				message_apk.what = MainView.APK_INDEX_REFRESH;
//				message_apk.arg1 = getScreen();
//				pageHandler.sendMessage(message_apk);
			}
		} else {
			int sPage=0,ly=0,lx=0;
			@SuppressWarnings("unused")
			int cScreen = 0,cRow = 0,cColoum = 0;
			for (int i = 0; i < childCount; i++) {
				child = getChildAt(i);
				if (child.getVisibility() != View.GONE) {
					child.layout(sPage+lx, ly,
							sPage+lx+ cWidth, ly+ cHeight);
					lx+=(cWidth+wMargin);
					cColoum++;
					if(cColoum>=coloum){
						lx=0;
						cRow++;
//					if(lx+(wMargin)>=width){
						ly+=(cHeight+hMargin);
						cColoum=0;
						if(cRow>=row){
							cRow=0;
							cScreen++;
//						if(ly+hMargin>=height){
							ly=0;
							sPage+=width;
						}
					}
					
				}
			}
		}
		scrollBy(scroll, 0);
	}

	/***
	 * protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	 * final int width = MeasureSpec.getSize(widthMeasureSpec); final int
	 * widthMode = MeasureSpec.getMode(widthMeasureSpec); int mCellWidth = width
	 * / coloum; int mCellHeight = widthMode / row; int cellWidthSpec =
	 * MeasureSpec.makeMeasureSpec(mCellWidth, MeasureSpec.AT_MOST); int
	 * cellHeightSpec = MeasureSpec.makeMeasureSpec(mCellHeight,
	 * MeasureSpec.AT_MOST); int count = getChildCount(); for (int index = 0;
	 * index < count; index++) { final View child = getChildAt(index);
	 * child.measure(cellWidthSpec, cellHeightSpec); } // Use the size our
	 * parents gave us setMeasuredDimension(resolveSize(mCellWidth * count,
	 * widthMeasureSpec), resolveSize(mCellHeight * count, heightMeasureSpec));
	 * }
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		final int width = MeasureSpec.getSize(widthMeasureSpec);
		final int height = MeasureSpec.getSize(heightMeasureSpec);
//		Log.d(TAG,"onMeasure("+width+","+height+")");

		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		if (widthMode != MeasureSpec.EXACTLY) {
			throw new IllegalStateException(
					"ScrollLayout only canmCurScreen run at EXACTLY mode!");
		}
		final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		if (heightMode != MeasureSpec.EXACTLY) {
			throw new IllegalStateException(
					"ScrollLayout only can run at EXACTLY mode!");
		}
		// The children are given the same width and height as the scrollLayout
		final int count = getChildCount();
		int cellWidthSpec = MeasureSpec.makeMeasureSpec(width,MeasureSpec.AT_MOST);
		int cellHeightSpec = MeasureSpec.makeMeasureSpec(height,MeasureSpec.AT_MOST);
		for (int i = 0; i < count; i++) {
			getChildAt(i).measure(cellWidthSpec, cellHeightSpec);
		}
		scrollTo(mCurScreen * width, 0);
	}

	/**
	 * According to the position of current layout scroll to the destination
	 * page.
	 */
	public void snapToDestination() {
		final int screenWidth = getWidth();
		final int destScreen = (getScrollX() + (screenWidth >> 1))
				/ screenWidth;
		snapToScreen(destScreen);
	}

	public void snapToScreen(int whichScreen) {
		// get the valid layout page
//		Log.d(TAG,"toScreen:"+whichScreen);
		whichScreen = Math.max(0, Math.min(whichScreen, getScreen() - 1));
		if (getScrollX() != (whichScreen * getWidth())) {

			final int delta = whichScreen * getWidth() - getScrollX();
			mScroller.startScroll(getScrollX(), 0, delta, 0,
					Math.abs(delta) << 1);
			mCurScreen = whichScreen;
			invalidate(); // Redraw the layout
		}
		// if(pageHandler!=null){
		// Message message = new Message();
		// message.what = pageWhat;
		// message.arg1=getCurScreen();
		// pageHandler.sendMessage(message);
		// }
		sendPage(getCurScreen());
	}

	public void setToScreen(int whichScreen) {
		whichScreen = Math.max(0, Math.min(whichScreen, getScreen() - 1));
		mCurScreen = whichScreen;
		scrollTo(whichScreen * getWidth(), 0);
		sendPage(whichScreen);
	}

	private void sendPage(int s) {
		if (pageHandler != null) {
			Message message = new Message();
			message.what = pageWhat;
			message.arg1 = s;
			pageHandler.sendMessage(message);
		}
	}

	public int getScreen() {
//		if (locationEnable) {
//			// log.d("screentCnt:"+screenCnt);
//			return screenCnt;
//		} else {
			return (getChildCount() + (row * coloum - 1)) / (row * coloum);
//		}
	}

	public int getCurScreen() {
		return mCurScreen;
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		}
	}
int scroll=0;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
//		logEvent.d("onTouchEvent=" + event.getAction() + " getRawX="
//				+ event.getRawX() + " getRawY=" + event.getRawY());
		// logEvent.d("onTouchEvent="+event.getAction()+" X="+event.getX()+" Y="+event.getY());

		if (mDragItem != null) {
			return false;
		}

		final int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			// Log.d(TAG, "event down!");
			if (!mScroller.isFinished()) {
				mScroller.abortAnimation();
			}
			mLastMotionX = event.getX();
			if (mVelocityTracker == null) {
				mVelocityTracker = VelocityTracker.obtain();
			}else{
				mVelocityTracker.clear();
			}
			mVelocityTracker.addMovement(event);
			break;

		case MotionEvent.ACTION_MOVE:
			if (mVelocityTracker == null) {
				mVelocityTracker = VelocityTracker.obtain();
			}
			mVelocityTracker.addMovement(event);
			int deltaX = (int) (mLastMotionX - event.getX());
			mLastMotionX = event.getX();
//			Log.d(TAG,"scrollby:"+deltaX+"="+mLastMotionX);
			scrollBy(deltaX, 0);
			scroll=getScrollX();
			break;

		case MotionEvent.ACTION_UP:
			// Log.d(TAG, "event : up");
			// if (mTouchState == TOUCH_STATE_SCROLLING) {
			mVelocityTracker.computeCurrentVelocity(1000);
			int velocityX = (int) mVelocityTracker.getXVelocity();

			// Log.d(TAG, "velocityX:"+velocityX);

			if (velocityX > SNAP_VELOCITY && mCurScreen > 0) {
				// Fling enough to move left
//				Log.d(TAG, "snap left:"+(mCurScreen - 1));
				snapToScreen(mCurScreen - 1);
			} else if (velocityX < -SNAP_VELOCITY&& mCurScreen < getChildCount() - 1) {
				// Fling enough to move right
				snapToScreen(mCurScreen + 1);
			} else {
//				Log.d(TAG, "snapToDestination");
				snapToDestination();
			}

			if (mVelocityTracker != null) {
				mVelocityTracker.recycle();
				mVelocityTracker.clear();
			}
			scroll=0;
			// }
			mTouchState = TOUCH_STATE_REST;
			break;
		case MotionEvent.ACTION_CANCEL:
			mTouchState = TOUCH_STATE_REST;
			break;
		}

		return super.onTouchEvent(event);
	}

	private View mDragItem;
	private int mDragItemOffsetX,mDragItemOffsetY;
	private int mDragPointX, mDragPointY;
	private int mCoordOffsetX, mCoordOffsetY;
	private int mouseX, mouseY;
	private int mAreaX,mAreaY;
	private TouchMoveHandler mTouchMoveHandler = new TouchMoveHandler();
	private PageDelayHandler mMoveDelayHandler = new PageDelayHandler();
	private MoveHandler mMoveHandler = new MoveHandler();
	private MotionEvent evtCache;
	private ImageView mDragView;
	private int dragndropBackgroundColor = 0x00ff0000;
	private Bitmap mDragBitmap;
	private WindowManager mWindowManager; //
	private WindowManager.LayoutParams mWindowParams;
	private int oldx,oldy;
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// logEvent.d("onInterceptTouchEvent="+ev.getAction()+" x="+ev.getX()+" y="+ev.getY());
		// logEvent.d("onInterceptTouchEvent="+ev.getAction()+" getRawX="+ev.getRawX()+" getRawY="+ev.getRawY());
		mouseX = (int) ev.getX();
		mouseY = (int) ev.getY();
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			oldx=(int) ev.getX();
			oldy=(int) ev.getY();
			evtCache = ev;
			if (mDragItem != null) {
				// log.d(ev.getAction()+" down mDragItem!=null");

				int location[] = new int[2];
				mDragItem.getLocationInWindow(location);

				mDragPointY = ((int) ev.getRawY()) - location[1]; // getRawX相对于屏幕位置坐标
				mDragPointX = ((int) ev.getRawX()) - location[0];

				mCoordOffsetY = (int) ev.getRawY() - mouseY;
				mCoordOffsetX = (int) ev.getRawX() - mouseX;
				return false;
			}
			mLastMotionX = mouseX;
			mLastMotionY = mouseY;
			mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST: TOUCH_STATE_SCROLLING;
			mAreaX=(int) (ev.getRawX()-ev.getX());
			mAreaY=(int) (ev.getRawY()-ev.getY());
			//
			break;

		case MotionEvent.ACTION_MOVE:
			if(mMoveListener!=null){
				if(Math.abs(ev.getX()-oldx)>10||Math.abs(ev.getY()-oldy)>10)
				mMoveListener.move();
			}
			if (mDragItem != null) {
				// log.d(ev.getAction()+" move mDragItem!=null");
				if (!mTouchMoveHandler.mIsStart) {
					mDragItemOffsetX=(int) (evtCache.getRawX()-mDragItem.getLeft());
					mDragItemOffsetY=(int) (evtCache.getRawY()-mDragItem.getTop());
					startDragging();
				}
				return false;
			}

			if (mTouchState != TOUCH_STATE_REST) {
				return true;
			}
			final int xDiff = (int) Math.abs(mLastMotionX - mouseX);
			if (xDiff > mTouchSlop) {
				mTouchState = TOUCH_STATE_SCROLLING;
			}
			break;

		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			if (mDragItem != null) {
				// log.d(ev.getAction()+" up mDragItem!=null");
				stopDragging();
				if(mMoveListener!=null){
					mMoveListener.cancel();
				}
				if (mTouchMoveHandler.mIsStart){
					mTouchMoveHandler.stop();
					mMoveDelayHandler.stop();
				}
				return false;
			}

			mTouchState = TOUCH_STATE_REST;
			break;
		}
		return mTouchState != TOUCH_STATE_REST;
	}

	private void moveToStep(int position,int step){
		if (childList.size() > position) {
			View old = childList.get(position);
			if (old != null) {
				int cnt = childList.size();
				for (int i = position; i < cnt; i++){
					old = childList.get(i);
					if (old == null) {
						break;
					}
					((GridHolder) old.getTag()).app.getLocation().moveStep(row,coloum,step);
				}
			}
		}
	}
	
	public void setView(View view) {
		mDragItem = view;
		// log.d("setView");
	}

	public void startDragging() {
		stopDragging();

		mDragItem.setDrawingCacheEnabled(true);
		Bitmap bitmap = Bitmap.createBitmap(mDragItem.getDrawingCache());

		mWindowParams = new WindowManager.LayoutParams();
		mWindowParams.gravity = Gravity.TOP | Gravity.LEFT;

		// mWindowParams.x = mouseX - mCoordOffsetX + mDragPointX;
		// mWindowParams.y = mouseY - mCoordOffsetY + mDragPointY;

		int location[] = new int[2];
		mDragItem.getLocationInWindow(location);

		mDragPointY = ((int) evtCache.getRawY()) - location[1];// getRawX相对于屏幕位置坐标
		mDragPointX = ((int) evtCache.getRawX()) - location[0];

		mCoordOffsetY = (int) evtCache.getRawY() - mouseY;
		mCoordOffsetX = (int) evtCache.getRawX() - mouseX;

		mWindowParams.y = mouseY - mDragPointY + mCoordOffsetY;
		mWindowParams.x = mouseX - mDragPointX + mCoordOffsetX;

		mWindowParams.height = (int) (mDragItem.getHeight()*1.3);
		mWindowParams.width = (int) (mDragItem.getWidth()* 1.3);
		mWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
				| WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
		mWindowParams.format = PixelFormat.TRANSLUCENT;
		mWindowParams.windowAnimations = 0;

		mWindowParams.alpha = 0;

		mDragView = new ImageView(getContext());
		mDragView.setScaleType(ScaleType.FIT_XY);
		mDragView.setBackgroundColor(dragndropBackgroundColor);
		mDragView.setImageBitmap(bitmap);
		mDragBitmap = bitmap;

		mWindowManager = (WindowManager) getContext()
				.getSystemService("window");

		// Log.e("mWindowParams.y1:","mWindowParams.y1:"+mWindowParams.y);

		mWindowManager.addView(mDragView, mWindowParams);

		mDragItem.setVisibility(View.INVISIBLE);

		mTouchMoveHandler.start();
		mMoveDelayHandler.start();
	}

	private void stopDragging() {
		// delete.setBackgroundColor(Color.WHITE);

		// mDragItem.setVisibility(View.VISIBLE);

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
		if (mDragItem != null && mDragItem.getVisibility() != View.VISIBLE) {
			mDragItem.setVisibility(View.VISIBLE);
			AppItem app=((GridHolder) mDragItem.getTag()).app;
//			log.d("getx:"+evtCache.getX()+" gety:"+evtCache.getY()+" getRawX:"+evtCache.getRawX()+" getRawY:"+evtCache.getRawY()+" AreaX:"+mAreaX+" AreaY:"+mAreaY+" mDragItemOffsetX="+mDragItemOffsetX+" mDragItemOffsetY="+mDragItemOffsetY);
			mDragItem.layout((int)evtCache.getRawX()-mDragItemOffsetX, (int)evtCache.getRawY()-mDragItemOffsetY, (int)evtCache.getRawX()-mDragItemOffsetX+mDragItem.getWidth(), (int)evtCache.getRawY()-mDragItemOffsetY+mDragItem.getHeight());
			app.setMove(marginLeft,marginTop, width,cWidth+wMargin, cHeight+hMargin);
			mMoveHandler.addItem(app);
			if(!mMoveHandler.mIsStart){
				mMoveHandler.startNow();
			}
			mDragItem = null;
		}
	}

	private void dragView() {
		if (mDragView == null)
			return;

		mWindowParams.alpha = 0.5f;

		mWindowParams.y = mouseY - mDragPointY + mCoordOffsetY;
		mWindowParams.x = mouseX - mDragPointX + mCoordOffsetX;

		// mDragItem.layout(mWindowParams.x,mWindowParams.y,
		// mWindowParams.x+mDragItem.getWidth(),mWindowParams.y+mDragItem.getHeight());
		// mDragItem.invalidate();//postInvalidate();
		// mDragItem.bringToFront();
		// if((mouseY+mDragView.getHeight()-mDragPointY>delete.getTop()+DELETE_OFFSET)
		// && ((mouseX+mDragView.getWidth()-mDragPointX>delete.getLeft()) &&
		// (mouseX-mDragPointX<delete.getRight()))
		// ){
		// delete.setBackgroundColor(Color.RED);
		// mDragView.setImageBitmap(MyImg.createRedBitmap(mDragBitmap));
		// }else{
		// delete.setBackgroundColor(Color.WHITE);
		// mDragView.setImageBitmap(mDragBitmap);
		// }

		// log.d("wX="+mWindowParams.x+" wY="+mWindowParams.y);
		mWindowManager.updateViewLayout(mDragView, mWindowParams);
	}

//	public void setRow(int num) {
//		if (num <= 0) {
//			return;
//		}
//		row = num;
//	}
//
//	public void setColoum(int num) {
//		if (num <= 0) {
//			return;
//		}
//		coloum = num;
//	}

	public void setPageHandler(int indexNumWhat,int what, Handler handler) {
		this.pageHandler = handler;
		this.pageWhat = what;
		this.indexNumWhat=indexNumWhat;
	}

	private PagePosition getPosition(int x, int y) {
		PagePosition pp = new PagePosition();
		pp.row = y / (cHeight+hMargin);
		if(pp.row<0){
			pp.row=0;
		}else if(pp.row>=ScrollLayout.this.row){
			pp.row=ScrollLayout.this.row-1;
		}
		
		pp.coloum = x / (cWidth+wMargin);
		if(pp.coloum<0){
			pp.coloum=0;
		}else if(pp.coloum>=ScrollLayout.this.coloum){
			pp.coloum=ScrollLayout.this.coloum-1;
		}
		
		pp.page = getCurScreen();
		
		return pp;
	}

	private class TouchMoveHandler extends Handler {

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

		private void myDragView() {
			ScrollLayout.this.dragView();
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

	private class MoveHandler extends Handler {
		List<AppItem> echeView = new ArrayList<AppItem>();
		private final int MESSAGEWHAT = 111;
		private final int MESSAGEDELAY = 80;

		private boolean mIsStart = false;

		public void addItem(AppItem item) {
			echeView.add(item);
		}

		public void moveTo(int srcPosition,int desPosition,int step) {
//			Log.d("ScrollLayout","move to :"+srcPosition+" -> "+desPosition);
			
			if(desPosition<childList.size()&&srcPosition>=0){
				AppItem app;
				View old;
				for(int i=desPosition;i>=srcPosition;i--){
					old = childList.get(i);
					app = ((GridHolder) old.getTag()).app;
//					log.d("i: "+i);
					app.getLocation().moveStep(row,coloum,step);
					app.setMove(marginLeft,marginTop, width,cWidth+wMargin, cHeight+hMargin);
//					app.setMove(p.page * coloum * cWidth + p.coloum * cWidth, p.row * cHeight);
//					mMoveHandler.addItem(app);
					echeView.add(app);
				}
				if(step>0){
					AppItem adge=((GridHolder) childList.get(desPosition).getTag()).app;
					for(int i=desPosition+1;i<=desPosition+step;i++){
						old=childList.get(i);
						app=((GridHolder) old.getTag()).app;
						if(adge.getLocation().getIndex()>app.getLocation().getIndex()){
							childList.remove(i);
							childList.add(srcPosition,old);
						}
					}
				}else if(step<0){
					AppItem adge=((GridHolder) childList.get(srcPosition).getTag()).app;
					for(int i=srcPosition-1;i>=srcPosition+step;i--){
						old=childList.get(i);
						app=((GridHolder) old.getTag()).app;
						if(adge.getLocation().getIndex()<app.getLocation().getIndex()){
							childList.remove(i);
							childList.add(desPosition,old);
						}
					}
				}
			}
		}

		public void startNow() {
			if(!this.mIsStart){
				this.mIsStart = true;
				this.sendEmptyMessageDelayed(MESSAGEWHAT, MESSAGEDELAY);
			}
		}

		public void stopNow() {
			if(this.mIsStart){
				this.mIsStart = false;
				this.removeMessages(MESSAGEWHAT);
			}
		}

		private void myDragView() {
			for (int i = echeView.size() - 1; i >= 0; i--) {
				if (!echeView.get(i).move()) {
					 echeView.remove(i);
				}

				// view.layout(view.getLeft()+sx,view.getTop()+sy,
				// view.getLeft()+view.getWidth(),view.getTop()+view.getHeight());
			}
			// mDragItem.invalidate();//postInvalidate();
			// view.bringToFront();
		}

		@Override
		public void handleMessage(Message msg) {

			super.handleMessage(msg);

			myDragView();

			if (echeView.size()<=0) {
				stopNow();
			}else{
				this.sendEmptyMessageDelayed(MESSAGEWHAT, MESSAGEDELAY);
			}
		}
	}

	private class PageDelayHandler extends Handler {
		private final int MESSAGEWHAT = 111;
		private final int MESSAGEDELAY = 100;

		private boolean mIsStart = false;
		private int screenEdge = 30;
		private final int pageMoveRate = 10, itemMoveRate = 10;
		private final int DELAY = 3;
		private final int PAGEDELAY=10;
		private int old;
		private int pageDelay;
		private int itemDelay;

		public void start() {
			this.mIsStart = true;
			this.sendEmptyMessageDelayed(MESSAGEWHAT, MESSAGEDELAY);
		}

		public void stop() {
			this.mIsStart = false;
			this.removeMessages(MESSAGEWHAT);
		}

		private void myDragView() {
//			 Log.v("VVV","MainView.metric.widthPixels="+MainView.metric.widthPixels+
//					 "   evtCache.getX()="+evtCache.getX()+"   evtCache.getRawX()="+evtCache.getRawX()+" old="+old+ " screenEdge="+screenEdge);
			if (Math.abs(old - evtCache.getRawX()) < pageMoveRate
					&& (evtCache.getRawX() < screenEdge || evtCache.getRawX() > MainView.metric.widthPixels
							- screenEdge)) {
				pageDelay++;
			} else {
				pageDelay = 0;
			}
			if (pageDelay == 0&& Math.abs(old - evtCache.getRawX()) < itemMoveRate) {
				itemDelay++;
			} else {
				itemDelay = 0;
			}
			old = (int) evtCache.getRawX();
			if (pageDelay >= PAGEDELAY) {
				if (evtCache.getRawX() < screenEdge) {
					snapToScreen(getCurScreen()-1);
//					MainView.mHall.unInstallMovePrePage();
				} else {
					snapToScreen(getCurScreen()+1);
//					MainView.mHall.unInstallMoveNextPage();
				}
				pageDelay = 0;
			}
			if (itemDelay > DELAY) {
				itemDelay = -1;
				PagePosition srcPosition=((GridHolder) mDragItem.getTag()).app.getLocation();
				PagePosition desPosition = getPosition((int) evtCache.getRawX()-mAreaX,(int) evtCache.getRawY()-mAreaY);
				int srcIndex=srcPosition.getIndex(row, coloum);
				int desIndex=desPosition.getIndex(row, coloum);
//				if(desIndex>=childList.size()){
//					desIndex=childList.size()-1;
//					desPosition.set(desIndex,row,coloum);
//				}
//				Log.d("ScrollLayout","src:"+srcIndex+" des:"+desIndex);
				if(desIndex<childList.size()&&srcIndex!=desIndex){
					View v=childList.get(srcIndex);
					
					if(srcIndex>desIndex){
						mMoveHandler.moveTo(desIndex,srcIndex-1,1);
					}else{
						mMoveHandler.moveTo(srcIndex+1,desIndex,-1);
					}
					childList.remove(srcIndex);
					((GridHolder) v.getTag()).app.getLocation().set(desIndex, row, coloum);
					childList.add(desIndex,v);
	//				log.d("itemDelay:"
	//								+ evtCache.getX()
	//								+ " =x|y= "
	//								+ (int) evtCache.getRawY()
	//								+ " index: "
	//								+ (pp.page * row * coloum + pp.row * coloum + pp.coloum)
	//								+ " positon=" + pp.page + pp.row + pp.coloum
	//								+ " top:" + ScrollLayout.this.getTop());
					if (!mMoveHandler.mIsStart) {
						mMoveHandler.startNow();
					}
				}
//				for(int i=0;i<childList.size();i++){
//					Log.d(TAG,"move "+i+" index="+((GridHolder) childList.get(i).getTag()).app.getLocation().getIndex());
//				}
			}
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
	
	onMoveListener mMoveListener;
	public interface onMoveListener{
		public abstract void move();
		public abstract void cancel();
	}
}
