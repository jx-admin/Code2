package wu.a.lib.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wu.a.lib.R;

/**
 * Created by wjx on 2015/11/11.
 */
public class RecyclerBannerView extends ReHeightFramelayout implements ViewPager.OnPageChangeListener {
    public static RecyclerBannerView sInstance;
    private static final String TAG = "MainBannerView";
    private MyAdapter viewPagerAdapter;
    private ViewPager viewPager;
    private IndicatorView indicatorView;
    private View contentView;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (isStart && viewPager != null && mDatas.size() > 1) {
                setView(currentVirtual + 1);
                viewPager.setCurrentItem(currentVirtual + 1);
            }
        }
    };

    private boolean isStart;

    public void start() {
        isStart = true;
        handler.removeMessages(0);
        next();
    }

    private void next() {
        if (isStart && viewPager != null && mDatas.size() > 1) {
            handler.sendEmptyMessageDelayed(0, 3000);
        }
    }

    public void stop() {
        isStart = false;
        handler.removeMessages(0);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
//        start();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == View.VISIBLE) {
            start();
        } else {
            stop();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
//        stop();
    }

    public RecyclerBannerView(Context context) {
        this(context, null);
    }

    public RecyclerBannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerBannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        sInstance = this;
        viewPagerAdapter = new MyAdapter(context);
        contentView = inflate(getContext(), R.layout.recycler_banner_layout, null);
        viewPager = (ViewPager) contentView.findViewById(R.id.viewpager);
        indicatorView = (IndicatorView) contentView.findViewById(R.id.indicator);
        indicatorView.setRadius((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
        indicatorView.setCheckedColor(0xFFFF0000);
        indicatorView.setBackgroundColor(0xFFFFFFFF);
        viewPager.addOnPageChangeListener(this);
        addView(contentView);
        update();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    public void addView(View child) {
        if (contentView == child) {
            super.addView(child);
        } else {
            mDatas.add(child);
            setData(mDatas);
        }
    }

    private void update() {
        if (viewPager != null) {
            viewPager.setAdapter(viewPagerAdapter);
            viewPager.setCurrentItem(currentVirtual);
        }
        if (indicatorView != null) {
            indicatorView.setTotalPage(sizeTrue);
            indicatorView.setIndex(currentTrue);
        }
    }

    /**
     * 使用的View
     */
    private Map<Integer, FrameLayout> usedView = new HashMap<Integer, FrameLayout>();
    private List<View> mDatas = new ArrayList<View>();
    private int sizeVirtual;
    private int sizeTrue;
    private int currentVirtual;
    private int currentTrue;

    public void setData(List<View> mImageViewList) {
        this.mDatas = mImageViewList;
        sizeTrue = mDatas == null ? 0 : mDatas.size();
        sizeVirtual = sizeTrue;
        if (sizeVirtual > 1) {
            indicatorView.setVisibility(View.VISIBLE);
            sizeVirtual = Integer.MAX_VALUE;
            currentVirtual = sizeTrue * 100;
            currentTrue = currentVirtual % sizeTrue;
        } else {
            indicatorView.setVisibility(View.GONE);
        }
        update();
    }

    /**
     * @author abc
     */
    public class MyAdapter extends PagerAdapter {
        private Context context;
        private List<FrameLayout> cacheView;

        public MyAdapter(Context context) {
            super();
            this.context = context;
            cacheView = new ArrayList<FrameLayout>();
        }

        /**
         * 该方法将返回所包含的 Item总个数。为了实现一种循环滚动的效果，返回了基本整型的最大值，这样就会创建很多的Item,
         * 其实这并非是真正的无限循环。
         */
        @Override
        public int getCount() {
            return sizeVirtual;
        }

        /**
         * 判断出去的view是否等于进来的view 如果为true直接复用
         */
        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        /**
         * 销毁预加载以外的view对象, 会把需要销毁的对象的索引位置传进来，就是position，
         * 因为mImageViewList只有五条数据，而position将会取到很大的值，
         * 所以使用取余数的方法来获取每一条数据项。
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            FrameLayout contener = usedView.remove(position);
            container.removeView(contener);
            contener.removeAllViews();
            cacheView.add(contener);
        }

        /**
         * 创建一个view，
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            FrameLayout contenrer;
            if (cacheView.size() > 0) {
                contenrer = cacheView.remove(cacheView.size() - 1);
            } else {
                contenrer = new FrameLayout(context);
            }
            container.addView(contenrer);
            usedView.put(position, contenrer);
            if (currentVirtual == position) {
                setView(position);
            }
            return contenrer;
        }
    }

    boolean isDrag;

    @Override
    public void onPageScrollStateChanged(int arg0) {
        if (arg0 == 1) {
            isDrag = true;
            nextItem = 0;
            stop();
        } else if (arg0 == 0) {
            start();
        } else {
            isDrag = false;
        }

    }


    private int nextItem;

    @Override
    public void onPageScrolled(int position, float off, int offPx) {
        if (isDrag) {
            if (position == currentVirtual) {
                if (nextItem != 1) {
                    nextItem = 1;
                    setView(currentVirtual + nextItem);
                }
            } else {
                if (nextItem != -1) {
                    nextItem = -1;
                    setView(currentVirtual + nextItem);
                }

            }
        }

    }

    @Override
    public void onPageSelected(int position) {
        currentVirtual = position;
        currentTrue = position % mDatas.size();
        indicatorView.setIndex(currentTrue);
        next();
    }

    private void setView(int position) {
        FrameLayout fl = usedView.get(position);
        if (fl == null) {
            return;
        }
        position = position % mDatas.size();
        View chile = mDatas.get(position);
        if (chile.getParent() != null) {
            ((FrameLayout) chile.getParent()).removeAllViews();
        }
        fl.addView(chile);
    }
}
