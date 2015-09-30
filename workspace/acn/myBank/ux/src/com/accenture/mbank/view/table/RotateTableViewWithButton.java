
package com.accenture.mbank.view.table;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import android.view.View;

public class RotateTableViewWithButton extends RotatTableView {

    public BitmapDrawable getButtonImage() {
        return buttonImage;
    }

    public void setButtonImage(int res) {
        this.buttonImage = (BitmapDrawable)getContext().getResources().getDrawable(res);
        initSize();
    }

    BitmapDrawable buttonImage;

    BitmapDrawable buttonImageOver;

    double buttonHeight;

    double buttonWidth;

    public BitmapDrawable getButtonImageOver() {
        return buttonImageOver;
    }

    public void setButtonImageOver(int res) {

        buttonImageOver = (BitmapDrawable)getContext().getResources().getDrawable(res);
        initSize();
    }

    public RotateTableViewWithButton(Context context) {
        super(context);
    }

    @Override
    protected void initSize() {
        // TODO Auto-generated method stub
        super.initSize();
        if (buttonImageOver != null) {

            Bitmap bit = buttonImageOver.getBitmap();
            buttonImageOver.setBounds(0, 0, bit.getWidth(), bit.getHeight());
            double xxx = (double)bit.getWidth() / (double)imageWidth;
            buttonXScale = (1 - xxx) / 2;
            buttonHeight = xx * bit.getHeight();
            buttonWidth = xx * bit.getWidth();
        }
        if (buttonImage != null) {

            Bitmap bit = buttonImage.getBitmap();
            buttonImage.setBounds(0, 0, bit.getWidth(), bit.getHeight());
            double xxx = (double)bit.getWidth() / (double)imageWidth;
            buttonXScale = (1 - xxx) / 2;

            buttonHeight = xx * bit.getHeight();
            buttonWidth = xx * bit.getWidth();
        }

        buttonX = buttonXScale * maxwidth;
        buttonY = buttonYScale * maxHeight;
        buttonWidthLength = xx * buttonWidthLength;
        buttonHeightLength = xx * buttonHeightLength;
    }

    public double buttonXScale = 1d / 3d;

    public double buttonYScale = 2.5d / 4d;

    double buttonX;

    double buttonY;

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        canvas.save();
        canvas.translate((float)buttonX, (float)buttonY);
        canvas.scale((float)xx, (float)xx);

        if (isTouchButton) {
            if (buttonImageOver != null) {

                buttonImageOver.draw(canvas);
            } else if (buttonImage != null) {
                buttonImage.draw(canvas);
            }
        } else {
            if (buttonImage != null) {

                buttonImage.draw(canvas);
            }
        }
        canvas.restore();

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

                if (super.isSlidDown) {
                } else {
                    isTouchButton = false;
                    invalidate();
                    if (isTouchButton(touchX, touchY)) {

                        performButtonClick();
                        return true;
                    }
                }
                break;

            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    double buttonWidthLength = 20;

    double buttonHeightLength = 20;

    private boolean isTouchButton(float touchX, float touchY) {

        if (buttonImage == null && buttonImageOver == null) {

            return false;
        }
        double yStart = buttonY;
        // System.out.println(touchY);
        // System.out.println(yStart);
        yStart = yStart - buttonHeightLength;
        double yEnd = buttonY + buttonHeight + buttonHeightLength;

        double xStart = buttonX - buttonWidthLength;

        double xEnd = buttonX + buttonWidth + buttonWidthLength;

        if (touchY >= yStart && touchY <= yEnd) {

            if (touchX >= xStart && touchX <= xEnd) {
                return true;
            }
        }
        return false;
    }

    public void performButtonClick() {

        if (this.onClickListener != null) {
            onClickListener.onClick(this);
        }
    }

    OnButtonClickListener onClickListener;

    public void setOnButtonClickListener(OnButtonClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public static interface OnButtonClickListener {

        void onClick(View v);
    }
}
