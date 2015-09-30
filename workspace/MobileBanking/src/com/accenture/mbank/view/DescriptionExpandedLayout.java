
package com.accenture.mbank.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.accenture.mbank.R;
import com.accenture.mbank.util.ServiceCode;

public class DescriptionExpandedLayout extends ExpandedContainer implements TextWatcher {

    EditText edit;

    String description;

    public DescriptionExpandedLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (edit == null) {
            edit = (EditText)findViewById(R.id.description_edit);
            edit.addTextChangedListener(this);
            edit.setOnFocusChangeListener(new OnFocusChangeListener() {

                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    // TODO Auto-generated method stub

                    if (!hasFocus) {
                        expandFocusResultChange(edit.getText().toString());
                        description = edit.getText().toString();
                    }
                }
            });
        }
    }

    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        return false;
    }

    public void onFocusChange(View v, boolean hasFocus) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        expandResultChange(edit.getText().toString());
        description = edit.getText().toString();
    }

    @Override
    public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onRecover(String text) {
        // TODO Auto-generated method stub
        if (text == null || text.equals("")) {
            return;
        }
        description = text;
        edit.setText(description);
    }

    @Override
    protected void resetResult() {
        super.resetResult();
        edit.setText("");
        ViewGroup v = (ViewGroup)this.expandBarResultListener;
        v.setVisibility(View.VISIBLE);
    }

    @Override
    protected Object getValue() {
        // TODO Auto-generated method stub
        return description;
    }

    public void showType(final String typeCode) {
        ViewGroup v = (ViewGroup)this.expandBarResultListener;

        resetResult();
        if (typeCode.equals(ServiceCode.BANK_TRANSFER_PAYMENT)) {
            v.setVisibility(View.VISIBLE);
        } else if (typeCode.equals(ServiceCode.TRANSFER_ENTRY_PAYMENT)) {
            v.setVisibility(View.VISIBLE);
        } else if (typeCode.equals(ServiceCode.SIM_TOP_UP)) {
            v.setVisibility(View.GONE);
        } else if (typeCode.equals(ServiceCode.CARD_RECHARGE_PAYMENT)) {
            v.setVisibility(View.VISIBLE);
        }
    }
}
