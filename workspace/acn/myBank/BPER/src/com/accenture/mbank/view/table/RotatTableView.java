
package com.accenture.mbank.view.table;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

import com.accenture.mbank.BaseActivity;
import com.accenture.mbank.util.LogManager;

public class RotatTableView extends BankTableView {
    /**
     */
    public ScrollView parentScrollView;

    /**
     * 原心坐标x
     */
    float o_x;

    /**
     * 原心坐标y
     */
    float o_y;

    /**
     * 图片的宽度
     */
    int width;

    /**
     * 图片的高度
     */
    int height;

    /**
     * view的真实宽度与高度:因为是旋转，所以这个view是正方形，它的值是图片的对角线长度
     */
    double maxwidth;

    double maxHeight;

    BitmapDrawable sliderDrawable;

    BitmapDrawable bgDrawable;

    BitmapDrawable arrowDrawable;

    /**
     * 旋转的图片
     */
    Bitmap slidBitmap;

    Bitmap backgroundBitmap;

    public RotatTableView(Context context) {

        super(context);
        init();

    }

    public RotatTableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    /**
     * 初始化handler与速度计算器
     */
    protected void init() {
        initSize();
    }

    private int count = 5;

    public int getCount() {
        return count;
    }

    double[] xifu;

    public void setCount(int count) {
        this.count = count;
        // 4;i=3,i=2
        xifu = new double[count];
        for (int i = 0; i < count; i++) {
            xifu[count - i - 1] = ((i + 1) * 360) / count;
        }
    }

    /**
     * 设置
     * 
     * @param slider  -- slider image
     * @param bg      -- background image
     * @param arrow   -- arrow image
     */
    public void setRotatResource(int slider, int bg, int arrow) {
        if (bgDrawable != null) {
            bgDrawable = null;
        }

        if (sliderDrawable != null) {
            sliderDrawable = null;
        }

        if (arrowDrawable != null) {
            arrowDrawable = null;
        }
        if (slider != -1) {
        	sliderDrawable = (BitmapDrawable)getContext().getResources().getDrawable(slider);
        }

        bgDrawable = (BitmapDrawable)getContext().getResources().getDrawable(bg);
        if (arrow != -1) {
            arrowDrawable = (BitmapDrawable)getContext().getResources().getDrawable(arrow);
        }
        try {
        	if (sliderDrawable != null)
        		slidBitmap = sliderDrawable.getBitmap();
        	if (bgDrawable != null)
        		backgroundBitmap = bgDrawable.getBitmap();
        } catch (OutOfMemoryError outofMemory) {
            System.gc();
            System.runFinalization();
            slidBitmap = sliderDrawable.getBitmap();
            backgroundBitmap = bgDrawable.getBitmap();
            LogManager.e("I'm here!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
        initSize();
    }

    public static final int margin = 16;

    /**
     * 触摸半径
     */
    protected double radius = 0;

    /**
     * 触摸半径系数
     */
    public double radiusX = (double)(213d / 558d);

    /**
     * 画小黑圆的画笔
     */
    private Paint paint1 = new Paint();

    /**
     * 度数
     */
    public static final int isTouchPrecision = 45;

    /**
     * 手指触摸sliderBar的半径范围
     */
    public static double isTouchPrecisionLength = 0;

    /**
     * 手指触摸sliderBar的角度范围
     */
    public static final double doubleIsTouchPrecisionLength = 120d / 558d;

    public static final double widthXX = 600d / 720d;

    double xx;

    protected int imageWidth;

    protected int imageHeight;

    protected void initSize() {

        if (backgroundBitmap == null) {

            return;
        }
        imageWidth = backgroundBitmap.getWidth();
        imageHeight = backgroundBitmap.getHeight();

        maxwidth = BaseActivity.screen_width * widthXX;

        xx = maxwidth / imageWidth;

        maxHeight = imageHeight * xx;

        o_x = (float)(maxwidth / 2);
        o_y = (float)(maxHeight / 2);
        radius = xx * radiusX * imageWidth;
        isTouchPrecisionLength = xx * doubleIsTouchPrecisionLength * imageWidth;
        if (isTouchPrecisionLength < 40) {
            isTouchPrecisionLength = 40;
        }
        bgDrawable.setBounds(new Rect(0, 0, (int)imageWidth, (int)imageHeight));
        if (sliderDrawable != null) {
        	sliderDrawable.setBounds(new Rect(0, 0, (int)imageWidth, (int)imageHeight));
        }
        if (arrowDrawable != null) {
            arrowDrawable.setBounds(new Rect(0, 0, (int)imageWidth, (int)imageHeight));
        }
    }

    /**
     * 通过此方法来控制旋转度数，如果超过360，让它求余，防止，该值过大造成越界
     * 
     * @param added
     */
    private void addDegree(float added) {
        deta_degree += added;

        if (deta_degree >= 360) {
            deta_degree = deta_degree - 360;
        }
        if (deta_degree <= -360) {
            deta_degree = deta_degree + 360;
        }

    }

    Rect rect = new Rect();
    @Override
    protected void onDraw(Canvas canvas) {

        canvas.save();

        canvas.scale((float)xx, (float)xx);

        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        }
        if (arrowDrawable != null) {
            arrowDrawable.draw(canvas);
        }

        canvas.restore();
        if (isSlidDown) {
            drawMiniCycle(canvas);
        }
        
		if (sliderDrawable != null) {

            double sin = Math.sin((deta_degree -90)  * Math.PI / 180);
            double cos = Math.cos((deta_degree -90)* Math.PI / 180);
            double x = radius * cos;
            double y = radius * sin;
            int centerx = (int) o_x + (int)x;
            int centery = (int) o_y + (int)y;
            int offset = sliderDrawable.getBitmap().getWidth()/2;

            rect.left = centerx - offset;
            rect.top =  centery - offset;
            rect.right = centerx + offset;
            rect.bottom = centery + offset;
			canvas.drawBitmap(sliderDrawable.getBitmap(), null, rect, null);
		}
        super.onDraw(canvas);
        return;
    }

    private void drawMiniCycle(Canvas canvas) {

    	if (xifu == null)
    		return; 

        for (int i = 0; i < xifu.length; i++) {

            double dd = xifu[i] - 90;
            double sin = Math.sin(dd * Math.PI / 180);
            double cos = Math.cos(dd * Math.PI / 180);
            double x = radius * cos;
            double y = radius * sin;
            x = x + o_x;
            y = y + o_y;

            paint1.setColor(Color.argb(100, 20, 20, 20));
            canvas.drawCircle((float)x, (float)y, (float)10, paint1);

        }

    }

	public void startAnimation() {
		final int DELTA = 6; // degree
		final int PERIOD = 10; // ms
		final int NUMBER = 360 / DELTA;

		LogManager.d("startAnimation");

		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case BaseActivity.MSG_DASHBOARD_ANIMATION:
					addDegree(-DELTA);
					invalidate();
					break;
				}
			}
		};

		final Timer t = new Timer();
		t.schedule(new TimerTask() {
			int num = 0;

			@Override
			public void run() {
				handler.sendEmptyMessage(BaseActivity.MSG_DASHBOARD_ANIMATION);

				num++;
				if (num >= NUMBER)
					t.cancel();
			}
		}, 0, PERIOD);
	}

    Paint paint = new Paint();

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 它的宽高不是图片的宽高，而是以宽高为直角的矩形的对角线的长度
        setMeasuredDimension((int)maxwidth, (int)maxHeight);

    }

    /**
     * 手指触屏的初始x的坐标
     */
    float down_x;

    /**
     * 手指触屏的初始y的坐标
     */
    float down_y;

    /**
     * 移动时的x的坐标
     */
    float target_x;

    /**
     * 移动时的y的坐标
     */
    float target_y;

    /**
     * 放手时的x的坐标
     */
    float up_x;

    /**
     * 放手时的y的坐标
     */
    float up_y;

    /**
     * 当前的弧度(以该 view 的中心为圆点)
     */
    float current_degree;

    /**
     * 放手时的弧度(以该 view 的中心为圆点)
     */
    float up_degree;

    /**
     * 当前圆盘所转的弧度(以该 view 的中心为圆点)
     */
    float deta_degree = 0;

    protected boolean isSlidDown;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (slidBitmap == null) {

        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                down_x = event.getX();
                down_y = event.getY();
                current_degree = detaDegree(o_x, o_y, down_x, down_y);

                if (isTouchSlidBar(down_x, down_y)) {

                    setParentScrollAble(false);
                    isSlidDown = true;
                    invalidate();
                    return true;
                }
                // if (isTouchSlidBar()) {
                // return true;
                // } else {
                // return false;
                // }
                return false;

            }
            case MotionEvent.ACTION_MOVE: {
                // LogManager.d("move");
                if (isSlidDown) {
                    down_x = target_x = event.getX();
                    down_y = target_y = event.getY();
                    float degree = detaDegree(o_x, o_y, target_x, target_y);

                    // 滑过的弧度增量
                    float dete = degree - current_degree;
                    // 如果小于-90度说明 它跨周了，需要特殊处理350->17,
                    if (dete < -270) {
                        dete = dete + 360;

                        // 如果大于90度说明 它跨周了，需要特殊处理-350->-17,
                    } else if (dete > 270) {
                        dete = dete - 360;
                    }
                    addDegree(dete);
                    current_degree = degree;
                    postInvalidate();

                    return true;
                }
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {

                up_x = event.getX();
                up_y = event.getY();
                up_degree = detaDegree(o_x, o_y, up_x, up_y);

                noticAdsorbent();
                setParentScrollAble(true);
                break;

            }
        }
        // System.out.println("ontouch");
        return false;
    }

    @Override
    protected void onAttachedToWindow() {
        // TODO Auto-generated method stub
        super.onAttachedToWindow();
    }

    private void noticAdsorbent() {

    	if (xifu == null)
    		return;

        double min = 360;
        int result = -1;
        for (int i = 0; i < xifu.length; i++) {
            double xx = Math.abs(deta_degree - xifu[i]);

            if (xx > 270) {
                xx = Math.abs(360 - xx);
            }
            if (xx < min) {
                min = xx;
                result = i;
            }
        }
        deta_degree = (float)xifu[result];
        isSlidDown = false;
        invalidate();
        if (onSlidListener != null) {
            onSlidListener.onSlid(this, result);
            System.out.println("result+" + result);
        }
    }

    boolean isTouchSlidBar(float x, float y) {

        double length = (int)((x - o_x) * (x - o_x) + (o_y - y) * (o_y - y));

        int deltalength = (int)(length - radius * radius);
        deltalength = Math.abs(deltalength);
        if (deltalength > isTouchPrecisionLength * isTouchPrecisionLength) {

        } else {

            double degree = Math.abs((deta_degree - 90 + 360) - current_degree);
            if (degree > 270) {
                degree = Math.abs(360 - degree);
            }
            if ((degree % 360) < isTouchPrecision) {
                return true;
            }
        }
        // float xx = Math.abs(current_degree % 360 - deta_degree % 360);
        // if (xx < isTouchPrecision) {
        // return true;
        // }

        return false;
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

    /**
     * 是否把滚动事件交给父scrollview
     * 
     * @param flag
     */
    private void setParentScrollAble(boolean flag) {

        if (parentScrollView != null) {
            parentScrollView.requestDisallowInterceptTouchEvent(!flag);
        }
    }

    private OnSlidListener onSlidListener;

    public OnSlidListener getOnSlidListener() {
        return onSlidListener;
    }

    public void setOnSlidListener(OnSlidListener onSlidListener) {
        this.onSlidListener = onSlidListener;
    }

    public static interface OnSlidListener {
        void onSlid(View v, int index);

    }
}
