
package com.accenture.mbank.view;

import it.gruppobper.ams.android.bper.R;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.accenture.mbank.BaseActivity;
import com.accenture.mbank.MainActivity;
import com.accenture.mbank.logic.GetFinancingInfoJson;
import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.model.GetFinancingInfoModel;
import com.accenture.mbank.net.HttpConnector;
import com.accenture.mbank.net.ProgressOverlay;
import com.accenture.mbank.net.ProgressOverlay.OnProgressEvent;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.view.protocol.ShowAble;
import com.accenture.mbank.view.protocol.ShowListener;

public class LoansLayout extends AnimateAbleLayout implements ShowListener {

    LinearLayout loansContent;

    InnerScrollView innerScrollView;

    private static boolean needUpdate = true;

    public LoansLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init() {
    	super.init();

        loansContent = (LinearLayout)findViewById(R.id.loans_content);
        innerScrollView = (InnerScrollView)findViewById(R.id.parent_innser_scroll_view);
        
        innerScrollView.setOnInnerScrollListener(this);
		arrowController.setScrollView(innerScrollView);
    }

    public static void setNeedUpdate(boolean needUpdate) {
    	LoansLayout.needUpdate = needUpdate;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        init();
    }

    public void createUibyData() {
        if (Contants.loansAccounts != null && Contants.loansAccounts.size() > 0) {
            loansContent.removeAllViews();
            List<LoanItemLayout> list = new ArrayList<LoanItemLayout>();
            for (AccountsModel model : Contants.loansAccounts) {
                LoanItemLayout layout = addLoan(model);
                list.add(layout);
            }
            downloadDataAndUpdateUI(list);
        }
    }

    @Override
    protected ViewGroup getContainer() {
        return loansContent;
    }

    public void downloadDataAndUpdateUI(final List<LoanItemLayout> lists) {
        ProgressOverlay progressOverlay = new ProgressOverlay(getContext());
        progressOverlay.show("", new OnProgressEvent() {

            @Override
            public void onProgress() {

                for (int i = 0; i < lists.size(); i++) {
                    LoanItemLayout loanItemLayout = lists.get(i);

                    AccountsModel accountsModel = loanItemLayout.getAccount();
                    String postData = GetFinancingInfoJson.getFinancingInfoReportProtocal(Contants.publicModel, accountsModel.getAccountCode(),accountsModel.getFinanceType());

                    HttpConnector httpConnector = new HttpConnector();
                    String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url,postData, getContext());
                    final GetFinancingInfoModel getFinancingInfo = GetFinancingInfoJson.paresgetFinancingInfoResponse(httpResult);

                    if (getFinancingInfo != null &&
                    		getFinancingInfo.responsePublicModel.isSuccess()) {

                        loanItemLayout.updateUIByData(getFinancingInfo);
                		needUpdate = false;
                    } else {
                       	if (httpResult == null) {
    						/*
    						 *  A connection error happened
    						 */
                    		needUpdate = true;
    						handler.post(new Runnable() {
    							@Override
    							public void run() {
    								final MainActivity mainActivity = (MainActivity)getContext();
    								mainActivity.showTab(0);
    							}
    						});
                    	}
                    	else if (getFinancingInfo != null &&
    							!getFinancingInfo.responsePublicModel.isSuccess() &&
    							!getFinancingInfo.responsePublicModel.eventManagement.getErrorCode().equals(Contants.ERR_SESSION_EXPIRED_1) &&
    							!getFinancingInfo.responsePublicModel.eventManagement.getErrorCode().equals(Contants.ERR_SESSION_EXPIRED_2)) {
    						/*
    						 * maybe 90000 happens
    						 */
    						needUpdate = true;
    						handler.post(new Runnable() {
    							@Override
    							public void run() {
    								onError(getContext());
    							}
    						});
    					}
                    }
                }
                
            }
        });
    }

    public LoanItemLayout addLoan(AccountsModel model) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        LoanItemLayout child = (LoanItemLayout)inflater.inflate(R.layout.loan_item_layout, null);

        child.innerScrollView = innerScrollView;
        loansContent.addView(child);
        child.setShowListener(this);
        child.setAccountsModel(model);

        return child;

    }

    public void closeAllViewExcept(View v) {
        for (int i = 0; i < loansContent.getChildCount(); i++) {
            View child = loansContent.getChildAt(i);
            if (child instanceof ShowAble) {
                if (child != v) {
                    ShowAble showAble = (ShowAble)child;
                    showAble.close();
                }
            }
        }
    }

    @Override
    public void onShow(ShowAble showAble) {
        if (showAble instanceof View) {
            View v = (View)showAble;
            closeAllViewExcept(v);
        }
    }

    public void onShow() {
    	if (BaseActivity.isOffline) {
            List<LoanItemLayout> list = new ArrayList<LoanItemLayout>();

            AccountsModel model = new AccountsModel();
            model.setAccountCode("3482218");
            model.setAccountAlias("Mutuo 00717 - 123456789");
            model.setMortageType("I");

            LoanItemLayout layout = addLoan(model);
            list.add(layout);
            
            GetFinancingInfoModel getFinancingInfo = new GetFinancingInfoModel();
            getFinancingInfo.setResidueAmount("123");
            getFinancingInfo.setTotalAmountl("256");
            layout.updateUIByData(getFinancingInfo);
    	}

        if (needUpdate) {
            createUibyData();
        }
    }

}
