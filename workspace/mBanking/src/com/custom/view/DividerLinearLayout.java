package com.custom.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.act.mbanking.R;

/**
 * @author junxu.wang
 * 
 */
public class DividerLinearLayout extends LinearLayout {

    public DividerLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DividerLinearLayout(Context context) {
        super(context);
        init(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
    	super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onDraw(Canvas canvas) {
    	super.onDraw(canvas);
    }
    
    @Override
    protected void dispatchDraw(Canvas canvas) {
        drawDividers(canvas);
        super.dispatchDraw(canvas);
    }

    @Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        return super.drawChild(canvas, child, drawingTime);
    }

    @Override
    protected void measureChildren(int widthMeasureSpec, int heightMeasureSpec) {
        super.measureChildren(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        super.measureChild(child, parentWidthMeasureSpec, parentHeightMeasureSpec);
    }

    @Override
    protected void measureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed,
            int parentHeightMeasureSpec, int heightUsed) {
        super.measureChildWithMargins(child, parentWidthMeasureSpec, widthUsed,
                parentHeightMeasureSpec, heightUsed);
    }

    public static final String TAG = "ChartView";
    private boolean isSquare = false;
    private Bitmap dividerHorizontal;
    private Bitmap dividerHBitmap;
    private Matrix dividerMtx;

    private void initDraw(int width, int height) {
        if (isSquare) {
            width = height = Math.min(width, height);
        }
        float sy=(float)width/dividerHorizontal.getWidth();
        if(dividerHBitmap!=null){
            dividerHBitmap.recycle();
        }
        if(sy>0){
        dividerMtx.reset();
        dividerMtx.setScale(sy,1);
        dividerHBitmap=Bitmap.createBitmap(dividerHorizontal, 0, 0, dividerHorizontal.getWidth(), dividerHorizontal.getHeight(), dividerMtx,false);
        }
        
        invalidate();
    }

    private void init(Context context) {
    	setOrientation(LinearLayout.VERTICAL);
        dividerMtx=new Matrix();
        dividerHorizontal = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.divider_horizontal);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w != oldw || h != oldh) {
            initDraw(this.getWidth(), this.getHeight());
        }
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public void invalidate() {
        super.invalidate();
    }

    private void drawDividers(Canvas canvas) {
        int count = getChildCount();
        int dividerX = (getWidth() - dividerHBitmap.getWidth())>>1,
        deviderY = (- dividerHBitmap.getHeight())>>1;
        for (int i = 1; i < count; i++) {
            View v = getChildAt(i - 1);
            deviderY += v.getMeasuredHeight();
            canvas.drawBitmap(dividerHBitmap, dividerX, deviderY , null);
        }
    }
}
