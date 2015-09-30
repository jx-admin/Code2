
package com.accenture.mbank.view;

import it.gruppobper.ams.android.bper.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.accenture.mbank.BaseActivity;
import com.accenture.mbank.MainActivity;
import com.accenture.mbank.database.BankSqliteHelper;
import com.accenture.mbank.model.SettingModel;
import com.accenture.mbank.util.DialogManager;
import com.accenture.mbank.view.protocol.SaveEnAble;

public class ListsExpandedContainer extends ExpandedContainer implements OnClickListener,
        SaveEnAble, OnCheckedChangeListener {

    ImageButton save;

    RadioButton orderDateDESCRadioButton, orderAmountDESCRadioButton;

    RadioGroup dateGroup, amountGroup;

    RadioButton showTranBy20RadioButton, showTranBy2MonthsRadioButton;

    RadioButton recentPaymentDisplay10RadioButton, recentPaymentDisplay20RadioButton,
            recentPaymentDisplay30RadioButton;

    public ListsExpandedContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        // TODO Auto-generated method stub
        super.onAttachedToWindow();

        init();
        restore();
    }

    private void init() {
        save = (ImageButton)findViewById(R.id.list_save);
        save.setOnClickListener(this);

        dateGroup = (RadioGroup)findViewById(R.id.date_group);
        amountGroup = (RadioGroup)findViewById(R.id.amount_group);
        orderDateDESCRadioButton = (RadioButton)findViewById(R.id.order_date_desc);
        orderAmountDESCRadioButton = (RadioButton)findViewById(R.id.order_amount_desc);

        orderDateDESCRadioButton.setOnCheckedChangeListener(this);
        orderAmountDESCRadioButton.setOnCheckedChangeListener(this);

        showTranBy20RadioButton = (RadioButton)findViewById(R.id.show_tran_last_20);
        showTranBy2MonthsRadioButton = (RadioButton)findViewById(R.id.show_tran_last_2_months);

        recentPaymentDisplay10RadioButton = (RadioButton)findViewById(R.id.recent_payment_display_10);
        recentPaymentDisplay20RadioButton = (RadioButton)findViewById(R.id.recent_payment_display_20);
        recentPaymentDisplay30RadioButton = (RadioButton)findViewById(R.id.recent_payment_display_30);
    }

    public void save() {
        MainActivity mainActivity = (MainActivity)getContext();

        if (orderDateDESCRadioButton.isChecked()) {
            mainActivity.setting.setOrderListFor(SettingModel.SORT_DATE_DESC);
        } else if (orderAmountDESCRadioButton.isChecked()) {
            mainActivity.setting.setOrderListFor(SettingModel.SORT_AMOUNT_DESC);

        }

        if (showTranBy20RadioButton.isChecked()) {
            mainActivity.setting.setShowTransactionBy(SettingModel.LAST_20);
        } else {
            mainActivity.setting.setShowTransactionBy(SettingModel.LAST_2_MONTH);

        }
        if (recentPaymentDisplay10RadioButton.isChecked()) {
            mainActivity.setting.setRecentPaymentsDisplayed(SettingModel.RECENT_10);

        } else if (recentPaymentDisplay20RadioButton.isChecked()) {
            mainActivity.setting.setRecentPaymentsDisplayed(SettingModel.RECENT_20);

        } else {
            mainActivity.setting.setRecentPaymentsDisplayed(SettingModel.RECENT_30);
        }

        BankSqliteHelper bankSqliteHelper = BankSqliteHelper.getInstance();
        boolean flag = bankSqliteHelper.update(mainActivity.setting);
        if (flag) {
            DialogManager.createMessageDialog("",getContext().getResources().getString(R.string.saved_settings), getContext()).show();
        }

        AccountsLayout.setNeedUpdate(true);
        //CardsLayoutManager.setNeedUpdate(true);
    }

    @Override
    public void restore() {
        MainActivity mainActivity = (MainActivity)getContext();

        if (BaseActivity.isOffline) {
            return;
        }
        if (mainActivity.setting.getOrderListFor() == SettingModel.SORT_DATE_DESC) {
            this.orderDateDESCRadioButton.setChecked(true);
        } else if (mainActivity.setting.getOrderListFor() == SettingModel.SORT_AMOUNT_DESC) {
            this.orderAmountDESCRadioButton.setChecked(true);
        }
        if (mainActivity.setting.getShowTransactionBy() == SettingModel.LAST_20) {
            this.showTranBy20RadioButton.setChecked(true);
        } else {
            this.showTranBy2MonthsRadioButton.setChecked(true);
        }
        if (mainActivity.setting.getRecentPaymentsDisplayed() == SettingModel.RECENT_10) {
            recentPaymentDisplay10RadioButton.setChecked(true);
        } else if (mainActivity.setting.getRecentPaymentsDisplayed() == SettingModel.RECENT_20) {
            recentPaymentDisplay20RadioButton.setChecked(true);
        } else {
            recentPaymentDisplay30RadioButton.setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {

        if (v == save) {
            save();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (orderDateDESCRadioButton == buttonView) {
            amountGroup.clearCheck();
        } else if (orderAmountDESCRadioButton == buttonView) {
            amountGroup.clearCheck();
        }
    }

}
