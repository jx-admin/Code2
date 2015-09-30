package android.support.v4.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.SystemClock;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.widget.EdgeEffectCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.util.Log;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.Interpolator;
import android.widget.Scroller;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ViewPager extends ViewGroup
{
  private static final int CLOSE_ENOUGH = 2;
  private static final Comparator<ItemInfo> COMPARATOR;
  private static final boolean DEBUG = false;
  private static final int DEFAULT_GUTTER_SIZE = 16;
  private static final int DEFAULT_OFFSCREEN_PAGES = 1;
  private static final int DRAW_ORDER_DEFAULT = 0;
  private static final int DRAW_ORDER_FORWARD = 1;
  private static final int DRAW_ORDER_REVERSE = 2;
  private static final int INVALID_POINTER = -1;
  private static final int[] LAYOUT_ATTRS;
  private static final int MAX_SETTLE_DURATION = 600;
  private static final int MIN_DISTANCE_FOR_FLING = 25;
  public static final int SCROLL_STATE_DRAGGING = 1;
  public static final int SCROLL_STATE_IDLE = 0;
  public static final int SCROLL_STATE_SETTLING = 2;
  private static final String TAG = "ViewPager";
  private static final boolean USE_CACHE;
  private static final Interpolator sInterpolator;
  private static final ViewPositionComparator sPositionComparator;
  private int mActivePointerId = -1;
  private PagerAdapter mAdapter;
  private OnAdapterChangeListener mAdapterChangeListener;
  private int mBottomPageBounds;
  private boolean mCalledSuper;
  private int mChildHeightMeasureSpec;
  private int mChildWidthMeasureSpec;
  private int mCloseEnough;
  private int mCurItem;
  private int mDecorChildCount;
  private int mDefaultGutterSize;
  private int mDrawingOrder;
  private ArrayList<View> mDrawingOrderedChildren;
  private final Runnable mEndScrollRunnable = new Runnable()
  {
    public void run()
    {
      ViewPager.this.setScrollState(0);
      ViewPager.this.populate();
    }
  };
  private long mFakeDragBeginTime;
  private boolean mFakeDragging;
  private boolean mFirstLayout = true;
  private float mFirstOffset = -3.402824E+038F;
  private int mFlingDistance;
  private int mGutterSize;
  private boolean mIgnoreGutter;
  private boolean mInLayout;
  private float mInitialMotionX;
  private OnPageChangeListener mInternalPageChangeListener;
  private boolean mIsBeingDragged;
  private boolean mIsUnableToDrag;
  private final ArrayList<ItemInfo> mItems = new ArrayList();
  private float mLastMotionX;
  private float mLastMotionY;
  private float mLastOffset = 3.4028235E+38F;
  private EdgeEffectCompat mLeftEdge;
  private Drawable mMarginDrawable;
  private int mMaximumVelocity;
  private int mMinimumVelocity;
  private boolean mNeedCalculatePageOffsets = false;
  private PagerObserver mObserver;
  private int mOffscreenPageLimit = 1;
  private OnPageChangeListener mOnPageChangeListener;
  private int mPageMargin;
  private PageTransformer mPageTransformer;
  private boolean mPopulatePending;
  private Parcelable mRestoredAdapterState = null;
  private ClassLoader mRestoredClassLoader = null;
  private int mRestoredCurItem = -1;
  private EdgeEffectCompat mRightEdge;
  private int mScrollState = 0;
  private Scroller mScroller;
  private boolean mScrollingCacheEnabled;
  private int mSeenPositionMax;
  private int mSeenPositionMin;
  private Method mSetChildrenDrawingOrderEnabled;
  private final ItemInfo mTempItem = new ItemInfo();
  private final Rect mTempRect = new Rect();
  private int mTopPageBounds;
  private int mTouchSlop;
  private VelocityTracker mVelocityTracker;

  static
  {
    int[] arrayOfInt = new int[1];
    arrayOfInt[0] = 16842931;
    LAYOUT_ATTRS = arrayOfInt;
    COMPARATOR = new Comparator()
    {
      public int compare(ViewPager.ItemInfo paramItemInfo1, ViewPager.ItemInfo paramItemInfo2)
      {
        return paramItemInfo1.position - paramItemInfo2.position;
      }
    };
    sInterpolator = new Interpolator()
    {
      public float getInterpolation(float paramFloat)
      {
        float f = paramFloat - 1.0F;
        return 1.0F + f * (f * (f * (f * f)));
      }
    };
    sPositionComparator = new ViewPositionComparator();
  }

  public ViewPager(Context paramContext)
  {
    super(paramContext);
    initViewPager();
  }

  public ViewPager(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    initViewPager();
  }

  private void calculatePageOffsets(ItemInfo paramItemInfo1, int paramInt, ItemInfo paramItemInfo2)
  {
    int i = this.mAdapter.getCount();
    int j = getWidth();
    float f1;
    label31: int i6;
    int i9;
    float f7;
    if (j > 0)
    {
      f1 = this.mPageMargin / j;
      if (paramItemInfo2 == null)
        break label373;
      i6 = paramItemInfo2.position;
      if (i6 >= paramItemInfo1.position)
        break label218;
      i9 = 0;
      f7 = f1 + (paramItemInfo2.offset + paramItemInfo2.widthFactor);
    }
    for (int i10 = i6 + 1; ; ++i10)
    {
      if ((i10 > paramItemInfo1.position) || (i9 >= this.mItems.size()))
        break label373;
      for (ItemInfo localItemInfo4 = (ItemInfo)this.mItems.get(i9); ; localItemInfo4 = (ItemInfo)this.mItems.get(++i9))
        if ((i10 <= localItemInfo4.position) || (i9 >= -1 + this.mItems.size()))
          break label159;
      f1 = 0.0F;
      break label31:
      while (i10 < localItemInfo4.position)
      {
        label159: f7 += f1 + this.mAdapter.getPageWidth(i10);
        ++i10;
      }
      localItemInfo4.offset = f7;
      f7 += f1 + localItemInfo4.widthFactor;
    }
    if (i6 > paramItemInfo1.position)
    {
      label218: int i7 = -1 + this.mItems.size();
      float f6 = paramItemInfo2.offset;
      for (int i8 = i6 - 1; (i8 >= paramItemInfo1.position) && (i7 >= 0); --i8)
      {
        for (ItemInfo localItemInfo3 = (ItemInfo)this.mItems.get(i7); (i8 < localItemInfo3.position) && (i7 > 0); localItemInfo3 = (ItemInfo)this.mItems.get(--i7));
        while (i8 > localItemInfo3.position)
        {
          f6 -= f1 + this.mAdapter.getPageWidth(i8);
          --i8;
        }
        f6 -= f1 + localItemInfo3.widthFactor;
        localItemInfo3.offset = f6;
      }
    }
    label373: int k = this.mItems.size();
    float f2 = paramItemInfo1.offset;
    int l = -1 + paramItemInfo1.position;
    float f3;
    label410: float f4;
    label440: int i1;
    if (paramItemInfo1.position == 0)
    {
      f3 = paramItemInfo1.offset;
      this.mFirstOffset = f3;
      if (paramItemInfo1.position != i - 1)
        break label521;
      f4 = paramItemInfo1.offset + paramItemInfo1.widthFactor - 1.0F;
      this.mLastOffset = f4;
      i1 = paramInt - 1;
    }
    while (i1 >= 0)
    {
      ItemInfo localItemInfo2 = (ItemInfo)this.mItems.get(i1);
      while (true)
      {
        if (l <= localItemInfo2.position)
          break label528;
        PagerAdapter localPagerAdapter2 = this.mAdapter;
        int i5 = l - 1;
        f2 -= f1 + localPagerAdapter2.getPageWidth(l);
        l = i5;
      }
      f3 = -3.402824E+038F;
      break label410:
      label521: f4 = 3.4028235E+38F;
      break label440:
      label528: f2 -= f1 + localItemInfo2.widthFactor;
      localItemInfo2.offset = f2;
      if (localItemInfo2.position == 0)
        this.mFirstOffset = f2;
      --i1;
      --l;
    }
    float f5 = f1 + (paramItemInfo1.offset + paramItemInfo1.widthFactor);
    int i2 = 1 + paramItemInfo1.position;
    int i3 = paramInt + 1;
    while (i3 < k)
    {
      ItemInfo localItemInfo1 = (ItemInfo)this.mItems.get(i3);
      while (i2 < localItemInfo1.position)
      {
        PagerAdapter localPagerAdapter1 = this.mAdapter;
        int i4 = i2 + 1;
        f5 += f1 + localPagerAdapter1.getPageWidth(i2);
        i2 = i4;
      }
      if (localItemInfo1.position == i - 1)
        this.mLastOffset = (f5 + localItemInfo1.widthFactor - 1.0F);
      localItemInfo1.offset = f5;
      f5 += f1 + localItemInfo1.widthFactor;
      ++i3;
      ++i2;
    }
    this.mNeedCalculatePageOffsets = false;
  }

  private void completeScroll(boolean paramBoolean)
  {
    if (this.mScrollState == 2);
    for (int i = 1; ; i = 0)
    {
      if (i != 0)
      {
        setScrollingCacheEnabled(false);
        this.mScroller.abortAnimation();
        int k = getScrollX();
        int l = getScrollY();
        int i1 = this.mScroller.getCurrX();
        int i2 = this.mScroller.getCurrY();
        if ((k != i1) || (l != i2))
          scrollTo(i1, i2);
      }
      this.mPopulatePending = false;
      for (int j = 0; ; ++j)
      {
        if (j >= this.mItems.size())
          break label136;
        ItemInfo localItemInfo = (ItemInfo)this.mItems.get(j);
        if (!localItemInfo.scrolling)
          continue;
        i = 1;
        localItemInfo.scrolling = false;
      }
    }
    if (i != 0)
    {
      label136: if (!paramBoolean)
        break label153;
      ViewCompat.postOnAnimation(this, this.mEndScrollRunnable);
    }
    while (true)
    {
      return;
      label153: this.mEndScrollRunnable.run();
    }
  }

  private int determineTargetPage(int paramInt1, float paramFloat, int paramInt2, int paramInt3)
  {
    if ((Math.abs(paramInt3) > this.mFlingDistance) && (Math.abs(paramInt2) > this.mMinimumVelocity))
      if (paramInt2 <= 0);
    for (int i = paramInt1; ; i = (int)(0.5F + (paramFloat + paramInt1)))
      while (true)
      {
        if (this.mItems.size() > 0)
        {
          ItemInfo localItemInfo1 = (ItemInfo)this.mItems.get(0);
          ItemInfo localItemInfo2 = (ItemInfo)this.mItems.get(-1 + this.mItems.size());
          i = Math.max(localItemInfo1.position, Math.min(i, localItemInfo2.position));
        }
        return i;
        i = paramInt1 + 1;
        continue;
        if ((this.mSeenPositionMin >= 0) && (this.mSeenPositionMin < paramInt1) && (paramFloat < 0.5F))
          i = paramInt1 + 1;
        if ((this.mSeenPositionMax < 0) || (this.mSeenPositionMax <= paramInt1 + 1) || (paramFloat < 0.5F))
          break;
        i = paramInt1 - 1;
      }
  }

  private void enableLayers(boolean paramBoolean)
  {
    int i = getChildCount();
    int j = 0;
    if (j >= i)
      label7: return;
    if (paramBoolean);
    for (int k = 2; ; k = 0)
    {
      ViewCompat.setLayerType(getChildAt(j), k, null);
      ++j;
      break label7:
    }
  }

  private void endDrag()
  {
    this.mIsBeingDragged = false;
    this.mIsUnableToDrag = false;
    if (this.mVelocityTracker == null)
      return;
    this.mVelocityTracker.recycle();
    this.mVelocityTracker = null;
  }

  private Rect getChildRectInPagerCoordinates(Rect paramRect, View paramView)
  {
    if (paramRect == null)
      paramRect = new Rect();
    if (paramView == null)
    {
      paramRect.set(0, 0, 0, 0);
      return paramRect;
    }
    paramRect.left = paramView.getLeft();
    paramRect.right = paramView.getRight();
    paramRect.top = paramView.getTop();
    paramRect.bottom = paramView.getBottom();
    ViewGroup localViewGroup;
    for (ViewParent localViewParent = paramView.getParent(); ; localViewParent = localViewGroup.getParent())
    {
      if ((localViewParent instanceof ViewGroup) && (localViewParent != this));
      localViewGroup = (ViewGroup)localViewParent;
      paramRect.left += localViewGroup.getLeft();
      paramRect.right += localViewGroup.getRight();
      paramRect.top += localViewGroup.getTop();
      paramRect.bottom += localViewGroup.getBottom();
    }
  }

  private ItemInfo infoForCurrentScrollPosition()
  {
    float f1 = 0.0F;
    int i = getWidth();
    float f2;
    label20: int j;
    float f3;
    float f4;
    int k;
    Object localObject;
    if (i > 0)
    {
      f2 = getScrollX() / i;
      if (i > 0)
        f1 = this.mPageMargin / i;
      j = -1;
      f3 = 0.0F;
      f4 = 0.0F;
      k = 1;
      localObject = null;
    }
    for (int l = 0; ; ++l)
    {
      ItemInfo localItemInfo;
      float f5;
      if (l < this.mItems.size())
      {
        localItemInfo = (ItemInfo)this.mItems.get(l);
        if ((k == 0) && (localItemInfo.position != j + 1))
        {
          localItemInfo = this.mTempItem;
          localItemInfo.offset = (f1 + (f3 + f4));
          localItemInfo.position = (j + 1);
          localItemInfo.widthFactor = this.mAdapter.getPageWidth(localItemInfo.position);
          --l;
        }
        f5 = localItemInfo.offset;
        float f6 = f1 + (f5 + localItemInfo.widthFactor);
        if ((k != 0) || (f2 >= f5))
        {
          if ((f2 >= f6) && (l != -1 + this.mItems.size()))
            break label207;
          localObject = localItemInfo;
        }
      }
      return localObject;
      f2 = 0.0F;
      break label20:
      label207: k = 0;
      j = localItemInfo.position;
      f3 = f5;
      f4 = localItemInfo.widthFactor;
      localObject = localItemInfo;
    }
  }

  private boolean isGutterDrag(float paramFloat1, float paramFloat2)
  {
    if (((paramFloat1 < this.mGutterSize) && (paramFloat2 > 0.0F)) || ((paramFloat1 > getWidth() - this.mGutterSize) && (paramFloat2 < 0.0F)));
    for (int i = 1; ; i = 0)
      return i;
  }

  private void onSecondaryPointerUp(MotionEvent paramMotionEvent)
  {
    int i = MotionEventCompat.getActionIndex(paramMotionEvent);
    if (MotionEventCompat.getPointerId(paramMotionEvent, i) == this.mActivePointerId)
      if (i != 0)
        break label56;
    for (int j = 1; ; j = 0)
    {
      this.mLastMotionX = MotionEventCompat.getX(paramMotionEvent, j);
      this.mActivePointerId = MotionEventCompat.getPointerId(paramMotionEvent, j);
      if (this.mVelocityTracker != null)
        this.mVelocityTracker.clear();
      label56: return;
    }
  }

  private boolean pageScrolled(int paramInt)
  {
    int i = 0;
    if (this.mItems.size() == 0)
    {
      this.mCalledSuper = false;
      onPageScrolled(0, 0.0F, 0);
      if (!this.mCalledSuper)
        throw new IllegalStateException("onPageScrolled did not call superclass implementation");
    }
    else
    {
      ItemInfo localItemInfo = infoForCurrentScrollPosition();
      int j = getWidth();
      int k = j + this.mPageMargin;
      float f1 = this.mPageMargin / j;
      int l = localItemInfo.position;
      float f2 = (paramInt / j - localItemInfo.offset) / (f1 + localItemInfo.widthFactor);
      int i1 = (int)(f2 * k);
      this.mCalledSuper = false;
      onPageScrolled(l, f2, i1);
      if (!this.mCalledSuper)
        throw new IllegalStateException("onPageScrolled did not call superclass implementation");
      i = 1;
    }
    return i;
  }

  private boolean performDrag(float paramFloat)
  {
    boolean bool = false;
    float f1 = this.mLastMotionX - paramFloat;
    this.mLastMotionX = paramFloat;
    float f2 = f1 + getScrollX();
    int i = getWidth();
    float f3 = i * this.mFirstOffset;
    float f4 = i * this.mLastOffset;
    int j = 1;
    int k = 1;
    ItemInfo localItemInfo1 = (ItemInfo)this.mItems.get(0);
    ItemInfo localItemInfo2 = (ItemInfo)this.mItems.get(-1 + this.mItems.size());
    if (localItemInfo1.position != 0)
    {
      j = 0;
      f3 = localItemInfo1.offset * i;
    }
    if (localItemInfo2.position != -1 + this.mAdapter.getCount())
    {
      k = 0;
      f4 = localItemInfo2.offset * i;
    }
    if (f2 < f3)
    {
      if (j != 0)
      {
        float f6 = f3 - f2;
        bool = this.mLeftEdge.onPull(Math.abs(f6) / i);
      }
      f2 = f3;
    }
    while (true)
    {
      this.mLastMotionX += f2 - (int)f2;
      scrollTo((int)f2, getScrollY());
      pageScrolled((int)f2);
      return bool;
      if (f2 <= f4)
        continue;
      if (k != 0)
      {
        float f5 = f2 - f4;
        bool = this.mRightEdge.onPull(Math.abs(f5) / i);
      }
      f2 = f4;
    }
  }

  private void recomputeScrollPosition(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if ((paramInt2 > 0) && (!this.mItems.isEmpty()))
    {
      int j = paramInt1 + paramInt3;
      int k = paramInt2 + paramInt4;
      int l = (int)(getScrollX() / k * j);
      scrollTo(l, getScrollY());
      if (!this.mScroller.isFinished())
      {
        int i1 = this.mScroller.getDuration() - this.mScroller.timePassed();
        ItemInfo localItemInfo2 = infoForPosition(this.mCurItem);
        this.mScroller.startScroll(l, 0, (int)(localItemInfo2.offset * paramInt1), 0, i1);
      }
      label110: return;
    }
    ItemInfo localItemInfo1 = infoForPosition(this.mCurItem);
    float f;
    if (localItemInfo1 != null)
      f = Math.min(localItemInfo1.offset, this.mLastOffset);
    while (true)
    {
      int i = (int)(f * paramInt1);
      if (i != getScrollX());
      completeScroll(false);
      scrollTo(i, getScrollY());
      break label110:
      f = 0.0F;
    }
  }

  private void removeNonDecorViews()
  {
    for (int i = 0; i < getChildCount(); ++i)
    {
      if (((LayoutParams)getChildAt(i).getLayoutParams()).isDecor)
        continue;
      removeViewAt(i);
      --i;
    }
  }

  private void scrollToItem(int paramInt1, boolean paramBoolean1, int paramInt2, boolean paramBoolean2)
  {
    ItemInfo localItemInfo = infoForPosition(paramInt1);
    int i = 0;
    if (localItemInfo != null)
      i = (int)(getWidth() * Math.max(this.mFirstOffset, Math.min(localItemInfo.offset, this.mLastOffset)));
    if (paramBoolean1)
    {
      smoothScrollTo(i, 0, paramInt2);
      if ((paramBoolean2) && (this.mOnPageChangeListener != null))
        this.mOnPageChangeListener.onPageSelected(paramInt1);
      if ((paramBoolean2) && (this.mInternalPageChangeListener != null))
        this.mInternalPageChangeListener.onPageSelected(paramInt1);
    }
    while (true)
    {
      return;
      if ((paramBoolean2) && (this.mOnPageChangeListener != null))
        this.mOnPageChangeListener.onPageSelected(paramInt1);
      if ((paramBoolean2) && (this.mInternalPageChangeListener != null))
        this.mInternalPageChangeListener.onPageSelected(paramInt1);
      completeScroll(false);
      scrollTo(i, 0);
    }
  }

  private void setScrollState(int paramInt)
  {
    int i = 1;
    if (this.mScrollState == paramInt)
      label10: return;
    this.mScrollState = paramInt;
    if (paramInt == i)
    {
      this.mSeenPositionMax = -1;
      this.mSeenPositionMin = -1;
    }
    if (this.mPageTransformer != null)
      if (paramInt == 0)
        break label69;
    while (true)
    {
      enableLayers(i);
      if (this.mOnPageChangeListener != null);
      this.mOnPageChangeListener.onPageScrollStateChanged(paramInt);
      break label10:
      label69: int j = 0;
    }
  }

  private void setScrollingCacheEnabled(boolean paramBoolean)
  {
    if (this.mScrollingCacheEnabled == paramBoolean)
      return;
    this.mScrollingCacheEnabled = paramBoolean;
  }

  public void addFocusables(ArrayList<View> paramArrayList, int paramInt1, int paramInt2)
  {
    int i = paramArrayList.size();
    int j = getDescendantFocusability();
    if (j != 393216)
      for (int k = 0; k < getChildCount(); ++k)
      {
        View localView = getChildAt(k);
        if (localView.getVisibility() != 0)
          continue;
        ItemInfo localItemInfo = infoForChild(localView);
        if ((localItemInfo == null) || (localItemInfo.position != this.mCurItem))
          continue;
        localView.addFocusables(paramArrayList, paramInt1, paramInt2);
      }
    if (((j == 262144) && (i != paramArrayList.size())) || (!isFocusable()));
    while (true)
    {
      return;
      if ((((paramInt2 & 0x1) == 1) && (isInTouchMode()) && (!isFocusableInTouchMode())) || (paramArrayList == null))
        continue;
      paramArrayList.add(this);
    }
  }

  ItemInfo addNewItem(int paramInt1, int paramInt2)
  {
    ItemInfo localItemInfo = new ItemInfo();
    localItemInfo.position = paramInt1;
    localItemInfo.object = this.mAdapter.instantiateItem(this, paramInt1);
    localItemInfo.widthFactor = this.mAdapter.getPageWidth(paramInt1);
    if ((paramInt2 < 0) || (paramInt2 >= this.mItems.size()))
      this.mItems.add(localItemInfo);
    while (true)
    {
      return localItemInfo;
      this.mItems.add(paramInt2, localItemInfo);
    }
  }

  public void addTouchables(ArrayList<View> paramArrayList)
  {
    for (int i = 0; i < getChildCount(); ++i)
    {
      View localView = getChildAt(i);
      if (localView.getVisibility() != 0)
        continue;
      ItemInfo localItemInfo = infoForChild(localView);
      if ((localItemInfo == null) || (localItemInfo.position != this.mCurItem))
        continue;
      localView.addTouchables(paramArrayList);
    }
  }

  public void addView(View paramView, int paramInt, ViewGroup.LayoutParams paramLayoutParams)
  {
    if (!checkLayoutParams(paramLayoutParams))
      paramLayoutParams = generateLayoutParams(paramLayoutParams);
    LayoutParams localLayoutParams = (LayoutParams)paramLayoutParams;
    localLayoutParams.isDecor |= paramView instanceof Decor;
    if (this.mInLayout)
    {
      if ((localLayoutParams != null) && (localLayoutParams.isDecor))
        throw new IllegalStateException("Cannot add pager decor view during layout");
      localLayoutParams.needsMeasure = true;
      addViewInLayout(paramView, paramInt, paramLayoutParams);
    }
    while (true)
    {
      return;
      super.addView(paramView, paramInt, paramLayoutParams);
    }
  }

  public boolean arrowScroll(int paramInt)
  {
    View localView1 = findFocus();
    if (localView1 == this)
      localView1 = null;
    boolean bool = false;
    View localView2 = FocusFinder.getInstance().findNextFocus(this, localView1, paramInt);
    if ((localView2 != null) && (localView2 != localView1))
      if (paramInt == 17)
      {
        int k = getChildRectInPagerCoordinates(this.mTempRect, localView2).left;
        int l = getChildRectInPagerCoordinates(this.mTempRect, localView1).left;
        if ((localView1 == null) || (k < l));
      }
    for (bool = pageLeft(); ; bool = pageRight())
      do
        while (true)
        {
          if (bool)
            playSoundEffect(SoundEffectConstants.getContantForFocusDirection(paramInt));
          return bool;
          bool = localView2.requestFocus();
          continue;
          if (paramInt != 66)
            continue;
          int i = getChildRectInPagerCoordinates(this.mTempRect, localView2).left;
          int j = getChildRectInPagerCoordinates(this.mTempRect, localView1).left;
          if ((localView1 != null) && (i <= j))
            bool = pageRight();
          bool = localView2.requestFocus();
          continue;
          if ((paramInt != 17) && (paramInt != 1))
            break;
          bool = pageLeft();
        }
      while ((paramInt != 66) && (paramInt != 2));
  }

  public boolean beginFakeDrag()
  {
    int i = 0;
    if (this.mIsBeingDragged)
      label9: return i;
    this.mFakeDragging = true;
    setScrollState(1);
    this.mLastMotionX = 0.0F;
    this.mInitialMotionX = 0.0F;
    if (this.mVelocityTracker == null)
      this.mVelocityTracker = VelocityTracker.obtain();
    while (true)
    {
      long l = SystemClock.uptimeMillis();
      MotionEvent localMotionEvent = MotionEvent.obtain(l, l, 0, 0.0F, 0.0F, 0);
      this.mVelocityTracker.addMovement(localMotionEvent);
      localMotionEvent.recycle();
      this.mFakeDragBeginTime = l;
      i = 1;
      break label9:
      this.mVelocityTracker.clear();
    }
  }

  protected boolean canScroll(View paramView, boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3)
  {
    int l;
    if (paramView instanceof ViewGroup)
    {
      ViewGroup localViewGroup = (ViewGroup)paramView;
      int j = paramView.getScrollX();
      int k = paramView.getScrollY();
      l = -1 + localViewGroup.getChildCount();
      if (l >= 0)
      {
        label35: View localView = localViewGroup.getChildAt(l);
        if ((paramInt2 + j < localView.getLeft()) || (paramInt2 + j >= localView.getRight()) || (paramInt3 + k < localView.getTop()) || (paramInt3 + k >= localView.getBottom()) || (!canScroll(localView, true, paramInt1, paramInt2 + j - localView.getLeft(), paramInt3 + k - localView.getTop())));
      }
    }
    for (int i = 1; ; i = 0)
      while (true)
      {
        return i;
        --l;
        break label35:
        if ((!paramBoolean) || (!ViewCompat.canScrollHorizontally(paramView, -paramInt1)))
          break;
        i = 1;
      }
  }

  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    if ((paramLayoutParams instanceof LayoutParams) && (super.checkLayoutParams(paramLayoutParams)));
    for (int i = 1; ; i = 0)
      return i;
  }

  public void computeScroll()
  {
    if ((!this.mScroller.isFinished()) && (this.mScroller.computeScrollOffset()))
    {
      int i = getScrollX();
      int j = getScrollY();
      int k = this.mScroller.getCurrX();
      int l = this.mScroller.getCurrY();
      if ((i != k) || (j != l))
      {
        scrollTo(k, l);
        if (!pageScrolled(k))
        {
          this.mScroller.abortAnimation();
          scrollTo(0, l);
        }
      }
      ViewCompat.postInvalidateOnAnimation(this);
    }
    while (true)
    {
      return;
      completeScroll(true);
    }
  }

  void dataSetChanged()
  {
    int i;
    label37: int j;
    int k;
    int l;
    label47: ItemInfo localItemInfo;
    int i3;
    if ((this.mItems.size() < 1 + 2 * this.mOffscreenPageLimit) && (this.mItems.size() < this.mAdapter.getCount()))
    {
      i = 1;
      j = this.mCurItem;
      k = 0;
      l = 0;
      if (l >= this.mItems.size())
        break label235;
      localItemInfo = (ItemInfo)this.mItems.get(l);
      i3 = this.mAdapter.getItemPosition(localItemInfo.object);
      if (i3 != -1)
        break label105;
    }
    while (true)
    {
      ++l;
      break label47:
      i = 0;
      break label37:
      if (i3 == -2)
      {
        label105: this.mItems.remove(l);
        --l;
        if (k == 0)
        {
          this.mAdapter.startUpdate(this);
          k = 1;
        }
        this.mAdapter.destroyItem(this, localItemInfo.position, localItemInfo.object);
        i = 1;
        if (this.mCurItem != localItemInfo.position)
          continue;
        j = Math.max(0, Math.min(this.mCurItem, -1 + this.mAdapter.getCount()));
        i = 1;
      }
      if (localItemInfo.position == i3)
        continue;
      if (localItemInfo.position == this.mCurItem)
        j = i3;
      localItemInfo.position = i3;
      i = 1;
    }
    if (k != 0)
      label235: this.mAdapter.finishUpdate(this);
    Collections.sort(this.mItems, COMPARATOR);
    if (i == 0)
      return;
    int i1 = getChildCount();
    for (int i2 = 0; i2 < i1; ++i2)
    {
      LayoutParams localLayoutParams = (LayoutParams)getChildAt(i2).getLayoutParams();
      if (localLayoutParams.isDecor)
        continue;
      localLayoutParams.widthFactor = 0.0F;
    }
    setCurrentItemInternal(j, false, true);
    requestLayout();
  }

  public boolean dispatchKeyEvent(KeyEvent paramKeyEvent)
  {
    if ((super.dispatchKeyEvent(paramKeyEvent)) || (executeKeyEvent(paramKeyEvent)));
    for (int i = 1; ; i = 0)
      return i;
  }

  public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent)
  {
    int i = getChildCount();
    int j = 0;
    if (j < i)
    {
      label7: View localView = getChildAt(j);
      if (localView.getVisibility() == 0)
      {
        ItemInfo localItemInfo = infoForChild(localView);
        if ((localItemInfo == null) || (localItemInfo.position != this.mCurItem) || (!localView.dispatchPopulateAccessibilityEvent(paramAccessibilityEvent)));
      }
    }
    for (int k = 1; ; k = 0)
    {
      return k;
      ++j;
      break label7:
    }
  }

  float distanceInfluenceForSnapDuration(float paramFloat)
  {
    return (float)Math.sin((float)(0.47123891676382D * (paramFloat - 0.5F)));
  }

  public void draw(Canvas paramCanvas)
  {
    super.draw(paramCanvas);
    boolean bool = false;
    int i = ViewCompat.getOverScrollMode(this);
    if ((i == 0) || ((i == 1) && (this.mAdapter != null) && (this.mAdapter.getCount() > 1)))
    {
      if (!this.mLeftEdge.isFinished())
      {
        int i1 = paramCanvas.save();
        int i2 = getHeight() - getPaddingTop() - getPaddingBottom();
        int i3 = getWidth();
        paramCanvas.rotate(270.0F);
        paramCanvas.translate(-i2 + getPaddingTop(), this.mFirstOffset * i3);
        this.mLeftEdge.setSize(i2, i3);
        bool = false | this.mLeftEdge.draw(paramCanvas);
        paramCanvas.restoreToCount(i1);
      }
      if (!this.mRightEdge.isFinished())
      {
        int j = paramCanvas.save();
        int k = getWidth();
        int l = getHeight() - getPaddingTop() - getPaddingBottom();
        paramCanvas.rotate(90.0F);
        paramCanvas.translate(-getPaddingTop(), -(1.0F + this.mLastOffset) * k);
        this.mRightEdge.setSize(l, k);
        bool |= this.mRightEdge.draw(paramCanvas);
        paramCanvas.restoreToCount(j);
      }
    }
    while (true)
    {
      if (bool)
        ViewCompat.postInvalidateOnAnimation(this);
      return;
      this.mLeftEdge.finish();
      this.mRightEdge.finish();
    }
  }

  protected void drawableStateChanged()
  {
    super.drawableStateChanged();
    Drawable localDrawable = this.mMarginDrawable;
    if ((localDrawable == null) || (!localDrawable.isStateful()))
      return;
    localDrawable.setState(getDrawableState());
  }

  public void endFakeDrag()
  {
    if (!this.mFakeDragging)
      throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
    VelocityTracker localVelocityTracker = this.mVelocityTracker;
    localVelocityTracker.computeCurrentVelocity(1000, this.mMaximumVelocity);
    int i = (int)VelocityTrackerCompat.getXVelocity(localVelocityTracker, this.mActivePointerId);
    this.mPopulatePending = true;
    int j = getWidth();
    int k = getScrollX();
    ItemInfo localItemInfo = infoForCurrentScrollPosition();
    setCurrentItemInternal(determineTargetPage(localItemInfo.position, (k / j - localItemInfo.offset) / localItemInfo.widthFactor, i, (int)(this.mLastMotionX - this.mInitialMotionX)), true, true, i);
    endDrag();
    this.mFakeDragging = false;
  }

  public boolean executeKeyEvent(KeyEvent paramKeyEvent)
  {
    boolean bool = false;
    if (paramKeyEvent.getAction() == 0)
      switch (paramKeyEvent.getKeyCode())
      {
      default:
      case 21:
      case 22:
      case 61:
      }
    while (true)
    {
      return bool;
      bool = arrowScroll(17);
      continue;
      bool = arrowScroll(66);
      continue;
      if (Build.VERSION.SDK_INT < 11)
        continue;
      if (KeyEventCompat.hasNoModifiers(paramKeyEvent))
        bool = arrowScroll(2);
      if (!KeyEventCompat.hasModifiers(paramKeyEvent, 1))
        continue;
      bool = arrowScroll(1);
    }
  }

  public void fakeDragBy(float paramFloat)
  {
    if (!this.mFakeDragging)
      throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
    this.mLastMotionX = (paramFloat + this.mLastMotionX);
    float f1 = getScrollX() - paramFloat;
    int i = getWidth();
    float f2 = i * this.mFirstOffset;
    float f3 = i * this.mLastOffset;
    ItemInfo localItemInfo1 = (ItemInfo)this.mItems.get(0);
    ItemInfo localItemInfo2 = (ItemInfo)this.mItems.get(-1 + this.mItems.size());
    if (localItemInfo1.position != 0)
      f2 = localItemInfo1.offset * i;
    if (localItemInfo2.position != -1 + this.mAdapter.getCount())
      f3 = localItemInfo2.offset * i;
    if (f1 < f2)
      f1 = f2;
    while (true)
    {
      this.mLastMotionX += f1 - (int)f1;
      scrollTo((int)f1, getScrollY());
      pageScrolled((int)f1);
      long l = SystemClock.uptimeMillis();
      MotionEvent localMotionEvent = MotionEvent.obtain(this.mFakeDragBeginTime, l, 2, this.mLastMotionX, 0.0F, 0);
      this.mVelocityTracker.addMovement(localMotionEvent);
      localMotionEvent.recycle();
      return;
      if (f1 <= f3)
        continue;
      f1 = f3;
    }
  }

  protected ViewGroup.LayoutParams generateDefaultLayoutParams()
  {
    return new LayoutParams();
  }

  public ViewGroup.LayoutParams generateLayoutParams(AttributeSet paramAttributeSet)
  {
    return new LayoutParams(getContext(), paramAttributeSet);
  }

  protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    return generateDefaultLayoutParams();
  }

  public PagerAdapter getAdapter()
  {
    return this.mAdapter;
  }

  protected int getChildDrawingOrder(int paramInt1, int paramInt2)
  {
    if (this.mDrawingOrder == 2);
    for (int i = paramInt1 - 1 - paramInt2; ; i = paramInt2)
      return ((LayoutParams)((View)this.mDrawingOrderedChildren.get(i)).getLayoutParams()).childIndex;
  }

  public int getCurrentItem()
  {
    return this.mCurItem;
  }

  public int getOffscreenPageLimit()
  {
    return this.mOffscreenPageLimit;
  }

  public int getPageMargin()
  {
    return this.mPageMargin;
  }

  ItemInfo infoForAnyChild(View paramView)
  {
    label0: ViewParent localViewParent = paramView.getParent();
    if (localViewParent != this)
      if ((localViewParent != null) && (localViewParent instanceof View));
    for (ItemInfo localItemInfo = null; ; localItemInfo = infoForChild(paramView))
    {
      return localItemInfo;
      paramView = (View)localViewParent;
      break label0:
    }
  }

  ItemInfo infoForChild(View paramView)
  {
    int i = 0;
    label2: ItemInfo localItemInfo;
    if (i < this.mItems.size())
    {
      localItemInfo = (ItemInfo)this.mItems.get(i);
      if (!this.mAdapter.isViewFromObject(paramView, localItemInfo.object));
    }
    while (true)
    {
      return localItemInfo;
      ++i;
      break label2:
      localItemInfo = null;
    }
  }

  ItemInfo infoForPosition(int paramInt)
  {
    int i = 0;
    label2: ItemInfo localItemInfo;
    if (i < this.mItems.size())
    {
      localItemInfo = (ItemInfo)this.mItems.get(i);
      if (localItemInfo.position != paramInt);
    }
    while (true)
    {
      return localItemInfo;
      ++i;
      break label2:
      localItemInfo = null;
    }
  }

  void initViewPager()
  {
    setWillNotDraw(false);
    setDescendantFocusability(262144);
    setFocusable(true);
    Context localContext = getContext();
    this.mScroller = new Scroller(localContext, sInterpolator);
    ViewConfiguration localViewConfiguration = ViewConfiguration.get(localContext);
    this.mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(localViewConfiguration);
    this.mMinimumVelocity = localViewConfiguration.getScaledMinimumFlingVelocity();
    this.mMaximumVelocity = localViewConfiguration.getScaledMaximumFlingVelocity();
    this.mLeftEdge = new EdgeEffectCompat(localContext);
    this.mRightEdge = new EdgeEffectCompat(localContext);
    float f = localContext.getResources().getDisplayMetrics().density;
    this.mFlingDistance = (int)(25.0F * f);
    this.mCloseEnough = (int)(2.0F * f);
    this.mDefaultGutterSize = (int)(16.0F * f);
    ViewCompat.setAccessibilityDelegate(this, new MyAccessibilityDelegate());
    if (ViewCompat.getImportantForAccessibility(this) != 0)
      return;
    ViewCompat.setImportantForAccessibility(this, 1);
  }

  public boolean isFakeDragging()
  {
    return this.mFakeDragging;
  }

  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    this.mFirstLayout = true;
  }

  protected void onDetachedFromWindow()
  {
    removeCallbacks(this.mEndScrollRunnable);
    super.onDetachedFromWindow();
  }

  protected void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    int i;
    int j;
    float f1;
    int k;
    ItemInfo localItemInfo;
    float f2;
    int l;
    int i1;
    int i2;
    if ((this.mPageMargin > 0) && (this.mMarginDrawable != null) && (this.mItems.size() > 0) && (this.mAdapter != null))
    {
      i = getScrollX();
      j = getWidth();
      f1 = this.mPageMargin / j;
      k = 0;
      localItemInfo = (ItemInfo)this.mItems.get(0);
      f2 = localItemInfo.offset;
      l = this.mItems.size();
      i1 = localItemInfo.position;
      i2 = ((ItemInfo)this.mItems.get(l - 1)).position;
    }
    for (int i3 = i1; ; ++i3)
    {
      float f4;
      if (i3 < i2)
      {
        while ((i3 > localItemInfo.position) && (k < l))
        {
          ArrayList localArrayList = this.mItems;
          localItemInfo = (ItemInfo)???.get(++k);
        }
        if (i3 != localItemInfo.position)
          break label272;
        f4 = (localItemInfo.offset + localItemInfo.widthFactor) * j;
        f2 = f1 + (localItemInfo.offset + localItemInfo.widthFactor);
      }
      while (true)
      {
        if (f4 + this.mPageMargin > i)
        {
          this.mMarginDrawable.setBounds((int)f4, this.mTopPageBounds, (int)(0.5F + (f4 + this.mPageMargin)), this.mBottomPageBounds);
          this.mMarginDrawable.draw(paramCanvas);
        }
        if (f4 <= i + j)
          break;
        return;
        label272: float f3 = this.mAdapter.getPageWidth(i3);
        f4 = (f2 + f3) * j;
        f2 += f3 + f1;
      }
    }
  }

  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    int i = 0xFF & paramMotionEvent.getAction();
    if ((i == 3) || (i == 1))
    {
      this.mIsBeingDragged = false;
      this.mIsUnableToDrag = false;
      this.mActivePointerId = -1;
      if (this.mVelocityTracker != null)
      {
        this.mVelocityTracker.recycle();
        this.mVelocityTracker = null;
      }
    }
    for (boolean bool = false; ; bool = false)
    {
      while (true)
      {
        label56: return bool;
        if (i == 0)
          break label86;
        if (!this.mIsBeingDragged)
          break;
        bool = true;
      }
      if (!this.mIsUnableToDrag)
        break;
    }
    label86: switch (i)
    {
    default:
    case 2:
    case 0:
    case 6:
    }
    while (true)
    {
      if (this.mVelocityTracker == null)
        label120: this.mVelocityTracker = VelocityTracker.obtain();
      this.mVelocityTracker.addMovement(paramMotionEvent);
      bool = this.mIsBeingDragged;
      break label56:
      int j = this.mActivePointerId;
      if (j == -1)
        continue;
      int k = MotionEventCompat.findPointerIndex(paramMotionEvent, j);
      float f2 = MotionEventCompat.getX(paramMotionEvent, k);
      float f3 = f2 - this.mLastMotionX;
      float f4 = Math.abs(f3);
      float f5 = MotionEventCompat.getY(paramMotionEvent, k);
      float f6 = Math.abs(f5 - this.mLastMotionY);
      if ((f3 != 0.0F) && (!isGutterDrag(this.mLastMotionX, f3)) && (canScroll(this, false, (int)f3, (int)f2, (int)f5)))
      {
        this.mLastMotionX = f2;
        this.mInitialMotionX = f2;
        this.mLastMotionY = f5;
        this.mIsUnableToDrag = true;
        bool = false;
      }
      float f7;
      if ((f4 > this.mTouchSlop) && (f4 > f6))
      {
        this.mIsBeingDragged = true;
        setScrollState(1);
        if (f3 > 0.0F)
        {
          f7 = this.mInitialMotionX + this.mTouchSlop;
          label329: this.mLastMotionX = f7;
          setScrollingCacheEnabled(true);
        }
      }
      while (true)
      {
        if ((this.mIsBeingDragged) && (performDrag(f2)));
        ViewCompat.postInvalidateOnAnimation(this);
        break label120:
        f7 = this.mInitialMotionX - this.mTouchSlop;
        break label329:
        if (f6 <= this.mTouchSlop)
          continue;
        this.mIsUnableToDrag = true;
      }
      float f1 = paramMotionEvent.getX();
      this.mInitialMotionX = f1;
      this.mLastMotionX = f1;
      this.mLastMotionY = paramMotionEvent.getY();
      this.mActivePointerId = MotionEventCompat.getPointerId(paramMotionEvent, 0);
      this.mIsUnableToDrag = false;
      this.mScroller.computeScrollOffset();
      if ((this.mScrollState == 2) && (Math.abs(this.mScroller.getFinalX() - this.mScroller.getCurrX()) > this.mCloseEnough))
      {
        this.mScroller.abortAnimation();
        this.mPopulatePending = false;
        populate();
        this.mIsBeingDragged = true;
        setScrollState(1);
      }
      completeScroll(false);
      this.mIsBeingDragged = false;
      continue;
      onSecondaryPointerUp(paramMotionEvent);
    }
  }

  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    this.mInLayout = true;
    populate();
    this.mInLayout = false;
    int i = getChildCount();
    int j = paramInt3 - paramInt1;
    int k = paramInt4 - paramInt2;
    int l = getPaddingLeft();
    int i1 = getPaddingTop();
    int i2 = getPaddingRight();
    int i3 = getPaddingBottom();
    int i4 = getScrollX();
    int i5 = 0;
    int i6 = 0;
    if (i6 < i)
    {
      label68: View localView2 = getChildAt(i6);
      int i12;
      label172: int i13;
      if (localView2.getVisibility() != 8)
      {
        LayoutParams localLayoutParams2 = (LayoutParams)localView2.getLayoutParams();
        if (localLayoutParams2.isDecor)
        {
          int i10 = 0x7 & localLayoutParams2.gravity;
          int i11 = 0x70 & localLayoutParams2.gravity;
          switch (i10)
          {
          case 2:
          case 4:
          default:
            i12 = l;
            switch (i11)
            {
            default:
              i13 = i1;
            case 48:
            case 16:
            case 80:
            }
          case 3:
          case 1:
          case 5:
          }
        }
      }
      while (true)
      {
        int i14 = i12 + i4;
        localView2.layout(i14, i13, i14 + localView2.getMeasuredWidth(), i13 + localView2.getMeasuredHeight());
        ++i5;
        ++i6;
        break label68:
        i12 = l;
        l += localView2.getMeasuredWidth();
        break label172:
        i12 = Math.max((j - localView2.getMeasuredWidth()) / 2, l);
        break label172:
        i12 = j - i2 - localView2.getMeasuredWidth();
        i2 += localView2.getMeasuredWidth();
        break label172:
        i13 = i1;
        i1 += localView2.getMeasuredHeight();
        continue;
        i13 = Math.max((k - localView2.getMeasuredHeight()) / 2, i1);
        continue;
        i13 = k - i3 - localView2.getMeasuredHeight();
        i3 += localView2.getMeasuredHeight();
      }
    }
    for (int i7 = 0; i7 < i; ++i7)
    {
      View localView1 = getChildAt(i7);
      if (localView1.getVisibility() == 8)
        continue;
      LayoutParams localLayoutParams1 = (LayoutParams)localView1.getLayoutParams();
      if (localLayoutParams1.isDecor)
        continue;
      ItemInfo localItemInfo = infoForChild(localView1);
      if (localItemInfo == null)
        continue;
      int i8 = l + (int)(j * localItemInfo.offset);
      int i9 = i1;
      if (localLayoutParams1.needsMeasure)
      {
        localLayoutParams1.needsMeasure = false;
        localView1.measure(View.MeasureSpec.makeMeasureSpec((int)((j - l - i2) * localLayoutParams1.widthFactor), 1073741824), View.MeasureSpec.makeMeasureSpec(k - i1 - i3, 1073741824));
      }
      localView1.layout(i8, i9, i8 + localView1.getMeasuredWidth(), i9 + localView1.getMeasuredHeight());
    }
    this.mTopPageBounds = i1;
    this.mBottomPageBounds = (k - i3);
    this.mDecorChildCount = i5;
    this.mFirstLayout = false;
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    setMeasuredDimension(getDefaultSize(0, paramInt1), getDefaultSize(0, paramInt2));
    int i = getMeasuredWidth();
    this.mGutterSize = Math.min(i / 10, this.mDefaultGutterSize);
    int j = i - getPaddingLeft() - getPaddingRight();
    int k = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
    int l = getChildCount();
    int i1 = 0;
    if (i1 < l)
    {
      label72: View localView2 = getChildAt(i1);
      int i7;
      int i8;
      label167: int i9;
      if (localView2.getVisibility() != 8)
      {
        LayoutParams localLayoutParams2 = (LayoutParams)localView2.getLayoutParams();
        if ((localLayoutParams2 != null) && (localLayoutParams2.isDecor))
        {
          int i4 = 0x7 & localLayoutParams2.gravity;
          int i5 = 0x70 & localLayoutParams2.gravity;
          int i6 = -2147483648;
          i7 = -2147483648;
          if ((i5 != 48) && (i5 != 80))
            break label304;
          i8 = 1;
          if ((i4 != 3) && (i4 != 5))
            break label310;
          i9 = 1;
          label182: if (i8 == 0)
            break label316;
          i6 = 1073741824;
          label192: int i10 = j;
          int i11 = k;
          if (localLayoutParams2.width != -2)
          {
            i6 = 1073741824;
            if (localLayoutParams2.width != -1)
              i10 = localLayoutParams2.width;
          }
          if (localLayoutParams2.height != -2)
          {
            i7 = 1073741824;
            if (localLayoutParams2.height != -1)
              i11 = localLayoutParams2.height;
          }
          localView2.measure(View.MeasureSpec.makeMeasureSpec(i10, i6), View.MeasureSpec.makeMeasureSpec(i11, i7));
          if (i8 == 0)
            break label329;
          k -= localView2.getMeasuredHeight();
        }
      }
      while (true)
      {
        ++i1;
        break label72:
        label304: i8 = 0;
        break label167:
        label310: i9 = 0;
        break label182:
        label316: if (i9 != 0);
        i7 = 1073741824;
        break label192:
        label329: if (i9 == 0)
          continue;
        j -= localView2.getMeasuredWidth();
      }
    }
    this.mChildWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec(j, 1073741824);
    this.mChildHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(k, 1073741824);
    this.mInLayout = true;
    populate();
    this.mInLayout = false;
    int i2 = getChildCount();
    for (int i3 = 0; i3 < i2; ++i3)
    {
      View localView1 = getChildAt(i3);
      if (localView1.getVisibility() == 8)
        continue;
      LayoutParams localLayoutParams1 = (LayoutParams)localView1.getLayoutParams();
      if ((localLayoutParams1 != null) && (localLayoutParams1.isDecor))
        continue;
      localView1.measure(View.MeasureSpec.makeMeasureSpec((int)(j * localLayoutParams1.widthFactor), 1073741824), this.mChildHeightMeasureSpec);
    }
  }

  protected void onPageScrolled(int paramInt1, float paramFloat, int paramInt2)
  {
    if (this.mDecorChildCount > 0)
    {
      int l = getScrollX();
      int i1 = getPaddingLeft();
      int i2 = getPaddingRight();
      int i3 = getWidth();
      int i4 = getChildCount();
      View localView2;
      LayoutParams localLayoutParams;
      for (int i5 = 0; ; ++i5)
      {
        if (i5 >= i4)
          break label215;
        localView2 = getChildAt(i5);
        localLayoutParams = (LayoutParams)localView2.getLayoutParams();
        label73: if (localLayoutParams.isDecor)
          break;
      }
      int i6;
      switch (0x7 & localLayoutParams.gravity)
      {
      case 2:
      case 4:
      default:
        i6 = i1;
      case 3:
      case 1:
      case 5:
      }
      while (true)
      {
        int i7 = i6 + l - localView2.getLeft();
        if (i7 != 0);
        localView2.offsetLeftAndRight(i7);
        break label73:
        i6 = i1;
        i1 += localView2.getWidth();
        continue;
        i6 = Math.max((i3 - localView2.getMeasuredWidth()) / 2, i1);
        continue;
        i6 = i3 - i2 - localView2.getMeasuredWidth();
        i2 += localView2.getMeasuredWidth();
      }
    }
    if ((this.mSeenPositionMin < 0) || (paramInt1 < this.mSeenPositionMin))
      label215: this.mSeenPositionMin = paramInt1;
    if ((this.mSeenPositionMax < 0) || (FloatMath.ceil(paramFloat + paramInt1) > this.mSeenPositionMax))
      this.mSeenPositionMax = (paramInt1 + 1);
    if (this.mOnPageChangeListener != null)
      this.mOnPageChangeListener.onPageScrolled(paramInt1, paramFloat, paramInt2);
    if (this.mInternalPageChangeListener != null)
      this.mInternalPageChangeListener.onPageScrolled(paramInt1, paramFloat, paramInt2);
    if (this.mPageTransformer != null)
    {
      int i = getScrollX();
      int j = getChildCount();
      int k = 0;
      if (k < j)
      {
        label325: View localView1 = getChildAt(k);
        if (((LayoutParams)localView1.getLayoutParams()).isDecor);
        while (true)
        {
          ++k;
          break label325:
          float f = (localView1.getLeft() - i) / getWidth();
          this.mPageTransformer.transformPage(localView1, f);
        }
      }
    }
    this.mCalledSuper = true;
  }

  protected boolean onRequestFocusInDescendants(int paramInt, Rect paramRect)
  {
    int i = getChildCount();
    int j;
    int k;
    int l;
    label20: int i1;
    if ((paramInt & 0x2) != 0)
    {
      j = 0;
      k = 1;
      l = i;
      i1 = j;
      label24: if (i1 == l)
        break label114;
      View localView = getChildAt(i1);
      if (localView.getVisibility() != 0)
        break label104;
      ItemInfo localItemInfo = infoForChild(localView);
      if ((localItemInfo == null) || (localItemInfo.position != this.mCurItem) || (!localView.requestFocus(paramInt, paramRect)))
        break label104;
    }
    for (int i2 = 1; ; i2 = 0)
    {
      return i2;
      j = i - 1;
      k = -1;
      l = -1;
      break label20:
      label104: i1 += k;
      label114: break label24:
    }
  }

  public void onRestoreInstanceState(Parcelable paramParcelable)
  {
    if (!paramParcelable instanceof SavedState)
      super.onRestoreInstanceState(paramParcelable);
    while (true)
    {
      return;
      SavedState localSavedState = (SavedState)paramParcelable;
      super.onRestoreInstanceState(localSavedState.getSuperState());
      if (this.mAdapter != null)
      {
        this.mAdapter.restoreState(localSavedState.adapterState, localSavedState.loader);
        setCurrentItemInternal(localSavedState.position, false, true);
      }
      this.mRestoredCurItem = localSavedState.position;
      this.mRestoredAdapterState = localSavedState.adapterState;
      this.mRestoredClassLoader = localSavedState.loader;
    }
  }

  public Parcelable onSaveInstanceState()
  {
    SavedState localSavedState = new SavedState(super.onSaveInstanceState());
    localSavedState.position = this.mCurItem;
    if (this.mAdapter != null)
      localSavedState.adapterState = this.mAdapter.saveState();
    return localSavedState;
  }

  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    if (paramInt1 == paramInt3)
      return;
    recomputeScrollPosition(paramInt1, paramInt3, this.mPageMargin, this.mPageMargin);
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if (this.mFakeDragging);
    for (int i = 1; ; i = 0)
    {
      while (true)
      {
        label9: return i;
        if ((paramMotionEvent.getAction() != 0) || (paramMotionEvent.getEdgeFlags() == 0))
          break;
        i = 0;
      }
      if ((this.mAdapter != null) && (this.mAdapter.getCount() != 0))
        break;
    }
    if (this.mVelocityTracker == null)
      this.mVelocityTracker = VelocityTracker.obtain();
    this.mVelocityTracker.addMovement(paramMotionEvent);
    int j = paramMotionEvent.getAction();
    boolean bool = false;
    switch (j & 0xFF)
    {
    case 4:
    default:
    case 0:
    case 2:
    case 1:
    case 3:
    case 5:
    case 6:
    }
    while (true)
    {
      if (bool)
        label128: ViewCompat.postInvalidateOnAnimation(this);
      i = 1;
      break label9:
      this.mScroller.abortAnimation();
      this.mPopulatePending = false;
      populate();
      this.mIsBeingDragged = true;
      setScrollState(1);
      float f5 = paramMotionEvent.getX();
      this.mInitialMotionX = f5;
      this.mLastMotionX = f5;
      this.mActivePointerId = MotionEventCompat.getPointerId(paramMotionEvent, 0);
      continue;
      float f4;
      if (!this.mIsBeingDragged)
      {
        int i3 = MotionEventCompat.findPointerIndex(paramMotionEvent, this.mActivePointerId);
        float f1 = MotionEventCompat.getX(paramMotionEvent, i3);
        float f2 = Math.abs(f1 - this.mLastMotionX);
        float f3 = Math.abs(MotionEventCompat.getY(paramMotionEvent, i3) - this.mLastMotionY);
        if ((f2 > this.mTouchSlop) && (f2 > f3))
        {
          this.mIsBeingDragged = true;
          if (f1 - this.mInitialMotionX <= 0.0F)
            break label345;
          f4 = this.mInitialMotionX + this.mTouchSlop;
        }
      }
      while (true)
      {
        this.mLastMotionX = f4;
        setScrollState(1);
        setScrollingCacheEnabled(true);
        if (this.mIsBeingDragged);
        bool = false | performDrag(MotionEventCompat.getX(paramMotionEvent, MotionEventCompat.findPointerIndex(paramMotionEvent, this.mActivePointerId)));
        break label128:
        label345: f4 = this.mInitialMotionX - this.mTouchSlop;
      }
      if (!this.mIsBeingDragged)
        continue;
      VelocityTracker localVelocityTracker = this.mVelocityTracker;
      localVelocityTracker.computeCurrentVelocity(1000, this.mMaximumVelocity);
      int l = (int)VelocityTrackerCompat.getXVelocity(localVelocityTracker, this.mActivePointerId);
      this.mPopulatePending = true;
      int i1 = getWidth();
      int i2 = getScrollX();
      ItemInfo localItemInfo = infoForCurrentScrollPosition();
      setCurrentItemInternal(determineTargetPage(localItemInfo.position, (i2 / i1 - localItemInfo.offset) / localItemInfo.widthFactor, l, (int)(MotionEventCompat.getX(paramMotionEvent, MotionEventCompat.findPointerIndex(paramMotionEvent, this.mActivePointerId)) - this.mInitialMotionX)), true, true, l);
      this.mActivePointerId = -1;
      endDrag();
      bool = this.mLeftEdge.onRelease() | this.mRightEdge.onRelease();
      continue;
      if (!this.mIsBeingDragged)
        continue;
      scrollToItem(this.mCurItem, true, 0, false);
      this.mActivePointerId = -1;
      endDrag();
      bool = this.mLeftEdge.onRelease() | this.mRightEdge.onRelease();
      continue;
      int k = MotionEventCompat.getActionIndex(paramMotionEvent);
      this.mLastMotionX = MotionEventCompat.getX(paramMotionEvent, k);
      this.mActivePointerId = MotionEventCompat.getPointerId(paramMotionEvent, k);
      continue;
      onSecondaryPointerUp(paramMotionEvent);
      this.mLastMotionX = MotionEventCompat.getX(paramMotionEvent, MotionEventCompat.findPointerIndex(paramMotionEvent, this.mActivePointerId));
    }
  }

  boolean pageLeft()
  {
    boolean bool = true;
    if (this.mCurItem > 0)
      setCurrentItem(-1 + this.mCurItem, bool);
    while (true)
    {
      return bool;
      bool = false;
    }
  }

  boolean pageRight()
  {
    boolean bool = true;
    if ((this.mAdapter != null) && (this.mCurItem < -1 + this.mAdapter.getCount()))
      setCurrentItem(1 + this.mCurItem, bool);
    while (true)
    {
      return bool;
      bool = false;
    }
  }

  void populate()
  {
    populate(this.mCurItem);
  }

  void populate(int paramInt)
  {
    ItemInfo localItemInfo1 = null;
    if (this.mCurItem != paramInt)
    {
      localItemInfo1 = infoForPosition(this.mCurItem);
      this.mCurItem = paramInt;
    }
    if (this.mAdapter == null);
    do
    {
      do
        return;
      while ((this.mPopulatePending) || (getWindowToken() == null));
      this.mAdapter.startUpdate(this);
      int i = this.mOffscreenPageLimit;
      int j = Math.max(0, this.mCurItem - i);
      int k = this.mAdapter.getCount();
      int l = Math.min(k - 1, i + this.mCurItem);
      Object localObject1 = null;
      int i1 = 0;
      if (i1 < this.mItems.size())
      {
        label101: ItemInfo localItemInfo8 = (ItemInfo)this.mItems.get(i1);
        if (localItemInfo8.position < this.mCurItem)
          break label530;
        if (localItemInfo8.position == this.mCurItem)
          localObject1 = localItemInfo8;
      }
      if ((localObject1 == null) && (k > 0))
        localObject1 = addNewItem(this.mCurItem, i1);
      float f1;
      int i7;
      ItemInfo localItemInfo5;
      label210: int i8;
      label228: float f3;
      int i9;
      ItemInfo localItemInfo6;
      label299: int i10;
      if (localObject1 != null)
      {
        f1 = 0.0F;
        i7 = i1 - 1;
        if (i7 < 0)
          break label536;
        localItemInfo5 = (ItemInfo)this.mItems.get(i7);
        float f2 = 2.0F - ((ItemInfo)localObject1).widthFactor;
        i8 = -1 + this.mCurItem;
        if (i8 >= 0)
        {
          if ((f1 < f2) || (i8 >= j))
            break label634;
          if (localItemInfo5 != null)
            break label542;
        }
        f3 = ((ItemInfo)localObject1).widthFactor;
        i9 = i1 + 1;
        if (f3 < 2.0F)
        {
          if (i9 >= this.mItems.size())
            break label747;
          localItemInfo6 = (ItemInfo)this.mItems.get(i9);
          i10 = 1 + this.mCurItem;
          if (i10 < k)
          {
            label307: if ((f3 < 2.0F) || (i10 <= l))
              break label846;
            if (localItemInfo6 != null)
              break label753;
          }
        }
        calculatePageOffsets((ItemInfo)localObject1, i1, localItemInfo1);
      }
      PagerAdapter localPagerAdapter1 = this.mAdapter;
      int i2 = this.mCurItem;
      Object localObject2;
      label366: int i3;
      if (localObject1 != null)
      {
        localObject2 = ((ItemInfo)localObject1).object;
        localPagerAdapter1.setPrimaryItem(this, i2, localObject2);
        this.mAdapter.finishUpdate(this);
        if (this.mDrawingOrder == 0)
          break label977;
        i3 = 1;
        if (i3 != 0)
        {
          label394: if (this.mDrawingOrderedChildren != null)
            break label983;
          this.mDrawingOrderedChildren = new ArrayList();
        }
      }
      while (true)
      {
        int i4 = getChildCount();
        for (int i5 = 0; ; ++i5)
        {
          if (i5 >= i4)
            break label993;
          View localView3 = getChildAt(i5);
          LayoutParams localLayoutParams = (LayoutParams)localView3.getLayoutParams();
          localLayoutParams.childIndex = i5;
          if ((!localLayoutParams.isDecor) && (localLayoutParams.widthFactor == 0.0F))
          {
            ItemInfo localItemInfo4 = infoForChild(localView3);
            if (localItemInfo4 != null)
            {
              localLayoutParams.widthFactor = localItemInfo4.widthFactor;
              localLayoutParams.position = localItemInfo4.position;
            }
          }
          if (i3 == 0)
            continue;
          this.mDrawingOrderedChildren.add(localView3);
        }
        label530: ++i1;
        break label101:
        label536: localItemInfo5 = null;
        break label210:
        label542: int i15 = localItemInfo5.position;
        if ((i8 == i15) && (!localItemInfo5.scrolling))
        {
          this.mItems.remove(i7);
          PagerAdapter localPagerAdapter3 = this.mAdapter;
          Object localObject4 = localItemInfo5.object;
          localPagerAdapter3.destroyItem(this, i8, localObject4);
          --i7;
          --i1;
          if (i7 < 0)
            break label628;
        }
        for (localItemInfo5 = (ItemInfo)this.mItems.get(i7); ; localItemInfo5 = null)
        {
          label622: --i8;
          label628: break label228:
        }
        if (localItemInfo5 != null)
        {
          label634: int i14 = localItemInfo5.position;
          if (i8 == i14)
          {
            f1 += localItemInfo5.widthFactor;
            if (--i7 >= 0);
            for (localItemInfo5 = (ItemInfo)this.mItems.get(i7); ; localItemInfo5 = null)
              break label622:
          }
        }
        int i13 = i7 + 1;
        f1 += addNewItem(i8, i13).widthFactor;
        ++i1;
        if (i7 >= 0);
        for (localItemInfo5 = (ItemInfo)this.mItems.get(i7); ; localItemInfo5 = null)
          break label622:
        label747: localItemInfo6 = null;
        break label299:
        label753: int i12 = localItemInfo6.position;
        if ((i10 == i12) && (!localItemInfo6.scrolling))
        {
          this.mItems.remove(i9);
          PagerAdapter localPagerAdapter2 = this.mAdapter;
          Object localObject3 = localItemInfo6.object;
          localPagerAdapter2.destroyItem(this, i10, localObject3);
          if (i9 >= this.mItems.size())
            break label840;
        }
        for (localItemInfo6 = (ItemInfo)this.mItems.get(i9); ; localItemInfo6 = null)
        {
          label834: ++i10;
          label840: break label307:
        }
        if (localItemInfo6 != null)
        {
          label846: int i11 = localItemInfo6.position;
          if (i10 == i11)
          {
            f3 += localItemInfo6.widthFactor;
            if (++i9 < this.mItems.size());
            for (localItemInfo6 = (ItemInfo)this.mItems.get(i9); ; localItemInfo6 = null)
              break label834:
          }
        }
        ItemInfo localItemInfo7 = addNewItem(i10, i9);
        ++i9;
        f3 += localItemInfo7.widthFactor;
        if (i9 < this.mItems.size());
        for (localItemInfo6 = (ItemInfo)this.mItems.get(i9); ; localItemInfo6 = null)
          break label834:
        localObject2 = null;
        break label366:
        label977: i3 = 0;
        break label394:
        label983: this.mDrawingOrderedChildren.clear();
      }
      label993: if (i3 == 0)
        continue;
      Collections.sort(this.mDrawingOrderedChildren, sPositionComparator);
    }
    while (!hasFocus());
    View localView1 = findFocus();
    if (localView1 != null);
    for (ItemInfo localItemInfo2 = infoForAnyChild(localView1); ; localItemInfo2 = null)
    {
      if ((localItemInfo2 == null) || (localItemInfo2.position != this.mCurItem));
      for (int i6 = 0; ; ++i6)
      {
        if (i6 < getChildCount());
        View localView2 = getChildAt(i6);
        ItemInfo localItemInfo3 = infoForChild(localView2);
        if ((localItemInfo3 != null) && (localItemInfo3.position == this.mCurItem) && (localView2.requestFocus(2)));
      }
    }
  }

  public void setAdapter(PagerAdapter paramPagerAdapter)
  {
    if (this.mAdapter != null)
    {
      this.mAdapter.unregisterDataSetObserver(this.mObserver);
      this.mAdapter.startUpdate(this);
      for (int i = 0; i < this.mItems.size(); ++i)
      {
        ItemInfo localItemInfo = (ItemInfo)this.mItems.get(i);
        this.mAdapter.destroyItem(this, localItemInfo.position, localItemInfo.object);
      }
      this.mAdapter.finishUpdate(this);
      this.mItems.clear();
      removeNonDecorViews();
      this.mCurItem = 0;
      scrollTo(0, 0);
    }
    PagerAdapter localPagerAdapter = this.mAdapter;
    this.mAdapter = paramPagerAdapter;
    if (this.mAdapter != null)
    {
      if (this.mObserver == null)
        this.mObserver = new PagerObserver(null);
      this.mAdapter.registerDataSetObserver(this.mObserver);
      this.mPopulatePending = false;
      this.mFirstLayout = true;
      if (this.mRestoredCurItem < 0)
        break label236;
      this.mAdapter.restoreState(this.mRestoredAdapterState, this.mRestoredClassLoader);
      setCurrentItemInternal(this.mRestoredCurItem, false, true);
      this.mRestoredCurItem = -1;
      this.mRestoredAdapterState = null;
      this.mRestoredClassLoader = null;
    }
    while (true)
    {
      if ((this.mAdapterChangeListener != null) && (localPagerAdapter != paramPagerAdapter))
        this.mAdapterChangeListener.onAdapterChanged(localPagerAdapter, paramPagerAdapter);
      return;
      label236: populate();
    }
  }

  void setChildrenDrawingOrderEnabledCompat(boolean paramBoolean)
  {
    if (this.mSetChildrenDrawingOrderEnabled == null);
    try
    {
      Class[] arrayOfClass = new Class[1];
      arrayOfClass[0] = Boolean.TYPE;
      label63: this.mSetChildrenDrawingOrderEnabled = ViewGroup.class.getDeclaredMethod("setChildrenDrawingOrderEnabled", arrayOfClass);
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      try
      {
        Method localMethod = this.mSetChildrenDrawingOrderEnabled;
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = Boolean.valueOf(paramBoolean);
        localMethod.invoke(this, arrayOfObject);
        return;
        localNoSuchMethodException = localNoSuchMethodException;
        Log.e("ViewPager", "Can't find setChildrenDrawingOrderEnabled", localNoSuchMethodException);
      }
      catch (Exception localException)
      {
        Log.e("ViewPager", "Error changing children drawing order", localException);
        break label63:
      }
    }
  }

  public void setCurrentItem(int paramInt)
  {
    this.mPopulatePending = false;
    if (!this.mFirstLayout);
    for (boolean bool = true; ; bool = false)
    {
      setCurrentItemInternal(paramInt, bool, false);
      return;
    }
  }

  public void setCurrentItem(int paramInt, boolean paramBoolean)
  {
    this.mPopulatePending = false;
    setCurrentItemInternal(paramInt, paramBoolean, false);
  }

  void setCurrentItemInternal(int paramInt, boolean paramBoolean1, boolean paramBoolean2)
  {
    setCurrentItemInternal(paramInt, paramBoolean1, paramBoolean2, 0);
  }

  void setCurrentItemInternal(int paramInt1, boolean paramBoolean1, boolean paramBoolean2, int paramInt2)
  {
    boolean bool = true;
    if ((this.mAdapter == null) || (this.mAdapter.getCount() <= 0))
      setScrollingCacheEnabled(false);
    while (true)
    {
      label25: return;
      if ((paramBoolean2) || (this.mCurItem != paramInt1) || (this.mItems.size() == 0))
        break;
      setScrollingCacheEnabled(false);
    }
    if (paramInt1 < 0);
    for (paramInt1 = 0; ; paramInt1 = -1 + this.mAdapter.getCount())
      do
      {
        int i = this.mOffscreenPageLimit;
        if ((paramInt1 <= i + this.mCurItem) && (paramInt1 >= this.mCurItem - i))
          break label153;
        for (int j = 0; ; ++j)
        {
          if (j >= this.mItems.size())
            break label153;
          ((ItemInfo)this.mItems.get(j)).scrolling = bool;
        }
      }
      while (paramInt1 < this.mAdapter.getCount());
    label153: if (this.mCurItem != paramInt1);
    while (true)
    {
      populate(paramInt1);
      scrollToItem(paramInt1, paramBoolean1, paramInt2, bool);
      break label25:
      bool = false;
    }
  }

  OnPageChangeListener setInternalPageChangeListener(OnPageChangeListener paramOnPageChangeListener)
  {
    OnPageChangeListener localOnPageChangeListener = this.mInternalPageChangeListener;
    this.mInternalPageChangeListener = paramOnPageChangeListener;
    return localOnPageChangeListener;
  }

  public void setOffscreenPageLimit(int paramInt)
  {
    if (paramInt < 1)
    {
      Log.w("ViewPager", "Requested offscreen page limit " + paramInt + " too small; defaulting to " + 1);
      paramInt = 1;
    }
    if (paramInt == this.mOffscreenPageLimit)
      return;
    this.mOffscreenPageLimit = paramInt;
    populate();
  }

  void setOnAdapterChangeListener(OnAdapterChangeListener paramOnAdapterChangeListener)
  {
    this.mAdapterChangeListener = paramOnAdapterChangeListener;
  }

  public void setOnPageChangeListener(OnPageChangeListener paramOnPageChangeListener)
  {
    this.mOnPageChangeListener = paramOnPageChangeListener;
  }

  public void setPageMargin(int paramInt)
  {
    int i = this.mPageMargin;
    this.mPageMargin = paramInt;
    int j = getWidth();
    recomputeScrollPosition(j, j, paramInt, i);
    requestLayout();
  }

  public void setPageMarginDrawable(int paramInt)
  {
    setPageMarginDrawable(getContext().getResources().getDrawable(paramInt));
  }

  public void setPageMarginDrawable(Drawable paramDrawable)
  {
    this.mMarginDrawable = paramDrawable;
    if (paramDrawable != null)
      refreshDrawableState();
    if (paramDrawable == null);
    for (boolean bool = true; ; bool = false)
    {
      setWillNotDraw(bool);
      invalidate();
      return;
    }
  }

  public void setPageTransformer(boolean paramBoolean, PageTransformer paramPageTransformer)
  {
    int i = 1;
    label17: label27: int i2;
    if (Build.VERSION.SDK_INT >= 11)
    {
      if (paramPageTransformer == null)
        break label74;
      int j = i;
      if (this.mPageTransformer == null)
        break label80;
      int l = i;
      if (j == l)
        break label86;
      i2 = i;
      label37: this.mPageTransformer = paramPageTransformer;
      setChildrenDrawingOrderEnabledCompat(j);
      if (j == 0)
        break label92;
      if (paramBoolean)
        i = 2;
    }
    for (this.mDrawingOrder = i; ; this.mDrawingOrder = 0)
    {
      if (i2 != 0)
        populate();
      return;
      label74: int k = 0;
      break label17:
      label80: int i1 = 0;
      break label27:
      label86: i2 = 0;
      label92: break label37:
    }
  }

  void smoothScrollTo(int paramInt1, int paramInt2)
  {
    smoothScrollTo(paramInt1, paramInt2, 0);
  }

  void smoothScrollTo(int paramInt1, int paramInt2, int paramInt3)
  {
    if (getChildCount() == 0)
      setScrollingCacheEnabled(false);
    label12: int i;
    int j;
    int k;
    int l;
    while (true)
    {
      return;
      i = getScrollX();
      j = getScrollY();
      k = paramInt1 - i;
      l = paramInt2 - j;
      if ((k != 0) || (l != 0))
        break;
      completeScroll(false);
      populate();
      setScrollState(0);
    }
    setScrollingCacheEnabled(true);
    setScrollState(2);
    int i1 = getWidth();
    int i2 = i1 / 2;
    float f1 = Math.min(1.0F, 1.0F * Math.abs(k) / i1);
    float f2 = i2 + i2 * distanceInfluenceForSnapDuration(f1);
    int i3 = Math.abs(paramInt3);
    if (i3 > 0);
    float f3;
    for (int i4 = 4 * Math.round(1000.0F * Math.abs(f2 / i3)); ; i4 = (int)(100.0F * (1.0F + Math.abs(k) / (f3 + this.mPageMargin))))
    {
      int i5 = Math.min(i4, 600);
      this.mScroller.startScroll(i, j, k, l, i5);
      ViewCompat.postInvalidateOnAnimation(this);
      break label12:
      f3 = i1 * this.mAdapter.getPageWidth(this.mCurItem);
    }
  }

  protected boolean verifyDrawable(Drawable paramDrawable)
  {
    if ((super.verifyDrawable(paramDrawable)) || (paramDrawable == this.mMarginDrawable));
    for (int i = 1; ; i = 0)
      return i;
  }

  static abstract interface Decor
  {
  }

  static class ItemInfo
  {
    Object object;
    float offset;
    int position;
    boolean scrolling;
    float widthFactor;
  }

  public static class LayoutParams extends ViewGroup.LayoutParams
  {
    int childIndex;
    public int gravity;
    public boolean isDecor;
    boolean needsMeasure;
    int position;
    float widthFactor = 0.0F;

    public LayoutParams()
    {
      super(-1, -1);
    }

    public LayoutParams(Context paramContext, AttributeSet paramAttributeSet)
    {
      super(paramContext, paramAttributeSet);
      TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, ViewPager.LAYOUT_ATTRS);
      this.gravity = localTypedArray.getInteger(0, 48);
      localTypedArray.recycle();
    }
  }

  class MyAccessibilityDelegate extends AccessibilityDelegateCompat
  {
    MyAccessibilityDelegate()
    {
    }

    public void onInitializeAccessibilityEvent(View paramView, AccessibilityEvent paramAccessibilityEvent)
    {
      super.onInitializeAccessibilityEvent(paramView, paramAccessibilityEvent);
      paramAccessibilityEvent.setClassName(ViewPager.class.getName());
    }

    public void onInitializeAccessibilityNodeInfo(View paramView, AccessibilityNodeInfoCompat paramAccessibilityNodeInfoCompat)
    {
      int i = 1;
      super.onInitializeAccessibilityNodeInfo(paramView, paramAccessibilityNodeInfoCompat);
      paramAccessibilityNodeInfoCompat.setClassName(ViewPager.class.getName());
      if ((ViewPager.this.mAdapter != null) && (ViewPager.this.mAdapter.getCount() > i));
      while (true)
      {
        paramAccessibilityNodeInfoCompat.setScrollable(i);
        if ((ViewPager.this.mAdapter != null) && (ViewPager.this.mCurItem >= 0) && (ViewPager.this.mCurItem < -1 + ViewPager.this.mAdapter.getCount()))
          paramAccessibilityNodeInfoCompat.addAction(4096);
        if ((ViewPager.this.mAdapter != null) && (ViewPager.this.mCurItem > 0) && (ViewPager.this.mCurItem < ViewPager.this.mAdapter.getCount()))
          paramAccessibilityNodeInfoCompat.addAction(8192);
        return;
        int j = 0;
      }
    }

    public boolean performAccessibilityAction(View paramView, int paramInt, Bundle paramBundle)
    {
      int i = 1;
      if (super.performAccessibilityAction(paramView, paramInt, paramBundle));
      while (true)
      {
        return i;
        switch (paramInt)
        {
        default:
          i = 0;
          break;
        case 4096:
          if ((ViewPager.this.mAdapter != null) && (ViewPager.this.mCurItem >= 0) && (ViewPager.this.mCurItem < -1 + ViewPager.this.mAdapter.getCount()))
            ViewPager.this.setCurrentItem(1 + ViewPager.this.mCurItem);
          i = 0;
          break;
        case 8192:
        }
        if ((ViewPager.this.mAdapter != null) && (ViewPager.this.mCurItem > 0) && (ViewPager.this.mCurItem < ViewPager.this.mAdapter.getCount()))
          ViewPager.this.setCurrentItem(-1 + ViewPager.this.mCurItem);
        i = 0;
      }
    }
  }

  static abstract interface OnAdapterChangeListener
  {
    public abstract void onAdapterChanged(PagerAdapter paramPagerAdapter1, PagerAdapter paramPagerAdapter2);
  }

  public static abstract interface OnPageChangeListener
  {
    public abstract void onPageScrollStateChanged(int paramInt);

    public abstract void onPageScrolled(int paramInt1, float paramFloat, int paramInt2);

    public abstract void onPageSelected(int paramInt);
  }

  public static abstract interface PageTransformer
  {
    public abstract void transformPage(View paramView, float paramFloat);
  }

  private class PagerObserver extends DataSetObserver
  {
    private PagerObserver()
    {
    }

    public void onChanged()
    {
      ViewPager.this.dataSetChanged();
    }

    public void onInvalidated()
    {
      ViewPager.this.dataSetChanged();
    }
  }

  public static class SavedState extends View.BaseSavedState
  {
    public static final Parcelable.Creator<SavedState> CREATOR = ParcelableCompat.newCreator(new ViewPager.SavedState.1());
    Parcelable adapterState;
    ClassLoader loader;
    int position;

    SavedState(Parcel paramParcel, ClassLoader paramClassLoader)
    {
      super(paramParcel);
      if (paramClassLoader == null)
        paramClassLoader = super.getClass().getClassLoader();
      this.position = paramParcel.readInt();
      this.adapterState = paramParcel.readParcelable(paramClassLoader);
      this.loader = paramClassLoader;
    }

    public SavedState(Parcelable paramParcelable)
    {
      super(paramParcelable);
    }

    public String toString()
    {
      return "FragmentPager.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " position=" + this.position + "}";
    }

    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      paramParcel.writeInt(this.position);
      paramParcel.writeParcelable(this.adapterState, paramInt);
    }
  }

  public static class SimpleOnPageChangeListener
    implements ViewPager.OnPageChangeListener
  {
    public void onPageScrollStateChanged(int paramInt)
    {
    }

    public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2)
    {
    }

    public void onPageSelected(int paramInt)
    {
    }
  }

  static class ViewPositionComparator
    implements Comparator<View>
  {
    public int compare(View paramView1, View paramView2)
    {
      ViewPager.LayoutParams localLayoutParams1 = (ViewPager.LayoutParams)paramView1.getLayoutParams();
      ViewPager.LayoutParams localLayoutParams2 = (ViewPager.LayoutParams)paramView2.getLayoutParams();
      if (localLayoutParams1.isDecor != localLayoutParams2.isDecor)
        if (!localLayoutParams1.isDecor);
      for (int i = 1; ; i = localLayoutParams1.position - localLayoutParams2.position)
        while (true)
        {
          return i;
          i = -1;
        }
    }
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     android.support.v4.view.ViewPager
 * JD-Core Version:    0.5.4
 */