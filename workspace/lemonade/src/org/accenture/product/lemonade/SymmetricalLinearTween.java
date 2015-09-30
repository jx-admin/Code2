 

package org.accenture.product.lemonade;

import android.os.Handler;
import android.os.SystemClock;

/**
 * Provides an animation between 0.0f and 1.0f over a given duration.
 */
class SymmetricalLinearTween {

    private static final int FPS = 30;
    private static final int FRAME_TIME = 1000 / FPS;

    Handler mHandler;
    int mDuration;
    TweenCallback mCallback;

    boolean mRunning;
    long mBase;
    boolean mDirection;
    float mValue;

    /**
     * @param duration milliseconds duration
     * @param callback callbacks
     */
    public SymmetricalLinearTween(boolean initial, int duration, TweenCallback callback) {
        mValue = initial ? 1.0f : 0.0f;
        mDirection = initial;
        mDuration = duration;
        mCallback = callback;
        mHandler = new Handler();
    }

    /**
     * Starts the tweening.
     *
     * @param direction If direction is true, the value goes towards 1.0f.  If direction
     *                  is false, the value goes towards 0.0f.
     */
    public void start(boolean direction) {
        start(direction, SystemClock.uptimeMillis());
    }

    /**
     * Starts the tweening.
     *
     * @param direction If direction is true, the value goes towards 1.0f.  If direction
     *                  is false, the value goes towards 0.0f.
     * @param baseTime  The time to use as zero for this animation, in the
     *                  {@link SystemClock.uptimeMillis} time base.  This allows you to
     *                  synchronize multiple animations.
     */
    public void start(boolean direction, long baseTime) {
        if (direction != mDirection) {
            if (!mRunning) {
                mBase = baseTime;
                mRunning = true;
                mCallback.onTweenStarted();
                long next = SystemClock.uptimeMillis() + FRAME_TIME;
                mHandler.postAtTime(mTick, next);
            } else {
                // reverse direction
                long now = SystemClock.uptimeMillis();
                long diff = now - mBase;
                mBase = now + diff - mDuration;
            }
            mDirection = direction;
        }
    }

    Runnable mTick = new Runnable() {
        public void run() {
            long base = mBase;
            long now = SystemClock.uptimeMillis();
            long diff = now-base;
            int duration = mDuration;
            float val = diff/(float)duration;
            if (!mDirection) {
                val = 1.0f - val;
            }
            if (val > 1.0f) {
                val = 1.0f;
            } else if (val < 0.0f) {
                val = 0.0f;
            }
            float old = mValue;
            mValue = val;
            mCallback.onTweenValueChanged(val, old);
            int frame = (int)(diff / FRAME_TIME);
            long next = base + ((frame+1)*FRAME_TIME);
            if (diff < duration) {
                mHandler.postAtTime(this, next);
            }
            if (diff >= duration) {
                mCallback.onTweenFinished();
                mRunning = false;
            }
        }
    };
}

