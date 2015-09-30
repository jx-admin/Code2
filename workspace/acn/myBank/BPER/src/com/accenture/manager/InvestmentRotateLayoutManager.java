
package com.accenture.manager;

import it.gruppobper.ams.android.bper.R;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.accenture.manager.protocol.RotateBankViewManager;
import com.accenture.mbank.BaseActivity;
import com.accenture.mbank.ChartActivity;
import com.accenture.mbank.InvestmentsDetailActivity;
import com.accenture.mbank.MainActivity;
import com.accenture.mbank.logic.GetAssetsInformationJson;
import com.accenture.mbank.logic.GetDepositInfoRequestJson;
import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.model.AssetsInformationModel;
import com.accenture.mbank.model.ChartProp;
import com.accenture.mbank.model.DepositInfo;
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
import com.accenture.mbank.view.InnerScrollView.OnInnerScrollListener;
import com.accenture.mbank.view.RotateTitleView;
import com.accenture.mbank.view.table.ChartView;
import com.accenture.mbank.view.table.InvestRotatTableView;
import com.accenture.mbank.view.table.InvestRotatTableView.OnClickListener;

public class InvestmentRotateLayoutManager extends RotateBankViewManager implements
        OnCheckedChangeListener, OnInnerScrollListener {

	class InvestmentInfo {
		String investmentType;
		int investmentIndex;
	};
	
	String INVESTMENT_TYPE_ASSERT = "GP";
	String INVESTMENT_TYPE_DEPOSIT = "DT";

    List<AccountsModel> list;
    List<InvestmentInfo> investInfoList = new ArrayList<InvestmentInfo>();

    static boolean reloadData = true;

    Context context;

    Handler handler;

    List<AssetsInformationModel> assetsInformationList = new ArrayList<AssetsInformationModel>();

    List<DepositInfoModel> depositInfoList = new ArrayList<DepositInfoModel>();

    AssetsInformationModel assetsInformationModel;

    DepositInfoModel depositInfoModel;

    public static void setNeedUpdate(boolean needUpdate) {
    	InvestmentRotateLayoutManager.reloadData = needUpdate;
    }

    public void setContainer(ViewGroup container) {
    	super.setContainer(container);

        this.context = container.getContext();

        handler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case 0:
                    	updateInfoUI();
                        updateArrowState();
                        if (needShowChart) {
                            showChart();
                        }
                        break;
                    default:
                        break;
                }
            };
        };
    }

    @Override
    public void onShow() {
        container.removeAllViews();
        
        if (BaseActivity.isOffline) {
        	createOfflineData();
        	return;
        }

        if (reloadData == false) {
            handler.sendEmptyMessage(0);
            handler.sendEmptyMessage(1);
            return;
        }
        
        investInfoList.clear();
    	assetsInformationList.clear();
    	depositInfoList.clear();
    	setCanOrientation();

        ProgressOverlay progressOverlay = new ProgressOverlay(container.getContext());
        progressOverlay.show("", new OnProgressEvent() {
            @Override
            public void onProgress() {
            	loadInfoData();
            }
        });
    }

    public void loadInfoData() {
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getMortageType().equals(INVESTMENT_TYPE_ASSERT)) {
                    assetsInformationModel = new AssetsInformationModel();
                    assetsInformationModel.setAccountAlias(list.get(i).getAccountAlias());
                    assetsInformationModel.setIsPreferred(list.get(i).getIsPreferred());
                    String postData = GetAssetsInformationJson.GetAssetsInformantionReportProtocal(Contants.publicModel, list.get(i).getAccountCode());
                    HttpConnector httpConnector = new HttpConnector();
                    String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url,postData, context);
                    GetAssetsInformationResponseModel getAssetsInformation = GetAssetsInformationJson.parseGetAssetsInformationResponse(httpResult);
                    
                	if (httpResult == null) {
						/*
						 *  A connection error happened
						 */
						reloadData = true;
						handler.post(new Runnable() {
							@Override
							public void run() {
								final MainActivity mainActivity = (MainActivity)container.getContext();
								mainActivity.showTab(0);
							}
						});
						return;
					}
                	
                    if (getAssetsInformation != null &&
                    	!getAssetsInformation.responsePublicModel.isSuccess() &&
                    	!getAssetsInformation.responsePublicModel.eventManagement.getErrorCode().equals(Contants.ERR_SESSION_EXPIRED_1) &&
						!getAssetsInformation.responsePublicModel.eventManagement.getErrorCode().equals(Contants.ERR_SESSION_EXPIRED_2)) {
                		/*
						 * maybe the 90000 happens
						 */
						reloadData = true;
						handler.post(new Runnable() {
							@Override
							public void run() {
								onError(container.getContext());
							}
						});
						return;
                    }
                    
                    if (getAssetsInformation != null && getAssetsInformation.responsePublicModel.isSuccess()) {
                    	assetsInformationModel.setGetAssetsInfomation(getAssetsInformation);
                    	assetsInformationList.add(assetsInformationModel);
                    	reloadData = false;
                    	
                    	InvestmentInfo investinfo = new InvestmentInfo();
                    	investinfo.investmentIndex = assetsInformationList.size() - 1;
                    	investinfo.investmentType = INVESTMENT_TYPE_ASSERT;
                    	investInfoList.add(investinfo);
                    	
                    	setCanOrientation();
                    }
                }else if (list.get(i).getMortageType().equals(INVESTMENT_TYPE_DEPOSIT)) {
                        depositInfoModel = new DepositInfoModel();
                        depositInfoModel.setAccountAlias(list.get(i).getAccountAlias());
                        depositInfoModel.setIsPreferred(list.get(i).getIsPreferred());
                        String postData = GetDepositInfoRequestJson.GetDepositInfoReportProtocal(Contants.publicModel, list.get(i).getAccountCode());
                        HttpConnector httpConnector = new HttpConnector();
                        String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url,postData, context);
                        GetDepositInfoResponseModel getDepositInfo = GetDepositInfoRequestJson.parseGetDepositInfoResponse(httpResult);
                    	if (httpResult == null) {
    						/*
    						 *  A connection error happened
    						 */
    						reloadData = true;
    						handler.post(new Runnable() {
    							@Override
    							public void run() {
    								final MainActivity mainActivity = (MainActivity)container.getContext();
    								mainActivity.showTab(0);
    							}
    						});
    						return;
    					}

                    	else if (getDepositInfo != null &&
                    			!getDepositInfo.responsePublicModel.isSuccess() &&
                    			!getDepositInfo.responsePublicModel.eventManagement.getErrorCode().equals(Contants.ERR_SESSION_EXPIRED_1) &&
    							!getDepositInfo.responsePublicModel.eventManagement.getErrorCode().equals(Contants.ERR_SESSION_EXPIRED_2))  {
                    		/*
    						 * maybe the 90000 happens
    						 */
    						reloadData = true;
    						handler.post(new Runnable() {
    							@Override
    							public void run() {
    								onError(container.getContext());
    							}
    						});
    						return;
                        }
                    	
                        if (getDepositInfo != null && getDepositInfo.responsePublicModel.isSuccess()) {
                        	depositInfoModel.setGetDepositInfo(getDepositInfo);
                        	depositInfoList.add(depositInfoModel);
                        	reloadData = false;
                        	
                        	InvestmentInfo investinfo = new InvestmentInfo();
                        	investinfo.investmentIndex = depositInfoList.size() - 1;
                        	investinfo.investmentType = INVESTMENT_TYPE_DEPOSIT;
                        	investInfoList.add(investinfo);
                        	
                        	setCanOrientation();
                        }
                }
            }
            handler.sendEmptyMessage(0);
        }
    }
    
    
    /**
     * order by userinfo
     */
    void updateInfoUI() {
    	for(final AccountsModel account: list){
    		if(account.getMortageType().equals(INVESTMENT_TYPE_ASSERT)){
    			if(assetsInformationList !=null && assetsInformationList.size() > 0){
    				for(AssetsInformationModel assetsInformation : assetsInformationList){
    					if(account.getAccountAlias().equals(assetsInformation.getAccountAlias())){
						 	LayoutInflater inflater = LayoutInflater.from(container.getContext());
			                RotateTitleView rotateTitleView = (RotateTitleView)inflater.inflate(R.layout.rotate_title_view, null);
			                rotateTitleView.init();
			                rotateTitleView.setTitleIcon(R.drawable.investments_icon);
			                rotateTitleView.setTitleText(assetsInformation.getAccountAlias());
			                long time = System.currentTimeMillis();
			                String nowTime = TimeUtil.getDateString(time, TimeUtil.dateFormat5);
			                rotateTitleView.setUpdateTitle(container.getContext().getResources().getString(R.string.invest_update)+ " " + nowTime);
			                container.addView(rotateTitleView);

			                InvestRotatTableView investRotatTableView = new InvestRotatTableView(container.getContext());
			                investRotatTableView.setRotatResource(R.drawable.investment_bg,R.drawable.btn_details, R.drawable.btn_details_over);
			                investRotatTableView.setOnClickListener(new OnClickListener() {
			                    @Override
			                    public void onClick(View v) {
			                    	Intent intent = new Intent(container.getContext(),InvestmentsDetailActivity.class);
			    					intent.putExtra("TYPE",InvestmentsDetailActivity.ASSETS + "");
			    					intent.putExtra("ACCOUNT_MODEL", account);
			    					container.getContext().startActivity(intent);
			    					
			    			        MainActivity mainActivity = (MainActivity)container.getContext();
			    			        mainActivity.overridePendingTransition(R.anim.zoomin, 0);
			                    }
			                });
			                investRotatTableView.setTotalAmountTitle(container.getContext().getResources().getString(R.string.total_amount));
			                String amount = Utils.generateFormatMoney(assetsInformation.getGetAssetsInfomation().getPortfolioValue());
			                investRotatTableView.setTotalAmount(amount);
			                container.addView(investRotatTableView);
    					}
    				}
    			}
    		}else if(account.getMortageType().equals(INVESTMENT_TYPE_DEPOSIT)){
    			if(depositInfoList !=null && depositInfoList.size() > 0){
    				for(DepositInfoModel depositInfoModel : depositInfoList){
    					if(account.getAccountAlias().equals(depositInfoModel.getAccountAlias())){
    		                LayoutInflater inflater = LayoutInflater.from(container.getContext());
    		                RotateTitleView rotateTitleView = (RotateTitleView)inflater.inflate(R.layout.rotate_title_view, null);
    		                rotateTitleView.init();
    		                rotateTitleView.setTitleIcon(R.drawable.investments_icon);
    		                rotateTitleView.setTitleText(depositInfoModel.getAccountAlias());
    		                long time = System.currentTimeMillis();
    		                String nowTime = TimeUtil.getDateString(time, TimeUtil.dateFormat5);
    		                rotateTitleView.setUpdateTitle(container.getContext().getResources().getString(R.string.invest_update)+ " " + nowTime);
    		                container.addView(rotateTitleView);

    		                InvestRotatTableView investRotatTableView = new InvestRotatTableView(container.getContext());
    		                investRotatTableView.setOnClickListener(new OnClickListener() {
    		                    @Override
    		                    public void onClick(View v) {
    		                    	Intent intent = new Intent(container.getContext(),InvestmentsDetailActivity.class);
    		    					intent.putExtra("TYPE",InvestmentsDetailActivity.DEPOSIT +"");
    		    					intent.putExtra("ACCOUNT_MODEL", account);
    		    					container.getContext().startActivity(intent);
    		    					
    		    			        MainActivity mainActivity = (MainActivity)container.getContext();
    		    			        mainActivity.overridePendingTransition(R.anim.zoomin, 0);
    		                    }
    		                });
    		                investRotatTableView.setRotatResource(R.drawable.investment_bg,R.drawable.btn_details, R.drawable.btn_details_over);
    		                investRotatTableView.setTotalAmountTitle(container.getContext().getResources().getString(R.string.portfolio_1));

    		                String amount = Utils.generateFormatMoney(depositInfoModel.getGetDepositInfo().getPortfolioValue());
    		                investRotatTableView.setTotalAmount(amount);
    		                investRotatTableView.setDepositInfo(depositInfoModel.getGetDepositInfo());
    		                container.addView(investRotatTableView);
    					}
    				}
    			}
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

    boolean needShowChart;

    int sharesColor = Color.rgb(255, 224, 82);
    int bondsColor = Color.rgb(254, 187, 10);
    int fundsColor = Color.rgb(243, 96, 0);
    int more = Color.rgb(209, 80, 0);

    @Override
    public void showChart() {
        if (BaseActivity.isOffline) {
        	createOfflineChart();
        	return;
        }
        
        int indexTotal = getVisiableRotateViewIndex();
        LogManager.d(indexTotal + "index");

        if (investInfoList == null || indexTotal < 0 || indexTotal >= investInfoList.size())
        	return;
        
        InvestmentInfo investmentInfo = investInfoList.get(indexTotal);
        int index = investmentInfo.investmentIndex;

        if (chartLayout != null) {
            chartLayout.removeAllViews();
        }
        
        if (investmentInfo.investmentType.equals(INVESTMENT_TYPE_ASSERT)) {
            showChartAssets();

            String amount = Utils.generateFormatMoney(container.getContext()
                    .getResources().getString(R.string.eur), assetsInformationList.get(index)
                    .getGetAssetsInfomation().getPortfolioValue());
            drawChartTitleBar(amount, assetsInformationList.get(index).getAccountAlias(), assetsInformationList.get(index).getIsPreferred());
            
            drawChartTitleBarAsset();
        } else {
            GetDepositInfoResponseModel depositInfo = depositInfoList.get(index).getGetDepositInfo();
            showChartDeposit(depositInfo);

            String amount = Utils.generateFormatMoney(container.getContext().getResources().getString(R.string.eur), depositInfo.getPortfolioValue());
            drawChartTitleBar(amount, depositInfoList.get(index).getAccountAlias(), depositInfoList.get(index).getIsPreferred());
        }
    }

    
	public void setCanOrientation() {
		MainActivity mainActivity = (MainActivity) container.getContext();

		if (BaseActivity.isOffline) {
			mainActivity.setCanOrientation(true);
			return;
		}

		if (investInfoList == null || investInfoList.size() == 0) {
			mainActivity.setCanOrientation(false);
		} else {
			mainActivity.setCanOrientation(true);
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

	private void showChartAssets() {
		LayoutInflater inflater = LayoutInflater.from(container.getContext());
		LinearLayout investmentsChartsContainer = (LinearLayout) inflater
				.inflate(R.layout.investment_charts, null);
		investmentsChartsContainer
				.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
					}
				});

		btn_shares = (RadioButton) investmentsChartsContainer
				.findViewById(R.id.btn_shares);
		btn_shares.setBackgroundResource(R.drawable.btn_asset_selector);

		btn_shares.setOnCheckedChangeListener(this);

		btn_bonds = (RadioButton) investmentsChartsContainer
				.findViewById(R.id.btn_bonds);
		btn_bonds.setVisibility(View.GONE);
		btn_funds = (RadioButton) investmentsChartsContainer
				.findViewById(R.id.btn_funds);
		btn_funds.setVisibility(View.GONE);
		btn_more = (RadioButton) investmentsChartsContainer
				.findViewById(R.id.btn_more);
		btn_more.setVisibility(View.GONE);

		chartView = (ChartView) investmentsChartsContainer
				.findViewById(R.id.chartView);
		chartView.setAntiAlias(true);
		chartView.setCenterTitle(context.getResources().getString(R.string.assets));
		chartView.setContainer(investmentsChartsContainer);
		ChartActivity chartActivity = (ChartActivity) ChartActivity.getContext();
		chartView.setTitleBarFont(chartActivity.chartsWindow);

		acps = chartView.createCharts(1);
		int size = acps.size();
		String name = context.getString(R.string.assets);
		for (int i = 0; i < size; i++) {
			ChartProp chartProp = acps.get(i);
			chartProp.setColor(sharesColor);
			chartProp.setPercent((float) 100);
			chartProp.setName(name);
		}

		chartLayout.addView(investmentsChartsContainer);
	}

	private void showChartDeposit(GetDepositInfoResponseModel depositInfo) {
		int color[] = new int[] { sharesColor, bondsColor, fundsColor, more };

		double percent[] = new double[] {
				depositInfo.getShares().getPercentage(),
				depositInfo.getBonds().getPercentage(),
				depositInfo.getFunds().getPercentage(),
				depositInfo.getOtherSecurities().getPercentage() };

		String names[] = context.getResources().getStringArray(
				R.array.investment_rote_names);// new String[] { "shares",
												// "bonds", "funds", "more" };

		LayoutInflater inflater = LayoutInflater.from(container.getContext());
		LinearLayout investmentsChartsContainer = (LinearLayout) inflater
				.inflate(R.layout.investment_charts, null);
		investmentsChartsContainer
				.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
					}
				});

		btn_shares = (RadioButton) investmentsChartsContainer
				.findViewById(R.id.btn_shares);
		if (depositInfo.getShares().getPercentage() == 0) {
			btn_shares
					.setBackgroundResource(R.drawable.graphics_investments_actions_off);
		} else {
			btn_shares.setOnCheckedChangeListener(this);
		}
		btn_bonds = (RadioButton) investmentsChartsContainer
				.findViewById(R.id.btn_bonds);
		if (depositInfo.getBonds().getPercentage() == 0) {
			btn_bonds
					.setBackgroundResource(R.drawable.graphics_investments_bonds_off);
		} else {
			btn_bonds.setOnCheckedChangeListener(this);
		}
		btn_funds = (RadioButton) investmentsChartsContainer
				.findViewById(R.id.btn_funds);
		if (depositInfo.getFunds().getPercentage() == 0) {
			btn_funds
					.setBackgroundResource(R.drawable.graphics_investments_sources_off);
		} else {
			btn_funds.setOnCheckedChangeListener(this);
		}
		btn_more = (RadioButton) investmentsChartsContainer
				.findViewById(R.id.btn_more);
		if (depositInfo.getOtherSecurities().getPercentage() == 0) {
			btn_more.setBackgroundResource(R.drawable.graphics_investments_other_off);
		} else {
			btn_more.setOnCheckedChangeListener(this);
		}
		chartView = (ChartView) investmentsChartsContainer
				.findViewById(R.id.chartView);
		chartView.setAntiAlias(true);
		chartView.setCenterTitle(context.getResources().getString(R.string.deposit_securities));
		ChartActivity chartActivity = (ChartActivity) ChartActivity.getContext();
		chartView.setTitleBarFont(chartActivity.chartsWindow);
	
		acps = chartView.createCharts(color.length);
		int size = acps.size();
		for (int i = 0; i < size; i++) {
			ChartProp chartProp = acps.get(i);
			chartProp.setColor(color[i]);
			chartProp.setPercent((float) percent[i]);
			chartProp.setName(names[i]);
		}
		chartLayout.addView(investmentsChartsContainer);
	}
	

	/*
	 * Only called in offline mode.
	 */
	private void createOfflineData() {
	     container.removeAllViews();
	     
	     //title
         LayoutInflater inflater = LayoutInflater.from(container.getContext());
         RotateTitleView rotateTitleView = (RotateTitleView)inflater.inflate(
                 R.layout.rotate_title_view, null);
         rotateTitleView.init();
         rotateTitleView.setTitleText("Offline");
         rotateTitleView.setTitleIcon(R.drawable.investments_icon);
         long time = System.currentTimeMillis();
         String nowTime = TimeUtil.getDateString(time, TimeUtil.dateFormat5);
         rotateTitleView.setUpdateTitle(container.getContext().getResources()
                 .getString(R.string.invest_update)
                 + " " + nowTime);
         container.addView(rotateTitleView);
         
         /*
          * One data for depositInfo
          */
         InvestRotatTableView investRotatTableView = new InvestRotatTableView(
                 container.getContext());
         investRotatTableView.setRotatResource(R.drawable.investment_bg,
                 R.drawable.btn_details, R.drawable.btn_details_over);
         investRotatTableView.setTotalAmountTitle(container.getContext().getResources()
                 .getString(R.string.portfolio_1));
         investRotatTableView.setTotalAmount("+$100,00");

         GetDepositInfoResponseModel depositInfo = new GetDepositInfoResponseModel();
         DepositInfo sharesInfo = new DepositInfo();
         sharesInfo.setPercentage(20.01);
         depositInfo.setShares(sharesInfo);
         
         DepositInfo bondsInfo = new DepositInfo();
         bondsInfo.setPercentage(40.00);
         depositInfo.setBonds(bondsInfo);
         
         DepositInfo fundsInfo = new DepositInfo();
         fundsInfo.setPercentage(10.00);
         depositInfo.setFunds(fundsInfo);

         DepositInfo otherSecuritiesInfo = new DepositInfo();
         otherSecuritiesInfo.setPercentage(30.00);
         depositInfo.setOtherSecurities(otherSecuritiesInfo);
         
         investRotatTableView.setDepositInfo(depositInfo);
         container.addView(investRotatTableView);
         
         /*
          * 2nd data for assess info
          */
	     //title
         LayoutInflater inflater1 = LayoutInflater.from(container.getContext());
         RotateTitleView rotateTitleView1 = (RotateTitleView)inflater1.inflate(
                 R.layout.rotate_title_view, null);
         rotateTitleView1.init();
         rotateTitleView1.setTitleText("Offline");
         rotateTitleView1.setTitleIcon(R.drawable.investments_icon);
         rotateTitleView1.setUpdateTitle(container.getContext().getResources()
                 .getString(R.string.invest_update)
                 + " " + nowTime);
         container.addView(rotateTitleView1);
         
         InvestRotatTableView investRotatTableView1 = new InvestRotatTableView(
                 container.getContext());
         investRotatTableView1.setRotatResource(R.drawable.investment_bg,
                 R.drawable.btn_details, R.drawable.btn_details_over);
         investRotatTableView1.setTotalAmountTitle(container.getContext().getResources()
                 .getString(R.string.total_amount));
         investRotatTableView1.setTotalAmount("+$200,00");
         container.addView(investRotatTableView1);
	}

	
	/*
	 * Create Offline Chart
	 */
	void createOfflineChart()  {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        LinearLayout investmentsChartsContainer = (LinearLayout)inflater.inflate(R.layout.investment_charts, null);

        chartView = (ChartView)investmentsChartsContainer.findViewById(R.id.chartView);
        chartView.setAntiAlias(true);
		chartView.setCenterTitle(context.getResources().getString(R.string.assets));

		int color[] = new int[] { sharesColor, bondsColor, fundsColor, more };
		double percent[] = new double[] { 100, 0, 0, 0 };
        String names[] =context.getResources().getStringArray(R.array.investment_rote_names);// new String[] { "shares", "bonds", "funds", "more" };

        acps = chartView.createCharts(color.length);
//        int size = acps.size();
        for (int i = 0; i < 1; i++) {
            ChartProp chartProp = acps.get(i);
            chartProp.setColor(color[i]);
            chartProp.setPercent((float)percent[i]);
            chartProp.setName(names[i]);
        }
        chartLayout.removeAllViews();
        chartLayout.addView(investmentsChartsContainer);
        drawChartTitleBar("+$100", "Personized Name", true);
        
        btn_shares = (RadioButton)investmentsChartsContainer.findViewById(R.id.btn_shares);
        btn_shares.setOnCheckedChangeListener(this);
        btn_bonds = (RadioButton)investmentsChartsContainer.findViewById(R.id.btn_bonds);
        btn_bonds.setOnCheckedChangeListener(this);
        btn_funds = (RadioButton)investmentsChartsContainer.findViewById(R.id.btn_funds);
        btn_funds.setOnCheckedChangeListener(this);
        btn_more = (RadioButton)investmentsChartsContainer.findViewById(R.id.btn_more);
        btn_more.setOnCheckedChangeListener(this);
        
        createOfflineChartAsset(investmentsChartsContainer);
    }
	
	void createOfflineChartAsset(LinearLayout investmentsChartsContainer) {
		btn_shares = (RadioButton) investmentsChartsContainer
				.findViewById(R.id.btn_shares);
		btn_shares.setBackgroundResource(R.drawable.btn_asset_selector);

		btn_shares.setOnCheckedChangeListener(this);

		btn_bonds = (RadioButton) investmentsChartsContainer
				.findViewById(R.id.btn_bonds);
		btn_bonds.setVisibility(View.GONE);
		btn_funds = (RadioButton) investmentsChartsContainer
				.findViewById(R.id.btn_funds);
		btn_funds.setVisibility(View.GONE);
		btn_more = (RadioButton) investmentsChartsContainer
				.findViewById(R.id.btn_more);
		btn_more.setVisibility(View.GONE);

		chartView = (ChartView) investmentsChartsContainer
				.findViewById(R.id.chartView);
		chartView.setAntiAlias(true);
		chartView.setContainer(investmentsChartsContainer);
		
		ChartActivity chartActivity = (ChartActivity) ChartActivity.getContext();
		chartView.setTitleBarFont(chartActivity.chartsWindow);

		drawChartTitleBarAsset();
	}
	
	void FillText(ViewGroup context, int intIdTitle, String title,int intIdValue, String value) {
		TextView tvTitle = (TextView) context.findViewById(intIdTitle);
		tvTitle.setText(title);

		TextView tvValue = (TextView) context.findViewById(intIdValue);
		tvValue.setText(value);
	}

	void FillText(ViewGroup context, int intIdTitle, int idStringTitle,int intIdValue, String value) {
		String title = context.getResources().getString(idStringTitle);
		FillText(context, intIdTitle, title, intIdValue, value);
	}

	private void drawChartTitleBar(String amount, String personlizedName, boolean isPreferred) {
		ChartActivity chartActivity = (ChartActivity) ChartActivity.getContext();

		FillText(chartActivity.chartsWindow, R.id.mid_title,R.string.portfolio_1, R.id.mid_value, amount);
		FillText(chartActivity.chartsWindow, R.id.accountType,R.string.deposit_3, R.id.accountName, personlizedName);

		chartActivity.chartsWindow.findViewById(R.id.chart_bar_table).setVisibility(View.GONE);

		TextView tvAccountName = (TextView) chartActivity.chartsWindow.findViewById(R.id.accountName);
		tvAccountName.setTextColor(container.getContext().getResources().getColor(R.color.xycolor_investment));

		TextView tvAccountType = (TextView) chartActivity.chartsWindow.findViewById(R.id.accountType);
		tvAccountType.setTextColor(container.getContext().getResources().getColor(R.color.xycolor_investment));

		TextView tvMidTitle = (TextView) chartActivity.chartsWindow.findViewById(R.id.mid_title);
		tvMidTitle.setTextColor(container.getContext().getResources().getColor(R.color.xycolor_investment));
		TextView tvMid = (TextView) chartActivity.chartsWindow.findViewById(R.id.mid_value);
		tvMid.setTextColor(container.getContext().getResources().getColor(R.color.xycolor_investment));
		
        /*
         * Icon 
         */
        ImageView iv = (ImageView) chartActivity.chartsWindow.findViewById(R.id.chart_icon);
        iv.setImageResource(R.drawable.top_icons_investments);
        
        setIsPreferred(isPreferred);
	}
	
    private void drawChartTitleBarAsset() {
		ChartActivity chartActivity = (ChartActivity) ChartActivity.getContext();
		TextView tvTitle = (TextView)chartActivity.chartsWindow.findViewById(R.id.mid_title);
		tvTitle.setText(R.string.total_amount);
		
		TextView tvAccountType = (TextView)chartActivity.chartsWindow.findViewById(R.id.accountType);
		tvAccountType.setText(R.string.assets_management_1);
    }

    
    public void setIsPreferred(boolean isPreferred) {
		ChartActivity chartActivity = (ChartActivity) ChartActivity
				.getContext();
		
		ImageView isPreferredStar =(ImageView) chartActivity.chartsWindow.findViewById(R.id.isPreferredStar);
		
		isPreferredStar.setImageResource(R.drawable.icone_star_orange);
		if (isPreferred)
			isPreferredStar.setVisibility(View.VISIBLE);
		else
			isPreferredStar.setVisibility(View.GONE);
    }
}
