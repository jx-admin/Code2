
package com.act.mbanking.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.act.mbanking.AggregatedAccountType;
import com.act.mbanking.ChartModelManager;
import com.act.mbanking.Contants;
import com.act.mbanking.R;
import com.act.mbanking.activity.MainActivity;
import com.act.mbanking.bean.AccountsModel;
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
import com.act.mbanking.utils.ColorManager;
import com.act.mbanking.utils.LogManager;
import com.act.mbanking.utils.TimeUtil;
import com.act.mbanking.utils.Utils;
import com.act.mbanking.view.ChartModelMapTool;
import com.act.mbanking.view.ChartView;
import com.act.mbanking.view.HorizontalChartView;
import com.custom.view.WheelButton;
import com.custom.view.WheelButtonItem;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

public class InvestmentManager extends MainMenuSubScreenManager implements
        OnDashboardSub1ClickListener {

    DashBoardSubOneManager dashboardSubOne;

    Handler myHandler;

    private String lastUpdate = "";

    String recentlyDate;

    int defaultMonth = 5;
    
    private GoogleAnalytics mGaInstance;
    
  	private Tracker mGaTracker1;

    public InvestmentManager(MainActivity activity) {
        super(activity);
        myHandler = new Handler();
        long nowTime = TimeUtil.nowTimeMillis();
        lastUpdate = TimeUtil.getDateString(nowTime, TimeUtil.dateFormat13);
    }

    @Override
    protected void init() {
        layout = (ViewGroup)activity.findViewById(R.id.investment);
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
        mGaTracker1.sendView("view.investments"); 
        super.onShow(object);
        defaultMonth = 5;
        loadData();
//        ProgressOverlay progressOverlay = new ProgressOverlay(activity);
//        progressOverlay.show(activity.getResources().getString(R.string.waiting),
//                new OnProgressEvent() {
//                    @Override
//                    public void onProgress() {
//                        String updateTime = TimeUtil.getDateString(TimeUtil.nowTimeMillis(),TimeUtil.dateFormat2);
//                        SynchDashBoardModel synchDashBoard = synchDashBoard(AggregatedAccountType.INVESTMENT,updateTime);
//                        if (synchDashBoard != null) {
//                            for (int i = 0; i < synchDashBoard.getAggregatedAccountsList().size(); i++) {
//                                if (synchDashBoard.getAggregatedAccountsList().get(i).getDashboardDataList().size() != 0) {
//                                    myHandler.post(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            Contants.setAccounts();
//                                            defaultMonth = 5;
//                                            loadData();
//                                        }
//                                    });
//                                    return;
//                                }
//                            }
//                        }
//                    }
//                });
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
    public void onChartButtonItemClick(View view, int position, long type) {
        LogManager.d("position: = " + position);
        if (type == WheelButton.ITEM_TYPE_CIRCLE) {
            mainManager.showInvestmentLevel2(false, Contants.investmentAccounts.get(position));
        }
    }

    @Override
    public void onLegendItemSelected(View view, LegendItemText legendItemText, int position) {
        mainManager.showInvestmentLevel2(false, Contants.investmentAccounts.get(position));
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
    private double totalPortfolioSum = 0;

    /**
     * investmentsTitle上面的total Investment 总和
     */
    private double totalInvestmentsSum = 0;

    public void updateUI(long date) {
        totalInvestmentsSum = 0;
        dashboardSubOne.clear();
        BitmapDrawable cyclebitmapDrawable = (BitmapDrawable)getDrawable(R.drawable.investments_legend_closed);
        BitmapDrawable openBitmapDrawable = (BitmapDrawable)getDrawable(R.drawable.investments_legend_opened);
        BitmapDrawable bitmapDrawable = (BitmapDrawable)getDrawable(R.drawable.investments_legend_closed);
        int size = Contants.investmentAccounts.size();
        totalInvestmentsSum = Contants.investmentsSyncDashBoard(Contants.investmentAccounts, date);

        for (int i = 0; i < size; i++) {
            totalPortfolioSum = 0;
            List<AggregatedAccount> aggregatedAccountList = Contants.investmentAccounts.get(i)
                    .getDashboardAggregatedAccountsList();
            if (aggregatedAccountList == null || aggregatedAccountList.size() == 0) {
                continue;
            }

            totalPortfolioSum = Contants.getInvestmentDashBoardData(aggregatedAccountList, date);

            LegendItem.LegendItemText item = new LegendItem.LegendItemText();
            dashboardSubOne.addLegendItem(item);
            BankBitmapDrawable cycleBankBitmapDrawable = new BankBitmapDrawable(
                    cyclebitmapDrawable, BankBitmapDrawable.drawable_type_half_circle);
            cycleBankBitmapDrawable.setBitmapLevel(i + 1);
            cycleBankBitmapDrawable.setMaxLevel(size);
            cycleBankBitmapDrawable.setMainColor(BankBitmapDrawable.COLOR_INVESTMENT);

            BankBitmapDrawable openbaBankBitmapDrawable = new BankBitmapDrawable(
                    openBitmapDrawable, BankBitmapDrawable.drawable_type_rect);
            openbaBankBitmapDrawable.setBitmapLevel(i + 1);
            openbaBankBitmapDrawable.setMainColor(BankBitmapDrawable.COLOR_INVESTMENT);
            openbaBankBitmapDrawable.setMaxLevel(size);
            String totalPortfolioAmount = Utils.formatMoney(totalPortfolioSum, activity
                    .getResources().getString(R.string.dollar), true, true, false, false, true);
            item.setData(Contants.investmentAccounts.get(i).getAccountAlias(),
                    totalPortfolioAmount, cycleBankBitmapDrawable, openbaBankBitmapDrawable);

            WheelButtonItem itemButton = new WheelButtonItem();
            itemButton.weight = (int)totalPortfolioSum;
            itemButton.clickAble = true;
            itemButton.text = Utils.getPerecntages(totalPortfolioSum, totalInvestmentsSum);
            itemButton.textColor = activity.getResources().getColor(R.color.white);
            BankBitmapDrawable bankBitmapDrawable = new BankBitmapDrawable(bitmapDrawable,
                    BankBitmapDrawable.drawable_type_rect);
            bankBitmapDrawable.setBitmapLevel(i + 1);
            bankBitmapDrawable.setMaxLevel(size);
            bankBitmapDrawable.setMainColor(BankBitmapDrawable.COLOR_INVESTMENT);
            itemButton.backgroundColor = bankBitmapDrawable.getColor();
            dashboardSubOne.addCircleButton(itemButton);
        }

        String totalInvestments = Utils.formatMoney(totalInvestmentsSum, activity.getResources()
                .getString(R.string.dollar), true, true, false, false, true);
        dashboardSubOne.setTotalAssets(totalInvestments + "");
        dashboardSubOne.setTimeSelectDrawableRes(R.drawable.timescale_investment_on,
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
        targetChartModelMapTools = ChartModelManager.getInvestmentChartModels();
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
        mGaTracker1.sendView("view.chart.investments"); 
//        List<ChartModelMapTool> allChartModelMapTools = ChartModelManager.getAllChartModelMapTools(activity);
//        List<ChartModelMapTool> targetChartModelMapTools =ChartModelManager.getInvestmentChartModels();

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
        timeSelectorManager.setSelectedRes(R.drawable.timescale_investment_on);
        timeSelectorManager.setUnSelectedRes(R.drawable.timescale_btn_on);
        timeSelectorManager.generateButton();
        horizontalChartView.init();
        String totalInvestments = Utils.formatMoney(totalInvestmentsSum, activity.getResources()
                .getString(R.string.dollar), true, true, false, false, true);
        horizontalChartView.setTotalAssetText(activity.getString(R.string.ss_total_investments)+" " + totalInvestments);
        horizontalChartView.setLastUpdateOnText(lastUpdate);
        final ChartView chartView = (ChartView) horizontalChartView.getChartView();

//        chartView.horizontalChartView = horizontalChartView;
//        horizontalChartView.setList(targetChartModelMapTools);
//        horizontalChartView.setAllList(allChartModelMapTools);
        horizontalChartView.setRightText(activity.getResources().getString(
                R.string.investments));
        timeSelectorManager.setSelected(1);
//        horizontalChartView.setStyle(ChartView.HALF_YEAR);
        timeSelectorManager.setOnTimeItemSelectedListener(new OnTimeItemSelectedListener() {

            @Override
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
