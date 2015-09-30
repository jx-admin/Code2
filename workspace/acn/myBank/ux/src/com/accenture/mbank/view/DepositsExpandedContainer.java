
package com.accenture.mbank.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.accenture.mbank.R;
import com.accenture.mbank.logic.GetDepositInfoRequestJson;
import com.accenture.mbank.model.GetDepositInfoResponseModel;
import com.accenture.mbank.net.HttpConnector;
import com.accenture.mbank.net.ProgressOverlay;
import com.accenture.mbank.net.ProgressOverlay.OnProgressEvent;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.Utils;

public class DepositsExpandedContainer extends ExpandedContainer {

    public ItemExpander portfolioExpander;

    PortfolioExpandedContainer portfolioExpandedContainer;

    private String accountCode;

    Handler handler;

    GetDepositInfoResponseModel getDepositInfo;

    TextView port, shares, bonds, funds, more;

    public DepositsExpandedContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        handler = new Handler();
    }

    @Override
    protected void onAttachedToWindow() {
        // TODO Auto-generated method stub
        super.onAttachedToWindow();
        init();
    }

    private void init() {

        if (portfolioExpander == null) {
            portfolioExpander = (ItemExpander)findViewById(R.id.portfolio_expander);
            portfolioExpander.setLevel(1);
            portfolioExpander.setExpandButtonBackground(R.drawable.investment_expand_selector);
            portfolioExpander.setResultVisible(false);
            portfolioExpander.setExpand(false);
            portfolioExpander.setTitle(getResources().getString(R.string.portfolio));
            LayoutInflater inflater = LayoutInflater.from(getContext());

            portfolioExpandedContainer = (PortfolioExpandedContainer)inflater.inflate(
                    R.layout.portfolio_expanded_layout, null);

            portfolioExpander.setExpandedContainer(portfolioExpandedContainer);
            portfolioExpandedContainer.setAccountCode(accountCode);

            shares = (TextView)findViewById(R.id.share);
            bonds = (TextView)findViewById(R.id.bonds);
            funds = (TextView)findViewById(R.id.funds);
            more = (TextView)findViewById(R.id.more);
            port = (TextView)findViewById(R.id.protfolio);

        }
    }

    public void setAccountCode(String accountCode) {

        this.accountCode = accountCode;

    }

    @Override
    public void onExpand() {
        super.onExpand();
        if (accountCode == null || accountCode.equals("")) {
            return;
        }
        if (getDepositInfo != null) {
            return;
        }
        ProgressOverlay progressOverlay = new ProgressOverlay(getContext());
        progressOverlay.show("", new OnProgressEvent() {

            @Override
            public void onProgress() {

                String postData = GetDepositInfoRequestJson.GetDepositInfoReportProtocal(
                        Contants.publicModel, accountCode);
                HttpConnector httpConnector = new HttpConnector();
                String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData,
                        getContext());
                getDepositInfo = GetDepositInfoRequestJson.parseGetDepositInfoResponse(httpResult);
                if (getDepositInfo == null || !getDepositInfo.responsePublicModel.isSuccess()) {
                    return;
                }

                handler.post(new Runnable() {

                    @Override
                    public void run() {

                        setData();
                    }
                });

            }
        });
    }

    public void setData() {

        if (getDepositInfo != null) {
            String present = Utils.generateMoney(getDepositInfo.getShares().getPercentage());
            String value = Utils.generateMoney(getDepositInfo.getShares().getValue());
            if(getDepositInfo.getShares().getValue()>0){
                
            }
            value = Utils.generateFormatMoney(getContext().getResources().getString(R.string.dollar),getDepositInfo.getShares().getValue());
            shares.setText(value + "(" + present + "%)");

            present = Utils.generateMoney(getDepositInfo.getBonds().getPercentage());
            value = Utils.generateFormatMoney(getContext().getResources().getString(R.string.dollar),getDepositInfo.getBonds().getValue());
            bonds.setText(value + "(" + present + "%)");

            present = Utils.generateMoney(getDepositInfo.getFunds().getPercentage());
            value = Utils.generateFormatMoney(getContext().getResources().getString(R.string.dollar),getDepositInfo.getFunds().getValue());
            funds.setText(value + "(" + present + "%)");

            present = Utils.generateMoney(getDepositInfo.getOtherSecurities().getPercentage());
            value = Utils.generateFormatMoney(getContext().getResources().getString(R.string.dollar),getDepositInfo.getOtherSecurities().getValue());
            more.setText(value + "(" + present + "%)");

            value =Utils.generateFormatMoney(getContext().getResources().getString(R.string.dollar),getDepositInfo.getPortfolioValue());
            port.setText(value);

        }

    }
}
