
package com.act.mbanking.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.act.mbanking.manager.LegendManager.OnLegendItemSelectedListener;
import com.act.mbanking.manager.TimeSelectorManager.OnTimeItemSelectedListener;
import com.act.mbanking.manager.view.BankBitmapDrawable;
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
import com.act.mbanking.view.ProportionBar;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

public class LoansManager extends MainMenuSubScreenManager implements OnTimeItemSelectedListener,
        OnClickListener, OnLegendItemSelectedListener {

    private LinearLayout loansContainer;

    private LegendManager legendManager;

    private TimeSelectorManager timeSelectorManager;

    private int defaultMonth = 5;

    private Handler myHandler;

    private String lastUpdate;

    private String recentlyDate;

    TextView total_assetsTextView;

    TextView lastUpdateTextView;

    TextView totalTitle;
    
    private GoogleAnalytics mGaInstance;
    
  	private Tracker mGaTracker1;

    public LoansManager(MainActivity activity) {
        super(activity);
        myHandler = new Handler();
        long nowTime = TimeUtil.nowTimeMillis();
        lastUpdate = TimeUtil.getDateString(nowTime, TimeUtil.dateFormat13);
    }

    @Override
    protected void init() {

        layout = (ViewGroup)activity.findViewById(R.id.loans);
        setLeftNavigationText(activity.getResources().getString(R.string.dashboard));
        loansContainer = (LinearLayout)layout.findViewById(R.id.loans_container);

        legendManager = new LegendManager(activity, layout);
        legendManager.setOnLegendItemSelectedListener(this);
        timeSelectorManager = new TimeSelectorManager(activity, layout);
        totalTitle = (TextView)layout.findViewById(R.id.total);
        total_assetsTextView = (TextView)layout.findViewById(R.id.money);
        lastUpdateTextView = (TextView)layout.findViewById(R.id.update_time);
    }

    @Override
    protected void onShow(Object object) {
    	mGaInstance = GoogleAnalytics.getInstance(activity);
        mGaTracker1 = mGaInstance.getTracker(Contants.TRACKER_ID);
        mGaTracker1.sendView("view.finances"); 
        super.onShow(object);
        defaultMonth = 5;
        loadData();
        ProgressOverlay progressOverlay = new ProgressOverlay(activity);
        progressOverlay.show(activity.getResources().getString(R.string.waiting),
                new OnProgressEvent() {
                    @Override
                    public void onProgress() {
                        SynchDashBoardModel synchDashBoard = synchDashBoard(
                                AggregatedAccountType.FINANCING,
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
    public boolean onLeftNavigationButtonClick(View v) {

        mainManager.showAggregatedView(false, null);
        return true;
    }

    
    public void onLoadChartData(Object o){
    	allChartModelMapTools=ChartModelManager.getAllChartModelMapTools(activity);
        targetChartModelMapTools = ChartModelManager.getLoansChartModels();
    }
    
        List<ChartModelMapTool> allChartModelMapTools;// = ChartModelManager.getAllChartModelMapTools(activity);
        List<ChartModelMapTool> targetChartModelMapTools;// = ChartModelManager.getLoansChartModels();
    HorizontalChartView horizontalChartView;
    public void setChartData(){
      horizontalChartView.setList(targetChartModelMapTools);
      horizontalChartView.setAllList(allChartModelMapTools);
      myHandler.post(new Runnable() {
          @Override
          public void run() {
              horizontalChartView.setStyle(ChartView.HALF_YEAR);
          }
      });
    }
    @Override
    protected void showChart(ViewGroup chartLayout) {
    	mGaInstance = GoogleAnalytics.getInstance(activity);
        mGaTracker1 = mGaInstance.getTracker(Contants.TRACKER_ID);
        mGaTracker1.sendView("view.chart.finances"); 

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
        timeSelectorManager.setSelectedRes(R.drawable.timescale_laons_on);
        timeSelectorManager.setUnSelectedRes(R.drawable.timescale_btn_on);
        timeSelectorManager.generateButton();
        horizontalChartView.init();
        String total = Utils.formatMoney(totalLoans,
                activity.getResources().getString(R.string.dollar), true, true, false, false, true);

        horizontalChartView.setTotalAssetText(activity.getString(R.string.ss_total_loans)+" " + total);
        horizontalChartView.setLastUpdateOnText(lastUpdate);
        final ChartView chartView = (ChartView) horizontalChartView.getChartView();


//        chartView.horizontalChartView = horizontalChartView;
//        horizontalChartView.setList(targetChartModelMapTools);
//        horizontalChartView.setAllList(allChartModelMapTools);
        horizontalChartView.setRightText(activity.getResources()
                .getString(R.string.loans));
//        horizontalChartView.setStyle(ChartView.HALF_YEAR);
        timeSelectorManager.setSelected(1);
        timeSelectorManager.setOnTimeItemSelectedListener(new OnTimeItemSelectedListener() {

            @Override
            public void onTimeSelected(TimeText timeText, int position) {

                int style = 0;
                switch (position) {
                    case 0:
                    	chartView.line=1;
                        style = ChartView.YEAR;
                        break;
                    case 1:
                    	chartView.line=1;
                        style = ChartView.HALF_YEAR;
                        break;
                    case 2:
                    	chartView.line=2;
                        style = ChartView.MONTH;
                        break;
                    case 3:
                    	chartView.line=2;
                        style = ChartView.WEEK;
                        break;
                    case 4:
                    	chartView.line=2;
                        style = ChartView.DAY;
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
    private double totalPortfolioSum = 0;

    /**
     * Loans Title上面的total Loans 总和
     */
    private double totalLoans = 0;

    /**
     * 每个账号还欠多少钱
     */
    private double residualAmountSum = 0;

    /**
     * 多个residualAmountSum相加 == 还欠多少钱
     */
    private double outstandings = 0;

    private int lastIndex;

    ProportionBarManager proportionBarManager;

    public void updateUI(long date) {
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        int size = Contants.loansAccounts.size();
        totalLoans = 0;
        outstandings = 0;
        clear();
        BitmapDrawable cycleBitmapDrawable = (BitmapDrawable)getDrawable(R.drawable.loanlegend_closed);
        BitmapDrawable openBitmapDrawable = (BitmapDrawable)getDrawable(R.drawable.loanlegend_opened);

        BitmapDrawable drawable = (BitmapDrawable)activity.getResources().getDrawable(
                R.drawable.loanlegend_opened);
        BitmapDrawable drawable1 = (BitmapDrawable)activity.getResources().getDrawable(
                R.drawable.loanlegend_opened);

        for (int i = 0; i < size; i++) {
            BankBitmapDrawable bankBitmapDrawableEnd = new BankBitmapDrawable(drawable1,
                    BankBitmapDrawable.drawable_type_rect);
            bankBitmapDrawableEnd.setBitmapLevel(10);
            bankBitmapDrawableEnd.setMaxLevel(size);
            bankBitmapDrawableEnd.setMainColor(BankBitmapDrawable.COLOR_LOANS);

            residualAmountSum = 0;
            totalPortfolioSum = 0;
            List<AggregatedAccount> aggregatedAccountList = Contants.loansAccounts.get(i).getDashboardAggregatedAccountsList();
            if (aggregatedAccountList == null || aggregatedAccountList.size() == 0) {
                continue;
            }
            for (int j = 0; j < aggregatedAccountList.size(); j++) {
                totalPortfolioSum += aggregatedAccountList.get(j).getTotalAmount();
                List<DashboardDataModel> dashBoardDatalist = aggregatedAccountList.get(j).getDashboardDataList();
                if (dashBoardDatalist == null || dashBoardDatalist.size() == 0) {
                    continue;
                }
                int index = getDateIndex(dashBoardDatalist, date);
                lastUpdate = TimeUtil.changeFormattrString(dashBoardDatalist.get(index).getLastUpdate(), TimeUtil.dateFormat2, TimeUtil.dateFormat13);
                residualAmountSum += dashBoardDatalist.get(index).getResidueAmount();
            }
            outstandings += residualAmountSum;
            totalLoans += totalPortfolioSum;
            BankBitmapDrawable cycle = new BankBitmapDrawable(cycleBitmapDrawable,
                    BankBitmapDrawable.drawable_type_half_circle);
            cycle.setMainColor(BankBitmapDrawable.COLOR_LOANS);
            cycle.setBitmapLevel(i + 1);
            cycle.setMaxLevel(size);

            BankBitmapDrawable open = new BankBitmapDrawable(openBitmapDrawable,
                    BankBitmapDrawable.drawable_type_rect);
            open.setMainColor(BankBitmapDrawable.COLOR_LOANS);
            open.setBitmapLevel(i + 1);
            open.setMaxLevel(size);
            LegendItemText legendItemText = new LegendItemText();
            legendItemText.cycleDrawable = cycle;
            legendItemText.openDrawable = open;
            legendItemText.titleText = Contants.loansAccounts.get(i).getAccountAlias();
            legendItemText.setType(i + "");
            double payed = totalPortfolioSum - residualAmountSum;
            String payedAmount = Utils.formatMoney(payed,activity.getResources().getString(R.string.dollar), true, true, false, false,true);
            legendItemText.openText = payedAmount;
            legendManager.addLegendItem(legendItemText);

            View v = layoutInflater.inflate(R.layout.proportionbar_layout, null);
            loansContainer.addView(v);
            proportionBarManager = new ProportionBarManager(activity, loansContainer, v);
            BankBitmapDrawable bankBitmapDrawable = new BankBitmapDrawable(drawable,BankBitmapDrawable.drawable_type_rect);
            bankBitmapDrawable.setBitmapLevel(i + 1);
            bankBitmapDrawable.setMaxLevel(size);
            bankBitmapDrawable.setMainColor(BankBitmapDrawable.COLOR_LOANS);
            String title =activity.getString(R.string.ss_payed)+ " " + Contants.loansAccounts.get(i).getAccountAlias() + " / "+ activity.getString(R.string.ss_outstanding)+ " " + Contants.loansAccounts.get(i).getAccountAlias();
            proportionBarManager.loansTitleTextView.setText(title);
            String value = payedAmount+ " - " + Utils.formatMoney(residualAmountSum,activity.getResources().getString(R.string.dollar), true, true, false,false, true);

            proportionBarManager.loansValueTextView.setText(value);

            double payedLoan = totalPortfolioSum - residualAmountSum;
            String payedPercentages = loansPerecntages(payedLoan, totalPortfolioSum);
            proportionBarManager.proportionBar.addItem(payedPercentages, (int)payedLoan,
                    bankBitmapDrawable);
            String residualPercentages = loansPerecntages(residualAmountSum, totalPortfolioSum);
            proportionBarManager.proportionBar.addItem(residualPercentages, (int)residualAmountSum,
                    bankBitmapDrawableEnd);
            Contants.loansAccounts.get(i).setPayedloan(payedLoan);
            Contants.loansAccounts.get(i).setOutstandingloan(residualAmountSum);
            proportionBarManager.proportionBar.setTag(Contants.loansAccounts.get(i));
            proportionBarManager.proportionBar.setOnClickListener(this);

            if (i == size) {
                lastIndex = i;
            }
        }

        // 添加outStanding
        BankBitmapDrawable cycle = new BankBitmapDrawable(cycleBitmapDrawable,
                BankBitmapDrawable.drawable_type_half_circle);
        cycle.setMainColor(BankBitmapDrawable.COLOR_LOANS);
        cycle.setBitmapLevel(10);
        cycle.setMaxLevel(size);
        BankBitmapDrawable open = new BankBitmapDrawable(openBitmapDrawable,
                BankBitmapDrawable.drawable_type_rect);
        open.setMainColor(BankBitmapDrawable.COLOR_LOANS);
        open.setBitmapLevel(10);
        open.setMaxLevel(size);
        LegendItemText legendItemText = new LegendItemText();
        legendItemText.cycleDrawable = cycle;
        legendItemText.openDrawable = open;
        legendItemText.titleText = activity.getResources().getString(R.string.loans_total_outstanding);
        legendItemText.openText = Utils.formatMoney(outstandings, activity.getResources().getString(R.string.dollar), true, true, false, false, true);

        legendManager.addLegendItem(legendItemText);

        legendManager.generateUi();
        timeSelectorManager.generateDefaultVerticalTimes();
        timeSelectorManager.setSelectStateRes(R.drawable.timescale_laons_on,R.drawable.timescale_btn_on);
        timeSelectorManager.generateButton();
        timeSelectorManager.setSelected(defaultMonth);
        timeSelectorManager.setOnTimeItemSelectedListener(this);
        totalTitle.setText(activity.getResources().getString(R.string.loans_total_title));
        lastUpdateTextView.setText(lastUpdate);
        total_assetsTextView.setText(Utils.formatMoney(totalLoans, activity.getResources().getString(R.string.dollar), true, true, false, false, true));
    }

    private String loansPerecntages(double divisor, double dividend) {
        double result = (divisor / dividend) * 100;
        String value = String.format(Locale.US, "%.0f", result);
        value += "%";
        return value;
    }

    public void clear() {
        legendManager.clear();
        timeSelectorManager.clear();
        loansContainer.removeAllViews();
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

    public static int getDateIndex(List<DashboardDataModel> dashboardData, long dstDate) {
        long m = 0;
        int index = 0;
        for (int i = 0; i < dashboardData.size(); i++) {
            String str = dashboardData.get(i).getLastUpdate();
            long nowM = Math.abs(dstDate - TimeUtil.getMillis(str));
            if (i == 0 || nowM < m) {
                m = nowM;
                index = i;
                continue;
            }
        }
        return index;
    }
    
    @Override
    public void onClick(View v) {
        if (v instanceof ProportionBar) {
            mainManager.showLoanDetails(false, v.getTag());
        }
    }

    @Override
    public void onLegendItemSelected(View view, LegendItemText legendItemText, int position) {
        if (legendItemText.getType() != null) {
            position = Integer.parseInt(legendItemText.getType());
            mainManager.showLoanDetails(false, Contants.loansAccounts.get(position));
        }
    }
}
