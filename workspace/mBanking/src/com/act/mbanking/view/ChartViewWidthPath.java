
package com.act.mbanking.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;

public class ChartViewWidthPath extends BaseChartView {

    protected Path mPath;

    Paint pathPaint;

    public ChartViewWidthPath(Context context, AttributeSet attrs) {
        super(context, attrs);

        mPath = new Path();

        pathPaint = new Paint();
        pathPaint.setStyle(Style.FILL_AND_STROKE);
        pathPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // mPath.reset();
        // mPath.moveTo(o_x, o_y);
        // float x = 0;
        // float y = 0;
        // for (int i = 0; i < xValues.length; i++) {
        // x = o_x + i * cellXWidth;
        // y = o_y - (float)(200 * Math.random());
        // LogManager.d("path-x" + x + "y" + y);
        // mPath.lineTo(x, y);
        // }
        // mPath.lineTo(x, o_y);
        // mPath.close();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // canvas.drawPath(mPath, pathPaint);
    }

}
