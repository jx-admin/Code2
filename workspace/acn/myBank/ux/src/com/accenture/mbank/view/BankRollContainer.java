
package com.accenture.mbank.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.accenture.mbank.R;
import com.accenture.mbank.util.LogManager;
import com.accenture.mbank.view.protocol.ShowAble;
import com.accenture.mbank.view.protocol.ShowAbleContainer;
import com.accenture.mbank.view.protocol.ShowListener;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

public class BankRollContainer extends InnerScrollView implements ShowListener, ShowAbleContainer {

	public static BankRollView pushNotificationRollView;
    LinearLayout rollContainer;
    
    private GoogleAnalytics mGaInstance;
    
	private Tracker mGaTracker1;

    /**
     * @return the rollContainer
     */
    public LinearLayout getRollContainer() {
        return rollContainer;
    }

    private BankRollContainerManager bankRollContainerManager;

    public BankRollContainerManager getBankRollContainerManager() {
        return bankRollContainerManager;
    }

    public void setBankRollContainerManager(BankRollContainerManager bankRollContainerManager) {
        this.bankRollContainerManager = bankRollContainerManager;
    }

    public BankRollContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init() {
        rollContainer = (LinearLayout)findViewById(R.id.roll_container);
    }

    public void addShowAble(ShowAble showAble) {
        if (showAble instanceof View) {

            rollContainer.addView((View)showAble);
            showAble.setShowListener(this);
        }

    }

    private void closeAllViewExcept(View v) {
 
 
        for (int i = 0; i < rollContainer.getChildCount(); i++) {

            View child = rollContainer.getChildAt(i);
            if (child instanceof ShowAble) {

                if (child != v) {
                	
                    ShowAble showAble = (ShowAble)child;
                    showAble.close();
                }

            }

        }
    }

    public void show(int index) {
    	mGaInstance = GoogleAnalytics.getInstance(getContext());
   	    mGaTracker1 = mGaInstance.getTracker("UA-42551791-1");
        BankRollView show = (BankRollView)getShowAble(index);
        if( ((show.getContent()).getId()) == R.layout.contact_us_layout){        	 
             mGaTracker1.sendView("view.contact.us"); 
    	} else if( ((show.getContent()).getId()) == R.layout.search_width_map){   
             mGaTracker1.sendView("view.branch");
    	}
        closeAllViewExcept(show);
        show.show();
        scrollTo(show);
    }

    public ShowAble getShowAble(int index) {

        if (index >= rollContainer.getChildCount()) {
        } else {
            return (ShowAble)rollContainer.getChildAt(index);

        }
        return null;
    }

    public int getBankRollViewCount() {
        return rollContainer.getChildCount();
    }

    public void animateToShowAble(ShowAble showAble) {
        if (showAble instanceof View) {
            View v = (View)showAble;
            closeAllViewExcept(v);

            scrollTo(v);
        }
    }

    @Override
    public void onShow(final ShowAble showAble) { 
        animateToShowAble(showAble);
        if (bankRollContainerManager != null) {
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    bankRollContainerManager.onShow(showAble);
                }
            }, 300);

        }
    }

    @Override
    public void scrollTo(int x, int y) {
        // TODO Auto-generated method stub

        super.scrollTo(x, y);
    }

    @Override
    public void scrollBy(int x, int y) {
        // TODO Auto-generated method stub

        int scrollY = getScrollY();
        if (y > 0) {
            if (maxScrollY == -1 || scrollY < maxScrollY) {

            } else {
                return;
            }
        }
        super.scrollBy(x, y);
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY,
            int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY,
            boolean isTouchEvent) {
        if (deltaY > 0) {
            if (maxScrollY == -1 || scrollY < maxScrollY) {

            } else {
                return false;
            }
        }
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY,
                maxOverScrollX, maxScrollY, isTouchEvent);

    }

    int maxScrollY = -1;

    public void setMaxScrollY(int y) {
        maxScrollY = y;
    }
}
