package wu.batteryaidl.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Region;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import wu.batteryaidl.BuildConfig;

/**
 * 水流波动控件
 *
 * @author jx
 */
public class WaveView extends View {
    private OnStatusChanaged mOnStatusChanaged;

    public void setOnStatusChanaged(OnStatusChanaged mOnStatusChanaged) {
        this.mOnStatusChanaged = mOnStatusChanaged;
    }

    public static interface OnStatusChanaged {
        void onStatusChanaged();
    }

    private static boolean DEBUG = BuildConfig.DEBUG;
    private static final String TAG = "WaveView";

    public Wave wave;
    /**
     * 水位初始化
     */
    public static final byte INIT = -1;
    /**
     * 水位监听
     */
    public static final byte NORMAL = 0;
    /**
     * 清理
     */
    public static final byte CLEAR_TO = 1;
    /**
     * 水位更新
     */
    public static final byte CLEAR_BACK = 2;
    /**
     * 当前状态
     */
    private int status = INIT;

    //view info
    private int width, height;

    private boolean isCircle = false;

    /**
     * wave 前
     */
    private WaveSprite waveSprite1;
    /**
     * wave 后
     */
    private WaveSprite waveSprite2;
    private Paint mWavePaint;
    /**
     * wave前状态颜色值：绿黄红
     */
    private int wave1Color = 0xFF00AAFF;
//    private int[] wave1Color = new int[]{0xE6CA3830, 0xE6FF9F1F, 0xE63CB034};
    /**
     * wave后状态颜色值：绿黄红
     */
//    private int[] wave2Color = new int[]{0xffEF6650, 0xFFFFD126, 0xFF80DF70};
//    private int[] waveLevel = new int[]{0, 10, 30};
//    private int colorIndex;
    private int wave2Color = 0xFF3BBFFF;

    /**
     * 水速
     */
    private float speed;

    /**
     * 当前水位
     */
    private float level;
    /**
     * 目标水位
     */
    private float destLeve;
    /**
     * 最小水位
     */
    private int minLevel;
    /**
     * 最大水位
     */
    private int maxLevel;
    /**
     * 水位变化速度
     */
    private float levelSpeed = 1f;
    /**
     * 是否正在清理内部标志
     */
    private boolean isCleaning = false;

    //Circle's bk & edge
    private Path circlePath;
    private Paint circlePain;
    private float circleBorlderWidth;
    private int circleColr = 0xffffffff;
    private int backColor = 0x1A000000;

    //clip Circle
    private Path clipPath;
    private Path emptyPath = new Path();

    //TextView 百分比
    private TextView textView;
    private TextView textView2;
    int progressFormatId;

    //animation executor
    private Timer timer;
    private MyTimerTask mTask;
    /**
     * 计时器
     */
    private int pos;
    /**
     * 每帧时间
     */
    private static int FRAME = 10;
    private Handler updateHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            invalidate();
        }

    };
//    private Runnable mRunable=new Runnable() {
//        @Override
//        public void run() {
//            logic();
//            updateHandler.post(mRunable);
//        }
//    };

    public WaveView(Context context) {
        super(context);
        init();
    }

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WaveView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
//        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        circleBorlderWidth = getResources().getDisplayMetrics().density * 2;
        circlePain = new Paint();
        circlePain.setAntiAlias(true);
        circlePain.setColor(Color.WHITE);
        circlePain.setStrokeWidth(circleBorlderWidth);
        circlePain.setStyle(Style.STROKE);

        clipPath = new Path();

        circlePath = new Path();

        waveSprite1 = new WaveSprite();
        waveSprite2 = new WaveSprite();
        waveSprite2.setDirect(-1);
        waveSprite2.setSpeed(1.2f);
        waveSprite1.color = wave1Color;
        waveSprite2.color = wave2Color;
        setStatus(NORMAL);

        mWavePaint = new Paint();
        mWavePaint.setAntiAlias(true);
        mWavePaint.setStyle(Style.FILL);
        mWavePaint.setColor(Color.BLUE);

        timer = new Timer();
//        setLayerType(LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        if (DEBUG)
            Log.d(TAG, "onWindowFocusChanged " + hasWindowFocus);
        super.onWindowFocusChanged(hasWindowFocus);
//        if (hasWindowFocus) {
//            start();
//        } else {
//            stop();
//        }
    }

    public void setTextView(TextView textView, TextView textView2, int progressFormatId) {
        this.textView = textView;
        this.textView2 = textView2;
        this.progressFormatId = progressFormatId;
    }

    public void reset() {
        level = 0;
        setStatus(INIT);
    }

    /**
     * 动画开始
     */
    public void start() {
        if (DEBUG)
            Log.d(TAG, "start() ");
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
        mTask = new MyTimerTask(updateHandler);
        timer.schedule(mTask, 0, FRAME);

//        updateHandler.post(mRunable);
    }

    public void stop() {
        if (DEBUG)
            Log.d(TAG, "stop() ");
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
//        updateHandler.removeCallbacks(mRunable);
    }

    /**
     * 内存模拟，百分比制
     */
    private void mockMemory() {
        if (mOnStatusChanaged != null) {
            mOnStatusChanaged.onStatusChanaged();
            return;
        }
//        long totalMemory = 100;
//        long usedMemory = (long) (Math.random() * totalMemory);
//        long totalMemory = DeviceInfoUtils.getTotalMemory();
//        long usedMemory = totalMemory - DeviceInfoUtils.getAvailMemory(getContext());
//        setMemory((float) usedMemory / totalMemory);
    }

    private float destPersent;

    public void setLevel(float levelPersent, boolean anim) {
        this.destPersent = levelPersent;
        long levelLength = maxLevel - minLevel;
        destLeve = levelPersent * levelLength;
        if (!anim) {
            level = destLeve;
        }
        if (Math.abs(destLeve - level) >= levelSpeed) {
            setStatus(NORMAL);
        }
        if (DEBUG) {
            Log.d(TAG, "mock memory used=" + levelPersent + " destLeve=" + destLeve + " levelLength = " + levelLength);
        }
    }

    /**
     * 设置状态
     */
    public void setStatus(int status) {
        this.status = status;
        switch (status) {
            case INIT:
                isCleaning = false;
                waveSprite1.setSpeed(speed * 3);
                waveSprite2.setSpeed(speed * 3);
                levelSpeed = speed * 2;
                break;
            case NORMAL:
                isCleaning = false;
                waveSprite1.setSpeed(speed);
                waveSprite2.setSpeed(speed);
                levelSpeed = speed;
                break;
            case CLEAR_TO:
                waveSprite1.setSpeed(speed * 3);
                waveSprite2.setSpeed(speed * 3);
                levelSpeed = speed;
                break;
            case CLEAR_BACK:
                float backDestLevel = (float) (0.7 + Math.random() * 0.049);
                if (DEBUG) {
                    Log.d(TAG, "clean back destLevel = " + backDestLevel);
                }
                destLeve = Math.min(destLeve, (maxLevel - minLevel) * (backDestLevel));
                waveSprite1.setSpeed(speed * 3);
                waveSprite2.setSpeed(speed * 3);
                levelSpeed = speed;
                break;
        }
        if (DEBUG)
            Log.d(TAG, "setStatus =" + status);
    }

    public void setCleaning(boolean cleaning) {
        this.isCleaning = cleaning;
    }

    private void logic() {
//        Log.d(TAG, "logic() " + status + " level " + level + " destLeve " + destLeve);
        if (wave == null) {
            return;
        }
        switch (status) {
            case INIT:
                if (level == destLeve) {
                    setStatus(NORMAL);
                }
            case NORMAL:
                if (level == destLeve) {
                    if (pos % 1000 == 0) {
                        mockMemory();
                    }
                } else if (level + levelSpeed < destLeve) {
                    level += levelSpeed;
                    updateColor();
                } else if (level - levelSpeed > destLeve) {
                    level -= levelSpeed;
                    updateColor();
                } else {
                    level = destLeve;
                    updateColor();
                }
                break;
            case CLEAR_TO:
                if (level > minLevel) {
                    level -= levelSpeed;
                    updateColor();
                } else if (level != minLevel) {
                    level = minLevel;
                    updateColor();
                } else if (!isCleaning) {
                    destLeve = (float) Math.min(destLeve, (maxLevel - minLevel) * 0.58);
                    setStatus(CLEAR_BACK);
                }
                break;
            case CLEAR_BACK:
                if (level + levelSpeed <= destLeve) {
                    level += levelSpeed;
                    updateColor();
                } else if (level - levelSpeed >= destLeve) {
                    level -= levelSpeed;
                    updateColor();
                } else if (level == destLeve) {
                    setStatus(NORMAL);
                } else {
                    level = destLeve;
                }
                break;
        }
        waveSprite1.logic();
        waveSprite2.logic();
        pos++;
    }

    /**
     * 根据水位更新颜色值
     */
    private void updateColor() {
//        int stage = (int) (100 * level / (maxLevel - minLevel));
//        if (textView != null) {
//            textView.setText(getContext().getString(progressFormatId, stage));
//        }
//        if (textView2 != null && (status == INIT || status == NORMAL)) {
//            textView2.setText(getContext().getString(progressFormatId, 100 - stage));
//        }
//        if (stage > waveLevel[2]) {
//            colorIndex = 2;
//        } else if (stage >= waveLevel[1]) {
//            colorIndex = 1;
//        } else {
//            colorIndex = 0;
//        }
//        waveSprite1.color = wave1Color[colorIndex];
//        waveSprite2.color = wave2Color[colorIndex];
    }

    public void setWaveColor(int colorBk, int colorFr) {
        if (waveSprite1 != null) {
            waveSprite1.color = colorBk;
        }
        if (waveSprite2 != null) {
            waveSprite2.color = colorFr;
        }
        this.wave1Color = colorBk;
        this.wave2Color = colorFr;
    }

//    public void setWaveHeight(int waveHeight){
//        if(this.mWaveHeight!=waveHeight){
//            this.mWaveHeight=waveHeight;
//            if(wave!=null){
//                wave.setWaveHeight(waveHeight);
//            }
//        }
//    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (DEBUG)
            Log.d(TAG, "onMeasure " + getMeasuredWidth() + " " + getMeasuredHeight());

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (DEBUG)
            Log.d(TAG, "onSizeChanged " + w + " " + w);
    }

    private int x0, y0;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            layout(isCircle, getMeasuredWidth(), getMeasuredHeight());
        }
        if (DEBUG)
            Log.d(TAG, "onLayout " + changed + " " + (right - left) + " " + (bottom - top));
    }

    public void setStyle(boolean isCircle) {
        layout(isCircle, width, height);
    }

    private void layout(boolean isCircle, int w, int mViewHeight) {
        if (isCircle == this.isCircle && w == width && mViewHeight == height) {
            return;
        }
        this.isCircle = isCircle;
        width = w;
        height = mViewHeight;
        if (isCircle) {
            w = Math.min(w, mViewHeight);
            mViewHeight = w;
            x0 = (width - w) >> 1;
            y0 = (height - mViewHeight) >> 1;
        } else {
            x0 = 0;
            y0 = 0;
        }
        minLevel = (int) (mViewHeight * 0.08f);
        maxLevel = mViewHeight - minLevel;
        speed = w / 30f / FRAME;
        setStatus(status);
        if (wave == null) {
            wave = new Wave();
            wave.setWaveHeight((int) (getResources().getDisplayMetrics().density * 10));
        }
        wave.onMeasure(w, mViewHeight);
        waveSprite1.setWave(wave);
        waveSprite2.setWave(wave);
        clipPath.addCircle(w >> 1, mViewHeight >> 1, (Math.min(w, mViewHeight) - circleBorlderWidth) / 2, Path.Direction.CCW);
        circlePath.addCircle(w >> 1, mViewHeight >> 1, (Math.min(w, mViewHeight) - circleBorlderWidth) / 2, Path.Direction.CW);
        setLevel(destPersent, false);
        if (DEBUG)
            Log.d(TAG, "layout " + " maxLevel=" + maxLevel + " minLevel=" + minLevel + " speed=" + speed);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(x0, y0);
        if (isCircle) {
            canvas.save(Canvas.CLIP_SAVE_FLAG);
            canvas.clipPath(emptyPath);
            canvas.clipPath(clipPath, Region.Op.REPLACE);

            //draw circle bk
            circlePain.setColor(backColor);
            circlePain.setStyle(Style.FILL);
            canvas.drawPath(circlePath, circlePain);
        }
        //draw wave
        canvas.save();
        canvas.translate(0, maxLevel - level /*- minLevel*/);
        waveSprite2.onDraw(canvas, mWavePaint);
        waveSprite1.onDraw(canvas, mWavePaint);


        canvas.restore();
        if (isCircle) {
            canvas.restore();
            //draw circle
            circlePain.setColor(circleColr);
            circlePain.setStyle(Style.STROKE);
            canvas.drawPath(circlePath, circlePain);
        }
        canvas.restore();
    }

    class MyTimerTask extends TimerTask {
        Handler handler;

        public MyTimerTask(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void run() {
            logic();
            handler.sendEmptyMessage(0);
        }

    }

    static final class Point {
        public float x;
        public float y;

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return super.toString() + ": " + x + "," + y;
        }
    }

    /**
     * 波浪Path图形
     */
    static final class Wave {
        /**
         * 可视区域
         */
        private int width, height;
        /**
         * 水位
         */
        private float mLevelLine;
        /**
         * 波浪起伏幅度
         */
        private float mWaveHeight = 80;
        /**
         * 波长
         */
        private float mWaveWidth = 200;
        /**
         * 左边隐藏波形
         */
        private float mLeftSide;
        private int direct = 1;

        private List<Point> mPointsList;
        private Path mWavePath;

        public Wave() {
            init();
        }

        public void init() {
            mPointsList = new ArrayList<Point>();
            mWavePath = new Path();
        }

        public void setWaveHeight(int waveHeight) {
            if (this.mWaveHeight != waveHeight) {
                this.mWaveHeight = waveHeight;
            }
        }

        /**
         * 所有点的x坐标都还原到初始状态，也就是一个周期前的状态
         */
        private void resetPoints() {
            mLeftSide = -mWaveWidth;
            int size = mPointsList.size();
            //一个波浪4个path点，每两个点的宽度
            float pointWidth = mWaveWidth / 4;
            for (int i = 0; i < size; i++) {
                mPointsList.get(i).setX(i * pointWidth - mWaveWidth);
            }
        }

        /**
         * move all the points
         *
         * @param dx
         */
        private void moveWave(float dx) {
            Point point;
            for (int i = 0, size = mPointsList.size(); i < size; i++) {
                point = mPointsList.get(i);
                point.setX(point.getX() + dx);
                switch (i % 4) {
                    case 0:
                    case 2:
                        point.setY(mLevelLine);
                        break;
                    case 1:
                        point.setY(mLevelLine + mWaveHeight);
                        break;
                    case 3:
                        point.setY(mLevelLine - mWaveHeight);
                        break;
                }
            }
        }

        /**
         * update path by all the points
         */
        private void updatePath(int height) {
            mWavePath.reset();
            int size = mPointsList.size();
            if (size > 2) {
                Point p = mPointsList.get(0);
                Point p1, p2 = null;
                mWavePath.moveTo(p.x, p.y);
                for (int i = 1; i < mPointsList.size() - 1; i = i + 2) {
                    p1 = mPointsList.get(i);
                    p2 = mPointsList.get(i + 1);
                    mWavePath.quadTo(p1.x, p1.y, p2.x, p2.y);
                }
                mWavePath.lineTo(p2.x, height);
                mWavePath.lineTo(p.x, height);
                mWavePath.close();
            }
        }

        public void onMeasure(int mViewWidth, int mViewHeight) {
            this.width = mViewWidth;
            this.height = mViewHeight;
            // 水位线从最底下开始上升
            mLevelLine = 0;
            // 波长等于四倍View宽度也就是View中只能看到四分之一个波形，这样可以使起伏更明显
            mWaveWidth = width;
            // 根据View宽度计算波形峰值
//            mWaveHeight = mWaveWidth / 14;
            // 这里计算在可见的View宽度中能容纳几个波形，注意n上取整
            mLeftSide = -mWaveWidth;
            // ��������ڿɼ���View����������ɼ������Σ�ע��n��ȡ��
            int n = Math.round(width / mWaveWidth);
            // n个波形需要4n+1个点，但是我们要预留一个波形在左边隐藏区域，所以需要4n+5个点
            int pointCount = 4 * n + 5;
            float pointDivid = mWaveWidth / 4;
            mPointsList.clear();
            for (int i = 0; i < pointCount; i++) {
                // 从P0开始初始化到P4n+4，总共4n+5个点
                float x = i * pointDivid /*- mWaveWidth*/;
                float y = 0;
                switch (i % 4) {
                    case 0:
                    case 2:
                        // 零点位于水位线上
                        y = mLevelLine;
                        break;
                    case 1:
                        // 往下波动的控制点
                        y = mLevelLine + mWaveHeight;
                        break;
                    case 3:
                        // 往上波动的控制点
                        y = mLevelLine - mWaveHeight;
                        break;
                }
//                y += mWaveHeight / 2;
                mPointsList.add(new Point(x, y));
            }
            updatePath(height);
            if (DEBUG) {
                Log.d(TAG, getString());
            }
        }

        public String getString() {
            StringBuilder sb = new StringBuilder();
            sb.append("wave size(");
            sb.append(width);
            sb.append(",");
            sb.append(height);
            sb.append("), mLevelLine=");
            sb.append(mLevelLine);
            sb.append(", mWaveWidth=" + mWaveWidth);
            sb.append(", mWaveHeight=");
            sb.append(mWaveHeight);
            sb.append(", direct=");
            sb.append(direct);
            return sb.toString();
        }
    }

    static final class WaveSprite {
        private float x0, y0;
        private Wave wave;
        /**
         * 水位
         */
        private float mLevelLine;

        /**
         * 左边隐藏波形
         */
        private float mLeftSide;
        /**
         * 平移距离
         */
        private float mMoveLen;
        private int direct = 1;
        /**
         * 平移波速
         */
        public float speed = direct * 1.7f;

        public int color;

        public WaveSprite() {
            init();
        }

        public void init() {
        }

        public void setDirect(int direct) {
            this.direct = direct;
            x0 = direct > 0 ? -wave.mWaveWidth : 0;
            speed = Math.abs(speed) * direct;
        }

        public void setSpeed(float speed) {
            this.speed = speed * direct;
        }

        public float getSpeed() {
            return speed;
        }

        public void logic() {
            if (wave == null) {
                return;
            }
            // 平移计数
            mMoveLen += Math.abs(speed);
            // 水位
//            mLevelLine -= 0.1f;
//            if (mLevelLine < 0)
//                mLevelLine = 0;
            mLeftSide += speed;
            if (mMoveLen >= wave.mWaveWidth) {
                //平移复位
                mMoveLen = 0;
            }
        }

        public void setWave(Wave w) {
            this.wave = w;
            // 水位线从最底下开始上升
            mLevelLine = 0;
            // 这里计算在可见的View宽度中能容纳几个波形，注意n上取整
            mLeftSide = -w.mWaveWidth;
            x0 = direct > 0 ? -wave.mWaveWidth : 0;
            if (DEBUG) {
                Log.i(TAG, getString());
            }
        }

        public String getString() {
            StringBuilder sb = new StringBuilder();
            if (wave != null) {
                sb.append("wave size(");
                sb.append(wave.width);
                sb.append(",");
                sb.append(wave.height);
                sb.append(", mWaveWidth=");
                sb.append(wave.mWaveWidth);
                sb.append(", mWaveHeight=");
                sb.append(wave.mWaveHeight);
            }
            sb.append("), mLevelLine=");
            sb.append(mLevelLine);
            sb.append("， speed=");
            sb.append(speed);
            sb.append(", direct=");
            sb.append(direct);
            sb.append(", color=#");
            sb.append(Long.toHexString(color));
            return sb.toString();
        }


        public void onDraw(Canvas canvas, Paint mPaint) {
            if (wave != null) {
                mPaint.setColor(color);
                canvas.save();
                canvas.translate(x0 + mMoveLen * direct, mLevelLine);
                canvas.drawPath(wave.mWavePath, mPaint);
                canvas.restore();
            }
        }
    }
}