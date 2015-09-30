
package com.accenture.manager;

import java.util.ArrayList;
import java.util.List;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;

import com.accenture.manager.protocol.RotateBankViewManager;
import com.accenture.mbank.BaseActivity;
import com.accenture.mbank.MainActivity;
import com.accenture.mbank.R;
import com.accenture.mbank.logic.GetDashBoardDataJson;
import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.model.DashBoardModel;
import com.accenture.mbank.model.DashboardDataModel;
import com.accenture.mbank.model.GetDashBoardDataResponseModel;
import com.accenture.mbank.net.HttpConnector;
import com.accenture.mbank.net.ProgressOverlay;
import com.accenture.mbank.net.ProgressOverlay.OnProgressEvent;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.LogManager;
import com.accenture.mbank.util.TimeUtil;
import com.accenture.mbank.util.Utils;
import com.accenture.mbank.view.GoogleAnalya;
import com.accenture.mbank.view.InnerScrollView;
import com.accenture.mbank.view.InnerScrollView.OnInnerScrollListener;
import com.accenture.mbank.view.RotateTitleView;
import com.accenture.mbank.view.table.AccountsRotateTableView;
import com.accenture.mbank.view.table.LineFormView;
import com.accenture.mbank.view.table.RotatTableView.OnSlidListener;
import com.accenture.mbank.view.table.RotateTableViewWithButton.OnButtonClickListener;
import com.accenture.mbank.view.GoogleAnalya;

public class AccountRotateLayoutManager extends RotateBankViewManager {

    Handler handler;
    public AccountRotateLayoutManager() {
        handler = new Handler();
    }

    public ViewGroup getContainer() {
        return container;
    }

    public void setContainer(ViewGroup container) {
        this.container = container;
    }

    int dashboardAccounts = 0;

    private InnerScrollView scroll;

    @Override
    public void onShow() {
    	
        MainActivity mainActivity = (MainActivity)container.getContext();
       
        int accounts = mainActivity.setting.getDashboardAccounts() + 1;
        if (accounts != dashboardAccounts) {

            if (BaseActivity.isOffline) {

                if (container.getChildCount() <= 0) {

                    for (int i = 0; i < 4; i++) {

                        LayoutInflater layoutInflater = LayoutInflater.from(container.getContext());
                        final RotateTitleView rTitleView = (RotateTitleView)layoutInflater.inflate(R.layout.rotate_title_view, null);
                        rTitleView.init();
                        rTitleView.setTitleIcon(R.drawable.accounts_icon);
                        
                        container.addView(rTitleView);
                        final AccountsRotateTableView rotatTableView0 = new AccountsRotateTableView(container.getContext());
                        container.addView(rotatTableView0);
                        rotatTableView0.setCount(5);
                        int rotateImageRes = getRotateImageRes(accounts);
                        rotatTableView0.setRotatResource(R.drawable.accounts_slider,R.drawable.cards_5place, R.drawable.accounts_arrow);
                        rotatTableView0.parentScrollView = (ScrollView)container.getParent();
                        rotatTableView0.setButtonImage(R.drawable.account_btn_transaction);
                        rotatTableView0.setButtonImageOver(R.drawable.account_btn_transaction_over);

                    }

                }
            } else {
                getDashBoardData();
                dashboardAccounts = accounts;
            }
        }

    }

    List<DashBoardModel> list = new ArrayList<DashBoardModel>();

    public void getDashBoardData() {
        ProgressOverlay overlay = new ProgressOverlay(container.getContext());
        overlay.show("", new OnProgressEvent() {

            @Override
            public void onProgress() {
                list.clear();
                for (AccountsModel accountsModel : Contants.baseAccounts) {
                    String postData = GetDashBoardDataJson.getDashBoardDataReportProtocal(
                            Contants.publicModel, dashboardAccounts, 2,
                            accountsModel.getAccountCode());
                    HttpConnector httpConnector = new HttpConnector();
                    String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url,
                            postData, container.getContext());
                    GetDashBoardDataResponseModel getDashBoardData = GetDashBoardDataJson
                            .ParseGetDashBoardDataResponse(httpResult);
                    list.add(getDashBoardData.getDashboardsList().get(0));
                }

                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        setAccount();
                        if (needShowChart) {
                            showChart();
                        }
                    }
                });
            }
        });

    }

    void setAccount() {
        if (list != null && list.size() > 0) {
            container.removeAllViews();

            for (DashBoardModel dashBoardModel : list) {
                final DashBoardModel dashBoardModel2 = dashBoardModel;
                LayoutInflater layoutInflater = LayoutInflater.from(container.getContext());
                final RotateTitleView rTitleView = (RotateTitleView)layoutInflater.inflate(R.layout.rotate_title_view, null);
                rTitleView.init();
                rTitleView.setTitleIcon(R.drawable.accounts_icon);
                rTitleView.setTitleText(dashBoardModel.getPersonalizedName());
                rTitleView.setUpdateTitle(dashBoardModel.getDashboardDataList().get(0).getLastUpdate());
//                long time = System.currentTimeMillis();
//                String nowTime = TimeUtil.getDateString(time, TimeUtil.dateFormat5);                
//                rTitleView.setUpdateTitle(nowTime);
                
                container.addView(rTitleView);
                final AccountsRotateTableView rotatTableView0 = new AccountsRotateTableView(container.getContext());
                container.addView(rotatTableView0);
                rotatTableView0.setCount(dashboardAccounts);
                int rotateImageRes = getRotateImageRes(dashboardAccounts);
                rotatTableView0.setRotatResource(R.drawable.accounts_slider, rotateImageRes,R.drawable.accounts_arrow);
                rotatTableView0.parentScrollView = (ScrollView)container.getParent();
                rotatTableView0.setAccountBalanceValue(dashBoardModel.getPersonalizedName());
                rotatTableView0.setButtonImage(R.drawable.account_btn_transaction);
                rotatTableView0.setButtonImageOver(R.drawable.account_btn_transaction_over);
                String accountBalanceValue = Utils.notPlusGenerateFormatMoney(container.getContext().getResources().getString(R.string.dollar),dashBoardModel.getAccountBalance());
                rotatTableView0.setAccountBalanceValue(accountBalanceValue);

                String avaString = Utils.notPlusGenerateFormatMoney(container.getContext().getResources().getString(R.string.dollar),dashBoardModel.getAvailableBalance());
                rotatTableView0.setAvailableBalanceValue(avaString);
                if (dashBoardModel2 != null && dashBoardModel2.getDashboardDataList() != null && dashBoardModel2.getDashboardDataList().size() > 0) {
                    setDashboardUi(dashBoardModel2, rotatTableView0, rTitleView,0);
                }
                rotatTableView0.setOnSlidListener(new OnSlidListener() {
                    @Override
                    public void onSlid(View v, int index) {

                        if (index != 0) {
                            rotatTableView0.setAvailableBalanceValue(container.getContext().getResources().getString(R.string.not_able));
                        } else {
                            String avaString = Utils.notPlusGenerateFormatMoney(container.getContext().getResources().getString(R.string.dollar),dashBoardModel2.getAvailableBalance());
                            rotatTableView0.setAvailableBalanceValue(avaString);
                        }
                        setDashboardUi(dashBoardModel2, rotatTableView0, rTitleView, index);
                    }

                });
                rotatTableView0.setOnButtonClickListener(new OnButtonClickListener() {

                    @Override
                    public void onClick(View v) {
                        MainActivity mainActivity = (MainActivity)container.getContext();
                        mainActivity.accountsLayout.setAnimateTo(dashBoardModel2);
                        mainActivity.showTab(1);
                    }
                });

            }
        }
    }

    public static int getRotateImageRes(int count) {
        int rotateImageRes = R.drawable.cards_5place;
        switch (count) {
            case 5:
                rotateImageRes = R.drawable.cards_5place;
                break;
            case 6:
                rotateImageRes = R.drawable.cards_6place;
                break;
            case 7:
                rotateImageRes = R.drawable.cards_7place;
                break;
            case 8:
                rotateImageRes = R.drawable.cards_8place;
                break;
            case 9:
                rotateImageRes = R.drawable.cards_9place;
                break;
            case 10:
                rotateImageRes = R.drawable.cards_10place;
                break;
            case 11:
                rotateImageRes = R.drawable.cards_11place;
                break;
            case 12:
                rotateImageRes = R.drawable.cards_12place;
                break;
            case 13:
                rotateImageRes = R.drawable.cards_13place;
                break;
            default:
                break;
        }
        return rotateImageRes;
    }

    private void setDashboardUi(final DashBoardModel dashBoardModel2,final AccountsRotateTableView rotatTableView0, RotateTitleView titleView, int index) {
        List<DashboardDataModel> list = dashBoardModel2.getDashboardDataList();
        DashboardDataModel dashboardDataModel = list.get(index);

        String deposite = Utils.notPlusGenerateFormatMoney(container.getContext().getResources().getString(R.string.dollar), dashboardDataModel.getDeposits());
        rotatTableView0.setDepositValue(deposite);
        String width = Utils.notPlusGenerateFormatMoney(container.getContext().getResources().getString(R.string.dollar), dashboardDataModel.getWithdrawals());
        rotatTableView0.setWidthdrawalsValue(width);

        double value = dashboardDataModel.getAccountBalance();
        String accountBalanceValue = Utils.notPlusGenerateFormatMoney(container.getContext().getResources().getString(R.string.dollar), value);

        rotatTableView0.setAccountBalanceValue(accountBalanceValue);
        String date = TimeUtil.changeFormattrString(dashboardDataModel.getLastUpdate(),TimeUtil.dateFormat2, TimeUtil.dateFormat5);
        titleView.setUpdateTitle(container.getContext().getResources().getString(R.string.account_balance_to)+ date);
    }

    boolean needShowChart;

    @Override
    public void showChart() {
    	GoogleAnalya.getInstance().getGaInstance(container.getContext(),"view.accounts.graph");
        if (BaseActivity.isOffline) {
            LineFormView lineFormView = new LineFormView(container.getContext());
            chartLayout.removeAllViews();
            chartLayout.addView(lineFormView);
            return;
        }
        if (list.size() == 0) {
            needShowChart = true;
            return;
        }
        int index = getVisiableRotateViewIndex();
        this.chartLayout.removeAllViews();
        LineFormView child = new LineFormView(chartLayout.getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT);
        chartLayout.addView(child, params);

        DashBoardModel dashBoardModel = list.get(index);
        List<DashboardDataModel> dashboardList = dashBoardModel.getDashboardDataList();

        List<Double> yValue = new ArrayList<Double>();
        List<String> xValue = new ArrayList<String>();

        for (int i = dashboardList.size() - 1; i >= 0; i--) {
            DashboardDataModel dashboardDataModel = dashboardList.get(i);
            double money = Utils.changeMoney(dashboardDataModel.getWithdrawals()
                    + dashboardDataModel.getDeposits());
            yValue.add(money);
            String time = TimeUtil.changeFormattrString(chartLayout.getContext(),dashboardDataModel.getLastUpdate(),
                    TimeUtil.dateFormat2, TimeUtil.detaFormat6);
            xValue.add(time);
        }
        child.xYValues = yValue;
        child.xValue = xValue;
        child.initYValue();
        child.setTextLeft(chartLayout.getContext().getString(R.string.account_rotate_title_l));//"DIFFERENCE DEPOSITS/WITHDRAWALS");
        child.setTextRight(dashBoardModel.getPersonalizedName());

        // dashboardList.get(i);

        // dashboardAccounts

    }

    // @Override
    // public void onScrollChanged(int l, int t, int oldl, int oldt) {
    //
    // MainActivity mainActivity = (MainActivity)container.getContext();
    // int index = getVisiableRotateViewIndex();
    // LogManager.d("index" + index);
    // if (index % 2 == 0) {
    // mainActivity.setCanOrientation(true);
    // } else {
    // mainActivity.setCanOrientation(false);
    // }
    //
    // }
}
