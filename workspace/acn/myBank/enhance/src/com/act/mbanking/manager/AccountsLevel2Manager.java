
package com.act.mbanking.manager;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.act.mbanking.R;
import com.act.mbanking.activity.MainActivity;
import com.act.mbanking.bean.AccountsModel;
import com.act.mbanking.bean.DashboardDataModel;
import com.act.mbanking.manager.TimeSelectorManager.OnTimeItemSelectedListener;
import com.act.mbanking.manager.view.TimeItem.TimeText;
import com.act.mbanking.utils.JXSuperscriptSpan;
import com.act.mbanking.utils.TimeUtil;
import com.act.mbanking.utils.Utils;
import com.custom.view.CircleLinearView;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

public class AccountsLevel2Manager extends MainMenuSubScreenManager implements
        OnTimeItemSelectedListener, OnClickListener {
    LayoutInflater lf;

    Button sureBtn;

    private double availableBalance;

    private double accountBalance;

    private double deposite;

    private double withdrawals;

    private Handler myHandler;

    private String recentlyDate;

    private String lastUpdate;

    private int defaultMonth = 5;

    private AccountsModel accountModel;

    TimeSelectorManager tsm;
    
    private GoogleAnalytics mGaInstance;
    
  	private Tracker mGaTracker1;

    public AccountsLevel2Manager(MainActivity activity) {
        super(activity);
        myHandler = new Handler();
    }

    @Override
    protected void init() {

        lf = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = (ViewGroup)super.activity.findViewById(R.id.level3_circle);
        setLeftNavigationText(R.string.accounts);
        // create a LineView
        cv = (CircleLinearView)layout.findViewById(R.id.content_cirlin);
        ViewGroup v = (ViewGroup)layout.findViewById(R.id.time_selector);
        tsm = new TimeSelectorManager(activity, v);
    }

    @Override
    public boolean onLeftNavigationButtonClick(View v) {
        mainManager.showAccounts(false, null);
        return true;
    }

    @Override
    protected void loadData() {
        setUI();
    }

    @Override
    protected void setUI() {
        updateUI(TimeUtil.nowTimeMillis());
    }

    private void updateUI(long date) {
        if (accountModel == null
                && (accountModel.getDashboardAggregatedAccountsList().size() == 0 || accountModel
                        .getDashboardAggregatedAccountsList() == null)) {
            return;
        }

        for (int i = 0; i < accountModel.getDashboardAggregatedAccountsList().size(); i++) {
            availableBalance = accountModel.getDashboardAggregatedAccountsList().get(i)
                    .getAvailableBalance();

            List<DashboardDataModel> dashBoardDatalist = accountModel
                    .getDashboardAggregatedAccountsList().get(i).getDashboardDataList();
            if (dashBoardDatalist == null || dashBoardDatalist.size() == 0) {
                continue;
            }
            int index = Utils.getDateIndex(dashBoardDatalist, date);
            withdrawals = dashBoardDatalist.get(index).getWithdrawals();
            accountBalance = dashBoardDatalist.get(index).getAccountBalance();
            deposite = dashBoardDatalist.get(index).getDeposits();
            lastUpdate = TimeUtil.changeFormattrString(dashBoardDatalist.get(index).getLastUpdate(), TimeUtil.dateFormat2,TimeUtil.dateFormat13);
        }

        cv.removeAllViews();
        tsm.clear();

        setTitle(accountModel.getAccountAlias());
        String time = activity.getResources().getString(R.string.last_update_on);
        time = String.format(time, lastUpdate);
        setSubTitle(time);
        android.view.ViewGroup.LayoutParams chileLayoutLp = new LayoutParams(
                android.view.ViewGroup.LayoutParams.FILL_PARENT,
                android.view.ViewGroup.LayoutParams.FILL_PARENT);
        // add available_balance
        String accountBanlanceAmount = Utils.formatMoney(accountBalance, activity.getResources()
                .getString(R.string.dollar), true, true, false, false, true);
        String available_balanceAmount = Utils.formatMoney(availableBalance, activity
                .getResources().getString(R.string.dollar), true, true, false, false, true);
        if (defaultMonth == 5) {
            cv.addView(CircleLinearViewUtils.createItemView(lf, chileLayoutLp, Gravity.CENTER, activity.getResources()
                    .getString(R.string.account_available_balance), accountBanlanceAmount));
        } else {
            // cv.addView(createItemView(lf, chileLayoutLp, Gravity.CENTER,
            // activity.getResources().getString(R.string.account_available_balance),activity.getResources().getString(R.string.not_value)));
            cv.addView(CircleLinearViewUtils.createItemView(lf, chileLayoutLp, Gravity.CENTER, activity.getResources()
                    .getString(R.string.account_available_balance), available_balanceAmount));
        }
        // add account_balance
        cv.addView(CircleLinearViewUtils.createItemView(lf, chileLayoutLp, Gravity.CENTER, activity.getResources()
                .getString(R.string.account_balance), accountBanlanceAmount));

        LinearLayout BottomLin = new LinearLayout(activity);
        BottomLin.setGravity(Gravity.CENTER);
        BottomLin.setLayoutParams(chileLayoutLp);
        LinearLayout.LayoutParams chileLayoutLp2 = new LayoutParams(0,
                android.view.ViewGroup.LayoutParams.FILL_PARENT);
        chileLayoutLp2.weight = 1;
        String depositeAmount = Utils.formatMoney(deposite,
                activity.getResources().getString(R.string.dollar), true, true, true, false, true);
        BottomLin.addView(CircleLinearViewUtils.createItemView(lf, chileLayoutLp2, Gravity.RIGHT
                | Gravity.CENTER_VERTICAL,
                activity.getResources().getString(R.string.account_deposits), depositeAmount));
        ImageView diviImg = new ImageView(activity);
        diviImg.setScaleType(ScaleType.FIT_XY);
        diviImg.setImageResource(R.drawable.divider_vertical);
        android.widget.LinearLayout.LayoutParams dividerLp = new android.widget.LinearLayout.LayoutParams(
                2, android.view.ViewGroup.LayoutParams.FILL_PARENT);
        dividerLp.setMargins(4, 0, 4, 0);
        BottomLin.addView(diviImg, dividerLp);
        String withdrawalsAmount = Utils.formatMoney(withdrawals, activity.getResources()
                .getString(R.string.dollar), true, true, true, false, true);
        BottomLin
                .addView(CircleLinearViewUtils.createItemView(lf, chileLayoutLp2, Gravity.LEFT | Gravity.CENTER_VERTICAL,
                        activity.getResources().getString(R.string.account_withdrawals),
                        withdrawalsAmount));
        cv.addView(BottomLin);

        View view = lf.inflate(R.layout.level2_details_btn_model, null);
        sureBtn = (Button)view.findViewById(R.id.detail_btn);
        sureBtn.setOnClickListener(this);
        sureBtn.setText(activity.getResources().getString(R.string.account_transactions));
        cv.addView(view);

        tsm.setSelectedRes(R.drawable.timescale_account__on);
        tsm.setUnSelectedRes(R.drawable.timescale_btn_on);
        tsm.setOnTimeItemSelectedListener(this);
        tsm.generateDefaultVerticalTimes();
        tsm.generateButton();
        tsm.setSelected(defaultMonth);
    }

    public void onShow(Object object) {
        cv.setMiddleColor(0xff999ACB);
        defaultMonth = 5;
        accountModel = (AccountsModel)object;
        loadData();
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
                    updateUI(TimeUtil.getMillis(recentlyDate));
                }
            });
        } else if (position == 1) {
            myHandler.post(new Runnable() {
                @Override
                public void run() {
                    recentlyDate = TimeUtil.getMonthAgo(4);
                    defaultMonth = 1;
                    updateUI(TimeUtil.getMillis(recentlyDate));
                }
            });
        } else if (position == 2) {
            myHandler.post(new Runnable() {
                @Override
                public void run() {
                    recentlyDate = TimeUtil.getMonthAgo(3);
                    defaultMonth = 2;
                    updateUI(TimeUtil.getMillis(recentlyDate));
                }
            });
        } else if (position == 3) {
            myHandler.post(new Runnable() {
                @Override
                public void run() {
                    recentlyDate = TimeUtil.getMonthAgo(2);
                    defaultMonth = 3;
                    updateUI(TimeUtil.getMillis(recentlyDate));
                }
            });
        } else if (position == 4) {
            myHandler.post(new Runnable() {
                @Override
                public void run() {
                    recentlyDate = TimeUtil.getMonthAgo(1);
                    defaultMonth = 4;
                    updateUI(TimeUtil.getMillis(recentlyDate));
                }
            });

        } else if (position == 5) {
            myHandler.post(new Runnable() {
                @Override
                public void run() {
                    long nowTime = TimeUtil.nowTimeMillis();
                    defaultMonth = 5;
                    updateUI(nowTime);
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        if (v == sureBtn) {
            mainManager.showAccountDetails(false, accountModel);
        }
    }
}
