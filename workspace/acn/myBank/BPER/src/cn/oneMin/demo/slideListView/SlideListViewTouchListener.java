package cn.oneMin.demo.slideListView;

import it.gruppobper.ams.android.bper.R;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

public class SlideListViewTouchListener implements View.OnTouchListener {

	/** ��Ļ��� */
	private float screenWidth = 1;

	/** ���һ�δ�������� */
	private float lastMotionX = 0;
	private float lastMotionY = 0;

	/** �����ƶ���·�� */
	private float moveX = 0;
	/** �Զ����е��ٶ� */
	private long animationTime = 200;

	/** �����ж�Y��Ļ��� */
	private int touchSlop = 0;

	/** ��ֱ���� */
	private boolean isScrollInY = false;

	private SlideListView slideListView = null;
//	HeaderViewListAdapter mHeaderViewListAdapte
	private HeaderViewListAdapter slideAdapter = null;

	private View itemView = null;
	private View showView = null;
	private View hideView = null;

	/** ���ڰ��µ�λ�� */
	private int downPosition = 2;

	private Rect rect = new Rect();

	private Button btnDelete = null;

	// private List list=null;

	public SlideListViewTouchListener(final SlideListView slideListView, int touchSlop) {
		this.slideListView = slideListView;
		this.touchSlop = touchSlop;
	}

	int cuId = 0;
	boolean move;
	@Override
	public boolean onTouch(View view, MotionEvent motionEvent) {
//		try {
			if (screenWidth < 2) {
				screenWidth = slideListView.getWidth();
			}
			slideAdapter=(HeaderViewListAdapter) slideListView.getAdapter();
			
//			slideAdapter = (SlideAdapter)mHeaderViewListAdapter.getWrappedAdapter();// slideListView.getAdapter();
			if (slideAdapter.getCount() <= 0) {
				return false;
			}
			switch (motionEvent.getActionMasked()) {
			case MotionEvent.ACTION_DOWN: {
				move=false;
				cuId = -1;
				lastMotionX = motionEvent.getRawX();
				lastMotionY = motionEvent.getRawY();
				final int position = getPosition();
				if(position<2){
					return false;
				}
				final int firstVisiblePosition = slideListView.getFirstVisiblePosition();
				final int lastVisiblePosition = slideListView.getLastVisiblePosition();
				if ((downPosition != position) && (downPosition >= firstVisiblePosition)
						&& (downPosition <= lastVisiblePosition)) {
					int itemp=downPosition - firstVisiblePosition;
					itemView = slideListView.getChildAt(itemp);
					showView = itemView.findViewById(R.id.show_item);
					hideView = itemView.findViewById(R.id.hide_item);
					ViewPropertyAnimator.animate(showView).translationX(0).setDuration(200);
					ViewPropertyAnimator.animate(hideView).translationX(0).setDuration(200);
					moveX = 0;
				}
				downPosition = position;
				itemView = slideListView.getChildAt(downPosition - slideListView.getFirstVisiblePosition());
				btnDelete = (Button) itemView.findViewById(R.id.btn_delete);
				btnDelete.setClickable(false);
				break;
			}
			case MotionEvent.ACTION_UP: {
				boolean isClick = isClick(motionEvent.getRawX(), motionEvent.getRawY());
				if(!isClick){
					return false;
				}
				if(!move){
					int item=downPosition - slideListView.getFirstVisiblePosition();
					if(item>=2){
					itemView = slideListView.getChildAt(downPosition - slideListView.getFirstVisiblePosition());
					showView = itemView.findViewById(R.id.show_item);
					hideView = itemView.findViewById(R.id.hide_item);
					ViewPropertyAnimator.animate(showView).translationX(0).setDuration(200);
					ViewPropertyAnimator.animate(hideView).translationX(0).setDuration(200);
					SlideAdapter mSlideAdapter=(SlideAdapter)slideAdapter.getWrappedAdapter();
					mSlideAdapter.onItemClick(itemView, downPosition);
					}
//					slideAdapter.onItemClick(itemView, downPosition);
					return false;
				}
				if (isScrollInY == true || downPosition == -1) {
					break;
				} else {
					final int hideViewWidth = hideView.getWidth();
					final float translationX = ViewHelper.getTranslationX(showView);
					float deltaX = 0;

					if (translationX == -hideViewWidth) {
						animationTime = 0;
					} else {
						animationTime = 200;
					}

					if (translationX > -hideViewWidth / 2) {
						deltaX = 0;
					} else if (translationX <= -hideViewWidth / 2) {
						deltaX = -hideViewWidth;
					}
					moveX += deltaX;
					if ((moveX >= 0) || (deltaX == 0)) {
						moveX = 0;
					} else if (moveX <= -hideViewWidth) {
						moveX = -hideViewWidth;
					}
					/** �Զ����� */
					ViewPropertyAnimator.animate(showView).translationX(deltaX).setDuration(animationTime);
					ViewPropertyAnimator.animate(hideView).translationX(deltaX).setDuration(animationTime)
							.setListener(new AnimatorListener() {
								@Override
								public void onAnimationStart(Animator animation) {
								}

								/** ��������ʱ���ܵ��ɾ��ť */
								@Override
								public void onAnimationEnd(Animator animation) {
									setBtnOnClickListener();
								}

								@Override
								public void onAnimationCancel(Animator animation) {
								}

								@Override
								public void onAnimationRepeat(Animator animation) {
								}
							});

				}
				cuId = downPosition - slideListView.getFirstVisiblePosition();
				break;
			}
			case MotionEvent.ACTION_MOVE: {
				if (downPosition == -1) {
					break;
				}
				float deltaX = motionEvent.getRawX() - lastMotionX;
				isScrollInY = IsMovingInY(motionEvent.getRawX(), motionEvent.getRawY());
				if (isScrollInY) {
					return false;
				}
				itemView = slideListView.getChildAt(downPosition - slideListView.getFirstVisiblePosition());
				if(itemView==null){
					return false;
				}
				showView = itemView.findViewById(R.id.show_item);
				hideView = itemView.findViewById(R.id.hide_item);
				final int hideViewWidth = hideView.getWidth();
				deltaX += moveX;
				if (deltaX >= 0) {
					deltaX = 0;
				}
				if (deltaX <= -hideViewWidth) {
					deltaX = -hideViewWidth;
				}
				move=true;
				ViewHelper.setTranslationX(showView, deltaX);
				ViewHelper.setTranslationX(hideView, deltaX);
				return true;
			}
			default: {
				return true;
			}
			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return false;
	}

	/**
	 * ����Ƿ��Ǻ��򻬶�
	 * 
	 * @param x
	 *            Position X
	 * @param y
	 *            Position Y
	 */
	private boolean IsMovingInY(float x, float y) {
		final int xDiff = (int) Math.abs(x - lastMotionX);
		final int yDiff = (int) Math.abs(y - lastMotionY);
		final int touchSlop = this.touchSlop;
		if (xDiff == 0) {
			return true;
		}
		if ((yDiff / xDiff >= 1) && (yDiff > touchSlop)) {
			return true;
		} else {
			return false;
		}
	}
	private boolean isClick(float x, float y){
		final int xDiff = (int) Math.abs(x - lastMotionX);
		final int yDiff = (int) Math.abs(y - lastMotionY);
		final int touchSlop = this.touchSlop;
		if (xDiff == 0&&yDiff==0) {
			return true;
		}
		return false;
	}

	/**
	 * ��ȡ��ǰ��ָ���ڵ�position
	 */
	private int getPosition() {
		final int childCount = slideListView.getChildCount();
		int[] listViewCoords = new int[2];
		slideListView.getLocationOnScreen(listViewCoords);
		final int x = (int) lastMotionX - listViewCoords[0];
		final int y = (int) lastMotionY - listViewCoords[1];
		View child;
		int childPosition = 0;
		for (int i = 0; i < childCount; i++) {
			child = slideListView.getChildAt(i);
			child.getHitRect(rect);
			childPosition = slideListView.getPositionForView(child);
			if (rect.contains(x, y)) {
				return childPosition;
			}
		}
		return childPosition;
	}

	public View onTapDown(float eY, ListView listview) {
		// float eY = ev.getY();
		Rect r = new Rect();
		/*
		 * 获取第一个可见item相对于listview的可见区域,
		 * 注意getChildAt(0)得到的是listview这个ViewGroup中的第一个子view,
		 * 如果listview的adapter中getView进行了优化， 则listview的子view个数是屏幕可最多显示的item个数，
		 * 此时得到的子view并不一定对应adapter数据的第一项。
		 */
		listview.getChildAt(0).getGlobalVisibleRect(r);
		/* 这里得到的position是和list数据列表真实对应的 */
		int first = listview.getFirstVisiblePosition();
		int count = 0;
		int index_of_childview = 0;

		/* 第一个可见项是listview header */
		if (0 == first) {
			if (eY > r.height()) {
				/* 触摸不在listview header上，根据触摸的Y坐标和listitem的高度计算索引 */
				count = (int) ((eY - r.height()) / listview.getChildAt(1).getHeight());
				index_of_childview = count + 1;
			} else {
				/* 触摸在listview header上 */
				return null;
			}
		}
		/* 第一个可见项不是listview header */
		else {
			if (eY > r.height()) {
				/* 用触摸点坐标和item高度相除来计算索引 */
				count = (int) ((eY - r.height()) / listview.getChildAt(1).getHeight());
				index_of_childview = count + 1;
			} else {
				index_of_childview = 0;
			}
		}
		/* 这即是触摸的那个list item view. */
		ViewGroup child_view = (ViewGroup) listview.getChildAt(index_of_childview);
		/* index_in_adapter即是触摸的子item view在adapter中对应的数据项 */
		/* index_of_childview即是触摸的item view在listview中的索引 */
		return child_view;
	}

	public void hide() {
		itemView = slideListView.getChildAt(downPosition - slideListView.getFirstVisiblePosition());
		showView = itemView.findViewById(R.id.show_item);
		hideView = itemView.findViewById(R.id.hide_item);
		ViewPropertyAnimator.animate(showView).translationX(0).setDuration(200);
		ViewPropertyAnimator.animate(hideView).translationX(0).setDuration(200).setListener(new AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {
			}

			/** ��������ʱ���ܵ��ɾ��ť */
			@Override
			public void onAnimationEnd(Animator animation) {
//				slideAdapter.onHideClick(cuId);
//				slideAdapter.notifyDataSetChanged();
				((SlideAdapter)slideAdapter.getWrappedAdapter()).onHideClick(cuId);
				((SlideAdapter)slideAdapter.getWrappedAdapter()).notifyDataSetChanged();
				slideListView.invalidate();
			}

			@Override
			public void onAnimationCancel(Animator animation) {
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}
		});
		moveX = 0;
	}

	/**
	 * ����ɾ��ť�ļ���
	 * 
	 * @param
	 */
	private void setBtnOnClickListener() {
		// itemView=slideListView.getChildAt(downPosition-slideListView.getFirstVisiblePosition());
		// btnDelete.findViewById(R.id.btn_delete);
		btnDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				hide();
			}
		});
	}

}
