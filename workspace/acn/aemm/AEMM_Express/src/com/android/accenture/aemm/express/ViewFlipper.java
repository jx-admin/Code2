package com.android.accenture.aemm.express;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class ViewFlipper  extends ViewAnimator {
    private static final String LOGCAT = "ViewFlipper";
    private static final boolean LOGD = false;

    static final int DEFAULT_INTERVAL = 3000;

    private int mFlipInterval = DEFAULT_INTERVAL;
    private int mNewFlipInterval=3000;
    private boolean mAutoStart = false;

    private boolean mRunning = false;
    private boolean mStarted = false;
    private boolean mVisible = false;
    private boolean mUserPresent = true;

    public ViewFlipper(Context context) {
        super(context);
    }

    public ViewFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);

//        TypedArray a = context.obtainStyledAttributes(attrs,
//                com.android.internal.R.styleable.ViewFlipper);
//        mFlipInterval = a.getInt(
//                com.android.internal.R.styleable.ViewFlipper_flipInterval, DEFAULT_INTERVAL);
//        mAutoStart = a.getBoolean(
//                com.android.internal.R.styleable.ViewFlipper_autoStart, false);
//        a.recycle();
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                mUserPresent = false;
                updateRunning();
            } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
                mUserPresent = true;
                updateRunning();
            }
        }
    };

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        // Listen for broadcasts related to user-presence
        final IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        Log.v(LOGCAT,"onAttachedToWindow to registerReceiver mReceiver");
        isRegister=true;
        getContext().registerReceiver(mReceiver, filter);

        if (mAutoStart) {
            // Automatically start when requested
            startFlipping();
        }
    }
    boolean isRegister=false;
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mVisible = false;
        Log.v(LOGCAT,"onDetachedFromWindow to unRegisterReceiver mReceiver");
        if(isRegister){
        	isRegister=false;
        	getContext().unregisterReceiver(mReceiver);
        }
        updateRunning();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        mVisible = visibility == VISIBLE;
        updateRunning();
    }

    /**
     * How long to wait before flipping to the next view
     *
     * @param milliseconds
     *            time in milliseconds
     */
//    @android.view.RemotableViewMethod
    public void setFlipInterval(int milliseconds) {
        mFlipInterval = milliseconds;
    }

    /**
     * Start a timer to cycle through child views
     */
    public void startFlipping() {
        mStarted = true;
        updateRunning();
    }

    /**
     * No more flips
     */
    public void stopFlipping() {
        mStarted = false;
        updateRunning();
    }
//    public void addView(View v){
//    	super.addView(v);
//    	 boolean running = mVisible && mStarted && mUserPresent;
//    	 if (running) {
//    		 int index=indexOfChild(v);
//    		 if (index >= 0) {
//	             showOnly(index);
//	             mHandler.removeMessages(FLIP_MSG);
//	             Message msg = mHandler.obtainMessage(FLIP_MSG);
//	             mHandler.sendMessageDelayed(msg,mNewFlipInterval);
//    		 }
//    	 }
//    	
//    }

    /**
     * Internal method to start or stop dispatching flip {@link Message} based
     * on {@link #mRunning} and {@link #mVisible} state.
     */
    private void updateRunning() {
        boolean running = mVisible && mStarted && mUserPresent;
        if (running != mRunning) {
            if (running) {
                showOnly(mWhichChild);
                Message msg = mHandler.obtainMessage(FLIP_MSG);
                mHandler.sendMessageDelayed(msg, mFlipInterval);
            } else {
                mHandler.removeMessages(FLIP_MSG);
            }
            mRunning = running;
        }
        if (LOGD) {
            Log.d(LOGCAT, "updateRunning() mVisible=" + mVisible + ", mStarted=" + mStarted
                    + ", mUserPresent=" + mUserPresent + ", mRunning=" + mRunning);
        }
    }

    /**
     * Returns true if the child views are flipping.
     */
    public boolean isFlipping() {
        return mStarted;
    }

    /**
     * Set if this view automatically calls {@link #startFlipping()} when it
     * becomes attached to a window.
     */
    public void setAutoStart(boolean autoStart) {
        mAutoStart = autoStart;
    }

    /**
     * Returns true if this view automatically calls {@link #startFlipping()}
     * when it becomes attached to a window.
     */
    public boolean isAutoStart() {
        return mAutoStart;
    }

    private final int FLIP_MSG = 1;

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == FLIP_MSG) {
                if (mRunning) {
                	MessageView mv=(MessageView) getCurrentView();
                	if(mv!=null&&mv.isOutTime()){
                		removeView(mv);
                	}
                	if(getChildCount()>1){
                    	showNext();
                	}
                    mv=(MessageView) getCurrentView();
                    int delay=DEFAULT_INTERVAL;
                    if(mv!=null){
                    	mv.nextTime();
                    	delay=mv.message.getDelayTime();
                    }
                    msg = obtainMessage(FLIP_MSG);
                    sendMessageDelayed(msg, delay);
                }
            }
        }
    };
    private void checkOlder(){
		MessageView mv;
    	for(int i=0;i<getChildCount();i++){
			mv=(MessageView) getChildAt(i);
			if(mv.message.getMaxTime()!=HallMessagedb.EVER&&mv.message.getStartTime()>=mv.message.getMaxTime()){
				removeViewAt(i);
			}
		}
    }
}
