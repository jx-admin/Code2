
package com.accenture.mbank.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.accenture.mbank.BaseActivity;
import com.accenture.mbank.R;
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

    public LoansLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init() {
        loansContent = (LinearLayout)findViewById(R.id.loans_content);
        innerScrollView = (InnerScrollView)findViewById(R.id.parent_innser_scroll_view);

        if (BaseActivity.isOffline) {
            addLoan();
            addLoan();
            addLoan();
        }
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
        // TODO Auto-generated method stub
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
                    String postData = GetFinancingInfoJson.getFinancingInfoReportProtocal(
                            Contants.publicModel, accountsModel.getAccountCode(),
                            accountsModel.getFinanceType());

                    HttpConnector httpConnector = new HttpConnector();
                    String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url,
                            postData, getContext());
                    final GetFinancingInfoModel getFinancingInfo = GetFinancingInfoJson
                            .paresgetFinancingInfoResponse(httpResult);

                    if (getFinancingInfo.responsePublicModel.isSuccess()) {

                        loanItemLayout.updateUIByData(getFinancingInfo);

                    }
                }

                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub

                        performShowFromDashBoard(true);
                    }
                });

            }
        });
    }

    public void addLoan() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        LoanItemLayout child = (LoanItemLayout)inflater.inflate(R.layout.loan_item_layout, null);

        child.innerScrollView = innerScrollView;
        loansContent.addView(child);
        child.setShowListener(this);
        if (BaseActivity.isOffline) {
            child.setStates();
        }

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

        if (loansContent.getChildCount() <= 0) {
            createUibyData();
        }
        if (animateToDashBoardAccountCode != null) {
            performShowFromDashBoard(false);
        }
    }

}
