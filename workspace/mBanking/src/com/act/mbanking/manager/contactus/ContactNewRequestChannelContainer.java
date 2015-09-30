
package com.act.mbanking.manager.contactus;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.act.mbanking.R;
import com.act.mbanking.bean.TableContentList;

public class ContactNewRequestChannelContainer extends ExpandedContainer {

    private RadioGroup channel_group;

    private boolean bInitialized = false;

    private MyCheckedListener listener = new MyCheckedListener();

    private List<onChannelChangedListener> radioGroupListeners = new LinkedList<ContactNewRequestChannelContainer.onChannelChangedListener>();

    public ContactNewRequestChannelContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    public void init(List<TableContentList> tableContentList) {
        if (!bInitialized) {
            RadioButton radioButton;
            // int index = 0;
            channel_group = (RadioGroup)findViewById(R.id.request_channel_group);
            for (TableContentList tableContent : tableContentList) {
                radioButton = (RadioButton)LayoutInflater.from(getContext()).inflate(
                        R.layout.bank_radio_button_item, null);
                radioButton.setText(tableContent.getDescription());
                radioButton.setOnCheckedChangeListener(listener);
                channel_group.addView(radioButton);
            }
            bInitialized = true;
        }
    }

    public void addRadioGroupListener(onChannelChangedListener listener) {
        radioGroupListeners.add(listener);
    }

    public void removeRadioGroupListener(onChannelChangedListener listener) {
        radioGroupListeners.remove(listener);
    }

    private class MyCheckedListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                expandFocusResultChange(String.valueOf(buttonView.getText()));
                for (onChannelChangedListener listener : radioGroupListeners) {
                    listener.onModeChanged((String)buttonView.getTag(),
                            String.valueOf(buttonView.getText()));
                }
            }
        }

    }

    public interface onChannelChangedListener {
        public void onModeChanged(final String code, final String description);
    }
}
