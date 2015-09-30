
package com.accenture.mbank.view;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.accenture.mbank.BaseActivity;
import com.accenture.mbank.R;
import com.accenture.mbank.model.AccountsForServiceModel;
import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.net.ProgressOverlay;
import com.accenture.mbank.net.ProgressOverlay.OnProgressEvent;
import com.accenture.mbank.util.LogManager;

public class AccountExpandedLayout extends ExpandedContainer implements OnCheckedChangeListener {

    RadioGroup accountRG;

    Handler handler;

    AccountsModel object;

    public AccountExpandedLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        handler = new Handler();
    }

    @Override
    protected void onAttachedToWindow() {
        // TODO Auto-generated method stub
        super.onAttachedToWindow();
        if (accountRG == null) {
            accountRG = (RadioGroup)findViewById(R.id.account_rg);

            for (int index = 0; index < accountRG.getChildCount(); index++) {
                RadioButton child = (RadioButton)accountRG.getChildAt(index);
                child.setOnCheckedChangeListener(this);
            }
        }
    }

    List<AccountsForServiceModel> accountsForServiceModels;

    public void createAccountsUI(final List<AccountsForServiceModel> accountsForServiceModels) {
        this.accountsForServiceModels = accountsForServiceModels;

        for (int index = 0; index < accountRG.getChildCount(); index++) {
            RadioButton child = (RadioButton)accountRG.getChildAt(index);
            child.setOnCheckedChangeListener(null);

        }
        accountRG.removeAllViews();
        for (int i = 0; i < accountsForServiceModels.size(); i++) {
            AccountsForServiceModel accountsForServiceModel = accountsForServiceModels.get(i);
            LogManager.d(accountsForServiceModel.getServiceCode());

            for (AccountsModel accountsModel : accountsForServiceModel.getAccounts()) {

                // LogManager.d(accountsModel.getAccountAlias());
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                RadioButton radioButton = (RadioButton)layoutInflater.inflate(R.layout.bank_radio_button_item, null);
                radioButton.setText(accountsModel.getAccountAlias());
                LogManager.d("recover " + accountsModel);
                radioButton.setTag(accountsModel);

                radioButton.setOnCheckedChangeListener(AccountExpandedLayout.this);

                accountRG.addView(radioButton);
                if (object != null  && object.getAccountCode().equals(accountsModel.getAccountCode())) {
                    radioButton.setChecked(true);
                }
            }
        }
    }
    
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (isChecked) {

            expandFocusResultChange(buttonView.getText().toString());
            object = (AccountsModel)buttonView.getTag();

            PaymentConfirmLayout paymentConfirmLayout = (PaymentConfirmLayout)expandBarResultListener
                    .getOwener();
            PayeeExpandedLayout payeeExpander = (PayeeExpandedLayout)paymentConfirmLayout.payeeExpander.expandedContainer;
            payeeExpander.createAccountsUI(accountsForServiceModels, object);
        }

    }

    @Override
    protected void resetResult() {
        super.resetResult();
        accountRG.clearCheck();
        object=null;
        
    }

    @Override
    protected void onRecover(String text) {
        if (text == null || text.equals("")) {
            return;
        }
        if (accountsForServiceModels == null || accountsForServiceModels.size() <= 0) {
            return;
        }
        
        for (int i = 0; i < accountRG.getChildCount(); i++) {
            RadioButton sub = (RadioButton)accountRG.getChildAt(i);
            AccountsModel accountsModel = (AccountsModel)sub.getTag();

            if (accountsModel.getAccountCode().equals(text)) {
                sub.setChecked(true);
                object = accountsModel;
            } else {
                sub.setChecked(false);
            }
        }
    }

    @Override
    public void setEditable(boolean flag) {
        super.setEditable(flag);
        for (int i = 0; i < accountRG.getChildCount(); i++) {
            RadioButton sub = (RadioButton)accountRG.getChildAt(i);
            sub.setEnabled(flag);
        }
    }

    @Override
    protected Object getValue() {

        return object;
    }

}
