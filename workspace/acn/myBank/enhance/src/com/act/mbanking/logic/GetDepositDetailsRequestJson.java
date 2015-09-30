package com.act.mbanking.logic;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.act.mbanking.ServiceType;
import com.act.mbanking.bean.DepositInfo;
import com.act.mbanking.bean.GetDepositDetailsResponseModel;
import com.act.mbanking.bean.InvestmentDetail;
import com.act.mbanking.bean.RequestPublicModel;
import com.act.mbanking.utils.LogManager;

public class GetDepositDetailsRequestJson {
	
	public static String GetDepositDetailsReportProtocal(RequestPublicModel publicModel,String accountCode) {
		 String result = null;
	        try {
	            JSONObject jsonObj = new JSONObject();
	            JSONObject getDepositInfoObj = new JSONObject();
	            getDepositInfoObj.put("bankName", publicModel.getBankName());
	            getDepositInfoObj.put("serviceType", ServiceType.getDepositDetails);
	            getDepositInfoObj.put("enterpriseId", publicModel.getEnterpriseId());
	            getDepositInfoObj.put("customerCode", publicModel.getCustomerCode());
	            getDepositInfoObj.put("channel", publicModel.getChannel());
	            getDepositInfoObj.put("userAgent", publicModel.getUserAgent());
	            getDepositInfoObj.put("sessionId",publicModel.getSessionId());
	            getDepositInfoObj.put("token",publicModel.getToken());
	            getDepositInfoObj.put("accountCode",accountCode);
	            
	            jsonObj.put("GetDepositDetailsRequest", getDepositInfoObj);
	            result = jsonObj.toString();
	        }catch(Exception e){
	        	LogManager.e("GetDepositDetailsReportProtocal is error" + e.getLocalizedMessage());
	        }
		return result;
	}
	
	public static GetDepositDetailsResponseModel parseGetDeponsitDetailsResponse(String json){
		GetDepositDetailsResponseModel getDepositDetailsInfoResponse = new GetDepositDetailsResponseModel();
        
        if (json == null) {
            return null;
        }
        
        try {
            JSONObject jsonObj = new JSONObject(json);
            JSONObject getDepositDetailsObj = jsonObj.getJSONObject("GetDepositDetailsResponse");
            
            getDepositDetailsInfoResponse.responsePublicModel.setResultCode(getDepositDetailsObj.optInt("resultCode"));
            getDepositDetailsInfoResponse.responsePublicModel.setResultDescription(getDepositDetailsObj.optString("resultDescription"));
            if (getDepositDetailsInfoResponse.responsePublicModel.getResultCode() != 0) {
                JSONObject eventManagementObj = getDepositDetailsObj.getJSONObject("eventManagement");
                getDepositDetailsInfoResponse.responsePublicModel.eventManagement.setErrorCode(eventManagementObj.optString("errorCode"));
                getDepositDetailsInfoResponse.responsePublicModel.eventManagement.setErrorDescription(eventManagementObj.optString("errorDescription"));
                return getDepositDetailsInfoResponse;
            }
            
            getDepositDetailsInfoResponse.responsePublicModel.setTransactionId(getDepositDetailsObj.optString("transactionId"));
            getDepositDetailsInfoResponse.setPortfolioValue(getDepositDetailsObj.optDouble("portfolioValue"));
            getDepositDetailsInfoResponse.setHolder(getDepositDetailsObj.optString("holder"));
            
            JSONObject sharesObj = getDepositDetailsObj.getJSONObject("shares");
            DepositInfo depositInfo = new DepositInfo();
            depositInfo.setPercentage(sharesObj.optDouble("percentage"));
            depositInfo.setValue(sharesObj.optDouble("value"));
            JSONArray investmentDetailsArray = sharesObj.optJSONArray("investmentDetails");
            List<InvestmentDetail> investmentDetails = new ArrayList<InvestmentDetail>();
            for(int i=0;i<investmentDetailsArray.length();i++){
            	InvestmentDetail investmentDetail = new InvestmentDetail();
                investmentDetail.setAccrual(investmentDetailsArray.getJSONObject(i).optString("accrual"));
                investmentDetail.setCurrency(investmentDetailsArray.getJSONObject(i).optString("currency"));
                investmentDetail.setDeposit(investmentDetailsArray.getJSONObject(i).optInt("deposit"));
                investmentDetail.setDescription(investmentDetailsArray.getJSONObject(i).optString("description"));
                investmentDetail.setExchangeRate(investmentDetailsArray.getJSONObject(i).optDouble("exchangeRate"));
                investmentDetail.setExpiringDate(investmentDetailsArray.getJSONObject(i).optString("expiringDate"));
                investmentDetail.setGrossValue(investmentDetailsArray.getJSONObject(i).optInt("grossValue"));
                investmentDetail.setPrice(investmentDetailsArray.getJSONObject(i).optDouble("price"));
                investmentDetail.setPriceDate(investmentDetailsArray.getJSONObject(i).optString("priceDate"));
                investmentDetail.setPriceType(investmentDetailsArray.getJSONObject(i).optString("priceType"));
                investmentDetail.setPtFlag(investmentDetailsArray.getJSONObject(i).optString("ptFlag"));
                investmentDetail.setReliability(investmentDetailsArray.getJSONObject(i).optString("reliability"));
                investmentDetail.setTitle(investmentDetailsArray.getJSONObject(i).optString("title"));
                investmentDetail.setAmount(investmentDetailsArray.getJSONObject(i).optInt("amount"));
                
                investmentDetails.add(investmentDetail);
            }
            depositInfo.setInvestmentDetails(investmentDetails);
            getDepositDetailsInfoResponse.setShares(depositInfo);
            
            JSONObject bondsObj = getDepositDetailsObj.getJSONObject("bonds");
            depositInfo = new DepositInfo();
            depositInfo.setPercentage(bondsObj.optDouble("percentage"));
            depositInfo.setValue(bondsObj.optDouble("value"));
            investmentDetailsArray = bondsObj.optJSONArray("investmentDetails");
            investmentDetails = new ArrayList<InvestmentDetail>();
            for(int i=0;i<investmentDetailsArray.length();i++){
            	InvestmentDetail investmentDetail = new InvestmentDetail();
                investmentDetail.setAccrual(investmentDetailsArray.getJSONObject(i).optString("accrual"));
                investmentDetail.setCurrency(investmentDetailsArray.getJSONObject(i).optString("currency"));
                investmentDetail.setDeposit(investmentDetailsArray.getJSONObject(i).optInt("deposit"));
                investmentDetail.setDescription(investmentDetailsArray.getJSONObject(i).optString("description"));
                investmentDetail.setExchangeRate(investmentDetailsArray.getJSONObject(i).optDouble("exchangeRate"));
                investmentDetail.setExpiringDate(investmentDetailsArray.getJSONObject(i).optString("expiringDate"));
                investmentDetail.setGrossValue(investmentDetailsArray.getJSONObject(i).optInt("grossValue"));
                investmentDetail.setPrice(investmentDetailsArray.getJSONObject(i).optDouble("price"));
                investmentDetail.setPriceDate(investmentDetailsArray.getJSONObject(i).optString("priceDate"));
                investmentDetail.setPriceType(investmentDetailsArray.getJSONObject(i).optString("priceType"));
                investmentDetail.setPtFlag(investmentDetailsArray.getJSONObject(i).optString("ptFlag"));
                investmentDetail.setReliability(investmentDetailsArray.getJSONObject(i).optString("reliability"));
                investmentDetail.setTitle(investmentDetailsArray.getJSONObject(i).optString("title"));
                investmentDetail.setAmount(investmentDetailsArray.getJSONObject(i).optInt("amount"));
                investmentDetails.add(investmentDetail);
            }
            depositInfo.setInvestmentDetails(investmentDetails);
            getDepositDetailsInfoResponse.setBonds(depositInfo);
            
            JSONObject fundsObj = getDepositDetailsObj.getJSONObject("funds");
            depositInfo = new DepositInfo();
            depositInfo.setPercentage(fundsObj.optDouble("percentage"));
            depositInfo.setValue(fundsObj.optDouble("value"));
            investmentDetailsArray = fundsObj.optJSONArray("investmentDetails");
            investmentDetails = new ArrayList<InvestmentDetail>();
            for(int i=0;i<investmentDetailsArray.length();i++){
            	InvestmentDetail investmentDetail = new InvestmentDetail();
                investmentDetail.setAccrual(investmentDetailsArray.getJSONObject(i).optString("accrual"));
                investmentDetail.setCurrency(investmentDetailsArray.getJSONObject(i).optString("currency"));
                investmentDetail.setDeposit(investmentDetailsArray.getJSONObject(i).optInt("deposit"));
                investmentDetail.setDescription(investmentDetailsArray.getJSONObject(i).optString("description"));
                investmentDetail.setExchangeRate(investmentDetailsArray.getJSONObject(i).optDouble("exchangeRate"));
                investmentDetail.setExpiringDate(investmentDetailsArray.getJSONObject(i).optString("expiringDate"));
                investmentDetail.setGrossValue(investmentDetailsArray.getJSONObject(i).optInt("grossValue"));
                investmentDetail.setPrice(investmentDetailsArray.getJSONObject(i).optDouble("price"));
                investmentDetail.setPriceDate(investmentDetailsArray.getJSONObject(i).optString("priceDate"));
                investmentDetail.setPriceType(investmentDetailsArray.getJSONObject(i).optString("priceType"));
                investmentDetail.setPtFlag(investmentDetailsArray.getJSONObject(i).optString("ptFlag"));
                investmentDetail.setReliability(investmentDetailsArray.getJSONObject(i).optString("reliability"));
                investmentDetail.setTitle(investmentDetailsArray.getJSONObject(i).optString("title"));
                investmentDetail.setAmount(investmentDetailsArray.getJSONObject(i).optInt("amount"));
                investmentDetails.add(investmentDetail);
            }
            depositInfo.setInvestmentDetails(investmentDetails);
            getDepositDetailsInfoResponse.setFunds(depositInfo);
            
            JSONObject otherSecuritiesObj = getDepositDetailsObj.getJSONObject("otherSecurities");
            depositInfo = new DepositInfo();
            depositInfo.setPercentage(otherSecuritiesObj.optDouble("percentage"));
            depositInfo.setValue(otherSecuritiesObj.optDouble("value"));
            investmentDetailsArray = otherSecuritiesObj.optJSONArray("investmentDetails");
            investmentDetails = new ArrayList<InvestmentDetail>();
            for(int i=0;i<investmentDetailsArray.length();i++){
            	InvestmentDetail investmentDetail = new InvestmentDetail();
                investmentDetail.setAccrual(investmentDetailsArray.getJSONObject(i).optString("accrual"));
                investmentDetail.setCurrency(investmentDetailsArray.getJSONObject(i).optString("currency"));
                investmentDetail.setDeposit(investmentDetailsArray.getJSONObject(i).optInt("deposit"));
                investmentDetail.setDescription(investmentDetailsArray.getJSONObject(i).optString("description"));
                investmentDetail.setExchangeRate(investmentDetailsArray.getJSONObject(i).optDouble("exchangeRate"));
                investmentDetail.setExpiringDate(investmentDetailsArray.getJSONObject(i).optString("expiringDate"));
                investmentDetail.setGrossValue(investmentDetailsArray.getJSONObject(i).optInt("grossValue"));
                investmentDetail.setPrice(investmentDetailsArray.getJSONObject(i).optDouble("price"));
                investmentDetail.setPriceDate(investmentDetailsArray.getJSONObject(i).optString("priceDate"));
                investmentDetail.setPriceType(investmentDetailsArray.getJSONObject(i).optString("priceType"));
                investmentDetail.setPtFlag(investmentDetailsArray.getJSONObject(i).optString("ptFlag"));
                investmentDetail.setReliability(investmentDetailsArray.getJSONObject(i).optString("reliability"));
                investmentDetail.setTitle(investmentDetailsArray.getJSONObject(i).optString("title"));
                investmentDetail.setAmount(investmentDetailsArray.getJSONObject(i).optInt("amount"));
                investmentDetails.add(investmentDetail);
            }
            depositInfo.setInvestmentDetails(investmentDetails);
            getDepositDetailsInfoResponse.setOtherSecurities(depositInfo);
            
        }catch(Exception e){
        	LogManager.e("parseGetDeponsitDetailsResponse is error " + e.getLocalizedMessage());
        }
        return getDepositDetailsInfoResponse;
	}
}
