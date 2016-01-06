package wu.batteryaidl.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2015/12/23.
 */
public class AdView extends LinearLayout {

    private IADLoadCallback mCallbacker;
    private int mTouchSlop;

    public AdView(Context context) {
        super(context);
    }

    public AdView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AdView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    public void setCallbacker(IADLoadCallback cbk) {
        mCallbacker = cbk;
    }

    public void loadAdData() {

        //TODO  加载广告并且调用回调进行显示

        if (mCallbacker != null) {
            mCallbacker.onAdLoadFinish(true);
        }
    }

    private float mLastTouchedY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastTouchedY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //如果是滑动，并且滑动过一段距离，则吃掉事件，防止Facebook广告触摸就会触发
                if (Math.abs(event.getY() - mLastTouchedY) > mLastTouchedY) {
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
//                Log.e("@@@", "......");
                break;
        }
        return true;
    }

    public static interface IADLoadCallback {
        public void onAdLoadFinish(boolean success);
    }

}
