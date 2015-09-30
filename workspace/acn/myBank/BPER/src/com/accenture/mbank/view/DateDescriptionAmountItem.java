
package com.accenture.mbank.view;

import it.gruppobper.ams.android.bper.R;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
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

import com.accenture.mbank.AccountDetailActivity.RecordAdapter;
import com.accenture.mbank.AccountDetailActivity.RecordAdapter.TYPE;
import com.accenture.mbank.model.MovementsModel;
import com.accenture.mbank.util.TimeUtil;
import com.accenture.mbank.util.Utils;

public class DateDescriptionAmountItem extends LinearLayout {

    public ToggleButton toggleButton;

    LinearLayout expandedContainer,currencyDateContainer;

    ViewGroup expanderBar;
    
    TextView currency_date,currencyDateValue;

    TextView accountOperationDate ,account_description,
    account_amount, accountOperationDateValue, descriptionValue, amountValue;

    TextView titleDate, titleReason, titleAmount;

    private MovementsModel model;

    public TYPE type = RecordAdapter.TYPE.ACCOUNTS;

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
        super.onAttachedToWindow();
        init();
    }
	public void init(final QuickReturnListView childListView, final int listSize,final Context context) {
		if (toggleButton == null) {
            toggleButton = (ToggleButton)findViewById(R.id.expand_btn);
            toggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                    	closeOther();
						expandedContainer.setVisibility(View.VISIBLE);
		                DateDescriptionAmountItem.this.expandedContainer.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		                int heightconvertView = DateDescriptionAmountItem.this.expandedContainer.getMeasuredHeight();
		                childListView.getLayoutParams().height = childListView.getLayoutParams().height + heightconvertView+20;
                    } else {
                        expandedContainer.setVisibility(View.GONE);
                        DateDescriptionAmountItem.this.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
                		int heightconvertView = DateDescriptionAmountItem.this.getMeasuredHeight();
                		childListView.getLayoutParams().height = heightconvertView*listSize+20;
                    }
                }
            });

            expandedContainer = (LinearLayout)findViewById(R.id.expanded_container);
            expandedContainer.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					toggleButton.performClick();
				}
			});

            expanderBar = (ViewGroup)findViewById(R.id.expander_bar);
            expanderBar.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    toggleButton.performClick();
                }
            });
            currencyDateContainer = (LinearLayout)findViewById(R.id.currency_date_container);
            accountOperationDate = (TextView)findViewById(R.id.account_operation_date);
            currency_date = (TextView)findViewById(R.id.currency_date);
            account_description= (TextView)findViewById(R.id.account_description);
            account_amount= (TextView)findViewById(R.id.account_amount);
            
            accountOperationDateValue = (TextView)findViewById(R.id.account_operation_date_value);
            descriptionValue = (TextView)findViewById(R.id.account_description_value);
            amountValue = (TextView)findViewById(R.id.account_amount_value);
            currencyDateValue = (TextView)findViewById(R.id.currency_date_value);
            titleDate = (TextView)findViewById(R.id.time);
            titleReason = (TextView)findViewById(R.id.expand_type);
            titleAmount = (TextView)findViewById(R.id.expand_result);
        }
	}

    public void init() {
        if (toggleButton == null) {
            toggleButton = (ToggleButton)findViewById(R.id.expand_btn);
            toggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                    	closeOther();
                        expandedContainer.setVisibility(View.VISIBLE);
                    } else {
                        expandedContainer.setVisibility(View.GONE);
                    }
                }
            });

            expandedContainer = (LinearLayout)findViewById(R.id.expanded_container);
            expandedContainer.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					toggleButton.performClick();
				}
			});

            expanderBar = (ViewGroup)findViewById(R.id.expander_bar);
            expanderBar.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    toggleButton.performClick();
                }
            });
            currencyDateContainer = (LinearLayout)findViewById(R.id.currency_date_container);
            accountOperationDate = (TextView)findViewById(R.id.account_operation_date);
            currency_date = (TextView)findViewById(R.id.currency_date);
            account_description= (TextView)findViewById(R.id.account_description);
            account_amount= (TextView)findViewById(R.id.account_amount);
            
            accountOperationDateValue = (TextView)findViewById(R.id.account_operation_date_value);
            descriptionValue = (TextView)findViewById(R.id.account_description_value);
            amountValue = (TextView)findViewById(R.id.account_amount_value);
            currencyDateValue = (TextView)findViewById(R.id.currency_date_value);
            titleDate = (TextView)findViewById(R.id.time);
            titleReason = (TextView)findViewById(R.id.expand_type);
            titleAmount = (TextView)findViewById(R.id.expand_result);
        }
    }

    private void closeOther() {
        ViewGroup parent = (ViewGroup)getParent();
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            
            if (child instanceof DateDescriptionAmountItem) {
            	DateDescriptionAmountItem itemExpander = (DateDescriptionAmountItem)child;
                if (itemExpander != this && itemExpander.toggleButton != null && itemExpander.toggleButton.isChecked()) {
                    itemExpander.toggleButton.performClick();
                }
            }
        }
    }
    
    
    void setTexts(String operationDate, String description, String amount, String currencyDate) {
        if (accountOperationDateValue == null) {
            init();
        }
        accountOperationDateValue.setText(operationDate);
        descriptionValue.setText(description);
        amountValue.setText(amount);
        currencyDateValue.setText(currencyDate);
        titleDate.setText(operationDate);
        titleAmount.setText(amount);
        titleReason.setText(description);
    }

    void setUiBydata() {
        if (accountOperationDateValue == null) {
            init();
        }
        
        if(type == RecordAdapter.TYPE.CARDS){
        	accountOperationDate.setText(R.string.opt_date);
        	account_description.setText(R.string.description_2);
        	account_amount.setText(R.string.amount_3);
        	currency_date.setVisibility(View.GONE);
        	currencyDateValue.setVisibility(View.GONE);
        }
        
        String operationDate = TimeUtil.changeFormattrString(model.getOperationDate(), TimeUtil.dateFormat2,TimeUtil.dateFormat5);
        String description = model.getDescription();

		String valueDate = "";
		if (!model.getValueDate().equals("")) {
			valueDate = TimeUtil.changeFormattrString(model.getValueDate(),TimeUtil.dateFormat2, TimeUtil.dateFormat5);
		}

        double amount = model.getAmount();

        if (amount > 0) {
            titleAmount.setTextColor(Color.rgb(0, 0, 0));
        } else {
            titleAmount.setTextColor(Color.rgb(255, 0, 0));
        }
        String amountStr = Utils.generateFormatMoney(getContext().getResources().getString(R.string.eur), amount);

        toggleButton.setChecked(model.isExpanded);
        setTexts(operationDate, description, amountStr, valueDate);

    }

	public TYPE getType() {
		return type;
	}

	public void setType(TYPE type) {
		this.type = type;
	}
	
	public void hideCurrencyDate(){
		if (currencyDateContainer != null) {
			currencyDateContainer.setVisibility(View.GONE);
		}
	}
}
