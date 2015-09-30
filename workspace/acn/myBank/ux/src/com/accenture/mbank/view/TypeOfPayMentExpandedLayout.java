
package com.accenture.mbank.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.accenture.mbank.R;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.LogManager;

public class TypeOfPayMentExpandedLayout extends ExpandedContainer implements
        OnCheckedChangeListener {

    RadioButton bankTransfor;

    RadioGroup rg;

    Handler handler;

    public TypeOfPayMentExpandedLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        handler = new Handler();
    }

    @Override
    protected void onAttachedToWindow() {
        // TODO Auto-generated method stub
        super.onAttachedToWindow();

        // bankTransfor = (RadioButton)findViewById(R.id.bank_transfor);
        // bankTransfor.setOnCheckedChangeListener(this);
        rg = (RadioGroup)findViewById(R.id.type_of_payment_rg);
        for (int i = 0; i < rg.getChildCount(); i++) {
            RadioButton sub = (RadioButton)rg.getChildAt(i);
            sub.setOnCheckedChangeListener(this);

        }

    }

    String serviceCode;

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (isChecked) {
            PaymentConfirmLayout paymentConfirmLayout = (PaymentConfirmLayout)expandBarResultListener
                    .getOwener();
            String text = buttonView.getText().toString();
            expandFocusResultChange(text);

            paymentConfirmLayout.onTypeSelected(text);
        }
    }

    @Override
    protected void onRecover(String text) {
        if (text == null || text.equals("")) {
            return;
        }
        for (int i = 0; i < rg.getChildCount(); i++) {
            RadioButton sub = (RadioButton)rg.getChildAt(i);
            if (sub.getText().toString().toUpperCase().equals(text.toUpperCase())
                    || text.toUpperCase().equals(Contants.PREPAID_CARD_RELOAD)) {

                LogManager.d("recover" + "checked type " + text);
                sub.setChecked(true);
            } else {
                sub.setChecked(false);
            }

        }
    }

    @Override
    public void setEditable(boolean flag) {
        super.setEditable(flag);
        for (int i = 0; i < rg.getChildCount(); i++) {
            RadioButton sub = (RadioButton)rg.getChildAt(i);
            sub.setEnabled(flag);
        }
    }

    public boolean isSelected() {
        boolean result = false;
        for (int i = 0; i < rg.getChildCount(); i++) {
            RadioButton sub = (RadioButton)rg.getChildAt(i);

            if (sub.isChecked()) {
                result = true;
            }
        }
        return result;
    }

    @Override
    protected void resetResult() {
        super.resetResult();
        rg.clearCheck();
    }

    @Override
    protected Object getValue() {
        // TODO Auto-generated method stub
        return serviceCode;
    }
}
