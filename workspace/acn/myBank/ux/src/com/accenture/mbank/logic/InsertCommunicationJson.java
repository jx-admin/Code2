
package com.accenture.mbank.logic;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.accenture.mbank.model.EntryModel;
import com.accenture.mbank.model.RequestPublicModel;
import com.accenture.mbank.model.ResponsePublicModel;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.LogManager;
import com.accenture.mbank.util.ServiceType;

public class InsertCommunicationJson {

    public static final String CONTACT_MODE_EMAIL = "email";

    public static final String CONTACT_MODE_PHONE = "phone";

    public static final String CONTACT_MODE_BRANCH = "branch";
    
    public static final String BRANCH_PHONE = "Mail";
    
    public static final String BRANCH_EMAIL = "Phone";

    public static class UserInfo {
        public String user_name;

        public String user_email;

        public String user_phone;

        public UserInfo() {

        }
    }

    public static String InsertCommunicationProtocol(final RequestPublicModel publicModel,
            final String contactMode, final String topicOfInterest, final String description,
            final UserInfo userInfo, final String branch,
            final String contactTime, final String contactDate) {

        try {
            JSONObject jsonObj = new JSONObject();
            JSONObject insertCommunicationObj = new JSONObject();
            JSONObject userinfoObj = new JSONObject();
            insertCommunicationObj.put("serviceType", ServiceType.insertCommunication);
            insertCommunicationObj.put("enterpriseId", publicModel.getEnterpriseId());
            insertCommunicationObj.put("bankName", publicModel.getBankName());
            insertCommunicationObj.put("token", publicModel.getToken());
            insertCommunicationObj.put("customerCode", publicModel.getCustomerCode());
            insertCommunicationObj.put("channel", publicModel.getChannel());
            insertCommunicationObj.put("userAgent", publicModel.getUserAgent());
            insertCommunicationObj.put("sessionId", publicModel.getSessionId());

            insertCommunicationObj.put("contactMode", contactMode);
            insertCommunicationObj.put("topicOfInterest", topicOfInterest);
            
            insertCommunicationObj.put("description", description);
            insertCommunicationObj.put("ndgCode", Contants.getUserInfo.getNdgHBCode());
            userinfoObj.put("name", userInfo.user_name);
            if (contactMode.equals(CONTACT_MODE_EMAIL) || contactMode.equals(CONTACT_MODE_BRANCH)) {
                userinfoObj.put("email", userInfo.user_email);
            }

            if (contactMode.equals(CONTACT_MODE_PHONE) || contactMode.equals(CONTACT_MODE_BRANCH)) {
                userinfoObj.put("mobilePhoneNumber", userInfo.user_phone);
                insertCommunicationObj.put("contactTime", contactTime);
            }

            if (contactMode.equals(CONTACT_MODE_BRANCH)) {
                insertCommunicationObj.put("branch", branch);
                insertCommunicationObj.put("contactDate", contactDate);
            }
            
            insertCommunicationObj.put("userInfo", userinfoObj);
            jsonObj.put("InsertCommunicationRequest", insertCommunicationObj);
            return jsonObj.toString();
        } catch (Exception e) {
            LogManager.e("InsertCommunicationJson: " + e.getLocalizedMessage());
        }
        return null;
    }

    public static ResponsePublicModel ParseInsertCommunicationResponse(final String jsonString) {
        ResponsePublicModel responsePublicModel = new ResponsePublicModel();
        EntryModel entryModel = null;
        List<Object> fieldMap = new ArrayList<Object>();
        if (jsonString == null) {
            return null;
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject insertCommunicationObj = jsonObject
                    .getJSONObject("InsertCommunicationResponse");
            responsePublicModel.setResultCode(insertCommunicationObj.getInt("resultCode"));
            responsePublicModel.setResultDescription(insertCommunicationObj
                    .optString("resultDescription"));
            if (responsePublicModel.getResultCode() != 0) {
                responsePublicModel.eventManagement.setErrorCode(insertCommunicationObj
                        .optString("errorCode"));
                responsePublicModel.eventManagement.setErrorDescription(insertCommunicationObj
                        .optString("errorDescription"));
                return responsePublicModel;
            }
            responsePublicModel.setTransactionId(insertCommunicationObj.optString("transactionId"));
            JSONObject fieldMapObj = insertCommunicationObj.getJSONObject("fieldMap");
            JSONArray entryArray = fieldMapObj.getJSONArray("entry");
            for (int i = 0; i < entryArray.length(); i++) {
                entryModel = new EntryModel();
                entryModel.setKey(entryArray.getJSONObject(i).optString("key"));
                entryModel.setValue(entryArray.getJSONObject(i).optString("value"));
                fieldMap.add(entryModel);
            }
            responsePublicModel.setFieldMap(fieldMap);
        } catch (Exception e) {
            LogManager.e("ParseInsertCommunicationResponse: " + e.getLocalizedMessage());
        }

        return responsePublicModel;
    }
}
