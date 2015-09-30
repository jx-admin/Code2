package com.accenture.mbank.view;


import com.accenture.mbank.ContactSearchActivity;
import com.accenture.mbank.MainActivity;
import com.accenture.mbank.util.LogManager;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class TouchableWrapper extends FrameLayout {

	private Context context;
	private boolean click = false;
	public TouchableWrapper(Context context) {
    super(context);
    this.context= context;
  }

  @Override
  public boolean dispatchTouchEvent(MotionEvent event) {
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
    	  click= true;
    	  if (this.context instanceof ContactSearchActivity) {
			final View searchPoint = ((ContactSearchActivity)(this.context)).getSearchPoint();
			searchPoint.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (!((ContactSearchActivity)(TouchableWrapper.this.context)).isPopupVisible()) {
						searchPoint.setVisibility(View.VISIBLE);
					}
					
				}
			}, 1000);
			
		} else if (this.context instanceof MainActivity) {
			final View searchPoint = ((MainActivity)(this.context)).getSearchPoint();
			searchPoint.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (!((MainActivity)(TouchableWrapper.this.context)).isPopupVisible()) {
						searchPoint.setVisibility(View.VISIBLE);
					}
					
				}
			}, 1000);
			
		}
            break;
      case MotionEvent.ACTION_UP:
    	  if (click) {
    		  
    		  if (this.context instanceof ContactSearchActivity) {
    			  ((ContactSearchActivity)(this.context)).setPopupVisible(false);
    			  
    	  			final View searchPoint = ((ContactSearchActivity)(this.context)).getSearchPoint();
    	  			searchPoint.postDelayed(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if (!((ContactSearchActivity)(TouchableWrapper.this.context)).isPopupVisible()) {
								searchPoint.setVisibility(View.VISIBLE);
							}
							
						}
					}, 1000);
    	  			
    	  		} else if (this.context instanceof MainActivity) {
      			  ((MainActivity)(this.context)).setPopupVisible(false);
    			  
  	  			final View searchPoint = ((MainActivity)(this.context)).getSearchPoint();
  	  			searchPoint.postDelayed(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if (!((MainActivity)(TouchableWrapper.this.context)).isPopupVisible()) {
								searchPoint.setVisibility(View.VISIBLE);
							}
							
						}
					}, 1000);
  	  			
  	  		}
    	  }
    	 
    	  break;
      case MotionEvent.ACTION_MOVE:
    	  LogManager.d( "ACTION_MOVE");
    	  click=false;
    	  break;
    }
    return super.dispatchTouchEvent(event);
  }
}