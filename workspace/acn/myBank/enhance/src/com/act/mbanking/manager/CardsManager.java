
package com.act.mbanking.manager;

import java.util.ArrayList;
import java.util.List;

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
import com.act.mbanking.ServiceCode;
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

public class CardsManager extends MainMenuSubScreenManager implements OnDashboardSub1ClickListener {

    DashBoardSubOneManager dashboardSubOne;

    Handler myHandler;

    String recentlyDate;

    private String lastUpdate = "";

    int defaultMonth = 5;
    
    private GoogleAnalytics mGaInstance;
    
  	private Tracker mGaTracker1;
  	
  	private boolean sbItaly = true;
    public CardsManager(MainActivity activity) {
        super(activity);
        myHandler = new Handler();
    }

    @Override
    protected void init() {

        layout = (ViewGroup)activity.findViewById(R.id.cards);

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
        mGaTracker1.sendView("view.cards"); 
        super.onShow(object);
        defaultMonth = 5;
        loadData();
        ProgressOverlay progressOverlay = new ProgressOverlay(activity);
        progressOverlay.show(activity.getResources().getString(R.string.waiting),
                new OnProgressEvent() {
                    @Override
                    public void onProgress() {
                        SynchDashBoardModel synchDashBoard = synchDashBoard(
                                AggregatedAccountType.CARDS, Contants.getUserInfo.getLastUpdate());
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
    public void onChartButtonItemClick(View view, int position, long type) {
        LogManager.d("position: = " + position);
        if (type == WheelButton.ITEM_TYPE_CIRCLE) {
            int color = ((WheelButton)view).getCircleButton(position).backgroundColor;
            mainManager.showCardsLevel2(
                    false,
                    CreditCardLevel2Manager.saveData(color,
                            Contants.prepaidCardAccounts.get(position)));
        } else if (type == WheelButton.ITEM_TYPE_CENTER) {
            int color = ((WheelButton)view).getCenterButton(position).backgroundColor;
            mainManager.showCardsLevel2(
                    false,
                    CreditCardLevel2Manager.saveData(color,
                            Contants.creditCardAccounts.get(position)));
        }
    }

    @Override
    public void onLegendItemSelected(View view, LegendItemText legendItemText, int position) {
        if (legendItemText.getType().equals(ServiceCode.PREPAID_CARD_CODE)) {
            mainManager.showCardsLevel2(false,CreditCardLevel2Manager.saveData(legendItemText.getBackgroundColor(),Contants.prepaidCardAccounts.get(position)));
        } else if (legendItemText.getType().equals(ServiceCode.CREDIT_CARD_CODE)) {
            position = position - Contants.prepaidCardAccounts.size(); //因为是先画的prepaid card 所以这里要用当前点击的位置 - prepaid card的总数得到当前creditcard的位置
            mainManager.showCardsLevel2(false,CreditCardLevel2Manager.saveData(legendItemText.getBackgroundColor(),Contants.creditCardAccounts.get(position)));
        }
    }

    
    public void onLoadChartData(Object o){
    	allChartModelMapTools=ChartModelManager.getAllChartModelMapTools(activity);
        targetChartModelMapTools = ChartModelManager.getCardChartModels();
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
        mGaTracker1.sendView("view.chart.cards"); 
//        List<ChartModelMapTool> allChartModelMapTools = ChartModelManager.getAllChartModelMapTools(activity);
//        List<ChartModelMapTool> targetChartModelMapTools = ChartModelManager.getCardChartModels();
        chartLayout.removeAllViews();
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        horizontalChartView = (HorizontalChartView)layoutInflater.inflate(R.layout.horizontal_chart_view, null);
        LinearLayout.LayoutParams pm = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        chartLayout.addView(horizontalChartView, pm);
        TimeSelectorManager timeSelectorManager = new TimeSelectorManager(activity,horizontalChartView);
        timeSelectorManager.generateDefaultHorizontalTimes();
        timeSelectorManager.setSelectedRes(R.drawable.timescale_prepaid_on);
        timeSelectorManager.setUnSelectedRes(R.drawable.timescale_btn_on);
        timeSelectorManager.generateButton();
        horizontalChartView.init();
        String totalInvestments = Utils.formatMoney(prepaidCardTotalAccountsSum, activity.getResources().getString(R.string.dollar), false, true, true, true, true);
        horizontalChartView.setTotalAssetText(activity.getString(R.string.ss_total_cards)+" " + totalInvestments + "");
        horizontalChartView.setLastUpdateOnText(lastUpdate);
        final ChartView chartView = (ChartView) horizontalChartView.getChartView();


//        chartView.horizontalChartView = horizontalChartView;
//        horizontalChartView.setAllList(allChartModelMapTools);
//        horizontalChartView.setList(targetChartModelMapTools);
        horizontalChartView.setRightText(activity.getResources().getString(R.string.cards));
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

    // /**
    // * accountTitle上面的total Investment 总和
    // */
    // private double totalAccountsSum = 0;

    public double getPrepaidCardTotalSum(long date, List<AccountsModel> accountsList) {
        double sum = 0;
        double accountBalance = 0;
        int size = accountsList.size();
        for (int i = 0; i < size; i++) {
            accountBalance = 0;
            List<AggregatedAccount> aggregatedAccountList = accountsList.get(i).getDashboardAggregatedAccountsList();
            if (aggregatedAccountList == null || aggregatedAccountList.size() == 0) {
                continue;
            }
            for (int j = 0; j < aggregatedAccountList.size(); j++) {
                List<DashboardDataModel> dashBoardDatalist = aggregatedAccountList.get(j).getDashboardDataList();
                if (dashBoardDatalist == null || dashBoardDatalist.size() == 0) {
                    continue;
                }
                int index = Utils.getDateIndex(dashBoardDatalist, date);
                accountBalance += dashBoardDatalist.get(index).getAccountBalance();
                if(Double.isNaN(accountBalance)){
                    if(sbItaly){
                        accountBalance = aggregatedAccountList.get(j).getAvailableBalance();
                    }else{
                        accountBalance = 0;
                    }
                }
            }
            sum += accountBalance;
        }
        return sum;
    }

    public double getCreditCardTotalSum(long date, List<AccountsModel> accountsList) {
        double sum = 0;
        double accountBalance = 0;
        int size = accountsList.size();
        for (int i = 0; i < size; i++) {
            accountBalance = 0;
            List<AggregatedAccount> aggregatedAccountList = accountsList.get(i).getDashboardAggregatedAccountsList();
            if (aggregatedAccountList == null || aggregatedAccountList.size() == 0) {
                continue;
            }
            for (int j = 0; j < aggregatedAccountList.size(); j++) {
                List<DashboardDataModel> dashBoardDatalist = aggregatedAccountList.get(j).getDashboardDataList();
                if (dashBoardDatalist == null || dashBoardDatalist.size() == 0) {
                    continue;
                }
                int index = Utils.getDateIndex(dashBoardDatalist, date);
                accountBalance += dashBoardDatalist.get(index).getWithdrawals();
                if(Double.isNaN(accountBalance)){
                    accountBalance = 0;
                }
            }
            sum += accountBalance;
        }
        return sum;
    }

    private double prepaidCardTotalAccountsSum;

    private double creditCardTotalAccountsSum;

    private void updateUI(long date) {
    	
    	
        dashboardSubOne.clear();

        prepaidCardTotalAccountsSum = 0;
        BitmapDrawable cyclebitmapDrawable = (BitmapDrawable)getDrawable(R.drawable.prepaid_legend_closed);
        BitmapDrawable openBitmapDrawable = (BitmapDrawable)getDrawable(R.drawable.prepaid_legend_opened);
        BitmapDrawable bitmapDrawable = (BitmapDrawable)getDrawable(R.drawable.prepaid_legend_closed);

        prepaidCardTotalAccountsSum = getPrepaidCardTotalSum(date, Contants.prepaidCardAccounts);
        int size = Contants.prepaidCardAccounts.size();
        for (int i = 0; i < size; i++) {
            accountBalanceSum = 0;
            List<AggregatedAccount> aggregatedAccountList = Contants.prepaidCardAccounts.get(i).getDashboardAggregatedAccountsList();
            if (aggregatedAccountList == null || aggregatedAccountList.size() == 0) {
                continue;
            }
            for (int j = 0; j < aggregatedAccountList.size(); j++) {
                List<DashboardDataModel> dashBoardDatalist = aggregatedAccountList.get(j).getDashboardDataList();
                if (dashBoardDatalist == null || dashBoardDatalist.size() == 0) {
                    continue;
                }
                int index = Utils.getDateIndex(dashBoardDatalist, date);
                lastUpdate = TimeUtil.changeFormattrString(dashBoardDatalist.get(index).getLastUpdate(), TimeUtil.dateFormat2,TimeUtil.dateFormat13);
                accountBalanceSum += dashBoardDatalist.get(index).getAccountBalance();
                if(Double.isNaN(accountBalanceSum)){
                    if(sbItaly){
                        accountBalanceSum = aggregatedAccountList.get(j).getAvailableBalance();
                    }else{
                        accountBalanceSum = 0;
                    }
                }
            }

            LegendItem.LegendItemText item = new LegendItem.LegendItemText();
            dashboardSubOne.addLegendItem(item);
            BankBitmapDrawable cycleBankBitmapDrawable = new BankBitmapDrawable(cyclebitmapDrawable, BankBitmapDrawable.drawable_type_half_circle);
            cycleBankBitmapDrawable.setBitmapLevel(i + 1);
            cycleBankBitmapDrawable.setMaxLevel(size);
            cycleBankBitmapDrawable.setMainColor(BankBitmapDrawable.COLOR_PREPARD_CARD);

            BankBitmapDrawable openbaBankBitmapDrawable = new BankBitmapDrawable(openBitmapDrawable, BankBitmapDrawable.drawable_type_rect);
            openbaBankBitmapDrawable.setBitmapLevel(i + 1);
            openbaBankBitmapDrawable.setMainColor(BankBitmapDrawable.COLOR_PREPARD_CARD);
            openbaBankBitmapDrawable.setMaxLevel(size);
            String accountBalanceAmount = Utils.formatMoney(accountBalanceSum, activity.getResources().getString(R.string.dollar), true, true, false, false, true);
            item.setData(Contants.prepaidCardAccounts.get(i).getAccountAlias(),accountBalanceAmount, cycleBankBitmapDrawable, openbaBankBitmapDrawable);
            item.setType(Contants.prepaidCardAccounts.get(i).getBankServiceType().getBankServiceCode());

            WheelButtonItem itemButton = new WheelButtonItem();
            itemButton.weight = (int)accountBalanceSum;
            itemButton.clickAble = true;
            itemButton.text = Utils.getPerecntages(accountBalanceSum, prepaidCardTotalAccountsSum);
            itemButton.textColor = activity.getResources().getColor(R.color.white);
            BankBitmapDrawable bankBitmapDrawable = new BankBitmapDrawable(bitmapDrawable,
                    BankBitmapDrawable.drawable_type_rect);
            bankBitmapDrawable.setBitmapLevel(i + 1);
            bankBitmapDrawable.setMaxLevel(size);
            bankBitmapDrawable.setMainColor(BankBitmapDrawable.COLOR_PREPARD_CARD);
            itemButton.backgroundColor = bankBitmapDrawable.getColor();
            item.setBackgroundColor(itemButton.backgroundColor);
            dashboardSubOne.addCircleButton(itemButton);
        }

        cyclebitmapDrawable = (BitmapDrawable)getDrawable(R.drawable.credit_legend_closed);
        openBitmapDrawable = (BitmapDrawable)getDrawable(R.drawable.credit_legend_opened);
        bitmapDrawable = (BitmapDrawable)getDrawable(R.drawable.credit_legend_closed);
        creditCardTotalAccountsSum = 0;
        creditCardTotalAccountsSum = getCreditCardTotalSum(date, Contants.creditCardAccounts);
        size = Contants.creditCardAccounts.size();
        for (int i = 0; i < size; i++) {
            accountBalanceSum = 0;
            List<AggregatedAccount> aggregatedAccountList = Contants.creditCardAccounts.get(i).getDashboardAggregatedAccountsList();
            if (aggregatedAccountList == null || aggregatedAccountList.size() == 0) {
                continue;
            }
            for (int j = 0; j < aggregatedAccountList.size(); j++) {
                List<DashboardDataModel> dashBoardDatalist = aggregatedAccountList.get(j).getDashboardDataList();
                if (dashBoardDatalist == null || dashBoardDatalist.size() == 0) {
                    continue;
                }
                int index = Utils.getDateIndex(dashBoardDatalist, date);
                accountBalanceSum += dashBoardDatalist.get(index).getWithdrawals();
                if(Double.isNaN(accountBalanceSum)){
                    accountBalanceSum = 0;
                }
                lastUpdate = TimeUtil.changeFormattrString(dashBoardDatalist.get(index).getLastUpdate(), TimeUtil.dateFormat2,TimeUtil.dateFormat13);
            }

            LegendItem.LegendItemText item = new LegendItem.LegendItemText();
            dashboardSubOne.addLegendItem(item);
            BankBitmapDrawable cycleBankBitmapDrawable = new BankBitmapDrawable(cyclebitmapDrawable, BankBitmapDrawable.drawable_type_half_circle);
            cycleBankBitmapDrawable.setBitmapLevel(i + 1);
            cycleBankBitmapDrawable.setMaxLevel(size);
            cycleBankBitmapDrawable.setMainColor(BankBitmapDrawable.COLOR_CREDIT_CARD);

            BankBitmapDrawable openbaBankBitmapDrawable = new BankBitmapDrawable(openBitmapDrawable, BankBitmapDrawable.drawable_type_rect);
            openbaBankBitmapDrawable.setBitmapLevel(i + 1);
            openbaBankBitmapDrawable.setMainColor(BankBitmapDrawable.COLOR_CREDIT_CARD);
            openbaBankBitmapDrawable.setMaxLevel(size);
            String accountBalanceAmount = Utils.formatMoney(accountBalanceSum, activity.getResources().getString(R.string.dollar), true, true, false, false, true);
            item.setData(Contants.creditCardAccounts.get(i).getAccountAlias(),accountBalanceAmount, cycleBankBitmapDrawable, openbaBankBitmapDrawable);
            item.setType(Contants.creditCardAccounts.get(i).getBankServiceType().getBankServiceCode());

            WheelButtonItem itemButton = new WheelButtonItem();
            itemButton.weight = (int)accountBalanceSum;
            itemButton.clickAble = true;
            itemButton.text = Utils.getPerecntages(accountBalanceSum, creditCardTotalAccountsSum);
            itemButton.textColor = activity.getResources().getColor(R.color.white);
            BankBitmapDrawable bankBitmapDrawable = new BankBitmapDrawable(bitmapDrawable,BankBitmapDrawable.drawable_type_rect);
            bankBitmapDrawable.setBitmapLevel(i + 1);
            bankBitmapDrawable.setMaxLevel(size);
            bankBitmapDrawable.setMainColor(BankBitmapDrawable.COLOR_CREDIT_CARD);
            itemButton.backgroundColor = bankBitmapDrawable.getColor();
            item.setBackgroundColor(itemButton.backgroundColor);
            dashboardSubOne.addCenterButton(itemButton);
        }

        String totalInvestments = Utils.formatMoney(prepaidCardTotalAccountsSum, activity.getResources().getString(R.string.dollar), true, true, false, false, true);
        dashboardSubOne.setTotalTitle(activity.getResources().getString(R.string.cards_total_title));
        dashboardSubOne.setTotalAssets(totalInvestments);
        dashboardSubOne.setLastUpdate(lastUpdate);
        dashboardSubOne.generateLegend();
        dashboardSubOne.generateMonthSelector();
        dashboardSubOne.generateTimeButton();
        dashboardSubOne.setOnDashboardSub1ClickListener(this);
        dashboardSubOne.selectedTime(defaultMonth);
    }

}
