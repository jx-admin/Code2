
package com.act.mbanking.manager;

import android.content.Context;
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
import com.act.mbanking.activity.MainActivity;
import com.act.mbanking.bean.AccountsModel;
import com.act.mbanking.manager.TimeSelectorManager.OnTimeItemSelectedListener;
import com.act.mbanking.manager.view.TimeItem.TimeText;
import com.act.mbanking.utils.TimeUtil;
import com.act.mbanking.utils.Utils;
import com.custom.view.CircleLinearView;

public class InvestmentLevel2Manager extends MainMenuSubScreenManager implements
        OnTimeItemSelectedListener, OnClickListener {
    LayoutInflater lf;

    TimeSelectorManager tsm;

    Button sureBtn;

    private Handler myHandler;

    private String recentlyDate;

    private String lastUpdate;

    private int defaultMonth = 5;

    private AccountsModel accountModel;

    private double deposite;

    public InvestmentLevel2Manager(MainActivity activity) {
        super(activity);
        myHandler = new Handler();
        long nowTime = TimeUtil.nowTimeMillis();
        lastUpdate = TimeUtil.getDateString(nowTime, TimeUtil.dateFormat13);
    }

    @Override
    protected void init() {
        lf = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = (ViewGroup)super.activity.findViewById(R.id.level3_circle);
        setLeftNavigationText(R.string.investments);
        cv = (CircleLinearView)layout.findViewById(R.id.content_cirlin);
        ViewGroup v = (ViewGroup)layout.findViewById(R.id.time_selector);
        tsm = new TimeSelectorManager(activity, v);
    }

    @Override
    public boolean onLeftNavigationButtonClick(View v) {

        mainManager.showInvestments(false, null);
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
        if (accountModel == null && (accountModel.getDashboardAggregatedAccountsList().size() == 0 || accountModel
                        .getDashboardAggregatedAccountsList() == null)) {
            return;
        }

        deposite = Contants.getInvestmentDashBoardData(accountModel.getDashboardAggregatedAccountsList(),date);
        
        cv.removeAllViews();
        tsm.clear();

        setTitle(accountModel.getAccountAlias());
        String time = activity.getResources().getString(R.string.last_update_on);
        time = String.format(activity.getResources().getString(R.string.last_update_on), lastUpdate);
        setSubTitle(time);

        android.view.ViewGroup.LayoutParams chileLayoutLp = new LayoutParams(
                android.view.ViewGroup.LayoutParams.FILL_PARENT,
                android.view.ViewGroup.LayoutParams.FILL_PARENT);
        String depositeAmount = Utils.formatMoney(deposite, activity.getResources().getString(R.string.dollar), true, true,false, false, true);
        cv.addView(CircleLinearViewUtils.createItemView(lf, chileLayoutLp, Gravity.CENTER, activity.getResources().getString(R.string.investments_deposit),depositeAmount));

        View view = lf.inflate(R.layout.level2_details_btn_model, null);
        sureBtn = (Button)view.findViewById(R.id.detail_btn);
        sureBtn.setOnClickListener(this);
        sureBtn.setText(activity.getResources().getString(R.string.investments_details));
        cv.addView(view);

        tsm.setSelectStateRes(R.drawable.timescale_investment_on, R.drawable.timescale_btn_on);
        tsm.generateDefaultVerticalTimes();
        tsm.setOnTimeItemSelectedListener(this);
        tsm.generateButton();
        tsm.setSelected(defaultMonth);
    }

    public void onShow(Object object) {
        defaultMonth = 5;
        cv.setMiddleColor(0xff6b7a21);
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
                    try {
                        lastUpdate = Utils.getLastMonth(TimeUtil.getNowTime(), 0, -5, 0);
                        lastUpdate = TimeUtil.changeFormattrString(lastUpdate, TimeUtil.dateFormat4,TimeUtil.dateFormat13);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    defaultMonth = 0;
                    updateUI(TimeUtil.getMillis(recentlyDate));
                }
            });
        } else if (position == 1) {
            myHandler.post(new Runnable() {
                @Override
                public void run() {
                    recentlyDate = TimeUtil.getMonthAgo(4);
                    try {
                        lastUpdate = Utils.getLastMonth(TimeUtil.getNowTime(), 0, -4, 0);
                        lastUpdate = TimeUtil.changeFormattrString(lastUpdate, TimeUtil.dateFormat4,TimeUtil.dateFormat13);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    defaultMonth = 1;
                    updateUI(TimeUtil.getMillis(recentlyDate));
                }
            });
        } else if (position == 2) {
            myHandler.post(new Runnable() {
                @Override
                public void run() {
                    recentlyDate = TimeUtil.getMonthAgo(3);
                    try {
                        lastUpdate = Utils.getLastMonth(TimeUtil.getNowTime(), 0, -3, 0);
                        lastUpdate = TimeUtil.changeFormattrString(lastUpdate, TimeUtil.dateFormat4,TimeUtil.dateFormat13);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    defaultMonth = 2;
                    updateUI(TimeUtil.getMillis(recentlyDate));
                }
            });
        } else if (position == 3) {
            myHandler.post(new Runnable() {
                @Override
                public void run() {
                    recentlyDate = TimeUtil.getMonthAgo(2);
                    try {
                        lastUpdate = Utils.getLastMonth(TimeUtil.getNowTime(), 0, -2, 0);
                        lastUpdate = TimeUtil.changeFormattrString(lastUpdate, TimeUtil.dateFormat4,TimeUtil.dateFormat13);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    defaultMonth = 3;
                    updateUI(TimeUtil.getMillis(recentlyDate));
                }
            });
        } else if (position == 4) {
            myHandler.post(new Runnable() {
                @Override
                public void run() {
                    recentlyDate = TimeUtil.getMonthAgo(1);
                    try {
                        lastUpdate = Utils.getLastMonth(TimeUtil.getNowTime(), 0, -1, 0);
                        lastUpdate = TimeUtil.changeFormattrString(lastUpdate, TimeUtil.dateFormat4,TimeUtil.dateFormat13);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    defaultMonth = 4;
                    updateUI(TimeUtil.getMillis(recentlyDate));
                }
            });

        } else if (position == 5) {
            myHandler.post(new Runnable() {
                @Override
                public void run() {
                    long nowTime = TimeUtil.nowTimeMillis();
                    lastUpdate = TimeUtil.getDateString(nowTime, TimeUtil.dateFormat13);
                    defaultMonth = 5;
                    updateUI(nowTime);
                }
            });
        }

    }

    @Override
    public void onClick(View v) {
        if (v == sureBtn) {
            if (accountModel.getMortageType().equals("DT")) {
                mainManager.showInvestmentDetails(false, accountModel);
            } else if (accountModel.getMortageType().equals("GP")) {
                mainManager.showInvestmentAssetDetailsManager(false, accountModel);
            }

        }
    }

}
