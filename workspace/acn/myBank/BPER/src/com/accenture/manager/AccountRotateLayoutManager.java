
package com.accenture.manager;

import it.gruppobper.ams.android.bper.R;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;

import com.accenture.manager.protocol.RotateBankViewManager;
import com.accenture.mbank.AccountDetailActivity;
import com.accenture.mbank.BaseActivity;
import com.accenture.mbank.MainActivity;
import com.accenture.mbank.logic.BalanceJson;
import com.accenture.mbank.logic.GetDashBoardDataJson;
import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.model.BalanceAccountsModel;
import com.accenture.mbank.model.DashBoardModel;
import com.accenture.mbank.model.GetBalanceResponseModel;
import com.accenture.mbank.model.GetDashBoardDataResponseModel;
import com.accenture.mbank.net.HttpConnector;
import com.accenture.mbank.net.ProgressOverlay;
import com.accenture.mbank.net.ProgressOverlay.OnProgressEvent;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.TimeUtil;
import com.accenture.mbank.view.AccountsLayout;
import com.accenture.mbank.view.InnerScrollView.OnInnerScrollListener;
import com.accenture.mbank.view.RotateTitleView;
import com.accenture.mbank.view.table.AccountsRotateTableView;
import com.accenture.mbank.view.table.LineFormView;
import com.accenture.mbank.view.table.RotatTableView;
import com.accenture.mbank.view.table.RotateTableViewWithButton.OnButtonClickListener;

public class AccountRotateLayoutManager extends RotateBankViewManager implements OnInnerScrollListener{

    Handler handler;
    public AccountRotateLayoutManager() {
        handler = new Handler();
    }

    static boolean reloadData = true;
    int dashboardAccounts = 0;

    @Override
    public void onShow() {
        MainActivity mainActivity = (MainActivity)container.getContext();
        int accounts = mainActivity.setting.getDashboardAccounts() + 1;
        if (BaseActivity.isOffline) {

                if (container.getChildCount() <= 0) {

                    for (int i = 0; i < 3; i++) {

                        LayoutInflater layoutInflater = LayoutInflater.from(container.getContext());
                        final RotateTitleView rTitleView = (RotateTitleView)layoutInflater.inflate(R.layout.rotate_title_view, null);
                        rTitleView.init();
                        rTitleView.setTitleIcon(R.drawable.icon_black_3);
                        rTitleView.setTitleText("Offline");
                        rTitleView.setUpdateTitle(TimeUtil.getDateString(System.currentTimeMillis(), TimeUtil.dateFormat5));
                        
                        container.addView(rTitleView);
                        final AccountsRotateTableView rotatTableView0 = new AccountsRotateTableView(container.getContext());
                        container.addView(rotatTableView0);
                        rotatTableView0.setCount(5);
                        rotatTableView0.setRotatResource(R.drawable.accounts_slider,R.drawable.account_5, -1);
                        rotatTableView0.parentScrollView = (ScrollView)container.getParent();
                        rotatTableView0.setButtonImage(R.drawable.account_btn_transaction);
                        rotatTableView0.setButtonImageOver(R.drawable.account_btn_transaction_over);

                        startAnimation(rotatTableView0);
                        return;
                    } //end for

                } // end if
                return;
        }//end if BaseActivity.isOffline
            
		if (reloadData == true || accounts != dashboardAccounts) {
			getDashBoardData();
			dashboardAccounts = accounts;
		}
    }

    List<DashBoardModel> list = new ArrayList<DashBoardModel>();

	public void getDashBoardData() {
        container.removeAllViews();
		list.clear();
		setCanOrientation();

		ProgressOverlay overlay = new ProgressOverlay(container.getContext());
		overlay.show("", new OnProgressEvent() {

			@Override
			public void onProgress() {
				String postData = GetDashBoardDataJson
						.getDashBoardDataReportProtocal(Contants.publicModel,
								dashboardAccounts, 2);
				HttpConnector httpConnector = new HttpConnector();
				String httpResult = httpConnector.requestByHttpPost(
						Contants.mobile_url, postData, container.getContext());
				GetDashBoardDataResponseModel getDashBoardData = GetDashBoardDataJson
						.ParseGetDashBoardDataResponse(httpResult);
				if (getDashBoardData != null &&
					getDashBoardData.getDashboardsList()!=null &&
					getDashBoardData.responsePublicModel.isSuccess()) {

					list = getDashBoardData.getDashboardsList();
					reloadData = false;

					/*
					 * To get locked balance from Balance Response
					 */
					postData = BalanceJson.BalanceReportProtocal(AccountsLayout.ACCOUNT_PAYMENTMETHOD,Contants.publicModel);
					httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData, container.getContext());
					GetBalanceResponseModel getBalanceResponse = BalanceJson.parseGetBalanceResponse(httpResult);
					
					if (getBalanceResponse != null) {
						mAccounts = getBalanceResponse.getBanlaceAccounts();
					} else {
						if (httpResult == null) {
							/*
							 *  A connection error happened
							 */
							onConnectionError();
						}
					}
				} else {
					if (httpResult == null) {
						/*
						 *  A connection error happened
						 */
						onConnectionError();
					}
					
					else if (getDashBoardData != null &&
							!getDashBoardData.responsePublicModel.isSuccess() &&
							!getDashBoardData.responsePublicModel.eventManagement.getErrorCode().equals(Contants.ERR_SESSION_EXPIRED_1) &&
							!getDashBoardData.responsePublicModel.eventManagement.getErrorCode().equals(Contants.ERR_SESSION_EXPIRED_2)) {
						/*
						 * maybe 90000 happens
						 */
						reloadData = true;
						handler.post(new Runnable() {
							@Override
							public void run() {
								onError(container.getContext());
							}
						});
					}
				}

				handler.post(new Runnable() {
					@Override
					public void run() {
						setAccount();
						setCanOrientation();

						if (needShowChart) {
							showChart();
						}
					}
				});
			}
		});
	}

	private void onConnectionError() {
		reloadData = true;
		handler.post(new Runnable() {
			@Override
			public void run() {
				final MainActivity mainActivity = (MainActivity)container.getContext();
				mainActivity.showTab(0);
			}
		});
	}

    void setAccount() {
        if (list != null && list.size() > 0) {
            container.removeAllViews();
            for (DashBoardModel dashBoardModel : list) {
                LayoutInflater layoutInflater = LayoutInflater.from(container.getContext());
                final RotateTitleView rTitleView = (RotateTitleView)layoutInflater.inflate(R.layout.rotate_title_view, null);
                rTitleView.init();
                rTitleView.setTitleIcon(R.drawable.icon_black_3);
                rTitleView.setTitleText(dashBoardModel.getPersonalizedName());
                rTitleView.setUpdateTitle(dashBoardModel.getDashboardDataList().get(0).getLastUpdate());
                container.addView(rTitleView);
                final AccountsRotateTableView rotatTableView0 = new AccountsRotateTableView(container.getContext());
                container.addView(rotatTableView0);

        		int rotateImageRes = AccountRotateLayoutManager.getRotateImageRes(dashboardAccounts);
        		rotatTableView0.setRotatResource(R.drawable.accounts_slider,
        				rotateImageRes, -1);
                drawAccountsRotateTableView(rotatTableView0, dashBoardModel, rTitleView, dashboardAccounts);
                
                startAnimation(rotatTableView0);

            	rotatTableView0.setOnButtonClickListener(new OnButtonClickListener() {

            		@Override
            		public void onClick(View v) {
            			loadAccountsAndGoDetail(container.getContext());
            		}
            	});
            }
        }
    }

	protected void startAnimation(final RotatTableView rotview) {
		updateArrowState();

		if (Contants.DASHBOARD_ROTATE_ANIMATION_ACCOUNTS == false)
			return;

		Contants.DASHBOARD_ROTATE_ANIMATION_ACCOUNTS = false;
		
		handler.postDelayed(new Runnable() {
			public void run() {
				rotview.startAnimation();
			}
		}, 300);
	}

    Handler mHandler = new Handler();
    List<BalanceAccountsModel> mAccounts = null;
    
    private void loadAccountsAndGoDetail(final Context context) {
    	if (mAccounts != null) {
    		GoDetail(context);
    		return;
    	}

        ProgressOverlay progressOverlay = new ProgressOverlay(context);
        progressOverlay.show("", new OnProgressEvent() {
            @Override
            public void onProgress() {
                String postData = BalanceJson.BalanceReportProtocal(AccountsLayout.ACCOUNT_PAYMENTMETHOD,Contants.publicModel);
                HttpConnector httpConnector = new HttpConnector();
                String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData, context);
                GetBalanceResponseModel getBalanceResponse = BalanceJson.parseGetBalanceResponse(httpResult);
                mAccounts = getBalanceResponse.getBanlaceAccounts();
                if (mAccounts != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                        	GoDetail(context);
                        }
                    });
                }
            }
        });
    }
    
	void GoDetail(Context context) {
		BalanceAccountsModel account = getAccountsByAccountCode(mAccounts, list);
		Intent intent = new Intent(context, AccountDetailActivity.class);
		for(AccountsModel accountModel : Contants.accountsList){
			if(accountModel.getAccountCode().equals(account.getAccountCode())){
				intent.putExtra("ACCOUNT_MODEL", accountModel);
			}
		}
		intent.putExtra("BALANCE_ACCOUNT", account);
		context.startActivity(intent);

        MainActivity mainActivity = (MainActivity)container.getContext();
        mainActivity.overridePendingTransition(R.anim.zoomin, 0);
	}
    
    public static int getRotateImageRes(int count) {
        int rotateImageRes = R.drawable.account_5;
        switch (count) {
            case 5:
                rotateImageRes = R.drawable.account_5;
                break;
            case 6:
                rotateImageRes = R.drawable.account_6;
                break;
            case 7:
                rotateImageRes = R.drawable.account_7;
                break;
            case 8:
                rotateImageRes = R.drawable.account_8;
                break;
            case 9:
                rotateImageRes = R.drawable.account_9;
                break;
            case 10:
                rotateImageRes = R.drawable.account_10;
                break;

            default:
                break;
        }
        return rotateImageRes;
    }



    private boolean needShowChart;

    @Override
    public void showChart() {
        if (BaseActivity.isOffline) {
            LineFormView lineFormView = new LineFormView(container.getContext(), null);
            chartLayout.removeAllViews();
            lineFormView.setIsPreferred(true);
            chartLayout.addView(lineFormView);
            return;
        }
        if (list != null && list.size() == 0) {
            needShowChart = true;
            return;
        }
        int index = getVisiableRotateViewIndex();
        DashBoardModel dashBoardModel = list.get(index);
        this.chartLayout.removeAllViews();
        LineFormView child = new LineFormView(chartLayout.getContext(), dashBoardModel);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        chartLayout.addView(child, params);

        double boundBalance = getBoundBalanceFromAccountCode(dashBoardModel.getAccountCode(), mAccounts);
        if ( boundBalance > 0)
        	child.setBoundBalance(boundBalance);

        child.setIsPreferred(getIsPreferredFromAccountCode(dashBoardModel.getAccountCode(), mAccounts));
        child.initValues();
    }

	public void setCanOrientation() {
		MainActivity mainActivity = (MainActivity) container.getContext();

		if (BaseActivity.isOffline) {
			mainActivity.setCanOrientation(true);
			return;
		}

		if (list == null || list.size() == 0) {
			mainActivity.setCanOrientation(false);
		} else {
			mainActivity.setCanOrientation(true);
		}

	}
}
