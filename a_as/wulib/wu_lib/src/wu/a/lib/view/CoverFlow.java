package wu.a.lib.view;

import wu.a.lib.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.Gallery;

/**
 * 继承自Gallery，重写getChildStaticTransformation方法，根据item据view中心位置（远近不同）的条件，
 * 进行不同程度的变化处理
 * 
 * <pre>
 * <font color='red'>ItemView的LayoutParmar需要指定高宽,否则旋转会变形,待完善...</font>
 * 这个就是CoverFlow类，说明几点：
 * 1. 成员函数
 * mCamera是用来做类3D效果处理,比如z轴方向上的平移,绕y轴的旋转等
 * mMaxRotationAngle是图片绕y轴最大旋转角度,也就是屏幕最边上那两张图片的旋转角度
 * mMaxZoom是图片在z轴平移的距离,视觉上看起来就是放大缩小的效果.
 * 其他的变量都可以无视
 * setStaticTransformationsEnabled(true)也就是说把这个属性设成true的时候每次viewGroup(看Gallery的源码就可以看到它是从ViewGroup间接继承过来的)
 * 在重新画它的child的时候都会促发getChildStaticTransformation这个函数,所以我们只需要在这个函数里面去加
 * 上旋转和放大的操作就可以了
 * 其他的getter和setter函数都可以无视
 * </pre>
 * 
 * @author junxu.wang
 * 
 */
public class CoverFlow extends Gallery {
	/** 是用来做类3D效果处理,比如z轴方向上的平移,绕y轴的旋转等 */
	private static final Camera mCamera = new Camera();

	private static final PaintFlagsDrawFilter filter = new PaintFlagsDrawFilter(
			0, Paint.FILTER_BITMAP_FLAG);

	/** 是图片绕y轴最大旋转角度,也就是屏幕最边上那两张图片的旋转角度 */
	private float mMaxRotationAngleY = 55;

	/** 在z轴的最大平移的距离,视觉上看起来就是放大缩小的效果 */
	private float mMaxTranslateValueZ = 255;

	/** Y轴变换,垂直效果 */
	private float mMaxTranslateValueY = -55;

	/** X轴变换,垂直效果 */
	private float mMaxTranslateValueX = 55;

	/** X方向中心位置 */
	private float mCoveflowCenter;

	/** z轴变换(缩放[+,-])效果 */
	private boolean isTranslateModeZ = false;

	/** 透明度效果 */
	private boolean isAlphaMode = false;

	/** Y轴平移,垂直效果 ,使用Camera变换的,组件事件位置与ui位置错位,待修改 */
	private boolean isTranslateModeY = false;

	/** X轴平移 * */
	private boolean isTranslateModeX = false;

	/** Y轴旋转 * */
	private boolean isRotationModeY = false;

	public CoverFlow(Context context) {

		super(context);
		init();
	}

	public CoverFlow(Context context, AttributeSet attrs) {

		super(context, attrs);
		parseAttributes(context, attrs);
		init();

	}

	public CoverFlow(Context context, AttributeSet attrs, int defStyle) {

		super(context, attrs, defStyle);
		parseAttributes(context, attrs);
		init();

	}

	private void init() {

		/*
		 * 也就是说把这个属性设成true的时候每次viewGroup(看Gallery的源码就可以看到它是从ViewGroup间接继承过来的)
		 * 在重新画它的child的时候都会促发getChildStaticTransformation这个函数
		 * ,所以我们只需要在这个函数里面去加上旋转和放大的操作就可以了
		 */
		this.setStaticTransformationsEnabled(true);

		setSpacing(-50);
	}

	
	public float getMaxRotationAngleY() {
		return mMaxRotationAngleY;
	}

	public float getMaxTranslateValueZ() {
		return mMaxTranslateValueZ;
	}

	public float getMaxTranslateValueY() {
		return mMaxTranslateValueY;
	}

	public float getMaxTranslateValueX() {
		return mMaxTranslateValueX;
	}

	public boolean isTranslateModeZ() {
		return isTranslateModeZ;
	}

	public boolean isAlphaMode() {
		return isAlphaMode;
	}

	public boolean isTranslateModeY() {
		return isTranslateModeY;
	}

	public boolean isTranslateModeX() {
		return isTranslateModeX;
	}

	public boolean isRotationModeY() {
		return isRotationModeY;
	}

	public void setMaxRotationAngleY(float mMaxRotationAngleY) {
		this.mMaxRotationAngleY = mMaxRotationAngleY;
	}

	public void setMaxTranslateValueZ(float mMaxTranslateValueZ) {
		this.mMaxTranslateValueZ = mMaxTranslateValueZ;
	}

	public void setMaxTranslateValueY(float mMaxTranslateValueY) {
		this.mMaxTranslateValueY = mMaxTranslateValueY;
	}

	public void setMaxTranslateValueX(float mMaxTranslateValueX) {
		this.mMaxTranslateValueX = mMaxTranslateValueX;
	}

	public void setTranslateModeZ(boolean isTranslateModeZ) {
		this.isTranslateModeZ = isTranslateModeZ;
	}

	public void setTranslateModeY(boolean isTranslateModeY) {
		this.isTranslateModeY = isTranslateModeY;
	}

	public void setTranslateModeX(boolean isTranslateModeX) {
		this.isTranslateModeX = isTranslateModeX;
	}

	public void setRotationModeY(boolean isRotationModeY) {
		this.isRotationModeY = isRotationModeY;
	}

	/**
	 * 仅对ImageView有效
	 * 
	 * @param isAlpha
	 */
	@Deprecated
	public void setAlphaMode(boolean isAlpha) {

		isAlphaMode = isAlpha;

	}

	private int getCenterOfCoverflow() {

		// return (getWidth() - getPaddingLeft() - getPaddingRight())/2 +
		// getPaddingLeft();
		return (getWidth() + getPaddingLeft() - getPaddingRight()) >> 1;

	}

	private static int getCenterOfView(View view) {

		return view.getLeft() + (view.getWidth() >> 1);

	}

	@Override
	protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
		canvas.setDrawFilter(filter);
		return super.drawChild(canvas, child, drawingTime);
	}

	protected int getChildDrawingOrder(int childCount, int i) {
		// int selectedIndex = mSelectedPosition - mFirstPosition;
		int selectedIndex = this.getSelectedItemPosition()
				- this.getFirstVisiblePosition();
		// Just to be safe
		if (selectedIndex < 0)
			return i;

		if (i == childCount - 1) {
			// Draw the selected child last
			return selectedIndex;
		} else if (i >= selectedIndex) {
			// Move the children after the selected child earlier one
			return childCount - (i - selectedIndex) - 1;
			// return i + 1;
		} else {
			// Keep the children before the selected child the same
			return i;
		}
	}

	/**
	 * 
	 * 这就是所谓的在大小的布局时,这一观点已经发生了改变。如果 你只是添加到视图层次,有人叫你旧的观念 价值观为0。
	 * 
	 * @param w
	 *            Current width of this view.
	 * @param h
	 *            Current height of this view.
	 * 
	 * @param oldw
	 *            Old width of this view.
	 * @param oldh
	 *            Old height of this view.
	 */

	protected void onSizeChanged(int w, int h, int oldw, int oldh) {

		mCoveflowCenter = getCenterOfCoverflow();

		super.onSizeChanged(w, h, oldw, oldh);

	}

	/**
	 * ChildView 变化重载 (non-Javadoc)
	 * 
	 * @see android.widget.Gallery#getChildStaticTransformation(android.view.View,
	 *      android.view.animation.Transformation)
	 */
	protected boolean getChildStaticTransformation(View child, Transformation t) {

		final int childCenter = getCenterOfView(child);

		float rotationAngle = 0;

		t.clear();

		t.setTransformationType(Transformation.TYPE_MATRIX);// 动作变换为Matrix

		if (childCenter == mCoveflowCenter) {// view在中心不需变化

			transformImageBitmap(child, t, 0);

		} else {

			/* 两侧需要变化：（居中心间距/子View宽度）*最大弧度 */
			rotationAngle = ((float) (mCoveflowCenter - childCenter) / mCoveflowCenter);

			transformImageBitmap(child, t, rotationAngle);

		}

		return true;

	}

	/**
	 * 对子view的具体变换实现
	 * 
	 * @param child
	 *            Item view
	 * @param t
	 * @param rotationAngle
	 */
	private void transformImageBitmap(View child, Transformation t,
			float rotationAngle) {

		mCamera.save();

		final Matrix imageMatrix = t.getMatrix();

		final int imageHeight = child.getLayoutParams().height;

		final int imageWidth = child.getLayoutParams().width;

		final float rotation = Math.abs(rotationAngle);

		// 在Z轴上正向移动camera的视角，实际效果为放大图片。
		// 如果在Y轴上移动，则图片上下移动；X轴上对应图片左右移动。

		if (isTranslateModeZ) {// z缩放
			float zoomAmount = mMaxTranslateValueZ * rotation;
			mCamera.translate(0.0f, 0.0f, zoomAmount);
		}
		if (isTranslateModeX) {// x平移
			float zoomAmount = mMaxTranslateValueX * rotationAngle;
			mCamera.translate(zoomAmount, 0.0f, 0.0f);
		}

		if (isTranslateModeY) {// y平移
			float zoomAmount = mMaxTranslateValueY * rotation;
			mCamera.translate(0.0f, zoomAmount, 0.0f);
		}

		if (isAlphaMode) {// 0透明，255为全不透明
		// ((ImageView) (child)).setAlpha((int) (255 - rotation * 2.5));
		}

		// 在Y轴上旋转，对应图片竖向向里翻转。
		// 如果在X轴上旋转，则对应图片以X轴横向向里翻转。
		// 不同的组合不同的效果
		if (isRotationModeY) {
			float zoomAmount = mMaxRotationAngleY * rotationAngle
					* mCoveflowCenter / imageWidth;
			zoomAmount = Math.max(zoomAmount, -mMaxRotationAngleY);
			zoomAmount = Math.min(zoomAmount, mMaxRotationAngleY);
			mCamera.rotateY(zoomAmount);
		}

		mCamera.getMatrix(imageMatrix);

		// 两次平移操作
		imageMatrix.preTranslate(-(imageWidth >> 1), -(imageHeight >> 1));

		imageMatrix.postTranslate((imageWidth >> 1), (imageHeight >> 1));

		mCamera.restore();

	}
	private void parseAttributes(final Context context, final AttributeSet attrs) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CoverFlow);
        try {
        	mMaxRotationAngleY = a.getDimension(R.styleable.CoverFlow_mMaxRotationAngleY, mMaxRotationAngleY);
        	mMaxTranslateValueZ = a.getDimension(R.styleable.CoverFlow_mMaxTranslateValueZ, mMaxTranslateValueZ);
        	mMaxTranslateValueY = a.getDimension(R.styleable.CoverFlow_mMaxTranslateValueY, mMaxTranslateValueY);
        	mMaxTranslateValueX = a.getFloat(R.styleable.CoverFlow_mMaxTranslateValueX,mMaxTranslateValueX);
        	isTranslateModeZ = a.getBoolean(R.styleable.CoverFlow_isTranslateModeZ, false);
        	isTranslateModeY = a.getBoolean(R.styleable.CoverFlow_isTranslateModeY, false);
        	isTranslateModeX = a.getBoolean(R.styleable.CoverFlow_isTranslateModeX, false);
        	isRotationModeY = a.getBoolean(R.styleable.CoverFlow_isRotationModeY, false);
        } finally {
            a.recycle();
        }
    }

}