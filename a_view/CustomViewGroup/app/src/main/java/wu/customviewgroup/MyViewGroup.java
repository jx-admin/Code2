package wu.customviewgroup;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Scroller;
import android.widget.Toast;

public class MyViewGroup extends ViewGroup implements OnGestureListener {
    private static final String TAG = MyViewGroup.class.getSimpleName();

    private float mLastMotionY;// 最后点击的点
    private GestureDetector detector;
    int move = 0;// 移动距离
    int scrollerHeight = 850;// 最大允许的移动距离
    private Scroller mScroller;
    int up_excess_move = 0;// 往上多移的距离
    int down_excess_move = 0;// 往下多移的距离
    private final static int TOUCH_STATE_REST = 0;
    private final static int TOUCH_STATE_SCROLLING = 1;
    private int mTouchSlop;
    private int mTouchState = TOUCH_STATE_REST;
    Context mContext;


    public MyViewGroup(Context context) {
        super(context);
        mContext = context;
        setBackgroundColor(0xff892132);
        mScroller = new Scroller(context);
        detector = new GestureDetector(this);

        final ViewConfiguration configuration = ViewConfiguration.get(context);
        // 获得可以认为是滚动的距离
        mTouchSlop = configuration.getScaledTouchSlop();

        // 添加子View
        for (int i = 0; i < 48; i++) {
            final Button MButton = new Button(context);
            MButton.setText("" + (i + 1));
            MButton.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    Toast.makeText(mContext, MButton.getText(), Toast.LENGTH_SHORT).show();
                }
            });
            addView(MButton);
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            // 返回当前滚动X方向的偏移
            scrollTo(0, mScroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final float y = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:

                mLastMotionY = y;
                mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST
                        : TOUCH_STATE_SCROLLING;
                break;
            case MotionEvent.ACTION_MOVE:
                final int yDiff = (int) Math.abs(y - mLastMotionY);
                boolean yMoved = yDiff > mTouchSlop;
                // 判断是否是移动
                if (yMoved) {
                    mTouchState = TOUCH_STATE_SCROLLING;
                }
                break;
            case MotionEvent.ACTION_UP:
                mTouchState = TOUCH_STATE_REST;
                break;
        }
        return mTouchState != TOUCH_STATE_REST;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final float y = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.forceFinished(true);
                    move = mScroller.getFinalY();
                }
                mLastMotionY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                if (ev.getPointerCount() == 1) {
                    // 随手指 拖动的代码
                    int deltaY = (int) (mLastMotionY - y);
                    mLastMotionY = y;
                    Log.d("move", "" + move);
                    if (deltaY < 0) {
                        // 下移
                        // 判断上移 是否滑过头
                        if (up_excess_move == 0) {
                            if (move > 0) {
                                int move_this = Math.max(-move, deltaY);//限制不超过最顶端
                                move = move + move_this;
                                scrollBy(0, move_this);
                            } else if (move == 0) {// 如果已经是最顶端 继续往下拉
                                Log.d("down_excess_move", "" + down_excess_move);
                                int downExcessMove = deltaY / 2;//超出移动距离要慢一半
                                down_excess_move -= downExcessMove;// 记录下多往下拉的值
                                scrollBy(0, downExcessMove);
                            }
                        } else if (up_excess_move > 0)// 之前有上移过头
                        {
                            if (up_excess_move >= (-deltaY)) {
                                up_excess_move = up_excess_move + deltaY;
                                scrollBy(0, deltaY);
                            } else {
                                up_excess_move = 0;
                                scrollBy(0, -up_excess_move);
                            }
                        }
                    } else if (deltaY > 0) {
                        // 上移
                        if (down_excess_move == 0) {
                            if (scrollerHeight - move > 0) {
                                int move_this = Math.min(scrollerHeight - move, deltaY);
                                move = move + move_this;
                                scrollBy(0, move_this);
                            } else if (scrollerHeight - move == 0) {
                                if (up_excess_move <= 100) {
                                    up_excess_move = up_excess_move + deltaY / 2;
                                    scrollBy(0, deltaY / 2);
                                }
                            }
                        } else if (down_excess_move > 0) {
                            if (down_excess_move >= deltaY) {
                                down_excess_move = down_excess_move - deltaY;
                                scrollBy(0, deltaY);
                            } else {
                                down_excess_move = 0;
                                scrollBy(0, down_excess_move);
                            }
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                // 多滚是负数 记录到move里
                if (up_excess_move > 0) {
                    // 多滚了 要弹回去
                    scrollBy(0, -up_excess_move);
                    invalidate();
                    up_excess_move = 0;
                }
                if (down_excess_move > 0) {
                    // 多滚了 要弹回去
                    scrollBy(0, down_excess_move);
                    invalidate();
                    down_excess_move = 0;
                }
                mTouchState = TOUCH_STATE_REST;
                break;
        }
        return this.detector.onTouchEvent(ev);
    }

    int Fling_move = 0;

    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        //随手指 快速拨动的代码
        Log.d("onFling", "onFling");
        if (up_excess_move == 0 && down_excess_move == 0) {

            int slow = -(int) velocityY * 3 / 4;
            mScroller.fling(0, move, 0, slow, 0, 0, 0, scrollerHeight);
            move = mScroller.getFinalY();
            computeScroll();
        }
        return false;
    }

    public boolean onDown(MotionEvent e) {
        // TODO Auto-generated method stub
        return true;
    }

    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        return false;
    }

    public void onShowPress(MotionEvent e) {
        // // TODO Auto-generated method stub
    }

    public boolean onSingleTapUp(MotionEvent e) {
        // TODO Auto-generated method stub
        return false;
    }

    public void onLongPress(MotionEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d(TAG, String.format("onLayout: %b, %d, %d, %d, %d ", changed, l, t, r, b));
        int childTop = 0;
        int childLeft = 0;
        int width = r - l, height = b - t;
        int chWidth = width / 4;
        int maxChHeight = 0;
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                child.setVisibility(View.VISIBLE);
                child.measure(r - l, b - t);
                child.layout(childLeft, childTop, childLeft + chWidth,
                        childTop + child.getMeasuredHeight());
                childLeft += chWidth;
                maxChHeight = Math.max(child.getMeasuredHeight(), maxChHeight);
                if (childLeft >= width) {
                    childTop += maxChHeight;
                    childLeft = 0;
                    maxChHeight = 0;
                }
            }
        }
        scrollerHeight = childTop + maxChHeight - height;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthModel = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightModel = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        Log.d(TAG, String.format("onMeasure:widthModel=%d, widthSize=%d, heightModel=%d, heightSize=%d", widthModel, widthSize, heightModel, heightSize));
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


}

