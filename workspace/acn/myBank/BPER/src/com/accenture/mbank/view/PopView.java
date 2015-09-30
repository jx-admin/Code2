
package com.accenture.mbank.view;

import it.gruppobper.ams.android.bper.R;

import java.util.Calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.accenture.mbank.model.BranchListModel;
import com.accenture.mbank.util.LogManager;

public class PopView extends LinearLayout {
    BranchListModel currentBankListModel;

    TextView nameText, phoneNumberText, addressText, daysText;

    public PopView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setBranchListModel(BranchListModel branchListModel) {

        this.currentBankListModel = branchListModel;
        LogManager.d(branchListModel.toString());
        if (nameText == null) {
            init();
        }
        updateUI();

    }

    boolean chinaPerforms = false;

    private void updateUI() {
        if (currentBankListModel != null) {
            if (chinaPerforms) {
                nameText.setText(currentBankListModel.getName());
                addressText.setText("China, Beijing, Chaoyang, Jianguomen Outer St");
                addressText.setLines(2);
                phoneNumberText.setText("+86 010-6658008");
                String time = "9:00 AM - 5:00 PM";
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_WEEK);
                if (day == Calendar.SATURDAY || day == Calendar.SUNDAY) {

                	daysText.setText(currentBankListModel.getSaturdayTime());
                } else {
                	daysText.setText(time);
                }
            } else {
                nameText.setText(currentBankListModel.getName());
                addressText.setText(currentBankListModel.getAddress());
                phoneNumberText.setText(currentBankListModel.getPhoneNumber());
                String daysString = this.getResources().getString(R.string.monday_popmap) + " " + currentBankListModel.getMondayTime();
                daysString = daysString + " " + this.getResources().getString(R.string.tuesday_popmap) + " " + currentBankListModel.getTuesdayTime();
                daysString = daysString + " " + this.getResources().getString(R.string.wednesday_popmap) + " " + currentBankListModel.getWednesdayTime();
                daysString = daysString + " " + this.getResources().getString(R.string.thursday_popmap) + " " + currentBankListModel.getThursdayTime();
                daysString = daysString + " " + this.getResources().getString(R.string.friday_popmap) + " " + currentBankListModel.getFridayTime();
                daysString = daysString + " " + this.getResources().getString(R.string.saturday_popmap) + " " + currentBankListModel.getSaturdayTime();
                daysString = daysString + " " + this.getResources().getString(R.string.sunday_popmap) + " " + currentBankListModel.getSundayTime();

                daysText.setText(daysString);
            }

        }
    }

    void init() {
        nameText = (TextView)findViewById(R.id.popover_name);
        addressText = (TextView)findViewById(R.id.popover_address);
        phoneNumberText = (TextView)findViewById(R.id.popover_phone);

        daysText = (TextView)findViewById(R.id.pop_days);

    }
}
