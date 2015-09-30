
package com.act.mbanking.logic;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.act.mbanking.ServiceType;
import com.act.mbanking.bean.BankRecipient;
import com.act.mbanking.bean.CardRecipient;
import com.act.mbanking.bean.EntryModel;
import com.act.mbanking.bean.PhoneRecipient;
import com.act.mbanking.bean.RequestPublicModel;
import com.act.mbanking.bean.ResponsePublicModel;
import com.act.mbanking.utils.LogManager;

public class InsertRecipientJson {

    public static final String BANK = "BANK";

    public static final String CARD = "CARD";

    public static final String PHONE = "PHONE";

    public static String InsertRecipientReportProtocal(RequestPublicModel publicModel,
            String recipientType, Object data) {
        String result = null;
        try {
            JSONObject jsonObj = new JSONObject();
            JSONObject insertRecipientObj = new JSONObject();
            insertRecipientObj.put("serviceType", ServiceType.insertRecipient);
            insertRecipientObj.put("bankName", publicModel.getBankName());
            insertRecipientObj.put("enterpriseId", publicModel.getEnterpriseId());
            insertRecipientObj.put("customerCode", publicModel.getCustomerCode());
            insertRecipientObj.put("channel", publicModel.getChannel());
            insertRecipientObj.put("userAgent", publicModel.getUserAgent());
            insertRecipientObj.put("sessionId", publicModel.getSessionId());
            insertRecipientObj.put("token", publicModel.getToken());
            insertRecipientObj.put("recipientType", recipientType);
            if(data!=null){
            if (recipientType.equals(BANK)) {
                BankRecipient bankRecipient=(BankRecipient)data;
                JSONObject bankRecipientObj = new JSONObject();
                JSONObject bankObj = new JSONObject();
                bankObj.put("ibanCode", bankRecipient.getIbanCode());
                bankObj.put("name", bankRecipient.getName());
                bankRecipientObj.put("bank", bankObj);
                insertRecipientObj.put("bankRecipient", bankRecipientObj);
            } else if (recipientType.equals(CARD)) {
                CardRecipient cardRecipient=(CardRecipient)data;
                JSONObject cardRecipientObj = new JSONObject();
                JSONObject cardObj = new JSONObject();
                cardObj.put("cardNumber", cardRecipient.getCardNumber());
                cardObj.put("name", cardRecipient.getName());
                cardRecipientObj.put("card", cardObj);
                insertRecipientObj.put("cardRecipient", cardRecipientObj);
            } else if (recipientType.equals(PHONE)) {
                PhoneRecipient phoneRecipient=(PhoneRecipient)data;
                JSONObject phoneRecipientObj = new JSONObject();
                JSONObject phoneObj = new JSONObject();
                phoneObj.put("phoneNumber", phoneRecipient.getPhoneNumber());
                phoneObj.put("name", phoneRecipient.getName());
                phoneObj.put("provider", phoneRecipient.getProvider());
                phoneRecipientObj.put("phone", phoneObj);
                insertRecipientObj.put("phoneRecipient", phoneRecipientObj);
            }
            }
            jsonObj.put("InsertRecipientRequest", insertRecipientObj);
            result = jsonObj.toString();
        } catch (Exception e) {
            LogManager.e("InsertRecipientReportProtocal is error" + e.getLocalizedMessage());
        }
        return result;
    }

    public static ResponsePublicModel ParseInsertRecipientResponse(String json) {
        ResponsePublicModel responsePublicModel = new ResponsePublicModel();
        EntryModel entryModel = new EntryModel();
        List<Object> fieldMap = new ArrayList<Object>();
        if (json == null) {
            return null;
        }

        try {
            JSONObject jsonObj = new JSONObject(json);
            JSONObject insertRecipientObj = jsonObj.getJSONObject("InsertRecipientResponse");

            responsePublicModel.setResultCode(insertRecipientObj.getInt("resultCode"));
            responsePublicModel.setResultDescription(insertRecipientObj
                    .optString("resultDescription"));
            if (responsePublicModel.getResultCode() != 0) {
                JSONObject eventManagementObj = insertRecipientObj.getJSONObject("eventManagement");
                responsePublicModel.eventManagement.setErrorCode(eventManagementObj
                        .optString("errorCode"));
                responsePublicModel.eventManagement.setErrorDescription(eventManagementObj
                        .optString("errorDescription"));
                return responsePublicModel;
            }

            responsePublicModel.setTransactionId(insertRecipientObj.optString("transactionId"));

            JSONObject fieldMapObj = insertRecipientObj.getJSONObject("fieldMap");
            JSONArray entryArray = fieldMapObj.getJSONArray("entry");
            for (int i = 0; i < entryArray.length(); i++) {
                entryModel.setKey(entryArray.getJSONObject(i).optString("key"));
                entryModel.setValue(entryArray.getJSONObject(i).optString("value"));
                fieldMap.add(entryModel);
            }
            responsePublicModel.setFieldMap(fieldMap);
        } catch (JSONException e) {
            LogManager.e("ParseInsertRecipientResponse is error " + e.getLocalizedMessage());
        }
        return responsePublicModel;

    }

}
