
package com.accenture.mbank.logic;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.model.AccountsForServiceModel;
import com.accenture.mbank.model.BankServiceType;
import com.accenture.mbank.model.GetAccountsByServicesResponseModel;
import com.accenture.mbank.model.RequestPublicModel;
import com.accenture.mbank.model.ServicesModel;
import com.accenture.mbank.util.LogManager;
import com.accenture.mbank.util.ServiceType;
import com.accenture.mbank.util.TimeUtil;

public class AccountsByServicesJson {
    /**
     * @param publicModel
     * @param services
     * @return
     */
    public static String AccountsByServicesReportProtocal(RequestPublicModel publicModel,
            List<ServicesModel> services) {
        String result = null;
        try {
            JSONObject jsonObj = new JSONObject();
            JSONObject getAccountsByServicesObj = new JSONObject();
            getAccountsByServicesObj.put("bankName", publicModel.getBankName());
            getAccountsByServicesObj.put("serviceType", ServiceType.getAccountsByServices);
            getAccountsByServicesObj.put("enterpriseId", publicModel.getEnterpriseId());
            getAccountsByServicesObj.put("customerNumber", publicModel.getCustomerNumber());
            getAccountsByServicesObj.put("channel", publicModel.getChannel());
            getAccountsByServicesObj.put("userAgent", publicModel.getUserAgent());
            getAccountsByServicesObj.put("sessionId", publicModel.getSessionId());
            getAccountsByServicesObj.put("token", publicModel.getToken());

            JSONArray servicesArray = new JSONArray();
            for (int i = 0; i < services.size(); i++) {
                JSONObject serviceObj = new JSONObject();
                serviceObj.put("serviceCode", services.get(i).getServiceCode());
                servicesArray.put(i, serviceObj);
            }
            getAccountsByServicesObj.put("services", servicesArray);

            jsonObj.put("GetAccountsByServicesRequest", getAccountsByServicesObj);
            result = jsonObj.toString();
        } catch (Exception e) {
            LogManager.e("AccountsByServicesReportProtocal is error " + e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * @param json
     * @return
     */
    public static GetAccountsByServicesResponseModel ParseGetAccountsByServicesResponse(String json) {

        GetAccountsByServicesResponseModel responsePublicModel = new GetAccountsByServicesResponseModel();

        List<AccountsForServiceModel> accountsForServiceList = new ArrayList<AccountsForServiceModel>();
        if (json == null) {
            
            LogManager.d("ParseInsertRechargeCardResponse"+"json null!");
            return null;
        }

        try {
            JSONObject jsonObj = new JSONObject(json);
            JSONObject getAccountbySercicesObj = jsonObj.getJSONObject("GetAccountsByServicesResponse");

            responsePublicModel.responsePublicModel.setResultCode(getAccountbySercicesObj .optInt("resultCode"));
            responsePublicModel.responsePublicModel.setResultDescription(getAccountbySercicesObj .optString("resultDescription"));
            JSONObject eventManagementObj = getAccountbySercicesObj.optJSONObject("eventManagement");
            if(eventManagementObj!=null){
            	responsePublicModel.responsePublicModel.eventManagement
            	.setErrorCode(eventManagementObj.optString("errorCode"));
            	responsePublicModel.responsePublicModel.eventManagement
            	.setErrorDescription(eventManagementObj.optString("errorDescription"));
            }

            responsePublicModel.responsePublicModel.setTransactionId(getAccountbySercicesObj.optString("transactionId"));
            
            responsePublicModel.setEffectiveDate(TimeUtil.getTimeByString(getAccountbySercicesObj .optString("effectiveDate"), TimeUtil.dateFormat2a));

            JSONArray accountsForServiceArray = getAccountbySercicesObj.optJSONArray("accountsForServiceList");
            if(accountsForServiceArray!=null){
	            for (int i = 0; i < accountsForServiceArray.length(); i++) {
	                AccountsForServiceModel accountsForService = new AccountsForServiceModel();
	                JSONObject mJSONObject=accountsForServiceArray.optJSONObject(i);
	                if(mJSONObject!=null){
	                	accountsForService.setServiceCode(mJSONObject.optString("serviceCode"));
	
		                List<AccountsModel> accountsList = new ArrayList<AccountsModel>();
		                JSONArray accountsArray = mJSONObject.getJSONArray("accounts");
		                if(accountsArray!=null){
			                for (int j = 0; j < accountsArray.length(); j++) {
			                	JSONObject mAccountsModelJSONObject=accountsArray.optJSONObject(j);
			                	if(mAccountsModelJSONObject!=null){
			                		AccountsModel account = new AccountsModel();
			                    
			                		account.setAccountId(mAccountsModelJSONObject.optString("accountId"));
			                		account.setAccountCode(mAccountsModelJSONObject.optString("accountCode"));
			                		account.setIbanCode(mAccountsModelJSONObject.optString("ibanCode"));
			                		account.setIsInformative(mAccountsModelJSONObject.optString("isInformative"));
			                		account.setAccountAlias(mAccountsModelJSONObject.optString("accountAlias"));
			                		account.setBranchCode(mAccountsModelJSONObject.optString("branchCode"));
			                		account.setBankCode(mAccountsModelJSONObject.optString("bankCode"));
			
			                		BankServiceType bankServiceType = new BankServiceType();
			                		JSONObject bankServiceTypeObj = mAccountsModelJSONObject.optJSONObject("bankServiceType");
			                		if(bankServiceTypeObj!=null){
			                			bankServiceType.setBankServiceCode(bankServiceTypeObj.optString("bankServiceCode"));
			                			bankServiceType.setServiceCategory(bankServiceTypeObj.optString("serviceCategory"));
			                			bankServiceType.setSubCategory(bankServiceTypeObj.optString("subCategory"));
			                		}
			                		account.setBankServiceType(bankServiceType);
			
			                		account.setCurrencyAcc(mAccountsModelJSONObject.optString("currencyAcc"));
			                		account.setFrontendCategory(mAccountsModelJSONObject.optString("frontendCategory"));
			                		account.setMortageType(mAccountsModelJSONObject.optString("mortageType"));
			                		account.setCabCode(mAccountsModelJSONObject.optString("cabCode"));
			                		account.setNotes(mAccountsModelJSONObject.optString("notes"));
			                		account.setOpeningDate(mAccountsModelJSONObject.optString("openingDate"));
			                		account.setProdAccount(mAccountsModelJSONObject.optString("prodAccount"));
			                		account.setProdAccountDesc(mAccountsModelJSONObject.optString("prodAccountDesc"));
			                		account.setPreferred(mAccountsModelJSONObject.optString("preferred"));
			                		account.setAccountBalance(mAccountsModelJSONObject.optDouble("accountBalance"));
			                		account.setAvailableBalance(mAccountsModelJSONObject.optDouble("availableBalance"));
			                		account.setAccountType(mAccountsModelJSONObject.optString("accountType"));
			                		account.setRelatedNdg(mAccountsModelJSONObject.optString("relatedNdg"));
			                		account.setNdg(mAccountsModelJSONObject.optString("ndg"));
			                		account.setOverrideCode(mAccountsModelJSONObject.optString("overrideCode"));
			                		account.setOverrideIsInfo(mAccountsModelJSONObject.optString("overrideIsInfo"));
			                		account.setOverridePerim(mAccountsModelJSONObject.optString("overridePerim"));
			                		account.setCardExpirationDate(mAccountsModelJSONObject.optString("cardExpirationDate"));
			                		account.setIsPreferred(mAccountsModelJSONObject.optBoolean("isPreferred"));
			                		accountsList.add(account);
			                	}
			                }
		                }
		                accountsForService.setAccounts(accountsList);
	                }
	                accountsForServiceList.add(accountsForService);
	            }
            }
            responsePublicModel.setAccountsForServiceList(accountsForServiceList);

        } catch (JSONException e) {
            e.printStackTrace();
            LogManager.e("ParseInsertRechargeCardResponse is error " + e);
        }
        
        return responsePublicModel;
    }
}
