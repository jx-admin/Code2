
package com.accenture.mbank.view;

import it.gruppobper.ams.android.bper.R;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PortfolioDetailItem extends LinearLayout {

    TextView title, quantity, price, datePrice;

    public PortfolioDetailItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        init();
    }

    private void init() {
        if (title == null) {

            title = (TextView)findViewById(R.id.port_detail_title);
            quantity = (TextView)findViewById(R.id.port_detail_quantity);
            price = (TextView)findViewById(R.id.port_detail_price);
            datePrice = (TextView)findViewById(R.id.port_detail_date_price);
        }

    }

    public void setTexts(String title, String quantity, String price, String datePrice) {
        init();
        this.title.setText(title);
        this.quantity.setText(quantity);
        this.price.setText(price);
        this.datePrice.setText(datePrice);
        this.title.setTextSize(10);
        this.datePrice.setTextSize(10);
        this.quantity.setTextSize(10);
        this.price.setTextSize(10);

    }

}
