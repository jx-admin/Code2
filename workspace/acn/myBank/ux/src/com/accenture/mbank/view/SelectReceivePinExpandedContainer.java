
package com.accenture.mbank.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RadioButton;

import com.accenture.mbank.BaseActivity;
import com.accenture.mbank.MainActivity;
import com.accenture.mbank.R;
import com.accenture.mbank.database.BankSqliteHelper;
import com.accenture.mbank.model.SettingModel;
import com.accenture.mbank.util.DialogManager;
import com.accenture.mbank.view.protocol.SaveEnAble;

public class SelectReceivePinExpandedContainer extends ExpandedContainer implements
        OnClickListener, SaveEnAble {

    ImageButton save;

    RadioButton selectReceivePinMailRadioButton, selectReceivePinSMSRadioButton;

    public SelectReceivePinExpandedContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        // TODO Auto-generated method stub
        super.onAttachedToWindow();

        init();
        restore();
    }

    public void init() {
        save = (ImageButton)findViewById(R.id.select_receive_pin_save);
        save.setOnClickListener(this);

        selectReceivePinMailRadioButton = (RadioButton)findViewById(R.id.select_receive_pin_mail);
        selectReceivePinSMSRadioButton = (RadioButton)findViewById(R.id.select_receive_pin_sms);

    }

    public void save() {

        MainActivity mainActivity = (MainActivity)getContext();

        if (selectReceivePinMailRadioButton.isChecked()) {
            mainActivity.setting.setChannelToRecelvePin(SettingModel.EMAIL);
        } else {
            mainActivity.setting.setChannelToRecelvePin(SettingModel.SMS);
        }
        BankSqliteHelper bankSqliteHelper = BankSqliteHelper.getInstance();
        boolean flag = bankSqliteHelper.update(mainActivity.setting);
        if (flag) {
//            mainActivity.displaySuccessMessage("save success!");
            DialogManager.createMessageDialog(getContext().getResources().getString(R.string.saved_settings),mainActivity).show();
        }

    }

    @Override
    public void restore() {
        MainActivity mainActivity = (MainActivity)getContext();
        if (BaseActivity.isOffline) {
            return;
        }
        if (mainActivity.setting.getChannelToRecelvePin() == SettingModel.EMAIL) {
            this.selectReceivePinMailRadioButton.setChecked(true);
        } else {

            this.selectReceivePinSMSRadioButton.setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {

        if (v == save) {
            save();
        }
    }

}
