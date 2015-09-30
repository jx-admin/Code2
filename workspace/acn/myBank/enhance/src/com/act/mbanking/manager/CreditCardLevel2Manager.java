
package com.act.mbanking.manager;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.act.mbanking.Contants;
import com.act.mbanking.R;
import com.act.mbanking.ServiceCode;
import com.act.mbanking.activity.MainActivity;
import com.act.mbanking.bean.AccountsModel;
import com.act.mbanking.bean.AggregatedAccount;
import com.act.mbanking.bean.DashboardDataModel;
import com.act.mbanking.manager.TimeSelectorManager.OnTimeItemSelectedListener;
import com.act.mbanking.manager.view.TimeItem.TimeText;
import com.act.mbanking.utils.TimeUtil;
import com.act.mbanking.utils.Utils;
import com.custom.view.CircleLinearView;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

public class CreditCardLevel2Manager extends MainMenuSubScreenManager implements
        OnTimeItemSelectedListener, OnClickListener {
    LayoutInflater lf;

    Button sureBtn;

    private Handler myHandler;

    private String recentlyDate;

    private String lastUpdate;

    private int defaultMonth = 5;

    private AccountsModel accountModel;

    TimeSelectorManager tsm;

    private double withdrawals;

    private double accountBalance;
    
    private GoogleAnalytics mGaInstance;
    
  	private Tracker mGaTracker1;
  	
  	private boolean sbItaly = true;

    public CreditCardLevel2Manager(MainActivity activity) {
        super(activity);
        myHandler = new Handler();
    }

    @Override
    protected void init() {
        lf = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = (ViewGroup)super.activity.findViewById(R.id.level3_circle);
        setLeftNavigationText(R.string.cards);
        // create a LineView
        cv = (CircleLinearView)layout.findViewById(R.id.content_cirlin);
        ViewGroup v = (ViewGroup)layout.findViewById(R.id.time_selector);
        tsm = new TimeSelectorManager(activity, v);
    }

    @Override
    protected void loadData() {
        setUI();
    }

    @Override
    public boolean onLeftNavigationButtonClick(View v) {
        mainManager.showCards(false, null);
        return true;
    }

    @Override
    protected void setUI() {
        updateUI(TimeUtil.nowTimeMillis());
    }

    public void onShow(Object object) {
    	mGaInstance = GoogleAnalytics.getInstance(activity);
        mGaTracker1 = mGaInstance.getTracker(Contants.TRACKER_ID);
        mGaTracker1.sendView("event.card.transaction");
        defaultMonth = 5;
        getData((Bundle) object);
        loadData();
    }
    
    public static Bundle saveData(int color,AccountsModel accountsModel){
    	Bundle bundle=new Bundle();
    	bundle.putInt("color", color);
    	bundle.putSerializable("AccountsModel", accountsModel);
    	return bundle;
    }
    
    private void getData(Bundle bundle){
    	if(bundle==null){
    		return;
    	}
        cv.setMiddleColor(bundle.getInt("color"));
      accountModel = (AccountsModel)bundle.getSerializable("AccountsModel");
    }
    String accountBalanceAmount;
    String withdrawalsAmount;
    private void updateUI(long date) {
        if (accountModel == null && (accountModel.getDashboardAggregatedAccountsList().size() == 0 || accountModel.getDashboardAggregatedAccountsList() == null)) {
            return;
        }
        for (int i = 0; i < accountModel.getDashboardAggregatedAccountsList().size(); i++) {
            List<AggregatedAccount> aggregatedAccount = accountModel.getDashboardAggregatedAccountsList();
            List<DashboardDataModel> dashBoardDatalist = aggregatedAccount.get(i).getDashboardDataList();
            if (dashBoardDatalist == null || dashBoardDatalist.size() == 0) {
                continue;
            }
            
            if(accountModel.getBankServiceType().getBankServiceCode().equals(ServiceCode.PREPAID_CARD_CODE)){
                int index = Utils.getDateIndex(dashBoardDatalist, date);
//                accountBalance = dashBoardDatalist.get(index).getAccountBalance();
                if(index == 0 && sbItaly){
                    accountBalance = aggregatedAccount.get(i).getAvailableBalance();
                }else{
                    accountBalance = 0;
                }
                withdrawals = dashBoardDatalist.get(index).getWithdrawals();
                lastUpdate = TimeUtil.changeFormattrString(dashBoardDatalist.get(index).getLastUpdate(), TimeUtil.dateFormat2,TimeUtil.dateFormat13);
            }else{
                int index = Utils.getDateIndex(dashBoardDatalist, date);
                String timebar = TimeUtil.getDateString(date, TimeUtil.dateFormat2);
                String lastUpdate = dashBoardDatalist.get(index).getLastUpdate();
                
                if(index == 0){
                    accountBalance = aggregatedAccount.get(i).getAvailableBalance();
                }else{
                    accountBalance = 0;
                }
                
                if(Utils.compareMonth(timebar,lastUpdate)){
                    withdrawals = dashBoardDatalist.get(index).getWithdrawals();
                    withdrawals = Math.abs(withdrawals);
                }else{
                    withdrawals = 0;
                }
                this.lastUpdate = TimeUtil.changeFormattrString(lastUpdate, TimeUtil.dateFormat2,TimeUtil.dateFormat13);
            }
        }

        cv.removeAllViews();
        tsm.clear();

        setTitle(accountModel.getAccountAlias());
        String time = activity.getResources().getString(R.string.last_update_on);
        time = String.format(time, this.lastUpdate);
        setSubTitle(time);

        android.view.ViewGroup.LayoutParams chileLayoutLp = new LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,android.view.ViewGroup.LayoutParams.FILL_PARENT);
        withdrawalsAmount = Utils.formatMoney(withdrawals, activity.getResources().getString(R.string.dollar), true, true,false, false, true);
        cv.addView(CircleLinearViewUtils.createItemView(lf, chileLayoutLp, Gravity.CENTER, activity.getResources().getString(R.string.cards_withdrawals), withdrawalsAmount));
        if(accountBalance == 0){
            accountBalanceAmount = activity.getResources().getString(R.string.not_value);
        }else{
            accountBalanceAmount = Utils.formatMoney(accountBalance, activity.getResources().getString(R.string.dollar), true, true,false, false, true);
        }
        cv.addView(CircleLinearViewUtils.createItemView(lf, chileLayoutLp, Gravity.CENTER, activity.getResources().getString(R.string.account_balance), accountBalanceAmount));

        View view = lf.inflate(R.layout.level2_details_btn_model, null);
        sureBtn = (Button)view.findViewById(R.id.detail_btn);
        sureBtn.setOnClickListener(this);
        sureBtn.setText(activity.getResources().getString(R.string.account_transactions));
        cv.addView(view);

        tsm.setOnTimeItemSelectedListener(this);
        if (accountModel.getBankServiceType().getBankServiceCode().equals(ServiceCode.CREDIT_CARD_CODE)) {
            tsm.setSelectedRes(R.drawable.timescale_credit_on);
            tsm.setUnSelectedRes(R.drawable.timescale_btn_on);
        } else if (accountModel.getBankServiceType().getBankServiceCode().equals(ServiceCode.PREPAID_CARD_CODE)) {
            tsm.setSelectedRes(R.drawable.timescale_prepaid_on);
            tsm.setUnSelectedRes(R.drawable.timescale_btn_on);
        }
        tsm.generateDefaultVerticalTimes();
        tsm.generateButton();
        tsm.setSelected(defaultMonth);
    }

    public void setTitle(String title) {
        ((TextView)layout.findViewById(R.id.layout_title_tv)).setText(title);
    }

    public void setSubTitle(String subtitle) {
        ((TextView)layout.findViewById(R.id.layout_subtitle_tv)).setText(subtitle);
    }

    public void setItemContent(int id, String title, String content) {

    }
    
    @Override
    public void onTimeSelected(TimeText timeText, int position) {

        if (position == 0) {
            myHandler.post(new Runnable() {
                @Override
                public void run() {
                    recentlyDate = TimeUtil.getMonthAgo(5);
                    defaultMonth = 0;
                    sbItaly = false;
                    updateUI(TimeUtil.getMillis(recentlyDate));
                }
            });
        } else if (position == 1) {
            myHandler.post(new Runnable() {
                @Override
                public void run() {
                    recentlyDate = TimeUtil.getMonthAgo(4);
                    defaultMonth = 1;
                    sbItaly = false;
                    updateUI(TimeUtil.getMillis(recentlyDate));
                }
            });
        } else if (position == 2) {
            myHandler.post(new Runnable() {
                @Override
                public void run() {
                    recentlyDate = TimeUtil.getMonthAgo(3);
                    defaultMonth = 2;
                    sbItaly = false;
                    updateUI(TimeUtil.getMillis(recentlyDate));
                }
            });
        } else if (position == 3) {
            myHandler.post(new Runnable() {
                @Override
                public void run() {
                    recentlyDate = TimeUtil.getMonthAgo(2);
                    defaultMonth = 3;
                    sbItaly = false;
                    updateUI(TimeUtil.getMillis(recentlyDate));
                }
            });
        } else if (position == 4) {
            myHandler.post(new Runnable() {
                @Override
                public void run() {
                    recentlyDate = TimeUtil.getMonthAgo(1);
                    defaultMonth = 4;
                    sbItaly = false;
                    updateUI(TimeUtil.getMillis(recentlyDate));
                }
            });

        } else if (position == 5) {
            myHandler.post(new Runnable() {
                @Override
                public void run() {
                    long nowTime = TimeUtil.nowTimeMillis();
                    defaultMonth = 5;
                    sbItaly = true;
                    updateUI(nowTime);
                }
            });
        }

    }

    @Override
    public void onClick(View v) {
        if (v == sureBtn) {
            mainManager.showCreditCardsDetails(false, accountModel);
        }
    }

}
