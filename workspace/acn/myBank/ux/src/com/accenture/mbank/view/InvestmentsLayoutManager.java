
package com.accenture.mbank.view;

import java.util.List;

import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.accenture.mbank.R;
import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.view.protocol.ShowAble;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

public class InvestmentsLayoutManager extends BankRollContainerManager {
	
    private GoogleAnalytics mGaInstance;
    
	private Tracker mGaTracker1;

    @Override
    public void createUiByData() {
    }

    protected void setAccountsUI(BankRollView root, List<AccountsModel> list) {
        LinearLayout linearLayout1 = new LinearLayout(getContext());
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        linearLayout1.setLayoutParams(layoutParams1);
        linearLayout1.setOrientation(LinearLayout.VERTICAL);
        root.setContent(linearLayout1);
        linearLayout1.setBackgroundResource(R.drawable.box_details);

        LinearLayout linearLayout2 = new LinearLayout(getContext());
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(
                LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        linearLayout2.setLayoutParams(layoutParams2);
        linearLayout2.setOrientation(LinearLayout.VERTICAL);
        root.setContent(linearLayout2);
        linearLayout2.setBackgroundResource(R.drawable.box_details);

        if (list != null && list.size() > 0) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            for (AccountsModel amAccountsModel : list) {
                ItemExpander itemExpander = (ItemExpander)inflater.inflate(R.layout.item_expander,
                        null);
                String title = amAccountsModel.getAccountAlias();
                itemExpander.setTitle(title);
                itemExpander.setTypeface(Typeface.DEFAULT);
                itemExpander.setResultVisible(false);
                linearLayout2.addView(itemExpander);
                setExpandedContainer(itemExpander, amAccountsModel);

            }
        } else {

            TextView textView = new TextView(getContext());
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundResource(R.drawable.box_details);
            textView.setText(root.getResources().getString(R.string.not_available));
            linearLayout2.addView(textView);
        }
    }

    public void setExpandedContainer(ItemExpander itemExpander, AccountsModel accountsModel) {

        String type = accountsModel.getMortageType();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        if ("GP".equals(type)) {
            AssetManagementExpandContainer assetManagementExpandContainer = (AssetManagementExpandContainer)inflater
                    .inflate(R.layout.asset_management_expander, null);
            itemExpander.setExpandButtonBackground(R.drawable.investment_expand_selector);

            itemExpander.setExpandedContainer(assetManagementExpandContainer);
            assetManagementExpandContainer.setAccountCode(accountsModel.getAccountCode());

        } else if ("DT".equals(type)) {

            DepositsExpandedContainer depositsExpandedContainer = (DepositsExpandedContainer)inflater
                    .inflate(R.layout.deposits_expanded_layout, null);
            itemExpander.setExpandButtonBackground(R.drawable.investment_expand_selector);

            depositsExpandedContainer.setAccountCode(accountsModel.getAccountCode());
            itemExpander.setExpandedContainer(depositsExpandedContainer);
        }
    }

    @Override
    protected void showFromDashBoard(boolean isDelay) {
        BankRollContainer bankRollContainer = getRollContainer();
        for (int j = 0; j < bankRollContainer.getBankRollViewCount(); j++) {
            BankRollView showAble = (BankRollView)bankRollContainer.getShowAble(j);
            LinearLayout container = (LinearLayout)showAble.getContent();
            BankRollView needShowBankRollView = null;
            for (int i = 0; i < container.getChildCount(); i++) {
            	View view = container.getChildAt(i);
            	if(view instanceof ItemExpander){
            		ItemExpander investmentDetail = (ItemExpander)view;
                    if (investmentDetail.getTitle().equals(animateToDashBoardModel.getAccountCode())) {

                        needShowBankRollView = showAble;

                        needShowBankRollView.show();
                        investmentDetail.performExpand();

                    }
            	}
                
            }
        }
    }

    @Override
    public void onShow() {
        BankRollView bankRotatView = (BankRollView)getRollContainer().getRollContainer()
                .getChildAt(0);
        BankRollView bankRotatView1 = (BankRollView)getRollContainer().getRollContainer()
                .getChildAt(1);
        
        if (bankRotatView.getContent() == null && Contants.depositInvestmentAccounts.size() > 0) {
            setAccountsUI(bankRotatView, Contants.depositInvestmentAccounts);
            setAccountsUI(bankRotatView1, Contants.assertInvestmentAccounts);
            performShowFromDashBoard(true);
        } else {
            performShowFromDashBoard(false);
        }
        // if (bankRotatView1.getContent() == null &&
        // Contants.assertInvestmentAccounts.size() > 0) {
        //
        // }
    }

    @Override
    public void onShow(ShowAble showAble) {
    	
    }

}
