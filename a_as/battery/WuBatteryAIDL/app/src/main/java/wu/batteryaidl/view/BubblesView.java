package wu.batteryaidl.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;

/**
 * Created by lj on 15-12-22.
 */
public class BubblesView extends View implements ValueAnimator.AnimatorUpdateListener {
    static String TAG = "BubblesView";
    Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    ValueAnimator mAnimator;
    Random mRandom = new Random();
    int mWidth;
    int mHeight;
    State[] mStates;

    int KEY_LEFT = 1;
    int KEY_RIGHT = 2;

    public BubblesView(Context context) {
        super(context);
        init();
    }

    public BubblesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BubblesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setWillNotDraw(false);
        mPaint.setStrokeWidth(5);
        mPaint.setColor(Color.WHITE);
        //mPaint.setAntiAlias(true);

        mAnimator = ValueAnimator.ofInt(0, 100);
        mAnimator.addUpdateListener(this);
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.setRepeatMode(ValueAnimator.RESTART);
        mAnimator.setDuration(200000);//随便定义，大点好了

        mStates = new State[10];
        mAnimator.start();

    }

    public void start() {
        if (!mAnimator.isRunning()) {
            mAnimator.start();
        }
    }

    public void stop() {
        if (mAnimator.isRunning()) {
            mAnimator.cancel();
        }
        for (int i = 0; i < mStates.length; i++) {
            if (mStates[i] == null) continue;
            createState(mStates[i]);
        }
        invalidate();
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        //Log.d(TAG, "onAnimationUpdate"  + (int)animation.getAnimatedValue());
        for (int i = 0; i < mStates.length; i++) {
            if (mStates[i] == null) continue;
            updateState(mStates[i]);
        }
        postInvalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (mWidth == 0) {
            mWidth = getWidth();
            mHeight = getHeight();
            for (int i = 0; i < mStates.length; i++) {
                mStates[i] = new State();
                createState(mStates[i]);
                mStates[i].countDown += (i * 100);
            }
        }
        canvas.drawColor(Color.TRANSPARENT);
        for (int i = 0; i < mStates.length; i++) {
            mPaint.setAlpha(mStates[i].alpha);
            canvas.drawCircle((int) mStates[i].currentX, (int) mStates[i].currentY, mStates[i].currentSize, mPaint);
        }
        super.onDraw(canvas);
    }

    public class State {
        public int currentSize; //当前半径
        public int maxSize; //最大半径
        public float verticalSpeed; //垂直速度
        public float horizontalSpeed; //水平速度
        public int direction; //水平移动方向
        public int alpha; //透明度
        public float currentX; //当前X
        public float currentY; //当前Y
        public int countDown;//倒计时
    }

    private void createState(State state) {
        state.alpha = (int) (random(5, 9) * 0.1f * 255);
        state.maxSize = mWidth / random(8, 16);
        state.direction = random(2);
        state.horizontalSpeed = random(mWidth / 160, mWidth / 40) / 10f;
        state.verticalSpeed = random(mHeight / 50, mHeight / 30) / 10f;
        state.currentSize = 0;
        state.currentX = mWidth / 2;
        state.currentY = mHeight;
        state.countDown = random(50, 100);
    }

    private void updateState(State state) {

        //倒计时
        if (state.countDown > 0) {
            state.countDown--;
            return;
        }

        //大小改变
        if (state.currentSize < state.maxSize) {
            state.currentSize++;
        }

        //垂直坐标改变
        state.currentY -= state.verticalSpeed;
        if (state.currentY - state.currentSize < 0) {
            //到头了
            createState(state);
            return;
        }

        //水平方向坐标改变
        if (state.direction == KEY_LEFT) {
            if (state.currentX - state.currentSize <= 0) {
                state.direction = KEY_RIGHT;
            } else {
                state.currentX -= state.horizontalSpeed;
            }
        } else {
            if (state.currentX + state.currentSize >= mWidth) {
                state.direction = KEY_LEFT;
            } else {
                state.currentX += state.horizontalSpeed;
            }
        }

    }

    //返回1-n
    private int random(int n) {
        return mRandom.nextInt(n) + 1;
    }

    private int random(int n, int max) {
        return n + mRandom.nextInt(max - n);
    }


}