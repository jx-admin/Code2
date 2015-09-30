package android.support.v4.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;
import java.lang.ref.WeakReference;

public class PagerTitleStrip extends ViewGroup
  implements ViewPager.Decor
{
  private static final int[] ATTRS;
  private static final PagerTitleStripImpl IMPL;
  private static final float SIDE_ALPHA = 0.6F;
  private static final String TAG = "PagerTitleStrip";
  private static final int[] TEXT_ATTRS;
  private static final int TEXT_SPACING = 16;
  TextView mCurrText;
  private int mGravity;
  private int mLastKnownCurrentPage = -1;
  private float mLastKnownPositionOffset = -1.0F;
  TextView mNextText;
  private int mNonPrimaryAlpha;
  private final PageListener mPageListener = new PageListener(null);
  ViewPager mPager;
  TextView mPrevText;
  private int mScaledTextSpacing;
  int mTextColor;
  private boolean mUpdatingPositions;
  private boolean mUpdatingText;
  private WeakReference<PagerAdapter> mWatchingAdapter;

  static
  {
    int[] arrayOfInt1 = new int[4];
    arrayOfInt1[0] = 16842804;
    arrayOfInt1[1] = 16842901;
    arrayOfInt1[2] = 16842904;
    arrayOfInt1[3] = 16842927;
    ATTRS = arrayOfInt1;
    int[] arrayOfInt2 = new int[1];
    arrayOfInt2[0] = 16843660;
    TEXT_ATTRS = arrayOfInt2;
    if (Build.VERSION.SDK_INT >= 14)
      IMPL = new PagerTitleStripImplIcs();
    while (true)
    {
      return;
      IMPL = new PagerTitleStripImplBase();
    }
  }

  public PagerTitleStrip(Context paramContext)
  {
    this(paramContext, null);
  }

  public PagerTitleStrip(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    TextView localTextView1 = new TextView(paramContext);
    this.mPrevText = localTextView1;
    addView(localTextView1);
    TextView localTextView2 = new TextView(paramContext);
    this.mCurrText = localTextView2;
    addView(localTextView2);
    TextView localTextView3 = new TextView(paramContext);
    this.mNextText = localTextView3;
    addView(localTextView3);
    TypedArray localTypedArray1 = paramContext.obtainStyledAttributes(paramAttributeSet, ATTRS);
    int i = localTypedArray1.getResourceId(0, 0);
    if (i != 0)
    {
      this.mPrevText.setTextAppearance(paramContext, i);
      this.mCurrText.setTextAppearance(paramContext, i);
      this.mNextText.setTextAppearance(paramContext, i);
    }
    int j = localTypedArray1.getDimensionPixelSize(1, 0);
    if (j != 0)
      setTextSize(0, j);
    if (localTypedArray1.hasValue(2))
    {
      int k = localTypedArray1.getColor(2, 0);
      this.mPrevText.setTextColor(k);
      this.mCurrText.setTextColor(k);
      this.mNextText.setTextColor(k);
    }
    this.mGravity = localTypedArray1.getInteger(3, 80);
    localTypedArray1.recycle();
    this.mTextColor = this.mCurrText.getTextColors().getDefaultColor();
    setNonPrimaryAlpha(0.6F);
    this.mPrevText.setEllipsize(TextUtils.TruncateAt.END);
    this.mCurrText.setEllipsize(TextUtils.TruncateAt.END);
    this.mNextText.setEllipsize(TextUtils.TruncateAt.END);
    boolean bool = false;
    if (i != 0)
    {
      TypedArray localTypedArray2 = paramContext.obtainStyledAttributes(i, TEXT_ATTRS);
      bool = localTypedArray2.getBoolean(0, false);
      localTypedArray2.recycle();
    }
    if (bool)
    {
      setSingleLineAllCaps(this.mPrevText);
      setSingleLineAllCaps(this.mCurrText);
      setSingleLineAllCaps(this.mNextText);
    }
    while (true)
    {
      this.mScaledTextSpacing = (int)(16.0F * paramContext.getResources().getDisplayMetrics().density);
      return;
      this.mPrevText.setSingleLine();
      this.mCurrText.setSingleLine();
      this.mNextText.setSingleLine();
    }
  }

  private static void setSingleLineAllCaps(TextView paramTextView)
  {
    IMPL.setSingleLineAllCaps(paramTextView);
  }

  int getMinHeight()
  {
    int i = 0;
    Drawable localDrawable = getBackground();
    if (localDrawable != null)
      i = localDrawable.getIntrinsicHeight();
    return i;
  }

  public int getTextSpacing()
  {
    return this.mScaledTextSpacing;
  }

  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    ViewParent localViewParent = getParent();
    if (!localViewParent instanceof ViewPager)
      throw new IllegalStateException("PagerTitleStrip must be a direct child of a ViewPager.");
    ViewPager localViewPager = (ViewPager)localViewParent;
    PagerAdapter localPagerAdapter1 = localViewPager.getAdapter();
    localViewPager.setInternalPageChangeListener(this.mPageListener);
    localViewPager.setOnAdapterChangeListener(this.mPageListener);
    this.mPager = localViewPager;
    if (this.mWatchingAdapter != null);
    for (PagerAdapter localPagerAdapter2 = (PagerAdapter)this.mWatchingAdapter.get(); ; localPagerAdapter2 = null)
    {
      updateAdapter(localPagerAdapter2, localPagerAdapter1);
      return;
    }
  }

  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    if (this.mPager == null)
      return;
    updateAdapter(this.mPager.getAdapter(), null);
    this.mPager.setInternalPageChangeListener(null);
    this.mPager.setOnAdapterChangeListener(null);
    this.mPager = null;
  }

  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    float f = 0.0F;
    if (this.mPager == null)
      return;
    if (this.mLastKnownPositionOffset >= 0.0F)
      f = this.mLastKnownPositionOffset;
    updateTextPositions(this.mLastKnownCurrentPage, f, true);
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i = View.MeasureSpec.getMode(paramInt1);
    int j = View.MeasureSpec.getMode(paramInt2);
    int k = View.MeasureSpec.getSize(paramInt1);
    int l = View.MeasureSpec.getSize(paramInt2);
    if (i != 1073741824)
      throw new IllegalStateException("Must measure with an exact width");
    int i1 = getMinHeight();
    int i2 = getPaddingTop() + getPaddingBottom();
    int i3 = l - i2;
    int i4 = View.MeasureSpec.makeMeasureSpec((int)(0.8F * k), -2147483648);
    int i5 = View.MeasureSpec.makeMeasureSpec(i3, -2147483648);
    this.mPrevText.measure(i4, i5);
    this.mCurrText.measure(i4, i5);
    this.mNextText.measure(i4, i5);
    if (j == 1073741824)
      setMeasuredDimension(k, l);
    while (true)
    {
      return;
      setMeasuredDimension(k, Math.max(i1, i2 + this.mCurrText.getMeasuredHeight()));
    }
  }

  public void requestLayout()
  {
    if (this.mUpdatingText)
      return;
    super.requestLayout();
  }

  public void setGravity(int paramInt)
  {
    this.mGravity = paramInt;
    requestLayout();
  }

  public void setNonPrimaryAlpha(float paramFloat)
  {
    this.mNonPrimaryAlpha = (0xFF & (int)(255.0F * paramFloat));
    int i = this.mNonPrimaryAlpha << 24 | 0xFFFFFF & this.mTextColor;
    this.mPrevText.setTextColor(i);
    this.mNextText.setTextColor(i);
  }

  public void setTextColor(int paramInt)
  {
    this.mTextColor = paramInt;
    this.mCurrText.setTextColor(paramInt);
    int i = this.mNonPrimaryAlpha << 24 | 0xFFFFFF & this.mTextColor;
    this.mPrevText.setTextColor(i);
    this.mNextText.setTextColor(i);
  }

  public void setTextSize(int paramInt, float paramFloat)
  {
    this.mPrevText.setTextSize(paramInt, paramFloat);
    this.mCurrText.setTextSize(paramInt, paramFloat);
    this.mNextText.setTextSize(paramInt, paramFloat);
  }

  public void setTextSpacing(int paramInt)
  {
    this.mScaledTextSpacing = paramInt;
    requestLayout();
  }

  void updateAdapter(PagerAdapter paramPagerAdapter1, PagerAdapter paramPagerAdapter2)
  {
    if (paramPagerAdapter1 != null)
    {
      paramPagerAdapter1.unregisterDataSetObserver(this.mPageListener);
      this.mWatchingAdapter = null;
    }
    if (paramPagerAdapter2 != null)
    {
      paramPagerAdapter2.registerDataSetObserver(this.mPageListener);
      this.mWatchingAdapter = new WeakReference(paramPagerAdapter2);
    }
    if (this.mPager == null)
      return;
    this.mLastKnownCurrentPage = -1;
    this.mLastKnownPositionOffset = -1.0F;
    updateText(this.mPager.getCurrentItem(), paramPagerAdapter2);
    requestLayout();
  }

  void updateText(int paramInt, PagerAdapter paramPagerAdapter)
  {
    int i;
    label9: TextView localTextView;
    if (paramPagerAdapter != null)
    {
      i = paramPagerAdapter.getCount();
      this.mUpdatingText = true;
      CharSequence localCharSequence1 = null;
      if ((paramInt >= 1) && (paramPagerAdapter != null))
        localCharSequence1 = paramPagerAdapter.getPageTitle(paramInt - 1);
      this.mPrevText.setText(localCharSequence1);
      localTextView = this.mCurrText;
      if ((paramPagerAdapter == null) || (paramInt >= i))
        break label229;
    }
    for (CharSequence localCharSequence2 = paramPagerAdapter.getPageTitle(paramInt); ; localCharSequence2 = null)
    {
      localTextView.setText(localCharSequence2);
      CharSequence localCharSequence3 = null;
      if ((paramInt + 1 < i) && (paramPagerAdapter != null))
        localCharSequence3 = paramPagerAdapter.getPageTitle(paramInt + 1);
      this.mNextText.setText(localCharSequence3);
      int k = getWidth() - getPaddingLeft() - getPaddingRight();
      int l = getHeight() - getPaddingTop() - getPaddingBottom();
      int i1 = View.MeasureSpec.makeMeasureSpec((int)(0.8F * k), -2147483648);
      int i2 = View.MeasureSpec.makeMeasureSpec(l, -2147483648);
      this.mPrevText.measure(i1, i2);
      this.mCurrText.measure(i1, i2);
      this.mNextText.measure(i1, i2);
      this.mLastKnownCurrentPage = paramInt;
      if (!this.mUpdatingPositions)
        updateTextPositions(paramInt, this.mLastKnownPositionOffset, false);
      this.mUpdatingText = false;
      return;
      int j = 0;
      label229: break label9:
    }
  }

  void updateTextPositions(int paramInt, float paramFloat, boolean paramBoolean)
  {
    int i;
    int k;
    int i1;
    int i2;
    int i3;
    int i4;
    int i5;
    int i6;
    int i10;
    int i11;
    int i16;
    int i17;
    int i18;
    int i22;
    int i24;
    int i25;
    if (paramInt != this.mLastKnownCurrentPage)
    {
      updateText(paramInt, this.mPager.getAdapter());
      this.mUpdatingPositions = true;
      i = this.mPrevText.getMeasuredWidth();
      int j = this.mCurrText.getMeasuredWidth();
      k = this.mNextText.getMeasuredWidth();
      int l = j / 2;
      i1 = getWidth();
      i2 = getHeight();
      i3 = getPaddingLeft();
      i4 = getPaddingRight();
      i5 = getPaddingTop();
      i6 = getPaddingBottom();
      int i7 = i3 + l;
      int i8 = i4 + l;
      int i9 = i1 - i7 - i8;
      float f = paramFloat + 0.5F;
      if (f > 1.0F)
        f -= 1.0F;
      i10 = i1 - i8 - (int)(f * i9) - j / 2;
      i11 = i10 + j;
      int i12 = this.mPrevText.getBaseline();
      int i13 = this.mCurrText.getBaseline();
      int i14 = this.mNextText.getBaseline();
      int i15 = Math.max(Math.max(i12, i13), i14);
      i16 = i15 - i12;
      i17 = i15 - i13;
      i18 = i15 - i14;
      int i19 = i16 + this.mPrevText.getMeasuredHeight();
      int i20 = i17 + this.mCurrText.getMeasuredHeight();
      int i21 = i18 + this.mNextText.getMeasuredHeight();
      i22 = Math.max(Math.max(i19, i20), i21);
      switch (0x70 & this.mGravity)
      {
      default:
        i24 = i5 + i16;
        i25 = i5 + i17;
      case 16:
      case 80:
      }
    }
    int i23;
    for (int i26 = i5 + i18; ; i26 = i23 + i18)
    {
      while (true)
      {
        TextView localTextView1 = this.mCurrText;
        int i27 = i25 + this.mCurrText.getMeasuredHeight();
        localTextView1.layout(i10, i25, i11, i27);
        int i28 = Math.min(i3, i10 - this.mScaledTextSpacing - i);
        TextView localTextView2 = this.mPrevText;
        int i29 = i28 + i;
        int i30 = i24 + this.mPrevText.getMeasuredHeight();
        localTextView2.layout(i28, i24, i29, i30);
        int i31 = Math.max(i1 - i4 - k, i11 + this.mScaledTextSpacing);
        TextView localTextView3 = this.mNextText;
        int i32 = i31 + k;
        int i33 = i26 + this.mNextText.getMeasuredHeight();
        localTextView3.layout(i31, i26, i32, i33);
        this.mLastKnownPositionOffset = paramFloat;
        this.mUpdatingPositions = false;
        while (true)
        {
          return;
          if ((paramBoolean) || (paramFloat != this.mLastKnownPositionOffset));
        }
        int i34 = (i2 - i5 - i6 - i22) / 2;
        i24 = i34 + i16;
        i25 = i34 + i17;
        i26 = i34 + i18;
      }
      i23 = i2 - i6 - i22;
      i24 = i23 + i16;
      i25 = i23 + i17;
    }
  }

  private class PageListener extends DataSetObserver
    implements ViewPager.OnPageChangeListener, ViewPager.OnAdapterChangeListener
  {
    private int mScrollState;

    private PageListener()
    {
    }

    public void onAdapterChanged(PagerAdapter paramPagerAdapter1, PagerAdapter paramPagerAdapter2)
    {
      PagerTitleStrip.this.updateAdapter(paramPagerAdapter1, paramPagerAdapter2);
    }

    public void onChanged()
    {
      float f = 0.0F;
      PagerTitleStrip.this.updateText(PagerTitleStrip.this.mPager.getCurrentItem(), PagerTitleStrip.this.mPager.getAdapter());
      if (PagerTitleStrip.this.mLastKnownPositionOffset >= 0.0F)
        f = PagerTitleStrip.this.mLastKnownPositionOffset;
      PagerTitleStrip.this.updateTextPositions(PagerTitleStrip.this.mPager.getCurrentItem(), f, true);
    }

    public void onPageScrollStateChanged(int paramInt)
    {
      this.mScrollState = paramInt;
    }

    public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2)
    {
      if (paramFloat > 0.5F);
      PagerTitleStrip.this.updateTextPositions(++paramInt1, paramFloat, false);
    }

    public void onPageSelected(int paramInt)
    {
      float f = 0.0F;
      if (this.mScrollState != 0)
        return;
      PagerTitleStrip.this.updateText(PagerTitleStrip.this.mPager.getCurrentItem(), PagerTitleStrip.this.mPager.getAdapter());
      if (PagerTitleStrip.this.mLastKnownPositionOffset >= 0.0F)
        f = PagerTitleStrip.this.mLastKnownPositionOffset;
      PagerTitleStrip.this.updateTextPositions(PagerTitleStrip.this.mPager.getCurrentItem(), f, true);
    }
  }

  static abstract interface PagerTitleStripImpl
  {
    public abstract void setSingleLineAllCaps(TextView paramTextView);
  }

  static class PagerTitleStripImplBase
    implements PagerTitleStrip.PagerTitleStripImpl
  {
    public void setSingleLineAllCaps(TextView paramTextView)
    {
      paramTextView.setSingleLine();
    }
  }

  static class PagerTitleStripImplIcs
    implements PagerTitleStrip.PagerTitleStripImpl
  {
    public void setSingleLineAllCaps(TextView paramTextView)
    {
      PagerTitleStripIcs.setSingleLineAllCaps(paramTextView);
    }
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     android.support.v4.view.PagerTitleStrip
 * JD-Core Version:    0.5.4
 */