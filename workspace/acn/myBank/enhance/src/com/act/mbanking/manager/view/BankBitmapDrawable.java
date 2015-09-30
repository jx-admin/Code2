
package com.act.mbanking.manager.view;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Shader.TileMode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.act.mbanking.utils.ColorManager;
import com.act.mbanking.utils.LogManager;

/**
 * 因为本应用需要好多同色系的图，所以特用此类来搞定他们
 * 
 * @author seekting.x.zhang
 */
public class BankBitmapDrawable extends Drawable {

    public BitmapDrawable src;

    private int bitmapLevel;

    Paint paint = null;

    ColorMatrix colorMatrix = new ColorMatrix();

    private int mainColor;

    private int maxLevel;

    int width;

    int height;

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public int getMainColor() {
        return mainColor;
    }

    public void setMainColor(int mainColor) {
        this.mainColor = mainColor;
    }

    public int getBitmapLevel() {
        return bitmapLevel;
    }

    public void setBitmapLevel(int bitmapLevel) {
        this.bitmapLevel = bitmapLevel;
    }

    public BankBitmapDrawable(BitmapDrawable src, int drawableType) {

        this.src = src;

        paint = new Paint();
        paint.setAntiAlias(true);
        initBounds();
        this.drawableType = drawableType;
    }

    int initwidth;

    int initHeight;

    public void initBounds() {

        Bitmap bitmap = src.getBitmap();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        setBounds(rect);
        width = initwidth = bitmap.getWidth();
        height = initHeight = bitmap.getHeight();
    }

    @Override
    public void draw(Canvas canvas) {

        if (src != null) {
            if (src.getBitmap() != null) {

                Bitmap bitmap = src.getBitmap();

                // 画一个带level颜色变化 的图
                Bitmap bitmap2 = createBitmap(bitmap, initwidth, initHeight);

                Rect bounds = getBounds();
                LogManager.d("bounds" + bounds);

                BitmapDrawable drawable = new BitmapDrawable(bitmap2);
                drawable.setBounds(bounds);
                drawable.draw(canvas);
                bitmap2.recycle();

            }
        }
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        width = bounds.right - bounds.left;
        height = bounds.bottom - bounds.top;
    }

    private Bitmap createBitmap(Bitmap bitmap, int width, int height) {
        // 代表是圆
        Bitmap bitmap2 = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas2 = new Canvas(bitmap2);
        paint.setStyle(Style.FILL);
        int color = 0;
        switch (mainColor) {
            case COLOR_LOANS: {
                color = ColorManager.getLoansColor(bitmapLevel);

                break;
            }
            case COLOR_INVESTMENT: {
                color = ColorManager.getInvestmentColor(bitmapLevel);
                break;
            }
            case COLOR_ACCOUNT: {
                color = ColorManager.getAccountColor(bitmapLevel);
                break;
            }
            case COLOR_PREPARD_CARD: {
                color = ColorManager.getPrepaidCardsColor(bitmapLevel);
                break;
            }
            case COLOR_CREDIT_CARD: {
                color = ColorManager.getCreditCardsColor(bitmapLevel);
                break;
            }

        }
        paint.setColor(color);
        if (drawableType == drawable_type_half_circle) {
            paint.setStyle(Style.FILL);
            paint.setMaskFilter(null);

            int soldWidth = 1;
            canvas2.drawCircle(width / 2, height, height - soldWidth, paint);
//            BlurMaskFilter maskFilter = new BlurMaskFilter(soldWidth, BlurMaskFilter.Blur.SOLID);
//
//            paint.setMaskFilter(maskFilter);
//            paint.setStyle(Style.STROKE);
//            paint.setStrokeWidth(soldWidth);
//            paint.setColor(Color.argb(100, 100, 100, 100));
//
//            canvas2.drawCircle(width / 2, height, height - soldWidth, paint);

        } else if (drawableType == drawable_type_rect) {

            int tt = 25;
            int from = getRightColor(color);
            int to = getShardowColor(color);
            LinearGradient gradient = new LinearGradient(0, 0, 0, height, from, to, TileMode.MIRROR);
            RectF rect = new RectF(0, 0, width, height);
            paint.setShader(gradient);
            canvas2.drawRect(rect, paint);
        } else if (drawableType == drawable_type_cicle) {
            paint.setStyle(Style.FILL);
            paint.setMaskFilter(null);

            int soldWidth = 1;
            canvas2.drawCircle(width / 2, height / 2, height / 2 - soldWidth, paint);
            BlurMaskFilter maskFilter = new BlurMaskFilter(soldWidth, BlurMaskFilter.Blur.SOLID);
//
//            paint.setMaskFilter(maskFilter);
//            paint.setStyle(Style.STROKE);
//            paint.setStrokeWidth(soldWidth);
//            paint.setColor(Color.argb(100, 100, 100, 100));
//
//            canvas2.drawCircle(width / 2, height / 2, height - soldWidth, paint);

        }
        // colorMatrix.reset();
        // colorMatrix.set(getColorMatrix(getBitmapLevel(), mainColor,
        // maxLevel));
        // // 设置画笔的colorFilter
        // paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        // Bitmap bitmap2 = Bitmap.createBitmap(width, height,
        // Config.ARGB_8888);
        // Canvas canvas2 = new Canvas(bitmap2);
        // canvas2.drawBitmap(bitmap, 0, 0, paint);
        return bitmap2;
    }

    @Override
    public void setAlpha(int alpha) {

    }

    public int getColor() {

        if (src != null) {
            if (src.getBitmap() != null) {

                Bitmap bitmap = src.getBitmap();

                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                // 画一个带level颜色变化 的图
                Bitmap bitmap2 = createBitmap(bitmap, width, height);
                int color = bitmap2.getPixel(width / 2, height / 2);
                return color;

            }
        }
        return 0;
    }

    @Override
    public void setColorFilter(ColorFilter cf) {

    }

    @Override
    public int getIntrinsicHeight() {

        return initHeight;
    }

    public int getIntrinsicWidth() {
        return initwidth;
    }

    @Override
    public int getOpacity() {
        return 0;
    }

    public static final int ColorAdd = 15;

    public static float[] getColorMatrix(int level, int mainColor, int maxLevel) {
        float redWeight = 1f;
        float greenWeight = 1f;
        float blueWeight = 1f;
        int colorAdd = 0;
        if (maxLevel == 0) {
            colorAdd = ColorAdd;
        } else {
            colorAdd = 60 / maxLevel;
        }
        switch (mainColor) {
            case COLOR_INVESTMENT:
                greenWeight = 1.5f;
                break;

            case COLOR_PREPARD_CARD:
                redWeight = 1.5f;
                break;
            case COLOR_ACCOUNT:
                blueWeight = 1.5f;

            case COLOR_LOANS: {
                greenWeight = 1.5f;
                redWeight = 1.5f;
            }

            default:
                break;
        }
        float max[] = new float[] {
                1, 0, 0, 0, colorAdd * level * redWeight, 0, 1, 0, 0,
                colorAdd * level * greenWeight, 0, 0, 1, 0, colorAdd * level * blueWeight, 0, 0, 0,
                1, 0
        };
        return max;
    }

    int Right = 0;

    int shardow = -40;

    public int getRightColor(int color) {

        int alpha = Color.alpha(color);
        alpha = getRight(alpha + Right);
        int red = Color.red(color);
        red = getRight(red + Right);
        int green = Color.green(color);
        green = getRight(green + Right);
        int blue = Color.blue(color);
        blue = getRight(blue + Right);

        return Color.argb(alpha, red, green, blue);

    }

    public int getRight(int right) {

        if (right > 255) {
            right = 255;
        }
        return right;
    }

    public int getShardow(int shardow) {
        if (shardow < 0) {

            shardow = 0;
        }
        return shardow;

    }

    public int getShardowColor(int color) {

        int alpha = Color.alpha(color);
        // alpha = getShardow(alpha + shardow);
        int red = Color.red(color);
        red = getShardow(red + shardow);
        int green = Color.green(color);
        green = getShardow(green + shardow);
        int blue = Color.blue(color);
        blue = getShardow(blue + shardow);

        return Color.argb(alpha, red, green, blue);

    }

    public int drawableType = 0;

    /**
     * 矩形图
     */
    public static final int drawable_type_rect = 1;

    /**
     * 全圆图
     */
    public static final int drawable_type_cicle = 2;

    /**
     * 半圆图
     */
    public static final int drawable_type_half_circle = 3;

    public static final int COLOR_INVESTMENT = 1;

    public static final int COLOR_PREPARD_CARD = 2;

    public static final int COLOR_ACCOUNT = 3;

    public static final int COLOR_LOANS = 4;

    public static final int COLOR_CREDIT_CARD = 5;

}
