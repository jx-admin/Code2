
package com.accenture.manager;

import java.util.ArrayList;
import java.util.List;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.LinearLayout.LayoutParams;

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
import com.accenture.mbank.util.TimeUtil;
import com.accenture.mbank.util.Utils;
import com.accenture.mbank.view.GoogleAnalya;
import com.accenture.mbank.view.RotateTitleView;
import com.accenture.mbank.view.table.AccountsRotateTableView;
import com.accenture.mbank.view.table.CardsBarGraphic;
import com.accenture.mbank.view.table.CardsRotateTableView;
import com.accenture.mbank.view.table.LoansRotateTableView;
import com.accenture.mbank.view.table.RotatTableView;
import com.accenture.mbank.view.table.RotatTableView.OnSlidListener;
import com.accenture.mbank.view.table.RotateTableViewWithButton;
import com.accenture.mbank.view.table.RotateTableViewWithButton.OnButtonClickListener;

public class CardsRotateLayoutManager extends RotateBankViewManager {

    int dashboardCards;

    public CardsRotateLayoutManager() {
        handler = new Handler();
    }

    public ViewGroup getContainer() {
        return container;
    }

    public void setContainer(ViewGroup container) {
        this.container = container;
    }

    protected List<DashBoardModel> list = new ArrayList<DashBoardModel>();;

    @Override
    public void onShow() {

        if (BaseActivity.isOffline) {
            addCards();
        } else {

            MainActivity mainActivity = (MainActivity)container.getContext();
            int cards = mainActivity.setting.getDashboardCards() + 1;
            if (cards != dashboardCards) {
                dashboardCards = cards;
                getDashBoardData();
            }
        }

    }

    Handler handler;

    public void getDashBoardData() {
        ProgressOverlay overlay = new ProgressOverlay(container.getContext());
        overlay.show("", new OnProgressEvent() {

            @Override
            public void onProgress() {
                // TODO Auto-generated method stub

                list.clear();
                for (AccountsModel accountsModel : Contants.cardAccounts) {
                    String postData = GetDashBoardDataJson.getDashBoardDataReportProtocal(
                            Contants.publicModel, dashboardCards, 1, accountsModel.getAccountCode());
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
                        setCards();
                        if (needShowChart) {
                            showChart();
                            needShowChart = false;
                        }
                    }

                });
            }
        });

    }

    private void setCards() {

        if (list != null && list.size() > 0) {
            container.removeAllViews();
            for (DashBoardModel dashBoardModel : list) {
                final DashBoardModel dashBoardModel2 = dashBoardModel;
                LayoutInflater layoutInflater = LayoutInflater.from(container.getContext());
                final RotateTitleView rTitleView = (RotateTitleView)layoutInflater.inflate(
                        R.layout.rotate_title_view, null);
                rTitleView.init();
                // rTitleView.setTitleIcon(R.drawable.accounts_icon);
                rTitleView.setTitleIcon(R.drawable.cards_icon);
                rTitleView.setTitleText(dashBoardModel.getPersonalizedName());
                rTitleView.setUpdateTitle(dashBoardModel.getDashboardDataList().get(0)
                        .getLastUpdate());

                container.addView(rTitleView);
                final CardsRotateTableView cardsRotateTableView = new CardsRotateTableView(
                        container.getContext());
                container.addView(cardsRotateTableView);
                String text = Utils.notPlusGenerateFormatMoney(container.getContext()
                        .getResources().getString(R.string.dollar),
                        dashBoardModel.getAccountBalance());
                cardsRotateTableView.setCount(dashboardCards);
                int bag = AccountRotateLayoutManager.getRotateImageRes(dashboardCards);
                cardsRotateTableView.setRotatResource(R.drawable.card_slider, bag,
                        R.drawable.cards_arrow);
                cardsRotateTableView.setTotalWithDrawals(text);
                text = Utils.notPlusGenerateFormatMoney(container.getContext().getResources()
                        .getString(R.string.dollar), dashBoardModel.getAvailableBalance());
                cardsRotateTableView.setAvaliablity(text);
                cardsRotateTableView.parentScrollView = (ScrollView)container.getParent();
                cardsRotateTableView.setButtonImage(R.drawable.cards_btn_transactions);
                cardsRotateTableView.setButtonImageOver(R.drawable.cards_btn_transactions_over);

                cardsRotateTableView.setOnButtonClickListener(new OnButtonClickListener() {

                    @Override
                    public void onClick(View v) {

                        MainActivity mainActivity = (MainActivity)container.getContext();
                        mainActivity.cardsLayoutManager.setAnimateTo(dashBoardModel2);
                        mainActivity.showTab(2);

                    }
                });
                if (dashBoardModel2 != null && dashBoardModel2.getDashboardDataList() != null
                        && dashBoardModel2.getDashboardDataList().size() > 0) {
                    setDashboardUi(dashBoardModel2, cardsRotateTableView, rTitleView,0);
                }
                cardsRotateTableView.setOnSlidListener(new OnSlidListener() {

                    @Override
                    public void onSlid(View v, int index) {
                        if (index != 0) {
                            cardsRotateTableView.setAvaliablity(container.getContext()
                                    .getResources().getString(R.string.not_able));
                        } else {
                            String avaString = Utils.notPlusGenerateFormatMoney(container
                                    .getContext().getResources().getString(R.string.dollar),
                                    dashBoardModel2.getAvailableBalance());
                            cardsRotateTableView.setAvaliablity(avaString);
                        }

                        setDashboardUi(dashBoardModel2, cardsRotateTableView, rTitleView, index);
                    }
                });

            }
        }
    }

    private void setDashboardUi(final DashBoardModel dashBoardModel2,
            final CardsRotateTableView rotatTableView0, RotateTitleView titleView, int index) {
        List<DashboardDataModel> list = dashBoardModel2.getDashboardDataList();

        DashboardDataModel dashboardDataModel = list.get(index);

        String withdwawals = Utils.generateFormatMoney(container.getContext().getResources()
                .getString(R.string.dollar), dashboardDataModel.getWithdrawals());
        rotatTableView0.setTotalWithDrawals(withdwawals);

        String date = TimeUtil.changeFormattrString(dashboardDataModel.getLastUpdate(),
                TimeUtil.dateFormat2, TimeUtil.dateFormat5);
        titleView.setUpdateTitle(container.getContext().getResources()
                .getString(R.string.card_update_to)
                + date);
    }

    private void addCards() {
        container.removeAllViews();
        container.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });

        LoansRotateTableView rotatTableView0 = new LoansRotateTableView(container.getContext());
        container.addView(rotatTableView0);
        container.setPadding(RotatTableView.margin, 0, 0, 0);
        rotatTableView0.setCount(13);
        rotatTableView0.setRotatResource(R.drawable.card_slider, R.drawable.cards_13place,
                R.drawable.cards_arrow);
        rotatTableView0.setButtonImage(R.drawable.loans_btn_details);
        rotatTableView0.setButtonImageOver(R.drawable.loans_btn_details_over);
        rotatTableView0.parentScrollView = (ScrollView)container.getParent();
        CardsRotateTableView rotatTableView = new CardsRotateTableView(container.getContext());
        container.addView(rotatTableView);
        container.setPadding(RotatTableView.margin, 0, 0, 0);
        rotatTableView.setCount(13);
        rotatTableView.setRotatResource(R.drawable.card_slider, R.drawable.cards_13place,
                R.drawable.cards_arrow);
        rotatTableView.setButtonImage(R.drawable.cards_btn_transactions);
        rotatTableView.setButtonImageOver(R.drawable.cards_btn_transactions_over);

        AccountsRotateTableView rotatTableView1 = new AccountsRotateTableView(
                container.getContext());
        container.addView(rotatTableView1);
        container.setPadding(RotateTableViewWithButton.margin, 0, 0, 0);
        rotatTableView1.setCount(13);
        rotatTableView1.setRotatResource(R.drawable.card_slider, R.drawable.cards_13place,
                R.drawable.cards_arrow);
        rotatTableView1.setButtonImage(R.drawable.account_btn_transaction);
        rotatTableView1.setButtonImageOver(R.drawable.account_btn_transaction_over);

        rotatTableView1.parentScrollView = (ScrollView)container.getParent();
        rotatTableView.parentScrollView = (ScrollView)container.getParent();

        rotatTableView.setOnButtonClickListener(new OnButtonClickListener() {

            @Override
            public void onClick(View v) {
                // rotatTableView
            }
        });
    }

    boolean needShowChart = false;

    @Override
    public void showChart() {
    	 GoogleAnalya.getInstance().getGaInstance(container.getContext(),"view.cards.graph");
        if (list.size() == 0) {
            needShowChart = true;
            return;
        }
        DashBoardModel model = list.get(getVisiableRotateViewIndex());
        chartLayout.removeAllViews();
        CardsBarGraphic child = new CardsBarGraphic(chartLayout.getContext(), model);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT);
        chartLayout.addView(child, params);
    }

}
