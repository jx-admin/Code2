
package com.accenture.mbank.view.payment;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.accenture.mbank.NewPayments;
import com.accenture.mbank.R;
import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.model.RecentTransferModel;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

public class NewRecentPaymentItem extends LinearLayout {

    ToggleButton toggleButton;

    LinearLayout recentExpandedContainer;

    public TextView timeText, typeText, resultText, one_text, four_text, two_text, three_text,
            five_text;

    ImageButton recentRecoverBtn;

    ViewGroup recentExpanderBar;
    
    private AccountsModel accountModel;
    
    
    private GoogleAnalytics mGaInstance;
    
  	private Tracker mGaTracker1;
    

    public AccountsModel getAccountModel() {
        return accountModel;
    }

    public void setAccountModel(AccountsModel accountModel) {
        this.accountModel = accountModel;
    }

    private RecentTransferModel recentTransferModel;
    Context context;
    public NewRecentPaymentItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    public void init() {
        if (toggleButton == null) {
            toggleButton = (ToggleButton)findViewById(R.id.recent_expand_btn);
            toggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        mGaInstance = GoogleAnalytics.getInstance(getContext());
                        mGaTracker1 = mGaInstance.getTracker("UA-42551791-1");
                        mGaTracker1.sendView("event.recent.payment.recover"); 
                        recentTransferModel.isExpanded = isChecked;
                        recentExpandedContainer.setVisibility(View.VISIBLE);
                    } else {
                        recentExpandedContainer.setVisibility(View.GONE);
                    }
                }
            });

            recentExpandedContainer = (LinearLayout)findViewById(R.id.recent_expanded_container);
            timeText = (TextView)findViewById(R.id.recent_time);
            resultText = (TextView)findViewById(R.id.recent_expand_result);
            typeText = (TextView)findViewById(R.id.recent_expand_type);
            one_text = (TextView)findViewById(R.id.one_text);
            two_text = (TextView)findViewById(R.id.two_text);
            three_text = (TextView)findViewById(R.id.three_text);
            four_text = (TextView)findViewById(R.id.four_text);
            five_text = (TextView)findViewById(R.id.five_text);
            recentRecoverBtn = (ImageButton)findViewById(R.id.recent_recover_btn);
            recentRecoverBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO 
                    NewPayments.startForRecover((Activity)context, getRecentTransferModel(),getAccountModel());
                }
            });
            recentExpanderBar = (ViewGroup)findViewById(R.id.recent_expander_bar);
            recentExpanderBar.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                	
                	
                    toggleButton.performClick();
                }
            });
        }
    }

    private void setUpdateChecked() {
    	
    	
        toggleButton.setChecked(recentTransferModel.isExpanded);
    }

    /**
     * @return recentTransferModel
     */
    public RecentTransferModel getRecentTransferModel() {
        return recentTransferModel;
    }

    /**
     * @param recentTransferModel 要设置的 recentTransferModel
     */
    public void setRecentTransferModel(RecentTransferModel recentTransferModel) {
        this.recentTransferModel = recentTransferModel;
        setUpdateChecked();
    }
}
