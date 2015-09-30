
package com.act.mbanking.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.ActionBar.LayoutParams;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.act.mbanking.AggregatedAccountType;
import com.act.mbanking.App;
import com.act.mbanking.ChartModelManager;
import com.act.mbanking.Contants;
import com.act.mbanking.R;
import com.act.mbanking.activity.BaseActivity;
import com.act.mbanking.activity.MainActivity;
import com.act.mbanking.bean.AccountsModel;
import com.act.mbanking.bean.AggregatedAccount;
import com.act.mbanking.bean.DashBoardModel;
import com.act.mbanking.bean.DashboardDataModel;
import com.act.mbanking.bean.SyncDashboardDataAmount;
import com.act.mbanking.bean.SynchDashBoardModel;
import com.act.mbanking.logic.SynchDashboardJson;
import com.act.mbanking.manager.DashBoardManager.OnDashboardClickListener;
import com.act.mbanking.manager.TimeSelectorManager.OnTimeItemSelectedListener;
import com.act.mbanking.manager.view.BankBitmapDrawable;
import com.act.mbanking.manager.view.LegendItem.LegendItemText;
import com.act.mbanking.manager.view.TimeItem;
import com.act.mbanking.manager.view.TimeItem.TimeText;
import com.act.mbanking.net.HttpConnector;
import com.act.mbanking.net.ProgressOverlay;
import com.act.mbanking.net.ProgressOverlay.OnProgressEvent;
import com.act.mbanking.utils.DialogManager;
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

public class AggregatedViewManager extends MainMenuSubScreenManager implements
        OnTimeItemSelectedListener, OnDashboardClickListener, OnClickListener {
    private static final int INVESTMENTS = 0;

    private static final int ACCOUNTS = 1;

    private static final int PREPAIDCARDS = 2;

    private static final int CREDITCARDS = 3;

    DashBoardManager dashBoardManager;

    private double investmentsSum;

    private double accountSum;

    private double prepaidCardsSum;

    private double creditCardNum;

    private double creditPlafondNum;

    private SyncDashboardDataAmount loansDataAmount;

    private String payedPercentages;

    private String residualPercentages;

    private String accountPercentages;

    private String creditCardPercentages;

    private String prepaidCardsPercentages;

    private String investmentsPercentages;

    private String totalAssets;

    private int defaultMonth = 5;

    private String lastUpdate;

    Handler myHandler;
    
    private GoogleAnalytics mGaInstance;
    
  	private Tracker mGaTracker1;

  	private boolean sbItaly = true;
    public AggregatedViewManager(MainActivity activity) {
        super(activity);
        myHandler = new Handler();
    }

    @Override
    protected void init() {
        Contants.setAccounts();
        needRefresh = true;
        layout = (ViewGroup)activity.findViewById(R.id.aggregated_view);
        dashBoardManager = new DashBoardManager(activity, layout);
        dashBoardManager.setOnDashboardClickListener(this);
        layout.addView(dashBoardManager.getView(), LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT);
        downloading_pd = new ProgressDialog(activity);
        downloading_pd.setTitle(R.string.waiting);
    }

    @Override
    public void onTimeSelected(TimeItem.TimeText timetext, int position) {
        BaseActivity baseActivity = (BaseActivity)activity;
        baseActivity.displayToastMessage("position" + position + "Item" + timetext);
    }

    @Override
    public boolean onLeftNavigationButtonClick(View v) {
        return true;
    }

    long recentlyDate;

    @Override
    public void onTimeClick(TimeText timeText, int position) {
        if (position == 0) {
            recentlyDate = TimeUtil.monthAgo(5);
            defaultMonth = 0;
            sbItaly = false;
        } else if (position == 1) {
            recentlyDate = TimeUtil.monthAgo(4);
            defaultMonth = 1;
            sbItaly = false;
        } else if (position == 2) {
            recentlyDate = TimeUtil.monthAgo(3);
            defaultMonth = 2;
            sbItaly = false;
        } else if (position == 3) {
            recentlyDate = TimeUtil.monthAgo(2);
            defaultMonth = 3;
            sbItaly = false;
        } else if (position == 4) {
            recentlyDate = TimeUtil.monthAgo(1);
            defaultMonth = 4;
            sbItaly = false;
        } else if (position == 5) {
            recentlyDate = TimeUtil.nowTimeMillis();
            defaultMonth = 5;
            sbItaly = true;
        }
        // final ProgressDialog bpd=ProgressDialog.show(activity,
        // activity.getText(R.string.waiting), "");
        myHandler.post(new Runnable() {
            @Override
            public void run() {
                updateUI(recentlyDate);
                // bpd.dismiss();
            }
        });
    }

    @Override
    public void onLegendItemSelected(View view, LegendItemText legendItemText, int position) {
        if(legendItemText.titleText.equals(activity.getResources().getString(R.string.legend_accounts))){
            mainManager.showAccounts(false, null);
        }else if(legendItemText.titleText.equals(activity.getResources().getString(R.string.legend_investments))){
            mainManager.showInvestments(false, null);
        }else if(legendItemText.titleText.equals(activity.getResources().getString(R.string.legend_credit_cards)) || legendItemText.titleText.equals(activity.getResources().getString(R.string.legend_prepaid_cards))){
            mainManager.showCards(false, null);
        }else if(legendItemText.titleText.equals(activity.getResources().getString(R.string.legend_total_loans))){
            mainManager.showLoans(false, null);
        }
    }

    @Override
    public void onChartButtonItemClick(WheelButtonItem itemButton, int position, long type) {
        LogManager.d("position: = " + position);
        if (type == WheelButton.ITEM_TYPE_CENTER) {
            mainManager.showCards(false, null);
        } else if (type == WheelButton.ITEM_TYPE_CIRCLE) {
            if (itemButton.id == INVESTMENTS) {
                mainManager.showInvestments(false, null);
            } else if (itemButton.id == ACCOUNTS) {
                mainManager.showAccounts(false, null);
            } else if (itemButton.id == PREPAIDCARDS) {
                mainManager.showCards(false, null);
            }
        }
    }

    @Override
    protected void onShow(Object object) {
        super.onShow(object);
        if (App.app.isFrist) {
            if (Contants.isNotLastUpdate && (!Utils.isInitRegisterPushNotification(activity))) {
                myHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        notification();
                    }
                });
            }
            creditPlafondNum = 0;
            loadData();
        } else {

            ProgressOverlay progressOverlay = new ProgressOverlay(activity);
            progressOverlay.show(activity.getResources().getString(R.string.waiting),
                    new OnProgressEvent() {
                        @Override
                        public void onProgress() {
                            synchDashBoard(AggregatedAccountType.CARDS,
                                    Contants.getUserInfo.getLastUpdate());
                            synchDashBoard(AggregatedAccountType.INVESTMENT,
                                    Contants.getUserInfo.getLastUpdate());
                            synchDashBoard(AggregatedAccountType.FINANCING,
                                    Contants.getUserInfo.getLastUpdate());
                            SynchDashBoardModel synchDashBoard = synchDashBoard(
                                    AggregatedAccountType.ACCOUNTS,
                                    Contants.getUserInfo.getLastUpdate());
                            if (synchDashBoard != null) {
                                myHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Contants.setAccounts();
                                        creditPlafondNum = 0;
                                        loadData();
                                    }
                                });
                            }
                        }
                    });
        
        }
    }

    protected void notification() {
        final SharedPreferences sp = activity.getSharedPreferences(Contants.SETTING_FILE_NAME,
                activity.MODE_PRIVATE);
        LayoutInflater inflater = LayoutInflater.from(activity);
        LinearLayout linearLahyout = (LinearLayout)inflater.inflate(
                R.layout.exit_message_dialog_layout, null);

        final Dialog dialog = new Dialog(activity, R.style.selectorDialog);
        dialog.setContentView(linearLahyout);
        Button yesButton = (Button)dialog.findViewById(R.id.yes_btn);
        Button noButton = (Button)dialog.findViewById(R.id.no_btn);
        TextView text = (TextView)dialog.findViewById(R.id.exit_message_text);
        text.setText(R.string.is_receive_notifictions);
        dialog.show();
        noButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        yesButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Contants.updataInitSetting(sp.edit(), true);
                Utils.registerGCM(activity);
                DialogManager.createMessageDialog(activity.getResources().getString(R.string.notifictions_confirmation), activity).show();
            }
        });
    }

    public void loginFailed(boolean needshowLoginInput, String msg) {
        if (msg == null) {
            msg = activity.getResources().getString(R.string.login_error);
        }
        activity.displayErrorMessage(msg);
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

    
    
    
    public void onLoadChartData(Object o){
    	targetChartModelMapTools=ChartModelManager.getAllChartModelMapTools(activity);
    }
    
    List<ChartModelMapTool> targetChartModelMapTools;// = ChartModelManager.getLoansChartModels();
    ProgressDialog downloading_pd ;
    HorizontalChartView horizontalChartView;
    public void setChartData(){
      horizontalChartView.setList(targetChartModelMapTools);
      horizontalChartView.setStyle(ChartView.HALF_YEAR);
    }
    @Override
    protected void showChart(ViewGroup chartLayout) {
    	mGaInstance = GoogleAnalytics.getInstance(activity);
        mGaTracker1 = mGaInstance.getTracker(Contants.TRACKER_ID);
        mGaTracker1.sendView("view.chart.aggregated"); 
        
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
        horizontalChartView.setTotalAssetText(activity.getString(R.string.total_assets)+" " + totalAssets);
        horizontalChartView.setLastUpdateOnText(lastUpdate);

        horizontalChartView.hideLeftRightGroup();
        final ChartView chartView = (ChartView) horizontalChartView.getChartView();

//        chartView.horizontalChartView = horizontalChartView;
//        horizontalChartView.setList(ChartModelManager.getAllChartModelMapTools(activity));
//        horizontalChartView.setStyle(ChartView.HALF_YEAR);
        timeSelectorManager.setSelected(1);
        timeSelectorManager.setOnTimeItemSelectedListener(new OnTimeItemSelectedListener() {

            @Override
            public void onTimeSelected(TimeText timeText, final int position) {

            	
            	downloading_pd.show();
            	handler.post(new Runnable(){
//                new Thread(){
            		@Override
            		public void run() {
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
            			downloading_pd.dismiss();
//            			handler.sendEmptyMessage(-1);
            		}});
            }
        });

    }

    Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			downloading_pd.dismiss();
			super.handleMessage(msg);
		}
    	
    };

    List<DashBoardModel> accountDashBoardModes = new ArrayList<DashBoardModel>();

    @Override
    protected void loadData() {
        updateUI(TimeUtil.nowTimeMillis());
    }

    /**
     * Loans 获取数据
     * 
     * @param accountList
     * @param dstM
     * @return
     */
    private SyncDashboardDataAmount loansSyncDashBoard(List<AccountsModel> accountList, long dstM) {
        SyncDashboardDataAmount syncDashBoardDataAmount = new SyncDashboardDataAmount();
        double loansSum = 0;
        double residueAmount = 0;
        double payedLoansAmount = 0;
        for (int i = 0; i < accountList.size(); i++) {
            List<AggregatedAccount> aggregatedAccountList = accountList.get(i)
                    .getDashboardAggregatedAccountsList();

            if (aggregatedAccountList == null || aggregatedAccountList.size() == 0) {
                continue;
            }
            for (int j = 0; j < aggregatedAccountList.size(); j++) {
                loansSum += aggregatedAccountList.get(j).getTotalAmount();
                List<DashboardDataModel> dashBoardDatalist = aggregatedAccountList.get(j)
                        .getDashboardDataList();
                if (dashBoardDatalist == null || dashBoardDatalist.size() == 0) {
                    continue;
                }
                int index = LoansManager.getDateIndex(dashBoardDatalist, dstM);
                residueAmount += dashBoardDatalist.get(index).getResidueAmount();
            }
        }

        payedLoansAmount = loansSum - residueAmount;
        syncDashBoardDataAmount.setLoansAmountSum(loansSum);
        syncDashBoardDataAmount.setResidual_Amount(residueAmount);
        syncDashBoardDataAmount.setPayedLoans_Amount(payedLoansAmount);
        return syncDashBoardDataAmount;
    }

    /**
     * 拿到account的总和
     * 
     * @param accountList
     * @param dstM
     * @return
     */
    private double accountSyncDashBoard(List<AccountsModel> accountList, long dstM) {
        double accountSum = 0;
        if (accountList == null || accountList.size() == 0) {
            return 0;
        }
        for (int i = 0; i < accountList.size(); i++) {
            List<AggregatedAccount> aggregatedAccountList = accountList.get(i)
                    .getDashboardAggregatedAccountsList();

            if (aggregatedAccountList == null || aggregatedAccountList.size() == 0) {
                continue;
            }
            for (int j = 0; j < aggregatedAccountList.size(); j++) {
                List<DashboardDataModel> dashBoardDatalist = aggregatedAccountList.get(j)
                        .getDashboardDataList();
                if (dashBoardDatalist == null || dashBoardDatalist.size() == 0) {
                    continue;
                }
                int index = Utils.getDateIndex(dashBoardDatalist, dstM);
                accountSum += dashBoardDatalist.get(index).getAccountBalance();
                lastUpdate = TimeUtil.changeFormattrString(dashBoardDatalist.get(index)
                        .getLastUpdate(), TimeUtil.dateFormat2, TimeUtil.dateFormat13);
            }
        }
        return accountSum;
    }

    /**
     * 信用卡 总和
     * 
     * @param accountList
     * @param dstM
     * @return
     */
    private double creditCardSyncDashBoard(List<AccountsModel> accountList, long dstM) {
        double accountSum = 0;
        creditPlafondNum = 0;
        if (accountList == null || accountList.size() == 0) {
            return 0;
        }
        for (int i = 0; i < accountList.size(); i++) {
            AccountsModel mAccountsModel = accountList.get(i);
            List<AggregatedAccount> aggregatedAccountList = mAccountsModel
                    .getDashboardAggregatedAccountsList();
            creditPlafondNum += mAccountsModel.getPlafond();
            if (aggregatedAccountList == null || aggregatedAccountList.size() == 0) {
                continue;
            }
            for (int j = 0; j < aggregatedAccountList.size(); j++) {
                List<DashboardDataModel> dashBoardDatalist = aggregatedAccountList.get(j)
                        .getDashboardDataList();
                if (dashBoardDatalist == null || dashBoardDatalist.size() == 0) {
                    continue;
                }
                int index = Utils.getDateIndex(dashBoardDatalist, dstM);
                accountSum += dashBoardDatalist.get(index).getWithdrawals();
            }
        }
        return accountSum;
    }

    private double prepaidCardSyncDashBoard(List<AccountsModel> accountList, long dstM) {
        double accountSum = 0;
        if (accountList == null || accountList.size() == 0) {
            return 0;
        }
        for (int i = 0; i < accountList.size(); i++) {
            List<AggregatedAccount> aggregatedAccountList = accountList.get(i)
                    .getDashboardAggregatedAccountsList();
            if (aggregatedAccountList == null || aggregatedAccountList.size() == 0) {
                continue;
            }
            for (int j = 0; j < aggregatedAccountList.size(); j++) {
                List<DashboardDataModel> dashBoardDatalist = aggregatedAccountList.get(j)
                        .getDashboardDataList();
                if (dashBoardDatalist == null || dashBoardDatalist.size() == 0) {
                    continue;
                }
                int index = Utils.getDateIndex(dashBoardDatalist, dstM);
                accountSum += dashBoardDatalist.get(index).getAccountBalance();
                if(Double.isNaN(accountSum)){
                    if(sbItaly){
                        accountSum = aggregatedAccountList.get(j).getAvailableBalance();
                    }else{
                        accountSum = 0;
                    }
                }
            }
        }
        return accountSum;
    }

    /**
     * 传时间格式为long
     * 
     * @param date
     */
    private void updateUI(long date) {
        App.app.isFrist = false;
        loansDataAmount = loansSyncDashBoard(Contants.loansAccounts, date);
        accountSum = accountSyncDashBoard(Contants.baseAccounts, date);
        creditCardNum = creditCardSyncDashBoard(Contants.creditCardAccounts, date);
         prepaidCardsSum = prepaidCardSyncDashBoard(Contants.prepaidCardAccounts, date);
        investmentsSum = Contants.investmentsSyncDashBoard(Contants.investmentAccounts, date);
        // loans百分比
        payedPercentages = loansPerecntages(loansDataAmount.getPayedLoans_Amount(),loansDataAmount.getLoansAmountSum());
        residualPercentages = loansPerecntages(loansDataAmount.getResidual_Amount(),loansDataAmount.getLoansAmountSum());
        // 圈圈里的百分比
        accountPercentages = getPercentages(accountSum, investmentsSum, prepaidCardsSum, accountSum);
        creditCardPercentages = getPercentages(accountSum, investmentsSum, prepaidCardsSum,Math.abs(creditCardNum));
        prepaidCardsPercentages = getPercentages(accountSum, investmentsSum, prepaidCardsSum,prepaidCardsSum);
        investmentsPercentages = getPercentages(accountSum, investmentsSum, prepaidCardsSum,investmentsSum);

        double total_assets = accountSum + prepaidCardsSum + investmentsSum + creditCardNum;
        totalAssets = Utils.formatMoney(total_assets,
                activity.getResources().getString(R.string.dollar), true, true, false, false, true);
        Contants.total_assets = totalAssets;
        setUI();
    }

    /**
     * 求百分比
     * 
     * @param accountSum
     * @param depositsInvestmentSum
     * @param prepaidCardSum
     * @param creditCards
     * @param divider 求谁的百分比他就是谁的sum
     * @return
     */
    private String getPercentages(double accountSum, double depositsInvestmentSum,double prepaidCardSum, double divider) {
        double all_item_sum = accountSum + depositsInvestmentSum + prepaidCardSum;
        double percentages = divider / all_item_sum * 100;
        String value = String.format(Locale.US, "%.0f", percentages);
        value += "%";
        return value;
    }

    LegendItemText legendItemText;

    @Override
    protected void setUI() {
        dashBoardManager.clear();
        String welcomeText = " ";
        if (!Contants.getUserInfo.getCustomerName().equals("")
                && Contants.getUserInfo.getCustomerName() != null) {
            welcomeText = Contants.getUserInfo.getCustomerName();
            welcomeText += " ";
            if (!Contants.getUserInfo.getCustomerSurname().equals("")
                    && Contants.getUserInfo.getCustomerSurname() != null) {
                welcomeText += Contants.getUserInfo.getCustomerSurname();
            }
        }

        dashBoardManager.setWellcomeText(activity.getString(R.string.ss_welcome) + welcomeText);
        dashBoardManager.setTotalAssets(totalAssets);
        dashBoardManager.setLastUpdate(lastUpdate);
        // 添加 investments
        LegendItemText legendItemText = new LegendItemText();
        WheelButtonItem centerChartButton = new WheelButtonItem();
        Drawable closeDrawable = activity.getResources().getDrawable(R.drawable.investments_legend_closed);
        Drawable openDrawable = activity.getResources().getDrawable(R.drawable.investments_legend_opened);
        if (Contants.investmentAccounts != null && Contants.investmentAccounts.size() != 0) {
            String investmentAmount = Utils.formatMoney(investmentsSum, activity.getResources().getString(R.string.dollar), true, true, false, false, true);
            legendItemText.setData(activity.getResources().getString(R.string.legend_investments),investmentAmount, closeDrawable, openDrawable);
            // 添加Investments的圆球;k
            centerChartButton.id = INVESTMENTS;
            centerChartButton.text = investmentsPercentages;
            centerChartButton.textColor = activity.getResources().getColor(R.color.white);
            centerChartButton.weight = (int)investmentsSum;
            centerChartButton.backgroundColor = activity.getResources().getColor(
                    R.color.legend_investments_color);
            dashBoardManager.addLegendItem(legendItemText);
            dashBoardManager.addCircleButton(centerChartButton);
        } else {
            activity.menu.hideMenuItem(activity.menu.investments);
        }

        // 新键accounts
        if (Contants.baseAccounts != null && Contants.baseAccounts.size() != 0) {
            legendItemText = new LegendItemText();
            closeDrawable = activity.getResources().getDrawable(R.drawable.accounts_legend_closed);
            openDrawable = activity.getResources().getDrawable(R.drawable.accounts_legend_opened);
            String accountsAmount = Utils.formatMoney(accountSum, activity.getResources()
                    .getString(R.string.dollar), true, true, false, false, true);
            legendItemText.setData(activity.getResources().getString(R.string.legend_accounts),
                    accountsAmount, closeDrawable, openDrawable);
            // 添加accounts的圆球
            centerChartButton = new WheelButtonItem();
            centerChartButton.id = ACCOUNTS;
            centerChartButton.text = accountPercentages;
            centerChartButton.textColor = activity.getResources().getColor(R.color.white);
            centerChartButton.weight = (int)accountSum;
            centerChartButton.backgroundColor = activity.getResources().getColor(
                    R.color.legend_accounts_color);
            dashBoardManager.addLegendItem(legendItemText);
            dashBoardManager.addCircleButton(centerChartButton);
        } else {
            activity.menu.hideMenuItem(activity.menu.accounts);
        }

        if (Contants.prepaidCardAccounts != null && Contants.prepaidCardAccounts.size() != 0) {
            // 新键prepaid cards
            legendItemText = new LegendItemText();
            closeDrawable = activity.getResources().getDrawable(R.drawable.prepaid_legend_closed);
            openDrawable = activity.getResources().getDrawable(R.drawable.prepaid_legend_opened);
            String prepaidCardsAmount = Utils.formatMoney(prepaidCardsSum, activity.getResources()
                    .getString(R.string.dollar), true, true, false, false, true);
            legendItemText.setData(
                    activity.getResources().getString(R.string.legend_prepaid_cards),
                    prepaidCardsAmount, closeDrawable, openDrawable);
            BankBitmapDrawable bankBitmapDrawable = new BankBitmapDrawable(
                    (BitmapDrawable)openDrawable, BankBitmapDrawable.drawable_type_rect);
            bankBitmapDrawable.setMainColor(BankBitmapDrawable.COLOR_PREPARD_CARD);
            bankBitmapDrawable.setBitmapLevel(0);
            // 添加prepaidCard的圆球
            centerChartButton = new WheelButtonItem();
            centerChartButton.id = PREPAIDCARDS;
            centerChartButton.text = prepaidCardsPercentages;
            centerChartButton.textColor = activity.getResources().getColor(R.color.white);
            centerChartButton.weight = (int)prepaidCardsSum;
            centerChartButton.backgroundColor = bankBitmapDrawable.getColor();
            dashBoardManager.addLegendItem(legendItemText);
            dashBoardManager.addCircleButton(centerChartButton);
        }

        // 新键credit cards
        if (Contants.creditCardAccounts != null && Contants.creditCardAccounts.size() != 0) {
            legendItemText = new LegendItemText();
            closeDrawable = activity.getResources().getDrawable(R.drawable.credit_legend_closed);
            openDrawable = activity.getResources().getDrawable(R.drawable.credit_legend_opened);
            String creditCardsAmount = Utils.formatMoney(creditCardNum, activity.getResources()
                    .getString(R.string.dollar), true, true, false, false, true);
            legendItemText.setData(activity.getResources().getString(R.string.legend_credit_cards),
                    creditCardsAmount, closeDrawable, openDrawable);
            // 添加credit card的中心半球
            WheelButton wheelButton = dashBoardManager.getWheelButton();
            wheelButton.setmCenterUserWeightCount((float)(accountSum + investmentsSum + prepaidCardsSum));
            centerChartButton = new WheelButtonItem();
            if ("NaN%".equals(creditCardPercentages)) {
                centerChartButton.text = "";
            } else {
                centerChartButton.text = creditCardPercentages;
            }
            centerChartButton.id = CREDITCARDS;
            centerChartButton.textColor = activity.getResources().getColor(R.color.white);
            centerChartButton.weight = (int)creditCardNum;
            centerChartButton.backgroundColor = activity.getResources().getColor(
                    R.color.legend_credit_cards_color);
            dashBoardManager.addLegendItem(legendItemText);
            // dashBoardManager.getWheelButton().setmCenterUserWeightCount((float)creditPlafondNum);
            dashBoardManager.addCenterButton(centerChartButton);
        }
        if (Contants.cardAccounts == null || Contants.cardAccounts.size() == 0) {
            activity.menu.hideMenuItem(activity.menu.cards);
        }

        // 新键loans
        legendItemText = new LegendItemText();
        closeDrawable = activity.getResources().getDrawable(R.drawable.loanlegend_closed);
        openDrawable = activity.getResources().getDrawable(R.drawable.loanlegend_opened);
        String loansAmount = Utils.formatMoney(loansDataAmount.getLoansAmountSum(), activity
                .getResources().getString(R.string.dollar), true, true, false, false, true);
        legendItemText.setData(activity.getResources().getString(R.string.legend_total_loans),
                loansAmount, closeDrawable, openDrawable);
        // 新建下方的进度条
        BitmapDrawable drawable = (BitmapDrawable)activity.getResources().getDrawable(
                R.drawable.loanlegend_opened);

        BankBitmapDrawable bankBitmapDrawableEnd = new BankBitmapDrawable(drawable,
                BankBitmapDrawable.drawable_type_rect);
        bankBitmapDrawableEnd.setBitmapLevel(10);
        bankBitmapDrawableEnd.setMainColor(BankBitmapDrawable.COLOR_LOANS);

        String title = activity.getString(R.string.padyed_out_loans);
        dashBoardManager.setLoansProportionBarTitle(title);
        String payedLoans_Amount = Utils.formatMoney(loansDataAmount.getPayedLoans_Amount(),
                activity.getResources().getString(R.string.dollar), true, true, false, false, true);
        int getPayedLoans_Amount = (int)loansDataAmount.getPayedLoans_Amount();
        int getResidual_Amount = (int)loansDataAmount.getResidual_Amount();

        String residual_Amount = Utils.formatMoney(getResidual_Amount, activity.getResources()
                .getString(R.string.dollar), true, true, false, false, true);
        String value = payedLoans_Amount + " - " + residual_Amount;
        dashBoardManager.setLoansProportionBarValue(value);
        if (getPayedLoans_Amount > 0 && getResidual_Amount > 0) {
            dashBoardManager.addLegendItem(legendItemText);
            if (getPayedLoans_Amount > 0) {
                dashBoardManager.addProportionBarItem(payedPercentages, getPayedLoans_Amount,
                        drawable);
            }
            if (getResidual_Amount > 0) {
                dashBoardManager.addProportionBarItem(residualPercentages, getResidual_Amount,
                        bankBitmapDrawableEnd);
            }
        } else {
            dashBoardManager.hideProportionBar();
        }
        if (Contants.loansAccounts == null || Contants.loansAccounts.size() == 0) {
            activity.menu.hideMenuItem(activity.menu.loans);
        }

        dashBoardManager.setProportionBarClickListener(this);
        dashBoardManager.generateMonthSelector();
        dashBoardManager.setOnDashboardClickListener(this);
        dashBoardManager.generateTimeButton();
        dashBoardManager.generateLegend();
        dashBoardManager.selectedTime(defaultMonth);

    }

    private String loansPerecntages(double divisor, double dividend) {
        double result = (divisor / dividend) * 100;
        String value = String.format(Locale.US, "%.0f", result);
        value += "%";
        return value;
    }

    @Override
    public void onClick(View v) {
        if (v == dashBoardManager.proportionBar) {
            mainManager.showLoans(false, null);
        }
    }

}
