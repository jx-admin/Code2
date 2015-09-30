
package com.accenture.mbank.view.table;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;

import com.accenture.mbank.BaseActivity;
import com.accenture.mbank.R;
import com.accenture.mbank.util.Utils;

public class LoansRotateTableView extends RotateTableViewWithButton {

    public static final String INSTALLMENT_TO = "Installment to:";

    private String installToDate = "01.15.13";

    public String getInstallToDate() {
        return installToDate;
    }

    public void setInstallToDate(String installToDate) {
        this.installToDate = installToDate;
    }

    private String installToValue = "$+590,70";

    public static final String RESIDUAL_CAPITAL = "Residual Capital";

    private String residualCapitalValue = "$+450,84.86";

    public static final String NEXT_INSTALLMENT = "Next Installment";

    private String next_InstallmentValue = "$+450,84.86";

    public static final String DEADLINE = "DeadLine";

    private String deadLine = "01.15.13";

    private BitmapDrawable placeholderLineDrawable;

    private double placeholderLineDrawableWidth = 0;

    private BitmapDrawable isPaidDrawable;

    private double isPaidDrawableWidth = 0;

    private BitmapDrawable notPaidDrawable;

    public static final double line = 1d / 20d;

    public static final double beginY = 1d / 4d + line;

    public static final double beginX = 1d / 4d;

    private boolean isPaid = true;

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }

    public String getInstallToValue() {
        return installToValue;
    }

    public void setInstallToValue(String installToValue) {
        this.installToValue = installToValue;
    }

    public String getResidualCapitalValue() {
        return residualCapitalValue;
    }

    public void setResidualCapitalValue(String residualCapitalValue) {
        this.residualCapitalValue = residualCapitalValue;
    }

    public String getNext_InstallmentValue() {
        return next_InstallmentValue;
    }

    public String getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(String deadLine) {
        this.deadLine = deadLine;
    }

    public void setNext_InstallmentValue(String next_InstallmentValue) {
        this.next_InstallmentValue = next_InstallmentValue;
    }

    public LoansRotateTableView(Context context) {
        super(context);
    }

    @Override
    protected void initSize() {
        super.initSize();

        if (placeholderLineDrawable == null) {
            placeholderLineDrawable = (BitmapDrawable)getResources().getDrawable(
                    R.drawable.placeholder_line);
        }
        Bitmap bitmap = placeholderLineDrawable.getBitmap();

        placeholderLineDrawable.setBounds(new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()));
        placeholderLineDrawableWidth = bitmap.getWidth() * 3;

        if (isPaidDrawable == null) {
            isPaidDrawable = (BitmapDrawable)getResources().getDrawable(R.drawable.check_paid);
            isPaidDrawableWidth = isPaidDrawable.getBitmap().getWidth();
            Bitmap bottom = isPaidDrawable.getBitmap();

            isPaidDrawable.setBounds(new Rect(0, 0, bottom.getWidth(), bottom.getHeight()));
        }
        if (notPaidDrawable == null) {
            notPaidDrawable = (BitmapDrawable)getResources().getDrawable(R.drawable.check_not_paid);

            isPaidDrawableWidth = notPaidDrawable.getBitmap().getWidth();
            Bitmap bottom = notPaidDrawable.getBitmap();

            notPaidDrawable.setBounds(new Rect(0, 0, bottom.getWidth(), bottom.getHeight()));

        }

        double ispaidWidth = isPaidWidth * BaseActivity.screen_width;
        xxx = (float)(ispaidWidth / isPaidDrawable.getBitmap().getWidth());

    }

    float xxx;

    Paint paint = new Paint();

    public double textSize = 18d / 480d;

    public double bigTextSize = 36d / 480d;

    public double minTextSize = 15d / 480d;

    public double isPaidWidth = 36d / 480d;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int textHeight = (int)(textSize * BaseActivity.screen_width);
        int bigTextHeight = (int)(bigTextSize * BaseActivity.screen_width);
        int minTextHeight = (int)(minTextSize * BaseActivity.screen_width);
        int absline = (int)(this.line * maxHeight);
        paint.setTypeface(Typeface.create("", Typeface.BOLD));

        paint.setTextSize(minTextHeight);
        paint.setAntiAlias(true);
        // paint.setColor(Color.rgb(100, 100, 100));

        String text = INSTALLMENT_TO + installToDate;
        float x = Utils.measureX(paint, text, maxwidth);

        float y = (float)(beginY * maxHeight);

        canvas.drawText(text, x, y, paint);

        y = (float)(y + textHeight + absline);
        text = getInstallToValue();

        paint.setTextSize((int)(bigTextSize * BaseActivity.screen_width));

        float length = paint.measureText(text);

        x = (float)(beginX * maxwidth);
        canvas.drawText(text, x, y, paint);
        if (isPaid) {
            if (isPaidDrawable != null) {
                canvas.save();
                canvas.translate(x + length, (float)(y - bigTextHeight));
                canvas.scale(xxx, xxx);
                isPaidDrawable.draw(canvas);
                canvas.restore();
            }

        } else {
            if (notPaidDrawable != null) {
                canvas.save();
                canvas.translate(x + length, (float)(y - bigTextHeight));

                notPaidDrawable.draw(canvas);
                canvas.restore();
            }
        }

        y = y + 2 * absline;
        paint.setTextSize((int)(textSize * BaseActivity.screen_width));

        paint.setTextSize(minTextHeight);
        text = RESIDUAL_CAPITAL;
        x = (float)(o_x - paint.measureText(text) - placeholderLineDrawableWidth);
        canvas.drawText(text, x, y, paint);
        text = NEXT_INSTALLMENT;
        x = (float)(o_x + placeholderLineDrawableWidth);
        canvas.drawText(text, x, y, paint);
        if (placeholderLineDrawable != null) {
            canvas.save();
            // canvas.scale((float)xx, (float)xx);
            canvas.translate((float)(o_x), y - absline);
            placeholderLineDrawable.draw(canvas);
            canvas.restore();
        }
        paint.setTextSize(textHeight);
        y = (float)(y + absline);
        text = getResidualCapitalValue();
        x = (float)(o_x - paint.measureText(text) - placeholderLineDrawableWidth);
        canvas.drawText(text, x, y, paint);

        text = getNext_InstallmentValue();
        x = (float)(o_x + placeholderLineDrawableWidth);
        canvas.drawText(text, x, y, paint);

        paint.setTextSize(minTextHeight);
        y = y + absline;
        text = DEADLINE + " " + getDeadLine();
        x = Utils.measureX(paint, text, maxwidth);
        canvas.drawText(text, x, y, paint);

    }
}
