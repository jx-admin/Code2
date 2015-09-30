
package com.act.mbanking.view;

import java.util.Calendar;

import com.act.mbanking.R;
import com.act.mbanking.bean.BranchListModel;
import com.act.mbanking.utils.LogManager;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PopView extends LinearLayout {
    BranchListModel currentBankListModel;

    TextView nameText, phoneNumberText, addressText, mondayToFridayText, saturdayText, sundayText;

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

                    mondayToFridayText.setText(currentBankListModel.getSaturdayTime());
                } else {
                    mondayToFridayText.setText(time);
                }
            } else {
                nameText.setText(currentBankListModel.getName());
                addressText.setText(currentBankListModel.getAddress());
                phoneNumberText.setText(currentBankListModel.getPhoneNumber());
                mondayToFridayText.setText(currentBankListModel.getMondayTime());
                saturdayText.setText(currentBankListModel.getSaturdayTime());
                sundayText.setText(currentBankListModel.getSundayTime());
            }

        }
    }

    void init() {
        nameText = (TextView)findViewById(R.id.popover_name);
        addressText = (TextView)findViewById(R.id.popover_address);
        phoneNumberText = (TextView)findViewById(R.id.popover_phone);

        mondayToFridayText = (TextView)findViewById(R.id.pop_monday_to_friday);
        saturdayText = (TextView)findViewById(R.id.pop_saturday);
        sundayText = (TextView)findViewById(R.id.pop_sunday);

    }
}
