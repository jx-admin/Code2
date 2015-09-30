
package com.act.mbanking.manager;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.act.mbanking.R;
import com.act.mbanking.activity.MainActivity;
import com.act.mbanking.manager.view.MainLayout;

public class MainManager extends Manager {
	public static final String TAG="MainManager";

    /**
     * 暂时不使用它
     */
    protected MainLayout mainLayout;

    public ViewGroup chartLayout;

    protected MainMenuSubScreenManager paymentsManager, aggregatedManager, accountsManager,
            investmentsManager, cardsManager, loansManager, recentPaymentsManager, contactsManager,
            guideManager, guideDetailManager, userInfoManager, accountsLevel2, investmentLevel2,
            cardsLevel2, investmentDetailsManager, investmentAssetDetailsManager,
            loansDetailsManager, creditCardsDetailsManager, accountDetailsManager;

    protected MainActivity activity;

    MainMenuSubScreenManager currentSubScreenManager;

    MainMenuSubScreenManager lastSubScreenManager;

    protected List<MainMenuSubScreenManager> mainMenuSubScreenManagers;

    public MainManager(MainActivity activity) {

        mainMenuSubScreenManagers = new ArrayList<MainMenuSubScreenManager>();
        this.activity = activity;
        init();
    }

    void init() {

        mainMenuSubScreenManagers.clear();
        mainLayout = (MainLayout)activity.findViewById(R.id.main_layout);
        paymentsManager = new NPaymentsManager(activity);
        aggregatedManager = new AggregatedViewManager(activity);
        recentPaymentsManager = new NRecentPaymentsManager(activity);
        investmentsManager = new InvestmentManager(activity);
        loansManager = new LoansManager(activity);
        cardsManager = new CardsManager(activity);
        accountsManager = new AccountsManager(activity);
        userInfoManager = new UserInfoManager(activity);
        guideManager = new GuideManager(activity);
        guideDetailManager = new GuideDetailManager(activity);
        contactsManager = new ContactsManager(activity);
        investmentLevel2 = new InvestmentLevel2Manager(activity);
        cardsLevel2 = new CreditCardLevel2Manager(activity);
        accountsLevel2 = new AccountsLevel2Manager(activity);
        investmentDetailsManager = new InvestmentDetailsManager(activity);
        investmentAssetDetailsManager = new InvestmentAssetDetailsManager(activity);
        loansDetailsManager = new LoansDetailsManager(activity);
        creditCardsDetailsManager = new CreditCardsDetailsManager(activity);
        accountDetailsManager = new AccountDetailsManager(activity);

        mainMenuSubScreenManagers.add(paymentsManager);
        mainMenuSubScreenManagers.add(aggregatedManager);
        mainMenuSubScreenManagers.add(recentPaymentsManager);
        mainMenuSubScreenManagers.add(investmentsManager);
        mainMenuSubScreenManagers.add(loansManager);
        mainMenuSubScreenManagers.add(cardsManager);
        mainMenuSubScreenManagers.add(accountsManager);
        mainMenuSubScreenManagers.add(userInfoManager);
        mainMenuSubScreenManagers.add(guideManager);
        mainMenuSubScreenManagers.add(guideDetailManager);
        mainMenuSubScreenManagers.add(contactsManager);
        mainMenuSubScreenManagers.add(investmentLevel2);
        mainMenuSubScreenManagers.add(cardsLevel2);
        mainMenuSubScreenManagers.add(accountsLevel2);
        mainMenuSubScreenManagers.add(investmentDetailsManager);
        mainMenuSubScreenManagers.add(investmentAssetDetailsManager);
        mainMenuSubScreenManagers.add(loansDetailsManager);
        mainMenuSubScreenManagers.add(creditCardsDetailsManager);
        mainMenuSubScreenManagers.add(accountDetailsManager);

        for (int i = 0; i < mainMenuSubScreenManagers.size(); i++) {
            MainMenuSubScreenManager mainMenuSubScreenManager = mainMenuSubScreenManagers.get(i);
            mainMenuSubScreenManager.setMainManager(this);
        }

        showAggregatedView(false, null);
        // showCreditCardsDetails(false, null);
        // showAccountDetails(false, null);
        // showInvestments(false, null);
        // showLoanDetails(false, null);
        // AccountsModel mAccountsModel = new AccountsModel();
        // mAccountsModel.setAccountCode(Contants.investmentAccounts.get(0).getAccountCode());
        // showInvestmentDetails(false, mAccountsModel);
        // showLevel3CircleManager(false,null);
        // showInvestmentLevel2(false,null);
        // showCardsLevel2(false,null);
        // showAccountLevel2(false, null);
        // showLevel3CircleInfo(false, null);
        activity.canOrientation = true;

    }

    public void showNewPayment(boolean isback, Object object) {
        changeCurrentSubScreenManager(paymentsManager, isback, object);
        activity.canOrientation = false;
    }

    public void showLoans(boolean isback, Object object) {
        changeCurrentSubScreenManager(loansManager, isback, object);
        activity.canOrientation = true;
        activity.setCanOrientation(true);

    }

    public void showUserInfo(boolean isback, Object object) {
        changeCurrentSubScreenManager(userInfoManager, isback, object);
        activity.canOrientation = false;
    }

    public void showContact(boolean isback, Object object) {

        changeCurrentSubScreenManager(contactsManager, isback, object);
        activity.canOrientation = false;
    }

    public void showInvestmentLevel2(boolean isback, Object object) {
        changeCurrentSubScreenManager(investmentLevel2, isback, object);
        activity.setCanOrientation(false);
    }

    public void showCardsLevel2(boolean isback, Object object) {
        changeCurrentSubScreenManager(cardsLevel2, isback, object);
        activity.setCanOrientation(false);
    }

    public void showAccountLevel2(boolean isback, Object object) {
        changeCurrentSubScreenManager(accountsLevel2, isback, object);
        activity.setCanOrientation(false);
    }

    public void showInvestmentDetails(boolean isback, Object object) {
        changeCurrentSubScreenManager(investmentDetailsManager, isback, object);
        activity.setCanOrientation(false);
    }

    public void showLoanDetails(boolean isback, Object object) {
        changeCurrentSubScreenManager(loansDetailsManager, isback, object);
        activity.setCanOrientation(false);

    }

    public void showCreditCardsDetails(boolean isback, Object object) {
        changeCurrentSubScreenManager(creditCardsDetailsManager, isback, object);
        activity.setCanOrientation(false);
    }

    public void showAccountDetails(boolean isback, Object object) {
        changeCurrentSubScreenManager(accountDetailsManager, isback, object);
        activity.setCanOrientation(false);
    }

    public void showInvestmentAssetDetailsManager(boolean isback, Object object) {
        changeCurrentSubScreenManager(investmentAssetDetailsManager, isback, object);
        activity.setCanOrientation(false);
    }

    public void showGuide(boolean isback, Object object) {
        changeCurrentSubScreenManager(guideManager, isback, object);
        activity.canOrientation = false;
    }

    public void showDetailGuide(boolean isback, Object object) {
        changeCurrentSubScreenManager(guideDetailManager, isback, object);
        activity.setCanOrientation(false);
    }

    public void showRecentPayments(boolean isback, Object object) {
        changeCurrentSubScreenManager(recentPaymentsManager, isback, object);
        activity.canOrientation = false;
    }

    public void showCards(boolean isback, Object object) {
        changeCurrentSubScreenManager(cardsManager, isback, object);
        activity.canOrientation = true;
            activity.setCanOrientation(true);
    }

    public void showInvestments(boolean isback, Object object) {
        changeCurrentSubScreenManager(investmentsManager, isback, object);
        activity.canOrientation = true;
            activity.setCanOrientation(true);
    }

    public void showAccounts(boolean isback, Object object) {

        changeCurrentSubScreenManager(accountsManager, isback, object);
        activity.canOrientation = true;
            activity.setCanOrientation(true);
    }

    public void showAggregatedView(boolean isback, Object object) {
        changeCurrentSubScreenManager(aggregatedManager, isback, object);
        activity.canOrientation = true;
            activity.setCanOrientation(true);
    }
    
    public void onLoadChartData(Configuration newConfig){
    	if (currentSubScreenManager == null) {
            return;
        }
        currentSubScreenManager.onOrientationChanged(newConfig);
        if(newConfig!=null&&newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE) { 
        	currentSubScreenManager.onLoadChartData(null);
        }
    }
    public void setChartData(){
    	currentSubScreenManager.setChartData();
    }

    public boolean onOrientationChange(Configuration newConfig) {
        if (currentSubScreenManager == null) {
            return true;
        }
        currentSubScreenManager.onOrientationChanged(newConfig);
        if(newConfig!=null&&newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE) {   
            Log.i(TAG, "To Portrait");   
            this.mainLayout.setVisibility(View.GONE);
            chartLayout.setVisibility(View.VISIBLE);
            chartLayout.removeAllViews();
            currentSubScreenManager.showChart(chartLayout);
            return false;
        }else{
            Log.i(TAG, "To Landscape");   
            this.mainLayout.setVisibility(View.VISIBLE);
            chartLayout.setVisibility(View.GONE);
        }

//        if (!isVertical) {
//        } else {
//        }
        return true;
    }

    /**
     * @param mainMenuSubScreenManager
     */
    protected void hideAllSubScreenExcept(MainMenuSubScreenManager mainMenuSubScreenManager) {

        for (MainMenuSubScreenManager mainMenuSubScreenManager1 : mainMenuSubScreenManagers) {

            if (mainMenuSubScreenManager1 == mainMenuSubScreenManager) {
                mainMenuSubScreenManager1.show();
            } else {
                mainMenuSubScreenManager1.hide();
            }
        }

    }

    private void changeCurrentSubScreenManager(MainMenuSubScreenManager mainMenuSubScreenManager,
            boolean isback, Object object) {

        if (mainMenuSubScreenManager == null) {
            hideAllSubScreenExcept(mainMenuSubScreenManager);
            return;
        }
        if(mainMenuSubScreenManager.equals(currentSubScreenManager)){
        	return ;
        }
        saveCurrentSubScreenManager();
        currentSubScreenManager = mainMenuSubScreenManager;
        // 先让左导航消失
        activity.setLeftNavigationImg(-1);
        hideAllSubScreenExcept(mainMenuSubScreenManager);
        if (!isback) {
            mainMenuSubScreenManager.onShow(object);
        }

    }

    public void dispatchLeftNavigationClick(View v) {

        if (!currentSubScreenManager.onLeftNavigationButtonClick(v)) {
            this.onLeftNavigationButtonClick(v);
        }
    }

    public boolean onLeftNavigationButtonClick(View v) {

        return false;
    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        paymentsManager.onActivityResult(requestCode, resultCode, data);
        return false;
    }
    
    public boolean onDestroyed(){
    	return paymentsManager.onDestroyed();
    }
    
    public void saveCurrentSubScreenManager(){
    	lastSubScreenManager=currentSubScreenManager;
    }
    

    public void showView(MainMenuSubScreenManager subScreenManager,boolean isback, Object object) {
        changeCurrentSubScreenManager(subScreenManager, isback, object);
        activity.canOrientation = true;
            activity.setCanOrientation(true);
    }
    
    public MainMenuSubScreenManager getLastView(){
    	return lastSubScreenManager;
    }
}
