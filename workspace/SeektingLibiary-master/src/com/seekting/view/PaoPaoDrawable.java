
package com.seekting.view;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.widget.TextView;

/**
 * 1.0
 * 
 * @author seekting.x.zhang
 */
public class PaoPaoDrawable extends Drawable {

    int textPadding = 7;

    int textSize;

    public int num = 1;

    Drawable src;

    Paint paint = new Paint();

    Paint textPaint = new Paint();

    int textColor;

    ColorStateList textColorStateList;

    int textFocusColor;

    TextView textView;

    Drawable textBg;

    int maxNum = -1;

    int srcWidth;

    int srcHeight;

    int textBackground;

    public PaoPaoDrawable(Drawable src, int textSize, int textColor, int textBackground, int maxNum) {
        this.textSize = textSize;
        this.src = src;
        this.textColor = textColor;
        this.textBackground = textBackground;
        this.maxNum = maxNum;

        init();
    }

    public PaoPaoDrawable(Drawable src, int textSize, int textColor, Drawable textBackground,
            int maxNum) {
        this.textSize = textSize;
        this.src = src;
        this.textColor = textColor;
        this.textBg = textBackground;
        this.maxNum = maxNum;

        init();
    }

    public PaoPaoDrawable(Drawable src, int textSize, ColorStateList textColor,
            Drawable textBackground, int maxNum) {
        this.textSize = textSize;
        this.src = src;
        this.textColorStateList = textColor;
        this.textBg = textBackground;
        this.maxNum = maxNum;

        init();
    }

    private void init() {

        int initWidth = src.getIntrinsicWidth();
        int initHeight = src.getIntrinsicHeight();
        Rect rect = new Rect(0, 0, initWidth, initHeight);
        setBounds(rect);
        paint.setAntiAlias(true);
        textPaint.setAntiAlias(true);
        // textPaint.setTextAlign(Align.CENTER);
        srcWidth = initWidth;
        srcHeight = initHeight;
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);

    }

    public int getIntrinsicWidth() {

        srcWidth = src.getIntrinsicWidth();
        return srcWidth;
    }

    public int getIntrinsicHeight() {

        srcHeight = src.getIntrinsicHeight();
        return srcHeight;
    }

    @Override
    public boolean setState(int[] stateSet) {

        super.setState(stateSet);
        boolean state = false;
        if (textBg != null) {
            if (textBg instanceof StateListDrawable) {
                state = textBg.setState(stateSet);
            }
        }
        if (textColorStateList != null) {
            state = state | true;
        }
        if (src instanceof StateListDrawable) {

            state = state | src.setState(stateSet);

        }
        if (state) {
            onStateChange(stateSet);
        }
        return state;
    }

    @Override
    public boolean isStateful() {
        boolean isStateful = false;
        if (textBg != null) {
            isStateful = textBg.isStateful();

        }
        if (textColorStateList != null) {
            isStateful = isStateful | textColorStateList.isStateful();
        }

        isStateful = isStateful | src.isStateful();

        return isStateful;
    }

    @Override
    public ConstantState getConstantState() {
        return src.getConstantState();
    }

    @Override
    public void draw(Canvas canvas) {
        if (src instanceof StateListDrawable) {
            System.out.println("xx");
        }
        Rect rect = new Rect(0, 0, srcWidth, srcHeight);
        src.setBounds(rect);

        Drawable drawable = src.getCurrent();
        System.out.println("drawable" + drawable);

        src.draw(canvas);

        if (num > 0) {
            int numStr = num;
            if (num >= maxNum) {
                numStr = maxNum;
            }
            String text = String.valueOf(numStr);
            float length = textPaint.measureText(text);
            float beginX = srcWidth - length - textPadding * 2;
            float endX = srcWidth;
            float beginY = 0;
            FontMetrics fm = textPaint.getFontMetrics();
            float endY = fm.descent - fm.ascent;
            float d = endY;
            float o_x = (beginX + endX) / 2;
            float o_y = (beginY + endY) / 2;
            if (textBg != null) {
                Rect padding = new Rect();
                textBg.getPadding(padding);

                double maxWidth = Math.max(endX - beginX,
                        textBg.getIntrinsicWidth() - padding.width());

                maxWidth = endX - beginX;
                textBg.setBounds(0, 0, (int)(maxWidth) + padding.width(), (int)(endY - beginY)
                        + padding.height());

                int x = (int)(srcWidth - textBg.getBounds().width());
                canvas.save();
                canvas.translate(x, 0);
                o_x = x + textBg.getBounds().width() / 2;
                textBg.draw(canvas);
                canvas.restore();
            } else {
                paint.setStyle(Style.FILL);
                paint.setColor(textBackground);

                // canvas.drawCircle(o_x, o_y, (endY - beginY) / 2, paint);

                RectF oval = new RectF();
                oval.top = beginY;
                oval.bottom = endY;
                oval.left = beginX;
                oval.right = endX;
                canvas.drawArc(oval, 0, 360, true, paint);
            }

            float baseLine = endY - fm.descent;

            if (textColorStateList != null) {
                int currentColor = textColorStateList.getColorForState(getState(),
                        textColorStateList.getDefaultColor());
                textPaint.setColor(currentColor);
            }
            canvas.drawText(String.valueOf(numStr), o_x - length / 2, baseLine, textPaint);
            //
            // FontMetrics fm = textPaint.getFontMetrics();
            //
            // textPaint.setColor(Color.BLACK);
            // canvas.save();
            // canvas.translate(0, 30);
            // canvas.drawText("xxYz��", 0, 0, textPaint);
            // canvas.drawLine(0, fm.ascent, bitmap.getWidth(), fm.ascent,
            // textPaint);
            // canvas.drawLine(0, fm.descent, bitmap.getWidth(), fm.descent,
            // textPaint);
            //
            // canvas.restore();
        }
    }

    public float getFontHeight() {
        FontMetrics fm = textPaint.getFontMetrics();
        return fm.descent - fm.ascent;
        // return (int)Math.ceil(fm.descent - fm.top) + 2;
        // return (int)Math.ceil(fm.top - fm.bottom);
    }

    @Override
    public void setAlpha(int alpha) {

        src.setAlpha(alpha);

    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        src.setColorFilter(cf);

    }

    @Override
    public boolean getPadding(Rect padding) {
        // TODO Auto-generated method stub
        return src.getPadding(padding);
    }

    @Override
    public int getOpacity() {
        return src.getOpacity();
    }

    @Override
    protected boolean onStateChange(int[] state) {

        boolean needInvalidateSelf = false;
        if (textBg != null && textBg instanceof StateListDrawable) {
            needInvalidateSelf = needInvalidateSelf | true;
        }
        if (src instanceof StateListDrawable) {
            needInvalidateSelf = needInvalidateSelf | true;

        }
        if (textColorStateList != null) {
            needInvalidateSelf = needInvalidateSelf | true;
        }
        if (needInvalidateSelf) {
            invalidateSelf();
        }
        return needInvalidateSelf;
    }
}
