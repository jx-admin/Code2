package wu.a.lib.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;

/**
 * Created by jx on 2016/9/26.
 */
public class UtilsBitmap {

    /**
     * @param source
     * @param x
     * @param y
     * @param width
     * @param height
     * @param offx 变换后的起始位置
     * @param offy 变换后的起始位置
     * @param offW 变换后的宽偏移
     * @param offH 变换后的宽偏移
     * @param m
     * @param filter
     * @return
     */
    public static Bitmap createBitmap(Bitmap source, int x, int y, int width, int height, int offx, int offy, int offW,
                                      int offH, Matrix m, boolean filter) {
        int neww = width;
        int newh = height;
        Canvas canvas = new Canvas();
        Bitmap bitmap;
        Paint paint;

        Rect srcR = new Rect(x, y, x + width, y + height);
        RectF dstR = new RectF(0, 0, width, height);

        Bitmap.Config newConfig = Bitmap.Config.ARGB_8888;
        final Bitmap.Config config = source.getConfig();
        // GIF files generate null configs, assume ARGB_8888
        if (config != null) {
            switch (config) {
                case RGB_565:
                    newConfig = Bitmap.Config.RGB_565;
                    break;
                case ALPHA_8:
                    newConfig = Bitmap.Config.ALPHA_8;
                    break;
                // noinspection deprecation
                case ARGB_4444:
                case ARGB_8888:
                default:
                    newConfig = Bitmap.Config.ARGB_8888;
                    break;
            }
        }

        if (m == null || m.isIdentity()) {
            neww = neww + offW;
            newh = newh + offH;
            canvas.translate(offx, offy);
            bitmap = Bitmap.createBitmap(neww, newh, newConfig);
            paint = null; // not needed
        } else {
            final boolean transformed = !m.rectStaysRect();

            RectF deviceR = new RectF();
            m.mapRect(deviceR, dstR);

            neww = Math.round(deviceR.width()) + offW;
            newh = Math.round(deviceR.height()) + offH;

            bitmap = Bitmap.createBitmap(neww, newh, transformed ? Bitmap.Config.ARGB_8888 : newConfig);

            canvas.translate(-deviceR.left + offx, -deviceR.top + offy);
            canvas.concat(m);

            paint = new Paint();
            paint.setFilterBitmap(filter);
            if (transformed) {
                paint.setAntiAlias(true);
            }
        }

        // The new bitmap was created from a known bitmap source so assume that
        // they use the same density
        bitmap.setDensity(source.getDensity());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            bitmap.setHasAlpha(source.hasAlpha());
        }
        // bitmap.setPremultiplied(source.ismRequestPremultiplied);

        canvas.setBitmap(bitmap);
        canvas.drawBitmap(source, srcR, dstR, paint);
        canvas.setBitmap(null);

        return bitmap;
    }
}
