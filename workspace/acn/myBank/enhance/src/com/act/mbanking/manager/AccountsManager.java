
package com.act.mbanking.manager;

import java.util.List;

import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.act.mbanking.AggregatedAccountType;
import com.act.mbanking.ChartModelManager;
import com.act.mbanking.Contants;
import com.act.mbanking.R;
import com.act.mbanking.activity.MainActivity;
import com.act.mbanking.bean.AggregatedAccount;
import com.act.mbanking.bean.DashboardDataModel;
import com.act.mbanking.bean.SynchDashBoardModel;
import com.act.mbanking.logic.SynchDashboardJson;
import com.act.mbanking.manager.DashBoardSubOneManager.OnDashboardSub1ClickListener;
import com.act.mbanking.manager.TimeSelectorManager.OnTimeItemSelectedListener;
import com.act.mbanking.manager.view.BankBitmapDrawable;
import com.act.mbanking.manager.view.LegendItem;
import com.act.mbanking.manager.view.LegendItem.LegendItemText;
import com.act.mbanking.manager.view.TimeItem.TimeText;
import com.act.mbanking.net.HttpConnector;
import com.act.mbanking.net.ProgressOverlay;
import com.act.mbanking.net.ProgressOverlay.OnProgressEvent;
import com.act.mbanking.utils.LogManager;
import com.act.mbanking.utils.TimeUtil;
import com.act.mbanking.utils.Utils;
import com.act.mbanking.view.ChartModelMapTool;
import com.act.mbanking.view.ChartView;
import com.act.mbanking.view.HorizontalChartView;
import com.custom.view.RectLD;
import com.custom.view.WheelButton;
import com.custom.view.WheelButtonItem;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

public class AccountsManager extends MainMenuSubScreenManager implements
        OnDashboardSub1ClickListener {
	
	public static final String TAG="AccountsManager";

    DashBoardSubOneManager dashboardSubOne;

    Handler myHandler;

    private String lastUpdate = "";

    String recentlyDate;

    int defaultMonth = 5;
    
    private GoogleAnalytics mGaInstance;
    
  	private Tracker mGaTracker1;
    
    public AccountsManager(MainActivity activity) {
        super(activity);
        myHandler = new Handler();

        long nowTime = TimeUtil.nowTimeMillis();
        lastUpdate = TimeUtil.getDateString(nowTime, TimeUtil.dateFormat13);
    }

    @Override
    protected void init() {

        layout = (ViewGroup)activity.findViewById(R.id.accounts);

        dashboardSubOne = new DashBoardSubOneManager(activity, layout);
        dashboardSubOne.setOnDashboardSub1ClickListener(this);
        layout.addView(dashboardSubOne.getView());
        setLeftNavigationText(activity.getResources().getString(R.string.dashboard));
    }

    @Override
    public boolean onLeftNavigationButtonClick(View v) {
        mainManager.showAggregatedView(false, null);
        return true;
    }

    @Override
    public void hide() {
        super.hide();
    }

    @Override
    protected void onShow(Object object) {
    	mGaInstance = GoogleAnalytics.getInstance(activity);
        mGaTracker1 = mGaInstance.getTracker(Contants.TRACKER_ID);
        mGaTracker1.sendView("view.balances");
        super.onShow(object);
        defaultMonth = 5;
        loadData();
        ProgressOverlay progressOverlay = new ProgressOverlay(activity);
        progressOverlay.show(activity.getResources().getString(R.string.waiting),
                new OnProgressEvent() {
                    @Override
                    public void onProgress() {
                        SynchDashBoardModel synchDashBoard = synchDashBoard(
                                AggregatedAccountType.ACCOUNTS,
                                Contants.getUserInfo.getLastUpdate());
                        if (synchDashBoard != null) {
                            for (int i = 0; i < synchDashBoard.getAggregatedAccountsList().size(); i++) {
                                if (synchDashBoard.getAggregatedAccountsList().get(i)
                                        .getDashboardDataList().size() != 0) {
                                    myHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Contants.setAccounts();
                                            defaultMonth = 5;
                                            loadData();
                                        }
                                    });
                                    return;
                                }
                            }
                        }
                    }
                });
    }

    private SynchDashBoardModel synchDashBoard(int aggregatedAccountType, String lastUpdate) {
        String postData = SynchDashboardJson.synchDashboardReportProtocal(Contants.publicModel,
                aggregatedAccountType, lastUpdate);
        HttpConnector httpConnector = new HttpConnector();
        String httpResult = httpConnector
                .requestByHttpPost(Contants.mobile_url, postData, activity);
        SynchDashBoardModel syncDashBoard = SynchDashboardJson
                .parseSynchDashBoardResponse(httpResult);

        if (syncDashBoard == null) {
            LogManager.d("syncDashBoard = null" + postData);
            return null;
        }
        return syncDashBoard;
    }

    @Override
    public void onTimeClick(TimeText timeText, int position) {
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
    public void onChartButtonItemClick(View view, int position, long type) {
        LogManager.d("position: = " + position);
        if (type == WheelButton.ITEM_TYPE_CIRCLE) {
            mainManager.showAccountLevel2(false, Contants.baseAccounts.get(position));
        }
    }

    @Override
    public void onLegendItemSelected(View view ,LegendItemText legendItemText, int position) {
        mainManager.showAccountLevel2(false, Contants.baseAccounts.get(position));
    }

    @Override
    protected void loadData() {
        setUI();
    }

    @Override
    protected void setUI() {
        updateUI(TimeUtil.nowTimeMillis());
    }

    /**
     * legendItem上显示的totalPortfolio的总和
     */
    private double accountBalanceSum = 0;

    /**
     * accountTitle上面的total Investment 总和
     */
    private double totalAccountsSum = 0;

    public void getTotalAccountSum(long date) {
        int size = Contants.baseAccounts.size();
        for (int i = 0; i < size; i++) {
            accountBalanceSum = 0;
            List<AggregatedAccount> aggregatedAccountList = Contants.baseAccounts.get(i).getDashboardAggregatedAccountsList();
            if (aggregatedAccountList == null || aggregatedAccountList.size() == 0) {
                continue;
            }
            for (int j = 0; j < aggregatedAccountList.size(); j++) {
                List<DashboardDataModel> dashBoardDatalist = aggregatedAccountList.get(j)
                        .getDashboardDataList();
                if (dashBoardDatalist == null || dashBoardDatalist.size() == 0) {
                    continue;
                }
                int index = Utils.getDateIndex(dashBoardDatalist, date);
                totalAccountsSum += dashBoardDatalist.get(index).getAccountBalance();
            }
        }
    }

    public void updateUI(long date) {
    	mGaInstance = GoogleAnalytics.getInstance(activity);
        mGaTracker1 = mGaInstance.getTracker(Contants.TRACKER_ID);
        mGaTracker1.sendView("event.account.transaction"); 
        totalAccountsSum = 0;
        dashboardSubOne.clear();
        BitmapDrawable cyclebitmapDrawable = (BitmapDrawable)getDrawable(R.drawable.accounts_legend_closed);
        BitmapDrawable openBitmapDrawable = (BitmapDrawable)getDrawable(R.drawable.accounts_legend_opened);
        BitmapDrawable bitmapDrawable = (BitmapDrawable)getDrawable(R.drawable.accounts_legend_closed);

        getTotalAccountSum(date);

        int size = Contants.baseAccounts.size();
        for (int i = 0; i < size; i++) {
            accountBalanceSum = 0;
            List<AggregatedAccount> aggregatedAccountList = Contants.baseAccounts.get(i)
                    .getDashboardAggregatedAccountsList();
            if (aggregatedAccountList == null || aggregatedAccountList.size() == 0) {
                continue;
            }
            for (int j = 0; j < aggregatedAccountList.size(); j++) {
                List<DashboardDataModel> dashBoardDatalist = aggregatedAccountList.get(j).getDashboardDataList();
                if (dashBoardDatalist == null || dashBoardDatalist.size() == 0) {
                    continue;
                }
                int index = Utils.getDateIndex(dashBoardDatalist, date);
                accountBalanceSum += dashBoardDatalist.get(index).getAccountBalance();
                lastUpdate = TimeUtil.changeFormattrString(dashBoardDatalist.get(index).getLastUpdate(), TimeUtil.dateFormat2,TimeUtil.dateFormat13);
            }

            LegendItem.LegendItemText item = new LegendItem.LegendItemText();
            dashboardSubOne.addLegendItem(item);
            BankBitmapDrawable cycleBankBitmapDrawable = new BankBitmapDrawable(
                    cyclebitmapDrawable, BankBitmapDrawable.drawable_type_half_circle);
            cycleBankBitmapDrawable.setBitmapLevel(i + 1);
            cycleBankBitmapDrawable.setMaxLevel(size);
            cycleBankBitmapDrawable.setMainColor(BankBitmapDrawable.COLOR_ACCOUNT);

            BankBitmapDrawable openbaBankBitmapDrawable = new BankBitmapDrawable(
                    openBitmapDrawable, BankBitmapDrawable.drawable_type_rect);
            openbaBankBitmapDrawable.setBitmapLevel(i + 1);
            openbaBankBitmapDrawable.setMainColor(BankBitmapDrawable.COLOR_ACCOUNT);
            openbaBankBitmapDrawable.setMaxLevel(size);
            String accountBalanceAmount = Utils.formatMoney(accountBalanceSum, activity
                    .getResources().getString(R.string.dollar), true, true, false, false, true);
            item.setData(Contants.baseAccounts.get(i).getAccountAlias(), accountBalanceAmount,
                    cycleBankBitmapDrawable, openbaBankBitmapDrawable);

            WheelButtonItem itemButton = new WheelButtonItem();
            itemButton.weight = (int)accountBalanceSum;
            itemButton.clickAble = true;
            itemButton.text = Utils.getPerecntages(accountBalanceSum, totalAccountsSum);
            itemButton.textColor = activity.getResources().getColor(R.color.white);
            BankBitmapDrawable bankBitmapDrawable = new BankBitmapDrawable(bitmapDrawable,
                    BankBitmapDrawable.drawable_type_rect);
            bankBitmapDrawable.setBitmapLevel(i + 1);
            bankBitmapDrawable.setMaxLevel(size);
            bankBitmapDrawable.setMainColor(BankBitmapDrawable.COLOR_ACCOUNT);
            itemButton.backgroundColor = bankBitmapDrawable.getColor();
            dashboardSubOne.addCircleButton(itemButton);
        }

        String totalInvestments = Utils.formatMoney(totalAccountsSum, activity.getResources()
                .getString(R.string.dollar), true, true, false, false, true);
        dashboardSubOne.setTotalAssets(totalInvestments + "");
        dashboardSubOne.setTotalTitle(activity.getResources().getString(
                R.string.account_total_title));
        dashboardSubOne.setTimeSelectDrawableRes(R.drawable.timescale_account__on,
                R.drawable.timescale_btn_on);
        dashboardSubOne.setLastUpdate(lastUpdate);
        dashboardSubOne.generateLegend();
        dashboardSubOne.generateMonthSelector();
        dashboardSubOne.generateTimeButton();
        dashboardSubOne.setOnDashboardSub1ClickListener(this);
        dashboardSubOne.selectedTime(defaultMonth);
    }
    
    public void onLoadChartData(Object o){
    	allChartModelMapTools=ChartModelManager.getAllChartModelMapTools(activity);
        targetChartModelMapTools = ChartModelManager.getBaseAccountChartModels();
    }
    
        List<ChartModelMapTool> allChartModelMapTools;// = ChartModelManager.getAllChartModelMapTools(activity);
        List<ChartModelMapTool> targetChartModelMapTools;// = ChartModelManager.getLoansChartModels();
    HorizontalChartView horizontalChartView;
    public void setChartData(){
      horizontalChartView.setList(targetChartModelMapTools);
      horizontalChartView.setAllList(allChartModelMapTools);
      horizontalChartView.setStyle(ChartView.HALF_YEAR);
    }
    @Override
    protected void showChart(ViewGroup chartLayout) {
    	mGaInstance = GoogleAnalytics.getInstance(activity);
        mGaTracker1 = mGaInstance.getTracker(Contants.TRACKER_ID);
        mGaTracker1.sendView("view.chart.aggregated"); 
//        List<ChartModelMapTool> allChartModelMapTools = ChartModelManager.getAllChartModelMapTools(activity);
//        List<ChartModelMapTool> targetChartModelMapTools = ChartModelManager.getBaseAccountChartModels();//new ArrayList<ChartModelMapTool>();

        chartLayout.removeAllViews();
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        horizontalChartView = (HorizontalChartView)layoutInflater.inflate(
                R.layout.horizontal_chart_view, null);
        LinearLayout.LayoutParams pm = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        chartLayout.addView(horizontalChartView, pm);
        TimeSelectorManager timeSelectorManager = new TimeSelectorManager(activity,
                horizontalChartView);
        timeSelectorManager.generateDefaultHorizontalTimes();
        timeSelectorManager.setSelectedRes(R.drawable.timescale_account__on);
        timeSelectorManager.setUnSelectedRes(R.drawable.timescale_btn_on);
        timeSelectorManager.generateButton();
        horizontalChartView.init();
        String totalInvestments = Utils.formatMoney(totalAccountsSum, activity.getResources()
                .getString(R.string.dollar), true, true, false, false, true);
        dashboardSubOne.setTotalAssets(totalInvestments + "");
        horizontalChartView.setTotalAssetText(activity.getString(R.string.ss_total_account)+" " + totalInvestments);
        horizontalChartView.setLastUpdateOnText(lastUpdate);
        final ChartView chartView = (ChartView) horizontalChartView.getChartView();

//        chartView.horizontalChartView = horizontalChartView;
//        horizontalChartView.setAllList(allChartModelMapTools);
//        horizontalChartView.setList(targetChartModelMapTools);
        horizontalChartView.setRightText(activity.getResources().getString(
                R.string.accounts));
        timeSelectorManager.setSelected(1);
//        horizontalChartView.setStyle(ChartView.HALF_YEAR);
        timeSelectorManager.setOnTimeItemSelectedListener(new OnTimeItemSelectedListener() {
            public void onTimeSelected(TimeText timeText, int position) {
                int style = 0;
                chartView.line = 1;
                switch (position) {
                    case 0:
                        style = ChartView.YEAR;
                        break;
                    case 1:
                        style = ChartView.HALF_YEAR;
                        break;
                    case 2:
                        style = ChartView.MONTH;
                        chartView.line = 2;
                        break;
                    case 3:
                        style = ChartView.WEEK;
                        chartView.line = 2;
                        break;
                    case 4:
                        style = ChartView.DAY;
                        chartView.line = 2;
                        break;
                    default:
                        break;
                }
                horizontalChartView.setStyle(style);
            }
        });

    }

}
