
package com.act.mbanking.manager.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.act.mbanking.R;
import com.act.mbanking.bean.AccountsModel;
import com.act.mbanking.bean.RecentTransferModel;

public class RecentPaymentItem extends LinearLayout {

    ToggleButton toggleButton;

    LinearLayout recentExpandedContainer;

    public TextView timeText, typeText, resultText, one_text, four_text, two_text, three_text,
            five_text;

    Button recentRecoverBtn;

    ViewGroup recentExpanderBar;

    private RecentTransferModel recentTransferModel;

    private AccountsModel accountModel;

    public AccountsModel getAccountModel() {
        return accountModel;
    }

    public void setAccountModel(AccountsModel accountModel) {
        this.accountModel = accountModel;
    }

    public RecentPaymentItem(Context context, AttributeSet attrs) {
        super(context, attrs);
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
            recentRecoverBtn = (Button)findViewById(R.id.recent_recover_btn);
            recentRecoverBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

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
