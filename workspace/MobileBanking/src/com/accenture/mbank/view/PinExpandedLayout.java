
package com.accenture.mbank.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

import com.accenture.mbank.R;

public class PinExpandedLayout extends ExpandedContainer implements OnFocusChangeListener {

    EditText edit;

    String pin;

    public PinExpandedLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (edit == null) {
            edit = (EditText)findViewById(R.id.pin_edit);
            edit.setOnFocusChangeListener(this);
            edit.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // TODO Auto-generated method stub
                    expandResultChange(edit.getText().toString());
                    pin = edit.getText().toString();
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void afterTextChanged(Editable s) {
                    // TODO Auto-generated method stub

                }
            });

        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        if (v == edit && !hasFocus) {
            expandResultChange(edit.getText().toString());
            pin = edit.getText().toString();
        }
    }

    @Override
    protected void resetResult() {
        super.resetResult();

        edit.setText("");

    }

    @Override
    protected Object getValue() {
        // TODO Auto-generated method stub
        return pin;
    }
}
