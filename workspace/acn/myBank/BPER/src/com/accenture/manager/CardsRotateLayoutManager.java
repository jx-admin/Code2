
package com.accenture.manager;

import it.gruppobper.ams.android.bper.R;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;

import com.accenture.manager.protocol.RotateBankViewManager;
import com.accenture.mbank.AccountDetailActivity;
import com.accenture.mbank.BaseActivity;
import com.accenture.mbank.CardDetailActivity;
import com.accenture.mbank.CardDetailCorporateActivity;
import com.accenture.mbank.CardListActivity;
import com.accenture.mbank.MainActivity;
import com.accenture.mbank.logic.BalanceJson;
import com.accenture.mbank.logic.GetDashBoardDataJson;
import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.model.BalanceAccountsModel;
import com.accenture.mbank.model.DashBoardModel;
import com.accenture.mbank.model.DashboardDataModel;
import com.accenture.mbank.model.GetBalanceResponseModel;
import com.accenture.mbank.model.GetDashBoardDataResponseModel;
import com.accenture.mbank.net.HttpConnector;
import com.accenture.mbank.net.ProgressOverlay;
import com.accenture.mbank.net.ProgressOverlay.OnProgressEvent;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.LogManager;
import com.accenture.mbank.util.TimeUtil;
import com.accenture.mbank.util.Utils;
import com.accenture.mbank.view.CardsLayoutManager;
import com.accenture.mbank.view.RotateTitleView;
import com.accenture.mbank.view.table.AccountsRotateTableView;
import com.accenture.mbank.view.table.CardsBarGraphic;
import com.accenture.mbank.view.table.CardsRotateTableView;
import com.accenture.mbank.view.table.CorporateCardsRotateTableView;
import com.accenture.mbank.view.table.LineFormView;
import com.accenture.mbank.view.table.RotatTableView;
import com.accenture.mbank.view.table.RotatTableView.OnSlidListener;
import com.accenture.mbank.view.table.RotateTableViewWithButton;
import com.accenture.mbank.view.table.RotateTableViewWithButton.OnButtonClickListener;

public class CardsRotateLayoutManager extends RotateBankViewManager{

    int dashboardCards;

    int m_currentIndex = -1;
    int m_index_final = -1;
    
    public CardsRotateLayoutManager() {
        handler = new Handler();
    }

    static boolean reloadData = true;
    protected List<DashBoardModel> list = new ArrayList<DashBoardModel>();;

    @Override
    public void onShow() {

        if (BaseActivity.isOffline) {
        	addOfflineCards();
        } else {

            MainActivity mainActivity = (MainActivity)container.getContext();
            int cards = mainActivity.setting.getDashboardCards() + 1;
            if (cards != dashboardCards || reloadData == true) {
                dashboardCards = cards;
                getDashBoardData();
            }
        }
    }

    Handler handler;

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
								dashboardCards, 1);
				HttpConnector httpConnector = new HttpConnector();
				String httpResult = httpConnector.requestByHttpPost(
						Contants.mobile_url, postData, container.getContext());
				GetDashBoardDataResponseModel getDashBoardData = GetDashBoardDataJson
						.ParseGetDashBoardDataResponse(httpResult);
				if (getDashBoardData != null &&
					getDashBoardData.getDashboardsList() != null &&
					getDashBoardData.responsePublicModel.isSuccess()) {

					list = getDashBoardData.getDashboardsList();
					reloadData = false;

					/*
					 * To get locked balance from Balance Response
					 */
					postData = BalanceJson.BalanceReportProtocal(CardListActivity.CARDS_PAYMENTMETHOD,Contants.publicModel);
					httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData,container.getContext());
					GetBalanceResponseModel getBalanceResponse = BalanceJson.parseGetBalanceResponse(httpResult);
					if (getBalanceResponse != null) {
						mAccounts = getBalanceResponse.getBanlaceAccounts();
					}
					else {
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
						setCards();
						setCanOrientation();
						
						if (needShowChart) {
							showChart();
							needShowChart = false;
						}
					}

				});
			}
		});
	}

	private void onConnectionError() {
		/*
		 * A connection error happened
		 */
		reloadData = true;
		handler.post(new Runnable() {
			@Override
			public void run() {
				final MainActivity mainActivity = (MainActivity) container
						.getContext();
				mainActivity.showTab(0);
			}
		});
	}

    private void setCards() {

        if (list != null && list.size() > 0) {
            container.removeAllViews();
            for (DashBoardModel dashBoardModel : list) {
                LayoutInflater layoutInflater = LayoutInflater.from(container.getContext());
                final RotateTitleView rTitleView = (RotateTitleView)layoutInflater.inflate(
                        R.layout.rotate_title_view, null);
                rTitleView.init();

                rTitleView.setTitleIcon(R.drawable.cards_icon);
                rTitleView.setTitleText(dashBoardModel.getPersonalizedName());
				if (dashBoardModel.getDashboardDataList().size() > 0)
					rTitleView.setUpdateTitle(dashBoardModel
							.getDashboardDataList().get(0).getLastUpdate());
                container.addView(rTitleView);

				/*
				 * Special treat with IBAN card, behavior same to Account
				 */
				if (getServiceCodeFromAccountCode(
						dashBoardModel.getAccountCode()).equals(Contants.IBAN_CARD_CODE)) {
					final AccountsRotateTableView rotatTableView = new AccountsRotateTableView(
							container.getContext());
					container.addView(rotatTableView);

					int bag = CardsRotateLayoutManager.getRotateImageRes(dashboardCards);
					rotatTableView.setRotatResource(R.drawable.card_slider, bag, -1);

					drawAccountsRotateTableView(rotatTableView, dashBoardModel,
							rTitleView, dashboardCards);

					setButtonImage(rotatTableView);
					setButtonOnClick(rotatTableView, dashBoardModel);

					startAnimation(rotatTableView);
				} 
				/*
				 * Normal Cards
				 */
				else if (!getCardRelationsFromAccountCode(dashBoardModel.getAccountCode()).equals("P") ) {
					final CardsRotateTableView rotateTableView = new CardsRotateTableView(
							container.getContext());

					container.addView(rotateTableView);
					drawCardsRotateTableView(rotateTableView, dashBoardModel,
							rTitleView);
					setButtonImage(rotateTableView);
					setButtonOnClick(rotateTableView, dashBoardModel);

					startAnimation(rotateTableView);
				}
				/*
				 * Corporate Card, (CARTA AZIENDALE)
				 */
				else {
					final CorporateCardsRotateTableView rotateTableView = new CorporateCardsRotateTableView(
							container.getContext());
					container.addView(rotateTableView);

					drawCorporateCardsRotateTableView(rotateTableView, dashBoardModel,
							rTitleView);
					rTitleView.disableRotateImage();
					
					/*
					 * Use the detail button.
					 */
					setButtonImage2(rotateTableView);
					setButtonOnClick(rotateTableView, dashBoardModel);
				}
			}
		}
    }

	protected void drawAccountsRotateTableView(
			final AccountsRotateTableView rotatTableView0,
			final DashBoardModel dashBoardModel,
			final RotateTitleView rTitleView, int dashboardAccounts) {
		rotatTableView0.setCount(dashboardAccounts);

		rotatTableView0.parentScrollView = (ScrollView) container.getParent();
		rotatTableView0.setAccountBalanceValue(dashBoardModel
				.getPersonalizedName());
		rotatTableView0.setButtonImage(R.drawable.account_btn_transaction);
		rotatTableView0
				.setButtonImageOver(R.drawable.account_btn_transaction_over);
		String accountBalanceValue = Utils.generateFormatMoney(dashBoardModel.getAccountBalance());
		rotatTableView0.setAccountBalanceValue(accountBalanceValue);

		String avaString = Utils.generateFormatMoney(dashBoardModel.getAvailableBalance());
		rotatTableView0.setAvailableBalanceValue(avaString);
		if (dashBoardModel != null
				&& dashBoardModel.getDashboardDataList() != null
				&& dashBoardModel.getDashboardDataList().size() > 0) {
			setDashboardUi(dashBoardModel, rotatTableView0, rTitleView, 0);
		}
		rotatTableView0.setOnSlidListener(new OnSlidListener() {
			@Override
			public void onSlid(View v, int index) {

				if (index != 0) {
					rotatTableView0.setAvailableBalanceValue(container
							.getContext().getResources()
							.getString(R.string.not_able));
				} else {
					String avaString = Utils.generateFormatMoney(dashBoardModel.getAvailableBalance());
					rotatTableView0.setAvailableBalanceValue(avaString);
				}
				setDashboardUi(dashBoardModel, rotatTableView0, rTitleView,
						index);
			}

		});
	}

    protected void setDashboardUi(final DashBoardModel dashBoardModel2,final AccountsRotateTableView rotatTableView0, RotateTitleView titleView, int index) {
        List<DashboardDataModel> list = dashBoardModel2.getDashboardDataList();

		if (index < 0 || index >= list.size())
			return;

        DashboardDataModel dashboardDataModel = list.get(index);

        String deposite = Utils.generateFormatMoney(dashboardDataModel.getDeposits());
        rotatTableView0.setDepositValue(deposite);
        String width = Utils.generateFormatMoney(dashboardDataModel.getWithdrawals());
        rotatTableView0.setWidthdrawalsValue(width);

        double value = dashboardDataModel.getAccountBalance();
        String accountBalanceValue = Utils.generateFormatMoney(value);

        rotatTableView0.setAccountBalanceValue(accountBalanceValue);
        String date = TimeUtil.changeFormattrString(dashboardDataModel.getLastUpdate(),TimeUtil.dateFormat2, TimeUtil.dateFormat5);
        titleView.setUpdateTitle(container.getContext().getResources().getString(R.string.updated_date)+ " " + date);
    }

	protected void startAnimation(final RotatTableView rotview) {
		updateArrowState();

		if (Contants.DASHBOARD_ROTATE_ANIMATION_CARDS == false)
			return;

		Contants.DASHBOARD_ROTATE_ANIMATION_CARDS = false;
		
		handler.postDelayed(new Runnable() {
			public void run() {
				rotview.startAnimation();
			}
		}, 300);
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

	void drawCorporateCardsRotateTableView(
			final CorporateCardsRotateTableView cardsRotateTableView,
			final DashBoardModel dashBoardModel,
			final RotateTitleView rTitleView) {

		cardsRotateTableView.setRotatResource(-1, R.drawable.investment_bg, -1);
		cardsRotateTableView.parentScrollView = (ScrollView) container
				.getParent();

		/*
		 * Available Balance
		 */
		String text = Utils.generateFormatMoney(dashBoardModel.getAvailableBalance());
		if(dashBoardModel.getAvailableBalance()==0){
			text=text.replace("+", "");
		}
		cardsRotateTableView.setAvailableBalance(text);

		/*
		 * Plafond
		 */
		text = Utils.generateFormatMoney(getPlafondFromAccountCode(dashBoardModel.getAccountCode()));
		cardsRotateTableView.setPlafond(text);

		/*
		 * expirationDate
		 */
		String date = getExpirationDateFromAccountCode(dashBoardModel
				.getAccountCode());
		if (!date.equals("")) {
			String datetext = TimeUtil.changeFormattrString(date,
					TimeUtil.dateFormat2, TimeUtil.dateFormat5);
			cardsRotateTableView.setExpirationDate(datetext);
		}

		/*
		 * Current time in the title
		 */
		String nowTime = TimeUtil.getDateString(System.currentTimeMillis(),
				TimeUtil.dateFormat5);
		rTitleView.setUpdateTitle(container.getContext().getResources()
				.getString(R.string.updated_date)
				+ " " + nowTime);
	}

	void drawCardsRotateTableView(
			final CardsRotateTableView cardsRotateTableView,
			final DashBoardModel dashBoardModel,
			final RotateTitleView rTitleView) {
		cardsRotateTableView.setCount(dashboardCards);
		int bag = CardsRotateLayoutManager.getRotateImageRes(dashboardCards);
		cardsRotateTableView.setRotatResource(R.drawable.card_slider, bag, -1);

		String text = Utils.noSignGenerateFormatMoney(container.getContext()
				.getResources().getString(R.string.eur),
				dashBoardModel.getAccountBalance());
		cardsRotateTableView.setTotalWithDrawals(text);
		text = Utils.generateFormatMoney(dashBoardModel.getAvailableBalance());
		cardsRotateTableView.setAvaliablity(text);

		cardsRotateTableView.parentScrollView = (ScrollView) container
				.getParent();

		if (dashBoardModel != null
				&& dashBoardModel.getDashboardDataList() != null
				&& dashBoardModel.getDashboardDataList().size() > 0) {
			setDashboardUi(dashBoardModel, cardsRotateTableView, rTitleView, 0);
		}
		cardsRotateTableView.setOnSlidListener(new OnSlidListener() {

			@Override
			public void onSlid(View v, int index) {
				LogManager.i(dashBoardModel.toString());
				if (index != 0) {
					cardsRotateTableView.setAvaliablity(container.getContext()
							.getResources().getString(R.string.not_able));
				} else {
					String avaString = Utils.generateFormatMoney(dashBoardModel.getAvailableBalance());
					cardsRotateTableView.setAvaliablity(avaString);
				}

				setDashboardUi(dashBoardModel, cardsRotateTableView,
						rTitleView, index);
			}
		});
	}

    protected void setDashboardUi(final DashBoardModel dashBoardModel2,
            final CardsRotateTableView rotatTableView0, RotateTitleView titleView, int index) {
        List<DashboardDataModel> list = dashBoardModel2.getDashboardDataList();

		if (index < 0 || index >= list.size())
			return;
        DashboardDataModel dashboardDataModel = list.get(index);

        /*
         * Prepaid card, with out +-. normal card with +-
         */
        String withdwawals = "";
        String serviceCode = getServiceCodeFromAccountCode(dashBoardModel2.getAccountCode());
        if(serviceCode.equals(Contants.PREPAID_CARD_CODE) || 
        		serviceCode.equals(Contants.PREPAID_CARD_CODE_1) ) {
        	withdwawals = Utils.noSignGenerateFormatMoney(container.getContext().getResources()
                .getString(R.string.eur), dashboardDataModel.getWithdrawals());
        }
        else {
        	withdwawals = Utils.generateFormatMoney(dashboardDataModel.getWithdrawals());
        }

        rotatTableView0.setTotalWithDrawals(withdwawals);

        String date = TimeUtil.changeFormattrString(dashboardDataModel.getLastUpdate(),
                TimeUtil.dateFormat2, TimeUtil.dateFormat5);
        titleView.setUpdateTitle(container.getContext().getResources()
                .getString(R.string.updated_date) + " "
                + date);
    }

	void setButtonImage(RotateTableViewWithButton button) {
		button.setButtonImage(R.drawable.cards_btn_transactions);
		button.setButtonImageOver(R.drawable.cards_btn_transactions_over);
	}

	void setButtonImage2(RotateTableViewWithButton button) {
		button.setButtonImage(R.drawable.btn_movimenti_cards);
		button.setButtonImageOver(R.drawable.btn_down_movimenti_cards);
	}

	void setButtonOnClick(RotateTableViewWithButton button,
			final DashBoardModel dashBoardModel) {
		button.setOnButtonClickListener(new OnButtonClickListener() {

			@Override
			public void onClick(View v) {
				loadCardsAndGoDetail(container.getContext(), dashBoardModel.getAccountCode());
			}
		});
	}

    Handler mHandler = new Handler();
    List<BalanceAccountsModel> mAccounts = null;
    
    public void loadCardsAndGoDetail(final Context context, final String accountCode) {
    	if (mAccounts != null) {
    		GoDetail(context, accountCode);
    		return;
    	}

        ProgressOverlay progressOverlay = new ProgressOverlay(context);
        progressOverlay.show("", new OnProgressEvent() {

            @Override
            public void onProgress() {
                String postData = BalanceJson.BalanceReportProtocal(CardListActivity.CARDS_PAYMENTMETHOD,Contants.publicModel);
                HttpConnector httpConnector = new HttpConnector();
                String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData,context);
                GetBalanceResponseModel getBalanceResponse = BalanceJson.parseGetBalanceResponse(httpResult);
                mAccounts = getBalanceResponse.getBanlaceAccounts();
                if (mAccounts != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                        	GoDetail(context, accountCode);
                        }
                    });

                }
            }
        });
    }

	void GoDetail(Context context, String accountCode) {
		BalanceAccountsModel account = getAccountsByAccountCode(mAccounts, list);
		AccountsModel _accountModel = null;
		
		for(AccountsModel accountModel : Contants.cardAccounts){
			if(accountModel.getAccountCode().equals(account.getAccountCode())){
				_accountModel = accountModel;
			}
		}
		
		Intent intent = null;
		/*
		 * Special treat with IBAN card, behavior same to Account
		 */
		if (getServiceCodeFromAccountCode(accountCode).equals(Contants.IBAN_CARD_CODE)) {
			intent = new Intent(context, AccountDetailActivity.class);
		}
		else {
			if(_accountModel.getCardRelations().equals("P")){
				intent = new Intent(context, CardDetailCorporateActivity.class);
			}else{
				intent = new Intent(context, CardDetailActivity.class);
			}
		}
		
		intent.putExtra("ACCOUNT_MODEL", _accountModel);
		intent.putExtra("BALANCE_ACCOUNT", account);
		intent.putExtra("BANK_SERVICE_CODE", account.getBankServiceCode());
		context.startActivity(intent);

        MainActivity mainActivity = (MainActivity)container.getContext();
        mainActivity.overridePendingTransition(R.anim.zoomin, 0);
	}

    private void addOfflineCards() {
        container.removeAllViews();
        container.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });

        /*
         * Add 1st dashborad
         */
        LayoutInflater layoutInflater = LayoutInflater.from(container.getContext());
        final RotateTitleView rTitleView = (RotateTitleView)layoutInflater.inflate(
                R.layout.rotate_title_view, null);
        rTitleView.init();

        rTitleView.setTitleIcon(R.drawable.cards_icon);
        container.addView(rTitleView);

        CardsRotateTableView rotatTableView = new CardsRotateTableView(container.getContext());
        container.addView(rotatTableView);
        container.setPadding(RotatTableView.margin, 0, 0, 0);
        rotatTableView.setCount(13);
        rotatTableView.setRotatResource(R.drawable.card_slider, R.drawable.cards_13place, -1);
        rotatTableView.setButtonImage(R.drawable.cards_btn_transactions);
        rotatTableView.setButtonImageOver(R.drawable.cards_btn_transactions_over);
        startAnimation(rotatTableView);
        /*
         * Add 2nd dashborad
         */
        LayoutInflater layoutInflater1 = LayoutInflater.from(container.getContext());
        final RotateTitleView rTitleView1 = (RotateTitleView)layoutInflater1.inflate(
                R.layout.rotate_title_view, null);
        rTitleView1.init();

        rTitleView1.setTitleIcon(R.drawable.cards_icon);
        container.addView(rTitleView1);

        AccountsRotateTableView rotatTableView1 = new AccountsRotateTableView(
                container.getContext());
        container.addView(rotatTableView1);
        container.setPadding(RotateTableViewWithButton.margin, 0, 0, 0);
        rotatTableView1.setCount(13);
        rotatTableView1.setRotatResource(R.drawable.card_slider, R.drawable.cards_13place, -1);
        rotatTableView1.setButtonImage(R.drawable.cards_btn_transactions);
        rotatTableView1.setButtonImageOver(R.drawable.cards_btn_transactions_over);

        rotatTableView1.parentScrollView = (ScrollView)container.getParent();
        rotatTableView.parentScrollView = (ScrollView)container.getParent();

        rotatTableView.setOnButtonClickListener(new OnButtonClickListener() {

            @Override
            public void onClick(View v) {
                // rotatTableView
            }
        });

        /*
         * Add 3rd dashborad
         */
        LayoutInflater layoutInflater2 = LayoutInflater.from(container.getContext());
        final RotateTitleView rTitleView2 = (RotateTitleView)layoutInflater2.inflate(
                R.layout.rotate_title_view, null);
        rTitleView2.init();

        rTitleView2.setTitleIcon(R.drawable.cards_icon);
        container.addView(rTitleView2);
 
        CorporateCardsRotateTableView rotatTableView2 = new CorporateCardsRotateTableView(
                container.getContext());
        container.addView(rotatTableView2);

        container.setPadding(RotatTableView.margin, 0, 0, 0);
        rotatTableView2.setRotatResource(-1, R.drawable.investment_bg, -1);
        rotatTableView2.setButtonImage(R.drawable.cards_btn_transactions);
        rotatTableView2.setButtonImageOver(R.drawable.cards_btn_transactions_over);
        rotatTableView2.parentScrollView = (ScrollView)container.getParent();
        rotatTableView2.setAvailableBalance("+$100");
        rotatTableView2.setPlafond("+$100");
        rotatTableView2.setExpirationDate("25.2.2014");
        
    }

    private boolean needShowChart = false;

	@Override
	public void showChart() {
        if (BaseActivity.isOffline) {
        	CardsBarGraphic child = new CardsBarGraphic(container.getContext(), null);
            chartLayout.removeAllViews();
            chartLayout.addView(child);
	        child.setIsPrepaidCard(true);
            child.setIsPreferred(true);
            return;
        }

		if (list.size() == 0) {
			needShowChart = true;
			return;
		}
		
		/*
		 * To use the value updated in scroll event
		 */
		int index = m_index_final;
		if (index == -1)
			index = getVisiableRotateViewIndex();

		DashBoardModel model = list.get(index);
		chartLayout.removeAllViews();

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		/*
		 * Special treat with IBAN cards
		 */
		String serviceCode = getServiceCodeFromAccountCode(model
				.getAccountCode());
		if (serviceCode.equals(Contants.IBAN_CARD_CODE)) {
			LineFormView child = new LineFormView(chartLayout.getContext(),
					serviceCode, model);
			
	        double boundBalance = getBoundBalanceFromAccountCode(model.getAccountCode(), mAccounts);
	        if ( boundBalance > 0)
	        	child.setBoundBalance(boundBalance);
	        
	        child.setIsPreferred(getIsPreferredFromAccountCode(model.getAccountCode()));
			child.initValues();
			chartLayout.addView(child, params);
		} else {
			CardsBarGraphic child = new CardsBarGraphic(
					chartLayout.getContext(), model);
			
	        child.setIsPreferred(getIsPreferredFromAccountCode(model.getAccountCode()));
	        if(serviceCode.equals(Contants.PREPAID_CARD_CODE) || 
	        		serviceCode.equals(Contants.PREPAID_CARD_CODE_1)){
	        	child.setIsPrepaidCard(true);
	        }
			chartLayout.addView(child, params);
		}
	}

	/*
	 * Corporation card can not rotate to show chart
	 */
	@Override
	public void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);

		int index = getVisiableRotateViewIndex();
	
		if (m_currentIndex != index) {
			LogManager.i("INDEX" + m_currentIndex + "->" + index);
			m_currentIndex = index;
			setCanOrientation();
		}
	}

	public void setCanOrientation() {
		MainActivity mainActivity = (MainActivity) container.getContext();
		mainActivity.setCanOrientation(false);

		int index;
		if (m_currentIndex != -1)
			index = m_currentIndex;
		else
			index = getVisiableRotateViewIndex();

		final int f_index = index;
		if (BaseActivity.isOffline) {
			mainActivity.setCanOrientation(true);
			return;
		}
		if (list == null || list.size() ==0 ) {
			mainActivity.setCanOrientation(false);
			return;
		}

		if (list != null && index >= 0 && index < list.size()) {
			DashBoardModel model = list.get(index);

			if (getCardRelationsFromAccountCode(model.getAccountCode()).equals("P")) {
				mainActivity.setCanOrientation(false);
				
				handler.postDelayed(new Runnable() {
					public void run() {
						m_index_final = f_index;
						LogManager.i(m_index_final + "rotate false");
					}
				}, 500);

			} else {
				mainActivity.setCanOrientation(true);
				
				handler.postDelayed(new Runnable() {
					public void run() {
						m_index_final = f_index;
						LogManager.i(m_index_final + "rotate true");
					}
				}, 500);
			}
		}
	}
	
	private String getServiceCodeFromAccountCode(String accountCode) {
		String ret = "";
		if (Contants.getUserInfo != null
				&& Contants.getUserInfo.getUserprofileHb().getAccountList() != null
				&& Contants.getUserInfo.getUserprofileHb().getAccountList()
						.size() > 0) {

			for (AccountsModel accountsModel : Contants.getUserInfo
					.getUserprofileHb().getAccountList()) {

				if (accountsModel.getAccountCode().equals(accountCode))
					ret = accountsModel.getBankServiceType()
							.getBankServiceCode();
			}
		}
		return ret;
	}

	private AccountsModel getAccountsModelFromUserInfo(String accountCode) {
		AccountsModel ret = null;
		if (Contants.getUserInfo != null
				&& Contants.getUserInfo.getUserprofileHb().getAccountList() != null
				&& Contants.getUserInfo.getUserprofileHb().getAccountList()
						.size() > 0) {

			for (AccountsModel accountsModel : Contants.getUserInfo
					.getUserprofileHb().getAccountList()) {
				if (accountsModel.getAccountCode().equals(accountCode))
					ret = accountsModel;
			}
		}
		return ret;
	}

	private String getCardRelationsFromAccountCode(String accountCode) {
		String ret = "";
		AccountsModel account = getAccountsModelFromUserInfo(accountCode);
		if (account != null)
			ret = account.getCardRelations();
		return ret;
	}

	private double getPlafondFromAccountCode(String accountCode) {
		double ret = 0;
		AccountsModel account = getAccountsModelFromUserInfo(accountCode);
		if (account != null)
			ret = account.getPlafond();
		return ret;
	}

	private String getExpirationDateFromAccountCode(String accountCode) {
		String ret = "";
		AccountsModel account = getAccountsModelFromUserInfo(accountCode);
		if (account != null)
			ret = account.getExpirationDate();
		return ret;
	}
	
	private boolean getIsPreferredFromAccountCode(String accountCode) {
		boolean ret = false;
		AccountsModel account = getAccountsModelFromUserInfo(accountCode);
		if (account != null)
			ret = account.getIsPreferred();
		return ret;
	}
}
