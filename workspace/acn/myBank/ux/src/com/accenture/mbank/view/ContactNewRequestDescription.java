
package com.accenture.mbank.view;

import com.accenture.mbank.R;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

public class ContactNewRequestDescription extends ExpandedContainer {
    private EditText description;

    private boolean bInitialized = false;

    private MyTextWatcher textWatcher = new MyTextWatcher();

    public ContactNewRequestDescription(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        init();
    }

    private void init() {
        if (!bInitialized) {
            description = (EditText)findViewById(R.id.request_description);
            description.addTextChangedListener(textWatcher);
            bInitialized = true;
        }
    }

    public String getDescription() {
        return description.getText().toString();
    }
    
    public void setDesc(String desc){
        description.setText(desc);
    }

    private class MyTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            expandResultChange(String.valueOf(s));
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }

    }
}
