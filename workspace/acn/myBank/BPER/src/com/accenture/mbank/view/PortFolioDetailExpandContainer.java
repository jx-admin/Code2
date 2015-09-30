
package com.accenture.mbank.view;

import it.gruppobper.ams.android.bper.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.accenture.mbank.model.InvestmentDetail;
import com.accenture.mbank.util.TimeUtil;
import com.accenture.mbank.util.Utils;

public class PortFolioDetailExpandContainer extends ExpandedContainer {

    LinearLayout content;
    TextView port_detail_quantity;
    boolean isObbligazioni;
    public PortFolioDetailExpandContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        init();
    };

    private void init() {
        if (content == null) {
        	port_detail_quantity = (TextView)findViewById(R.id.port_detail_quantity);
            content = (LinearLayout)findViewById(R.id.protfolio_detail_content);
        }
    }
    
    public void add(InvestmentDetail investmentDetail,String type) {
        init();
        
        if(isObbligazioni){
        	port_detail_quantity.setText(R.string.valore);
        }
        LayoutInflater inflater = LayoutInflater.from(getContext());
        if (investmentDetail != null) {
            PortfolioDetailItem portfolioDetailItem = (PortfolioDetailItem)inflater.inflate(R.layout.portfolio_detail_item, null);
            
            String price = Utils.generateFormatMoney(getContext().getResources().getString(R.string.eur),investmentDetail.getPrice());
            String amount = Utils.notPlusGenerateFormatMoney("",investmentDetail.getAmount());
//            String amount =investmentDetail.getAmount() + "";
            String pricedate = "";
            long time;
            time = TimeUtil.getTimeByString(investmentDetail.getPriceDate(),TimeUtil.dateFormat2);
            pricedate = TimeUtil.getDateString(time, TimeUtil.dateFormat5);

            portfolioDetailItem.setTexts(investmentDetail.getDescription(), amount, price,pricedate);
            content.addView(portfolioDetailItem);
        }else {
        	View miss_data = inflater.inflate(R.layout.miss_data, null);
        	TextView missDataTextView = (TextView) miss_data.findViewById(R.id.investments_miss_data);
        	if(DepositsExpandedContainer.SHARES.equals(type)){
        		missDataTextView.setText(getContext().getResources().getString(R.string.missing_azione));
        	}else if(DepositsExpandedContainer.BONDS.equals(type)){
        		missDataTextView.setText(getContext().getResources().getString(R.string.missing_obbligazione));
        	}else if(DepositsExpandedContainer.FUNDS.equals(type)){
        		missDataTextView.setText(getContext().getResources().getString(R.string.missing_fondo));
        	}else if(DepositsExpandedContainer.OTHER.equals(type)){
        		missDataTextView.setText(getContext().getResources().getString(R.string.missing_altro));
        	}
        	content.addView(miss_data);
        }
    }

    public static class PortfolioDetailItemData {
        public String title, quantity, price, datePrice;
    }

	public boolean isObbligazioni() {
		return isObbligazioni;
	}

	public void setObbligazioni(boolean isObbligazioni) {
		this.isObbligazioni = isObbligazioni;
	}
}
