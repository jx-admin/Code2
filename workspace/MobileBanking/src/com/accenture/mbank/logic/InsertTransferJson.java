
package com.accenture.mbank.logic;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.accenture.mbank.model.CheckTransferResponseModel;
import com.accenture.mbank.model.DestaccountModel;
import com.accenture.mbank.model.EntryModel;
import com.accenture.mbank.model.RequestPublicModel;
import com.accenture.mbank.model.ResponsePublicModel;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.LogManager;
import com.accenture.mbank.util.ServiceType;

public class InsertTransferJson {

    public static String InsertTransferReportProtocal(RequestPublicModel publicModel,
            String accountCode, double amount, String puroseCurrency, String transferType,
            String purposeDescription, String executionDate, DestaccountModel destAccount,
            String otpValue, String otpKeySession, String otpChannel,String currency,CheckTransferResponseModel checkTransfer) {
        String result = null;
        try {
            JSONObject jsonObj = new JSONObject();
            JSONObject insertTransferObj = new JSONObject();
            JSONObject destAccountObj = new JSONObject();
            insertTransferObj.put("serviceType", ServiceType.insertTransfer);
            insertTransferObj.put("bankName", publicModel.getBankName());
            insertTransferObj.put("enterpriseId", publicModel.getEnterpriseId());
            insertTransferObj.put("token", publicModel.getToken());
            insertTransferObj.put("customerCode", publicModel.getCustomerCode());
            insertTransferObj.put("channel", publicModel.getChannel());
            insertTransferObj.put("userAgent", publicModel.getUserAgent());
            insertTransferObj.put("sessionId",publicModel.getSessionId());
            insertTransferObj.put("currency", currency);
            if(Contants.isNewPaymentsUpdate){
                insertTransferObj.put("transferCheck", true);
                insertTransferObj.put("transferId", checkTransfer.getTransferId());
            }
            
            insertTransferObj.put("accountCode", accountCode);
            insertTransferObj.put("transferType", transferType);
            insertTransferObj.put("amount", amount);
            insertTransferObj.put("executionDate", executionDate);
            insertTransferObj.put("purposeCurrency", puroseCurrency);
            insertTransferObj.put("purposeDescription", purposeDescription);
            insertTransferObj.put("otpValue", otpValue);
            insertTransferObj.put("otpKeySession", otpKeySession);
            insertTransferObj.put("otpChannel", otpChannel);

            destAccountObj.put("title", destAccount.getTitle());
            destAccountObj.put("state", destAccount.getState());
            destAccountObj.put("iban", destAccount.getIban());
            destAccountObj.put("bic", destAccount.getBic());
            
            insertTransferObj.put("destAccount", destAccountObj);
            jsonObj.put("InsertTransferRequest", insertTransferObj);
            result = jsonObj.toString();
        } catch (Exception e) {
            LogManager.e("InsertTransferReportProtocal is error " + e.getLocalizedMessage());
        }
        return result;
    }

    public static ResponsePublicModel ParseInsertTransferResponse(String json) {
        ResponsePublicModel responsePublicModel = new ResponsePublicModel();
        EntryModel entryModel = new EntryModel();
        List<Object> fieldMap = new ArrayList<Object>();
        if (json == null) {
            return null;
        }

        try {
            JSONObject jsonObj = new JSONObject(json);
            JSONObject insertTransferObj = jsonObj.getJSONObject("InsertTransferResponse");

            responsePublicModel.setResultCode(insertTransferObj.getInt("resultCode"));
            responsePublicModel.setResultDescription(insertTransferObj.optString("resultDescription"));
            if (responsePublicModel.getResultCode() != 0) {
                JSONObject eventManagementObj = insertTransferObj.getJSONObject("eventManagement");
                responsePublicModel.eventManagement.setErrorCode(eventManagementObj.optString("errorCode"));
                responsePublicModel.eventManagement.setErrorDescription(eventManagementObj.optString("errorDescription"));
                return responsePublicModel;
            }
            
            responsePublicModel.setTransactionId(insertTransferObj.optString("transactionId"));

            JSONObject fieldMapObj = insertTransferObj.getJSONObject("fieldMap");
            JSONArray entryArray = fieldMapObj.getJSONArray("entry");
            for (int i = 0; i < entryArray.length(); i++) {
                entryModel.setKey(entryArray.getJSONObject(i).optString("key"));
                entryModel.setValue(entryArray.getJSONObject(i).optString("value"));
                fieldMap.add(entryModel);
            }
            responsePublicModel.setFieldMap(fieldMap);
        } catch (JSONException e) {
            LogManager.e("ParseLoginResponse is error " + e.getLocalizedMessage());
        }
        return responsePublicModel;
    }
}
