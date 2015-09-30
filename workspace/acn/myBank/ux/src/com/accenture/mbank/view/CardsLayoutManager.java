
package com.accenture.mbank.view;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.accenture.mbank.MainActivity;
import com.accenture.mbank.R;
import com.accenture.mbank.logic.BalanceJson;
import com.accenture.mbank.model.BalanceAccountsModel;
import com.accenture.mbank.model.DashBoardModel;
import com.accenture.mbank.model.GetBalanceResponseModel;
import com.accenture.mbank.net.HttpConnector;
import com.accenture.mbank.net.ProgressOverlay;
import com.accenture.mbank.net.ProgressOverlay.OnProgressEvent;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.LogManager;
import com.accenture.mbank.view.protocol.ShowAble;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

public class CardsLayoutManager extends BankRollContainerManager {
    Handler mHandler;

    public List<BalanceAccountsModel> creditCardsAccounts = new ArrayList<BalanceAccountsModel>();

    public List<BalanceAccountsModel> prepaidCardsAccounts = new ArrayList<BalanceAccountsModel>();

    boolean isCreditCard = true;

    public static final String CREDIT_CARD_CODE = "872";

    public static final String PREPAID_CARD_CODE = "867";
    
    private GoogleAnalytics mGaInstance;
    
	private Tracker mGaTracker1;

    public CardsLayoutManager() {
        mHandler = new Handler();
    }

    public void setCards(LinearLayout linearLayout, List<BalanceAccountsModel> accounts,
            String paymentMethod) {
        linearLayout.removeAllViews();
        if (paymentMethod.equals("1")) {
            this.creditCardsAccounts = accounts;
            for (BalanceAccountsModel balanceAccountsModel : this.creditCardsAccounts) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                CardsDescriptionItem viewGroup = (CardsDescriptionItem)inflater.inflate(
                        R.layout.cards_desc_item, null);
                viewGroup.setAccount(balanceAccountsModel, paymentMethod);
                linearLayout.addView(viewGroup);
            }
        } else if (paymentMethod.equals("2")) {
            this.prepaidCardsAccounts = accounts;
            for (BalanceAccountsModel balanceAccountsModel : this.prepaidCardsAccounts) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                CardsDescriptionItem viewGroup = (CardsDescriptionItem)inflater.inflate(
                        R.layout.cards_desc_item, null);
                viewGroup.setAccount(balanceAccountsModel, paymentMethod);
                linearLayout.addView(viewGroup);
            }
        }
    }

    /**
     * 提取数据
     * 
     * @param balanceAccounts
     */
    public void getCreditCardsAccounts(List<BalanceAccountsModel> balanceAccounts) {
        for (BalanceAccountsModel balanceAccount : balanceAccounts) {
            if (balanceAccount.getBankServiceCode().equals(CREDIT_CARD_CODE)) {
                creditCardsAccounts.add(balanceAccount);
            } else if (balanceAccount.getBankServiceCode().equals(PREPAID_CARD_CODE)) {
                prepaidCardsAccounts.add(balanceAccount);
            }
        }

        createUiByData();

    }

    public void setAccounts(List<BalanceAccountsModel> accounts) {
        getCreditCardsAccounts(accounts);
    }

    @Override
    public void createUiByData() {

        BankRollContainer bankRollContainer = getRollContainer();

        BankRollView credit = (BankRollView)bankRollContainer.getShowAble(0);
        
        mGaInstance = GoogleAnalytics.getInstance(getContext());
        mGaTracker1 = mGaInstance.getTracker("UA-42551791-1");
        mGaTracker1.sendView("event.creditcards.detail");
        mGaTracker1.sendView("event.prepaidcards.detail");

        if (this.creditCardsAccounts.size() == 0) {       	        	
            MainActivity mainActivity = (MainActivity)getContext();
            mainActivity.creditRollView.setCloseImage(R.drawable.credit_cards_disable);
            mainActivity.creditRollView.setShowImage(R.drawable.sphere_cards_show_down);
        } else {
        	
            LinearLayout linearLayout = new LinearLayout(getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
            linearLayout.setLayoutParams(layoutParams);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            credit.setContent(linearLayout);
            credit.close();
            linearLayout.setBackgroundResource(R.drawable.box_details);
            for (BalanceAccountsModel balanceAccountsModel : this.creditCardsAccounts) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                CardsDescriptionItem viewGroup = (CardsDescriptionItem)inflater.inflate(
                        R.layout.cards_desc_item, null);
                viewGroup.setAccount(balanceAccountsModel, "1");
                linearLayout.addView(viewGroup);
                viewGroup.recordAllLayout.setParentScrollView(rollContainer);
            }
        }

        if (this.prepaidCardsAccounts.size() == 0) {
            MainActivity mainActivity = (MainActivity)getContext();
            mainActivity.prepaidRollView.setCloseImage(R.drawable.prepaid_card_disable);
            mainActivity.prepaidRollView.setShowImage(R.drawable.sphere_cards_show_down);
        } else {
            LinearLayout linearLayout1 = new LinearLayout(getContext());
            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                    LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
            linearLayout1.setLayoutParams(layoutParams1);
            linearLayout1.setOrientation(LinearLayout.VERTICAL);
            BankRollView prepaid = (BankRollView)bankRollContainer.getShowAble(1);
            prepaid.setContent(linearLayout1);
            linearLayout1.setBackgroundResource(R.drawable.box_details);
            for (BalanceAccountsModel balanceAccountsModel : this.prepaidCardsAccounts) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                CardsDescriptionItem viewGroup = (CardsDescriptionItem)inflater.inflate(
                        R.layout.cards_desc_item, null);
                viewGroup.setAccount(balanceAccountsModel, "1");

                linearLayout1.addView(viewGroup);
                viewGroup.recordAllLayout.setParentScrollView(rollContainer);

            }
            prepaid.close();
        }

    }

    public static boolean needUpdate = false;

    @Override
    public void onShow() {
        if (needUpdate && accounts != null) {

            needUpdate = false;
            accounts.clear();
            creditCardsAccounts.clear();
            prepaidCardsAccounts.clear();

        }
        if (accounts == null || accounts.size() <= 0) {
            loadCards();
        } else {
            performShowFromDashBoard(false);
        }
    }

    public static final String CARDS_PAYMENTMETHOD = "1";

    List<BalanceAccountsModel> accounts;

    private void loadCards() {
        ProgressOverlay progressOverlay = new ProgressOverlay(getRollContainer().getContext());
        progressOverlay.show("", new OnProgressEvent() {

            @Override
            public void onProgress() {
                String postData = BalanceJson.BalanceReportProtocal(CARDS_PAYMENTMETHOD,
                        Contants.publicModel);
                HttpConnector httpConnector = new HttpConnector();
                String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData,
                        getContext());
                GetBalanceResponseModel getBalanceResponse = BalanceJson
                        .parseGetBalanceResponse(httpResult);
                accounts = getBalanceResponse.getBanlaceAccounts();

                if (accounts != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            setAccounts(accounts);
                            performShowFromDashBoard(true);
                        }
                    });

                }
            }
        });
    }

    @Override
    public void onShow(ShowAble showAble) {

    }

    private static final int delayTime = 500;

    @Override
    protected void showFromDashBoard(boolean isDelay) {
        if (animateToDashBoardModel == null) {
            return;
        }
        BankRollContainer bankRollContainer = getRollContainer();
        for (int j = 0; j < bankRollContainer.getBankRollViewCount(); j++) {
            BankRollView showAble = (BankRollView)bankRollContainer.getShowAble(j);
            LinearLayout container = (LinearLayout)showAble.getContent();
            if (container == null) {
                continue;
            }
            BankRollView needShowBankRollView = null;
            CardsDescriptionItem needShowCardsDescriptionItem = null;
            for (int i = 0; i < container.getChildCount(); i++) {
                CardsDescriptionItem cardsDescriptionItem = (CardsDescriptionItem)container
                        .getChildAt(i);
                BalanceAccountsModel balanceAccountsModel2 = cardsDescriptionItem.getAccount();
                if (balanceAccountsModel2.getAccountCode().equals(
                        animateToDashBoardModel.getAccountCode())) {
                    needShowBankRollView = showAble;
                    needShowCardsDescriptionItem = cardsDescriptionItem;

                    break;
                }

            }
            if (needShowBankRollView != null) {

                if (needShowCardsDescriptionItem != null) {

                }

                final BankRollView bankRollView = needShowBankRollView;
                bankRollView.show();
                if (isDelay) {
                    needShowCardsDescriptionItem.expandAndOpen(bankRollView);
                } else {
                    needShowCardsDescriptionItem.expand();
                }
                animateToDashBoardModel = null;
                break;
                // int time = 0;
                // if (isDelay) {
                // time = delayTime;
                // }
                // mHandler.postDelayed(new Runnable() {
                //
                // @Override
                // public void run() {
                //
                // }
                // }, time);
                // break;
                // }
            }
        }

    }
}
