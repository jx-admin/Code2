package cn.oneMin.demo.slideListView;


import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewParent;
import android.widget.Button;
import cn.oneMin.demo.slideListView.ItemSlideAdapter.ViewHolder;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;


public class ItemSlideTouchListener implements View.OnTouchListener{

	/**最后一次触发的坐标*/
    private float lastMotionX=0;
    private float lastMotionY=0;
    
    /**横向移动的路径*/
    private float moveX=0;
    /**自动滑行的速度*/
    private long animationTime=200;
    
    /**用来判定Y轴的滑动*/
    private int touchSlop=0;    
    
    /**垂直滑动*/
	private boolean isScrollInY=false;
    
	private View itemView=null;        		
	private View showView=null;        		
	private View hideView=null;
	
	/**现在按下的位置*/
	private int downPosition=0;
		
	private Button btnDelete=null;

	

	int cuId = 0;
	boolean move;
	@Override
	public boolean onTouch(View view, MotionEvent motionEvent) {
		switch (motionEvent.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: { 
				move=false;
            	 ViewParent  mParent = view.getParent();
                 if (mParent != null) {
                     mParent.requestDisallowInterceptTouchEvent(true);
                 }
            	lastMotionX = motionEvent.getRawX();
            	lastMotionY = motionEvent.getRawY();
            	ViewHolder mViewHolder=(ViewHolder) view.getTag();
            	final int position=mViewHolder.position;//getPosition();
            	if(itemView!=null){
            		ViewHolder lastHolder=(ViewHolder) itemView.getTag();
            		if(lastHolder.position==downPosition){
            			showView=itemView.findViewById(R.id.show_item);        		
            			hideView=itemView.findViewById(R.id.hide_item);
            			ViewPropertyAnimator.animate(showView)
            			.translationX(0)
            			.setDuration(100);
            			ViewPropertyAnimator.animate(hideView)
            			.translationX(0)
            			.setDuration(100).setListener(new AnimatorListener(){						
         		    	   @Override						
         		    	   public void onAnimationStart(Animator animation) {							
         		    		   // TODO Auto-generated method stub													
         		    	   }                        
         		    	   /**动画结束时才能点击删除按钮*/						
         		    	   @Override						
         		    	   public void onAnimationEnd(Animator animation) {							
         		    		   // TODO Auto-generated method stub							
         		    		  moveX=0;				
         		    	   }
         		    	  						
         		    	   @Override						
         		    	   public void onAnimationCancel(Animator animation) {							
         		    		   // TODO Auto-generated method stub						
 						   }

 						   @Override
 						   public void onAnimationRepeat(Animator animation) {
 							   // TODO Auto-generated method stub
 							
 						   }        		    							
         		       });    		      
            			
            		}
            	} 
            	downPosition=position; 
            	itemView=view;//slideListView.getChildAt(downPosition-slideListView.getFirstVisiblePosition());   	
        		btnDelete=(Button)itemView.findViewById(R.id.btn_delete);
              	btnDelete.setClickable(false);
        		return true;      
            }        
            case  MotionEvent.ACTION_UP:{ 
            	boolean isClick = isClick(motionEvent.getRawX(), motionEvent.getRawY());
				if(isClick&&!move){
					showView = itemView.findViewById(R.id.show_item);
					hideView = itemView.findViewById(R.id.hide_item);
					ViewPropertyAnimator.animate(showView).translationX(0).setDuration(200);
					ViewPropertyAnimator.animate(hideView).translationX(0).setDuration(200);
					if(mOnSlideItemClickListener!=null){
	    				mOnSlideItemClickListener.onItemClick(itemView, downPosition);
	    			}
					return false;
				}
            	if(isScrollInY==true || downPosition==-1){
					break;
				}
            	else{
            	    final int hideViewWidth=hideView.getWidth();
            	    final float translationX=ViewHelper.getTranslationX(showView);
            	    float deltaX=0;
            	    
            	    if(translationX==-hideViewWidth){
            	    	animationTime=0;
            	    }
            	    else{
            	    	animationTime=200;
            	    }
            	    
            	    if(translationX > -hideViewWidth/2){            		   
            	    	deltaX=0;
            	    }
            	    else if(translationX<=-hideViewWidth/2){
            		    deltaX=-hideViewWidth;
            	    }
            	    moveX+=deltaX;
            	    if((moveX>=0) || (deltaX==0)){
            	    	moveX=0;
            	    }
            	    else if(moveX<=-hideViewWidth){
            	    	moveX=-hideViewWidth;
            	    }
            	    /**自动滑行*/
            	    ViewPropertyAnimator.animate(showView)
        		       .translationX(deltaX)
        		       .setDuration(animationTime);
        		    ViewPropertyAnimator.animate(hideView)
        		       .translationX(deltaX)
        		       .setDuration(animationTime)
        		       .setListener(new AnimatorListener(){						
        		    	   @Override						
        		    	   public void onAnimationStart(Animator animation) {							
        		    		   // TODO Auto-generated method stub													
        		    	   }                        
        		    	   /**动画结束时才能点击删除按钮*/						
        		    	   @Override						
        		    	   public void onAnimationEnd(Animator animation) {							
        		    		   // TODO Auto-generated method stub							
        		    		   setBtnOnClickListener();						
        		    	   }
        		    	  						
        		    	   @Override						
        		    	   public void onAnimationCancel(Animator animation) {							
        		    		   // TODO Auto-generated method stub						
						   }

						   @Override
						   public void onAnimationRepeat(Animator animation) {
							   // TODO Auto-generated method stub
							
						   }        		    							
        		       });
        		            		       		           		   
            	}
            	break;        
            }
            case  MotionEvent.ACTION_MOVE:{ 
            	if(downPosition==-1){
            		break;
            	}         		
            	float deltaX = motionEvent.getRawX() - lastMotionX;
            	isScrollInY=IsMovingInY(motionEvent.getRawX(),motionEvent.getRawY());
            	if(isScrollInY){
            		ViewParent  mParent = view.getParent();
                    if (mParent != null) {
                        mParent.requestDisallowInterceptTouchEvent(false);
                    }
            		return false;
            	}
            	itemView=view;//slideListView.getChildAt(downPosition-slideListView.getFirstVisiblePosition());        		
        		showView=itemView.findViewById(R.id.show_item);        		
        		hideView=itemView.findViewById(R.id.hide_item);
            	final int hideViewWidth=hideView.getWidth();
            	deltaX+=moveX;
            	if(deltaX>=0){
            		deltaX=0;
            	}
            	if(deltaX<=-hideViewWidth)
            	{
            		deltaX=-hideViewWidth;
            	}

				move=true;
            	ViewHelper.setTranslationX(showView, deltaX); 
            	ViewHelper.setTranslationX(hideView, deltaX);
            	return true;            	
            }
            default:{
            	return true;
            }
		}
		return false;
	}

	/**
     *检测是否是横向滑动
     * @param x Position X
     * @param y Position Y
     */
    private boolean IsMovingInY(float x,float y) {
    	final int xDiff = (int) Math.abs(x- lastMotionX);
        final int yDiff = (int) Math.abs(y- lastMotionY);
        final int touchSlop = this.touchSlop;
        if(xDiff==0)
        {
        	return true;
        }
        if((yDiff/xDiff>=1) && (yDiff>touchSlop)){
        	return true;
        }
        else{
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
     *获取当前手指所在的position
     */    
//    private int getPosition(){
//    	final int childCount = slideListView.getChildCount();
//    	int[] listViewCoords = new int[2];
//    	slideListView.getLocationOnScreen(listViewCoords);
//        final int x = (int) lastMotionX - listViewCoords[0];
//        final int y = (int) lastMotionY - listViewCoords[1];
//        View child;
//        int childPosition=0;
//    	for(int i=0;i<childCount;i++){
//    		child = slideListView.getChildAt(i);
//            child.getHitRect(rect);
//			childPosition = slideListView.getPositionForView(child);
//			if (rect.contains(x, y)) {
//				return childPosition;
//			}            		
//    	}
//    	return childPosition;
//    }
    
    /**
     *设置删除按钮的监听
     *@param 
     */  
    private void setBtnOnClickListener(){
//    	itemView=slideListView.getChildAt(downPosition-slideListView.getFirstVisiblePosition());
    	btnDelete.findViewById(R.id.btn_delete);
    	btnDelete.setClickable(true);
    	btnDelete.setOnClickListener(new OnClickListener() {					
    		@Override			
    		public void onClick(View v) {

    			if(mOnSlideItemClickListener!=null){
    				mOnSlideItemClickListener.onHideClick(downPosition);
    			}
    			// 删除	            
//    			moveX=0;			
//    			list=slideAdapter.getInfoList();            
//    			list.remove(downPosition);            			
//    			slideAdapter.setInfoList(list);			
//    			slideAdapter.notifyDataSetChanged();
    			
    			ViewHolder lastHolder=null;
    			if(itemView!=null){
    				lastHolder=(ViewHolder) itemView.getTag();
    			}
    			if(lastHolder!=null&&lastHolder.position==downPosition){
    				showView=itemView.findViewById(R.id.show_item);        		
    				hideView=itemView.findViewById(R.id.hide_item);
    				ViewPropertyAnimator.animate(showView)
    				.translationX(0)
    				.setDuration(200);
    				ViewPropertyAnimator.animate(hideView)
    				.translationX(0)
    				.setDuration(200);    		      
    				moveX=0;
    			}else{
    				moveX=0;
    			}
    		}		
    	});   	    
    }

    OnSlideItemClickListener mOnSlideItemClickListener;
	public interface OnSlideItemClickListener{
		public void onItemClick(View view, int position);
		public void onHideClick(int position);
	}
	
	public void setOnSlideItemClickListener(OnSlideItemClickListener monSlideItemClickListener){
		this.mOnSlideItemClickListener=monSlideItemClickListener;
	}
}
