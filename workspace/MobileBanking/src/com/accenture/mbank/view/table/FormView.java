
package com.accenture.mbank.view.table;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class FormView extends BankTableView {

    /**
     * Y轴坐标值画笔的宽度
     */
    float y_paint_scale = 20f / 577;

    /**
     * Y坐标的文字的长度
     */
    int yPaintScale = 0;

    float x_paint_scale = 20f / 577;

    int xPaintScale = 0;

    private String textLeft = "2012 deposit/withdrawal gap";

    private String textRight = "frank's account";

    public String getTextLeft() {
        return textLeft;
    }

    public void setTextLeft(String textLeft) {
        this.textLeft = textLeft;
    }

    public String getTextRight() {
        return textRight;
    }

    public void setTextRight(String textRight) {
        this.textRight = textRight;
    }

    /**
     * title文字的大小比例
     */
    float title_text_size = 35f / 577;

    float title_line_size = 2f / 577;

    int titleLineSize = 0;

    /**
     * title文字的实际大小
     */
    int titleTextSize = 0;

    /**
     * 标题的下划线位置
     */
    float title_line_y = 47f / 577;

    int titleLineY = 0;

    /**
     * 图表的上下左右的边距
     */
    float padding_ = 50f / 577;

    int padding = 0;

    /**
     * x轴的x方向初始位置
     */
    int xAxisStartXPosition = 0;

    /**
     * x轴的x方向终位置
     */
    int xAxisEndXPosition = 0;

    /**
     * x轴的Y方向的位置
     */
    int xAxisYPosition = 0;

    /**
     * y轴的y方向初始位置
     */
    int yAxisStartYPosition = 0;

    /**
     * y轴的y方向终位置
     */
    int yAxisEndYPosition = 0;

    /**
     * y轴的X方向的位置
     */
    int yAxisXPosition = 0;

    /**
     * y轴的粗细比例
     */
    float y_axis_scale = 6f / 577;

    /**
     * y轴的粗细值
     */
    int yAxisScale = 0;

    /**
     * X轴的粗细比例
     */
    float x_axis_scale = 6f / 577;

    /**
     * X轴的粗细值
     */
    int xAxisScale = 0;

    /**
     * 画X坐标的画笔颜色
     */
    int xAxisColor = Color.rgb(100, 100, 100);

    /**
     * 画Y坐标的画笔颜色
     */
    int yAxisColor = Color.rgb(100, 100, 100);

    public FormView(Context context) {
        super(context);
    }

    @Override
    protected void init() {
        // TODO Auto-generated method stub
        super.init();
    };

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(Math.max(width, height), Math.min(width, height));

        initPosition();

    }

    protected void initPosition() {

        int height = getMeasuredHeight();
        int width = getMeasuredWidth();

        titleTextSize = (int)(height * title_text_size);
        titleLineSize = (int)(height * title_line_size);
        titleLineY = (int)(height * title_line_y) + titleLineSize;

        padding = (int)(height * padding_);

        xAxisScale = (int)(height * x_axis_scale);
        yAxisScale = (int)(height * y_axis_scale);
        
        xPaintScale = (int)(height * x_paint_scale);
        yPaintScale = (int)(height * y_paint_scale);

    }

    Paint paint = new Paint();

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);

        int width = getMeasuredWidth();
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setTextSize(titleTextSize);
        canvas.drawText(textLeft, titleTextSize, titleTextSize, paint);

        float rightTextLength = paint.measureText(textRight);

        int right = (int)(width - rightTextLength - titleTextSize);

        canvas.drawText(textRight, right, titleTextSize, paint);

        paint.setStrokeWidth(titleLineSize);
        canvas.drawLine(0, titleLineY, getMeasuredWidth(), titleLineY, paint);

    }
}
