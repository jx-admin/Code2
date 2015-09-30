
package com.act.mbanking.manager.contactus;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.act.mbanking.R;
import com.act.mbanking.bean.TableContentList;

public class ContactNewRequestObjectContainer extends ExpandedContainer {

    private RadioGroup object_group;

    private boolean bInitialized = false;

    private String topicOfInterest;

    private MyCheckedListener listener = new MyCheckedListener();

    public ContactNewRequestObjectContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    public void init(List<TableContentList> tableContentList) {
        if (!bInitialized) {
            RadioButton radioButton;
            object_group = (RadioGroup)findViewById(R.id.request_object_group);
            for (TableContentList tableContent : tableContentList) {
                radioButton = (RadioButton)LayoutInflater.from(getContext()).inflate(
                        R.layout.bank_radio_button_item, null);
                radioButton.setText(tableContent.getDescription());
                radioButton.setOnCheckedChangeListener(listener);
                object_group.addView(radioButton);
            }
            bInitialized = true;
        }
    }

    public String getTopicOfInterest() {
        return topicOfInterest;
    }

    private class MyCheckedListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                topicOfInterest = (String)buttonView.getText();
                expandFocusResultChange(topicOfInterest);
            }
        }

    }
}
