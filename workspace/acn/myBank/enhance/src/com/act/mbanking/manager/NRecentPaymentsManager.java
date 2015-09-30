
package com.act.mbanking.manager;

import android.content.Intent;
import android.view.View;

import com.act.mbanking.R;
import com.act.mbanking.activity.MainActivity;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

/**
 * @author junxu.wang
 */
public class NRecentPaymentsManager extends MainMenuSubScreenManager {

    private RecentPaymentManager rpm;
    
    private GoogleAnalytics mGaInstance;
    
  	private Tracker mGaTracker1;

    public NRecentPaymentsManager(MainActivity activity) {
        super(activity);
    }

    @Override
    protected void loadData() {
//        pm.initData();
    }

    @Override
    protected void setUI() {
    }

    protected void onShow(Object object) {
    	lastMainMenuSubScreenManager=mainManager.getLastView();
        onCreate(object);
    }

    @Override
    protected void init() {
        rpm = new RecentPaymentManager(activity);
        layout = rpm.init();
//        pm.getLayout();
        setLeftNavigationText(R.string.back);
    }

    public boolean onLeftNavigationButtonClick(View v) {
        if (!rpm.onLeftNavigationButtonClick(v)) {
            	if(lastMainMenuSubScreenManager!=null){
            		mainManager.showView(lastMainMenuSubScreenManager,true, null);
            	}else{
            		mainManager.showAggregatedView(true, null);
            	}
        }
        return true;
    }


    protected void onCreate(Object obj) {
//        EasyTracker.getInstance().activityStart(this); // Add this method.
        rpm.onShow(null);
    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent intent) {
    	return false;
//        return rpm.onActivityResult(requestCode, resultCode, intent);
    }
    public boolean onDestroyed(){
    	return false;
//    	return rpm.onDestroyed();
    }
}
