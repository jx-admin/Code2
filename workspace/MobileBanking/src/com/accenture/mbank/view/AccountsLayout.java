
package com.accenture.mbank.view;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.accenture.mbank.R;
import com.accenture.mbank.logic.BalanceJson;
import com.accenture.mbank.model.BalanceAccountsModel;
import com.accenture.mbank.model.GetBalanceResponseModel;
import com.accenture.mbank.net.HttpConnector;
import com.accenture.mbank.net.ProgressOverlay;
import com.accenture.mbank.net.ProgressOverlay.OnProgressEvent;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.view.protocol.ShowAble;
import com.accenture.mbank.view.protocol.ShowListener;

public class AccountsLayout extends AnimateAbleLayout implements ShowListener {

    InnerScrollView parentScrollView;

    LinearLayout content;

    public List<BalanceAccountsModel> accounts;

    Handler mHandler;

    public AccountsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mHandler = new Handler();

    }

    @Override
    protected void onAttachedToWindow() {
        // TODO Auto-generated method stub
        super.onAttachedToWindow();
    }

    void init() {

        parentScrollView = (InnerScrollView)findViewById(R.id.accounts_scroll);
        content = (LinearLayout)findViewById(R.id.accounts_content);
    }

    public void addAccount() {

        if (content == null || parentScrollView == null) {
            init();
        }
        LayoutInflater inflater = LayoutInflater.from(getContext());
        AccountItemLayout viewGroup = (AccountItemLayout)inflater.inflate(
                R.layout.account_item_layout, null);
        viewGroup.innerScrollView = parentScrollView;

        content.addView(viewGroup);
        viewGroup.setShowListener(this);

    }

    public void addAccount(BalanceAccountsModel account) {

        if (content == null || parentScrollView == null) {
            init();
        }
        LayoutInflater inflater = LayoutInflater.from(getContext());
        AccountItemLayout viewGroup = (AccountItemLayout)inflater.inflate(
                R.layout.account_item_layout, null);
        viewGroup.innerScrollView = parentScrollView;
        viewGroup.setAccount(account);

        content.addView(viewGroup);
        viewGroup.setShowListener(this);

    }

    public void setAccounts(List<BalanceAccountsModel> accounts) {
        if (content == null || parentScrollView == null) {
            init();
        }
        content.removeAllViews();
        this.accounts = accounts;
        for (BalanceAccountsModel balanceAccountsModel : accounts) {

            addAccount(balanceAccountsModel);
        }

    }

    @Override
    protected ViewGroup getContainer() {

        return content;
    }

    public void closeAllViewExcept(View v) {

        for (int i = 0; i < content.getChildCount(); i++) {

            View child = content.getChildAt(i);
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

    public static final String ACCOUNT_PAYMENTMETHOD = "2";

    private void loadAccounts() {

        ProgressOverlay progressOverlay = new ProgressOverlay(getContext());
        progressOverlay.show("", new OnProgressEvent() {

            @Override
            public void onProgress() {
                String postData = BalanceJson.BalanceReportProtocal(ACCOUNT_PAYMENTMETHOD,
                        Contants.publicModel);
                HttpConnector httpConnector = new HttpConnector();
                String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData,
                        getContext());
                GetBalanceResponseModel getBalanceResponse = BalanceJson
                        .parseGetBalanceResponse(httpResult);
                final List<BalanceAccountsModel> accounts = getBalanceResponse.getBanlaceAccounts();

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

    public static boolean needUpdate = false;

    public void onShow() {

        if (needUpdate && accounts != null) {

            needUpdate = false;
            accounts.clear();

        }
        if (accounts == null || accounts.size() == 0) {
            loadAccounts();
        } else {
            performShowFromDashBoard(false);
        }

    }
}
