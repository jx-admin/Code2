
package com.accenture.mbank.view;

import it.gruppobper.ams.android.bper.R;

import com.accenture.mbank.util.Contants;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

public class ContactNewRequestChannelEmail extends ExpandedContainer {
    private EditText emailInput;

    private boolean bInitialized = false;

    public ContactNewRequestChannelEmail(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        init();
    }

    private void init() {
        if (!bInitialized) {
            emailInput = (EditText)findViewById(R.id.request_channel_email_email);
            emailInput.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    expandResultChange(String.valueOf(s));
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            emailInput.setText(Contants.getUserInfo.getUserprofileHb().getReferenceEmail());
            bInitialized = true;
        }
    }

    public String getEmail() {
        return emailInput.getText().toString();
    }
}
