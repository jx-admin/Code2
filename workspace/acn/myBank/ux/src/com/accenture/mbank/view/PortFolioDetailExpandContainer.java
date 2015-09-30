
package com.accenture.mbank.view;

import java.text.ParseException;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.accenture.mbank.R;
import com.accenture.mbank.model.InvestmentDetail;
import com.accenture.mbank.util.TimeUtil;
import com.accenture.mbank.util.Utils;

public class PortFolioDetailExpandContainer extends ExpandedContainer {

    LinearLayout content;

    public PortFolioDetailExpandContainer(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onAttachedToWindow() {
        // TODO Auto-generated method stub
        super.onAttachedToWindow();
        init();
    };

    private void init() {

        if (content == null) {
            content = (LinearLayout)findViewById(R.id.protfolio_detail_content);
        }
    }

    public void add(InvestmentDetail investmentDetail) {
        init();
        if (investmentDetail != null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            PortfolioDetailItem portfolioDetailItem = (PortfolioDetailItem)inflater.inflate(
                    R.layout.portfolio_detail_item, null);
            
            String price = Utils.notPlusGenerateFormatMoney(getContext().getResources().getString(R.string.dollar),investmentDetail.getPrice());
//            String amount = Utils.notPlusGenerateFormatMoney("", investmentDetail.getAmount());
            String amount = investmentDetail.getAmount() + "";
            String pricedate = "";
            try {
                long time;
                time = TimeUtil.getTimeByString(investmentDetail.getPriceDate(),TimeUtil.dateFormat2);
                pricedate = TimeUtil.getDateString(time, TimeUtil.dateFormat5);
            } catch (ParseException e) {
                e.printStackTrace();
            } 

            portfolioDetailItem.setTexts(investmentDetail.getDescription(), amount, price,pricedate);
            content.addView(portfolioDetailItem);

        }

    }

    public static class PortfolioDetailItemData {
        public String title, quantity, price, datePrice;
    }
}
