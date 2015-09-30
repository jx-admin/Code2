
package com.accenture.mbank.logic;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.accenture.mbank.model.AmountAvailable;
import com.accenture.mbank.model.CompanyAmountResponseModel;
import com.accenture.mbank.model.HelpItemListModel;
import com.accenture.mbank.model.RequestPublicModel;
import com.accenture.mbank.util.LogManager;
import com.accenture.mbank.util.ServiceType;

public class CompanyAmountJson {

    /**
     * 
     * @param accountCode
     * @param destProvider
     * @param phoneNumber
     * @param publicModel
     * @return
     */
    public static String CompanyAmountReportProtocal(String accountCode, String destProvider,
            String phoneNumber, RequestPublicModel publicModel) {
        String result = null;
        try {
            JSONObject jsonObj = new JSONObject();
            JSONObject getCompanyAmountObj = new JSONObject();
            getCompanyAmountObj.put("bankName", publicModel.getBankName());
            getCompanyAmountObj.put("serviceType", ServiceType.getCompanyAmount);
            getCompanyAmountObj.put("enterpriseId", publicModel.getEnterpriseId());
            getCompanyAmountObj.put("customerCode", publicModel.getCustomerCode());
            getCompanyAmountObj.put("channel", publicModel.getChannel());
            getCompanyAmountObj.put("userAgent", publicModel.getUserAgent());
            getCompanyAmountObj.put("token", publicModel.getToken());
            getCompanyAmountObj.put("sessionId", publicModel.getSessionId());
            getCompanyAmountObj.put("accountCode", accountCode);
            getCompanyAmountObj.put("destProvider", destProvider);
            getCompanyAmountObj.put("phoneNumber", phoneNumber);
            jsonObj.put("GetCompanyAmountRequest", getCompanyAmountObj);
            result = jsonObj.toString();
        } catch (Exception e) {
            LogManager.e("CompanyAmountReportProtocal is error " + e.getLocalizedMessage());
        }
        return result;
    }

    
    public static CompanyAmountResponseModel ParseCompanyAmountResponse(String json) {

        CompanyAmountResponseModel companyAmountResponse = new CompanyAmountResponseModel();
         List<AmountAvailable> amountAvailables = new ArrayList<AmountAvailable>();
         
         if (json == null) {
             return null;
         }
         
         try {
             JSONObject jsonObj = new JSONObject(json);
             JSONObject companyAmountObj = jsonObj.getJSONObject("GetCompanyAmountResponse");
             
             companyAmountResponse.responsePublicModel.setResultCode(companyAmountObj.optInt("resultCode"));
             companyAmountResponse.responsePublicModel.setResultDescription(companyAmountObj.optString("resultDescription"));
             if (companyAmountResponse.responsePublicModel.getResultCode() != 0) {
                 JSONObject eventManagementObj = companyAmountObj.getJSONObject("eventManagement");
                 companyAmountResponse.responsePublicModel.eventManagement.setErrorCode(eventManagementObj.optString("errorCode"));
                 companyAmountResponse.responsePublicModel.eventManagement.setErrorDescription(eventManagementObj.optString("errorDescription"));
                 return companyAmountResponse;
             }
             
             companyAmountResponse.responsePublicModel.setTransactionId(companyAmountObj.optString("transactionId"));
             
             JSONArray amountAvailableArray = companyAmountObj.getJSONArray("amountAvailable");
             for(int i=0;i<amountAvailableArray.length();i++){
                 AmountAvailable amountAvailable = new AmountAvailable();
                 amountAvailable.setCommissionAmount(amountAvailableArray.getJSONObject(i).optInt("commissionAmount"));
                 amountAvailable.setRechargeAmount(amountAvailableArray.getJSONObject(i).optInt("rechargeAmount"));
                 amountAvailable.setDescription(amountAvailableArray.getJSONObject(i).optString("description"));
                 amountAvailables.add(amountAvailable);
             }
             companyAmountResponse.setAmountAvailable(amountAvailables);
         }catch(Exception e){
             LogManager.e("ParseCompanyAmountResponse is error" + e.getLocalizedMessage());
         }
         return companyAmountResponse;
    }
    
    
}
