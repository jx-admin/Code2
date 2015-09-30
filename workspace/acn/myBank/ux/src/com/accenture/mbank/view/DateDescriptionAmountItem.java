
package com.accenture.mbank.view;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.accenture.mbank.R;
import com.accenture.mbank.model.BalanceAccountsModel;
import com.accenture.mbank.model.MovementsModel;
import com.accenture.mbank.util.TimeUtil;
import com.accenture.mbank.util.Utils;

public class DateDescriptionAmountItem extends LinearLayout {

    ToggleButton toggleButton;

    LinearLayout expandedContainer;

    ViewGroup expanderBar;

    TextView accountDate, accountDateValue, descriptionValue, amountValue, currencyDateValue,
            isexpand;

    TextView titleDate, titleReason, titleAmount;

    private MovementsModel model;

    public List<BalanceAccountsModel> accounts;

    public MovementsModel getModel() {
        return model;
    }

    public void setModel(MovementsModel model) {
        this.model = model;
        setUiBydata();
    }

    public DateDescriptionAmountItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        // TODO Auto-generated method stub
        super.onAttachedToWindow();
        init();
    }

    public void init() {
        if (toggleButton == null) {
            toggleButton = (ToggleButton)findViewById(R.id.expand_btn);

            toggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    model.isExpanded = isChecked;
                    if (isChecked) {
                        expandedContainer.setVisibility(View.VISIBLE);

                    } else {
                        expandedContainer.setVisibility(View.GONE);
                    }
                    if (model.isExpanded) {
                        isexpand.setText("[-]");
                    } else {
                        isexpand.setText("[+]");

                    }
                }
            });

            expandedContainer = (LinearLayout)findViewById(R.id.expanded_container);

            expanderBar = (ViewGroup)findViewById(R.id.expander_bar);
            expanderBar.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    toggleButton.performClick();
                }
            });
            accountDate = (TextView)findViewById(R.id.account_date);
            accountDateValue = (TextView)findViewById(R.id.account_date_value);
            descriptionValue = (TextView)findViewById(R.id.account_description_value);
            amountValue = (TextView)findViewById(R.id.account_amount_value);
            currencyDateValue = (TextView)findViewById(R.id.currency_date_value);
            titleDate = (TextView)findViewById(R.id.time);
            titleReason = (TextView)findViewById(R.id.expand_type);
            titleAmount = (TextView)findViewById(R.id.expand_result);
            isexpand = (TextView)findViewById(R.id.is_expand);

        }
    }

    void setTexts(String date, String description, String amount, String currencyDate) {
        if (accountDateValue == null) {
            init();
        }
        accountDateValue.setText(date);
        descriptionValue.setText(description);
        amountValue.setText(amount);
        currencyDateValue.setText(currencyDate);
        titleDate.setText(date);
        titleAmount.setText(amount);
        titleReason.setText(description);

    }

    /**
     * @deprecated
     */
    private void closeOther() {
        ViewGroup parent = (ViewGroup)getParent();
        if (parent == null) {
            return;
        }
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            if (child instanceof DateDescriptionAmountItem) {
                DateDescriptionAmountItem itemExpander = (DateDescriptionAmountItem)child;
                if (itemExpander != this && itemExpander.toggleButton.isChecked()) {
                    itemExpander.toggleButton.performClick();
                }
            }
        }
    }

    void setUiBydata() {
        if (accountDateValue == null) {
            init();
        }
        String date = model.getOperationDate();

        String newDate = TimeUtil.changeFormattrString(date, TimeUtil.dateFormat2,
                TimeUtil.dateFormat5);
        String description = model.getDescription();

        String valueDate = TimeUtil.changeFormattrString(model.getValueDate(),
                TimeUtil.dateFormat2, TimeUtil.dateFormat5);

        double amount = model.getAmount();
        String temp = "";
        if (amount > 0) {
            temp = "+";
            titleAmount.setTextColor(Color.rgb(0, 0, 0));
        } else {
            titleAmount.setTextColor(Color.rgb(255, 0, 0));
        }
        String amountStr = Utils.generateFormatMoney(
                getContext().getResources().getString(R.string.dollar), amount);/*
                                                                                 * temp
                                                                                 * +
                                                                                 * " $ "
                                                                                 * +
                                                                                 * Utils
                                                                                 * .
                                                                                 * generateMoney
                                                                                 * (
                                                                                 * amount
                                                                                 * )
                                                                                 * ;
                                                                                 */

        toggleButton.setChecked(model.isExpanded);
        if (model.isExpanded) {
            isexpand.setText("[-]");
        } else {
            isexpand.setText("[+]");

        }
        setTexts(valueDate, description, amountStr, newDate);

    }
}
