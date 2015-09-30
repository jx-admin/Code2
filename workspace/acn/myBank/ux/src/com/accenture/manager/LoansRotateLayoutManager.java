
package com.accenture.manager;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.graphics.YuvImage;
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
import com.accenture.mbank.logic.GetFinancingInfoJson;
import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.model.DashBoardModel;
import com.accenture.mbank.model.GetFinancingInfoModel;
import com.accenture.mbank.model.InstallmentsModel;
import com.accenture.mbank.net.HttpConnector;
import com.accenture.mbank.net.ProgressOverlay;
import com.accenture.mbank.net.ProgressOverlay.OnProgressEvent;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.LogManager;
import com.accenture.mbank.util.TimeUtil;
import com.accenture.mbank.util.Utils;
import com.accenture.mbank.view.GoogleAnalya;
import com.accenture.mbank.view.RotateTitleView;
import com.accenture.mbank.view.table.AccountsRotateTableView;
import com.accenture.mbank.view.table.LoansRotateTableView;
import com.accenture.mbank.view.table.RotatTableView.OnSlidListener;
import com.accenture.mbank.view.table.RotateTableViewWithButton.OnButtonClickListener;
import com.accenture.mbank.view.table.SignLineFormView;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

public class LoansRotateLayoutManager extends RotateBankViewManager {

    Handler handler;

    public LoansRotateLayoutManager() {
        handler = new Handler();
    }

    public ViewGroup getContainer() {
        return container;
    }

    public void setContainer(ViewGroup container) {
        this.container = container;
    }

    int dashboardAccounts = 0;

    @Override
    public void onShow() {

        // MainActivity mainActivity = (MainActivity)container.getContext();
        // int accounts = mainActivity.setting.getDashboardAccounts();
        int accounts = 13;
        if (hashMap.size() <= 0) {

            if (BaseActivity.isOffline) {

                if (container.getChildCount() <= 0) {
                    final AccountsRotateTableView rotatTableView0 = new AccountsRotateTableView(
                            container.getContext());
                    container.addView(rotatTableView0);
                    rotatTableView0.setCount(5);
                    int rotateImageRes = getRotateImageRes(accounts);
                    rotatTableView0.setRotatResource(R.drawable.card_slider,
                            R.drawable.cards_5place, -1);
                    rotatTableView0.parentScrollView = (ScrollView)container.getParent();
                    rotatTableView0.setButtonImage(R.drawable.account_btn_transaction);
                    rotatTableView0.setButtonImageOver(R.drawable.account_btn_transaction_over);

                }
            } else {
                getDashBoardData();
                dashboardAccounts = accounts;
            }
        }

    }

    List<AccountsModel> list;

    HashMap<AccountsModel, GetFinancingInfoModel> hashMap = new HashMap<AccountsModel, GetFinancingInfoModel>();

    private boolean needShowChart;

    public void getDashBoardData() {
        ProgressOverlay overlay = new ProgressOverlay(container.getContext());
        overlay.show("", new OnProgressEvent() {

            @Override
            public void onProgress() {

                list = Contants.loansAccounts;
                hashMap.clear();
                for (AccountsModel accountsModel : list) {
                    String postData = GetFinancingInfoJson.getFinancingInfoReportProtocal(
                            Contants.publicModel, accountsModel.getAccountCode(),
                            accountsModel.getFinanceType());

                    HttpConnector httpConnector = new HttpConnector();
                    String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url,
                            postData, container.getContext());
                    GetFinancingInfoModel getFinancingInfo = GetFinancingInfoJson
                            .paresgetFinancingInfoResponse(httpResult);

                    hashMap.put(accountsModel, getFinancingInfo);
                }

                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        setAccount();
                        if (needShowChart) {
                            showChart();
                            needShowChart = false;
                        }
                    }
                });
            }
        });

    }

    void setAccount() {
        if (list != null && list.size() > 0) {
            container.removeAllViews();

            for (AccountsModel dashBoardModel : list) {
                final AccountsModel dashBoardModel2 = dashBoardModel;
                LayoutInflater layoutInflater = LayoutInflater.from(container.getContext());
                final RotateTitleView rTitleView = (RotateTitleView)layoutInflater.inflate(
                        R.layout.rotate_title_view, null);
                rTitleView.init();
                // rTitleView.setTitleIcon(R.drawable.accounts_icon);
                rTitleView.setTitleIcon(R.drawable.loans_icon);
                rTitleView.setTitleText(dashBoardModel.getAccountAlias());
                String time = TimeUtil.getDateString(System.currentTimeMillis(),
                        TimeUtil.dateFormat5);
                rTitleView.setUpdateTitle(container.getContext().getResources()
                        .getString(R.string.loans_update_to)
                        + time);

                container.addView(rTitleView);
                final LoansRotateTableView rotatTableView0 = new LoansRotateTableView(
                        container.getContext());
                container.addView(rotatTableView0);
                final GetFinancingInfoModel getFinancingInfoModel = hashMap.get(dashBoardModel);
                if (getFinancingInfoModel == null) {
                    continue;
                }

                rotatTableView0.setCount(dashboardAccounts);
                rotatTableView0.setRotatResource(R.drawable.loans_slider, R.drawable.loans_cycle,
                        -1);
                rotatTableView0.parentScrollView = (ScrollView)container.getParent();
                rotatTableView0.setButtonImage(R.drawable.loans_btn_details);
                rotatTableView0.setButtonImageOver(R.drawable.loans_btn_details_over);
                if (dashBoardModel2 != null && getFinancingInfoModel.getInstallments() != null
                        && getFinancingInfoModel.getInstallments().size() > 0) {
                    setDashboardUi(getFinancingInfoModel, rotatTableView0, rTitleView, 0);
                }
                rotatTableView0.setOnSlidListener(new OnSlidListener() {

                    @Override
                    public void onSlid(View v, int index) {

                        setDashboardUi(getFinancingInfoModel, rotatTableView0, rTitleView, index);
                    }

                });
                rotatTableView0.setOnButtonClickListener(new OnButtonClickListener() {

                    @Override
                    public void onClick(View v) {
                        MainActivity mainActivity = (MainActivity)container.getContext();

                        DashBoardModel dashBoardModel = new DashBoardModel();
                        dashBoardModel.setAccountCode(dashBoardModel2.getAccountCode());

                        mainActivity.loansLayoutContainer.setAnimateTo(dashBoardModel);
                        mainActivity.showLoans();
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

    private void setDashboardUi(final GetFinancingInfoModel getFinancingInfoModel,
            final LoansRotateTableView rotatTableView0, RotateTitleView titleView, int index) {
        List<InstallmentsModel> list = getFinancingInfoModel.getInstallments();
        if (list == null) {
            return;
        }
        int todayIndex = getTodayIndex(list);
        int begin = Math.max(0, todayIndex - 13);

        boolean over = todayIndex == list.size() - 1;
        List<InstallmentsModel> newInstallments = new ArrayList<InstallmentsModel>();
        for (int i = 1; i < todayIndex - begin + 1; i++) {
            newInstallments.add(list.get(begin + i));
        }
        int size = newInstallments.size();

        InstallmentsModel install = newInstallments.get(size - 1 - index);

        String amount = Utils.notPlusGenerateFormatMoney(container.getContext().getResources()
                .getString(R.string.dollar), install.getAmount());
        rotatTableView0.setInstallToValue(amount);

        if (over) {
            rotatTableView0.setNext_InstallmentValue("n.a.");

        } else {
            rotatTableView0.setNext_InstallmentValue(amount);
        }

        String residu = Utils.notPlusGenerateFormatMoney(container.getContext().getResources()
                .getString(R.string.dollar), getFinancingInfoModel.getResidueAmount());
        rotatTableView0.setResidualCapitalValue(residu);

        String deadLine = install.getDeadlineDate();

        deadLine = TimeUtil.changeFormattrString(deadLine, TimeUtil.dateFormat2,
                TimeUtil.dateFormat5);
        rotatTableView0.setDeadLine(deadLine);
        rotatTableView0.setInstallToDate(deadLine);

        String paid = install.getPaidState();
        boolean ispaid = "S".equals(paid);
        rotatTableView0.setPaid(ispaid);

    }

    private int getTodayIndex(List<InstallmentsModel> installments) {
        int result = 0;
        int size = installments.size();
        for (int i = 0; i < size; i++) {
            InstallmentsModel inModel = installments.get(i);
            String deadLinedate = inModel.getDeadlineDate();
            LogManager.d(deadLinedate);
            try {
                long longTime = TimeUtil.getTimeByString(deadLinedate, TimeUtil.dateFormat2);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(longTime);
                Calendar now = Calendar.getInstance();
                now.setTimeInMillis(System.currentTimeMillis());
                if (now.before(calendar)) {
                    result = Math.min(i, size - 1);
                    break;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        if (result == 0) {
            result = size - 1;
        }
        return result;
    }

    @Override
    public void showChart() {
    	 GoogleAnalya.getInstance().getGaInstance(container.getContext(),"view.loans.graph");
        if (list.size() == 0) {
            needShowChart = true;
            return;
        }
        if (chartLayout != null) {
            chartLayout.removeAllViews();

            SignLineFormView sinFormView = new SignLineFormView(chartLayout.getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

            // chartLayout.addView(sinFormView, params);
            List<String> xValues = new ArrayList<String>();
            // for (String str : Utils.months) {
            // xValues.add(str);
            //
            // }
            int index = getVisiableRotateViewIndex();

            if (index >= 0 && index < list.size()) {
                AccountsModel accountsModel = list.get(index);
                List<Double> xYValues = new ArrayList<Double>();
                GetFinancingInfoModel getFinancingInfoModel = hashMap.get(accountsModel);
                if (getFinancingInfoModel == null) {
                    needShowChart = true;
                    return;
                }
                List<InstallmentsModel> installments = getFinancingInfoModel.getInstallments();
                // 91021 Not valid account code
                if (installments == null) {
                    return;
                }
                int todayIndex = getTodayIndex(installments);
                int begin = Math.max(0, todayIndex - 13);

                List<InstallmentsModel> newInstallments = new ArrayList<InstallmentsModel>();
                for (int i = 1; i < todayIndex - begin + 1; i++) {
                    newInstallments.add(installments.get(begin + i));
                }
                // 今天剩下的
                double nowRes = Double.parseDouble(getFinancingInfoModel.getResidueAmount());
                // 过去还钱累积数量
                double[] yValuesss = new double[newInstallments.size()];

                for (int i = newInstallments.size() - 1; i >= 0; i--) {

                    InstallmentsModel inModel = newInstallments.get(i);
                    // The most right value is ResidueAmount
                    if (i == newInstallments.size() - 1) {
                        yValuesss[newInstallments.size() - 1] = nowRes;

                    } else {
                        double returns = yValuesss[i + 1];
                        if (inModel.getPaidState().equals("S")) {
                            returns = returns + inModel.getAmount();
                        }
                        yValuesss[i] = returns;
                    }
                }
                for (int i = 0; i < newInstallments.size(); i++) {
                    InstallmentsModel inModel = newInstallments.get(i);
                    // xYValues.add(inModel.getAmountCapitalShare());

                    String time = TimeUtil.changeFormattrString(chartLayout.getContext(),inModel.getDeadlineDate(),
                            TimeUtil.dateFormat2, TimeUtil.detaFormat6);
                    LogManager.d(time);
                    xValues.add(time);
                }
                for (int i = 0; i < yValuesss.length; i++) {
                    xYValues.add(yValuesss[i]);
                }
                sinFormView.xValue = xValues;
                sinFormView.xYValues = xYValues;
                String residu = Utils.notPlusGenerateFormatMoney(container.getContext()
                        .getResources().getString(R.string.dollar),
                        getFinancingInfoModel.getResidueAmount());
                sinFormView.residualCapital = residu;
                sinFormView.initYValue();
                sinFormView.installments = newInstallments;
                sinFormView.setTextLeft(container.getContext().getString(R.string.sa_loan));
                sinFormView.setTextRight(accountsModel.getAccountAlias());
                chartLayout.addView(sinFormView, params);
            }

        }

    }
}
