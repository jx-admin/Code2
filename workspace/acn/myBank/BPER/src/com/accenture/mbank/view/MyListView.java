
package com.accenture.mbank.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

public class MyListView extends ListView {

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private float lastTouchY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastTouchY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float del = event.getY() - lastTouchY;
                addTranslate((int) del);
                lastTouchY = event.getY();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                lastTouchY = 0;
                break;

            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    private void addTranslate(int y) {

        translateListener.onTranslate(y);
    }

    private TranslateListener translateListener;

    public static interface TranslateListener {
        public void onTranslate(int y);
    }

    public TranslateListener getTranslateListener() {
        return translateListener;
    }

    public void setTranslateListener(TranslateListener translateListener) {
        this.translateListener = translateListener;
    }
}
