
package com.accenture.mbank.view.table;

import it.gruppobper.ams.android.bper.R;

import java.util.ArrayList;
import java.util.Locale;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.accenture.mbank.model.ChartProp;
import com.accenture.mbank.util.TimeUtil;
import com.accenture.mbank.util.Utils;

/**
 * customized chartsView 自定义饼状图
 */

public class ChartView extends FormView {
    private boolean mAa;

    private ArrayList<ChartProp> mChartProps;

    private Point mCenterPoint;

    private int mR;

    /**
     * 扇形的起始角度
     */
    private float mStartAngle;

    private int mScreenWidth;

    private int mScreenHeight;

    private String chartName;

    private String chartPercent;

    private String mCenterTitle = "";

    private boolean isSelect = false;

    private int selectIndex;
	
    ViewGroup mContainer;
    
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

    float ox = 620f / 1280f;

    float oy = 360f / 720f;

    int oox = 0;

    int ooy = 0;
    
    float r= 320f/720f;
    
    float _frameLine =  5/1280;
    int frameLine =0;

    /**
     * initial some params 初始化默认参数*
     */
    private void initParams() {
        mAa = true;
      
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

       initParams();
    }

    /**
     * create charts' property 创建饼状图的属性
     * 
     * @param chartsNum charts' number 饼状图的个数
     * @return charts' property's list 饼状图属性的list
     */
    public ArrayList<ChartProp> createCharts(int chartsNum) {
        createChartProp(chartsNum);
        return mChartProps;
    }

    public void setContainer(ViewGroup vg) {
    	this.mContainer = vg;
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

    public void setCenterTitle(String centerTitle) {
		this.mCenterTitle = centerTitle;
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

        chartName = mChartProps.get(selectIndex).getName();
        chartPercent = mChartProps.get(selectIndex).getPercent() + "";
        chartPercent = Utils.formatPercent(chartPercent);
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
            	if (!mCenterTitle.equals(getResources().getString(R.string.assets)))
            			break;

                float x = event.getX();
                float y = event.getY();
                ChartProp clickChart = getUpChartProp(x, y);
                if (clickChart != null) {
 
                    chartName = clickChart.getName();
                    chartPercent = clickChart.getPercent() + "";
                    chartPercent = Utils.formatPercent(chartPercent);
                    isSelect = true;
                    performClickButton();

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

            if ((angle > chartProp.getStartAngle() && angle <= chartProp.getEndAngle())
                    || (angle + 360 > chartProp.getStartAngle() && angle + 360 <= chartProp
                            .getEndAngle())) {
                return chartProp;
            }
        }
        return null;
    }

	private void performClickButton() {
		if (mContainer != null) {
			RadioButton btn_shares = (RadioButton) mContainer
					.findViewById(R.id.btn_shares);
			if (btn_shares != null)
				btn_shares.performClick();
		}
	}

    Paint paint = new Paint();
    
    /**
     * 
     */
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        paint.setTypeface(mFontFace);
        paint.setAntiAlias(mAa);
        canvas.drawColor(bgColor1);

        paint.setColor(Color.WHITE);
        canvas.drawCircle(mCenterPoint.x, mCenterPoint.y, mR *1.03f, paint);

        float startAngle = mStartAngle;
        int size = mChartProps.size();
        RectF oval = new RectF(mCenterPoint.x - mR, mCenterPoint.y - mR, mCenterPoint.x + mR,
                mCenterPoint.y + mR);
        // for循环画扇形
        for (int i = 0; i < size; i++) {
            ChartProp chartProp = mChartProps.get(i);
            // drawArc
            paint.setColor(chartProp.getColor());

			if (isSelect) {
				if (i == getSelectIndex())
					paint.setAlpha(0xFF);
				else
					paint.setAlpha(0xFF / 5);
			} else {
				paint.setAlpha(0xFF);
			}

            paint.setStyle(Style.FILL);
            float sweepAngle = chartProp.getSweepAngle();
            canvas.drawArc(oval, startAngle, sweepAngle, true, paint);

            // add startAngle
            chartProp.setStartAngle(startAngle);
            startAngle += sweepAngle;
            chartProp.setEndAngle(startAngle);

            // 画遮罩层
            paint.setColor(bgColor1);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
            canvas.drawCircle(mCenterPoint.x, mCenterPoint.y, (float)(mR / 1.5), paint);

            drawText(canvas);
        }
    }

    public int getStringWidth(Paint mPaint, String str) {
        return (int)mPaint.measureText(str);
    }

    public int getFontHeight(Paint mPaint) {
        FontMetrics fm = mPaint.getFontMetrics();
        return (int)Math.ceil(fm.descent - fm.top);
    }

	void drawText(Canvas canvas) {
        paint.setTypeface(mFontFace);
		String textChartName = "";
		String text2 = "";
		if (isSelect) {
			textChartName = chartName.toUpperCase(Locale.US);
			text2 = chartPercent;
		}
		else {
			text2 = mCenterTitle;
		}

		String updatedOn = context.getResources().getString(R.string.invest_update);   //Investimenti aggiornati al
		String currentDate = TimeUtil.getDateString(System.currentTimeMillis(),
				TimeUtil.dateFormat5);

		paint.setColor(getResources().getColor(R.color.chart_title));
		int fontHight = (int) (mScreenHeight * (42f / 577));

		paint.setTextSize(fontHight);
		canvas.drawText(textChartName, 
				mCenterPoint.x - getStringWidth(paint, textChartName)/ 2,
				getFontHeight(paint) - fontHight * 2 + mR,
				paint);
		
		if (isSelect) {
			paint.setTextSize(fontHight * 2);
			canvas.drawText(text2, 
					mCenterPoint.x - getStringWidth(paint, text2)/ 2,
					getFontHeight(paint) - fontHight + mR,
					paint);
		}
		else 
		{
			paint.setTextSize(fontHight);
			canvas.drawText(text2, 
					mCenterPoint.x - getStringWidth(paint, text2)/ 2,
					getFontHeight(paint) - fontHight / 2 + mR,
					paint);
		}

		paint.setTextSize((int) (fontHight * 0.67));
		canvas.drawText(updatedOn, 
				mCenterPoint.x - getStringWidth(paint, updatedOn)/ 2,
				getFontHeight(paint) + fontHight * 2 + mR, 
				paint);

		canvas.drawText(currentDate, 
				mCenterPoint.x - getStringWidth(paint, currentDate)/ 2, 
				getFontHeight(paint) + fontHight * 3 + mR, 
				paint);
	}
}
