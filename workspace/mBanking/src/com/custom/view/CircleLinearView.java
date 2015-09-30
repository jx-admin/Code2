package com.custom.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.act.mbanking.R;

/**
 * @author junxu.wang
 * 
 */
public class CircleLinearView extends ViewGroup {

    public CircleLinearView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public CircleLinearView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CircleLinearView(Context context) {
        super(context);
        init(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        upDate(rf4);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    
    @Override
    protected void dispatchDraw(Canvas canvas) {
        drawBack(canvas);
        drawDividers(canvas,rf4);
    	canvas.save();
        // canvas.translate(rf5.left,rf5.top);
        mPath.reset();
        canvas.clipPath(mPath); // makes the clip empty
        mPath.addOval(rf4, Path.Direction.CCW);
        canvas.clipPath(mPath, Region.Op.REPLACE);
        // drawLinear(canvas);
        super.dispatchDraw(canvas);
        canvas.restore();
    }
    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        // TODO Auto-generated method stub
        return super.drawChild(canvas, child, drawingTime);
    }

    @Override
    protected void measureChildren(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.measureChildren(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        // TODO Auto-generated method stub
        super.measureChild(child, parentWidthMeasureSpec, parentHeightMeasureSpec);
    }

    @Override
    protected void measureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed,
            int parentHeightMeasureSpec, int heightUsed) {
        // TODO Auto-generated method stub
        super.measureChildWithMargins(child, parentWidthMeasureSpec, widthUsed,
                parentHeightMeasureSpec, heightUsed);
    }

    public static final String TAG = "ChartView";

    // private int w, h;// width,height,half w,half h
    private Paint mPaint;

    private Paint paint2;

    private Path mPath;

    private boolean isSquare = true;

    RectF rf1 = new RectF();

    RectF rf2 = new RectF();

    RectF rf3 = new RectF();

    RectF rf4 = new RectF();

    RectF rf5 = new RectF();

    Context context;

    TextView tv;

    protected int backColor = Color.WHITE;

    protected int adgeLineColor = 0xa6a6a6a6;

    private int middleColor=0xffB71C7A;
    
    protected int adgeLineWidth = 2;

    private Bitmap dividerHorizontal;
    private Bitmap dividerHBitmap;
    private Matrix dividerMtx;

    private void initDraw(int width, int height) {
        if (isSquare) {
            width = height = Math.min(width, height);
        }
        rf1.left=getPaddingLeft();
        rf1.top=getPaddingTop();
        rf1.right = width-getPaddingRight();
        rf1.bottom = height-getPaddingBottom();

        rf2.left = (width * 5 / 50) >> 1;
        rf2.top = (height * 5 / 50) >> 1;
        rf2.right = width - rf2.left;
        rf2.bottom = height - rf2.top;

        rf3.left = (width * 8 / 50) >> 1;
        rf3.top = (height * 8 / 50) >> 1;
        rf3.right = width - rf3.left;
        rf3.bottom = height - rf3.top;

        rf4.left = (width * 11 / 50) >> 1;
        rf4.top = (height * 11 / 50) >> 1;
        rf4.right = width - rf4.left;
        rf4.bottom = height - rf4.top;

        rf5.left = (width * 22 / 50) >> 1;
        rf5.top = (height * 22 / 50) >> 1;
        rf5.right = width - rf5.left;
        rf5.bottom = height - rf5.top; 
        float sy=rf5.width()/dividerHorizontal.getWidth();
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

    // ���Խ���
    LinearGradient shader = new LinearGradient(0, 0, 0, rf1.height(), new int[] {
            0xe2e2e2e2, 0xcccccccc
    }, null, TileMode.CLAMP);

    LinearGradient shader2 = new LinearGradient(0, 0, 0, rf2.height(), new int[] {
            0xb0b0b0b0, 0xeeeeeeee
    }, null, TileMode.CLAMP);

    LinearGradient shader3 = new LinearGradient(0, 0, 0, rf3.height(), new int[] {
            0xeeDD4DB0, 0xff9E1A69
    }, null, TileMode.CLAMP);

    LinearGradient shader4 = new LinearGradient(0, 0, 0, rf4.height(), new int[] {
            0xbcda5dc3, 0xffdf42a0
    }, null, TileMode.CLAMP);

    // LinearGradient shaderAge = new LinearGradient(0, 0, 0, ageHeight, new
    // int[]{0xd8d8d8d8,0xbcbcbcbc},null, TileMode.CLAMP);
    PaintFlagsDrawFilter filter = new PaintFlagsDrawFilter(0, Paint.FILTER_BITMAP_FLAG);

    private void init(Context context) {
        mPaint = new Paint();
        mPaint.setColor(Color.GRAY);
        mPaint.setAntiAlias(true);
        mPath = new Path();
        paint2 = new Paint();
        paint2.setAntiAlias(true);
        // ͼ��ϳ�ģʽ
        paint2.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        dividerMtx=new Matrix();
        dividerHorizontal = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.account_data_opened_divider);
    }

    protected void drawBack(Canvas canvas) {

        // back circle's light
        mPaint.setColor(backColor);
        mPaint.setStyle(Style.FILL);
         canvas.drawOval(rf1, mPaint);

        // back circle's ageLine
        mPaint.setColor(adgeLineColor);
        mPaint.setStrokeWidth(adgeLineWidth);
        mPaint.setStyle(Style.STROKE);
        canvas.drawOval(rf1, mPaint);
        // shader
        paint2.setShader(shader);
        canvas.drawOval(rf1, paint2);

        // back circle's dark
        mPaint.setStyle(Style.FILL);
        mPaint.setColor(Color.WHITE);
         canvas.drawOval(rf2, mPaint);
        //
        paint2.setShader(shader2);
        canvas.drawOval(rf2, paint2);

        // middle button'bk
        // 846277
        // C12689
        mPaint.setColor(middleColor);
        canvas.drawOval(rf3, mPaint);

        // middle button
        // drawMidButtons(canvas,rf3);
        paint2.setShader(shader3);
        canvas.drawOval(rf3, paint2);

        // center circle effect
        mPaint.setColor(middleColor);
         canvas.drawOval(rf4, mPaint);
        // ���Խ���
        paint2.setShader(shader4);
        canvas.drawOval(rf4, paint2);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w != oldw || h != oldh) {
            Log.d(TAG, "onSizeChanged w " + w + " h" + h);
            initDraw(this.getWidth(), this.getHeight());
        }
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public void invalidate() {
        upDate(rf4);
        super.invalidate();
    }

    private void upDate(RectF rf4) {
        int count = getChildCount();
        if (count <= 0) {
            return;
        }
        int cx = (int)rf4.centerX(), cy = (int)rf4.top, cw = (int)rf4.width(), ch = (int)(rf4.height() / count);
        int tmpH = 0;
        int measureSpecWidth;
        int measureSpecHeight;
        View v;
        LayoutParams vLp ;
        for (int i = 0; i < count; i++) {
            v = getChildAt(i);
            vLp = v.getLayoutParams();
            if (vLp != null && vLp.width == LayoutParams.FILL_PARENT) {
                measureSpecWidth = MeasureSpec.makeMeasureSpec(cw, MeasureSpec.EXACTLY);
            } else {
                measureSpecWidth = MeasureSpec.makeMeasureSpec(cw, MeasureSpec.AT_MOST);
            }
            if (vLp != null && vLp.height == LayoutParams.FILL_PARENT) {
                measureSpecHeight = MeasureSpec.makeMeasureSpec(ch, MeasureSpec.EXACTLY);
            } else {
                measureSpecHeight = MeasureSpec.makeMeasureSpec(ch, MeasureSpec.AT_MOST);
            }
            v.measure(measureSpecWidth, measureSpecHeight);
            int vw = v.getMeasuredWidth(), vh = v.getMeasuredHeight();
            int vx = cx - vw/ 2, vy = cy + (ch-vh) / 2;
            vy+=tmpH;
            v.layout(vx, vy, vx + v.getMeasuredWidth(), vy + v.getMeasuredHeight());
            tmpH += vh;
        }
    }

    private void drawDividers(Canvas canvas,RectF rf4) {
        int count = getChildCount();
        int dividerX = (int)rf4.centerX() - dividerHBitmap.getWidth() / 2, 
        deviderY = (int)rf4.top- dividerHBitmap.getHeight() / 2;
        for (int i = 1; i < count; i++) {
            View v = getChildAt(i - 1);
            deviderY += v.getMeasuredHeight();
            canvas.drawBitmap(dividerHBitmap, dividerX, deviderY , null);
        }
    }
    
    public void setMiddleColor(int color){
        middleColor=color;
    }
}
