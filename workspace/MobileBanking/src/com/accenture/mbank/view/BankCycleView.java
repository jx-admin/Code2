
package com.accenture.mbank.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.accenture.mbank.BaseActivity;
import com.accenture.mbank.R;

public class BankCycleView extends SurfaceView implements SurfaceHolder.Callback {

    /**
     * 转速，弧度
     */
    double speed = 0;

    /**
     * 速度计算器
     */
    VRecord vRecord;

    /**
     * The number of cycle nodes
     */
    private int cycleNode = 5;

    SurfaceHolder holder;

    public static final double widthsqr = 1.4;

    /**
     * the point last ontouch
     */
    int current_X, current_Y, imageWidth, imageHeight;

    double maxWidth;

    /**
     * center point of cycle
     */
    float o_x, o_y;

    /**
     * the thread ,control UI at background
     */
    UiThread thread;

    private Bitmap rotatBitmap;

    /**
     * 最小加速度（当手指放手是）
     */
    public static final float a_min = 0.002f;

    /**
     * 加速度增量
     */
    public static final float a_add = 0.001f;

    /**
     * 加速度
     */
    float a = a_min;

    /**
     * 最大加速度（当手指按住时）
     */
    public static final float a_max = a_min * 5;

    /**
     * 当前圆盘所转的弧度(以该 view 的中心为圆点)
     */
    float deta_degree;

    private float down_x;

    private float down_y;

    private float current_degree;

    private long lastMoveTime;

    private float up_x;

    private float up_y;

    private float up_degree;

    private boolean isClockWise;

    private long currentTime;

    private long delayedTime;

    public BankCycleView(Context context) {
        super(context);

        init();
    }

    public BankCycleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public BankCycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Bitmap getRotateDrawable() {
        if (rotatBitmap == null) {
        }
        return rotatBitmap;
    }

    public void setRotatDrawableResource(int id) {

        BitmapDrawable drawable = (BitmapDrawable)getContext().getResources().getDrawable(id);

        setRotatDrawable(drawable);
    }

    /**
     * set the rotate drawable
     * 
     * @param drawable
     */
    public void setRotatDrawable(BitmapDrawable drawable) {
        rotatBitmap = drawable.getBitmap();
        initSize();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension((int)maxWidth, (int)maxWidth);

    }

    private void initSize() {
        if (rotatBitmap == null) {

            // throw new NoBitMapError("Error,No bitmap in RotatView!");
            return;
        }
        int width = rotatBitmap.getWidth();
        int height = rotatBitmap.getHeight();
        if (width > BaseActivity.screen_width || height > BaseActivity.screen_height) {

            // height = width = (int)(BaseActivity.screen_width / widthsqr);
            height = width = 200;
            rotatBitmap = Bitmap.createScaledBitmap(rotatBitmap, width, height, false);
        }

        maxWidth = Math.sqrt(width * width + height * height);
        this.imageWidth = width;
        this.imageHeight = height;

        o_x = o_y = (float)(maxWidth / 2);// 确定圆心坐标
    }

    private void init() {
        holder = this.getHolder();
        thread = new UiThread(holder, this);
        holder.addCallback(this);
        setZOrderOnTop(true);// 设置画布 背景透明
        holder.setFormat(PixelFormat.TRANSLUCENT);
        vRecord = new VRecord();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                down_x = event.getX();
                down_y = event.getY();
                current_degree = detaDegree(o_x, o_y, down_x, down_y);
                vRecord.reset();
                // handler.sendEmptyMessage(stop);
                a = a_max;
                speed = 0;

                break;

            }
            case MotionEvent.ACTION_MOVE: {
                down_x = event.getX();
                down_y = event.getY();
                float degree = detaDegree(o_x, o_y, down_x, down_y);

                // 滑过的弧度增量
                float dete = degree - current_degree;
                // 如果小于-90度说明 它跨周了，需要特殊处理350->17,
                if (dete < -270) {
                    dete = dete + 360;

                    // 如果大于90度说明 它跨周了，需要特殊处理-350->-17,
                } else if (dete > 270) {
                    dete = dete - 360;
                }
                lastMoveTime = System.currentTimeMillis();
                vRecord.add(dete, lastMoveTime);
                addDegree(dete);
                current_degree = degree;

                postDraw();
                speed = 0;
                break;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                a = a_min;

                double lastupTime = System.currentTimeMillis();
                double detaTime = lastupTime - lastMoveTime;
                up_x = event.getX();
                up_y = event.getY();
                up_degree = detaDegree(o_x, o_y, up_x, up_y);
                // 放手时的速度
                speed = speed + vRecord.getSpeed();
                if (speed > 0) {
                    speed = Math.min(VRecord.max_speed, speed);
                } else {
                    speed = Math.max(-VRecord.max_speed, speed);
                }
                // System.out.println("speed:" + speed);
                if (speed > 0) {
                    isClockWise = true;
                    // v = 1;
                } else {
                    isClockWise = false;
                    // v = -1;
                }
                currentTime = System.currentTimeMillis();
                postDraw();
                break;

            }
        }
        return true;
    }

    boolean cycle_meode = true;

    /**
     * 通过此方法来控制旋转度数，如果超过360，让它求余，防止，该值过大造成越界
     * 
     * @param d
     */
    private void addDegree(double d) {
        deta_degree += d;

        if (cycle_meode) {
            if (deta_degree > 360 || deta_degree < -360) {
                deta_degree = deta_degree % 360;
            }
        } else {
            if (deta_degree > 360) {
                deta_degree = 360;
            }
            if (deta_degree < 0) {
                deta_degree = 0;
            }
        }

    }

    /**
     * 计算以(src_x,src_y)为坐标圆点，建立直角体系，求出(target_x,target_y)坐标与x轴的夹角
     * 主要是利用反正切函数的知识求出夹角
     * 
     * @param src_x
     * @param src_y
     * @param target_x
     * @param target_y
     * @return
     */
    float detaDegree(float src_x, float src_y, float target_x, float target_y) {

        float detaX = target_x - src_x;
        float detaY = target_y - src_y;
        double d;
        // 坐标在四个象限里
        if (detaX != 0) {
            float tan = Math.abs(detaY / detaX);

            if (detaX > 0) {

                // 第一象限
                if (detaY >= 0) {
                    d = Math.atan(tan);

                } else {
                    // 第四象限
                    d = 2 * Math.PI - Math.atan(tan);
                }

            } else {
                if (detaY >= 0) {
                    // 第二象限
                    d = Math.PI - Math.atan(tan);
                } else {
                    // 第三象限
                    d = Math.PI + Math.atan(tan);
                }
            }

        } else {
            // 坐标在y轴上
            if (detaY > 0) {
                // 坐标在y>0上
                d = Math.PI / 2;
            } else {
                // 坐标在y<0上
                d = -Math.PI / 2;
            }
        }

        return (float)((d * 180) / Math.PI);
    }

    private void postDraw() {
        synchronized (thread.lock) {

            if (thread.isLock) {
                thread.lock.notify();
                thread.isLock = false;
            }
        }

    }

    Paint paint = new Paint();

    private boolean onThreadDraw(Canvas c) {

        Matrix matrix = new Matrix();
        // 设置转轴位置
        matrix.setTranslate((float)imageWidth / 2, (float)imageHeight / 2);

        // 开始转
        matrix.preRotate(deta_degree);
        // 转轴还原
        matrix.preTranslate(-(float)imageWidth / 2, -(float)imageWidth / 2);

        // 将位置送到view的中心
        matrix.postTranslate((float)(maxWidth - imageWidth) / 2,
                (float)(maxWidth - imageHeight) / 2);
        c.drawColor(0, PorterDuff.Mode.CLEAR);
        try {
            if (rotatBitmap != null) {

                c.drawBitmap(rotatBitmap, matrix, null);
            }
        } catch (Exception e) {
        } finally {
            holder.unlockCanvasAndPost(c);

        }

        if (keepRotat()) {
            return true;
        }
        return false;

    }

    private boolean keepRotat() {

        delayedTime = System.currentTimeMillis() - currentTime;
        currentTime = System.currentTimeMillis();
        double lastSpeed = speed;
        System.out.println("lastSpeed" + lastSpeed);
        float newa = 0;
        if (speed > 0) {
            newa = -Math.abs(a);
        } else if (speed < 0) {
            newa = Math.abs(a);
        } else {
            return false;
        }
        double degree = keepMove(newa);

        addDegree(degree);
        System.out.println("speed" + speed);
        if (lastSpeed * speed <= 0) {

            return false;
        } else {
            return true;
        }

    }

    /**
     * 物理
     * 
     * @param newa
     * @return
     */
    private double keepMove(double newa) {
        speed = speed + delayedTime * newa;
        double degree = newa * delayedTime * delayedTime / 2 + speed * delayedTime;
        return degree;
    }

    private double keepMove1(double newa) {
        speed = speed + 5 * newa;

        double aa = 0;
        if (newa < 0) {
            aa = 5;
        } else {
            aa = -5;
        }
        double degree = aa;
        return degree;
    }

    /**
     * 速度计算器 原来是将最近的 弧度增量和时间点记录下来，然后<br>
     * 通过增量除以总时间求出平均值做为它的即时手势滑过的速度
     */
    static class VRecord {

        /**
         * 数组中的有效数字
         */
        int addCount;

        /**
         * 最大能装的数据空间
         */
        public static final int length = 15;

        /**
         * 二维数组，1.保存弧度增量.2.保存产生这个增量的时间点
         */
        double[][] record = new double[length][2];

        /**
         * 为二维数组装载数据<br>
         * 注：通过此方法，有个特点，能把最后的length组数据记录下来，length以外的会丢失
         * 
         * @param detadegree
         * @param time
         */
        public void add(double detadegree, double time) {

            for (int i = length - 1; i > 0; i--) {
                record[i][0] = record[i - 1][0];
                record[i][1] = record[i - 1][1];
            }
            record[0][0] = detadegree;
            record[0][1] = time;
            addCount++;

        }

        /**
         * 最大速度
         */
        public static final double max_speed = 4;

        /**
         * 通过数组里所装载的数据分析出即时速度<br>
         * 原理是：计算数组里的时间长度和增量的总数，然后求出每毫秒所走过的弧度<br>
         * 当然不能超过{@link VRecord#max_speed}
         * 
         * @return
         */
        public double getSpeed() {

            if (addCount == 0) {
                return 0;
            }
            int maxIndex = Math.min(addCount, length) - 1;

            if ((record[0][1] - record[maxIndex][1]) == 0) {
                return 0;
            }

            double detaTime = record[0][1] - record[maxIndex][1];
            double sumdegree = 0;
            for (int i = 0; i < length - 1; i++) {

                sumdegree += record[i][0];
                // System.out.println(record[i][0]);
            }

            // System.out.println("----------");
            // System.out.println(sumdegree);
            // System.out.println(detaTime);
            double result = sumdegree / detaTime;
            if (result > 0) {
                return Math.min(result, max_speed);
            } else {
                return Math.max(result, -max_speed);
            }
            // System.out.println("v=" + result);

        }

        /**
         * 重置
         */
        public void reset() {
            addCount = 0;
            for (int i = length - 1; i > 0; i--) {
                record[i][0] = 0;
                record[i][1] = 0;
            }
        }
    }

    private static class UiThread extends Thread {
        SurfaceHolder holder;

        BankCycleView bankCycleView;

        public Object lock = new Object();

        public boolean isLock;

        public UiThread(SurfaceHolder holder, BankCycleView bankCycleView) {

            this.bankCycleView = bankCycleView;
            this.holder = holder;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            super.run();

            while (true) {
                Canvas c = holder.lockCanvas();
                if (c != null) {

                    boolean goOn = bankCycleView.onThreadDraw(c);
                    if (goOn) {

                    } else {
                        synchronized (lock) {
                            try {
                                isLock = true;
                                lock.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

            }

        }

    }
}
