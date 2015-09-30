package com.accenture.mbank.view;

import it.gruppobper.ams.android.bper.R;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.accenture.mbank.util.LogManager;

public class QuickReturnListView extends ListView implements OnScrollListener {

	private int mItemCount;
	private int mItemOffsetY[];
	private boolean scrollIsComputed = false;
	private int mHeight;
	private View headerView;
	int headerViewHeight;
	int listViewHeight;
	private static final int STATE_ONSCREEN = 0;
	private static final int STATE_OFFSCREEN = 1;
	private static final int STATE_RETURNING = 2;
	public int mState = STATE_ONSCREEN;
	private int mScrollY;
	private int mMinRawY = 0;
	private TranslateAnimation anim;
	// 自动加载更多
	private boolean mIsAutoLoadMore = true;
	private View mEndRootView;
	private ProgressBar mEndLoadProgressBar;
	private TextView mEndLoadTipsTextView;
	private LayoutInflater mInflater;
	private boolean isFrist = true;
	public QuickReturnListView(Context context) {
		super(context);
		mInflater = LayoutInflater.from(context);
		super.setOnScrollListener(this);
		super.getViewTreeObserver().addOnGlobalLayoutListener(this);
		setFooterDividersEnabled(false);
	}

	public QuickReturnListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mInflater = LayoutInflater.from(context);
		super.setOnScrollListener(this);
		super.getViewTreeObserver().addOnGlobalLayoutListener(this);
		setFooterDividersEnabled(false);
	}

	public int getListHeight() {
		return mHeight;
	}

	public void computeScrollY() {
		mHeight = 0;
		if (getAdapter() == null) {
			return;
		}
		mItemCount = getAdapter().getCount();
		if (mItemOffsetY == null) {
			mItemOffsetY = new int[mItemCount];
		} else {
			if (mItemCount != mItemOffsetY.length) {
				mItemOffsetY = new int[mItemCount];
			}
		}
		for (int i = 0; i < mItemCount; ++i) {
			View view = getAdapter().getView(i, null, this);
			view.measure(
					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
			mItemOffsetY[i] = mHeight;
			mHeight += view.getMeasuredHeight();
		}
		
		if (mHeight < this.getHeight()) {
			mHeight = this.getHeight();
		}
		scrollIsComputed = true;
	}

	public boolean scrollYIsComputed() {
		return scrollIsComputed;
	}

	public int getComputedScrollY() {
		int pos, nScrollY, nItemY;
		View view = null;
		pos = getFirstVisiblePosition();
		view = getChildAt(0);
		nItemY = view.getTop();
		nScrollY = mItemOffsetY[pos] - nItemY;
		return nScrollY;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		mFirstItemIndex = firstVisibleItem;
		mLastItemIndex = mFirstItemIndex + visibleItemCount - 2;
		mCount = totalItemCount - 2;

		if (headerView == null) {
			return;
		}
		mScrollY = 0;
		int translationY = 0;

		if (scrollYIsComputed()) {
			mScrollY = getComputedScrollY();
		}
		
		int rawY = getHeaderView().getTop() - Math.min(listViewHeight - view.getHeight(), mScrollY);
		LogManager.d("rawY --> " + rawY);
		LogManager.d("headerViewHeight --> " + headerViewHeight);
		LogManager.d("getHeaderView() --> " + getHeaderView().getHeight());
		LogManager.d("view.getHeight() --> " + view.getHeight());
		LogManager.d("listViewHeight --> " + listViewHeight);
		LogManager.d("mMinRawY --> " + mMinRawY);
		switch (mState) {
		case STATE_OFFSCREEN:
			if (rawY <= mMinRawY) {
				mMinRawY = rawY;
			} else {
				mState = STATE_RETURNING;
			}
			translationY = rawY;
			break;

		case STATE_ONSCREEN:
			if (rawY < -headerViewHeight) {
				mState = STATE_OFFSCREEN;
				mMinRawY = rawY;
			}
			if(isFrist){
				translationY = 0;
			}else {
				translationY = rawY;
			}
			break;

		case STATE_RETURNING:
			translationY = (rawY - mMinRawY) - headerViewHeight;
			if (translationY > 0) {
				translationY = 0;
				mMinRawY = rawY - headerViewHeight;
			}

			if (rawY > 0) {
				mState = STATE_ONSCREEN;
				translationY = rawY;
			}

			if (translationY < -headerViewHeight) {
				mState = STATE_OFFSCREEN;
				mMinRawY = rawY;
			}
			break;
		}

		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.HONEYCOMB) {
			anim = new TranslateAnimation(0, 0, translationY, translationY);
			anim.setFillAfter(true);
			anim.setDuration(0);
			headerView.startAnimation(anim);
		} else {
			headerView.setTranslationY(translationY);
		}
	}

	public void translationY(int y){
		reInitialize();
		headerView.setTranslationY(0);
	}
	
	public void reInitialize(){
		mState = STATE_ONSCREEN;
		isFrist = true;
		mScrollY = 0;
		mMinRawY = 0;
		scrollTo(0, 0);
		
		/*mCount = 0;
		mFirstItemIndex = 0;
		mLastItemIndex= 0;
		mEndState= 0;*/
	}
	
	
	private int mCount;
	private int mFirstItemIndex;
	private int mLastItemIndex;
	private int mEndState;
	/** 可以加载更多？ */
	private boolean mCanLoadMore = false;
	/** 加载中 */
	private final static int ENDINT_LOADING = 1;
	private OnLoadMoreListener mLoadMoreListener;
	private final static int ENDINT_AUTO_LOAD_DONE = 3;

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		isFrist = false;
		if (mLastItemIndex == mCount && scrollState == SCROLL_STATE_IDLE) {
			if (mEndState != ENDINT_LOADING) {
				if (mIsAutoLoadMore) {// 自动加载更多，我们让FootView显示 “更 多”
					mEndState = ENDINT_LOADING;
					onLoadMore();
					changeEndViewByState();
				}
			}
		}
	}

	/**
	 * 改变加载更多状态
	 * 
	 * @date 2013-11-11 下午10:05:27
	 * @change JohnWatson
	 * @version 1.0
	 */
	private void changeEndViewByState() {
		if (mCanLoadMore) {
			// 允许加载更多
			switch (mEndState) {
			case ENDINT_AUTO_LOAD_DONE:// 自动刷新完成
				// 更 多
				mEndLoadTipsTextView.setText(R.string.p2refresh_head_load_more);
				mEndLoadTipsTextView.setVisibility(View.VISIBLE);
				mEndLoadProgressBar.setVisibility(View.GONE);
				mEndRootView.setVisibility(View.VISIBLE);
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void onGlobalLayout() {
		if (headerView == null) {
			return;
		}
		headerViewHeight = headerView.getHeight();
		this.computeScrollY();
		listViewHeight = this.getListHeight();
	}

	/**
	 * 添加加载更多FootView
	 */
	private void addFooterView() {
		mEndRootView = mInflater.inflate(R.layout.listfooter_more, null);
		mEndRootView.setVisibility(View.VISIBLE);
		mEndLoadProgressBar = (ProgressBar) mEndRootView
				.findViewById(R.id.pull_to_refresh_progress);
		mEndLoadTipsTextView = (TextView) mEndRootView
				.findViewById(R.id.load_more);
		mEndRootView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mCanLoadMore){
					if(mEndState != ENDINT_LOADING){
						// 当不能下拉刷新时，FootView不正在加载时，才可以点击加载更多。
						mEndState = ENDINT_LOADING;
						onLoadMore();
					}
				}
			}
		});
		addFooterView(mEndRootView);

		if (mIsAutoLoadMore) {
			mEndState = ENDINT_AUTO_LOAD_DONE;
		}
	}

	/**
	 * 正在加载更多，FootView显示 ： 加载中...
	 */
	private void onLoadMore() {
		if (mLoadMoreListener != null) {
			// 加载中...
			mEndLoadTipsTextView.setText(R.string.p2refresh_doing_end_refresh);
			mEndLoadTipsTextView.setVisibility(View.VISIBLE);
			mEndLoadProgressBar.setVisibility(View.VISIBLE);

			mLoadMoreListener.onLoadMore();
		}
	}

	/**
	 * 加载更多完成
	 */
	public void onLoadMoreComplete() {
		if (mIsAutoLoadMore) {
			mEndState = ENDINT_AUTO_LOAD_DONE;
		}
		changeEndViewByState();
	}

	public View getHeaderView() {
		return headerView;
	}

	public void setHeaderView(View headerView) {
		this.headerView = headerView;
	}

	public boolean isAutoLoadMore() {
		return mIsAutoLoadMore;
	}

	public void setAutoLoadMore(boolean pIsAutoLoadMore) {
		mIsAutoLoadMore = pIsAutoLoadMore;
	}

	public void setOnLoadListener(OnLoadMoreListener pLoadMoreListener) {
		if (pLoadMoreListener != null) {
			mLoadMoreListener = pLoadMoreListener;
			mCanLoadMore = true;
			mIsAutoLoadMore = true;
			if(getFooterViewsCount() != 0 && mEndRootView != null){
				removeFooterView(mEndRootView);
			}
			if (mCanLoadMore && getFooterViewsCount() == 0) {
				addFooterView();
			}
		}else {
			mIsAutoLoadMore = false;
			mCanLoadMore = false;
			if(getFooterViewsCount() == 0){
				addFooterView();
			}
			mEndLoadTipsTextView.setText(R.string.p2refresh_head_no_load_more);
		}
	}
	
	public void setMissData(boolean isNotData){
		if(isNotData){
			mEndRootView = mInflater.inflate(R.layout.miss_movement_foot_view, null);
			if(getFooterViewsCount() == 0){
				addFooterView(mEndRootView);
			}
		}
	}
	
	/**
	 * 加载更多监听接口
	 */
	public interface OnLoadMoreListener {
		public void onLoadMore();
	}
}