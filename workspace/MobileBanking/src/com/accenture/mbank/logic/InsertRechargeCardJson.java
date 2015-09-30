
package com.accenture.mbank.logic;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.accenture.mbank.model.CheckRechargeCardResponseModel;
import com.accenture.mbank.model.EntryModel;
import com.accenture.mbank.model.RequestPublicModel;
import com.accenture.mbank.model.ResponsePublicModel;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.LogManager;
import com.accenture.mbank.util.ServiceType;

public class InsertRechargeCardJson {
    /**
     * @param publicModel
     * @param accountCode
     * @param amount
     * @param destCardCode
     * @param cardNumber
     * @param purposeDescription
     * @param name
     * @param title
     * @param otpValue
     * @param otpKeySession
     * @param otpChannel
     * @param checkRechargeCard 
     * @return
     */
    public static String InsertRechargeCardReportProtocal(RequestPublicModel publicModel,
            String accountCode, double amount, String destCardCode, String cardNumber,
            String purposeDescription, String name, String title, String otpValue,
            String otpKeySession, String otpChannel,String currency, CheckRechargeCardResponseModel checkRechargeCard) {
        String result = null;
        try {
            JSONObject jsonObj = new JSONObject();
            JSONObject insertRechargeCardObj = new JSONObject();
            insertRechargeCardObj.put("serviceType", ServiceType.insertRechargeCard);
            insertRechargeCardObj.put("bankName", publicModel.getBankName());
            insertRechargeCardObj.put("enterpriseId", publicModel.getEnterpriseId());
            insertRechargeCardObj.put("customerCode", publicModel.getCustomerCode());
            insertRechargeCardObj.put("channel", publicModel.getChannel());
            insertRechargeCardObj.put("userAgent", publicModel.getUserAgent());
            insertRechargeCardObj.put("sessionId", publicModel.getSessionId());
            insertRechargeCardObj.put("token", publicModel.getToken());
            insertRechargeCardObj.put("accountCode", accountCode);
            insertRechargeCardObj.put("amount", amount);
            insertRechargeCardObj.put("purposeDescription", purposeDescription);
            insertRechargeCardObj.put("destCardCode", destCardCode);
            insertRechargeCardObj.put("cardNumber", cardNumber);
            insertRechargeCardObj.put("name", name);
            insertRechargeCardObj.put("title", title);
            insertRechargeCardObj.put("currency", currency);
            if(Contants.isNewPaymentsUpdate){
            	insertRechargeCardObj.put("transferCheck", true);
            	insertRechargeCardObj.put("transferId", checkRechargeCard.getTransferId());
            }

            insertRechargeCardObj.put("otpValue", otpValue);
            insertRechargeCardObj.put("otpKeySession", otpKeySession);
            insertRechargeCardObj.put("otpChannel", otpChannel);

            jsonObj.put("InsertRechargeCardRequest", insertRechargeCardObj);
            result = jsonObj.toString();
        } catch (Exception e) {
            LogManager.e("InsertRechargeCardReportProtocal is error " + e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * @param json
     * @return
     */
    public static ResponsePublicModel ParseInsertRechargeCardResponse(String json) {
        ResponsePublicModel responsePublicModel = new ResponsePublicModel();
        EntryModel entryModel = new EntryModel();
        List<Object> fieldMap = new ArrayList<Object>();
        if (json == null) {
            return null;
        }

        try {
            JSONObject jsonObj = new JSONObject(json);
            JSONObject insertTransferObj = jsonObj.getJSONObject("InsertRechargeCardResponse");

            responsePublicModel.setResultCode(insertTransferObj.getInt("resultCode"));
            responsePublicModel.setResultDescription(insertTransferObj
                    .optString("resultDescription"));
            if (responsePublicModel.getResultCode() != 0) {
                JSONObject eventManagementObj = insertTransferObj.getJSONObject("eventManagement");
                responsePublicModel.eventManagement.setErrorCode(eventManagementObj
                        .optString("errorCode"));
                responsePublicModel.eventManagement.setErrorDescription(eventManagementObj
                        .optString("errorDescription"));
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
            LogManager.e("ParseInsertRechargeCardResponse is error " + e.getLocalizedMessage());
        }
        return responsePublicModel;
    }

}
