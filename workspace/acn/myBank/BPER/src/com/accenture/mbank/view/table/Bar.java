
package com.accenture.mbank.view.table;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

public class Bar {
    private static int WIDTH = 20;

    private static int WIDTH_MIN = 20;
    public int left;

    private int right;

    public int top;

    public int bottom;
    
    public int middle;
    
    public String xValue;
    
    public String yValue;
    
    public double withdrawal;
    
    public float XOYR = 20;

    public Bar(int middle, int top, int bottom) {
        this.middle = middle;
        this.left = middle - WIDTH / 2;
        this.right = middle + WIDTH / 2;
        this.top = top;
        this.bottom = bottom;
    }

    public static void setBarWidth(int width) {
    	Bar.WIDTH = width;
    	if (Bar.WIDTH < WIDTH_MIN)
    		Bar.WIDTH = WIDTH_MIN;
    }

    public void drawSelf(Canvas canvas, Paint paint) {
        canvas.drawRect(left, top, right, bottom, paint);
    }
    
    public boolean isPointInBar(Point point) {
        if (point.x >= left && point.x <= right && point.y >= top && point.y <= bottom) {
            return true;
        }
        
        /*
         * Also returns true if the top point circle is touched.
         */
        float beginX = middle - 4f * XOYR;
        float endX = middle + 4f * XOYR;
        float beginY;
        float endY;
        
        /*
         * If the value is less than zero, the point shows at bottom of the bar.
         */
        if (withdrawal >= 0) {
        	beginY = top - 4f * XOYR;
        	endY = top + 4f * XOYR;
        }
        else {
        	beginY = bottom - 4f * XOYR;
        	endY = bottom + 4f * XOYR;
        }

        if (point.x > beginX && point.x < endX && point.y > beginY && point.y < endY) {
        	return true;
        }
        return false;
    }
}
