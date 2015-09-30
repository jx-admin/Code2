package com.accenture.mbank.logic;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.accenture.mbank.model.BalanceAccountsModel;
import com.accenture.mbank.model.GetBalanceResponseModel;
import com.accenture.mbank.model.GetDepositInfoResponseModel;
import com.accenture.mbank.model.RequestPublicModel;
import com.accenture.mbank.util.LogManager;
import com.accenture.mbank.util.ServiceType;

public class BalanceJson {
	public static String BalanceReportProtocal(String paymentMethod,
			RequestPublicModel publicModel) {
		String result = null;
		try {
			JSONObject jsonObj = new JSONObject();
			JSONObject getBalanceObj = new JSONObject();
			getBalanceObj.put("bankName", publicModel.getBankName());
			getBalanceObj.put("serviceType", ServiceType.getBalance);
			getBalanceObj.put("enterpriseId", publicModel.getEnterpriseId());
			getBalanceObj.put("paymentMethod", paymentMethod);
			getBalanceObj.put("customerCode", publicModel.getCustomerCode());
			getBalanceObj.put("channel", publicModel.getChannel());
			getBalanceObj.put("sorting", publicModel.getChannel());
			getBalanceObj.put("userAgent", publicModel.getUserAgent());
			getBalanceObj.put("token", publicModel.getToken());
			getBalanceObj.put("sessionId", publicModel.getSessionId());

			jsonObj.put("GetBalanceRequest", getBalanceObj);
			result = jsonObj.toString();
		} catch (Exception e) {
			LogManager.e("BalanceReportProtocal is error "
					+ e.getLocalizedMessage());
		}
		return result;
	}

	public static GetBalanceResponseModel parseGetBalanceResponse(String json) {
		GetBalanceResponseModel getBalanceResponse = new GetBalanceResponseModel();
        
        if (json == null) {
            return null;
        }
        
        try {
            JSONObject jsonObj = new JSONObject(json);
            JSONObject  getBalanceObj = jsonObj.getJSONObject("GetBalanceResponse");
            
            getBalanceResponse.responsePublicModel.setResultCode(getBalanceObj.optInt("resultCode"));
            getBalanceResponse.responsePublicModel.setResultDescription(getBalanceObj.optString("resultDescription"));
            if (getBalanceResponse.responsePublicModel.getResultCode() != 0) {
                JSONObject eventManagementObj = getBalanceObj.getJSONObject("eventManagement");
                getBalanceResponse.responsePublicModel.eventManagement.setErrorCode(eventManagementObj.optString("errorCode"));
                getBalanceResponse.responsePublicModel.eventManagement.setErrorDescription(eventManagementObj.optString("errorDescription"));
                return getBalanceResponse;
            }
            
            getBalanceResponse.responsePublicModel.setTransactionId(getBalanceObj.optString("transactionId"));
            
            JSONArray accountsArray = getBalanceObj.getJSONArray("accounts");
            List<BalanceAccountsModel> banlanceAccounts = new ArrayList<BalanceAccountsModel>();
            for(int i=0;i<accountsArray.length();i++){
            	BalanceAccountsModel banlanceAccount = new BalanceAccountsModel();
            	banlanceAccount.setAccountBalance(accountsArray.optJSONObject(i).optString("accountBalance"));
            	banlanceAccount.setAccountCode(accountsArray.optJSONObject(i).optString("accountCode"));
            	banlanceAccount.setAvailableBalance(accountsArray.optJSONObject(i).optDouble("availableBalance"));
            	banlanceAccount.setBankServiceCode(accountsArray.optJSONObject(i).optString("bankServiceCode"));
            	banlanceAccount.setCardState(accountsArray.optJSONObject(i).optString("cardState"));
            	banlanceAccount.setCurrency(accountsArray.optJSONObject(i).optString("currency"));
            	banlanceAccount.setExpirationDate(accountsArray.optJSONObject(i).optString("expirationDate"));
            	banlanceAccount.setHolder(accountsArray.optJSONObject(i).optString("holder"));
            	banlanceAccount.setPersonalizedName(accountsArray.optJSONObject(i).optString("personalizedName"));
            	banlanceAccount.setPlafond(accountsArray.optJSONObject(i).optDouble("plafond"));
            	banlanceAccounts.add(banlanceAccount);
            }
            getBalanceResponse.setBanlaceAccounts(banlanceAccounts);
            
        }catch(Exception e){
        	LogManager.e("parseGetBalanceResponse is error " + e.getLocalizedMessage());
        }
		return getBalanceResponse;
	}
}
