
package com.accenture.mbank.view.table;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;

import com.accenture.mbank.BaseActivity;
import com.accenture.mbank.model.InstallmentsModel;
import com.accenture.mbank.util.LogManager;
import com.accenture.mbank.util.TimeUtil;
import com.accenture.mbank.util.Utils;

/**
 * @author seekting.x.zhang
 */
public class SignLineFormView extends FormView {
    public List<InstallmentsModel> installments;

    public String residualCapital;

    boolean signType = true;

    Paint mPaint = new Paint();

    Paint textPaint = new Paint();

    /**
     * X轴上每等分的距离
     */
    int xCellLength = 0;

    /**
     * Y轴上每等分的距离
     */
    int yCellLength = 0;

    /**
     * X轴上的标尺值
     */
    public List<String> xValue;

    /**
     * Y轴上的标尺值
     */
    public List<Double> yValue;

    public List<Double> xYValues;

    /**
     * y轴方向每个像素所占的值
     */
    float yCell = 0;

    int xyColor = Color.rgb(255, 163, 48);

    int yFocusColor = Color.rgb(255, 163, 48);

    /**
     * 折点有焦点时的颜色
     */
    int xyTouchColor = Color.argb(100, 255, 163, 48);

    /**
     * 圆矩形的颜色
     */
    int xyDetailColor = Color.argb(200, 255, 163, 48);

    List<XY> xys;

    /**
     * 画拆线的画笔宽度比例
     */
    float x_y_paint_scale = 5f / 577;

    int xyPaintScale = 0;

    float y_paint_width = 2f / 577;

    int yPaintWidth = 0;

    float y_paint_width_focus = 4f / 577;

    int ypaintWidthFoucs = 0;

    /**
     * 拆线转折点的半径比例
     */
    float x_o_y_r = 15f / 577;

    int XOYR = 0;

    /**
     * 详情矩形宽度
     */
    int detailWidth = 0;

    /**
     * 详情矩形高度
     */
    int detailHeight = 0;

    float detail_round_r = 10f / 577;

    int detailRoundR = 0;

    /**
     * 拆线转折点有焦点时显示 的半径比例
     */
    float x_o_y_r_touch = 30f / 577;

    int xOYRTouch = 0;

    /**
     * 圆与矩形 的间隔
     */
    float detail_touch_y = 5f / 577;

    int detailTouchY = 0;

    float detail_text_1_size = 40f / 577;

    float detail_text_2_size = 30f / 577;

    int detailText1Size = 0;

    int detailText2Size = 0;

    /**
     * 双手触摸时出现slidBar的大小
     */
    float slid_bar_r = 30f / 577;

    int slidBarR = 0;

    String twoPointDetailText1 = "-$3.000,99(-1,5%)";

    String twoPointDetailText2 = "01 feb 2012 -115 apr 2012";

    float two_point_detail_text1_size = 30f / 577;

    int twoPointDetailText1Size = 0;

    float two_point_detail_text2_size = 20f / 577;

    int twoPointDetailText2Size = 0;

    /**
     * 详情显示时上下margin
     */
    float two_point_detail_rect_margin = 10f / 577;

    int twoPointDetailRectMargin = 0;

    /**
     * 下三角边长
     */
    float two_point_detail_sanjiao_length = 20f / 577;

    int twoPointDetailSanjiaoLength = 0;

    float residual_capital_size = 29f / 577;

    int residualCapitalSize = 0;

    public SignLineFormView(Context context) {
        super(context);

        if (BaseActivity.isOffline) {
            xValue = new ArrayList<String>();
            xValue.add("may");
            xValue.add("jun");
            xValue.add("jul");
            xValue.add("aug");
            xValue.add("set");

            // this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

            xYValues = new ArrayList<Double>();
            xYValues.add(34d);
            xYValues.add(173d);
            xYValues.add(60d);
            xYValues.add(250d);
            xYValues.add(20d);

            // xYValues.add(-34d);
            // xYValues.add(200d);
            // xYValues.add(60d);
            // xYValues.add(-182d);
            // xYValues.add(-30d);

            initYValue();
        }
    }

    public static final int NO_SIGN_SIZE = 4;

    public static final int SIGN_SIZE = 10;

    public void initYValue() {
        // double min = -200d;
        // double max = -0d;
        double min = Integer.MAX_VALUE;
        double max = -Integer.MAX_VALUE;
        for (int i = 0; i < xYValues.size(); i++) {
            min = Math.min(xYValues.get(i), min);
            max = Math.max(xYValues.get(i), max);
        }

        // double length = max - min;
        // 对min,max取整
        // if (min > 0) {
        // int a = min / 10;
        // }
        yValue = new ArrayList<Double>();
        if (signType) {

            min=Math.max(0, min);
            double cell = getCell(max, min, NO_SIGN_SIZE);
            for (int i = 0; i <= NO_SIGN_SIZE; i++) {
                yValue.add(begin + i * cell);
            }
            return;
        }
        if (min >= 0) {

            double cell = getCell(max, NO_SIGN_SIZE);

            for (int i = 0; i <= NO_SIGN_SIZE; i++) {
                yValue.add(i * cell);
            }

        } else if (min < 0 && max > 0) {

            double mm = Math.max(Math.abs(min), Math.abs(max));
            double cell = getCell(mm, SIGN_SIZE / 2);
            for (int i = -SIGN_SIZE / 2; i <= SIGN_SIZE / 2; i++) {

                yValue.add(i * cell);
            }

        } else if (max <= 0) {

            double mm = Math.abs(min);
            double cell = getCell(mm, NO_SIGN_SIZE);

            for (int i = -NO_SIGN_SIZE; i <= 0; i++) {
                yValue.add(i * cell);
            }

        }

    }

    double begin = 0;

    private double getCell(double max, double min, int size) {
        max = Math.ceil(max);

        String str = String.valueOf((int)max);
        int maxValue = Integer.valueOf(str);

        max = Math.ceil(min);
        // maxValue=125; 2
        int bb = (int)(maxValue / Math.pow(10, str.length() - 1)) + 1;
        int resultMax = (int)(bb * Math.pow(10, str.length() - 1));
        str = String.valueOf((int)min);
        int minValue = Integer.valueOf(str);
        bb = (int)(minValue / Math.pow(10, str.length() - 1));
        int resultMin = (int)(bb * Math.pow(10, str.length() - 1));
        double cell = (resultMax - resultMin) / size;
        begin = resultMin;
        return cell;
    }

    private double getCell(double max, int size) {
        max = Math.ceil(max);

        String str = String.valueOf((int)max);
        int aa = Integer.valueOf(str);
        int bb = (int)(aa / Math.pow(10, str.length() - 1)) + 1;
        int result = (int)(bb * Math.pow(10, str.length() - 1));

        double cell = result / (double)size;
        return cell;
    }

    @Override
    protected void initPosition() {
        super.initPosition();
        // 算出Y轴坐标值需要的最大值
        int height = getMeasuredHeight();
        int width = getMeasuredWidth();
        detailText1Size = (int)(detail_text_1_size * height);

        detailText2Size = (int)(detail_text_2_size * height);

        mPaint.setTextSize(yPaintScale);
        residualCapitalSize = (int)(residual_capital_size * height);
        int maxLength = 0;
        for (double value : yValue) {
            String str = Utils.generateFormatMoneyInt("$", value);
            // String str = String.valueOf(value);
            maxLength = (int)Math.max(mPaint.measureText(str), maxLength);
        }
        textPaint.setTextSize(residualCapitalSize);
        maxLength = (int)Math.max(maxLength, textPaint.measureText(residual));
        maxLength = (int)Math.max(maxLength, textPaint.measureText(capital));

        yAxisXPosition = maxLength + padding;
        yAxisStartYPosition = titleLineY + padding;
        if (signType) {
            yAxisStartYPosition = titleLineY + padding / 2 + detailText1Size + detailText2Size * 2;
        }

        yAxisEndYPosition = getMeasuredHeight() - padding - 2 * yPaintScale;
        xAxisEndXPosition = getMeasuredWidth() - padding;
        xAxisStartXPosition = yAxisXPosition;

        // 每等分的长度
        yCellLength = (int)((float)(yAxisEndYPosition - yAxisStartYPosition)
                / ((float)yValue.size()) - 1);

        // y轴每
        yCell = (float)((yValue.get(1) - yValue.get(0)) / yCellLength);

        xCellLength = (int)((float)(xAxisEndXPosition - xAxisStartXPosition) / ((float)xValue
                .size()));

        // 初始化拆线的点的坐标

        xys = new ArrayList<SignLineFormView.XY>();
        for (int i = 0; i < xYValues.size(); i++) {

            double xyValue = xYValues.get(i);
            int x = yAxisXPosition + (i + 1) * xCellLength;
            // Y轴长度
            float y = yAxisEndYPosition - (int)((xyValue - yValue.get(0)) / yCell);
            XY xy = new XY();
            xy.x = x;
            xy.y = y;
            xys.add(xy);
        }

        xyPaintScale = (int)(x_y_paint_scale * height);
        XOYR = (int)(x_o_y_r * height);

        xOYRTouch = (int)(x_o_y_r_touch * height);

        detailTouchY = (int)(detail_touch_y * height);

        detailRoundR = (int)(detail_round_r * height);

        slidBarR = (int)(slid_bar_r * height);

        twoPointDetailText1Size = (int)(two_point_detail_text1_size * height);
        twoPointDetailText2Size = (int)(two_point_detail_text2_size * height);
        twoPointDetailRectMargin = (int)(two_point_detail_rect_margin * height);
        twoPointDetailSanjiaoLength = (int)(two_point_detail_sanjiao_length * height);
        yAxisStartYPosition = yAxisStartYPosition + twoPointDetailText1Size
                + twoPointDetailText2Size;

        // 竖线画笔宽度
        yPaintWidth = (int)(y_paint_width * height);
        ypaintWidthFoucs = (int)(y_paint_width_focus * height);

    }

    String residual = "residual";

    String capital = "capital";

    Path mpath = new Path();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setAntiAlias(true);
        mPaint.setColor(xAxisColor);
        mPaint.setStrokeWidth(yAxisScale);
        // 画Y坐标轴
        canvas.drawLine(yAxisXPosition, yAxisStartYPosition, yAxisXPosition, yAxisEndYPosition,
                mPaint);
        // 画X坐标轴
        canvas.drawLine(yAxisXPosition, yAxisEndYPosition, xAxisEndXPosition, yAxisEndYPosition,
                mPaint);

        mPaint.setStrokeWidth(1);
        // for (int i = 1; i < yValue.size(); i++) {
        // // 画横线
        // double yy = yAxisEndYPosition - i * yCellLength;
        // canvas.drawLine(yAxisXPosition, (int)yy, xAxisEndXPosition, (int)yy,
        // mPaint);
        // }
        // 画虚线
        if (yValue.size() == SIGN_SIZE + 1) {
            float yy = yAxisEndYPosition - (SIGN_SIZE / 2) * yCellLength;
            mpath.reset();
            mpath.moveTo(yAxisXPosition, yy);
            mpath.lineTo(xAxisEndXPosition, yy);

            PathEffect effect = new DashPathEffect(new float[] {
                    5, 5, 5, 5
            }, 1);
            mPaint.setColor(Color.GRAY);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setAntiAlias(true);
            mPaint.setPathEffect(effect);
            mPaint.setStrokeWidth(1);
            canvas.drawPath(mpath, mPaint);
            // canvas.drawLine(yAxisXPosition, (int)yy, xAxisEndXPosition,
            // (int)yy, mPaint);
        }
        mPaint.setPathEffect(null);

        // 画竖线
        mPaint.setStrokeWidth(yPaintWidth);
        for (int i = 1; i <= xValue.size(); i++) {
            float xx = yAxisXPosition + i * xCellLength;
            canvas.drawLine(xx, yAxisStartYPosition, yAxisXPosition + i * xCellLength,
                    yAxisEndYPosition, mPaint);
            LogManager.d("draw line:|" + xx);
        }
        // mPaint.setTextSize(yPaintScale);
        mPaint.setColor(Color.BLACK);
        // 画横坐标的数值
        for (int i = 0; i < xValue.size(); i++) {
            mpath.reset();

            String text = xValue.get(i);
            String[] strs = text.split(TimeUtil.detaFormat6Split);

            String str1 = text;
            String str2 = "";
            if (text.contains(TimeUtil.detaFormat6Split)) {
                str1 = text.substring(0, text.indexOf(TimeUtil.detaFormat6Split));
                str2 = text.substring(text.indexOf(TimeUtil.detaFormat6Split) + 1, text.length());
            }

            int x = yAxisXPosition + (i + 1) * xCellLength;
            // canvas.drawText(text, x, yAxisEndYPosition + yPaintScale,
            // mPaint);

            mpath.moveTo(x - yPaintScale / 2, yAxisEndYPosition + 2 * yPaintScale);
            mpath.lineTo(x + 3 * yPaintScale, yAxisEndYPosition);
            // mpath.close();

            textPaint.setAntiAlias(true);
            textPaint.setColor(Color.BLACK);
            textPaint.setTextAlign(Paint.Align.LEFT);
            textPaint.setTypeface(Typeface.SERIF);
            textPaint.setTextSize(xPaintScale);
            canvas.drawTextOnPath(str1, mpath, 0, 0, textPaint);
            canvas.drawTextOnPath(str2, mpath, 0, yPaintScale, textPaint);
            // mPaint.setStyle(Paint.Style.STROKE);
            // canvas.drawPath(mpath, mPaint);
        }
        // 画纵坐标的数值
        textPaint.setTextSize(yPaintScale);
        for (int i = 0; i < yValue.size(); i++) {

            String text = Utils.generateFormatMoneyInt("$", yValue.get(i));

            int y = yAxisEndYPosition - i * yCellLength;
            int x = yAxisXPosition - 2 * yAxisScale;
            textPaint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(text, x, y, textPaint);
        }

        mpath.reset();
        // 画折线
        for (int i = 0; i < xys.size(); i++) {

            XY xy = xys.get(i);

            if (i == 0) {
                mpath.moveTo(xy.x, xy.y);
            } else {
                mpath.lineTo(xy.x, xy.y);
            }
        }

        // mpath.close();
        mPaint.setColor(xyColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(xyPaintScale);

        canvas.save();
        canvas.drawPath(mpath, mPaint);
        canvas.restore();
        mpath.reset();

        mPaint.setStyle(Paint.Style.FILL);

        // 画实心圆
        for (int i = 0; i < xys.size(); i++) {

            XY xy = xys.get(i);
            canvas.drawCircle(xy.x, xy.y, XOYR, mPaint);
        }
        if (!signType) {
            // 画点击时获取焦点的大圆

            if (touchPoint > -1) {
                mPaint.setColor(xyTouchColor);
                XY xy = xys.get(touchPoint);
                canvas.drawCircle(xy.x, xy.y, xOYRTouch, mPaint);
            }

            // 画点击时显示详情时的圆边矩形
            if (touchPoint > -1 && showDetail) {
                mPaint.setColor(xyDetailColor);

                XY xy = xys.get(touchPoint);

                String text2 = this.xValue.get(touchPoint);
                textPaint.setTextSize(detailText1Size);
                float text1Length = textPaint.measureText(text2) + detailText1Size;
                textPaint.setTextSize(detailText2Size);
                String text1 = Utils.generateFormatMoney("$", this.xYValues.get(touchPoint));
                float text2Length = textPaint.measureText(text1) + detailText2Size;
                detailWidth = (int)((int)Math.max(text1Length, text2Length) * 1.5f);
                float start_x = (xy.x - 0.5f * detailWidth);
                float end_x = start_x + detailWidth;
                detailHeight = (int)(1.2f * (detailText1Size + detailText2Size));
                float end_y = (xy.y - detailTouchY - xOYRTouch);
                float start_y = end_y - detailHeight;

                if (start_y < titleLineY) {
                    start_y = xy.y + xOYRTouch + detailTouchY;
                    end_y = start_y + detailHeight;

                }
                if (end_x > getMeasuredWidth()) {
                    end_x = getMeasuredWidth() - detailTouchY;

                    start_x = end_x - detailWidth;
                }
                if (start_x < 0) {

                    start_x = detailTouchY;
                    end_x = start_x + detailWidth;
                }
                // 矩形
                RectF outerRect = new RectF(start_x, start_y, end_x, end_y);
                canvas.drawRoundRect(outerRect, detailRoundR, detailRoundR, mPaint);

                textPaint.setColor(Color.WHITE);
                textPaint.setTextSize(detailText1Size);
                textPaint.setTextAlign(Paint.Align.CENTER);
                float text1Y = start_y + 1f * detailText1Size;
                canvas.drawText(text1, (start_x + end_x) / 2, text1Y, textPaint);

                textPaint.setTextSize(detailText2Size);
                float text2Y = end_y - 0.5f * detailText2Size;
                canvas.drawText(text2, (start_x + end_x) / 2, text2Y, textPaint);

            }
        }
        // 画slidbar
        if (isTwoPointTouch) {
            drawSlidbar(canvas, x0, y0, 0);
            drawSlidbar(canvas, x1, y1, 1);
        }
        if (showPoint0) {
            drawSlidbar(canvas, x0, y0, 0);
        }
        if (showPoint1) {

            drawSlidbar(canvas, x1, y1, 1);

        }
        if (showTwoPointDetail) {
            drawSlidbar(canvas, x0, y0, 0);
            drawSlidbar(canvas, x1, y1, 1);
            // 画中间的
            float x = (x0 + x1) / 2;
            float startY = titleLineY + twoPointDetailRectMargin;
            float endY = yAxisStartYPosition - twoPointDetailRectMargin;

            textPaint.setTextSize(twoPointDetailText1Size);
            float length = textPaint.measureText(twoPointDetailText1);
            textPaint.setTextSize(twoPointDetailText2Size);
            length = Math.max(length, textPaint.measureText(twoPointDetailText2)) * 1.2f;

            float startX = x - length / 2;
            float endX = x + length / 2;
            if (startX < 0) {
                startX = startX + detailTouchY;
                endX = startX + length;
            }
            if (endX > getMeasuredWidth()) {
                endX = getMeasuredWidth() - detailTouchY;
                startX = endX - length;

            }
            RectF outerRect = new RectF(startX, startY, endX, endY);
            mPaint.setColor(xyDetailColor);
            // 画圆角矩形
            canvas.drawRoundRect(outerRect, detailRoundR, detailRoundR, mPaint);
            // 画下三角
            mpath.reset();
            mpath.moveTo(x, endY + twoPointDetailSanjiaoLength);
            mpath.lineTo(x - twoPointDetailSanjiaoLength / 2, endY);
            mpath.lineTo(x + twoPointDetailSanjiaoLength / 2, endY);
            mpath.close();
            canvas.drawPath(mpath, mPaint);
            // 画文字
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize(twoPointDetailText1Size);
            textPaint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText(twoPointDetailText1,
                    startX + (endX - startX - textPaint.measureText(twoPointDetailText1)) / 2,
                    startY + twoPointDetailText1Size, textPaint);

            textPaint.setTextSize(twoPointDetailText2Size);
            canvas.drawText(twoPointDetailText2,
                    startX + (endX - startX - textPaint.measureText(twoPointDetailText2)) / 2, endY
                            - twoPointDetailText2Size, textPaint);

        }

        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(residualCapitalSize);
        Typeface typeface = Typeface.DEFAULT_BOLD;
        textPaint.setTypeface(typeface);
        textPaint.setTextAlign(Paint.Align.RIGHT);
        float enddy = yAxisStartYPosition - twoPointDetailSanjiaoLength;

        // 画文字residual
        float startY = titleLineY + padding / 2;

        float yy = (startY + enddy) / 2;
        canvas.drawText(residual, yAxisXPosition, yy - yPaintScale, textPaint);
        canvas.drawText(capital, yAxisXPosition, yy + yPaintScale, textPaint);
        if (showDetail && signType) {
            // 画有焦点的竖线
            mPaint.setStrokeWidth(ypaintWidthFoucs);
            mPaint.setColor(yFocusColor);
            float xx = yAxisXPosition + (touchPoint + 1) * xCellLength;
            // canvas.drawLine(xx, yAxisStartYPosition, xx, yAxisEndYPosition,
            // mPaint);

            XY xy = xys.get(touchPoint);

            mPaint.setColor(xyTouchColor);
            canvas.drawCircle(xy.x, xy.y, xOYRTouch, mPaint);
            // titleLineY

            textPaint.setTextSize(detailText2Size);
            String left = "cap.residue";
            String right = "amount install";

            double xyValue = xYValues.get(touchPoint);
            String left1 = Utils.generateFormatMoney("$", xyValue);

            String right1 = Utils
                    .generateFormatMoney("$", installments.get(touchPoint).getAmount());

            float length = Math.max(textPaint.measureText(left), textPaint.measureText(right));
            length = padding / 2 + 2 * length;
            textPaint.setTextSize(detailText1Size);
            float length1 = Math.max(textPaint.measureText(left1), textPaint.measureText(right1));
            length1 = padding / 2 + 2 * length1;
            length = Math.max(length, length1);
            float startX = xy.x - length / 2;
            // startY = titleLineY + padding / 2;
            float endY = xys.get(touchPoint).y - detailTouchY - xOYRTouch;
            startY = (float)(endY - (detailText1Size + 2 * detailText2Size) * 1.2);
            float endX = startX + length;
            // float endY = yAxisStartYPosition - twoPointDetailSanjiaoLength;
            if (endX > getMeasuredWidth()) {
                endX = getMeasuredWidth() - detailTouchY;
                startX = endX - length;
            }
            if (startX < yAxisXPosition + detailTouchY) {
                startX = yAxisXPosition + detailTouchY;
                endX = startX + length;
            }
            float x = (startX + endX) / 2;
            // 画矩形
            RectF rectF = new RectF(startX, startY, endX, endY);
            mPaint.setColor(xyDetailColor);
            // 画圆角矩形
            canvas.drawRoundRect(rectF, detailRoundR, detailRoundR, mPaint);

            textPaint.setTypeface(Typeface.DEFAULT);
            // 画下三角
            // mpath.reset();
            // mpath.moveTo(xy.x, endY + twoPointDetailSanjiaoLength);
            // mpath.lineTo(xy.x - twoPointDetailSanjiaoLength / 2, endY);
            // mpath.lineTo(xy.x + twoPointDetailSanjiaoLength / 2, endY);
            // mpath.close();
            // canvas.drawPath(mpath, mPaint);

            // 画文字
            // 第一行
            float y = startY + detailText2Size;
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize(detailText2Size);
            textPaint.setTextAlign(Paint.Align.CENTER);
            String xText = xValue.get(touchPoint);

            xText = xText.replace('.', ' ');

            canvas.drawText(xText, x, y, textPaint);

            y = y + detailText2Size;
            textPaint.setTextAlign(Paint.Align.CENTER);

            canvas.drawText(left, x - length / 4, y, textPaint);
            textPaint.setTextSize(detailText1Size);

            canvas.drawText(left1, x - length / 4, y + detailText1Size, textPaint);

            textPaint.setTextAlign(Paint.Align.CENTER);
            textPaint.setTextSize(detailText2Size);
            canvas.drawText(right, x + length / 4, y, textPaint);
            textPaint.setTextSize(detailText1Size);
            canvas.drawText(right1, x + length / 4, y + detailText1Size, textPaint);

            // 画中间线
            mPaint.setColor(Color.WHITE);
            mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            LinearGradient lg = new LinearGradient(x, y, x, y + detailText1Size, xyColor,
                    Color.WHITE, Shader.TileMode.CLAMP);

            mPaint.setShader(lg);
            canvas.drawLine(x, y, x, y + detailText1Size, mPaint);
            lg = new LinearGradient(x, y + detailText1Size, x, y + 2 * detailText2Size,
                    Color.WHITE, xyColor, Shader.TileMode.CLAMP);
            mPaint.setShader(lg);
            canvas.drawLine(x, y + detailText1Size, x, y + 2 * detailText2Size, mPaint);

            mPaint.setShader(null);

        }

    }

    private void drawSlidbar(Canvas canvas, float x, float y, int index) {
        // x = x - 100;
        // y = y - 100;

        mPaint.setColor(Color.rgb(150, 150, 150));
        canvas.drawCircle(x, y, slidBarR, mPaint);
        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(x, y, slidBarR - 1, mPaint);
        mPaint.setColor(xyDetailColor);
        canvas.drawCircle(x, y, slidBarR * 0.8f, mPaint);

        // canvas.drawText(String.valueOf(index), x, y, textPaint);

    }

    /**
     * 双点触摸开关
     */
    boolean isTwoPointTouch = false;

    /**
     * 是否显示双点
     */
    boolean showTwoPoint = false;

    /**
     * 是否显示双点详情
     */
    boolean showTwoPointDetail = false;

    /**
     * 是否吸附第1个点
     */
    boolean showPoint1 = false;

    /**
     * 是否吸附第0个点
     */
    boolean showPoint0 = false;

    /**
     * x0在x1在右边x0大于x1
     */
    float x0 = -100, x1 = -100, y0 = -100, y1 = -100;

    boolean touchSlid0;

    boolean touchSlid1;

    boolean up0 = false;

    int f = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int pointerCount = event.getPointerCount();
        float x = event.getX();
        float y = event.getY();
        int touch = touchThePoint(x, y);

        LogManager.d("touch" + action + "count" + pointerCount);
        float minX = xys.get(0).x;
        float maxX = xys.get(xys.size() - 1).x;

        if (action != MotionEvent.ACTION_UP && (x > maxX + slidBarR || x < minX - slidBarR)) {
            return true;
        }
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (up0) {
                    up0 = false;
                    // int from = adsorptionPoint1();
                    // int to = adsorptionPoint0();
                    //
                    // performSlid(from, to);
                    // invalidate();
                    LogManager.d("bug fix");
                    return true;
                }
                // 如果已经显示twoPointDetail
                if (showTwoPointDetail) {
                    touchSlid0 = isTouchSlid0Bar(x, y);
                    touchSlid1 = isTouchSlid1Bar(x, y);

                    if (pointerCount == 1) {
                        if (touchSlid0) {
                            x0 = x;
                            y0 = getSlidBarY(x0);
                        } else if (touchSlid1) {
                            x1 = x;
                            y1 = getSlidBarY(x1);
                        }
                    }

                    if (touchSlid0 || touchSlid1) {

                        show2Point();
                        hideDetail();
                        hideTouchPoint();
                    } else if (touch > -1) {
                        showTwoPointDetail = false;
                        downTouchThePoint = true;
                        isTwoPointTouch = false;
                        hide2Point();

                        showTouchPoint(touch);

                    } else {
                        if (touch > -1) {
                            showTwoPointDetail = false;
                            downTouchThePoint = true;
                            isTwoPointTouch = false;
                            hide2Point();

                            showTouchPoint(touch);

                        }
                    }
                } else {

                    // 两点没显示
                    touchSlid0 = isTouchSlid0Bar(x, y);
                    touchSlid1 = isTouchSlid1Bar(x, y);

                    if (pointerCount == 1) {
                        if (touchSlid0) {
                            x0 = x;
                            y0 = getSlidBarY(x0);
                        } else if (touchSlid1) {
                            x1 = x;
                            y1 = getSlidBarY(x1);
                        }
                    }

                    if (touch > -1) {
                        showTwoPointDetail = false;
                        downTouchThePoint = true;
                        isTwoPointTouch = false;
                        hide2Point();

                        showTouchPoint(touch);

                    } else if (touchSlid0 || touchSlid1) {

                        show2Point();
                        hideDetail();
                        hideTouchPoint();
                    } else {
                        if (touch > -1) {
                            showTwoPointDetail = false;
                            downTouchThePoint = true;
                            isTwoPointTouch = false;
                            hide2Point();

                            showTouchPoint(touch);

                        }
                    }

                }

                break;

            case MotionEvent.ACTION_POINTER_2_DOWN: {
                if (signType) {
                    return true;
                }
                hideTouchPoint();
                isTwoPointTouch = true;
                showTwoPoint = true;
                hideDetail();
                float xx0 = event.getX(0);
                float xx1 = event.getX(1);
                x1 = Math.min(xx0, xx1);
                y1 = getSlidBarY(x1);
                x0 = Math.max(xx0, xx1);
                y0 = getSlidBarY(x0);

                break;

            }
            case MotionEvent.ACTION_MOVE: {
                if (x > maxX || x < minX) {
                    return true;
                }
                if (isTwoPointTouch) {
                    showTwoPointDetail = false;
                    if (pointerCount == 1) {
                        if (touchSlid0) {
                            if (x < xys.get(0).x + slidBarR) {
                                return true;
                            }
                            x0 = x;
                            y0 = getSlidBarY(x0);
                            if (x0 - slidBarR < x1) {
                                x1 = x0 - slidBarR;
                                y1 = getSlidBarY(x1);
                            }
                        } else if (touchSlid1) {
                            if (x > xys.get(xys.size() - 1).x - slidBarR) {
                                return true;
                            }
                            x1 = x;
                            y1 = getSlidBarY(x1);
                            if (x1 + slidBarR > x0) {
                                x0 = x1 + slidBarR;
                                y0 = getSlidBarY(x0);
                            }
                        }

                    } else if (pointerCount == 2) {
                        float xx0 = event.getX(0);

                        float xx1 = event.getX(1);
                        x1 = Math.min(xx0, xx1);
                        x0 = Math.max(xx0, xx1);
                        if (x1 < minX) {
                            x1 = minX;
                        }
                        if (x1 > maxX) {
                            x1 = maxX;
                        }
                        if (x0 < minX) {
                            x0 = minX;
                        }
                        if (x0 > maxX) {
                            x0 = maxX;
                        }
                        y1 = getSlidBarY(x1);

                        y0 = getSlidBarY(x0);
                    }
                    hideDetail();

                } else {
                    if (touch > -1 && downTouchThePoint) {
                        showTouchPoint(touch);
                        isTwoPointTouch = false;
                    } else {

                    }
                }

                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {

                if (touch > -1 && downTouchThePoint) {

                    showDeatil(touch);
                } else {
                    hideTouchPoint();
                    hideDetail();
                }
                downTouchThePoint = false;
                if (isTwoPointTouch) {
                    showTwoPointDetail = true;
                    if (touchSlid0) {
                        int to = adsorptionPoint0();

                        int from = adsorptionPoint1();
                        performSlid(from, to);
                    } else if (touchSlid1) {

                        int from = adsorptionPoint1();
                        int to = adsorptionPoint0();
                        performSlid(from, to);
                    } else {
                        // 双指触摸先放1后放0时
                        int to = adsorptionPoint0();
                        int from = adsorptionPoint1();
                        performSlid(from, to);
                    }
                    touchSlid0 = false;
                    touchSlid1 = false;

                    invalidate();
                }

                break;
            }
            case MotionEvent.ACTION_POINTER_1_UP: {
                // 先放0时
                up0 = true;
                int from = adsorptionPoint1();
                int to = adsorptionPoint0();
                performSlid(from, to);

                invalidate();

                break;
            }
            case MotionEvent.ACTION_POINTER_2_UP: {
                showPoint1 = true;
                adsorptionPoint1();
                invalidate();
                break;
            }

            default:
                break;
        }

        return true;
    }

    private int adsorptionPoint1() {
        if (signType) {
            return -1;
        }
        int result = -1;
        float deta = Float.MAX_VALUE;
        int minIndex1 = -1;
        for (int i = 0; i < xys.size(); i++) {

            XY xy = xys.get(i);
            float deta1 = Math.abs(xy.x - x1);

            if (deta1 < deta) {
                deta = deta1;
                minIndex1 = i;
            }
        }

        float x = xys.get(minIndex1).x;
        if (x0 <= x) {
            x1 = xys.get(minIndex1 - 1).x;
            y1 = getSlidBarY(x1);
            result = minIndex1 - 1;
        } else {
            x1 = xys.get(minIndex1).x;
            y1 = getSlidBarY(x1);
            result = minIndex1;
        }
        return result;

    }

    private int adsorptionPoint0() {
        if (signType) {
            return -1;
        }
        int result = -1;
        float deta = Float.MAX_VALUE;
        int minIndex1 = -1;
        for (int i = 0; i < xys.size(); i++) {

            XY xy = xys.get(i);
            float deta1 = Math.abs(xy.x - x0);

            if (deta1 < deta) {
                deta = deta1;
                minIndex1 = i;
            }
        }

        float x = xys.get(minIndex1).x;

        if (x1 >= x) {
            x0 = xys.get(minIndex1 + 1).x;
            y0 = getSlidBarY(x0);
            result = minIndex1 + 1;
        } else {
            x0 = xys.get(minIndex1).x;
            y0 = getSlidBarY(x0);
            result = minIndex1;
        }

        return result;
    }

    private void show2Point() {

        isTwoPointTouch = true;
        showPoint1 = true;
        showPoint0 = true;
    }

    private void hide2Point() {
        showPoint1 = false;
        showPoint0 = false;
    }

    /**
     * 隐藏被点击的节点
     */
    private void hideTouchPoint() {
        touchPoint = -1;
        invalidate();
    }

    int touchPoint = -1;

    /**
     * 显示被点击的节点
     * 
     * @param touch
     */
    private void showTouchPoint(int touch) {

        touchPoint = touch;
        invalidate();
        performOnclick();

    }

    boolean showDetail = false;

    /**
     * 显示详情
     */
    private void showDeatil(int touch) {

        showDetail = true;
        invalidate();
    }

    private void hideDetail() {

        showDetail = false;
        invalidate();
    }

    boolean downTouchThePoint = false;

    /**
     * 判断手势是否碰到了拆线点
     * 
     * @return
     */
    private int touchThePoint(float x, float y) {
        int result = -1;
        for (int i = 0; i < xys.size(); i++) {
            XY xy = xys.get(i);

            float beginX = xy.x - 1.2f * xOYRTouch;
            float endX = xy.x + 1.2f * xOYRTouch;
            float beginY = xy.y - 1.2f * xOYRTouch;
            float endY = xy.y + 1.2f * xOYRTouch;

            if (x > beginX && x < endX && y > beginY && y < endY) {

                result = i;
                return result;
            }

        }
        return result;

    }

    /**
     * @param srcx
     * @param srcy
     * @param x 手指位置
     * @param y
     * @return
     */
    private boolean isTouchSlidBar(float srcx, float srcy, float x, float y) {

        float beginX = srcx - 1.2f * xOYRTouch;
        float endX = srcx + 1.2f * xOYRTouch;
        float beginY = srcy - 1.2f * xOYRTouch;
        float endY = srcy + 1.2f * xOYRTouch;

        if (x > beginX && x < endX && y > beginY && y < endY) {

            return true;

        }
        return false;

    }

    private boolean isTouchSlid1Bar(float x, float y) {

        return isTouchSlidBar(x1, y1, x, y);

    }

    private boolean isTouchSlid0Bar(float x, float y) {
        // if (!showPoint1) {
        // if (x1 < 0) {
        // x1 = xys.get(0).x;
        // y1 = getSlidBarY(x1);
        // x0 = x;
        //
        // }
        //
        // if (x1 > x0) {
        // x0 = x1 + slidBarR;
        // }
        //
        // return true;
        // }
        return isTouchSlidBar(x0, y0, x, y);

    }

    private float getSlidBarY(float x) {

        int index = -1;
        float result = -1;
        for (int i = 0; i < xys.size(); i++) {
            XY xy = xys.get(i);
            float detaX = xy.x - x;
            if (detaX >= 0) {
                index = i;
                break;
            }

        }

        if (index <= 0) {
            return xys.get(0).y;
        }
        XY xy0 = xys.get(index - 1);
        XY xy1 = xys.get(index);

        float k = (xy1.y - xy0.y) / (xy1.x - xy0.x);

        // k=(xy1.y-?)/(xy1.x-x);
        result = xy1.y - k * (xy1.x - x);

        return result;
    }

    public static class XY {
        float x;

        float y;
    }

    private void performOnclick() {

        // touchPoint;

    }

    private void performSlid(int from, int to) {
        if (signType) {
            return;
        }

        LogManager.d("from=" + from + "to=" + to);

        double fromXY = xYValues.get(from);

        double toXY = xYValues.get(to);

        double deta = toXY - fromXY;

        double min = Integer.MAX_VALUE;
        double max = -Integer.MAX_VALUE;
        for (int i = 0; i < xYValues.size(); i++) {
            min = Math.min(xYValues.get(i), min);
            max = Math.max(xYValues.get(i), max);
        }

        StringBuffer sb = new StringBuffer();
        sb.append(Utils.generateFormatMoney("$", deta));
        sb.append("(");
        double deta100 = 100 * (deta / (max - min));
        String deta100str = Utils.generateMoney(deta100);
        sb.append(deta100str + "%");
        sb.append(")");

        String fromX = xValue.get(from);
        String toX = xValue.get(to);
        twoPointDetailText1 = sb.toString();
        sb = new StringBuffer();
        sb.append(fromX);
        sb.append("-");
        sb.append(toX);
        twoPointDetailText2 = sb.toString();

        if (mOnSlidListener != null) {

            mOnSlidListener.onSlidTwoBar(this, from, to);
        }
    }

    OnSlidListener mOnSlidListener;

    public void setOnSlidListener(OnSlidListener onSlidListener) {
        this.mOnSlidListener = onSlidListener;

    }

    public static interface OnSlidListener {
        void onSlidTwoBar(View v, int from, int to);
    }

    public static interface onPointClickListener {

        void onClick();
    }

}
