
package com.accenture.mbank.logic;

import org.json.JSONObject;

import com.accenture.mbank.model.CheckRechargeCardResponseModel;
import com.accenture.mbank.model.RequestPublicModel;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.LogManager;
import com.accenture.mbank.util.ServiceType;

public class CheckRechargeCardJson {
    /**
     * 
     * @param accountCode
     * @param publicModel
     * @param amount
     * @param purposeDescription
     * @param destCardCode
     * @param cardNumber
     * @param title
     * @param name
     * @return
     */
    public static String CheckRechargeCardReportProtocal(String accountCode,
            RequestPublicModel publicModel, double amount, String purposeDescription,
            String destCardCode, String cardNumber, String title, String name,String currency,String transactionId) {
        String result = null;
        try {
            JSONObject jsonObj = new JSONObject();
            JSONObject checkRechargeCardObj = new JSONObject();
            checkRechargeCardObj.put("serviceType", ServiceType.checkRechargeCard);
            checkRechargeCardObj.put("bankName", publicModel.getBankName());
            checkRechargeCardObj.put("enterpriseId", publicModel.getEnterpriseId());
            checkRechargeCardObj.put("customerCode", publicModel.getCustomerCode());
            checkRechargeCardObj.put("channel", publicModel.getChannel());
            checkRechargeCardObj.put("userAgent", publicModel.getUserAgent());
            checkRechargeCardObj.put("sessionId", publicModel.getSessionId());
            checkRechargeCardObj.put("token", publicModel.getToken());
            checkRechargeCardObj.put("amount", amount);
            checkRechargeCardObj.put("purposeDescription", purposeDescription);
            checkRechargeCardObj.put("destCardCode", destCardCode);
            checkRechargeCardObj.put("cardNumber", cardNumber);
            checkRechargeCardObj.put("title", title);
            checkRechargeCardObj.put("name", name);
            checkRechargeCardObj.put("accountCode", accountCode);
            checkRechargeCardObj.put("currency", currency);
            if(Contants.isNewPaymentsUpdate){
            	checkRechargeCardObj.put("transferCheck", true);
            	checkRechargeCardObj.put("transactionId", transactionId);
            }

            jsonObj.put("CheckRechargeCardRequest", checkRechargeCardObj);
            result = jsonObj.toString();
        } catch (Exception e) {
            LogManager.e("CheckRechargeCardReportProtocal is error" + e.getLocalizedMessage());
        }
        return result;
    }
    
    
    /**
     * 
     * @param json
     * @return
     */
    public static CheckRechargeCardResponseModel ParseCheckRechargeCardResponse(String json) {
        CheckRechargeCardResponseModel checkRechargeCardResponse = new CheckRechargeCardResponseModel();
        if (json == null) {
            return null;
        }
        
        try {
            JSONObject jsonObj = new JSONObject(json);
            JSONObject checkRechargeCardUpObj = jsonObj.getJSONObject("CheckRechargeCardResponse");
            
            checkRechargeCardResponse.responsePublicModel.setResultCode(checkRechargeCardUpObj.getInt("resultCode"));
            checkRechargeCardResponse.responsePublicModel.setResultDescription(checkRechargeCardUpObj.optString("resultDescription"));
            if (checkRechargeCardResponse.responsePublicModel.getResultCode() != 0) {
                JSONObject eventManagementObj = checkRechargeCardUpObj.getJSONObject("eventManagement");
                checkRechargeCardResponse.responsePublicModel.eventManagement.setErrorCode(eventManagementObj.optString("errorCode"));
                checkRechargeCardResponse.responsePublicModel.eventManagement.setErrorDescription(eventManagementObj.optString("errorDescription"));
                return checkRechargeCardResponse;
            }
            checkRechargeCardResponse.responsePublicModel.setTransactionId(checkRechargeCardUpObj.optString("transactionId"));

            if(Contants.isNewPaymentsUpdate){
            	checkRechargeCardResponse.setTransferId(checkRechargeCardUpObj.optString("transferId"));
            }
            checkRechargeCardResponse.setCharges(checkRechargeCardUpObj.optDouble("charges"));
            checkRechargeCardResponse.setExecutionDate(checkRechargeCardUpObj.optString("executionDate"));
            checkRechargeCardResponse.setSrcbankName(checkRechargeCardUpObj.optString("srcbankName"));
            checkRechargeCardResponse.setSrcBranchName(checkRechargeCardUpObj.optString("srcBranchName"));
            
            
        }catch(Exception e){
            LogManager.e("ParseCheckRechargeCardResponse is error" + e.getLocalizedMessage());
        }
        return checkRechargeCardResponse;
    }

}
