
package com.act.mbanking.view;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.Gallery;

import com.act.mbanking.utils.LogManager;

/**
 * @author junxu.wang
 */
public class Switch3DGallery extends Gallery {
    public static final String TAG = "CoverFlow";

    private Camera mCamera = new Camera();

    PaintFlagsDrawFilter filter = new PaintFlagsDrawFilter(0, Paint.FILTER_BITMAP_FLAG);

    private int mMaxRotationAngle = 50;

    private int mMaxZoom = -380;

    private int mCoveflowCenter;

    public Switch3DGallery(Context context) {
        super(context);
        this.setStaticTransformationsEnabled(true);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        canvas.setDrawFilter(filter);
        return super.drawChild(canvas, child, drawingTime);
    }

    public Switch3DGallery(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setStaticTransformationsEnabled(true);
    }

    public Switch3DGallery(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setStaticTransformationsEnabled(true);
    }

    public int getMaxRotationAngle() {
        return mMaxRotationAngle;
    }

    public void setMaxRotationAngle(int maxRotationAngle) {
        mMaxRotationAngle = maxRotationAngle;
    }

    public int getMaxZoom() {
        return mMaxZoom;

    }

    public void setMaxZoom(int maxZoom) {
        mMaxZoom = maxZoom;
    }

    private int getCenterOfCoverflow() {
        return (getWidth() + getPaddingLeft() - getPaddingRight()) >> 1;
    }

    private static int getCenterOfView(View view) {

        return view.getLeft() + view.getWidth() / 2;

    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        mCoveflowCenter = getCenterOfCoverflow();

        super.onSizeChanged(w, h, oldw, oldh);

    }

    protected boolean getChildStaticTransformation(View child, Transformation t) {

        final int childCenter = getCenterOfView(child);

        final int childWidth = child.getWidth();

        int rotationAngle = 0;

        t.clear();

        t.setTransformationType(Transformation.TYPE_MATRIX);

        LogManager.d("childCenter" + childCenter + "mCoveflowCenter" + mCoveflowCenter
                + "rotationAngle" + rotationAngle);

        if (childCenter == mCoveflowCenter) {

            transformImageBitmap(child, t, 0);

        } else {

            rotationAngle = (int)(((float)(mCoveflowCenter - childCenter) / childWidth) * mMaxRotationAngle);

            if (Math.abs(rotationAngle) > mMaxRotationAngle) {
                rotationAngle = (rotationAngle < 0) ? -mMaxRotationAngle : mMaxRotationAngle;
            }
            transformImageBitmap(child, t, rotationAngle);
        }
        LogManager.d("childCenter" + childCenter + "mCoveflowCenter" + mCoveflowCenter
                + "rotationAngle" + rotationAngle);
        return true;

    }

    protected int getChildDrawingOrder(int childCount, int i) {
        // int selectedIndex = mSelectedPosition - mFirstPosition;
        int selectedIndex = this.getSelectedItemPosition() - this.getFirstVisiblePosition();
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

    private void transformImageBitmap(View child, Transformation t, int rotationAngle) {

        mCamera.save();

        final Matrix imageMatrix = t.getMatrix();

        final int imageHeight = child.getLayoutParams().height;

        final int imageWidth = child.getLayoutParams().width;

        final int childCenter = getCenterOfView(child);

        int trx = (int)(0.5f * (mCoveflowCenter - childCenter));
        mCamera.translate(trx, 0.0f, (float)Math.abs(rotationAngle));
        mCamera.rotateY(rotationAngle);

        mCamera.getMatrix(imageMatrix);

        imageMatrix.preTranslate(-(imageWidth / 2), -(imageHeight / 2));
        imageMatrix.postTranslate((imageWidth / 2), (imageHeight / 2));
        mCamera.restore();

    }


}
