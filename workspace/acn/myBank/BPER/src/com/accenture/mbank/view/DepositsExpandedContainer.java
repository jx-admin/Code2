
package com.accenture.mbank.view;

import it.gruppobper.ams.android.bper.R;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.accenture.mbank.logic.GetDepositDetailsRequestJson;
import com.accenture.mbank.model.GetDepositDetailsResponseModel;
import com.accenture.mbank.model.GetDepositInfoResponseModel;
import com.accenture.mbank.model.InvestmentDetail;
import com.accenture.mbank.net.HttpConnector;
import com.accenture.mbank.net.ProgressOverlay;
import com.accenture.mbank.net.ProgressOverlay.OnProgressEvent;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.Utils;

public class DepositsExpandedContainer extends ExpandedContainer {

    public EnhanceItemExpander sharesExpander,bondsExpander,fundsExpander,otherSecuritiesExpander;
    PortFolioDetailExpandContainer sharesExpanderContainer, bondsExpanderContainer, fundsExpanderContainer, otherSecuritiesContainer;
    private String accountCode;

    Handler handler;
    
    GetDepositInfoResponseModel getDepositInfo;

    public DepositsExpandedContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        handler = new Handler();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
		init();
    }

    private void init() {
    	String names[] =getContext().getResources().getStringArray(R.array.investment_names);
    	String value = Utils.generateFormatMoney(getContext().getResources().getString(R.string.eur),0);
        if (sharesExpander == null) {
        	sharesExpander = (EnhanceItemExpander)findViewById(R.id.sharesExpander);
        	sharesExpander.setLevel(0);
        	sharesExpander.setTitle(names[0]);
        	sharesExpander.setItem_lable(R.string.controvalore);
        	sharesExpander.setResult(value);
        	sharesExpander.setExpandButtonBackground(R.drawable.investment_expand_selector);
        	sharesExpander.setExpand(false);
        	
        	bondsExpander = (EnhanceItemExpander)findViewById(R.id.bondsExpander);
        	bondsExpander.setLevel(0);
        	bondsExpander.setTitle(names[1]);
        	bondsExpander.setItem_lable(R.string.controvalore);
        	bondsExpander.setResult(value);
        	bondsExpander.setExpandButtonBackground(R.drawable.investment_expand_selector);
        	bondsExpander.setExpand(false);
        	
        	fundsExpander = (EnhanceItemExpander)findViewById(R.id.fundsExpander);
        	fundsExpander.setLevel(0);
        	fundsExpander.setTitle(names[2]);
        	fundsExpander.setItem_lable(R.string.controvalore);
        	fundsExpander.setResult(value);
        	fundsExpander.setExpandButtonBackground(R.drawable.investment_expand_selector);
        	fundsExpander.setExpand(false);
        	
        	otherSecuritiesExpander = (EnhanceItemExpander)findViewById(R.id.otherSecuritiesExpander);
        	otherSecuritiesExpander.setLevel(0);
        	otherSecuritiesExpander.setTitle(names[3]);
        	otherSecuritiesExpander.setItem_lable(R.string.controvalore);
        	otherSecuritiesExpander.setResult(value);
        	otherSecuritiesExpander.setExpandButtonBackground(R.drawable.investment_expand_selector);
        	otherSecuritiesExpander.setExpand(false);
        	
        	LayoutInflater inflater = LayoutInflater.from(getContext());
         	sharesExpanderContainer = (PortFolioDetailExpandContainer)inflater.inflate(R.layout.portfolio_detail_expander, null);
         	
         	sharesExpander.setExpandedContainer(sharesExpanderContainer);

         	bondsExpanderContainer = (PortFolioDetailExpandContainer)inflater.inflate(R.layout.portfolio_detail_expander, null);
         	bondsExpanderContainer.setObbligazioni(true);
         	bondsExpander.setExpandedContainer(bondsExpanderContainer);
         	
         	fundsExpanderContainer = (PortFolioDetailExpandContainer)inflater.inflate(R.layout.portfolio_detail_expander, null);
         	
            fundsExpander.setExpandedContainer(fundsExpanderContainer);

            otherSecuritiesContainer = (PortFolioDetailExpandContainer)inflater.inflate(R.layout.portfolio_detail_expander, null);
            
            otherSecuritiesExpander.setExpandedContainer(otherSecuritiesContainer);
        }
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    @Override
    public void onExpand() {
        super.onExpand();
        getDepositInfo = (GetDepositInfoResponseModel) getTag();
        loadDepositDetails(this.accountCode);
    }
    
    
    GetDepositDetailsResponseModel getDepositDetails;
	private void loadDepositDetails(final String accountCode){
		 ProgressOverlay progressOverlay = new ProgressOverlay(getContext());
	        progressOverlay.show("", new OnProgressEvent() {
	            @Override
	            public void onProgress() {
	                String postData = GetDepositDetailsRequestJson.GetDepositDetailsReportProtocal(Contants.publicModel, accountCode);
	                HttpConnector httpConnector = new HttpConnector();
	                String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData,getContext());
	                getDepositDetails = GetDepositDetailsRequestJson.parseGetDeponsitDetailsResponse(httpResult);
	                if (getDepositDetails == null || !getDepositDetails.responsePublicModel.isSuccess()) {
	                    return;
	                }
	                
	                handler.post(new Runnable() {
	                    @Override
	                    public void run() {
	                        //update data
	                    	setData();
	                    }
	                });
	            }
	        });
	}

    public void setData() {
        if (getDepositInfo != null) {
            String present = "";
            String value = "";
            
            sharesExpanderContainer.setTag(getDepositDetails.getShares().getInvestmentDetails());
            
            bondsExpanderContainer.setTag(getDepositDetails.getBonds().getInvestmentDetails());
            
            fundsExpanderContainer.setTag(getDepositDetails.getFunds().getInvestmentDetails());
            
            otherSecuritiesContainer.setTag(getDepositDetails.getOtherSecurities().getInvestmentDetails());
            
            present = Utils.formatPercentDouble(getDepositInfo.getShares().getPercentage());
            value = Utils.generateFormatMoney(getContext().getResources().getString(R.string.eur),getDepositInfo.getShares().getValue());
            sharesExpander.setResult(value + "(" + present + ")");

            present = Utils.formatPercentDouble(getDepositInfo.getBonds().getPercentage());
            value = Utils.generateFormatMoney(getContext().getResources().getString(R.string.eur),getDepositInfo.getBonds().getValue());
            bondsExpander.setResult(value + "(" + present + ")");

            present = Utils.formatPercentDouble(getDepositInfo.getFunds().getPercentage());
            value = Utils.generateFormatMoney(getContext().getResources().getString(R.string.eur),getDepositInfo.getFunds().getValue());
            fundsExpander.setResult(value + "(" + present + ")");
            
            present = Utils.formatPercentDouble(getDepositInfo.getOtherSecurities().getPercentage());
            value = Utils.generateFormatMoney(getContext().getResources().getString(R.string.eur),getDepositInfo.getOtherSecurities().getValue());
            otherSecuritiesExpander.setResult(value + "(" + present + ")");
           
            List<InvestmentDetail> shares = getDepositDetails.getShares().getInvestmentDetails();
            List<InvestmentDetail> bonds = getDepositDetails.getBonds().getInvestmentDetails();
            List<InvestmentDetail> funds = getDepositDetails.getFunds().getInvestmentDetails();
            List<InvestmentDetail> mores = getDepositDetails.getOtherSecurities().getInvestmentDetails();
            setData(shares, sharesExpanderContainer,SHARES,getDepositInfo.getShares().getPercentage());
            setData(bonds, bondsExpanderContainer,BONDS,getDepositInfo.getBonds().getPercentage());
            setData(funds, fundsExpanderContainer,FUNDS,getDepositInfo.getFunds().getPercentage());
            setData(mores, otherSecuritiesContainer,OTHER,getDepositInfo.getOtherSecurities().getPercentage());
        }
    }

    public static final String SHARES = "SHARES";
    public static final String BONDS = "BONDS";
    public static final String FUNDS = "FUNDS";
    public static final String OTHER = "OTHER";
    
    private void setData(List<InvestmentDetail> list,PortFolioDetailExpandContainer portFolioDetailExpandContainer,String type,double present) {
    	portFolioDetailExpandContainer.content.removeAllViews();
    	if(list.size() == 0 || present == 0){
    		InvestmentDetail investmentDetail = null;
    		portFolioDetailExpandContainer.add(investmentDetail,type);
    	}else {
    		for (InvestmentDetail investmentDetail : list) {
                portFolioDetailExpandContainer.add(investmentDetail,type);
            }
    	}
    }

}
