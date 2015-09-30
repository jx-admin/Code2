
package com.accenture.mbank.view.table;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import com.accenture.mbank.model.ChartProp;

import android.content.Context;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.MeasureSpec;

/**
 * customized chartsView 自定义饼状图
 */

public class ChartView extends View {
    private boolean mAa;

    /**
     * 饼图的个数
     */
    private int mChartsNum;

    private ArrayList<ChartProp> mChartProps;

    private Point mCenterPoint;

    private int mR;

    /**
     * 扇形的起始角度
     */
    private float mStartAngle;

    private int mScreenWidth;

    private int mScreenHeight;

    /**
     * 选择画框的x , y 坐标
     */
    private float selectX = 0;

    private float selectY = 0;

    private String chartName;

    private String chartPercent;

    private boolean isSelect = false;

    private int selectIndex;

    public ChartView(Context context) {
        super(context);
        mChartProps = new ArrayList<ChartProp>();
    }

    public ChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mChartProps = new ArrayList<ChartProp>();
    }

    public ChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mChartProps = new ArrayList<ChartProp>();
    }

    float ox = 550f / 1280f;

    float oy = 370f / 720f;

    int oox = 0;

    int ooy = 0;
    
    float r=300f/720f;
    
    float _frameLine =  5/1280;
    int frameLine =0;

    /**
     * initial some params 初始化默认参数*
     */
    private void initParams() {
        mAa = true;
        mChartsNum = 1;
       
        mR = (int)(r*mScreenHeight);// TODO
        mStartAngle = 0;
        frameLine = (int)(5 * _frameLine);
        oox = (int)(ox * mScreenWidth);
        ooy = (int)(oy * mScreenHeight);
        mCenterPoint = new Point(oox, ooy); // TODO
        
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

       int mScreenWidth = MeasureSpec.getSize(widthMeasureSpec);
        int mScreenHeight = MeasureSpec.getSize(heightMeasureSpec);
       this.mScreenWidth = Math.max(mScreenWidth, mScreenHeight);
        this.mScreenHeight = Math.min(mScreenWidth, mScreenHeight);
//        setMeasuredDimension(Math.max(mScreenWidth, mScreenHeight),
//                Math.min(mScreenWidth, mScreenHeight));

        initParams();

    }

    /**
     * create charts' property 创建饼状图的属性
     * 
     * @param chartsNum charts' number 饼状图的个数
     * @return charts' property's list 饼状图属性的list
     */
    public ArrayList<ChartProp> createCharts(int chartsNum) {
        mChartsNum = chartsNum;
        createChartProp(chartsNum);
        return mChartProps;
    }

    /**
     * 设置第一个扇形绘制时的起始角度
     * 
     * @param 第一个扇形绘制时的起始角度
     */
    public void setStartAngle(float startAngle) {
        mStartAngle = startAngle;
        invalidate();
    }

    /**
     * set the view anti alias. 设置是否抗锯齿。
     * 
     * @param true 意味着高质量绘图
     */
    public void setAntiAlias(boolean aa) {
        mAa = aa;
        invalidate();
    }

    /**
     * @return the selectIndex
     */
    public int getSelectIndex() {
        return selectIndex;
    }

    /**
     * @param selectIndex the selectIndex to set
     */
    public void setSelectIndex(int selectIndex) {
        this.selectIndex = selectIndex;
        selectX = mChartProps.get(selectIndex).getStartAngle();
        selectY = mChartProps.get(selectIndex).getSweepAngle();
        chartName = mChartProps.get(selectIndex).getName();
        chartPercent = mChartProps.get(selectIndex).getPercent() + "";
        chartPercent = fomartPercent(chartPercent);
        isSelect = true;
        invalidate();
    }

    /**
     * 设置饼状图的中心点
     * 
     * @param 饼状图的中心点坐标
     */
    public void setCenter(Point centerPoint) {
        mCenterPoint = centerPoint;
        invalidate();
    }

    /**
     * 设置饼状图半径
     * 
     * @param 饼状图的半径
     */
    public void setR(int r) {
        mR = r;
        invalidate();
    }

    /**
     * 真正创建扇形属性的方法
     * 
     * @param 饼状图的个数
     */
    private void createChartProp(int chartsNum) {
        for (int i = 0; i < chartsNum; i++) {
            ChartProp chartProp = new ChartProp(this);
            chartProp.setId(i);
            mChartProps.add(chartProp);
        }
    }

    /**
     * 处理抬起事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_UP:
                float x = event.getX();
                float y = event.getY();
                ChartProp clickChart = getUpChartProp(x, y);
                if (clickChart != null) {
                    selectX = clickChart.getStartAngle();
                    selectY = clickChart.getSweepAngle();
                    chartName = clickChart.getName();
                    chartPercent = clickChart.getPercent() + "";
                    chartPercent = fomartPercent(chartPercent);
                    isSelect = true;
                    invalidate();
                } else {
                    return true;
                }
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 获取当抬起时，坐标所在的charProp
     * 
     * @param x action_up's x up时的x坐标
     * @param y action_up's y up时的y坐标
     * @return 如果返回值为null，说明不在任何的扇形内。
     */
    private ChartProp getUpChartProp(float x, float y) {
        double angle = Math.atan2(y - mCenterPoint.y, x - mCenterPoint.x) * 180 / Math.PI;
        if (angle < 0) {
            angle = 360 + angle;
        }
        Log.d("test", "up angle = " + angle);

        ChartProp chartPropPosible = getPosibleChartProp(angle);
        if (chartPropPosible != null && inChartZone(x, y)) {
            return chartPropPosible;
        }

        return null;
    }

    /**
     * 判断抬起时，坐标是否在圆内。
     * 
     * @param x action_up's x up时的x坐标
     * @param y action_up's y up时的y坐标
     * @return 返回值为true，表示在圆内。
     */
    private boolean inChartZone(float x, float y) {
        float a2 = (x - mCenterPoint.x) * (x - mCenterPoint.x);
        float b2 = (y - mCenterPoint.y) * (y - mCenterPoint.y);
        float R2 = mR * mR;
        if (a2 + b2 <= R2) {
            return true;
        }
        return false;
    }

    /**
     * 根据抬起时的角度，获取可能的ChartProp
     * 
     * @param 抬起时的角度
     * @return 可能的charProp。因为还要判断是不是在圆内。
     */
    private ChartProp getPosibleChartProp(double angle) {
        int size = mChartProps.size();
        for (int i = 0; i < size; i++) {
            ChartProp chartProp = mChartProps.get(i);
            Log.i("test", "chartProp S angle = " + chartProp.getStartAngle()
                    + ", chartProp E angle = " + chartProp.getEndAngle());
            if ((angle > chartProp.getStartAngle() && angle <= chartProp.getEndAngle())
                    || (angle + 360 > chartProp.getStartAngle() && angle + 360 <= chartProp
                            .getEndAngle())) {
                return chartProp;
            }
        }
        return null;
    }

    Paint paint = new Paint();
    
    /**
     * 
     */
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        paint.setAntiAlias(mAa);
        paint.setColor(Color.WHITE);
        canvas.drawColor(Color.WHITE);

        float startAngle = mStartAngle;
        int size = mChartProps.size();
        RectF oval = new RectF(mCenterPoint.x - mR, mCenterPoint.y - mR, mCenterPoint.x + mR,
                mCenterPoint.y + mR);
        // for循环画扇形
        for (int i = 0; i < size; i++) {
            ChartProp chartProp = mChartProps.get(i);
            // drawArc
            paint.setColor(chartProp.getColor());
            paint.setStyle(Style.FILL);
            float sweepAngle = chartProp.getSweepAngle();
            canvas.drawArc(oval, startAngle, sweepAngle, true, paint);

            // add startAngle
            chartProp.setStartAngle(startAngle);
            startAngle += sweepAngle;
            chartProp.setEndAngle(startAngle);

            // 判断选中添加边框
            if (isSelect) {
                Paint mPaints = new Paint();
                mPaints.setStyle(Paint.Style.STROKE);
                mPaints.setStrokeWidth(frameLine);
                mPaints.setAntiAlias(true);
                mPaints.setColor(Color.BLACK);
                RectF _oval = new RectF(mCenterPoint.x - mR, mCenterPoint.y - mR, mCenterPoint.x + mR, mCenterPoint.y + mR);
                canvas.drawArc(_oval, selectX, selectY, true, mPaints);
            }

            // 画遮罩层
            paint.setColor(Color.WHITE);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
            canvas.drawCircle(mCenterPoint.x, mCenterPoint.y, (float)(mR / 1.5), paint);

            if (isSelect) {
                paint.setColor(Color.BLACK);
                float fontHight = mScreenHeight * (35f / 577);
                float fontWidth = getStringWidth(paint,"00,%");
                
                canvas.drawText(chartName, mR / 1.5f + (fontWidth/2),getFontHeight(paint, fontHight) + mR, paint);
                canvas.drawText(chartPercent, mR / 1.5f  + (fontWidth/2),getFontHeight(paint, fontHight) + fontHight + mR, paint);
            }
        }
    }

    public int getStringWidth(Paint mPaint, String str) {
        return (int)mPaint.measureText(str);
    }

    public int getFontHeight(Paint mPaint, float fontSize) {
        FontMetrics fm = mPaint.getFontMetrics();
        paint.setTextSize(fontSize);
        return (int)Math.ceil(fm.descent - fm.top) + 2;
    }

    public String fomartPercent(String chartPercent) {
        double percent = Double.valueOf(chartPercent);

        chartPercent = String.format(Locale.US, "%.2f", percent);
        NumberFormat nf2 = NumberFormat.getInstance(Locale.GERMAN);
        double dd = Double.parseDouble(chartPercent);
        chartPercent = nf2.format(dd);
        chartPercent += "%";

        return chartPercent;
    }

}
