
package com.act.mbanking.manager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.act.mbanking.Contants;
import com.act.mbanking.R;
import com.act.mbanking.activity.MainActivity;
import com.act.mbanking.bean.AccountsModel;
import com.act.mbanking.bean.DestaccountModel;
import com.act.mbanking.bean.PendingTransferModel;
import com.act.mbanking.bean.RecentTransferModel;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

/**
 * @author junxu.wang
 */
public class NPaymentsManager extends MainMenuSubScreenManager {

    private PaymentsManager pm;
    
    private GoogleAnalytics mGaInstance;
    
  	private Tracker mGaTracker1;

    public NPaymentsManager(MainActivity activity) {
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
        onCreate((Intent)object);
    }

    @Override
    protected void init() {
        pm = new PaymentsManager(activity);
        layout = pm.init();
//        pm.getLayout();
        setLeftNavigationText(R.string.back);
    }

    public boolean onLeftNavigationButtonClick(View v) {
        if (!pm.onLeftNavigationButtonClick(v)) {
            int showForm=pm.getShowForm();
            if(showForm==PaymentsManager.FORM_ACCOUNT){
                mainManager.showAccountDetails(true, null);
            }else if(showForm==PaymentsManager.FORM_RECENT_PAYMENT){
                mainManager.showRecentPayments(true, null);
            }else{
            	if(lastMainMenuSubScreenManager!=null){
            		mainManager.showView(lastMainMenuSubScreenManager,true, null);
            	}else{
            		mainManager.showAggregatedView(true, null);
            	}
            }
        }
        return true;
    }


    /**
     * @see PaymentsManager#setRecover
     */
    public static void startForRecover(MainManager mainManager, int showForm, AccountsModel payer,
            DestaccountModel payee, long time, double amount, int paymentType) {
        Intent intent = new Intent();
        intent.putExtra("payer", payer);
        intent.putExtra("showForm", showForm);
        intent.putExtra("payee", payee);
        intent.putExtra("time", time);
        intent.putExtra("amount", amount);
        intent.putExtra("paymentType", paymentType);
        mainManager.showNewPayment(false, intent);
    }

    /**
     * @see PaymentsManager#setRecover
     */
    public static void startForRecover(MainManager mainManager, RecentTransferModel recentTransferModel,
            AccountsModel accountModel) {
        Intent intent = new Intent();
        intent.putExtra("RecentTransferModel", recentTransferModel);
        intent.putExtra("showForm", PaymentsManager.FORM_RECENT_PAYMENT);
        intent.putExtra("payer", accountModel);
        mainManager.showNewPayment(false, intent);
    }

    /**
     * @see PaymentsManager#setRecover
     */
    public static void startForRecover(MainManager mainManager, PendingTransferModel recentTransferModel,
            AccountsModel accountModel) {
        Intent intent = new Intent();
        intent.putExtra("PendingTransferModel", recentTransferModel);
        intent.putExtra("showForm", PaymentsManager.FORM_PENDING_PAYMENT);
        intent.putExtra("payer", accountModel);
        mainManager.showNewPayment(false, intent);
    }
    protected void onCreate(Intent intent ) {
    	
//        pm.init();
        if(intent!=null){
        	int showForm = intent.getIntExtra("showForm", PaymentsManager.FORM_DEFAULT);
        if (showForm == PaymentsManager.FORM_ACCOUNT) {
            AccountsModel payer = (AccountsModel)intent.getSerializableExtra("payer");
            DestaccountModel payee = (DestaccountModel)intent.getSerializableExtra("payee");
            long time = intent.getLongExtra("time", 0);
            double amount = intent.getDoubleExtra("amount", 0);
            int paymentType = intent.getIntExtra("paymentType", 0);
            pm.setRecover(showForm, payer, payee, time, amount, paymentType);


        	mGaInstance = GoogleAnalytics.getInstance(activity);
            mGaTracker1 = mGaInstance.getTracker(Contants.TRACKER_ID);
            mGaTracker1.sendView("event.balance.transaction.payment"); 
        } else if (showForm == PaymentsManager.FORM_RECENT_PAYMENT) {
            RecentTransferModel recentTransferModel = (RecentTransferModel)intent
                    .getSerializableExtra("RecentTransferModel");
            AccountsModel payer = (AccountsModel)intent.getSerializableExtra("payer");
            pm.setRecentRecover(showForm, recentTransferModel, payer);

        	mGaInstance = GoogleAnalytics.getInstance(activity);
            mGaTracker1 = mGaInstance.getTracker(Contants.TRACKER_ID);
            mGaTracker1.sendView("event.recent.payment.recover"); 
        }else if (showForm == PaymentsManager.FORM_PENDING_PAYMENT) {
        	PendingTransferModel recentTransferModel = (PendingTransferModel)intent
            .getSerializableExtra("PendingTransferModel");
		    AccountsModel payer = (AccountsModel)intent.getSerializableExtra("payer");
		    pm.setRecentRecover(showForm,recentTransferModel,payer);
		}else{
			pm.setShowForm(showForm);
        	mGaInstance = GoogleAnalytics.getInstance(activity);
        	mGaTracker1 = mGaInstance.getTracker(Contants.TRACKER_ID);
        	mGaTracker1.sendView("view.newpayment"); 

        }
        }else{
        	pm.setShowForm(PaymentsManager.FORM_DEFAULT);
        	mGaInstance = GoogleAnalytics.getInstance(activity);
        	mGaTracker1 = mGaInstance.getTracker(Contants.TRACKER_ID);
        	mGaTracker1.sendView("view.newpayment"); 
        }
        pm.onShow(this);
        pm.initData();
    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent intent) {
        return pm.onActivityResult(requestCode, resultCode, intent);
    }
    public boolean onDestroyed(){
    	return pm.onDestroyed();
    }
}
