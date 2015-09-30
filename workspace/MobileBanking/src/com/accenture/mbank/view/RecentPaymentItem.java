
package com.accenture.mbank.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.accenture.mbank.MainActivity;
import com.accenture.mbank.R;
import com.accenture.mbank.model.RecentTransferModel;
import com.accenture.mbank.util.TimeUtil;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

public class RecentPaymentItem extends LinearLayout {

    ToggleButton toggleButton;

    LinearLayout recentExpandedContainer;

    public TextView timeText, typeText, resultText, account_text, ibanCode_text, des_text,
            bic_text, transfer_status_text;

    ImageView recentRecoverBtn;

    ViewGroup recentExpanderBar;


    public RecentPaymentItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        // TODO Auto-generated method stub
        super.onAttachedToWindow();
    }

    public void init() {
        if (toggleButton == null) {
            toggleButton = (ToggleButton)findViewById(R.id.recent_expand_btn);

            toggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked) {
                   	
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

            account_text = (TextView)findViewById(R.id.account_text);
            des_text = (TextView)findViewById(R.id.description_text);
            bic_text = (TextView)findViewById(R.id.bic_text);
            ibanCode_text = (TextView)findViewById(R.id.ibanCode_text);
            transfer_status_text = (TextView)findViewById(R.id.transfer_status_text);
            recentRecoverBtn = (ImageView)findViewById(R.id.recent_recover_btn);
            recentRecoverBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    RecentTransferModel recentTransferModel = (RecentTransferModel)getTag();
                    MainActivity mainActivity = (MainActivity)getContext();
                    long time = System.currentTimeMillis();
                    String nowTime = TimeUtil.getDateString(time, TimeUtil.dateFormat2);
                    recentTransferModel.setOperationDate(nowTime);
                    
                    PaymentConfirmLayout paymentConfirmLayout = mainActivity.paymentConfirmLayout;
                    mainActivity.tab4.show(0);
                    paymentConfirmLayout.recover(recentTransferModel);
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
}
