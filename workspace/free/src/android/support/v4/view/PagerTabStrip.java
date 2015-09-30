package android.support.v4.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.TextView;

public class PagerTabStrip extends PagerTitleStrip
{
  private static final int FULL_UNDERLINE_HEIGHT = 1;
  private static final int INDICATOR_HEIGHT = 3;
  private static final int MIN_PADDING_BOTTOM = 6;
  private static final int MIN_STRIP_HEIGHT = 32;
  private static final int MIN_TEXT_SPACING = 64;
  private static final int TAB_PADDING = 16;
  private static final int TAB_SPACING = 32;
  private static final String TAG = "PagerTabStrip";
  private boolean mDrawFullUnderline = false;
  private boolean mDrawFullUnderlineSet = false;
  private int mFullUnderlineHeight;
  private boolean mIgnoreTap;
  private int mIndicatorColor = this.mTextColor;
  private int mIndicatorHeight;
  private float mInitialMotionX;
  private float mInitialMotionY;
  private int mMinPaddingBottom;
  private int mMinStripHeight;
  private int mMinTextSpacing;
  private int mTabAlpha = 255;
  private int mTabPadding;
  private final Paint mTabPaint = new Paint();
  private final Rect mTempRect = new Rect();
  private int mTouchSlop;

  public PagerTabStrip(Context paramContext)
  {
    this(paramContext, null);
  }

  public PagerTabStrip(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.mTabPaint.setColor(this.mIndicatorColor);
    float f = paramContext.getResources().getDisplayMetrics().density;
    this.mIndicatorHeight = (int)(0.5F + 3.0F * f);
    this.mMinPaddingBottom = (int)(0.5F + 6.0F * f);
    this.mMinTextSpacing = (int)(64.0F * f);
    this.mTabPadding = (int)(0.5F + 16.0F * f);
    this.mFullUnderlineHeight = (int)(0.5F + 1.0F * f);
    this.mMinStripHeight = (int)(0.5F + 32.0F * f);
    this.mTouchSlop = ViewConfiguration.get(paramContext).getScaledTouchSlop();
    setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom());
    setTextSpacing(getTextSpacing());
    setWillNotDraw(false);
    this.mPrevText.setFocusable(true);
    this.mPrevText.setOnClickListener(new PagerTabStrip.1(this));
    this.mNextText.setFocusable(true);
    this.mNextText.setOnClickListener(new PagerTabStrip.2(this));
    if (getBackground() != null)
      return;
    this.mDrawFullUnderline = true;
  }

  public boolean getDrawFullUnderline()
  {
    return this.mDrawFullUnderline;
  }

  int getMinHeight()
  {
    return Math.max(super.getMinHeight(), this.mMinStripHeight);
  }

  public int getTabIndicatorColor()
  {
    return this.mIndicatorColor;
  }

  protected void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    int i = getHeight();
    int j = this.mCurrText.getLeft() - this.mTabPadding;
    int k = this.mCurrText.getRight() + this.mTabPadding;
    int l = i - this.mIndicatorHeight;
    this.mTabPaint.setColor(this.mTabAlpha << 24 | 0xFFFFFF & this.mIndicatorColor);
    paramCanvas.drawRect(j, l, k, i, this.mTabPaint);
    if (!this.mDrawFullUnderline)
      return;
    this.mTabPaint.setColor(0xFF000000 | 0xFFFFFF & this.mIndicatorColor);
    paramCanvas.drawRect(getPaddingLeft(), i - this.mFullUnderlineHeight, getWidth() - getPaddingRight(), i, this.mTabPaint);
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    int i = 0;
    int j = paramMotionEvent.getAction();
    if ((j != 0) && (this.mIgnoreTap))
      label18: return i;
    float f1 = paramMotionEvent.getX();
    float f2 = paramMotionEvent.getY();
    switch (j)
    {
    default:
    case 0:
    case 2:
    case 1:
    }
    while (true)
    {
      i = 1;
      break label18:
      this.mInitialMotionX = f1;
      this.mInitialMotionY = f2;
      this.mIgnoreTap = false;
      continue;
      if ((Math.abs(f1 - this.mInitialMotionX) <= this.mTouchSlop) && (Math.abs(f2 - this.mInitialMotionY) <= this.mTouchSlop))
        continue;
      this.mIgnoreTap = true;
      continue;
      if (f1 < this.mCurrText.getLeft() - this.mTabPadding)
        this.mPager.setCurrentItem(-1 + this.mPager.getCurrentItem());
      if (f1 <= this.mCurrText.getRight() + this.mTabPadding)
        continue;
      this.mPager.setCurrentItem(1 + this.mPager.getCurrentItem());
    }
  }

  public void setBackgroundColor(int paramInt)
  {
    super.setBackgroundColor(paramInt);
    if (!this.mDrawFullUnderlineSet)
      if ((0xFF000000 & paramInt) != 0)
        break label27;
    for (int i = 1; ; i = 0)
    {
      this.mDrawFullUnderline = i;
      label27: return;
    }
  }

  public void setBackgroundDrawable(Drawable paramDrawable)
  {
    super.setBackgroundDrawable(paramDrawable);
    if (!this.mDrawFullUnderlineSet)
      if (paramDrawable != null)
        break label24;
    for (int i = 1; ; i = 0)
    {
      this.mDrawFullUnderline = i;
      label24: return;
    }
  }

  public void setBackgroundResource(int paramInt)
  {
    super.setBackgroundResource(paramInt);
    if (!this.mDrawFullUnderlineSet)
      if (paramInt != 0)
        break label24;
    for (int i = 1; ; i = 0)
    {
      this.mDrawFullUnderline = i;
      label24: return;
    }
  }

  public void setDrawFullUnderline(boolean paramBoolean)
  {
    this.mDrawFullUnderline = paramBoolean;
    this.mDrawFullUnderlineSet = true;
    invalidate();
  }

  public void setPadding(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (paramInt4 < this.mMinPaddingBottom)
      paramInt4 = this.mMinPaddingBottom;
    super.setPadding(paramInt1, paramInt2, paramInt3, paramInt4);
  }

  public void setTabIndicatorColor(int paramInt)
  {
    this.mIndicatorColor = paramInt;
    this.mTabPaint.setColor(this.mIndicatorColor);
    invalidate();
  }

  public void setTabIndicatorColorResource(int paramInt)
  {
    setTabIndicatorColor(getContext().getResources().getColor(paramInt));
  }

  public void setTextSpacing(int paramInt)
  {
    if (paramInt < this.mMinTextSpacing)
      paramInt = this.mMinTextSpacing;
    super.setTextSpacing(paramInt);
  }

  void updateTextPositions(int paramInt, float paramFloat, boolean paramBoolean)
  {
    Rect localRect = this.mTempRect;
    int i = getHeight();
    int j = this.mCurrText.getLeft() - this.mTabPadding;
    int k = this.mCurrText.getRight() + this.mTabPadding;
    int l = i - this.mIndicatorHeight;
    localRect.set(j, l, k, i);
    super.updateTextPositions(paramInt, paramFloat, paramBoolean);
    this.mTabAlpha = (int)(255.0F * (2.0F * Math.abs(paramFloat - 0.5F)));
    localRect.union(this.mCurrText.getLeft() - this.mTabPadding, l, this.mCurrText.getRight() + this.mTabPadding, i);
    invalidate(localRect);
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     android.support.v4.view.PagerTabStrip
 * JD-Core Version:    0.5.4
 */