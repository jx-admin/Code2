
package com.act.mbanking.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.act.mbanking.App;
import com.act.mbanking.R;

/**
 * 这是所有画图表的基类
 * 
 * @author seekting.x.zhang
 */
public class BaseChartView extends View {

    /**
     * 画X轴的画笔宽度
     */
    float xAxisWidth = 0;

    /**
     * 画y轴的画笔宽度
     */
    float yAxisWidth = 0;

    /**
     * y坐标轴上的文字大小
     */
    float yAxisTextSize = 10;

    /**
     * x坐标轴上的文字 大小
     */
    float xAxisTextSize = 10;

    int yAxisColor;

    int xAxisColor;

    /**
     * 画坐标的画笔
     */
    Paint axisPaint;

    /**
     * y刻度
     */
    public ValueWidthName[] yValues = new ValueWidthName[7];

    /**
     * x刻度
     */
    public ValueWidthName[] xValues = new ValueWidthName[6];
    public int line=1;

    public BaseChartView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray type = context.obtainStyledAttributes(attrs, R.styleable.BaseChartView);
        xAxisTextSize = type.getDimension(R.styleable.BaseChartView_x_axis_value_text_size, 10);
        yAxisTextSize = type.getDimension(R.styleable.BaseChartView_y_axis_value_text_size, 10);
        xAxisWidth = type.getDimension(R.styleable.BaseChartView_x_axis_paint_width, 1);
        yAxisWidth = type.getDimension(R.styleable.BaseChartView_y_axis_paint_width, 1);
        axisPaint = new Paint();
        axisPaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);

        axisPaint.setStyle(Style.STROKE);
        areaPaint = new Paint();
        areaPaint.setAntiAlias(true);

        areaPaint.setStyle(Style.FILL);
        type.recycle();

    }

    /**
     * Y轴上最大的文字的宽度
     */
    float maxYAxisTextWidth = 0;

    /**
     * X轴上最大的文字的宽度
     */
    float maxXAxisTextWidth = 0;

    /**
     * 坐标系原点坐标
     */
    float o_x;

    /**
     * 坐标系原点坐标
     */
    float o_y;

    /**
     * y轴终点值(y)
     */
    float y_end;

    /**
     * x轴终点值(x)
     */
    float x_end;

    /**
     * 每一行所占的高度
     */
    protected float cellYHeight;

    /**
     * 每一列所占的高度
     */
    protected float cellXWidth;

    /**
     * 画文字 的画笔
     */
    Paint textPaint;

    /**
     * 画黑白区域的画笔
     */
    Paint areaPaint;

    /**
     * y轴文字与y轴之间的间距
     */
    int yAxisValueSpacing = 5;

    /**
     * x轴文字与x轴之间的间距
     */
    int xAxisValueSpacing = 5;

    protected int areaColor2 = Color.rgb(223, 223, 223);

    protected int areaColor1 = Color.rgb(230, 230, 230);

    boolean drawArea = true;

    /**
     * x轴长度
     */
    float xAxisLength = 0;

    /**
     * y轴长度
     */
    float yAxisLength = 0;

    private void init() {
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // TODO Auto-generated method stub
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        init();
        initOXY();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawAreas(canvas);
        drawXaxis(canvas);
        drawXLine(canvas);
        drawYAxis(canvas);
        drawYAxisText(canvas);
        drawXaxisText(canvas);

    }

    protected void initOXY() {

        for (int i = 0; i < yValues.length; i++) {

            textPaint.setTextSize(yAxisTextSize);//设置y辆文字大小

            ValueWidthName valueWidthName = yValues[i];
            if (valueWidthName != null) {
                float textWidth = textPaint.measureText(yValues[i].getName()); // 取出来Y辆上设置的文字的宽度
                maxYAxisTextWidth = Math.max(maxYAxisTextWidth, textWidth); 
            }
        }
        textPaint.setTextSize(xAxisTextSize); //设置X辆文字大小
        FontMetrics fm = textPaint.getFontMetrics();
        maxXAxisTextWidth = (int)Math.ceil(fm.descent - fm.top);
        o_x = maxYAxisTextWidth + yAxisValueSpacing;
        o_y = getMeasuredHeight() - maxXAxisTextWidth*line - xAxisValueSpacing;
        x_end = getMeasuredWidth();
        y_end = maxXAxisTextWidth/2;

        xAxisLength = x_end - o_x;
        yAxisLength = o_y - y_end;
        cellYHeight = yAxisLength / ((float)yValues.length-1);
        cellXWidth = xAxisLength / (float)xValues.length; 
    }

    /**
     * 画x轴上的文字
     * 
     * @param canvas
     */
    protected void drawXaxisText(Canvas canvas) {
        textPaint.setTextAlign(Align.CENTER);
        textPaint.setTextSize(xAxisTextSize);
        for (int i = 0; i < xValues.length; i++) {
            ValueWidthName valueWidthName = xValues[i];
            if (valueWidthName != null) {
            	String names[]=valueWidthName.getName().split("\n");
            	line=Math.min(line, names.length);
            	for(int j=0;j<line;j++){
                canvas.drawText(names[j], o_x + i * cellXWidth + cellXWidth / 2,
                        o_y + xAxisValueSpacing + xAxisTextSize*(j+1), textPaint);
            	}
            }
        }
    }

    /**
     * 画y轴上的文字
     * 
     * @param canvas
     */
    private void drawYAxisText(Canvas canvas) {
        textPaint.setTextSize(yAxisTextSize);

        textPaint.setTextAlign(Align.RIGHT);
        float ty=o_y -textPaint.ascent()/2;
        for (int i = 0; i < yValues.length; i++) {
            ValueWidthName valueWidthName = yValues[i];

            if (valueWidthName != null) {
                canvas.drawText(valueWidthName.getName(), o_x - yAxisValueSpacing,ty, textPaint);
            }
            ty-= cellYHeight;
        }
    }

    /**
     * 画纵坐标
     * 
     * @param canvas
     */
    private void drawYAxis(Canvas canvas) {
        axisPaint.setColor(Color.BLACK);
        axisPaint.setStrokeWidth(yAxisWidth);
        //
        canvas.drawLine(o_x, o_y, o_x, y_end, axisPaint);
    }

    /**
     * 画一条一条的白横线
     * 
     * @param canvas
     */
    private void drawXLine(Canvas canvas) {
        axisPaint.setColor(Color.WHITE);
        float yy = o_y ;
        for (int i = 0; i < yValues.length-1; i++) {
            yy-=cellYHeight;
            canvas.drawLine(o_x, yy, x_end, yy, axisPaint);
        }
    }

    /**
     * 画横坐标
     * 
     * @param canvas
     */
    private void drawXaxis(Canvas canvas) {
        axisPaint.setStrokeWidth(xAxisWidth);
        axisPaint.setColor(Color.BLACK);
        canvas.drawLine(o_x, o_y, x_end, o_y, axisPaint);
    }

    /**
     * 画区域方格
     * 
     * @param canvas
     */
    private void drawAreas(Canvas canvas) {
        if (!drawArea) {
            return;
        }

        for (int i = 0; i < xValues.length; i++) {

            int color = (i % 2) == 0 ? areaColor1 : areaColor2;
            areaPaint.setColor(color);
            RectF r = new RectF();
            r.left = o_x + cellXWidth * i;
            r.right = r.left + cellXWidth;
            r.top = 0;
            r.bottom = o_y;
            canvas.drawRect(r, areaPaint);
        }
    }

    public static class ValueWidthName {
        double value;

        String name = "";

        public ValueWidthName() {

        }

        public ValueWidthName(String name, double value) {

            this.name = name;
            this.value = value;
        }

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

}
