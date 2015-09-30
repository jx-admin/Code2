
package com.act.mbanking.logic;

import org.json.JSONObject;

import com.act.mbanking.App;
import com.act.mbanking.ServiceType;
import com.act.mbanking.bean.BankInformationModel;
import com.act.mbanking.bean.CheckTransferResponseModel;
import com.act.mbanking.bean.DestaccountModel;
import com.act.mbanking.bean.RequestPublicModel;
import com.act.mbanking.utils.LogManager;

public class CheckTransferJson {

    public static CheckTransferResponseModel ParseCheckTransferResponse(String json) {
        CheckTransferResponseModel checkTransferResponse = new CheckTransferResponseModel();
        BankInformationModel bankInformationModel = new BankInformationModel();
        if (json == null) {
            return null;
        }

        try {
            JSONObject jsonObj = new JSONObject(json);
            JSONObject checkTransferResponseObj = jsonObj.getJSONObject("CheckTransferResponse");
            checkTransferResponse.setResultCode(checkTransferResponseObj.getInt("resultCode"));
            checkTransferResponse.setResultDescription(checkTransferResponseObj.optString("resultDescription"));
//            if(checkTransferResponse.getResultCode() != 0){
                JSONObject eventManagementObj = checkTransferResponseObj.getJSONObject("eventManagement");
                checkTransferResponse.responsePublicModel.eventManagement.setErrorCode(eventManagementObj.optString("errorCode"));
                checkTransferResponse.responsePublicModel.eventManagement.setErrorDescription(eventManagementObj.optString("errorDescription"));
//                return checkTransferResponse;
//            }
            
            JSONObject bankInformationObj = checkTransferResponseObj.getJSONObject("bankInformation");
            bankInformationModel.setSrcBankName(bankInformationObj.optString("srcBankName"));
            bankInformationModel.setSrcBranchName(bankInformationObj.optString("srcBranchName"));
            bankInformationModel.setDestBankName(bankInformationObj.optString("destBankName"));
            bankInformationModel.setDestBranchName(bankInformationObj.optString("destBranchName"));
            checkTransferResponse.setBankInfomation(bankInformationModel);
            if(App.isNewPaymentsUpdate){
                checkTransferResponse.setTransferId(checkTransferResponseObj.optString("transferId"));
            }
            checkTransferResponse.setCharges(checkTransferResponseObj.optDouble("charges"));
            
        } catch (Exception e) {
            LogManager.e("ParseCheckTransfer is error " + e.getLocalizedMessage());
        }
        return checkTransferResponse;
    }

    public static String CheckTransferReportProtocal(String accountCode, double amount,
            String purposecurrency, String transferType, String purposeDescription,
            String executionDate, DestaccountModel destaccount, RequestPublicModel publicModel,String currency,String transactionId) {
        String resultStr = null;
        try {
            JSONObject jsonObj = new JSONObject();
            JSONObject checkTransferObj = new JSONObject();

            checkTransferObj.put("serviceType", ServiceType.checkTransfer);
            checkTransferObj.put("bankName", publicModel.getBankName());
            checkTransferObj.put("enterpriseId", publicModel.getEnterpriseId());
            checkTransferObj.put("accountCode", accountCode);
            checkTransferObj.put("amount", amount);
            checkTransferObj.put("purposecurrency", purposecurrency);
            checkTransferObj.put("transferType", transferType);
            checkTransferObj.put("purposeDescription", purposeDescription);
            checkTransferObj.put("executionDate", executionDate);
            checkTransferObj.put("sessionId", publicModel.getSessionId());
            checkTransferObj.put("token", publicModel.getToken());
            
            JSONObject destaccountObj = new JSONObject();
            destaccountObj.put("title", destaccount.getTitle());
            destaccountObj.put("state", destaccount.getState());
            destaccountObj.put("iban", destaccount.getIban());
            destaccountObj.put("bic", destaccount.getBic());
            
            checkTransferObj.put("destAccount", destaccountObj);

            checkTransferObj.put("customerCode", publicModel.getCustomerCode());
            checkTransferObj.put("channel", publicModel.getChannel());
            checkTransferObj.put("userAgent", publicModel.getUserAgent());
            checkTransferObj.put("currency",currency);
            if(App.isNewPaymentsUpdate){
            	checkTransferObj.put("transferCheck", true);
            	checkTransferObj.put("transactionId", transactionId);
            }

            jsonObj.put("CheckTransferRequest", checkTransferObj);
            resultStr = jsonObj.toString();

            LogManager.d("CheckTransferRequest  :" + resultStr);
        } catch (Exception e) {
            LogManager.e("CheckTransferReportProtocal is error" + e.getLocalizedMessage());
        }
        return resultStr;
    }
}
