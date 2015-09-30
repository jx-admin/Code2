
package com.accenture.manager;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.accenture.manager.protocol.RotateBankViewManager;
import com.accenture.mbank.MainActivity;
import com.accenture.mbank.R;
import com.accenture.mbank.logic.GetAssetsInformationJson;
import com.accenture.mbank.logic.GetDepositInfoRequestJson;
import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.model.AssetsInformationModel;
import com.accenture.mbank.model.ChartProp;
import com.accenture.mbank.model.DashBoardModel;
import com.accenture.mbank.model.DepositInfoModel;
import com.accenture.mbank.model.GetAssetsInformationResponseModel;
import com.accenture.mbank.model.GetDepositInfoResponseModel;
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
import com.accenture.mbank.view.table.ChartView;
import com.accenture.mbank.view.table.InvestRotatTableView;
import com.accenture.mbank.view.table.InvestRotatTableView.OnClickListener;

public class InvestmentRotateLayoutManager extends RotateBankViewManager implements
        OnCheckedChangeListener, OnInnerScrollListener {

    List<AccountsModel> list;

    Context context;

    Handler handler;

    /**
     * 没有横屏的类型
     */
    List<AssetsInformationModel> assetsInformationList = new ArrayList<AssetsInformationModel>();

    List<DepositInfoModel> depositInfoList = new ArrayList<DepositInfoModel>();

    AssetsInformationModel assetsInformationModel;

    DepositInfoModel depositInfoModel;

    private InnerScrollView scroll;

    public ViewGroup getContainer() {
        return container;
    }

    public void setContainer(ViewGroup container) {
        this.context = container.getContext();
        this.container = container;
        scroll = (InnerScrollView)container.getParent();
        scroll.setOnInnerScrollListener(this);
        handler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case 0:
                        setDepositInfoListUI();
                        if (needShowChart) {
                            showChart();
                        }
                        break;
                    case 1:
                        setAssetsInformationListUI();
                    default:
                        break;
                }
            };
        };
    }

    @Override
    public void onShow() {
        container.removeAllViews();
        if (((assetsInformationList != null && assetsInformationList.size() > 0) && (depositInfoList != null && depositInfoList
                .size() > 0))) {
            handler.sendEmptyMessage(0);
            handler.sendEmptyMessage(1);
        } else {
        	assetsInformationList.clear();
        	depositInfoList.clear();
            ProgressOverlay progressOverlay = new ProgressOverlay(container.getContext());
            progressOverlay.show("", new OnProgressEvent() {
                @Override
                public void onProgress() {
                    loadDepositInfoData();
                    loadgetAssetsInfoData();
                }
            });
        }
    }

    public void loadgetAssetsInfoData() {
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getMortageType().equals("GP")) {
                    assetsInformationModel = new AssetsInformationModel();
                    assetsInformationModel.setAccountAlias(list.get(i).getAccountAlias());
                    String postData = GetAssetsInformationJson.GetAssetsInformantionReportProtocal(
                            Contants.publicModel, list.get(i).getAccountCode());
                    HttpConnector httpConnector = new HttpConnector();
                    String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url,
                            postData, context);
                    GetAssetsInformationResponseModel getAssetsInformation = GetAssetsInformationJson
                            .parseGetAssetsInformationResponse(httpResult);
                    if (!getAssetsInformation.responsePublicModel.isSuccess()) {
                        return;
                    }
                    assetsInformationModel.setGetAssetsInfomation(getAssetsInformation);
                    assetsInformationList.add(assetsInformationModel);
                }
            }
            handler.sendEmptyMessage(0);
            handler.sendEmptyMessage(1);
        }
    }

    public void loadDepositInfoData() {
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getMortageType().equals("DT")) {
                    depositInfoModel = new DepositInfoModel();
                    depositInfoModel.setAccountAlias(list.get(i).getAccountAlias());
                    String postData = GetDepositInfoRequestJson.GetDepositInfoReportProtocal(
                            Contants.publicModel, list.get(i).getAccountCode());
                    HttpConnector httpConnector = new HttpConnector();
                    String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url,
                            postData, context);
                    GetDepositInfoResponseModel getDepositInfo = GetDepositInfoRequestJson
                            .parseGetDepositInfoResponse(httpResult);
                    if (getDepositInfo == null || !getDepositInfo.responsePublicModel.isSuccess()) {
                        return;
                    }
                    depositInfoModel.setGetDepositInfo(getDepositInfo);
                    depositInfoList.add(depositInfoModel);
                }
            }
        }
    }

    void setDepositInfoListUI() {
        if (depositInfoList != null && depositInfoList.size() > 0) {
            for (int i = 0; i < depositInfoList.size(); i++) {
                final String account = depositInfoList.get(i).getAccountAlias();
                LayoutInflater inflater = LayoutInflater.from(container.getContext());
                RotateTitleView rotateTitleView = (RotateTitleView)inflater.inflate(
                        R.layout.rotate_title_view, null);
                rotateTitleView.init();
                rotateTitleView.setTitleIcon(R.drawable.investments_icon);
                rotateTitleView.setTitleText(depositInfoList.get(i).getAccountAlias());
                long time = System.currentTimeMillis();
                String nowTime = TimeUtil.getDateString(time, TimeUtil.dateFormat5);
                rotateTitleView.setUpdateTitle(container.getContext().getResources()
                        .getString(R.string.invest_update)
                        + " " + nowTime);
                container.addView(rotateTitleView);

                InvestRotatTableView investRotatTableView = new InvestRotatTableView(
                        container.getContext());
                investRotatTableView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MainActivity mainActivity = (MainActivity)container.getContext();
                        DashBoardModel dashBoardModel = new DashBoardModel();
                        dashBoardModel.setAccountCode(account);
                        mainActivity.investmentsLayoutManager.setAnimateTo(dashBoardModel);
                        mainActivity.showInvestments();
                    }
                });
                investRotatTableView.setRotatResource(R.drawable.investment_bg,
                        R.drawable.btn_details, R.drawable.btn_details_over);
                investRotatTableView.setTotalAmountTitle(container.getContext().getResources()
                        .getString(R.string.portfolio_1));

                String amount = Utils.notPlusGenerateFormatMoney(container.getContext()
                        .getResources().getString(R.string.dollar), depositInfoList.get(i)
                        .getGetDepositInfo().getPortfolioValue());
                investRotatTableView.setTotalAmount(amount);
                container.addView(investRotatTableView);
            }
        }
    }

    void setAssetsInformationListUI() {

        if (assetsInformationList != null && assetsInformationList.size() > 0) {
            for (int i = 0; i < assetsInformationList.size(); i++) {
                final String account = assetsInformationList.get(i).getAccountAlias();
                LayoutInflater inflater = LayoutInflater.from(container.getContext());
                RotateTitleView rotateTitleView = (RotateTitleView)inflater.inflate(
                        R.layout.rotate_title_view, null);
                rotateTitleView.init();
                rotateTitleView.setTitleIcon(R.drawable.investments_icon);
                rotateTitleView.setTitleText(assetsInformationList.get(i).getAccountAlias());
                long time = System.currentTimeMillis();
                String nowTime = TimeUtil.getDateString(time, TimeUtil.dateFormat5);
                rotateTitleView.setUpdateTitle(container.getContext().getResources()
                        .getString(R.string.invest_update)
                        + " " + nowTime);
                container.addView(rotateTitleView);

                InvestRotatTableView investRotatTableView = new InvestRotatTableView(
                        container.getContext());
                investRotatTableView.setRotatResource(R.drawable.investment_bg,
                        R.drawable.btn_details, R.drawable.btn_details_over);
                investRotatTableView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MainActivity mainActivity = (MainActivity)container.getContext();
                        DashBoardModel dashBoardModel = new DashBoardModel();
                        dashBoardModel.setAccountCode(account);
                        mainActivity.investmentsLayoutManager.setAnimateTo(dashBoardModel);
                        mainActivity.showInvestments();
                    }
                });
                // investRotatTableView.setTotalAmountTitle(list.get(i).getAccountAlias().toString()
                // + " " +
                // container.getContext().getResources().getString(R.string.total_amount));
                investRotatTableView.setTotalAmountTitle(container.getContext().getResources()
                        .getString(R.string.total_amount));
                String amount = Utils.notPlusGenerateFormatMoney(container.getContext()
                        .getResources().getString(R.string.dollar), assetsInformationList.get(i)
                        .getGetAssetsInfomation().getPortfolioValue());
                investRotatTableView.setTotalAmount(amount);
                container.addView(investRotatTableView);
            }
        }

    }

    public void setAccount(List<AccountsModel> list) {
        this.list = list;
    }

    RadioButton btn_shares;

    RadioButton btn_bonds;

    RadioButton btn_funds;

    RadioButton btn_more;
    
    RadioButton btn_asset;
    TextView leftTitle_TextView;

    TextView RightTitle_TextView;

    boolean needShowChart;

    @Override
    public void showChart() {
    	GoogleAnalya.getInstance().getGaInstance(container.getContext(),"view.investments.graph");
        int sharesColor = Color.rgb(255, 224, 82);
        int bondsColor = Color.rgb(254, 187, 10);
        int fundsColor = Color.rgb(243, 96, 0);
        int more = Color.rgb(209, 80, 0);

        int index = getVisiableRotateViewIndex();
        LogManager.d(index + "index");

        if (chartLayout != null) {
            chartLayout.removeAllViews();
        }
        
        if (depositInfoList.size() == 0) {
            needShowChart = true;
            return;
        }

        if (index >= depositInfoList.size()) {
            index = index - depositInfoList.size();
            
            if(index >= assetsInformationList.size()){
                return;
            }
            
            LayoutInflater inflater = LayoutInflater.from(container.getContext());
            LinearLayout investmentsChartsContainer = (LinearLayout)inflater.inflate(R.layout.investment_charts, null);
            investmentsChartsContainer.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                }
            });
            leftTitle_TextView = (TextView)investmentsChartsContainer.findViewById(R.id.left_title_TextView);
            leftTitle_TextView.setText(R.string.asset_management);
            RightTitle_TextView = (TextView)investmentsChartsContainer.findViewById(R.id.right_title_TextView);
            RightTitle_TextView.setText(context.getString(R.string.ss_invesments)+" " + assetsInformationList.get(index).getAccountAlias());
            btn_shares = (RadioButton)investmentsChartsContainer.findViewById(R.id.btn_shares);
            btn_shares.setBackgroundResource(R.drawable.btn_asset_selector);
            
            btn_shares.setOnCheckedChangeListener(this);
            
            btn_bonds = (RadioButton)investmentsChartsContainer.findViewById(R.id.btn_bonds);
            btn_bonds.setVisibility(View.GONE);
            btn_funds = (RadioButton)investmentsChartsContainer.findViewById(R.id.btn_funds);
            btn_funds.setVisibility(View.GONE);
            btn_more = (RadioButton)investmentsChartsContainer.findViewById(R.id.btn_more);
            btn_more.setVisibility(View.GONE);
            
            chartView = (ChartView)investmentsChartsContainer.findViewById(R.id.chartView);
            chartView.setAntiAlias(true);
            
            acps = chartView.createCharts(1);
            int size = acps.size();
            String name=context.getString(R.string.assets);
            for (int i = 0; i < size; i++) {
                ChartProp chartProp = acps.get(i);
                chartProp.setColor(sharesColor);
                chartProp.setPercent((float)100);
                chartProp.setName(name);
            }
            
            chartLayout.addView(investmentsChartsContainer);
        }else{
            GetDepositInfoResponseModel depositInfo = depositInfoList.get(index).getGetDepositInfo();
            
            int color[] = new int[] {
                    sharesColor, bondsColor, fundsColor, more
            };

            double percent[] = new double[] {
                    depositInfo.getShares().getPercentage(), depositInfo.getBonds().getPercentage(),
                    depositInfo.getFunds().getPercentage(),
                    depositInfo.getOtherSecurities().getPercentage()
            };

            String names[] =context.getResources().getStringArray(R.array.investment_rote_names);// new String[] { "shares", "bonds", "funds", "more" };

            LayoutInflater inflater = LayoutInflater.from(container.getContext());
            LinearLayout investmentsChartsContainer = (LinearLayout)inflater.inflate(R.layout.investment_charts, null);
            investmentsChartsContainer.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                }
            });
            
            leftTitle_TextView = (TextView)investmentsChartsContainer.findViewById(R.id.left_title_TextView);
            leftTitle_TextView.setText(R.string.portfolio_1);
            RightTitle_TextView = (TextView)investmentsChartsContainer.findViewById(R.id.right_title_TextView);
            RightTitle_TextView.setText(context.getString(R.string.ss_invesments)+" " + depositInfoList.get(index).getAccountAlias());
            btn_shares = (RadioButton)investmentsChartsContainer.findViewById(R.id.btn_shares);
            if (depositInfo.getShares().getPercentage() == 0) {
                btn_shares.setBackgroundResource(R.drawable.btn_shares_disable);
            } else {
                btn_shares.setOnCheckedChangeListener(this);
            }
            btn_bonds = (RadioButton)investmentsChartsContainer.findViewById(R.id.btn_bonds);
            if (depositInfo.getBonds().getPercentage() == 0) {
                btn_bonds.setBackgroundResource(R.drawable.btn_bonds_disable);
            } else {
                btn_bonds.setOnCheckedChangeListener(this);
            }
            btn_funds = (RadioButton)investmentsChartsContainer.findViewById(R.id.btn_funds);
            if (depositInfo.getFunds().getPercentage() == 0) {
                btn_funds.setBackgroundResource(R.drawable.btn_funds_disable);
            } else {
                btn_funds.setOnCheckedChangeListener(this);
            }
            btn_more = (RadioButton)investmentsChartsContainer.findViewById(R.id.btn_more);
            if (depositInfo.getOtherSecurities().getPercentage() == 0) {
                btn_more.setBackgroundResource(R.drawable.btn_more_disable);
            } else {
                btn_more.setOnCheckedChangeListener(this);
            }
            chartView = (ChartView)investmentsChartsContainer.findViewById(R.id.chartView);
            chartView.setAntiAlias(true);

            acps = chartView.createCharts(color.length);
            int size = acps.size();
            for (int i = 0; i < size; i++) {
                ChartProp chartProp = acps.get(i);
                chartProp.setColor(color[i]);
                chartProp.setPercent((float)percent[i]);
                chartProp.setName(names[i]);
            }
            chartLayout.addView(investmentsChartsContainer);
        }
    }

    ChartView chartView;

    ArrayList<ChartProp> acps;

    public void onCheckedChanged(CompoundButton buttonView, boolean isCheck) {
        if (isCheck) {
            if (buttonView == btn_shares) {
                chartView.setSelectIndex(0);
            } else if (buttonView == btn_bonds) {
                chartView.setSelectIndex(1);
            } else if (buttonView == btn_funds) {
                chartView.setSelectIndex(2);
            } else if (buttonView == btn_more) {
                chartView.setSelectIndex(3);
            }
        }
    }

    @Override
    public void onScrollChanged(int l, int t, int oldl, int oldt) {

        int index = getVisiableRotateViewIndex();
        MainActivity mainActivity = (MainActivity)container.getContext();

        if (index >= depositInfoList.size()) {
            mainActivity.setCanOrientation(true);
        } else {

            mainActivity.setCanOrientation(true);
        }

    }
}
