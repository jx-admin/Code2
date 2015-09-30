
package com.act.mbanking.manager.view;

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

    public void setBitmap(Bitmap bitmap) {
        Bitmap old = this.bitmap;
        this.bitmap = bitmap;

        invalidate();
        if (old != null && old != this.bitmap) {
            old.recycle();
        }
    }

    public BankImageView(Context context) {
        super(context);
        paint.setAntiAlias(true);
    }

    Paint paint = new Paint();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        PaintFlagsDrawFilter filter = new PaintFlagsDrawFilter(0, Paint.FILTER_BITMAP_FLAG);
        canvas.setDrawFilter(filter);
        canvas.drawColor(Color.TRANSPARENT);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        canvas.restore();
    }
}
