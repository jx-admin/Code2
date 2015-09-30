
package com.accenture.mbank.logic;

import org.json.JSONObject;

import android.text.TextUtils;

import com.accenture.mbank.model.BankInformationModel;
import com.accenture.mbank.model.CheckTransferResponseModel;
import com.accenture.mbank.model.DestaccountModel;
import com.accenture.mbank.model.RequestPublicModel;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.LogManager;
import com.accenture.mbank.util.ServiceType;

public class CheckTransferJson {

    public static CheckTransferResponseModel ParseCheckTransferResponse(String json) {
        CheckTransferResponseModel checkTransferResponse = new CheckTransferResponseModel();
        BankInformationModel bankInformationModel = new BankInformationModel();
        if (json == null) {
            return null;
        }

        try {
            JSONObject jsonObj = new JSONObject(json);
            JSONObject checkTransferResponseObj = jsonObj.optJSONObject("CheckTransferResponse");
            checkTransferResponse.setResultCode(checkTransferResponseObj.optInt("resultCode"));
            checkTransferResponse.setResultDescription(checkTransferResponseObj.optString("resultDescription"));
            JSONObject bankInformationObj = checkTransferResponseObj.optJSONObject("bankInformation");
            if(bankInformationObj!=null){
            bankInformationModel.setSrcBankName(bankInformationObj.optString("srcBankName"));
            bankInformationModel.setSrcBranchName(bankInformationObj.optString("srcBranchName"));
            bankInformationModel.setDestBankName(bankInformationObj.optString("destBankName"));
            bankInformationModel.setDestBranchName(bankInformationObj.optString("destBranchName"));
            }
            checkTransferResponse.setBankInfomation(bankInformationModel);
            checkTransferResponse.setTransferId(checkTransferResponseObj.optString("transferId"));
            checkTransferResponse.setCharges(checkTransferResponseObj.optDouble("charges"));
//          if(checkTransferResponse.optResultCode() != 0){
            JSONObject eventManagementObj = checkTransferResponseObj.optJSONObject("eventManagement");
            if(eventManagementObj!=null){
            checkTransferResponse.responsePublicModel.eventManagement.setErrorCode(eventManagementObj.optString("errorCode"));
            checkTransferResponse.responsePublicModel.eventManagement.setErrorDescription(eventManagementObj.optString("errorDescription"));
            }
//            return checkTransferResponse;
//        }
            checkTransferResponse.setOtpByEmail(checkTransferResponseObj.optBoolean("otpByEmail"));
            
        } catch (Exception e) {
            LogManager.e("ParseCheckTransfer is error " + e.getLocalizedMessage());
        }
        return checkTransferResponse;
    }

    public static String CheckTransferReportProtocal(String accountCode, double amount,
            String purposecurrency, String transferType, String purposeDescription,
            String executionDate, DestaccountModel destaccount, RequestPublicModel publicModel,String transactionId) {
        String resultStr = null;
        try {
            JSONObject jsonObj = new JSONObject();
            JSONObject checkTransferObj = new JSONObject();

            checkTransferObj.put("serviceType", ServiceType.checkTransfer);
            checkTransferObj.put("bankName", publicModel.getBankName());
            checkTransferObj.put("enterpriseId", publicModel.getEnterpriseId());
            checkTransferObj.put("accountCode", accountCode);
            checkTransferObj.put("amount", amount);
            if(!TextUtils.isEmpty(purposecurrency)){
            	checkTransferObj.put("purposeCurrency", purposecurrency);
            }
            checkTransferObj.put("transferType", transferType);
            checkTransferObj.put("purposeDescription", purposeDescription);
            checkTransferObj.put("executionDate", executionDate);
            checkTransferObj.put("sessionId", publicModel.getSessionId());
            checkTransferObj.put("token", publicModel.getToken());
            
            if(destaccount!=null){
            	JSONObject destaccountObj = destaccount.getProtocol();
            	if(destaccountObj!=null){
            		checkTransferObj.put("destAccount", destaccountObj);
            	}
            }

            checkTransferObj.put("customerNumber", publicModel.getCustomerNumber());
            checkTransferObj.put("channel", publicModel.getChannel());
            checkTransferObj.put("userAgent", publicModel.getUserAgent());
//            checkTransferObj.put("currency","1000");
            checkTransferObj.put("transactionId", transactionId);

            jsonObj.put("CheckTransferRequest", checkTransferObj);
            resultStr = jsonObj.toString();

            LogManager.d("CheckTransferRequest  :" + resultStr);
        } catch (Exception e) {
            LogManager.e("CheckTransferReportProtocal is error" + e.getLocalizedMessage());
        }
        return resultStr;
    }
}
