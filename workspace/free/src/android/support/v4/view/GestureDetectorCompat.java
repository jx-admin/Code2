package android.support.v4.view;

import android.content.Context;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;

public class GestureDetectorCompat
{
  private final GestureDetectorCompatImpl mImpl;

  public GestureDetectorCompat(Context paramContext, GestureDetector.OnGestureListener paramOnGestureListener)
  {
    this(paramContext, paramOnGestureListener, null);
  }

  public GestureDetectorCompat(Context paramContext, GestureDetector.OnGestureListener paramOnGestureListener, Handler paramHandler)
  {
    if (Build.VERSION.SDK_INT >= 17);
    for (this.mImpl = new GestureDetectorCompatImplJellybeanMr1(paramContext, paramOnGestureListener, paramHandler); ; this.mImpl = new GestureDetectorCompatImplBase(paramContext, paramOnGestureListener, paramHandler))
      return;
  }

  public boolean isLongpressEnabled()
  {
    return this.mImpl.isLongpressEnabled();
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    return this.mImpl.onTouchEvent(paramMotionEvent);
  }

  public void setIsLongpressEnabled(boolean paramBoolean)
  {
    this.mImpl.setIsLongpressEnabled(paramBoolean);
  }

  public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener paramOnDoubleTapListener)
  {
    this.mImpl.setOnDoubleTapListener(paramOnDoubleTapListener);
  }

  static abstract interface GestureDetectorCompatImpl
  {
    public abstract boolean isLongpressEnabled();

    public abstract boolean onTouchEvent(MotionEvent paramMotionEvent);

    public abstract void setIsLongpressEnabled(boolean paramBoolean);

    public abstract void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener paramOnDoubleTapListener);
  }

  static class GestureDetectorCompatImplBase
    implements GestureDetectorCompat.GestureDetectorCompatImpl
  {
    private static final int DOUBLE_TAP_TIMEOUT = 0;
    private static final int LONGPRESS_TIMEOUT = 0;
    private static final int LONG_PRESS = 2;
    private static final int SHOW_PRESS = 1;
    private static final int TAP = 3;
    private static final int TAP_TIMEOUT = ViewConfiguration.getTapTimeout();
    private boolean mAlwaysInBiggerTapRegion;
    private boolean mAlwaysInTapRegion;
    private MotionEvent mCurrentDownEvent;
    private GestureDetector.OnDoubleTapListener mDoubleTapListener;
    private int mDoubleTapSlopSquare;
    private float mDownFocusX;
    private float mDownFocusY;
    private final Handler mHandler;
    private boolean mInLongPress;
    private boolean mIsDoubleTapping;
    private boolean mIsLongpressEnabled;
    private float mLastFocusX;
    private float mLastFocusY;
    private final GestureDetector.OnGestureListener mListener;
    private int mMaximumFlingVelocity;
    private int mMinimumFlingVelocity;
    private MotionEvent mPreviousUpEvent;
    private boolean mStillDown;
    private int mTouchSlopSquare;
    private VelocityTracker mVelocityTracker;

    static
    {
      DOUBLE_TAP_TIMEOUT = ViewConfiguration.getDoubleTapTimeout();
    }

    public GestureDetectorCompatImplBase(Context paramContext, GestureDetector.OnGestureListener paramOnGestureListener, Handler paramHandler)
    {
      if (paramHandler != null);
      for (this.mHandler = new GestureHandler(paramHandler); ; this.mHandler = new GestureHandler())
      {
        this.mListener = paramOnGestureListener;
        if (paramOnGestureListener instanceof GestureDetector.OnDoubleTapListener)
          setOnDoubleTapListener((GestureDetector.OnDoubleTapListener)paramOnGestureListener);
        init(paramContext);
        return;
      }
    }

    private void cancel()
    {
      this.mHandler.removeMessages(1);
      this.mHandler.removeMessages(2);
      this.mHandler.removeMessages(3);
      this.mVelocityTracker.recycle();
      this.mVelocityTracker = null;
      this.mIsDoubleTapping = false;
      this.mStillDown = false;
      this.mAlwaysInTapRegion = false;
      this.mAlwaysInBiggerTapRegion = false;
      if (!this.mInLongPress)
        return;
      this.mInLongPress = false;
    }

    private void cancelTaps()
    {
      this.mHandler.removeMessages(1);
      this.mHandler.removeMessages(2);
      this.mHandler.removeMessages(3);
      this.mIsDoubleTapping = false;
      this.mAlwaysInTapRegion = false;
      this.mAlwaysInBiggerTapRegion = false;
      if (!this.mInLongPress)
        return;
      this.mInLongPress = false;
    }

    private void dispatchLongPress()
    {
      this.mHandler.removeMessages(3);
      this.mInLongPress = true;
      this.mListener.onLongPress(this.mCurrentDownEvent);
    }

    private void init(Context paramContext)
    {
      if (paramContext == null)
        throw new IllegalArgumentException("Context must not be null");
      if (this.mListener == null)
        throw new IllegalArgumentException("OnGestureListener must not be null");
      this.mIsLongpressEnabled = true;
      ViewConfiguration localViewConfiguration = ViewConfiguration.get(paramContext);
      int i = localViewConfiguration.getScaledTouchSlop();
      int j = localViewConfiguration.getScaledDoubleTapSlop();
      this.mMinimumFlingVelocity = localViewConfiguration.getScaledMinimumFlingVelocity();
      this.mMaximumFlingVelocity = localViewConfiguration.getScaledMaximumFlingVelocity();
      this.mTouchSlopSquare = (i * i);
      this.mDoubleTapSlopSquare = (j * j);
    }

    private boolean isConsideredDoubleTap(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2, MotionEvent paramMotionEvent3)
    {
      int i = 0;
      if (!this.mAlwaysInBiggerTapRegion);
      while (true)
      {
        return i;
        if (paramMotionEvent3.getEventTime() - paramMotionEvent2.getEventTime() > DOUBLE_TAP_TIMEOUT)
          continue;
        int j = (int)paramMotionEvent1.getX() - (int)paramMotionEvent3.getX();
        int k = (int)paramMotionEvent1.getY() - (int)paramMotionEvent3.getY();
        if (j * j + k * k >= this.mDoubleTapSlopSquare)
          continue;
        i = 1;
      }
    }

    public boolean isLongpressEnabled()
    {
      return this.mIsLongpressEnabled;
    }

    public boolean onTouchEvent(MotionEvent paramMotionEvent)
    {
      int i = paramMotionEvent.getAction();
      if (this.mVelocityTracker == null)
        this.mVelocityTracker = VelocityTracker.obtain();
      this.mVelocityTracker.addMovement(paramMotionEvent);
      int j;
      label39: int k;
      label49: float f1;
      float f2;
      int l;
      int i1;
      if ((i & 0xFF) == 6)
      {
        j = 1;
        if (j == 0)
          break label89;
        k = MotionEventCompat.getActionIndex(paramMotionEvent);
        f1 = 0.0F;
        f2 = 0.0F;
        l = MotionEventCompat.getPointerCount(paramMotionEvent);
        i1 = 0;
        label64: if (i1 >= l)
          break label121;
        if (k != i1)
          break label96;
      }
      while (true)
      {
        ++i1;
        break label64:
        j = 0;
        break label39:
        label89: k = -1;
        break label49:
        label96: f1 += MotionEventCompat.getX(paramMotionEvent, i1);
        f2 += MotionEventCompat.getY(paramMotionEvent, i1);
      }
      label121: int i2;
      label131: float f3;
      float f4;
      boolean bool1;
      if (j != 0)
      {
        i2 = l - 1;
        f3 = f1 / i2;
        f4 = f2 / i2;
        bool1 = false;
        switch (i & 0xFF)
        {
        case 4:
        default:
        case 5:
        case 6:
        case 0:
        case 2:
        case 1:
        case 3:
        }
      }
      while (true)
      {
        label196: return bool1;
        i2 = l;
        break label131:
        this.mLastFocusX = f3;
        this.mDownFocusX = f3;
        this.mLastFocusY = f4;
        this.mDownFocusY = f4;
        cancelTaps();
        continue;
        this.mLastFocusX = f3;
        this.mDownFocusX = f3;
        this.mLastFocusY = f4;
        this.mDownFocusY = f4;
        this.mVelocityTracker.computeCurrentVelocity(1000, this.mMaximumFlingVelocity);
        int i7 = MotionEventCompat.getActionIndex(paramMotionEvent);
        int i8 = MotionEventCompat.getPointerId(paramMotionEvent, i7);
        float f9 = VelocityTrackerCompat.getXVelocity(this.mVelocityTracker, i8);
        float f10 = VelocityTrackerCompat.getYVelocity(this.mVelocityTracker, i8);
        int i9 = 0;
        label315: if (i9 >= l)
          continue;
        if (i9 == i7);
        int i10;
        do
        {
          ++i9;
          break label315:
          i10 = MotionEventCompat.getPointerId(paramMotionEvent, i9);
        }
        while (f9 * VelocityTrackerCompat.getXVelocity(this.mVelocityTracker, i10) + f10 * VelocityTrackerCompat.getYVelocity(this.mVelocityTracker, i10) >= 0.0F);
        this.mVelocityTracker.clear();
        continue;
        if (this.mDoubleTapListener != null)
        {
          boolean bool2 = this.mHandler.hasMessages(3);
          if (bool2)
            this.mHandler.removeMessages(3);
          if ((this.mCurrentDownEvent == null) || (this.mPreviousUpEvent == null) || (!bool2) || (!isConsideredDoubleTap(this.mCurrentDownEvent, this.mPreviousUpEvent, paramMotionEvent)))
            break label627;
          this.mIsDoubleTapping = true;
          bool1 = false | this.mDoubleTapListener.onDoubleTap(this.mCurrentDownEvent) | this.mDoubleTapListener.onDoubleTapEvent(paramMotionEvent);
        }
        while (true)
        {
          this.mLastFocusX = f3;
          this.mDownFocusX = f3;
          this.mLastFocusY = f4;
          this.mDownFocusY = f4;
          if (this.mCurrentDownEvent != null)
            this.mCurrentDownEvent.recycle();
          this.mCurrentDownEvent = MotionEvent.obtain(paramMotionEvent);
          this.mAlwaysInTapRegion = true;
          this.mAlwaysInBiggerTapRegion = true;
          this.mStillDown = true;
          this.mInLongPress = false;
          if (this.mIsLongpressEnabled)
          {
            this.mHandler.removeMessages(2);
            this.mHandler.sendEmptyMessageAtTime(2, this.mCurrentDownEvent.getDownTime() + TAP_TIMEOUT + LONGPRESS_TIMEOUT);
          }
          this.mHandler.sendEmptyMessageAtTime(1, this.mCurrentDownEvent.getDownTime() + TAP_TIMEOUT);
          bool1 |= this.mListener.onDown(paramMotionEvent);
          break label196:
          label627: this.mHandler.sendEmptyMessageDelayed(3, DOUBLE_TAP_TIMEOUT);
        }
        if (this.mInLongPress)
          continue;
        float f7 = this.mLastFocusX - f3;
        float f8 = this.mLastFocusY - f4;
        if (this.mIsDoubleTapping)
          bool1 = false | this.mDoubleTapListener.onDoubleTapEvent(paramMotionEvent);
        if (this.mAlwaysInTapRegion)
        {
          int i4 = (int)(f3 - this.mDownFocusX);
          int i5 = (int)(f4 - this.mDownFocusY);
          int i6 = i4 * i4 + i5 * i5;
          if (i6 > this.mTouchSlopSquare)
          {
            bool1 = this.mListener.onScroll(this.mCurrentDownEvent, paramMotionEvent, f7, f8);
            this.mLastFocusX = f3;
            this.mLastFocusY = f4;
            this.mAlwaysInTapRegion = false;
            this.mHandler.removeMessages(3);
            this.mHandler.removeMessages(1);
            this.mHandler.removeMessages(2);
          }
          if (i6 <= this.mTouchSlopSquare)
            continue;
          this.mAlwaysInBiggerTapRegion = false;
        }
        if ((Math.abs(f7) < 1.0F) && (Math.abs(f8) < 1.0F))
          continue;
        bool1 = this.mListener.onScroll(this.mCurrentDownEvent, paramMotionEvent, f7, f8);
        this.mLastFocusX = f3;
        this.mLastFocusY = f4;
        continue;
        this.mStillDown = false;
        MotionEvent localMotionEvent = MotionEvent.obtain(paramMotionEvent);
        if (this.mIsDoubleTapping);
        float f5;
        float f6;
        for (bool1 = false | this.mDoubleTapListener.onDoubleTapEvent(paramMotionEvent); ; bool1 = this.mListener.onFling(this.mCurrentDownEvent, paramMotionEvent, f6, f5))
          do
          {
            while (true)
            {
              if (this.mPreviousUpEvent != null)
                this.mPreviousUpEvent.recycle();
              this.mPreviousUpEvent = localMotionEvent;
              if (this.mVelocityTracker != null)
              {
                this.mVelocityTracker.recycle();
                this.mVelocityTracker = null;
              }
              this.mIsDoubleTapping = false;
              this.mHandler.removeMessages(1);
              this.mHandler.removeMessages(2);
              break label196:
              if (this.mInLongPress)
              {
                this.mHandler.removeMessages(3);
                this.mInLongPress = false;
              }
              if (!this.mAlwaysInTapRegion)
                break;
              bool1 = this.mListener.onSingleTapUp(paramMotionEvent);
            }
            VelocityTracker localVelocityTracker = this.mVelocityTracker;
            int i3 = MotionEventCompat.getPointerId(paramMotionEvent, 0);
            localVelocityTracker.computeCurrentVelocity(1000, this.mMaximumFlingVelocity);
            f5 = VelocityTrackerCompat.getYVelocity(localVelocityTracker, i3);
            f6 = VelocityTrackerCompat.getXVelocity(localVelocityTracker, i3);
          }
          while ((Math.abs(f5) <= this.mMinimumFlingVelocity) && (Math.abs(f6) <= this.mMinimumFlingVelocity));
        cancel();
      }
    }

    public void setIsLongpressEnabled(boolean paramBoolean)
    {
      this.mIsLongpressEnabled = paramBoolean;
    }

    public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener paramOnDoubleTapListener)
    {
      this.mDoubleTapListener = paramOnDoubleTapListener;
    }

    private class GestureHandler extends Handler
    {
      GestureHandler()
      {
      }

      GestureHandler(Handler arg2)
      {
        super(localObject.getLooper());
      }

      public void handleMessage(Message paramMessage)
      {
        switch (paramMessage.what)
        {
        default:
          throw new RuntimeException("Unknown message " + paramMessage);
        case 1:
          GestureDetectorCompat.GestureDetectorCompatImplBase.this.mListener.onShowPress(GestureDetectorCompat.GestureDetectorCompatImplBase.this.mCurrentDownEvent);
        case 2:
        case 3:
        }
        while (true)
        {
          return;
          GestureDetectorCompat.GestureDetectorCompatImplBase.this.dispatchLongPress();
          continue;
          if ((GestureDetectorCompat.GestureDetectorCompatImplBase.this.mDoubleTapListener == null) || (GestureDetectorCompat.GestureDetectorCompatImplBase.this.mStillDown))
            continue;
          GestureDetectorCompat.GestureDetectorCompatImplBase.this.mDoubleTapListener.onSingleTapConfirmed(GestureDetectorCompat.GestureDetectorCompatImplBase.this.mCurrentDownEvent);
        }
      }
    }
  }

  static class GestureDetectorCompatImplJellybeanMr1
    implements GestureDetectorCompat.GestureDetectorCompatImpl
  {
    private final GestureDetector mDetector;

    public GestureDetectorCompatImplJellybeanMr1(Context paramContext, GestureDetector.OnGestureListener paramOnGestureListener, Handler paramHandler)
    {
      this.mDetector = new GestureDetector(paramContext, paramOnGestureListener, paramHandler);
    }

    public boolean isLongpressEnabled()
    {
      return this.mDetector.isLongpressEnabled();
    }

    public boolean onTouchEvent(MotionEvent paramMotionEvent)
    {
      return this.mDetector.onTouchEvent(paramMotionEvent);
    }

    public void setIsLongpressEnabled(boolean paramBoolean)
    {
      this.mDetector.setIsLongpressEnabled(paramBoolean);
    }

    public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener paramOnDoubleTapListener)
    {
      this.mDetector.setOnDoubleTapListener(paramOnDoubleTapListener);
    }
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     android.support.v4.view.GestureDetectorCompat
 * JD-Core Version:    0.5.4
 */