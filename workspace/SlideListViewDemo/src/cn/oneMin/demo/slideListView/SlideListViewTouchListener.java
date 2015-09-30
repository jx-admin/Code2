package cn.oneMin.demo.slideListView;

import java.util.List;

import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.android.wu.slidelistview.R;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;




public class SlideListViewTouchListener implements View.OnTouchListener{

	/**屏幕宽度*/
	private float screenWidth=1;
	
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
    
	private SlideListView slideListView=null;
	private SlideAdapter slideAdapter=null;
	
	private View itemView=null;        		
	private View showView=null;        		
	private View hideView=null;
	
	/**现在按下的位置*/
	private int downPosition=0;
		
	private Rect rect = new Rect();
	
	private Button btnDelete=null;
	private List<Info> list=null;

	public SlideListViewTouchListener(SlideListView slideListView,int touchSlop){
		this.slideListView=slideListView;
		this.touchSlop=touchSlop;
	}
	
	
	@Override
	public boolean onTouch(View view, MotionEvent motionEvent) {
		if (screenWidth < 2) {
            screenWidth = slideListView.getWidth();
        }
	    slideAdapter=(SlideAdapter)slideListView.getAdapter();
		switch (motionEvent.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: { 
            	lastMotionX = motionEvent.getRawX();
            	lastMotionY = motionEvent.getRawY();
            	final int position=getPosition();
            	final int firstVisiblePosition=slideListView.getFirstVisiblePosition();
            	final int lastVisiblePosition=slideListView.getLastVisiblePosition();
            	if((downPosition!=position) && (downPosition>=firstVisiblePosition) && (downPosition<=lastVisiblePosition)){            	
            		itemView=slideListView.getChildAt(downPosition-firstVisiblePosition);        		
            		showView=itemView.findViewById(R.id.show_item);        		
            		hideView=itemView.findViewById(R.id.hide_item);
            		ViewPropertyAnimator.animate(showView)
     		       .translationX(0)
     		       .setDuration(200);
     		        ViewPropertyAnimator.animate(hideView)
     		       .translationX(0)
     		       .setDuration(200);    		      
     		       moveX=0;
            	} 
            	downPosition=position; 
            	itemView=slideListView.getChildAt(downPosition-slideListView.getFirstVisiblePosition());   	
        		btnDelete=(Button)itemView.findViewById(R.id.btn_delete);
              	btnDelete.setClickable(false);
            	break;      
            }        
            case  MotionEvent.ACTION_UP:{ 
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
            		return false;
            	}
            	itemView=slideListView.getChildAt(downPosition-slideListView.getFirstVisiblePosition());        		
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
    
    /**
     *获取当前手指所在的position
     */    
    private int getPosition(){
    	final int childCount = slideListView.getChildCount();
    	int[] listViewCoords = new int[2];
    	slideListView.getLocationOnScreen(listViewCoords);
        final int x = (int) lastMotionX - listViewCoords[0];
        final int y = (int) lastMotionY - listViewCoords[1];
        View child;
        int childPosition=0;
    	for(int i=0;i<childCount;i++){
    		child = slideListView.getChildAt(i);
            child.getHitRect(rect);
			childPosition = slideListView.getPositionForView(child);
			if (rect.contains(x, y)) {
				return childPosition;
			}            		
    	}
    	return childPosition;
    }
    
    /**
     *设置删除按钮的监听
     *@param 
     */  
    private void setBtnOnClickListener(){
    	itemView=slideListView.getChildAt(downPosition-slideListView.getFirstVisiblePosition());
    	btnDelete.findViewById(R.id.btn_delete);
    	btnDelete.setOnClickListener(new OnClickListener() {					
    		@Override			
    		public void onClick(View v) {				
    			// 删除	            
    			moveX=0;			
    			list=slideAdapter.getInfoList();            
    			list.remove(downPosition);            			
    			slideAdapter.setInfoList(list);			
    			slideAdapter.notifyDataSetChanged();			
    		}		
    	});   	    
    }

}
