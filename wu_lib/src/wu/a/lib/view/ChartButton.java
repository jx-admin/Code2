package wu.a.lib.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

/**
 * @author junxu.wang
 * 
 */
public class ChartButton extends View {
	private static final String TAG = "ChartButton";
	private float mBtnWeightCount = 0;
	private float mCenterBtnWeightCount = 0;
	private float mCenterUserWeightCount = 100;
	private Paint mPaint;
	private Paint paint2;
	private Path mPath;
	private Point mCenterPoint;
	private int w, h;
	private List<ItemButton> mChileLs;
	private List<ItemButton> mChileCenterLs;
	private int selectIndex = -1;
	private boolean centerSelect;
	private int selectColor = 0x22222222;
	private float division = 2;
	private OnItemClickListener mItemClickListener;
	public static final int ITEM_TYPE_CIRCLE = 0, ITEM_TYPE_CENTER = 1;
	private int ageHeight = 2;
	private boolean isSquare = true;
	private Context context;

	public ChartButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}

	public ChartButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
		// TODO Auto-generated constructor stub
	}

	public ChartButton(Context context) {
		super(context);
		this.context = context;
		init();
		// TODO Auto-generated constructor stub
	}

	private void init() {
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setColor(Color.GRAY);
		// mPaint.setTextSize(20);
		mCenterPoint = new Point();
		mPath = new Path();
		paint2 = new Paint();
		paint2.setAntiAlias(true);
		// 图像合成模式
		paint2.setXfermode(new PorterDuffXfermode(Mode.DST_IN));

		mChileLs = new ArrayList<ItemButton>();
		mChileCenterLs = new ArrayList<ItemButton>();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

	@Override
	protected void initializeFadingEdge(TypedArray a) {
		// TODO Auto-generated method stub
		super.initializeFadingEdge(a);
	}

	@Override
	public int getVerticalFadingEdgeLength() {
		// TODO Auto-generated method stub
		return super.getVerticalFadingEdgeLength();
	}

	@Override
	public void setFadingEdgeLength(int length) {
		// TODO Auto-generated method stub
		super.setFadingEdgeLength(length);
	}

	@Override
	public int getHorizontalFadingEdgeLength() {
		// TODO Auto-generated method stub
		return super.getHorizontalFadingEdgeLength();
	}

	@Override
	public int getVerticalScrollbarWidth() {
		// TODO Auto-generated method stub
		return super.getVerticalScrollbarWidth();
	}

	@Override
	protected int getHorizontalScrollbarHeight() {
		// TODO Auto-generated method stub
		return super.getHorizontalScrollbarHeight();
	}

	@Override
	protected void initializeScrollbars(TypedArray a) {
		// TODO Auto-generated method stub
		super.initializeScrollbars(a);
	}

	@Override
	public void setOnFocusChangeListener(OnFocusChangeListener l) {
		// TODO Auto-generated method stub
		super.setOnFocusChangeListener(l);
	}

	@Override
	public OnFocusChangeListener getOnFocusChangeListener() {
		// TODO Auto-generated method stub
		return super.getOnFocusChangeListener();
	}

	@Override
	public void setOnClickListener(OnClickListener l) {
		// TODO Auto-generated method stub
		super.setOnClickListener(l);
	}

	@Override
	public void setOnLongClickListener(OnLongClickListener l) {
		// TODO Auto-generated method stub
		super.setOnLongClickListener(l);
	}

	@Override
	public void setOnCreateContextMenuListener(OnCreateContextMenuListener l) {
		// TODO Auto-generated method stub
		super.setOnCreateContextMenuListener(l);
	}

	@Override
	public boolean performClick() {
		// TODO Auto-generated method stub
		return super.performClick();
	}

	@Override
	public boolean performLongClick() {
		// TODO Auto-generated method stub
		return super.performLongClick();
	}

	@Override
	public boolean showContextMenu() {
		// TODO Auto-generated method stub
		return super.showContextMenu();
	}

	@Override
	public void setOnKeyListener(OnKeyListener l) {
		// TODO Auto-generated method stub
		super.setOnKeyListener(l);
	}

	@Override
	public void setOnTouchListener(OnTouchListener l) {
		// TODO Auto-generated method stub
		super.setOnTouchListener(l);
	}

	@Override
	public boolean requestRectangleOnScreen(Rect rectangle) {
		// TODO Auto-generated method stub
		return super.requestRectangleOnScreen(rectangle);
	}

	@Override
	public boolean requestRectangleOnScreen(Rect rectangle, boolean immediate) {
		// TODO Auto-generated method stub
		return super.requestRectangleOnScreen(rectangle, immediate);
	}

	@Override
	public void clearFocus() {
		// TODO Auto-generated method stub
		super.clearFocus();
	}

	@Override
	public boolean hasFocus() {
		// TODO Auto-generated method stub
		return super.hasFocus();
	}

	@Override
	public boolean hasFocusable() {
		// TODO Auto-generated method stub
		return super.hasFocusable();
	}

	@Override
	protected void onFocusChanged(boolean gainFocus, int direction,
			Rect previouslyFocusedRect) {
		// TODO Auto-generated method stub
		super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
	}

	@Override
	public void sendAccessibilityEvent(int eventType) {
		// TODO Auto-generated method stub
		super.sendAccessibilityEvent(eventType);
	}

	@Override
	public void sendAccessibilityEventUnchecked(AccessibilityEvent event) {
		// TODO Auto-generated method stub
		super.sendAccessibilityEventUnchecked(event);
	}

	@Override
	public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
		// TODO Auto-generated method stub
		return super.dispatchPopulateAccessibilityEvent(event);
	}

	@Override
	public CharSequence getContentDescription() {
		// TODO Auto-generated method stub
		return super.getContentDescription();
	}

	@Override
	public void setContentDescription(CharSequence contentDescription) {
		// TODO Auto-generated method stub
		super.setContentDescription(contentDescription);
	}

	@Override
	public boolean isFocused() {
		// TODO Auto-generated method stub
		return super.isFocused();
	}

	@Override
	public View findFocus() {
		// TODO Auto-generated method stub
		return super.findFocus();
	}

	@Override
	public void setScrollContainer(boolean isScrollContainer) {
		// TODO Auto-generated method stub
		super.setScrollContainer(isScrollContainer);
	}

	@Override
	public int getDrawingCacheQuality() {
		// TODO Auto-generated method stub
		return super.getDrawingCacheQuality();
	}

	@Override
	public void setDrawingCacheQuality(int quality) {
		// TODO Auto-generated method stub
		super.setDrawingCacheQuality(quality);
	}

	@Override
	public boolean getKeepScreenOn() {
		// TODO Auto-generated method stub
		return super.getKeepScreenOn();
	}

	@Override
	public void setKeepScreenOn(boolean keepScreenOn) {
		// TODO Auto-generated method stub
		super.setKeepScreenOn(keepScreenOn);
	}

	@Override
	public int getNextFocusLeftId() {
		// TODO Auto-generated method stub
		return super.getNextFocusLeftId();
	}

	@Override
	public void setNextFocusLeftId(int nextFocusLeftId) {
		// TODO Auto-generated method stub
		super.setNextFocusLeftId(nextFocusLeftId);
	}

	@Override
	public int getNextFocusRightId() {
		// TODO Auto-generated method stub
		return super.getNextFocusRightId();
	}

	@Override
	public void setNextFocusRightId(int nextFocusRightId) {
		// TODO Auto-generated method stub
		super.setNextFocusRightId(nextFocusRightId);
	}

	@Override
	public int getNextFocusUpId() {
		// TODO Auto-generated method stub
		return super.getNextFocusUpId();
	}

	@Override
	public void setNextFocusUpId(int nextFocusUpId) {
		// TODO Auto-generated method stub
		super.setNextFocusUpId(nextFocusUpId);
	}

	@Override
	public int getNextFocusDownId() {
		// TODO Auto-generated method stub
		return super.getNextFocusDownId();
	}

	@Override
	public void setNextFocusDownId(int nextFocusDownId) {
		// TODO Auto-generated method stub
		super.setNextFocusDownId(nextFocusDownId);
	}

	@Override
	public boolean isShown() {
		// TODO Auto-generated method stub
		return super.isShown();
	}

	@Override
	protected boolean fitSystemWindows(Rect insets) {
		// TODO Auto-generated method stub
		return super.fitSystemWindows(insets);
	}

	@Override
	public int getVisibility() {
		// TODO Auto-generated method stub
		return super.getVisibility();
	}

	@Override
	public void setVisibility(int visibility) {
		// TODO Auto-generated method stub
		super.setVisibility(visibility);
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return super.isEnabled();
	}

	@Override
	public void setEnabled(boolean enabled) {
		// TODO Auto-generated method stub
		super.setEnabled(enabled);
	}

	@Override
	public void setFocusable(boolean focusable) {
		// TODO Auto-generated method stub
		super.setFocusable(focusable);
	}

	@Override
	public void setFocusableInTouchMode(boolean focusableInTouchMode) {
		// TODO Auto-generated method stub
		super.setFocusableInTouchMode(focusableInTouchMode);
	}

	@Override
	public void setSoundEffectsEnabled(boolean soundEffectsEnabled) {
		// TODO Auto-generated method stub
		super.setSoundEffectsEnabled(soundEffectsEnabled);
	}

	@Override
	public boolean isSoundEffectsEnabled() {
		// TODO Auto-generated method stub
		return super.isSoundEffectsEnabled();
	}

	@Override
	public void setHapticFeedbackEnabled(boolean hapticFeedbackEnabled) {
		// TODO Auto-generated method stub
		super.setHapticFeedbackEnabled(hapticFeedbackEnabled);
	}

	@Override
	public boolean isHapticFeedbackEnabled() {
		// TODO Auto-generated method stub
		return super.isHapticFeedbackEnabled();
	}

	@Override
	public void setWillNotDraw(boolean willNotDraw) {
		// TODO Auto-generated method stub
		super.setWillNotDraw(willNotDraw);
	}

	@Override
	public boolean willNotDraw() {
		// TODO Auto-generated method stub
		return super.willNotDraw();
	}

	@Override
	public void setWillNotCacheDrawing(boolean willNotCacheDrawing) {
		// TODO Auto-generated method stub
		super.setWillNotCacheDrawing(willNotCacheDrawing);
	}

	@Override
	public boolean willNotCacheDrawing() {
		// TODO Auto-generated method stub
		return super.willNotCacheDrawing();
	}

	@Override
	public boolean isClickable() {
		// TODO Auto-generated method stub
		return super.isClickable();
	}

	@Override
	public void setClickable(boolean clickable) {
		// TODO Auto-generated method stub
		super.setClickable(clickable);
	}

	@Override
	public boolean isLongClickable() {
		// TODO Auto-generated method stub
		return super.isLongClickable();
	}

	@Override
	public void setLongClickable(boolean longClickable) {
		// TODO Auto-generated method stub
		super.setLongClickable(longClickable);
	}

	@Override
	public void setPressed(boolean pressed) {
		// TODO Auto-generated method stub
		super.setPressed(pressed);
	}

	@Override
	protected void dispatchSetPressed(boolean pressed) {
		// TODO Auto-generated method stub
		super.dispatchSetPressed(pressed);
	}

	@Override
	public boolean isPressed() {
		// TODO Auto-generated method stub
		return super.isPressed();
	}

	@Override
	public boolean isSaveEnabled() {
		// TODO Auto-generated method stub
		return super.isSaveEnabled();
	}

	@Override
	public void setSaveEnabled(boolean enabled) {
		// TODO Auto-generated method stub
		super.setSaveEnabled(enabled);
	}

	@Override
	public View focusSearch(int direction) {
		// TODO Auto-generated method stub
		return super.focusSearch(direction);
	}

	@Override
	public boolean dispatchUnhandledMove(View focused, int direction) {
		// TODO Auto-generated method stub
		return super.dispatchUnhandledMove(focused, direction);
	}

	@Override
	public ArrayList<View> getFocusables(int direction) {
		// TODO Auto-generated method stub
		return super.getFocusables(direction);
	}

	@Override
	public void addFocusables(ArrayList<View> views, int direction) {
		// TODO Auto-generated method stub
		super.addFocusables(views, direction);
	}

	@Override
	public void addFocusables(ArrayList<View> views, int direction,
			int focusableMode) {
		// TODO Auto-generated method stub
		super.addFocusables(views, direction, focusableMode);
	}

	@Override
	public ArrayList<View> getTouchables() {
		// TODO Auto-generated method stub
		return super.getTouchables();
	}

	@Override
	public void addTouchables(ArrayList<View> views) {
		// TODO Auto-generated method stub
		super.addTouchables(views);
	}

	@Override
	public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
		// TODO Auto-generated method stub
		return super.requestFocus(direction, previouslyFocusedRect);
	}

	@Override
	public void onStartTemporaryDetach() {
		// TODO Auto-generated method stub
		super.onStartTemporaryDetach();
	}

	@Override
	public void onFinishTemporaryDetach() {
		// TODO Auto-generated method stub
		super.onFinishTemporaryDetach();
	}

	@Override
	public boolean dispatchKeyEventPreIme(KeyEvent event) {
		// TODO Auto-generated method stub
		return super.dispatchKeyEventPreIme(event);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		return super.dispatchKeyEvent(event);
	}

	@Override
	public boolean dispatchKeyShortcutEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		return super.dispatchKeyShortcutEvent(event);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return super.dispatchTouchEvent(event);
	}

	@Override
	public boolean dispatchTrackballEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return super.dispatchTrackballEvent(event);
	}

	@Override
	public void dispatchWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.dispatchWindowFocusChanged(hasFocus);
	}

	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasWindowFocus);
	}

	@Override
	public boolean hasWindowFocus() {
		// TODO Auto-generated method stub
		return super.hasWindowFocus();
	}

	@Override
	public void dispatchWindowVisibilityChanged(int visibility) {
		// TODO Auto-generated method stub
		super.dispatchWindowVisibilityChanged(visibility);
	}

	@Override
	protected void onWindowVisibilityChanged(int visibility) {
		// TODO Auto-generated method stub
		super.onWindowVisibilityChanged(visibility);
	}

	@Override
	public int getWindowVisibility() {
		// TODO Auto-generated method stub
		return super.getWindowVisibility();
	}

	@Override
	public void getWindowVisibleDisplayFrame(Rect outRect) {
		// TODO Auto-generated method stub
		super.getWindowVisibleDisplayFrame(outRect);
	}

	@Override
	public boolean isInTouchMode() {
		// TODO Auto-generated method stub
		return super.isInTouchMode();
	}

	@Override
	public boolean onKeyPreIme(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyPreIme(keyCode, event);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyUp(keyCode, event);
	}

	@Override
	public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyMultiple(keyCode, repeatCount, event);
	}

	@Override
	public boolean onKeyShortcut(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyShortcut(keyCode, event);
	}

	@Override
	public boolean onCheckIsTextEditor() {
		// TODO Auto-generated method stub
		return super.onCheckIsTextEditor();
	}

	@Override
	public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
		// TODO Auto-generated method stub
		return super.onCreateInputConnection(outAttrs);
	}

	@Override
	public boolean checkInputConnectionProxy(View view) {
		// TODO Auto-generated method stub
		return super.checkInputConnectionProxy(view);
	}

	@Override
	public void createContextMenu(ContextMenu menu) {
		// TODO Auto-generated method stub
		super.createContextMenu(menu);
	}

	@Override
	protected ContextMenuInfo getContextMenuInfo() {
		// TODO Auto-generated method stub
		return super.getContextMenuInfo();
	}

	@Override
	protected void onCreateContextMenu(ContextMenu menu) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu);
	}

	@Override
	public boolean onTrackballEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return super.onTrackballEvent(event);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		// Log.d(TAG, "action " + action);
		switch (action) {
		case MotionEvent.ACTION_CANCEL:
			if (selectIndex >= 0) {
				invalidate();
				selectIndex = -1;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			float x = event.getX();
			float y = event.getY();
			int distance = (int) getDistance(mCenterPoint.x, mCenterPoint.y, x,
					y);
			if (distance < ((int) rf5.width() >> 1)) {
				selectIndex = getCenterIndex(y
						- ((int) (rf1.bottom - rf5.height()) >> 1));
				if (selectIndex >= 0
						&& mChileCenterLs.get(selectIndex).clickAble) {
					centerSelect = true;
					// if(mItemClickListener!=null){
					// mItemClickListener.onItemClick(null, this, selectIndex,
					// ITEM_TYPE_CENTER);
					// }
					invalidate();
					return true;
				} else if (selectIndex >= 0) {
					invalidate();
					selectIndex = -1;
				}
			} else if (distance < (int) rf3.width() >> 1) {
				centerSelect = false;
				selectIndex = getUpChartProp(x, y);

				if (selectIndex >= 0 && mChileLs.get(selectIndex).clickAble) {
					// if(mItemClickListener!=null){
					// mItemClickListener.onItemClick(null, this, selectIndex,
					// ITEM_TYPE_CIRCLE);
					// }
					invalidate();
					return true;
				} else if (selectIndex >= 0) {
					invalidate();
					selectIndex = -1;
				}
			} else if (selectIndex >= 0) {
				invalidate();
				selectIndex = -1;
			}
			break;
		case MotionEvent.ACTION_UP:
			if (selectIndex >= 0 && mItemClickListener != null) {
				if (centerSelect) {
					mItemClickListener.onItemClick(this, this.mChileCenterLs.get(selectIndex), selectIndex,
							ITEM_TYPE_CENTER);
				} else {
					mItemClickListener.onItemClick( this, mChileLs.get(selectIndex),selectIndex,
							ITEM_TYPE_CIRCLE);
				}
			}
			selectIndex = -1;
			invalidate();
			return true;
		case MotionEvent.ACTION_DOWN:
			x = event.getX();
			y = event.getY();
			distance = (int) getDistance(mCenterPoint.x, mCenterPoint.y, x, y);
			if (distance < ((int) rf5.width() >> 1)) {
				selectIndex = getCenterIndex(y
						- ((int) (rf1.bottom - rf5.height()) >> 1));
				if (selectIndex >= 0
						&& mChileCenterLs.get(selectIndex).clickAble) {
					centerSelect = true;
					// if(mItemClickListener!=null){
					// mItemClickListener.onItemClick(null, this, selectIndex,
					// ITEM_TYPE_CENTER);
					// }
					invalidate();
					return true;
				} else {
					selectIndex = -1;
				}
			} else if (distance < (int) rf3.width() >> 1) {
				centerSelect = false;
				selectIndex = getUpChartProp(x, y);

				if (selectIndex >= 0 && mChileLs.get(selectIndex).clickAble) {
					// if(mItemClickListener!=null){
					// mItemClickListener.onItemClick(null, this, selectIndex,
					// ITEM_TYPE_CIRCLE);
					// }
					invalidate();
					return true;
				} else {
					selectIndex = -1;
				}
			} else {
				selectIndex = -1;
			}
			break;
		default:
			break;
		}

		// TODO Auto-generated method stub
		return super.onTouchEvent(event);
	}

	// private boolean isCenterCircle(float x,float y){
	// return getDistance(mCenterPoint.x, mCenterPoint.y, x, y)<=mCenterBtnW;
	// }
	//
	// private boolean isMidBtnCircle(float x,float y){
	// return getDistance(mCenterPoint.x, mCenterPoint.y, x, y)<=mMidBtnW;
	// }
	@Override
	public void cancelLongPress() {
		// TODO Auto-generated method stub
		super.cancelLongPress();
	}

	@Override
	public void setTouchDelegate(TouchDelegate delegate) {
		// TODO Auto-generated method stub
		super.setTouchDelegate(delegate);
	}

	@Override
	public TouchDelegate getTouchDelegate() {
		// TODO Auto-generated method stub
		return super.getTouchDelegate();
	}

	@Override
	public void bringToFront() {
		// TODO Auto-generated method stub
		super.bringToFront();
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		// TODO Auto-generated method stub
		super.onScrollChanged(l, t, oldl, oldt);
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.dispatchDraw(canvas);
	}

	@Override
	public void getDrawingRect(Rect outRect) {
		// TODO Auto-generated method stub
		super.getDrawingRect(outRect);
	}

	@Override
	public void getHitRect(Rect outRect) {
		// TODO Auto-generated method stub
		super.getHitRect(outRect);
	}

	@Override
	public void getFocusedRect(Rect r) {
		// TODO Auto-generated method stub
		super.getFocusedRect(r);
	}

	@Override
	public boolean getGlobalVisibleRect(Rect r, Point globalOffset) {
		// TODO Auto-generated method stub
		return super.getGlobalVisibleRect(r, globalOffset);
	}

	@Override
	public void offsetTopAndBottom(int offset) {
		// TODO Auto-generated method stub
		super.offsetTopAndBottom(offset);
	}

	@Override
	public void offsetLeftAndRight(int offset) {
		// TODO Auto-generated method stub
		super.offsetLeftAndRight(offset);
	}

	@Override
	public LayoutParams getLayoutParams() {
		// TODO Auto-generated method stub
		return super.getLayoutParams();
	}

	@Override
	public void setLayoutParams(LayoutParams params) {
		// TODO Auto-generated method stub
		super.setLayoutParams(params);
	}

	@Override
	public void scrollTo(int x, int y) {
		// TODO Auto-generated method stub
		super.scrollTo(x, y);
	}

	@Override
	public void scrollBy(int x, int y) {
		// TODO Auto-generated method stub
		super.scrollBy(x, y);
	}

	@Override
	public void invalidate(Rect dirty) {
		// TODO Auto-generated method stub
		super.invalidate(dirty);
	}

	@Override
	public void invalidate(int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.invalidate(l, t, r, b);
	}

	@Override
	public void invalidate() {
		// TODO Auto-generated method stub
		super.invalidate();
	}

	@Override
	public Handler getHandler() {
		// TODO Auto-generated method stub
		return super.getHandler();
	}

	@Override
	public boolean post(Runnable action) {
		// TODO Auto-generated method stub
		return super.post(action);
	}

	@Override
	public boolean postDelayed(Runnable action, long delayMillis) {
		// TODO Auto-generated method stub
		return super.postDelayed(action, delayMillis);
	}

	@Override
	public boolean removeCallbacks(Runnable action) {
		// TODO Auto-generated method stub
		return super.removeCallbacks(action);
	}

	@Override
	public void postInvalidate() {
		// TODO Auto-generated method stub
		super.postInvalidate();
	}

	@Override
	public void postInvalidate(int left, int top, int right, int bottom) {
		// TODO Auto-generated method stub
		super.postInvalidate(left, top, right, bottom);
	}

	@Override
	public void postInvalidateDelayed(long delayMilliseconds) {
		// TODO Auto-generated method stub
		super.postInvalidateDelayed(delayMilliseconds);
	}

	@Override
	public void postInvalidateDelayed(long delayMilliseconds, int left,
			int top, int right, int bottom) {
		// TODO Auto-generated method stub
		super.postInvalidateDelayed(delayMilliseconds, left, top, right, bottom);
	}

	@Override
	public void computeScroll() {
		// TODO Auto-generated method stub
		super.computeScroll();
	}

	@Override
	public boolean isHorizontalFadingEdgeEnabled() {
		// TODO Auto-generated method stub
		return super.isHorizontalFadingEdgeEnabled();
	}

	@Override
	public void setHorizontalFadingEdgeEnabled(
			boolean horizontalFadingEdgeEnabled) {
		// TODO Auto-generated method stub
		super.setHorizontalFadingEdgeEnabled(horizontalFadingEdgeEnabled);
	}

	@Override
	public boolean isVerticalFadingEdgeEnabled() {
		// TODO Auto-generated method stub
		return super.isVerticalFadingEdgeEnabled();
	}

	@Override
	public void setVerticalFadingEdgeEnabled(boolean verticalFadingEdgeEnabled) {
		// TODO Auto-generated method stub
		super.setVerticalFadingEdgeEnabled(verticalFadingEdgeEnabled);
	}

	@Override
	protected float getTopFadingEdgeStrength() {
		// TODO Auto-generated method stub
		return super.getTopFadingEdgeStrength();
	}

	@Override
	protected float getBottomFadingEdgeStrength() {
		// TODO Auto-generated method stub
		return super.getBottomFadingEdgeStrength();
	}

	@Override
	protected float getLeftFadingEdgeStrength() {
		// TODO Auto-generated method stub
		return super.getLeftFadingEdgeStrength();
	}

	@Override
	protected float getRightFadingEdgeStrength() {
		// TODO Auto-generated method stub
		return super.getRightFadingEdgeStrength();
	}

	@Override
	public boolean isHorizontalScrollBarEnabled() {
		// TODO Auto-generated method stub
		return super.isHorizontalScrollBarEnabled();
	}

	@Override
	public void setHorizontalScrollBarEnabled(boolean horizontalScrollBarEnabled) {
		// TODO Auto-generated method stub
		super.setHorizontalScrollBarEnabled(horizontalScrollBarEnabled);
	}

	@Override
	public boolean isVerticalScrollBarEnabled() {
		// TODO Auto-generated method stub
		return super.isVerticalScrollBarEnabled();
	}

	@Override
	public void setVerticalScrollBarEnabled(boolean verticalScrollBarEnabled) {
		// TODO Auto-generated method stub
		super.setVerticalScrollBarEnabled(verticalScrollBarEnabled);
	}

	@Override
	public void setScrollBarStyle(int style) {
		// TODO Auto-generated method stub
		super.setScrollBarStyle(style);
	}

	@Override
	public int getScrollBarStyle() {
		// TODO Auto-generated method stub
		return super.getScrollBarStyle();
	}

	@Override
	protected int computeHorizontalScrollRange() {
		// TODO Auto-generated method stub
		return super.computeHorizontalScrollRange();
	}

	@Override
	protected int computeHorizontalScrollOffset() {
		// TODO Auto-generated method stub
		return super.computeHorizontalScrollOffset();
	}

	@Override
	protected int computeHorizontalScrollExtent() {
		// TODO Auto-generated method stub
		return super.computeHorizontalScrollExtent();
	}

	@Override
	protected int computeVerticalScrollRange() {
		// TODO Auto-generated method stub
		return super.computeVerticalScrollRange();
	}

	@Override
	protected int computeVerticalScrollOffset() {
		// TODO Auto-generated method stub
		return super.computeVerticalScrollOffset();
	}

	@Override
	protected int computeVerticalScrollExtent() {
		// TODO Auto-generated method stub
		return super.computeVerticalScrollExtent();
	}

	RectF rf1 = new RectF();
	RectF rf2 = new RectF();
	RectF rf3 = new RectF();
	RectF rf4 = new RectF();
	RectF rf5 = new RectF();

	private void initDraw(int width, int height) {
		if (isSquare) {
			w = h = Math.min(width, height);
		} else {
			w = width;
			h = height;
		}
		mCenterPoint.x = w >> 1;
		mCenterPoint.y = h >> 1;
		rf1.right = w;
		rf1.bottom = h;

		rf2.left = (w * 5 / 50) >> 1;
		rf2.top = (h * 5 / 50) >> 1;
		rf2.right = w - rf2.left;
		rf2.bottom = h - rf2.top;

		rf3.left = (w * 8 / 50) >> 1;
		rf3.top = (h * 8 / 50) >> 1;
		rf3.right = w - rf3.left;
		rf3.bottom = h - rf3.top;

		rf4.left = (w * 11 / 50) >> 1;
		rf4.top = (h * 11 / 50) >> 1;
		rf4.right = w - rf4.left;
		rf4.bottom = h - rf4.top;

		rf5.left = (w * 22 / 50) >> 1;
		rf5.top = (h * 22 / 50) >> 1;
		rf5.right = w - rf5.left;
		rf5.bottom = h - rf5.top;

	}

	// 线性渐变
	LinearGradient shader = new LinearGradient(0, 0, 0, rf1.height(),
			new int[] { 0xe2e2e2e2, 0xcccccccc }, null, TileMode.CLAMP);
	LinearGradient shader2 = new LinearGradient(0, 0, 0, rf2.height(),
			new int[] { 0xb6b6b6b6, 0xedededed }, null, TileMode.CLAMP);
	LinearGradient shader3 = new LinearGradient(0, 0, 0, rf3.height(),
			new int[] { 0xcccccccc, 0xf1f1f1f1 }, null, TileMode.CLAMP);
	LinearGradient shader4 = new LinearGradient(0, 0, 0, rf4.height(),
			new int[] { 0xbabababa, 0xe9e9e9e9 }, null, TileMode.CLAMP);
	LinearGradient shaderAge = new LinearGradient(0, 0, 0, ageHeight,
			new int[] { 0xd8d8d8d8, 0xbcbcbcbc }, null, TileMode.CLAMP);
	PaintFlagsDrawFilter filter = new PaintFlagsDrawFilter(0,
			Paint.FILTER_BITMAP_FLAG);

	@Override
	protected void onDraw(Canvas canvas) {
		// back circle's light
		mPaint.setColor(Color.WHITE);
		mPaint.setStyle(Style.FILL);
		canvas.drawOval(rf1, mPaint);

		// back circle's ageLine
		mPaint.setColor(0xa6a6a6a6);
		mPaint.setStrokeWidth(2);
		mPaint.setStyle(Style.STROKE);
		canvas.drawOval(rf1, mPaint);

		paint2.setShader(shader);
		canvas.drawOval(rf1, paint2);

		// back circle's dark
		mPaint.setStyle(Style.FILL);
		mPaint.setColor(Color.WHITE);
		canvas.drawOval(rf2, mPaint);

		paint2.setShader(shader2);
		canvas.drawOval(rf2, paint2);

		// middle button'bk
		// mPaint.setColor(Color.WHITE);
		// canvas.drawOval(rf3, mPaint);

		// middle button
		drawMidButtons(canvas, rf3);
		paint2.setShader(shader3);
		canvas.drawOval(rf3, paint2);

		// center circle effect
		// mPaint.setColor(0x44444444);
		// canvas.drawOval(rf4, mPaint);
		// 线性渐变
		paint2.setShader(shader4);
		canvas.drawOval(rf4, paint2);

		// center Button bk
		mPaint.setColor(Color.WHITE);
		canvas.drawOval(rf5, mPaint);

		// center'bk 0xe0e0e0e0,0x89898989
		RadialGradient rg = new RadialGradient(rf5.centerX() + rf5.width()
				* 0.05f, rf5.centerY() + rf5.height() * 0.05f,
				rf5.width() * 0.6f, new int[] { 0xffff0000, 0x80000000 },
				new float[] { 0.77f, 1f }, TileMode.CLAMP);
		paint2.setShader(rg);
		canvas.drawOval(rf5, paint2);

		if (mChileCenterLs.size() == 1) {
			drawSigleCenter(canvas, rf5);
			drawSigleCenterText(canvas, rf5);
		} else {
			// center buttons
			canvas.save();
			// canvas.translate(rf5.left,rf5.top);
			mPath.reset();
			canvas.clipPath(mPath); // makes the clip empty
			mPath.addOval(rf5, Path.Direction.CCW);
			// mPath.addCircle((int)rf5.width()>>1,
			// (int)rf5.height()>>1,(int)rf5.width()>>1, Path.Direction.CCW);
			canvas.clipPath(mPath, Region.Op.REPLACE);
			drawLinear(canvas);

			canvas.restore();

			drawLinearText(canvas);
		}

		drawText(canvas, rf3);
		super.onDraw(canvas);
	}

	private void drawMidButtons(Canvas canvas, RectF r) {
		float sc = 0;
		int size = mChileLs.size();
		ItemButton chile;
		mPaint.setStyle(Style.FILL);
		for (int i = 0; i < size; i++) {
			chile = mChileLs.get(i);
			mPaint.setColor(chile.backgroundColor);
			canvas.drawArc(r, sc, chile.value, true, mPaint);
			if (!centerSelect && i == selectIndex) {
				mPaint.setColor(selectColor);
				canvas.drawArc(r, sc, chile.value, true, mPaint);
			}
			sc += chile.value;
			mPaint.setColor(Color.WHITE);
			canvas.drawArc(r, sc, division, true, mPaint);

			sc += division;

			// float arg = sc - chile.value / 2;
			// double mPiv = arg * (Math.PI / 180);
			// float x = (r.width() / 2) * 0.8f * (float) Math.cos(mPiv)
			// + ((int) rf1.right >> 1);
			// float y = (r.height() / 2) * 0.8f * (float) Math.sin(mPiv)
			// + ((int) rf1.right >> 1);
			// mPaint.setTextAlign(Align.CENTER);
			// mPaint.setColor(chile.shaderColor);
			// canvas.drawText(chile.text, x+2, y+2, mPaint);
			// mPaint.setColor(chile.textColor);
			// canvas.drawText(chile.text, x, y, mPaint);
		}

	}

	private void drawText(Canvas canvas, RectF r) {
		float sc = 0;
		int size = mChileLs.size();
		ItemButton chile;
		mPaint.setStyle(Style.FILL);
		for (int i = 0; i < size; i++) {
			chile = mChileLs.get(i);
			sc += chile.value;
			sc += division;
			float arg = sc - chile.value / 2;
			double mPiv = arg * (Math.PI / 180);
			float x = (r.width() / 2) * 0.8f * (float) Math.cos(mPiv)
					+ ((int) rf1.right >> 1);
			float y = (r.height() / 2) * 0.8f * (float) Math.sin(mPiv)
					+ ((int) rf1.right >> 1);
			mPaint.setTextAlign(Align.CENTER);
			mPaint.setColor(chile.shaderColor);
			// canvas.drawText(chile.text, x+2, y+2, mPaint);
			canvas.drawText(chile.text, x + 1, y + 1, mPaint);
			mPaint.setColor(chile.textColor);
			canvas.drawText(chile.text, x, y, mPaint);
		}
	}

	private void drawSigleCenter(Canvas canvas, RectF rf) {
		ItemButton chile = mChileCenterLs.get(0);
		if (chile.visibility) {
			// 线性渐变
			paint2.setShader(shaderAge);
			canvas.drawArc(rf, (180 - chile.value) / 2 - ageHeight, chile.value
					+ ageHeight + ageHeight, false, paint2);

			mPaint.setColor(chile.backgroundColor);
			mPaint.setStyle(Style.FILL);
			canvas.drawArc(rf, (180 - chile.value) / 2, chile.value, false,
					mPaint);
			if (centerSelect && selectIndex == 0) {
				mPaint.setColor(selectColor);
				canvas.drawArc(rf, (180 - chile.value) / 2, chile.value, false,
						mPaint);
			}
			// mPaint.setTextAlign(Paint.Align.CENTER);
			// mPaint.setColor(chile.shaderColor);
			// canvas.drawText(chile.text, rf.centerX()+2, rf.bottom - ((int)
			// chile.value >> 1)+2, mPaint);
			// canvas.drawText(chile.text, rf.centerX()+1, rf.bottom - ((int)
			// chile.value >> 1)+1, mPaint);
			// mPaint.setColor(chile.textColor);
			// canvas.drawText(chile.text, rf.centerX(), rf.bottom - ((int)
			// chile.value >> 1), mPaint);
		}
	}

	private void drawSigleCenterText(Canvas canvas, RectF rf) {
		ItemButton chile = mChileCenterLs.get(0);
		if (chile.visibility) {
			mPaint.setTextAlign(Paint.Align.CENTER);
			mPaint.setColor(chile.shaderColor);
			// canvas.drawText(chile.text, rf.centerX()+2, rf.bottom - ((int)
			// chile.value >> 1)+2, mPaint);
			canvas.drawText(chile.text, rf.centerX() + 1, rf.bottom
					- ((int) chile.value >> 1) + 1, mPaint);
			mPaint.setColor(chile.textColor);
			canvas.drawText(chile.text, rf.centerX(), rf.bottom
					- ((int) chile.value >> 1), mPaint);
		}
	}

	private void drawLinear(Canvas canvas) {
		ItemButton chile;
		float ch = rf5.bottom;
		// float percent=h/mCenterBtnWeightCount;
		Log.d(TAG, "w=" + this.w + " h=" + this.h + " rf5x=" + rf5.left
				+ " rf5y=" + rf5.top + " rf5r=" + rf5.right + " rf5.b="
				+ rf5.bottom);
		float tmH;
		for (int i = 0; i < mChileCenterLs.size(); i++) {
			chile = mChileCenterLs.get(i);
			Log.d(TAG, "ch" + i + " v=" + chile.value);
			// ch-=(int)(percent*mChileCenterLs.get(i).value);
			ch -= chile.value;
			if (chile.visibility) {
				mPaint.setColor(chile.backgroundColor);
				mPaint.setStyle(Style.FILL);
				canvas.drawRect(new RectF(rf5.left, ch, rf5.right,
						(ch + chile.value)), mPaint);
				if (centerSelect && selectIndex == i) {
					mPaint.setColor(selectColor);
					mPaint.setStyle(Style.FILL_AND_STROKE);
					canvas.drawRect(new RectF(rf5.left, ch, rf5.right,
							(ch + chile.value)), mPaint);
				}
				// tmH=percent*mChileCenterLs.get(i).value+0.05f;
				canvas.drawRect(new RectF(rf5.left, ch, rf5.right,
						(ch + chile.value)), mPaint);
				// mPaint.setTextAlign(Paint.Align.CENTER);
				// mPaint.setColor(chile.shaderColor);
				// canvas.drawText(chile.text, rf5.centerX()+2, ch + ((int)
				// chile.value >> 1)+2, mPaint);
				// canvas.drawText(chile.text, rf5.centerX()+1, ch + ((int)
				// chile.value >> 1)+1, mPaint);
				// mPaint.setColor(chile.textColor);
				// canvas.drawText(chile.text, rf5.centerX(), ch + ((int)
				// chile.value >> 1), mPaint);
			} else {
				// 线性渐变
				paint2.setShader(shaderAge);
				canvas.drawRect(new RectF(rf5.left, ch + chile.value
						- ageHeight, rf5.right, (ch + chile.value)), paint2);
			}
		}
	}

	private void drawLinearText(Canvas canvas) {
		ItemButton chile;
		float ch = rf5.bottom;
		for (int i = 0; i < mChileCenterLs.size(); i++) {
			chile = mChileCenterLs.get(i);
			ch -= chile.value;
			if (chile.visibility) {
				mPaint.setTextAlign(Paint.Align.CENTER);
				mPaint.setColor(chile.shaderColor);
				// canvas.drawText(chile.text, rf5.centerX()+2, ch + ((int)
				// chile.value >> 1)+2, mPaint);
				canvas.drawText(chile.text, rf5.centerX() + 1, ch
						+ ((int) chile.value >> 1) + 1, mPaint);
				mPaint.setColor(chile.textColor);
				canvas.drawText(chile.text, rf5.centerX(), ch
						+ ((int) chile.value >> 1), mPaint);
			}
		}
	}

	@Override
	protected void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();
	}

	@Override
	protected void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		super.onDetachedFromWindow();
	}

	@Override
	protected int getWindowAttachCount() {
		// TODO Auto-generated method stub
		return super.getWindowAttachCount();
	}

	@Override
	public IBinder getWindowToken() {
		// TODO Auto-generated method stub
		return super.getWindowToken();
	}

	@Override
	public IBinder getApplicationWindowToken() {
		// TODO Auto-generated method stub
		return super.getApplicationWindowToken();
	}

	@Override
	public void saveHierarchyState(SparseArray<Parcelable> container) {
		// TODO Auto-generated method stub
		super.saveHierarchyState(container);
	}

	@Override
	protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
		// TODO Auto-generated method stub
		super.dispatchSaveInstanceState(container);
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		// TODO Auto-generated method stub
		return super.onSaveInstanceState();
	}

	@Override
	public void restoreHierarchyState(SparseArray<Parcelable> container) {
		// TODO Auto-generated method stub
		super.restoreHierarchyState(container);
	}

	@Override
	protected void dispatchRestoreInstanceState(
			SparseArray<Parcelable> container) {
		// TODO Auto-generated method stub
		super.dispatchRestoreInstanceState(container);
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(state);
	}

	@Override
	public long getDrawingTime() {
		// TODO Auto-generated method stub
		return super.getDrawingTime();
	}

	@Override
	public void setDuplicateParentStateEnabled(boolean enabled) {
		// TODO Auto-generated method stub
		super.setDuplicateParentStateEnabled(enabled);
	}

	@Override
	public boolean isDuplicateParentStateEnabled() {
		// TODO Auto-generated method stub
		return super.isDuplicateParentStateEnabled();
	}

	@Override
	public void setDrawingCacheEnabled(boolean enabled) {
		// TODO Auto-generated method stub
		super.setDrawingCacheEnabled(enabled);
	}

	@Override
	public boolean isDrawingCacheEnabled() {
		// TODO Auto-generated method stub
		return super.isDrawingCacheEnabled();
	}

	@Override
	public Bitmap getDrawingCache() {
		// TODO Auto-generated method stub
		return super.getDrawingCache();
	}

	@Override
	public Bitmap getDrawingCache(boolean autoScale) {
		// TODO Auto-generated method stub
		return super.getDrawingCache(autoScale);
	}

	@Override
	public void destroyDrawingCache() {
		// TODO Auto-generated method stub
		super.destroyDrawingCache();
	}

	@Override
	public void setDrawingCacheBackgroundColor(int color) {
		// TODO Auto-generated method stub
		super.setDrawingCacheBackgroundColor(color);
	}

	@Override
	public int getDrawingCacheBackgroundColor() {
		// TODO Auto-generated method stub
		return super.getDrawingCacheBackgroundColor();
	}

	@Override
	public void buildDrawingCache() {
		// TODO Auto-generated method stub
		super.buildDrawingCache();
	}

	@Override
	public void buildDrawingCache(boolean autoScale) {
		// TODO Auto-generated method stub
		super.buildDrawingCache(autoScale);
	}

	@Override
	public boolean isInEditMode() {
		// TODO Auto-generated method stub
		return super.isInEditMode();
	}

	@Override
	protected boolean isPaddingOffsetRequired() {
		// TODO Auto-generated method stub
		return super.isPaddingOffsetRequired();
	}

	@Override
	protected int getLeftPaddingOffset() {
		// TODO Auto-generated method stub
		return super.getLeftPaddingOffset();
	}

	@Override
	protected int getRightPaddingOffset() {
		// TODO Auto-generated method stub
		return super.getRightPaddingOffset();
	}

	@Override
	protected int getTopPaddingOffset() {
		// TODO Auto-generated method stub
		return super.getTopPaddingOffset();
	}

	@Override
	protected int getBottomPaddingOffset() {
		// TODO Auto-generated method stub
		return super.getBottomPaddingOffset();
	}

	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.draw(canvas);
	}

	@Override
	public int getSolidColor() {
		// TODO Auto-generated method stub
		return super.getSolidColor();
	}

	@Override
	public boolean isLayoutRequested() {
		// TODO Auto-generated method stub
		return super.isLayoutRequested();
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		// TODO Auto-generated method stub
		super.onLayout(changed, left, top, right, bottom);
	}

	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
	}

	@Override
	public Resources getResources() {
		// TODO Auto-generated method stub
		return super.getResources();
	}

	@Override
	public void invalidateDrawable(Drawable drawable) {
		// TODO Auto-generated method stub
		super.invalidateDrawable(drawable);
	}

	@Override
	public void scheduleDrawable(Drawable who, Runnable what, long when) {
		// TODO Auto-generated method stub
		super.scheduleDrawable(who, what, when);
	}

	@Override
	public void unscheduleDrawable(Drawable who, Runnable what) {
		// TODO Auto-generated method stub
		super.unscheduleDrawable(who, what);
	}

	@Override
	public void unscheduleDrawable(Drawable who) {
		// TODO Auto-generated method stub
		super.unscheduleDrawable(who);
	}

	@Override
	protected boolean verifyDrawable(Drawable who) {
		// TODO Auto-generated method stub
		return super.verifyDrawable(who);
	}

	@Override
	protected void drawableStateChanged() {
		// TODO Auto-generated method stub
		super.drawableStateChanged();
	}

	@Override
	public void refreshDrawableState() {
		// TODO Auto-generated method stub
		super.refreshDrawableState();
	}

	@Override
	protected int[] onCreateDrawableState(int extraSpace) {
		// TODO Auto-generated method stub
		return super.onCreateDrawableState(extraSpace);
	}

	@Override
	public void setBackgroundColor(int color) {
		// TODO Auto-generated method stub
		super.setBackgroundColor(color);
	}

	@Override
	public void setBackgroundResource(int resid) {
		// TODO Auto-generated method stub
		super.setBackgroundResource(resid);
	}

	@Override
	public void setBackgroundDrawable(Drawable background) {
		// TODO Auto-generated method stub
		super.setBackgroundDrawable(background);
	}

	@Override
	public Drawable getBackground() {
		// TODO Auto-generated method stub
		return super.getBackground();
	}

	@Override
	public void setPadding(int left, int top, int right, int bottom) {
		// TODO Auto-generated method stub
		super.setPadding(left, top, right, bottom);
	}

	@Override
	public int getPaddingTop() {
		// TODO Auto-generated method stub
		return super.getPaddingTop();
	}

	@Override
	public int getPaddingBottom() {
		// TODO Auto-generated method stub
		return super.getPaddingBottom();
	}

	@Override
	public int getPaddingLeft() {
		// TODO Auto-generated method stub
		return super.getPaddingLeft();
	}

	@Override
	public int getPaddingRight() {
		// TODO Auto-generated method stub
		return super.getPaddingRight();
	}

	@Override
	public void setSelected(boolean selected) {
		// TODO Auto-generated method stub
		super.setSelected(selected);
	}

	@Override
	protected void dispatchSetSelected(boolean selected) {
		// TODO Auto-generated method stub
		super.dispatchSetSelected(selected);
	}

	@Override
	public boolean isSelected() {
		// TODO Auto-generated method stub
		return super.isSelected();
	}

	@Override
	public ViewTreeObserver getViewTreeObserver() {
		// TODO Auto-generated method stub
		return super.getViewTreeObserver();
	}

	@Override
	public View getRootView() {
		// TODO Auto-generated method stub
		return super.getRootView();
	}

	@Override
	public void getLocationOnScreen(int[] location) {
		// TODO Auto-generated method stub
		super.getLocationOnScreen(location);
	}

	@Override
	public void getLocationInWindow(int[] location) {
		// TODO Auto-generated method stub
		super.getLocationInWindow(location);
	}

	@Override
	public void setId(int id) {
		// TODO Auto-generated method stub
		super.setId(id);
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return super.getId();
	}

	@Override
	public Object getTag() {
		// TODO Auto-generated method stub
		return super.getTag();
	}

	@Override
	public void setTag(Object tag) {
		// TODO Auto-generated method stub
		super.setTag(tag);
	}

	@Override
	public Object getTag(int key) {
		// TODO Auto-generated method stub
		return super.getTag(key);
	}

	@Override
	public void setTag(int key, Object tag) {
		// TODO Auto-generated method stub
		super.setTag(key, tag);
	}

	@Override
	public int getBaseline() {
		// TODO Auto-generated method stub
		return super.getBaseline();
	}

	@Override
	public void requestLayout() {
		// TODO Auto-generated method stub
		super.requestLayout();
	}

	@Override
	public void forceLayout() {
		// TODO Auto-generated method stub
		super.forceLayout();
	}

	@Override
	protected int getSuggestedMinimumHeight() {
		// TODO Auto-generated method stub
		return super.getSuggestedMinimumHeight();
	}

	@Override
	protected int getSuggestedMinimumWidth() {
		// TODO Auto-generated method stub
		return super.getSuggestedMinimumWidth();
	}

	@Override
	public void setMinimumHeight(int minHeight) {
		// TODO Auto-generated method stub
		super.setMinimumHeight(minHeight);
	}

	@Override
	public void setMinimumWidth(int minWidth) {
		// TODO Auto-generated method stub
		super.setMinimumWidth(minWidth);
	}

	@Override
	public Animation getAnimation() {
		// TODO Auto-generated method stub
		return super.getAnimation();
	}

	@Override
	public void startAnimation(Animation animation) {
		// TODO Auto-generated method stub
		super.startAnimation(animation);
	}

	@Override
	public void clearAnimation() {
		// TODO Auto-generated method stub
		super.clearAnimation();
	}

	@Override
	public void setAnimation(Animation animation) {
		// TODO Auto-generated method stub
		super.setAnimation(animation);
	}

	@Override
	protected void onAnimationStart() {
		// TODO Auto-generated method stub
		super.onAnimationStart();
	}

	@Override
	protected void onAnimationEnd() {
		// TODO Auto-generated method stub
		super.onAnimationEnd();
	}

	@Override
	protected boolean onSetAlpha(int alpha) {
		// TODO Auto-generated method stub
		return super.onSetAlpha(alpha);
	}

	@Override
	public void playSoundEffect(int soundConstant) {
		// TODO Auto-generated method stub
		super.playSoundEffect(soundConstant);
	}

	@Override
	public boolean performHapticFeedback(int feedbackConstant) {
		// TODO Auto-generated method stub
		return super.performHapticFeedback(feedbackConstant);
	}

	@Override
	public boolean performHapticFeedback(int feedbackConstant, int flags) {
		// TODO Auto-generated method stub
		return super.performHapticFeedback(feedbackConstant, flags);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (isSquare) {
			int mScreenWidth = MeasureSpec.getSize(widthMeasureSpec);
			int mScreenHeight = MeasureSpec.getSize(heightMeasureSpec);
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			if(mScreenWidth<mScreenHeight){
				setMeasuredDimension(mScreenWidth, mScreenWidth);
			}else{
				setMeasuredDimension(mScreenHeight, mScreenHeight);
			}
		} else {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		if (w != oldw || h != oldh) {
			Log.d(TAG, "onSizeChanged w " + w + " h" + h);
			initDraw(this.getWidth(), this.getHeight());
			this.initValue(ITEM_TYPE_CENTER);
			this.initValue(ITEM_TYPE_CIRCLE);
		}
		super.onSizeChanged(w, h, oldw, oldh);
	}

	/**
	 * 根据抬起时的角度，获取可能的ChartProp
	 * 
	 * @param 抬起时的角度
	 * @return 可能的charProp。因为还要判断是不是在圆内。
	 */
	private int getPosibleChartProp(double angle) {
		int size = this.mChileLs.size();
		if (angle < 0) {
			angle += 360;
		}
		int ch = 0;
		for (int i = 0; i < size; i++) {
			ch += mChileLs.get(i).value;
			if (angle <= ch) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 获取当抬起时，坐标所在的charProp
	 * 
	 * @param x
	 *            action_up's x up时的x坐标
	 * @param y
	 *            action_up's y up时的y坐标
	 * @return 如果返回值为null，说明不在任何的扇形内。
	 */
	private int getUpChartProp(float x, float y) {
		double angle = Math.atan2(y - mCenterPoint.y, x - mCenterPoint.x) * 180
				/ Math.PI;
		if (angle < 0) {
			angle = 360 + angle;
		}
		Log.d("test", "up angle = " + angle);

		return getPosibleChartProp(angle);
	}

	private int getCenterIndex(float y) {
		int size = mChileCenterLs.size();
		int ch = (int) rf5.height();
		// float percent=(float)mCenterBtnH/mCenterBtnWeightCount;
		for (int i = 0; i < size; i++) {
			ch -= mChileCenterLs.get(i).value;// mChileCenterLs.get(i).value*percent;
			if (y >= ch) {
				return i;
			}
		}
		return -1;
	}

	private double getDistance(int x, int y, float x1, float y1) {
		return Math.sqrt(Math.pow(Math.abs(x - x1), 2)
				+ Math.pow(Math.abs(y - y1), 2));
	}

	public void setOnItemClickListener(
			OnItemClickListener listener) {
		this.mItemClickListener = listener;
	}

	public ItemButton getCircleButton(int location) {
		if (location < 0 || location >= mChileLs.size()) {
			return null;
		}
		return mChileLs.get(location);
	}

	public ItemButton getCenterButton(int location) {
		if (location < 0 || location >= mChileCenterLs.size()) {
			return null;
		}
		return mChileCenterLs.get(location);
	}

	public void addCircleButton(ItemButton chile) {
		mChileLs.add(chile);
		this.mBtnWeightCount += chile.weight;
		initValue(ChartButton.ITEM_TYPE_CIRCLE);
	}

	public void addCenterButton(ItemButton chile) {
		mChileCenterLs.add(chile);
		this.mCenterBtnWeightCount += chile.weight;
		initValue(ChartButton.ITEM_TYPE_CENTER);
	}

	private void initValue(int type) {
		float percent = 0;
		ItemButton chile;
		if (type == ChartButton.ITEM_TYPE_CIRCLE) {
			int size = mChileLs.size();
			if (size > 1) {
				percent = ((360 - size * division) / mBtnWeightCount);
				for (--size; size >= 0; size--) {
					chile = mChileLs.get(size);
					chile.value = percent * chile.weight;
				}
			} else if (size == 1) {
				mChileLs.get(0).value = 360;
			}
		} else if (type == ChartButton.ITEM_TYPE_CENTER) {
			if (mCenterUserWeightCount <= 0) {
				percent = rf5.height() / mCenterUserWeightCount;
			} else {
				percent = rf5.height() / mCenterBtnWeightCount;
			}
			for (int i = mChileCenterLs.size() - 1; i >= 0; i--) {
				chile = mChileCenterLs.get(i);
				chile.value = percent * chile.weight;
			}
		}
	}

	private void setTextSize(float textSize) {
		mPaint.setTextSize(textSize);
	}

	public float getmCenterUserWeightCount() {
		return mCenterUserWeightCount;
	}

	public void setmCenterUserWeightCount(float mCenterUserWeightCount) {
		this.mCenterUserWeightCount = mCenterUserWeightCount;
	}

	public void clear() {
		this.mBtnWeightCount = 0;
		this.mCenterBtnWeightCount = 0;
		this.mChileCenterLs.clear();
		this.mChileLs.clear();
	}
	
	/**{@link #ITEM_TYPE_CIRCLE },{@link #ITEM_TYPE_CENTER}
	 * @param position
	 * @param flag
	 * @return
	 */
	public ItemButton getItem(int position,int flag){
	    if(flag==ITEM_TYPE_CIRCLE){
	        if(position>=0&&position<mChileLs.size()){
	            return mChileLs.get(position);
	        }
	    }else if(flag==ITEM_TYPE_CENTER){
	        if(position>=0&&position<mChileCenterLs.size()){
                return mChileCenterLs.get(position);
            }
	    }
	    return null;
	}
	
	public interface OnItemClickListener {

        /**
         * Callback method to be invoked when an item in this AdapterView has
         * been clicked.
         * <p>
         * Implementers can call getItemAtPosition(position) if they need
         * to access the data associated with the selected item.
         *
         * @param parent The AdapterView where the click happened.
         * @param view The view within the AdapterView that was clicked (this
         *            will be a view provided by the adapter)
         * @param position The position of the view in the adapter.
         * @param id The row id of the item that was clicked.
         */
        void onItemClick(ChartButton parent, ItemButton child, int position, int flag);
    }

}
