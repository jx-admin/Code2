
package com.accenture.mbank.view.table;

import com.accenture.mbank.BaseActivity;
import com.accenture.mbank.util.Utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

public class CardsRotateTableView extends RotateTableViewWithButton {

    String totalWithDrawals = "$+1000";

    String avaliablity = "$+1000";

    public String getTotalWithDrawals() {
        return totalWithDrawals;
    }

    public void setTotalWithDrawals(String totalWithDrawals) {
        this.totalWithDrawals = totalWithDrawals;
    }

    public String getAvaliablity() {
        return avaliablity;
    }

    public void setAvaliablity(String avaliablity) {
        this.avaliablity = avaliablity;
    }

    public CardsRotateTableView(Context context) {
        super(context);
    }

    double line = 1 / 16d;

    double totalWithDrawalsTitleY = 2d / 7d + line;

    double totalWithDrawalsY = totalWithDrawalsTitleY + line;

    double avaliablityTitleY = totalWithDrawalsY + 1.5d * line;

    double avaliablityY = avaliablityTitleY + line;

    Paint paint = new Paint();

    private static final String totalWithDrawalsTitle = "Total Withdrawals";

    private static final String availabilityTitle = "Availability";

    public double textSize = 18d / 480;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setTypeface(Typeface.create("", Typeface.BOLD));
        paint.setTextSize((int)(textSize * BaseActivity.screen_width));
        paint.setAntiAlias(true);
        paint.setColor(Color.rgb(100, 100, 100));

        float x = Utils.measureX(paint, totalWithDrawalsTitle, maxwidth);
        canvas.drawText(totalWithDrawalsTitle, x, (float)(totalWithDrawalsTitleY * maxHeight),
                paint);

        x = Utils.measureX(paint, availabilityTitle, maxwidth);
        canvas.drawText(availabilityTitle, x, (float)(avaliablityTitleY * maxHeight), paint);

        paint.setColor(Color.BLACK);
        x = Utils.measureX(paint, totalWithDrawals, maxwidth);
        
        float avaliablityX = getStringWidth(paint, avaliablity);
        canvas.drawText(avaliablity, (float)(maxwidth-avaliablityX)/2, (float)(avaliablityY * maxHeight), paint);
        float totalWithDrawalsX = getStringWidth(paint, totalWithDrawals);
        canvas.drawText(totalWithDrawals, (float)(maxwidth-totalWithDrawalsX)/2, (float)(totalWithDrawalsY * maxHeight), paint);

    }

    public int getStringWidth(Paint mPaint, String str) {
        return (int)mPaint.measureText(str);
    }
    
}
