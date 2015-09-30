
package com.accenture.mbank.view.table;

import it.gruppobper.ams.android.bper.R;

import com.accenture.mbank.BaseActivity;
import com.accenture.mbank.util.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;

public class AccountsRotateTableView extends RotateTableViewWithButton {

    public String AVAILABLE_BALANCE_TITLE = "Available Balance";

    public String ACCOUNT_BALANCE_TITLE = "Account Balance";

    public String DEPOSIT_TITLE = "Deposit";

    public String WITHDRAWALS_TITLE = "Withdrawals";

    private String availableBalanceValue = "$+1000";

    private String accountBalanceValue = "$+1000";

    private String depositValue = "$+1000";

    private String widthdrawalsValue = "$+1000";

    public static final double line = 1d / 15d;

    public static final double accountY  = 1d / 5d + line;

    public static final double availableY  = accountY + 2 * line;

    public static final double depositY = availableY + 2 * line;

    private BitmapDrawable placeholderLineDrawable;

    private double placeholderLineDrawableWidth = 0;

    public String getAvailableBalanceValue() {
        return availableBalanceValue;
    }

    public void setAvailableBalanceValue(String availableBalanceValue) {
        this.availableBalanceValue = availableBalanceValue;
    }

    public String getAccountBalanceValue() {
        return accountBalanceValue;
    }

    public void setAccountBalanceValue(String accountBalanceValue) {
        this.accountBalanceValue = accountBalanceValue;
    }

    public String getDepositValue() {
        return depositValue;
    }

    public void setDepositValue(String depositValue) {
        this.depositValue = depositValue;
    }

    public String getWidthdrawalsValue() {
        return widthdrawalsValue;
    }

    public void setWidthdrawalsValue(String widthdrawalsValue) {
        this.widthdrawalsValue = widthdrawalsValue;
    }

	public AccountsRotateTableView(Context context) {
		super(context);

		AVAILABLE_BALANCE_TITLE = context.getResources().getString(
				R.string.available_account);
		AVAILABLE_BALANCE_TITLE = Utils.toUppString(AVAILABLE_BALANCE_TITLE);

		ACCOUNT_BALANCE_TITLE = context.getResources().getString(
				R.string.balance_account);
		ACCOUNT_BALANCE_TITLE = Utils.toUppString(ACCOUNT_BALANCE_TITLE);
		
		DEPOSIT_TITLE = context.getResources().getString(R.string.deposits);
		WITHDRAWALS_TITLE = context.getResources().getString(
				R.string.withdrawals);
	}

    Paint paint = new Paint();

    @Override
    protected void initSize() {
        super.initSize();

        if (placeholderLineDrawable == null) {
            placeholderLineDrawable = (BitmapDrawable)getResources().getDrawable(R.drawable.placeholder_line);
        }
        Bitmap bitmap = placeholderLineDrawable.getBitmap();

        placeholderLineDrawable.setBounds(new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()));
        placeholderLineDrawableWidth = bitmap.getWidth() * 5;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setTypeface(Typeface.create("", Typeface.BOLD));

        paint.setTextSize((int)(textSize * BaseActivity.screen_width));
        paint.setAntiAlias(true);
        paint.setColor(Color.rgb(100, 100, 100));

        float x = Utils.measureX(paint, AVAILABLE_BALANCE_TITLE, maxwidth);
        canvas.drawText(AVAILABLE_BALANCE_TITLE, x, (float)(availableY * maxHeight), paint);

        x = Utils.measureX(paint, ACCOUNT_BALANCE_TITLE, maxwidth);
        canvas.drawText(ACCOUNT_BALANCE_TITLE, x, (float)((accountY) * maxHeight), paint);

        paint.setColor(Color.BLACK);
        x = Utils.measureX(paint, getAvailableBalanceValue(), maxwidth);
        canvas.drawText(getAvailableBalanceValue(), x, (float)((availableY + line) * maxHeight),
                paint);

        x = Utils.measureX(paint, getAccountBalanceValue(), maxwidth);
        canvas.drawText(getAccountBalanceValue(), x, (float)((accountY + line) * maxHeight), paint);

        paint.setColor(Color.rgb(100, 100, 100));
        x = (float)(maxwidth / 2 - paint.measureText(DEPOSIT_TITLE) - placeholderLineDrawableWidth);
        canvas.drawText(DEPOSIT_TITLE, x, (float)((depositY) * maxHeight), paint);

        x = (float)(maxwidth / 2 + placeholderLineDrawableWidth);
        canvas.drawText(WITHDRAWALS_TITLE, x, (float)((depositY) * maxHeight), paint);
        paint.setColor(Color.BLACK);

        x = (float)(maxwidth / 2 - paint.measureText(getDepositValue()) - placeholderLineDrawableWidth);
        canvas.drawText(getDepositValue(), x, (float)((depositY + line) * maxHeight), paint);

        x = (float)(maxwidth / 2 + placeholderLineDrawableWidth);

        canvas.drawText(getWidthdrawalsValue(), x, (float)((depositY + line) * maxHeight), paint);

        if (placeholderLineDrawable != null) {
            canvas.save();

            canvas.translate((float)(o_x), (float)((depositY) * maxHeight) - paint.getTextSize());
            placeholderLineDrawable.draw(canvas);
            canvas.restore();
        }

    }

}
