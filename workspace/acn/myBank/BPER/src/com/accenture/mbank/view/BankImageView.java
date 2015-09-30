
package com.accenture.mbank.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.view.View;

public class BankImageView extends View {

    Bitmap bitmap;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public synchronized void setBitmap(Bitmap bitmap) {

        // LogManager.d("noImage" + this + "begin setBitmap");
        if (this.bitmap == null) {

            this.bitmap = bitmap;
        } else {
            if (this.bitmap == bitmap) {

            } else {

                Bitmap old = this.bitmap;
                this.bitmap = bitmap;
                old.recycle();
                // LogManager.d("noImage" + this + old + "recycle");

            }

        }
        // LogManager.d("noImage" + this + "end setBitmap");
        invalidate();

    }

    public BankImageView(Context context) {
        super(context);
        paint.setAntiAlias(true);
    }

    Paint paint = new Paint();

    @Override
    protected synchronized void onDraw(Canvas canvas) {

        // LogManager.d("noImage" + this + "begin onDraw");
        super.onDraw(canvas);
        canvas.save();
        PaintFlagsDrawFilter filter = new PaintFlagsDrawFilter(0, Paint.FILTER_BITMAP_FLAG);
        canvas.setDrawFilter(filter);
        canvas.drawColor(Color.TRANSPARENT);
        if (!bitmap.isRecycled()) {
            canvas.drawBitmap(bitmap, 0, 0, paint);
        } else {
            // LogManager.d("noImage" + this + bitmap + "isRecycled");
        }
        canvas.restore();
        // LogManager.d("noImage" + this + "end onDraw");
    }
}
