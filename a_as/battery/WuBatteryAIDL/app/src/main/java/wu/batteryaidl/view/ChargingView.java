package wu.batteryaidl.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;


import wu.battery.model.Battery;
import wu.batteryaidl.R;
import wu.batteryaidl.help.Utils;
import wu.batteryaidl.model.SmsAndMissedCallHelper;
import wu.utils.LogUtils;


/**
 * Created by Jiangjia on 15-12-23.
 */
public class ChargingView extends FrameLayout implements AdView.IADLoadCallback, SmsAndMissedCallHelper.IMissedCallAndSmsCallback, View.OnClickListener {
    private static final int DRAG_UNLOCK = 0x00;
    private static final int DRAG_AD = 0x01;
    private static final int FLING_DURATION = 1000;
    private static int AD_CONTENT_HEIGHT;
    private static final float CHARGE_MODEL_ALPHA_FULL = 1f;
    private static final float CHARGE_MODEL_ALPHA_HALF = 0.5f;
    private static final float CHARGE_MODEL_ALPHA_BOTTOM = 0.3f;
    private View currentChargeModelView;
    private boolean mADLoadSuccess;

    private int chargingModel;
    private AdView mAdView;
    private View mAdContainerView;
    private View mBottomCover;
    private TextView mPercent;
    private View mStep;
    private ViewGroup mUnLockLayer;
    private BubblesView mBubblesView;
    private TextView mTipMessage;
    private WaveView waveView;
    private View mTipLayer;
    private LinearLayout mMessageLayer;
    private View chargingSetting;
    private View settingMenu;
    private View menuItemSetting;

    private int mTouchSlop;
    private int mStartScrollY = 0;
    private IDelegate mDelegator;

    private int mDragState;
    private DigitalClock mTimeLabel;
    private int mTouchDistanceX;
    private int mTouchDistanceY;
    private float mADPercent, mUnlockPercent;
    private VelocityTracker mVelocityTracker;
    private Scroller mScroller;
    private GestureDetector mGesture;
    private boolean mDragVertical;
    private int mLastTouchedX, mLastTouchedY;
    private int mADStartScrollY, mADEndScrollY;
    private ValueAnimator chargingModelVAnimator;
    private boolean isRepeate;
    private SmsAndMissedCallHelper mObserver;

    public ChargingView(Context context) {
        this(context, null, 0);
    }

    public ChargingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChargingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        AD_CONTENT_HEIGHT = Utils.dp2px(getContext(), 320);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mScroller = new Scroller(getContext());
        mVelocityTracker = VelocityTracker.obtain();
        mGesture = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onDown(MotionEvent e) {
                mLastTouchedX = (int) e.getX();
                mLastTouchedY = (int) e.getY();
                mDragState = -1;
                if (mScroller != null) {
                    if (!mScroller.isFinished()) {
                        mScroller.abortAnimation();
                    }
                }
                mTouchDistanceX = (int) (e.getX() - mUnLockLayer.getLeft());
                mTouchDistanceY = (int) (e.getY() - mAdView.getTop());
                mStartScrollY = mAdView.getScrollY();
                return super.onDown(e);
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                float aby = Math.abs(distanceY), abx = Math.abs(distanceX);
                onPositionChanged();
                mDragVertical = aby >= abx;
                if (mDragState == -1) {
                    mDragState = mDragVertical ? (mADLoadSuccess ? DRAG_AD : -1) : DRAG_UNLOCK;
                }
                do {
                    if (mDragState == DRAG_AD) {
                        int y = -(int) (e2.getY() - mTouchDistanceY) + mStartScrollY;
                        y = Math.min(mADEndScrollY, Math.max(y, mADStartScrollY));
                        mAdView.scrollTo(0, y);
                        break;
                    }
                    if (mDragState == DRAG_UNLOCK) {
                        if (e2.getX() - mLastTouchedX > mTouchSlop) {
                            mUnLockLayer.setTranslationX(Math.max(0, (int) e2.getX() - mTouchDistanceX - mUnLockLayer.getLeft()));
                        }
                    }

                } while (false);
                return super.onScroll(e1, e2, distanceX, distanceY);
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                mVelocityTracker.computeCurrentVelocity(1000);
                if (mDragState == DRAG_AD) {
                    int target = velocityY > 0 ? mADStartScrollY : mADEndScrollY;
                    mScroller.startScroll(0, mAdView.getScrollY(), 0, (target - mAdView.getScrollY()), FLING_DURATION);
                } else {
                    if (velocityX > 0) {
                        int target = velocityX > 0 ? getWidth() : 0;
                        mScroller.startScroll((int) mUnLockLayer.getTranslationX(), 0, (int) (target - mUnLockLayer.getTranslationX()), 0, FLING_DURATION);
                    }
                }
                invalidate();
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        mVelocityTracker.addMovement(event);
        mGesture.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mDragState == DRAG_AD) {
                    if (mScroller.isFinished()) {
                        int target = event.getY() > getHeight() / 2 ? mADEndScrollY : mADStartScrollY;
                        mScroller.startScroll(0, mAdView.getScrollY(), 0, (target - mAdView.getScrollY()), FLING_DURATION);
                    }
                } else if (mDragState == DRAG_UNLOCK) {
                    if (mScroller.isFinished()) {
                        mScroller.startScroll((int) mUnLockLayer.getTranslationX(), 0, -(int) mUnLockLayer.getTranslationX(), 0, FLING_DURATION);
                    }
                }
                invalidate();
                break;
        }
        return super.onInterceptTouchEvent(event);
    }

    public void setDelegate(IDelegate d) {
        mDelegator = d;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        waveView = (WaveView) findViewById(R.id.wave_view);
        waveView.setWaveColor(0xFF00AAFF, 0xFF3BBFFF);
        mAdView = (AdView) findViewById(R.id.ad);
        mMessageLayer = (LinearLayout) findViewById(R.id.message_layer);
        mTipLayer = findViewById(R.id.tip_layer);
        mAdView.setCallbacker(this);
        mStep = findViewById(R.id.step_view);
        mAdContainerView = findViewById(R.id.ad_container);
        mUnLockLayer = (ViewGroup) findViewById(R.id.unlock_layer);
        mPercent = (TextView) findViewById(R.id.tip_percent);
        mBubblesView = (BubblesView) findViewById(R.id.bubbles);
        mTimeLabel = (DigitalClock) findViewById(R.id.time_label);
        mBottomCover = findViewById(R.id.unlock_label);
        mTipMessage = (TextView) findViewById(R.id.tip_message);
        chargingSetting = findViewById(R.id.charging_setting_layout);
        settingMenu = findViewById(R.id.setting_menu);
        menuItemSetting = findViewById(R.id.menu_item_setting);

        Typeface face = Typeface.create("sans-serif-light", Typeface.NORMAL);
        if (face != null) {
            mPercent.setTypeface(face);
            mTimeLabel.setTypeface(face);
            mTipMessage.setTypeface(face);
        }
        mADLoadSuccess = false;
        mBottomCover.setVisibility(INVISIBLE);
        chargingSetting.setOnClickListener(this);
        settingMenu.setOnClickListener(this);
        menuItemSetting.setOnClickListener(this);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mADStartScrollY == 0 && mADEndScrollY == 0) {
            mADStartScrollY = -(int) (getHeight() - mBottomCover.getHeight() - AD_CONTENT_HEIGHT / 3f);
            mADEndScrollY = -(getHeight() - mBottomCover.getHeight() - AD_CONTENT_HEIGHT);
            resetAll();
        }
    }

    private void resetAll() {
        mAdView.scrollTo(0, mADStartScrollY);
    }

    public void setPercent(int percent) {
        String str = percent + "%";
        SpannableString s = new SpannableString(str);
        s.setSpan(new RelativeSizeSpan(0.4f), str.length() - 1, str.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        s.setSpan(new StyleSpan(Typeface.ITALIC), str.length() - 1, str.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        mPercent.setText(s);
    }


    protected void onProgress(float percent) {
        int pw = mPercent.getWidth(), ph = mPercent.getHeight(), cx = mPercent.getLeft() + pw / 2, cy = mPercent.getTop() + ph / 2;
        mBottomCover.getBackground().setAlpha((int) (percent * 255));
        float pp = Math.max(.5f, percent);
        mPercent.setScaleX(pp);
        mPercent.setScaleY(pp);
        int mTopOffset = mTimeLabel.getTop();
        int targetW = (int) (pw * .5f), targetX = targetW / 2 + mAdView.getPaddingLeft();
        int targetH = (int) (ph * .5f), targetY = mTopOffset + targetH / 2 + mAdView.getPaddingTop();
        mPercent.setTranslationX((percent - 1) * (cx - targetX));
        mPercent.setTranslationY((percent - 1) * (cy - targetY));

        mStep.setScaleX(pp);
        mStep.setScaleY(pp);
        mStep.setTranslationY((percent - 1) * cy);
        mStep.setAlpha(percent);

        pw = mTipMessage.getWidth();
        ph = mTipMessage.getHeight();
        cx = mTipMessage.getLeft() + mTipMessage.getWidth() / 2;
        cy = mTipMessage.getTop() + mTipMessage.getHeight();

        mTipMessage.setTranslationX((percent - 1) * (cx - mAdView.getPaddingLeft() - 30 - targetW - pw / 2));
        mTipMessage.setTranslationY((percent - 1) * (cy - targetY - targetH / 2 - mAdView.getPaddingTop()));


//        double alpha = Math.max(0, 1 - Math.sin(Math.PI * percent));
//        mTipMessage.setAlpha((float) (alpha));
    }

    private void onPositionChanged() {
        mUnlockPercent = mUnLockLayer.getTranslationX() / getWidth();
        mADPercent = 1 - Math.abs((float) (mAdView.getScrollY() - mADStartScrollY) / (mADEndScrollY - mADStartScrollY));
        if (mDragState == DRAG_AD) {
            onProgress(mADPercent);
        } else if (mDragState == DRAG_UNLOCK) {
            if (getBackground() != null) {
                getBackground().setAlpha((int) (255 * (1 - mUnlockPercent)));
            }
            if (mUnlockPercent == 1 && mDelegator != null) {
                mDelegator.onClose(this);
            }
        }
    }

    @Override
    public void computeScroll() {
        onPositionChanged();
        if (mScroller.computeScrollOffset()) {
            if (mDragState == DRAG_UNLOCK) {
                mUnLockLayer.setTranslationX(mScroller.getCurrX());
            } else if (mDragState == DRAG_AD) {
                mAdView.scrollTo(0, mScroller.getCurrY());
            }
            invalidate();
        }
    }

    @Override
    public void onAdLoadFinish(boolean success) {
        mADLoadSuccess = success;
        mAdContainerView.setVisibility(success ? VISIBLE : INVISIBLE);
        mBottomCover.setVisibility(success ? VISIBLE : INVISIBLE);
    }

    public void onStop() {
        waveView.stop();
        mTimeLabel.stop();
        stopChargingModelAmimator();
    }

    public void onStart() {
        mTimeLabel.start();
        waveView.start();
        if (currentChargeModelView != null) {
            startChargingModelAmimator();
        }
    }

    public void setModel(Battery battery) {
        if (chargingModel == battery.chargingModel) {
            return;
        }
        chargingModel = battery.chargingModel;
        switch (battery.chargingModel) {
            case Battery.CHARGING_MODEL_DONE:
                stopChargingModelAmimator();
                findViewById(R.id.step1).setAlpha(CHARGE_MODEL_ALPHA_FULL);
                findViewById(R.id.model_divider_1).setAlpha(CHARGE_MODEL_ALPHA_FULL);
                findViewById(R.id.step2).setAlpha(CHARGE_MODEL_ALPHA_FULL);
                findViewById(R.id.model_divider_2).setAlpha(CHARGE_MODEL_ALPHA_FULL);
                findViewById(R.id.step3).setAlpha(CHARGE_MODEL_ALPHA_FULL);
                currentChargeModelView = null;
                break;
            case Battery.CHARGING_MODEL_SLOW:
                findViewById(R.id.step1).setAlpha(CHARGE_MODEL_ALPHA_FULL);
                findViewById(R.id.model_divider_1).setAlpha(CHARGE_MODEL_ALPHA_FULL);
                findViewById(R.id.step2).setAlpha(CHARGE_MODEL_ALPHA_FULL);
                findViewById(R.id.model_divider_2).setAlpha(CHARGE_MODEL_ALPHA_FULL);
                findViewById(R.id.step3).setAlpha(CHARGE_MODEL_ALPHA_FULL);
                currentChargeModelView = findViewById(R.id.step3);
                currentChargeModelView.setAlpha(CHARGE_MODEL_ALPHA_HALF);
                break;
            case Battery.CHARGING_MODEL_NOMAL:
                findViewById(R.id.step1).setAlpha(CHARGE_MODEL_ALPHA_FULL);
                findViewById(R.id.model_divider_1).setAlpha(CHARGE_MODEL_ALPHA_FULL);
                currentChargeModelView = findViewById(R.id.step2);
                currentChargeModelView.setAlpha(CHARGE_MODEL_ALPHA_HALF);
                findViewById(R.id.model_divider_2).setAlpha(CHARGE_MODEL_ALPHA_BOTTOM);
                findViewById(R.id.step3).setAlpha(CHARGE_MODEL_ALPHA_BOTTOM);
                break;
            case Battery.CHARGING_MODEL_QUICK:
                currentChargeModelView = findViewById(R.id.step1);
                currentChargeModelView.setAlpha(CHARGE_MODEL_ALPHA_HALF);
                findViewById(R.id.model_divider_1).setAlpha(CHARGE_MODEL_ALPHA_BOTTOM);
                findViewById(R.id.step2).setAlpha(CHARGE_MODEL_ALPHA_BOTTOM);
                findViewById(R.id.model_divider_2).setAlpha(CHARGE_MODEL_ALPHA_BOTTOM);
                findViewById(R.id.step3).setAlpha(CHARGE_MODEL_ALPHA_BOTTOM);
                break;
        }
        if (currentChargeModelView != null) {
            startChargingModelAmimator();
        }
    }

    private void startChargingModelAmimator() {
        isRepeate = true;
        if (chargingModelVAnimator == null) {
            chargingModelVAnimator = ValueAnimator.ofFloat(0, 1);
            chargingModelVAnimator.setDuration(1000);
            chargingModelVAnimator.setInterpolator(new DecelerateInterpolator(1.2f));
            chargingModelVAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float v = (float) animation.getAnimatedValue();
                    if (currentChargeModelView != null) {
                        currentChargeModelView.setAlpha((CHARGE_MODEL_ALPHA_FULL - CHARGE_MODEL_ALPHA_HALF) * v + CHARGE_MODEL_ALPHA_BOTTOM);
                    }
                }
            });
            chargingModelVAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if (isRepeate) {
                        chargingModelVAnimator.setStartDelay(3000);
                        chargingModelVAnimator.start();
                    }
                }
            });
            chargingModelVAnimator.setRepeatCount(ValueAnimator.INFINITE);
            chargingModelVAnimator.setRepeatMode(ValueAnimator.REVERSE);
        }
        if (!chargingModelVAnimator.isStarted()) {
            chargingModelVAnimator.start();
        }
    }

    private void stopChargingModelAmimator() {
        isRepeate = false;
        if (chargingModelVAnimator != null) {
            chargingModelVAnimator.cancel();
        }
    }

    public void onBatteryChanged(final Battery battery) {
        post(new Runnable() {
            @Override
            public void run() {
                float level = (float) battery.level / battery.scale;
                setPercent(battery.level);
                waveView.setLevel(level, false);
                LogUtils.d("ChargingView model " + battery.chargingModel);
                setModel(battery);

                int mViewHeight = mBubblesView.getHeight();
                int minLevel = (int) (mViewHeight * 0.08f);
                int maxLevel = mViewHeight - minLevel;
                int levelHeight = (int) ((maxLevel - minLevel) * level);

                mBubblesView.setTranslationY(mBubblesView.getHeight() - minLevel - levelHeight);
                if (battery.isCharging()) {
                    switch (battery.chargingModel) {
                        case Battery.CHARGING_MODEL_DONE:
                            mTipMessage.setText(R.string.charge_finish);
                            break;
                        case Battery.CHARGING_MODEL_SLOW:
                        case Battery.CHARGING_MODEL_NOMAL:
                        case Battery.CHARGING_MODEL_QUICK:
//                            String strDes=getContext().getString(R.string.remaining);
                            mTipMessage.setText(R.string.remaining);
                            String time = parseTime(battery.remainTime);
                            SpannableString spanString = new SpannableString(time);
                            ForegroundColorSpan span = new ForegroundColorSpan(0xFF96FF23);
                            spanString.setSpan(span, 0, time.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            mTipMessage.append(" ");
                            mTipMessage.append(spanString);
                            break;
                    }
                } else {
                    mTipMessage.setText("");
                }
            }
        });
    }

    private String parseTime(long time) {
        long h = (time / 60);
        long m = time % 60;
        if (h > 0) {
            return h + getContext().getString(R.string.avai_time_hour) + m + getContext().getString(R.string.avai_time_minute);
        } else {
            return m + getContext().getString(R.string.avai_time_minute);
        }
    }

    public void onPowerConnected() {
        LogUtils.d("onPowerConnected in CharingView");
        post(new Runnable() {
            @Override
            public void run() {
                mBubblesView.setVisibility(VISIBLE);
            }
        });
    }

    public void onPowerDisConnected() {
        LogUtils.d("onPowerDisConnected in CharingView");
        post(new Runnable() {
            @Override
            public void run() {
                mBubblesView.setVisibility(INVISIBLE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_menu:
                settingMenu.setVisibility(View.GONE);
                break;
            case R.id.charging_setting_layout:
                settingMenu.setVisibility(View.VISIBLE);
                break;
            case R.id.menu_item_setting:
                settingMenu.setVisibility(View.GONE);
                // TODO 打开设置页面
                break;
        }
    }

    public static interface IDelegate {
        public void onClose(View view);
    }

    @Override
    public void onChange(int smsCount, int callCount) {
        Log.e("@@@", "sms: " + smsCount + " callcount: " + callCount);
        Utils.unlockAndWakeLockScreen(getContext(), 5000);
        mMessageLayer.setVisibility(VISIBLE);
        mMessageLayer.removeAllViews();
        if (smsCount > 0) {
            showMsg(Utils.getMSMIntent(), smsCount);
        }
        if (callCount > 0) {
            showMsg(Utils.getCallIntent(), callCount);
        }
    }

    private void showMsg(Intent intent, int num) {
        TileView tileView = new TileView(getContext());
        tileView.render(intent);
        tileView.setCount(num);
        int padding = Utils.dp2px(getContext(), 10);
        tileView.setPadding(padding, padding, padding, padding);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(5 * padding, 5 * padding);
        params.setMargins(padding >> 1, padding >> 1, padding >> 1, padding >> 1);
        mMessageLayer.addView(tileView, params);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mObserver = new SmsAndMissedCallHelper(getContext(), new Handler(), this);
        mObserver.registerObservers();
        postDelayed(new Runnable() {
            @Override
            public void run() {
                onChange(2, 3);
                onAdLoadFinish(true);
            }
        }, 3000);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mObserver != null) {
            mObserver.unregisterObservers();
        }
    }
}

