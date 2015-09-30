
package com.accenture.mbank.view.table;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

public class Bar {
    private static final int WIDTH = 20;

    public int left;

    private int right;

    public int top;

    public int bottom;
    
    public int middle;
    
    public String xValue;
    
    public String yValue;
    
    public double withdrawal;

    public Bar(int middle, int top, int bottom) {
        this.middle = middle;
        this.left = middle - WIDTH / 2;
        this.right = middle + WIDTH / 2;
        this.top = top;
        this.bottom = bottom;
    }

    public void drawSelf(Canvas canvas, Paint paint) {
        canvas.drawRect(left, top, right, bottom, paint);
    }
    
    public boolean isPointInBar(Point point) {
        if (point.x >= left && point.x <= right && point.y >= top && point.y <= bottom) {
            return true;
        }
        
        return false;
    }
}
