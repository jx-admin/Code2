package com.baidu.lbsapi.album;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.baidu.lbsapi.album.util.ImageLoader;
import com.baidu.lbsapi.album.util.StreetscapeUtil;
import com.baidu.lbsapi.album.widget.SSAsyncImageView;
import com.baidu.lbsapi.album.widget.SinglePhotoLayout;
import com.baidu.lbsapi.controller.PanoramaController;
import com.baidu.lbsapi.panoramadata.PanoramaParser;

/**
 * @author ljx
 * @version V1.0
 * @Description: TODO
 * @date 2013-12-4 下午2:14:24
 */
public class IndoorPhotoAlbumView extends HorizontalScrollView {

    private static final int WIFI_BUFFER = 10;
    private static final int MOBILE_BUFFER = 3;

    private static final float INFLEXION = 0.35f; // Tension lines cross at (INFLEXION, 1)
    private static float DECELERATION_RATE = (float) (Math.log(0.78) / Math.log(0.9));//减速率

    private static final int ALBUM_FINGER_UP = 0;
    private static final int PHOTOALBUM_STOP_SRCOLL = 1;
    /* X轴范围最大超出距离 */
    private static final int MAX_X_OVERSCROLL_DISTANCE = 200;
    /* 从属Activity */
    Activity mContext;
    /* SSPhotoAlbumView中的横向布局 */
    public LinearLayout mLayout;
    /* 当前X轴位置 */
    private int mCurrentX;
    /* 允许最大滑动距离，以像素为单位 */
    private int mMaxOverScrollDistance;
    /* 第一张图片宽度，第二章图片宽度，上次载入前左边图片的index，上次载入前右边图片的index */
    private int mFirstWidth, mSecondWidth, mLastLeft, mLastRight;
    /* 物理系数 */
    private float mPhysicalCoeff;
    /* 判断移动方向true=向左移动,false=向右移动 */
    private boolean mIsMoveToLeft;
    /* 是否以计算过子控件宽度 */
    private boolean mIsWidthCalculated = false;
    /* 摩擦力 */
    private float mFlingFriction = ViewConfiguration.getScrollFriction();
    private Handler mHandler = new PhotoAlbumScrollStopedHandler(this);
    
    //返回正确json
    private final int GET_INDOORDESC_SUCC = 1;
    //返回错误json
    private final int GET_INDOORDESC_FAIL = 2;
    
    private PanoramaController mPanoramaController;
    
    private Handler mIndoorDescriptionHandler = new Handler() {
    	
        @Override
        public void handleMessage(Message msg) {
        	if(msg.what == GET_INDOORDESC_SUCC) {
        		updatePhotoAlbum();
        	}else if(msg.what == GET_INDOORDESC_FAIL) {
        		
        	}
        }
    };

    private int mInitialPosition = 0;

    private Bitmap mDefaultBitmap;
    /*所有照片的总点击监听器*/
    private SinglePhotoLayoutOnClickListener mSinglePhotoLayoutOnClickListener = new SinglePhotoLayoutOnClickListener();
    
    private ArrayList<HashMap<String, String>> mPhotoPointList;
    
    private IndoorAlbumOnClickListener mListener;
    
    private int mCurrentPhotoIndex;
    
    public IndoorPhotoAlbumView(Context context) {
        super(context);
        mContext = (Activity) context;
    }

    public IndoorPhotoAlbumView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = (Activity) context;
        mPanoramaController = new PanoramaController();
        final float ppi = context.getResources().getDisplayMetrics().density * 160.0f;
        mPhysicalCoeff = SensorManager.GRAVITY_EARTH // g (m/s^2)
                * 39.37f // inch/meter
                * ppi * 0.84f; // look and feel tuning
        setBackgroundColor(Color.parseColor("#ccd2dc"));
        mDefaultBitmap = StreetscapeUtil.getImageFromAssetsFile(mContext, "ss_photoalbum_background.png");
        mLayout = new LinearLayout(mContext);
        mMaxOverScrollDistance = (int) mContext.getResources().getDisplayMetrics().density * MAX_X_OVERSCROLL_DISTANCE;
        this.addView(mLayout);
    }

    /**
     * 设置点击相册接口
     * @param indoorAlbumOnClickListener
     */
    public void setOnIndoorAlbumClickListener(IndoorAlbumOnClickListener indoorAlbumOnClickListener) {
    	mListener = indoorAlbumOnClickListener;
    }
    
    /**
     * 显示相册
     * @param panoramaView
     * @param iid
     */
    public void setIndoorPanoramaId(final String iid) {

    	new Thread(new Runnable() {
			
			@Override
			public void run() {
					String json = mPanoramaController.getPanoramaByIIdWithJson(iid);
					if(TextUtils.isEmpty(json)) {
						//发送获取失败消息
						mIndoorDescriptionHandler.sendEmptyMessage(GET_INDOORDESC_FAIL);
					}else {
						mPhotoPointList = PanoramaParser.parseIdataJson(json);
						mIndoorDescriptionHandler.sendEmptyMessage(GET_INDOORDESC_SUCC);
					}
			}
		}).start();
    	
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
    	if(mPhotoPointList == null) {
    		return;
    	}

        //记录当前X轴位置
        mCurrentX = l;
        //判断滑动方向
        if (oldl - l < 0) {
            mIsMoveToLeft = true;
        } else if (oldl - l > 0) {
            mIsMoveToLeft = false;
        }

        mHandler.removeMessages(PHOTOALBUM_STOP_SRCOLL);
        mHandler.sendEmptyMessageDelayed(PHOTOALBUM_STOP_SRCOLL, 50);
        super.onScrollChanged(l, t, oldl, oldt);
    }

    private void resetPhotoAlbum() {
        mLayout.removeAllViews();
        smoothScrollTo(0, 0);
        mCurrentX = 0;
        mLastLeft = 0;
        mLastRight = 0;
    }
    

    public void updatePhotoAlbum() {
    	
    	if(mPhotoPointList == null || mPhotoPointList.size() == 0) {
    		setVisibility(View.GONE);
    		return;
    	}
        /* 将相册中的旧图片清除 */
        resetPhotoAlbum();
        for (int i = 0; i < mPhotoPointList.size(); i++) {
        	SinglePhotoLayout singlePhotoLayout = new SinglePhotoLayout(mContext);
        	singlePhotoLayout = singlePhotoLayout.getLayout();
            
            singlePhotoLayout.setOnClickListener(mSinglePhotoLayoutOnClickListener);

            mLayout.addView(singlePhotoLayout);
            singlePhotoLayout.setName(mPhotoPointList.get(i).get("title"));
            singlePhotoLayout.setImageUrl(mPhotoPointList.get(i).get("url"));

        }

        if (!mIsWidthCalculated) {
            mLayout.post(new Runnable() {
                @Override
                public void run() {

                    if (mPhotoPointList.size() > 1) {
                        mFirstWidth = mLayout.getChildAt(0).getWidth();
                        mSecondWidth = mLayout.getChildAt(1).getWidth();
                        mLastLeft = getCurrentLeft();
                        mLastRight = getCurrentRight();
                        mIsWidthCalculated = true;
                    }
                    /*下载新图片*/
                    loadImage();
                }
            });
        } else {
            /*下载新图片*/
            loadImage();
        }

    }

    private int getCurrentLeft() {
        if (mLayout != null && mPhotoPointList.size() > 1) {
            if (mCurrentX < mFirstWidth) {
                return 0;
            } else {
                int temp = (mCurrentX - mFirstWidth) / mSecondWidth;
                return temp + 1;
            }
        }
        return 0;
    }

    private int getCurrentRight() {
        if (mLayout != null && mLayout.getMeasuredWidth() <= this.getMeasuredWidth()) {
            return mPhotoPointList.size() - 1;
        } else if (mPhotoPointList.size() > 1) {
            int temp = Math.min(mLayout.getMeasuredWidth(), mCurrentX + this.getMeasuredWidth());
            int offset = ((temp - mFirstWidth) % mSecondWidth) == 0 ? 1 : 2;
            return ((temp - mFirstWidth) / mSecondWidth) + offset;
        }
        return 0;
    }

    private void loadImage() {
        ImageLoader.getInstance().cancelAllTask();
        int buffer;
        if (StreetscapeUtil.isWifiConnected(mContext)) {
            /* WIFI已经连接 */
            buffer = WIFI_BUFFER;
        } else {
            /* 2G或3G网络 */
            buffer = MOBILE_BUFFER;
        }
        
        int viewportLeft = Math.max(getCurrentLeft(), 0);
        int viewportRight = Math.min(getCurrentRight(), mLayout.getChildCount() - 1);
        int windowLeft = Math.max(getCurrentLeft() - buffer, 0);
        int windowRight = Math.min(getCurrentRight() + buffer, mLayout.getChildCount() - 1);
        for (int i = viewportLeft; i <= viewportRight; i++) {
            View v = mLayout.getChildAt(i);
            SSAsyncImageView asyncImageView = ((SinglePhotoLayout)v).getImageView();
            asyncImageView.show();
        }

        if (mIsMoveToLeft) {
            for (int r = getCurrentRight(); r < windowRight; r++) {
                View v = mLayout.getChildAt(r);
                SSAsyncImageView asyncImageView = ((SinglePhotoLayout)v).getImageView();
                asyncImageView.show();
            }
            for (int l = getCurrentLeft(); l > windowLeft; l--) {
                View v = mLayout.getChildAt(l);
                SSAsyncImageView asyncImageView = ((SinglePhotoLayout)v).getImageView();
                asyncImageView.show();
            }

            if (windowLeft > 0) {
                for (int i = mLastLeft; i < windowLeft; i++) {
                    View v = mLayout.getChildAt(i);
                    SSAsyncImageView asyncImageView = ((SinglePhotoLayout)v).getImageView();
                    asyncImageView.hide(mDefaultBitmap);
                }

            }
        } else {
            for (int l = getCurrentLeft(); l > windowLeft; l--) {
                View v = mLayout.getChildAt(l);
                SSAsyncImageView asyncImageView = ((SinglePhotoLayout)v).getImageView();
                asyncImageView.show();
            }
            for (int r = getCurrentRight(); r < windowRight; r++) {
                View v = mLayout.getChildAt(r);
                SSAsyncImageView asyncImageView = ((SinglePhotoLayout)v).getImageView();
                asyncImageView.show();
            }

            if (windowRight < mLayout.getChildCount() - 1) {
                for (int i = windowRight; i < mLastRight; i++) {
                    View v = mLayout.getChildAt(i);
                    SSAsyncImageView asyncImageView = ((SinglePhotoLayout)v).getImageView();
                    asyncImageView.hide(mDefaultBitmap);
                }
            }
        }
        mLastLeft = windowLeft;
        mLastRight = windowRight;
        scrollToInitialPosition();
    }
    
    private static class PhotoAlbumScrollStopedHandler extends Handler {

        private final WeakReference<IndoorPhotoAlbumView> mView;

        private PhotoAlbumScrollStopedHandler(IndoorPhotoAlbumView view) {
            mView = new WeakReference<IndoorPhotoAlbumView>(view);
        }

        @Override
        public void handleMessage(Message msg) {
            IndoorPhotoAlbumView photoAlbumView = mView.get();
            if (photoAlbumView == null) {
                return;
            }
            switch (msg.what) {
            case ALBUM_FINGER_UP:
                //得到停止滚动消息后，求第一张图宽度
                int firstWidth = photoAlbumView.mLayout.getChildAt(0).getWidth();
                int secondWidth = 0;
                if (photoAlbumView.mPhotoPointList.size() > 1) {
                    //求第二章图宽度
                    secondWidth = photoAlbumView.mLayout.getChildAt(1).getWidth();
                    //求偏移量
                    int offset = (photoAlbumView.mCurrentX - firstWidth) % secondWidth;
                    if (photoAlbumView.mIsMoveToLeft) {
                        //向右滑动左对齐
                        if (photoAlbumView.mCurrentX <= firstWidth) {
                            offset = photoAlbumView.mCurrentX % firstWidth;
                            photoAlbumView.smoothScrollBy(firstWidth - offset, 0);
                        } else if (photoAlbumView.getWidth() + photoAlbumView.mCurrentX < photoAlbumView.mLayout
                                .getWidth()) {
                            photoAlbumView.smoothScrollBy(secondWidth - offset, 0);
                        }
                    } else {
                        if (photoAlbumView.mCurrentX <= firstWidth) {
                            //如到达第一张图片则滑动到起始位置
                            photoAlbumView.smoothScrollTo(0, 0);
                        } else {
                            //向左滑动左对齐
                            photoAlbumView.smoothScrollBy(-offset, 0);
                        }

                    }
                }
                break;
            case PHOTOALBUM_STOP_SRCOLL:
                /*只有在2G或3G网络下采用部分下载策略*/
                photoAlbumView.loadImage();
                break;
            default:
                break;
            }
            super.handleMessage(msg);
        }

    }

    /**
     * 重写fling方法达到左对齐效果
     *
     * @param velocityX 加速度
     * @author Ziji Wang
     */
    @Override
    public void fling(int velocityX) {
    	if(mPhotoPointList == null) {
    		return;
    	}
        //为防止滑动过快，原加速度除4
        int newVelocityX = velocityX / 4;
        //如监听到fling则移除相册停止消息
        mHandler.removeMessages(ALBUM_FINGER_UP);

        //得到第一张图片宽度
        int firstWidth = mLayout.getChildAt(0).getWidth();
        int secondWidth = 0;
        if (mPhotoPointList.size() > 1) {

            //得到现在X轴上位置
            final int currentX = this.mCurrentX;
            //得到第二张图片宽度
            secondWidth = mLayout.getChildAt(1).getWidth();
            int expDistance;

            // 计算在当前加速度下滑动距离
            int flingDistance = (int) getSplineFlingDistance(newVelocityX);
            if (velocityX > 0) {
                // 向左滑动时的预计停止位置
                expDistance = currentX + flingDistance;
                // 求出当前停止位置超出单张图片的距离
                int offset = (expDistance - firstWidth) % secondWidth;
                if (offset != 0) {
                    // 求出达到左对齐时的滑动距离
                    int newDistance;
                    if (expDistance <= firstWidth) {
                        newDistance = firstWidth - currentX;
                    } else {
                        newDistance = flingDistance + (secondWidth - Math.abs(offset));
                    }
                    newVelocityX = getVelocity(newDistance);
                }
            } else {
                // 向右滑动同上
                expDistance = currentX - flingDistance;
                int offset = (expDistance - firstWidth) % secondWidth;
                if (offset != 0) {
                    int newDistance;
                    if (expDistance <= firstWidth) {
                        newDistance = flingDistance + expDistance;
                    } else {
                        newDistance = flingDistance + Math.abs(offset);
                    }
                    newVelocityX = -getVelocity(newDistance);
                }
            }
        }
        super.fling(newVelocityX);
    }

    /**
     * 获得弹簧减速率
     *
     * @param velocity 加速度
     * @return
     */
    private double getSplineDeceleration(int velocity) {
        return Math.log(INFLEXION * Math.abs(velocity) / (mFlingFriction * mPhysicalCoeff));
    }

    /**
     * 获得滑动距离
     *
     * @param velocity 速度
     * @return
     */
    private double getSplineFlingDistance(int velocity) {
        final double l = getSplineDeceleration(velocity);
        final double decelMinusOne = DECELERATION_RATE - 1.0;
        return mFlingFriction * mPhysicalCoeff * Math.exp(DECELERATION_RATE / decelMinusOne * l);
    }

    /**
     * 根据距离求出应有的速度
     *
     * @param distance
     * @return
     * @author Ziji Wang
     */
    private int getVelocity(double distance) {
        final double decelMinusOne = DECELERATION_RATE - 1.0;
        double l = (decelMinusOne * Math.log(distance / (mFlingFriction * mPhysicalCoeff))) / DECELERATION_RATE;
        return (int) (Math.exp(l) * mFlingFriction * mPhysicalCoeff / INFLEXION);
    }

    /**
     * 回收所有照片接口，在IndoorScapePage页面销毁时调用
     */
    protected void clearPhotos() {
        for (int i = mLayout.getChildCount() - 1; i > 0; i--) {
            View v = mLayout.getChildAt(i);
            SSAsyncImageView asyncImageView = ((SinglePhotoLayout)v).getImageView();
            asyncImageView.hide(null);
        }
    }

    @SuppressLint("NewApi")
    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX,
            int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        //重新定义最远滚动距离
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, mMaxOverScrollDistance,
                maxOverScrollY, isTouchEvent);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
    	if(mPhotoPointList == null) {
    		return true;
    	}
        switch (ev.getAction() & MotionEvent.ACTION_MASK) {
        case MotionEvent.ACTION_UP:
            //手指离开100微妙后发出停止滚动消息
            Message msg = Message.obtain(mHandler, ALBUM_FINGER_UP, (int) ev.getRawX());
            mHandler.sendMessageDelayed(msg, 100);
            break;
        }
        return super.onTouchEvent(ev);
    }

    public void setSinglePhotoHighLight(int index) {
        for (int i = 0; i < mLayout.getChildCount(); i++) {
            if (i == index) {
                ((SinglePhotoLayout) mLayout.getChildAt(i)).setHighLight(true);
            } else if (((SinglePhotoLayout) mLayout.getChildAt(i)).mIsHighlighted) {
                ((SinglePhotoLayout) mLayout.getChildAt(i)).setHighLight(false);
            }
        }
        if (index != 0) {
            mInitialPosition = index;
        }
    }

    private void scrollToInitialPosition() {
        if (mInitialPosition != 0) {
            if (mInitialPosition == 1) {
                scrollTo(mFirstWidth, 0);
            } else {
                int length = mFirstWidth + (mInitialPosition - 1) * mSecondWidth;
                scrollTo(length, 0);
            }
            mInitialPosition = 0;
        }
    }

    class SinglePhotoLayoutOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            for (int j = 0; j < mLayout.getChildCount(); j++) {
                if (v == mLayout.getChildAt(j)) {
                	((SinglePhotoLayout) mLayout.getChildAt(j)).setHighLight(true);
                    if(mListener != null) {
                    String pid = mPhotoPointList.get(j).get("pid");
                    mListener.onItemClick(pid);
                    }
                } else {
                    if (((SinglePhotoLayout) mLayout.getChildAt(j)).mIsHighlighted) {
                        ((SinglePhotoLayout) mLayout.getChildAt(j)).setHighLight(false);
                    }
                }
            }
        }
    }
}
