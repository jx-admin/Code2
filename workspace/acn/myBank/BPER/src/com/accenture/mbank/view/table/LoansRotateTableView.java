
package com.accenture.mbank.view.table;

import it.gruppobper.ams.android.bper.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;

import com.accenture.mbank.BaseActivity;
import com.accenture.mbank.util.Utils;

public class LoansRotateTableView extends RotateTableViewWithButton {

    public String INSTALLMENT_TO = "Installment to:";

    private String installToDate = "01.15.13";

    public String getInstallToDate() {
        return installToDate;
    }

    public void setInstallToDate(String installToDate) {
        this.installToDate = installToDate;
    }

    private String installToValue = "$+590,70";

    public String RESIDUAL_CAPITAL = "Residual Capital";

    private String residualCapitalValue = "$+450,84.86";

    public String NEXT_INSTALLMENT = "Next Installment";

    private String next_InstallmentValue = "$+450,84.86";

    public String DEADLINE = "DeadLine";

    private String deadLine = "01.15.13";

    private BitmapDrawable placeholderLineDrawable;

    private double placeholderLineDrawableWidth = 0;

    private BitmapDrawable isPaidDrawable;

    private BitmapDrawable notPaidDrawable;

    double line = 1.2d / 20d;

    double beginY = 1d / 4d + line;

    double beginX = 1d / 4d;

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

        INSTALLMENT_TO = context.getResources().getString(R.string.installment_to);
        RESIDUAL_CAPITAL = context.getResources().getString(R.string.residual_capital2);
        NEXT_INSTALLMENT = context.getResources().getString(R.string.next_installment);
        DEADLINE = context.getResources().getString(R.string.deadline);
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
        placeholderLineDrawableWidth = bitmap.getWidth() * 2;

        if (isPaidDrawable == null) {
            isPaidDrawable = (BitmapDrawable)getResources().getDrawable(R.drawable.check_paid);
            Bitmap bottom = isPaidDrawable.getBitmap();
            isPaidDrawable.setBounds(new Rect(0, 0, bottom.getWidth(), bottom.getHeight()));
        }
        if (notPaidDrawable == null) {
            notPaidDrawable = (BitmapDrawable)getResources().getDrawable(R.drawable.check_not_paid);
            Bitmap bottom = notPaidDrawable.getBitmap();
            notPaidDrawable.setBounds(new Rect(0, 0, bottom.getWidth(), bottom.getHeight()));
        }

    }

    Paint paint = new Paint();
    public double minTextSize = 15d / 480d;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int textHeight = (int)(textSize * BaseActivity.screen_width);
        int minTextHeight = (int)(minTextSize * BaseActivity.screen_width);

        int absline = (int)(this.line * maxHeight);
        paint.setTypeface(Typeface.create("", Typeface.BOLD));

        paint.setColor(Color.rgb(100, 100, 100));
        paint.setTextSize(textHeight);
        paint.setAntiAlias(true);
 
        String text = INSTALLMENT_TO + " " + installToDate;
        float x = o_x - paint.measureText(text)/2;
        float begin_y = (float)(beginY * maxHeight);
        float y = begin_y;
        canvas.drawText(text, x, y, paint);
        paint.setColor(Color.BLACK);

        /*
         * install amount
         */
        y = (float)(y + absline * 1.1f);
        text = getInstallToValue();
        paint.setTextSize(textHeight);

        float length = paint.measureText(text);
        x = (float)o_x - paint.measureText(text)/2;

		if (isPaid) {
			paint.setColor(getResources().getColor(R.color.green));
		} else {
			paint.setColor(getResources().getColor(R.color.red));
		}
        if (text.equals(getResources().getString(R.string.not_able))) {
            paint.setColor(Color.BLACK);
        }
        canvas.drawText(text, x, y , paint);
        paint.setColor(Color.BLACK);

        /*
         * install state
         */
		if (!text.equals(getResources().getString(R.string.not_able))) {
			if (isPaid) {
				if (isPaidDrawable != null) {
					canvas.save();
					canvas.translate(x + length, (float) (begin_y));
					isPaidDrawable.draw(canvas);
					canvas.restore();
				}

			} else {
				if (notPaidDrawable != null) {
					canvas.save();
					canvas.translate(x + length, (float) (begin_y));
					notPaidDrawable.draw(canvas);
					canvas.restore();
				}
			}
		}
		
        y = y + 1.3f * absline;
        paint.setColor(Color.rgb(100, 100, 100));
        paint.setTextSize(minTextHeight);

        text = RESIDUAL_CAPITAL;
        x = (float)(o_x - paint.measureText(text) - placeholderLineDrawableWidth);
        canvas.drawText(text, x, y, paint);
        text = NEXT_INSTALLMENT;
        x = (float)(o_x + placeholderLineDrawableWidth);
        canvas.drawText(text, x, y, paint);
        if (placeholderLineDrawable != null) {
            canvas.save();
            canvas.translate((float)(o_x), y - textHeight);
            placeholderLineDrawable.draw(canvas);
            canvas.restore();
        }

        paint.setTextSize(textHeight);
        paint.setColor(Color.BLACK);

        y = (float)(y + absline);
        text = getResidualCapitalValue();
        x = (float)(o_x - paint.measureText(text) - placeholderLineDrawableWidth);
        canvas.drawText(text, x, y, paint);

        text = getNext_InstallmentValue();
        x = (float)(o_x + placeholderLineDrawableWidth);
        canvas.drawText(text, x, y, paint);

        y = y + absline;
        text = DEADLINE + " " + getDeadLine();
        x = Utils.measureX(paint, text, maxwidth);
        canvas.drawText(text, x, y, paint);

    }
}
