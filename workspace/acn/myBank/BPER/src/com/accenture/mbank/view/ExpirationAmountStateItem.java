
package com.accenture.mbank.view;

import it.gruppobper.ams.android.bper.R;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ExpirationAmountStateItem extends LinearLayout {

    TextView ex, amount;

    ImageView state;

    public ExpirationAmountStateItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        // TODO Auto-generated method stub
        super.onAttachedToWindow();
        if (ex == null) {
            init();
        }
    }

    private void init() {

        ex = (TextView)findViewById(R.id.state_exp);
        amount = (TextView)findViewById(R.id.state_amo);

        state = (ImageView)findViewById(R.id.state_sta);
    }

    public void setUI(String ex, String amount, boolean paided) {

        if (this.ex == null) {
            init();
        }
        this.ex.setText(ex);
        this.amount.setText(amount);
        if (paided) {
            state.setBackgroundResource(R.drawable.check_paid);

        } else {

            state.setBackgroundResource(R.drawable.check_not_paid);
        }
    }

}
