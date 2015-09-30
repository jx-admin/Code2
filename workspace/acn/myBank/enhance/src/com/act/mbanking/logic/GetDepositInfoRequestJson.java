package com.act.mbanking.logic;

import org.json.JSONObject;

import com.act.mbanking.ServiceType;
import com.act.mbanking.bean.DepositInfo;
import com.act.mbanking.bean.GetDepositInfoResponseModel;
import com.act.mbanking.bean.RequestPublicModel;
import com.act.mbanking.utils.LogManager;

public class GetDepositInfoRequestJson {
	public static String GetDepositInfoReportProtocal(RequestPublicModel publicModel,String accountCode) {
		 String result = null;
	        try {
	            JSONObject jsonObj = new JSONObject();
	            JSONObject getDepositInfoObj = new JSONObject();
	            getDepositInfoObj.put("bankName", publicModel.getBankName());
	            getDepositInfoObj.put("serviceType", ServiceType.getDepositInfo);
	            getDepositInfoObj.put("enterpriseId", publicModel.getEnterpriseId());
	            getDepositInfoObj.put("customerCode", publicModel.getCustomerCode());
	            getDepositInfoObj.put("channel", publicModel.getChannel());
	            getDepositInfoObj.put("userAgent", publicModel.getUserAgent());
	            getDepositInfoObj.put("sessionId",publicModel.getSessionId());
	            getDepositInfoObj.put("token",publicModel.getToken());
	            getDepositInfoObj.put("accountCode",accountCode);
	            jsonObj.put("GetDepositInfoRequest", getDepositInfoObj);
	            result = jsonObj.toString();
	        }catch(Exception e){
	        	LogManager.e("GetDepositInfoReportProtocal is error" + e.getLocalizedMessage());
	        }
		return result;
	}
	
	public static GetDepositInfoResponseModel parseGetDepositInfoResponse(String json){
		GetDepositInfoResponseModel getDepositInfoResponse = new GetDepositInfoResponseModel();
        
        if (json == null) {
            return null;
        }
        
        try {
            JSONObject jsonObj = new JSONObject(json);
            JSONObject getDepositInfoObj = jsonObj.getJSONObject("GetDepositInfoResponse");
            
            getDepositInfoResponse.responsePublicModel.setResultCode(getDepositInfoObj.optInt("resultCode"));
            getDepositInfoResponse.responsePublicModel.setResultDescription(getDepositInfoObj.optString("resultDescription"));
            if (getDepositInfoResponse.responsePublicModel.getResultCode() != 0) {
                JSONObject eventManagementObj = getDepositInfoObj.getJSONObject("eventManagement");
                getDepositInfoResponse.responsePublicModel.eventManagement.setErrorCode(eventManagementObj.optString("errorCode"));
                getDepositInfoResponse.responsePublicModel.eventManagement.setErrorDescription(eventManagementObj.optString("errorDescription"));
                return getDepositInfoResponse;
            }
            
            getDepositInfoResponse.responsePublicModel.setTransactionId(getDepositInfoObj.optString("transactionId"));
            getDepositInfoResponse.setPortfolioValue(getDepositInfoObj.optDouble("portfolioValue"));
            getDepositInfoResponse.setHolder(getDepositInfoObj.optString("holder"));
            
            JSONObject sharesObj = getDepositInfoObj.getJSONObject("shares");
            DepositInfo sharesInfo = new DepositInfo();
            sharesInfo.setType(sharesObj.optString("type"));
            sharesInfo.setValue(sharesObj.optDouble("value"));
            sharesInfo.setPercentage(sharesObj.optDouble("percentage"));
            getDepositInfoResponse.setShares(sharesInfo);
            
            JSONObject bondObj = getDepositInfoObj.getJSONObject("bonds");
            DepositInfo bondInfo = new DepositInfo();
            bondInfo.setType(bondObj.optString("type"));
            bondInfo.setValue(bondObj.optDouble("value"));
            bondInfo.setPercentage(bondObj.optDouble("percentage"));
            getDepositInfoResponse.setBonds(bondInfo);
            
            JSONObject fundObj = getDepositInfoObj.getJSONObject("funds");
            DepositInfo fundInfo = new DepositInfo();
            fundInfo.setType(fundObj.optString("type"));
            fundInfo.setValue(fundObj.optDouble("value"));
            fundInfo.setPercentage(fundObj.optDouble("percentage"));
            getDepositInfoResponse.setFunds(fundInfo);
            
            JSONObject otherSecuritiesObj = getDepositInfoObj.getJSONObject("otherSecurities");
            DepositInfo otherSecuritiesInfo = new DepositInfo();
            otherSecuritiesInfo.setType(otherSecuritiesObj.optString("type"));
            otherSecuritiesInfo.setValue(otherSecuritiesObj.optDouble("value"));
            otherSecuritiesInfo.setPercentage(otherSecuritiesObj.optDouble("percentage"));
            getDepositInfoResponse.setOtherSecurities(otherSecuritiesInfo);
            
        }catch(Exception e){
        	LogManager.e("parseGetDepositInfoResponse is error " + e.getLocalizedMessage());
        }
        return getDepositInfoResponse;
	}
}
