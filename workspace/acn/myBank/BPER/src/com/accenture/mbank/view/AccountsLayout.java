
package com.accenture.mbank.view;

import it.gruppobper.ams.android.bper.R;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.accenture.mbank.BaseActivity;
import com.accenture.mbank.MainActivity;
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

    public List<BalanceAccountsModel> accounts = new ArrayList<BalanceAccountsModel>();

    Handler mHandler;

    private static boolean needUpdate = true;

    public AccountsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHandler = new Handler();
    }

    public static void setNeedUpdate(boolean needUpdate) {
    	AccountsLayout.needUpdate = needUpdate;
    }

    @Override
    protected void init() {
    	super.init();
   
        parentScrollView = (InnerScrollView)findViewById(R.id.accounts_scroll);
        content = (LinearLayout)findViewById(R.id.accounts_content);
        
		parentScrollView.setOnInnerScrollListener(this);
		arrowController.setScrollView(parentScrollView);
    }

    public void addAccount(BalanceAccountsModel account) {
        if (content == null || parentScrollView == null) {
            init();
        }
        LayoutInflater inflater = LayoutInflater.from(getContext());
        AccountItemLayout viewGroup = (AccountItemLayout)inflater.inflate(R.layout.account_item_layout, null);
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
                String postData = BalanceJson.BalanceReportProtocal(ACCOUNT_PAYMENTMETHOD,Contants.publicModel);
                HttpConnector httpConnector = new HttpConnector();
                String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData,getContext());
                GetBalanceResponseModel getBalanceResponse = BalanceJson.parseGetBalanceResponse(httpResult);
                
                if (getBalanceResponse != null &&
                		getBalanceResponse.getBanlaceAccounts() != null &&
                				getBalanceResponse.responsePublicModel.isSuccess() ) {
                	accounts = getBalanceResponse.getBanlaceAccounts();
            		needUpdate = false;
                } else
                {
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
                	else if (getBalanceResponse != null &&
							!getBalanceResponse.responsePublicModel.isSuccess() &&
							!getBalanceResponse.responsePublicModel.eventManagement.getErrorCode().equals(Contants.ERR_SESSION_EXPIRED_1) &&
							!getBalanceResponse.responsePublicModel.eventManagement.getErrorCode().equals(Contants.ERR_SESSION_EXPIRED_2)) {
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
                
                if (accounts != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            setAccounts(accounts);
                            updateArrowState();
                        }
                    });
                }
            }
        });
    }

    public void onShow() {
        if (BaseActivity.isOffline) {
        	List<BalanceAccountsModel> accounts = new ArrayList<BalanceAccountsModel>();
        	BalanceAccountsModel account1 = new BalanceAccountsModel();
        	account1.setAccountBalance(28.960);
        	account1.setAccountCode("1064205");
        	account1.setAccountName("wlaquiBONOVOX - 1234567");
        	account1.setAccountType("Ordinari");
        	account1.setAccountNumber(1);
        	account1.setAvailableBalance(25.960);
        	account1.setBankServiceCode("020");
        	account1.setPersonalizedName("wlaquiBONOVOX - 1234567");
        	accounts.add(account1);
        	
        	BalanceAccountsModel account2 = new BalanceAccountsModel();
        	account2.setAccountBalance(174687.490);
        	account2.setAccountCode("1617752");
        	account2.setAccountName("aaaaa");
        	account2.setAccountType("Ordinari");
        	account2.setAccountNumber(1);
        	account2.setAvailableBalance(25.960);
        	account2.setBankServiceCode("020");
        	account2.setPersonalizedName("aaaaa");
        	account2.setDipiuActive(true);
        	account2.setDipiuAvailable(true);
        	account2.setDipiuBalance(123);
        	accounts.add(account2);
        	setAccounts(accounts);
        	return;
        }
//        if (needUpdate) {
            accounts.clear();
            loadAccounts();
//        }
    }
}
