
package com.accenture.mbank.view.table;

import it.gruppobper.ams.android.bper.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import android.view.View;

import com.accenture.mbank.BaseActivity;
import com.accenture.mbank.model.GetDepositInfoResponseModel;
import com.accenture.mbank.util.Utils;

public class InvestRotatTableView extends View {

    private BitmapDrawable bgBitmapDrawable;

    private BitmapDrawable btnDetailsDrawable;

    private BitmapDrawable btnDetailsOverDrawable;

    private Bitmap bg;

    private Bitmap btnDetails;

    private double maxwidth;

    private double maxHeight;

    private String totalTitle = "";

    private String totalAmount = "";

    private GetDepositInfoResponseModel depositInfo;

    private BitmapDrawable placeholderLineDrawable;
    private double placeholderLineDrawableWidth = 0;

	/**
     * 各项所在圆球的百分比位置
     */
    public static final double line = 1d / 15d;

    public double totalTitleY = 2 / 7d;

    public double totalAmountY = totalTitleY + line;

    public double sharsTitleY = totalTitleY + 2.3 * line;
    
    public double bondsTitleY = sharsTitleY;
    
    public double fundsTitleY = sharsTitleY + 2* line;
    
    public double otherSecuritiesTitleY = fundsTitleY;

    public double detailsX = 1 / 3d;

    public double detailsY = fundsTitleY + 1.5 * line;
    
    private double xx;

    private int width;

    private int height;

    public double textSize = 18d / 480;

    public static final double widthXX = 600d / 720d;

    // button的宽高
    public double detailsWidth;

    public double detailsHeight;

    public String SHARES_TITLE;
    public String BONDS_TITLE;
    public String FUNDS_TITLE;
    public String OTHER_SECURITIES_TITLE;

    public InvestRotatTableView(Context context) {
        super(context);
        initSize();
        
        String names[] =context.getResources().getStringArray(R.array.investment_rote_names);// new String[] { "shares", "bonds", "funds", "more" };
        SHARES_TITLE= names[0];
        BONDS_TITLE = names[1];
        FUNDS_TITLE = names[2];
        OTHER_SECURITIES_TITLE = names[3];
    }

    /**
     * 设置
     * 
     * @param slider
     * @param bg
     */
    public void setRotatResource(int bg, int details, int detailsOver) {
        bgBitmapDrawable = (BitmapDrawable)getContext().getResources().getDrawable(bg);
        btnDetailsDrawable = (BitmapDrawable)getContext().getResources().getDrawable(details);
        btnDetailsOverDrawable = (BitmapDrawable)getContext().getResources().getDrawable(detailsOver);
        this.bg = bgBitmapDrawable.getBitmap();
        this.btnDetails = btnDetailsDrawable.getBitmap();
        initSize();
    }

    protected void initSize() {
        if (bg == null) {
            return;
        }
        width = bg.getWidth();
        height = bg.getHeight();

        maxwidth = BaseActivity.screen_width * widthXX;
        xx = maxwidth / width;

        maxHeight = height * xx;

        bgBitmapDrawable.setBounds(new Rect(0, 0, (int)bg.getWidth(), (int)bg.getHeight()));
        btnDetailsDrawable.setBounds(new Rect(0, 0, (int)btnDetails.getWidth(), (int)btnDetails
                .getHeight()));
        btnDetailsOverDrawable.setBounds(new Rect(0, 0, (int)btnDetails.getWidth(), (int)btnDetails
                .getHeight()));

        detailsWidth = btnDetails.getWidth();
        detailsHeight = btnDetails.getHeight();

        detailsX = detailsX * xx * width;
        detailsY = detailsY * xx * height;

        totalAmountY = totalAmountY * xx * height;
        totalTitleY = totalTitleY * xx * height;
        
        
        if (placeholderLineDrawable == null) {
            placeholderLineDrawable = (BitmapDrawable)getResources().getDrawable(R.drawable.placeholder_line);
        }
        Bitmap bitmap = placeholderLineDrawable.getBitmap();

        placeholderLineDrawable.setBounds(new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()));
        placeholderLineDrawableWidth = bitmap.getWidth() * 5;
    }

    Paint paint = new Paint();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.scale((float)xx, (float)xx);
        bgBitmapDrawable.draw(canvas);
        canvas.restore();

        paint.setTypeface(Typeface.create("", Typeface.BOLD));
        paint.setTextSize((int)(textSize * BaseActivity.screen_width));
        paint.setAntiAlias(true);
        paint.setColor(Color.rgb(100, 100, 100));

        drawTitles(canvas);
        
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setTextSize((int)(textSize * BaseActivity.screen_width));
        paint.setColor(Color.BLACK);

        drawValues(canvas);

        canvas.save();
        canvas.translate((float)detailsX, (float)detailsY);
        canvas.scale((float)xx, (float)xx);
        if (isTouchButton && btnDetailsOverDrawable != null) {
            btnDetailsOverDrawable.draw(canvas);
        } else {
            btnDetailsDrawable.draw(canvas);
        }
        canvas.restore();
    }

    private void drawTitles(Canvas canvas) {
        /*
         * Draw titles
         */
        float totalTitleX = getStringWidth(paint, totalTitle);
        canvas.drawText(totalTitle, (float)(maxwidth - (float)totalTitleX) / 2, (float)totalTitleY,
                paint);

        if (depositInfo == null)
        	return;

        float x;
        x = (float)(maxwidth / 2 - paint.measureText(SHARES_TITLE) - placeholderLineDrawableWidth);
        canvas.drawText(SHARES_TITLE, x, (float)((sharsTitleY) * maxHeight), paint);
        
        x = (float)(maxwidth / 2 - paint.measureText(FUNDS_TITLE) - placeholderLineDrawableWidth);
        canvas.drawText(FUNDS_TITLE, x, (float)((fundsTitleY) * maxHeight), paint);

        x = (float)(maxwidth / 2 + placeholderLineDrawableWidth);
        canvas.drawText(BONDS_TITLE, x, (float)((bondsTitleY) * maxHeight), paint);
        
        x = (float)(maxwidth / 2 + placeholderLineDrawableWidth);
        canvas.drawText(OTHER_SECURITIES_TITLE, x, (float)((otherSecuritiesTitleY) * maxHeight), paint);

        if (placeholderLineDrawable != null) {
            canvas.save();

            canvas.translate((float)(maxwidth/2), (float)((sharsTitleY) * maxHeight) - paint.getTextSize());
            placeholderLineDrawable.draw(canvas);
            canvas.restore();
            
            canvas.save();

            canvas.translate((float)(maxwidth/2), (float)((fundsTitleY) * maxHeight) - paint.getTextSize());
            placeholderLineDrawable.draw(canvas);
            canvas.restore();
        }
        
    }
    
    private void drawValues(Canvas canvas) {
        /*
         * Draw value
         */
        if (depositInfo == null)
        	totalAmountY = 1 / 2d * xx * height;

        float totalAmountX = getStringWidth(paint, totalAmount);
        canvas.drawText(totalAmount, (float)(maxwidth - (float)totalAmountX) / 2,
                (float)totalAmountY, paint);
        
        if (depositInfo == null)
        	return;

        float x;
        String sharesValue = Utils.formatPercentDouble(depositInfo.getShares().getPercentage());
        x = (float)(maxwidth / 2 - paint.measureText(sharesValue) - placeholderLineDrawableWidth);
        canvas.drawText(sharesValue, x, (float)((sharsTitleY + line) * maxHeight), paint);
        
        String fundsValue = Utils.formatPercentDouble(depositInfo.getFunds().getPercentage());
        x = (float)(maxwidth / 2 - paint.measureText(fundsValue) - placeholderLineDrawableWidth);
        canvas.drawText(fundsValue, x, (float)((fundsTitleY + line) * maxHeight), paint);

        String bondsValue = Utils.formatPercentDouble(depositInfo.getBonds().getPercentage());
        x = (float)(maxwidth / 2 + placeholderLineDrawableWidth);
        canvas.drawText(bondsValue, x, (float)((bondsTitleY + line) * maxHeight), paint);
        
        String otherSecutitiesValue = Utils.formatPercentDouble(depositInfo.getOtherSecurities().getPercentage());
        x = (float)(maxwidth / 2 + placeholderLineDrawableWidth);
        canvas.drawText(otherSecutitiesValue, x, (float)((otherSecuritiesTitleY + line) * maxHeight), paint);
    }
    
    boolean isTouchButton = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float touchX = event.getX();
        float touchY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isTouchButton(touchX, touchY)) {
                    isTouchButton = true;
                    invalidate();
                    return true;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                isTouchButton = false;
                invalidate();
                if (isTouchButton(touchX, touchY)) {
                    performButtonClick();
                    return true;
                }
                break;

            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    private boolean isTouchButton(float touchX, float touchY) {
        double yStart = detailsY * maxHeight;
        double yEnd = yStart + width;
        double xStart = detailsX;
        double xEnd = detailsX + detailsWidth;
        if ((touchY > detailsY && touchY < yEnd) && (touchX >= xStart && touchX <= xEnd)) {
            return true;
        }
        return false;
    }

    public void performButtonClick() {
        if (this.onClickListener != null) {
            onClickListener.onClick(this);
        }
    }

    OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public static interface OnClickListener {

        void onClick(View v);
    }

    public int getStringWidth(Paint mPaint, String str) {
        return (int)mPaint.measureText(str);
    }

    public int getFontHeight(Paint mPaint, float fontSize) {
        FontMetrics fm = mPaint.getFontMetrics();
        paint.setTextSize(fontSize);
        return (int)Math.ceil(fm.descent - fm.top) + 2;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 它的宽高不是图片的宽高，而是以宽高为直角的矩形的对角线的长度
        setMeasuredDimension((int)maxwidth, (int)maxwidth);
    }

    /**
     * @param totalAmountTitle the totalAmountTitle to set
     */
    public void setTotalAmountTitle(int totalAmountTitle) {
        this.totalTitle = getResources().getString(totalAmountTitle);
    }

    public void setTotalAmountTitle(String totalAmountTitle) {
        this.totalTitle = totalAmountTitle;
    }
    
    /**
     * @param totalAmount the totalAmount to set
     */
    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public GetDepositInfoResponseModel getDepositInfo() {
		return depositInfo;
	}

	public void setDepositInfo(GetDepositInfoResponseModel depositInfo) {
		this.depositInfo = depositInfo;
	}
}
