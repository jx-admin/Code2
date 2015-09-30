package com.accenture.mbank.view.table;

import it.gruppobper.ams.android.bper.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.accenture.mbank.BaseActivity;
import com.accenture.mbank.util.Utils;

public class CorporateCardsRotateTableView extends RotateTableViewWithButton {

	String availableBalance = "";
	String plafond = "";
	String expirationDate = "";

	String AVAILABLE_BALANCE_TITLE = "";
	String PLATFOND_TITLE = "";
	String EXPIRATION_DATE_TITLE = "";

	double line = 1 / 15d;

	double availableBalanceTitleY = 2d / 7d;

	double plafondTitleY = availableBalanceTitleY + 2.5d * line;

	double expirationDateTitleY = plafondTitleY + 2d * line;

	public CorporateCardsRotateTableView(Context context) {
		super(context);

		AVAILABLE_BALANCE_TITLE = context.getResources().getString(
				R.string.available_account);
		AVAILABLE_BALANCE_TITLE = Utils.toUppString(AVAILABLE_BALANCE_TITLE);

		PLATFOND_TITLE = context.getResources()
				.getString(R.string.plafond_card);
		EXPIRATION_DATE_TITLE = context.getResources().getString(
				R.string.expiration_1);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		paint.setTypeface(Typeface.create("", Typeface.BOLD));
		paint.setTextSize((int) (textSize * BaseActivity.screen_width));
		paint.setAntiAlias(true);
		paint.setColor(Color.rgb(100, 100, 100));

		float x = Utils.measureX(paint, AVAILABLE_BALANCE_TITLE, maxwidth);
		canvas.drawText(AVAILABLE_BALANCE_TITLE, x,
				(float) (availableBalanceTitleY * maxHeight), paint);

		x = Utils.measureX(paint, PLATFOND_TITLE, maxwidth);
		canvas.drawText(PLATFOND_TITLE, x, (float) (plafondTitleY * maxHeight),
				paint);

		paint.setColor(Color.BLACK);

		float availableBalanceX = getStringWidth(paint, availableBalance);
		canvas.drawText(availableBalance,
				(float) (maxwidth - (float) availableBalanceX) / 2,
				(float) ((availableBalanceTitleY + line) * maxHeight), paint);

		float plafondX = getStringWidth(paint, plafond);
		canvas.drawText(plafond, (float) (maxwidth - (float) plafondX) / 2,
				(float) ((plafondTitleY + line) * maxHeight), paint);
	}

	public int getStringWidth(Paint mPaint, String str) {
		return (int) mPaint.measureText(str);
	}

	public String getAvailable() {
		return availableBalance;
	}

	public void setAvailableBalance(String availableBalance) {
		this.availableBalance = availableBalance;
	}

	public String getPlafond() {
		return plafond;
	}

	public void setPlafond(String plafond) {
		this.plafond = plafond;
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

}
